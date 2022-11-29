package com.xc.service.impl;


import com.xc.dao.*;
import com.xc.pojo.*;
import com.xc.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xc.common.ServerResponse;
import com.xc.service.*;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.KeyUtils;
import com.xc.utils.stock.BuyAndSellUtils;
import com.xc.utils.stock.sina.SinaStockApi;
import com.xc.vo.agent.AgentIncomeVO;
import com.xc.vo.futuresposition.AdminFuturesPositionVO;
import com.xc.vo.futuresposition.AgentFuturesPositionVO;
import com.xc.vo.futuresposition.FuturesPositionProfitVO;
import com.xc.vo.futuresposition.FuturesPositionVO;
import com.xc.vo.futuresposition.UserFuturesPositionVO;
import com.xc.vo.position.UserPositionVO;
import com.xc.vo.stock.MarketVO;
import com.xc.vo.stockfutures.FuturesVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("iUserFuturesPositionService")
public class UserFuturesPositionServiceImpl implements IUserFuturesPositionService {
    private static final Logger log = LoggerFactory.getLogger(UserFuturesPositionServiceImpl.class);

    @Autowired
    UserFuturesPositionMapper userFuturesPositionMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    IStockFuturesService iStockFuturesService;

    @Autowired
    IAgentUserService iAgentUserService;

    @Autowired
    AgentUserMapper agentUserMapper;

    @Autowired
    IStockCoinService iStockCoinService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserCashDetailMapper userCashDetailMapper;
    @Autowired
    ISiteFuturesSettingService iSiteFuturesSettingService;
    @Autowired
    StockFuturesMapper stockFuturesMapper;
    @Autowired
    ISiteProductService iSiteProductService;

