import Vue from 'vue'
import App from './App'
import store from './store'
import router from './router'
import ElementUI from 'element-ui'
import axios from './axios/index' // 这里这里
import * as filters from '@/utils/utils'
import VueClipboard from 'vue-clipboard2'
import preview from 'vue-photo-preview' // 查看大图
import 'vue-photo-preview/dist/skin.css'
import md5 from 'js-md5'
import animated from 'animate.css' // npm install animate.css --save安装，在引入

Vue.prototype.$md5 = md5
// Vue.use(axios)
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// axios.defaults.headers.get['Content-Type'] = 'application/x-www-form-urlencoded';
// axios.defaults.transformRequest = [function (data) {
//     let ret = ''
//     for (let it in data) {
//       ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
//     }
//     return ret
// }]

// Vue.prototype.$http = axios
Vue.config.productionTip = false
Vue.use(preview)
Vue.use(animated)
Vue.use(VueClipboard)
Vue.use(ElementUI)
// Vue.use(MetaInfo)
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
// Vue.http.options.emulateJSON = true;
// Vue.http.options.headers = {
//   // 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
//   'Content-Type': 'application/json;charset=UTF-8'
// };
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
  render: h => h(App)
}).$mount('#app')
