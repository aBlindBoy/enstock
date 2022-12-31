package com.xc.service.impl;


import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;

import com.google.common.collect.Lists;

import com.xc.common.ServerResponse;

import com.xc.dao.StockFuturesMapper;

import com.xc.dao.StockOptionMapper;
import com.xc.pojo.*;

import com.xc.service.IStockCoinService;

import com.xc.service.IStockFuturesService;

import com.xc.service.IStockOptionService;
import com.xc.service.IUserService;
import com.xc.utils.HttpClientRequest;

import com.xc.utils.PropertiesUtil;

import com.xc.vo.foreigncurrency.ExchangeVO;

import com.xc.vo.stockfutures.FuturesAdminListVO;

import com.xc.vo.stockfutures.FuturesVO;

import com.xc.vo.stockfutures.StockFuturesListVO;

import java.math.BigDecimal;

import java.util.Date;

import java.util.List;

import com.xc.vo.stockindex.StockIndexVO;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service("iStockFuturesService")

public class StockFuturesServiceImpl implements IStockFuturesService {

    private static final Logger log = LoggerFactory.getLogger(StockFuturesServiceImpl.class);

    @Autowired
    StockOptionMapper stockOptionMapper;

    @Autowired
    StockFuturesMapper stockFuturesMapper;

    @Autowired
    IStockCoinService iStockCoinService;

    @Autowired
    IUserService iUserService;

    @Autowired
    IStockOptionService iStockOptionService;


    public ServerResponse listByAdmin(String fuName, String fuCode, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<StockFutures> stockFutures = this.stockFuturesMapper.listByAdmin(fuName, fuCode);


        List<FuturesAdminListVO> futuresAdminListVOS = Lists.newArrayList();
        String httpResult = getSinaDatas(stockFutures);

        for (StockFutures stockFuture : stockFutures) {


            FuturesAdminListVO futuresAdminListVO = assembleFuturesAdminListVO(stockFuture, httpResult);

            futuresAdminListVOS.add(futuresAdminListVO);

        }


        PageInfo pageInfo = new PageInfo(stockFutures);

        pageInfo.setList(futuresAdminListVOS);

        return ServerResponse.createBySuccess(pageInfo);

    }

    private FuturesAdminListVO assembleFuturesAdminListVO(StockFutures stockFutures, String httpResult) {

        FuturesAdminListVO futuresAdminListVO = new FuturesAdminListVO();


        futuresAdminListVO.setId(stockFutures.getId());

        futuresAdminListVO.setFuturesName(stockFutures.getFuturesName());

        futuresAdminListVO.setFuturesCode(stockFutures.getFuturesCode());

        futuresAdminListVO.setFuturesGid(stockFutures.getFuturesGid());

        futuresAdminListVO.setFuturesUnit(stockFutures.getFuturesUnit());

        futuresAdminListVO.setFuturesStandard(stockFutures.getFuturesStandard());

        futuresAdminListVO.setCoinCode(stockFutures.getCoinCode());

        futuresAdminListVO.setHomeShow(stockFutures.getHomeShow());

        futuresAdminListVO.setListShow(stockFutures.getListShow());

        futuresAdminListVO.setDepositAmt(stockFutures.getDepositAmt());

        futuresAdminListVO.setTransFee(stockFutures.getTransFee());

        futuresAdminListVO.setMinNum(stockFutures.getMinNum());

        futuresAdminListVO.setMaxNum(stockFutures.getMaxNum());

        futuresAdminListVO.setTransState(stockFutures.getTransState());

        futuresAdminListVO.setTransAmBegin(stockFutures.getTransAmBegin());

        futuresAdminListVO.setTransAmEnd(stockFutures.getTransAmEnd());

        futuresAdminListVO.setTransPmBegin(stockFutures.getTransPmBegin());

        futuresAdminListVO.setTransPmEnd(stockFutures.getTransPmEnd());

        futuresAdminListVO.setAddTime(stockFutures.getAddTime());

        futuresAdminListVO.setTDesc(stockFutures.gettDesc());

        futuresAdminListVO.setTransPmBegin2(stockFutures.getTransPmBegin2());
        futuresAdminListVO.setTransPmEnd2(stockFutures.getTransPmEnd2());
        futuresAdminListVO.setEachPoint(stockFutures.getEachPoint());


        FuturesVO futuresVO = setFuturesVO(futuresAdminListVO, httpResult);

        if (futuresVO != null) {

            futuresAdminListVO.setNow_price(futuresVO.getNowPrice());

            futuresAdminListVO.setLast_close(futuresVO.getLastClose());

        }


        BigDecimal coinRate = new BigDecimal("0");

        ServerResponse serverResponse = getExchangeRate(stockFutures.getCoinCode());

        if (serverResponse.isSuccess()) {

            coinRate = (BigDecimal) serverResponse.getData();

        }

        futuresAdminListVO.setCoinRate(coinRate);


        return futuresAdminListVO;

    }

