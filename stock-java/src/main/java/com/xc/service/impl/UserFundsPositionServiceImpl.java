package com.xc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xc.common.ServerResponse;
import com.xc.dao.UserCashDetailMapper;
import com.xc.dao.UserFundsPositionMapper;
import com.xc.dao.UserMapper;
import com.xc.pojo.*;
import com.xc.service.*;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.KeyUtils;
import com.xc.utils.stock.BuyAndSellUtils;
import com.xc.utils.stock.GeneratePosition;
import com.xc.utils.stock.sina.SinaStockApi;
import com.xc.vo.position.PositionProfitVO;
import com.xc.vo.position.UserPositionVO;
import com.xc.vo.stock.StockListVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * εεδΊ€ζ
 * @author lr
 * @date 2020/07/24
 */
@Service("IUserFundsPositionService")
public class UserFundsPositionServiceImpl implements IUserFundsPositionService {
    private static final Logger log = LoggerFactory.getLogger(UserPositionServiceImpl.class);

    @Resource
    private UserFundsPositionMapper userFundsPositionMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserCashDetailMapper userCashDetailMapper;

    @Autowired
    ISiteProductService iSiteProductService;
    @Autowired
    IUserService iUserService;
    @Autowired
    ISiteSettingService iSiteSettingService;
    @Autowired
    IStockService iStockService;
    @Autowired
    ISiteSpreadService iSiteSpreadService;
    @Autowired
    IAgentAgencyFeeService iAgentAgencyFeeService;


    @Override
    public ServerResponse insert(UserFundsPosition model, HttpServletRequest request) {
        int ret = 0;
        if (model == null) {
            return ServerResponse.createByErrorMsg("δΈε?εΌεΈΈοΌθ«η¨εεθ―οΌ");
        }
        ret = userFundsPositionMapper.insert(model);
        if(ret>0){
            return ServerResponse.createBySuccessMsg("δΈε?ζεοΌ");
        } else {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ«η¨εεθ―οΌ");
        }
    }

    @Override
    public int update(UserFundsPosition model) {
        int ret = userFundsPositionMapper.update(model);
        return ret>0 ? ret: 0;
    }

    /**
     * εεδΊ€ζ-δΏε­
     */
    @Override
    public ServerResponse save(UserFundsPosition model) {
        int ret = 0;
        if(model!=null && model.getId()>0){
            ret = userFundsPositionMapper.update(model);
        } else{
            ret = userFundsPositionMapper.insert(model);
        }
        if(ret>0){
            return ServerResponse.createBySuccessMsg("ζδ½ζε");
        }
        return ServerResponse.createByErrorMsg("ζδ½ε€±ζ");
    }

