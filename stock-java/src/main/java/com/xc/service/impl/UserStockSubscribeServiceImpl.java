package com.xc.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.dao.UserMapper;
import com.xc.dao.UserStockSubscribeMapper;
import com.xc.pojo.SiteAdmin;
import com.xc.pojo.SiteMessage;
import com.xc.pojo.User;
import com.xc.pojo.UserStockSubscribe;
import com.xc.service.*;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.HttpRequest;
import com.xc.utils.PropertiesUtil;
import com.xc.utils.redis.CookieUtils;
import com.xc.utils.redis.JsonUtil;
import com.xc.utils.redis.RedisShardedPoolUtils;
import com.xc.vo.user.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Parser;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 新股申购
 * @author lr
 * @date 2020/07/24
 */
@Service("IUserStockSubscribeService")
@Slf4j
public class UserStockSubscribeServiceImpl implements IUserStockSubscribeService {

    @Resource
    private UserStockSubscribeMapper userStockSubscribeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ISiteMessageService iSiteMessageService;

    @Autowired
    IUserService iUserService;

    @Autowired
    IUserCashDetailService userCashDetailService;

    @Override
    public int insert(UserStockSubscribe model) {
        int ret = 0;
        if (model == null) {
            return 0;
        }
        ret = userStockSubscribeMapper.insert(model);
        return ret;
    }

    @Override
    public ServerResponse update(UserStockSubscribe model) {

        int ret = userStockSubscribeMapper.update(model);
        if (ret>0){
            return ServerResponse.createBySuccess("修改成功");
        }
        return ServerResponse.createBySuccess("修改失败");
    }