    /**
     * 返回期货列表的sina api数据
     *
     * @param list StockFutures 集合-从库中查出
     * @return httpResult sina api返回的数据 格式如下（多个期货股票的查询结果）
     * var hq_str_hf_XAU="1745.44,1732.600,1745.44,1745.62,1745.44,1722.70,21:35:52,1732.60,1732.22,0,0,0,2021-04-13,伦敦金（現货黄金）";
     * var hq_str_hf_CL="60.176,,60.130,60.140,60.590,59.640,21:35:41,59.700,59.640,199986.000,2,34,2021-04-13,WTI纽约原油,130058";
     */
    public String getSinaDatas(List<StockFutures> list) {
        String queryStr = "";
        for (StockFutures futures : list) {
            queryStr += futures.getFuturesGid() + ",";
        }
        //TODO 代理到dl去获取sina data
        //String market_url = PropertiesUtil.getProperty("sina.single.futures.url") + queryStr;
        String HOST = PropertiesUtil.getProperty("proxy.host.api");
        String market_url = HOST + queryStr;
        String httpResult = null;
        try {
            httpResult = HttpClientRequest.doGet(market_url);
        } catch (Exception e) {
            log.error("获取 大盘指数 出错 e = {}", e);
        }
        return httpResult;
    }

    ;

    /**
     * 往futuresVO添加查询到的实時sina指数数据
     *
     * @param stockFuturesListVO
     * @param resultString       返回指数列表的sina api数据
     * @return 封装好的futuresVO
     */
    public FuturesVO setFuturesVO(StockFuturesListVO stockFuturesListVO, String resultString) {
        String[] stockIndexArray = resultString.split(";");
        FuturesVO futuresVO = new FuturesVO();
        for (int i = 0; i < stockIndexArray.length; i++) {
            String hqstr = stockIndexArray[i];
            try {
                if (StringUtils.isNotBlank(hqstr)) {

                    //获取期货的gid
                    String futuresGid = hqstr.substring(hqstr.indexOf("str_") + 4, hqstr.indexOf("="));
                    hqstr = hqstr.substring(hqstr.indexOf("\"") + 1, hqstr.lastIndexOf("\""));
                    String[] sh01_arr = hqstr.split(",");

                    if (stockFuturesListVO.getFuturesGid().equals(futuresGid)) {
                        futuresVO.setNowPrice(sh01_arr[0]);

                        futuresVO.setLastClose(sh01_arr[1]);

                        futuresVO.setBuyPrice(sh01_arr[2]);

                        futuresVO.setSellPrice(sh01_arr[3]);

                        futuresVO.setMaxPrice(sh01_arr[4]);

                        futuresVO.setMinPrice(sh01_arr[5]);

                        futuresVO.setHourTime(sh01_arr[6]);

                        futuresVO.setTodayOpen(sh01_arr[7]);

                        futuresVO.setExtra1(sh01_arr[8]);

                        futuresVO.setExtra2(sh01_arr[9]);

                        futuresVO.setExtra3(sh01_arr[10]);

                        futuresVO.setDayTime(sh01_arr[11]);

                        futuresVO.setName(sh01_arr[12]);
                    }
                }
            } catch (Exception e) {
                log.error("str = {} ,  e = {}", hqstr, e);
            }
        }
        return futuresVO;
    }

