import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import UserMan from '@/pages/userMan/index'
import Statistics from '@/pages/statistics/index'

import Agent from '@/pages/agent/index'
import AgentcyFee from '@/pages/agentcyFee/index'
import HoldPositions from '@/pages/holdPositions/index'
import CapitalDetail from '@/pages/capitalDetail/index'
import Entry from '@/pages/entry/index'
import Exit from '@/pages/exit/index'
import Login from '@/pages/login/index'

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
      name: 'Home',
      meta: { title: '首页' },
      component: Home
    },
    {
      path: '/userMan',
      name: 'userMan',
      meta: { title: '用户管理' },
      component: UserMan
    },
    {
      path: '/statistics',
      name: 'statistics',
      meta: { title: '数据统计' },
      component: Statistics
    },{
      path: '/agent',
      name: 'agent',
      meta: { title: '代理管理' },
      component: Agent
    },{
      path: '/agentcyFee',
      name: 'agentcyFee',
      meta: { title: '利润明细' },
      component: AgentcyFee
    },{
      path: '/holdPositions',
      name: 'holdPositions',
      meta: { title: '持仓管理' },
      component: HoldPositions
    }, {
      path: '/capitalDetail',
      name: 'capitalDetail',
      meta: { title: '资金明细' },
      component: CapitalDetail
    }, {
      path: '/entry',
      name: 'entry',
      meta: { title: '入金明细' },
      component: Entry
    }, {
      path: '/exit',
      name: 'exit',
      meta: { title: '出金明细' },
      component: Exit
    }, {
      path: '/login',
      name: 'login',
      meta: { title: '登录' },
      component: Login
    }
  ]
})
