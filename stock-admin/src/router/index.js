import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/pages/Home'
import Login from '@/pages/login/index'

const _import = require('./import_production.js')

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'Home',
      meta: { title: '首页' },
      component: _import('Home')
    },
    {
      path: '/userMan',
      name: 'userMan',
      meta: { title: '用户管理' },
      component: _import('Main'),
      children: [
        {
          path: 'list',
          meta: { title: '用户列表' },
          component: _import('userMan/components/table')
        }
      ]
    },
    {
      path: '/agentMan',
      name: 'agentMan',
      meta: { title: '代理管理' },
      component: _import('Main'),
      children: [
        {
          path: 'list',
          meta: { title: '代理列表' },
          component: _import('agent/components/table')
        }
      ]
    }, {
      path: '/product',
      name: 'product',
      meta: { title: '产品管理' },
      component: _import('Main'),
      children: [
        {
          path: 'twstock',
          meta: { title: '股票管理' },
          component: _import('stock/components/twstock-table')
        },
        {
          path: 'stock',
          meta: { title: '股票管理' },
          component: _import('stock/components/table')
        }, {
          path: 'index',
          meta: { title: '指数管理' },
          component: _import('stock/components/index-table')
        }, {
          path: 'futures',
          meta: { title: '期货管理' },
          component: _import('stock/components/futures-table')
        }, {
          path: 'coin',
          meta: { title: '基币管理' },
          component: _import('stock/components/jibi-table')
        }, {
          path: 'addfutures',
          meta: { title: '添加期货' },
          component: _import('stock/components/add-futures')
        }, {
          path: 'addindex',
          meta: { title: '添加指数' },
          component: _import('stock/components/add-index')
        }
      ]
    }, {
      path: '/statistics',
      name: 'statistics',
      meta: { title: '持仓统计' },
      component: _import('Main'),
      children: [
        {
          path: 'agentStatistics',
          meta: { title: '股票持仓统计' },
          component: _import('agent/components/statistics-table')
        }, {
          path: 'inedxStatistics',
          meta: { title: '指数持仓统计' },
          component: _import('agent/components/indexstatistics-table')
        }, {
          path: 'futuresStatistics',
          meta: { title: '期货持仓统计' },
          component: _import('agent/components/futures-table')
        }
      ]
    }, {
      path: '/hold',
      name: 'hold',
      meta: { title: '持仓管理' },
      component: _import('Main'),
      children: [
        {
          path: 'holdPositions',
          meta: { title: '股票持仓管理' },
          component: _import('holdPositions/index')
        }, {
          path: 'indexHoldPositions',
          meta: { title: '指数持仓管理' },
          component: _import('holdPositions/index-index')
        }, {
          path: 'futuresHoldPositions',
          meta: { title: '期货持仓管理' },
          component: _import('holdPositions/futures')
        },  {
          path: 'addHoldPositions',
          meta: { title: '创建持仓' },
          component: _import('holdPositions/components/creat-new')
        },
        {
          path: 'subscribe',
          meta: { title: '期货持仓管理' },
          component: _import('holdPositions/subscribe')
        }, 
      ]
    }, {
      path: '/cap',
      name: 'cap',
      meta: { title: '资金管理' },
      component: _import('Main'),
      children: [
        {
          path: 'capitalDetail',
          meta: { title: '充值管理' },
          component: _import('capitalDetail/components/rechage-table')
        }, {
          path: 'Withdraw',
          meta: { title: '提现记录' },
          component: _import('withdraw/components/withdraw-table')
        }, {
          path: 'cash',
          meta: { title: '资金记录' },
          component: _import('cash/components/cash-table')
        }
      ]
    }, {
      path: '/log',
      name: 'log',
      meta: { title: '日志管理' },
      component: _import('Main'),
      children: [
        {
          path: 'task',
          meta: { title: '日志管理' },
          component: _import('task/components/table')
        }, {
          path: 'msglist',
          meta: { title: '短信日志' },
          component: _import('task/components/msg-table')
        }, {
          path: 'loginlist',
          meta: { title: '登录日志' },
          component: _import('task/components/login-table')
        }, {
          path: 'exchange',
          meta: { title: '资金互转记录' },
          component: _import('task/components/capital-table')
        }, {
          path: 'message',
          meta: { title: '站内消息' },
          component: _import('task/components/message-table')
        }
      ]
    }, {
      path: '/manSet',
      name: 'manSet',
      meta: { title: '管理设置' },
      component: _import('Main'),
      children: [
        {
          path: 'manSetting',
          meta: { title: '管理员列表' },
          component: _import('manSetting/components/table')
        }
      ]
    }, {
      path: '/risk',
      name: 'risk',
      meta: { title: '风控设置' },
      component: _import('Main'),
      children: [
        {
          path: 'riskSetting',
          meta: { title: '股票风控' },
          component: _import('riskSetting/components/table')
        },
        {
          path: 'indexriskSetting',
          meta: { title: '指数风控' },
          component: _import('riskSetting/components/index-table')
        },
        {
          path: 'futuresRiskSetting',
          meta: { title: '期货风控' },
          component: _import('riskSetting/components/futures-table')
        }, {
          path: 'product',
          meta: { title: '产品配置' },
          component: _import('baseSetting/components/product-setting')
        }, {
          path: 'spreadSetting',
          meta: { title: '点差设置' },
          component: _import('riskSetting/components/spread-table')
        }
      ]
    }, {
      path: '/set',
      name: 'set',
      meta: { title: '系统设置' },
      component: _import('Main'),
      children: [
        {
          path: 'baseSetting',
          meta: { title: '系统基本设置' },
          component: _import('baseSetting/components/table')
        }, {
          path: 'paychannel',
          meta: { title: '支付渠道管理' },
          component: _import('payChannel/components/table')
        }, {
          path: 'banner',
          meta: { title: 'banner设置' },
          component: _import('baseSetting/components/table-banner')
        }, {
          path: 'art',
          meta: { title: '公告设置' },
          component: _import('art/components/table')
        }
      ]
    }, {
      path: '/login',
      name: 'login',
      meta: { title: '登录' },
      component: Login
    }, { /* Not Found 路由，必须是最后一个路由 */
      path: '*',
      component: Home
    },
    //============================分仓配资-相关，2020年7月23日19:14:55=========================================
    {
      path: '/funds',
      name: 'funds',
      meta: { title: '配置设置' },
      component: _import('Main'),
      children: [
        {
          path: 'setting',
          meta: { title: '配置设置' },
          component: _import('funds/components/setting')
        },{
          path: 'lever',
          meta: { title: '配置杠杆' },
          component: _import('funds/components/lever-table')
        },{
          path: 'dealer',
          meta: { title: '券商机构管理' },
          component: _import('funds/components/dealer-table')
        },{
          path: 'securities',
          meta: { title: '证券信息管理' },
          component: _import('funds/components/securities-table')
        },{
          path: 'account',
          meta: { title: '交易账户管理' },
          component: _import('funds/components/account-table')
        },{
          path: 'apply',
          meta: { title: '待审核配资' }, 
          component: _import('funds/components/apply-table')
        },{
          path: 'delay',
          meta: { title: '续期申请' }, 
          component: _import('funds/components/delay-table')
        },{
          path: 'expand',
          meta: { title: '扩大配资申请' }, 
          component: _import('funds/components/expand-table')
        },{
          path: 'margin',
          meta: { title: '追加保证金' }, 
          component: _import('funds/components/margin-table')
        },{
          path: 'end',
          meta: { title: '终止操盘' }, 
          component: _import('funds/components/end-table')
        }
      ]
    }
  ]
})
