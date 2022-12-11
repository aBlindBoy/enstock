package com.xc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xc.dao.*;
import com.xc.pojo.*;
import com.xc.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xc.common.ServerResponse;
import com.xc.service.*;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.HolidayUtil;
import com.xc.utils.KeyUtils;
import com.xc.utils.stock.BuyAndSellUtils;
import com.xc.utils.stock.GeneratePosition;
import com.xc.utils.stock.GetStayDays;
import com.xc.utils.stock.UsStockApi;
import com.xc.utils.stock.sina.SinaStockApi;
import com.xc.vo.agent.AgentIncomeVO;
import com.xc.vo.position.AdminPositionVO;
import com.xc.vo.position.AgentPositionVO;
import com.xc.vo.position.PositionProfitVO;
import com.xc.vo.position.PositionVO;
import com.xc.vo.position.UserPositionVO;
import com.xc.vo.stock.StockListVO;
import com.xc.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service("iUserPositionService")
public class UserPositionServiceImpl implements IUserPositionService {

    private static final Logger log = LoggerFactory.getLogger(UserPositionServiceImpl.class);

    @Autowired
    UserPositionMapper userPositionMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    ISiteSettingService iSiteSettingService;

    @Autowired
    ISiteSpreadService iSiteSpreadService;

    @Autowired
    IStockService iStockService;

//    @Autowired
//    ITwStockService iTwStockService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCashDetailMapper userCashDetailMapper;
    @Autowired
    IAgentUserService iAgentUserService;
    @Autowired
    AgentUserMapper agentUserMapper;
    @Autowired
    SiteTaskLogMapper siteTaskLogMapper;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    AgentAgencyFeeMapper agentAgencyFeeMapper;
    @Autowired
    IAgentAgencyFeeService iAgentAgencyFeeService;
    @Autowired
    ISiteProductService iSiteProductService;

    @Autowired
    FundsApplyMapper fundsApplyMapper;

   /* @Transactional
    public ServerResponse buy(Integer stockId, Integer buyNum, Integer buyType, Integer lever, HttpServletRequest request) throws Exception {

        // 判断週末不能买
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        *//*int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }
        if (weekday == 7) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }*//*

        *//*實名認證开关开启*//*
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        User user = this.iUserService.getCurrentRefreshUser(request);
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("Failed to place an order, please verify your real name first");
        }
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("用戶 {} 下單，股票id = {} ，数量 = {} , 方向 = {} , 杠杆 = {}", new Object[]{user
                .getId(), stockId, buyNum, buyType, lever});
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Order failed, account has been locked");
        }
        if (siteProduct.getHolidayDisplay()) {
            return ServerResponse.createByErrorMsg("No trading on weekends or holidays");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("下單出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place order, system setting error");
        }

        String am_begin = siteSetting.getTransAmBegin();
        String am_end = siteSetting.getTransAmEnd();
        String pm_begin = siteSetting.getTransPmBegin();
        String pm_end = siteSetting.getTransPmEnd();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));

        if (!am_flag && !pm_flag) {
            return ServerResponse.createByErrorMsg("Failed to place order, not during trading hours");
        }

        Stock stock = null;
        ServerResponse stock_res = this.iStockService.findStockById(stockId);
        if (!stock_res.isSuccess()) {
            return ServerResponse.createByErrorMsg("Failed to place order, wrong stock symbol");
        }
        stock = (Stock) stock_res.getData();

        if (stock.getIsLock().intValue() != 0) {
            return ServerResponse.createByErrorMsg("The order failed, the current stock cannot be traded");
        }

        List dbPosition = findPositionByStockCodeAndTimes(siteSetting.getBuySameTimes().intValue(), stock
                .getStockCode(), user.getId());
        if (dbPosition.size() >= siteSetting.getBuySameNums().intValue()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteSetting.getBuySameTimes() + "The same stock position cannot exceed more than one minute" + siteSetting
                    .getBuySameNums() + "strip");
        }

        Integer transNum = findPositionNumByTimes(siteSetting.getBuyNumTimes().intValue(), user.getId());
        if (transNum.intValue() / 100 >= siteSetting.getBuyNumLots().intValue()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteSetting
                    .getBuyNumTimes() + "no more than a minute" + siteSetting.getBuyNumLots());
        }

        if (buyNum.intValue() < siteSetting.getBuyMinNum().intValue()) {
            return ServerResponse.createByErrorMsg("The order failed, the purchase quantity is less than" + siteSetting
                    .getBuyMinNum() + "share");
        }
        if (buyNum.intValue() > siteSetting.getBuyMaxNum().intValue()) {
            return ServerResponse.createByErrorMsg("The order failed, the purchase quantity is greater than" + siteSetting
                    .getBuyMaxNum() + "share");
        }


        StockListVO stockListVO = SinaStockApi.assembleStockListVO(SinaStockApi.getSinaStock(stock.getStockGid()));
        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());

        if (now_price.compareTo(new BigDecimal("0")) == 0) {
            return ServerResponse.createByErrorMsg("Quote 0, please try again later");
        }


        double stock_crease = stockListVO.getHcrate().doubleValue();


        BigDecimal maxRisePercent = new BigDecimal("0");
        if (stock.getStockPlate() != null) {
            maxRisePercent = new BigDecimal("0.2");
            log.info("【科创股票】");
        } else {
            maxRisePercent = new BigDecimal("0.1");
            log.info("【普通A股】");
        }

        if (stockListVO.getName().startsWith("ST") || stockListVO.getName().endsWith("退")) {
            return ServerResponse.createByErrorMsg("ST和已退市的股票不能入倉");
        }

        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());

        BigDecimal ztPrice = zsPrice.multiply(maxRisePercent).add(zsPrice);
        ztPrice = ztPrice.setScale(2, 4);
        BigDecimal chaPrice = ztPrice.subtract(zsPrice);

        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);

        log.info("當前涨跌幅 = {} % , 涨停幅度 = {} %", Double.valueOf(stock_crease), ztRate);
        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && buyType
                .intValue() == 0) {
            return ServerResponse.createByErrorMsg("當前股票已涨停不能看涨");
        }


        if (stock.getStockPlate() == null || StringUtils.isEmpty(stock.getStockPlate())) {

            int maxcrease = siteSetting.getCreaseMaxPercent().intValue();
            if (stock_crease > 0.0D &&
                    stock_crease >= maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，股票當前涨幅:" + stock_crease + ",大於最大涨幅:" + maxcrease);
            }


            if (stock_crease < 0.0D &&
                    -stock_crease > maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，股票當前跌幅:" + stock_crease + ",大於最大跌幅:" + maxcrease);

            }

        } else if ("创业".equals(stock.getStockPlate())) {

            int maxcrease = siteSetting.getCyCreaseMaxPercent().intValue();
            if (stock_crease > 0.0D &&
                    stock_crease >= maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，创业股當前涨幅:" + stock_crease + ",大於最大涨幅:" + maxcrease);
            }


            if (stock_crease < 0.0D &&
                    -stock_crease > maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，创业股當前跌幅:" + stock_crease + ",大於最大跌幅:" + maxcrease);
            }
        } else {

            int maxcrease = siteSetting.getKcCreaseMaxPercent().intValue();
            if (stock_crease > 0.0D &&
                    stock_crease >= maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，科创股當前涨幅:" + stock_crease + ",大於最大涨幅:" + maxcrease);
            }


            if (stock_crease < 0.0D &&
                    -stock_crease > maxcrease) {
                return ServerResponse.createByErrorMsg("下單失敗，科创股當前跌幅:" + stock_crease + ",大於最大跌幅:" + maxcrease);
            }
        }


        ServerResponse serverResponse = this.iStockService.selectRateByDaysAndStockCode(stock
                .getStockCode(), siteSetting.getStockDays().intValue());
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        BigDecimal daysRate = (BigDecimal) serverResponse.getData();
        log.info("股票 {} ， {} 天内 涨幅 {} , 設置的涨幅 = {}", new Object[]{stock.getStockCode(), siteSetting
                .getStockDays(), daysRate, siteSetting.getStockRate()});

        if (daysRate != null &&
                siteSetting.getStockRate().compareTo(daysRate) == -1) {
            return serverResponse.createByErrorMsg(siteSetting.getStockDays() + "天内涨幅超過 " + siteSetting
                    .getStockRate() + "不能交易");
        }


        //BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue())).setScale(2, 4);
        BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue()));


        //BigDecimal buy_amt_autual = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue()), 2, 4);
        BigDecimal buy_amt_autual = buy_amt.divide(new BigDecimal(lever.intValue()), 2, 4);


        int compareInt = buy_amt_autual.compareTo(new BigDecimal(siteSetting.getBuyMinAmt().intValue()));
        if (compareInt == -1) {
            return ServerResponse.createByErrorMsg("下單失敗，購買金额小於" + siteSetting
                    .getBuyMinAmt() + "元");
        }


        BigDecimal max_buy_amt = user_enable_amt.multiply(siteSetting.getBuyMaxAmtPercent());
        int compareCwInt = buy_amt_autual.compareTo(max_buy_amt);
        if (compareCwInt == 1) {
            return ServerResponse.createByErrorMsg("下單失敗，不能超過可用資金的" + siteSetting
                    .getBuyMaxAmtPercent().multiply(new BigDecimal("100")) + "%");
        }


        int compareUserAmtInt = user_enable_amt.compareTo(buy_amt_autual);
        log.info("用戶可用金额 = {}  实际購買金额 =  {}", user_enable_amt, buy_amt_autual);
        log.info("比较 用戶金额 和 实际 購買金额 =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("下單失敗，融資可用金额小於" + buy_amt_autual + "元");
        }

        if (user.getUserIndexAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("失敗，指数总資金小於0");
        }
        if (user.getUserFutAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("失敗，期货总資金小於0");
        }

        UserPosition userPosition = new UserPosition();
        userPosition.setPositionType(user.getAccountType());
        userPosition.setPositionSn(KeyUtils.getUniqueKey());
        userPosition.setUserId(user.getId());
        userPosition.setNickName(user.getRealName());
        userPosition.setAgentId(user.getAgentId());
        userPosition.setStockCode(stock.getStockCode());
        userPosition.setStockName(stock.getStockName());
        userPosition.setStockGid(stock.getStockGid());
        userPosition.setStockSpell(stock.getStockSpell());
        userPosition.setBuyOrderId(GeneratePosition.getPositionId());
        userPosition.setBuyOrderTime(new Date());
        userPosition.setBuyOrderPrice(now_price);
        userPosition.setOrderDirection((buyType.intValue() == 0) ? "看涨" : "看跌");

        userPosition.setOrderNum(buyNum);


        if (stock.getStockPlate() != null) {
            userPosition.setStockPlate(stock.getStockPlate());
        }


        userPosition.setIsLock(Integer.valueOf(0));


        userPosition.setOrderLever(lever);


        userPosition.setOrderTotalPrice(buy_amt);

        //遞延費特殊处理
        BigDecimal stayFee = userPosition.getOrderTotalPrice().multiply(siteSetting.getStayFee());
        BigDecimal allStayFee = stayFee.multiply(new BigDecimal(1));
        userPosition.setOrderStayFee(allStayFee);
        userPosition.setOrderStayDays(1);


        BigDecimal buy_fee_amt = buy_amt.multiply(siteSetting.getBuyFee()).setScale(2, 4);
        log.info("用戶購買手续费（配資后总資金 * 百分比） = {}", buy_fee_amt);
        userPosition.setOrderFee(buy_fee_amt);


        BigDecimal buy_yhs_amt = buy_amt.multiply(siteSetting.getDutyFee()).setScale(2, 4);
        log.info("用戶購買印花税（配資后总資金 * 百分比） = {}", buy_yhs_amt);
        userPosition.setOrderSpread(buy_yhs_amt);

        SiteSpread siteSpread = iSiteSpreadService.findSpreadRateOne(new BigDecimal(stock_crease), buy_amt, stock.getStockCode(), now_price);
        BigDecimal spread_rate_amt = new BigDecimal("0");
        if (siteSpread != null) {
            spread_rate_amt = buy_amt.multiply(siteSpread.getSpreadRate()).setScale(2, 4);
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", siteSpread.getSpreadRate(), spread_rate_amt);
        } else {
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", "設置异常", spread_rate_amt);
        }

        userPosition.setSpreadRatePrice(spread_rate_amt);


        BigDecimal profit_and_lose = new BigDecimal("0");
        userPosition.setProfitAndLose(profit_and_lose);


        BigDecimal all_profit_and_lose = profit_and_lose.subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
        userPosition.setAllProfitAndLose(all_profit_and_lose);


        userPosition.setOrderStayDays(Integer.valueOf(0));
        userPosition.setOrderStayFee(new BigDecimal("0"));

        int insertPositionCount = 0;
        this.userPositionMapper.insert(userPosition);
        insertPositionCount = userPosition.getId();
        if (insertPositionCount > 0) {
            //修改用戶可用余额= 當前余额-下單金额-买入手续费-印花税-点差费
            //BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual).subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
            //修改用戶可用余额= 當前余额-下單总金额
            BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual);
            user.setEnableAmt(reckon_enable);
            int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateUserCount > 0) {
                log.info("【用戶交易下單】修改用戶金额成功");
            } else {
                log.error("用戶交易下單】修改用戶金额出错");
                throw new Exception("用戶交易下單】修改用戶金额出错");
            }
            //核算代理收入-入倉手续费
            iAgentAgencyFeeService.AgencyFeeIncome(1, userPosition.getPositionSn());
            log.info("【用戶交易下單】保存持倉記錄成功");
        } else {
            log.error("用戶交易下單】保存持倉記錄出错");
            throw new Exception("用戶交易下單】保存持倉記錄出错");
        }

        return ServerResponse.createBySuccess("下單成功");
    }*/

