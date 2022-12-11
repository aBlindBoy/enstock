<template>
  <div class="wrapper">
    <!-- <div class="header">
      <mt-header title="期貨交易">
        <router-link to="/list" slot="left">
          <mt-button icon="back">行情</mt-button>
        </router-link>
      </mt-header>
    </div> -->
    <div class="detail-part">
      <div class="index-name">
        <p>{{detail.futuresName}} <span>{{detail.futuresCode}}</span></p>
      </div>
      <div class="clearfix">
        <div class="pull-left col-xs-4">
          <p
            :class="(statusDetail.nowPrice - statusDetail.lastClose)>0?'price red':(statusDetail.nowPrice - statusDetail.lastClose)<0?'green price':'price'">
            {{Number(statusDetail.nowPrice).toFixed(2)}}</p>
          <p
            :class="(statusDetail.nowPrice - statusDetail.lastClose)>0?'gain red':(statusDetail.nowPrice - statusDetail.lastClose)<0?'green gain':'gain'">
            <span>{{Number(statusDetail.nowPrice - statusDetail.lastClose).toFixed(2)}}</span>
            <span>{{Number((statusDetail.nowPrice - statusDetail.lastClose)/statusDetail.lastClose*100).toFixed(2)}}%</span>
          </p>
        </div>
        <div class="pull-right col-xs-8">
          <ul class="price-detail text-center">
            <li>
              <!-- <p class="title"></p> -->
              <p :class="(statusDetail.nowPrice - statusDetail.lastClose)<0?'number green':'number red'">
                <span class="title">open today</span>
                {{Number(statusDetail.todayOpen).toFixed(2)}}
              </p>
            </li>
            <li>
              <!-- <p class="title"></p> -->
              <p :class="(statusDetail.nowPrice - statusDetail.lastClose)<0?'number green':'number red'">
                <span class="title">received yesterday</span>
                {{Number(statusDetail.lastClose).toFixed(2)}}
              </p>
            </li>
            <li>
              <!-- <p class="title"></p> -->
              <p :class="(statusDetail.nowPrice - statusDetail.lastClose)<0?'number green':'number red'">
                <span class="title">Highest</span>
                {{Number(statusDetail.maxPrice).toFixed(2)}}
              </p>
            </li>
            <li>
              <!-- <p class="title"></p> -->
              <p :class="(statusDetail.nowPrice - statusDetail.lastClose)<0?'number green':'number red'">
                <span class="title">lowest</span>
                {{Number(statusDetail.minPrice).toFixed(2)}}
              </p>
            </li>
            <!-- <li>
                <p class="number red">
                    <span class="title red">漲停限制</span>
                    {{(Number(detail.currentPoint) * settingInfo.riseLimit + Number(detail.currentPoint)).toFixed(2)}}
                </p>
            </li>
            <li>
                <p :class="detail.floatRate<0?'number green':'number red'">
                    <span class="title">昨收</span>
                    {{Number(detail.floatRate).toFixed(2)}}%
                </p>
            </li> -->
            <!-- <li>
                <p class="green">
                    <span class="title green">跌停限制</span>
                    {{(detail.currentPoint - Number(detail.currentPoint) * settingInfo.downLimit ).toFixed(2)}}
                </p>
            </li> -->
          </ul>
        </div>
      </div>
      <!-- <div class="clearfix">
          <div class="col-xs-4 red">漲停限制 </div>
          <div class="col-xs-4 green">跌停限制 </div>
      </div> -->
    </div>
    <div v-if="false" class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>Stock Details
      </div>
      <div class="tab-con">
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.indexName}}
          </li>
          <li :class="detail.floatPoint < 0?'pull-left green':detail.floatPoint == 0?'pull-left':'pull-left red'">
            Current price:
            <span>{{Number(detail.currentPoint).toFixed(2)}}</span>
          </li>
        </ul>
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.indexCode}}
            <!-- <span>(Scrb1905)</span> -->
          </li>
          <li :class="detail.floatRate < 0?'pull-left green':detail.floatRate == 0?'pull-left':'pull-left red'">
            <!-- 當前價： -->
            <span>{{detail.floatRate}}%</span>
          </li>
        </ul>
        <!-- <div class='buy-price clearfix'>
            <div class="col-xs-4">
                <p>市價買入</p>
            </div>
            <div class="col-xs-4">
                <p class="red">{{detail.nowPrice}}</p>
            </div>
            <div class="col-xs-4">
                <p>說明</p>
            </div>
        </div> -->
      </div>
    </div>
    <div class="box-tab">
      <!-- <div class="tab-title">
        <span class="circle"></span>選擇手數
      </div> -->
      <div class="tab-con">
        <!-- <ul class="radio-group clearfix">
          <li v-for="item in numberList" :key="item.key" @click="selectNumberFun(item.value)">
            <div :class="selectNumber == item.value?'on':''">
              {{item.label}}
            </div>
          </li>
          <li class="auto" v-show="!selectNumber">
            <input @keyup="changeAutoNumber" v-model="autoNumber" type="text">手
          </li>
        </ul> -->
        <p class="clearfix">
          <span class="pull-left">Minimum purchase {{Number(detail.minNum)}} share</span>
          <span class="protem pull-right">Maximum available for purchase {{Number(detail.maxNum)}} share</span>
        </p>
        <!-- <p class="clearfix">
          <span class="pull-left">1手 = {{detail.futuresStandard}}{{detail.futuresUnit}}</span>
          <span class="pull-right">1標准手為{{detail.depositAmt}}{{detail.futuresCode}}/{{detail.futuresUnit}}</span>
        </p> -->
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>buying and selling direction
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li v-for="item in type" :key="item.key" @click="selectTypeFun(item.value)">
            <div :class="selectType == item.value?'on':''">
              {{item.label}}
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>Choose leverage
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li v-for="item in siteLeverList" :key="item.key" @click="selectCycleFun(item.value)">
            <div :class="selectCycle == item.value?'on':''">
              {{item.label}}
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-con">
        <p class="text-left page-part">
          <span class="">{{selectNumber?selectNumber*detail.futuresStandard:autoNumber*detail.futuresStandard}}{{detail.futuresUnit}}</span>
          <span class="pull-right">Maximum purchase amount:{{(settingInfo.buyMaxPercent * $store.state.userInfo.enableFuturesAmt).toFixed(2)}}</span>
        </p>
        <p class="clearfix">
          <!-- <span class="pull-right">最小購買金額{{settingInfo.buyMinAmt}}USD</span> -->
          <span class="pull-left protem">Payable :{{total?total:0}} {{detail.coinCode}} ≈ {{USDTotal?USDTotal:0}} USD</span>
        </p>

      </div>
    </div>
    <div class="agree text-center">
      <!-- <p style="line-height: 0.4rem;padding: 0 0.2rem;">
          當該期貨漲幅達到<span class="red">漲停限制</span>時,不能bullish；達到<span class="green">跌停限制</span>時，不能bearish.
      </p> -->
      <!-- <p>
          <i @click="isAgree" :class="agree?'glyphicon glyphicon glyphicon-ok-sign red':'glyphicon glyphicon-ok-circle'"></i>
          我已閱讀併同意
          <a @click="totrageUrl" >《期貨交易交易⻛險揭示書》</a>
      </p> -->
    </div>
    <div class="footer-btn">
      <div class="total">
        <p class="pay">Pay Margin <span class="protem">{{USDTotal?USDTotal:0}}</span></p>
        <p class="account">(Futures Balance:{{$store.state.userInfo.enableFuturesAmt}}USD)</p>
      </div>
      <mt-button :disabled='buying' class="btn-red" size="small" type="danger" @click="toInquiry">place an order</mt-button>
    </div>
    <foot></foot>
  </div>
