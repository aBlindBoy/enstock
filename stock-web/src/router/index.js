import Vue from 'vue'
import Router from 'vue-router'
import Home from '../pages/Home/index' // 首页
import Stock from '../pages/stock/index' // 股票大厅
import Transaction from '../pages/transaction/index' // 交易
import Product from '../pages/product/index' // 产品介绍
import Introduce from '../pages/introduce/index' // 公司简介
import Down from '../pages/down/index' // 产品介绍
import Notice from '../pages/notice/index' // 公告
import NoticeDetail from '../pages/notice/components/detail' // 公告详情
import User from '../pages/user/index' // 用户中心
import CashDetail from '../pages/user/components/list' // 资金记录
import DetailList from '../pages/user/components/table/detail' // 资金明细
import WithdrawList from '../pages/user/components/table/withdraw' // 提现记录
import RechargeList from '../pages/user/components/table/recharge' // Recharge记录
import Recharge from '../pages/user/components/cash/recharge' //Recharge 
import Withdraw from '../pages/user/components/cash/withdraw' // 提现
import ChangeCash from '../pages/user/components/change' // 资产互转
import HoldPosition from '../pages/user/components/transform/holdposition' // 持仓单
import IndexHoldPosition from '../pages/user/components/transform/indexholdposition' // 持仓单 指数
import FuturesHoldPosition from '../pages/user/components/transform/futuresholdposition' // 持仓单 指数
import SellOrder from '../pages/user/components/transform/sellorder' // 平仓单
import IndexSellOrder from '../pages/user/components/transform/indexsellorder' // 平仓单 指数
import FuturesSellOrder from '../pages/user/components/transform/futuressellorder' // 平仓单 指数
import Bank from '../pages/user/components/save/bank' // 银行卡
import Auth from '../pages/user/components/save/auth' // 实名认证
import ChangePwd from '../pages/user/components/save/changepwd' // 修改银行卡
import Login from '../pages/login/login' // 登录
import Register from '../pages/login/register' // 注册
import Forget from '../pages/login/forget' // 忘记密码
import Message from '../pages/user/components/table/message' // 站内消息
import Ipo from '../pages/user/components/table/ipo' // 新股申購

//============================分仓配资-相关，2020年7月25日19:14:55=========================================
import Funds from '../pages/funds/index' // 分仓配资
import Days from '../pages/funds/components/days' // 按天配资
import Applyfund from '../pages/funds/components/table/applyfund' // 我的配资
import FundsHoldPosition from '../pages/user/components/transform/fundsholdposition' // 分仓持仓单
import FundsSellOrder from '../pages/user/components/transform/fundssellorder' // 分仓平仓单
import Funding from '../pages/funds/components/funding' // 配资详情
import Contract from '../pages/funds/components/contract' // 合同


import Enterprise from '../pages/enterprise/index' // 企业公告
import enterpriseDetails from '../pages/enterprise/enterprise-details' // 企业公告详情



// import HoldPositions from '@/pages/holdPositions/index'
// import CapitalDetail from '@/pages/capitalDetail/index'
// import Entry from '@/pages/entry/index'
// import Exit from '@/pages/exit/index'
// import Login from '@/pages/login/index'
Vue.use(Router)

