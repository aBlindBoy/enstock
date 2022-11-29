import { post, get } from '@/axios/index'
// import APIUrl from '@/axios/api.url'

// var img_url = APIUrl.util.image // 这个就是图片上传的api url

// 登录
export function login (options) {
  return post('/api/admin/login.do', options)
}

// 更改密码
export function changepwd (options) {
  return post('/admin/update.do', options)
}

// 注销登录
export function logout (options) {
  return post('/api/admin/logout.do', options)
}

// 验证验证码
export function checkCode (options) {
  return get('/code/checkCode.do', options)
}

// /*******资金管理********/

// 充值列表
export function rechargeList (options) {
  return post('/admin/recharge/list.do', options)
}

// 修改充值订单状态
export function updateState (options) {
  return post('/admin/recharge/updateState.do', options)
}

// 生成充值订单
export function createOrder (options) {
  return post('/admin/recharge/createOrder.do', options)
}

// 提现列表
export function withdrawList (options) {
  return post('/admin/withdraw/list.do', options)
}

// 删除充值
export function delcashList (options) {
  return post('/admin/recharge/del.do', options)
}

// 修改提现记录
export function withdrawUpdateState (options) {
  return post('/admin/withdraw/updateState.do', options)
}

// 资金记录
export function cashList (options) {
  return post('/admin/cash/list.do', options)
}

// /******用户管理*******/
// 获取用户管理数据
export function getUserManList (options) {
  return post('/admin/user/list.do', options)
}

// 获取单个用户详细数据
export function getUserDetail (options) {
  return post('/admin/user/detail.do', options)
}

// 删除用户所有信息
export function deleteDetail (options) {
  return post('/admin/user/delete.do', options)
}

// 锁定用户 / 解锁用户
export function updateLock (options) {
  return post('/admin/user/updateLock.do', options)
}

// 手动入扣款
export function updateAmt (options) {
  return post('/admin/user/updateAmt.do', options)
}

// 代理手动扣款
export function updateAgentAmt (options) {
  return post('/admin/agent/updateAgentAmt.do', options)
}

// 添加模拟账户
export function addSimulatedAccount (options) {
  return post('/admin/user/addSimulatedAccount.do', options)
}

// 获取银行卡信息

export function getBank (options) {
  return post('/admin/user/getBank.do', options)
}

// 修改银行卡信息
export function updateBank (options) {
  return post('/admin/user/updateBank.do', options)
}

// 修改用户信息
export function updateUser (options) {
  return post('/admin/user/update.do', options)
}

// 审核用户信息
export function authByAdmin (options) {
  return post('/admin/user/authByAdmin.do', options)
}

// /*******代理管理*****/
// 下级代理列表
export function getSecondAgent (options) {
  return post('/admin/agent/list.do', options)
}

// 添加代理
export function addAgent (options) {
  return post('/admin/agent/add.do', options)
}

// 修改代理
export function updateAgent (options) {
  return post('/admin/agent/update.do', options)
}

// 代理返佣统计
export function getIncomeAgent (options) {
  return get('/admin/agent/getIncome.do', options)
}

// 代理返佣统计 指数
export function getIndexIncome (options) {
  return get('/admin/agent/getIndexIncome.do', options)
}

// 代理返佣统计 期货
export function getFuturesIncome (options) {
  return get('/admin/agent/getFuturesIncome.do', options)
}

// /*******股票管理********/
// 获取股票列表
export function getStockList (options) {
  return post('/admin/stock/list.do', options)
}

// 修改锁定状态
export function updateStockLock (options) {
  return post('/admin/stock/updateLock.do', options)
}

// 修改显示状态
export function updateStockShow (options) {
  return post('/admin/stock/updateShow.do', options)
}

// 添加股票
export function addStock (options) {
  return post('/admin/stock/add.do', options)
}

// 修改股票
export function updateStock (options) {
  return post('/admin/stock/updateStock.do', options)
}

// 股票详情
export function getSingleStock (options) {
  return post('/api/stock/getSingleStock.do', options)
}

// 指数列表
export function indexList (options) {
  return post('/admin/index/list.do', options)
}

// 修改指数
export function updateIndex (options) {
  return post('/admin/index/updateIndex.do', options)
}

// 添加指数
export function addIndex (options) {
  return post('/admin/index/addIndex.do', options)
}

// 期货列表
export function futuresList (options) {
  return post('/admin/futures/list.do', options)
}

// 修改期货
export function updateFutures (options) {
  return post('/admin/futures/update.do', options)
}

// 添加期货
export function addFutures (options) {
  return post('/admin/futures/add.do', options)
}

// 获取可用的基币
export function getSelectCoin (options) {
  return post('/admin/coin/getSelectCoin.do', options)
}

// 基币列表
export function coinList (options) {
  return post('/admin/coin/list.do', options)
}

