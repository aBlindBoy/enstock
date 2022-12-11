<template>
	<div style="height: 216px;">
		<!-- :style="'?'':'margin-bottom: 20px;'" -->
		<div class="wrapper buy-table" style="display:flex;justify-content: space-between; width: 100%;" :style="activeName1 == 'three'?'margin-bottom: 30px;':activeName1 == 'five'?'margin-bottom: 25px;':''">
			<div style="display: flex;justify-content: space-between;width: 100%;">
				<el-tabs v-model="activeName" class="buy-box-cont" style="width: 100%;">
					<!-- 兩融交易開始 -->
					<el-tab-pane name="first">
						<!-- 買 -->
						<div class="buy-box" style="margin-top: 15px;">
							<el-form ref="ruleForm" :hide-required-asterisk="true" size="mini" :model="form" label-width="60px">
								<div class="buy-item" style="display: flex;">
									<span class="buy-name1">{{detail.name}}</span>
									<!-- <div class="" style="position: relative;">
										<span class="iconfont icon-triangle-left sanjiao"></span>
										<span class="buy-code1">{{detail.code}}</span>
									</div> -->
									<span style="font-size:12px;margin-left: 10px;">{{$t('common.lastPrice')}}</span>

									<span style="font-size:12px;color:#B12525;margin-left: 5px;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{detail.nowPrice}}</span>
									<span style="font-size:12px;margin-left:10px">{{$t('common.chgRate')}}</span>
									<span style="font-size:12px;color:#B12525;margin-left: 5px;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{Number(detail.hcrate).toFixed(3)}}%</span>
									
								</div>
								<div style="display: flex;">
									<div>
										<div style="display: flex;position: relative;">
											<el-form-item prop="buyNum" style="margin-bottom:10px;margin-right: 10px;">
												<el-input placeholder="" v-model="form.buyNum">
											
												</el-input>
												<span style="position: absolute;right: 6px;top: 0px; font-size: 12px;">{{$t('common.shares')}}</span>
											</el-form-item>
											<el-form-item prop="buyNum" style="margin-bottom:10px;">
												<el-input placeholder="lever" v-model="form.lever" class="input-with-select" disabled>
													<el-select v-model="form.lever" title="Click to select leverage" slot="prepend" placeholder="please choose">
														<el-option v-for="i in siteLeverList" :key="i.value" :label="i.label" :value="i.value"></el-option>
													</el-select>
												</el-input>
												<span style="position: absolute;right: 10px;top: 2px; font-size: 12px;"></span>
											</el-form-item>
										</div>
										<p class="prompt clearfix">
											<el-form-item label="direction" prop="buyType">
												<el-radio-group v-model="form.buyType">
													<el-radio label="bullish" value="0"></el-radio>
													<el-radio label="bearish" value="1"></el-radio>
												</el-radio-group>
											</el-form-item>
										</p>
										<el-row class="buy-item" >
											<el-col :span="12">
												<span class="baozheng" style="font-size: 13px;">{{$t('common.margin')}}: </span>
												<span class="price">{{(price/ form.lever).toFixed(2) || 0}}</span>
											</el-col>
											<el-col :span="12">
												<span class="keyong">{{$t('common.totalHandlingFee')}}</span>
												<span class="price">{{poundage?poundage:0}}</span>
												<el-tooltip class="item" effect="dark" :content="'Total Fee = Buy Fee ('+ (settingInfo.buyFee*100) + '%）+ Stamp duty（'+ (settingInfo.dutyFee*100) + '%） + Spread fee（'+ (settingSpreadRate.spreadRate*100).toFixed(2) + '%）'"
												 placement="bottom-end">
												</el-tooltip>
											</el-col>
										</el-row>
										 <el-row class="buy-item" >
											<el-col :span="12">
												<span class="keyong">{{$t('tradingFloor.needToPay')}}:</span>
												<span class="price">{{total?total:0}}</span>
											</el-col>
											<el-col :span="12">
												<span class="keyong">{{$t('common.availableFunds')}}:{{$store.state.userInfo.enableAmt}}USD</span>
											</el-col>
										</el-row> 
									</div>
								</div>
							</el-form>
							<div>
								<el-button :loading="loadingBtn" class="buy-button ru" type="primary" @click="onSubmit('ruleForm')" style="margin-top:14px">Warehousing</el-button>
							</div>
						</div>
					</el-tab-pane>
					<!-- 兩融交易結束 -->

				</el-tabs>
				
			</div>

		</div>
	</div>
