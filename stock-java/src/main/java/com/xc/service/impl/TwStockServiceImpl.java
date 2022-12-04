//package com.xc.service.impl;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.google.common.collect.Lists;
//import com.xc.common.ServerResponse;
//import com.xc.dao.StockOptionMapper;
//import com.xc.dao.TwStockMapper;
//import com.xc.pojo.StockOption;
//import com.xc.pojo.TwStock;
//import com.xc.pojo.User;
//import com.xc.service.ITwStockService;
//import com.xc.service.IUserService;
//
//import com.xc.utils.stock.TwStockApi;
//import com.xc.utils.stock.sina.SinaStockApi;
//import com.xc.vo.stock.StockListVO;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import javax.servlet.http.HttpServletRequest;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service("ITwStockService")
//@Slf4j
//public class TwStockServiceImpl implements ITwStockService {
//
//    @Autowired
//    TwStockMapper twStockMapper;
//
//    @Autowired
//    IUserService iUserService;
//
//    @Autowired
//    StockOptionMapper stockOptionMapper;
//
//    public ServerResponse<TwStock> findStockByName(String stockName) {
//        return ServerResponse.createBySuccess(this.twStockMapper.findStockByName(stockName));
//    }
//
//    public ServerResponse<TwStock> findStockByCode(String stockCode) {
//        TwStock stockByCode = this.twStockMapper.findStockByCode(stockCode);
//        return ServerResponse.createBySuccess(stockByCode);
//    }
//
//    public ServerResponse<TwStock> findStockById(Integer stockId) {
//        return ServerResponse.createBySuccess(this.twStockMapper.selectByPrimaryKey(stockId));
//    }
//
//    public ServerResponse addStock(String stockName, String stockCode, String stockType, String stockPlate) {
//        if (StringUtils.isBlank(stockName) || StringUtils.isBlank(stockCode) || StringUtils.isBlank(stockType))
//            return ServerResponse.createByErrorMsg("");
//        TwStock cstock = (TwStock) findStockByCode(stockCode).getData();
//        if (cstock != null)
//            return ServerResponse.createByErrorMsg("存在該股票code");
//        TwStock nstock = (TwStock) findStockByName(stockName).getData();
//        if (nstock != null)
//            return ServerResponse.createByErrorMsg("");
//        TwStock stock = new TwStock();
//        stock.setStockName(stockName.replaceAll("\\s*", ""));
//        stock.setStockCode(stockCode.replaceAll("\\s*", ""));
//        stock.setStockPlate(stockPlate.replaceAll("\\s*", ""));
//        stock.setStockType(stockType);
//        String result = TwStockApi.getTwStock(stock.getStockCode());
//        JSONObject jsonObject= JSONObject.fromObject(result);
//        List<JSONObject> list =jsonObject.getJSONArray("data");
//        JSONObject jsonObject1 = list.get(0);
//        if(!jsonObject1.has("股票名稱")){
//            return ServerResponse.createByErrorMsg("獲取不到該股票行情數據,請核對是否輸入錯誤");
//        }
//        int insertCount = this.twStockMapper.insert(stock);
//        if (insertCount > 0)
//            return ServerResponse.createBySuccessMsg("成功添加");
//        return ServerResponse.createByErrorMsg("添加失败");
//    }
//
//    public ServerResponse<List> findStockList() {
//        List<TwStock> stockList=this.twStockMapper.findStockList();
//        return ServerResponse.createBySuccess(stockList);
//    }
//    public ServerResponse<List> restoreStockList() {
//        List<TwStock> stockList=this.twStockMapper.findStockList();
//        int i=0;
//        String queryString = "";
//        for (TwStock stock:stockList){
//            String code= stock.getStockCode().replaceAll("\\s*", "");
//            String name= stock.getStockName().replaceAll("\\s*", "");
//            String plate= "上市";
//            if(i<stockList.size()-1){
//                queryString += code + ",";
//            }else{
//                queryString += code;
//            }
//            i++;
////            if(code.equals(stock.getStockCode())&&name.equals(stock.getStockName())){
////                continue;
////            }
//            stock.setStockCode(code);
//            stock.setStockName(name);
//            stock.setStockPlate(plate);
//            updateStock(stock);
//        }
//        String result = TwStockApi.getTwStock(queryString);
//        JSONObject jsonObject= JSONObject.fromObject(result);
//        List<JSONObject> list =jsonObject.getJSONArray("data");
//        for(int index= 0;index<list.size();index++){
//            JSONObject jsonObject1 = list.get(index);
//            if(!jsonObject1.has("股票名稱")){
//                int stockId = stockList.get(index).getId();
//                deleteByPrimaryKey(stockId);
//            }
//        }
//        return ServerResponse.createBySuccess(stockList);
//    }
//
//    @Override
//    public ServerResponse deleteByCode(Integer code) {
//        int updateCount = this.twStockMapper.deleteByCode(code);
//        if (updateCount > 0) {
//            return ServerResponse.createBySuccessMsg("操作成功");
//        }
//        return ServerResponse.createByErrorMsg("操作失敗");
//    }
//
//    public ServerResponse updateStock(TwStock model) {
//        if (StringUtils.isBlank(model.getId().toString()) || StringUtils.isBlank(model.getStockName()))
//            return ServerResponse.createByErrorMsg("");
//        TwStock stock = this.twStockMapper.selectByPrimaryKey(model.getId());
//        if (stock == null)
//            return ServerResponse.createByErrorMsg("");
//        stock.setStockCode(model.getStockCode());
//        stock.setStockName(model.getStockName());
//        stock.setStockPlate(model.getStockPlate());
//        stock.setStockType("tw");
//        int updateCount = this.twStockMapper.updateByPrimaryKey(stock);
//        if (updateCount > 0)
//            return ServerResponse.createBySuccessMsg("");
//        return ServerResponse.createByErrorMsg("");
//    }
//
//    public ServerResponse deleteByPrimaryKey(Integer id) {
//        int updateCount = this.twStockMapper.deleteByPrimaryKey(id);
//        if (updateCount > 0) {
//            return ServerResponse.createBySuccessMsg("操作成功");
//        }
//        return ServerResponse.createByErrorMsg("操作失敗");
//    }
//
//    public ServerResponse getTwStockPageList(int pageNum, int pageSize, String keyWords, String stockType, String stockPlate, HttpServletRequest request) {
//        PageHelper.startPage(pageNum, pageSize);
//        User user = iUserService.getCurrentUser(request);
//        List<TwStock> stockList = this.twStockMapper.findStockListByKeyWords(keyWords, stockType,stockPlate);
//        List<StockOption> stockOptions=null;
//        List<StockListVO> stockListVOS = Lists.newArrayList();
//        if(user!=null){
//            stockOptions = this.stockOptionMapper.findMyOptionByKeywords(user.getId(), null);
//        }
//        if (stockList.size() > 0) {
//            for (int i = 0; i < stockList.size(); i++) {
//
//                StockListVO stockListVO = new StockListVO();
//                stockListVO.setCode(stockList.get(i).getStockCode());
//                stockListVO.setName((stockList.get(i).getStockName()));
//                stockListVO.setStock_type(stockList.get(i).getStockType());
//                stockListVO.setStock_plate(stockList.get(i).getStockPlate());
//                //是否添加自選
//                if (user == null) {
//                    stockListVO.setIsOption("0");
//                } else {
//                    stockListVO.setIsOption("0");
//                    //stockListVO.setIsOption(iStockOptionService.isMyOption(user.getId(), stockList.get(i).getStockCode()));
//                    for (StockOption stockOption : stockOptions) {
//                        if (stockList.get(i).getStockCode().equals(stockOption.getStockCode())) {
//                            stockListVO.setIsOption("1");
//                        }
//                    }
//                }
//                stockListVOS.add(stockListVO);
//            }
//        }else{
//            String result = TwStockApi.getTwStock(keyWords);
//            log.info("没有查询到数据，请求接口查询:{}",result);
//            synchronized (keyWords){
//                StockListVO stockListVO = TwStockApi.assembleStockListVO(result);
//                this.addStock(stockListVO.getName(), stockListVO.getCode());
//                TwStock stockByCode =  this.twStockMapper.findStockByCode(stockListVO.getCode());
//                stockList.add(stockByCode);
//            }
//
//        }
//
//        PageInfo pageInfo = new PageInfo(stockList);
//        pageInfo.setList(stockListVOS);
//        return ServerResponse.createBySuccess(pageInfo);
//    }
//
//
//    public ServerResponse addStock(String stockName, String stockCode) {
//        if (StringUtils.isBlank(stockName) || StringUtils.isBlank(stockCode) )
//            return ServerResponse.createByErrorMsg("");
//        TwStock cstock = (TwStock) findStockByCode(stockCode).getData();
//        if (cstock != null)
//            return ServerResponse.createByErrorMsg("存在該股票code");
//        TwStock nstock = (TwStock) findStockByName(stockName).getData();
//        if (nstock != null)
//            return ServerResponse.createByErrorMsg("");
//        TwStock stock = new TwStock();
//        stock.setStockName(stockName);
//        stock.setStockCode(stockCode);
//        stock.setStockPlate("上櫃");
//        stock.setStockType("tw");
//        String result = TwStockApi.getTwStock(stock.getStockCode());
//        JSONObject jsonObject= JSONObject.fromObject(result);
//        List<JSONObject> list =jsonObject.getJSONArray("data");
//        JSONObject jsonObject1 = list.get(0);
//        if(!jsonObject1.has("股票名稱")){
//            return ServerResponse.createByErrorMsg("獲取不到該股票行情數據,請核對是否輸入錯誤");
//        }
//        int insertCount = this.twStockMapper.insert(stock);
//        if (insertCount > 0)
//            return ServerResponse.createBySuccessMsg("成功添加");
//        return ServerResponse.createByErrorMsg("添加失败");
//    }
//
//}
