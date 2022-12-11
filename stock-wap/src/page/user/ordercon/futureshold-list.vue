<template>
  <div class="wrapper">
    <div v-if="list.length<=0 && !getStatus" class="empty text-center">
      No order information yet!
    </div>
    <div v-if="list.length<=0 && getStatus" class="empty text-center">
      <mt-spinner type="fading-circle"></mt-spinner>
      Loading...
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
              <span @click="toDetail(item.futuresCode)" class="main">{{item.futuresName}}</span>
              <span class="secondary">({{item.futuresCode}})</span>
              <span :class="item.orderDirection=='bullish'?'type type-up':'type type-down'">{{item.orderDirection=='bullish'?'bullish':'bearish'}}</span>
              <span class="direction pull-right big-font">
                        total profit and loss:
                        <b v-if="item.nowPrice == 0">-</b>
                        <b v-else
                           :class="item.allProfitAndLose<0?'space green':item.allProfitAndLose==0?'space':'space red'">{{item.allProfitAndLose}} <span
                          class="mini-size">{{item.coinCode}}</span></b>
                        <b class="mini-font-title"> ≈ {{(item.allProfitAndLose * item.coinRate).toFixed(2)}} USD</b>
                    </span>
              <!-- <span class="red direction pull-right">{{item.orderDirection}}<i class="iconfont icon-up"></i></span> -->
              <!-- <span class="secondary ">123456789</span> -->
            </div>
            <div class="order-info">
              <div class="table-title clearfix">
                <span class="col-xs-3">Buy/Last Price</span>
                <span class="col-xs-3">Quantity/Margin</span>
                <span class="col-xs-3">handling fee</span>
                <span class="col-xs-3">Floating profit and loss</span>
              </div>
              <div class="table-value clearfix">
                <div class="col-xs-3">
                  <p>{{item.buyOrderPrice}} <span class="mini-size">{{item.coinCode}}</span></p>
                  <!-- <p class="mini-font"> ≈ {{(item.buyOrderPrice * item.coinRate).toFixed(2)}} USD</p> -->
                  <p>
                    <b v-if="item.nowPrice == 0">-</b>
                    <b v-else
                       :class="item.nowPrice-item.buyOrderPrice<0?'green':item.nowPrice-item.buyOrderPrice==0?'':' red'">{{Number(item.nowPrice).toFixed(2)}}
                      <span class="mini-size">{{item.coinCode}}</span></b>
                  </p>
                  <!-- <p class="mini-font"> ≈ {{(item.nowPrice * item.coinRate).toFixed(2)}} USD</p> -->
                </div>
                <div class="col-xs-3">
                  <p>{{item.orderNum}} <span class="mini-size"></span></p>
                  <p>{{item.allDepositAmt}} <span class="mini-size"> USD</span></p>
                  <!-- <p class="mini-font"> ≈ {{(item.allDepositAmt * item.coinRate).toFixed(2)}} USD</p> -->
                </div>
                <div class="col-xs-3">
                  <p>{{item.orderFee}} <span class="mini-size">{{item.coinCode}}</span></p>
                  <p class="mini-font"> ≈ {{(item.orderFee * item.coinRate).toFixed(2)}} USD</p>
                  <!-- <p>
                      <b v-if="item.nowPrice == 0">-</b>
                      <b v-else :class="item.profitAndLose<0?' green':item.profitAndLose==0?'':' red'">{{item.profitAndLose}} <span class="mini-size">{{item.coinCode}}</span></b>
                  </p>
                  <p class="mini-font"> ≈ {{(item.profitAndLose * item.coinRate).toFixed(2)}} USD</p> -->
                </div>
                <div class="col-xs-3">
                  <!-- <p>{{item.orderFee}} <span class="mini-size">{{item.coinCode}}</span></p>
                  <p class="mini-font"> ≈ {{(item.orderFee * item.coinRate).toFixed(2)}} USD</p> -->
                  <p>
                    <b v-if="item.nowPrice == 0">-</b>
                    <b v-else :class="item.profitAndLose<0?' green':item.profitAndLose==0?'':' red'">{{item.profitAndLose}}
                      <span class="mini-size">{{item.coinCode}}</span></b>
                  </p>
                  <p class="mini-font"> ≈ {{(item.profitAndLose * item.coinRate).toFixed(2)}} USD</p>
                </div>
              </div>
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
        Loading...
      </div>
      <div v-show="!loading" class="load-all text-center">
        all loaded
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
      hasChangeSell: false, // 平倉狀態改變
      timer:''
    }
  },
  watch: {
    selectedNumber (val) {
      if (val === '4') {
        if (!this.$store.state.userInfo.idCard) {
          this.getUserInfo()
        }
        this.getListDetail()
        this.timer = setInterval(this.refreshList, 5000)
      } else {
        clearInterval(this.timer)
      }
    }
  },
  computed: {},
  created () {
    // this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {

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
        fnCode: '', // 代碼
        fnName: '', // 簡拼
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }
      this.getStatus = true
      let data = await api.getFuturesOrderList(opt)
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
        fnCode: '', // 代碼
        fnName: '', // 簡拼
        pageNum: 1,
        pageSize: this.currentNum
      }
      this.isRefresh = true
      let data = await api.getFuturesOrderList(opt)
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
        Toast('You have not verified your real name, please verify your real name before placing an order')
        this.$router.push('/authentication')
        return
      }
      MessageBox.confirm('Are you sure you want to close the position?').then(async action => {
        let opt = {
          positionSn: val
        }
        let data = await api.sellFutures(opt)
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
        path: '/list',
        query: {
          index: '4'
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    padding-bottom: 0;
    .empty {
      background-color: #16171d;
    }
  }
  .col-xs-3 .iconfont {
    font-size: 0.22rem;
  }

  .order-info-box {
    .order-info {
      padding-top: 0.1rem;
    }

    .mini-font-title {
      color: #777;
      font-size: 0.2rem;
      position: absolute;
      right: 0;
      top: 0.56rem;
      color: #777;
      line-height: 0.2rem;
    }

    .mini-size {
      font-size: 0.2rem;
    }

    .big-font {
      position: relative;
      line-height: 0.6rem;
    }
  }

  .table-title {
    font-size: 0.22rem;
    line-height: 0.4rem;
    margin-bottom: 0.1rem;

    .col-xs-3 {
      padding: 0;
    }
  }

  .table-value {
    .col-xs-3 {
      padding: 0;

      p {
        padding: 0;
        line-height: 0.32rem;
      }
    }

    .mini-font {
      font-size: 0.2rem;
      color: #777;
      line-height: 0.2rem;
    }
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
