package com.xc.controller;

import com.xc.common.ServerResponse;
import com.xc.pojo.Stock;
import com.xc.pojo.StockCoin;
import com.xc.service.IStockCoinService;
import com.xc.service.IStockService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping({"/api/stock/"})
public class StockApiController {
    private static final Logger log = LoggerFactory.getLogger(StockApiController.class);

    @Autowired
    IStockService iStockService;

    @Autowired
    IStockCoinService iStockCoinService;

    //查询 股票指数、大盘指数信息
//    @RequestMapping({"getMarket.do"})
    @ResponseBody
    public ServerResponse getMarket() {
        return this.iStockService.getMarket();
    }

    //查询官网PC端交易 所有股票信息及指定股票信息
    @RequestMapping({"getStock.do"})
    @ResponseBody
    public ServerResponse getStock(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "stockPlate", required = false) String stockPlate, @RequestParam(value = "stockType", required = false) String stockType, @RequestParam(value = "keyWords", required = false) String keyWords, HttpServletRequest request) {
        return this.iStockService.getStock(pageNum, pageSize, keyWords, stockPlate, stockType, request);
    }

    //通過股票代码查询股票信息
    @RequestMapping({"getSingleStock.do"})
    @ResponseBody
    public ServerResponse getSingleStock(@RequestParam("code") String code) {
        return this.iStockService.getSingleStock(code);
    }

    @RequestMapping({"getQuote.do"})
    @ResponseBody
    public ServerResponse getQuote(int type) throws IOException {
//        return this.iStockService.getSingleStock(code);
        Connection connect = Jsoup.connect( "https://www.moomoo.com/quote/list/us/"+type+"/1");
//        connect.proxy("127.0.0.1",7890);
        Document document = connect.execute().parse();
        Elements aTags = document.getElementsByClass("list-item-main").first().getElementsByTag("a");
        List<Stock> stockList = new ArrayList<>();

        for (Element element:aTags){
//                String href = element.attr("href");
            Elements divTags = element.getElementsByTag("div");
            String stockCode = divTags.get(0).attr("title");
            Stock stock = new Stock();
            stock.setStockCode(stockCode);
            stock.setStockName(divTags.get(1).attr("title"));
            stock.setLatestPrice(new BigDecimal(divTags.get(2).attr("title")));
            stock.setChg( new BigDecimal(divTags.get(3).attr("title").replace("%","")));
            stock.setChgRate(new BigDecimal(divTags.get(4).attr("title").replace("%","")));
//            Connection detailConn = Jsoup.connect( "https://www.moomoo.com/stock/"+stockCode+"-US/company-profile");
//            Document document1 = detailConn.execute().parse();
//            Elements companyMain =
//                    document1.getElementsByClass("company-item");
//            for (Element company:companyMain) {
//                if (company.getElementsByClass("name").first().text().contains("Company Name")){
//                    stock.setCompanyName(company.getElementsByClass("value").last().text());
//                }
//                if (company.getElementsByClass("name").first().text().contains("Market")){
//                    stock.setStockPlate(company.getElementsByClass("value").last().text());
//                }
//                if (company.getElementsByClass("name").first().text().contains("Securities Type")){
//                    stock.setStockType(company.getElementsByClass("value").last().text());
//                }
//                if (company.getElementsByClass("name").first().text().contains("Website")){
//                    stock.setWebsite(company.getElementsByClass("value").last().text());
//                }
//                if (company.getElementsByClass("name").first().text().contains("Profile")){
//                    stock.setProfile(company.getElementsByClass("value").last().text());
//                }
//            }
//            stock.setIsLock(0);
//            stock.setIsShow(0);
//            stock.setAddTime(new Date());
//        }
            stockList.add(stock);
        }
        return ServerResponse.createBySuccess(stockList);
}

    @RequestMapping({"getExchangeRate.do"})
    @ResponseBody
    public ServerResponse getExchangeRate(String coinCode)  {
        StockCoin stockCoin = iStockCoinService.selectCoinByCode(coinCode);
        return ServerResponse.createBySuccess(stockCoin);
    }



//    @RequestMapping({"getMinK.do"})
//    @ResponseBody
//    public ServerResponse getMinK(@RequestParam("code") String code, @RequestParam("time") Integer time, @RequestParam("ma") Integer ma, @RequestParam("size") Integer size) {
//        return this.iStockService.getMinK(code, time, ma, size);
//    }
//
//    /*查询股票日线*/
//    @RequestMapping({"getDayK.do"})
//    @ResponseBody
//    public ServerResponse getDayK(@RequestParam("code") String code) {
//        return this.iStockService.getDayK_Echarts(code);
//    }
//
//    //查询股票历史数据数据
//    @RequestMapping({"getMinK_Echarts.do"})
//    @ResponseBody
//    public ServerResponse getMinK_Echarts(@RequestParam("code") String code, @RequestParam("time") Integer time, @RequestParam("ma") Integer ma, @RequestParam("size") Integer size) {
//        return this.iStockService.getMinK_Echarts(code, time, ma, size);
//    }
//
//    /*期货分時-k线*/
//    @RequestMapping({"getFuturesMinK_Echarts.do"})
//    @ResponseBody
//    public ServerResponse getFuturesMinK_Echarts(@RequestParam("code") String code, @RequestParam("time") Integer time, @RequestParam("size") Integer size) {
//        return this.iStockService.getFuturesMinK_Echarts(code, time, size);
//    }
//
//    /*指数分時-k线*/
//    @RequestMapping({"getIndexMinK_Echarts.do"})
//    @ResponseBody
//    public ServerResponse getIndexMinK_Echarts(@RequestParam("code") String code, @RequestParam("time") Integer time, @RequestParam("size") Integer size) {
//        return this.iStockService.getIndexMinK_Echarts(code, time, size);
//    }
//
//    /*查询期货日线*/
//    @RequestMapping({"getFuturesDayK.do"})
//    @ResponseBody
//    public ServerResponse getFuturesDayK(@RequestParam("code") String code) {
//        return this.iStockService.getFuturesDayK(code);
//    }
//
//    /*指数日线*/
//    @RequestMapping({"getIndexDayK.do"})
//    @ResponseBody
//    public ServerResponse getIndexDayK(@RequestParam("code") String code) {
//        return this.iStockService.getIndexDayK(code);
//    }
}