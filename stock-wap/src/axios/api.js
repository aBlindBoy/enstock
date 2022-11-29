import { post, get } from '@/axios/index'
import $ from "jquery";
// import APIUrl from '@/axios/api.url'

// var img_url = APIUrl.util.image // 这个就是图片上传的api url

// 就可以使用 post 和 get 了
// 获取产品配置信息
export function getProductSetting (options) {
  return post('/api/admin/getProductSetting.do', options)
}


// 申購 弃用
// export function xingusg (options) {
//   return get('/new/public/index.php/api/Newlist/index', options)
// }
// // 申購提交 弃用
// export function xingusgs (options) {
//   return post('/new/public/index.php/api/Lists/add', options)
// }

// //新股冻结资金 弃用
// export function getprice (options) {
//   return get('/new/public/index.php/api/Lists/getprice', options)
// }


// // 申購提交列表 弃用
// export function xingusgsList (options) {
//   return get('/new/public/index.php/api/Lists/index', options)
// }

/**获取所有新股 */
export function getHiStockList (options) {
  return get('/user/subscribe/getHiStockList.do', options)
}

// 申購
export function saveStockSubscribe (options) {
  return post('/user/subscribe/saveStockSubscribe.do', options)
}


// 获取申购记录
export function getStockSubscribeHistoryList (options) {
  return post('/user/subscribe/getStockSubscribeHistoryList.do', options)
}

// 繳費
export function payStockSubscribe (options) {
  return post('/user/subscribe/payStockSubscribe.do', options)
}



// 登录
export function login (options) {
  return post('/api/user/login.do', options)
}

// 注册
export function register (options) {
  return post('/api/user/reg.do', options)
}

// 注销登录
export function logout (options) {
  return post('/api/user/logout.do', options)
}

// 验证是否注册
export function checkPhone (options) {
  return post('/api/user/checkPhone.do', options)
}

// 更改密码 -- 忘记密码
export function forgetPas (options) {
  return get('/api/user/updatePwd.do', options)
}

// 修改密码
export function changePassword (options) {
  return get('/user/updatePwd.do', options)
}

// 获取验证码  -- 注册
export function getCode (options) {
  return get('/api/sms/sendRegSms.do', options)
}

// 获取验证码  -- 忘记密码
export function sendForgetSms (options) {
  return get('/api/sms/sendForgetSms.do', options)
}

// 获取图片验证码   -- 查看验证码
export function getCode2 (options) {
  return get('/code/getCode.do', options)
}

// 验证图片验证码 -- 验证
export function checkCode (options) {
  return get('/code/checkCode.do', options)
}

// /*** 首页 ****/
// 查询首页显示的指数
// export function getIndexMarket (options) {
//   return get('/api/index/queryHomeIndex.do', options)
// }

// 查询列表页显示的指数
export function getListMarket (options) {
  return get('/api/index/queryListIndex.do', options)
}

// 查询指数是否可交易
export function getTransMarket (options) {
  return get('/api/index/queryTransIndex.do', options)
}

// 获取大盘指数
export function getMarket (options) {
  return get('/api/stock/getMarket.do', options)
}

// 股票列表数据
export function getStock (options) {
  return get('/api/stock/getStock.do', options)
}

// 单只股票行情数据
export function getSingleStock (options) {
  return post('/api/stock/getSingleStock.do', options)
}

// 单只指数行情数据
export function getSingleIndex (options) {
  return post('/api/index/querySingleIndex.do', options)
}

// 添加自选
export function addOption (options) {
  return post('/user/addOption.do', options)
}

// 删除自选
export function delOption (options) {
  return post('/user/delOption.do', options)
}

// 获取期货列表
export function getFutures (options) {
  return get('/api/futures/queryList.do', options)
}

// 获取首页显示的期货列表
export function getHomeFutures (options) {
  return get('/api/futures/queryHome.do', options)
}

// 查询期货产品的交易状态
export function queryTrans (options) {
  return get('/api/futures/queryTrans.do', options)
}

// 查询基币的汇率，对外暴露
export function queryExchange (options) {
  return get('/api/futures/queryExchange.do', options)
}

// 查询单个期货产品的行情（行情源的数据）
export function querySingleMarket (options) {
  return get('/api/futures/querySingleMarket.do', options)
}

// 期货下单
export function buyFutures (options) {
  return post('/user/buyFutures.do', options)
}

// 下单
export function buy (options) {
  return post('/user/buy.do', options)
}

// 指数下单
export function indexBuy (options) {
  return post('/user/buyIndex.do', options)
}

// 用户平仓
export function sell (options) {
  return post('/user/sell.do', options)
}

// 指数平仓
export function sellIndex (options) {
  return post('/user/sellIndex.do', options)
}

// 期货平仓
export function sellFutures (options) {
  return post('/user/sellFutures.do', options)
}

// /***用户中心***/
// 用户资金户转
export function AmtChange (options) {
  return post('/user/transAmt.do', options)
}

// 用户详情
export function getUserInfo (options) {
  return post('/user/getUserInfo.do', options)
}

