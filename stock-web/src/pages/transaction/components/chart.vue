<!-- <template>
	<div class="wrapper" style="width: 100%;">
		<div class="panel-box">
			<div class="panel-title clearfix">
				<div class="detail-box pull-left">
					<a title="Add optional" v-if="!isOptionOpt" href="javascript:;" @click="addOptions"><i class="iconfont icon-wujiaoxing2"></i></a>
					<a title="delete optional" v-if="isOptionOpt" href="javascript:;" @click="deteleOptions"><i class="iconfont icon-wujiaoxing"></i></a>
					<span class="name">{{detail.name}}</span>
					<span class="code">{{detail.code}}</span>
				</div>
				<ul class="chart-title pull-right">

					<li @click="changeType(0)" :class="type=='0'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">1 minute</a>
					</li>
					<li @click="changeType(5)" :class="type=='5'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">5 minute</a>
					</li>
					<li @click="changeType(15)" :class="type=='15'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">15 minutes</a>
					</li>
					<li @click="changeType(30)" :class="type=='30'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">30 minutes</a>
					</li>
					<li @click="changeType(60)" :class="type=='60'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">60 minutes</a>
					</li>
					<li @click="changeType(1)" :class="type=='1'?'on li-type':'li-type'">
						<a class="chart-type" style="font-size: 12px;" href="javascript:;">day line</a>
					</li>
				</ul>
			</div>
			<div class="panel-body">
				<div class="chart-box" v-show="type === 0" ref="myEchart" style="width:100%;position: relative;" :style="'height:'+windowHeight+'px;'">
					<div v-if="emptyShow" class="empty text-center">
						<i class="iconfont icon-wushuju"></i>
						No data
					</div>
				</div>
				<div class="chart-box" :style="'height:'+windowHeight+'px;'" v-show="type != 0" ref="myEchart2"></div>
			</div>
		</div>
		<div class="table">
			<div class="panel">
			</div>
		</div>
	</div>
</template>

