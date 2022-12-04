package com.xc.service;


import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.pojo.StockOption;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IStockOptionService {
  ServerResponse<PageInfo> findMyStockOptions(String paramString, HttpServletRequest paramHttpServletRequest, int paramInt1, int paramInt2);
  
  ServerResponse isOption(Integer paramInteger, String paramString);

  String isMyOption(Integer paramInteger, String paramString);

  List<StockOption> allOption(Integer id);
}
