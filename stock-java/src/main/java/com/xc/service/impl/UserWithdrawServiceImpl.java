package com.xc.service.impl;


import com.xc.pojo.*;
import com.xc.service.*;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;

import com.xc.common.ServerResponse;

import com.xc.dao.AgentUserMapper;

import com.xc.dao.UserMapper;

import com.xc.dao.UserWithdrawMapper;

import com.xc.service.*;
import com.xc.utils.MessageUtils;
import com.xc.utils.stock.WithDrawUtils;

import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


@Service("iUserWithdrawService")
public class UserWithdrawServiceImpl implements IUserWithdrawService {

    private static final Logger log = LoggerFactory.getLogger(UserWithdrawServiceImpl.class);


    @Autowired
    UserWithdrawMapper userWithdrawMapper;


    @Autowired
    IUserService iUserService;


    @Autowired
    UserMapper userMapper;


    @Autowired
    IAgentUserService iAgentUserService;

    @Autowired
    AgentUserMapper agentUserMapper;

    @Autowired
    IUserPositionService iUserPositionService;

    @Autowired
    IUserBankService iUserBankService;

    @Autowired
    ISiteSettingService iSiteSettingService;

    @Autowired
    ISiteProductService iSiteProductService;


    @Transactional
    public ServerResponse outMoney(String amt, String with_Pwd, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(amt)) {
            return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.amountNull"));
        }
        User user = this.iUserService.getCurrentRefreshUser(request);
        String w = user.getWithPwd();
        if (w == null) {
            w = "";
        }
        if (with_Pwd == null) {
            with_Pwd = "";
        }
        if (w.equals(with_Pwd)) {
            if (user.getIsLogin().intValue() == 1) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.accountLock"));
            }
            List<UserPosition> userPositions = this.iUserPositionService.findPositionByUserIdAndSellIdIsNull(user.getId());

            if (userPositions.size() > 0) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.existsPosition"));
            }

//            if (StringUtils.isBlank(user.getRealName()) || StringUtils.isBlank(user.getIdCard())) {
//
//                return ServerResponse.createByErrorMsg("未實名認證");
//
//            }

            UserBank userBank = this.iUserBankService.findUserBankByUserId(user.getId());

            if (userBank == null) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.noBank"));
            }

//            if (user.getAccountType().intValue() == 1) {
//                return ServerResponse.createByErrorMsg("模拟用戶不能出金");
//            }


            SiteSetting siteSetting = this.iSiteSettingService.getSiteSetting();

            if ((new BigDecimal(amt)).compareTo(new BigDecimal(siteSetting.getWithMinAmt().intValue())) == -1) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.minAmount") + siteSetting.getWithMinAmt() + " USD");
            }


            int with_time_begin = siteSetting.getWithTimeBegin().intValue();

            int with_time_end = siteSetting.getWithTimeEnd().intValue();

            SiteProduct siteProduct = iSiteProductService.getProductSetting();
            if (siteProduct.getHolidayDisplay()) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.holidays") );
            }

            if (!WithDrawUtils.checkIsWithTime(with_time_begin, with_time_end)) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.hour")+ " "+with_time_begin + " - " + with_time_end );
            }


