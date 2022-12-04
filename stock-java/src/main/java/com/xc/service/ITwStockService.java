//package com.xc.service;
//
//import com.xc.common.ServerResponse;
//import com.xc.pojo.TwStock;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//
//public interface ITwStockService {
//
//
//  ServerResponse<TwStock> findStockByName(String paramString);
//
//  ServerResponse<TwStock> findStockByCode(String paramString);
//
//  ServerResponse<TwStock> findStockById(Integer paramInteger);
//
//  ServerResponse addStock(String paramString1, String paramString2, String paramString3, String paramString4);
//
//  ServerResponse<List> findStockList();
//
//  ServerResponse updateStock(TwStock model);
//
//  ServerResponse deleteByPrimaryKey(Integer id);
//
//  ServerResponse getTwStockPageList(int paramInt1, int paramInt2, String paramString1, String paramString2,String paramString3,  HttpServletRequest request);
//
//  ServerResponse<List> restoreStockList();
//
//  ServerResponse deleteByCode(Integer code);
//}