    /**
     * 往futuresVO添加查询到的实時sina指数数据
     *
     * @param adminFuturesVO
     * @param resultString   返回指数列表的sina api数据
     * @return 封装好的futuresVO
     */
    public FuturesVO setFuturesVO(FuturesAdminListVO adminFuturesVO, String resultString) {
        String[] stockIndexArray = resultString.split(";");
        FuturesVO futuresVO = new FuturesVO();
        for (int i = 0; i < stockIndexArray.length; i++) {
            String hqstr = stockIndexArray[i];
            try {
                if (StringUtils.isNotBlank(hqstr)) {

                    hqstr = hqstr.substring(hqstr.indexOf("\"") + 1, hqstr.lastIndexOf("\""));
                    String[] sh01_arr = hqstr.split(",");
                    String futuresName = sh01_arr[sh01_arr.length - 1];
                    if (adminFuturesVO.getFuturesName().equals(futuresName)) {
                        futuresVO.setNowPrice(sh01_arr[0]);

                        futuresVO.setLastClose(sh01_arr[1]);

                        futuresVO.setBuyPrice(sh01_arr[2]);

                        futuresVO.setSellPrice(sh01_arr[3]);

                        futuresVO.setMaxPrice(sh01_arr[4]);

                        futuresVO.setMinPrice(sh01_arr[5]);

                        futuresVO.setHourTime(sh01_arr[6]);

                        futuresVO.setTodayOpen(sh01_arr[7]);

                        futuresVO.setExtra1(sh01_arr[8]);

                        futuresVO.setExtra2(sh01_arr[9]);

                        futuresVO.setExtra3(sh01_arr[10]);

                        futuresVO.setDayTime(sh01_arr[11]);

                        futuresVO.setName(sh01_arr[12]);
                    }
                }
            } catch (Exception e) {
                log.error("str = {} ,  e = {}", hqstr, e);
            }
        }
        return futuresVO;
    }

    public ServerResponse add(StockFutures stockFutures) {

        if (StringUtils.isBlank(stockFutures.getFuturesName()) ||

                StringUtils.isBlank(stockFutures.getFuturesCode())) {

            return ServerResponse.createByErrorMsg("參數不能為空");

        }


        StockFutures fuName = this.stockFuturesMapper.selectFuturesByName(stockFutures.getFuturesName());

        if (fuName != null) {

            return ServerResponse.createByErrorMsg("產品名不能重复");

        }


        StockFutures fuCode = this.stockFuturesMapper.selectFuturesByCode(stockFutures.getFuturesCode());

        if (fuCode != null) {

            return ServerResponse.createByErrorMsg("代码不能重复");

        }


        StockCoin stockCoin = this.iStockCoinService.selectCoinByCode(stockFutures.getCoinCode());

        if (stockCoin == null) {

            return ServerResponse.createByErrorMsg("基币不存在");

        }


        stockFutures.setAddTime(new Date());


        int insertCount = this.stockFuturesMapper.insert(stockFutures);

        if (insertCount > 0) {

            return ServerResponse.createBySuccessMsg("添加成功");

        }

        return ServerResponse.createByErrorMsg("添加失敗");

    }


    public ServerResponse update(StockFutures stockFutures) {

        if (stockFutures.getId() == null) {

            return ServerResponse.createByErrorMsg("修改id不能為空");

        }


        StockFutures dbFutures = this.stockFuturesMapper.selectByPrimaryKey(stockFutures.getId());

        if (dbFutures == null) {

            return ServerResponse.createByErrorMsg("產品不存在");

        }


        if (stockFutures.getFuturesName() != null) {

            return ServerResponse.createByErrorMsg("產品名不能修改");

        }


        if (stockFutures.getFuturesCode() != null) {

            return ServerResponse.createByErrorMsg("產品代码不能修改");

        }


        if (stockFutures.getFuturesGid() != null) {

            return ServerResponse.createByErrorMsg("Gid不能修改");

        }


        int updateCount = this.stockFuturesMapper.updateByPrimaryKeySelective(stockFutures);

        if (updateCount > 0) {

            return ServerResponse.createBySuccessMsg("修改成功");

        }

        return ServerResponse.createByErrorMsg("修改失敗");

    }


    public ServerResponse queryHome() {

        List<StockFutures> list = stockFuturesMapper.queryHome();


        List<StockFuturesListVO> stockFuturesListVOS = Lists.newArrayList();

        String httpResult = getSinaDatas(list);
        for (StockFutures stockFutures : list) {

            StockFuturesListVO stockFuturesListVO = assembleStockFuturesListVO(stockFutures, httpResult);

            stockFuturesListVOS.add(stockFuturesListVO);

        }

        return ServerResponse.createBySuccess(stockFuturesListVOS);

    }


