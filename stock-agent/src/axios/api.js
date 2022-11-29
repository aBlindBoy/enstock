import { post, get } from '@/axios/index'
import APIUrl from '@/axios/api.url'

// var img_url = APIUrl.util.image // 这个就是图片上传的api url

// 获取代理信息
export function getAgentInfo (options) {
  return get('/agent/getAgentInfo.do', options)
}

// 获取用户管理数据
export function getUserManList (options) {
  return get('/agent/user/list.do', options)
}

// 获取当前下级代理
export function getSecondAgent (options) {
  return get('/agent/getSecondAgent.do', options)
}

// 查询代理利润明细
export function getAgentAgencyFeeList (options) {
  return get('/agent/getAgentAgencyFeeList.do', options)
}

// 代理返佣统计
export function getIncomeAgent (options) {
  return get('/agent/getIncome.do', options)
}

// 代理返佣统计 指数
export function getIndexIncome (options) {
  return get('/agent/getIndexIncome.do', options)
}

// 代理返佣统计 期货
export function getFuturesIncome (options) {
  return get('/agent/getFuturesIncome.do', options)
}

// 获取用户持仓
export function getUserPositionList (options) {
  return get('/agent/position/list.do', options)
}

// 获取用户持仓 指数
export function getUserIndexPositionList (options) {
  return get('/agent/index/position/list.do', options)
}

// 获取用户持仓 期货
export function getUserFuturesPositionList (options) {
  return get('/agent/futures/position/list.do', options)
}

// 资金明细
export function getUserCapitalList (options) {
  return get('/agent/cash/list.do', options)
}

// 入金记录
export function getUserComeinList (options) {
  return get('/agent/recharge/list.do', options)
}

// 出金记录
export function getUserwithdrawList (options) {
  return get('/agent/withdraw/list.do', options)
}

// 添加模拟账户
export function addSimulatedAccount (options) {
  return post('/agent/addSimulatedAccount.do', options)
}

// 添加下级代理
export function addAgent (options) {
  return get('/agent/addAgent.do', options)
}

// 修改密码
export function updatePwd (options) {
  return get('/agent/updatePwd.do', options)
}

// 登录
export function login (options) {
  return post('/api/agent/login.do', options)
}

// 注销登录
export function logout (options) {
  return post('/api/agent/logout.do', options)
}

// 验证验证码
export function checkCode (options) {
  return get('/code/checkCode.do', options)
}

// 获取站点基本信息
export function getSiteInfo (options) {
  return get('/api/site/getInfo.do', options)
}

// 获取连接
export function getLink (options) {
  return get('Link/getListWithIconAndUrl.action', options)
}
