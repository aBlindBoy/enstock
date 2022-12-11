<template>
  <div class="wrapper">
    <div v-if="list.length<=0" class="empty text-center">
      No Withdrawal information yet!
    </div>
    <div v-else>
      <ul
        class="table-list"
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="loading"
        infinite-scroll-distance="10">
        <li class="list-body" v-for="(item) in list" :key="item.key">
          <div class="order-info-box">
            <div class="order-title">
                    <span class="main">
                      Withdrawal to financial account
                    </span>
              <span class="payNumber">{{item.withAmt}}</span>
              <span class="red pull-right">
                        {{item.withStatus == 1?'Withdrawal success':item.withStatus == 2?'Withdrawal failed':item.withStatus == 3?'cancel the order':'under review'}}
                        <i v-if="item.withStatus == 1" class="iconfont icon-tongguo4 animated bounceIn"></i>
                        <i v-if="item.withStatus==0" class="iconfont icon-dengdai animated bounceInDown"></i>
                        <i v-if="item.withStatus == 2" class="iconfont icon-failure animated bounceInDown"></i>
                        <i v-if="item.withStatus == 3"
                           class="iconfont icon-iconfontweitongguo animated bounceInDown"></i>
                    </span>
              <!-- <span class="secondary ">123456789</span> -->
            </div>
            <div class="order-info">
              <p class="clearfix">
                <span class="col-xs-6">Handling fee:<b class="space">{{item.withFee}}</b></span>
                <!-- <span class="col-xs-6">實際到帳金額:<b class="space" style="font-size:0.26rem">{{item.withAmt - item.withFee}}</b>USD</span>                         -->
              </p>
              <p class="clearfix">
                <span class="col-xs-12">financial account:<b class="space">{{item.bankName}}-{{item.bankAddress}}</b></span>
              </p>
              <p class="clearfix">
                <span class="col-xs-12">card number:<b class="space">{{item.bankNo}}</b></span>
              </p>
              <p v-if="item.withStatus == 3" class="clearfix">
                <span class="col-xs-12">Reason for Cancellation:<b class="space">{{item.withMsg}}</b></span>
              </p>
              <p v-if="item.withStatus == 2" class="clearfix">
                <span class="col-xs-12">Reason for Cancellation:<b class="space">{{item.withMsg}}</b></span>
              </p>
              <p class="clearfix">
                        <span class="secondary col-xs-6">time:
                            <b v-if="item.applyTime">{{new Date(item.applyTime) | timeFormat}}</b>
                            <b v-else></b>
                        </span>
              </p>
            </div>
            <div v-if="item.withStatus == 0" class="order-foot clearfix">
              <div @click="cancle(item.id)" class="foot-btn">
                <i class='font-icon'></i>
                Cancel Withdrawal
              </div>
            </div>

          </div>
          <!-- <div class="capital">
              <div class="pro">
                  股票:300092/開山股份 <span class="pull-right">金額:50.241</span>
              </div>
              <div class=" clearfix">
                  <div class="col-xs-8">01-21 13:22</div>
                  <div class="col-xs-4 ">
                      <span class="pull-right">交易狀態</span>
                  </div>
              </div>
          </div> -->
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
  props: {},
  data () {
    return {
      loading: false,
      list: [],
      pageNum: 1,
      pageSize: 15
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getListDetail()
  },
  methods: {
    async getListDetail () {
      let opt = {
        withStatus: '', // Withdrawal狀態 0已提交，1轉帳成功，2轉帳失敗
        pageNum: this.pageNum,
        pageSize: 15
      }
      let data = await api.withdrawList(opt)
      if (data.status === 0) {
        data.data.list.forEach(element => {
          this.list.push(element)
        })
      } else {
        Toast(data.msg)
      }
    },
    async loadMore () {
      if (this.list.length < 10) {
        return
      }
      this.loading = true
      // 加載下一頁
      this.pageNum++
      await this.getListDetail()
      this.loading = false
    },
    async cancle (val) {
      // 取消Withdrawal
      // MessageBox.confirm('Are you sure you want to close the position?').then(async action => {
      let opt = {
        withId: val
      }
      let data = await api.canceloutMoney(opt)
      if (data.status === 0) {
        this.list = []
        Toast(data.msg)
        this.getListDetail()
      } else {
        Toast(data.msg)
      }
      // });
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    // padding-top: 0.9rem;
  }

  .payNumber {
    font-size: 0.3rem;
    color: #ff8000;
  }

  .table-list {
    padding: 0.2rem 0;

    .list-body {
      padding: 0.1rem 0.2rem;

      .capital:nth-child(1) {
        border-top: 0.01rem solid #3f444a;
      }

      .capital {
        padding: 0.2rem;
        // border-radius: 0.2rem;
        border-bottom: 0.01rem solid #3f444a;

        div {
          line-height: 0.4rem;
        }

        .col-xs-4 {
          padding-left: 0;
          padding-right: 0;
        }

        .pro {
          color: #999;
        }
      }
    }
  }
</style>
