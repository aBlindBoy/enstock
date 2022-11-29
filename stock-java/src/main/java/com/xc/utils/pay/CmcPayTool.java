package com.xc.utils.pay;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.xc.common.CmcPayConfig;
import com.xc.controller.protol.UserPayController;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by xieyuxing on 2017/9/22.
 */

public class CmcPayTool {
    private static final Logger log = LoggerFactory.getLogger(UserPayController.class);

    /*Cmc支付提交*/
    public static String submitOrder(String amount, String orderid, String pay_id, HttpServletRequest request) throws Exception {
        if (orderid==null||orderid.trim().equals("")) orderid= "OF"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        LinkedMap params=new LinkedMap();
        params.put("merchantid", CmcPayConfig.UID);//商戶ID
        params.put("orderno", orderid); //商戶业务訂單号
        params.put("orderamount", amount);//支付購買数量：USDT 單位為（个），USD 單位為（元）不支持小数点
        params.put("paytype", "bank");// 付款方式(小写英文)：alipay 支付宝，bank 銀联卡转帳
        params.put("ordercurrency", "USD");//購買币种：USDT（泰达币），USD（人民币）
        String userAgent = request.getHeader("user-agent");
        String returnUrl = CmcPayConfig.RETURN_URL;
        //update by
//        if (userAgent.indexOf("Android") != -1 || userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
//            returnUrl = returnUrl.replace("/homes/","/wap/");
//        }
        params.put("callbackurl", returnUrl);//支付完成跳转地址，需带 http:/
        params.put("serverbackurl", CmcPayConfig.NOTIFY_URL); //訂單回调通知地址，需带 http://
        params.put("signtype", "md5");//签名加密算法：目前只支持 md5，32 位小写
        Base64.Encoder encoder = Base64.getEncoder();
        log.info("提交支付訂單key="+CmcPayConfig.KEY);
        String _sign=  CmcPayOuterRequestUtil.getSign(params, CmcPayConfig.KEY);
        params.put("sign", _sign);
        params.put("url", CmcPayConfig.URL);
        String par = JSON.toJSONString(params).toString();
        log.info("提交支付訂單參數="+par);
        //String urlWithParams=CmcPayOuterRequestUtil.post(CmcPayConfig.URL,params);
        return par;
    }

    /*H5支付提交*/
    public static String submitOrderH5(String amount, String orderid, String pay_id) throws Exception {
        if (orderid==null||orderid.trim().equals("")) orderid= "OF"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        LinkedMap params=new LinkedMap();
        params.put("appid", CmcPayConfig.H5UID);//商戶ID
        params.put("data", orderid); //商戶业务訂單号
        Double d= Double.parseDouble(amount);
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(d);
        params.put("money", s);//訂單价格（單位為分）
        params.put("type", pay_id);//類型  1 支付宝  2 QQ钱包  3 微信   4 云闪付
        InetAddress addr = InetAddress.getLocalHost();
        params.put("uip",addr.getHostAddress());//客戶IP地址
        Base64.Encoder encoder = Base64.getEncoder();
        log.info("提交支付訂單key="+CmcPayConfig.H5KEY);
        String _sign=  CmcPayOuterRequestUtil.getSignH5(params, CmcPayConfig.H5KEY);
        params.put("token", _sign);
        String urlWithParams=CmcPayOuterRequestUtil.post(CmcPayConfig.H5URL,params);
        return urlWithParams;
        /*params.put("url", CmcPayConfig.H5URL);
        String par = JSON.toJSONString(params).toString();
        log.info("提交支付訂單參數="+par);
        return par;*/
    }
}