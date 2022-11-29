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
     * 创建支付訂單
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
        String username = request.getParameter("username");  //持卡人姓名
        String card = request.getParameter("card"); //金融卡后四位
        if (org.springframework.util.StringUtils.isEmpty(amount) || org.springframework.util.StringUtils.isEmpty(productId) || org.springframework.util.StringUtils.isEmpty(mchOrderNo)) {
            return "參數丢失！";
        }

        //金额转换為  分 為單位
        String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
        String localAddr = request.getLocalAddr();
        int serverPort = request.getServerPort();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mchId", mchId + "");  //商戶ID
        params.put("appId", appId);  //TODO 应用ID 請根据实际配置传参
        params.put("productId", productId);  //支付產品ID
        params.put("mchOrderNo", mchOrderNo);   //商戶訂單号
        params.put("currency", "USD");   //币种
        params.put("amount", amountParam);   //支付金额
//        params.put("clientIp", "210.73.10.148");   //客戶端IP
//        params.put("device", "ios10.3.1");   //客戶端设备
        params.put("returnUrl", returnUrl);   //支付结果前端跳转URL
        params.put("notifyUrl", serverIp + "/api/user/notify.do");   //支付结果后台回调URL
        //params.put("notifyUrl", "http://pq1576621225.e1.luyouxia.net:28616/stock2c1_war/api/user/notify.do");   //支付结果后台回调URL
        params.put("subject", "网络购物");  //商品主题
        params.put("body", "网络购物");   //商品描述信息
        params.put("param1", username);   //扩展參數1
        params.put("param2", card);   //扩展參數2
        //params.put("extra", "");  //附加參數

        String sign = SignUtil.getSign(params, mchKey);  //签名
        params.put("sign", sign);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("params", new Gson().toJson(params));
        return HttpClientRequest.doPost(payHost + "/api/pay/create_order", paramsMap);

    }

    public ServerResponse checkInMoney(int maxOrder, Integer userId) {
        int count = this.userRechargeMapper.checkInMoney(0, userId);

        if (count >= maxOrder) {
            return ServerResponse.createByErrorMsg("一小時内只能发起" + maxOrder + "次入金");
        }
        return ServerResponse.createBySuccess();
    }


    public ServerResponse inMoney(String amt, String payType, String imgUrl, HttpServletRequest request) throws Exception {
//        if (StringUtils.isBlank(amt) || StringUtils.isBlank(payType)) {
       if (StringUtils.isBlank(amt)) {
            return ServerResponse.createByErrorMsg("參數不能為空");
        }

        SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();
        if (siteSetting == null) {
            return ServerResponse.createByErrorMsg("設置set未初始化");
        }
        if ((new BigDecimal(siteSetting.getChargeMinAmt() + "")).compareTo(new BigDecimal(amt)) == 1) {
            return ServerResponse.createByErrorMsg("充值金额不得低於" + siteSetting.getChargeMinAmt() + "元");
        }


        SiteInfo siteInfo = null;
        ServerResponse serverResponseInfo = this.iSiteInfoService.getInfo();
        if (serverResponseInfo.isSuccess()) {
            siteInfo = (SiteInfo) serverResponseInfo.getData();
            /*if (StringUtils.isBlank(siteInfo.getSiteHost()) ||
                    StringUtils.isBlank(siteInfo.getSiteEmailTo())) {
                return ServerResponse.createByErrorMsg("請先設置Host and ToEmail");
            }*/
        } else {
            return serverResponseInfo;
        }

        User user = this.iUserService.getCurrentRefreshUser(request);

        //取消买入需校验身份证
//        if (user.getIsActive().intValue() != 2) {
//            return ServerResponse.createByErrorMsg("未實名認證不能发起充值");
//        }


        ServerResponse serverResponse = checkInMoney(5, user.getId());
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
        return createOrder(user.getId(), 0, new BigDecimal(amt), payType, imgUrl);

        //三方支付
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

                log.info("用戶充值，保存redis token成功，redisSetExResult = {}", redisSetExResult);

            *//*SendHTMLMail.send(user, userRecharge, email_token, siteInfo
                    .getSiteHost(), siteInfo.getSiteEmailTo());
            log.info("用戶充值，發送審核邮件成功");*//*
                return ServerResponse.createBySuccess("创建支付訂單成功！",map);
            }
        }
        return ServerResponse.createByErrorMsg("创建支付訂單失敗");*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ServerResponse updateMoney(String orderSn, String imgUrl, HttpServletRequest request) {
        try {
            UserRecharge userRecharge = userRechargeMapper.findUserRechargeByOrderSn(orderSn);

            if (Objects.isNull(userRecharge)) {
                return ServerResponse.createByErrorMsg("未找到該訂單");
            }

            userRecharge.setImgUrl(imgUrl);
            if (userRechargeMapper.updateByPrimaryKeySelective(userRecharge) > 0) {
                return ServerResponse.createBySuccess();
            }
        } catch (Exception e) {
            log.error("上传汇款凭证异常:", e);
        }
        return ServerResponse.createByErrorMsg("上傳失敗");
    }

    public String notify(HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(request.getParameter("sign"))) {   //sign參數 不存在
            return "fail(sign not exists)";
        }

        String resSign = request.getParameter("sign");  //接口返回sign參數值

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

        String sign = SignUtil.getSign(paramsMap, mchKey);   //根据返回数据 和商戶key 生成sign

        //验签
        if (!resSign.equals(sign)) {
            return "fail(verify fail)";
        }

        //处理业务...
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
        return ServerResponse.createByErrorMsg("找不到充值訂單");
    }


    @Transactional
    public ServerResponse chargeSuccess(UserRecharge userRecharge) throws Exception {
        log.info("充值訂單 确认成功操作 id = {}", userRecharge.getId());

        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("訂單狀態不能重复修改");
        }


        User user = this.userMapper.selectByPrimaryKey(userRecharge.getUserId());
        if (user == null) {
            return ServerResponse.createByErrorMsg("用戶不存在");
        }
        BigDecimal userAmt_before = user.getUserAmt();
        BigDecimal enableAmt_before = user.getEnableAmt();
        user.setUserAmt(userAmt_before.add(userRecharge.getPayAmt()));
        user.setEnableAmt(enableAmt_before.add(userRecharge.getPayAmt()));
        int updateCount = this.userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            log.info("1.修改用戶資金成功");
        } else {
            return ServerResponse.createByErrorMsg("失敗，修改用戶資金失敗");
        }


        userRecharge.setOrderStatus(Integer.valueOf(1));
        userRecharge.setPayTime(new Date());
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            log.info("2.修改訂單狀態成功");
        } else {
            throw new Exception("2. 修改訂單狀態失敗!");
        }


            UserCashDetail ucd = new UserCashDetail();
        ucd.setAgentId(user.getAgentId());
        ucd.setAgentName(user.getAgentName());
        ucd.setUserId(user.getId());
        ucd.setUserName(user.getRealName());
        ucd.setDeType("用戶充值");
        ucd.setDeAmt(userRecharge.getPayAmt());
        ucd.setDeSummary("用戶充值成功，充值前总金额:" + userAmt_before + ",充值后总金额:" + user.getUserAmt() + ",充值前可用:" + enableAmt_before + ",充值后可用:" + user
                .getEnableAmt());

        ucd.setAddTime(new Date());
        ucd.setIsRead(Integer.valueOf(0));
        int insertCount = this.userCashDetailMapper.insert(ucd);
        if (insertCount > 0) {
            return ServerResponse.createBySuccessMsg("充值成功！");
        }
        return ServerResponse.createByErrorMsg("充值失敗");
    }


    public ServerResponse chargeFail(UserRecharge userRecharge) throws Exception {
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("訂單狀態不能重复修改");
        }

        userRecharge.setOrderStatus(Integer.valueOf(2));
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            return ServerResponse.createBySuccessMsg("訂單");
        }
        return ServerResponse.createByErrorMsg("修改出現异常");
    }


    public ServerResponse chargeCancel(UserRecharge userRecharge) throws Exception {
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("訂單狀態不能重复修改");
        }

        userRecharge.setOrderStatus(Integer.valueOf(3));
        int updateCCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCCount > 0) {
            return ServerResponse.createBySuccessMsg("訂單取消成功");
        }
        return ServerResponse.createByErrorMsg("訂單取消出現异常");
    }


    public ServerResponse<PageInfo> findUserChargeList(String payChannel, String orderStatus, HttpServletRequest request, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        User user = this.iUserService.getCurrentUser(request);


        List<UserRecharge> userRecharges = this.userRechargeMapper.findUserChargeList(user.getId(), payChannel, orderStatus);

        log.info("充值列表，增加用戶 {} ，payChannel = {} , orderStatus = {}， 数量 = {}", new Object[]{user.getId(), payChannel, orderStatus, userRecharges.size()});

        PageInfo pageInfo = new PageInfo(userRecharges);

        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<PageInfo> listByAgent(Integer agentId, String realName, String payChannel, Integer state, HttpServletRequest request, int pageNum, int pageSize) {
        AgentUser currentAgent = this.iAgentUserService.getCurrentAgent(request);


        if (agentId != null) {
            AgentUser agentUser = this.agentUserMapper.selectByPrimaryKey(agentId);
            if (agentUser.getParentId() != currentAgent.getId()) {
                return ServerResponse.createByErrorMsg("不能查询非下级代理記錄");
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
            return ServerResponse.createByErrorMsg("充值訂單不存在");
        }
        if (userRecharge.getOrderStatus().intValue() != 0) {
            return ServerResponse.createByErrorMsg("訂單狀態不是下單狀態不能更改");
        }


        if (state.intValue() == 1) {

            User user = this.userMapper.selectByPrimaryKey(userRecharge.getUserId());
            if (user == null) {
                return ServerResponse.createByErrorMsg("用戶不存在");
            }
            BigDecimal user_amt = user.getUserAmt().add(userRecharge.getPayAmt());
            log.info("管理员确认訂單成功，增加用戶 {} 总資金，原金额 = {} , 增加后 = {}", new Object[]{user.getId(), user.getUserAmt(), user_amt});
            user.setUserAmt(user_amt);
            BigDecimal user_enable_amt = user.getEnableAmt().add(userRecharge.getPayAmt());
            log.info("管理员确认訂單成功，增加用戶 {} 可用資金，原金额 = {} , 增加后 = {}", new Object[]{user.getId(), user.getEnableAmt(), user_enable_amt});
            user.setEnableAmt(user_enable_amt);

            int updateCount = this.userMapper.updateByPrimaryKeySelective(user);
            if (updateCount > 0) {
                log.info("修改用戶資金成功！");
            } else {
                log.error("修改用戶資金出错，抛出异常");
                throw new Exception("修改用戶資金出错，抛出异常");
            }
        }


        userRecharge.setOrderStatus(Integer.valueOf((state.intValue() == 1) ? 1 : 2));


        userRecharge.setPayTime(new Date());
        int updateCount = this.userRechargeMapper.updateByPrimaryKeySelective(userRecharge);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("修改訂單狀態成功！");
        }
        return ServerResponse.createByErrorMsg("修改訂單狀態失敗！");
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
            return ServerResponse.createByErrorMsg("id不能為空");
        }
        int updateCount = this.userRechargeMapper.deleteByPrimaryKey(cId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("刪除成功");
        }
        return ServerResponse.createByErrorMsg("刪除失敗");
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