    @Transactional
    public ServerResponse buyTwStock(Integer stockId, Integer buyNum,
                                     Integer buyType, Integer lever,
                                     String nowPrice, String hcrate,
                                     String preClose, HttpServletRequest request) throws Exception {

        // 判断週末不能买
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        /*int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }
        if (weekday == 7) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }*/

        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        User user = this.iUserService.getCurrentRefreshUser(request);
        if (user==null){
            return ServerResponse.createByErrorMsg("請先登錄");
        }
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("下單失敗，請先實名認證");
        }
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("用戶 {} 下單，股票id = {} ，数量 = {} , 方向 = {} , 杠杆 = {}", new Object[]{user
                .getId(), stockId, buyNum, buyType, lever});
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("下單失敗，帳戶已被锁定");
        }
        if (siteProduct.getHolidayDisplay()) {
            return ServerResponse.createByErrorMsg("週末或節假日不能交易！");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("下單出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("下單失敗，系统設置錯誤");
        }

        String am_begin = siteSetting.getTransAmBegin();
        String am_end = siteSetting.getTransAmEnd();
        String pm_begin = siteSetting.getTransPmBegin();
        String pm_end = siteSetting.getTransPmEnd();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));

        if (!am_flag && !pm_flag) {
            return ServerResponse.createByErrorMsg("下單失敗，不在交易時段内");
        }

//        TwStock stock = null;
        Stock stock = this.stockMapper.findStockByCode(stockId.toString());
