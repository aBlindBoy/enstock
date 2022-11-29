<template>
	<!-- style="box-sizing: border-box;padding: 0 0 0 30px;" -->
	<div class="wrapper buy-table table-chart2-cont">
		<div class="box-buy" style="margin-bottom: 2px;">
			<div class="box-buy-list" style="display: flex; padding: 14px 10px 10px 20px;color: rgb(108, 108, 111);font-size: 12px;">
				<div class="" style="width: 45%;">
					Name
				</div>
				<div class="" style="width: 35%;">
					price
				</div>
				<div class="" style="width: 20%;">
					volume
				</div>
			</div>
			<div v-for="(item,index) in list" :key="item.name" class="box-buy-list" style="line-height: 28px; height: 28px;padding: 0 10px 0 20px;display: flex;font-size: 12px;">
				<div style="width: 45%;">{{item.name}}</div>
				<div style="width: 35%;" :style="index==2?'color:#41A44F;':'color:#B12525'">
					<span v-if="item.price === 0">-</span>
					<span v-else>{{Number(item.price).toFixed(2)}}</span>
				</div>
				<div style="width: 20%;" :style="index==2?'color:#41A44F;':'color:#B12525'">{{item.volumes}}</div>
			
			</div>
			<div style="display: flex;align-items: center;justify-content: center;">
				<div class="buy-bottom-xianjia">
					<span style="font-size: 12px;">Current price {{detail.nowPrice}}</span>
				</div>
			</div>
			<div v-for="(item,index) in listbuy" :key="item.name" class="box-buy-list" style="line-height: 28px; height: 28px;padding: 0 10px 0 20px;display: flex;font-size: 12px;">
				<div style="width: 45%;">{{item.name}}</div>
				<div style="width: 35%;" :style="index==2?'color:#41A44F;':'color:#B12525'">
					<span v-if="item.price === 0">-</span>
					<span v-else>{{Number(item.price).toFixed(2)}}</span>
				</div>
				<div style="width: 20%;" :style="index==2?'color:#41A44F;':'color:#B12525'">{{item.volumes}}</div>
			
			</div>
		</div>


		<!-- <div class="table transaction-table ">
			<el-table stripe :data="list" style="width: 100%">
				<el-table-column prop="name" label="名称"></el-table-column>
				<el-table-column prop="price" label="价格">
					<template slot-scope="scope">
						<div class="price">
							<div :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">
								<span v-if="scope.row.price === 0">-</span>
								<span v-else>{{Number(scope.row.price).toFixed(2)}}</span>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="volumes" label="成交量"></el-table-column>
			</el-table>
			<div class="text-center nowprice-box">
				现价
				<span :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{detail.nowPrice}}</span>
			</div>
			<el-table stripe :data="listbuy" class="buy-table" style="width: 100%">
				<el-table-column prop="name"></el-table-column>
				<el-table-column prop="price" label="价格">
					<template slot-scope="scope">
						<div class="price">
							<div :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">

								<span v-if="scope.row.price === 0">-</span>
								<span v-else>{{Number(scope.row.price).toFixed(2)}}</span>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="volumes" label="成交量"></el-table-column>
			</el-table>
		</div>
		
		 -->
		<div class="listhi-cont" ref="test" :style="'height:'+windowHeight+'px;'" style="width: 100%;  padding: 20px 0 0 0;overflow: auto;">
			<div style="display:flex;align-items: center; justify-content: center; margin-bottom: 20px;" >
				<span class="buy-xian">——</span>
				<span style="margin: 0 10px;" class="buy-jiaoyi">Transaction history</span>
				<span class="buy-xian">——</span>
			</div>
			<div class="buy-item-red-bg buy-item-teshu" style="margin: 0px 20px 10px 20px; padding-bottom: 5px;" v-for="(item,index) in dealHistoryList"
			 :key="item.name">
				<div style="display: flex;">
					<div style="font-weight: bold;font-size: 12px;" class="history-title">{{item.stockName}}</div>
					<div style="font-size: 12px;position: relative;">
						<span class="iconfont icon-triangle-left sanjiao"></span>
						<span class="quantity">{{item.stockCode}}</span>
					</div>
				</div>
				<div style="display: flex; justify-content: space-between;margin-top: 12px;">
					<div>
						<span style="font-size: 12px;" class="xianjin">Current price：</span>
						<span style="font-size: 12px;" class="current-price">{{item.now_price || 0}}</span>
					</div>
					<div>
						<span style="font-size: 12px;" class="xianjin">Quote change：</span>
						<span style="font-size: 12px;" class="current-price">{{item.orderDirection}}</span>
					</div>
				</div>
			</div>
		</div>

		<BuyFutures :handleOptions3="handleOptions3" :futuresInfo="futuresInfo" ref="futuresDialog" />
	</div>