// 添加银行卡
export function addBankCard (options) {
  return post('/user/bank/add.do', options)
}

// 修改银行卡
export function updateBankCard (options) {
  return post('/user/bank/update.do', options)
}

// 获取银行卡信息
export function getBankCard (options) {
  return post('/user/bank/getBankInfo.do', options)
}

// 获取我的持仓单
export function getOrderList (options) {
  return post('/user/position/list.do', options)
}

// 获取我的持仓单 - 指数
export function getIndexOrderList (options) {
  return post('/user/index/position/list.do', options)
}

// 获取我的持仓单 - 期货
export function getFuturesOrderList (options) {
  return post('/user/futures/position/list.do', options)
}

// 获取我的自选列表
export function getMyList (options) {
  return post('/user/option/list.do', options)
}

// 实名认证
export function userAuth (options) {
  return post('/user/auth.do', options)
}

// 资金明细
export function cashDetail (options) {
  return post('/user/cash/list.do', options)
}

// 提现记录
export function rechargeList (options) {
  return post('/user/recharge/list.do', options)
}

// Recharge记录
export function withdrawList (options) {
  return post('/user/withdraw/list.do', options)
}

// Recharge
export function inMoney (options) {
  return post('/user/recharge/inMoney.do', options)
}

// 提现
export function outMoney (options) {
  return post('/user/withdraw/outMoney.do', options)
}

// 取消提现
export function canceloutMoney (options) {
  return post('/user/withdraw/cancel.do', options)
}

// 修改Recharge，上传汇款凭证
export function updateMoney (options) {
  return post('/user/recharge/updateMoney.do', options)
}

// k线图
export function getMinK (options) {
  return post('/api/stock/getMinK.do', options)
}

// k线图
export function getMinKEcharts (options) {
  return post('/api/stock/getMinK_Echarts.do', options)
}

// 是否已添加自选
export function isOption (options) {
  return post('/user/isOption.do', options)
}

// 获取网站设置信息
export function getSetting (options) {
  return post('/api/admin/getSetting.do', options)
}

// 获取指数网站设置信息
export function getIndexSetting (options) {
  return post('/api/admin/getIndexSetting.do', options)
}

// 获取期货网站设置信息
export function getFuturesSetting (options) {
  return post('/api/admin/getFuturesSetting.do', options)
}

// 获取首页banner
export function getBannerByPlat (options) {
  return post('/api/site/getBannerByPlat.do', options)
}

// 公告列表
export function getArtList (options) {
  return post('/api/art/list.do', options)
}

// 公告详情
export function getArtDetail (options) {
  return post('/api/art/detail.do', options)
}

// 获取支付渠道
export function getPayInfo (options) {
  return post('/api/site/getPayInfo.do', options)
}

// 获取单个渠道信息
export function getPayInfoDetail (options) {
  return post('/api/site/getPayInfoById.do', options)
}

// 获取网站设置信息
export function getInfoSite (options) {
  return post('/api/site/getInfo.do', options)
}

// k线图 分时
export function getMinuteLine (options) {
  return post('/api/realTime/findStock.do', options)
}

// 新增渠道  支付宝扫码
export function getjuhe1 (options) {
  return post('/user/pay/juhe1.do', options)
}

//H5支付
export function getjuheH5 (options) {
  return post('/user/pay/juheh5.do', options)
}

// 支付渠道
export function payLInk (url, options) {
  return post(url, options)
}

// 图片上传 uploadimg
export function uploadimg (options) {
  return post('/user/upload.do', options)
}

// 查询点差费率
export function findSpreadRateOne (options) {
  return post('/api/user/findSpreadRateOne.do', options)
}

// ==================最新修改内容：日线、添加自选等，2020年7月10日15:37:20======================
// 期货分钟-k线图
export function getFuturesMinKEcharts (options) {
  return post('/api/stock/getFuturesMinK_Echarts.do', options)
}

// 指数分钟-k线图
export function getIndexMinKEcharts (options) {
  return post('/api/stock/getIndexMinK_Echarts.do', options)
}

// 股票日线图
export function getDayK (options) {
  return post('/api/stock/getDayK.do', options)
}

// 期货日线图
export function getFuturesDayK (options) {
  return post('/api/stock/getFuturesDayK.do', options)
}

// 指数日线图
export function getIndexDayK (options) {
  return post('/api/stock/getIndexDayK.do', options)
}

// 查询期货详情
export function queryFuturesByCode (options) {
  return get('/api/futures/queryFuturesByCode.do', options)
}


// ==================最新修改内容：新版-新闻资讯、交易大厅，2020年8月26日10:39======================

// 查询新闻
export function queryNewsList () {
  return get(`/api/news/getNewsList.do?pageNum=1&pageSize=15`, {})
}
// 查询新闻详情
export function getNewDetail(id) {
  return get(`/api/news/getDetail.do?id=${id}`, {})
}

// 配资申请-用户配资列表
export function getUserApplyList (options) {
  return post('/user/funds/getUserApplyList.do', options)
}

// -----分仓配资----- 2020 08 30 

