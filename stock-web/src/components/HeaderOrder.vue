<template>
	<div>
	
		<div class="header-nav" style="display: flex;">
			<div class="header-nav-wrap clearfix" style="width: 100%;display: flex;">
				<a @click="toTransform" class="nav-left pull-left">
					<!-- <img src="../assets/image/logo-mini.png" alt=""> -->
					<img :src="siteInfo.siteLogoSm" alt />
				</a>
				<div class="nav-menu pull-left">
					<el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect" style="text-align: center;display: flex;align-items: center;">
						<el-menu-item :class="activeIndex === 'home'?'is-active':''" index="home">
							{{$t('header.home')}}
							</el-menu-item>
						<el-menu-item index="stock">{{$t('header.stock')}}</el-menu-item>
						<el-menu-item index="transaction">{{$t('header.trade')}}</el-menu-item>
						<el-menu-item index="product">{{$t('header.productDescription')}}</el-menu-item>
						<el-menu-item index="down">{{$t('header.softwareDownload')}}</el-menu-item>
						<el-menu-item index="introduce">{{$t('header.companyProfile')}}</el-menu-item>
						<el-menu-item index="enterprise">{{$t('header.news')}}</el-menu-item>
						<el-menu-item index="/transaction?code=A" style="color:#000; display: flex;align-items: center;">
							<div class="market-data">
								<img src="../assets/image/dp.png" v-if="this.$store.state.systemColor == 'red-bg'" alt />
								<img src="../../static/img/gupiao.png" v-if="this.$store.state.systemColor == 'black-bg'" alt="">
								<span>Market data</span>
							</div>
						</el-menu-item>
						<!-- <el-menu-item index="/transaction?code=300498" style="color:#000; display: flex;align-items: center; ">
							<div class="stock-show">
								<img src="../assets/image/gp.png" alt />
								<span>股票展示</span>
							</div>
						</el-menu-item> -->
					</el-menu>
				</div>
				<div class="user pull-right main-col" style="width: 100%; display: flex;justify-content: flex-end;padding:14px;font-size: 16px;overflow: hidden;">
					<!-- <a href="https://088992.kefu.easemob.com/webim/im.html?configId=d5d08347-1fa9-4c95-87a8-c6100336ab58" size="mini" class="stock-show" @click="onlineService">
						<span>在線客服</span>

					</a> -->
					<!-- <a size="mini" class="stock-show" v-clipboard:copy="'thrt9'"
						v-clipboard:success="onCopy"
						v-clipboard:error="onError">
						<i class="el-icon-s-custom"></i>
						line:<span >thrt9</span>
					</a> -->
					<span>
						

					 </span>
					<a href="javascript:;"  id="google_translate_element" style="width: 40px;"></a>
					<!-- <a href="javascript:;" class="language" type="primary" @click="isEn1">En</a> -->
					<a href="javascript:;" v-if="!this.$store.state.haslogin" class="header-btn" type="primary" @click="toLogin">{{$t('common.login')}}</a>
					<a href="javascript:;" v-if="!this.$store.state.haslogin" class="header-btn" type="primary" @click="toRegister">{{$t('common.register')}}</a>
					<!-- <a href="javascript:;" class="iconfont icon-lingdang xiaoxiding" style=" margin-right: 10px;"></a> -->
					<el-dropdown trigger="click" @command="selectColor">
						<span class="el-dropdown-link" style="color:#fff;line-height: 30px;font-size: 16px;">{{$t('header.style')}}<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item command="black-bg">{{$t('header.dark')}}</el-dropdown-item>
							<el-dropdown-item command="red-bg">{{$t('header.red')}}</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
					<el-dropdown trigger="click" @command="selectLocale">
						<span class="el-dropdown-link" style="color:#fff;line-height: 30px;font-size: 16px;">
							
							{{this.locale == 'en'?'English':'中文繁体'}}
							<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item command="en">English</el-dropdown-item>
							<el-dropdown-item command="zh-TW">中文繁体</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>

					<el-dropdown v-if="this.$store.state.haslogin" @command="handleCommand">
						<span class="el-dropdown-link">
							<i class="iconfont icon-yonghu"></i>
							{{$store.state.userInfo.realName}}
							<i class="el-icon-arrow-down el-icon--setting"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item v-if="!this.$store.state.haslogin" command="c">{{$t('common.login')}}</el-dropdown-item>
							<!-- <el-dropdown-item v-if="this.$store.state.haslogin" command="d">用戶中心</el-dropdown-item> -->
							<!-- <el-dropdown-item v-if="this.$store.state.haslogin" command="a">重置密碼</el-dropdown-item> -->
							<el-dropdown-item v-if="this.$store.state.haslogin" command="b">{{$t('header.logout')}}</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
					<div class="" v-if="this.$store.state.haslogin" style="display: flex; align-items: center;width: 100px;">
						<a v-if="this.$store.state.haslogin" @click="toUserCenter" class="user-center">{{$t('header.personalCenter')}}</a>
					</div>
				</div>


			</div>
		</div>
	</div>
</template>