//        ServerResponse stock_res = this.stockMapper.findStockByCode(stockId.toString());
//        if (!stock_res.isSuccess()) {
//            return ServerResponse.createByErrorMsg("下單失敗，股票代码錯誤");
//        }
//        stock = (TwStock) stock_res.getData();


        List dbPosition = findPositionByStockCodeAndTimes(siteSetting.getBuySameTimes(), stock.getStockCode(), user.getId());
        if (dbPosition.size() >= siteSetting.getBuySameNums()) {
            return ServerResponse.createByErrorMsg("频繁交易," + siteSetting.getBuySameTimes() + "分鐘内同一股票持倉不得超過" + siteSetting
                    .getBuySameNums() + "條");
        }

        Integer transNum = findPositionNumByTimes(siteSetting.getBuyNumTimes(), user.getId());
        if (transNum / 100 >= siteSetting.getBuyNumLots()) {
            return ServerResponse.createByErrorMsg("频繁交易," + siteSetting
                    .getBuyNumTimes() + "分鐘内不能超過" + siteSetting.getBuyNumLots() + "手");
        }

        if (buyNum < siteSetting.getBuyMinNum()) {
            return ServerResponse.createByErrorMsg("下單失敗，購買数量小於" + siteSetting
                    .getBuyMinNum() + "股");
        }
        if (buyNum.intValue() > siteSetting.getBuyMaxNum().intValue()) {
            return ServerResponse.createByErrorMsg("下單失敗，購買数量大於" + siteSetting
                    .getBuyMaxNum() + "股");
        }



        BigDecimal now_price = new BigDecimal(nowPrice);

        if (now_price.compareTo(new BigDecimal("0")) == 0) {
            return ServerResponse.createByErrorMsg("报价0，請稍后再试");
        }


        double stock_crease = new BigDecimal(hcrate).doubleValue();

        if ( siteSetting.getStockRate().compareTo((new BigDecimal(String.valueOf(stock_crease)))) == -1) {
            return ServerResponse.createByErrorMsg(siteSetting.getStockDays() + "天内涨幅超過 " + siteSetting
                    .getStockRate() + "不能交易");
        }


        //BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue())).setScale(2, 4);
        BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue()));


        //BigDecimal buy_amt_autual = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue()), 2, 4);
        BigDecimal buy_amt_autual = buy_amt.divide(new BigDecimal(lever.intValue()), 2, 4);


        int compareInt = buy_amt_autual.compareTo(new BigDecimal(siteSetting.getBuyMinAmt().intValue()));
        if (compareInt == -1) {
            return ServerResponse.createByErrorMsg("下單失敗，購買金额小於" + siteSetting
                    .getBuyMinAmt() + "元");
        }


        BigDecimal max_buy_amt = user_enable_amt.multiply(siteSetting.getBuyMaxAmtPercent());
        int compareCwInt = buy_amt_autual.compareTo(max_buy_amt);
        if (compareCwInt == 1) {
            return ServerResponse.createByErrorMsg("下單失敗，不能超過可用資金的" + siteSetting
                    .getBuyMaxAmtPercent().multiply(new BigDecimal("100")) + "%");
        }


        int compareUserAmtInt = user_enable_amt.compareTo(buy_amt_autual);
        log.info("用戶可用金额 = {}  实际購買金额 =  {}", user_enable_amt, buy_amt_autual);
        log.info("比较 用戶金额 和 实际 購買金额 =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("下單失敗，融資可用金额小於" + buy_amt_autual + "元");
        }

        if (user.getUserIndexAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("失敗，指数总資金小於0");
        }
        if (user.getUserFutAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("失敗，期货总資金小於0");
        }

        UserPosition userPosition = new UserPosition();
        userPosition.setPositionType(user.getAccountType());
        userPosition.setPositionSn(KeyUtils.getUniqueKey());
        userPosition.setUserId(user.getId());
        userPosition.setNickName(user.getRealName());
        userPosition.setAgentId(user.getAgentId());
        userPosition.setStockCode(stock.getStockCode());
        userPosition.setStockName(stock.getStockName());
        //userPosition.setStockGid(stock.getStockGid());
        //userPosition.setStockSpell(stock.getStockSpell());
        userPosition.setBuyOrderId(GeneratePosition.getPositionId());
        userPosition.setBuyOrderTime(new Date());
        userPosition.setBuyOrderPrice(now_price);
        userPosition.setOrderDirection((buyType.intValue() == 0) ? "看涨" : "看跌");

        userPosition.setOrderNum(buyNum);
        userPosition.setOrderLever(lever);
        userPosition.setOrderTotalPrice(buy_amt);

        //遞延費特殊处理
        BigDecimal stayFee = userPosition.getOrderTotalPrice().multiply(siteSetting.getStayFee());
        BigDecimal allStayFee = stayFee.multiply(new BigDecimal(1));
        userPosition.setOrderStayFee(allStayFee);
        userPosition.setOrderStayDays(1);
        userPosition.setMarginAdd(BigDecimal.ZERO);

        BigDecimal buy_fee_amt = buy_amt.multiply(siteSetting.getBuyFee()).setScale(2, 4);
        log.info("用戶購買手续费（配資后总資金 * 百分比） = {}", buy_fee_amt);
        userPosition.setOrderFee(buy_fee_amt);


        BigDecimal buy_yhs_amt = buy_amt.multiply(siteSetting.getDutyFee()).setScale(2, 4);
        log.info("用戶購買印花税（配資后总資金 * 百分比） = {}", buy_yhs_amt);
        userPosition.setOrderSpread(buy_yhs_amt);

        SiteSpread siteSpread = iSiteSpreadService.findSpreadRateOne(new BigDecimal(stock_crease), buy_amt, stock.getStockCode(), now_price);
        BigDecimal spread_rate_amt = new BigDecimal("0");
        if (siteSpread != null) {
            spread_rate_amt = buy_amt.multiply(siteSpread.getSpreadRate()).setScale(2, 4);
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", siteSpread.getSpreadRate(), spread_rate_amt);
        } else {
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", "設置异常", spread_rate_amt);
        }

        userPosition.setSpreadRatePrice(spread_rate_amt);


        BigDecimal profit_and_lose = new BigDecimal("0");
        userPosition.setProfitAndLose(profit_and_lose);


        BigDecimal all_profit_and_lose = profit_and_lose.subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
        userPosition.setAllProfitAndLose(all_profit_and_lose);


        userPosition.setOrderStayDays(Integer.valueOf(0));
        userPosition.setOrderStayFee(new BigDecimal("0"));

        int insertPositionCount = 0;
        this.userPositionMapper.insert(userPosition);
        insertPositionCount = userPosition.getId();
        if (insertPositionCount > 0) {
            //修改用戶可用余额= 當前余额-下單金额-买入手续费-印花税-点差费
            //BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual).subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
            //修改用戶可用余额= 當前余额-下單总金额
            BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual);
            user.setEnableAmt(reckon_enable);
            int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateUserCount > 0) {
                log.info("【用戶交易下單】修改用戶金额成功");
            } else {
                log.error("用戶交易下單】修改用戶金额出错");
                throw new Exception("用戶交易下單】修改用戶金额出错");
            }
            //核算代理收入-入倉手续费
            iAgentAgencyFeeService.AgencyFeeIncome(1, userPosition.getPositionSn());
            log.info("【用戶交易下單】保存持倉記錄成功");
        } else {
            log.error("用戶交易下單】保存持倉記錄出错");
            throw new Exception("用戶交易下單】保存持倉記錄出错");
        }

        return ServerResponse.createBySuccess("下單成功");
    }

    @Override
    public ServerResponse sellTwStock(String paramString, int paramInt) throws Exception {
        return null;
    }

   /* public ServerResponse sell(String positionSn, int doType) throws Exception {
        log.info("【用戶交易平倉】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("平倉出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("下單失敗，系统設置錯誤");
        }
        SiteProduct siteProduct = iSiteProductService.getProductSetting();

        if (doType != 0) {
            String am_begin = siteSetting.getTransAmBegin();
            String am_end = siteSetting.getTransAmEnd();
            String pm_begin = siteSetting.getTransPmBegin();
            String pm_end = siteSetting.getTransPmEnd();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag) {
                return ServerResponse.createByErrorMsg("平倉失敗，不在交易時段内");
            }

            if (siteProduct.getHolidayDisplay()) {
                return ServerResponse.createByErrorMsg("週末或節假日不能交易！");
            }

        }

        UserPosition userPosition = this.userPositionMapper.findPositionBySn(positionSn);
        if (userPosition == null) {
            return ServerResponse.createByErrorMsg("平倉失敗，訂單不存在");
        }

        User user = this.userMapper.selectByPrimaryKey(userPosition.getUserId());
        *//*實名認證开关开启*//*

        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("平倉失敗，用戶已被锁定");
        }


        if (userPosition.getSellOrderId() != null) {
            return ServerResponse.createByErrorMsg("平倉失敗，此訂單已平倉");
        }

        if (1 == userPosition.getIsLock().intValue()) {
            return ServerResponse.createByErrorMsg("平倉失敗 " + userPosition.getLockMsg());
        }

        if (!DateTimeUtil.isCanSell(userPosition.getBuyOrderTime(), siteSetting.getCantSellTimes().intValue())) {
            return ServerResponse.createByErrorMsg(siteSetting.getCantSellTimes() + "分鐘内不能平倉");
        }

//        if (DateTimeUtil.sameDate(DateTimeUtil.getCurrentDate(),userPosition.getBuyOrderTime())) {
//            return ServerResponse.createByErrorMsg("当天入倉的股票需要隔天才能出倉");
//        }


        StockListVO stockListVO = SinaStockApi.assembleStockListVO(SinaStockApi.getSinaStock(userPosition.getStockGid()));

        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());
        if (now_price.compareTo(new BigDecimal("0")) != 1) {
            log.error("股票 = {} 收到报价 = {}", userPosition.getStockName(), now_price);
            return ServerResponse.createByErrorMsg("报价0，平倉失敗，請稍后再试");
        }

        double stock_crease = stockListVO.getHcrate().doubleValue();

        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());

        BigDecimal ztPrice = zsPrice.multiply(new BigDecimal("0.1")).add(zsPrice);
        ztPrice = ztPrice.setScale(2, 4);
        BigDecimal chaPrice = ztPrice.subtract(zsPrice);

        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);

        ztRate = ztRate.negate();
        log.info("股票當前涨跌幅 = {} 跌停幅度 = {}", Double.valueOf(stock_crease), ztRate);
        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && "看涨"
                .equals(userPosition.getOrderDirection())) {
            return ServerResponse.createByErrorMsg("當前股票已跌停不能卖出");
        }

        Integer buy_num = userPosition.getOrderNum();

        BigDecimal all_buy_amt = userPosition.getOrderTotalPrice();
        //BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue())).divide(new BigDecimal(userPosition.getOrderLever())).setScale(2,4);
        BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue()));

        BigDecimal profitLoss = new BigDecimal("0");
        if ("看涨".equals(userPosition.getOrderDirection())) {
            log.info("买卖方向：{}", "涨");
            profitLoss = all_sell_amt.subtract(all_buy_amt);
        } else {
            log.info("买卖方向：{}", "跌");
            profitLoss = all_buy_amt.subtract(all_sell_amt);
        }
        log.info("买入总金额 = {} , 卖出总金额 = {} , 盈虧 = {}", new Object[]{all_buy_amt, all_sell_amt, profitLoss});

        BigDecimal user_all_amt = user.getUserAmt();
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("用戶原本总資金 = {} , 可用 = {}", user_all_amt, user_enable_amt);

        BigDecimal buy_fee_amt = userPosition.getOrderFee();
        log.info("买入手续费 = {}", buy_fee_amt);

        BigDecimal orderSpread = userPosition.getOrderSpread();
        log.info("印花税 = {}", orderSpread);

        BigDecimal orderStayFee = userPosition.getOrderStayFee();
        log.info("遞延費 = {}", orderStayFee);

        BigDecimal spreadRatePrice = userPosition.getSpreadRatePrice();
        log.info("点差费 = {}", spreadRatePrice);

        BigDecimal sell_fee_amt = all_sell_amt.multiply(siteSetting.getSellFee()).setScale(2, 4);
        log.info("卖出手续费 = {}", sell_fee_amt);

        //總手續費= 买入手续费+卖出手续费+印花税+遞延費+点差费
        BigDecimal all_fee_amt = buy_fee_amt.add(sell_fee_amt).add(orderSpread).add(orderStayFee).add(spreadRatePrice);
        log.info("总的手续费费用 = {}", all_fee_amt);

        userPosition.setSellOrderId(GeneratePosition.getPositionId());
        userPosition.setSellOrderPrice(now_price);
        userPosition.setSellOrderTime(new Date());

        //修改orderFee
        BigDecimal order_fee_all = buy_fee_amt.add(sell_fee_amt);
        //order_fee最后修改為平倉手续费
        userPosition.setOrderFee(sell_fee_amt);

        userPosition.setProfitAndLose(profitLoss);

        BigDecimal all_profit = profitLoss.subtract(all_fee_amt);
        userPosition.setAllProfitAndLose(all_profit);

        int updatePositionCount = this.userPositionMapper.updateByPrimaryKeySelective(userPosition);
        if (updatePositionCount > 0) {
            log.info("【用戶平倉】修改浮动盈虧記錄成功");
        } else {
            log.error("用戶平倉】修改浮动盈虧記錄出错");
            throw new Exception("用戶平倉】修改浮动盈虧記錄出错");
        }

        BigDecimal freez_amt = all_buy_amt.divide(new BigDecimal(userPosition.getOrderLever().intValue()), 2, 4);
        //BigDecimal freez_amt = all_buy_amt;

        BigDecimal reckon_all = user_all_amt.add(all_profit);
        //修改用戶可用余额=當前可用余额+總盈虧+买入总金额+追加保证金
        BigDecimal reckon_enable = user_enable_amt.add(all_profit).add(freez_amt).add(userPosition.getMarginAdd());

        log.info("用戶平倉后的总資金  = {} , 可用資金 = {}", reckon_all, reckon_enable);
        user.setUserAmt(reckon_all);
        user.setEnableAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶平倉】修改用戶金额成功");
        } else {
            log.error("用戶平倉】修改用戶金额出错");
            throw new Exception("用戶平倉】修改用戶金额出错");
        }

        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("總盈虧");
        ucd.setDeAmt(all_profit);
//        ucd.setDeSummary("賣出股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",佔用本金：" + freez_amt + ",總手續費：" + all_fee_amt + ",建倉費：" + buy_fee_amt + ",遞延費：" + orderStayFee + ",印花税：" + orderSpread + ",盈虧：" + profitLoss + "，總盈虧：" + all_profit);
        ucd.setDeSummary("賣出股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",佔用本金：" + freez_amt + ",總手續費：" + all_fee_amt + ",建倉費：" + buy_fee_amt + ",遞延費：" + orderStayFee + ",盈虧：" + profitLoss + "，總盈虧：" + all_profit);
        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            //核算代理收入-平倉手续费
            iAgentAgencyFeeService.AgencyFeeIncome(2, userPosition.getPositionSn());
            //核算代理收入-分红
            iAgentAgencyFeeService.AgencyFeeIncome(4, userPosition.getPositionSn());
            log.info("【用戶平倉】保存明细記錄成功");
        } else {
            log.error("用戶平倉】保存明细記錄出错");
            throw new Exception("用戶平倉】保存明细記錄出错");
        }

        return ServerResponse.createBySuccessMsg("平倉成功！");
    }*/

