import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
	userInfo: {
		phone: ''
	},
	bankInfo: {}, // 银行卡信息
	siteInfo: {}, // 站点基本信息发
	loginIsShow: false,
	haslogin: false, // 是否登录
	hide: false, // 数字隐藏显示
	userMenu: '2-1',
	productSetting: null,
	systemColor: '',
	userPositionData: {}, //出仓数据
	language: ''
}



export default new Vuex.Store({
	state,
	mutations: {
		setSystemColor(state, data) {
			state.systemColor = data
		},
		setUserInfo(state,data){
			state.userInfo = data
		},
		setLanguage(state, data) {
			state.language = data
		},
		setUserPositionData(state, data) {
			if (!data) {
				state.userPositionData = {}
				return
			}
			state.userPositionData = data

		}
	}
})