export default new Router({
  // mode :'history',
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      meta: { title: 'home' },
      component: Home
    },
    {
      path: '/stock',
      name: 'stock',
      meta: { title: 'stock hall' },
      component: Stock
    },
    {
      path: '/transaction',
      name: 'transaction',
      meta: { title: 'trading floor' },
      component: Transaction
    },
    {
      path: '/product',
      name: 'product',
      meta: { title: 'product description' },
      component: Product
    },
    {
      path: '/introduce',
      name: 'introduce',
      meta: { title: 'Company Profile' },
      component: Introduce
    }, {
      path: '/down',
      name: 'down',
      meta: { title: 'software download' },
      component: Down
    }, {
      path: '/notice',
      name: 'notice',
      meta: { title: 'announcement' },
      component: Notice
    }, {
      path: '/noticedetail',
      name: 'noticedetail',
      meta: { title: 'Announcement Details' },
      component: NoticeDetail
    }, {
      path: '/user',
      name: 'user',
      meta: { title: 'User Center' },
      component: User
    }, {
      path: '/message',
      name: 'message',
      meta: { title: 'Station news' },
      component: Message
    }, {
      path: '/ipo',
      name: 'ipo',
      meta: { title: 'IPO subscription' },
      component: Ipo
    },{
      path: '/cashdetail',
      name: 'cashdetail',
      meta: { title: 'Funding records' },
      component: CashDetail
    }, {
      path: '/detaillist',
      name: 'detaillist',
      meta: { title: 'Funding Details' },
      component: DetailList
    }, {
      path: '/withdrawlist',
      name: 'withdrawlist',
      meta: { title: 'Withdrawal records' },
      component: WithdrawList
    }, {
      path: '/rechargelist',
      name: 'rechargelist',
      meta: { title: 'Recharge records' },
      component: RechargeList
    }, {
      path: '/recharge',
      name: 'recharge',
      meta: { title: 'Recharge' },
      component: Recharge
    }, {
      path: '/withdraw',
      name: 'withdraw',
      meta: { title: 'Withdrawal' },
      component: Withdraw
    }, {
      path: '/changeCash',
      name: 'changeCash',
      meta: { title: 'Asset account transfer' },
      component: ChangeCash
    }, {
      path: '/holdposition',
      name: 'holdposition',
      meta: { title: 'Financing position order Number' },
      component: HoldPosition
    }, {
      path: '/indexholdposition',
      name: 'indexholdposition',
      meta: { title: 'Index Position Receipt' },
      component: IndexHoldPosition
    }, {
      path: '/futuressell',
      name: 'futuressell',
      meta: { title: 'futures close' },
      component: FuturesSellOrder
    }, {
      path: '/futuresholdposition',
      name: 'futuresholdposition',
      meta: { title: 'Futures Position Receipt' },
      component: FuturesHoldPosition
    }, {
      path: '/sell',
      name: 'sell',
      meta: { title: 'Closing order' },
      component: SellOrder
    }, {
      path: '/indexsell',
      name: 'indexsell',
      meta: { title: 'Index Closing Order' },
      component: IndexSellOrder
    }, {
      path: '/bank',
      name: 'bank',
      meta: { title: 'financial account' },
      component: Bank
    }, {
      path: '/auth',
      name: 'auth',
      meta: { title: 'Verified' },
      component: Auth
    }, {
      path: '/changepwd',
      name: 'changepwd',
      meta: { title: 'change Password' },
      component: ChangePwd
    },
    {
      path: '/login',
      name: 'login',
      meta: { title: 'Log in' },
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      meta: { title: 'register' },
      component: Register
    }, {
      path: '/forget',
      name: 'forget',
      meta: { title: 'Forgot password' },
      component: Forget
    },
    //============================分仓配资-相关，2020年7月25日19:14:55=========================================
    {
      path: '/funds',
      name: 'funds',
      meta: { title: 'Homepage' },
      component: Funds
    },
    {
      path: '/days',
      name: 'days',
      meta: { title: 'Daily allocation' },
      component: Days
    },
    {
      path: '/applyfund',
      name: 'applyfund',
      meta: { title: 'my allocation' },
      component: Applyfund
    }, {
      path: '/fundsholdposition',
      name: 'fundsholdposition',
      meta: { title: 'Allotment position receipt' },
      component: FundsHoldPosition
    }, {
      path: '/fundssellorder',
      name: 'fundssellorder',
      meta: { title: 'Funding closing order' },
      component: FundsSellOrder
    }, {
      path: '/funding',
      name: 'funding',
      meta: { title: 'Funding details' },
      component: Funding
    }, {
      path: '/contract',
      name: 'contract',
      meta: { title: 'contract' },
      component: Contract
    },


    {
      path: '/enterprise',
      name: 'enterprise',
      meta: { title: 'news' },//企業公告
      component: Enterprise
    },
    {
      path: '/enterprise-details',
      name: 'enterprise-details',
      meta: { title: 'news' },//企業公告詳情
      component: enterpriseDetails
    }
    
  ]
})