//    @Transactional
//    public ServerResponse sellTwStock(String positionSn, int doType) throws Exception {
//        log.info("【用戶交易平倉】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));
//
//        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
//        if (siteSetting == null) {
//            log.error("平倉出错，网站設置表不存在");
//            return ServerResponse.createByErrorMsg("下單失敗，系统設置錯誤");
//        }
//        SiteProduct siteProduct = iSiteProductService.getProductSetting();
//
//        if (doType != 0) {
//            String am_begin = siteSetting.getTransAmBegin();
//            String am_end = siteSetting.getTransAmEnd();
//            String pm_begin = siteSetting.getTransPmBegin();
//            String pm_end = siteSetting.getTransPmEnd();
//            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
//            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
//            log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
//            if (!am_flag && !pm_flag) {
//                return ServerResponse.createByErrorMsg("平倉失敗，不在交易時段内");
//            }
//
//            if (siteProduct.getHolidayDisplay()) {
//                return ServerResponse.createByErrorMsg("週末或節假日不能交易！");
//            }
//
//        }
//
//        UserPosition userPosition = this.userPositionMapper.findPositionBySn(positionSn);
//        if (userPosition == null) {
//            return ServerResponse.createByErrorMsg("平倉失敗，訂單不存在");
//        }
//
//        User user = this.userMapper.selectByPrimaryKey(userPosition.getUserId());
//        /*實名認證开关开启*/
//
//        if (siteProduct.getRealNameDisplay() && user.getIsLock() == 1) {
//            return ServerResponse.createByErrorMsg("平倉失敗，用戶已被锁定");
//        }
//
//
//        if (userPosition.getSellOrderId() != null) {
//            return ServerResponse.createByErrorMsg("平倉失敗，此訂單已平倉");
//        }
//
////        if (1 == userPosition.getIsLock().intValue()) {
//////            return ServerResponse.createByErrorMsg("平倉失敗 " + userPosition.getLockMsg());
//////        }
//
//        if (!DateTimeUtil.isCanSell(userPosition.getBuyOrderTime(), siteSetting.getCantSellTimes())) {
//            return ServerResponse.createByErrorMsg(siteSetting.getCantSellTimes() + "分鐘内不能平倉");
//        }
//
////        if (DateTimeUtil.sameDate(DateTimeUtil.getCurrentDate(),userPosition.getBuyOrderTime())) {
////            return ServerResponse.createByErrorMsg("当天入倉的股票需要隔天才能出倉");
////        }
//
//        String resultStr=UsStockApi.getTwStock(userPosition.getStockCode());
//        StockListVO stockListVO = UsStockApi.assembleStockListVO(resultStr);
//
//        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());
//        if (now_price.compareTo(new BigDecimal("0")) != 1) {
//            log.error("股票 = {} 收到报价 = {}", userPosition.getStockName(), now_price);
//            return ServerResponse.createByErrorMsg("报价0，平倉失敗，請稍后再试");
//        }
//
//        double stock_crease = stockListVO.getHcrate().doubleValue();
//
//        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());
//
//        BigDecimal ztPrice = zsPrice.multiply(new BigDecimal("0.1")).add(zsPrice);
//        ztPrice = ztPrice.setScale(2, 4);
//        BigDecimal chaPrice = ztPrice.subtract(zsPrice);
//
//        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);
//
//        ztRate = ztRate.negate();
//        log.info("股票當前涨跌幅 = {} 跌停幅度 = {}", stock_crease, ztRate);
//        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && "看涨"
//                .equals(userPosition.getOrderDirection())) {
//            return ServerResponse.createByErrorMsg("當前股票已跌停不能卖出");
//        }
//
//        Integer buy_num = userPosition.getOrderNum();
//
//        BigDecimal all_buy_amt = userPosition.getOrderTotalPrice();
//        //BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue())).divide(new BigDecimal(userPosition.getOrderLever())).setScale(2,4);
//        BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue()));
//
//        BigDecimal profitLoss = new BigDecimal("0");
//        if ("看涨".equals(userPosition.getOrderDirection())) {
//            log.info("买卖方向：{}", "涨");
//            profitLoss = all_sell_amt.subtract(all_buy_amt);
//        } else {
//            log.info("买卖方向：{}", "跌");
//            profitLoss = all_buy_amt.subtract(all_sell_amt);
//        }
//        log.info("买入总金额 = {} , 卖出总金额 = {} , 盈虧 = {}", new Object[]{all_buy_amt, all_sell_amt, profitLoss});
//
//        BigDecimal user_all_amt = user.getUserAmt();
//        BigDecimal user_enable_amt = user.getEnableAmt();
//        log.info("用戶原本总資金 = {} , 可用 = {}", user_all_amt, user_enable_amt);
//
//        BigDecimal buy_fee_amt = userPosition.getOrderFee();
//        log.info("买入手续费 = {}", buy_fee_amt);
//
//        BigDecimal orderSpread = userPosition.getOrderSpread();
//        log.info("印花税 = {}", orderSpread);
//
//        BigDecimal orderStayFee = userPosition.getOrderStayFee();
//        log.info("遞延費 = {}", orderStayFee);
//
//        BigDecimal spreadRatePrice = userPosition.getSpreadRatePrice();
//        log.info("点差费 = {}", spreadRatePrice);
//
//        BigDecimal sell_fee_amt = all_sell_amt.multiply(siteSetting.getSellFee()).setScale(2, 4);
//        log.info("卖出手续费 = {}", sell_fee_amt);
//
//        //總手續費= 买入手续费+卖出手续费+印花税+遞延費+点差费
//        BigDecimal all_fee_amt = buy_fee_amt.add(sell_fee_amt).add(orderSpread).add(orderStayFee).add(spreadRatePrice);
//        log.info("总的手续费费用 = {}", all_fee_amt);
//
//        userPosition.setSellOrderId(GeneratePosition.getPositionId());
//        userPosition.setSellOrderPrice(now_price);
//        userPosition.setSellOrderTime(new Date());
//
//        //修改orderFee
//        BigDecimal order_fee_all = buy_fee_amt.add(sell_fee_amt);
//        //order_fee最后修改為平倉手续费
//        userPosition.setOrderFee(sell_fee_amt);
//
//        userPosition.setProfitAndLose(profitLoss);
//
//        BigDecimal all_profit = profitLoss.subtract(all_fee_amt);
//        userPosition.setAllProfitAndLose(all_profit);
//
//        int updatePositionCount = this.userPositionMapper.updateByPrimaryKeySelective(userPosition);
//        if (updatePositionCount > 0) {
//            log.info("【用戶平倉】修改浮动盈虧記錄成功");
//        } else {
//            log.error("用戶平倉】修改浮动盈虧記錄出错");
//            throw new Exception("用戶平倉】修改浮动盈虧記錄出错");
//        }
//
//        BigDecimal freez_amt = all_buy_amt.divide(new BigDecimal(userPosition.getOrderLever().intValue()), 2, 4);
//        //BigDecimal freez_amt = all_buy_amt;
//
//        BigDecimal reckon_all = user_all_amt.add(all_profit);
//        //修改用戶可用余额=當前可用余额+總盈虧+买入总金额+追加保证金
//        BigDecimal reckon_enable = user_enable_amt.add(all_profit);//.add(freez_amt).add(userPosition.getMarginAdd()).add(userPosition.getOrderTotalPrice());
//        reckon_enable = reckon_enable.add(freez_amt);
//        reckon_enable = reckon_enable.add(userPosition.getMarginAdd());
//       // reckon_enable = reckon_enable.add(userPosition.getOrderTotalPrice().divide(BigDecimal.valueOf(userPosition.getOrderLever())));
//        log.info("用戶平倉后的总資金  = {} , 可用資金 = {}", reckon_all, reckon_enable);
//        user.setUserAmt(reckon_all);
//        user.setEnableAmt(reckon_enable);
//        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
//        if (updateUserCount > 0) {
//            log.info("【用戶平倉】修改用戶金额成功");
//        } else {
//            log.error("用戶平倉】修改用戶金额出错");
//            throw new Exception("用戶平倉】修改用戶金额出错");
//        }
//
//        UserCashDetail ucd = new UserCashDetail();
//        ucd.setPositionId(userPosition.getId());
//        ucd.setAgentId(user.getAgentId());
//        ucd.setAgentName(user.getAgentName());
//        ucd.setUserId(user.getId());
//        ucd.setUserName(user.getRealName());
//        ucd.setDeType("總盈虧");
//        ucd.setDeAmt(all_profit);
////        ucd.setDeSummary("賣出股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",佔用本金：" + freez_amt + ",總手續費：" + all_fee_amt + ",建倉費：" + buy_fee_amt + ",遞延費：" + orderStayFee + ",印花税：" + orderSpread + ",盈虧：" + profitLoss + "，總盈虧：" + all_profit);
//        ucd.setDeSummary("賣出股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",佔用本金：" + freez_amt + ",總手續費：" + all_fee_amt + ",建倉費：" + buy_fee_amt + ",遞延費：" + orderStayFee + ",盈虧：" + profitLoss + "，總盈虧：" + all_profit);
//        ucd.setAddTime(new Date());
//        ucd.setIsRead(Integer.valueOf(0));
//
//        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
//        if (insertSxfCount > 0) {
//            //核算代理收入-平倉手续费
//            iAgentAgencyFeeService.AgencyFeeIncome(2, userPosition.getPositionSn());
//            //核算代理收入-分红
//            iAgentAgencyFeeService.AgencyFeeIncome(4, userPosition.getPositionSn());
//            log.info("【用戶平倉】保存明细記錄成功");
//        } else {
//            log.error("用戶平倉】保存明细記錄出错");
//            throw new Exception("用戶平倉】保存明细記錄出错");
//        }
//
//        return ServerResponse.createBySuccessMsg("平倉成功！");
//    }

    //用戶追加保证金操作
    public ServerResponse addmargin(String positionSn, int doType, BigDecimal marginAdd) throws Exception {
        log.info("【用戶追加保证金】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("追加保证金出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("追加失敗，系统設置錯誤");
        }

        if (doType != 0) {
            /*String am_begin = siteSetting.getTransAmBegin();
            String am_end = siteSetting.getTransAmEnd();
            String pm_begin = siteSetting.getTransPmBegin();
            String pm_end = siteSetting.getTransPmEnd();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag) {
                return ServerResponse.createByErrorMsg("追加失敗，不在交易時段内");
            }*/
        }

        UserPosition userPosition = this.userPositionMapper.findPositionBySn(positionSn);
        if (userPosition == null) {
            return ServerResponse.createByErrorMsg("追加失敗，訂單不存在");
        }

        User user = this.userMapper.selectByPrimaryKey(userPosition.getUserId());
        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        if (!siteProduct.getStockMarginDisplay()) {
            return ServerResponse.createByErrorMsg("不允许追加，請联系管理员");
        }

        if (siteProduct.getHolidayDisplay()) {
            return ServerResponse.createByErrorMsg("週末或節假日不能交易！");
        }

        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("追加失敗，用戶已被锁定");
        }

        if (1 == userPosition.getIsLock().intValue()) {
            return ServerResponse.createByErrorMsg("追加失敗 " + userPosition.getLockMsg());
        }

        BigDecimal user_all_amt = user.getUserAmt();
        BigDecimal user_enable_amt = user.getEnableAmt();
        int compareUserAmtInt = user_enable_amt.compareTo(marginAdd);
        log.info("用戶可用金额 = {}  追加金额 =  {}", user_enable_amt, marginAdd);
        log.info("比较 用戶金额 和 实际 購買金额 =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("追加失敗，融資可用金额小於" + marginAdd + "元");
        }


        userPosition.setMarginAdd(userPosition.getMarginAdd().add(marginAdd));

        int updatePositionCount = this.userPositionMapper.updateByPrimaryKeySelective(userPosition);
        if (updatePositionCount > 0) {
            log.info("【用戶追加保证金】追加保证金成功");
        } else {
            log.error("用戶追加保证金】追加保证金出错");
            throw new Exception("用戶追加保证金】追加保证金出错");
        }

        //修改用戶可用余额=當前可用余额-追加金额
        BigDecimal reckon_enable = user_enable_amt.subtract(marginAdd);

        log.info("用戶追加保证金后的总資金  = {} , 可用資金 = {}", user_all_amt, reckon_enable);
        user.setEnableAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶平倉】修改用戶金额成功");
        } else {
            log.error("用戶平倉】修改用戶金额出错");
            throw new Exception("用戶平倉】修改用戶金额出错");
        }

        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("追加保证金");
        ucd.setDeAmt(marginAdd.multiply(new BigDecimal("-1")));
        ucd.setDeSummary("追加股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",追加金额：" + marginAdd);

        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            log.info("【用戶平倉】保存明细記錄成功");
        } else {
            log.error("用戶平倉】保存明细記錄出错");
            throw new Exception("用戶平倉】保存明细記錄出错");
        }

        return ServerResponse.createBySuccessMsg("追加成功！");
    }