</template>

<script>
	import * as api from "../../../axios/api";
	import comNone from '@/pages/transaction/index'

	export default {
		components: {
			comNone
		},
		props: {
			detailsCont: {

			},
			cutIndex: {

			},
			handleOptions2: {
				type: Function,
				default: function() {},
			},
			handleOptions3: {
				type: Function,
				default: function() {},
			},
		},
		data() {
			return {
				siteInfo:{
					
				},
				activeName1:'first',
				tabPosition: "left",
				tradeDialogVisible: false, //
				loading: false,
				detail: "", // 当前股票的详情
				activeName: "first",
				tradeAgreeText: "",
				siteLeverList: [],
				form: {
					buyNum: "",
					buyType: "",
					lever: "",
					subaccountNumber: "",
				},
				settingInfo: {}, // 设置信息
				agree: true, // 协议
				buyNumber: 0, // 下单次数
				loadingBtn: false,
				futuresInfo: {}, // 期货信息
				settingSpreadRate: {
					spreadRate: 0,
				},
				isqihuo: false,
				isgupiao: false,
				exchangeNumber: "",
				subaccountList: {},
				pageNum: 1,
				newsList: [],
				width: 0,
				temi: null
			};
		},
		watch: {
			change(newVal, oldVal) {
				if (newVal !== oldVal) {
					this.getDetail(); // 分时数据
				}
				if (!newVal) {
					clearInterval(this.timer);
				}
			},
			
		},
		computed: {
			poundage() {
				//手续费= 买入手续费+印花税+点差费
				if (this.form.buyNum) {
					// * 1000
					let payfee = (this.detail.nowPrice * this.form.buyNum ).toFixed(2);
					return (
						payfee * this.settingInfo.buyFee +
						payfee * this.settingInfo.dutyFee +
						payfee * this.settingSpreadRate.spreadRate
					).toFixed(2);
					//+ (payfee * this.settingInfo.dutyFee).toFixed(2) + (payfee * this.settingSpreadRate.spreadRate).toFixed(2)
				} else {
					return 0;
				}
			},
			total() {
				if (this.form.buyNum) {
					if (this.$route.query.code.indexOf("hf_") != -1) {
						return (
							(this.detail.depositAmt * this.form.buyNum) /
							this.form.lever
						).toFixed(2);
					} else if (
						this.$route.query.code.indexOf("sh") != -1 ||
						this.$route.query.code.indexOf("sz") != -1
					) {
						return (
							(this.detail.depositAmt * this.form.buyNum) /
							this.form.lever
						).toFixed(2);
					} else {
						if (
							this.settingSpreadRate == undefined ||
							this.settingSpreadRate.spreadRate == undefined
						) {
							this.settingSpreadRate.spreadRate = 0;
						}
						let payfee =
							(this.detail.nowPrice * this.form.buyNum ) / this.form.lever; //  this.form.lever
						return (
							payfee +
							payfee * this.settingInfo.buyFee +
							payfee * this.settingInfo.dutyFee +
							payfee * this.settingSpreadRate.spreadRate
						).toFixed(2);
					}
				} else {
					return 0;
				}
				// 需支付总价 = 现价 * 股（1shares = 1000股）/ 杠杆倍数
			},
			// USDTotal() {
			// 	// 价钱 * 汇率 转为 人民币
			// 	return (this.total * this.exchangeNumber).toFixed(2);
			// },
			price() {
				if (this.form.buyNum) {
					return (this.detail.nowPrice * this.form.buyNum).toFixed(2);
				} else {
					return 0;
				}
				// 市值价 = 现价 * 股（1shares = 1000股）
			},
			change() {
				return this.$route.query.code;
			},
		},
		created() {
			this.timer = setInterval(this.getDetail, 10000);
			setInterval(()=>{
				this.activeName1 =  window.activeName1 || 'first'
			},100)
		},
		beforeDestroy() {
			clearInterval(this.timer);
		},
		mounted() {

			this.getDetail();
			this.getSettingInfo();
			this.getInfoSite();

		

			this.getNoticeList();

			if(this.$store.state.haslogin){
				this.getUserSubaccount();
			}
			setTimeout(() => {
				this.runMarquee()
				// this.$on('changeActiveName',(res)=>{
				// 	console.log(res)
				// })
			}, 1000)
			// this.$nextTick(()=>{
			// 	// var scrollBoxDOM = document.getElementById('scrollBox')
			// 	var scrollBoxDOM = this.$refs
			// 		console.log(scrollBoxDOM.scrollBox,'333333333333333')
			// 	// setInterval(()=>{
			// 	// 	// scrollBoxDOM.style.transform = 'translateX(10px)'
			// 	// },1000)
			// })
		},
		beforeDestroy() {
			clearInterval(this.temi)
		},
		methods: {
			runMarquee() {
				// 获取文字 计算后宽度
				var width = this.$refs.marquee[0].getBoundingClientRect().width;
				var marquee = this.$refs.marqueeBox;
				var disx = 0; // 位移距离
				// console.log(width)
				// //设置位移
				this.temi = setInterval(() => {
					disx--; // disx-=1; 滚动步长
					if (-disx >= width) {
						disx = 10; // 如果位移超过文字宽度，则回到起点  marquee-list的margin值
					}
					// marquee.style.transform = 
					marquee.style.transform = 'translateX(' + disx + 'px)'
				}, 30) //滚动速度
			},
			
			// 时间转换
			switchData(list, time) {
				list.forEach((item) => {
					var tempStr = item[time] + "";
					var timestamp = tempStr.slice(0, tempStr.length - 3);
					var newDate = new Date();
					newDate.setTime(timestamp * 1000);
					item[time] = newDate.toLocaleDateString();
				});
			},
			async selectDetails(item, index) {
				// 选择详情

				var data = await api.getNewsDetailList({
					id: item.id,
				});
				if (data.status == 0) {
					// this.optionalIndex = item.type
					var query = {
						pageNum: 1,
						pageSize: 15,
						type: item.type,
					};
					// this.newType = item.type
					// var newsList = await this.getNewsList(query);
					// this.switchData(newsList, "showTime");
					this.$emit('selectDetailsItem', data.data)
				}
				var data = await api.updateNewsViews({
					id: item.id,
				});
				if (data.status == 0) {}
			},
			async getNoticeList() {
				// 获取交易大厅-中间部分-通知公告
				let data = await api.getTransactionNoticeList({
					pageSize: 10,
				});
				if (data.status == 0) {
					this.switchData(data.data.list, "addTime");
					this.transactionNoticeList = data.data.list;
				}
			},
			cut(type) {
				var code = this.$route.query.code
				this.activeName = type;
				if(type == 'first'){
					 api.findUserPositionByCode({
						stockCode: code
					}).then(data=>{
						if (data.status == 0) {
							this.$store.commit('setUserPositionData', data.data.list[0])
						}
					})
					
				}else{
					api.findUserFundsPositionByCode({
						fundsCode:code
					}).then(res=>{
						if (res.status == 0) {
							this.$store.commit('setUserPositionData', data.data.list[0])
						}
					})
				}
				
			},
			async queryExchange() {
				// 基币汇率
				let data = await api.queryExchange({
					coinCode: this.futuresInfo.coinCode,
				});
				if (data.status === 0) {
					// 成功
					this.exchangeNumber = data.data;
				} else {
					Toast(data.msg);
				}
			},
			async getDetail() {
				// if (this.$route.query.code.indexOf("hf_") != -1) {
				// 	// 期货
				// 	this.isqihuo = true;
				// 	this.isgupiao = false;
				// } else if (
				// 	this.$route.query.code.indexOf("sh") != -1 ||
				// 	this.$route.query.code.indexOf("sz") != -1
				// ) {
				// 	// 指数
				// 	this.isqihuo = false;
				// 	this.isgupiao = false;
				// } else {
				// 	// 股票
				// 	this.isgupiao = true;
				// 	this.isqihuo = false;
				// }
				// if (this.$route.query.futuresInfo != undefined) {
				// 	this.futuresInfo = this.$route.query.futuresInfo;
				// 	this.queryExchange(); // 获取当前基币汇率
				// }
				let opts = {
					code: this.$route.query.code,
				};
				this.loading = true;
				
				let res1 = await api.getUsStockData(opts.code)
				let stock = res1.data[0];
				let stockDetail ={}
				stockDetail.name = stock["200024"];//股票名稱
				stockDetail.date = stock["200007"];//最近交易日期
				// stockDetail.time = stock[""];//最近成交時刻
				stockDetail.nowPrice = stock["6"];//最新價格
				stockDetail.rate = stock["11"];//漲跌
				stockDetail.hcrate = stock["56"];//漲跌幅
				stockDetail.high = stock["12"];//最高價
				stockDetail.low = stock["13"];//最低價
				stockDetail.volumn = stock["800001"];//累積成交量
				stockDetail.amount = stock[""];//成交金額
				stockDetail.yes = stock["21"];//昨收
				stockDetail.open = stock["19"];//開盤價
				
				if (stockDetail.rate > 0) {
					stockDetail.color = "upColor";
				}
				if (stockDetail.rate < 0) {
					stockDetail.color = "lowColor";
				}
				this.ucode = opts.code;
				this.loading = false;
				this.detail = stockDetail;
			},
			async getInfoSite() {
				// 获取网站信息
				let result = await api.getInfoSite();
				if (result.status === 0) {
					this.siteInfo = result.data;
					this.tradeAgreeText = this.siteInfo.tradeAgreeText;
				} else {
					this.$message.error(result.msg);
				}
			},
			async getUserSubaccount() {
				// 用户操盘中子账户
				let result = await api.getUserSubaccount();
				if (result.status === 0) {
					if (result.data.list.length > 0) {
						this.subaccountList = result.data.list;
						if (
							this.$route.query.sub != undefined &&
							this.$route.query.sub != ""
						) {
							this.form.subaccountNumber = this.$route.query.sub;
						} else {
							this.form.subaccountNumber = this.subaccountList[0].subaccountNumber;
						}
					}
				} else {
					this.$message.error(result.msg);
				}
			},
			async findSpreadRateOne() {
				// 查询点差费率
				let opts = {
					applies: this.detail.hcrate, // 涨跌幅
					turnover: this.total, //成交额
					unitprice: this.detail.nowPrice, //股票单价
					code: this.$route.query.code,
				};
				let data = await api.findSpreadRateOne(opts);
				if (data.status === 0) {
					// 成功
					if (data.data != undefined) {
						this.settingSpreadRate = data.data;
					}
					// console.log(this.settingSpreadRate)
				} else {
					this.$message.error(data.msg);
				}
			},
			async getSettingInfo() {
				// 网站设置信息
				let data = await api.getSetting();
				if (data.status === 0) {
					// 成功
					this.settingInfo = data.data;
					// 杠杆倍数，如果登录并且有代理先走代理设置杠杆
					if (
						this.$store.state.userInfo !== undefined &&
						this.$store.state.userInfo !== null &&
						this.$store.state.userInfo.phone !== "" &&
						this.$store.state.userInfo.siteLever !== null
					) {
						this.form.lever = this.$store.state.userInfo.siteLever.split("/")[0];
						this.siteLeverList = [];
						for (
							let i = 0; i < this.$store.state.userInfo.siteLever.split("/").length; i++
						) {
							let val = this.$store.state.userInfo.siteLever.split("/")[i];
							let item = {
								label: val + "倍",
								value: val,
							};
						
							this.siteLeverList.push(item);
						}
					} else {
						this.form.lever = data.data.siteLever.split("/")[0];
						this.siteLeverList = [];
						for (let i = 0; i < data.data.siteLever.split("/").length; i++) {
							let val = data.data.siteLever.split("/")[i];
							let item = {
								label: val + "倍",
								value: val,
							};
						}
					}

					// this.buyNumList = [];
					// for (let i = 0; i < 10; i++) {
					// 	if (i === 0 || i % 2 === 1) {
					// 		let val = data.data.buyMinNum * i + data.data.buyMinNum;
					// 		let item = {
					// 			label: val / 100 + "shares",
					// 			value: val / 100,
					// 		};
					// 		this.buyNumList.push(item);
					// 	}
					// }
				} else {
					this.$message.error(data.msg);
				}
			},
			async onSubmit(formName) {
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						if (!this.$store.state.haslogin) {
							this.$store.state.loginIsShow = true;
							this.$message.error(this.$t('commmon.pleaseLogin'));
							return;
						}

						// if (!this.agree) {
						// 	this.$message.error("Read and agree to the registration agreement to place an order");
						// 	return;
						// }
						var regisKong = /^\s*$/g;
						if (regisKong.test(this.form.buyNum)) {
							this.$message.error(this.$t('commmon.sharesIsNull'));
							return
						}
						if (regisKong.test(this.form.lever)) {
							this.$message.error(this.$t('commmon.leverageIsNull'));
							return
						}
						if (regisKong.test(this.form.buyType)) {

							this.$message.error(this.$t('commmon.directionIsNull'));
							return
						}

						this.loadingBtn = true;
						// 股票買入
						var gCode = this.$route.query
						let res=await api.getUsStockData(gCode.code)
						let stock = res.data[0];
						let nowPrice = stock["6"];//最新價格
						let hcrate = stock["56"];//漲跌幅
						let res2 = await api.getUsOpenClose(gCode.code)
						let preClose = res2.data["o"][0];//開盤價
						let opts = {
							stockId: gCode.code,
							buyNum: this.form.buyNum ,
							buyType: this.form.buyType === "bullish" ? 0 : 1,
							lever: this.form.lever,
							nowPrice,
							hcrate,
							preClose
						};
						let data = await api.buyUsStock(opts);
						if (data.status === 0) {
							this.buyNumber++;
							this.handleOptions2(this.buyNumber);
							this.getUserInfo();
							this.$message.success(data.data);
							var data = await api.findUserPositionByCode({
								stockCode: gCode.code
							})
							if (data.status == 0) {
							
								this.$store.commit('setUserPositionData', data.data.list[0])
							}
							
							
						} else {
							this.$message.error(data.msg);
						}
					}

					this.loadingBtn = false;
				});
			},
		
			async getUserInfo() {
				// 获取用户信息
				let data = await api.getUserInfo();
				if (data.status === 0) {
					this.$store.state.userInfo = data.data;
				} else {
					this.$message.error(data.msg);
				}
			},
			async getOpation() {
				// 获取是不是已添加自选
				let opts = {
					code: this.$route.query.code,
				};
				let data = await api.isOption(opts);
				if (data.status === 0) {
					// 0 --> 未添加
					this.isOptionOpt = false;
				} else {
					this.isOptionOpt = true;
				}
			},
		},
	};
