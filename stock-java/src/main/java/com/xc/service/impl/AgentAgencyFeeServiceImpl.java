package com.xc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.dao.AgentAgencyFeeMapper;
import com.xc.dao.AgentUserMapper;
import com.xc.dao.UserMapper;
import com.xc.dao.UserPositionMapper;
import com.xc.pojo.*;
import com.xc.service.IAgentAgencyFeeService;
import com.xc.service.IAgentUserService;
import com.xc.service.IUserPositionService;
import com.xc.utils.PropertiesUtil;
import com.xc.utils.redis.CookieUtils;
import com.xc.utils.redis.JsonUtil;
import com.xc.utils.redis.RedisShardedPoolUtils;
import com.xc.vo.agent.AgentAgencyFeeVO;
import com.xc.vo.agent.AgentSecondInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Service("IAgentAgencyFeeService")
public class AgentAgencyFeeServiceImpl implements IAgentAgencyFeeService {
    @Resource
    private AgentAgencyFeeMapper agentAgencyFeeMapper;

    @Autowired
    IAgentUserService iAgentUserService;

    @Autowired
    IUserPositionService iUserPositionService;

    @Autowired
    AgentUserMapper agentUserMapper;

    @Autowired
    UserPositionMapper userPositionMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public int insert(AgentAgencyFee agentAgencyFee) {
        int ret = 0;
        // valid
        if (agentAgencyFee == null) {
            return 0;
        }

        ret = agentAgencyFeeMapper.insert(agentAgencyFee);
        return ret;
    }

    @Override
    public int update(AgentAgencyFee agentAgencyFee) {
        int ret = agentAgencyFeeMapper.update(agentAgencyFee);
        return ret>0 ? ret: 0;
    }

    /*获取收费比例
    * feeType：费用類型：1、入倉手续费，2、平倉手续费，3、延递费(留倉费)，4、分红
    * */
    private  BigDecimal getScale(AgentUser agentUser, int feeType){
        BigDecimal scale = new BigDecimal(0);//遞延費比例
        if(feeType == 1 || feeType == 2){
            scale = agentUser.getPoundageScale();
        } else if(feeType == 3){
            scale = agentUser.getDeferredFeesScale();
        } else if(feeType == 4){
            scale = agentUser.getReceiveDividendsScale();
        }
        return scale;
    }

