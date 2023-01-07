import Vue from 'vue'
import App from './App'
import router from './router'
import Icon from 'vue-svg-icon/Icon.vue'
import Mint from 'mint-ui'
// import Resource from 'vue-resource' // 已卸载用axios代替
import store from './store/index'
import axios from './axios/index'
import ElementUI from 'element-ui'
import VueClipboard from 'vue-clipboard2' // 复制
import state from './event'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'mint-ui/lib/style.css'
import 'element-ui/lib/theme-chalk/index.css'
// import md5 from 'js-md5'
import * as filters from '@/utils/utils'
import animated from 'animate.css' // npm install animate.css --save安装，在引入

import '../static/css/public2.css'
import en from './i18n/en'
import zhTW from './i18n/zh-TW'
import VueI18n from 'vue-i18n'
// import VueTouch from 'vue-touch'
import { Actionsheet } from 'mint-ui';
Vue.component(Actionsheet.name, Actionsheet);

Vue.use(animated)
Vue.use(ElementUI)
Vue.use(VueClipboard)
// Vue.use(VueTouch, { name: 'v-touch' })
// // Vue.prototype.$md5 = md5
// VueTouch.config.swipe = {
//   threshold: 100 // 手指左右滑动距离
// }
Vue.use(VueI18n)
const i18n = new VueI18n({
	locale: 'en',
	messages: {
		en,
		"zh-TW":zhTW
	}
})
Vue.use(Mint)
Vue.component('icon', Icon)
Vue.config.productionTip = false
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
Vue.prototype.$state = state
Vue.prototype.$setgoindex = function () {
  if (window.history.length <= 1) {
    if (location.href.indexOf('?') === -1) {
      window.location.href = location.href + '?goindex=true'
    } else if (location.href.indexOf('?') !== -1 && location.href.indexOf('goindex') === -1) {
      window.location.href = location.href + '&goindex=true'
    }
  }
}
Vue.prototype.setCookie = function (name, value, day) {
  if (day !== 0) { // 当设置的时间等于0时，不设置expires属性，cookie在浏览器关闭后删除
    var curDate = new Date()
    var curTamp = curDate.getTime()
    var curWeeHours = new Date(curDate.toLocaleDateString()).getTime() - 1
    var passedTamp = curTamp - curWeeHours
    var leftTamp = 24 * 60 * 60 * 1000 - passedTamp
    var leftTime = new Date()
    leftTime.setTime(leftTamp + curTamp)
    document.cookie = name + '=' + escape(value) + ';expires=' + leftTime.toGMTString()
  } else {
    document.cookie = name + '=' + escape(value)
  }
}
Vue.prototype.getCookie = function (name) {
  var arr
  var reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)')
  arr = document.cookie.match(reg)
  if (arr) {
    return unescape(arr[2])
  } else {
    return null
  }
  // document.cookie = name + "=" + escape(value);
}
Vue.prototype.clearCookie = function () {
  this.setCookie('USER_TOKEN', '', -1)
}
Vue.prototype.checkCookie = function () {
  var user = this.getCookie('USER_TOKEN')
  if (user !== '') {
    alert('Welcome again ' + user)
  } else {
    user = prompt('Please enter your name:', '')
    if (user !== '' && user != null) {
      this.setCookie('USER_TOKEN', user, 365)
    }
  }
}
// router.beforeEach((to, from, next) => {
// console.log(to.path)
// store.state.select = to.path
// document.title = to.meta.title || '亿点通'
// // 判断是否登录
// console.log(document.cookie)
// // console.log(checkCookie(),'checkCookie()')
// if(!to.meta.requireAuth){
// next()
// return
// }
// if (document.cookie && to.meta.requireAuth) {
// if (to.path === '/login') {
// next({ path: '/' })
// } else {
// if (!to.query.url && from.query.url) {
// to.query.url = from.query.url
// }
// next()
// }
// }else{
// if (to.path === '/login') {
// next()
// } else {
// next({ path: '/login' })
// }
// }
// })
router.beforeEach((to, from, next) => {
  store.state.select = to.path
  document.title = to.meta.title
  // if (!to.query.url && from.query.url) {
  //   to.query.url = from.query.url
  // }
  next()
})
// // 设置title
// router.beforeEach((to, from, next) => {
//   if (to.meta.title) {//如果设置标题，拦截后设置标题
//     if(store.state && store.state.siteInfo && store.state.siteInfo.siteName){
//       document.title =  store.state.siteInfo.siteName + '-' + to.meta.title
//     }else{
//       document.title =  to.meta.title
//     }
//   }
//   next()
// })
/* eslint-disable no-new */

new Vue({
  el: '#app',
  store,
  router,
  axios,
  i18n,
  render: h => h(App)
}).$mount('#app')
