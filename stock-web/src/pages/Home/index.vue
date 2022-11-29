<template>
	<el-container class="honmeIndex">
		<el-header>
			<home-header></home-header>
		</el-header>
		<el-container class="main-wrapper">
			<el-main style="padding-top: 60px;padding: 0;">
				<div class="bgd" style='width:100%'>
					<backdrop>
						<div class="bgdcontent">
								<!-- <div class="text">
								<div class="en">VICTORY GROUP</div>
								<div class="company">{{siteInfo.siteName}}</div>
								<div class="brief">{{siteInfo.siteIntro}}</div>
							</div> -->
						
					
							 <!-- poster="/static/img/bg-image-sailing.jpg" -->
							<!-- <div class="cot" style="position: relative;">
								<div class="notice" style="display:flex;width:100%;box-sizing:border-box;">
									<i class="iconfont icon-gonggao" style="margin-right:50px;width:8%;margin-right: 5px;overflow: hidden;">公告</i>
									<div class="bpx" style="width:80%;overflow:hidden">
										<div ref="marqueeBox" class="marquee-box" style='display:flex;white-space: nowrap;'>
											<div ref="marquee" class="marquee-list" style="margin-right:60px;" v-for="(item,index) in noticeList" :key="index">
												<router-link style="color:#fff" :to="{path:'/enterprise-details/',query:{id:item.id}}">
													{{item.title}}
												</router-link>
											</div>
										</div>
									</div>
								</div>
								<div class="numtop">
									<div class="numtoplis" v-for="(item,index) in numtoplis" :key="index" v-if="index<4">
										<div class="tit">{{item.name}}</div>
										<div class="num">{{item.nowPrice}}<span class="iconfont" :class="item.increaseRate>0?'icon-direction-top icon-direction-top_red':'el-icon-bottom icon-direction-bottom_green'"></span></div>
										<div class="percentum"><span style="">{{item.increase}}</span><span>{{item.increaseRate}}%</span></div>
									</div>
								</div>
								<div class="lobby" @click="toJiaoyi">
									<div class="lobbtn"><i class="iconfont icon-gupiao"></i><i>進入交易大廳</i></div>
								</div>
							</div> -->
						</div>
					</backdrop>
				</div>
			</el-main>
		</el-container>
		<backVideo></backVideo>
		<div class="contentIndex">
			<!-- <div class="lobby">
        <div class="lobbtn" @click="toJiaoyi"><i class="iconfont icon-gupiao"></i><i>進入交易大廳</i></div>
      </div> -->
			<div class="aboutus" style="pa">
				<layout>
					<div slot='left'>
						<div class="hongtiao"></div>
						<div class="tit">
							 <span>ABOUT US</span>
						</div>
						<div class="text">
							{{siteInfo.companyInfo}}
						</div>
						<div class="more1" @click="toLj">Understand more <span class="iconfont icon-direction-right"></span> </div>
					</div>
					<div slot='right'>
						<div class="img"><img src="../../../static/newimg/gongsi.png" alt=""></div>
					</div>
				</layout>
			</div>
			<div class="banner">
				<div class="cot">
					<div class="empt"></div>
					<div class="detail">
						<div class="hongtiao"></div>
						<div class="tit">Easy to get what you want, able to move the world</div>
						<div class="text">Adhering to the core development concept of "Easy to get where you want to be, to be able to move the world", with the tenet of "Fairness, Justice, Stability and Safety", with the service spirit of "Integrity, Professionalism, Value Creation and Innovation", service-oriented, courageous in innovation, global cross
