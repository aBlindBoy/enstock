package com.xc.utils.task.stock;

import com.xc.dao.StockMapper;
import com.xc.pojo.Stock;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//@Component
//@Order(9999)
public class MoomooTask {
    @Autowired
    StockMapper stockMapper;


//    @PostConstruct
    private void init() throws IOException, InterruptedException {
        this.deleteStockCode();
    }


//    @Scheduled(cron = "* * * * * *")
    public void deleteStockCode() throws IOException, InterruptedException {
        for (int i = 658; i <= 704; i++) {
            System.out.println("第"+i+"页");
            Connection connect = Jsoup.connect( "https://www.moomoo.com/quote/list/us/1/"+i);
//        connect.proxy("127.0.0.1",7890);
            Document document = connect.execute().parse();
            Elements aTags = document.getElementsByClass("list-item-main").first().getElementsByTag("a");
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

                Connection detailConn = Jsoup.connect( "https://www.moomoo.com/stock/"+stockCode+"-US/company-profile");
                Document document1 = detailConn.execute().parse();
                Elements companyMain =
                        document1.getElementsByClass("company-item");
                for (Element company:companyMain) {
                    if (company.getElementsByClass("name").first().text().contains("Company Name")){
                        stock.setCompanyName(company.getElementsByClass("value").last().text());
                    }
                    if (company.getElementsByClass("name").first().text().contains("Market")){
                        stock.setStockPlate(company.getElementsByClass("value").last().text());
                    }
                    if (company.getElementsByClass("name").first().text().contains("Securities Type")){
                        stock.setStockType(company.getElementsByClass("value").last().text());
                    }
                    if (company.getElementsByClass("name").first().text().contains("Website")){
                        stock.setWebsite(company.getElementsByClass("value").last().text());
                    }
                    if (company.getElementsByClass("name").first().text().contains("Profile")){
                        stock.setProfile(company.getElementsByClass("value").last().text());
                    }
                }
                stock.setIsLock(0);
                stock.setIsShow(0);
                stock.setAddTime(new Date());
                System.out.println(stock.toString());
                this.stockMapper.insert(stock);
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }



//
    }

}