//    @Override
//    public PositionProfitVO getPositionProfitVO(UserPosition position) {
//        return null;
//    }


    public ServerResponse lock(Integer positionId, Integer state, String lockMsg) {
        if (positionId == null || state == null) {
            return ServerResponse.createByErrorMsg("參數不能為空");
        }

        UserPosition position = this.userPositionMapper.selectByPrimaryKey(positionId);
        if (position == null) {
            return ServerResponse.createByErrorMsg("持倉不存在");
        }

        if (position.getSellOrderId() != null) {
            return ServerResponse.createByErrorMsg("平倉單不能锁倉");
        }

        if (state.intValue() == 1 &&
                StringUtils.isBlank(lockMsg)) {
            return ServerResponse.createByErrorMsg("锁倉提示信息必填");
        }


        if (state.intValue() == 1) {
            position.setIsLock(Integer.valueOf(1));
            position.setLockMsg(lockMsg);
        } else {
            position.setIsLock(Integer.valueOf(0));
        }

        int updateCount = this.userPositionMapper.updateByPrimaryKeySelective(position);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }

    public ServerResponse del(Integer positionId) {
        if (positionId == null) {
            return ServerResponse.createByErrorMsg("id不能為空");
        }
        UserPosition position = this.userPositionMapper.selectByPrimaryKey(positionId);
        if (position == null) {
            ServerResponse.createByErrorMsg("该持倉不存在");
        }
        /*if (position.getSellOrderId() == null) {
            return ServerResponse.createByErrorMsg("持倉單不能刪除！");
        }*/
        int updateCount = this.userPositionMapper.deleteByPrimaryKey(positionId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("刪除成功");
        }
        return ServerResponse.createByErrorMsg("刪除失敗");
    }

    public ServerResponse findMyPositionByCodeAndSpell(String stockCode, String stockSpell, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        User user = this.iUserService.getCurrentUser(request);

        PageHelper.startPage(pageNum, pageSize);


        List<UserPosition> userPositions = this.userPositionMapper.findMyPositionByCodeAndSpell(user.getId(), stockCode, stockSpell, state);

        List<UserPositionVO> userPositionVOS = Lists.newArrayList();
        if (userPositions.size() > 0) {
            for (UserPosition position : userPositions) {
                UserPositionVO userPositionVO = assembleUserPositionVO(position);
                userPositionVOS.add(userPositionVO);
            }
        }

        PageInfo pageInfo = new PageInfo(userPositions);
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    //updated edison
    public PositionVO findUserPositionAllProfitAndLose(Integer userId) {
        //List<UserPosition> userPositions = this.userPositionMapper.findPositionByUserIdAndSellIdIsNull(userId);

        //TODO 计算帳戶金额
        BigDecimal allProfitAndLose = new BigDecimal("0");
        BigDecimal allFreezAmt = new BigDecimal("0");
        /*String queryString = "";
        for (UserPosition position : userPositions) {
            queryString += position.getStockGid() + ",";
        }
        String[] querys = null;
        if(StringUtils.isNotBlank(queryString)){
            querys= SinaStockApi.getSinaStockList(queryString);
        }
        for (int i = 0; i < userPositions.size(); i++) {
            //單條股票的sina api数据
            String sinaData = querys[i].substring(querys[i].indexOf("\"") + 1, querys[i].lastIndexOf("\""));
            StockListVO stockListVO = SinaStockApi.assembleStockListVO(sinaData);

            BigDecimal nowPrice = new BigDecimal(stockListVO.getNowPrice());


            if (nowPrice.compareTo(new BigDecimal("0")) != 0) {

                BigDecimal buyPrice = userPositions.get(i).getBuyOrderPrice();
                BigDecimal subPrice = nowPrice.subtract(buyPrice);

                BigDecimal profit_and_lose = subPrice.multiply(new BigDecimal(userPositions.get(i).getOrderNum().intValue()));
                //BigDecimal profit_and_lose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue())).divide(new BigDecimal(position.getOrderLever())).setScale(2,4);
                if ("看跌".equals(userPositions.get(i).getOrderDirection())) {
                    profit_and_lose = profit_and_lose.negate();
                }


                BigDecimal total_fee = userPositions.get(i).getOrderFee().add(userPositions.get(i).getOrderSpread()).
                        add(userPositions.get(i).getOrderStayFee());


                BigDecimal position_profit = profit_and_lose.subtract(total_fee);


                allProfitAndLose = allProfitAndLose.add(position_profit);


                BigDecimal position_freez = userPositions.get(i).getOrderTotalPrice().
                        divide(new BigDecimal(userPositions.get(i).getOrderLever().intValue()), 2, 4);
                //BigDecimal position_freez = position.getOrderTotalPrice();
                allFreezAmt = allFreezAmt.add(position_freez).add(userPositions.get(i).getMarginAdd());
                continue;
            }
            log.info("查询所有持倉單的總盈虧，現价返回0，當前為集合竞价");
        }

        //加上分倉交易保证金
        List<FundsApply> fundsApplyList = fundsApplyMapper.getUserMarginList(userId);
        for (FundsApply fundsApply : fundsApplyList) {
            allFreezAmt = allFreezAmt.add(fundsApply.getMargin());
        }*/


        PositionVO positionVO = new PositionVO();
        positionVO.setAllProfitAndLose(allProfitAndLose);
        positionVO.setAllFreezAmt(allFreezAmt);
        return positionVO;
    }

    public List<UserPosition> findPositionByUserIdAndSellIdIsNull(Integer userId) {
        return this.userPositionMapper.findPositionByUserIdAndSellIdIsNull(userId);
    }

    public List<UserPosition> findPositionByStockCodeAndTimes(int minuteTimes, String stockCode, Integer userId) {
        Date paramTimes = null;
        paramTimes = DateTimeUtil.parseToDateByMinute(minuteTimes);

        return this.userPositionMapper.findPositionByStockCodeAndTimes(paramTimes, stockCode, userId);
    }

    public Integer findPositionNumByTimes(int minuteTimes, Integer userId) {
        Date beginDate = DateTimeUtil.parseToDateByMinute(minuteTimes);
        Integer transNum = this.userPositionMapper.findPositionNumByTimes(beginDate, userId);
        log.info("用戶 {} 在 {} 分鐘之内 交易手数 {}", new Object[]{userId, Integer.valueOf(minuteTimes), transNum});
        return transNum;
    }

    public ServerResponse listByAgent(Integer positionType, Integer state, Integer userId, Integer agentId, String positionSn, String beginTime, String endTime, HttpServletRequest request, int pageNum, int pageSize) {
        AgentUser currentAgent = this.iAgentUserService.getCurrentAgent(request);


        if (agentId != null) {
            AgentUser agentUser = this.agentUserMapper.selectByPrimaryKey(agentId);
            if (agentUser != null && agentUser.getParentId() != currentAgent.getId()) {
                return ServerResponse.createByErrorMsg("不能查询非下级代理用戶持倉");
            }
        }

        Integer searchId = null;
        if (agentId == null) {
            searchId = currentAgent.getId();
        } else {
            searchId = agentId;
        }


        Timestamp begin_time = null;
        if (StringUtils.isNotBlank(beginTime)) {
            begin_time = DateTimeUtil.searchStrToTimestamp(beginTime);
        }
        Timestamp end_time = null;
        if (StringUtils.isNotBlank(endTime)) {
            end_time = DateTimeUtil.searchStrToTimestamp(endTime);
        }

        PageHelper.startPage(pageNum, pageSize);


        List<UserPosition> userPositions = this.userPositionMapper.listByAgent(positionType, state, userId, searchId, positionSn, begin_time, end_time);

        List<AgentPositionVO> agentPositionVOS = Lists.newArrayList();
        for (UserPosition position : userPositions) {
            AgentPositionVO agentPositionVO = assembleAgentPositionVO(position);
            agentPositionVOS.add(agentPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userPositions);
        pageInfo.setList(agentPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    public ServerResponse getIncome(Integer agentId, Integer positionType, String beginTime, String endTime) {
        if (StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
            return ServerResponse.createByErrorMsg("時间不能為空");
        }

        Timestamp begin_time = null;
        if (StringUtils.isNotBlank(beginTime)) {
            begin_time = DateTimeUtil.searchStrToTimestamp(beginTime);
        }
        Timestamp end_time = null;
        if (StringUtils.isNotBlank(endTime)) {
            end_time = DateTimeUtil.searchStrToTimestamp(endTime);
        }


        List<UserPosition> userPositions = this.userPositionMapper.listByAgent(positionType, Integer.valueOf(1), null, agentId, null, begin_time, end_time);


        BigDecimal order_fee_amt = new BigDecimal("0");
        BigDecimal order_profit_and_lose = new BigDecimal("0");
        BigDecimal order_profit_and_lose_all = new BigDecimal("0");

        for (UserPosition position : userPositions) {

            order_fee_amt = order_fee_amt.add(position.getOrderFee()).add(position.getOrderSpread()).add(position.getOrderStayFee());

            order_profit_and_lose = order_profit_and_lose.add(position.getProfitAndLose());

            order_profit_and_lose_all = order_profit_and_lose_all.add(position.getAllProfitAndLose());
        }

        AgentIncomeVO agentIncomeVO = new AgentIncomeVO();
        agentIncomeVO.setOrderSize(Integer.valueOf(userPositions.size()));
        agentIncomeVO.setOrderFeeAmt(order_fee_amt);
        agentIncomeVO.setOrderProfitAndLose(order_profit_and_lose);
        agentIncomeVO.setOrderAllAmt(order_profit_and_lose_all);

        return ServerResponse.createBySuccess(agentIncomeVO);
    }

    public ServerResponse listByAdmin(Integer agentId, Integer positionType, Integer state, Integer userId, String positionSn, String beginTime, String endTime, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);


        Timestamp begin_time = null;
        if (StringUtils.isNotBlank(beginTime)) {
            begin_time = DateTimeUtil.searchStrToTimestamp(beginTime);
        }
        Timestamp end_time = null;
        if (StringUtils.isNotBlank(endTime)) {
            end_time = DateTimeUtil.searchStrToTimestamp(endTime);
        }


        List<UserPosition> userPositions = this.userPositionMapper.listByAgent(positionType, state, userId, agentId, positionSn, begin_time, end_time);

        List<AdminPositionVO> adminPositionVOS = Lists.newArrayList();
        for (UserPosition position : userPositions) {
            AdminPositionVO adminPositionVO = assembleAdminPositionVO(position);
            adminPositionVOS.add(adminPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userPositions);
        pageInfo.setList(adminPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    public int CountPositionNum(Integer state, Integer accountType) {
        return this.userPositionMapper.CountPositionNum(state, accountType);
    }

    public BigDecimal CountPositionProfitAndLose() {
        return this.userPositionMapper.CountPositionProfitAndLose();
    }

    public BigDecimal CountPositionAllProfitAndLose() {
        return this.userPositionMapper.CountPositionAllProfitAndLose();
    }

    public ServerResponse create(Integer userId, String stockCode, String buyPrice, String buyTime, Integer buyNum, Integer buyType, Integer lever) {
        if (userId == null || StringUtils.isBlank(buyPrice) || StringUtils.isBlank(stockCode) ||
                StringUtils.isBlank(buyTime) || buyNum == null || buyType == null || lever == null) {

            return ServerResponse.createByErrorMsg("參數不能為空");
        }

        User user = this.userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("用戶不存在");
        }
        if (user.getAccountType().intValue() != 1) {
            return ServerResponse.createByErrorMsg("正式用戶不能生成持倉單");
        }

        Stock stock = (Stock) this.iStockService.findStockByCode(stockCode).getData();
        if (stock == null) {
            return ServerResponse.createByErrorMsg("股票不存在");
        }


        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("下單出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("下單失敗，系统設置錯誤");
        }


        BigDecimal user_enable_amt = user.getEnableAmt();

        BigDecimal buy_amt = (new BigDecimal(buyPrice)).multiply(new BigDecimal(buyNum.intValue()));
        BigDecimal buy_amt_autual = buy_amt.divide(new BigDecimal(lever.intValue()), 2, 4);


        int compareUserAmtInt = user_enable_amt.compareTo(buy_amt_autual);
        log.info("用戶可用金额 = {}  实际購買金额 =  {}", user_enable_amt, buy_amt_autual);
        log.info("比较 用戶金额 和 实际 購買金额 =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("下單失敗，用戶可用金额小於" + buy_amt_autual + "元");
        }


        BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual);
        user.setEnableAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶交易下單】修改用戶金额成功");
        } else {
            log.error("用戶交易下單】修改用戶金额出错");
        }


        UserPosition userPosition = new UserPosition();
        userPosition.setPositionType(Integer.valueOf(1));
        userPosition.setPositionSn(KeyUtils.getUniqueKey());
        userPosition.setUserId(user.getId());
        userPosition.setNickName(user.getRealName());
        userPosition.setAgentId(user.getAgentId());
        userPosition.setStockCode(stock.getStockCode());
        userPosition.setStockName(stock.getStockName());
        userPosition.setStockGid(stock.getStockGid());
        userPosition.setStockSpell(stock.getStockSpell());
        userPosition.setBuyOrderId(GeneratePosition.getPositionId());
        userPosition.setBuyOrderTime(DateTimeUtil.strToDate(buyTime));
        userPosition.setBuyOrderPrice(new BigDecimal(buyPrice));
        userPosition.setOrderDirection((buyType.intValue() == 0) ? "看涨" : "看跌");

        userPosition.setOrderNum(buyNum);


        userPosition.setIsLock(Integer.valueOf(0));


        userPosition.setOrderLever(lever);


        userPosition.setOrderTotalPrice(buy_amt);


        BigDecimal buy_fee_amt = buy_amt.multiply(siteSetting.getBuyFee()).setScale(2, 4);
        log.info("创建模拟持倉 手续费（配資后总資金 * 百分比） = {}", buy_fee_amt);
        userPosition.setOrderFee(buy_fee_amt);


        BigDecimal buy_yhs_amt = buy_amt.multiply(siteSetting.getDutyFee()).setScale(2, 4);
        log.info("创建模拟持倉 印花税（配資后总資金 * 百分比） = {}", buy_yhs_amt);
        userPosition.setOrderSpread(buy_yhs_amt);


        BigDecimal profit_and_lose = new BigDecimal("0");
        userPosition.setProfitAndLose(profit_and_lose);


        BigDecimal all_profit_and_lose = profit_and_lose.subtract(buy_fee_amt).subtract(buy_yhs_amt);
        userPosition.setAllProfitAndLose(all_profit_and_lose);


        userPosition.setOrderStayDays(Integer.valueOf(0));
        userPosition.setOrderStayFee(new BigDecimal("0"));
        userPosition.setSpreadRatePrice(new BigDecimal("0"));

        int insertPositionCount = this.userPositionMapper.insert(userPosition);
        if (insertPositionCount > 0) {
            log.info("【创建模拟持倉】保存記錄成功");
        } else {
            log.error("【创建模拟持倉】保存記錄出错");
        }

        return ServerResponse.createBySuccess("生成模拟持倉成功");
    }

    public int deleteByUserId(Integer userId) {
        return this.userPositionMapper.deleteByUserId(userId);
    }

    public void doClosingStayTask() {
        List<UserPosition> userPositions = this.userPositionMapper.findAllStayPosition();


        if (userPositions.size() > 0) {
            log.info("查询到正在持倉的訂單数量 = {}", Integer.valueOf(userPositions.size()));

            SiteProduct siteProduct = iSiteProductService.getProductSetting();

            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
            String httpArg = f.format(new Date());
            String jsonResult = HolidayUtil.request(httpArg);
            //if (!siteProduct.getHolidayDisplay()) {
            //不是節假日
            if(!jsonResult.equals("2")){
                for (UserPosition position : userPositions) {
                    //int stayDays = GetStayDays.getDays(GetStayDays.getBeginDate(position.getBuyOrderTime()));
                    //getWorkDays 排除週末
                    int stayDays = GetStayDays.getWorkDays(GetStayDays.getBeginDate(position.getBuyOrderTime()));
                    //遞延費特殊处理
                    //stayDays = stayDays + 1;

                    log.info("");
                    log.info("开始处理 持倉訂單id = {} 訂單号 = {} 用戶id = {} realName = {} 留倉天数 = {}", new Object[]{position
                            .getId(), position.getPositionSn(), position.getUserId(), position
                            .getNickName(), Integer.valueOf(stayDays)});

                    if (stayDays != 0) {
                        log.info(" 开始收取 {} 天 留倉费", Integer.valueOf(stayDays));
                        try {

                            closingStayTask(position, Integer.valueOf(stayDays));
                        } catch (Exception e) {
                            log.error("doClosingStayTask = ", e);


                        }


                    } else {


                        log.info("持倉訂單 = {} ,持倉天数0天,不需要处理...", position.getId());
                    }

                    log.info("修改留倉费 处理结束。");
                    log.info("");
                }

                SiteTaskLog stl = new SiteTaskLog();
                stl.setTaskType("扣除留倉费");
                stl.setAddTime(new Date());
                stl.setIsSuccess(Integer.valueOf(0));
                stl.setTaskTarget("扣除留倉费，訂單数量為" + userPositions.size());
                this.siteTaskLogMapper.insert(stl);
            }
        } else {
            log.info("doClosingStayTask没有正在持倉的訂單");
        }
    }

    /*留倉到期強制平倉，每天15点执行*/
   /* public void expireStayUnwindTask() {
        List<UserPosition> userPositions = this.userPositionMapper.findAllStayPosition();


        if (userPositions.size() > 0) {
            log.info("查询到正在持倉的訂單数量 = {}", Integer.valueOf(userPositions.size()));

            SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
            for (UserPosition position : userPositions) {
                //int stayDays = GetStayDays.getDays(GetStayDays.getBeginDate(position.getBuyOrderTime()));
                //getWorkDays 排除週末
                int stayDays = GetStayDays.getWorkDays(GetStayDays.getBeginDate(position.getBuyOrderTime()));
                log.info("");
                log.info("开始处理 持倉訂單id = {} 訂單号 = {} 用戶id = {} realName = {} 留倉天数 = {}", new Object[]{position
                        .getId(), position.getPositionSn(), position.getUserId(), position
                        .getNickName(), Integer.valueOf(stayDays)});

                //留倉达到最大天数
                if (stayDays >= siteSetting.getStayMaxDays().intValue()) {
                    log.info(" 开始強平 {} 天", Integer.valueOf(stayDays));
                    try {
                        this.sell(position.getPositionSn(), 0);
                    } catch (Exception e) {
                        log.error("expireStayUnwindTask = ", e);
                    }
                } else {
                    log.info("持倉訂單 = {} ,持倉天数0天,不需要处理...", position.getId());
                }
            }
        } else {
            log.info("doClosingStayTask没有正在持倉的訂單");
        }
    }*/

    @Transactional
    public ServerResponse closingStayTask(UserPosition position, Integer stayDays) throws Exception {
        log.info("=================closingStayTask====================");
        log.info("修改留倉费，持倉id={},持倉天数={}", position.getId(), stayDays);

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("修改留倉费出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("修改留倉费出错，网站設置表不存在");
        }


        BigDecimal stayFee = position.getOrderTotalPrice().multiply(siteSetting.getStayFee());

        BigDecimal allStayFee = stayFee.multiply(new BigDecimal(stayDays.intValue()));

        log.info("总留倉费 = {}", allStayFee);


        position.setOrderStayFee(allStayFee);
        position.setOrderStayDays(stayDays);

        BigDecimal all_profit = position.getAllProfitAndLose().subtract(allStayFee);
        //用戶總盈虧+遞延費
        position.setAllProfitAndLose(all_profit);

        int updateCount = this.userPositionMapper.updateByPrimaryKeySelective(position);
        if (updateCount > 0) {
            //核算代理收入-延递费
            iAgentAgencyFeeService.AgencyFeeIncome(3, position.getPositionSn());
            log.info("【closingStayTask收持倉费】修改持倉記錄成功");
        } else {
            log.error("【closingStayTask收持倉费】修改持倉記錄出错");
            throw new Exception("【closingStayTask收持倉费】修改持倉記錄出错");
        }


        log.info("=======================================================");
        return ServerResponse.createBySuccess();
    }

    public List<Integer> findDistinctUserIdList() {
        return this.userPositionMapper.findDistinctUserIdList();
    }

    private AdminPositionVO assembleAdminPositionVO(UserPosition position) {
        AdminPositionVO adminPositionVO = new AdminPositionVO();

        adminPositionVO.setId(position.getId());
        adminPositionVO.setPositionSn(position.getPositionSn());
        adminPositionVO.setPositionType(position.getPositionType());
        adminPositionVO.setUserId(position.getUserId());
        adminPositionVO.setNickName(position.getNickName());
        adminPositionVO.setAgentId(position.getAgentId());
        adminPositionVO.setStockName(position.getStockName());
        adminPositionVO.setStockCode(position.getStockCode());
        adminPositionVO.setStockGid(position.getStockGid());
        adminPositionVO.setStockSpell(position.getStockSpell());
        adminPositionVO.setBuyOrderId(position.getBuyOrderId());
        adminPositionVO.setBuyOrderTime(position.getBuyOrderTime());
        adminPositionVO.setBuyOrderPrice(position.getBuyOrderPrice());
        adminPositionVO.setSellOrderId(position.getSellOrderId());
        adminPositionVO.setSellOrderTime(position.getSellOrderTime());
        adminPositionVO.setSellOrderPrice(position.getSellOrderPrice());
        adminPositionVO.setOrderDirection(position.getOrderDirection());
        adminPositionVO.setOrderNum(position.getOrderNum());
        adminPositionVO.setOrderLever(position.getOrderLever());
        adminPositionVO.setOrderTotalPrice(position.getOrderTotalPrice());
        adminPositionVO.setOrderFee(position.getOrderFee());
        adminPositionVO.setOrderSpread(position.getOrderSpread());
        adminPositionVO.setOrderStayFee(position.getOrderStayFee());
        adminPositionVO.setOrderStayDays(position.getOrderStayDays());

        adminPositionVO.setIsLock(position.getIsLock());
        adminPositionVO.setLockMsg(position.getLockMsg());

        adminPositionVO.setStockPlate(position.getStockPlate());

        PositionProfitVO positionProfitVO = getPositionProfitVO(position);
        adminPositionVO.setProfitAndLose(positionProfitVO.getProfitAndLose());
        adminPositionVO.setAllProfitAndLose(positionProfitVO.getAllProfitAndLose());
        adminPositionVO.setNow_price(positionProfitVO.getNowPrice());


        return adminPositionVO;
    }

    private AgentPositionVO assembleAgentPositionVO(UserPosition position) {
        AgentPositionVO agentPositionVO = new AgentPositionVO();

        agentPositionVO.setId(position.getId());
        agentPositionVO.setPositionSn(position.getPositionSn());
        agentPositionVO.setPositionType(position.getPositionType());
        agentPositionVO.setUserId(position.getUserId());
        agentPositionVO.setNickName(position.getNickName());
        agentPositionVO.setAgentId(position.getAgentId());
        agentPositionVO.setStockName(position.getStockName());
        agentPositionVO.setStockCode(position.getStockCode());
        agentPositionVO.setStockGid(position.getStockGid());
        agentPositionVO.setStockSpell(position.getStockSpell());
        agentPositionVO.setBuyOrderId(position.getBuyOrderId());
        agentPositionVO.setBuyOrderTime(position.getBuyOrderTime());
        agentPositionVO.setBuyOrderPrice(position.getBuyOrderPrice());
        agentPositionVO.setSellOrderId(position.getSellOrderId());
        agentPositionVO.setSellOrderTime(position.getSellOrderTime());
        agentPositionVO.setSellOrderPrice(position.getSellOrderPrice());
        agentPositionVO.setOrderDirection(position.getOrderDirection());
        agentPositionVO.setOrderNum(position.getOrderNum());
        agentPositionVO.setOrderLever(position.getOrderLever());
        agentPositionVO.setOrderTotalPrice(position.getOrderTotalPrice());
        agentPositionVO.setOrderFee(position.getOrderFee());
        agentPositionVO.setOrderSpread(position.getOrderSpread());
        agentPositionVO.setOrderStayFee(position.getOrderStayFee());
        agentPositionVO.setOrderStayDays(position.getOrderStayDays());

        agentPositionVO.setIsLock(position.getIsLock());
        agentPositionVO.setLockMsg(position.getLockMsg());

        agentPositionVO.setStockPlate(position.getStockPlate());

        PositionProfitVO positionProfitVO = getPositionProfitVO(position);
        agentPositionVO.setProfitAndLose(positionProfitVO.getProfitAndLose());
        agentPositionVO.setAllProfitAndLose(positionProfitVO.getAllProfitAndLose());
        agentPositionVO.setNow_price(positionProfitVO.getNowPrice());


        return agentPositionVO;
    }

    private UserPositionVO assembleUserPositionVO(UserPosition position) {
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
        userPositionVO.setProfitTargetPrice(position.getProfitTargetPrice());
        userPositionVO.setStopTargetPrice(position.getStopTargetPrice());
        userPositionVO.setOrderDirection(position.getOrderDirection());
        userPositionVO.setOrderNum(position.getOrderNum());
        userPositionVO.setOrderLever(position.getOrderLever());
        userPositionVO.setOrderTotalPrice(position.getOrderTotalPrice());
        userPositionVO.setOrderFee(position.getOrderFee());
        userPositionVO.setOrderSpread(position.getOrderSpread());
        userPositionVO.setOrderStayFee(position.getOrderStayFee());
        userPositionVO.setOrderStayDays(position.getOrderStayDays());
        userPositionVO.setMarginAdd(position.getMarginAdd());

        userPositionVO.setStockPlate(position.getStockPlate());
        userPositionVO.setSpreadRatePrice(position.getSpreadRatePrice());

        PositionProfitVO positionProfitVO = getPositionProfitVO(position);
        userPositionVO.setProfitAndLose(positionProfitVO.getProfitAndLose());
        userPositionVO.setAllProfitAndLose(positionProfitVO.getAllProfitAndLose());
        userPositionVO.setNow_price(positionProfitVO.getNowPrice());


        return userPositionVO;
    }

    public PositionProfitVO getPositionProfitVO(UserPosition position) {
        BigDecimal profitAndLose = new BigDecimal("0");
        BigDecimal allProfitAndLose = new BigDecimal("0");
        String nowPrice = "";

        if (position.getSellOrderId() != null) {

            BigDecimal subPrice = position.getSellOrderPrice().subtract(position.getBuyOrderPrice());
            //profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue())).multiply(new BigDecimal(position.getOrderLever())).setScale(2,4);
            profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue()));
            if ("看跌".equals(position.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }


            allProfitAndLose = profitAndLose.subtract(position.getOrderFee()).subtract(position.getOrderSpread()).subtract(position.getOrderStayFee()).subtract(position.getSpreadRatePrice());
        } else {

//            StockListVO stockListVO = SinaStockApi.assembleStockListVO(
//                    SinaStockApi.getSinaStock(position.getStockGid()));
//            nowPrice = stockListVO.getNowPrice();

//            String twStockResult= UsStockApi.getStock(position.getStockCode());
//            StockListVO stockListVO=UsStockApi.assembleStockListVO(twStockResult);
            StockListVO stockListVO= UsStockApi.getMoomooStock(position.getStockCode());
            nowPrice = stockListVO.getNowPrice();
            BigDecimal subPrice = (new BigDecimal(nowPrice)).subtract(position.getBuyOrderPrice());
            //profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue())).multiply(new BigDecimal(position.getOrderLever())).setScale(2,4);
            profitAndLose = subPrice.multiply(new BigDecimal(position.getOrderNum().intValue()));
            if ("bearish".equals(position.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            //總盈虧= 浮动盈虧 – 手续费 – 印花税 – 留倉费 – 点差费
            allProfitAndLose = profitAndLose.subtract(position.getOrderFee()).subtract(position.getOrderSpread()).subtract(position.getOrderStayFee()).subtract(position.getSpreadRatePrice());
        }
        PositionProfitVO positionProfitVO = new PositionProfitVO();
        positionProfitVO.setProfitAndLose(profitAndLose);
        positionProfitVO.setAllProfitAndLose(allProfitAndLose);
        positionProfitVO.setNowPrice(nowPrice);

        return positionProfitVO;
    }



    /*股票入倉最新top列表*/
    public ServerResponse findPositionTopList(Integer pageSize) {
        List<UserPosition> userPositions = this.userPositionMapper.findPositionTopList(pageSize);

        List<UserPositionVO> userPositionVOS = Lists.newArrayList();
        if (userPositions.size() > 0) {
            for (UserPosition position : userPositions) {

                UserPositionVO userPositionVO = assembleUserPositionVO(position);
                userPositionVOS.add(userPositionVO);
            }
        }

        PageInfo pageInfo = new PageInfo(userPositions);
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /*根据股票代码查询用戶最早入倉股票*/
    public ServerResponse findUserPositionByCode(HttpServletRequest request, String stockCode) {
        User user = this.iUserService.getCurrentRefreshUser(request);
        UserPosition position = this.userPositionMapper.findUserPositionByCode(user.getId(), stockCode);

        List<UserPositionVO> userPositionVOS = Lists.newArrayList();
        UserPositionVO userPositionVO = null;
        if (position != null) {
            userPositionVO = assembleUserPositionVO(position);
        }
        userPositionVOS.add(userPositionVO);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse buyUsStock(String stockId, Integer buyNum, Integer buyType, Integer lever, String nowPrice, String hcrate, String preClose, HttpServletRequest request) throws Exception {
        // 判断週末不能买
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        /*int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }
        if (weekday == 7) {
            return ServerResponse.createByErrorMsg("週末不能購買！");
        }*/

        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        User user = this.iUserService.getCurrentRefreshUser(request);
        if (user==null){
            return ServerResponse.createByErrorMsg("please log in first");
        }
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("Failed to place an order, please verify your real name first");
        }
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("用戶 {} 下單，股票id = {} ，数量 = {} , 方向 = {} , 杠杆 = {}", new Object[]{user
                .getId(), stockId, buyNum, buyType, lever});
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Order failed, account has been locked");
        }
        if (siteProduct.getHolidayDisplay()) {
            return ServerResponse.createByErrorMsg("Cannot be traded on weekends or holidays!");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("下單出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place an order, system settings error");
        }

        String am_begin = siteSetting.getTransAmBegin();
        String am_end = siteSetting.getTransAmEnd();
        String pm_begin = siteSetting.getTransPmBegin();
        String pm_end = siteSetting.getTransPmEnd();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));

        if (!am_flag && !pm_flag) {
            return ServerResponse.createByErrorMsg("Failed to place an order, it is not within the trading hours");
        }

//        TwStock stock = null;
        Stock stock = this.stockMapper.findStockByCode(stockId.toString());
//        ServerResponse stock_res = this.stockMapper.findStockByCode(stockId.toString());
//        if (!stock_res.isSuccess()) {
//            return ServerResponse.createByErrorMsg("下單失敗，股票代码錯誤");
//        }
//        stock = (TwStock) stock_res.getData();


        List dbPosition = findPositionByStockCodeAndTimes(siteSetting.getBuySameTimes(), stock.getStockCode(), user.getId());
        if (dbPosition.size() >= siteSetting.getBuySameNums()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteSetting.getBuySameTimes() + "The position of the same stock within one minute shall not exceed" + siteSetting
                    .getBuySameNums() + "strip");
        }

        Integer transNum = findPositionNumByTimes(siteSetting.getBuyNumTimes(), user.getId());
        if (transNum / 100 >= siteSetting.getBuyNumLots()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteSetting
                    .getBuyNumTimes() + "Minutes cannot exceed" + siteSetting.getBuyNumLots() + "share");
        }

        if (buyNum < siteSetting.getBuyMinNum()) {
            return ServerResponse.createByErrorMsg("Failed to place an order, the purchase quantity is less than" + siteSetting
                    .getBuyMinNum() + "share");
        }
        if (buyNum.intValue() > siteSetting.getBuyMaxNum().intValue()) {
            return ServerResponse.createByErrorMsg("Failed to place an order, the purchase quantity is greater than" + siteSetting
                    .getBuyMaxNum() + "share");
        }



        BigDecimal now_price = new BigDecimal(nowPrice);

        if (now_price.compareTo(new BigDecimal("0")) == 0) {
            return ServerResponse.createByErrorMsg("Quote 0, please try again later");
        }


        double stock_crease = new BigDecimal(hcrate).doubleValue();

//        if ( siteSetting.getStockRate().compareTo((new BigDecimal(String.valueOf(stock_crease)))) == -1) {
//            return ServerResponse.createByErrorMsg(siteSetting.getStockDays() + "天内涨幅超過 " + siteSetting
//                    .getStockRate() + "不能交易");
//        }


        //BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue())).setScale(2, 4);
        BigDecimal buy_amt = now_price.multiply(new BigDecimal(buyNum.intValue()));


        //BigDecimal buy_amt_autual = now_price.multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever.intValue()), 2, 4);
        BigDecimal buy_amt_autual = buy_amt.divide(new BigDecimal(lever.intValue()), 2, 4);


        int compareInt = buy_amt_autual.compareTo(new BigDecimal(siteSetting.getBuyMinAmt().intValue()));
        if (compareInt == -1) {
            return ServerResponse.createByErrorMsg("Failed to place an order, the purchase amount is less than" + siteSetting
                    .getBuyMinAmt() + "USD");
        }


        BigDecimal max_buy_amt = user_enable_amt.multiply(siteSetting.getBuyMaxAmtPercent());
        int compareCwInt = buy_amt_autual.compareTo(max_buy_amt);
        if (compareCwInt == 1) {
            return ServerResponse.createByErrorMsg("Failed to place an order, cannot exceed available funds" + siteSetting
                    .getBuyMaxAmtPercent().multiply(new BigDecimal("100")) + " %");
        }


        int compareUserAmtInt = user_enable_amt.compareTo(buy_amt_autual);
        log.info("用戶可用金额 = {}  实际購買金额 =  {}", user_enable_amt, buy_amt_autual);
        log.info("比较 用戶金额 和 实际 購買金额 =  {}", Integer.valueOf(compareUserAmtInt));
        if (compareUserAmtInt == -1) {
            return ServerResponse.createByErrorMsg("Failed to place an order, the available financing amount is less than" + buy_amt_autual + "USD");
        }

        if (user.getUserIndexAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Failed, index total fund is less than 0");
        }
        if (user.getUserFutAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Failed, the total futures fund is less than 0");
        }

        UserPosition userPosition = new UserPosition();
        userPosition.setPositionType(user.getAccountType());
        userPosition.setPositionSn(KeyUtils.getUniqueKey());
        userPosition.setUserId(user.getId());
        userPosition.setNickName(user.getRealName());
        userPosition.setAgentId(user.getAgentId());
        userPosition.setStockCode(stock.getStockCode());
        userPosition.setStockName(stock.getStockName());
        //userPosition.setStockGid(stock.getStockGid());
        //userPosition.setStockSpell(stock.getStockSpell());
        userPosition.setBuyOrderId(GeneratePosition.getPositionId());
        userPosition.setBuyOrderTime(new Date());
        userPosition.setBuyOrderPrice(now_price);
        userPosition.setOrderDirection((buyType.intValue() == 0) ? "bullish" : "bearish");

        userPosition.setOrderNum(buyNum);
        userPosition.setOrderLever(lever);
        userPosition.setOrderTotalPrice(buy_amt);

        //遞延費特殊处理
        BigDecimal stayFee = userPosition.getOrderTotalPrice().multiply(siteSetting.getStayFee());
        BigDecimal allStayFee = stayFee.multiply(new BigDecimal(1));
        userPosition.setOrderStayFee(allStayFee);
        userPosition.setOrderStayDays(1);
        userPosition.setMarginAdd(BigDecimal.ZERO);

        BigDecimal buy_fee_amt = buy_amt.multiply(siteSetting.getBuyFee()).setScale(2, 4);
        log.info("用戶購買手续费（配資后总資金 * 百分比） = {}", buy_fee_amt);
        userPosition.setOrderFee(buy_fee_amt);


        BigDecimal buy_yhs_amt = buy_amt.multiply(siteSetting.getDutyFee()).setScale(2, 4);
        log.info("用戶購買印花税（配資后总資金 * 百分比） = {}", buy_yhs_amt);
        userPosition.setOrderSpread(buy_yhs_amt);

        SiteSpread siteSpread = iSiteSpreadService.findSpreadRateOne(new BigDecimal(stock_crease), buy_amt, stock.getStockCode(), now_price);
        BigDecimal spread_rate_amt = new BigDecimal("0");
        if (siteSpread != null) {
            spread_rate_amt = buy_amt.multiply(siteSpread.getSpreadRate()).setScale(2, 4);
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", siteSpread.getSpreadRate(), spread_rate_amt);
        } else {
            log.info("用戶購買点差费（配資后总資金 * 百分比{}） = {}", "設置异常", spread_rate_amt);
        }

        userPosition.setSpreadRatePrice(spread_rate_amt);


        BigDecimal profit_and_lose = new BigDecimal("0");
        userPosition.setProfitAndLose(profit_and_lose);


        BigDecimal all_profit_and_lose = profit_and_lose.subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
        userPosition.setAllProfitAndLose(all_profit_and_lose);


        userPosition.setOrderStayDays(Integer.valueOf(0));
        userPosition.setOrderStayFee(new BigDecimal("0"));

        int insertPositionCount = 0;
        this.userPositionMapper.insert(userPosition);
        insertPositionCount = userPosition.getId();
        if (insertPositionCount > 0) {
            //修改用戶可用余额= 當前余额-下單金额-买入手续费-印花税-点差费
            //BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual).subtract(buy_fee_amt).subtract(buy_yhs_amt).subtract(spread_rate_amt);
            //修改用戶可用余额= 當前余额-下單总金额
            BigDecimal reckon_enable = user_enable_amt.subtract(buy_amt_autual);
            user.setEnableAmt(reckon_enable);
            int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateUserCount > 0) {
                log.info("【用戶交易下單】修改用戶金额成功");
            } else {
                log.error("用戶交易下單】修改用戶金额出错");
                throw new Exception("The user traded and placed an order, and the user's amount was modified incorrectly");
            }
            //核算代理收入-入倉手续费
            iAgentAgencyFeeService.AgencyFeeIncome(1, userPosition.getPositionSn());
            log.info("【用戶交易下單】保存持倉記錄成功");
        } else {
            log.error("用戶交易下單】保存持倉記錄出错");
            throw new Exception("The user traded and placed an order, and there was an error in saving the position record");
        }

        return ServerResponse.createBySuccess("successfully ordered");
    }
    @Transactional
    public ServerResponse sellUsStock(String positionSn, int doType) throws Exception {
        log.info("【用戶交易平倉】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            log.error("平倉出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place an order, system settings error");
        }
        SiteProduct siteProduct = iSiteProductService.getProductSetting();

        if (doType != 0) {
            String am_begin = siteSetting.getTransAmBegin();
            String am_end = siteSetting.getTransAmEnd();
            String pm_begin = siteSetting.getTransPmBegin();
            String pm_end = siteSetting.getTransPmEnd();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag) {
                return ServerResponse.createByErrorMsg("Failed to close the position, not within the trading hours");
            }

            if (siteProduct.getHolidayDisplay()) {
                return ServerResponse.createByErrorMsg("Cannot be traded on weekends or holidays!");
            }

        }

        UserPosition userPosition = this.userPositionMapper.findPositionBySn(positionSn);
        if (userPosition == null) {
            return ServerResponse.createByErrorMsg("Failed to close the position, the order does not exist");
        }

        User user = this.userMapper.selectByPrimaryKey(userPosition.getUserId());
        /*實名認證开关开启*/

        if (siteProduct.getRealNameDisplay() && user.getIsLock() == 1) {
            return ServerResponse.createByErrorMsg("Failed to close the position, the user has been locked");
        }


        if (userPosition.getSellOrderId() != null) {
            return ServerResponse.createByErrorMsg("Failed to close the position, this order has been closed");
        }

