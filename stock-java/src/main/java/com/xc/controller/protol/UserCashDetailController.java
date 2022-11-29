 package com.xc.controller.protol;


 import com.xc.common.ServerResponse;
 import com.xc.service.ISiteMessageService;
 import com.xc.service.IUserCashDetailService;
 import javax.servlet.http.HttpServletRequest;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RequestParam;
 import org.springframework.web.bind.annotation.ResponseBody;

 @Controller
 @RequestMapping({"/user/cash/"})
 public class UserCashDetailController {
     @Autowired
     IUserCashDetailService iUserCashDetailService;
     @Autowired
     ISiteMessageService iSiteMessageService;

     //查询所有資產明细
     @RequestMapping({"list.do"})
     @ResponseBody
     public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "positionId", required = false) Integer positionId) {
         return this.iUserCashDetailService.findUserCashDetailList(positionId, request, pageNum, pageSize);
     }

     //查询用戶站内消息
     @RequestMapping({"getMessagelist.do"})
     @ResponseBody
     public ServerResponse getMessagelist(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "userId", required = false) Integer userId) {
         return this.iSiteMessageService.getSiteMessageByUserIdList(pageNum, pageSize, userId,request);
     }

     //用戶站内消息狀態变已读
     @RequestMapping({"updateMessageStatus.do"})
     @ResponseBody
     public ServerResponse updateMessageStatus(HttpServletRequest request) {
         this.iSiteMessageService.updateMessageStatus(request);
         return ServerResponse.createBySuccess("View success");
     }

     //查询用戶未读消息数
     @RequestMapping({"getUnreadCount.do"})
     @ResponseBody
     public ServerResponse getUnreadCount(HttpServletRequest request) {
         int k = this.iSiteMessageService.getUnreadCount(request);
         return ServerResponse.createBySuccess(k);
     }

 }