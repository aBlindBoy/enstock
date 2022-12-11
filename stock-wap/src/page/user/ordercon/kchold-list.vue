<template>
  <div class="wrapper">
    <div v-if="list.length<=0 && !getStatus" class="empty text-center">
      No order information yet!
    </div>
    <div v-if="list.length<=0 && getStatus" class="empty text-center">
      <mt-spinner type="fading-circle"></mt-spinner>
      {{$t('common.loading')}}...
    </div>
    <div v-if="list.length>0">
      <ul
        class="order-info-box-wrap"
        v-infinite-scroll="loadMore"
        :infinite-scroll-disabled="loading"
        infinite-scroll-distance="0">
        <li v-for="(item) in list" :key="item.key">
          <div class="order-info-box">
            <div class="order-title">
              <span @click="toDetail(item.stockCode)" class="main">{{item.stockName}}</span>
              <span class="secondary">({{item.stockCode}})</span>
              <span :class="item.orderDirection=='bullish'?'type type-up':'type type-down'">{{item.orderDirection=='bullish'?'bullish':'bearish'}}</span>
              <span class="direction pull-right big-font">
                latest price:
                        <b v-if="item.now_price == 0">-</b>
                        <b v-else
                           :class="item.now_price-item.buyOrderPrice<0?'space green':item.now_price-item.buyOrderPrice==0?'space':'space red'">{{item.now_price}}</b>
                    </span>
              <!-- <span class="red direction pull-right">{{item.orderDirection}}<i class="iconfont icon-up"></i></span> -->
              <!-- <span class="secondary ">123456789</span> -->
            </div>
            <div class="order-info">
              <!-- <p class="font"><span class="col-xs-4 text-center pull-right red">審核中</span></p> -->
              <p class="clearfix">
                <span class="col-xs-4">Buying price:<b class="space">{{item.buyOrderPrice}}</b></span>
                <span class="col-xs-4 text-center">Quantity:<b class="space">{{item.orderNum}}</b></span>
                <!-- <span class="col-xs-4 text-right">杠桿:<b class="space">{{item.orderLever}}</b>倍</span> -->
                <span class="col-xs-4 text-right">market value:<b class="space">{{item.orderTotalPrice}}</b></span>
              </p>
              <!-- <p class="clearfix">
                  <span class="col-xs-4">方嚮:
                      <b v-if="item.orderDirection=='bullish'">{{item.orderDirection}}<i style="color:red" class="iconfont icon-up"></i></b>
                      <b v-if="item.orderDirection=='bearish'">{{item.orderDirection}}<i style="color:green" class="iconfont icon-down"></i></b>
                  </span>
                  <span class="col-xs-4 text-center">市值:<b class="space">{{item.orderTotalPrice}}</b></span>
                  <span class="col-xs-4 text-right">Days to stay:<b class="space">{{item.orderStayDays}}</b></span>
              </p> -->
              <p class="clearfix">
                <span class="col-xs-4">handling fee:<b class="space">{{item.orderFee}}</b></span>
                <span class="col-xs-4 text-center">Stamp duty:<b class="space">{{item.orderSpread}}</b></span>
                <span class="col-xs-4 text-right">Storage fee:<b class="space">{{item.orderStayFee}}</b></span>
              </p>
              <p class="clearfix">
                        <span class="col-xs-6">Floating profit and loss:
                            <b v-if="item.now_price == 0">-</b>
                            <b v-else
                               :class="item.profitAndLose<0?'space green':item.profitAndLose==0?'space':'space red'">{{item.profitAndLose}}</b>
                        </span>
                <span class="col-xs-6 text-right big-font">total profit and loss:
                            <b v-if="item.now_price == 0">-</b>
                            <b v-else
                               :class="item.allProfitAndLose<0?'space green':item.allProfitAndLose==0?'space':'space red'">{{item.allProfitAndLose}}</b>
                        </span>
              </p>
              <!-- <p class="clearfix">
                  <span class="secondary col-xs-12 text-right">Buy time:
                      <b v-if="item.buyOrderTime">{{new Date(item.buyOrderTime) | timeFormat}}</b>
                      <b v-else></b>
                  </span>
              </p> -->
            </div>
            <div class="order-foot clearfix">
              <div style="text-align: left;color: #666;padding: 0;" class="col-xs-6">
                <b v-if="item.buyOrderTime">{{new Date(item.buyOrderTime) | timeFormat}}</b>
                <b v-else></b>
              </div>
              <div @click="sell(item.positionSn)" class="foot-btn">
                <i class='font-icon'></i>
                sell
              </div>
            </div>
          </div>

        </li>
      </ul>
      <div v-show="loading" class="load-all text-center">
        <mt-spinner type="fading-circle"></mt-spinner>
        {{$t('common.loading')}}...
      </div>
      <div v-show="!loading" class="load-all text-center">
        {{$t('common.allLoaded')}}
      </div>
    </div>
  </div>
