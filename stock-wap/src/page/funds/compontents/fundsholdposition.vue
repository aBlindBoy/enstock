<template>
  <div class="wrapper">
    <div v-if="list.length<=0 && !getStatus" 
    class="empty text-center">
    No order information yet!
    </div>
    <div v-if="list.length<=0 && getStatus" 
    class="empty text-center">
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
              <!-- <span v-if="item.stockPlate=='科創'" :class="item.stockPlate=='科創'?'type':''">科創</span> -->
              <span class="direction pull-right big-font">
                        Latest price:
                        <b v-if="item.now_price == 0">-</b>
                        <b v-else
                           :class="item.now_price-item.buyOrderPrice<0?'space green':item.now_price-item.buyOrderPrice==0?'space':'space red'">{{item.now_price}}</b>
                    </span>
            </div>
            <div class="order-info">
              <p class="clearfix">
                <span class="col-xs-4">Buying price :<b class="space">{{item.buyOrderPrice}}</b></span>
                <span class="col-xs-4 text-center">Quantity:<b class="space">{{item.orderNum}}</b></span>
                <span class="col-xs-4 text-right">Market value:<b class="space">{{item.orderTotalPrice}}</b></span>
              </p>
              <p class="clearfix">
                <span class="col-xs-4">Handling fee:<b class="space">{{item.orderFee}}</b></span>
                <span class="col-xs-4 text-center">Stamp duty:<b class="space">{{item.orderSpread}}</b></span>
                <span class="col-xs-4 text-right">Storage fee:<b class="space">{{item.orderStayFee}}</b></span>
              </p>
              <p class="clearfix">
                <span class="col-xs-4">Spread fee:<b class="space">{{item.spreadRatePrice}}</b></span>
                        <span class="col-xs-4 text-center">Floating profit and loss:
                            <b v-if="item.now_price == 0">-</b>
                            <b v-else :class="item.profitAndLose > 0 ? 'red':'green'">{{item.profitAndLose}}</b>
                        </span>
                <span class="col-xs-4 text-right big-font">Total profit and loss:
                    <b v-if="item.now_price == 0">-</b>
                    <b v-else :class="item.allProfitAndLose > 0 ? 'red':'green'">{{item.allProfitAndLose}}</b>
                </span>
              </p>

            </div>
            <div class="order-foot clearfix">
              <div style="text-align: left;color: #666;padding: 0;" class="col-xs-6">
                <b v-if="item.buyOrderTime">{{new Date(item.buyOrderTime) | timeFormat}}</b>
                <b v-else></b>
              </div>
              <div @click="toSell(item)" class="foot-btn">
                <i class='font-icon'></i>
                Close the position
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
    },
    selectedNumber: {
      type: String
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
      hasChangeSell: false // 平倉狀態改變
    }
  },
  watch: {
    selectedNumber (val) {
      if (val === '1') {
        if (!this.$store.state.userInfo.idCard) {
          this.getUserInfo()
        }
        this.getListDetail()
        clearInterval(this.timer)
        this.timer = setInterval(this.refreshList, 5000)
      } else {
        clearInterval(this.timer)
      }
    }
  },
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
      if(this.pageNum == 1){
        this.list = []
      }
      let data = await api.getFundsOrderList(opt)
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
        stockCode: '', // 代碼
        stockSpell: '', // 簡拼
        pageNum: 1,
        pageSize: this.currentNum
      }
      this.isRefresh = true
      let data = await api.getFundsOrderList(opt)
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
    toSell (val) {
        MessageBox.confirm('Are you sure you want to close the position?').then(async action => {
        let opt = {
          positionSn: val.positionSn
        }
        let data = await api.sellFunds(opt)
        if (data.status === 0) {
          Toast(data.msg)
          this.hasChangeSell = true
          this.handleOptions(this.hasChangeSell)
          this.getListDetail()
        } else {
          Toast(data.msg)
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
  #app.red-theme{
    .order-info-box{
      background-color: #fff;
      .order-title{
        .main {
          color: #000;
        }
      }
      .order-info{
        color: #000;
      }
    }
    .order-foot {
      border-top-color: #ccc;
    }
    .load-all{
      background-color: #BB1815;
      color: #fff;
    }
      .empty{
      background-color: #E9E9E9;
      color: #000;
    }
  }
</style>
