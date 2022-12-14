package com.xc.service.impl;


import com.google.gson.Gson;
import com.xc.dao.UserRechargeMapper;
import com.xc.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.dao.AgentUserMapper;
import com.xc.dao.UserCashDetailMapper;
import com.xc.dao.UserMapper;
import com.xc.pojo.AgentUser;
import com.xc.pojo.SiteInfo;
import com.xc.pojo.SiteSetting;
import com.xc.pojo.User;
import com.xc.pojo.UserCashDetail;
import com.xc.pojo.UserRecharge;
import com.xc.service.*;
import com.xc.utils.*;
import com.xc.utils.email.SendHTMLMail;
import com.xc.utils.pay.SignUtil;
import com.xc.utils.redis.RedisShardedPoolUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service("iUserRechargeService")
public class UserRechargeServiceImpl implements IUserRechargeService {
    private static final Logger log = LoggerFactory.getLogger(UserRechargeServiceImpl.class);

    @Autowired
    UserRechargeMapper userRechargeMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    IAgentUserService iAgentUserService;
    @Autowired
    AgentUserMapper agentUserMapper;
    @Autowired
    ISiteSettingService iSiteSettingService;
    @Autowired
    UserCashDetailMapper userCashDetailMapper;
    @Autowired
    ISiteInfoService iSiteInfoService;

    private String mchId = PropertiesUtil.getProperty("bank.pay.mchId");

    private String mchKey = PropertiesUtil.getProperty("bank.pay.mchKey");

    private String payHost = PropertiesUtil.getProperty("bank.pay.payHost");

    private String appId = PropertiesUtil.getProperty("bank.pay.appId");

    private String serverIp = PropertiesUtil.getProperty("server.ip");

    /**
     * ??????????????????
     *
     * @param request
     * @return
     * @throws IOException
     */
    public String createOrder(String mchOrderNo, HttpServletRequest request) throws Exception {

        String amount = request.getParameter("amt");
        String productId = request.getParameter("productId");
//        String mchOrderNo = request.getParameter("mchOrderNo");
        String returnUrl = request.getParameter("returnUrl");
        String username = request.getParameter("username");  //???????????????
        String card = request.getParameter("card"); //??????????????????
        if (org.springframework.util.StringUtils.isEmpty(amount) || org.springframework.util.StringUtils.isEmpty(productId) || org.springframework.util.StringUtils.isEmpty(mchOrderNo)) {
            return "???????????????";
        }

        //???????????????  ??? ?????????
        String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mchId", mchId + "");  //??????ID
        params.put("appId", appId);  //TODO ??????ID ???????????????????????????
        params.put("productId", productId);  //????????????ID
        params.put("mchOrderNo", mchOrderNo);   //???????????????
        params.put("currency", "USD");   //??????
        params.put("amount", amountParam);   //????????????
//        params.put("clientIp", "210.73.10.148");   //?????????IP
//        params.put("device", "ios10.3.1");   //???????????????
        params.put("returnUrl", returnUrl);   //????????????????????????URL
        params.put("notifyUrl", serverIp + "/api/user/notify.do");   //????????????????????????URL
        //params.put("notifyUrl", "http://pq1576621225.e1.luyouxia.net:28616/stock2c1_war/api/user/notify.do");   //????????????????????????URL
        params.put("subject", "????????????");  //????????????
        params.put("body", "????????????");   //??????????????????
        params.put("param1", username);   //????????????1
        params.put("param2", card);   //????????????2
        //params.put("extra", "");  //????????????

        String sign = SignUtil.getSign(params, mchKey);  //??????
        params.put("sign", sign);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("params", new Gson().toJson(params));
        return HttpClientRequest.doPost(payHost + "/api/pay/create_order", paramsMap);

    }

    public ServerResponse checkInMoney(int maxOrder, Integer userId) {
        int count = this.userRechargeMapper.checkInMoney(0, userId);

        if (count >= maxOrder) {
            return ServerResponse.createByErrorMsg("????????????????????????" + maxOrder + "?????????");
        }
        return ServerResponse.createBySuccess();
    }


