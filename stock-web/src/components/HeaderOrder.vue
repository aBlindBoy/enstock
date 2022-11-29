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
						<el-menu-item :class="activeIndex === 'home'?'is-active':''" index="home">Home</el-menu-item>
						<el-menu-item index="stock">Stock</el-menu-item>
						<el-menu-item index="transaction">Trading Floor</el-menu-item>
						<el-menu-item index="product">Product Description</el-menu-item>
						<el-menu-item index="down">Software Download</el-menu-item>
						<el-menu-item index="introduce">Company Profile</el-menu-item>
						<el-menu-item index="enterprise">News</el-menu-item>
						<el-menu-item index="/transaction?code=6414" style="color:#000; display: flex;align-items: center;">
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
					<a href="javascript:;" v-if="!this.$store.state.haslogin" class="header-btn" type="primary" @click="toLogin">Login</a>
					<a href="javascript:;" v-if="!this.$store.state.haslogin" class="header-btn" type="primary" @click="toRegister">Register</a>
					<!-- <a href="javascript:;" class="iconfont icon-lingdang xiaoxiding" style=" margin-right: 10px;"></a> -->
					<el-dropdown trigger="click" @command="selectColor">
						<span class="el-dropdown-link" style="color:#fff;line-height: 30px;font-size: 16px;">Style<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item command="black-bg">Dark</el-dropdown-item>
							<el-dropdown-item command="red-bg">Red</el-dropdown-item>

						</el-dropdown-menu>
					</el-dropdown>

					<el-dropdown v-if="this.$store.state.haslogin" @command="handleCommand">
						<span class="el-dropdown-link">
							<i class="iconfont icon-yonghu"></i>
							{{$store.state.userInfo.realName}}
							<i class="el-icon-arrow-down el-icon--setting"></i>
						</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item v-if="!this.$store.state.haslogin" command="c">Login</el-dropdown-item>
							<!-- <el-dropdown-item v-if="this.$store.state.haslogin" command="d">用戶中心</el-dropdown-item> -->
							<!-- <el-dropdown-item v-if="this.$store.state.haslogin" command="a">重置密碼</el-dropdown-item> -->
							<el-dropdown-item v-if="this.$store.state.haslogin" command="b">Logout</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
					<div class="" v-if="this.$store.state.haslogin" style="display: flex; align-items: center;width: 100px;">
						<a v-if="this.$store.state.haslogin" @click="toUserCenter" class="user-center">Personal Center</a>
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
				newLanguage:'en'
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
			
			isEn1(){
				// var language = this.newLanguage
				transformLanguage();
				// this.newLanguage = this.newLanguage == 'en' ? 'zh':'en'
				function transformLanguage(newLanguage) {
				    // 獲取所有dom元素中文
				    let transformStr = '';
				    // 獲取所有dom元素
				    function getChildDom(dom, type, data = {}) {
				        if(type == 'read') {
				            [...dom.children].forEach(v => {
				                // 判斷中文
				                // /^[\u0391-\uFFE5]+$/
				                let re= /^[0-9A-Za-z\u4E00-\u9FFF]+$/;
				                // 防止某些標簽有內容並且有標簽 ，或者有空格 
				                let vHtml = $(v).contents().filter(function (index, content) {return content.nodeType === 3}).text().trim();
				                // 跳過script標簽
				                if (re.test(vHtml) && v.tagName != 'SCRIPT' && v.tagName != 'STYLE') {
				                    transformStr += `${vHtml},`
				                }
				                // 遞歸獲取元素
				                getChildDom(v, type, data);
				            })
				        }else {
				   //          let transOld = data.trans_result[0].src.split(',');
							// // 英文
				   //          let transNew = data.trans_result[0].dst.split(',');
							var strTemp
							var strTemp1
							data.trans_result.forEach(item=>{
								strTemp += item.src
							})
							data.trans_result.forEach(item=>{
								strTemp1 += item.dst
							})
							// 中文
							var transOld1 = strTemp.split(',')
							// 英文
							let transNew1 = strTemp1.split(',');
							console.log(transOld1,transNew1);
							// let transOld1 = data.trans_result[0].src.split(',');
							// let transNew1 = data.trans_result[0].dst.split(',');
							// console.log(transOld,transOld1);
							 // let transOld = data.trans_result;
							// console.log(transOld,transNew);
							// dom  元素 
							// type 
							// console.log(dom, type, data);
				            [...dom.children].forEach(v => {
				                // 判斷中文
				                // /^[\u0391-\uFFE5]+$/
				                let re= /^[0-9A-Za-z\u4E00-\u9FFF]+$/;
				                let vHtml = $(v).contents().filter(function (index, content) {
									return content.nodeType === 3
								}).text().trim();
				                // 跳過script標簽
				                if (re.test(vHtml) && v.tagName != 'SCRIPT' && v.tagName != 'STYLE') {
				                    // 防止標簽裏面還有標簽，所以只替換裏面的html,使用replace
									// var transOld,transNew;
									// data.trans_result.forEach((item,index)=>{
									// 	   transOld = item.src.split(',');
									// 	   transNew = item.dst.split(',');
									// })
									// // console.log(transOld,transNew)
									
				                   
										// $(v).html(
										//     $(v).html().replace(
										//     transOld[transOld.findIndex(arrList => {
												
										// 		return arrList == vHtml
										// 	})]
										//     ,
										//     transNew[transOld.findIndex(arrList => {
												
										// 		return arrList == vHtml
										// 	})]
										//     )
											
										// )
								
										$(v).html(
										    $(v).html().replace(
										    transOld1[transOld1.findIndex(arrList => arrList == vHtml)]
										    ,
										    transNew1[transOld1.findIndex(arrList => arrList == vHtml)]
										    )
											
										)
									
				                }
				                // 遞歸獲取元素
				                getChildDom(v, type, data);
				            })
				        }
				    }
				    getChildDom(document,'read');
					// console.log(transformStr)
				    getTranslateData();
				    // 獲取翻譯
				    function getTranslateData() {
						
						var appid = '20200822000549413';
						var key = 'h4holNDIhvibu_EEmVRh';
						var salt = (new Date).getTime();
						var query =transformStr;
						// query = encodeURIComponent(query);
						// 多個query可以用\n連接  如 query='apple\norange\nbanana\npear'
						var from = 'en';
						var to = 'zh';
						var str1 = appid + query + salt +key;
						var sign = md5(str1);
						$.ajax({
						    url: '/api/trans/vip/translate',
						    type: 'post',
						    // dataType: 'jsonp',
							// headers:{"application/x-ww":'application/x-www-form-urlencoded'},
						    data: {
						        q: query,
						        appid: appid,
						        salt: salt,
						        from: from,
						        to: to,
						        sign: sign
						    },
						    success: function (data) {
						               data.trans_result && getChildDom(document,'write',data);
						               console.log(data);
						    } 
						});
						
						
						
				    }
				}
			
			},
			
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
						this.$message.success("您已退出登錄");
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
					pageSize: 1,
					stockPlate:'上市'
				};
				this.loading = true;
				let data = await api.getTwStockPageList(opt);
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
			onCopy: function (e) {
				this.$message.success("復製成功！");
			},
			onError: function (e) {
				this.$message.error("復製失败，请重试！");
			},
		},
		mounted() {
			this.getInfoSite();
			//  if(this.$store.state.haslogin){
			this.getUserInfo();
			// setInterval(()=>{
			// 	this.isEn()
				
			// },900)

			//  }
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