<script>
	import * as api from '../../../axios/api'
	import echarts from 'echarts'
	import {
		Toast
	} from 'mint-ui'

	export default {
		components: {},
		props: {
			code: {
				type: String,
				default () {

				}
			},
			detail: {
				type: Object,
				default () {

				}
			}
		},
		data() {
			return {
				windowHeight: document.documentElement.clientHeight - 445, //实时屏幕高度
				emptyShow: true,
				ChartsTime: [], // 分时图时间
				ChartstFuturesTime: [], // 分时图时间
				timer: null,
				isOptionOpt: false, // 是否自选
				type: '0', // 类型
				styleName: 'red',
				data: // 分时数据
				{
					stockCode: '',
					amounts: [],
					price: [],
					rates: [],
					time: [],
					volumes: []
				},
				data2: { // 5分钟及以上的数据
					date: [],
					stockCode: '',
					stockName: '',
					values: [],
					volumes: []
				},
				k_top_closing: 99999999999
			}
		},
		watch: {
			change(newVal, oldVal) {
				if (newVal !== oldVal) {
					if (this.type === 0) {
						this.getDate() // 分时数据
					} else {
						this.getDate2(this.type)
					}
					if (this.$store.state.haslogin) {
						this.getOpation()
					}
				}
			}
		},
		computed: {
			change() {
				return this.$route.query.code
			}
		},
		created() {
			this.changeType(0)
			this.timer = setInterval(this.getDate, 60000)
		},
		beforeDestroy() {
			// console.log('准备清除定时任务')
			clearInterval(this.timer)
		},
		mounted() {
			setInterval(()=>{
					this.styleName = localStorage.getItem('styleName')
					
					this.windowHeight = document.documentElement.clientHeight - 445; //实时屏幕高度
			},500)
			
			this.getDate() // 分时数据

			if (this.$store.state.haslogin) {
				this.getOpation()
			}
		},
		methods: {
			changeType(val) {
				if (val !== 0) {
					clearInterval(this.timer)
					this.getDate2(val)
				} else {
					this.getDate()
					this.timer = setInterval(this.getDate, 60000)
				}
				this.type = val
			},
			handleCommand(val) {
				// 选择分钟
				this.type = 2
				clearInterval(this.timer)
				this.getDate2(val)
			},
			async getDate2(val) {
				if (val === 1) {
					if (this.$route.query.code.indexOf('hf_') != -1) {
						//期货日线图
						let data = await api.getFuturesDayK({
							code: this.$route.query.code
						})
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					} else if (this.$route.query.code.indexOf('sh') != -1 || this.$route.query.code.indexOf('sz') != -1) {
						//指数日线图
						let data = await api.getIndexDayK({
							code: this.$route.query.code
						})
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					} else {
						//股票日线图
						let data = await api.getDayK({
							code: this.$route.query.code
						})
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					}

				} else {
					//期货分时图
					if (this.$route.query.code.indexOf('hf_') != -1) {
						let data = await api.getFuturesMinKEcharts({
							code: this.$route.query.code,
							time: val,
							size: 50
						})
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					} else if (this.$route.query.code.indexOf('sh') != -1 || this.$route.query.code.indexOf('sz') != -1) {
						let data = await api.getIndexMinKEcharts({
							code: this.$route.query.code,
							time: val,
							size: 50
						})
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					} else {
						// 获取分时段数据
						let opts = {
							code: this.$route.query.code,
							time: val,
							ma: 5,
							size: 50
						}
						let data = await api.getMinKEcharts(opts)
						if (data.status === 0) {
							this.initEchartMap2(data.data)
						} else {
							Toast(data.msg)
						}
					}

				}
				this.getChrtsTime()
			},
			getChrtsTime() {
				this.ChartsTime = []
				// 获取时间
				if (this.$route.query.code.indexOf('hf_HSI') != -1) {
					// 9:15~12:00
					for (var i = 9; i < 12; i++) {
						let item = ''
						for (let j = 0; j < 60; j++) {
							if (i === 9 && j < 15) {

							} else {
								let min = i
								let sec = j
								if (sec < 10) {
									sec = '0' + j
								}
								if (min < 10) {
									min = '0' + i
								}
								item = min + ':' + sec
								this.ChartsTime.push(item)
							}
						}
					}
					// 13:00~16:30
					for (var i = 13; i < 17; i++) {
						let item = ''
						for (let j = 0; j < 60; j++) {
							if (i === 16 && j > 30) {

							} else {
								let min = i
								let sec = j
								if (sec < 10) {
									sec = '0' + j
								}
								if (min < 10) {
									min = '0' + i
								}
								item = min + ':' + sec
								this.ChartsTime.push(item)
							}
						}
					}
					// 17:15~23:45
					for (var i = 17; i <= 23; i++) {
						let item = ''
						for (let j = 0; j < 60; j++) {
							if (i === 17 && j < 15) {

							} else if (i === 23 && j > 45) {

							} else {
								let min = i
								let sec = j
								if (sec < 10) {
									sec = '0' + j
								}
								if (min < 10) {
									min = '0' + i
								}
								item = min + ':' + sec
								this.ChartsTime.push(item)
							}
						}
					}
				} else if (this.$route.query.code.indexOf('hf_') != -1) {
					// 0:01 ~ 23:59
					for (var i = 0; i <= 23; i++) {
						let item = ''
						for (let j = 0; j < 60; j++) {
							let min = i
							let sec = j
							if (sec < 10) {
								sec = '0' + j
							}
							if (min < 10) {
								min = '0' + i
							}
							item = min + ':' + sec
							this.ChartsTime.push(item)
						}
					}
				} else {
					// 9:30 ~ 11:30  13:00~15:00
					for (var i = 9; i < 15; i++) {
						let item = ''
						if (i === 12) {

						} else {
							for (let j = 0; j < 60; j++) {
								if (i === 11 && j > 30) {

								} else if (i === 9 && j < 30) {

								} else {
									let min = i
									let sec = j
									if (sec < 10) {
										sec = '0' + j
									}
									if (min < 10) {
										min = '0' + i
									}
									item = min + ':' + sec
									this.ChartsTime.push(item)
								}
							}
						}
					}
				}

			},
			valuesVolumes(data) {
				// 处理成交量 现在返回的成交量是每一次分钟返回的总量
				// 用下一个数据减去上一个数据得到这分钟的成交量
				let newData = []
				data.reduce(function(previousValue, currentValue, index, array) {
					newData.push([index, currentValue - previousValue, 1])
					return currentValue
				})
				return newData
			},
			async getDate() {
				// 获取分时数据
				let opts = {
					stockCode: this.$route.query.code,
					state: 0,
					stockSpell: '',
					pageNum: 1,
					pageSize: 10
				}
				if (opts.stockCode === '' || opts.stockCode === null) {
					return
				}
				let data = await api.getMinuteLine(opts)
				if (data.status === 0) {
					this.emptyShow = false
					this.getChrtsTime() // 获取分时时间
					this.initEchartMap(data.data)
				} else {
					this.emptyShow = true
					this.$message.success(data.msg)
				}
			},
			// 分时图
			initEchartMap(data) {
				let volumesVal = [0]
				if (data.volumes !== undefined && data.volumes.length > 0) {
					volumesVal = this.valuesVolumes(data.volumes)
				}
				if (data.price !== undefined && data.price.length <= 0) {
					data.price = [0]
				}
				// let upColor = '#00da3c';
				// let downColor = '#ec0000';
				let this_ = this
				let upColor = '#ec0000'
				let downColor = '#00da3c'
				// var data = this.splitData(rawData);
				let myChart = echarts.init(this.$refs.myEchart)
				window.onresize = myChart.resize
				let option = {
					backgroundColor: this_.styleName === 'red-bg' ? '#fff' : '#171e2a',
					animation: true,
					// legend: { // 图例组件
					//     bottom: 10, // 图例组件离容器下侧的距离。
					//     left: 'center', // 图例组件离容器左侧的距离。
					//     data: data.stockName
					// },
					tooltip: { // 提示框组件 坐标轴指示器是指示坐标轴当前刻度的工具
						trigger: 'axis', // 坐标轴触发，主要在柱状图，折线图等会使用类目轴的图
						axisPointer: {
							type: 'cross'
						},
						backgroundColor: 'rgba(245, 245, 245, 0.8)',
						borderWidth: 1,
						borderColor: '#ccc',
						padding: 10,
						textStyle: {
							color: '#000'
						},
						position: function(pos, params, el, elRect, size) {
							var obj = {
								top: 20
							}
							obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30
							return obj
						},
						formatter: function(param) {
							let param2
							if (param[0].seriesName === '成交量') {
								param2 = param[0]
								param = param[1]
							} else {
								param2 = param[1]
								param = param[0]
							}
							let classFont = data.rates[param.dataIndex] < 0 ? 'green' : 'red'
							// data.rates[] 涨跌幅度 amounts 成交额
							if (param && param2) {
								return [
									'<span style="font-size:16px;font-weight:bold;margin-right:10px;">' + param.seriesName + '</span>' + param.name +
									'<hr size=1 style="margin: 3px 0">',
									// '時間：' + param.name +'</br>',
									'Current price: ' + '<span class="' + classFont + '" style="font-size:16px;">' + param.value + '</span>' + '<br/>',
									'Quote change: ' + '<span class="' + classFont + '" style="font-size:16px;">' + data.rates[param.dataIndex] +
									'%</span>' + '<br/>',
									'成交量(手): ' + param2.value[0] / 100 + '<br/>',
									'成交額(萬): ' + (data.amounts[param.dataIndex] / 10000).toFixed(2) + '<br/>'
								].join('')
							}
						}
						// extraCssText: 'width: 170px'
					},
					axisPointer: { // 坐标轴指示器
						link: {
							xAxisIndex: 'all'
						},
						label: {
							backgroundColor: '#ccc' // hover  number bg
						}
					},
					toolbox: { // 工具栏
						feature: { // 各工具配置项。//除了各个内置的工具按钮外，还可以自定义工具按钮。
							dataZoom: { // 数据区域缩放
								yAxisIndex: false
							},
							brush: { // 选框组件的控制按钮
								type: ['clear']
							}
						}
					},
					brush: { // brush 是区域选择组
						xAxisIndex: 'all',
						brushLink: 'all',
						outOfBrush: {
							colorAlpha: 0.1
						}
					},
					visualMap: { // visualMap 是视觉映射组件，
						show: false,
						seriesIndex: 5,
						dimension: 2,
						pieces: [{
							value: 1,
							color: upColor
						}, {
							value: -1,
							color: downColor
						}]
					},
					grid: [ // 直角坐标系内绘图网格  设置表格的位置
						{
							left: '6%',
							right: '4%',
							height: '65%',
							top: '10%'
						},
						{
							left: '6%',
							right: '4%',
							top: '78%',
							height: '20%'
						}
					],
					xAxis: [{
							type: 'category',
							data: this_.ChartsTime,
							scale: true,
							boundaryGap: false,
							axisLine: {
								onZero: false
							},
							splitLine: {
								show: false
							},
							splitNumber: 20,
							min: 'dataMin',
							max: 'dataMax',
							axisPointer: {
								z: 100
							},
							axisLabel: {
								show: true,
								textStyle: {
									color: this_.styleName === 'red-bg' ? '#666' : '#ccc'
								}
							}
						},
						{
							type: 'category',
							gridIndex: 1,
							data: this_.ChartsTime,
							scale: true,
							boundaryGap: false,
							axisLine: {
								onZero: false
							},
							axisTick: {
								show: false
							},
							splitLine: {
								show: false
							},
							axisLabel: {
								show: false
							},
							splitNumber: 20,
							min: 'dataMin',
							max: 'dataMax'
							// axisPointer: {
							//     label: {
							//         formatter: function (params) {
							//             var seriesValue = (params.seriesData[0] || {}).value;
							//             return params.value
							//             + (seriesValue != null
							//                 ? '\n' + echarts.format.addCommas(seriesValue)
							//                 : ''
							//             );
							//         }
							//     }
							// }
						}
					],
					yAxis: [{
							scale: true,
							splitArea: { // 设置y轴分割块颜色
								show: false,
								areaStyle: {
									color: ['rgba(33,37,42,1)', 'rgba(33,37,42,1)'],
									opacity: 0
								}
							},
							axisLabel: {
								show: true,
								textStyle: {
									color: this_.styleName === 'red-bg' ? '#666' : '#ccc'
								}
							},
							splitLine: {
								lineStyle: {
									// 使用深浅的间隔色
									color: this_.styleName === 'red-bg' ? '#eee' : '#1e242f' // 分割线
								}
							}
						},
						{
							scale: true,
							gridIndex: 1,
							splitNumber: 2,
							axisLabel: {
								show: false
							},
							axisLine: {
								show: false
							},
							axisTick: {
								show: false
							},
							splitLine: {
								show: false
							}
						}
					],
					dataZoom: [{
							type: 'inside',
							xAxisIndex: [0, 1],
							start: 0,
							end: 100
						},
						{
							show: false,
							xAxisIndex: [0, 1],
							type: 'slider',
							top: '65%',
							start: 0,
							end: 100
						}
					],
					series: [{
							name: data.stockCode,
							type: 'candlestick',
							data: data.values,
							symbol: 'none',
							itemStyle: {
								normal: {
									color: upColor,
									color0: downColor,
									borderColor: null,
									borderColor0: null
								}
							},
							tooltip: {
								formatter: function(param) {
									param = param[0]
									return [
										'開盤: ' + param.data[0] + '<br/>',
										'收盤: ' + param.data[1] + '<br/>',
										'最低: ' + param.data[2] + '<br/>',
										'最高: ' + param.data[3] + '<br/>'
									].join('')
								}
							}
						},
						{
							name: data.stockCode,
							type: 'line',
							data: [],
							// smooth: true,
							symbol: 'none',
							areaStyle: {
								opacity: 0.5
							},
							lineStyle: {
								normal: {
									opacity: 0.5
								}
							}
						},
						{
							name: 'MA10',
							type: 'line',
							data: [],
							smooth: true,
							lineStyle: {
								normal: {
									opacity: 0.5
								}
							}
						},
						{
							name: data.stockCode,
							type: 'line',
							data: data.price,
							smooth: true,
							symbol: 'none', // 折线点是否显示
							lineStyle: {
								// color:'#7cb5ec',
								color: this_.styleName === 'red-bg' ? '#7cb5ec' : '#3e5281'
								// normal: {opacity: 0.5}
							},
							itemStyle: {
								color: '#3e5281' // 线条颜色
							},
							areaStyle: {
								color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
									offset: 0,
									color: this_.styleName === 'red-bg' ? 'rgba(117, 156, 247, 0.5)' : 'rgba(62, 82, 129,0.5)'
								}, {
									offset: 1,
									color: this_.styleName === 'red-bg' ? 'rgba(117, 156, 247, 0)' : 'rgba(62, 82, 129,0)'
								}])
							}
						},
						{
							name: 'MA30',
							type: 'line',
							data: [],
							smooth: true,
							lineStyle: {
								normal: {
									opacity: 0.5
								}
							}
						},
						{
							name: '成交量',
							type: 'bar',
							xAxisIndex: 1,
							yAxisIndex: 1,
							itemStyle: {
								color: '#c23531'
							},
							data: volumesVal
						}
					]
				}
				myChart.setOption(option)
			},
			// 分时端图
			initEchartMap2(data) {
				let this_ = this
				let upColor = '#00da3c'
				let downColor = '#ec0000'
				// var data = this.splitData(rawData);
				let myChart = echarts.init(this.$refs.myEchart2)
				window.onresize = myChart.resize
				let option = {
					backgroundColor: this_.styleName === 'red-bg' ? '#fff' : '#171e2a',
					animation: true,
					legend: { // 图例组件
						bottom: 10, // 图例组件离容器下侧的距离。
						left: 'center', // 图例组件离容器左侧的距离。
						data: data.stockName
					},
					tooltip: { // 提示框组件 坐标轴指示器是指示坐标轴当前刻度的工具
						trigger: 'axis', // 坐标轴触发，主要在柱状图，折线图等会使用类目轴的图
						axisPointer: {
							type: 'cross'
						},
						backgroundColor: 'rgba(245, 245, 245, 0.8)',
						borderWidth: 1,
						borderColor: '#ccc',
						padding: 10,
						textStyle: {
							color: '#000'
						},
						position: function(pos, params, el, elRect, size) {
							var obj = {
								top: 20
							}
							obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30
							return obj
						},
						formatter: function(param) {
							let param2
							if (param[0].seriesName === '成交量') {
								param2 = param[0]
								param = param[1]
							} else {
								param2 = param[1]
								param = param[0]
							}
							if (param) {
								return [
									param.seriesName + '<hr size=1 style="margin: 3px 0">',
									'開盤: ' + param.data[1] + '<br/>',
									'收盤: ' + param.data[2] + '<br/>',
									'最低: ' + param.data[3] + '<br/>',
									'最高: ' + param.data[4] + '<br/>',
									'成交量(手): ' + param2.data[1] / 100 + '<br/>',
									param.name
								].join('')
							}
						}
						// extraCssText: 'width: 170px'
					},
					axisPointer: { // 坐标轴指示器
						link: {
							xAxisIndex: 'all'
						},
						label: {
							backgroundColor: '#777' // hover  number bg
						}
					},
					toolbox: { // 工具栏
						feature: { // 各工具配置项。//除了各个内置的工具按钮外，还可以自定义工具按钮。
							dataZoom: { // 数据区域缩放
								yAxisIndex: false
							},
							brush: { // 选框组件的控制按钮
								type: ['clear']
							}
						}
					},
					brush: { // brush 是区域选择组
						xAxisIndex: 'all',
						brushLink: 'all',
						outOfBrush: {
							colorAlpha: 0.1
						}
					},
					visualMap: { // visualMap 是视觉映射组件，
						show: false,
						seriesIndex: 5,
						dimension: 2,
						pieces: [{
							value: 1,
							color: downColor
						}, {
							value: -1,
							color: upColor
						}]
					},
					grid: [ // 直角坐标系内绘图网格  设置表格的位置
						{
							left: '6%',
							right: '4%',
							height: '50%',
							top: '10%'
						},
						{
							left: '6%',
							right: '4%',
							top: '78%',
							height: '20%'
						}
					],
					xAxis: [{
							type: 'category',
							data: data.date,
							scale: true,
							boundaryGap: false,
							axisLine: {
								onZero: false
							},
							splitLine: {
								show: false
							},
							splitNumber: 20,
							min: 'dataMin',
							max: 'dataMax',
							axisPointer: {
								z: 100
							},
							axisLabel: {
								show: true,
								textStyle: {
									color: this_.styleName === 'red-bg' ? '#666' : '#ccc'

								}
							}
						},
						{
							type: 'category',
							gridIndex: 1,
							data: data.date,
							scale: true,
							boundaryGap: false,
							axisLine: {
								onZero: false
							},
							axisTick: {
								show: false
							},
							splitLine: {
								show: false
							},
							axisLabel: {
								show: false
							},
							splitNumber: 20,
							min: 'dataMin',
							max: 'dataMax'
							// axisPointer: {
							//     label: {
							//         formatter: function (params) {
							//             var seriesValue = (params.seriesData[0] || {}).value;
							//             return params.value
							//             + (seriesValue != null
							//                 ? '\n' + echarts.format.addCommas(seriesValue)
							//                 : ''
							//             );
							//         }
							//     }
							// }
						}
					],
					yAxis: [{
							scale: true,
							splitArea: { // 设置y轴分割块颜色
								show: false,
								areaStyle: {
									color: ['rgba(33,37,42,1)', 'rgba(33,37,42,1)'],
									opacity: 0
								}
							},
							axisLabel: {
								show: true,
								textStyle: {
									color: this_.styleName === 'red-bg' ? '#666' : '#ccc'
								}
							},
							splitLine: {
								lineStyle: {
									// 使用深浅的间隔色
									color: this_.styleName === 'red-bg' ? '#eee' : '#1e242f' // 分割线
								}
							}
						},
						{
							scale: true,
							gridIndex: 1,
							splitNumber: 2,
							axisLabel: {
								show: false
							},
							axisLine: {
								show: false
							},
							axisTick: {
								show: false
							},
							splitLine: {
								show: false
							}
						}
					],
					dataZoom: [{
							type: 'inside',
							xAxisIndex: [0, 1],
							start: 0,
							end: 100
						},
						{
							show: false,
							xAxisIndex: [0, 1],
							type: 'slider',
							top: '65%',
							start: 0,
							end: 100
						}
					],
					series: [{
							name: data.stockName,
							type: 'candlestick',
							data: data.values,
							itemStyle: {
								normal: {
									color: downColor,
									color0: upColor,
									borderColor: null,
									borderColor0: null
								}
							},
							tooltip: {
								formatter: function(param) {
									param = param[0]
									return [
										'开盘: ' + param.data[0] + '<br/>',
										'收盘: ' + param.data[1] + '<br/>',
										'最低: ' + param.data[2] + '<br/>',
										'最高: ' + param.data[3] + '<br/>'
									].join('')
								}
							}
						},
						{
							name: '成交量',
							type: 'bar',
							xAxisIndex: 1,
							yAxisIndex: 1,
							data: data.volumes
						}
					]
				}
				myChart.setOption(option)
			},
			async getOpation() {
				// 获取是不是已添加自选
				let opts = {
					code: this.$route.query.code
				}
				let data = await api.isOption(opts)
				if (data.status === 0) {
					// 0 --> 未添加
					this.isOptionOpt = false
				} else {
					this.isOptionOpt = true
				}
			},
			async addOptions() {
				let data = await api.addOption({
					code: this.$route.query.code
				})
				if (data.status === 0) {
					this.$message.success('添加自选成功')
					this.isOptionOpt = true
				} else {
					this.$message.error(data.msg)
				}
			},
			async deteleOptions() {
				let data = await api.delOption({
					code: this.$route.query.code
				})
				if (data.status === 0) {
					this.$message.success('删除自选股成功')
					this.isOptionOpt = false
				} else {
					this.$message.error(data.msg)
				}
			},
			// 获取当前时间
			getNewDateYMD() {
				// 获取当前时间
				let date = new Date()
				let year = date.getFullYear()
				let month = date.getMonth() + 1
				let day = date.getDate()
				if (month < 10) {
					month = '0' + month
				}
				if (day < 10) {
					day = '0' + day
				}
				let nowDate = year + '-' + month + '-' + day
				return nowDate
			},
			// 获取当前时间的前几天时间 ns 指几天前的时间
			getBeforeDate(ns) {
				let n = ns
				let d = new Date()
				let year = d.getFullYear()
				let mon = d.getMonth() + 1
				let day = d.getDate()
				if (day <= n) {
					if (mon > 1) {
						mon = mon - 1
					} else {
						year = year - 1
						mon = 12
					}
				}
				d.setDate(d.getDate() - n)
				year = d.getFullYear()
				mon = d.getMonth() + 1
				day = d.getDate()
				let s = year + '-' + (mon < 10 ? ('0' + mon) : mon) + '-' + (day < 10 ? ('0' + day) : day)
				return s
			},
			// 获取当前时间
			getPresent() {
				// 获取当前时间
				let date = new Date()
				let year = date.getFullYear()
				let month = date.getMonth() + 1
				let day = date.getDate()
				let hh = date.getHours()
				let mm = date.getMinutes()
				let ss = date.getSeconds()
				if (month < 10) {
					month = '0' + month
				}
				if (day < 10) {
					day = '0' + day
				}
				let nowDate = year + '-' + month + '-' + day + ' ' + hh + ':' + mm + ':' + ss
				return nowDate
			},
			// 获取当前时间n个小时前的时间段
			getPresentNbeforeTime(n) {
				// 获取当前时间
				let date = new Date()
				let dates = new Date(date.getTime() - n * 60 * 60 * 1000)
				let year = dates.getFullYear()
				let month = dates.getMonth() + 1
				let day = dates.getDate()
				let hh = dates.getHours()
				let mm = dates.getMinutes()
				let ss = dates.getSeconds()
				if (month < 10) {
					month = '0' + month
				}
				if (day < 10) {
					day = '0' + day
				}
				let nowDate = year + '-' + month + '-' + day + ' ' + hh + ':' + mm + ':' + ss
				return nowDate
			},
			// 参数number为毫秒时间戳，format为需要转换成的日期格式
			formatTime(number, format) {
				let time = new Date(number)
				let newArr = []
				let formatArr = ['Y', 'M', 'D', 'h', 'm', 's']
				newArr.push(time.getFullYear())
				newArr.push(this.formatNumber(time.getMonth() + 1))
				newArr.push(this.formatNumber(time.getDate()))

				newArr.push(this.formatNumber(time.getHours()))
				newArr.push(this.formatNumber(time.getMinutes()))
				newArr.push(this.formatNumber(time.getSeconds()))

				for (let i in newArr) {
					format = format.replace(formatArr[i], newArr[i])
				}
				return format
			},
			// 格式化日期，如月、日、时、分、秒保证为2位数
			formatNumber(n) {
				n = n.toString()
				return n[1] ? n : '0' + n
			}
		}
	}
</script>
<style lang="less" scoped>
	.panel-box {
		// background: #1a202c;
		// padding: 0 5px;

		.panel-title {
			padding: 3px 10px;
		}

		.panel-body {
			// background: #171e2a;
			position: relative;

			.chart-box {
				height: 480px;
			}
		}
	}

	.chart-title {
		line-height: 35px;

		.li-type {
			float: left;

			&.on {
				.chart-type {
					color: #efbb53;
				}
			}
		}

		.chart-type {
			color: #5a657e;
			font-size: 14px;
			padding: 7px 5px;
			height: 14px;
			font-weight: 800;
		}
	}

	.detail-box {
		height: 40px;
		line-height: 40px;

		.name {
			color: #efbb53;
			font-size: 16px;
			margin-left: 8px;
		}

		.code {
			color: #9e9e9e;
			margin-left: 6px;
		}
	}

	.empty {
		padding-top: 100px;
		color: #5a657e;

		.iconfont {
			display: block;
			font-size: 120px;
			color: #5a657e;
			margin-bottom: 20px;
		}
	}
</style> -->
