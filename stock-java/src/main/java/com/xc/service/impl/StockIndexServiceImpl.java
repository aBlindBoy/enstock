package com.xc.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.xc.common.ServerResponse;
import com.xc.dao.StockIndexMapper;
import com.xc.dao.StockOptionMapper;
import com.xc.pojo.StockIndex;
import com.xc.pojo.StockOption;
import com.xc.pojo.User;
import com.xc.service.IStockIndexService;
import com.xc.service.IStockOptionService;
import com.xc.service.IUserService;
import com.xc.utils.HttpClientRequest;
import com.xc.utils.PropertiesUtil;
import com.xc.vo.stock.MarketVO;
import com.xc.vo.stockindex.StockIndexVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("iStockIndexService")
public class StockIndexServiceImpl implements IStockIndexService {
    private static final Logger log = LoggerFactory.getLogger(StockIndexServiceImpl.class);

    @Autowired
    StockOptionMapper stockOptionMapper;
    @Autowired
    StockIndexMapper stockIndexMapper;

    @Autowired
    IUserService iUserService;

    @Autowired
    IStockOptionService iStockOptionService;


    public ServerResponse listByAdmin(Integer homeShow, Integer listShow, Integer transState, String indexCode, String indexName, int pageNum, int pageSize, HttpServletRequest request) {
        PageHelper.startPage(pageNum, pageSize);
        List<StockIndex> stockIndexList = this.stockIndexMapper.listByAdmin(homeShow, listShow, transState, indexCode, indexName);
        List<StockIndexVO> stockIndexVOS = Lists.newArrayList();
        String httpResult=getSinaDatas(stockIndexList);
        for(StockIndex stockIndex:stockIndexList){
            stockIndexVOS.add(assembleStockIndexVO(stockIndex,httpResult));
        }
        PageInfo pageInfo = new PageInfo(stockIndexList);
        pageInfo.setList(stockIndexVOS);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private StockIndexVO assembleStockIndexVO(StockIndex stockIndex,String httpResult) {
        StockIndexVO stockIndexVO = new StockIndexVO();
        stockIndexVO.setId(stockIndex.getId());
        stockIndexVO.setIndexName(stockIndex.getIndexName());
        stockIndexVO.setIndexCode(stockIndex.getIndexCode());
        stockIndexVO.setIndexGid(stockIndex.getIndexGid());
        stockIndexVO.setHomeShow(stockIndex.getHomeShow());
        stockIndexVO.setListShow(stockIndex.getListShow());
        stockIndexVO.setTransState(stockIndex.getTransState());
        stockIndexVO.setDepositAmt(stockIndex.getDepositAmt());
        stockIndexVO.setTransFee(stockIndex.getTransFee());
        stockIndexVO.setEachPoint(stockIndex.getEachPoint());
        stockIndexVO.setMinNum(stockIndex.getMinNum());
        stockIndexVO.setMaxNum(stockIndex.getMaxNum());
        stockIndexVO.setAddTime(stockIndex.getAddTime());
        stockIndexVO.setTDesc(stockIndex.getTDesc());
        return setStockIndexVO(stockIndexVO,httpResult);
    }

    /**
     * ???stockIndexVO????????????????????????sina???????????? ??????,??????????????????????????????
     * @param stockIndexVO stockIndexVO
     * @param resultString ?????????????????????sina api??????
     * @return ????????????stockIndexVO
     */
    public StockIndexVO setStockIndexVO(StockIndexVO stockIndexVO,String resultString){
        String[] stockIndexArray = resultString.split(";");
        String hqIndexGid = "";
        for (int i = 0; i < stockIndexArray.length; i++) {
            String hqstr = stockIndexArray[i];
            try {
                if (StringUtils.isNotBlank(hqstr)) {
                    hqIndexGid=hqstr.substring(hqstr.lastIndexOf("_") + 1, hqstr.lastIndexOf("="));
                    hqstr = hqstr.substring(hqstr.indexOf("\"") + 1, hqstr.lastIndexOf("\""));
                    String[] sh_arr = hqstr.split(",");
                    if(stockIndexVO.getIndexGid().equals(hqIndexGid)){
                        stockIndexVO.setCurrentPoint(sh_arr[1]);
                        stockIndexVO.setFloatPoint(sh_arr[2]);
                        stockIndexVO.setFloatRate(sh_arr[3]);
                    }
                }
            } catch (Exception e) {
                log.error("str = {} ,  e = {}", hqstr, e);
            }
        }
        return  stockIndexVO;
    }

    /**
     * ?????????????????????sina api??????
     * @param list StockIndex ??????-???????????????
     * @return httpResult sina api??????????????? ???????????????????????????????????????????????????
     * var hq_str_s_sh000001="????????????,3396.4700,-16.4777,-0.48,2706260,29053988";
     * var hq_str_s_sz399001="????????????,13528.31,32.596,0.24,331435012,38093656";
     */
    public String getSinaDatas(List<StockIndex> list){
        String queryStr = "";
        for (StockIndex stockIndex : list) {
            queryStr += "s_" + stockIndex.getIndexGid() + ",";
        }
        //String market_url = PropertiesUtil.getProperty("sina.single.stock.url") + queryStr;
        //TODO ?????????dl?????????sina data
        String HOST = PropertiesUtil.getProperty("proxy.host.api");
        String market_url = HOST + queryStr;
        String httpResult=null;
        try {
            httpResult = HttpClientRequest.doGet(market_url);
        } catch (Exception e) {
            log.error("?????? ???????????? ?????? e = {}", e);
        }
        return httpResult;
    };
    public ServerResponse updateIndex(StockIndex stockIndex) {
        if (stockIndex.getId() == null) {
            return ServerResponse.createByErrorMsg("??????id????????????");
        }

        StockIndex dbindex = this.stockIndexMapper.selectByPrimaryKey(stockIndex.getId());
        dbindex.setHomeShow(stockIndex.getHomeShow());
        dbindex.setListShow(stockIndex.getListShow());
        dbindex.setTransState(stockIndex.getTransState());
        dbindex.setDepositAmt(stockIndex.getDepositAmt());
        dbindex.setTransFee(stockIndex.getTransFee());
        dbindex.setEachPoint(stockIndex.getEachPoint());
        dbindex.setMinNum(stockIndex.getMinNum());
        dbindex.setMaxNum(stockIndex.getMaxNum());

        int updateCount = this.stockIndexMapper.updateByPrimaryKey(dbindex);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("????????????");
        }
        return ServerResponse.createByErrorMsg("????????????");
    }