</script>
<style lang="less" scoped>
	/deep/ .el-checkbox__inner {
		width: 11px;
		height: 11px;
	}
	// /deep/ .el-input.is-disabled .el-input__inner{
	// 	background-color:#000;
	// }
	.fage {
		position: absolute;
		left: -15px;
		width: 1px;
		height: 157px;
		background-color: #ccc;
		opacity: 0.2;
		top: 50%;
		transform: translateY(-50%);
	}

	.red-bg {
		.sanjiao {
			position: absolute;
			color: #C11815;
			top: 4px;
			left: 4px;
			font-size: 10px;
		}

		.scrollBoxCont {
			display: flex;
			align-items: center;
			height: 100%;
		}

		.bottom-new-box {
			overflow: hidden;
			height: 29px;
			width: 100%;
			background-color: #E5E5E5;
			padding: 0 15px;
			font-size: 12px;
			display: flex;
			align-items: center;
		}

		.news-list {
			width: 100%;

			p {
				width: 90%;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.bottom-right p {
				width: 80%;
			}
		}
	}

	.black-bg {
		.sanjiao {
			position: absolute;
			color: #C11815;
			top: 4px;
			left: 4px;
			font-size: 10px;
		}

		.scrollBoxCont {
			align-items: center;
			display: flex;
			height: 100%;
		}

		.bottom-new-box {
			overflow: hidden;
			height: 29px;
			width: 100%;
			background-color: #343434;
			padding: 0 15px;
			font-size: 12px;
			display: flex;
			align-items: center;
		}

		.news-list {
			width: 100%;

			p {
				width: 90%;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
			}

			.bottom-right p {
				width: 80%;
			}
		}
	}

	.black-bg .jiaoyi:hover {
		background: #09576d;
	}

	.red-bg .jiaoyi:hover {
		background: rgb(129, 14, 12);
	}

	/deep/ .el-tabs__nav-scroll {
		height: 25px;
	}

	.buy-box .buy-item[data-v-27e7503f] {
		// margin-bottom: 10px;
	}

	.el-form-item--mini.el-form-item,
	.el-form-item--small.el-form-item {
		margin: 0 !important;
	}

	/deep/ .el-form-item__content {
		margin: 0 30px 0 0 !important;
	}

	.buy-box-cont {
		margin-left: 20px;
		box-shadow: none !important;
	}

	.nowprice-box {
		height: 40px;
		line-height: 38px;
		color: #8b97b0;

		.price {
			font-size: 16px;
		}
	}

	.transaction-table .buy-table {
		/deep/ th.is-leaf {
			display: none;
		}
	}

	.buy-box {
		.buy-item {
			margin-bottom: 6px;

			.price {
				font-size: 16px;
			}
		}

		.prompt {
			font-size: 12px;
			color: #606266;
			margin-top: 10px;
		}

		.buy-name {
			font-size: 16px;
			color: #efbb53;
		}

		.buy-code {
			color: #4c5770;
			font-size: 12px;
			margin-left: 10px;
		}

		.buy-button {
			display: flex;
			justify-content: center;
			align-content: center;
			width: 92%;
			height: 30px;
			margin-bottom: 9px;
			line-height: 5px;
		}

		.check-box {
			margin-right: 10px;
		}

		/deep/ .el-form-item__label {
			color: #8b97b0;
			text-align: left;
		}

		// 涨
		/deep/ .el-radio__input.is-checked+.el-radio__label {
			color: #c11815;
		}

		/deep/ .el-radio__input.is-checked .el-radio__inner {
			border-color: #c11815;
			background: #c11815;
		}

		/deep/ .el-radio {
			color: #8b97b0;
		}
	}

	.dialog-iframe {
		.iframe-box {
			width: 100%;
			height: 460px;
		}

		.dialog-footer {
			//  height: 300px;
			margin-top: 20px;
		}
	}
</style>