Provide professional and comprehensive services to foreign investors. Open a new era of global investment!</div>
					</div>
				</div>
			</div>
			<div class="productcot">
				<div class="product">
					<div class="tit">Product Description</div>
					<div class="entit">PRODUCT MANUAL</div>
					<div class="text">Our greatest asset is our people, and Victory Group employs only those experienced professionals who have demonstrated their top-notch customer service skills and tie-in skills in the financial services industry. Provide professional customer service. Our sales and operations representatives will answer any inquiries from customers in professional Chinese.</div>
				</div>
				<div class="notice">
					<layout>
						<div slot="left">
							<div class="s_left">
								<div class="not_l" style="width:100%;overflow: hidden;">
									<span class="iconfont icon-kaihuhedui">
									</span>
									Account opening instructions
									</div>
								<div class="not_r">
									<ul>
										<li>Before opening a real account (registered member), you must carefully read and review the company's "Trading Rules" and "Risk Disclosure and Notification".</li>
										<li>Account opening requirements for customers: Citizens over the age of 18 with full capacity for civil conduct. Customers must open an account in a real and legal identity and ensure the legality of the source of funds.</li>
										<li>Customers should prepare their own financial accounts for mutual transfer (open online finance), and customers should keep their financial passwords and transaction passwords properly, and change the passwords regularly, and must not disclose the passwords to anyone.</li>
										<li>Register yourself and follow the "One Person One Account Rule".</li>
										<li>After the official account is successfully recharged, you can start trading.</li>
									</ul>
								</div>
							</div>
						</div>
						<div slot="right">
							<div class="s_right">
								<div class="not_l" style="width:100%;overflow: hidden;"><span class="iconfont icon-kaihuhedui"></span>
									<!--  -->
									Operating Instructions
								</div>
								<div class="not_r">
									<ul>
										<li>The principal you invest is the minimum1 USD;</li>
										<li>After applying for funds, buy and sell at any time;</li>
										<li>Once the accounts are matched, the same day can be traded;</li>
										<li> Liquidation when funds are close to a loss Warning line, need to be replenished as soon as possibleMargin; If the position is not covered in time, it may be liquidated by the risk control system;</li>
										<li>When the trading capital is lower than the Closing Line, we have the right to implement a stop loss operation on your account;</li>
										<li>A single stock position shall not exceed the total funds in the account90.00% ;</li>
									</ul>
								</div>
							</div>
						</div>
					</layout>
				</div>
				<div class="knowmore" @click="toCp">
					<div class="text">Understand more <span class="iconfont icon-direction-right"></span> </div>
				</div>
			</div>
			<div class="news">
				<!-- <div class="tit">新聞資訊</div> -->
				<div class="en">NEWS CENTER</div>
				<div class="text">
					<div class="block">
						<el-carousel :autoplay='true' height="400px">
							<el-carousel-item v-for="(item,index) in ArtList" :key="index" v-if="index<4">
								<layout>
									<div slot="left">
										<div class="top" style="overflow: hidden;">{{item.title}}</div>
										<div class="txt" style="height:160px;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 6;overflow: hidden;"
										 v-html="item.content"></div>
										<div class="baitiao"></div>
										<div class="date">{{formatDate(item.addTime)}}</div>
									</div>
									<div slot="right">
										<div class="top">{{ArtList[index+1].title}}</div>
										<div class="txt" style="height:160px;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 6;overflow: hidden;"
										 v-html="ArtList[index+1].content"></div>
										<div class="baitiao"></div>
										<div class="date">{{formatDate(ArtList[index+1].addTime)}}</div>
									</div>
								</layout>
							</el-carousel-item>
						</el-carousel>
					</div>
				</div>
				<!-- <div class="more1" style="margin:0 auto" @click="toNews">瞭解更多<span class="iconfont icon-direction-right"></span></div> -->
			</div>
		</div>
		<newFooter />
	</el-container>
</template>

