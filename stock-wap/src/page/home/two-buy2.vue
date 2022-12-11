<!-- <template>
  <div class="wrapper"> 
    <div class="detail-part">
      <div class="index-name">
        <p>{{detail.name}} 
          <span class="index-name_code">{{detail.code}}</span>
        </p>
      </div>
      <div class="clearfix">
        <div class="pull-left hangqin-left col-xs-4">
          <p :class="detail.floatPoint>0?'price red':detail.floatPoint<0?'green price':'price'">
            {{Number(detail.nowPrice).toFixed(2)}}</p>
          <p :class="detail.floatPoint>0?'gain red':detail.floatPoint<0?'green gain':'gain'">
            <span>{{Number(detail.floatPoint).toFixed(2)}}</span>
            <span style="margin-left: .1rem;">{{Number(detail.hcrate).toFixed(2)}}%</span>
          </p>
        </div>
        <div class="pull-right hangqin-right col-xs-8">
          <ul class="price-detail text-center">
            <li>
              <p :class="detail.floatPoint<0?'number green': 'number red'">
                <span class="title">漲跌</span>
                {{Number(detail.floatPoint).toFixed(2)}} 
              </p>
            </li>
            <li>
              <p class="number red">
                <span class="title red">漲停限制</span>
                {{(Number(detail.currentPoint) * settingInfo.riseLimit + Number(detail.currentPoint)).toFixed(2)}}
              </p>
            </li>
            <li>
              <p :class="detail.floatRate<0?'number green': 'number red'">
                <span class="title">漲幅</span>
                {{Number(detail.floatRate).toFixed(2)}}%
              </p>
            </li>
            <li>
              <p class="green">
                <span class="title green">跌停限制</span>
                {{(detail.currentPoint - Number(detail.currentPoint) * settingInfo.downLimit ).toFixed(2)}}
              </p>
            </li>
          </ul>
        </div>
      </div>
   
    </div>
    <div v-if="false" class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>股票詳情
      </div>
      <div class="tab-con">
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.indexName}}
          </li>
          <li :class="detail.floatPoint < 0?'pull-left green':detail.floatPoint == 0?'pull-left':'pull-left red'">
            當前價：
            <span>{{Number(detail.currentPoint).toFixed(2)}}</span>
          </li>
        </ul>
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.indexCode}}
          </li>
          <li :class="detail.floatRate < 0?'pull-left green':detail.floatRate == 0?'pull-left':'pull-left red'">
            <span>{{detail.floatRate}}%</span>
          </li>
        </ul>
       
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>選擇手數
        <span class="notify">最小購買股數1手，最大可購買Quantity100手  </span>
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li v-for="item in numberList" :key="item.key"
          @click="selectNumberFun(item.value)">
            <div :class="[selectNumber == item.value?'on':'']">
              {{item.label}}
            </div>
          </li>
          <li v-show="!selectNumber">
            <input @keyup="changeAutoNumber" v-model="autoNumber" type="text">手
          </li>
        </ul>
        <p class="clearfix">
          <span class="pull-left">最小購買股數{{Number(detail.minNum)}}手</span>
          <span class="protem pull-right">最大可購買Quantity{{Number(detail.maxNum)}}手</span>
        </p>
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>買賣方嚮
        <span class="notify">最大購買金額:{{settingInfo.buyMaxPercent * $store.state.userInfo.enableIndexAmt}}</span>
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
        <span class="circle"></span>選擇杠桿
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
   
    <div class="agree">
      <p style="line-height: 0.4rem;padding: 0 0.2rem;">
        當該指數漲幅達到<span class="red">漲停限制</span>時,不能bullish；達到<span class="green">跌停限制</span>時，不能bearish.
      </p>
      
    </div>
    <div class="footer-btn">
      <div class="total">
        <p class="pay">支付Margin<span class="protem">{{total?total:0}}</span></p>
        <p class="account">(帳戶餘額:{{$store.state.userInfo.enableIndexAmt}}USD)</p>
      </div>
      <div class="right-btn">
        <div class="btn-buy" @click="toInquiry">
          <img src="../../assets/ico/hangqing-btn.png" alt="" srcset="">
          兩融下單
        </div>
      </div>
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
        currentPoint: '2852.9948',
        depositAmt: 10000,
        eachPoint: 300,
        floatPoint: '0.4795',
        floatRate: '0.02',
        id: 1,
        indexCode: '000001',
        indexGid: 'sh000001',
        indexName: '上證指數',
        listShow: 1,
        maxNum: 100,
        minNum: 1,
        transFee: 200,
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
        buyMaxNum: 1000, // 最大買入股數
        buyMinNum: 100 // 最小買入股數
      }, // 設定規則信息
      dialogShow: false,
      timer: null,
      buying: false, // 點選下單
      siteLeverList:[]
    }
  },
  watch: {},
  computed: {
    total () {
      if (this.autoNumber) {
        return (this.detail.depositAmt * this.autoNumber  / this.selectCycle).toFixed(2)
      } else if (this.selectNumber) {
        return (this.detail.depositAmt * this.selectNumber / this.selectCycle).toFixed(2)
      } else {
        return 0
      }
      // 需支付總價 = 每手Margin * 股（1手 = 100股）
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
    this.getSettingIndexInfo()
    this.getSettingInfo()
    if (!this.$store.state.userInfo.enableAmt) {
      this.getUserInfo()
    }
    // this.detail = this.$route.query.info
  },
  methods: {
    changeAutoNumber () {
      // 自定義手數
      this.selectNumber = ''
    },
    async getSettingIndexInfo () {
      // 網站設定信息 指數
      let data = await api.getIndexSetting()
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data
        
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
        // console.log(this.$store.state.userInfo)
        if(this.$store.state.userInfo !== undefined && this.$store.state.userInfo !== null && this.$store.state.userInfo.phone !== '' && this.$store.state.userInfo.siteLever !== null){
            this.selectCycle = data.data.siteLever.split('/')[0]
            this.siteLeverList = []
            for (let i = 0; i < data.data.siteLever.split('/').length; i++) {
              let val = data.data.siteLever.split('/')[i]
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
    // async getDetail () {
    //   let opts = {
    //     indexCode: this.$route.query.info ? this.$route.query.info.indexGid : ''
    //   }
    //   let data = await api.getSingleIndex(opts)
    //   if (data.status === 0) {
    //     this.detail = data.data
    //   } else {
    //     Toast(data.msg)
    //   }
    // },
    async getDetail() {
      let opts = {
        code: this.$route.query.code
      }
      this.loading = true
      let data = await api.getSingleStock(opts)
      this.loading = false
      if (data.status === 0) {
        this.detail = data.data
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
        Toast('您還未實名認證,請先實名認證了再下單')
        this.$router.push('/authentication')
        return
      }
      if (!this.agree) {
        Toast('需同意合作協議才能交易!')
      } else if (isNull(this.selectNumber) && isNull(this.autoNumber)) {
        Toast('請選擇購買手數')
      } else if (isNull(this.selectType)) {
        Toast('請選擇買賣方嚮')
      } else {
        this.buying = true
        let opts = {
          indexId: this.detail.id,
          buyNum: this.selectNumber ? this.selectNumber : this.autoNumber, // 單位為手
          buyType: this.selectType,
          lever: this.selectCycle ? this.selectCycle : 0
        }
        let data = await api.indexBuy(opts)
        this.buying = false
        if (data.status === 0) {
          Toast(data.data)
          this.getUserInfo()
          this.$router.push('/orderlist?index=2')
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
  .wrapper {
    background-color: #16171d;
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
    height: 1.32rem;
    line-height: 1.32rem;
    display: flex;

    .total {
      font-size: 0.26rem;
      padding-left: 0.3rem;
      flex: 3;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .pay {
        line-height: 0.45rem;
        font-size: .32rem;
        .protem {
          margin-left: .1rem;
        }
      }

      .account {
        line-height: 0.3rem;
        font-size: 0.24rem;
        color: #999;
      }
    }

    .right-btn{
      flex: 2;
      display: flex;
      flex-direction: column;
      justify-content: center;
      .btn-buy {
        width: 2.4rem;
        height: .76rem;
        border-radius: .38rem;
        background-color: #7266BA;
        line-height: .76rem;
        text-align: center;
        font-size: .28rem;
        img{
          width: .26rem;
          height: .26rem;
          margin-right: .1rem;
        }
      }
    }

    .btn-red {
      flex: 2;
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
      font-size: 0.32rem;
      padding: 0.3rem;

      span.index-name_code {
        font-size: 0.22rem;
        color: #fff;
        margin-left: 0.2rem;
        background-image: url(../../assets/ico/zhishu.png);
        background-size: 100% 100%;
        padding:0.02rem .05rem 0.02rem 0.15rem;
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
      font-size: .24rem;
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
        margin-bottom: 0.15rem;
        margin-top: 0.15rem;
        text-align: right;
        div {
          background-color: #2D2E3B;
        }
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
  .hangqin-left {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding: 0 0.1rem 0 .3rem;
    position: relative;
    .price {
      padding-bottom: .35rem;
    }
    &:after {
      display: block;
      position: absolute;
      content: '';
      width: 2px;
      height: .77rem;
      background-color: #2E2F34;
      top: 0.2rem;
      right: 0;
    }
  }
  .hangqin-right{
    padding: 0 .3rem 0 0.1rem;
  }
  .box-tab {
    margin :0.15rem .3rem;
    width: 6.9rem;
    background-color: #1B1C25;
    border-bottom: none;
    border-radius: .1rem;
    .tab-title {
      margin-bottom: 0;
      margin-top: .14rem;
      border-bottom: 1px solid #32333B;
      font-size: .32rem;
      font-weight: bold;
      padding-top: .12rem;
      padding-bottom: .12rem;
      height: auto;
      .notify {
        font-size: .24rem;
        color: #fff8;
      }
      &:after {
        background: #138EB4;
      }
    }
  }
  .radio-group li div {
    background-color: #2D2E3B;
    border-radius: .03rem;
    border: none;
  }
  .radio-group li div.on {
    background-color: #E00101;
  }
  .radio-group li input {
    background-color: #2D2E3B;
    border-radius: .03rem;
    border: none;
    width: 1rem;
    margin-right: .15rem;
    padding: 0 0.2rem;
  }
  .agree {
    font-size: .24rem;
    padding-bottom: 1.32rem;
  }
  .red-theme {
    .wrapper {
      background-color: #E9E9E9;
    }
    .detail-part .index-name{
      color: #222222;
    }
    .detail-part .index-name span.index-name_code {
      background-image: url(../../assets/ico/zhishu-red.png);
    }
    .hangqin-left .price {
      color: #E00202;
      font-family: lightnumber;
    }
    .hangqin-left:after {
      background-color: transparent;
    }
    .detail-part .title {
      color: #000;
    }
    .box-tab {
      background-color: #fff;
    }
    .box-tab .tab-title {
      color: #000;
    }
    .box-tab .tab-title:after {
      background-color: #000;
    }
    .box-tab .tab-title .notify {
      color: #000;
    }
    .radio-group li div {
      background-color: #D9D9D9;
      border: 1px solid #AEAEAE;
      color: #222222;
    }
    .radio-group li div.on {
      background-color: #E00202;
      border: 1px solid #E00202;
      color: #FFFFFF;
    }
    .box-tab .tab-title {
      border-bottom-color: #E9E9E9;
    }
    .agree {
      color: #000;
    }
    .footer-btn {
      background-color: #E0E0E0;
    }
    .footer-btn .total .pay{
      color: #000;
    }
    .footer-btn .total .pay .protem{
      color: #E00202;
      font-family: lightnumber;
    }
    .footer-btn .right-btn .btn-buy {
      background-color: #E00202;
    }
  }
</style> -->
