package com.xc.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;

import com.google.common.collect.Lists;

import com.xc.common.ServerResponse;

import com.xc.dao.StockMapper;

import com.xc.dao.StockOptionMapper;

import com.xc.pojo.Stock;

import com.xc.pojo.StockOption;

import com.xc.pojo.User;

import com.xc.service.IStockOptionService;

import com.xc.service.IUserService;

import com.xc.utils.HttpClientRequest;
import com.xc.utils.PropertiesUtil;
import com.xc.utils.stock.UsStockApi;
import com.xc.utils.stock.sina.SinaStockApi;

import com.xc.vo.stock.StockListVO;
import com.xc.vo.stock.StockOptionListVO;

import com.xc.vo.stock.StockVO;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service("iStockOptionService")


public class StockOptionServiceImpl implements IStockOptionService {

    private static final Logger log = LoggerFactory.getLogger(StockOptionServiceImpl.class);

    @Autowired
    StockOptionMapper stockOptionMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    StockMapper stockMapper;

    //updated edison
    public ServerResponse<PageInfo> findMyStockOptions(String keyWords, HttpServletRequest request, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        User user = this.iUserService.getCurrentUser(request);
        List<StockOption> stockOptions = this.stockOptionMapper.findMyOptionByKeywords(user.getId(), keyWords);

//        for (StockOption option : stockOptions) {
//            queryString += option.getStockGid() + ",";
//        }
//        int index=0;
//        for (StockOption option : stockOptions) {
//            if(index<stockOptions.size()-1){
//                queryString += option.getStockCode() + ",";
//            }else{
//                queryString += option.getStockCode();
//            }
//            index++;
//        }
        //String[] httpResults = SinaStockApi.getSinaStockList(queryString);
        //TODO 代理到dl去获取sina data
//        String host= PropertiesUtil.getProperty("proxy.host.api");
//        String[] httpResults = HttpClientRequest.doGet(host+queryString).split(";");
        CountDownLatch countDownLatch = new CountDownLatch(stockOptions.size());
        StockOptionListVO [] stockOptionList = new StockOptionListVO[stockOptions.size()];

//        String queryString = "";

        for (int i = 0; i < stockOptions.size(); i++) {

            int finalI = i;
            ThreadUtil.execute(()->{
//                String result= UsStockApi.getStock(stockOptions.get(finalI).getStockCode());
//                StockListVO stockVO=UsStockApi.assembleStockListVO(result);
                StockListVO stockVO = UsStockApi.getMoomooStock(stockOptions.get(finalI).getStockCode());
                StockOptionListVO stockOptionListVO =new StockOptionListVO();
                stockOptionListVO.setId(stockOptions.get(finalI).getId());
                stockOptionListVO.setStockName(stockOptions.get(finalI).getStockName());
                stockOptionListVO.setStockCode(stockOptions.get(finalI).getStockCode());
                stockOptionListVO.setStockGid(stockOptions.get(finalI).getStockGid());
                stockOptionListVO.setNowPrice(stockVO.getNowPrice());
                stockOptionListVO.setHcrate(stockVO.getHcrate().toString());
                stockOptionListVO.setPreclose_px(stockVO.getPreclose_px());
                stockOptionListVO.setOpen_px(stockVO.getOpen_px());
                Stock stock = this.stockMapper.selectByPrimaryKey(stockOptions.get(finalI).getStockId());
                //stockOptionListVO.setStock_plate(stock.getStockPlate());

                stockOptionListVO.setStock_type(stock.getStockType());
                stockOptionListVO.setIsOption("1");
                stockOptionList[finalI] = stockOptionListVO;
                countDownLatch.countDown();
            });

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<StockOptionListVO> stockOptionListVOS = Arrays.asList(stockOptionList);
//        int i = 0;
//        for (StockOption option : stockOptions) {
//            StockOptionListVO stockOptionListVO = assembleStockOptionListVO(option, httpResults[i]);
//            i++;
//            stockOptionListVO.setIsOption("1");
//            stockOptionListVOS.add(stockOptionListVO);
//        }
        PageInfo pageInfo = new PageInfo(stockOptions);

        pageInfo.setList(stockOptionListVOS);

        return ServerResponse.createBySuccess(pageInfo);

    }

    public ServerResponse isOption(Integer uid, String code) {

        StockOption stockOption = this.stockOptionMapper.isOption(uid, code);

        if (stockOption == null) {

            return ServerResponse.createBySuccessMsg("not added");

        }

        return ServerResponse.createByErrorMsg("added");

    }

    public String isMyOption(Integer uid, String code) {
        StockOption stockOption = this.stockOptionMapper.isOption(uid, code);
        if (stockOption == null) {
            return "0";
        }
        return "1";

    }

    @Override
    public List<StockOption> allOption(Integer userId) {
        return this.stockOptionMapper.allOption(userId);
    }

    //updated edison
    private StockOptionListVO assembleStockOptionListVO(StockOption option, String httpResult) {

        StockOptionListVO stockOptionListVO = new StockOptionListVO();


        stockOptionListVO.setId(option.getId().intValue());

        stockOptionListVO.setStockName(option.getStockName());

        stockOptionListVO.setStockCode(option.getStockCode());

        stockOptionListVO.setStockGid(option.getStockGid());

        StockVO stockVO = new StockVO();
        if (option.getStockGid().contains("hf")) {
            stockVO = SinaStockApi.assembleStockFuturesVO(httpResult.substring(httpResult.indexOf("=") + 2));
        } else {
            stockVO = SinaStockApi.assembleStockVO(httpResult.substring(httpResult.indexOf("=") + 2));
        }

        stockOptionListVO.setNowPrice(stockVO.getNowPrice());

        stockOptionListVO.setHcrate(stockVO.getHcrate().toString());

        stockOptionListVO.setPreclose_px(stockVO.getPreclose_px());

        stockOptionListVO.setOpen_px(stockVO.getOpen_px());

        Stock stock = this.stockMapper.selectByPrimaryKey(option.getStockId());

        stockOptionListVO.setStock_plate(stock.getStockPlate());

        stockOptionListVO.setStock_type(stock.getStockType());

        return stockOptionListVO;

    }
}