<script>
	import HomeHeader from '../../components/HeaderOrder'
	import HomeFooter from '../../components/Footer'
	import TableBox from './components/table'
	import * as api from '../../axios/api'
	import backdrop from '../../components/backdrop'
	import layout from '../../components/layout'
	import newFooter from '../../components/newFooter'
	import backVideo from "./components/backVideo.vue"

	const dayjs = require('dayjs')
	export default {
		components: {
			HomeHeader,
			TableBox,
			HomeFooter,
			backdrop,
			layout,
			newFooter,
			backVideo
		},
		props: {},
		data() {
			return {
				ArtList: [],
				noticeList: [],
				pageNum: 1,
				transactionNoticeList: [],
				bannerList: [],
				market: {}, // 大盘详情
				siteInfo: {}, // 站点信息
				timer: null,
				currentIndex: 0,
				// 数字指数
				numtoplis: [{
					num: 1
				}, {
					num: 1
				}, {
					num: 1
				}, {
					num: 1
				}],
				newlist: [{
					num: 1
				}, {
					num: 1
				}, {
					num: 1
				}, {
					num: 1
				}]
			}
		},
		watch: {},
		computed: {},
		created() {
			this.timer = setInterval(this.refreshList, 5000)
			this.$store.state.activeIndex = 'home'
			// this.$router.push({
			//     path: '/transaction?code=300750'
			// })
			//this.toTransform()
		},
		beforeDestroy() {
			clearInterval(this.timer)
		},
		mounted() {
			setTimeout(() => {
				this.runMarquee()
			}, 1000)
			this.getArtList()
			this.getMarket()
			this.getInfoSite()
			this.getNoticeList()
			this.getNewsList()

		},
		methods: {
			// 获取公告
			scrollx() {
				console.log('mounted')
				// 延时滚动

			},
			async getNewsList() {
				let opts = {
					pageNum: this.pageNum,
					pageSize: 10,
					type: 1
				};
				let data = await api.getNewsList(opts);
				if (data.status == 0) {
					this.noticeList = data.data.list;
					console.log(this.noticeList, 122)
				}
			},
			// 获取新闻
			async getArtList() {
				let opts = {
					pageNum: 1,
					pageSize: 10,
				};
				let data = await api.queryNewsList(opts);
				this.ArtList = data.data.list;
				console.log(this.ArtList, 123)
				
			},
			// 通告滚动
			runMarquee() {
				// 获取文字 计算后宽度
				var width = this.$refs.marquee[0].getBoundingClientRect().width;
				var marquee = this.$refs.marqueeBox;
				console.log(marquee)
				var disx = 0; // 位移距离
				// console.log(width) 
				var allwidth = 0
				for (var i = 0; i < this.noticeList.length; i++) {
					allwidth += this.$refs.marquee[i].getBoundingClientRect().width
				}
				this.temi = setInterval(() => {
					disx--; // disx-=1; 滚动步长
					if (-disx >= allwidth) {
						disx = 0; // 如果位移超过文字宽度，则回到起点  marquee-list的margin值
					}
					// console.log(disx,'disx')
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
			async getNoticeList() {
				// 获取交易大厅-中间部分-通知公告
				let data = await api.getTransactionNoticeList({
					pageSize: 10,
				});
				if (data.status == 0) {
					this.switchData(data.data.list, "addTime");
					this.transactionNoticeList = data.data.list;
					console.log(this.transactionNoticeList, 32)
				}
			},
			toNews() {
				this.$router.push({
					path: '/enterprise'
				})
			},
			toCp() {
				this.$router.push({
					path: '/product'
				})
			},
			toLj() {

				this.$router.push({
					path: '/introduce'
				})
			},
			toJiaoyi() {
				this.$router.push({
					path: '/transaction?code=6414'
				})
			},
			refreshList() {
				// 刷新 大盘指数
				this.getMarket()
			},
			async getMsg() {
				// 获取显示的资讯
				// let result = await api.getMessageList({ pageNum:1,pageSize:15,type: 1 })
				// console.log(result)
				// if (result.status === 0) {
				//   // this.newlist = result.data
				// } else {
				//   this.$message.error(result.msg)
				// }
			},
			async getMarket() {
				// 获取大盘指数
				
				let result = await api.getMarket()
				if (result.status === 0) {
					this.numtoplis = result.data.market
				} else {
					this.$message.error(result.msg)
				}
			},
			async getInfoSite() {
				// 获取大盘指数
				let result = await api.getInfoSite()
				if (result.status === 0) {
					this.siteInfo = result.data
				} else {
					this.$message.error(result.msg)
				}
			},
			async cardChange(index) {
				this.currentIndex = index
			},
			async toTransform() {
				let opt = {
					pageNum: 1,
					pageSize: 1,
					stockPlate:'上市'
				}
				let data = await api.getTwStockPageList(opt);
				if (data.status === 0) {
					this.$router.push({
						path: "/transaction",
						query: {
							code: data.data.list[0].code,
						},
					});
				} else {
					this.$message.error(data.msg)
				}
			},
			formatDate(date){
				return dayjs(date).format("MMM D, YYYY h:mm A")
			}
		}
	}
</script>
<style lang="less" scoped>
	.main-wrapper {
		padding-top: 0;
	}

	/deep/ .el-main {
		padding: 60px 0px 0px 0px;
	}

	.carousel-box {
		height: 500px;
		text-align: center;

		/deep/ .el-carousel__container {
			height: 100%;
		}

		.img-box {
			height: 500px;
			// background: url(https://exproductdiag891.blob.core.windows.net/banner/0bb1ef6….jpg) ;
			background-position: center center;
			width: 100%;
			background-repeat: no-repeat;
			background-size: cover;
		}

		img {
			max-width: 100%;
			max-height: 100%;
		}

		.top-box {
			position: absolute;
			top: 34%;
			width: 100%;

			.prompt {
				color: #b6b6b6;
				font-size: 18px;
			}
		}

		p {
			color: #efbb53;
			font-size: 34px;
			margin-bottom: 40px;
		}
	}

	.table-box {
		width: 1200px;
		margin: 0 auto;
	}

	.down-box {
		padding-top: 65px;
		text-align: center;
		padding-bottom: 15px;

		.box {
			margin-bottom: 10px;

			.title {
				font-size: 30px;
				height: 34px;
				line-height: 34px;
			}

			.sub-title {
				font-size: 14px;
				color: #5a657e;
				height: 20px;
				line-height: 20px;
				padding: 10px 0;
			}
		}

		.img-box {
			width: 500px;
			margin: 0 auto;

			p {
				text-align: center;
				padding: 0px 0 10px 0px;
				color: #5a657e;
			}

			img {
				width: 150px;
				height: 150px;
				margin: 0 20px;
			}
		}
	}
</style>