// 添加基币
export function addCoin (options) {
  return post('/admin/coin/add.do', options)
}

// 修改基币
export function editCoin (options) {
  return post('/admin/coin/update.do', options)
}

// 单只指数行情数据
export function getSingleIndex (options) {
  return post('/api/index/querySingleIndex.do', options)
}

// /*******持仓管理*******/
// 持仓列表
export function Orderlist (options) {
  return post('/admin/position/list.do', options)
}

// 创建持仓单
export function CreateOrderlist (options) {
  return post('/admin/position/create.do', options)
}

// 强制平仓
export function positionSell (options) {
  return post('/admin/position/sell.do', options)
}

// 删除平仓
export function delPositionSell (options) {
  return post('/admin/position/del.do', options)
}

// 指数持仓
export function indexPositionList (options) {
  return post('/admin/index/position/list.do', options)
}

// 指数强制平仓
export function indexPositionSell (options) {
  return post('/admin/index/position/sell.do', options)
}

// 指数锁仓
export function indexPositionLock (options) {
  return post('/admin/index/position/lock.do', options)
}

// 期货持仓
export function futuresPositionList (options) {
  return post('/admin/futures/position/list.do', options)
}

// 期货强制平仓
export function futuresPositionSell (options) {
  return post('/admin/futures/position/sell.do', options)
}

// 期货锁仓
export function futuresPositionLock (options) {
  return post('/admin/futures/position/lock.do', options)
}

// /*****日志管理*****/
// 定时任务
export function taskList (options) {
  return get('/admin/log/taskList.do', options)
}

// 短信发送日志
export function smsList (options) {
  return get('/admin/log/smsList.do', options)
}

// 登陆日志列表
export function loginList (options) {
  return get('/admin/log/loginList.do', options)
}

// 资金互转记录
export function exchangeList (options) {
  return get('/admin/log/transList.do', options)
}

// /********管理列表*********/
// 管理设置
export function settingList (options) {
  return post('/admin/list.do', options)
}

// 锁定列表
export function lockMan (options) {
  return post('/admin/updateLock.do', options)
}

// 添加管理
export function addManager (options) {
  return post('/admin/add.do', options)
}

// /******风控设置******/
// 股票风控设置
export function riskSetting (options) {
  return post('/admin/set/update.do', options)
}

// 更改指数风控设置
export function indexRiskSetting (options) {
  return post('/admin/site/index/update.do', options)
}

// 更改期货风控设置
export function futuresRiskSetting (options) {
  return post('/admin/site/futures/update.do', options)
}

// 获取指数风控设置
export function getIndexRiskSetting (options) {
  return post('/api/admin/getIndexSetting.do', options)
}

// 获取期货风控设置
export function getFuturesRiskSetting (options) {
  return post('/api/admin/getFuturesSetting.do', options)
}

// 获取网站设置信息
export function getSettingInfo (options) {
  return post('/api/admin/getSetting.do', options)
}

// 获取统计信息
export function getCountInfo (options) {
  return get('/admin/count.do', options)
}

// 获取产品设置信息
export function getProductSetting (options) {
  return post('/api/admin/getProductSetting.do', options)
}

// 修改产品设置信息
export function updateProductSetting (options) {
  return post('/admin/product/update.do', options)
}

// 查询点差设置列表
export function getSiteSpreadList (options) {
  return post('/api/admin/getSiteSpreadList.do', options)
}

// 添加点差设置
export function addSiteSpread (options) {
  return post('/api/admin/addSiteSpread.do', options)
}

// 修改点差设置
export function updateSiteSpread (options) {
  return post('/api/admin/updateSiteSpread.do', options)
}

// 获取大盘指数
export function getMarket (options) {
  return get('/api/stock/getMarket.do', options)
}

// /******支付渠道*****/
// 添加支付渠道
export function addPayChannel (options) {
  return post('/admin/pay/add.do', options)
}

// 删除支付渠道
export function delChannel (options) {
  return post('/admin/pay/del.do', options)
}

// 修改支付渠道
export function updatePayChannel (options) {
  return post('/admin/pay/update.do', options)
}

// 支付渠道列表
export function payChannelList (options) {
  return get('/admin/pay/list.do', options)
}

// /********系统基本设置********/
//  添加设置信息
export function addSettingInfo (options) {
  return post('/admin/info/add.do', options)
}

//  修改设置信息
export function changeSettingInfo (options) {
  return post('/admin/info/update.do', options)
}

//  获取设置信息
export function getSettingInfo1 (options) {
  return post('/admin/info/get.do', options)
}

//  banner
export function getBannerList (options) {
  return get('/admin/banners/list.do', options)
}

// 添加banner
export function addBanner (options) {
  return post('/admin/banners/add.do', options)
}

// 删除banner
export function delBanner (options) {
  return post('/admin/banners/delete.do', options)
}