export function getFundsSetting (options) {
  return post('/user/funds/getFundsSetting.do', options)
}

// 查询配资类型杠杆
export function getFundsTypeList (options) {
  return post('/user/funds/getFundsTypeList.do', options)
}

// 配资申请-添加
export function addFundsApply (options) {
  return post('/user/funds/addFundsApply.do', options)
}
//分仓下单
export function buyFunds (options) {
  return post('/user/funds/buyFunds.do', options)
}

// 配资申请-用户操盘中子账户
export function getUserSubaccount (options) {
  return post('/user/funds/getUserSubaccount.do', options)
}

// 获取消息列表
export function getNoticeList(options) {
  return post('/user/cash/getMessagelist.do', options)
}

// 分仓交易-获取我的配资持仓单
export function getFundsOrderList (options) {
  return post('/user/funds/fundsList.do', options)
}

// 分仓交易-配资平仓
export function sellFunds (options) {
  return post('/user/funds/sellFunds.do', options)
}

// 获取台湾股票列表
export function getTwStockListApi (options) {
  return post('/api/tw/stock/list.do', options)
}

// 获取台湾股票分页列表
export function getTwStockPageList (options) {
  return post('/api/tw/stock/getTwStock.do', options)
}

// 获取台湾股票实时行情
export function getTwStockData (stock_ids) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: `/twstock/api/v2/real-time-quotes/data?stock_id=${stock_ids}`,
      type: "GET",
      success: function(recvData) {
        resolve(recvData)
      },
      error:function(error){
        reject(error)
      }
    });
  })
}


// 获取台湾股票买卖情况
export function getTwStockExchange (stock_ids) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: `/twstock/api/v2/five-price-stock-data/data?stock_id=${stock_ids}`,
      type: "GET",
      success: function(recvData) {
        resolve(recvData)
      },
      error:function(error){
        reject(error)
      }
    });
  })
}

// 下单台股
export function buyTwStock (options) {
  return post('/user/buyTwStock.do', options)
}

//平仓
export function sellTwStock (options) {
  return post('/user/sellTwStock.do', options)
}

// https://www.tradingview.com/markets/stocks-usa/news/
// 查询新闻
// export function tradingviewNewsList () {
//   return get(``, {})
// }


export function tradingviewNewsList() {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: `/tradingview/markets/stocks-usa/news/`,
      type: "GET",
      success: function(recvData) {
        resolve(recvData)
      },
      error:function(error){
        reject(error)
      }
    });
  })
}

//https://sbcharts.investing.com/charts_xml/jschart_markets_169.json

// export function getChats (options) {
//   return get('/investing/charts_xml/jschart_markets_169.json', options)
// }

export function getChats(id) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: `/investing/charts_xml/jschart_markets_${id}.json`,
      type: "GET",
      success: function(recvData) {
        resolve(recvData)
      },
      error:function(error){
        reject(error)
      }
    });
  })
}



//获取nstock新闻数据
// export function getTwNews (options) {
//   let query=''
//   for(let key in options){
//     query+=`${key}=${options[key]}&`
//   }
//   query=query.substring(0,query.length-1)
//   return new Promise((resolve, reject) => {
//     $.ajax({
//       url: `/twstock/api/cnyes-news/?${query}`,
//       type: "GET",
//       success: function(recvData) {
//         resolve(recvData)
//       },
//       error:function(error){
//         reject(error)
//       }
//     });
//   })
// }

// //获取nstock新闻数据
// export function getTwNewsByCategory (options) {
//   let query=''
//   for(let key in options){
//     query+=`${key}=${options[key]}&`
//   }
//   query=query.substring(0,query.length-1)
//   return new Promise((resolve, reject) => {
//     $.ajax({
//       url: `/twstock/api/stock/get_stock_urlnews?${query}`,
//       type: "GET",
//       success: function(recvData) {
//         resolve(recvData)
//       },
//       error:function(error){
//         reject(error)
//       }
//     });
//   })
// }


// // 获取大盘数据 https://www.nstock.tw/api/v2/real-time-quotes-index/data?stock_id=IX0001,IX0043,IX0024,IX0039,IX0028,IX0037,IX0027,IX0030,IX0016,IX0011
// export function getRealTimeQuotesIndex() {
//   return new Promise((resolve, reject) => {
//     $.ajax({
//       url: `/twstock/api/v2/real-time-quotes-index/data?stock_id=IX0001,IX0043,IX0028`,
//       type: "GET",
//       success: function(recvData) {
//         resolve(recvData)
//       },
//       error:function(error){
//         reject(error)
//       }
//     });
//   })
// }

// //https://www.nstock.tw/news/article_c?id=216191
// export function getNewDetail(id) {
//   return new Promise((resolve, reject) => {
//     $.ajax({
//       url: `/twstocknews/article_c?id=${id}`,
//       type: "GET",
//       success: function(recvData) {
//         resolve(recvData)
//       },
//       error:function(error){
//         reject(error)
//       }
//     });
//   })
// }

//公告 api/cnyes-news/?limit=12&category=1