</template>

<script>
import foot from '../../components/foot/foot'
import { Toast } from 'mint-ui'
import { isNull } from '@/utils/utils'
import * as api from '@/axios/api'

export default {
  components: {
    foot
  },
  props: {},
  data () {
    return {
      detail: {
        addTime: 1564227253000,
        coinCode: 'USD',
        depositAmt: 1000,
        futuresCode: 'XAU',
        futuresGid: 'hf_XAU',
        futuresName: '倫敦金',
        futuresStandard: 100,
        futuresUnit: '盎司',
        homeShow: 1,
        id: 1,
        listShow: 1,
        maxNum: 10000,
        minNum: 1,
        nowPrice: '1428.95',
        tdesc: null,
        transAmBegin: '1',
        transAmEnd: '12',
        transFee: 5,
        transPmBegin: '12',
        transPmEnd: '24',
        transState: 0
      }, //
      cycle: [ // 杠桿倍數
        { label: '10', value: '10' },
        { label: '20', value: '20' },
        { label: '30', value: '30' }
      ],
      selectCycle: '20',
      numberList: [
        { label: '1手', value: '1' },
        { label: '20手', value: '20' },
        { label: '30手', value: '30' },
        { label: '50手', value: '50' },
        { label: '80手', value: '80' },
        { label: '100手', value: '100' },
        { label: '自定義', value: '' }
      ],
      selectNumber: '',
      autoNumber: '',
      type: [
        { label: 'bullish', value: '0' },
        { label: 'bearish', value: '1' }
      ],
      selectType: '',
      // number:0,// 股
      // price:0,// 股價格
      // total:0, // 總價
      agree: true,
      settingInfo: {
        buyMaxNum: 1000, // 最大買入手數
        buyMinNum: 100 // 最小買入手數
      }, // 設定規則信息
      dialogShow: false,
      timer: null,
      statusDetail: {},
      exchangeNumber: '', // 匯率
      buying: false, // 下單狀態
      siteLeverList:[]
    }
  },
  watch: {},
  computed: {
    total () {
      if (this.autoNumber) {
        //   每手Margin * 手數  = 價錢
        return (this.detail.depositAmt * this.autoNumber  / this.selectCycle).toFixed(2)
      } else if (this.selectNumber) {
        return (this.detail.depositAmt * this.selectNumber  / this.selectCycle).toFixed(2)
      } else {
        return 0
      }
      // 需支付總價 = 每手Margin * 股（1手 = 100股）
    },
    USDTotal () {
      // 價錢 * 匯率 轉為 人民幣
      return (this.total * this.exchangeNumber).toFixed(2)
    },
    price () {
      if (this.autoNumber) {
        return (this.detail.eachPoint * this.autoNumber * 100).toFixed(2)
      } else if (this.selectNumber) {
        return (this.detail.eachPoint * this.selectNumber * 100).toFixed(2)
      } else {
        return 0
      }
      // 買入金額 = 每股價格 * 股（1手 = 100股）
    }
  },
  created () {
    // this.timer = setInterval(this.getDetail, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {
    this.getDetail()
    this.selectNumber = 0
    this.getSettingFuturesInfo()
    this.getSettingInfo()
    this.queryExchange()
    if (!this.$store.state.userInfo.enableAmt) {
      this.getUserInfo()
    }
    this.detail = this.$route.query.info
  },
  methods: {
    async queryExchange () {
      // 基幣匯率
      let data = await api.queryExchange({ coinCode: this.detail.coinCode })
      if (data.status === 0) {
        // 成功
        this.exchangeNumber = data.data
      } else {
        Toast(data.msg)
      }
    },
    changeAutoNumber () {
      // 自定義手數
      this.selectNumber = ''
    },
    async getSettingFuturesInfo () {
      // 網站設定信息 期貨
      let data = await api.getFuturesSetting()
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data
        // 杠桿倍數
        // this.selectCycle = data.data.siteLever
      } else {
        Toast(data.msg)
      }
    },
    async getSettingInfo () {
      // 網站設定信息
      let data = await api.getSetting()
      if (data.status === 0) {
        // 成功
        // 杠桿倍數
        this.selectCycle = data.data.siteLever
        if(this.$store.state.userInfo !== undefined && this.$store.state.userInfo !== null && this.$store.state.userInfo.phone !== '' && this.$store.state.userInfo.siteLever !== null){
            this.selectCycle = this.$store.state.userInfo.siteLever.split('/')[0]
            this.siteLeverList = []
            for (let i = 0; i < this.$store.state.userInfo.siteLever.split('/').length; i++) {
              let val = this.$store.state.userInfo.siteLever.split('/')[i]
              let item = { label: val + 'multiple', value: val }
              this.siteLeverList.push(item)
            }
          } else {
            this.selectCycle = data.data.siteLever.split('/')[0]
            this.siteLeverList = []
            for (let i = 0; i < data.data.siteLever.split('/').length; i++) {
              let val = data.data.siteLever.split('/')[i]
              let item = { label: val + 'multiple', value: val }
              this.siteLeverList.push(item)
            }
          }
      } else {
        Toast(data.msg)
      }
    },
    isAgree () {
      let i = false
      let j = true
      this.agree = this.agree ? i : j
    },
    totrageUrl () {
      this.$router.push('/trade')
    },
    async getDetail () {
      let opts = {
        futuresGid: this.$route.query.info ? this.$route.query.info.futuresGid : ''
      }
      let data = await api.querySingleMarket(opts)
      if (data.status === 0) {
        this.statusDetail = data.data
        if(this.statusDetail.lastClose === null || this.statusDetail.lastClose === ''){
          this.statusDetail.lastClose = this.statusDetail.extra1
        }
      } else {
        Toast(data.msg)
      }
    },
    selectCycleFun (value) {
      this.selectCycle = value
    },
    selectNumberFun (value) {
      this.selectNumber = value
      if (value !== 0) {
        this.autoNumber = ''
      }
    },
    selectTypeFun (value) {
      this.selectType = value
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
    async toInquiry () {
      // 判斷在不在開盤時間內
      // if(!this.canBuyStatus()){
      //     Toast('不在交易時段內！')
      //     return
      // }
      // 下單
      if (!this.$store.state.userInfo.idCard) {
        Toast('You have not verified your real name, please verify your real name before placing an order')
        this.$router.push('/authentication')
        return
      }
      if (!this.agree) {
        Toast('You need to agree to the cooperation agreement to trade!')
      }
      //  else if (isNull(this.selectNumber) && isNull(this.autoNumber)) {
      //   Toast('請選擇購買手數')
      // }
       else if (isNull(this.selectType)) {
        Toast('Please select the buying and selling direction')
      } else {
        this.buying = true
        let opts = {
          FuturesId: this.detail.id,
          buyNum: this.selectNumber ? this.selectNumber: this.autoNumber, // 單位為手
          buyType: this.selectType,
          lever: this.selectCycle ? this.selectCycle : 0
        }
        let data = await api.buyFutures(opts)
        this.buying = false
        if (data.status === 0) {
          Toast(data.data)
          this.getUserInfo()
          this.$router.push('/orderlist?index=4')
        } else {
          Toast(data.msg)
        }
      }
    },
    toDetail () {
      this.$router.push('/listdetail')
    },
    goBack () {
      this.$router.go(-1)
    },
    async getUserInfo () {
      // 獲取用戶信息
      let data = await api.getUserInfo()
      if (data.status === 0) {
        // Toast(data.msg)
        this.$store.state.userInfo = data.data
      } else {
        Toast(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  body {
    background: #fff;
  }

  .protem {
    color: #ff8000;
  }

  .agree {
    margin-top: 0.2rem;
    padding-bottom: 1rem;

    a {
      color: #428bca;
    }
  }

  .footer-btn {
    position: fixed;
    z-index: 1;
    width: 100%;
    padding-right: 0;
    bottom: 0.97rem;
    height: .89rem;
    line-height: .89rem;
    display: flex;

    .total {
      font-size: 0.26rem;
      padding-left: 0.3rem;
      flex: 3;

      .pay {
        line-height: 0.45rem;
      }

      .account {
        line-height: 0.3rem;
        font-size: 0.24rem;
        color: #999;
      }
    }

    .btn-red {
      flex: 2;
      background: #b60c0d;
      border-radius: 0;
      padding: 0;
    }
  }

  .auto {
    input {
      display: inline-block;
      width: 75%;
      border-bottom: 0.01rem solid #ddd;
    }
  }

  // bottom 7rem -> 0.97rem
  .buy-price {
    // border-top: 0.01rem solid #000000;
    padding-top: 0.15rem;

    p {
      height: 0.32rem;
      line-height: 0.32rem;
    }
  }

  .mint-popup-white {
    height: 6.5rem;
    padding: 0.25rem;

    .check-box {
      height: 5.3rem;
      line-height: 0.35rem;
      overflow: auto;

      h3 {
        margin-bottom: 0.2rem;
      }
    }

    .box-btn {
      width: 100%;
      padding-top: 0.2rem;

      .btn-red {
        width: 100%;
        height: 0.6rem;
      }
    }
  }

  .detail-part {
    .index-name {
      font-size: 0.3rem;
      padding: 0.2rem 0.3rem;

      span {
        font-size: 0.22rem;
        color: #999;
        margin-left: 0.2rem;
      }
    }

    .price {
      font-size: 0.5rem;
      padding-bottom: 0.1rem;
    }

    .gain {
      font-size: 0.24rem;
    }

    .title {
      color: #999;
      line-height: 0.36rem;
      padding-right: 0.1rem;

      &.red {
        color: #b60c0d;
      }

      &.green {
        color: #31b97e;
      }
    }

    .number {
      line-height: 0.36rem;
    }

    .price-detail {
      li {
        width: 60%;
        float: left;
        margin-bottom: 0.16rem;
        text-align: right;

        &:nth-child(odd) {
          width: 40%;
          text-align: left;
        }
      }
    }

    .detail-list {
      padding-left: 0.3rem;
    }
  }
</style>