//        if (1 == userPosition.getIsLock().intValue()) {
////            return ServerResponse.createByErrorMsg("平倉失敗 " + userPosition.getLockMsg());
////        }

        if (!DateTimeUtil.isCanSell(userPosition.getBuyOrderTime(), siteSetting.getCantSellTimes())) {
            return ServerResponse.createByErrorMsg(siteSetting.getCantSellTimes() + "The position cannot be closed within minutes");
        }

//        if (DateTimeUtil.sameDate(DateTimeUtil.getCurrentDate(),userPosition.getBuyOrderTime())) {
//            return ServerResponse.createByErrorMsg("当天入倉的股票需要隔天才能出倉");
//        }

        String resultStr=UsStockApi.getStock(userPosition.getStockCode());
        StockListVO stockListVO = UsStockApi.assembleStockListVO(resultStr);

        BigDecimal now_price = new BigDecimal(stockListVO.getNowPrice());
        if (now_price.compareTo(new BigDecimal("0")) != 1) {
            log.error("股票 = {} 收到报价 = {}", userPosition.getStockName(), now_price);
            return ServerResponse.createByErrorMsg("Quotation 0, failed to close position, please try again later");
        }

        double stock_crease = stockListVO.getHcrate().doubleValue();

        BigDecimal zsPrice = new BigDecimal(stockListVO.getPreclose_px());

        BigDecimal ztPrice = zsPrice.multiply(new BigDecimal("0.1")).add(zsPrice);
        ztPrice = ztPrice.setScale(2, 4);
        BigDecimal chaPrice = ztPrice.subtract(zsPrice);

        BigDecimal ztRate = chaPrice.multiply(new BigDecimal("100")).divide(zsPrice, 2, 4);

        ztRate = ztRate.negate();
        log.info("股票當前涨跌幅 = {} 跌停幅度 = {}", stock_crease, ztRate);