</template>

<script>
	import * as api from "../../../axios/api";
	import BuyFutures from "./futuresbuy-dialog";

	export default {
		components: {
			BuyFutures,
		},
		props: {
			// settingInfo:{
			//     type:Object,
			//     default(){
			//        return {
			//             buyMinAmt:'',
			//             buyMinNum:'',
			//             buyMaxNum:'',
			//        }
			//     }
			// },
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
				windowHeight: document.documentElement.clientHeight - 475, //实时屏幕高度
				dealHistoryList: [],
				tradeDialogVisible: false, //
				loading: false,
				detail: "", // 当前股票的详情
				activeName: "first",
				tradeAgreeText: "",
				list: [{
						name: "sell five",
						price: "",
						volumes: ""
					},
					{
						name: "sell four",
						price: "",
						volumes: ""
					},
					{
						name: "sell three",
						price: "",
						volumes: ""
					},
					{
						name: "sell two",
						price: "",
						volumes: ""
					},
					{
						name: "sell one",
						price: "",
						volumes: ""
					},
				],
				listbuy: [{
						name: "buy one",
						price: "",
						volumes: ""
					},
					{
						name: "buy two",
						price: "",
						volumes: ""
					},
					{
						name: "buy three",
						price: "",
						volumes: ""
					},
					{
						name: "buy four",
						price: "",
						volumes: ""
					},
					{
						name: "buy five",
						price: "",
						volumes: ""
					},
				],
				// buyNumList: [{
				// 	label: "50手",
				// 	value: 50
				// }],
				siteLeverList: [],
				form: {
					buyNum: "",
					buyType: "",
					lever: "",
					subaccountNumber: "",
				},
				rule: {
					buyNum: [{
						required: true,
						message: "Please enter or select the lot size to buy",
						trigger: "blur"
					}, ],
					buyType: [{
						required: true,
						message: "Please select the buying and selling direction",
						trigger: "blur"
					}, ],
				},
				ruleFunds: {
					subaccountNumber: [{
						required: true,
						message: "please choose sub account",
						trigger: "blur"
					}, ],
					buyNum: [{
						required: true,
						message: "Please enter or select the lot size to buy",
						trigger: "blur"
					}, ],
					buyType: [{
						required: true,
						message: "Please select the buying and selling direction",
						trigger: "blur"
					}, ],
				},

				settingInfo: {}, // 设置信息
				agree: false, // 协议
				buyNumber: 0, // 下单次数
				loadingBtn: false,
				futuresInfo: {}, // 期货信息
				settingSpreadRate: {
					spreadRate: 0
				},
				isqihuo: false,
				isgupiao: false,
				exchangeNumber: "",
				subaccountList: {},
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
			// <!--在watch中监听实时宽高-->
			// windowHeight(val) {
			// 	let that = this;
			// 	console.log("实时屏幕高度：", val, that.windowHeight);
			// },
			// windowWidth(val) {
			// 	let that = this;
			// 	console.log("实时屏幕宽度：", val, that.windowHeight);
			// }
		},
		computed: {
			poundage() {
				//手续费= 买入手续费+印花税+点差费
				if (this.form.buyNum) {
					let payfee = (this.detail.nowPrice * this.form.buyNum * 100).toFixed(2);
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
							(this.detail.nowPrice * this.form.buyNum * 100) / this.form.lever; //  this.form.lever
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
				// 需支付总价 = 现价 * 股（1手 = 100股）/ 杠杆倍数
			},
			USDTotal() {
				// 价钱 * 汇率 转为 人民币
				return (this.total * this.exchangeNumber).toFixed(2);
			},
			price() {
				if (this.form.buyNum) {
					return (this.detail.nowPrice * this.form.buyNum * 100).toFixed(2);
				} else {
					return 0;
				}
				// 市值价 = 现价 * 股（1手 = 100股）
			},
			change() {
				return this.$route.query.code;
			},
		},
		created() {
			this.timer = setInterval(this.getDetail, 10000);
		},
		beforeDestroy() {
			clearInterval(this.timer);
		},
		mounted() {
			var that = this

			setInterval(() => {
					this.windowHeight = document.documentElement.clientHeight - 475; //实时屏幕高度//实时屏幕高度
			}, 500);
			// window.onresize = () => {
			// 	return (() => {
			// 		window.fullHeight = document.documentElement.clientHeight;
			// 		window.fullWidth = document.documentElement.clientWidth;
			// 		that.windowHeight = window.fullHeight; // 高
			// 		that.windowWidth = window.fullWidth; // 宽
			// 	})()
			// };


			this.getDetail();
			this.getSettingInfo();
			this.getInfoSite();
			
			if(this.$store.state.haslogin){
				this.getUserSubaccount();
				
			}
			this.getDealHistoryList();
			// this.$refs.test.style.height = this.windowHeight
			// console.log(this.windowHeight,this.$refs.test.style.height,'2222222')
			// console.log(document.querySelector('.listhi-cont').style.height)
			// document.querySelector('.listhi-cont').style.height = this.windowHeight
		},
		methods: {
			async getDealHistoryList() {
				// 获取交易历史记录
				let data = await api.getDealHistory({
					pageSize: 10,
				});
				if (data.status == 0) {
					this.dealHistoryList = data.data.list;
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
				if (this.$route.query.code.indexOf("hf_") != -1) {
					this.isqihuo = true;
					this.isgupiao = false;
				} else if (
					this.$route.query.code.indexOf("sh") != -1 ||
					this.$route.query.code.indexOf("sz") != -1
				) {
					this.isqihuo = false;
					this.isgupiao = false;
				} else {
					this.isgupiao = true;
					this.isqihuo = false;
				}
				if (this.$route.query.futuresInfo != undefined) {
					this.futuresInfo = this.$route.query.futuresInfo;
					this.queryExchange(); // 获取当前基币汇率
				}
				let opts = {
					code: this.$route.query.code,
				};
				this.loading = true;
				//let data = await api.getSingleStock(opts);
				let [res1,res2]=await Promise.all([api.getTwStockData(opts.code),api.getTwStockExchange(opts.code)])
				let data={}
				let data1=res1.data[0]
				let data2=res2.data[0]['五檔']
				data.name=data1['股票名稱']
				data.code=opts.code
				data.spell=''
				data.gid=opts.code
				data.nowPrice=data1['當盤成交價']
				data.hcrate=data1['Quote change']
				data.today_max=data1['最高價']
				data.today_min=data1['最低價']
				data.business_balance=data1['成交金額']
				data.business_amount=data1['當盤成交量']
				data.preclose_px=parseFloat(data1['開盤價'])+parseFloat(data1['漲跌'])
				data.open_px=data1['開盤價']
				data.buy1=data2['買價1'].substring(1).replace(/\s+/g,"")
				data.buy2=data2['買價2'].substring(1).replace(/\s+/g,"")
				data.buy3=data2['買價3'].substring(1).replace(/\s+/g,"")
				data.buy4=data2['買價4'].substring(1).replace(/\s+/g,"")
				data.buy5=data2['買價5'].substring(1).replace(/\s+/g,"")
				data.sell1=data2['Selling price1'].substring(1).replace(/\s+/g,"")
				data.sell2=data2['Selling price2'].substring(1).replace(/\s+/g,"")
				data.sell3=data2['Selling price3'].substring(1).replace(/\s+/g,"")
				data.sell4=data2['Selling price4'].substring(1).replace(/\s+/g,"")
				data.sell5=data2['Selling price5'].substring(1).replace(/\s+/g,"")
				data.buy1_num=data2['買量1']
				data.buy2_num=data2['買量2']
				data.buy3_num=data2['買量3']
				data.buy4_num=data2['買量4']
				data.buy5_num=data2['買量5']
				data.sell1_num=data2['賣量1']
				data.sell2_num=data2['賣量2']
				data.sell3_num=data2['賣量3']
				data.sell4_num=data2['賣量4']
				data.sell5_num=data2['賣量5']
				
				// 卖
				this.list[0].price = data.sell5;
				this.list[1].price = data.sell4;
				this.list[2].price = data.sell3;
				this.list[3].price = data.sell2;
				this.list[4].price = data.sell1;
				this.list[0].volumes = data.sell5_num;
				this.list[1].volumes = data.sell4_num;
				this.list[2].volumes = data.sell3_num;
				this.list[3].volumes = data.sell2_num;
				this.list[4].volumes = data.sell1_num;
				// 买
				this.listbuy[0].price = data.buy1;
				this.listbuy[1].price = data.buy2;
				this.listbuy[2].price = data.buy3;
				this.listbuy[3].price = data.buy4;
				this.listbuy[4].price = data.buy5;
				this.listbuy[0].volumes = data.buy1_num;
				this.listbuy[1].volumes = data.buy2_num;
				this.listbuy[2].volumes = data.buy3_num;
				this.listbuy[3].volumes = data.buy4_num;
				this.listbuy[4].volumes = data.buy5_num;
				this.loading = false;
				this.detail = data;
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
								label: val + "張",
								value: val
							};
							this.siteLeverList.push(item);
						}
					} else {
						this.form.lever = data.data.siteLever.split("/")[0];
						this.siteLeverList = [];
						for (let i = 0; i < data.data.siteLever.split("/").length; i++) {
							let val = data.data.siteLever.split("/")[i];
							let item = {
								label: val + "張",
								value: val
							};
							this.siteLeverList.push(item);
						}
					}

					// this.buyNumList = [];
					// for (let i = 0; i < 10; i++) {
					// 	if (i === 0 || i % 2 === 1) {
					// 		let val = data.data.buyMinNum * i + data.data.buyMinNum;
					// 		let item = {
					// 			label: val / 100 + "手",
					// 			value: val / 100
					// 		};
					// 		this.buyNumList.push(item);
					// 	}
					// }
				} else {
					this.$message.error(data.msg);
				}
			},
			onSubmit(formName) {
				// 先判斷是否登錄
				if (!this.$store.state.haslogin) {
					this.$store.state.loginIsShow = true;
					this.$message.error("請先登錄");
					return;
				}
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						if (!this.agree) {
							this.$message.error("閱讀並同意註冊協議才能下單");
							return;
						}
						this.loadingBtn = true;
						if (this.$route.query.code.indexOf("hf_") != -1) {
							//期貨買入
							let opts = {
								FuturesId: this.detail.id,
								buyNum: this.form.buyNum ? this.form.buyNum : 0,
								buyType: this.form.buyType === "Bullish" ? 0 : 1,
								lever: this.form.lever ? this.form.lever : 0,
							};
							let data = await api.buyFutures(opts);
							if (data.status === 0) {
								this.buyNumber++;
								// this.handleOptions3(this.buyNumber)
								this.$message.success(data.data);
								this.getUserInfo(); // 刷新
							} else {
								this.$message.error(data.msg);
							}
						} else if (
							this.$route.query.code.indexOf("sh") != -1 ||
							this.$route.query.code.indexOf("sz") != -1
						) {
							//指數買入
							let opts = {
								indexId: this.detail.id,
								buyNum: this.form.buyNum ? this.form.buyNum : 0,
								buyType: this.form.buyType === "Bullish" ? 0 : 1,
								lever: this.form.lever ? this.form.lever : 0,
							};
							this.loadingBtn = true;
							let data = await api.indexBuy(opts);
							if (data.status === 0) {
								this.buyNumber++;
								// this.handleOptions2(this.buyNumber)
								this.$message.success(data.data);
								this.getUserInfo(); // 刷新
							} else {
								this.$message.error(data.msg);
							}
						} else {
							// 股票買入
							let opts = {
								stockId: this.detail.id,
								buyNum: this.form.buyNum ? this.form.buyNum * 100 : 0,
								buyType: this.form.buyType === "Bullish" ? 0 : 1,
								lever: this.form.lever,
							};
							let data = await api.buy(opts);
							if (data.status === 0) {
								this.buyNumber++;
								this.handleOptions2(this.buyNumber);
								this.getUserInfo();
								this.$message.success(data.data);
							} else {
								this.$message.error(data.msg);
							}
						}

						this.loadingBtn = false;
					}
				});
			},
			onFundsSubmit(formName) {
				// 先判斷是否登錄
				if (!this.$store.state.haslogin) {
					this.$store.state.loginIsShow = true;
					this.$message.error("請先登錄");
					return;
				}
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						if (!this.agree) {
							this.$message.error("閱讀並同意註冊協議才能下單");
							return;
						}
						this.loadingBtn = true;

						// 股票買入
						let opts = {
							stockId: this.detail.id,
							buyNum: this.form.buyNum ? this.form.buyNum * 100 : 0,
							buyType: this.form.buyType === "Bullish" ? 0 : 1,
							lever: this.form.lever,
							subaccountNumber: this.form.subaccountNumber,
						};
						let data = await api.buyFunds(opts);
						if (data.status === 0) {
							this.buyNumber++;
							this.handleOptions2(this.buyNumber);
							this.getUserInfo();
							this.$message.success(data.data);
						} else {
							this.$message.error(data.msg);
						}

						this.loadingBtn = false;
					}
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
			agreeTrade() {
				// 同意注册协议协议
				this.agree = true;
				this.tradeDialogVisible = false;
			},
			toTransaction3(row, column, event) {
				row.nowPrice = this.detail.nowPrice;
				// 期货交易 先判断是否登录
				if (this.$store.state.haslogin) {
					if (+row.transState === 1) {
						this.futuresInfo = row;
						this.$refs.futuresDialog.DialogVisible = true;
					} else {
						this.$message.error("該期貨暫不能交易!");
					}
				} else {
					this.$store.state.loginIsShow = true;
				}
			},
		},
	};