    /**
     * 新股申购-保存
     */
    @Override
    @Transactional
    public ServerResponse save(UserStockSubscribe model, HttpServletRequest request) {
//        int ret = 0;
//        if(model!=null && model.getId()>0){
//            model.setEndTime(DateTimeUtil.getCurrentDate());
//            ret = userStockSubscribeMapper.update(model);
//            UserStockSubscribe userStockSubscribe = userStockSubscribeMapper.load(model.getId());
//            if(model.getSubmitAmount() != null && model.getSubmitAmount().intValue() >0){
//                //客服修改提交金额
//            } else {
//                if(ret>0 && model.getStatus() == 3){
//                    //给达到消息強平提醒用戶推送消息
//                    SiteMessage siteMessage = new SiteMessage();
//                    siteMessage.setUserId(userStockSubscribe.getUserId());
//                    siteMessage.setUserName(userStockSubscribe.getRealName());
//                    siteMessage.setTypeName("新股申购");
//                    siteMessage.setStatus(1);
//                    siteMessage.setContent("【新股申购中签】恭喜您，新股申购中签成功，申购金额："+ userStockSubscribe.getSubmitAmount() +"，請及時关注哦。");
//                    siteMessage.setAddTime(DateTimeUtil.getCurrentDate());
//                    iSiteMessageService.insert(siteMessage);
//                }
//                if(ret>0 && model.getStatus() == 4){
//                    //给达到消息強平提醒用戶推送消息
//                    SiteMessage siteMessage = new SiteMessage();
//                    siteMessage.setUserId(userStockSubscribe.getUserId());
//                    siteMessage.setUserName(userStockSubscribe.getRealName());
//                    siteMessage.setTypeName("新股申购");
//                    siteMessage.setStatus(1);
//                    siteMessage.setContent("【新股申购未中签】很遗憾，您的新股申购本次未签，申购金额："+ userStockSubscribe.getSubmitAmount() +"。");
//                    siteMessage.setAddTime(DateTimeUtil.getCurrentDate());
//                    iSiteMessageService.insert(siteMessage);
//                }
//            }
//
//        } else{
//            if(model.getUserId() != null){
//                User user = userMapper.selectByPrimaryKey(model.getUserId());
//                model.setRealName(user.getRealName());
//                model.setPhone(user.getPhone());
//            }
//            String cookie_name = PropertiesUtil.getProperty("admin.cookie.name");
//            String logintoken = CookieUtils.readLoginToken(request, cookie_name);
//            String adminJson = RedisShardedPoolUtils.get(logintoken);
//            SiteAdmin siteAdmin = (SiteAdmin) JsonUtil.string2Obj(adminJson, SiteAdmin.class);
//            model.setAdminId(siteAdmin.getId());
//            model.setAdminName(siteAdmin.getAdminName());
//            model.setAddTime(DateTimeUtil.getCurrentDate());
//            model.setStatus(1);
//            ret = userStockSubscribeMapper.insert(model);
//        }
        User user = this.iUserService.getCurrentUser(request);
        if (user == null){
            return ServerResponse.createByErrorMsg("請先登入");
        }
        // 新增訂單
        model.setRealName(user.getRealName());
        model.setUserId(user.getId());
        model.setSubmitTime(new Date());
        model.setStatus(1); // 预约成功
        model.setDeductionStatus(1);//未扣款
        int insert = userStockSubscribeMapper.insert(model);
        if(insert>0){
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }

    /**
     * 發送站内信
     */
    @Override
    public ServerResponse sendMsg(UserStockSubscribe model, HttpServletRequest request) {
        int ret = 0;

        if(model!=null){
            //所有人发站内信
            if(model.getUserId() == 0){
                List<User> users = this.userMapper.listByAdmin(null, null, null, null, null);
                for(int k=0;k<users.size();k++){
                    User user = users.get(k);
                    SiteMessage siteMessage = new SiteMessage();
                    siteMessage.setUserId(user.getId());
                    siteMessage.setUserName(user.getRealName());
                    siteMessage.setTypeName("站内消息");
                    siteMessage.setStatus(1);
                    siteMessage.setContent("【站内消息】"+ model.getRemarks() );
                    siteMessage.setAddTime(DateTimeUtil.getCurrentDate());
                    ret = iSiteMessageService.insert(siteMessage);
                }
            } else {
                //指定用戶发站内信
                User user = userMapper.selectByPrimaryKey(model.getUserId());
                SiteMessage siteMessage = new SiteMessage();
                siteMessage.setUserId(user.getId());
                siteMessage.setUserName(user.getRealName());
                siteMessage.setTypeName("站内消息");
                siteMessage.setStatus(1);
                siteMessage.setContent("【站内消息】"+ model.getRemarks() );
                siteMessage.setAddTime(DateTimeUtil.getCurrentDate());
                ret = iSiteMessageService.insert(siteMessage);
            }
        }
        if(ret>0){
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }


    /*新股申购-查询列表*/
    @Override
    public ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, HttpServletRequest request){
        PageHelper.startPage(pageNum, pageSize);
        List<UserStockSubscribe> listData = this.userStockSubscribeMapper.pageList(pageNum, pageSize, keyword);
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*新股申购-查询详情*/
    @Override
    public ServerResponse getDetail(int id) {
        return ServerResponse.createBySuccess(this.userStockSubscribeMapper.load(id));
    }

    /*新股申购-查询用戶最新新股申购数据*/
    @Override
    public ServerResponse getOneSubscribeByUserId(Integer userId, HttpServletRequest request) {
        return ServerResponse.createBySuccess(this.userStockSubscribeMapper.getOneSubscribeByUserId(userId));
    }

    /**
     * 新股申购-用戶提交金额
     */
    @Override
    public ServerResponse userSubmit(UserStockSubscribe model, HttpServletRequest request) {
        int ret = 0;
        if(model!=null && model.getId()>0){
            UserStockSubscribe userStockSubscribe = userStockSubscribeMapper.load(model.getId());
            if(userStockSubscribe != null){
                model.setSubmitTime(DateTimeUtil.getCurrentDate());
                model.setStatus(2);
                ret = userStockSubscribeMapper.update(model);
            } else {
                return ServerResponse.createByErrorMsg("新股申购訂單不存在！");
            }

        }
        if(ret>0){
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }

    @Override
    public ServerResponse getHiStockList(HttpServletRequest request) throws Exception {
//        String html = HttpRequest.doGet("https://histock.tw/stock/public.aspx","");
//        Connection connect = Jsoup.connect("https://histock.tw/stock/public.aspx");
//        Document document = connect.get();
        String getUrl = "https://histock.tw/stock/public.aspx";
        Document document = Jsoup.parse( new URL(getUrl).openStream(), "utf-8",getUrl);
        Elements elementsByClass = document.getElementsByClass("alt-row");
        List<HashMap<String,String>> result = new ArrayList<>();
        for (Element element:elementsByClass ) {
            Elements td = element.select("td");
            HashMap<String,String> hashMap = new HashMap<String,String>();
            hashMap.put("drawDate",td.get(0).text()); // 抽籤日期
            hashMap.put("stockCode",td.get(1).text().split(" ")[0]);//股票代號
            hashMap.put("stockName",td.get(1).text().split(" ")[1]);//名稱
            hashMap.put("stockPlate",td.get(2).text());//發行市場
            hashMap.put("subscriptionTime",td.get(3).text());//申購期間
            hashMap.put("ticketingDate",td.get(4).text());//撥券日期
            hashMap.put("underwritingSheet",td.get(5).text());//承銷張數
            hashMap.put("underwritingPrice",td.get(6).text());//承銷價
            hashMap.put("marketPrice",td.get(7).text());//市價
//            hashMap.put("drawDate",td.get(0).text());//獲利
//            hashMap.put("drawDate",td.get(0).text());//報酬率(%)
//            hashMap.put("drawDate",td.get(0).text());//申購張數
//            hashMap.put("drawDate",td.get(0).text());//總合格件
            hashMap.put("remark",td.get(13).text());//備注

            result.add(hashMap);
        }
        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse getStockSubscribeHistoryList(int pageNum, int pageSize,  HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        User user = this.iUserService.getCurrentUser(request);
        List<UserStockSubscribe> listData = this.userStockSubscribeMapper.getStockSubscribeHistoryList(pageNum, pageSize, user.getId());
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse payStockSubscribe(UserStockSubscribe userStockSubscribe, HttpServletRequest request) {
        log.info("==================到中签日 扣款 开始=================");
        //查询当天，抽签日期
//        List<UserStockSubscribe> userStockSubscribes =
//                userStockSubscribeMapper.selectUserSubscribeByDrawDate(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd"));
//        for (UserStockSubscribe userStockSubscribe :userStockSubscribes) {
             userStockSubscribe = userStockSubscribeMapper.getById(userStockSubscribe.getId());

            ServerResponse serverResponse = iUserService.findByUserId(userStockSubscribe.getUserId());
            User user= (User) serverResponse.getData();
            userStockSubscribe.setEndTime(new Date());


            BigDecimal enableAmt = user.getEnableAmt();
            if (enableAmt.doubleValue() < userStockSubscribe.getSubmitAmount().doubleValue()){
                log.info("================== 余额不足 =================");
                return ServerResponse.createBySuccess("余额不足");
            }
            user.setEnableAmt(enableAmt.subtract(userStockSubscribe.getSubmitAmount()));
            iUserService.update(user);
            userStockSubscribe.setDeductionStatus(2);//設置扣款狀態
            userStockSubscribeMapper.update(userStockSubscribe);
            SiteMessage siteMessage = new SiteMessage();
            siteMessage.setUserId(userStockSubscribe.getUserId());
            siteMessage.setUserName(userStockSubscribe.getRealName());
            siteMessage.setTypeName("新穀申購");
            siteMessage.setStatus(1);
            siteMessage.setContent("【新穀申購中簽】恭喜您，新穀申購中簽成功，申購金額："+ userStockSubscribe.getSubmitAmount() +"，請及時关注哦。");
            siteMessage.setAddTime(DateTimeUtil.getCurrentDate());
            iSiteMessageService.insert(siteMessage);
//        }
        log.info("==================到中签日 扣款 结束=================");
        return ServerResponse.createBySuccess("繳費成功");
    }

}