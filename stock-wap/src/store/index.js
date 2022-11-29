import Vue from 'vue'
import Vuex from 'vuex'
// import getters from './getters'
// import actions from './actions'
// import mutations from './mutations'

Vue.use(Vuex)

const state = {
  className: 'black',
  userInfo: { // 用户信息
  },
  user: {},
  bankInfo: {
    bankNo: ''
  },
  hide: false,
  select: '/home', // 菜单选择
  token: 'USER62CCF5221CD752269D47681374DD0916',
  settingForm: { // 产品配置
    futuresDisplay: false,
    indexDisplay: false,
    kcStockDisplay: false,
    stockDisplay: false
  }
}

export default new Vuex.Store({
  state,
  actions: {},
  mutations: {},
  getters: {}
})
