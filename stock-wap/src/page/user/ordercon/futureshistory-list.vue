<template>
  <div class="wrapper">
    <div v-if="list.length<=0" class="empty text-center">
      No order information yet!
    </div>
    <div v-if="list.length>0">
      <ul
        class="order-info-box-wrap"
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="loading"
        infinite-scroll-distance="10">
        <li v-for="item in list" :key="item.key">
          <div class="order-info-box">
            <div class="order-title">
              <span class="main">{{item.futuresName}}</span>
              <span class="secondary">({{item.futuresCode}})</span>
              <span :class="item.orderDirection=='bullish'?'type type-up':'type type-down'">{{item.orderDirection=='bullish'?'bullish':'bearish'}}</span>
              <span class="pull-right big-font">total profit and loss:
                        <b :class="item.allProfitAndLose<0?'space green':item.allProfitAndLose==0?'space':'space red'">{{item.allProfitAndLose}}</b> <span
                  class="mini-size">{{item.coinCode}}</span>
                        <b class="mini-font"> ≈ {{(item.allProfitAndLose * item.sellRate).toFixed(2)}} USD</b>
                    </span>
            </div>
            <div class="order-info">
              <!-- <p class="clearfix">
                  <span class="col-xs-4">Buy point:<b class="space">{{item.buyOrderPrice}}</b></span>
                  <span class="col-xs-4 text-center">Quantity:<b class="space">{{item.orderNum}}</b></span>
                  <span class="col-xs-4 text-right">Margin:<b class="space">{{item.allDepositAmt}}</b></span>
              </p>
              <p class="clearfix">
                  <span class="col-xs-4">Sell points:<b class="space">{{item.sellOrderPrice}}</b></span>
                  <span class="col-xs-4 text-center">handling fee:<b class="space">{{item.orderFee}}</b></span>
                  <span class="col-xs-4 text-right">Floating profit and loss:<b :class="item.profitAndLose<0?'space green':item.profitAndLose==0?'space':'space red'">{{item.profitAndLose}}</b></span>
              </p> -->
              <div class="table-title clearfix">
                <span class="col-xs-4">buy/sell</span>
                <span class="col-xs-4">Quantity/Margin</span>
                <span class="col-xs-4">handling fee/Floating profit and loss</span>
              </div>
              <div class="table-value clearfix">
                <div class="col-xs-4">
                  <p>{{item.buyOrderPrice}} <span class="mini-size">{{item.coinCode}}</span></p>
                  <p>{{item.sellOrderPrice}} <span class="mini-size">{{item.coinCode}}</span></p>
                </div>
                <div class="col-xs-4">
                  <p>{{item.orderNum}} <span class="mini-size"></span></p>
                  <p>{{item.allDepositAmt}} <span class="mini-size">USD</span></p>
                </div>
                <div class="col-xs-4">
                  <p>{{item.orderFee}} <span class="mini-size">{{item.coinCode}}</span></p>
                  <p>{{item.profitAndLose}} <span class="mini-size">{{item.coinCode}}</span></p>
                </div>
              </div>
              <p class="clearfix">
                        <span class="secondary col-xs-6">buy:
                            <b v-if="item.buyOrderTime">{{new Date(item.buyOrderTime) | timeFormat}}</b>
                            <b v-else></b>
                        </span>
                <span class="secondary col-xs-6 text-right">sell:
                            <b v-if="item.sellOrderTime">{{new Date(item.sellOrderTime) | timeFormat}}</b>
                            <b v-else></b>
                        </span>
              </p>
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
import { Toast } from 'mint-ui'
import * as api from '@/axios/api'

export default {
  components: {},
  props: {

    hasChangeSell: {
      type: Boolean,
      default: function () {
        return false
      }
    }
  },
  data () {
    return {
      loading: false,
      pageNum: 1,
      pageSize: 10,
      currentNum: 0,
      list: [],
      total: 0 // 記錄總值
    }
  },
  watch: {
    hasChangeSell (newval) {
      if (newval) {
        this.list = []
        this.getListDetail()
      }
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getListDetail()
  },
  methods: {
    async loadMore () {
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
        state: 1,
        fnCode: '', // 代碼
        fnName: '', // 簡拼
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }
      let data = await api.getFuturesOrderList(opt)
      if (data.status === 0) {
        data.data.list.forEach(element => {
          this.list.push(element)
        })
        this.total = data.data.total
      } else {
        Toast(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    padding-bottom: 0;
    .empty{
      background-color: #16171d;
    }
  }

  .order-title {
    .icon--kulian {
      font-size: 0.7rem;
      color: #006b96;
    }

    .icon-xiaolian {
      font-size: 0.6rem;
      color: #d50000;
    }

    .icon-pingchanglian {
      font-size: 0.6rem;
      color: #ddd;
    }
  }

  .order-info-box {
    .order-info {
      padding-top: 0.1rem;
    }

    .mini-font {
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

    .col-xs-4 {
      padding: 0;
    }
  }

  .table-value {
    .col-xs-4 {
      padding: 0;

      p {
        padding: 0;
        line-height: 0.32rem;
      }
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