<script>
	
	
	
	
	
	import * as api from "../axios/api";
	import $ from 'jquery'
	import md5 from 'js-md5'
	export default {
		components: {},
		props: {},
		data() {
			
			return {
				activeIndex: this.$store.state.activeIndex, // 當前選中
				navShow: {
					order: false,
					artical: false,
				},
				outMoneyOrder: 0, // 出金待審核金額
				timer: null,
				siteInfo: {},
				locale:'en'
			};
		},
		watch: {
			haslogin(newVal, oldVal) {
				if (newVal !== oldVal && newVal) {
					this.getUserInfo();
				}
			},

		},
		computed: {
			haslogin() {
				return this.$store.state.haslogin;
			},

		},
		created() {

			// this.timer = setInterval(this.refreshOutMoneyOrderNum, 1000*60)
		},
		mounted() {
			
		},
		beforeDestroy() {
			clearInterval(this.timer);
		},
		methods: {
			
		
			
			// 選擇色系
			selectColor(command) {
				
				localStorage.setItem("styleName", command);
				this.$store.commit('setSystemColor', command)
				document.getElementsByTagName("body")[0].className = command;
				window.drawLine && window.drawLine()
				window.te && window.te()
			},
			async getInfoSite() {
				// 獲取網站信息
				let result = await api.getInfoSite();
				if (result.status === 0) {
					this.siteInfo = result.data;
					this.$store.state.siteInfo = this.siteInfo;
				} else {
					this.$message.error(result.msg);
				}
			},
			async getUserInfo() {
				// 獲取用戶信息
				let data = await api.getUserInfo();
				if (data.status === 0) {
					// 判斷是否登錄
					this.$store.state.haslogin = true;
					this.$store.state.userInfo = data.data;
				} else {
					this.$store.state.haslogin = false;
					// this.$message.error(data.msg)
				}
			},
			toUserCenter() {
				// 進入個人中心
				this.$router.push("/user");
			},
			toLogin() {
				// 進入個人中心
				this.$router.push("/login");
			},
			onlineService() {
				// 進入在線客服
				window.open(this.siteInfo.onlineService, "");
			},
			toRegister() {
				// 進入個人中心
				this.$router.push("/register");
			},
			async handleCommand(val) {
				if (val === "a") {
					// 修改密碼
				} else if (val === "c") {
					this.$store.state.loginIsShow = true;
				} else if (val === "d") {
					this.$router.push("/user");
				} else {
					// 退出登錄
					this.clearCookie();
					let data = await api.logout();
					if (data.status === 0) {
						// 退出登錄清除本地存儲 清除用戶數據
						window.localStorage.clear();
						this.$store.state.haslogin = false;
						this.$message.success($t('header.quit'));
						this.$store.state.userInfo = {};
						this.$router.push("/login");
					} else {
						this.$message.error(data.msg);
					}
				}
			},
			handleSelect(key, keyPath) {
				console.log(key, keyPath);
				if (key === "transaction") {
					this.toTransform();
				} else {
					this.$router.push(key);
				}
				this.$store.state.activeIndex = key;
			},
			async toTransform() {
				 let opt = {
					pageNum: 1,
					pageSize: 1
				};
				this.loading = true;
				let data = await api.getStock(opt);
				debugger
				if (data.status === 0) {
					this.$router.push({
						path: "/transaction",
						query: {
							code: data.data.list[0].code,
						},
					});
				} else {
					this.$message.error(data.msg);
				}
			},
			selectLocale(locale){
				debugger
				this.$i18n.locale = locale
				this.locale = locale
				localStorage.setItem("locale",locale) 
			},

		},
		mounted() {
			this.getInfoSite();
			this.getUserInfo();
		},
	};
</script>
<style lang="less" scoped>
	.black-bg .xiaoxiding {
		color: #fff;
	}

	.red-bg {
		.xiaoxiding {
			color: #ccc;
		}

		.main-col {
			background-color: #C11815;
		}
	}

	.red-bg .el-dropdown {
		font-size: 16px;
		color: #ccc;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 5px;
	}

	.red-bg .el-dropdown>span {
		display: flex;
		align-items: center;
	}

	.black-bg .el-dropdown {
		font-size: 16px;
		margin: 0 5px;
		color: #ccc;
		display: flex;
		align-items: center;
	}

	.user .user-center[data-v-50118b7e] {
		margin-right: 20px;
		margin-left: 20px;
		background: none;
		color: #ddd;
		cursor: pointer;
		display: flex;
		margin-left: 10px;
		justify-content: center;
		align-items: center;
	}

	.el-dropdown-link {
		cursor: pointer;
	}

	.el-header {
		width: 100%;
		height: 56px;
		padding-right: 0;
		min-width: 1200px;
	}

	.nav-left {
		line-height: 60px;
		margin-right: 24px;

		img {
			max-width: 180px;
			max-height: 50px;
			vertical-align: middle;
		}
	}

	.nav-menu {
		.el-menu {
			background: none;
			border-bottom: none;

			.el-menu-item {
				font-size: 16px;
				color: #fff;

				&:hover {
					color: #d0d0d0;
					background: none;
				}

				&:focus {
					color: #d0d0d0;
					background: none;
				}

				&.is-active {
					color: #efbb53;
					border-color: #efbb53;
				}

				&.on {
					color: #efbb53;
				}
			}
		}
	}

	// .test:hover {
	//   background: rgb(156,4,0) !important;
	// }

	.user {
		padding: 20px 0;
		color: #ccc;

		.icon-yonghu {
			font-size: 18px;
			margin-right: 6px;
		}

		.user-center {
			margin-right: 20px;
			margin-left: 20px;
			background: none;
			color: #ddd;
			cursor: pointer;
			margin-left: 10px;
		}
	}
</style>
