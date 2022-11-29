package com.xc.dao;

import com.xc.common.ServerResponse;
import com.xc.pojo.TwStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TwStockMapper {
    int deleteByPrimaryKey(Integer paramInteger);

    int insert(TwStock paramStock);

    TwStock selectByPrimaryKey(Integer paramInteger);

    int updateByPrimaryKey(TwStock paramStock);

    TwStock findStockByCode(String paramString);

    TwStock findStockByName(String paramString);

    List<TwStock> findStockList();

    List findStockListByKeyWords(@Param("keyWords") String paramString1, @Param("stockType") String paramString3,@Param("stockPlate") String paramString4);

    int deleteByCode(Integer code);
}
