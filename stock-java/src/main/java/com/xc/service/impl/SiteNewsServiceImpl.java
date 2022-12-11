package com.xc.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.dao.SiteNewsMapper;
import com.xc.pojo.SiteNews;
import com.xc.service.ISiteNewsService;
import com.xc.utils.DateTimeUtil;
import com.xc.utils.HttpRequest;
import com.xc.utils.PropertiesUtil;
import com.xc.utils.StringUtils;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 新闻資讯
 * @author lr
 * @date 2020/07/24
 */
@Service("ISiteNewsService")
public class SiteNewsServiceImpl implements ISiteNewsService {

    private static final Logger log = LoggerFactory.getLogger(SiteNewsServiceImpl.class);

//    public static void main(String[] args) {
////        DateTime dateTime = new DateTime("Tue 08 Nov 2022 14:02:01", DatePattern.NORM_DATETIME_FORMAT);
////        DateTime parse = DateUtil.parse("Nov 10, 2022, 10:01", DatePattern.NORM_DATETIME_FORMAT);
//        System.out.println(parse1);
//
//    }
    @Resource
    private SiteNewsMapper siteNewsMapper;


    @Override
    public int insert(SiteNews model) {
        int ret = 0;
        if (model == null) {
            return 0;
        }
        ret = siteNewsMapper.insert(model);
        return ret;
    }

    @Override
    public int update(SiteNews model) {
        int ret = siteNewsMapper.update(model);
        return ret>0 ? ret: 0;
    }

