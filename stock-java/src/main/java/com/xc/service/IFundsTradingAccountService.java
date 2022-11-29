package com.xc.service;

import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.pojo.FundsTradingAccount;

import javax.servlet.http.HttpServletRequest;

/**
 * 配資交易帳戶
 * @author lr
 * @date 2020/07/24
 */
public interface IFundsTradingAccountService {

    /**
     * 新增
     */
    int insert(FundsTradingAccount model);

    /**
     * 更新
     */
    int update(FundsTradingAccount model);

    /**
     * 配資交易帳戶-保存
     */
    ServerResponse save(FundsTradingAccount model);

    /**
     * 配資交易帳戶-列表查询
     */
    ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, Integer status, HttpServletRequest request);

    /**
     * 配資交易帳戶-查询详情
     */
    ServerResponse getDetail(int id);

    /**
     * 查询最新交易帳戶编号
     */
    ServerResponse getMaxNumber();

}