    /*
    * 代理费收入，考虑多级代理的问题
    * feeType：费用類型：1、入倉手续费，2、平倉手续费，3、延递费(留倉费)，4、分红
    * positionSn：入倉單号
    * 调用关键词：核算代理收入
    * */
    @Override
    public int AgencyFeeIncome(int feeType,String positionSn) {
        int ret = 0;
        int businessId = 0;
        int agentId = 0;
        int level = 0;//代理级别
        BigDecimal totalMoney = new BigDecimal(0);
        BigDecimal oneProfitMoney = new BigDecimal(0);//一级利润
        BigDecimal upProfitMoney = new BigDecimal(0);//上级利润
        BigDecimal selfProfitMoney = new BigDecimal(0);//上级利润
        BigDecimal downProfitMoney = new BigDecimal(0);//下级利润
        BigDecimal scale = new BigDecimal(0);//遞延費比例
        String remarks = "";
        int downAgentId = 0;
        AgentUser agentUser = null;
        UserPosition userPosition = null;
        userPosition = userPositionMapper.findPositionBySn(positionSn);

        //查询用戶的代理
        agentUser = agentUserMapper.findAgentByAgentId(userPosition.getAgentId());
        agentId = agentUser.getId();
        level = agentUser.getAgentLevel();
        businessId = userPosition.getId();
        if(feeType == 1){
            totalMoney = userPosition.getOrderFee();
            remarks = "【入倉收入】入倉手续费总额："+totalMoney+"，單号："+userPosition.getPositionSn();
        } else if(feeType == 2){
            //平倉浮动總盈虧负数，代理利润增加，否则减少
            totalMoney = userPosition.getOrderFee().multiply(new BigDecimal(-1));
            remarks = "【平倉收入】平倉手续费总额："+totalMoney+"，單号："+userPosition.getPositionSn();
        } else if(feeType == 3){
            //user_position总延递费
            totalMoney = userPosition.getOrderStayFee();
            remarks = "【遞延費收入】遞延費总额："+totalMoney+"，單号："+userPosition.getPositionSn();
        }else if(feeType == 4){
            //亏损
            totalMoney = userPosition.getProfitAndLose().multiply(new BigDecimal(-1));
            //totalMoney = userPosition.getAllProfitAndLose().multiply(new BigDecimal(-1));
            remarks = "【分红收入】分红总额："+totalMoney+"，單号："+userPosition.getPositionSn();
        }
        //金额為0不计算分红直接跳出分红收入】分红总额："
//        if(totalMoney.compareTo(new BigDecimal(0))<=0){
//            return -1;
//        }
        //模拟用戶下單不计算分红
        User user = userMapper.selectByPrimaryKey(userPosition.getUserId());
        if(user.getAccountType() != 0 || user.getIsLock() != 0 || user.getIsActive() != 2){
            return -2;
        }
        //代理的上面代理列表
        List<AgentUser> agentlist = iAgentUserService.getAgentSuperiorList(agentUser.getId());
        if(agentlist != null && agentlist.size()>0){
            if(agentlist.size() == 1){//一级代理的会员
                //一级代理利润
                AgentAgencyFee agentAgencyFee = new AgentAgencyFee();

                //末级代理
                scale = getScale(agentUser,feeType);
                selfProfitMoney = totalMoney.multiply(scale).setScale(4, 4);
                agentAgencyFee.setAgentId(agentUser.getId());
                agentAgencyFee.setStatus(1);
                agentAgencyFee.setAymentType(1);
                agentAgencyFee.setBusinessId(businessId);
                agentAgencyFee.setFeeType(feeType);
                agentAgencyFee.setTotalMoney(totalMoney);
                agentAgencyFee.setProfitMoney(selfProfitMoney);
                agentAgencyFee.setRemarks(remarks);
                saveAgencyFee(agentAgencyFee);
                //总代理利润
                agentUser = agentUserMapper.findAgentByAgentId(agentUser.getParentId());
                AgentAgencyFee totalAgent = new AgentAgencyFee();
                totalAgent.setAgentId(agentUser.getId());
                totalAgent.setStatus(1);
                totalAgent.setAymentType(1);
                totalAgent.setBusinessId(businessId);
                totalAgent.setFeeType(feeType);
                upProfitMoney = totalMoney.subtract(selfProfitMoney);
                totalAgent.setTotalMoney(totalMoney);
                totalAgent.setProfitMoney(upProfitMoney);
                totalAgent.setRemarks(remarks);
                saveAgencyFee(totalAgent);
            } else if(agentlist.size()>1) {//二级代理以上会员
                for (int i = 1; i < agentlist.size(); i++) {
                    AgentUser model = agentlist.get(i);
                    //一级代理，要把总代理的收入算出来
                    if (i == 1) {
                        //一级代理利润
                        AgentAgencyFee agentAgencyFee = new AgentAgencyFee();
                        if (agentlist.size() == 2) {
                            //末级代理 level=2
                            AgentUser selfAgentUser = agentUserMapper.findAgentByAgentId(agentId);
                            //获取一级代理的总利润
                            scale = getScale(model,feeType);
                            oneProfitMoney = totalMoney.multiply(scale).setScale(4, 4);

                            //获取末级代理level=2的总利润
                            scale = getScale(selfAgentUser,feeType);
                            downProfitMoney = oneProfitMoney.multiply(scale).setScale(4, 4);

                            //一级代理最终利润
                            selfProfitMoney = oneProfitMoney.subtract(downProfitMoney);

                        } else {
                            //第二级代理
                            AgentUser modeldown = agentlist.get(i + 1);
                            //一级代理scale
                            scale = getScale(model,feeType);
                            //一级代理总利润
                            oneProfitMoney = totalMoney.multiply(scale).setScale(4, 4);
                            //二级代理scale
                            scale = getScale(modeldown,feeType);
                            //2级的总利润
                            downProfitMoney = oneProfitMoney.multiply(scale).setScale(4, 4);
                            //一级最终利润
                            selfProfitMoney = oneProfitMoney.subtract(downProfitMoney);
                        }
                        //一级代理level=1的明细表
                        agentAgencyFee.setAgentId(model.getId());
                        agentAgencyFee.setStatus(1);
                        agentAgencyFee.setAymentType(1);
                        agentAgencyFee.setBusinessId(businessId);
                        agentAgencyFee.setFeeType(feeType);
                        agentAgencyFee.setTotalMoney(totalMoney);
                        agentAgencyFee.setProfitMoney(selfProfitMoney);
                        agentAgencyFee.setRemarks(remarks);
                        //增加一级代理利润
                        saveAgencyFee(agentAgencyFee);

                        //总代理level=0利润
                        agentUser = agentUserMapper.findAgentByAgentId(model.getParentId());
                        AgentAgencyFee totalAgent = new AgentAgencyFee();
                        totalAgent.setAgentId(agentUser.getId());
                        totalAgent.setStatus(1);
                        totalAgent.setAymentType(1);
                        totalAgent.setBusinessId(businessId);
                        totalAgent.setFeeType(feeType);
                        upProfitMoney = totalMoney.subtract(oneProfitMoney);
                        totalAgent.setTotalMoney(totalMoney);
                        totalAgent.setProfitMoney(upProfitMoney);
                        totalAgent.setRemarks(remarks);
                        saveAgencyFee(totalAgent);
                        upProfitMoney = downProfitMoney;
                        ret = ret +1 ;
                    } else {
                        //二级以下代理利润
                        AgentAgencyFee agentAgencyFee = new AgentAgencyFee();
                        if (i == (level-1)) {//倒数第二级，自己就是下级
                            AgentUser selfAgentUser = agentUserMapper.findAgentByAgentId(agentId);
                            scale = getScale(selfAgentUser,feeType);
                            //三级总利润
                            downProfitMoney = upProfitMoney.multiply(scale).setScale(4, 4);
                            //二级最终利润
                            selfProfitMoney = upProfitMoney.subtract(downProfitMoney);
                        } else {
                            AgentUser modeldown = agentlist.get(i + 1);
                            scale = getScale(modeldown,feeType);
                            downProfitMoney = upProfitMoney.multiply(scale).setScale(4, 4);
                            selfProfitMoney = upProfitMoney.subtract(downProfitMoney);
                        }

                        ///
                        agentAgencyFee.setAgentId(model.getId());
                        agentAgencyFee.setStatus(1);
                        agentAgencyFee.setAymentType(1);
                        agentAgencyFee.setBusinessId(businessId);
                        agentAgencyFee.setFeeType(feeType);
                        agentAgencyFee.setTotalMoney(totalMoney);
                        agentAgencyFee.setProfitMoney(selfProfitMoney);
                        agentAgencyFee.setRemarks(remarks);
                        saveAgencyFee(agentAgencyFee);
                        upProfitMoney = downProfitMoney;
                        ret = ret +1 ;
                    }
                }
                //最后一级的利润
                AgentAgencyFee agentAgencyFee = new AgentAgencyFee();
                agentAgencyFee.setAgentId(agentId);
                agentAgencyFee.setStatus(1);
                agentAgencyFee.setAymentType(1);
                agentAgencyFee.setBusinessId(businessId);
                agentAgencyFee.setFeeType(feeType);
                agentAgencyFee.setTotalMoney(totalMoney);
                agentAgencyFee.setProfitMoney(downProfitMoney);
                agentAgencyFee.setRemarks(remarks);
                saveAgencyFee(agentAgencyFee);
            }
        }
        return ret;
    }

