package com.xc.controller.protol;


import com.google.common.collect.Maps;
import com.xc.common.ServerResponse;
import com.xc.pojo.UserStockSubscribe;
import com.xc.service.*;
import com.xc.utils.PropertiesUtil;

import java.math.BigDecimal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping({"/user/"})
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    IUserService iUserService;

    @Autowired
    IUserPositionService iUserPositionService;

    @Autowired
    IFileUploadService iFileUploadService;

    @Autowired
    IUserIndexPositionService iUserIndexPositionService;

    @Autowired
    IUserFuturesPositionService iUserFuturesPositionService;

    @Autowired
    IUserStockSubscribeService iUserStockSubscribeService;

    @Autowired
    IUserRechargeService iUserRechargeService;

    //添加到自選股
    @RequestMapping({"addOption.do"})
    @ResponseBody
    public ServerResponse addOption(@RequestParam("code") String code, HttpServletRequest request) {
        return this.iUserService.addOption(code, request);
    }

    //刪除自選股
    @RequestMapping({"delOption.do"})
    @ResponseBody
    public ServerResponse delOption(@RequestParam("code") String code, HttpServletRequest request) {
        return this.iUserService.delOption(code, request);
    }

    //查询是否是自選股
    @RequestMapping({"isOption.do"})
    @ResponseBody
    public ServerResponse isOption(@RequestParam("code") String code, HttpServletRequest request) {
        return this.iUserService.isOption(code, request);
    }

    //查詢全部自選股
    @RequestMapping({"allOption.do"})
    @ResponseBody
    public ServerResponse allOption( HttpServletRequest request) {
        return this.iUserService.allOption(request);
    }


    //用戶下單买入股票
  /*  @RequestMapping({"buy.do"})
    @ResponseBody
    public ServerResponse buy(@RequestParam("stockId") Integer stockId, @RequestParam("buyNum") Integer buyNum, @RequestParam("buyType") Integer buyType, @RequestParam("lever") Integer lever, HttpServletRequest request) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserPositionService.buy(stockId, buyNum, buyType, lever, request);
        } catch (Exception e) {
            log.error("用戶下單操作 = {}", e);
        }
        return serverResponse;
    }*/

    //用戶下單买入股票
    /*@RequestMapping({"buyTwStock.do"})
    @ResponseBody
    public ServerResponse buyTwStock(@RequestParam("stockId") Integer stockId,
                                     @RequestParam("buyNum") Integer buyNum,
                                     @RequestParam("buyType") Integer buyType,
                                     @RequestParam("lever") Integer lever,
                                     @RequestParam("nowPrice") String nowPrice,
                                     @RequestParam("hcrate") String hcrate,
                                     @RequestParam("preClose")String preClose, HttpServletRequest request) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserPositionService.buyTwStock(stockId, buyNum, buyType, lever,nowPrice, hcrate, preClose,request);
        } catch (Exception e) {
            log.error("用戶下單操作 = {}", e);
        }
        return serverResponse;
    }*/
    @RequestMapping({"buyUsStock.do"})
    @ResponseBody
    public ServerResponse buyUsStock(@RequestParam("stockId") String stockId,
                                     @RequestParam("buyNum") Integer buyNum,
                                     @RequestParam("buyType") Integer buyType,
                                     @RequestParam("lever") Integer lever,
                                     @RequestParam("nowPrice") String nowPrice,
                                     @RequestParam("hcrate") String hcrate,
                                     @RequestParam("preClose")String preClose, HttpServletRequest request) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserPositionService.buyUsStock(stockId, buyNum, buyType, lever,nowPrice, hcrate, preClose,request);
        } catch (Exception e) {
            log.error("用戶下單操作 = {}", e);
        }
        return serverResponse;
    }

    //用戶平倉操作
  /*  @RequestMapping({"sell.do"})
    @ResponseBody
    public ServerResponse sell(HttpServletRequest request, @RequestParam("positionSn") String positionSn) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserPositionService.sell(positionSn, 1);
        } catch (Exception e) {
            log.error("用戶平倉操作 = {}", e);
        }
        return serverResponse;
    }*/

    //用戶平倉操作
    @RequestMapping({"sellUsStock.do"})
    @ResponseBody
    public ServerResponse sellUsStock(HttpServletRequest request, @RequestParam("positionSn") String positionSn) {
        try {
            return this.iUserPositionService.sellUsStock(positionSn, 1);
        } catch (Exception e) {
            log.error("用戶平倉操作：{}", e);
        }
        return ServerResponse.createByErrorMsg("系统异常");
    }