    public ServerResponse inMoney(String amt, String payType, String imgUrl, HttpServletRequest request) throws Exception {
//        if (StringUtils.isBlank(amt) || StringUtils.isBlank(payType)) {
       if (StringUtils.isBlank(amt)) {
            return ServerResponse.createByErrorMsg("??????????????????");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            return ServerResponse.createByErrorMsg("??????set????????????");
        }
        if ((new BigDecimal(siteSetting.getChargeMinAmt() + "")).compareTo(new BigDecimal(amt)) == 1) {
            return ServerResponse.createByErrorMsg("????????????????????????" + siteSetting.getChargeMinAmt() + "???");
        }


        SiteInfo siteInfo = null;
        ServerResponse serverResponseInfo = this.iSiteInfoService.getInfo();
        if (serverResponseInfo.isSuccess()) {
            siteInfo = (SiteInfo) serverResponseInfo.getData();
            /*if (StringUtils.isBlank(siteInfo.getSiteHost()) ||
                    StringUtils.isBlank(siteInfo.getSiteEmailTo())) {
                return ServerResponse.createByErrorMsg("????????????Host and ToEmail");
            }*/
        } else {
            return serverResponseInfo;
        }

        User user = this.iUserService.getCurrentRefreshUser(request);

        //??????????????????????????????
//        if (user.getIsActive().intValue() != 2) {
//            return ServerResponse.createByErrorMsg("?????????????????????????????????");
//        }


        ServerResponse serverResponse = checkInMoney(5, user.getId());
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        return createOrder(user.getId(), 0, new BigDecimal(amt), payType, imgUrl);

        //????????????
        /*UserRecharge userRecharge = new UserRecharge();

        userRecharge.setUserId(user.getId());
        userRecharge.setNickName(user.getRealName());
        userRecharge.setAgentId(user.getAgentId());

        String ordersn = KeyUtils.getRechargeOrderSn();
        userRecharge.setOrderSn(ordersn);

        userRecharge.setPayChannel(payType);
        userRecharge.setPayAmt(new BigDecimal(amt));
        userRecharge.setOrderStatus(Integer.valueOf(0));
        userRecharge.setAddTime(new Date());
        String str=createOrder(ordersn,request);
        Map<String,Object> map=new Gson().fromJson(str,Map.class);
        map.put("orderSn",ordersn);

        if("SUCCESS".equals(map.get("retCode"))){
            int insertCount = this.userRechargeMapper.insert(userRecharge);
            if (insertCount > 0) {

                String email_token = KeyUtils.getUniqueKey();

                String redisSetExResult = RedisShardedPoolUtils.setEx(email_token, email_token, 300);

                log.info("?????????????????????redis token?????????redisSetExResult = {}", redisSetExResult);

            *//*SendHTMLMail.send(user, userRecharge, email_token, siteInfo
                    .getSiteHost(), siteInfo.getSiteEmailTo());
            log.info("???????????????????????????????????????");*//*
                return ServerResponse.createBySuccess("???????????????????????????",map);
            }
        }
        return ServerResponse.createByErrorMsg("????????????????????????");*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ServerResponse updateMoney(String orderSn, String imgUrl, HttpServletRequest request) {
        try {
            UserRecharge userRecharge = userRechargeMapper.findUserRechargeByOrderSn(orderSn);

            if (Objects.isNull(userRecharge)) {
                return ServerResponse.createByErrorMsg("??????????????????");
            }

            userRecharge.setImgUrl(imgUrl);
            if (userRechargeMapper.updateByPrimaryKeySelective(userRecharge) > 0) {
                return ServerResponse.createBySuccess();
            }
        } catch (Exception e) {
            log.error("????????????????????????:", e);
        }
        return ServerResponse.createByErrorMsg("????????????");
    }

    public String notify(HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(request.getParameter("sign"))) {   //sign?????? ?????????
            return "fail(sign not exists)";
        }

        String resSign = request.getParameter("sign");  //????????????sign?????????

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("income", request.getParameter("income"));
        paramsMap.put("payOrderId", request.getParameter("payOrderId"));
        paramsMap.put("mchId", request.getParameter("mchId"));
        paramsMap.put("appId", request.getParameter("appId"));
        paramsMap.put("productId", request.getParameter("productId"));
        paramsMap.put("mchOrderNo", request.getParameter("mchOrderNo"));
        paramsMap.put("amount", request.getParameter("amount"));
        paramsMap.put("status", request.getParameter("status"));
        paramsMap.put("channelOrderNo", request.getParameter("channelOrderNo"));
        paramsMap.put("channelAttach", request.getParameter("channelAttach"));
        paramsMap.put("param1", request.getParameter("param1"));
        paramsMap.put("param2", request.getParameter("param2"));
        paramsMap.put("paySuccTime", request.getParameter("paySuccTime"));
        paramsMap.put("backType", request.getParameter("backType"));

        String sign = SignUtil.getSign(paramsMap, mchKey);   //?????????????????? ?????????key ??????sign

        //??????
        if (!resSign.equals(sign)) {
            return "fail(verify fail)";
        }

        //????????????...
        ServerResponse response = updateState(request.getParameter("mchOrderNo"), 1);
        int status = response.getStatus();
        if (status == 0) {
            return "success";
        } else {
            return "fail";
        }
    }


    public ServerResponse findUserRechargeByOrderSn(String orderSn) {
        UserRecharge userRecharge = this.userRechargeMapper.findUserRechargeByOrderSn(orderSn);
        if (userRecharge != null) {
            return ServerResponse.createBySuccess(userRecharge);
        }
        return ServerResponse.createByErrorMsg("?????????????????????");
    }


    @Transactional
    public ServerResponse chargeSuccess(UserRecharge userRecharge) throws Exception {
        log.info("???????????? ?????????????????? id = {}", userRecharge.getId());

        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("??????????????????????????????");
        }


        User user = this.userMapper.selectByPrimaryKey(userRecharge.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMsg("???????????????");
        }
        BigDecimal userAmt_before = user.getUserAmt();
        BigDecimal enableAmt_before = user.getEnableAmt();
        user.setUserAmt(userAmt_before.add(userRecharge.getPayAmt()));
        user.setEnableAmt(enableAmt_before.add(userRecharge.getPayAmt()));
        int updateCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            log.info("1.????????????????????????");
        } else {
            return ServerResponse.createByErrorMsg("?????????????????????????????????");
        }


        userRecharge.setOrderStatus(Integer.valueOf(1));
        userRecharge.setPayTime(new Date());
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            log.info("2.????????????????????????");
        } else {
            throw new Exception("2. ????????????????????????!");
        }


            UserCashDetail ucd = new UserCashDetail();
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("????????????");
        ucd.setDeAmt(userRecharge.getPayAmt());
        ucd.setDeSummary("???????????????????????????????????????:" + userAmt_before + ",??????????????????:" + user.getUserAmt() + ",???????????????:" + enableAmt_before + ",???????????????:" + user
                .getEnableAmt());

        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));
        int insertCount = this.userCashDetailMapper.insert(ucd);
        if (insertCount > 0) {
            return ServerResponse.createBySuccessMsg("???????????????");
        }
        return ServerResponse.createByErrorMsg("????????????");
    }


    public ServerResponse chargeFail(UserRecharge userRecharge) throws Exception {
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("??????????????????????????????");
        }

        userRecharge.setOrderStatus(Integer.valueOf(2));
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            return ServerResponse.createBySuccessMsg("??????");
        }
        return ServerResponse.createByErrorMsg("??????????????????");
    }


    public ServerResponse chargeCancel(UserRecharge userRecharge) throws Exception {
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("??????????????????????????????");
        }

        userRecharge.setOrderStatus(Integer.valueOf(3));
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            return ServerResponse.createBySuccessMsg("??????????????????");
        }
        return ServerResponse.createByErrorMsg("????????????????????????");
    }


    public ServerResponse<PageInfo> findUserChargeList(String payChannel, String orderStatus, HttpServletRequest request, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        User user = this.iUserService.getCurrentUser(request);


        List<UserRecharge> userRecharges = this.userRechargeMapper.findUserChargeList(user.getId(), payChannel, orderStatus);

        log.info("??????????????????????????? {} ???payChannel = {} , orderStatus = {}??? ?????? = {}", new Object[]{user.getId(), payChannel, orderStatus, userRecharges.size()});

        PageInfo pageInfo = new PageInfo(userRecharges);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<PageInfo> listByAgent(Integer agentId, String realName, String payChannel, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        AgentUser currentAgent = this.iAgentUserService.getCurrentAgent(request);


        if (agentId != null) {
            AgentUser agentUser = this.agentUserMapper.selectByPrimaryKey(agentId);
            if (agentUser.getParentId() != currentAgent.getId()) {
                return ServerResponse.createByErrorMsg("?????????????????????????????????");
            }
        }
        Integer searchId = null;
        if (agentId == null) {
            searchId = currentAgent.getId();
        } else {
            searchId = agentId;
        }


        PageHelper.startPage(pageNum, pageSize);


        List<UserRecharge> userRecharges = this.userRechargeMapper.listByAgent(searchId, realName, payChannel, state);

        PageInfo pageInfo = new PageInfo(userRecharges);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse listByAdmin(Integer agentId, Integer userId, String realName, Integer state, String beginTime,
                                      String endTime, HttpServletRequest request, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Timestamp begin_time = null;
        if (StringUtils.isNotBlank(beginTime)) {
            begin_time = DateTimeUtil.searchStrToTimestamp(beginTime);
        }
        Timestamp end_time = null;
        if (StringUtils.isNotBlank(endTime)) {
            end_time = DateTimeUtil.searchStrToTimestamp(endTime);
        }
        List<UserRecharge> userRecharges = this.userRechargeMapper.listByAdmin(agentId, userId, realName, state, begin_time, end_time);
        PageInfo pageInfo = new PageInfo(userRecharges);
        return ServerResponse.createBySuccess(pageInfo);
    }


    @Transactional
    public ServerResponse updateState(String mchOrderNo, Integer state) throws Exception {
        UserRecharge userRecharge = this.userRechargeMapper.findUserRechargeByOrderSn(mchOrderNo);

        if (userRecharge == null) {
            return ServerResponse.createByErrorMsg("?????????????????????");
        }
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("??????????????????????????????????????????");
        }


        if (state.intValue() == 1) {

            User user = this.userMapper.selectByPrimaryKey(userRecharge.getUserId());
            if (user == null) {
                return ServerResponse.createByErrorMsg("???????????????");
            }
            BigDecimal user_amt = user.getUserAmt().add(userRecharge.getPayAmt());
            log.info("?????????????????????????????????????????? {} ????????????????????? = {} , ????????? = {}", new Object[]{user.getId(), user.getUserAmt(), user_amt});
            user.setUserAmt(user_amt);
            BigDecimal user_enable_amt = user.getEnableAmt().add(userRecharge.getPayAmt());
            log.info("?????????????????????????????????????????? {} ???????????????????????? = {} , ????????? = {}", new Object[]{user.getId(), user.getEnableAmt(), user_enable_amt});
            user.setEnableAmt(user_enable_amt);

            int updateCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateCount > 0) {
                log.info("???????????????????????????");
            } else {
                log.error("???????????????????????????????????????");
                throw new Exception("???????????????????????????????????????");
            }
        }


        userRecharge.setOrderStatus(Integer.valueOf((state.intValue() == 1) ? 1 : 2));


        userRecharge.setPayTime(new Date());
        int updateCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("???????????????????????????");
        }
        return ServerResponse.createByErrorMsg("???????????????????????????");
    }


    public ServerResponse createOrder(Integer userId, Integer state, Integer amt, String payChannel) {
        if (userId == null || state == null || amt == null) {
            return ServerResponse.createByErrorMsg("parameter cannot be empty");
        }

        User user = this.userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("User not found");
        }

        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setUserId(user.getId());
        userRecharge.setNickName(user.getRealName());
        userRecharge.setAgentId(user.getAgentId());

        String ordersn = KeyUtils.getRechargeOrderSn();
        userRecharge.setOrderSn(ordersn);

        userRecharge.setPayChannel(payChannel);
        userRecharge.setPayAmt(new BigDecimal(amt.intValue()));
        userRecharge.setAddTime(new Date());
        userRecharge.setPayTime(new Date());

        if (state.intValue() == 0) {
            userRecharge.setOrderStatus(Integer.valueOf(0));
        } else if (state.intValue() == 1) {
            userRecharge.setOrderSn(payChannel);
            userRecharge.setPayChannel("2");
            userRecharge.setOrderStatus(Integer.valueOf(1));

            user.setUserAmt(user.getUserAmt().add(new BigDecimal(amt.intValue())));
            user.setEnableAmt(user.getEnableAmt().add(new BigDecimal(amt.intValue())));
            this.userMapper.updateByPrimaryKeySelective(user);
        } else if (state.intValue() == 2) {
            userRecharge.setOrderStatus(Integer.valueOf(2));
        } else {
            return ServerResponse.createByErrorMsg("Incorrect order status");
        }

        int insertCount = this.userRechargeMapper.insert(userRecharge);
        if (insertCount > 0) {
            return ServerResponse.createBySuccessMsg("The order was generated successfully!");
        }
        return ServerResponse.createByErrorMsg("Failed to generate order, please try again");
    }

    public ServerResponse<Object> createOrder(Integer userId, Integer state, BigDecimal amt, String payChannel, String imgUrl) {
        if (userId == null || state == null || amt == null) {
            return ServerResponse.createByErrorMsg("parameter cannot be empty");
        }

        User user = this.userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("User not found");
        }

        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setUserId(user.getId());
        userRecharge.setNickName(user.getRealName());
        userRecharge.setAgentId(user.getAgentId());

        String ordersn = KeyUtils.getRechargeOrderSn();
        userRecharge.setOrderSn(ordersn);

        userRecharge.setPayChannel(payChannel);
        userRecharge.setPayAmt(amt);
        userRecharge.setAddTime(new Date());
        userRecharge.setPayTime(new Date());
        userRecharge.setImgUrl(imgUrl);

        if (state.intValue() == 0) {
            userRecharge.setOrderStatus(Integer.valueOf(0));
        } else if (state.intValue() == 1) {
            userRecharge.setOrderSn(payChannel);
            userRecharge.setPayChannel("2");
            userRecharge.setOrderStatus(Integer.valueOf(1));

            user.setUserAmt(user.getUserAmt().add(new BigDecimal(amt.intValue())));
            user.setEnableAmt(user.getEnableAmt().add(new BigDecimal(amt.intValue())));
            this.userMapper.updateByPrimaryKeySelective(user);
        } else if (state.intValue() == 2) {
            userRecharge.setOrderStatus(Integer.valueOf(2));
        } else {
            return ServerResponse.createByErrorMsg("Incorrect order status");
        }

        int insertCount = this.userRechargeMapper.insert(userRecharge);
        if (insertCount > 0) {
            return ServerResponse.createBySuccess("Order generated successfully", userRecharge.getOrderSn());
        }
        return ServerResponse.createByErrorMsg("Failed to generate order, please try again");
    }

    public ServerResponse del(Integer cId) {
        if (cId == null) {
            return ServerResponse.createByErrorMsg("id????????????");
        }
        int updateCount = this.userRechargeMapper.deleteByPrimaryKey(cId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("????????????");
        }
        return ServerResponse.createByErrorMsg("????????????");
    }


    public int deleteByUserId(Integer userId) {
        return this.userRechargeMapper.deleteByUserId(userId);
    }


    public BigDecimal CountChargeSumAmt(Integer chargeState) {
        return this.userRechargeMapper.CountChargeSumAmt(chargeState);
    }

    public BigDecimal CountTotalRechargeAmountByTime(Integer chargeState) {
        return this.userRechargeMapper.CountTotalRechargeAmountByTime(chargeState);
    }


}