    @Transactional
    public ServerResponse buyFutures(Integer futuresId, Integer buyNum, Integer buyType, Integer lever, HttpServletRequest request) throws Exception {
        if (futuresId == null || buyNum == null || buyType == null) {
            return ServerResponse.createByErrorMsg("parameter cannot be empty");
        }
        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        User user = this.iUserService.getCurrentRefreshUser(request);
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("parameter cannot be empty");
        }

        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("No trading on weekends or holidays");
        }

        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Order failed, account has been locked");
        }

        StockFutures stockFutures = this.iStockFuturesService.selectFuturesById(futuresId);
        if (stockFutures == null) {
            return ServerResponse.createByErrorMsg("Product does not exist");
        }
        if (1 != stockFutures.getTransState().intValue()) {
            return ServerResponse.createByErrorMsg("This product cannot be traded");
        }
        if (buyNum.intValue() < stockFutures.getMinNum().intValue()) {
            return ServerResponse.createByErrorMsg("Minimum cannot be lower than" + stockFutures.getMinNum() );
        }
        if (buyNum.intValue() > stockFutures.getMaxNum().intValue()) {
            return ServerResponse.createByErrorMsg("no more than" + stockFutures.getMaxNum());
        }
        log.info("用戶 {} 下單, 期货產品 = {} ，数量 = {} 手 , 方向 = {} ， 杠杆倍数 = {}", new Object[]{user
                .getId(), stockFutures.getFuturesName(), buyNum, buyType, lever});


        String am_begin = stockFutures.getTransAmBegin();
        String am_end = stockFutures.getTransAmEnd();
        String pm_begin = stockFutures.getTransPmBegin();
        String pm_end = stockFutures.getTransPmEnd();
        String pm_begin2 = stockFutures.getTransPmBegin2();
        String pm_end2 = stockFutures.getTransPmEnd2();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        boolean pm_flag2 = BuyAndSellUtils.isTransTime(pm_begin2, pm_end2);
        log.info("futures 是否在上午交易時间 = {} 是否在下午交易時间 = {}是否在下午交易時间2 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag), Boolean.valueOf(pm_flag2));
        if (!am_flag && !pm_flag && !pm_flag2) {
            return ServerResponse.createByErrorMsg("Failed to place order, not during trading hours");
        }


        SiteFuturesSetting siteFuturesSetting = this.iSiteFuturesSettingService.getSetting();
        if (siteFuturesSetting == null) {
            log.error("下單出错，网站設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place order, system setting error");
        }

        List dbPosition = findPositionByFuturesCodeAndTimes(siteFuturesSetting.getBuySameTimes().intValue(), stockFutures
                .getFuturesCode(), user.getId());
        if (dbPosition.size() >= siteFuturesSetting.getBuySameNums().intValue()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteFuturesSetting.getBuySameTimes() + "Within one minute, the position of the same product cannot exceed" + siteFuturesSetting
                    .getBuySameNums() + "strip");
        }

        Integer transNum = findPositionNumByTimes(siteFuturesSetting.getBuyNumTimes().intValue(), user.getId());
        if (transNum.intValue() >= siteFuturesSetting.getBuyNumLots().intValue()) {
            return ServerResponse.createByErrorMsg("frequent transactions," + siteFuturesSetting.getBuyNumTimes() + "no more than a minute" + siteFuturesSetting
                    .getBuyNumLots());
        }


        // 保证金= 期货保证金*数量/杠杆倍数
        BigDecimal all_deposit_amt = (new BigDecimal(stockFutures.getDepositAmt().intValue())).multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever)).setScale(4,2);


        BigDecimal all_order_fee = (new BigDecimal(stockFutures.getTransFee().intValue())).multiply(new BigDecimal(buyNum.intValue()));


        ServerResponse exchangeRateResponse = this.iStockFuturesService.getExchangeRate(stockFutures.getCoinCode());
        if (!exchangeRateResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg(exchangeRateResponse.getMsg());
        }
        BigDecimal exchangeRate = (BigDecimal) exchangeRateResponse.getData();
        log.info("期货產品 {} , 基币 {} , 汇率模版采用 {} ", new Object[]{stockFutures.getFuturesName(), stockFutures
                .getCoinCode(), exchangeRate});

        BigDecimal all_usd_amt = all_deposit_amt;
        log.info("需要支付的总資金（保证金） = {}  未计算汇率", all_usd_amt);

        all_usd_amt = all_usd_amt.multiply(exchangeRate);
        log.info("计算汇率后 人民币 = {} ", all_usd_amt);


        if (user.getEnableFutAmt().compareTo(all_usd_amt) == -1) {
            return ServerResponse.createByErrorMsg("Insufficient funds in the account");
        }
        if (user.getUserAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Failed, the total financing fund is less than 0");
        }
        if (user.getUserIndexAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Failed, the total fund of the index is less than 0");
        }


        BigDecimal max_buy_amt = user.getEnableFutAmt().multiply(siteFuturesSetting.getBuyMaxPercent());
        if (max_buy_amt.compareTo(all_usd_amt) == -1) {
            return ServerResponse.createByErrorMsg("Cannot exceed the maximum purchase ratio");
        }


        FuturesVO futuresVO = this.iStockFuturesService.querySingleMarket(stockFutures.getFuturesGid());
        log.info("期货下單 {} , 价格 = {}", futuresVO.getName(), futuresVO.getNowPrice());


        BigDecimal reckon_enable = user.getEnableFutAmt().subtract(all_usd_amt);
        user.setEnableFutAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶交易 期货 下單】修改用戶金额成功");
        } else {
            log.error("【用戶交易 期货 下單】修改用戶金额出错");
            throw new Exception("【User Trading Futures Order】Error in modifying user amount");
        }


        UserFuturesPosition userFuturesPosition = new UserFuturesPosition();
        userFuturesPosition.setPositionType(user.getAccountType());
        userFuturesPosition.setPositionSn(KeyUtils.getUniqueKey());
        userFuturesPosition.setUserId(user.getId());
        userFuturesPosition.setRealName(user.getRealName());
        userFuturesPosition.setAgentId(user.getAgentId());
        userFuturesPosition.setFuturesName(stockFutures.getFuturesName());
        userFuturesPosition.setFuturesCode(stockFutures.getFuturesCode());
        userFuturesPosition.setFuturesGid(stockFutures.getFuturesGid());
        userFuturesPosition.setBuyOrderTime(new Date());
        userFuturesPosition.setBuyOrderPrice(new BigDecimal(futuresVO.getNowPrice()));
        userFuturesPosition.setOrderDirection((buyType.intValue() == 0) ? "bullish" : "bearish");

        userFuturesPosition.setOrderNum(buyNum);
        userFuturesPosition.setFuturesStandard(stockFutures.getFuturesStandard());
        userFuturesPosition.setFuturesUnit(stockFutures.getFuturesUnit());

        userFuturesPosition.setAllDepositAmt(all_usd_amt);
        userFuturesPosition.setOrderFee(all_order_fee);
        userFuturesPosition.setIsLock(Integer.valueOf(0));
        userFuturesPosition.setProfitAndLose(new BigDecimal("0"));
        userFuturesPosition.setAllProfitAndLose(new BigDecimal("0"));
        userFuturesPosition.setBuyRate(exchangeRate);

        userFuturesPosition.setCoinCode(stockFutures.getCoinCode());
        userFuturesPosition.setOrderLever(lever);


        int insertPositionCount = this.userFuturesPositionMapper.insert(userFuturesPosition);
        if (insertPositionCount > 0) {
            log.info("【用戶交易 期货 下單】保存持倉記錄成功");
        } else {
            log.error("用戶交易 期货 下單】保存持倉記錄出错");
            throw new Exception("User trading futures order] error saving position record");
        }

        return ServerResponse.createBySuccess("successfully ordered");
    }

    @Override
    public ServerResponse del(Integer positionId) {
        if (positionId == null) {
            return ServerResponse.createByErrorMsg("id cannot be empty");
        }
        UserFuturesPosition position = this.userFuturesPositionMapper.selectByPrimaryKey(positionId);
        if (position == null) {
            ServerResponse.createByErrorMsg("The position does not exist");
        }

        int updateCount = this.userFuturesPositionMapper.deleteByPrimaryKey(positionId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("successfully deleted");
        }
        return ServerResponse.createByErrorMsg("failed to delete");
    }


    public ServerResponse sellFutures(String positionSn, int doType) throws Exception {
        log.info("【 用戶交易平倉 期货 】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));


        UserFuturesPosition userFuturesPosition = this.userFuturesPositionMapper.selectPositionBySn(positionSn);
        if (userFuturesPosition == null) {
            return ServerResponse.createByErrorMsg("Operation failed, order not found");
        }


        StockFutures stockFutures = this.iStockFuturesService.selectFuturesByCode(userFuturesPosition.getFuturesCode());
        if (stockFutures == null) {
            return ServerResponse.createByErrorMsg("Operation failed, product does not exist or has been deleted");
        }


        if (doType != 0) {
            String am_begin = stockFutures.getTransAmBegin();
            String am_end = stockFutures.getTransAmEnd();
            String pm_begin = stockFutures.getTransPmBegin();
            String pm_end = stockFutures.getTransPmEnd();
            String pm_begin2 = stockFutures.getTransPmBegin2();
            String pm_end2 = stockFutures.getTransPmEnd2();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            boolean pm_flag2 = BuyAndSellUtils.isTransTime(pm_begin2, pm_end2);
            log.info("【 期货 】是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag && !pm_flag2) {
                return ServerResponse.createByErrorMsg("Operation failed, not during trading hours");
            }
        }


        User user = this.userMapper.selectByPrimaryKey(userFuturesPosition.getUserId());
        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Operation failed, user is locked out");
        }
        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("No trading on weekends or holidays");
        }

        if (userFuturesPosition.getSellOrderPrice() != null) {
            return ServerResponse.createByErrorMsg("Operation failed, this order has been closed");
        }

        if (1 == userFuturesPosition.getIsLock().intValue()) {
            return ServerResponse.createByErrorMsg("Closed position failed " + userFuturesPosition.getLockMsg());
        }


        FuturesVO futuresVO = this.iStockFuturesService.querySingleMarket(stockFutures.getFuturesGid());
        log.info("平倉 期货訂單 {} ，平倉价格為 {}", userFuturesPosition.getId(), futuresVO.getNowPrice());


        userFuturesPosition.setSellOrderPrice(new BigDecimal(futuresVO.getNowPrice()));
        userFuturesPosition.setSellOrderTime(new Date());

        BigDecimal point_sub = userFuturesPosition.getSellOrderPrice().setScale(2,4).subtract(userFuturesPosition.getBuyOrderPrice().setScale(2,4));

        //每手浮动价格
        BigDecimal eachPoint = new BigDecimal("1");
        if(stockFutures!=null && stockFutures.getEachPoint() != null){
            eachPoint = stockFutures.getEachPoint();
        }
        //BigDecimal profit_and_lose = point_sub.multiply(new BigDecimal(userFuturesPosition.getFuturesStandard().intValue())).multiply(new BigDecimal(userFuturesPosition.getOrderNum().intValue()));
        BigDecimal profit_and_lose = point_sub.multiply(eachPoint).multiply(new BigDecimal(userFuturesPosition.getOrderNum().intValue()));
        if ("看跌".equals(userFuturesPosition.getOrderDirection())) {
            profit_and_lose = profit_and_lose.negate();
        }
        BigDecimal all_profit = profit_and_lose.subtract(userFuturesPosition.getOrderFee());


        ServerResponse exchangeRateResponse = this.iStockFuturesService.getExchangeRate(stockFutures.getCoinCode());
        if (!exchangeRateResponse.isSuccess()) {
            return ServerResponse.createByErrorMsg(exchangeRateResponse.getMsg());
        }
        BigDecimal exchangeRate = (BigDecimal) exchangeRateResponse.getData();
        exchangeRate = exchangeRate.setScale(2, 4);
        log.info("【平倉】期货產品 {} , 基币 {} , 汇率模版采用 {} ", new Object[]{stockFutures.getFuturesName(), stockFutures
                .getCoinCode(), exchangeRate});

        userFuturesPosition.setSellRate(exchangeRate);


        profit_and_lose = profit_and_lose.multiply(exchangeRate);
        userFuturesPosition.setProfitAndLose(profit_and_lose);
        log.info("【平倉】USD總盈虧 {} ", all_profit);
        all_profit = all_profit.multiply(exchangeRate);
        userFuturesPosition.setAllProfitAndLose(all_profit);


        int updateFuturesPositionCount = this.userFuturesPositionMapper.updateByPrimaryKeySelective(userFuturesPosition);
        if (updateFuturesPositionCount > 0) {
            log.info("【用戶平倉 期货 】修改浮动盈虧記錄成功");
        } else {
            log.error("【用戶平倉 期货 】修改浮动盈虧記錄出错");
            throw new Exception("User closes futures] Error in modifying floating profit and loss record");
        }


        BigDecimal before_user_amt = user.getUserFutAmt();
        BigDecimal before_enable_amt = user.getEnableFutAmt();
        log.info("用戶平倉之前 的 总資金  = {} , 可用資金 = {}", before_user_amt, before_enable_amt);

        BigDecimal user_futures_amt = before_user_amt.add(all_profit);

        BigDecimal enable_futures_amt = before_enable_amt.add(all_profit).add(userFuturesPosition.getAllDepositAmt());

        log.info("用戶平倉后的总資金  = {} , 可用資金 = {}", user_futures_amt, enable_futures_amt);
        user.setUserFutAmt(user_futures_amt);
        user.setEnableFutAmt(enable_futures_amt);

        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶平倉 期货 】修改用戶金额成功");
        } else {
            log.error("【用戶平倉 期货 】修改用戶金额出错");
            throw new Exception("【User closes futures】There is an error in modifying the user's amount");
        }


        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userFuturesPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("futures profit and loss");
        ucd.setDeAmt(all_profit);
        String deSummary = "sell futures，" + userFuturesPosition.getFuturesName() + "/" + userFuturesPosition.getFuturesCode() + "，Total profit and loss=" + all_profit;
        ucd.setDeSummary(deSummary);
        log.info("卖出期货資金明细：{}", deSummary);


        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            log.info("【用戶平倉 期货 】保存明细記錄成功");
        } else {
            log.error("【用戶平倉 期货 】保存明细記錄出错");
            throw new Exception("【User closes futures】Error saving detailed records");
        }

        return ServerResponse.createBySuccessMsg("Closed position successfully");
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


        List<UserFuturesPosition> userFuturesPositions = this.userFuturesPositionMapper.listByAdmin(positionType, state, userId, agentId, positionSn, begin_time, end_time);


        List<AdminFuturesPositionVO> adminFuturesPositionVOS = Lists.newArrayList();
        String queryString="";
        String[] httpResults=null;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            queryString+=userFuturesPosition.getFuturesGid()+",";
        }
        if(StringUtils.isNotBlank(queryString)){
            httpResults= SinaStockApi.getSinaStockList(queryString);
        }
        int i=0;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            String httpResult = httpResults[i].
                    substring(httpResults[i].indexOf("\"") + 1, httpResults[i].lastIndexOf("\""));
            i++;
            FuturesVO futuresVO=new FuturesVO();
            if (StringUtils.isNotBlank(httpResult)) {
                if (httpResult.contains(",")) {
                    String[] sh01_arr = httpResult.split(",");
                    futuresVO=setFuturesVO(futuresVO,sh01_arr);
                }
            }
            AdminFuturesPositionVO adminFuturesPositionVO = assembleAdminFuturesPositionVO(userFuturesPosition,futuresVO);
            adminFuturesPositionVOS.add(adminFuturesPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userFuturesPositions);
        pageInfo.setList(adminFuturesPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse lock(Integer positionId, Integer state, String lockMsg) {
        if (positionId == null || state == null) {
            return ServerResponse.createByErrorMsg("參數不能為空");
        }

        UserFuturesPosition position = this.userFuturesPositionMapper.selectByPrimaryKey(positionId);
        if (position == null) {
            return ServerResponse.createByErrorMsg("持倉不存在");
        }

        if (position.getSellOrderPrice() != null) {
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

        int updateCount = this.userFuturesPositionMapper.updateByPrimaryKeySelective(position);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }


    public ServerResponse findMyFuturesPositionByNameAndCode(String fuName, String fuCode, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        User user = this.iUserService.getCurrentUser(request);

        PageHelper.startPage(pageNum, pageSize);


        List<UserFuturesPosition> userFuturesPositions = this.userFuturesPositionMapper.findMyFuturesPositionByNameAndCode(user.getId(), fuName, fuCode, state);

        List<UserFuturesPositionVO> userFuturesPositionVOS = Lists.newArrayList();
        String queryString="";
        String[] httpResults=null;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            queryString+=userFuturesPosition.getFuturesGid()+",";
        }
        if(StringUtils.isNotBlank(queryString)){
            httpResults= SinaStockApi.getSinaStockList(queryString);
        }
        int i=0;
        if (userFuturesPositions.size() > 0) {
            for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
                String httpResult = httpResults[i].
                        substring(httpResults[i].indexOf("\"") + 1, httpResults[i].lastIndexOf("\""));
                i++;
                FuturesVO futuresVO=new FuturesVO();
                if (StringUtils.isNotBlank(httpResult)) {
                    if (httpResult.contains(",")) {
                        String[] sh01_arr = httpResult.split(",");
                        futuresVO=setFuturesVO(futuresVO,sh01_arr);
                    }
                }
                UserFuturesPositionVO userFuturesPositionVO = assembleUserFuturesPositionVO(userFuturesPosition,futuresVO);
                userFuturesPositionVOS.add(userFuturesPositionVO);
            }
        }
        PageInfo pageInfo = new PageInfo(userFuturesPositions);
        pageInfo.setList(userFuturesPositionVOS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*根据期货代码查询用戶最早入倉股票*/
    public ServerResponse findUserFuturesPositionByCode(HttpServletRequest request, String futuresGid) {
        User user = this.iUserService.getCurrentRefreshUser(request);
        UserFuturesPosition position = this.userFuturesPositionMapper.findUserFuturesPositionByCode(user.getId(), futuresGid);

        List<UserFuturesPositionVO> userPositionVOS = Lists.newArrayList();
        UserFuturesPositionVO userPositionVO = null;
        String httpResult=SinaStockApi.getSinaStock(position.getFuturesGid());
        FuturesVO futuresVO=new FuturesVO();
        if (StringUtils.isNotBlank(httpResult)) {
            if (httpResult.contains(",")) {
                String[] sh01_arr = httpResult.split(",");
                futuresVO=setFuturesVO(futuresVO,sh01_arr);
            }
        }
        if(position != null){
            userPositionVO = assembleUserFuturesPositionVO(position,futuresVO);
        }
        userPositionVOS.add(userPositionVO);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse listByAgent(Integer positionType, Integer state, Integer userId, Integer agentId, String positionSn, String beginTime, String endTime, HttpServletRequest request, int pageNum, int pageSize) {
        AgentUser currentAgent = this.iAgentUserService.getCurrentAgent(request);


        if (agentId != null) {
            AgentUser agentUser = this.agentUserMapper.selectByPrimaryKey(agentId);
            if (agentUser.getParentId() != currentAgent.getId()) {
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

        List<UserFuturesPosition> userFuturesPositions = this.userFuturesPositionMapper.listByAgent(positionType, state, userId, searchId, positionSn, begin_time, end_time);

        List<AgentFuturesPositionVO> agentFuturesPositionVOS = Lists.newArrayList();

        String queryString="";
        String[] httpResults=null;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            queryString+=userFuturesPosition.getFuturesGid()+",";
        }
        if(StringUtils.isNotBlank(queryString)){
            httpResults= SinaStockApi.getSinaStockList(queryString);
        }
        int i=0;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            String httpResult = httpResults[i].
                    substring(httpResults[i].indexOf("\"") + 1, httpResults[i].lastIndexOf("\""));
            i++;
            FuturesVO futuresVO=new FuturesVO();
            if (StringUtils.isNotBlank(httpResult)) {
                if (httpResult.contains(",")) {
                    String[] sh01_arr = httpResult.split(",");
                    futuresVO=setFuturesVO(futuresVO,sh01_arr);
                }
            }
            AgentFuturesPositionVO agentFuturesPositionVO = assembleAgentFuturesPositionVO(userFuturesPosition,futuresVO);
            agentFuturesPositionVOS.add(agentFuturesPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userFuturesPositions);
        pageInfo.setList(agentFuturesPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public List<UserFuturesPosition> findPositionByFuturesCodeAndTimes(int minuteTimes, String futuresCode, Integer userId) {
        Date paramTimes = null;
        paramTimes = DateTimeUtil.parseToDateByMinute(minuteTimes);
        return this.userFuturesPositionMapper.findPositionByFuturesCodeAndTimes(paramTimes, futuresCode, userId);
    }


    public Integer findPositionNumByTimes(int minuteTimes, Integer userId) {
        Date beginDate = DateTimeUtil.parseToDateByMinute(minuteTimes);

        Integer transNum = this.userFuturesPositionMapper.findPositionNumByTimes(beginDate, userId);
        log.info("用戶 {} 在 {} 分鐘之内 交易手数 {}", new Object[]{userId, Integer.valueOf(minuteTimes), transNum});
        return transNum;
    }


    public List<Integer> findDistinctUserIdList() {
        return this.userFuturesPositionMapper.findDistinctUserIdList();
    }


    public List<UserFuturesPosition> findFuturesPositionByUserIdAndSellPriceIsNull(Integer userId) {
        return this.userFuturesPositionMapper.findFuturesPositionByUserIdAndSellPriceIsNull(userId);
    }

    //updated edison
    public FuturesPositionVO findUserFuturesPositionAllProfitAndLose(Integer userId) {
        List<UserFuturesPosition> userFuturesPositions = this.userFuturesPositionMapper.findFuturesPositionByUserIdAndSellPriceIsNull(userId);

        BigDecimal allProfitAndLose = new BigDecimal("0");
        BigDecimal allDepositAmt = new BigDecimal("0");
        String queryString="";
        for(UserFuturesPosition userFuturesPosition : userFuturesPositions){
            queryString+=userFuturesPosition.getFuturesGid()+",";
        }
        String[] httpResults = null;
        if(StringUtils.isNotBlank(queryString)){
            httpResults= SinaStockApi.getSinaStockList(queryString);
        }
        int i=0;
        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            //單條股票的sina api数据
            String httpResult = httpResults[i].
                    substring(httpResults[i].indexOf("\"") + 1, httpResults[i].lastIndexOf("\""));
            i++;
            FuturesVO futuresVO=new FuturesVO();
            if (StringUtils.isNotBlank(httpResult)) {
                if (httpResult.contains(",")) {
                    String[] sh01_arr = httpResult.split(",");
                    futuresVO=setFuturesVO(futuresVO,sh01_arr);
                }
            }
            //FuturesVO futuresVO = this.iStockFuturesService.querySingleMarket(userFuturesPosition.getFuturesGid());
            BigDecimal nowPrice = new BigDecimal(futuresVO.getNowPrice());


            if (nowPrice.compareTo(new BigDecimal("0")) != 0) {


                FuturesPositionProfitVO futuresPositionProfitVO = getFuturesPositionProfitVO(userFuturesPosition,futuresVO);

                allDepositAmt = allDepositAmt.add(userFuturesPosition.getAllDepositAmt());


                BigDecimal usd_profit = futuresPositionProfitVO.getAllProfitAndLose();

                ServerResponse serverResponse = this.iStockFuturesService.getExchangeRate(userFuturesPosition.getCoinCode());
                BigDecimal coinRate = new BigDecimal("0");
                if (serverResponse.isSuccess()) {
                    coinRate = (BigDecimal) serverResponse.getData();
                }


                usd_profit = usd_profit.multiply(coinRate);

                allProfitAndLose = allProfitAndLose.add(usd_profit);
                continue;
            }
            log.info("查询所有持倉單的總盈虧，現价返回0，當前為集合竞价");
        }


        FuturesPositionVO futuresPositionVO = new FuturesPositionVO();
        futuresPositionVO.setAllFuturesProfitAndLose(allProfitAndLose);
        futuresPositionVO.setAllFuturesDepositAmt(allDepositAmt);
        return futuresPositionVO;
    }


    public ServerResponse getFuturesIncome(Integer agentId, Integer positionType, String beginTime, String endTime) {
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


        List<UserFuturesPosition> userFuturesPositions = this.userFuturesPositionMapper.listByAdmin(positionType, Integer.valueOf(1), null, agentId, null, begin_time, end_time);
        log.info("查询到反佣週期内 futures 訂單数：{}", Integer.valueOf(userFuturesPositions.size()));

        BigDecimal order_fee_amt = new BigDecimal("0");
        BigDecimal order_profit_and_lose = new BigDecimal("0");
        BigDecimal order_profit_and_lose_all = new BigDecimal("0");

        for (UserFuturesPosition userFuturesPosition : userFuturesPositions) {
            order_fee_amt = order_fee_amt.add(userFuturesPosition.getOrderFee());

            order_profit_and_lose = order_profit_and_lose.add(userFuturesPosition.getProfitAndLose());

            order_profit_and_lose_all = order_profit_and_lose_all.add(userFuturesPosition.getAllProfitAndLose());
        }

        AgentIncomeVO agentIncomeVO = new AgentIncomeVO();
        agentIncomeVO.setOrderSize(Integer.valueOf(userFuturesPositions.size()));
        agentIncomeVO.setOrderFeeAmt(order_fee_amt);
        agentIncomeVO.setOrderProfitAndLose(order_profit_and_lose);
        agentIncomeVO.setOrderAllAmt(order_profit_and_lose_all);

        return ServerResponse.createBySuccess(agentIncomeVO);
    }


    private UserFuturesPositionVO assembleUserFuturesPositionVO(UserFuturesPosition userFuturesPosition,FuturesVO futuresVO) {
        UserFuturesPositionVO userFuturesPositionVO = new UserFuturesPositionVO();

        BigDecimal eachPoint = new BigDecimal("1");
        StockFutures stockFutures = stockFuturesMapper.selectFuturesByCode(userFuturesPosition.getFuturesCode());
        if(stockFutures != null){
            eachPoint = stockFutures.getEachPoint();
        }

        userFuturesPositionVO.setId(userFuturesPosition.getId());
        userFuturesPositionVO.setPositionType(userFuturesPosition.getPositionType());
        userFuturesPositionVO.setPositionSn(userFuturesPosition.getPositionSn());
        userFuturesPositionVO.setUserId(userFuturesPosition.getUserId());
        userFuturesPositionVO.setRealName(userFuturesPosition.getRealName());
        userFuturesPositionVO.setAgentId(userFuturesPosition.getAgentId());
        userFuturesPositionVO.setFuturesName(userFuturesPosition.getFuturesName());
        userFuturesPositionVO.setFuturesCode(userFuturesPosition.getFuturesCode());
        userFuturesPositionVO.setFuturesGid(userFuturesPosition.getFuturesGid());
        userFuturesPositionVO.setBuyOrderTime(userFuturesPosition.getBuyOrderTime());
        userFuturesPositionVO.setBuyOrderPrice(userFuturesPosition.getBuyOrderPrice());
        userFuturesPositionVO.setSellOrderTime(userFuturesPosition.getSellOrderTime());
        userFuturesPositionVO.setSellOrderPrice(userFuturesPosition.getSellOrderPrice());
        userFuturesPositionVO.setOrderDirection(userFuturesPosition.getOrderDirection());
        userFuturesPositionVO.setOrderNum(userFuturesPosition.getOrderNum());
        userFuturesPositionVO.setFuturesStandard(userFuturesPosition.getFuturesStandard());
        userFuturesPositionVO.setFuturesUnit(userFuturesPosition.getFuturesUnit());
        userFuturesPositionVO.setAllDepositAmt(userFuturesPosition.getAllDepositAmt());
        userFuturesPositionVO.setOrderFee(userFuturesPosition.getOrderFee());
        userFuturesPositionVO.setLockMsg(userFuturesPosition.getLockMsg());
        userFuturesPositionVO.setIsLock(userFuturesPosition.getIsLock());

        userFuturesPositionVO.setCoinCode(userFuturesPosition.getCoinCode());
        userFuturesPositionVO.setBuyRate(userFuturesPosition.getBuyRate());
        userFuturesPositionVO.setSellRate(userFuturesPosition.getSellRate());


        FuturesPositionProfitVO futuresPositionProfitVO = getFuturesPositionProfitVO(userFuturesPosition,futuresVO);

        userFuturesPositionVO.setAllProfitAndLose(futuresPositionProfitVO.getAllProfitAndLose());
        userFuturesPositionVO.setProfitAndLose(futuresPositionProfitVO.getProfitAndLose());

        userFuturesPositionVO.setNowPrice(futuresPositionProfitVO.getNowPrice());

        userFuturesPositionVO.setCoinRate(futuresPositionProfitVO.getCoinRate());
        userFuturesPositionVO.setEachPoint(eachPoint);

        return userFuturesPositionVO;
    }

    private AgentFuturesPositionVO assembleAgentFuturesPositionVO(UserFuturesPosition userFuturesPosition,FuturesVO futuresVO) {
        AgentFuturesPositionVO agentFuturesPositionVO = new AgentFuturesPositionVO();

        agentFuturesPositionVO.setId(userFuturesPosition.getId());
        agentFuturesPositionVO.setPositionType(userFuturesPosition.getPositionType());
        agentFuturesPositionVO.setPositionSn(userFuturesPosition.getPositionSn());
        agentFuturesPositionVO.setUserId(userFuturesPosition.getUserId());
        agentFuturesPositionVO.setRealName(userFuturesPosition.getRealName());
        agentFuturesPositionVO.setAgentId(userFuturesPosition.getAgentId());
        agentFuturesPositionVO.setFuturesName(userFuturesPosition.getFuturesName());
        agentFuturesPositionVO.setFuturesCode(userFuturesPosition.getFuturesCode());
        agentFuturesPositionVO.setFuturesGid(userFuturesPosition.getFuturesGid());
        agentFuturesPositionVO.setBuyOrderTime(userFuturesPosition.getBuyOrderTime());
        agentFuturesPositionVO.setBuyOrderPrice(userFuturesPosition.getBuyOrderPrice());
        agentFuturesPositionVO.setSellOrderTime(userFuturesPosition.getSellOrderTime());
        agentFuturesPositionVO.setSellOrderPrice(userFuturesPosition.getSellOrderPrice());
        agentFuturesPositionVO.setOrderDirection(userFuturesPosition.getOrderDirection());
        agentFuturesPositionVO.setOrderNum(userFuturesPosition.getOrderNum());
        agentFuturesPositionVO.setFuturesStandard(userFuturesPosition.getFuturesStandard());
        agentFuturesPositionVO.setFuturesUnit(userFuturesPosition.getFuturesUnit());
        agentFuturesPositionVO.setAllDepositAmt(userFuturesPosition.getAllDepositAmt());
        agentFuturesPositionVO.setOrderFee(userFuturesPosition.getOrderFee());
        agentFuturesPositionVO.setLockMsg(userFuturesPosition.getLockMsg());
        agentFuturesPositionVO.setIsLock(userFuturesPosition.getIsLock());

        agentFuturesPositionVO.setCoinCode(userFuturesPosition.getCoinCode());
        agentFuturesPositionVO.setBuyRate(userFuturesPosition.getBuyRate());
        agentFuturesPositionVO.setSellRate(userFuturesPosition.getSellRate());

        FuturesPositionProfitVO futuresPositionProfitVO = getFuturesPositionProfitVO(userFuturesPosition,futuresVO);

        agentFuturesPositionVO.setAllProfitAndLose(futuresPositionProfitVO.getAllProfitAndLose());
        agentFuturesPositionVO.setProfitAndLose(futuresPositionProfitVO.getProfitAndLose());

        agentFuturesPositionVO.setNowPrice(futuresPositionProfitVO.getNowPrice());
        agentFuturesPositionVO.setCoinRate(futuresPositionProfitVO.getCoinRate());

        return agentFuturesPositionVO;
    }

    private AdminFuturesPositionVO assembleAdminFuturesPositionVO(UserFuturesPosition userFuturesPosition,FuturesVO futuresVO) {
        AdminFuturesPositionVO adminFuturesPositionVO = new AdminFuturesPositionVO();

        adminFuturesPositionVO.setId(userFuturesPosition.getId());
        adminFuturesPositionVO.setPositionType(userFuturesPosition.getPositionType());
        adminFuturesPositionVO.setPositionSn(userFuturesPosition.getPositionSn());
        adminFuturesPositionVO.setUserId(userFuturesPosition.getUserId());
        adminFuturesPositionVO.setRealName(userFuturesPosition.getRealName());
        adminFuturesPositionVO.setAgentId(userFuturesPosition.getAgentId());
        adminFuturesPositionVO.setFuturesName(userFuturesPosition.getFuturesName());
        adminFuturesPositionVO.setFuturesCode(userFuturesPosition.getFuturesCode());
        adminFuturesPositionVO.setFuturesGid(userFuturesPosition.getFuturesGid());
        adminFuturesPositionVO.setBuyOrderTime(userFuturesPosition.getBuyOrderTime());
        adminFuturesPositionVO.setBuyOrderPrice(userFuturesPosition.getBuyOrderPrice());
        adminFuturesPositionVO.setSellOrderTime(userFuturesPosition.getSellOrderTime());
        adminFuturesPositionVO.setSellOrderPrice(userFuturesPosition.getSellOrderPrice());
        adminFuturesPositionVO.setOrderDirection(userFuturesPosition.getOrderDirection());
        adminFuturesPositionVO.setOrderNum(userFuturesPosition.getOrderNum());
        adminFuturesPositionVO.setFuturesStandard(userFuturesPosition.getFuturesStandard());
        adminFuturesPositionVO.setFuturesUnit(userFuturesPosition.getFuturesUnit());
        adminFuturesPositionVO.setAllDepositAmt(userFuturesPosition.getAllDepositAmt());
        adminFuturesPositionVO.setOrderFee(userFuturesPosition.getOrderFee());
        adminFuturesPositionVO.setLockMsg(userFuturesPosition.getLockMsg());
        adminFuturesPositionVO.setIsLock(userFuturesPosition.getIsLock());

        adminFuturesPositionVO.setCoinCode(userFuturesPosition.getCoinCode());
        adminFuturesPositionVO.setBuyRate(userFuturesPosition.getBuyRate());
        adminFuturesPositionVO.setSellRate(userFuturesPosition.getSellRate());


        FuturesPositionProfitVO futuresPositionProfitVO = getFuturesPositionProfitVO(userFuturesPosition,futuresVO);

        adminFuturesPositionVO.setAllProfitAndLose(futuresPositionProfitVO.getAllProfitAndLose());
        adminFuturesPositionVO.setProfitAndLose(futuresPositionProfitVO.getProfitAndLose());

        adminFuturesPositionVO.setNowPrice(futuresPositionProfitVO.getNowPrice());

        adminFuturesPositionVO.setCoinRate(futuresPositionProfitVO.getCoinRate());

        return adminFuturesPositionVO;
    }


    //updated edison
    private FuturesPositionProfitVO getFuturesPositionProfitVO(UserFuturesPosition userFuturesPosition,FuturesVO futuresVO) {
        BigDecimal profitAndLose = new BigDecimal("0");
        BigDecimal allProfitAndLose = new BigDecimal("0");
        String nowPrice = "";
        BigDecimal coinRate = new BigDecimal("0");

        //每手浮动价格
        StockFutures stockFutures = stockFuturesMapper.selectFuturesByCode(userFuturesPosition.getFuturesCode());
        BigDecimal eachPoint = new BigDecimal("1");
        if(stockFutures!=null && stockFutures.getEachPoint() != null){
            eachPoint = stockFutures.getEachPoint();
        }

        if (userFuturesPosition.getSellOrderPrice() != null) {

            BigDecimal subPrice = userFuturesPosition.getSellOrderPrice().subtract(userFuturesPosition.getBuyOrderPrice());

            profitAndLose = subPrice.multiply(eachPoint).multiply(new BigDecimal(userFuturesPosition.getOrderNum().intValue()));
            if ("看跌".equals(userFuturesPosition.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            allProfitAndLose = profitAndLose.subtract(userFuturesPosition.getOrderFee());

            coinRate = userFuturesPosition.getSellRate();

        } else {

            //FuturesVO futuresVO = this.iStockFuturesService.querySingleMarket(userFuturesPosition.getFuturesGid());
            nowPrice = futuresVO.getNowPrice();

            BigDecimal subPrice = (new BigDecimal(nowPrice)).subtract(userFuturesPosition.getBuyOrderPrice());

            profitAndLose = subPrice.multiply(eachPoint).multiply(new BigDecimal(userFuturesPosition.getOrderNum().intValue()));
            if ("bearish".equals(userFuturesPosition.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            allProfitAndLose = profitAndLose.subtract(userFuturesPosition.getOrderFee());


            ServerResponse serverResponse = this.iStockFuturesService.getExchangeRate(userFuturesPosition.getCoinCode());
            if (serverResponse.isSuccess()) {
                coinRate = (BigDecimal) serverResponse.getData();
            }
        }


        FuturesPositionProfitVO futuresPositionProfitVO = new FuturesPositionProfitVO();
        futuresPositionProfitVO.setProfitAndLose(profitAndLose);
        futuresPositionProfitVO.setAllProfitAndLose(allProfitAndLose);
        futuresPositionProfitVO.setNowPrice(nowPrice);
        futuresPositionProfitVO.setCoinRate(coinRate);

        return futuresPositionProfitVO;
    }

    public FuturesVO setFuturesVO(FuturesVO futuresVO,String[] sh01_arr){
        if(sh01_arr.length>0){
            futuresVO.setNowPrice(sh01_arr[0]);

            futuresVO.setLastClose(sh01_arr[1]);

            futuresVO.setBuyPrice(sh01_arr[2]);

            futuresVO.setSellPrice(sh01_arr[3]);

            futuresVO.setMaxPrice(sh01_arr[4]);

            futuresVO.setMinPrice(sh01_arr[5]);

            futuresVO.setHourTime(sh01_arr[6]);

            futuresVO.setTodayOpen(sh01_arr[7]);

            futuresVO.setExtra1(sh01_arr[8]);

            futuresVO.setExtra2(sh01_arr[9]);

            futuresVO.setExtra3(sh01_arr[10]);

            futuresVO.setDayTime(sh01_arr[11]);

            futuresVO.setName(sh01_arr[12]);
        }
        return  futuresVO;
    }

}
