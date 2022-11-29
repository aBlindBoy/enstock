<template>
  <div class="wrapper">
    <!-- <div class="header">
      <mt-header title="美股交易">
        <router-link :to="'/listdetail?code='+detail.code+'&stock_type='+detail.stockType" slot="left">
          <mt-button icon="back">詳情</mt-button>
        </router-link> -->
        <!-- <mt-button icon="more" slot="right"></mt-button> -->
      <!-- </mt-header>
    </div> -->

    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>Stock Details
      </div>
      <div class="tab-con">
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.name}}
          </li>
          <li :class="detail.hcrate < 0?'pull-left green':detail.hcrate == 0?'pull-left':'pull-left red'">
            Current price:
            <span>{{detail.nowPrice}}</span>
          </li>
        </ul>
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.code}}
            <!-- <span>(Scrb1905)</span> -->
          </li>
          <li :class="detail.hcrate < 0?'pull-left green':detail.hcrate == 0?'pull-left':'pull-left red'">
            <!-- 當前價： -->
            <span>{{detail.hcrate}}%</span>
          </li>
          <li class="last pull-right">
            <mt-button class="btn-red" @click="goBack" size="small" type="danger">Quotes</mt-button>
          </li>
        </ul>
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
          <span class="pull-left">Minimum purchase {{Number(settingInfo.buyMinNum)}} share</span>
          <span class="protem pull-right">Maximum available for purchase {{Number(settingInfo.buyMaxNum)}} share</span>
        </p>
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
    <!-- <div v-show="false" class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>杠桿倍數
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li v-for="item in cycle" :key="item.key" @click="selectCycleFun(item.value)">
            <div :class="selectCycle == item.value?'on':''">
              {{item.label}}
            </div>
          </li>
        </ul>
      </div>
    </div> -->
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
      <div class="tab-con">
        <p class="text-left page-part">
          <span class="">{{selectNumber?selectNumber*100:autoNumber*100}} share</span>
          <span class="pull-right">market value {{price?price:0}} USD</span>
        </p>
        <p class="text-left page-part">
          <span class="">Margin {{(price/ selectCycle).toFixed(2)}}</span>
          <span class="pull-right">Minimum purchase amount {{settingInfo.buyMinAmt}}USD</span>
        </p>
        <p class="text-left page-part">
          <span class="">total handling fee {{poundage?poundage:0}}USD <i @click="focePromptPopup = true" ref="button" class="iconfont icon-xinshou"></i></span>
          <span class="pull-right"></span>
        </p>
      </div>
    </div>
    <div class="agree text-center">
      <p>
        <i @click="isAgree"
           :class="agree?'glyphicon glyphicon glyphicon-ok-sign red':'glyphicon glyphicon-ok-circle'"></i>
           I have read and agree
        <!-- <a @click="dialogShow = true">《合作協議》</a> -->
        <a @click="totrageUrl">《{{settingInfo.tradeAgreeTitle}}》</a>
      </p>
    </div>
    <div class="footer-btn">
      <div class="total">
        <p class="pay">need to pay <span class="protem">{{total?total:0}}</span></p>
        <p class="account">(account balance :{{$store.state.userInfo.enableAmt}}USD)</p>
      </div>
      <mt-button :disabled="buying" class="btn-red" size="small" type="danger" @click="toInquiry">place an order</mt-button>
    </div>
    
    <mt-popup v-model="focePromptPopup" popup-transition="popup-fade" class="mint-popup-white">
      <div class="clearfix">
        <a @click="focePromptPopup = false" class="pull-right"><i class="iconfont icon-weitongguo"></i></a>
      </div>
      <p class="font-title">Calculation rules for total handling fee:</p>
      <p class="font-bold">Total Fee = Buying Fee（{{(settingInfo.buyFee*100)}}%）+ Stamp duty（{{(settingInfo.dutyFee*100)}}%）+ Spread fee（{{(settingSpreadRate.spreadRate*100)}}%）
      </p>
    </mt-popup>
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
        name: '',
        code: '',
        gid: '',
        stockType: ''
      }, //
      cycle: [ // 杠桿倍數
        { label: '10', value: '10' },
        { label: '20', value: '20' },
        { label: '30', value: '30' }
      ],
      selectCycle: '20',
      numberList: [
        { label: '50手', value: '50' },
        { label: '100手', value: '100' },
        { label: '150手', value: '150' },
        { label: '200手', value: '200' },
        { label: '250手', value: '250' },
        { label: '300手', value: '300' },
        { label: '自定義', value: '' }
      ],
      siteLeverList:[],
      selectNumber: '',
      autoNumber: '',
      type: [
        { label: 'Bullish', value: '0' },
        { label: 'Bearish', value: '1' }
      ],
      selectType: '',
      // number:0,// 股
      // price:0,// 股價格
      // total:0, // 總價
      agree: true,
      settingInfo: {
        buyMaxNum: 1000, // 最大買入股數
        buyMinNum: 100 // 最小買入股數
      }, // 設定規則信息
      dialogShow: false,
      timer: null,
      buying: false,
      focePromptPopup: false, // 總手續費提示框
      settingSpreadRate: {spreadRate: 0}
    }
  },
  watch: {},
  computed: {
    poundage () { //手續費= 買入手續費+Stamp duty+Spread fee
      if (this.autoNumber) {
        let payfee = (this.detail.nowPrice * this.autoNumber * 100).toFixed(2) // / this.selectCycle
        return ((payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
      } else if (this.selectNumber) {
        let payfee = (this.detail.nowPrice * this.selectNumber * 100).toFixed(2)
        return ((payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
      } else {
        return 0
      }
    },
    total () {
      if (this.settingSpreadRate == undefined || this.settingSpreadRate.spreadRate == undefined){
        this.settingSpreadRate.spreadRate = 0
      }
      if (this.autoNumber) {
        let payfee = (this.detail.nowPrice * this.autoNumber * 100 / this.selectCycle)
        return (payfee + (payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
        //return (this.detail.nowPrice * this.autoNumber * 100 / this.selectCycle).toFixed(2)
      } else if (this.selectNumber) {
        // alert("bb"+this.detail.nowPrice+"cc==="+this.selectNumber+"ff==="+this.selectCycle+"==="+this.settingSpreadRate.spreadRate)
        let payfee = (this.detail.nowPrice * this.selectNumber * 100 / this.selectCycle)
        return (payfee + (payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
        //return (this.detail.nowPrice * this.selectNumber * 100 / this.selectCycle).toFixed(2)
      } else {
        return 0
      }
      // 需支付總價 = Current price * 股（1手 = 100股）/ 杠桿倍數
    },
    price () {
      if (this.autoNumber) {
        return (this.detail.nowPrice * this.autoNumber * 100).toFixed(2)
      } else if (this.selectNumber) {
        return (this.detail.nowPrice * this.selectNumber * 100).toFixed(2)
      } else {
        return 0
      }
      // 市值價 = Current price * 股（1手 = 100股）
    }
  },
  created () {
    this.timer = setInterval(this.getDetail, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {
    //   this.getDetail()
    this.detail.gid = this.$route.params.gid
    this.detail.name = this.$route.params.name
    this.detail.code = this.$route.params.code
    this.detail.stockType = this.$route.params.stockType
    this.detail.hcrate = this.$route.params.hcrate
    this.detail.nowPrice = this.$route.params.nowPrice
    this.selectNumber = 0
    //   this.selectCycle = 10
    this.getSettingInfo()
    if (!this.$store.state.userInfo.enableAmt) {
      this.getUserInfo()
    }
  },
  methods: {
    changeAutoNumber () {
      // 自定義手數
      this.selectNumber = ''
    },
    async getSettingInfo () {
      // 網站設定信息
      let data = await api.getSetting()
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data
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
    async findSpreadRateOne () {
      // 查詢Spread fee率
      let opts = {
        applies: this.detail.hcrate, // Quote change
        turnover: this.total, //成交額
        unitprice: this.detail.nowPrice, //股票單價
        code: this.$route.params.code
      }
      let data = await api.findSpreadRateOne(opts)
      if (data.status === 0) {
        // 成功
        if(data.data != undefined){
          this.settingSpreadRate = data.data
        }
        
      } else {
        this.$message.error(data.msg)
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
        code: this.$route.params.code
      }
      let data = await api.getSingleStock(opts)
      if (data.status === 0) {
        this.detail = data.data
        this.findSpreadRateOne()
      } else {
        Toast(data.msg)
      }
    },
    selectCycleFun (value) {
      this.selectCycle = value
    },
    selectNumberFun (value) {
      this.selectNumber = value
      if (+value !== 0) {
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
      if (+day === 6 || +day === 7) {
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
      // else if (isNull(this.selectNumber) && isNull(this.autoNumber)) {
      //   Toast('請選擇購買手數')
      // }
       else if (isNull(this.selectType)) {
        Toast('Please select the buying and selling direction')
      } else {
        this.buying = true
        let opts = {
          stockId: this.$route.params.gid,
          buyNum: this.selectNumber ? this.selectNumber * 100 : this.autoNumber * 100,
          buyType: this.selectType,
          lever: this.selectCycle
        }
        let data = await api.buy(opts)
        this.buying = false
        if (+data.status === 0) {
          Toast(data.data)
          this.getUserInfo()
          this.$router.push('/orderlist?index=1')
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
      if (+data.status === 0) {
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

</style>