    public ServerResponse addIndex(StockIndex stockIndex) {
        log.info("name = {} code = {} gid = {}", new Object[]{stockIndex
                .getIndexName(), stockIndex.getIndexCode(), stockIndex.getIndexGid()});
        if (StringUtils.isBlank(stockIndex.getIndexName()) ||
                StringUtils.isBlank(stockIndex.getIndexCode()) ||
                StringUtils.isBlank(stockIndex.getIndexGid())) {
            return ServerResponse.createByErrorMsg("??????????????????");
        }

        StockIndex nameIndex = this.stockIndexMapper.selectIndexByName(stockIndex.getIndexName());
        if (nameIndex != null) {
            return ServerResponse.createByErrorMsg("?????????????????????");
        }
        StockIndex codeIndex = this.stockIndexMapper.selectIndexByCode(stockIndex.getIndexCode());
        if (codeIndex != null) {
            return ServerResponse.createByErrorMsg("?????????????????????");
        }

        stockIndex.setAddTime(new Date());
        int insertCount = this.stockIndexMapper.insert(stockIndex);

        if (insertCount > 0) {
            return ServerResponse.createBySuccessMsg("????????????");
        }
        return ServerResponse.createByErrorMsg("????????????");
    }


    public ServerResponse queryHomeIndex() {
        List<StockIndex> list = this.stockIndexMapper.queryHomeIndex();
        //?????????????????????stockIndexVO??????
        List<StockIndexVO> stockIndexVOS = Lists.newArrayList();
        String httpResult=getSinaDatas(list);
        for(StockIndex stockIndex:list){
            stockIndexVOS.add(assembleStockIndexVO(stockIndex,httpResult));
        }
        return ServerResponse.createBySuccess(stockIndexVOS);
    }

    public ServerResponse queryListIndex(HttpServletRequest request) {
        List<StockIndex> list = this.stockIndexMapper.queryListIndex();
        List<StockIndexVO> stockIndexVOS = Lists.newArrayList();
        User user = iUserService.getCurrentUser(request);
        String httpResult=getSinaDatas(list);
        List<StockOption> stockOptions=null;
        if(user!=null){
            stockOptions = this.stockOptionMapper.findMyOptionByKeywords(user.getId(), null);
        }
        for(StockIndex stockIndex:list){
            StockIndexVO stockIndexVO=assembleStockIndexVO(stockIndex,httpResult);
            //??????????????????
            if (user == null) {
                stockIndexVO.setIsOption("0");
            } else {
                stockIndexVO.setIsOption("0");
                //stockIndexVO.setIsOption(iStockOptionService.isMyOption(user.getId(), stockIndex.getIndexCode()));
                for(StockOption stockOption:stockOptions){
                    if(stockIndex.getIndexCode().equals(stockOption.getStockCode())){
                        stockIndexVO.setIsOption("1");
                    }
                }
            }
            stockIndexVOS.add(stockIndexVO);
        }
        return ServerResponse.createBySuccess(stockIndexVOS);
    }


    public ServerResponse queryTransIndex(Integer indexId) {
        StockIndex stockIndex = this.stockIndexMapper.selectByPrimaryKey(indexId);
        if (1 == stockIndex.getTransState().intValue()) {
            return ServerResponse.createBySuccessMsg("?????????");
        }
        return ServerResponse.createByErrorMsg("????????????");
    }


    public MarketVO querySingleIndex(String indexCode) {
        //String market_url = PropertiesUtil.getProperty("sina.single.market.url");
        //TODO ?????????dl?????????sina data
        String HOST = PropertiesUtil.getProperty("proxy.host.api");
        String market_url=HOST+"s_";
        String result = null;
        try {
            market_url = market_url + indexCode;
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.print("??????????????????????????????"+sdf.format(new Date())+"???market_url???"+market_url + "\n");

            result = HttpClientRequest.doGet(market_url);
            //System.out.print("??????????????????????????????"+sdf.format(new Date())+"???result???"+result + "\n");
        } catch (Exception e) {
            log.error("?????? ???????????? ?????? e = {}", e);
        }


        MarketVO marketVO = null;
        try {
            if (StringUtils.isNotBlank(result)) {
                result = result.substring(result.indexOf("\"") + 1, result.lastIndexOf("\""));

                marketVO = new MarketVO();
                if (result.contains(",")) {
                    String[] sh01_arr = result.split(",");
                    marketVO.setName(sh01_arr[0]);
                    marketVO.setNowPrice(sh01_arr[1]);
                    marketVO.setIncrease(sh01_arr[2]);
                    marketVO.setIncreaseRate(sh01_arr[3]);
                }
            }
        } catch (Exception e) {
            log.error("???????????????????????? str = {} ,  e = {}", result, e);
        }

        return marketVO;
    }


    public StockIndex selectIndexById(Integer indexId) {
        return this.stockIndexMapper.selectByPrimaryKey(indexId);
    }
}
