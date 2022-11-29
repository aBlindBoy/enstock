<template>
  <div class="wrapper">
    <div ref="myEchart" style="height:60px;"></div>
  </div>
</template>

<script>
  import * as api from '../../../axios/api'
  import echarts from 'echarts'

  export default {
    components: {},
    props: {
      code: {
        type: String,
        default () {

        }
      }
    },
    data () {
      return {
        ChartsTime: []
      }
    },
    watch: {},
    computed: {},
    created () {},
    mounted () {
      this.getDate()
      this.getChrtsTime()
    },
    methods: {
      getChrtsTime () {
        // 获取时间
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
      },
      async getDate () {
        let opts = {
          stockCode: this.code,
          code: this.code,
          time: 5,
          ma: 5,
          size: 50
        }

        let data = await api.getMinuteLine(opts)
        if (data.status === 0) {
          this.initEchartMap(data.data)
        } else {
          this.$message.success(data.msg)
        }
      },
      initEchartMap (data) {
        let myChart = echarts.init(this.$refs.myEchart)
        let this_ = this
        window.onresize = myChart.resize
        let option = {
          xAxis: {
            type: 'category',
            axisLabel: { show: false },
            axisLine: { show: false },
            axisTick: { show: false },
            splitLine: { show: false },
            data: this_.ChartsTime
          },
          yAxis: {
            type: 'value',
            axisLabel: { show: false },
            axisLine: { show: false },
            axisTick: { show: false },
            splitLine: { show: false },
            min: 'dataMin',
            max: 'dataMax'
          },
          series: [{
            data: data.price,
            type: 'line',
            symbol: 'none',
            itemStyle: {
              color: 'rgb(237, 186, 83)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(237, 186, 83,0.1)'
              }, {
                offset: 1,
                color: 'rgba(237, 186, 83,0)'
              }])
            }
          }]
        }
        myChart.setOption(option)
      }
    }
  }
</script>
<style lang="less" scoped>
</style>
