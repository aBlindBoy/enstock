<template>
  <div>
    <div v-if="list.length<=0" class="empty text-center">
      {{$t('rechargeList.transferToPublic')}}
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
                    <span :class="['main',item.payChannel == 0?'ali':item.payChannel == 1?'cart':'wechat']">
                        <!-- <i v-if="item.payChannel == 0 || item.payChannel == '支付寶'" style="color:#1296db;"
                           class="iconfont icon-zhifubao"></i>
                        <i v-if="item.payChannel == '微信' " style="color:#1296db;" class="iconfont icon-weixin"></i>
                        <i v-if="item.payChannel == 1 || item.payChannel == '對公轉帳'" style="color:#1296db;"
                           class="iconfont icon-yinlian"></i> -->
                        <!-- {{item.payChannel == 0?'對公轉帳':item.payChannel == 1?'對公轉帳':item.payChannel}} -->
                        {{$t('rechargeList.transferToPublic')}}
                      </span>
              <span class="payNumber">{{$t('common.recharge')}}:<span :style="{color:$state.theme =='red'?'#BB1815':''}">{{item.payAmt}}</span></span>
              <span
                :class="item.orderStatus == 1?'green pull-right':item.orderStatus == 2?'red pull-right':'red pull-right'">
                        <i v-if="item.orderStatus == 1" class="iconfont icon-tongguo4 animated bounceIn"></i>
                        <i v-if="item.orderStatus==0" class="iconfont icon-dengdai animated bounceInDown"></i>
                        <i v-if="item.orderStatus == 2" class="iconfont icon-failure animated bounceInDown"></i>
                        <i v-if="item.orderStatus == 3"
                           class="iconfont icon-iconfontweitongguo animated bounceInDown"></i>
                        <!-- 1 => 成功 2 失敗 3取消 4 等待 -->
                        <!-- {{item.orderStatus == 1?'Recharge succeeded':item.orderStatus == 2?'Recharge failed':item.orderStatus == 3?'Cancel Recharge':'under review'}} -->
                        {{orderStatus[item.orderStatus]}}
                    </span>
              <!-- <span class="secondary ">123456789</span> -->
            </div>
            <div class="order-info">
              <!-- <p class="clearfix">
                <span class="col-xs-5">{{item.orderDesc}}</span>
              </p> -->
              <!-- <p class="clearfix">
                <span class="col-xs-12">訂單號:<b>{{item.orderSn}}</b></span>
              </p>
              <p class="clearfix">
                        <span class="secondary col-xs-6">時間:
                            <b v-if="item.addTime">{{new Date(item.addTime) | timeFormat}}</b>
                            <b v-else></b>
                        </span>
              </p> -->
              <div class="info-mix ">
                <p class="info-item">{{$t('rechargeList.orderNumber')}}:<b>{{item.orderSn}}</b></p>
                <p class="info-item"> {{$t('rechargeList.time')}}:
                    <b v-if="item.addTime">{{new Date(item.addTime) | timeFormat}}</b>
                    <b v-else></b>
                </p>
              </div>
            </div>

          </div>
          <!-- <div class="capital">
              <div class="pro">
                  {{item.payChannel}} <span class="pull-right">金額:{{item.payAmt}}</span>
              </div>
              <div class=" clearfix">
                  <div class="col-xs-4"></div>
                  <div class="col-xs-8">
                      <span class="pull-right">
                          {{new Date(item.addTime) | timeFormat}}
                      </span>
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
      pageSize: 15,
      total: 0
    }
  },
  watch: {},
  computed: {

    orderStatus(){
      return [this.$t('rechargeList.underReview'),this.$t('rechargeList.rechargeSucceeded'),
      this.$t('rechargeList.rechargeFailed'),this.$t('rechargeList.cancelRecharge')]
    }
  },
  created () {},
  mounted () {
    this.getListDetail()
  },
  methods: {
    async getListDetail () {
      let opt = {
        payChannel: '', // 支付方式
        orderStatus: '', // 訂單狀態
        pageNum: this.pageNum,
        pageSize: 15
      }
      let data = await api.rechargeList(opt)
      if (data.status === 0) {
        data.data.list.forEach(element => {
          this.list.push(element)
        })
        this.total = data.data.total
      } else {
        Toast(data.msg)
      }
    },
    async loadMore () {
      if (this.list.length < 10 || this.total <= this.pageNum * this.pageNum) {
        return
      }
      this.loading = true
      // 加載下一頁
      this.pageNum++
      await this.getListDetail()
      this.loading = false
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    padding-top: 0.9rem;
  }

  .table-list {
    padding: 0.2rem 0;

    .list-body {
      padding: 0.1rem 0.3rem;

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

  .payNumber {
    font-size: 0.24rem;
    font-weight: bold;
    span {
      font-family: lightnumber;
    }
  }
  /deep/.order-info-box {
    background-color: #16171d;
    padding: 0;
    .main{
      padding: 0.05rem .15rem;
      letter-spacing:0;
      text-align: center;
      margin-right: .16rem;
      font-size: 0.24rem;
      border-radius: 3px;
      &.ali {
        background-color: #138EB4;
      }
      &.cart {
        background-color: #7266BA;
      }
      &.wechat {
        background-color: #009C46;
      }
    }
    .order-info {
      border-bottom: 1px solid #2e3237;
      padding-bottom: .3rem;
    }
    .order-title{
      border-bottom: none;
    }
    .info-mix{
      // display: flex;
      font-size: .2rem;
      width: 100%;
      .info-item {
        margin-right: .2rem;
        color: #fff8;
      }
    }
  }
  .red-theme {
    .list-body {
      background-color: #fff;
    }
    .order-info-box {
      background-color: #fff;
      .order-info {
        border-bottom-color: #e9e9e9;
      }
    }
    .order-info-box .main.cart {
      color: #fff;
    }
    .payNumber {
      color: #000;
    }
    .order-info-box .info-mix .info-item{
      color: #666666;
    }
    .load-all{
      background-color: #fff;
    }
  }
</style>