    public ServerResponse queryList(HttpServletRequest request) {

        List<StockFutures> list = this.stockFuturesMapper.queryList();


        List<StockFuturesListVO> stockFuturesListVOS = Lists.newArrayList();
        User user = iUserService.getCurrentUser(request);
        String httpResult = getSinaDatas(list);
        String [] httpResults=httpResult.split(",");
        List<StockOption> stockOptions=null;
        if(user!=null){
            stockOptions = this.stockOptionMapper.findMyOptionByKeywords(user.getId(), null);
        }
        for (StockFutures stockFutures : list) {

            StockFuturesListVO stockFuturesListVO = assembleStockFuturesListVO(stockFutures, httpResult);
            //是否添加自選
            if (user == null) {
                stockFuturesListVO.setIsOption("0");
            } else {
                stockFuturesListVO.setIsOption("0");
                //stockFuturesListVO.setIsOption(iStockOptionService.isMyOption(user.getId(), stockFutures.getFuturesCode()));
                for(StockOption stockOption:stockOptions){
                    if(stockFutures.getFuturesCode().equals(stockOption.getStockCode())){
                        stockFuturesListVO.setIsOption("1");
                    }
                }
            }

            stockFuturesListVOS.add(stockFuturesListVO);

        }

        return ServerResponse.createBySuccess(stockFuturesListVOS);

    }

    //updated edison
    private StockFuturesListVO assembleStockFuturesListVO(StockFutures stockFutures, String httpResult) {

        StockFuturesListVO stockFuturesListVO = new StockFuturesListVO();


        stockFuturesListVO.setId(stockFutures.getId());

        stockFuturesListVO.setFuturesName(stockFutures.getFuturesName());

        stockFuturesListVO.setFuturesCode(stockFutures.getFuturesCode());

        stockFuturesListVO.setFuturesGid(stockFutures.getFuturesGid());

        stockFuturesListVO.setFuturesUnit(stockFutures.getFuturesUnit());

        stockFuturesListVO.setFuturesStandard(stockFutures.getFuturesStandard());

        stockFuturesListVO.setCoinCode(stockFutures.getCoinCode());

        stockFuturesListVO.setHomeShow(stockFutures.getHomeShow());

        stockFuturesListVO.setListShow(stockFutures.getListShow());

        stockFuturesListVO.setDepositAmt(stockFutures.getDepositAmt());

        stockFuturesListVO.setTransFee(stockFutures.getTransFee());

        stockFuturesListVO.setMinNum(stockFutures.getMinNum());

        stockFuturesListVO.setMaxNum(stockFutures.getMaxNum());

        stockFuturesListVO.setTransState(stockFutures.getTransState());

        stockFuturesListVO.setTransAmBegin(stockFutures.getTransAmBegin());

        stockFuturesListVO.setTransAmEnd(stockFutures.getTransAmEnd());

        stockFuturesListVO.setTransPmBegin(stockFutures.getTransPmBegin());

        stockFuturesListVO.setTransPmEnd(stockFutures.getTransPmEnd());

        stockFuturesListVO.setAddTime(stockFutures.getAddTime());

        stockFuturesListVO.setTDesc(stockFutures.gettDesc());


        FuturesVO futuresVO = setFuturesVO(stockFuturesListVO, httpResult);

        if (futuresVO != null) {

            stockFuturesListVO.setNowPrice(futuresVO.getNowPrice());

        }


        BigDecimal coinRate = new BigDecimal("0");

        ServerResponse serverResponse = getExchangeRate(stockFutures.getCoinCode());

        if (serverResponse.isSuccess()) {

            coinRate = (BigDecimal) serverResponse.getData();

        }

        stockFuturesListVO.setCoinRate(coinRate);


        return stockFuturesListVO;

    }


    public ServerResponse queryTrans(Integer futuresId) {
        StockFutures stockFutures = this.stockFuturesMapper.selectByPrimaryKey(futuresId);
        if (1 == stockFutures.getTransState().intValue()) {
            return ServerResponse.createBySuccessMsg("可交易");
        }
        return ServerResponse.createByErrorMsg("不可交易");

    }


