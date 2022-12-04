//package com.xc.utils.stock;
//
//import com.xc.utils.HttpClientRequest;
//import com.xc.utils.PropertiesUtil;
//import com.xc.vo.stock.StockListVO;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import net.sf.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//public class TwStockApi {
//    public static final String TW_URL = PropertiesUtil.getProperty("tw.single.stock.url");
//    private static final Logger log = LoggerFactory.getLogger(TwStockApi.class);
//
//    public static String getTwStock(String code) {
//        String result = "";
//        try {
//            //TODO 代理到dl去获取sina data
//            result = HttpClientRequest.doGet(TW_URL + code);
//
//        } catch (Exception e) {
//            log.error("获取股票行情出错，錯誤信息 = {}", e);
//        }
//        return result;
//    }
//    public static StockListVO assembleStockListVO(String result) {
//        StockListVO stockListVO=new StockListVO();
//        JSONObject jsonObject= JSONObject.fromObject(result);
//        List list =jsonObject.getJSONArray("data");
//        net.sf.json.JSONObject jsonObject1= (net.sf.json.JSONObject) list.get(0);
//        stockListVO.setName((String) jsonObject1.get("股票名稱")) ;
//        stockListVO.setCode(jsonObject1.getString("股票代號"));
//        stockListVO.setNowPrice((String) jsonObject1.get("當盤成交價")) ;
//        stockListVO.setHcrate(new BigDecimal((String) jsonObject1.get("漲跌幅"))) ;
//        stockListVO.setToday_max((String) jsonObject1.get("最高價")) ;
//        stockListVO.setToday_min((String) jsonObject1.get("最低價")) ;
//        stockListVO.setBusiness_amount((String) jsonObject1.get("當盤成交量")) ;
//        stockListVO.setBusiness_balance((String) jsonObject1.get("成交金額")) ;
//        stockListVO.setPreclose_px((String) jsonObject1.get("參考價")) ;
//        stockListVO.setOpen_px((String) jsonObject1.get("開盤價")) ;
//        return stockListVO;
//    }
//    public static List<StockListVO> assembleStockList(String result) {
//
//        JSONObject jsonObject= JSONObject.fromObject(result);
//        List<JSONObject> list =jsonObject.getJSONArray("data");
//        List stockList =new ArrayList<StockListVO>();
//        for (JSONObject jsonObject1 : list){
//            if(!jsonObject1.has("股票名稱")){
//                continue;
//            }
//            StockListVO stockListVO=new StockListVO();
//            stockListVO.setName((String) jsonObject1.get("股票名稱")) ;
//            stockListVO.setNowPrice((String) jsonObject1.get("當盤成交價")) ;
//            stockListVO.setHcrate(new BigDecimal((String) jsonObject1.get("漲跌幅"))) ;
//            stockListVO.setToday_max((String) jsonObject1.get("最高價")) ;
//            stockListVO.setToday_min((String) jsonObject1.get("最低價")) ;
//            stockListVO.setBusiness_amount((String) jsonObject1.get("當盤成交量")) ;
//            stockListVO.setBusiness_balance((String) jsonObject1.get("成交金額")) ;
//            stockListVO.setPreclose_px((String) jsonObject1.get("參考價")) ;
//            stockListVO.setOpen_px((String) jsonObject1.get("開盤價")) ;
//            stockList.add(stockListVO);
//        }
//        return stockList;
//    }
//
//
//    public static void main(String[] args) {
////        List list = Lists.newArrayList();
////        list.add(Integer.valueOf(1));
////        list.add(Integer.valueOf(2));
////        list.add(Integer.valueOf(3));
////        System.out.println(list.size());
////
////        String[][] values = new String[list.size()][5];
////
////        System.out.println("[]" + values.length);
////        System.out.println("[][]" + values[1].length);
////
////        System.out.println(getSinaStock("sh601318"));
////
////        String sss = "[\n{\n\"day\": \"2019-03-05 14:50:00\",\n\"open\": \"13.020\",\n\"high\": \"13.040\",\n\"low\": \"13.000\",\n\"close\": \"13.040\",\n\"volume\": \"2611513\",\n\"ma_price5\": 13.01,\n\"ma_volume5\": 3216535\n},\n{\n\"day\": \"2019-03-05 14:55:00\",\n\"open\": \"13.040\",\n\"high\": \"13.040\",\n\"low\": \"13.010\",\n\"close\": \"13.030\",\n\"volume\": \"2296000\",\n\"ma_price5\": 13.016,\n\"ma_volume5\": 3044839\n}\n]";
////
////        sss = sss.substring(1, sss.length() - 1);
////
////        sss = "{" + sss + "}";
//        String result=getTwStock("6235,2330");
//
//        List<StockListVO> stockList=assembleStockList(result);
//        for (int i = 0; i < stockList.size(); i++) {
//            StockListVO stockVO=stockList.get(i);
//
//        }
//    }
//}
//