    /**
     * 新闻資讯-保存
     */
    @Override
    public ServerResponse save(SiteNews model) {
        int ret = 0;
        if(model!=null && model.getId()>0){
            ret = siteNewsMapper.update(model);
        } else{
            ret = siteNewsMapper.insert(model);
        }
        if(ret>0){
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }

    /*新闻資讯-查询列表*/
    @Override
    public ServerResponse<PageInfo> getList(int pageNum, int pageSize, Integer type, String sort, String keyword, HttpServletRequest request){
        PageHelper.startPage(pageNum, pageSize);
        List<SiteNews> listData = this.siteNewsMapper.pageList(pageNum, pageSize, type, sort, keyword);
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*新闻資讯-查询详情*/
    @Override
    public ServerResponse getDetail(int id) {
        this.siteNewsMapper.updateViews(id);
        return ServerResponse.createBySuccess(this.siteNewsMapper.load(id));
    }

    /*新闻資讯-修改新闻浏览量*/
    @Override
    public ServerResponse updateViews(Integer id) {
        return ServerResponse.createBySuccess(this.siteNewsMapper.updateViews(id));
    }

    /*新闻資讯-top最新新闻資讯*/
    @Override
    public ServerResponse getTopNewsList(int pageSize){
        List<SiteNews> listData = this.siteNewsMapper.getTopNewsList(pageSize);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*新闻資讯-抓取*/
    @Override
    public int grabNews() throws IOException {

        String getUrl = "https://www.tradingview.com/markets/stocks-usa/news/";
        Connection connect = Jsoup.connect(getUrl);
//        connect.proxy("127.0.0.1",7890);
        Document document = connect.execute().parse();
        Element first = document.getElementsByClass("js-news-category-page-container").first();
        Elements elementsByTag = first.getElementsByTag("a");

        int i = 0;
        for (Element aTag:elementsByTag) {
//            Elements href = aTag.getElementsByAttribute("href");
            String detailUrl = "https://www.tradingview.com"+aTag.attr("href");
//            Elements timeElement = aTag.getElementsByTag("time");
            // 数据库存在该新闻
            if (siteNewsMapper.getNewsBySourceIdCount(detailUrl) > 0){
                continue;
            }
            // 去除第一个新闻
            if (i == 0){
                i++;
                continue;
            }
            Connection detailConnect = Jsoup.connect(detailUrl);
//            detailConnect.proxy("127.0.0.1",7890);
            Document detailDocument = detailConnect.execute().parse();
            Element article = detailDocument.getElementsByTag("article").first();
            Elements children = article.children();
            System.out.println(children.text());
            String logo = children.get(0).text();
            String title = children.get(1).text();
            String datetime = children.get(2).getElementsByTag("time").first().attr("title");
            String s = datetime.split(" ")[0];
            String s1 = datetime.split(" ",2)[1];
            String month = this.handlerMonth(s);

            DateTime parse1 = DateUtil.parse(month+" "+s1,"MM dd, yyyy, HH:mm");

            String content = null;
           if (children.get(3).hasAttr("data-no-swipe")){
              content = children.get(4).toString();
           }else{
               content = children.get(3).toString();
           }

            String sourceName = aTag.getElementsByTag("article").first()
                    .getElementsByTag("div").first()
                    .getElementsByTag("div").first()
                    .getElementsByTag("span").first()
                    .getElementsByTag("span").last()
                    .text();
//            String sourceName = aTag.selectXpath("/article/div/div/span").text();

            SiteNews siteNews = new SiteNews();
            siteNews.setSourceId(detailUrl);
            siteNews.setSourceName(logo);
            siteNews.setTitle(title);
//            timeElement.text() + "|"+
//            siteNews.setDescription( .);
            siteNews.setAddTime(parse1);
            siteNews.setShowTime(new Date());
            siteNews.setContent(content);
            siteNews.setStatus(1);
            siteNews.setSourceName(sourceName);
            siteNewsMapper.insert(siteNews);
        }


        return 1;
    }

    private String handlerMonth(String s) {
        switch (s){
            case "Jan":
                return "1";
            case "Feb":
                return "2";
            case "Mar":
                return "3";
            case "Apr":
                return "4";
            case "May":
                return "5";
            case "Jun":
                return "6";
            case "Jul":
                return "7";
            case "Aug":
                return "8";
            case "Sep":
                return "9";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
        }
        return "";
    }


    /*
    *抓取新闻专用
    * type：新闻類型：1、财经要闻，2、经济数据，3、全球股市，4、7*24全球，5、商品資讯，6、上市公司，7、全球央行
    * */
   /* private int addNews(Integer type, String url){
        int k = 0;
        try {
            String newlist = HttpRequest.doGrabGet(url);
            JSONObject json = JSONObject.fromObject(newlist);
            if(json != null && json.getJSONArray("items") != null && json.getJSONArray("items").size() > 0){
                for (int i = 0; i < json.getJSONArray("items").size(); i++){
                    JSONObject model = JSONObject.fromObject(json.getJSONArray("items").getString(i));
                    String newsId = model.getString("code");
                    String imgUrl = null;
                    if(model.has("imgUrl")){
                        imgUrl = model.getString("imgUrl");
                    }
                    //新闻不存在则添加
                    if(siteNewsMapper.getNewsBySourceIdCount(newsId) == 0){
                        //获取新闻详情
                        String newdata = HttpRequest.doGrabGet(PropertiesUtil.getProperty("news.main.url") + "/PC_News/Detail/GetDetailContent?id="+ newsId +"&type=1");
                        newdata = newdata.substring(1,newdata.length()-1).replace("\\\\\\\"","\"");
                        newdata = newdata.replace("\\\"","\"");
                        newdata = StringUtils.UnicodeToCN(newdata);
                        newdata = StringUtils.delHTMLTag(newdata);

                        JSONObject jsonnew = JSONObject.fromObject(newdata);
                        if(jsonnew != null && jsonnew.get("data") != null){
                            JSONObject news = JSONObject.fromObject(jsonnew.get("data"));
                            SiteNews siteNews = new SiteNews();
                            siteNews.setSourceId(newsId);
                            siteNews.setSourceName(news.getString("source"));
                            siteNews.setTitle(news.getString("title"));
                            String showTime = news.getString("showTime");
                            siteNews.setShowTime(DateTimeUtil.strToDate(showTime));
                            siteNews.setImgurl(imgUrl);
                            siteNews.setDescription(news.getString("description"));
                            siteNews.setContent(news.getString("content"));
                            siteNews.setStatus(1);
                            siteNews.setType(type);
                            siteNewsMapper.insert(siteNews);
                            k++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return k;
    }*/

}