// 修改banner
export function updateBanner (options) {
  return post('/admin/banners/update.do', options)
}

// 获取设置信息
export function getInfo (options) {
  return post('/api/site/getInfo.do', options)
}

// 锁定持仓
export function positionLock (options) {
  return post('/admin/position/lock.do', options)
}

// /*****************公告管理******************/
// 公告列表
export function artList (options) {
  return post('/admin/art/list.do', options)
}

// 添加公告
export function addArt (options) {
  return post('/admin/art/add.do', options)
}

// 修改公告
export function updateArt (options) {
  return post('/admin/art/update.do', options)
}

// 公告详情
export function artDetail (options) {
  return post('/admin/art/detail.do', options)
}

// 修改密码
export function updatePwd (options) {
  return post('/admin/updatePwd.do', options)
}

//======================【最新添加，2020年7月17日18:12:38】===========================================================

// 短信发送日志
export function messageList (options) {
  return get('/admin/log/messageList.do', options)
}

//============================分仓配资-相关，2020年7月23日19:14:55=========================================
// 分仓配资设置信息查询
export function getFundsSetting (options) {
  return post('/admin/funds/getFundsSetting.do', options)
}

// 分仓配资设置信息保存
export function saveFundsSetting (options) {
  return post('/admin/funds/saveFundsSetting.do', options)
}

// 配资杠杆列表查询
export function getFundsLeverList (options) {
  return post('/admin/funds/getFundsLeverList.do', options)
}

// 配资杠杆列表查询
export function saveFundsLever (options) {
  return post('/admin/funds/saveFundsLever.do', options)
}

// 配资券商机构-列表查询
export function getFundsDealerInstitutionsList (options) {
  return post('/admin/funds/dealer/getDealerInstitutionsList.do', options)
}

// 配资券商机构-保存
export function saveFundsDealerInstitutions (options) {
  return post('/admin/funds/dealer/saveDealerInstitutions.do', options)
}

// 配资证券信息-列表查询
export function getSecuritiesInfoList (options) {
  return post('/admin/funds/dealer/getSecuritiesInfoList.do', options)
}

// 配资证券信息-保存
export function saveSecuritiesInfo (options) {
  return post('/admin/funds/dealer/saveSecuritiesInfo.do', options)
}

// 配资证券信息-查询可用的证券信息
export function getSecuritiesEnabledList (options) {
  return post('/admin/funds/dealer/getSecuritiesEnabledList.do', options)
}

// 配资交易账户-列表查询
export function getTradingAccountList (options) {
  return post('/admin/funds/dealer/getTradingAccountList.do', options)
}

// 配资交易账户-保存
export function saveTradingAccount (options) {
  return post('/admin/funds/dealer/saveTradingAccount.do', options)
}

// 配资交易账户-查询最新交易账户编号
export function getMaxNumber (options) {
  return post('/admin/funds/dealer/getMaxNumber.do', options)
}

// 配资申请-保存
export function saveApply (options) {
  return post('/admin/funds/apply/saveApply.do', options)
}

// 配资申请-列表查询
export function getApplyList (options) {
  return post('/admin/funds/apply/getApplyList.do', options)
}

// 配资申请-审核
export function auditApply (options) {
  return post('/admin/funds/apply/auditApply.do', options)
}

// 配资追加申请-查询审核列表
export function getAppendList (options) {
  return post('/admin/funds/apply/getAppendList.do', options)
}

// 配资追加申请-查询详情
export function getAppendDetail (options) {
  return post('/admin/funds/apply/getAppendDetail.do', options)
}

// 配资追加申请-保存
export function saveAppendApply (options) {
  return post('/admin/funds/apply/saveAppendApply.do', options)
}

// 删除代理
export function delAgent (options) {
  return post('/admin/agent/delAgent.do', options)
}

// 删除公告
export function delArt (options) {
  return post('/admin/art/delArt.do', options)
}

// 获取台湾股票列表
export function getTwStockListApi (options) {
  return post('/api/tw/stock/list.do', options)
}

// 获取台湾股票分页列表
export function getTwStockPageList (options) {
  return post('/api/tw/stock/getTwStock.do', options)
}

// 新增台湾股票
export function addTwStockApi (options) {
  return post('/admin/stock/addTwStock.do', options)
}

// 删除台湾股票
export function delTwStockApi (code) {
  return post('/admin/stock/delTwStock.do?code='+code)
}

// 修改台湾股票
export function updateTwStockApi (options) {
  return post('/admin/stock/updateTwStock.do', options)
}

// 获取订单列表
export function getStockSubscribeList (options) {
  return post('/admin/subscribe/getStockSubscribeList.do', options)
}

// 更新
export function updateStockSubscribe (options) {
  return post('/admin/subscribe/updateStockSubscribe.do', options)
}