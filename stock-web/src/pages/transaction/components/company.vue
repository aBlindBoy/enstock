<template>
	<!-- style="box-sizing: border-box;padding: 0 0 0 30px;" -->
	<div class="wrapper buy-table table-chart2-cont" >
		<div class="box-buy" style="margin-bottom: 2px;height: calc(100vh - 110px);padding: 10px">

			<div style="display:flex;align-items: center; justify-content: center; margin-bottom: 20px;padding-top: 20px;">
				<span class="buy-xian">——</span>
				<span style="margin: 0 10px;" class="buy-jiaoyi">{{$t('tradingFloor.companyProfile')}}</span>
				<span class="buy-xian">——</span>
			</div>
			<div style="line-height:30px;display:flex;">
				<div style="width: 120px;">{{$t('tradingFloor.symbol')}} </div>
				<div style="">{{ stockInfo.stockCode }}</div>
			</div>


			<div style="line-height:30px;display:flex;">
				<div style="width: 120px;">{{$t('tradingFloor.market')}} </div>
				<div style="">{{ stockInfo.stockPlate }}</div>
			</div>
			
			<div style="line-height:40px;">
				<div style="width: 100%;">{{$t('tradingFloor.companyName')}}</div>
				<div style="line-height:20px;">{{ stockInfo.companyName }}</div>
			</div>

		

			<div style="line-height:40px;">
				<div style="width: 100%;">{{$t('tradingFloor.companyOverview')}}</div>
				<div style="line-height:20px;">{{ stockInfo.profile }}</div>
			</div>



		</div>
	</div>
</template>

<script>
import * as api from "../../../axios/api";

export default {
	components: {
	},
	props: {
	},
	data() {
		return {
			windowHeight: document.documentElement.clientHeight - 475, //实时屏幕高度
			dealHistoryList: [],
			loading: false,
			stockInfo: {},
		};
	},
	watch: {
		change(newVal, oldVal) {
			if (newVal !== oldVal) {
				this.getDetail();
			}
		},
	
	},
	computed: {

	},
	created() {
	},
	beforeDestroy() {
		clearInterval(this.timer);
	},
	mounted() {
		this.getDetail();
	},
	methods: {
		async getDetail() {
			let opts = {
				code: this.$route.query.code
			}
			let data = await api.getSingleStock(opts)
			if (data.status === 0) {
				this.stockInfo = data.data
			} else {
				this.$message.error(data.msg)
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
	.listhi-cont {
		padding: 11px 0px 0px !important;
	}

	.box-buy-list {
		background-color: rgb(242, 242, 242);
		margin-bottom: 1px;
	}

	.box-buy {
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
	.box-buy {
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
