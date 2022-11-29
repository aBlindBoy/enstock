package com.xc.service;

import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.pojo.FundsApply;

import javax.servlet.http.HttpServletRequest;

/**
 * 配資申請
 * @author lr
 * @date 2020/07/24
 */
public interface IFundsApplyService {

    /**
     * 新增
     */
    ServerResponse insert(FundsApply model, HttpServletRequest request) throws Exception;

    /**
     * 更新
     */
    int update(FundsApply model);

    /**
     * 配資申請-保存
     */
    ServerResponse save(FundsApply model);

    /**
     * 配資申請-列表查询
     */
    ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, Integer status, HttpServletRequest request);

    /**
     * 配資申請-查询详情
     */
    ServerResponse getDetail(int id);

    /**
     * 配資申請-用戶配資列表
     */
    ServerResponse<PageInfo> getUserApplyList(int pageNum, int pageSize, int userId, HttpServletRequest request);

    /**
     * 配資申請-審核
     */
    ServerResponse audit(FundsApply model, HttpServletRequest request) throws Exception ;

    /**
     * 配資申請-用戶操盘中子帳戶
     */
    ServerResponse<PageInfo> getUserEnabledSubaccount(HttpServletRequest request);

}