    /*增加代理利润*/
    public int saveAgencyFee(AgentAgencyFee model){
        int k = 0;
        //添加利润明细
        k = insert(model);
        //修改代理帳戶余额
        AgentUser user = new AgentUser();
        user.setId(model.getAgentId());
        user.setTotalMoney(model.getProfitMoney());
        k = agentUserMapper.updateTotalMoney(user);
        return k;
    }

    /*查询代理利润明细列表
    * */
    public ServerResponse<PageInfo> getAgentAgencyFeeList(int pageNum, int pageSize, HttpServletRequest request) {
        Page<AgentAgencyFeeVO> page = PageHelper.startPage(pageNum, pageSize);
        AgentUser agentUser = getCurrentAgent(request);
        List<AgentAgencyFee> agentAgencyFees = this.agentAgencyFeeMapper.getAgentAgencyFeeList(agentUser.getId());
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setList(agentAgencyFees);
        return ServerResponse.createBySuccess(pageInfo);
    }

    public AgentUser getCurrentAgent(HttpServletRequest request) {
        String loginToken = CookieUtils.readLoginToken(request, PropertiesUtil.getProperty("agent.cookie.name"));
        String agentJson = RedisShardedPoolUtils.get(loginToken);
        AgentUser agentUser = (AgentUser) JsonUtil.string2Obj(agentJson, AgentUser.class);
        return this.agentUserMapper.selectByPrimaryKey(agentUser.getId());
    }
}
