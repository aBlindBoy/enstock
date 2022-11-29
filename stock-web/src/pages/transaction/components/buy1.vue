<template>
	<div style="height: 216px;">
		<!-- :style="'?'':'margin-bottom: 20px;'" -->
		<div class="wrapper buy-table" style="display:flex;justify-content: space-between; width: 100%;" :style="activeName1 == 'three'?'margin-bottom: 30px;':activeName1 == 'five'?'margin-bottom: 25px;':''">
			<div class="" style="margin-bottom: 10px;" v-if="activeName1 == 'first' || activeName1 == 'four' || activeName1 == 'start'">
				<div  style="width: 50px;display: flex;justify-content: center;margin-top: 20px;align-items: center;flex-direction: column;cursor: pointer;">
					<div @click="cut('first')" style=" cursor: pointer; padding: 20px 10px;color: #fff;border-radius: 5px;margin-bottom: 2px;"
					 :class="activeName == 'first'?'current':'currentNone'" class="jiaoyi">
						<div style="width: 15px;">Two financial transactions</div>
					</div>
					<div @click="cut('two')" :class="activeName == 'two'?'current':'currentNone'" class="jiaoyi" style="padding:20px 10px;color: rgb(255, 255, 255);border-radius: 5px;" v-if="$store.state.productSetting.fundsDisplay">
						<div style="width: 15px;">split trading</div>
					</div>
				</div>
			</div>
			<div style="display: flex;justify-content: space-between;width: 100%;">
				<el-tabs v-model="activeName" class="buy-box-cont" style="width: 100%;">
					<!-- 兩融交易開始 -->
					<el-tab-pane name="first">
						<!-- 買 -->
						<div class="buy-box" style="margin-top: 15px;">
							<el-form ref="ruleForm" :hide-required-asterisk="true" size="mini" :model="form" label-width="60px">
								<div class="buy-item" style="display: flex;">
									<span class="buy-name1">{{detail.name}}</span>
									<div class="" style="position: relative;">
										<span class="iconfont icon-triangle-left sanjiao"></span>
										<span class="buy-code1">{{detail.code}}</span>
									</div>
									<span style="font-size:12px;">Current price：</span>

									<span style="font-size:12px;color:#B12525;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{detail.nowPrice}}</span>
									<span style="font-size:12px;margin-left:10px">Quote change：</span>
									<span style="font-size:12px;color:#B12525;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{Number(detail.hcrate).toFixed(3)}}%</span>
									
								</div>


								<el-row class="buy-item">
									<el-col :span="12"></el-col>
									<el-col :span="12"></el-col>
								</el-row>
								<div style="display: flex;">
									<div>
										<div style="display: flex;position: relative;">
											<el-form-item prop="buyNum" style="margin-bottom:10px;margin-right: 10px;">
												<el-input placeholder="" v-model="form.buyNum" class>
													<!-- <el-select v-model="form.buyNum" title="點擊選擇張數" slot="prepend" placeholder="請選擇">
														<el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>
													</el-select> -->
													<!-- <el-select v-model="form.buyNum" title="點擊選擇手數" slot="prepend" placeholder="請選擇">
														<el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>
													</el-select> -->
												</el-input>
												<span style="position: absolute;right: 6px;top: 0px; font-size: 12px;">張</span>

												<!-- <el-form-item label="手數" prop="buyNum" style="margin-bottom:10px;">
													<el-input placeholder="手數" v-model="form.buyNum" class="input-with-select">
				
														<el-button slot="append">手</el-button>
													</el-input>
												</el-form-item> -->

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
													<el-radio label="Bullish" value="0"></el-radio>
													<el-radio label="Bearish" value="1"></el-radio>
												</el-radio-group>
											</el-form-item>
										</p>

										<el-row class="buy-item" v-if="!isqihuo">
										</el-row>
										<el-row class="buy-item" v-if="isgupiao">
											<el-col :span="12">
												<span class="baozheng" style="font-size: 13px;">Margin：</span>
												<span class="price">{{(price/ form.lever).toFixed(2) || 0}}</span>
											</el-col>
											<el-col :span="12">
												<sapn class="keyong">Total handling fee:</sapn>
												<span class="price">{{poundage?poundage:0}}</span>
												<el-tooltip class="item" effect="dark" :content="'Total Fee = Buy Fee ('+ (settingInfo.buyFee*100) + '%）+ Stamp duty（'+ (settingInfo.dutyFee*100) + '%） + Spread fee（'+ (settingSpreadRate.spreadRate*100).toFixed(2) + '%）'"
												 placement="bottom-end">
													<!-- <i class="iconfont icon-xiangqing"></i> -->
												</el-tooltip>
											</el-col>
										</el-row>
										<el-row class="buy-item" v-if="isgupiao">
											<el-col :span="24"></el-col>
										</el-row>
										<el-row class="buy-item" v-if="!isqihuo">
											<el-col :span="12" v-if="isgupiao">
												<span class="keyong">need to pay:</span>
												<span class="price">{{total?total:0}}</span>
											</el-col>
											<el-col :span="12" v-if="isgupiao">
												<span class="keyong">Available funds:{{$store.state.userInfo.enableAmt}}USD</span>
											</el-col>
											<el-col :span="12" v-if="!isgupiao">
												<span class="keyong">need to pay:</span>
												<span class="price">{{total?total:0}}</span>
											</el-col>
											<el-col :span="12" v-if="!isgupiao">
												<span class="keyong">Available funds:{{$store.state.userInfo.enableIndexAmt}}USD</span>
											</el-col>
										</el-row>
										<el-row class="buy-item" v-if="isqihuo">
											<el-col :span="24">
												<span class="keyong">need to pay:</span>
												<span class="price">{{total?total:0}} {{futuresInfo.coinCode}}</span>
												≈
												<span class="price">{{USDTotal?USDTotal:0}} USD</span>
											</el-col>
											<el-col :span="24">
											<p class="prompt clearfix" v-if="isqihuo">
												<!-- pull-right -->
												<span class="keyong">Available funds:{{$store.state.userInfo.enableFuturesAmt}}USD</span>
											</p>
											</el-col>
										</el-row>
										
										<!-- <el-row class="buy-item" style="font-size:10px;">
											<el-checkbox class="check-box" v-model="agree" name="type" style="font-size:10px;"></el-checkbox>我同意
											<a href="javascript:;" @click="tradeDialogVisible = true">《{{siteInfo.tradeAgreeTitle}}》</a>
										</el-row> -->
										<el-dialog :title="siteInfo.tradeAgreeTitle" class="agree-dialog" :center="true" :visible.sync="tradeDialogVisible"
										 width="80%">
											<div class="dialog-iframe">
												<div class="content" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
													<p v-for="item in tradeAgreeText.split('。')" :key="item">{{item}}。</p>
												</div>
												<!-- <iframe class="iframe-box" :src="$store.state.siteInfo.tradeAgree" frameborder="0"></iframe> -->
												<div slot="footer" class="text-center dialog-footer clearfix">
													<el-button type="primary" @click="agreeTrade">I have read and agree{{siteInfo.tradeAgreeTitle}}</el-button>
												</div>
											</div>
										</el-dialog>
									</div>
								</div>
							</el-form>
							<div>
								<el-button :loading="loadingBtn" class="buy-button ru" type="primary" @click="onSubmit('ruleForm')" style="margin-top:14px">Warehousing</el-button>
							</div>
						</div>
					</el-tab-pane>
					<!-- 兩融交易結束 -->

					<!-- 分倉交易開始 -->
					<el-tab-pane name="two">
						<!-- 買 -->
						<!-- :rules="ruleFunds" -->
						<div class="buy-box" style="margin-top: 15px;">
							<el-form ref="ruleForm1" :hide-required-asterisk="true" size="mini" :model="form" label-width="60px">
								<div class="buy-item" style="display: flex; align-items: center;">
									<span class="buy-name1">{{detail.name}}</span>
									<div class="" style="position: relative;">
										<span class="iconfont icon-triangle-left sanjiao"></span>
										<span class="buy-code1">{{detail.code}}</span>
									</div>
									<span style="font-size:12px;">Current price：</span>
									<span style="font-size:12px;color:#B12525;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{detail.nowPrice}}</span>
									<span style="font-size:12px;margin-left:10px">Quote change：</span>
									<span style="font-size:12px;color:#B12525;" :class="detail.hcrate < 0 ?'green price':detail.hcrate > 0 ?'red price':'price'">{{Number(detail.hcrate).toFixed(3)}}%</span>
									<div class="zi " style="margin-left: 5px;width: 23%;display: flex;align-items: center;">
										<span style="font-size: 10px;">子:</span>
										<el-select v-model="form.subaccountNumber" title="Click to select sub account" slot="prepend" placeholder="please choose">
											<el-option v-for="i in subaccountList" :key="i.subaccountNumber" :label="i.subaccountNumber" :value="i.subaccountNumber"></el-option>
										</el-select>
									</div>
								</div>
								<el-row class="buy-item">
									<el-col :span="12"></el-col>
									<el-col :span="12"></el-col>
								</el-row>
								<div>
									<div style="display: flex;">
										<el-form-item prop="buyNum" style="margin-bottom:10px;position: relative;">
											<el-input placeholder="" v-model="form.buyNum" class="input-with-select">
												<el-select v-model="form.buyNum" title="Click to select quantity" slot="prepend" placeholder="please choose">
													<el-option v-for="i in buyNumList" :key="i.value" :label="i.label" :value="i.value"></el-option>
												</el-select>
												<!-- <el-button slot="append">手</el-button> -->
											</el-input>
											<!-- <span style="position: absolute;right: 6px;top: 0px; font-size: 12px;">張</span> -->
										</el-form-item>
										<el-form-item prop="buyNum" style="margin-bottom:10px;position: relative;">
											<el-input placeholder="槓桿" v-model="form.lever" class="input-with-select">
												<el-select v-model="form.lever" title="Click to select leverage" slot="prepend" placeholder="please choose">
													<el-option v-for="i in siteLeverList" :key="i.value" :label="i.label" :value="i.value"></el-option>
												</el-select>
											</el-input>
											<span style="position: absolute;right: 10px;top: 2px; font-size: 12px;">multiple</span>
										</el-form-item>
									</div>
									<p class="prompt clearfix">
										<!-- <span class="pull-left">最小購買{{Number(settingInfo.buyMinNum)/100}}手</span> -->
										<!-- <span class="pull-right">最大可購買{{Number(settingInfo.buyMaxNum)/100}}手</span> -->
										<el-form-item label="方向" prop="buyType">
											<el-radio-group v-model="form.buyType">
												<el-radio label="Bullish" value="0"></el-radio>
												<el-radio label="Bearish" value="1"></el-radio>
											</el-radio-group>
										</el-form-item>
									</p>

									<el-row class="buy-item" v-if="!isqihuo">
										<!-- <el-col :span="8">{{form.buyNum * 100}}股</el-col> -->
										<!-- <el-col class="text-right" :span="16">市值：{{price}}USD</el-col> -->
									</el-row>
									<el-row class="buy-item" v-if="isgupiao">
										<el-col :span="12">
											<span style="font-size: 13px;">Margin：</span>
											<span class="price">{{(price/ form.lever).toFixed(2) || 0}}</span>
										</el-col>
										<el-col :span="12">
											<sapn class="keyong">Total handling fee:</sapn>
											<span class="price">{{poundage?poundage:0}}</span>
											<el-tooltip class="item" effect="dark" :content="'Total Fee = Buy Fee ('+ (settingInfo.buyFee*100) + '%）+ Stamp duty（'+ (settingInfo.dutyFee*100) + '%） + Spread fee（'+ (settingSpreadRate.spreadRate*100).toFixed(2) + '%）'"
											 placement="bottom-end">
												<!-- <i class="iconfont icon-xiangqing"></i> -->
											</el-tooltip>
											<!-- <span class="prompt pull-right">最小購買{{settingInfo.buyMinAmt}}USD</span> -->
										</el-col>
									</el-row>
									<el-row class="buy-item" v-if="isgupiao">
										<el-col :span="24"></el-col>
									</el-row>
									<el-row class="buy-item" v-if="!isqihuo">
										<el-col :span="12">
											<sapn class="keyong">need to pay:</sapn>
											<span class="price">{{total?total:0}}</span>
										</el-col>
										<el-col :span="12">
											<span class="keyong">Available funds:{{$store.state.userInfo.enableAmt}}USD</span>
										</el-col>
									</el-row>
									<el-row class="buy-item" v-if="isqihuo">
										<el-col :span="24">
											<sapn class="keyong">need to pay:</sapn>
											<span class="price">{{total?total:0}} {{futuresInfo.coinCode}}</span>
											≈
											<span class="price">{{USDTotal?USDTotal:0}} </span>
										</el-col>
									</el-row>
									<p class="prompt clearfix" v-if="isqihuo">
										<!-- pull-right -->
										<span class="keyong">可用資金：{{this.$store.state.userInfo.userFuturesAmt}}USD</span>
									</p>
									<!-- <el-row class="buy-item" style="font-size:10px;">
										<el-checkbox class="check-box" v-model="agree" name="type"></el-checkbox>我同意
										<a href="javascript:;" @click="tradeDialogVisible = true">《{{siteInfo.tradeAgreeTitle}}》</a>
									</el-row> -->
								</div>
							</el-form>
							<div>
								<el-button :loading="loadingBtn" class="buy-button ru" type="primary" @click="onFundsSubmit('ruleForm1')" style="margin-top:14px">入倉</el-button>
							</div>
						</div>
					</el-tab-pane>
					<!-- 分仓交易结束 -->
				</el-tabs>
				<div style="position: relative; width: 47.5%;margin-right: 10px;">
					<buy2 ref="buy2" :activeName="activeName"></buy2>
					<div class="fage">

					</div>
				</div>

			</div>

		</div>


		<div class="bottom-new-box" v-if="false">
			<div class="scrollBoxCont" ref="marqueeBox">
				<div style="display: flex;" ref="marquee" v-for="(item,index) in transactionNoticeList" :key="index">
					<div class="bottom-left" style="width: 40%;">
						<div @click="selectDetails(item,index)" style="cursor: pointer; font-size: 12px;display: flex; justify-content: space-between;padding:6px 0;">
							<div class="news-list" style="display: flex;">
								<span style="width: 100px;" v-if="index < 1">【announcement】</span>
								<span>•</span>
								<span>•</span>
								<p>{{item.artTitle}}</p>
							</div>
							<div class="showTime">{{item.addTime}}</div>
						</div>
					</div>
					<div class="bottom-right" style="width: 40%;margin-left: 95px;">
						<div @click="selectDetails(item,index)" style="cursor: pointer;font-size: 12px;display: flex; justify-content: space-between;padding:6px 0;">
							<div class="news-list" style="display: flex;">
								<span>•</span>
								<span>•</span>
								<p>{{item.artTitle}}</p>
							</div>
							<div class="showTime">{{item.addTime}}</div>
						</div>
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
	import comNone from '@/pages/transaction/index'
	import buy2 from "./buy2.vue";

	export default {
		components: {
			BuyFutures,
			buy2,
			comNone
		},
		props: {
			detailsCont: {

			},
			cutIndex: {

			},
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
				siteInfo:{
					
				},
				activeName1:'first',
				tabPosition: "left",
				tradeDialogVisible: false, //
				loading: false,
				detail: "", // 当前股票的详情
				activeName: "first",
				tradeAgreeText: "",
				list: [{
						name: "sell five",
						price: "",
						volumes: "",
					},
					{
						name: "sell four",
						price: "",
						volumes: "",
					},
					{
						name: "sell three",
						price: "",
						volumes: "",
					},
					{
						name: "sell two",
						price: "",
						volumes: "",
					},
					{
						name: "sell one",
						price: "",
						volumes: "",
					},
				],
				listbuy: [{
						name: "buy one",
						price: "",
						volumes: "",
					},
					{
						name: "buy two",
						price: "",
						volumes: "",
					},
					{
						name: "buy three",
						price: "",
						volumes: "",
					},
					{
						name: "buy four",
						price: "",
						volumes: "",
					},
					{
						name: "buy five",
						price: "",
						volumes: "",
					},
				],
				// buyNumList: [{
				// 	label: "50張",
				// 	value: 50,
				// }, ],
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
						message: "請輸入或選擇買入手數",
						trigger: "blur",
					}, ],
					buyType: [{
						required: true,
						message: "Please select the buying and selling direction",
						trigger: "blur",
					}, ],
				},
				ruleFunds: {
					subaccountNumber: [{
						required: true,
						message: "請選擇sub account",
						trigger: "blur",
					}, ],
					buyNum: [{
						required: true,
						message: "請輸入或選擇買入手數",
						trigger: "blur",
					}, ],
					buyType: [{
						required: true,
						message: "Please select the buying and selling direction",
						trigger: "blur",
					}, ],
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
					let payfee = (this.detail.nowPrice * this.form.buyNum * 1000).toFixed(2);
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
							(this.detail.nowPrice * this.form.buyNum * 1000) / this.form.lever; //  this.form.lever
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
				// 需支付总价 = 现价 * 股（1張 = 1000股）/ 杠杆倍数
			},
			USDTotal() {
				// 价钱 * 汇率 转为 人民币
				return (this.total * this.exchangeNumber).toFixed(2);
			},
			price() {
				if (this.form.buyNum) {
					return (this.detail.nowPrice * this.form.buyNum * 1000).toFixed(2);
				} else {
					return 0;
				}
				// 市值价 = 现价 * 股（1張 = 1000股）
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
			async getNewsList(query) {
				// 获取资讯列表接口（交易大厅左侧小列表）

				var data = await api.getNewsList(query);
				if (data.status == 0) {
					// this.newsList = data.data.list;
					return data.data.list;
				}
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
				this.$refs.buy2.activeName = type;
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
				if (this.$route.query.code.indexOf("hf_") != -1) {
					// 期货
					this.isqihuo = true;
					this.isgupiao = false;
				} else if (
					this.$route.query.code.indexOf("sh") != -1 ||
					this.$route.query.code.indexOf("sz") != -1
				) {
					// 指数
					this.isqihuo = false;
					this.isgupiao = false;
				} else {
					// 股票
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
				
				let code = data.code;
				this.ucode = code;
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
					// 			label: val / 100 + "張",
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
				// 先判断是否登录
				this.$refs[formName].validate(async (valid) => {
					if (valid) {
						if (!this.$store.state.haslogin) {
							this.$store.state.loginIsShow = true;
							this.$message.error("請先登錄");
							return;
						}

						if (!this.agree) {
							this.$message.error("閱讀並同意註冊協議才能下單");
							return;
						}
						var regisKong = /^\s*$/g;
						if (regisKong.test(this.form.buyNum)) {
							this.$message.error("請輸入或選擇買入張數");
							return
						}
						if (regisKong.test(this.form.lever)) {

							this.$message.error("請輸入或選擇槓桿");
							return
						}
						if (regisKong.test(this.form.buyType)) {

							this.$message.error("Please select the buying and selling direction");
							return
						}

						this.loadingBtn = true;
						if (this.$route.query.code.indexOf("hf_") != -1) {
							//期貨買入
							var qCode = this.$route.query
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
								
								
								api.findUserFuturesPositionByCode({
									futuresGid: qCode.code
								}).then(data=>{
									if (data.status == 0) {
										this.$store.commit('setUserPositionData', data.data.list[0])
									}
								})
								
								
								
							} else {
								this.$message.error(data.msg);
							}
						} else if (
							this.$route.query.code.indexOf("sh") != -1 ||
							this.$route.query.code.indexOf("sz") != -1
						) {
							//指数买入
							var zCode = this.$router.query
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
								
								
								
								api.findUserIndexPositionByCode({
									indexGid: zCode.code
								}).then(data=>{
									console.log(data)
									if (data.status == 0) {
										this.$store.commit('setUserPositionData', data.data.list[0])
									}
								})
								
								
								
							} else {
								this.$message.error(data.msg);
							}
						} else {
							// 股票買入
							
							var gCode = this.$route.query
							let twRes=await api.getTwStockData(gCode.code)
							let nowPrice=twRes.data[0]['當盤成交價']
							let hcrate=twRes.data[0]['Quote change']
							let preClose=twRes.data[0]['開盤價']
							let opts = {
								stockId: parseInt(gCode.code),
								buyNum: this.form.buyNum ? this.form.buyNum * 1000 : 0,
								buyType: this.form.buyType === "Bullish" ? 0 : 1,
								lever: this.form.lever,
								nowPrice,
								hcrate,
								preClose
							};
							// let data = await api.buy(opts);
							let data = await api.buyTwStock(opts);
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
						var regisKong = /^\s*$/g;
						if(regisKong.test(this.ruleFunds.softinputNavBar)){
							this.$message.error("請選擇sub account");
							return
						}
						if (regisKong.test(this.form.buyNum)) {
							this.$message.error("請輸入或選擇買入張數");
							return
						}
						if (regisKong.test(this.form.lever)) {

							this.$message.error("請輸入或選擇槓桿");
							return
						}
						if (regisKong.test(this.form.buyType)) {

							this.$message.error("Please select the buying and selling direction");
							return
						}
						this.loadingBtn = true;
						var grCode = this.$route.query
						// 股票買入
						let opts = {
							stockId: this.detail.id,
							buyNum: this.form.buyNum ? this.form.buyNum * 1000 : 0,
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
							
							
							api.findUserFundsPositionByCode({
								fundsCode:code
							}).then(res=>{
								if (res.status == 0) {
									this.$store.commit('setUserPositionData', data.data.list[0])
								}
							})
							
							
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
	/deep/ .el-checkbox__inner {
		width: 11px;
		height: 11px;
	}
	/deep/ .el-input.is-disabled .el-input__inner{
		background-color:#000;
	}
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
