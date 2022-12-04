//package com.xc.controller;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.http.HttpUtil;
//import com.xc.common.ServerResponse;
//import com.xc.pojo.TwStock;
//import com.xc.service.ITwStockService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.stereotype.Controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import org.springframework.web.bind.annotation.RequestParam;
//
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//
//@Controller
//@RequestMapping({"/api/tw/stock/"})
//public class TwStockApiController {
//    @Autowired
//    ITwStockService iTwStockService;
//
//
//    //获取台股列表
//    @RequestMapping({"list.do"})
//    @ResponseBody
//    public ServerResponse list() {
//        return this.iTwStockService.findStockList();
//    }
//
//    @RequestMapping({"restoreList.do"})
//    @ResponseBody
//    public ServerResponse restoreList() {
//        return this.iTwStockService.restoreStockList();
//    }
//
//    //根据id查询股票信息
//    @RequestMapping({"selectTwStockById.do"})
//    @ResponseBody
//    public ServerResponse selectTwStock(Integer id) {
//        return this.iTwStockService.findStockById(id);
//    }
//
//    //根据code查询股票信息
//    @RequestMapping({"selectTwStockByCode.do"})
//    @ResponseBody
//    public ServerResponse selectTwStockByCode(String stockCode) {
//        return this.iTwStockService.findStockByCode(stockCode);
//    }
//
//    //根据name查询股票信息
//    @RequestMapping({"selectTwStockByName.do"})
//    @ResponseBody
//    public ServerResponse selectTwStockByName(String stockName) {
//        return this.iTwStockService.findStockByName(stockName);
//    }
//    //查询官网PC端交易 所有股票信息及指定股票信息
//    @RequestMapping({"getTwStock.do"})
//    @ResponseBody
//    public ServerResponse getTwStock(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "stockType", required = false) String stockType, @RequestParam(value = "keyWords", required = false) String keyWords,@RequestParam(value = "stockPlate", required = false) String stockPlate, HttpServletRequest request) {
//        return this.iTwStockService.getTwStockPageList(pageNum, pageSize, keyWords, stockType, stockPlate, request);
//    }
//
//
//
//
//}