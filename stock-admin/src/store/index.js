import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  userInfo: {
    phone: '',
    userInfo: ''
  },
  siteInfo: {
    siteLogo: '',
    siteName: ''
  },
  isCollapse: false,
  loginIsShow: false
}

export default new Vuex.Store({
  state
})