</template>

<script>
import { Toast, MessageBox } from 'mint-ui'
import * as api from '@/axios/api'

export default {
  components: {},
  props: {
    // list:{
    //     type:Array,
    //     default:{
    //         return:[]
    //     }
    // },
    // form:{
    //     type:Object,
    //     default:{
    //         return:{}
    //     }
    // },
    // getListDetail:{
    //     type:Function,
    //     default: function(){}
    // },
    handleOptions: {
      type: Function,
      default: function () {}
    }
  },
  data () {
    return {
      loading: false, // 是否正在加載更多
      isRefresh: false, // 是否正在刷新
      getStatus: false, // 是否正在數據
      pageNum: 1,
      pageSize: 10,
      currentNum: 10,
      list: [],
      total: 0, // 記錄總值
      hasChangeSell: false ,// 平倉狀態改變
      timer:''
    }
  },
  watch: {},
  computed: {},
  created () {
    this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {
    if (!this.$store.state.userInfo.idCard) {
      this.getUserInfo()
    }
    this.getListDetail()
  },
  methods: {
    async getUserInfo () {
      // 獲取用戶信息
      let data = await api.getUserInfo()
      if (data.status === 0) {
        this.$store.state.userInfo = data.data
      } else {
        Toast(data.msg)
      }
    },
    async loadMore () {
      // 1、總數小於 該次查詢的總頁數 不進行加載更多 （無下一頁）
      // 2、當頁加載數據還未加載完 不進行下一個加載
      if (this.list.length < this.pageSize || this.loading || this.total <= this.currentNum) {
        return
      }
      this.loading = true
      // 加載下一頁
      this.pageNum++
      await this.getListDetail()
      this.currentNum = this.pageNum * this.pageSize
      this.loading = false
    },
    async getListDetail () {
      let opt = {
        state: 0,
        stockCode: '', // 代碼
        stockSpell: '', // 簡拼
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }
      this.getStatus = true
      let data = await api.getOrderList(opt)
      if (data.status === 0) {
        data.data.list.forEach(element => {
          this.list.push(element)
        })
        this.getStatus = false
        this.total = data.data.total
      } else {
        Toast(data.msg)
      }
    },
    async refreshList () {
      // 判斷是不是已經查詢 或者是否正在加載下一頁 是則退出，不進行刷新
      if (this.loading) {
        return
      }
      let opt = {
        state: 0,
        stockPlate: '科創',
        stockCode: '', // 代碼
        stockSpell: '', // 簡拼
        pageNum: 1,
        pageSize: this.currentNum
      }
      this.isRefresh = true
      let data = await api.getOrderList(opt)
      this.isRefresh = false
      this.total = data.data.total
      this.list = data.data.list
    },
    canBuyStatus () {
      let dataTime = new Date()
      let day = dataTime.getDay() // 星期幾
      let hour = dataTime.getHours() // 小時
      let minute = dataTime.getMinutes() // 分鐘
      if (day === 6 || day === 7) {
        return false
      }
      if (hour < 9 || (hour >= 12 && hour < 13) || hour >= 15) {
        return false
      }
      if (hour === 9 && minute < 32) {
        return false
      }
      if (hour === 11 && minute >= 30) {
        return false
      }
      if (hour === 14 && minute > 57) {
        return false
      }
      return true
    },
    sell (val) {
      // if(!this.canBuyStatus()){
      //     Toast('不在開盤時間內，暫不能交易！')
      //     return
      // }
      if (!this.$store.state.userInfo.idCard) {
        Toast('您還未實名認證,請先實名認證了再下單')
        this.$router.push('/authentication')
        return
      }
      MessageBox.confirm('Are you sure you want to close the position?').then(async action => {
        let opt = {
          positionSn: val
        }
        let data = await api.sell(opt)
        if (data.status === 0) {
          Toast(data.msg)
          this.hasChangeSell = true
          this.handleOptions(this.hasChangeSell)
          this.getListDetail()
        } else {
          Toast(data.msg)
        }
      })
    },
    toDetail (code) {
      this.$router.push({
        path: '/listdetail',
        query: {
          code: code
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    padding-bottom: 0
  }

  .col-xs-3 .iconfont {
    font-size: 0.22rem;
  }
</style>
