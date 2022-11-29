package com.xc.service.impl;


import com.xc.pojo.*;
import com.xc.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xc.common.ServerResponse;
import com.xc.dao.AgentUserMapper;
import com.xc.dao.UserCashDetailMapper;
import com.xc.dao.UserIndexPositionMapper;
import com.xc.dao.UserMapper;
import com.xc.service.*;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.KeyUtils;
import com.xc.utils.PropertiesUtil;
import com.xc.utils.redis.JsonUtil;
import com.xc.utils.stock.BuyAndSellUtils;
import com.xc.utils.stock.sina.SinaStockApi;
import com.xc.vo.agent.AgentIncomeVO;
import com.xc.vo.indexposition.AdminIndexPositionVO;
import com.xc.vo.indexposition.AgentIndexPositionVO;
import com.xc.vo.indexposition.IndexPositionProfitVO;
import com.xc.vo.indexposition.IndexPositionVO;
import com.xc.vo.indexposition.UserIndexPositionVO;
import com.xc.vo.position.UserPositionVO;
import com.xc.vo.stock.MarketVO;

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

@Service("iUserIndexPositionService")
public class UserIndexPositionServiceImpl implements IUserIndexPositionService {
    private static final Logger log = LoggerFactory.getLogger(UserIndexPositionServiceImpl.class);

    @Autowired
    UserIndexPositionMapper userIndexPositionMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    IStockIndexService iStockIndexService;

    @Autowired
    ISiteIndexSettingService iSiteIndexSettingService;

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserCashDetailMapper userCashDetailMapper;
    @Autowired
    IAgentUserService iAgentUserService;
    @Autowired
    AgentUserMapper agentUserMapper;
    @Autowired
    ISiteProductService iSiteProductService;