    public FuturesVO querySingleMarket(String futuresGid) {

        String market_url = PropertiesUtil.getProperty("sina.single.futures.url");

        String result = null;

        try {

            market_url = market_url + futuresGid;

            result = HttpClientRequest.doGet(market_url);

        } catch (Exception e) {

            log.error("获取 期货行情 出错 e = {}", e);

        }


        FuturesVO futuresVO = null;

        try {

            if (StringUtils.isNotBlank(result)) {

                result = result.substring(result.indexOf("\"") + 1, result

                        .lastIndexOf("\""));


                futuresVO = new FuturesVO();

                if (result.contains(",")) {

                    String[] sh01_arr = result.split(",");

                    futuresVO.setNowPrice(sh01_arr[0]);

                    futuresVO.setLastClose(sh01_arr[1]);

                    futuresVO.setBuyPrice(sh01_arr[2]);

                    futuresVO.setSellPrice(sh01_arr[3]);

                    futuresVO.setMaxPrice(sh01_arr[4]);

                    futuresVO.setMinPrice(sh01_arr[5]);

                    futuresVO.setHourTime(sh01_arr[6]);

                    futuresVO.setTodayOpen(sh01_arr[7]);

                    futuresVO.setExtra1(sh01_arr[8]);

                    futuresVO.setExtra2(sh01_arr[9]);

                    futuresVO.setExtra3(sh01_arr[10]);

                    futuresVO.setDayTime(sh01_arr[11]);

                    futuresVO.setName(sh01_arr[12]);

                }

            }

        } catch (Exception e) {

            log.error("转换 期货行情 出错 str = {} ,  e = {}", result, e);

        }

        return futuresVO;

    }


    public ServerResponse<ExchangeVO> queryExchangeVO(String coinCode) {

        StockCoin stockCoin = this.iStockCoinService.selectCoinByCode(coinCode);

        if (stockCoin == null) {

            return ServerResponse.createByErrorMsg("不存在该币种");

        }

        if (stockCoin.getCoinGid() == null) {

            return ServerResponse.createByErrorMsg("coin gid 為空");

        }


        //String market_url = PropertiesUtil.getProperty("sina.single.exchange.url")+stockCoin.getCoinGid();
        //TODO 代理到dl去获取sina data
        String HOST = PropertiesUtil.getProperty("proxy.host.api");
        String market_url = HOST + stockCoin.getCoinGid();
        String result = null;
        try {
            result = HttpClientRequest.doGet(market_url);

        } catch (Exception e) {

            log.error("获取 外汇 行情 出错 e = {}", e);

        }


        ExchangeVO exchangeVO = null;

        try {

            if (StringUtils.isNotBlank(result)) {

                result = result.substring(result.indexOf("\"") + 1, result

                        .lastIndexOf("\""));


                exchangeVO = new ExchangeVO();

                if (result.contains(",")) {

                    String[] sh01_arr = result.split(",");

                    exchangeVO.setHourTime(sh01_arr[0]);

                    exchangeVO.setExtra1(sh01_arr[1]);

                    exchangeVO.setExtra2(sh01_arr[2]);

                    exchangeVO.setLastClose(sh01_arr[3]);

                    exchangeVO.setBofu(sh01_arr[4]);

                    exchangeVO.setTodayOpen(sh01_arr[5]);

                    exchangeVO.setMaxPrice(sh01_arr[6]);

                    exchangeVO.setMinPrice(sh01_arr[7]);

                    exchangeVO.setNowPrice(sh01_arr[8]);

                    exchangeVO.setName(sh01_arr[9]);

                }

            }

        } catch (Exception e) {

            log.error("转换 外汇行情 出错 str = {} ,  e = {}", result, e);

        }

        return ServerResponse.createBySuccess(exchangeVO);

    }


    public ServerResponse<BigDecimal> getExchangeRate(String coinCode) {

        StockCoin stockCoin = this.iStockCoinService.selectCoinByCode(coinCode);

        if (stockCoin == null) {

            return ServerResponse.createByErrorMsg("基币設置錯誤");

        }


        BigDecimal exchangeRate = null;

        ExchangeVO exchangeVO = null;

        if (stockCoin.getDynamicRate().intValue() == 0) {


            ServerResponse serverResponse = queryExchangeVO(coinCode);

            if (serverResponse.isSuccess()) {

                exchangeVO = (ExchangeVO) serverResponse.getData();

                exchangeRate = new BigDecimal(exchangeVO.getNowPrice());

            } else {

                return ServerResponse.createByErrorMsg(serverResponse.getMsg());

            }

        } else {


            exchangeRate = stockCoin.getDefaultRate();

        }


        exchangeRate = exchangeRate.setScale(2, 4);


        return ServerResponse.createBySuccess(exchangeRate);

    }


    public StockFutures selectFuturesById(Integer futuresId) {
        return this.stockFuturesMapper.selectByPrimaryKey(futuresId);
    }


    public StockFutures selectFuturesByCode(String futuresCode) {
        return this.stockFuturesMapper.selectFuturesByCode(futuresCode);
    }

}

