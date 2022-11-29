package com.xc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xc.common.ServerResponse;
import com.xc.dao.FundsTradingAccountMapper;
import com.xc.pojo.FundsTradingAccount;
import com.xc.service.IFundsTradingAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 配資交易帳戶
 * @author lr
 * @date 2020/07/24
 */
@Service("IFundsTradingAccountService")
public class FundsTradingAccountServiceImpl implements IFundsTradingAccountService {

    @Resource
    private FundsTradingAccountMapper fundsTradingAccountMapper;


    @Override
    public int insert(FundsTradingAccount model) {
        int ret = 0;
        if (model == null) {
            return 0;
        }
        ret = fundsTradingAccountMapper.insert(model);
        return ret;
    }

    @Override
    public int update(FundsTradingAccount model) {
        int ret = fundsTradingAccountMapper.update(model);
        return ret>0 ? ret: 0;
    }

    /**
     * 配資交易帳戶-保存
     */
    @Override
    public ServerResponse save(FundsTradingAccount model) {
        int ret = 0;
        if(model!=null && model.getId()>0){
            ret = fundsTradingAccountMapper.update(model);
        } else{
            ret = fundsTradingAccountMapper.insert(model);
        }
        if(ret>0){
            return ServerResponse.createBySuccessMsg("操作成功");
        }
        return ServerResponse.createByErrorMsg("操作失敗");
    }

    /*配資交易帳戶-查询列表*/
    @Override
    public ServerResponse<PageInfo> getList(int pageNum, int pageSize, String keyword, Integer status, HttpServletRequest request){
        PageHelper.startPage(pageNum, pageSize);
        List<FundsTradingAccount> listData = this.fundsTradingAccountMapper.pageList(pageNum, pageSize, keyword, status);
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setList(listData);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /*配資交易帳戶-查询详情*/
    @Override
    public ServerResponse getDetail(int id) {
        return ServerResponse.createBySuccess(this.fundsTradingAccountMapper.load(id));
    }

    /**
     * 配資交易帳戶-查询最新交易帳戶编号
     */
    @Override
    public ServerResponse getMaxNumber() {
        int ret = fundsTradingAccountMapper.getMaxNumber();
        return ServerResponse.createBySuccess(String.valueOf(ret));
    }

}
