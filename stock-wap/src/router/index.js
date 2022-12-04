import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/page/home/home'
import Buy from '@/page/home/buy'
import Info from '@/page/home/info'
import Alertdetail from '@/page/home/components/alert' // 公告詳情
import Register from '@/page/register'
import Forget from '@/page/forget'
import Login from '@/page/login'
import List from '@/page/list/list'
import Searchlist from '@/page/list/list-search'
import IndexSearchlist from '@/page/list/indexlist-search' // 指數查詢
import SearchMylist from '@/page/list/my-list-search'
import ListDetail from '@/page/list/detail'
import ListDetail2 from '@/page/list/detail2'
import MyList from '@/page/list/my-list'
import Inquiry from '@/page/home/inquiry'
import User from '@/page/user/user'
import OrderList from '@/page/user/order-list'
import holdOrderList from '@/page/user/search-order/hold-stockCode'
import holdOrderList2 from '@/page/user/search-order/hold-stockSpell'
import sellOrderList from '@/page/user/search-order/sell-stockCode'
import sellOrderList2 from '@/page/user/search-order/sell-stockSpell'
import Detail from '@/page/user/detail'
import Card from '@/page/user/card'
import Authentication from '@/page/user/authentication'
import Aggre from '@/page/user/agreement'
import Recharge from '@/page/user/recharge'
import RechargeSure from '@/page/user/recharge-sure'
import RechargeList from '@/page/user/rechargelist'
import Cash from '@/page/user/cash'
import Cashlist from '@/page/user/cashlist'
import AddCard from '@/page/user/addCard'
import Setting from '@/page/user/my'
import Transfer from '@/page/user/transfer'
import IndexList from '@/page/list/index-list'
import indexBuy from '@/page/home/index-buy'
import TwoBuy from '@/page/home/two-buy'
import SubWarehouseBuy from '@/page/home/sub-warehouse-buy'
import futuresBuy from '@/page/home/futures-buy'
import Agree from '@/page/registerAgree'
import Trage from '@/page/tradeAgree'
import OpenAccount from '@/page/openaccount'
import FundsList from '@/page/funds/funds-list'
import newsDetail from '@/page/home/newsDetail'



Vue.use(Router)

const routerPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return routerPush.call(this, location).catch(error => error)
}

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/home'
    }, {
      path: '/home',
      name: 'home',
      meta: {
        title: 'Home',
        requireAuth: true
      },
      component: Home
    }, 
    
    {
      path: '/newsDetail',
      name: 'newsDetail',
      meta: {
        title: 'stock news',
        hasHeader: true
      },
      component: newsDetail
    },
    {
      path: '/buy',
      name: 'buy',
      meta: {
        title: 'Buy',
        requireAuth: true,
        hasHeader: true
      },
      component: Buy
    }, {
      path: '/register',
      name: 'register',
      meta: {
        title: 'Register'
      },
      component: Register
    }, {
      path: '/forget',
      name: 'forget',
      meta: {
        title: 'Forgot password'
      },
      component: Forget
    }, {
      path: '/login',
      name: 'login',
      meta: {
        title: 'Account login',
        hasHeader: true
      },
      component: Login
    }, {
      path: '/openaccount',
      name: 'openaccount',
      meta: {
        title: 'Open an account',
        hasHeader: true
      },
      component: OpenAccount
    }, {
      path: '/list',
      name: 'list',
      meta: {
        title: 'Quotes',
        requireAuth: false,
        hasHeader: true
      },
      component: List
    }, {
      path: '/indexsearchlist',
      name: 'Index query',
      meta: {
        title: 'Index query'
      },
      component: IndexSearchlist
    }, {
      path: '/indexlist',
      name: 'indexlist',
      meta: {
        title: 'Index list',
        requireAuth: false
      },
      component: IndexList
    }, {
      path: '/searchlist',
      name: 'Individual stock inquiry',
      meta: {
        title: 'Individual stock inquiry'
      },
      component: Searchlist
    }, {
      path: '/searchmylist',
      name: 'searchmylist',
      meta: {
        title: 'Optional query',
        requireAuth: true
      },
      component: SearchMylist
    }, {
      path: '/mylist',
      name: 'mylist',
      meta: {
        title: 'Picklist',
        requireAuth: true,
        hasHeader: true

      },
      component: MyList
    }, 
    {
      path: '/info',
      name: 'info',
      meta: {
        title: 'about us',
        requireAuth: true,
        hasHeader: true

      },
      component: Info
    }, {
      path: '/listdetail',
      name: 'listdetail',
      meta: {
        title: 'Details',
        requireAuth: false,
        hasHeader: true
      },
      component: ListDetail
    }, {
      path: '/listdetail2',
      name: 'listdetail2',
      meta: {
        title: 'Details',
        requireAuth: false,
        hasHeader: true
      },
      component: ListDetail2
    },
    {
      path: '/indexBuy',
      name: 'indexBuy',
      meta: {
        title: 'index buying',
        requireAuth: false,
        hasHeader: true,
        iconRight:'search'
      },
      component: indexBuy
    },
    {
      path: '/twoBuy',
      name: 'TwoBuy',
      meta: {
        title: 'Two financial transactions',
        requireAuth: false,
        hasHeader: true,
        iconRight:'search'
      },
      component: TwoBuy
    },
    {
      path: '/subWarehouseBuy',
      name: 'SubWarehouseBuy',
      meta: {
        title: 'split trading',
        requireAuth: false,
        hasHeader: true,
        iconRight:'search'
      },
      component: SubWarehouseBuy
    }, {
      path: '/futuresBuy',
      name: 'futuresBuy',
      meta: {
        title: 'futures purchase',
        requireAuth: false,
        hasHeader: true,
      },
      component: futuresBuy
    }, {
      path: '/inquiry',
      name: 'inquiry',
      meta: {
        title: 'Inquiry',
        requireAuth: true
      },
      component: Inquiry
    }, {
      path: '/user',
      name: 'user',
      meta: {
        title: 'Mine',
        requireAuth: false,
        hasHeader: true
      },
      component: User
    }, 
   {
      path: '/transfer',
      name: 'transfer',
      meta: {
        title: 'Mutual fund transfer',
        requireAuth: true
      },
      component: Transfer
    }, {
      path: '/orderlist',
      name: 'orderlist',
      meta: {
        title: 'Position',
        requireAuth: false,
        hasHeader: true,
      },
      component: OrderList
    }, 
    {
      path: '/holdorderlist',
      name: 'holdorderlist',
      meta: {
        title: 'Query positions',
        requireAuth: true,
        hasHeader:true
      },
      component: holdOrderList
    }, {
      path: '/holdorderlist2',
      name: 'holdorderlist2',
      meta: {
        title: 'Query positions',
        requireAuth: true,
        hasHeader:true
      },
      component: holdOrderList2
    }, {
      path: '/sellorderlist',
      name: 'sellorderlist',
      meta: {
        title: 'Inquiry to close a position',
        requireAuth: true,
        hasHeader: true
      },
      component: sellOrderList
    }, {
      path: '/sellorderlist2',
      name: 'sellorderlist2',
      meta: {
        title: 'Inquiry to close a position',
        requireAuth: true,
        hasHeader: true
      },
      component: sellOrderList2
    }, {
      path: '/detail',
      name: 'detail',
      meta: {
        title: 'Funding Details',
        requireAuth: true,
        hasHeader: true
      },
      component: Detail
    }, {
      path: '/card',
      name: 'card',
      meta: {
        title: 'Financial account',
        requireAuth: true,
        hasHeader: true
      },
      component: Card
    }, {
      path: '/authentication',
      name: 'authentication',
      meta: {
        title: 'Certification',
        requireAuth: true,
        hasHeader: true
      },
      component: Authentication
    }, {
      path: '/aggre',
      name: 'aggre',
      meta: {
        title: 'Cooperation agreement',
        requireAuth: true
      },
      component: Aggre
    }, {
      path: '/recharge',
      name: 'recharge',
      meta: {
        title: 'Recharge',
        requireAuth: true,
        hasHeader: true

      },
      component: Recharge
    }, {
      path: '/rechargeSure',
      name: 'rechargeSure',
      meta: {
        title: 'Confirm Recharge',
        requireAuth: true,
        hasHeader: true

      },
      component: RechargeSure
    }, {
      path: '/rechargelist',
      name: 'rechargelist',
      meta: {
        title: 'Recharge records',
        requireAuth: true,
        hasHeader: true
      },
      component: RechargeList
    }, {
      path: '/cash',
      name: 'cash',
      meta: {
        title: 'Withdrawal',
        requireAuth: true,
        hasHeader:true
      },
      component: Cash
    }, {
      path: '/addCard',
      name: 'addCard',
      meta: {
        title: 'Add a financial account',
        requireAuth: true,
        hasHeader:true

      },
      component: AddCard
    }, {
      path: '/cashlist',
      name: 'cashlist',
      meta: {
        title: 'Withdrawal records',
        requireAuth: true,
        hasHeader:true

      },
      component: Cashlist
    }, {
      path: '/setting',
      name: 'setting',
      meta: {
        title: 'set up',
        requireAuth: true
      },
      component: Setting
    }, {
      path: '/agree',
      name: 'agree',
      meta: {
        title: 'Registration Agreement',
        requireAuth: true
      },
      component: Agree
    }, {
      path: '/trade',
      name: 'trade',
      meta: {
        title: 'Transaction Risk Disclosure Statement',
        requireAuth: true
      },
      component: Trage
    },
    {
      path: '/alertdetail',
      name: 'alertdetail',
      meta: {
        title: 'Announcement Details',
        requireAuth: true
      },
      component: Alertdetail
    }, 
    {
      path: '/funds',
      name:'funds',
      meta: {
        title: 'Allotment',
        requireAuth: true,
        hasHeader: true,
        iconRight: 'setting'
      },
      component: () => import('../page/funds/index')
    },
    {
      path: '/days',
      name:'days',
      meta: {
        title: 'Daily allocation',
        requireAuth: true,
        hasHeader: true,
        iconRight: 'setting'
      },
      component: () => import('../page/funds/days')
    },
    {
      path: '/xingu',
      name:'xingu',
      meta: {
        title: 'IPO subscription',
        requireAuth: true,
        hasHeader: true,
        iconRight: 'setting'
      },
      component: () => import('../page/funds/xingu')
    },
    {
      path: '/searchStock',
      name: 'searchStock',
      meta: {
        title: 'Inquire about stocks',
        requireAuth: true,
        hasHeader: true,
      },
      component: () => import('../page/list/search')
    },
    {
      path: '/notify',
      name: 'notify',
      meta: {
        title: 'message log',
        requireAuth: true,
        hasHeader: true,
      },
      component: () => import('../page/user/notify')
    }, {
      path: '/fundslist',
      name: 'fundslist',
      meta: {
        title: 'Distribution allocation',
        requireAuth: false,
        hasHeader: true,
      },
      component: FundsList
    },
    {
      // 會匹配所有路徑
      path: '*',
      redirect: '/home'
    }
  ]
})
