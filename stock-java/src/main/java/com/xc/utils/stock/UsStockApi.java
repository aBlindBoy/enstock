package com.xc.utils.stock;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xc.utils.HttpClientRequest;
import com.xc.vo.stock.StockListVO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class UsStockApi {
    private static final Logger log = LoggerFactory.getLogger(UsStockApi.class);

    public static String getStock(String code) {
        String result = "";
        try {

//            HttpRequest get = HttpUtil.createRequest(Method.GET, TW_URL + code);
//            get.setHttpProxy("127.0.0.1",7890);
//            result = get.execute().body();
            result = HttpClientRequest.doGet(
                    String.format("https://ws.api.cnyes.com/ws/api/v1/quote/quotes/USS:%s:STOCK?column=G,F_FORMAT_V2",code));
//            result = ClientProxyHttpClientHttp.doGetRequest(TW_URL+ code);\
            log.info(String.format("https://ws.api.cnyes.com/ws/api/v1/quote/quotes/USS:%s:STOCK?column=G,F_FORMAT_V2",code));
            log.info("請求數據源接口：{}",result);
        } catch (Exception e) {
            log.error("获取股票行情出错，錯誤信息 = {}", e);
        }
        return result;
    }
    public static StockListVO assembleStockListVO(String result) {
        Object byPath = JSONUtil.getByPath(JSONUtil.parse(result), "$.data[0]");
        StockListVO stockListVO=new StockListVO();
        JSONObject jsonObject1 =  JSONUtil.parseObj(byPath);
//        List list =jsonObject.getJSONArray("data");
//        net.sf.json.JSONObject jsonObject1= (net.sf.json.JSONObject) list.get(0);
        stockListVO.setName(jsonObject1.getStr("200024")) ;//股票名稱
        stockListVO.setCode(jsonObject1.getStr("200010"));//股票代號
        stockListVO.setNowPrice(jsonObject1.getStr("6")) ;//當盤成交價
        stockListVO.setHcrate(new BigDecimal( jsonObject1.getStr("56"))) ;//漲跌幅
        stockListVO.setToday_max(jsonObject1.getStr("12")) ;//最高價
        stockListVO.setToday_min( jsonObject1.getStr("13")) ;//最低價
        stockListVO.setBusiness_amount( jsonObject1.getStr("800001")) ;//當盤成交量
//        stockListVO.setBusiness_balance((String) jsonObject1.get("成交金額")) ;
        stockListVO.setPreclose_px(jsonObject1.getStr("6")) ;//參考價
        stockListVO.setOpen_px( jsonObject1.getStr("19")) ;//開盤價
        return stockListVO;
    }

    /**
     * 获取moomoo数据
     * https://www.moomoo.com/stock/AABB-US
     */
    public static StockListVO getMoomooStock(String stockCode) {
        StockListVO stockListVO = new StockListVO();
        try {
            Connection detailConn = Jsoup.connect( "https://www.moomoo.com/stock/"+stockCode+"-US/");
            detailConn.proxy("127.0.0.1",7890);
            Document document1 = detailConn.execute().parse();
            Element stockMain = document1.getElementsByClass("stock-main").first();

            //stock-price
            stockListVO.setName(stockMain.getElementsByClass("stock-name").last().text().split(" ",2)[1]);
            stockListVO.setCode(stockCode);
            stockListVO.setNowPrice(stockMain.getElementsByClass("stock-price").last().text());
            stockListVO.setHcrate(new BigDecimal(stockMain.getElementsByClass("stock-change").get(1).text().replace("%","")));


            stockListVO.setToday_max(stockMain.getElementsByClass("stock-detail-li").get(0).text().replace("High",""));
            stockListVO.setToday_min( stockMain.getElementsByClass("stock-detail-li").get(3).text().replace("Low","")) ;//最低價
//            stockListVO.setBusiness_amount(stockMain.getElementsByClass("text-down").get(2).text());
            stockListVO.setOpen_px( stockMain.getElementsByClass("stock-detail-li").get(5).text().replace("Open","")) ;//開盤價

        } catch (Exception e) {
            log.error("获取股票行情出错，錯誤信息 = {}", e);
        }
        log.info("请取Moomoo数据源：{}",stockCode);
        log.info("Moomoo返回数据：{}",stockListVO);
        return stockListVO;
    }
  /*  public static List<StockListVO> assembleStockList(String result) {

        JSONObject jsonObject= JSONObject.fromObject(result);
        List<JSONObject> list =jsonObject.getJSONArray("data");
        List stockList =new ArrayList<StockListVO>();
        for (JSONObject jsonObject1 : list){
            if(!jsonObject1.has("股票名稱")){
                continue;
            }
            StockListVO stockListVO=new StockListVO();
            stockListVO.setName((String) jsonObject1.get("股票名稱")) ;
            stockListVO.setNowPrice((String) jsonObject1.get("當盤成交價")) ;
            stockListVO.setHcrate(new BigDecimal((String) jsonObject1.get("漲跌幅"))) ;
            stockListVO.setToday_max((String) jsonObject1.get("最高價")) ;
            stockListVO.setToday_min((String) jsonObject1.get("最低價")) ;
            stockListVO.setBusiness_amount((String) jsonObject1.get("當盤成交量")) ;
            stockListVO.setBusiness_balance((String) jsonObject1.get("成交金額")) ;
            stockListVO.setPreclose_px((String) jsonObject1.get("參考價")) ;
            stockListVO.setOpen_px((String) jsonObject1.get("開盤價")) ;
            stockList.add(stockListVO);
        }
        return stockList;
    }*/


}