    @Transactional
    public ServerResponse buyIndex(Integer indexId, Integer buyNum, Integer buyType, Integer lever, HttpServletRequest request) throws Exception {
        if (indexId == null || buyNum == null || buyType == null) {
            return ServerResponse.createByErrorMsg("parameter cannot be empty");
        }

        User user = this.iUserService.getCurrentRefreshUser(request);
        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        if (siteProduct.getRealNameDisplay() && (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard()))) {
            return ServerResponse.createByErrorMsg("Failed to place an order, please verify your real name first");
        }

        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("No trading on weekends or holidays");
        }

        log.info("用戶 {} 下單, 指数id = {} ，数量 = {} 手 , 方向 = {} ， 杠杆 = {}", new Object[]{user
                .getId(), indexId, buyNum, buyType, lever});

        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Order failed, account has been locked");
        }


        SiteIndexSetting siteIndexSetting = this.iSiteIndexSettingService.getSiteIndexSetting();
        if (siteIndexSetting == null) {
            log.error("下單出错，指数設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place order, system setting error");
        }


        String am_begin = siteIndexSetting.getTransAmBegin();
        String am_end = siteIndexSetting.getTransAmEnd();
        String pm_begin = siteIndexSetting.getTransPmBegin();
        String pm_end = siteIndexSetting.getTransPmEnd();
        boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
        boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
        log.info("是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
        if (!am_flag && !pm_flag) {
            return ServerResponse.createByErrorMsg("Failed to place order, not during trading hours");
        }


        StockIndex stockIndex = this.iStockIndexService.selectIndexById(indexId);
        if (stockIndex == null) {
            return ServerResponse.createByErrorMsg("index does not exist");
        }
        if (1 != stockIndex.getTransState().intValue()) {
            return ServerResponse.createByErrorMsg("The index cannot be traded");
        }

        //保证金= 指数保证金*数量/杠杆倍数
        BigDecimal all_deposit_amt = (new BigDecimal(stockIndex.getDepositAmt().intValue())).multiply(new BigDecimal(buyNum.intValue())).divide(new BigDecimal(lever)).setScale(4,2);

        if (user.getEnableIndexAmt().compareTo(all_deposit_amt) == -1) {
            return ServerResponse.createByErrorMsg("Insufficient funds in the index account");
        }

        BigDecimal max_buy_amt = user.getEnableIndexAmt().multiply(siteIndexSetting.getBuyMaxPercent());
        if (max_buy_amt.compareTo(all_deposit_amt) == -1) {
            return ServerResponse.createByErrorMsg("Cannot exceed the maximum purchase ratio");
        }
        if (user.getUserAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Cannot exceed the maximum purchase ratio");
        }
        if (user.getUserFutAmt().compareTo(new BigDecimal("0")) == -1) {
            return ServerResponse.createByErrorMsg("Failed, total futures capital is less than 0");
        }


        MarketVO marketVO = this.iStockIndexService.querySingleIndex(stockIndex.getIndexGid());
        log.info("當前指数行情 = {}", JsonUtil.obj2StringPretty(marketVO));


        BigDecimal increaseRate = new BigDecimal(marketVO.getIncreaseRate());
        if (increaseRate.compareTo(new BigDecimal("0")) == 1) {

            if (siteIndexSetting.getRiseLimit().multiply(new BigDecimal("100"))
                    .compareTo(increaseRate) == -1 && buyType
                    .intValue() == 0) {
                return ServerResponse.createByErrorMsg("Current Index Gains:" + increaseRate + ",can't be bullish");
            }
        } else {

            increaseRate = increaseRate.negate();
            if (siteIndexSetting.getRiseLimit().multiply(new BigDecimal("100"))
                    .compareTo(increaseRate) == -1 && buyType
                    .intValue() == 1) {
                return ServerResponse.createByErrorMsg("Current Index Decline:" + increaseRate + ",can't be bearish");
            }
        }


        BigDecimal reckon_enable = user.getEnableIndexAmt().subtract(all_deposit_amt);
        user.setEnableIndexAmt(reckon_enable);
        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶交易指数下單】修改用戶金额成功");
        } else {
            log.error("用戶交易指数下單】修改用戶金额出错");
            throw new Exception("User trading index order] Error in modifying user amount");
        }


        UserIndexPosition userIndexPosition = new UserIndexPosition();
        userIndexPosition.setPositionType(user.getAccountType());
        userIndexPosition.setPositionSn(KeyUtils.getUniqueKey());
        userIndexPosition.setUserId(user.getId());
        userIndexPosition.setRealName(user.getRealName());
        userIndexPosition.setAgentId(user.getAgentId());
        userIndexPosition.setIndexName(stockIndex.getIndexName());
        userIndexPosition.setIndexCode(stockIndex.getIndexCode());
        userIndexPosition.setIndexGid(stockIndex.getIndexGid());
        userIndexPosition.setBuyOrderTime(new Date());
        userIndexPosition.setBuyOrderPrice(new BigDecimal(marketVO.getNowPrice()));
        userIndexPosition.setOrderDirection((buyType.intValue() == 0) ? "bullish" : "bearish");

        userIndexPosition.setOrderNum(buyNum);


        userIndexPosition.setIsLock(Integer.valueOf(0));

        userIndexPosition.setAllDepositAmt(all_deposit_amt);
        userIndexPosition.setOrderFee((new BigDecimal(stockIndex.getTransFee().intValue()))
                .multiply(new BigDecimal(buyNum.intValue())));
        userIndexPosition.setOrderStayDays(Integer.valueOf(0));
        userIndexPosition.setEachPoint(new BigDecimal(stockIndex.getEachPoint().intValue()));
        userIndexPosition.setProfitAndLose(new BigDecimal("0"));
        userIndexPosition.setAllProfitAndLose(new BigDecimal("0"));
        userIndexPosition.setOrderLever(lever);

        int insertPositionCount = this.userIndexPositionMapper.insert(userIndexPosition);
        if (insertPositionCount > 0) {
            log.info("【用戶交易指数下單】保存持倉記錄成功");
        } else {
            log.error("用戶交易指数下單】保存持倉記錄出错");
            throw new Exception("User trading index order] Error saving position record");
        }

        return ServerResponse.createBySuccess("successfully ordered");
    }

    @Override
    public ServerResponse del(Integer positionId) {
        if (positionId == null) {
            return ServerResponse.createByErrorMsg("id不能為空");
        }
        UserIndexPosition position = this.userIndexPositionMapper.selectByPrimaryKey(positionId);

        if (position == null) {
            ServerResponse.createByErrorMsg("该持倉不存在");
        }

        int updateCount = this.userIndexPositionMapper.deleteByPrimaryKey(positionId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("刪除成功");
        }
        return ServerResponse.createByErrorMsg("刪除失敗");
    }

    @Transactional
    public ServerResponse sellIndex(String positionSn, int doType) throws Exception {
        log.info("【用戶交易平倉指数】 positionSn = {} ， dotype = {}", positionSn, Integer.valueOf(doType));


        SiteIndexSetting siteIndexSetting = this.iSiteIndexSettingService.getSiteIndexSetting();
        if (siteIndexSetting == null) {
            log.error("平倉出错，网站指数設置表不存在");
            return ServerResponse.createByErrorMsg("Failed to place order, system setting error");
        }


        if (doType != 0) {
            String am_begin = siteIndexSetting.getTransAmBegin();
            String am_end = siteIndexSetting.getTransAmEnd();
            String pm_begin = siteIndexSetting.getTransPmBegin();
            String pm_end = siteIndexSetting.getTransPmEnd();
            boolean am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
            boolean pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
            log.info("【指数】是否在上午交易時间 = {} 是否在下午交易時间 = {}", Boolean.valueOf(am_flag), Boolean.valueOf(pm_flag));
            if (!am_flag && !pm_flag) {
                return ServerResponse.createByErrorMsg("Failed to close the position, not within the trading hours");
            }
        }


        UserIndexPosition userIndexPosition = this.userIndexPositionMapper.selectIndexPositionBySn(positionSn);
        if (userIndexPosition == null) {
            return ServerResponse.createByErrorMsg("Failed to close the position, the index position does not exist");
        }


        User user = this.userMapper.selectByPrimaryKey(userIndexPosition.getUserId());
        /*實名認證开关开启*/
        SiteProduct siteProduct = iSiteProductService.getProductSetting();
        if (siteProduct.getRealNameDisplay() && user.getIsLock().intValue() == 1) {
            return ServerResponse.createByErrorMsg("Closing failed, the user has been locked");
        }
        if(siteProduct.getHolidayDisplay()){
            return ServerResponse.createByErrorMsg("No trading on weekends or holidays");
        }
        if (userIndexPosition.getSellOrderPrice() != null) {
            return ServerResponse.createByErrorMsg("Closing failed, this order has been closed");
        }

        if (1 == userIndexPosition.getIsLock().intValue()) {
            return ServerResponse.createByErrorMsg("Closed position failed " + userIndexPosition.getLockMsg());
        }


        MarketVO marketVO = this.iStockIndexService.querySingleIndex(userIndexPosition.getIndexGid());
        log.info("當前指数行情 = {}", JsonUtil.obj2StringPretty(marketVO));

        BigDecimal increaseRate = new BigDecimal(marketVO.getIncreaseRate());
        if (increaseRate.compareTo(new BigDecimal("0")) == 1) {

            if (siteIndexSetting.getRiseLimit().multiply(new BigDecimal("100"))
                    .compareTo(increaseRate) != 1 && "bearish"
                    .equals(userIndexPosition.getOrderDirection())) {
                return ServerResponse.createByErrorMsg("Current Index Gains:" + increaseRate + ",can't sell");
            }
        } else {

            increaseRate = increaseRate.negate();
            if (siteIndexSetting.getRiseLimit().multiply(new BigDecimal("100"))
                    .compareTo(increaseRate) == -1 && "bullish"
                    .equals(userIndexPosition.getOrderDirection())) {
                return ServerResponse.createByErrorMsg("Current Index Decline:" + increaseRate + ",can't sell");
            }
        }


        userIndexPosition.setSellOrderPrice(new BigDecimal(marketVO.getNowPrice()));
        userIndexPosition.setSellOrderTime(new Date());

        BigDecimal point_sub = userIndexPosition.getSellOrderPrice().subtract(userIndexPosition.getBuyOrderPrice());

        BigDecimal profit_and_lose = point_sub.multiply(userIndexPosition.getEachPoint()).multiply(new BigDecimal(userIndexPosition.getOrderNum().intValue()));
        if ("看跌".equals(userIndexPosition.getOrderDirection())) {
            profit_and_lose = profit_and_lose.negate();
        }

        userIndexPosition.setProfitAndLose(profit_and_lose);

        BigDecimal all_profit = profit_and_lose.subtract(userIndexPosition.getOrderFee());
        userIndexPosition.setAllProfitAndLose(all_profit);


        int updateIndexPositionCount = this.userIndexPositionMapper.updateByPrimaryKeySelective(userIndexPosition);
        if (updateIndexPositionCount > 0) {
            log.info("【用戶平倉指数】修改浮动盈虧記錄成功");
        } else {
            log.error("【用戶平倉指数】修改浮动盈虧記錄出错");
            throw new Exception("User liquidation index] Error in modifying floating profit and loss record");
        }


        BigDecimal before_user_amt = user.getUserIndexAmt();
        BigDecimal before_enable_amt = user.getEnableIndexAmt();
        log.info("用戶平倉之前 的 总資金  = {} , 可用資金 = {}", before_user_amt, before_enable_amt);

        BigDecimal user_index_amt = before_user_amt.add(all_profit);

        BigDecimal enable_index_amt = before_enable_amt.add(all_profit).add(userIndexPosition.getAllDepositAmt());

        log.info("用戶平倉后的总資金  = {} , 可用資金 = {}", user_index_amt, enable_index_amt);
        user.setUserIndexAmt(user_index_amt);
        user.setEnableIndexAmt(enable_index_amt);

        int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateUserCount > 0) {
            log.info("【用戶平倉指数】修改用戶金额成功");
        } else {
            log.error("【用戶平倉指数】修改用戶金额出错");
            throw new Exception("【User Closing Index】Error in modifying user amount");
        }

        UserCashDetail ucd = new UserCashDetail();
        ucd.setPositionId(userIndexPosition.getId());
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("Index profit and loss");
        ucd.setDeAmt(all_profit);
        ucd.setDeSummary("sell index，" + userIndexPosition.getIndexName() + "/" + userIndexPosition
                .getIndexCode() + "，total profit and loss：" + all_profit);


        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));

        int insertSxfCount = this.userCashDetailMapper.insert(ucd);
        if (insertSxfCount > 0) {
            log.info("【用戶平倉指数】保存明细記錄成功");
        } else {
            log.error("【用戶平倉指数】保存明细記錄出错");
            throw new Exception("【User Closing Index】Error saving detailed records");
        }

        return ServerResponse.createBySuccessMsg("Closed position successfully");
    }


    public ServerResponse lock(Integer positionId, Integer state, String lockMsg) {
        if (positionId == null || state == null) {
            return ServerResponse.createByErrorMsg("參數不能為空");
        }


        UserIndexPosition userIndexPosition = this.userIndexPositionMapper.selectByPrimaryKey(positionId);
        if (userIndexPosition == null) {
            return ServerResponse.createByErrorMsg("持倉不存在");
        }

        if (userIndexPosition.getSellOrderPrice() != null) {
            return ServerResponse.createByErrorMsg("平倉單不能锁倉");
        }

        if (state.intValue() == 1 &&
                StringUtils.isBlank(lockMsg)) {
            return ServerResponse.createByErrorMsg("锁倉提示信息必填");
        }


        if (state.intValue() == 1) {
            userIndexPosition.setIsLock(Integer.valueOf(1));
            userIndexPosition.setLockMsg(lockMsg);
        } else {
            userIndexPosition.setIsLock(Integer.valueOf(0));
        }

        int updateCount = this.userIndexPositionMapper.updateByPrimaryKeySelective(userIndexPosition);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
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

        List<UserIndexPosition> userIndexPositions = this.userIndexPositionMapper.listByAgent(positionType, state, userId, searchId, positionSn, begin_time, end_time);


        List<AgentIndexPositionVO> agentIndexPositionVOS = Lists.newArrayList();
        for (UserIndexPosition userIndexPosition : userIndexPositions) {
            AgentIndexPositionVO agentIndexPositionVO = assembleAgentIndexPositionVO(userIndexPosition);
            agentIndexPositionVOS.add(agentIndexPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userIndexPositions);
        pageInfo.setList(agentIndexPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private AgentIndexPositionVO assembleAgentIndexPositionVO(UserIndexPosition userIndexPosition) {
        AgentIndexPositionVO agentIndexPositionVO = new AgentIndexPositionVO();

        agentIndexPositionVO.setId(userIndexPosition.getId());
        agentIndexPositionVO.setPositionSn(userIndexPosition.getPositionSn());
        agentIndexPositionVO.setPositionType(userIndexPosition.getPositionType());
        agentIndexPositionVO.setUserId(userIndexPosition.getUserId());
        agentIndexPositionVO.setRealName(userIndexPosition.getRealName());
        agentIndexPositionVO.setAgentId(userIndexPosition.getAgentId());
        agentIndexPositionVO.setIndexName(userIndexPosition.getIndexName());
        agentIndexPositionVO.setIndexCode(userIndexPosition.getIndexCode());
        agentIndexPositionVO.setIndexGid(userIndexPosition.getIndexGid());
        agentIndexPositionVO.setBuyOrderTime(userIndexPosition.getBuyOrderTime());
        agentIndexPositionVO.setBuyOrderPrice(userIndexPosition.getBuyOrderPrice());

        agentIndexPositionVO.setSellOrderTime(userIndexPosition.getSellOrderTime());
        agentIndexPositionVO.setSellOrderPrice(userIndexPosition.getSellOrderPrice());
        agentIndexPositionVO.setOrderDirection(userIndexPosition.getOrderDirection());
        agentIndexPositionVO.setOrderNum(userIndexPosition.getOrderNum());
        agentIndexPositionVO.setAllDepositAmt(userIndexPosition.getAllDepositAmt());
        agentIndexPositionVO.setOrderFee(userIndexPosition.getOrderFee());
        agentIndexPositionVO.setOrderStayDays(userIndexPosition.getOrderNum());
        agentIndexPositionVO.setEachPoint(userIndexPosition.getEachPoint());
        agentIndexPositionVO.setIsLock(userIndexPosition.getIsLock());
        agentIndexPositionVO.setLockMsg(userIndexPosition.getLockMsg());


        IndexPositionProfitVO indexPositionProfitVO = getIndexPositionProfitVO(userIndexPosition);
        agentIndexPositionVO.setProfitAndLose(indexPositionProfitVO.getProfitAndLose());
        agentIndexPositionVO.setAllProfitAndLose(indexPositionProfitVO.getAllProfitAndLose());
        agentIndexPositionVO.setNow_price(indexPositionProfitVO.getNowPrice());

        return agentIndexPositionVO;
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


        List<UserIndexPosition> userIndexPositions = this.userIndexPositionMapper.listByAdmin(positionType, state, userId, agentId, positionSn, begin_time, end_time);


        List<AdminIndexPositionVO> adminIndexPositionVOS = Lists.newArrayList();
        for (UserIndexPosition userIndexPosition : userIndexPositions) {
            AdminIndexPositionVO adminIndexPositionVO = assembleAdminIndexPositionVO(userIndexPosition);
            adminIndexPositionVOS.add(adminIndexPositionVO);
        }

        PageInfo pageInfo = new PageInfo(userIndexPositions);
        pageInfo.setList(adminIndexPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse findMyIndexPositionByNameAndCode(String indexName, String indexCode, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        User user = this.iUserService.getCurrentUser(request);

        PageHelper.startPage(pageNum, pageSize);


        List<UserIndexPosition> userIndexPositions = this.userIndexPositionMapper.findMyIndexPositionByNameAndCode(user.getId(), indexName, indexCode, state);

        List<UserIndexPositionVO> userIndexPositionVOS = Lists.newArrayList();
        if (userIndexPositions.size() > 0) {
            for (UserIndexPosition userIndexPosition : userIndexPositions) {
                UserIndexPositionVO userIndexPositionVO = assembleUserIndexPositionVO(userIndexPosition);
                userIndexPositionVOS.add(userIndexPositionVO);
            }
        }
        PageInfo pageInfo = new PageInfo(userIndexPositions);
        pageInfo.setList(userIndexPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    /*根据指数代码查询用戶最早入倉股票*/
    public ServerResponse findUserIndexPositionByCode(HttpServletRequest request, String indexGid) {
        User user = this.iUserService.getCurrentRefreshUser(request);
        UserIndexPosition position = this.userIndexPositionMapper.findUserIndexPositionByCode(user.getId(), indexGid);

        List<UserIndexPositionVO> userPositionVOS = Lists.newArrayList();
        UserIndexPositionVO userPositionVO = null;
        if(position != null){
            userPositionVO = assembleUserIndexPositionVO(position);
        }
        userPositionVOS.add(userPositionVO);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(userPositionVOS);

        return ServerResponse.createBySuccess(pageInfo);
    }

    private UserIndexPositionVO assembleUserIndexPositionVO(UserIndexPosition userIndexPosition) {
        UserIndexPositionVO userIndexPositionVO = new UserIndexPositionVO();

        userIndexPositionVO.setId(userIndexPosition.getId());
        userIndexPositionVO.setPositionSn(userIndexPosition.getPositionSn());
        userIndexPositionVO.setPositionType(userIndexPosition.getPositionType());
        userIndexPositionVO.setUserId(userIndexPosition.getUserId());
        userIndexPositionVO.setRealName(userIndexPosition.getRealName());
        userIndexPositionVO.setAgentId(userIndexPosition.getAgentId());
        userIndexPositionVO.setIndexName(userIndexPosition.getIndexName());
        userIndexPositionVO.setIndexCode(userIndexPosition.getIndexCode());
        userIndexPositionVO.setIndexGid(userIndexPosition.getIndexGid());
        userIndexPositionVO.setBuyOrderTime(userIndexPosition.getBuyOrderTime());
        userIndexPositionVO.setBuyOrderPrice(userIndexPosition.getBuyOrderPrice());
        userIndexPositionVO.setSellOrderTime(userIndexPosition.getSellOrderTime());
        userIndexPositionVO.setSellOrderPrice(userIndexPosition.getSellOrderPrice());
        userIndexPositionVO.setOrderDirection(userIndexPosition.getOrderDirection());
        userIndexPositionVO.setOrderNum(userIndexPosition.getOrderNum());
        userIndexPositionVO.setAllDepositAmt(userIndexPosition.getAllDepositAmt());
        userIndexPositionVO.setOrderFee(userIndexPosition.getOrderFee());
        userIndexPositionVO.setOrderStayDays(userIndexPosition.getOrderNum());
        userIndexPositionVO.setEachPoint(userIndexPosition.getEachPoint());
        userIndexPositionVO.setIsLock(userIndexPosition.getIsLock());
        userIndexPositionVO.setLockMsg(userIndexPosition.getLockMsg());


        IndexPositionProfitVO indexPositionProfitVO = getIndexPositionProfitVO(userIndexPosition);
        userIndexPositionVO.setProfitAndLose(indexPositionProfitVO.getProfitAndLose());
        userIndexPositionVO.setAllProfitAndLose(indexPositionProfitVO.getAllProfitAndLose());
        userIndexPositionVO.setNow_price(indexPositionProfitVO.getNowPrice());

        return userIndexPositionVO;
    }


    public IndexPositionVO findUserIndexPositionAllProfitAndLose(Integer userId) {
        List<UserIndexPosition> userIndexPositions = this.userIndexPositionMapper.findPositionByUserIdAndSellPriceIsNull(userId);

        BigDecimal allProfitAndLose = new BigDecimal("0");
        BigDecimal allFreezAmt = new BigDecimal("0");

        String queryString="";
        for(UserIndexPosition userIndexPosition : userIndexPositions){
            queryString+=userIndexPosition.getIndexGid()+",";
        }
        String[] httpResults = null;
        if(StringUtils.isNotBlank(queryString)){
            httpResults= SinaStockApi.getSinaStockList(queryString);
        }
        int i=0;
        for (UserIndexPosition userIndexPosition : userIndexPositions) {
            //單條股票的sina api数据
            String httpResult = httpResults[i].substring(httpResults[i].indexOf("\"") + 1, httpResults[i].lastIndexOf("\""));
            MarketVO marketVO = new MarketVO();
            if (StringUtils.isNotBlank(httpResult)) {
                if (httpResult.contains(",")) {
                    String[] sh01_arr = httpResult.split(",");
                    marketVO.setName(sh01_arr[0]);
                    marketVO.setNowPrice(sh01_arr[1]);
                    marketVO.setIncrease(sh01_arr[2]);
                    marketVO.setIncreaseRate(sh01_arr[3]);
                }
            }

            log.info("IndexPositionVO 當前指数行情 = {}", JsonUtil.obj2StringPretty(marketVO));

            BigDecimal nowPrice = new BigDecimal(marketVO.getNowPrice());


            if (nowPrice.compareTo(new BigDecimal("0")) != 0) {

                BigDecimal buyPrice = userIndexPosition.getBuyOrderPrice();
                BigDecimal subPrice = nowPrice.subtract(buyPrice);

                BigDecimal profit_and_lose = subPrice.multiply(userIndexPosition.getEachPoint()).multiply(new BigDecimal(userIndexPosition.getOrderNum().intValue()));
                if ("看跌".equals(userIndexPosition.getOrderDirection())) {
                    profit_and_lose = profit_and_lose.negate();
                }

                log.info("持倉指数 {} ,現价点数 = {} ,买入点数 = {} 差价 = {} 盈虧 = {}", new Object[]{userIndexPosition
                        .getIndexName(), nowPrice, buyPrice, subPrice, profit_and_lose});


                BigDecimal position_profit = profit_and_lose.subtract(userIndexPosition.getOrderFee());


                allProfitAndLose = allProfitAndLose.add(position_profit);


                BigDecimal position_freez = userIndexPosition.getAllDepositAmt();
                allFreezAmt = allFreezAmt.add(position_freez);
                continue;
            }
            log.info("查询所有持倉單的總盈虧，現价返回0，當前為集合竞价");
        }


        IndexPositionVO indexPositionVO = new IndexPositionVO();
        indexPositionVO.setAllIndexProfitAndLose(allProfitAndLose);
        indexPositionVO.setAllIndexFreezAmt(allFreezAmt);

        return indexPositionVO;
    }


    public List<Integer> findDistinctUserIdList() {
        return this.userIndexPositionMapper.findDistinctUserIdList();
    }


    public List<UserIndexPosition> findIndexPositionByUserIdAndSellPriceIsNull(Integer userId) {
        return this.userIndexPositionMapper.findPositionByUserIdAndSellPriceIsNull(userId);
    }


    public ServerResponse getIndexIncome(Integer agentId, Integer positionType, String beginTime, String endTime) {
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


        List<UserIndexPosition> userIndexPositions = this.userIndexPositionMapper.listByAdmin(positionType, Integer.valueOf(1), null, agentId, null, begin_time, end_time);


        BigDecimal order_fee_amt = new BigDecimal("0");
        BigDecimal order_profit_and_lose = new BigDecimal("0");
        BigDecimal order_profit_and_lose_all = new BigDecimal("0");

        for (UserIndexPosition userIndexPosition : userIndexPositions) {
            order_fee_amt = order_fee_amt.add(userIndexPosition.getOrderFee());

            order_profit_and_lose = order_profit_and_lose.add(userIndexPosition.getProfitAndLose());

            order_profit_and_lose_all = order_profit_and_lose_all.add(userIndexPosition.getAllProfitAndLose());
        }

        AgentIncomeVO agentIncomeVO = new AgentIncomeVO();
        agentIncomeVO.setOrderSize(Integer.valueOf(userIndexPositions.size()));
        agentIncomeVO.setOrderFeeAmt(order_fee_amt);
        agentIncomeVO.setOrderProfitAndLose(order_profit_and_lose);
        agentIncomeVO.setOrderAllAmt(order_profit_and_lose_all);

        return ServerResponse.createBySuccess(agentIncomeVO);
    }

    private AdminIndexPositionVO assembleAdminIndexPositionVO(UserIndexPosition userIndexPosition) {
        AdminIndexPositionVO adminIndexPositionVO = new AdminIndexPositionVO();

        adminIndexPositionVO.setId(userIndexPosition.getId());
        adminIndexPositionVO.setPositionSn(userIndexPosition.getPositionSn());
        adminIndexPositionVO.setPositionType(userIndexPosition.getPositionType());
        adminIndexPositionVO.setUserId(userIndexPosition.getUserId());
        adminIndexPositionVO.setRealName(userIndexPosition.getRealName());
        adminIndexPositionVO.setAgentId(userIndexPosition.getAgentId());
        adminIndexPositionVO.setIndexName(userIndexPosition.getIndexName());
        adminIndexPositionVO.setIndexCode(userIndexPosition.getIndexCode());
        adminIndexPositionVO.setIndexGid(userIndexPosition.getIndexGid());
        adminIndexPositionVO.setBuyOrderTime(userIndexPosition.getBuyOrderTime());
        adminIndexPositionVO.setBuyOrderPrice(userIndexPosition.getBuyOrderPrice());

        adminIndexPositionVO.setSellOrderTime(userIndexPosition.getSellOrderTime());
        adminIndexPositionVO.setSellOrderPrice(userIndexPosition.getSellOrderPrice());
        adminIndexPositionVO.setOrderDirection(userIndexPosition.getOrderDirection());
        adminIndexPositionVO.setOrderNum(userIndexPosition.getOrderNum());
        adminIndexPositionVO.setAllDepositAmt(userIndexPosition.getAllDepositAmt());
        adminIndexPositionVO.setOrderFee(userIndexPosition.getOrderFee());
        adminIndexPositionVO.setOrderStayDays(userIndexPosition.getOrderNum());
        adminIndexPositionVO.setEachPoint(userIndexPosition.getEachPoint());
        adminIndexPositionVO.setIsLock(userIndexPosition.getIsLock());
        adminIndexPositionVO.setLockMsg(userIndexPosition.getLockMsg());


        IndexPositionProfitVO indexPositionProfitVO = getIndexPositionProfitVO(userIndexPosition);
        adminIndexPositionVO.setProfitAndLose(indexPositionProfitVO.getProfitAndLose());
        adminIndexPositionVO.setAllProfitAndLose(indexPositionProfitVO.getAllProfitAndLose());
        adminIndexPositionVO.setNow_price(indexPositionProfitVO.getNowPrice());

        return adminIndexPositionVO;
    }


    private IndexPositionProfitVO getIndexPositionProfitVO(UserIndexPosition userIndexPosition) {
        BigDecimal profitAndLose = new BigDecimal("0");
        BigDecimal allProfitAndLose = new BigDecimal("0");
        String nowPrice = "";

        if (userIndexPosition.getSellOrderPrice() != null) {


            BigDecimal subPrice = userIndexPosition.getSellOrderPrice().subtract(userIndexPosition.getBuyOrderPrice());

            profitAndLose = subPrice.multiply(userIndexPosition.getEachPoint()).multiply(new BigDecimal(userIndexPosition.getOrderNum().intValue()));
            if ("bearish".equals(userIndexPosition.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            allProfitAndLose = profitAndLose.subtract(userIndexPosition.getOrderFee());
        } else {

            MarketVO marketVO = this.iStockIndexService.querySingleIndex(userIndexPosition.getIndexGid());


            nowPrice = marketVO.getNowPrice();


            BigDecimal subPrice = (new BigDecimal(nowPrice)).subtract(userIndexPosition.getBuyOrderPrice());

            profitAndLose = subPrice.multiply(userIndexPosition.getEachPoint()).multiply(new BigDecimal(userIndexPosition.getOrderNum().intValue()));
            if ("bearish".equals(userIndexPosition.getOrderDirection())) {
                profitAndLose = profitAndLose.negate();
            }

            allProfitAndLose = profitAndLose.subtract(userIndexPosition.getOrderFee());
        }
        IndexPositionProfitVO indexPositionProfitVO = new IndexPositionProfitVO();
        indexPositionProfitVO.setProfitAndLose(profitAndLose);
        indexPositionProfitVO.setAllProfitAndLose(allProfitAndLose);
        indexPositionProfitVO.setNowPrice(nowPrice);

        return indexPositionProfitVO;
    }
}