//            BigDecimal index_user_amt = user.getUserIndexAmt();
//            if (index_user_amt.compareTo(new BigDecimal("0")) == -1) {
//                return ServerResponse.createByErrorMsg("指数資金不能小於0");
//            }
//            BigDecimal futures_user_amt = user.getUserFutAmt();
//            if (futures_user_amt.compareTo(new BigDecimal("0")) == -1) {
//                return ServerResponse.createByErrorMsg("期货資金不能小於0");
//            }
            BigDecimal enable_amt = user.getEnableAmt();
            int compareAmt = enable_amt.compareTo(new BigDecimal(amt));

            if (compareAmt == -1) {
                return ServerResponse.createByErrorMsg(MessageUtils.get("withdraw.fail.available"));
            }


            BigDecimal user_all_amt = user.getUserAmt();

            BigDecimal reckon_all_amt = user_all_amt.subtract(new BigDecimal(amt));

            BigDecimal reckon_enable_amt = enable_amt.subtract(new BigDecimal(amt));

            user.setUserAmt(reckon_all_amt);

            user.setEnableAmt(reckon_enable_amt);

            log.info("用戶提現{}，金额 = {},总資金 = {},可用資金 = {}", new Object[]{user.getId(), amt, user_all_amt, enable_amt});


            log.info("提現后，总金额={},可用資金={}", reckon_all_amt, reckon_enable_amt);

            int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);

            if (updateUserCount > 0) {

                log.info("修改用戶資金成功");

            } else {
                log.error("修改用戶資金失敗");
                throw new Exception("用戶提現，修改用戶資金失敗");
            }


            UserWithdraw userWithdraw = new UserWithdraw();

            userWithdraw.setUserId(user.getId());
            userWithdraw.setNickName(user.getRealName());
            userWithdraw.setAgentId(user.getAgentId());
            userWithdraw.setWithAmt(new BigDecimal(amt));
            userWithdraw.setApplyTime(new Date());
            userWithdraw.setWithName(user.getRealName());
            userWithdraw.setBankNo(userBank.getBankNo());
            userWithdraw.setBankName(userBank.getBankName());
            userWithdraw.setBankAddress(userBank.getBankAddress());
            userWithdraw.setWithStatus(Integer.valueOf(0));
            BigDecimal withfee = siteSetting.getWithFeePercent().multiply(new BigDecimal(amt)).add(new BigDecimal(siteSetting.getWithFeeSingle().intValue()));
            userWithdraw.setWithFee(withfee);
            int insertCount = this.userWithdrawMapper.insert(userWithdraw);
            if (insertCount > 0) {
                return ServerResponse.createBySuccessMsg(MessageUtils.get("withdraw.fail.success"));
            }
            log.error("保存提現記錄失敗");
            throw new Exception("用戶提現，保存提現記錄失敗");
        } else {
            return ServerResponse.createByErrorMsg("提現密碼不正確！！");
        }

    }


    public ServerResponse<PageInfo> findUserWithList(String withStatus, HttpServletRequest request, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);


        User user = this.iUserService.getCurrentUser(request);


        List<UserWithdraw> userWithdraws = this.userWithdrawMapper.findUserWithList(user.getId(), withStatus);


        PageInfo pageInfo = new PageInfo(userWithdraws);


        return ServerResponse.createBySuccess(pageInfo);

    }


    public ServerResponse userCancel(Integer withId) {

        if (withId == null) {

            return ServerResponse.createByErrorMsg("id不能為空");

        }


        UserWithdraw userWithdraw = this.userWithdrawMapper.selectByPrimaryKey(withId);

        if (userWithdraw == null) {

            return ServerResponse.createByErrorMsg("訂單不存在");

        }


        if (0 != userWithdraw.getWithStatus().intValue()) {

            return ServerResponse.createByErrorMsg("當前訂單不能取消");

        }


        userWithdraw.setWithStatus(Integer.valueOf(3));

        userWithdraw.setWithMsg("用戶取消出金");

        int updateCount = this.userWithdrawMapper.updateByPrimaryKeySelective(userWithdraw);

        if (updateCount > 0) {

            log.info("修改用戶提現訂單 {} 狀態成功", withId);


            User user = this.userMapper.selectByPrimaryKey(userWithdraw.getUserId());

            user.setUserAmt(user.getUserAmt().add(userWithdraw.getWithAmt()));

            user.setEnableAmt(user.getEnableAmt().add(userWithdraw.getWithAmt()));

            int updateUserCount = this.userMapper.updateByPrimaryKeySelective(user);

            if (updateUserCount > 0) {

                log.info("反还用戶資金，总 {} 可用 {}", user.getUserAmt(), user.getEnableAmt());

                return ServerResponse.createBySuccessMsg("取消成功");

            }

            return ServerResponse.createByErrorMsg("取消失敗");

        }


        log.info("修改用戶提現訂單 {} 狀態失敗", withId);

        return ServerResponse.createByErrorMsg("取消失敗");

    }


    public ServerResponse listByAgent(Integer agentId, String realName, Integer state, HttpServletRequest request, int pageNum, int pageSize) {

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


        List<UserWithdraw> userWithdraws = this.userWithdrawMapper.listByAgent(searchId, realName, state);


        PageInfo pageInfo = new PageInfo(userWithdraws);


        return ServerResponse.createBySuccess(pageInfo);

    }


    public ServerResponse<PageInfo> listByAdmin(Integer agentId, Integer userId, String realName, Integer state, String beginTime, String endTime, HttpServletRequest request, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);


        List<UserWithdraw> userWithdraws = this.userWithdrawMapper.listByAdmin(agentId, userId, realName, state, beginTime, endTime);


        PageInfo pageInfo = new PageInfo(userWithdraws);


        return ServerResponse.createBySuccess(pageInfo);

    }


    public ServerResponse updateState(Integer withId, Integer state, String authMsg) throws Exception {

        UserWithdraw userWithdraw = this.userWithdrawMapper.selectByPrimaryKey(withId);

        if (userWithdraw == null) {

            return ServerResponse.createByErrorMsg("提現訂單不存在");

        }


        if (userWithdraw.getWithStatus().intValue() != 0) {

            return ServerResponse.createByErrorMsg("提現訂單已处理，不要重复操作");

        }


        if (state.intValue() == 2 &&

                StringUtils.isBlank(authMsg)) {

            return ServerResponse.createByErrorMsg("失敗信息必填");

        }


        if (state.intValue() == 2) {


            User user = this.userMapper.selectByPrimaryKey(userWithdraw.getUserId());

            if (user == null) {

                return ServerResponse.createByErrorMsg("用戶不存在");

            }

            BigDecimal user_amt = user.getUserAmt().add(userWithdraw.getWithAmt());

            log.info("管理员确认提現訂單失敗，返还用戶 {} 总資金，原金额 = {} , 返还后 = {}", new Object[]{user.getId(), user.getUserAmt(), user_amt});

            user.setUserAmt(user_amt);

            BigDecimal user_enable_amt = user.getEnableAmt().add(userWithdraw.getWithAmt());

            log.info("管理员确认提現訂單失敗，返还用戶 {} 可用資金，原金额 = {} , 返还后 = {}", new Object[]{user.getId(), user.getEnableAmt(), user_enable_amt});

            user.setEnableAmt(user_enable_amt);


            int updateCount = this.userMapper.updateByPrimaryKeySelective(user);

            if (updateCount > 0) {

                log.info("提現失敗，返还用戶資金成功！");

            } else {

                log.error("返还用戶資金出错，抛出异常");

                throw new Exception("修改用戶資金出错，抛出异常");

            }


            userWithdraw.setWithMsg(authMsg);

        }


        userWithdraw.setWithStatus(Integer.valueOf((state.intValue() == 1) ? 1 : 2));


        userWithdraw.setTransTime(new Date());


        int updateCount = this.userWithdrawMapper.updateByPrimaryKeySelective(userWithdraw);

        if (updateCount > 0) {

            return ServerResponse.createBySuccessMsg("操作成功！");

        }

        return ServerResponse.createByErrorMsg("操作失敗！");

    }


    public int deleteByUserId(Integer userId) {
        return this.userWithdrawMapper.deleteByUserId(userId);
    }


    public BigDecimal CountSpWithSumAmtByState(Integer withState) {
        return this.userWithdrawMapper.CountSpWithSumAmtByState(withState);
    }

    public BigDecimal CountSpWithSumAmTodaytByState(Integer withState) {
        return this.userWithdrawMapper.CountSpWithSumAmTodaytByState(withState);
    }

    public ServerResponse deleteWithdraw(Integer withdrawId) {
        if (withdrawId == null) {
            return ServerResponse.createByErrorMsg("刪除id不能為空");
        }
        int updateCount = this.userWithdrawMapper.deleteByPrimaryKey(withdrawId);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("刪除成功");
        }
        return ServerResponse.createByErrorMsg("刪除失敗");
    }

}

