import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  userInfo: {
    phone: ''
  },
  siteInfo: {}, // 站点基本信息
  loginIsShow: false
}

export default new Vuex.Store({
  state
})