//        if ((new BigDecimal(String.valueOf(stock_crease))).compareTo(ztRate) == 0 && "bullish"
//                .equals(userPosition.getOrderDirection())) {
//            return ServerResponse.createByErrorMsg("當前股票已跌停不能卖出");
//        }

        Integer buy_num = userPosition.getOrderNum();

        BigDecimal all_buy_amt = userPosition.getOrderTotalPrice();
        //BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue())).divide(new BigDecimal(userPosition.getOrderLever())).setScale(2,4);
        BigDecimal all_sell_amt = now_price.multiply(new BigDecimal(buy_num.intValue()));

        BigDecimal profitLoss = new BigDecimal("0");
        if ("bullish".equals(userPosition.getOrderDirection())) {
            log.info("买卖方向：{}", "涨");
            profitLoss = all_sell_amt.subtract(all_buy_amt);
        } else {
            log.info("买卖方向：{}", "跌");
            profitLoss = all_buy_amt.subtract(all_sell_amt);
        }
        log.info("买入总金额 = {} , 卖出总金额 = {} , 盈虧 = {}", new Object[]{all_buy_amt, all_sell_amt, profitLoss});

        BigDecimal user_all_amt = user.getUserAmt();
        BigDecimal user_enable_amt = user.getEnableAmt();
        log.info("用戶原本总資金 = {} , 可用 = {}", user_all_amt, user_enable_amt);

        BigDecimal buy_fee_amt = userPosition.getOrderFee();
        log.info("买入手续费 = {}", buy_fee_amt);

        BigDecimal orderSpread = userPosition.getOrderSpread();
        log.info("印花税 = {}", orderSpread);

        BigDecimal orderStayFee = userPosition.getOrderStayFee();
        log.info("遞延費 = {}", orderStayFee);

        BigDecimal spreadRatePrice = userPosition.getSpreadRatePrice();
        log.info("点差费 = {}", spreadRatePrice);

        BigDecimal sell_fee_amt = all_sell_amt.multiply(siteSetting.getSellFee()).setScale(2, 4);
        log.info("卖出手续费 = {}", sell_fee_amt);

        //總手續費= 买入手续费+卖出手续费+印花税+遞延費+点差费
        BigDecimal all_fee_amt = buy_fee_amt.add(sell_fee_amt).add(orderSpread).add(orderStayFee).add(spreadRatePrice);
        log.info("总的手续费费用 = {}", all_fee_amt);

        userPosition.setSellOrderId(GeneratePosition.getPositionId());
        userPosition.setSellOrderPrice(now_price);
        userPosition.setSellOrderTime(new Date());

        //修改orderFee
        BigDecimal order_fee_all = buy_fee_amt.add(sell_fee_amt);
        //order_fee最后修改為平倉手续费
        userPosition.setOrderFee(sell_fee_amt);

        userPosition.setProfitAndLose(profitLoss);

        BigDecimal all_profit = profitLoss.subtract(all_fee_amt);
        userPosition.setAllProfitAndLose(all_profit);

        int updatePositionCount = this.userPositionMapper.updateByPrimaryKeySelective(userPosition);
        if (updatePositionCount > 0) {
            log.info("【用戶平倉】修改浮动盈虧記錄成功");
        } else {
            log.error("用戶平倉】修改浮动盈虧記錄出错");
            throw new Exception("用戶平倉】修改浮动盈虧記錄出错");
        }

        BigDecimal freez_amt = all_buy_amt.divide(new BigDecimal(userPosition.getOrderLever().intValue()), 2, 4);
        //BigDecimal freez_amt = all_buy_amt;

        BigDecimal reckon_all = user_all_amt.add(all_profit);
        //修改用戶可用余额=當前可用余额+總盈虧+买入总金额+追加保证金
        BigDecimal reckon_enable = user_enable_amt.add(all_profit);//.add(freez_amt).add(userPosition.getMarginAdd()).add(userPosition.getOrderTotalPrice());
        reckon_enable = reckon_enable.add(freez_amt);
        reckon_enable = reckon_enable.add(userPosition.getMarginAdd());
       // reckon_enable = reckon_enable.add(userPosition.getOrderTotalPrice().divide(BigDecimal.valueOf(userPosition.getOrderLever())));
        log.info("用戶平倉后的总資金  = {} , 可用資金 = {}", reckon_all, reckon_enable);
        user.setUserAmt(reckon_all);
        user.setEnableAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶平倉】修改用戶金额成功");
        } else {
            log.error("用戶平倉】修改用戶金额出错");
            throw new Exception("The user closed the position and modified the user's amount error");
        }

        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("total profit and loss ");
        ucd.setDeAmt(all_profit);
//        ucd.setDeSummary("賣出股票，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",佔用本金：" + freez_amt + ",總手續費：" + all_fee_amt + ",建倉費：" + buy_fee_amt + ",遞延費：" + orderStayFee + ",印花税：" + orderSpread + ",盈虧：" + profitLoss + "，總盈虧：" + all_profit);
        ucd.setDeSummary("sell stock，" + userPosition.getStockCode() + "/" + userPosition.getStockName() + ",Occupy the principal：" + freez_amt + ",total handling fee：" + all_fee_amt + ",Opening fee：" + buy_fee_amt + ",deferred fee：" + orderStayFee + ",profit and loss：" + profitLoss + "，total profit and loss：" + all_profit);
        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            //核算代理收入-平倉手续费
            iAgentAgencyFeeService.AgencyFeeIncome(2, userPosition.getPositionSn());
            //核算代理收入-分红
            iAgentAgencyFeeService.AgencyFeeIncome(4, userPosition.getPositionSn());
            log.info("【用戶平倉】保存明细記錄成功");
        } else {
            log.error("用戶平倉】保存明细記錄出错");
            throw new Exception("The user closes the position and saves the detailed record error");
        }

        return ServerResponse.createBySuccessMsg("Successfully closed!");
    }

}

