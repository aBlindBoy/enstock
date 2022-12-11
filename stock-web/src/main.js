import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import locale from 'element-ui/lib/locale/lang/en'
import localeTW from 'element-ui/lib/locale/lang/zh-TW'
import VueI18n from 'vue-i18n'
import en from './i18n/en'
import zhTW from './i18n/zh-TW'

import store from './store'
import axios from './axios/index' // 这里这里
import preview from 'vue-photo-preview' // 查看大图
import 'vue-photo-preview/dist/skin.css' // 查看大图样式
import echarts from "echarts";


import 'element-ui/lib/theme-chalk/index.css'

// import axios from 'axios'
import * as filters from './utils/utils'
import VueClipboard from 'vue-clipboard2' // 復製
import md5 from 'js-md5'
import animated from 'animate.css' // 动画

Vue.prototype.$echarts = echarts
Vue.prototype.$md5 = md5
Vue.config.productionTip = false
Vue.use(animated)
Vue.use(preview)
Vue.use(VueClipboard)
// localeTW
Vue.use(ElementUI,{locale,localeTW})


Vue.use(VueI18n)
// Vue.config.lang = 'zh-TW'
// Vue.locale('zh-TW', localeTW)
// Vue.locale('en', localeEN)
const i18n = new VueI18n({
	locale: 'zh-TW',
	messages: {
		en,
		"zh-TW":zhTW
	}
  })

  
// Vue.use(MetaInfo)
Object.keys(filters).forEach(key => {
	Vue.filter(key, filters[key])
})
// Vue.http.options.emulateJSON = true;
// Vue.http.options.headers = {
//   // 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
//   'Content-Type': 'application/json;charset=UTF-8'
// };
Vue.prototype.getCookie = function(name) {
	var arr
	var reg = new RegExp('(^| )' + name + '=([^;]*)(;|$)')
	console.log(document.cookie, 'document.cookie')
	if (arr === document.cookie.match(reg)) {
		return unescape(arr[2])
	} else {
		return null
		// document.cookie = name + "=" + escape(value);
	}
}
Vue.prototype.setCookie = function(name, value, day) {
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
Vue.prototype.clearCookie = function() {
	this.setCookie('USER_TOKEN', '', -1)
}
// 设置title
router.beforeEach((to, from, next) => {
	if (to.meta.title) { // 如果设置标题，拦截后设置标题
		if (store.state && store.state.siteInfo && store.state.siteInfo.siteName) {
			document.title = store.state.siteInfo.siteName + '-' + to.meta.title
		} else {
			document.title = to.meta.title
		}
	}
	next()
})
/* eslint-disable no-new */
new Vue({
	el: '#app',
	router,
	store,
	axios,
	i18n,
	render: h => h(App)
}).$mount('#app')