</script>
<style lang="less" scoped>
	/deep/ .black-bg .transaction-table .el-table tr {
		background-color: red;
	}

	.red-bg {
		.listhi-cont{
			padding: 11px 0px 0px !important;
		}
		.box-buy-list{
			background-color: rgb(242, 242, 242);
			margin-bottom: 1px;
		}
		.box-buy{
			padding: 0 10px;
		}
		.buy-bottom-xianjia {
			color: #000;
			margin-top: 1px;
			margin-bottom: 1px;
			font-weight: 400;
			color: #000;
			display: inline-flex;
			justify-content: center;
			align-items: center;
			width: 100%;
			height: 30px;
			background: rgb(244, 245, 246);
			border-radius: 3px;
		
		}
		.buy-top-box {
			padding-bottom: 5px;
			margin-bottom: 2px;
			background-color: #fff;

			.buy-list-cont {
				.buy-item-name {
					margin-left: 10px;
				}

				.buy-item-changjiao {
					margin-right: 10px;
				}

				padding: 0 5px;

				.buy-list-item {
					background-color: rgb(242, 242, 242);
				}
			}

			

			.buy-list-title {
				font-size: 12px;
				font-weight: 400;
				color: #4A4A4E;
			}

			.buy-list-title>div {}
		}

		.buy-item-red-bg {
			border-bottom: 1px solid #E5E5E5;
		}

		.listhi-cont {
			background-color: #fff;
		}

		.history-title {
			color: #000;
		}

		.sanjiao {
			position: absolute;
			color: #c11815;
			top: 3px;
			/* width: 0px; */
			left: 3px;
			font-size: 10px;
		}

		.current-price {
			color: #c11815;
		}

		.quantity {
			display: inline-block;
			background-color: #c11815;
			text-align: center;
			margin-left: 10px;
			border-radius: 2px;
			line-height: 13px;
			color: #fff;
			width: 49px;
			height: 13px;
		}

		.listhi-cont::-webkit-scrollbar {
			/*滚动条整体样式*/
			width: 5px;
			/*高宽分别对应横竖滚动条的尺寸*/
			height: 1px;
		}

		.listhi-cont::-webkit-scrollbar-thumb {
			/*滚动条里面小方块*/
			border-radius: 5px;
			box-shadow: inset 0 0 5px rgba(199, 14, 14, 0.2);
			background: rgb(193, 24, 21);
		}

		.listhi-cont::-webkit-scrollbar-track {
			/*滚动条里面轨道*/
			box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
			border-radius: 10px;
			background: #ededed;
		}

		.buy-xian {
			color: #e0e0ec;
		}
	}

	.black-bg {
		.box-buy{
			background-color: #000;
		}
		.buy-bottom-xianjia {
			margin-top: 1px;
			margin-bottom: 1px;
			font-weight: 400;
			color: #FFFFFF;
			display: inline-flex;
			justify-content: center;
			align-items: center;
			width: 95%;
			height: 30px;
			background: #293746;
			border-radius: 3px;
		}
		.box-buy-list>div {
			// width: 33.5%;
		}

		.buy-item-teshu {
			border-bottom: 1px solid rgba(255, 255, 255, 0.1);
		}

		.buy-top-box {
			padding-bottom: 5px;
			margin-bottom: 2px;
			background-color: #000;

			.buy-list-cont {
				padding: 0 10px;
			}

			

			.buy-list-title {
				font-size: 12px;
				font-weight: 400;
				color: #4A4A4E;
			}

			.buy-list-title>div {}
		}

		.buy-xian {
			color: #3E3E42;
		}

		.buy-jiaoyi {

			font-size: 14px;
			font-family: MicrosoftYaHeiLight;
			font-weight: 400;
			color: #DEDEDF;
		}

		.listhi-cont {
			background-color: #000;
			color: #ececec;
		}

		.history-title {
			color: #bcbcbc;
		}

		.sanjiao {
			position: absolute;
			color: #138eb4;
			top: 3px;
			/* width: 0px; */
			left: 3px;
			font-size: 10px;
		}

		.current-price {
			color: #c11815;
		}

		.quantity {
			display: inline-block;
			background-color: #138eb4;
			text-align: center;
			margin-left: 10px;
			border-radius: 2px;
			line-height: 13px;
			color: #dfdfdf;
			width: 49px;
			height: 13px;
		}

		.listhi-cont::-webkit-scrollbar {
			/*滚动条整体样式*/
			width: 5px;
			/*高宽分别对应横竖滚动条的尺寸*/
			height: 1px;
		}

		.listhi-cont::-webkit-scrollbar-thumb {
			/*滚动条里面小方块*/
			border-radius: 5px;
			box-shadow: inset 0 0 5px rgba(199, 14, 14, 0.2);
			background: #138eb4;
		}

		.listhi-cont::-webkit-scrollbar-track {
			/*滚动条里面轨道*/
			box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
			border-radius: 10px;
			background: #ededed;
		}

		.xianjin {
			color: #5A5A5E;
		}
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
			margin-bottom: 10px;

			.price {
				font-size: 16px;
			}
		}

		.prompt {
			font-size: 12px;
			color: #606266;
			margin-bottom: 10px;
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
			width: 100%;
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
