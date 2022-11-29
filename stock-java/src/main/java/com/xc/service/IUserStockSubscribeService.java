package com.xc.service;


import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.pojo.UserStockSubscribe;

import javax.servlet.http.HttpServletRequest;

/**
 * 新股申购
 * @author lr
 * @date 2020/07/24
 */
public interface IUserStockSubscribeService {

    /**
     * 新增
     */
    int insert(UserStockSubscribe model);

    /**
     * 更新
     */
    ServerResponse update(UserStockSubscribe model);

    /**
     * 新股申购-保存
     */
    ServerResponse save(UserStockSubscribe model, HttpServletRequest request);

    /**
     * 發送站内信
     */
    ServerResponse sendMsg(UserStockSubscribe model, HttpServletRequest request);

    /**
     * 新股申购-列表查询
     */
    ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, HttpServletRequest request);

    /**
     * 新股申购-查询详情
     */
    ServerResponse getDetail(int id);

    /**
     * 新股申购-查询用戶最新新股申购数据
     */
    ServerResponse getOneSubscribeByUserId(Integer userId, HttpServletRequest request);

    /**
     * 新股申购-用戶提交金额
     */
    ServerResponse userSubmit(UserStockSubscribe model, HttpServletRequest request);

    /**
     * 查询histock数据
     */
    ServerResponse getHiStockList(HttpServletRequest request) throws Exception;

    /**
     * 申购历史
     */
    ServerResponse getStockSubscribeHistoryList(int pageNum, int pageSize,  HttpServletRequest request);

    ServerResponse payStockSubscribe(UserStockSubscribe model, HttpServletRequest request);
}
