package com.xc.controller.protol;


import com.xc.common.ServerResponse;
import com.xc.pojo.UserStockSubscribe;
import com.xc.service.IUserStockSubscribeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/user/subscribe/"})
public class UserStockSubscribeController {
    private static final Logger log = LoggerFactory.getLogger(UserStockSubscribeController.class);
    @Autowired
    IUserStockSubscribeService iUserStockSubscribeService;

    //列表查询
    @RequestMapping({"getHiStockList.do"})
    @ResponseBody
    public ServerResponse getStockSubscribeList( HttpServletRequest request) throws Exception {
        return this.iUserStockSubscribeService.getHiStockList(request);
    }

    //新股申购-申請
    @RequestMapping({"saveStockSubscribe.do"})
    @ResponseBody
    public ServerResponse saveStockSubscribe(UserStockSubscribe model, HttpServletRequest request) {
        return this.iUserStockSubscribeService.save(model, request);
    }

    //新股申购-支付
    @RequestMapping({"payStockSubscribe.do"})
    @ResponseBody
    public ServerResponse payStockSubscribe(UserStockSubscribe model, HttpServletRequest request) {
        return this.iUserStockSubscribeService.payStockSubscribe(model, request);
    }


    //新股申购-列表查询
    @RequestMapping({"getStockSubscribeHistoryList.do"})
    @ResponseBody
    public ServerResponse getStockSubscribeList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "100") int pageSize, @RequestParam(value = "keyword", defaultValue = "") String keyword, HttpServletRequest request) {
        return this.iUserStockSubscribeService.getStockSubscribeHistoryList(pageNum, pageSize, request);
    }



}
