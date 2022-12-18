package com.xc.utils.stock;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.xc.utils.HttpClientRequest;
import com.xc.vo.stock.StockListVO;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TwStockApi {
    //    public static final String TW_URL = PropertiesUtil.getProperty("tw.single.stock.url");
    private static final Logger log = LoggerFactory.getLogger(TwStockApi.class);

    public static String getTwStock(String code) {
        String result = "";
        try {

//            HttpRequest get = HttpUtil.createRequest(Method.GET, TW_URL + code);
//            get.setHttpProxy("127.0.0.1",7890);
//            result = get.execute().body();
            result = HttpClientRequest.doGet(
                    String.format("https://ws.api.cnyes.com/ws/api/v1/quote/quotes/TWS:%s:STOCK?column=I,M",code));
//            result = ClientProxyHttpClientHttp.doGetRequest(TW_URL+ code);
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
        stockListVO.setName(jsonObject1.getStr("200009")) ;//股票名稱
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
   /* public static List<StockListVO> assembleStockList(String result) {

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


    public static void main(String[] args) {
//        List list = Lists.newArrayList();
//        list.add(Integer.valueOf(1));
//        list.add(Integer.valueOf(2));
//        list.add(Integer.valueOf(3));
//        System.out.println(list.size());
//
//        String[][] values = new String[list.size()][5];
//
//        System.out.println("[]" + values.length);
//        System.out.println("[][]" + values[1].length);
//
//        System.out.println(getSinaStock("sh601318"));
//
//        String sss = "[\n{\n\"day\": \"2019-03-05 14:50:00\",\n\"open\": \"13.020\",\n\"high\": \"13.040\",\n\"low\": \"13.000\",\n\"close\": \"13.040\",\n\"volume\": \"2611513\",\n\"ma_price5\": 13.01,\n\"ma_volume5\": 3216535\n},\n{\n\"day\": \"2019-03-05 14:55:00\",\n\"open\": \"13.040\",\n\"high\": \"13.040\",\n\"low\": \"13.010\",\n\"close\": \"13.030\",\n\"volume\": \"2296000\",\n\"ma_price5\": 13.016,\n\"ma_volume5\": 3044839\n}\n]";
//
//        sss = sss.substring(1, sss.length() - 1);
//
//        sss = "{" + sss + "}";
        String result=getTwStock("6235");

        StockListVO stockListVO = assembleStockListVO(result);
        System.out.println(stockListVO);
//        for (int i = 0; i < stockList.size(); i++) {
//            StockListVO stockVO=stockList.get(i);
//
//        }
    }
}