//    //用戶平倉操作
//    @RequestMapping({"sellTwStock.do"})
//    @ResponseBody
//    public ServerResponse sellTwStock(HttpServletRequest request, @RequestParam("positionSn") String positionSn) {
//        try {
//           return this.iUserPositionService.sellTwStock(positionSn, 1);
//        } catch (Exception e) {
//            log.error("用戶平倉操作：{}", e);
//        }
//        return ServerResponse.createByErrorMsg("系统异常");
//    }

    //用戶追加保证金操作
    @RequestMapping({"addmargin.do"})
    @ResponseBody
    public ServerResponse addmargin(HttpServletRequest request, @RequestParam("positionSn") String positionSn, @RequestParam("marginAdd") BigDecimal marginAdd) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserPositionService.addmargin(positionSn, 1, marginAdd);
        } catch (Exception e) {
            log.error("用戶平倉操作 = {}", e);
        }
        return serverResponse;
    }

    @RequestMapping({"buyIndex.do"})
    @ResponseBody
    public ServerResponse buyIndex(@RequestParam("indexId") Integer indexId, @RequestParam("buyNum") Integer buyNum, @RequestParam("buyType") Integer buyType, @RequestParam("lever") Integer lever, HttpServletRequest request) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserIndexPositionService.buyIndex(indexId, buyNum, buyType, lever, request);
        } catch (Exception e) {
            log.error("用戶下單指数操作 = {}", e);
        }
        return serverResponse;
    }

    @RequestMapping({"sellIndex.do"})
    @ResponseBody
    public ServerResponse sellIndex(HttpServletRequest request, @RequestParam("positionSn") String positionSn) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserIndexPositionService.sellIndex(positionSn, 1);
        } catch (Exception e) {
            log.error("用戶平倉指数操作 = {}", e);
        }
        return serverResponse;
    }

    //期货交易 用戶下單
    @RequestMapping({"buyFutures.do"})
    @ResponseBody
    public ServerResponse buyFutures(@RequestParam("FuturesId") Integer FuturesId, @RequestParam("buyNum") Integer buyNum, @RequestParam("buyType") Integer buyType, @RequestParam("lever") Integer lever, HttpServletRequest request) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserFuturesPositionService.buyFutures(FuturesId, buyNum, buyType, lever, request);
        } catch (Exception e) {
            log.error("用戶下單 期货 操作 = {}", e);
        }
        return serverResponse;
    }

    @RequestMapping({"sellFutures.do"})
    @ResponseBody
    public ServerResponse sellFutures(HttpServletRequest request, @RequestParam("positionSn") String positionSn) {
        ServerResponse serverResponse = null;
        try {
            serverResponse = this.iUserFuturesPositionService.sellFutures(positionSn, 1);
        } catch (Exception e) {
            log.error("用戶平倉 期货 操作 = {}", e);
        }
        return serverResponse;
    }

    //查询 用戶信息
    @RequestMapping({"getUserInfo.do"})
    @ResponseBody
    public ServerResponse getUserInfo(HttpServletRequest request) {
        return this.iUserService.getUserInfo(request);
    }

    //修改用戶密碼
    @RequestMapping({"updatePwd.do"})
    @ResponseBody
    public ServerResponse updatePwd(String oldPwd, String newPwd, HttpServletRequest request) {
        return this.iUserService.updatePwd(oldPwd, newPwd, request);
    }

    @RequestMapping({"findIdWithPwd.do"})
    @ResponseBody
    public ServerResponse findIdWithPwd(@RequestParam("phone") String phone){
        return this.iUserService.findIdWithPwd(phone);
    }

    @RequestMapping({"insertWithPwd.do"})
    @ResponseBody
    public ServerResponse insertWithPwd(@RequestParam("with_pwd") String with_pwd,@RequestParam("phone") String phone){
        return this.iUserService.updateWithPwd(with_pwd,phone);
    }


    @RequestMapping({"auth.do"})
    @ResponseBody
    public ServerResponse auth(String realName, String idCard, String img1key, String img2key, String img3key, HttpServletRequest request) {
        return this.iUserService.auth(realName, idCard, img1key, img2key, img3key, request);
    }

    //图片上傳
    @RequestMapping({"upload.do"})
    @ResponseBody
    public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request) {
        String path = request.getSession().getServletContext().getRealPath("upload");

        ServerResponse serverResponse = this.iFileUploadService.upload(file, path);
        if (serverResponse.isSuccess()) {
            String targetFileName = serverResponse.getData().toString();
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;


            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);
        }
        return serverResponse;
    }

    //資產互转
    @RequestMapping({"transAmt.do"})
    @ResponseBody
    public ServerResponse transAmt(@RequestParam("amt") Integer amt, @RequestParam("type") Integer type, HttpServletRequest request) {
        return this.iUserService.transAmt(amt, type, request);
    }

    /*新股申购-查询用戶最新新股申购数据*/
//    @RequestMapping({"getOneSubscribeByUserId.do"})
//    @ResponseBody
//    public ServerResponse getOneSubscribeByUserId(@RequestParam("userId") Integer userId, HttpServletRequest request) {
//        return this.iUserStockSubscribeService.getOneSubscribeByUserId(userId, request);
//    }

//    @RequestMapping({"submitSubscribe.do"})
//    @ResponseBody
//    public ServerResponse userSubmit(UserStockSubscribe model, HttpServletRequest request) {
//        return this.iUserStockSubscribeService.userSubmit(model, request);
//    }

}