    /*εεδΊ€ζ-ζ₯θ―’εθ‘¨*/
    @Override
    public ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, HttpServletRequest request){
        PageHelper.startPage(pageNum, pageSize);
        List<UserFundsPosition> listData = this.userFundsPositionMapper.pageList(pageNum, pageSize, keyword);
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*εεδΊ€ζ-ζ₯θ―’θ―¦ζ*/
    @Override
    public ServerResponse getDetail(int id) {
        return ServerResponse.createBySuccess(this.userFundsPositionMapper.load(id));
    }

    /**
     * εεδΊ€ζ-ε₯ε
     */
    @Transactional
    public ServerResponse buyFunds(Integer stockId, Integer buyNum, Integer buyType, Integer lever, Integer subaccountNumber, HttpServletRequest request)  throws Exception {
        // ε€ζ­ι±ζ«δΈθ½δΉ°
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        /*ε―¦εθͺθ­εΌε³εΌε―*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        User user = this.iUserService.getCurrentRefreshUser(request);
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ«εε―¦εθͺθ­");
        }
        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("ι±ζ«ζη―εζ₯δΈθ½δΊ€ζοΌ");
        }
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("η¨ζΆ {} δΈε?οΌθ‘η₯¨id = {} οΌζ°ι = {} , ζΉε = {} , ζ ζ = {}", new Object[]{user
                .getId(), stockId, buyNum, buyType, lever});
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌεΈ³ζΆε·²θ’«ιε?");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("δΈε?εΊιοΌη½η«θ¨­η½?θ‘¨δΈε­ε¨");
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌη³»η»θ¨­η½?ι―θͺ€");
        }

        String am_begin = siteSetting.getTransAmBegin();
        String am_end = siteSetting.getTransAmEnd();
        String pm_begin = siteSetting.getTransPmBegin();
        String pm_end = siteSetting.getTransPmEnd();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        log.info("ζ―ε¦ε¨δΈεδΊ€ζζι΄ = {} ζ―ε¦ε¨δΈεδΊ€ζζι΄ = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));

        if (!am_flag && !pm_flag) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌδΈε¨δΊ€ζζζ?΅ε");
        }

        Stock stock = null;
        ServerResponse stock_res = this.iStockService.findStockById(stockId);
        if (!stock_res.isSuccess()) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ‘η₯¨δ»£η ι―θͺ€");
        }
        stock = (Stock) stock_res.getData();

        if (stock.getIsLock().intValue() != 0) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌηΆεθ‘η₯¨δΈθ½δΊ€ζ");
        }

        /*List dbPosition = findPositionByStockCodeAndTimes(siteSetting.getBuySameTimes().intValue(), stock
                .getStockCode(), user.getId());
        if (dbPosition.size() >= siteSetting.getBuySameNums().intValue()) {
            return ServerResponse.createByErrorMsg("ι’ηΉδΊ€ζ," + siteSetting.getBuySameTimes() + "ειεεδΈθ‘η₯¨ζεδΈεΎθΆι" + siteSetting
                    .getBuySameNums() + "ζ’");
        }

        Integer transNum = findPositionNumByTimes(siteSetting.getBuyNumTimes().intValue(), user.getId());
        if (transNum.intValue() / 100 >= siteSetting.getBuyNumLots().intValue()) {
            return ServerResponse.createByErrorMsg("ι’ηΉδΊ€ζ," + siteSetting
                    .getBuyNumTimes() + "ειεδΈθ½θΆι" + siteSetting.getBuyNumLots() + "ζ");
        }*/

        if (buyNum.intValue() < siteSetting.getBuyMinNum().intValue()) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ³Όθ²·ζ°ιε°ζΌ" + siteSetting
                    .getBuyMinNum() + "θ‘");
        }
        if (buyNum.intValue() > siteSetting.getBuyMaxNum().intValue()) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ³Όθ²·ζ°ιε€§ζΌ" + siteSetting
                    .getBuyMaxNum() + "θ‘");
        }


        StockListVO stockListVO = SinaStockApi.assembleStockListVO(SinaStockApi.getSinaStock(stock.getStockGid()));
        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());

        if (now_price.compareTo(new BigDecimal("0")) == 0) {
            return ServerResponse.createByErrorMsg("ζ₯δ»·0οΌθ«η¨εεθ―");
        }


        double stock_crease = stockListVO.getHcrate().doubleValue();


        BigDecimal maxRisePercent = new BigDecimal("0");
        if (stock.getStockPlate() != null) {

            maxRisePercent = new BigDecimal("0.2");
            log.info("γη§εθ‘η₯¨γ");
        } else {
            maxRisePercent = new BigDecimal("0.1");
            log.info("γζ?ιAθ‘γ");
        }
        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());

        BigDecimal ztPrice = zsPrice.multiply(maxRisePercent).add(zsPrice);
        ztPrice = ztPrice.setScale(2, 4);
        BigDecimal chaPrice = ztPrice.subtract(zsPrice);

        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);

        log.info("ηΆεζΆ¨θ·εΉ = {} % , ζΆ¨εεΉεΊ¦ = {} %", Double.valueOf(stock_crease), ztRate);
        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && buyType
                .intValue() == 0) {
            return ServerResponse.createByErrorMsg("ηΆεθ‘η₯¨ε·²ζΆ¨εδΈθ½ηζΆ¨");
        }


        if (stock.getStockPlate() == null) {

            int maxcrease = siteSetting.getCreaseMaxPercent().intValue();
            if (stock_crease > 0.0D &&
                    stock_crease >= maxcrease) {
                return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ‘η₯¨ηΆεζΆ¨εΉ:" + stock_crease + ",ε€§ζΌζε€§ζΆ¨εΉ:" + maxcrease);
            }


            if (stock_crease < 0.0D &&
                    Math.abs(stock_crease) > maxcrease) {
                return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ‘η₯¨ηΆεθ·εΉ:" + stock_crease + ",ε€§ζΌζε€§θ·εΉ:" + maxcrease);

            }

        } else {

            int maxcrease = siteSetting.getKcCreaseMaxPercent().intValue();
            if (stock_crease > 0.0D &&
                    stock_crease >= maxcrease) {
                return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌη§εθ‘ηΆεζΆ¨εΉ:" + stock_crease + ",ε€§ζΌζε€§ζΆ¨εΉ:" + maxcrease);
            }


            if (stock_crease < 0.0D &&
                    Math.abs(stock_crease) > maxcrease) {
                return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌη§εθ‘ηΆεθ·εΉ:" + stock_crease + ",ε€§ζΌζε€§θ·εΉ:" + maxcrease);
            }
        }


        ServerResponse serverResponse = this.iStockService.selectRateByDaysAndStockCode(stock
                .getStockCode(), siteSetting.getStockDays().intValue());
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        BigDecimal daysRate = (BigDecimal) serverResponse.getData();
        log.info("θ‘η₯¨ {} οΌ {} ε€©ε ζΆ¨εΉ {} , θ¨­η½?ηζΆ¨εΉ = {}", new Object[]{stock.getStockCode(), siteSetting
                .getStockDays(), daysRate, siteSetting.getStockRate()});

        if (daysRate != null &&
                siteSetting.getStockRate().compareTo(daysRate) == -1) {
            return serverResponse.createByErrorMsg(siteSetting.getStockDays() + "ε€©εζΆ¨εΉθΆι " + siteSetting
                    .getStockRate() + "δΈθ½δΊ€ζ");
        }


        BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue())).setScale(2, 4);


        BigDecimal buy_amt_autual = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue()), 2, 4);


        int compareInt = buy_amt_autual.compareTo(new BigDecimal(siteSetting.getBuyMinAmt().intValue()));
        if (compareInt == -1) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθ³Όθ²·ιι’ε°ζΌ" + siteSetting
                    .getBuyMinAmt() + "ε");
        }


        BigDecimal max_buy_amt = user_enable_amt.multiply(siteSetting.getBuyMaxAmtPercent());
        int compareCwInt = buy_amt_autual.compareTo(max_buy_amt);
        if (compareCwInt == 1) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌδΈθ½θΆιε―η¨θ³ιη" + siteSetting
                    .getBuyMaxAmtPercent().multiply(new BigDecimal("100")) + "%");
        }


        int compareUserAmtInt = user_enable_amt.compareTo(buy_amt_autual);
        log.info("η¨ζΆε―η¨ιι’ = {}  ε?ιθ³Όθ²·ιι’ =  {}", user_enable_amt, buy_amt_autual);
        log.info("ζ―θΎ η¨ζΆιι’ ε ε?ι θ³Όθ²·ιι’ =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌθθ³ε―η¨ιι’ε°ζΌ" + buy_amt_autual + "ε");
        }

        if (user.getUserIndexAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("ε€±ζοΌζζ°ζ»θ³ιε°ζΌ0");
        }
        if (user.getUserFutAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("ε€±ζοΌζθ΄§ζ»θ³ιε°ζΌ0");
        }

        UserFundsPosition userPosition = new UserFundsPosition();
        userPosition.setPositionType(user.getAccountType());
        userPosition.setPositionSn(KeyUtils.getUniqueKey());
        userPosition.setUserId(user.getId());
        userPosition.setNickName(user.getRealName());
        userPosition.setAgentId(user.getAgentId());
        userPosition.setStockId(stock.getId());
        userPosition.setStockCode(stock.getStockCode());
        userPosition.setStockName(stock.getStockName());
        userPosition.setStockGid(stock.getStockGid());
        userPosition.setStockSpell(stock.getStockSpell());
        userPosition.setBuyOrderId(GeneratePosition.getPositionId());
        userPosition.setBuyOrderTime(new Date());
        userPosition.setBuyOrderPrice(now_price);
        userPosition.setOrderDirection((buyType.intValue() == 0) ? "ηζΆ¨" : "ηθ·");
        userPosition.setOrderNum(buyNum);
        userPosition.setSubaccountNumber(subaccountNumber);

        if (stock.getStockPlate() != null) {
            userPosition.setStockPlate(stock.getStockPlate());
        }
        userPosition.setIsLock(Integer.valueOf(0));
        userPosition.setOrderLever(lever);
        userPosition.setOrderTotalPrice(buy_amt);
        BigDecimal buy_fee_amt = buy_amt.multiply(siteSetting.getBuyFee()).setScale(2, 4);
        log.info("η¨ζΆθ³Όθ²·ζη»­θ΄ΉοΌιθ³εζ»θ³ι * ηΎεζ―οΌ = {}", buy_fee_amt);
        userPosition.setOrderFee(buy_fee_amt);


        BigDecimal buy_yhs_amt = buy_amt.multiply(siteSetting.getDutyFee()).setScale(2, 4);
        log.info("η¨ζΆθ³Όθ²·ε°θ±η¨οΌιθ³εζ»θ³ι * ηΎεζ―οΌ = {}", buy_yhs_amt);
        userPosition.setOrderSpread(buy_yhs_amt);

        SiteSpread siteSpread = iSiteSpreadService.findSpreadRateOne(new BigDecimal(stock_crease), buy_amt, stock.getStockCode(), now_price);
        BigDecimal spread_rate_amt = buy_amt.multiply(siteSpread.getSpreadRate()).setScale(2, 4);
        log.info("η¨ζΆθ³Όθ²·ηΉε·?θ΄ΉοΌιθ³εζ»θ³ι * ηΎεζ―{}οΌ = {}", siteSpread.getSpreadRate(), spread_rate_amt);
        userPosition.setSpreadRatePrice(spread_rate_amt);


        BigDecimal profit_and_lose = new BigDecimal("0");
        userPosition.setProfitAndLose(profit_and_lose);


        BigDecimal all_profit_and_lose = profit_and_lose.subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
        userPosition.setAllProfitAndLose(all_profit_and_lose);


        userPosition.setOrderStayDays(Integer.valueOf(0));
        userPosition.setOrderStayFee(new BigDecimal("0"));

        int insertPositionCount = 0;
        this.userFundsPositionMapper.insert(userPosition);
        insertPositionCount = userPosition.getId();
        if (insertPositionCount > 0) {
            //δΏ?ζΉη¨ζΆε―η¨δ½ι’= ηΆεδ½ι’-δΈε?ιι’-δΉ°ε₯ζη»­θ΄Ή-ε°θ±η¨-ηΉε·?θ΄Ή
            BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual).subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
            user.setEnableAmt(reckon_enable);
            /*int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateUserCount > 0) {
                log.info("γη¨ζΆδΊ€ζδΈε?γδΏ?ζΉη¨ζΆιι’ζε");
            } else {
                log.error("η¨ζΆδΊ€ζδΈε?γδΏ?ζΉη¨ζΆιι’εΊι");
                throw new Exception("η¨ζΆδΊ€ζδΈε?γδΏ?ζΉη¨ζΆιι’εΊι");
            }*/
            //ζ Έη?δ»£ηζΆε₯-ε₯εζη»­θ΄Ή
            //iAgentAgencyFeeService.AgencyFeeIncome(1,userPosition.getPositionSn());
            log.info("γη¨ζΆδΊ€ζδΈε?γδΏε­ζεθ¨ιζε");
        } else {
            log.error("η¨ζΆδΊ€ζδΈε?γδΏε­ζεθ¨ιεΊι");
            throw new Exception("η¨ζΆδΊ€ζδΈε?γδΏε­ζεθ¨ιεΊι");
        }

        return ServerResponse.createBySuccess("δΈε?ζε");
    }

    /*
    * εεδΊ€ζ-η¨ζΆεΉ³εζδ½
    * */
    @Transactional
    public ServerResponse sellFunds(String positionSn, int doType) throws Exception {
        log.info("γη¨ζΆδΊ€ζεΉ³εγ positionSn = {} οΌ dotype = {}", positionSn, Integer.valueOf(doType));

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("εΉ³εεΊιοΌη½η«θ¨­η½?θ‘¨δΈε­ε¨");
            return ServerResponse.createByErrorMsg("δΈε?ε€±ζοΌη³»η»θ¨­η½?ι―θͺ€");
        }

        if (doType != 0) {
            String am_begin = siteSetting.getTransAmBegin();
            String am_end = siteSetting.getTransAmEnd();
            String pm_begin = siteSetting.getTransPmBegin();
            String pm_end = siteSetting.getTransPmEnd();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            log.info("ζ―ε¦ε¨δΈεδΊ€ζζι΄ = {} ζ―ε¦ε¨δΈεδΊ€ζζι΄ = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag) {
                return ServerResponse.createByErrorMsg("εΉ³εε€±ζοΌδΈε¨δΊ€ζζζ?΅ε");
            }
        }

        UserFundsPosition userPosition = this.userFundsPositionMapper.findPositionBySn(positionSn);
        if (userPosition == null) {
            return ServerResponse.createByErrorMsg("εΉ³εε€±ζοΌθ¨ε?δΈε­ε¨");
        }

        User user = this.userMapper.selectByPrimaryKey(userPosition.getUserId());
        /*ε―¦εθͺθ­εΌε³εΌε―*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("εΉ³εε€±ζοΌη¨ζΆε·²θ’«ιε?");
        }
        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("ι±ζ«ζη―εζ₯δΈθ½δΊ€ζοΌ");
        }

        if (userPosition.getSellOrderId() != null) {
            return ServerResponse.createByErrorMsg("εΉ³εε€±ζοΌζ­€θ¨ε?ε·²εΉ³ε");
        }

        if (1 == userPosition.getIsLock().intValue()) {
            return ServerResponse.createByErrorMsg("εΉ³εε€±ζ " + userPosition.getLockMsg());
        }

        if (!DateTimeUtil.isCanSell(userPosition.getBuyOrderTime(), siteSetting.getCantSellTimes().intValue())) {
            return ServerResponse.createByErrorMsg(siteSetting.getCantSellTimes() + "ειεδΈθ½εΉ³ε");
        }

        StockListVO stockListVO = SinaStockApi.assembleStockListVO(SinaStockApi.getSinaStock(userPosition.getStockGid()));

        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());
        if (now_price.compareTo(new BigDecimal("0")) != 1) {
            log.error("θ‘η₯¨ = {} ζΆε°ζ₯δ»· = {}", userPosition.getStockName(), now_price);
            return ServerResponse.createByErrorMsg("ζ₯δ»·0οΌεΉ³εε€±ζοΌθ«η¨εεθ―");
        }

        double stock_crease = stockListVO.getHcrate().doubleValue();

        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());

        BigDecimal ztPrice = zsPrice.multiply(new BigDecimal("0.1")).add(zsPrice);
        ztPrice = ztPrice.setScale(2, 4);
        BigDecimal chaPrice = ztPrice.subtract(zsPrice);

        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);

        ztRate = ztRate.negate();
        log.info("θ‘η₯¨ηΆεζΆ¨θ·εΉ = {} θ·εεΉεΊ¦ = {}", Double.valueOf(stock_crease), ztRate);
        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && "ηζΆ¨"
                .equals(userPosition.getOrderDirection())) {
            return ServerResponse.createByErrorMsg("ηΆεθ‘η₯¨ε·²θ·εδΈθ½εεΊ");
        }

        Integer buy_num = userPosition.getOrderNum();

        BigDecimal all_buy_amt = userPosition.getOrderTotalPrice();
        BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue()));

        BigDecimal profitLoss = new BigDecimal("0");
        if ("ηζΆ¨".equals(userPosition.getOrderDirection())) {
            log.info("δΉ°εζΉεοΌ{}", "ζΆ¨");

            profitLoss = all_sell_amt.subtract(all_buy_amt);
        } else {
            log.info("δΉ°εζΉεοΌ{}", "θ·");
            profitLoss = all_buy_amt.subtract(all_sell_amt);
        }
        log.info("δΉ°ε₯ζ»ιι’ = {} , εεΊζ»ιι’ = {} , ηθ§ = {}", new Object[]{all_buy_amt, all_sell_amt, profitLoss});

        BigDecimal user_all_amt = user.getUserAmt();
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("η¨ζΆεζ¬ζ»θ³ι = {} , ε―η¨ = {}", user_all_amt, user_enable_amt);

        BigDecimal buy_fee_amt = userPosition.getOrderFee();
        log.info("δΉ°ε₯ζη»­θ΄Ή = {}", buy_fee_amt);

        BigDecimal orderSpread = userPosition.getOrderSpread();
        log.info("ε°θ±η¨ = {}", orderSpread);

        BigDecimal orderStayFee = userPosition.getOrderStayFee();
        log.info("ηεθ΄Ή = {}", orderStayFee);

        BigDecimal spreadRatePrice = userPosition.getSpreadRatePrice();
        log.info("ηΉε·?θ΄Ή = {}", spreadRatePrice);

        BigDecimal sell_fee_amt = all_sell_amt.multiply(siteSetting.getSellFee()).setScale(2, 4);
        log.info("εεΊζη»­θ΄Ή = {}", sell_fee_amt);

        BigDecimal all_fee_amt = buy_fee_amt.add(sell_fee_amt).add(orderSpread).add(orderStayFee).add(spreadRatePrice);
        log.info("ζ»ηζη»­θ΄Ήθ΄Ήη¨ = {}", all_fee_amt);

        userPosition.setSellOrderId(GeneratePosition.getPositionId());
        userPosition.setSellOrderPrice(now_price);
        userPosition.setSellOrderTime(new Date());

        BigDecimal order_fee_all = buy_fee_amt.add(sell_fee_amt);
        userPosition.setOrderFee(order_fee_all);

        userPosition.setProfitAndLose(profitLoss);

        BigDecimal all_profit = profitLoss.subtract(all_fee_amt);
        userPosition.setAllProfitAndLose(all_profit);

        int updatePositionCount = this.userFundsPositionMapper.update(userPosition);
        if (updatePositionCount > 0) {
            log.info("γη¨ζΆεΉ³εγδΏ?ζΉζ΅?ε¨ηθ§θ¨ιζε");
        } else {
            log.error("η¨ζΆεΉ³εγδΏ?ζΉζ΅?ε¨ηθ§θ¨ιεΊι");
            throw new Exception("η¨ζΆεΉ³εγδΏ?ζΉζ΅?ε¨ηθ§θ¨ιεΊι");
        }

        BigDecimal freez_amt = all_buy_amt.divide(new BigDecimal(userPosition.getOrderLever().intValue()), 2, 4);

        BigDecimal reckon_all = user_all_amt.add(all_profit);
        BigDecimal reckon_enable = user_enable_amt.add(all_profit).add(freez_amt);

        log.info("η¨ζΆεΉ³εεηζ»θ³ι  = {} , ε―η¨θ³ι = {}", reckon_all, reckon_enable);
        user.setUserAmt(reckon_all);
        user.setEnableAmt(reckon_enable);
        /*int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("γη¨ζΆεΉ³εγδΏ?ζΉη¨ζΆιι’ζε");
        } else {
            log.error("η¨ζΆεΉ³εγδΏ?ζΉη¨ζΆιι’εΊι");
            throw new Exception("η¨ζΆεΉ³εγδΏ?ζΉη¨ζΆιι’εΊι");
        }*/

        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("ιθ³ηΈ½ηθ§");
        ucd.setDeAmt(all_profit);
        ucd.setDeSummary("θ³£εΊθ‘η₯¨οΌ" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",δ½η¨ζ¬ιοΌ" + freez_amt + ",ηΈ½ζηΊθ²»οΌ" + all_fee_amt + ",ηεθ΄ΉοΌ" + orderStayFee+ ",ε°θ±η¨οΌ" + orderSpread + ",ηΉε·?θ΄ΉοΌ" + spreadRatePrice + ",ηθ§οΌ" + profitLoss + "οΌηΈ½ηθ§οΌ" + all_profit);

        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            /*//ζ Έη?δ»£ηζΆε₯-εΉ³εζη»­θ΄Ή
            iAgentAgencyFeeService.AgencyFeeIncome(2,userPosition.getPositionSn());
            //ζ Έη?δ»£ηζΆε₯-εηΊ’
            iAgentAgencyFeeService.AgencyFeeIncome(4,userPosition.getPositionSn());*/
            log.info("γη¨ζΆεΉ³εγδΏε­ζη»θ¨ιζε");
        } else {
            log.error("η¨ζΆεΉ³εγδΏε­ζη»θ¨ιεΊι");
            throw new Exception("η¨ζΆεΉ³εγδΏε­ζη»θ¨ιεΊι");
        }

        return ServerResponse.createBySuccessMsg("εΉ³εζεοΌ");
    }


    /*
     * εεδΊ€ζ-ζ₯θ―’ζζεΉ³ε/ζεδΏ‘ζ―
     * */
    public ServerResponse findMyPositionByCodeAndSpell(String stockCode, String stockSpell, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        User user = this.iUserService.getCurrentUser(request);

        PageHelper.startPage(pageNum, pageSize);


        List<UserFundsPosition> userPositions = this.userFundsPositionMapper.findMyPositionByCodeAndSpell(user.getId(), stockCode, stockSpell, state);

        List<UserPositionVO> userPositionVOS = Lists.newArrayList();
        if (userPositions.size() > 0) {
            for (UserFundsPosition position : userPositions) {

                UserPositionVO userPositionVO = assembleUserPositionVO(position);
                userPositionVOS.add(userPositionVO);
            }
        }

        PageInfo pageInfo = new PageInfo(userPositions);
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /*ζ Ήζ?εειθ³δ»£η ζ₯θ―’η¨ζΆζζ©ε₯εθ‘η₯¨*/
    public ServerResponse findUserFundsPositionByCode(HttpServletRequest request, String fundsCode) {
        User user = this.iUserService.getCurrentRefreshUser(request);
        UserFundsPosition position = this.userFundsPositionMapper.findUserFundsPositionByCode(user.getId(), fundsCode);

        List<UserPositionVO> userPositionVOS = Lists.newArrayList();
        UserPositionVO userPositionVO = null;
        if(position != null){
            userPositionVO = assembleUserPositionVO(position);
        }
        userPositionVOS.add(userPositionVO);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private UserPositionVO assembleUserPositionVO(UserFundsPosition position) {
        UserPositionVO userPositionVO = new UserPositionVO();

        userPositionVO.setId(position.getId());
        userPositionVO.setPositionType(position.getPositionType());
        userPositionVO.setPositionSn(position.getPositionSn());
        userPositionVO.setUserId(position.getUserId());
        userPositionVO.setNickName(position.getNickName());
        userPositionVO.setAgentId(position.getAgentId());
        userPositionVO.setStockName(position.getStockName());
        userPositionVO.setStockCode(position.getStockCode());
        userPositionVO.setStockGid(position.getStockGid());
        userPositionVO.setStockSpell(position.getStockSpell());
        userPositionVO.setBuyOrderId(position.getBuyOrderId());
        userPositionVO.setBuyOrderTime(position.getBuyOrderTime());
        userPositionVO.setBuyOrderPrice(position.getBuyOrderPrice());
        userPositionVO.setSellOrderId(position.getSellOrderId());
        userPositionVO.setSellOrderTime(position.getSellOrderTime());
        userPositionVO.setSellOrderPrice(position.getSellOrderPrice());
        /*userPositionVO.setProfitTargetPrice(position.getProfitTargetPrice());
        userPositionVO.setStopTargetPrice(position.getStopTargetPrice());*/
        userPositionVO.setOrderDirection(position.getOrderDirection());
        userPositionVO.setOrderNum(position.getOrderNum());
        userPositionVO.setOrderLever(position.getOrderLever());
        userPositionVO.setOrderTotalPrice(position.getOrderTotalPrice());
        userPositionVO.setOrderFee(position.getOrderFee());
        userPositionVO.setOrderSpread(position.getOrderSpread());
        userPositionVO.setOrderStayFee(position.getOrderStayFee());
        userPositionVO.setOrderStayDays(position.getOrderStayDays());

        userPositionVO.setStockPlate(position.getStockPlate());
        userPositionVO.setSpreadRatePrice(position.getSpreadRatePrice());

        PositionProfitVO positionProfitVO = getPositionProfitVO(position);
        userPositionVO.setProfitAndLose(positionProfitVO.getProfitAndLose());
        userPositionVO.setAllProfitAndLose(positionProfitVO.getAllProfitAndLose());
        userPositionVO.setNow_price(positionProfitVO.getNowPrice());


        return userPositionVO;
    }

    private PositionProfitVO getPositionProfitVO(UserFundsPosition position) {
        BigDecimal profitAndLose = new BigDecimal("0");
        BigDecimal allProfitAndLose = new BigDecimal("0");
        String nowPrice = "";

        if (position.getSellOrderId() != null) {

            BigDecimal subPrice = position.getSellOrderPrice().subtract(position.getBuyOrderPrice());
            profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue()));
            if ("ηθ·".equals(position.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }


            allProfitAndLose = profitAndLose.subtract(position.getOrderFee()).subtract(position.getOrderSpread()).subtract(position.getOrderStayFee()).subtract(position.getSpreadRatePrice());
        } else {

            StockListVO stockListVO = SinaStockApi.assembleStockListVO(
                    SinaStockApi.getSinaStock(position.getStockGid()));
            nowPrice = stockListVO.getNowPrice();


            BigDecimal subPrice = (new BigDecimal(nowPrice)).subtract(position.getBuyOrderPrice());
            profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue()));
            if ("ηθ·".equals(position.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            //ηΈ½ηθ§= ζ΅?ε¨ηθ§ β ζη»­θ΄Ή β ε°θ±η¨ β ηεθ΄Ή β ηΉε·?θ΄Ή
            allProfitAndLose = profitAndLose.subtract(position.getOrderFee()).subtract(position.getOrderSpread()).subtract(position.getOrderStayFee()).subtract(position.getSpreadRatePrice());
        }
        PositionProfitVO positionProfitVO = new PositionProfitVO();
        positionProfitVO.setProfitAndLose(profitAndLose);
        positionProfitVO.setAllProfitAndLose(allProfitAndLose);
        positionProfitVO.setNowPrice(nowPrice);

        return positionProfitVO;
    }


}
