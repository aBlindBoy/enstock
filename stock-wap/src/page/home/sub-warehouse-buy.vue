<!-- <template>
  <div class="wrapper"> 
    <div class="detail-part">
      <div class="index-name">
        <p>{{detail.name}} <span class="index-name_code">{{detail.code}}</span></p>
      </div>
      <div class="clearfix">
        <div class="pull-left hangqin-left col-xs-4">
          <p :class="detail.hcrate>0?'price red':detail.hcrate<0?'green price':'price'">
            {{Number(detail.nowPrice).toFixed(2)}}</p>
          <p :class="detail.hcrate>0?'gain red':detail.hcrate<0?'green gain':'gain'">
            <span>{{Number(detail.hcrate).toFixed(2)}}</span>
            <span style="margin-left: .1rem;">{{Number(detail.hcrate).toFixed(2)}}%</span>
          </p>
        </div>
        <div class="pull-right hangqin-right col-xs-8">
          <ul class="price-detail text-center">
            <li>
              <p :class="detail.hcrate<0?'number green': 'number red'">
                <span class="title">Chg</span>
                {{Number(detail.hcrate).toFixed(2)}} 
              </p>
            </li>
            <li>
              <p class="number red">
                <span class="title red">Chg limit</span>
                {{(Number(detail.nowPrice) * settingIndexInfo.riseLimit + Number(detail.nowPrice)).toFixed(2)}}
              </p>
            </li>
            <li>
              <p :class="detail.hcrate<0?'number green': 'number red'">
                <span class="title">Chg %</span>
                {{Number(detail.hcrate).toFixed(2)}}%
              </p>
            </li>
            <li>
              <p class="green">
                <span class="title green">Limit down</span>
                {{(detail.nowPrice - Number(detail.nowPrice) * settingIndexInfo.downLimit ).toFixed(2)}}
              </p>
            </li>
          </ul>
        </div>
      </div>

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
          <li :class="detail.hcrate < 0?'pull-left green':detail.hcrate == 0?'pull-left':'pull-left red'">
            Current price:
            <span>{{Number(detail.nowPrice).toFixed(2)}}</span>
          </li>
        </ul>
        <ul class="first clearfix">
          <li class="pull-left">
            {{detail.indexCode}}
          </li>
          <li :class="detail.hcrate < 0?'pull-left green':detail.hcrate == 0?'pull-left':'pull-left red'">
            <span>{{detail.hcrate}}%</span>
          </li>
        </ul>
    
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title special">
        <div class="notify">Minimum number of shares to buy {{settingInfo.buyMinNum}} ???Maximum Buyable Quantity{{settingInfo.buyMaxNum}} </div>
      </div>
      <div class="tab-con">
      
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
        <span class="circle"></span>buying and selling direction
        <span class="notify">Maximum purchase amount:{{(settingInfo.buyMaxAmtPercent * $store.state.userInfo.enableAmt).toFixed(2)}}</span>
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
      <div class="tab-title">
        <span class="circle"></span>sub account
      </div>
      <select class="set-account" v-model="subaccountNumber" title="Click to select sub account" slot="prepend" placeholder="please choose">
        <option v-for="i in subaccountList" :key="i.subaccountNumber" :label="i.subaccountNumber" :value="i.subaccountNumber">{{i.subaccountNumber}}</option>
      </select>
    </div>
  
    <div class="agree">
      <p style="line-height: 0.4rem;padding: 0 0.2rem;">
        When the index rises to <span class="red">daily limit </span>, cannotbullish;achieve<span class="green">Limit down</span>,Can't bearish.
      </p>
     
    </div>
    <div class="footer-btn">
      <div class="total">
        <p class="pay">Pay Margin<span class="protem">{{total?total:0}}</span></p>
        <p class="account">(account balance:{{$store.state.userInfo.enableIndexAmt}}USD)</p>
      </div>
      <div class="right-btn">
        <div class="btn-buy" @click="toInquiry">
          <img src="../../../static/img/detail/fencang-icon.png" alt="" srcset="">
          Place an order in a separate warehouse
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
        name: '',
        code: '',
        gid: '',
        stockType: ''
      }, //
      cycle: [ // ????????????
        { label: '10', value: '10' },
        { label: '20', value: '20' },
        { label: '30', value: '30' }
      ],
      selectCycle: '20',
      numberList: [
          { label: "1???", value: "1" },
          { label: "5???", value: "5" },
          { label: "10???", value: "10" },
          { label: "25???", value: "25" },
          { label: "50???", value: "50" },
          { label: "100???", value: "100" },
          { label: "?????????", value: "" }
      ],
      siteLeverList:[],
      selectNumber: '',
      autoNumber: '',
      type: [
        { label: 'bullish', value: '0' },
        { label: 'bearish', value: '1' }
      ],
      selectType: '',
      // number:0,// ???
      // price:0,// ?????????
      // total:0, // ??????
      agree: true,
      settingInfo: {
        buyMaxNum: 1000, // ??????????????????
        buyMinNum: 100 // ??????????????????
      }, // ??????????????????
      settingIndexInfo:{
        riseLimit:0.1,
        downLimit:0.1
      },
      
      dialogShow: false,
      timer: null,
      buying: false,
      focePromptPopup: false, // ?????????????????????
      settingSpreadRate: {spreadRate: 0},
      subaccountList:[1,2],//sub account??????
      subaccountNumber:'' //sub account
    }
  },
  watch: {},
  computed: {
    poundage () { //?????????= ???????????????+Stamp duty+Spread fee
      if (this.autoNumber) {
        let payfee = (this.detail.nowPrice * this.autoNumber * 1000).toFixed(2) // / this.selectCycle
        return ((payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
      } else if (this.selectNumber) {
        let payfee = (this.detail.nowPrice * this.selectNumber * 1000).toFixed(2)
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
        let payfee = (this.detail.nowPrice * this.autoNumber * 1000 / this.selectCycle)
        return (payfee + (payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
        //return (this.detail.nowPrice * this.autoNumber * 100 / this.selectCycle).toFixed(2)
      } else if (this.selectNumber) {
        // alert("bb"+this.detail.nowPrice+"cc==="+this.selectNumber+"ff==="+this.selectCycle+"==="+this.settingSpreadRate.spreadRate)
        let payfee = (this.detail.nowPrice * this.selectNumber * 1000 / this.selectCycle)
        return (payfee + (payfee * this.settingInfo.buyFee) + (payfee * this.settingInfo.dutyFee) + (payfee * this.settingSpreadRate.spreadRate)).toFixed(2)
        //return (this.detail.nowPrice * this.selectNumber * 100 / this.selectCycle).toFixed(2)
      } else {
        return 0
      }
      // ??????????????? = Current price * ??????1??? = 100??????/ ????????????
    },
    price () {
      if (this.autoNumber) {
        return (this.detail.nowPrice * this.autoNumber * 1000).toFixed(2)
      } else if (this.selectNumber) {
        return (this.detail.nowPrice * this.selectNumber * 1000).toFixed(2)
      } else {
        return 0
      }
      // ????????? = Current price * ??????1??? = 100??????
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
      this.getUserSubaccount()

    if (!this.$store.state.userInfo.enableAmt) {
      this.getUserInfo()
    }
    // this.detail = this.$route.query.info
  },
  methods: {
    changeAutoNumber () {
      // ???????????????
      this.selectNumber = ''
    },
    async getSettingIndexInfo () {
      // ?????????????????? ??????
      let data = await api.getIndexSetting()
      if (data.status === 0) {
        // ??????
        this.settingIndexInfo = data.data
        
      } else {
        Toast(data.msg)
      }
    },
    async getSettingInfo () {
      // ??????????????????
      let data = await api.getSetting()
      if (data.status === 0) {
        this.settingInfo = data.data

        // ??????
        // ????????????
        this.selectCycle = data.data.siteLever
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
        // this.numberList = []
        // for (let i = 0; i < 10; i++) {
        //   if (i === 0 || i % 2 === 1) {
        //     let val = data.data.buyMinNum * i + data.data.buyMinNum
        //     let item = { label: val / 100 + '???', value: val / 100 }
        //     this.numberList.push(item)
        //   }
        // }
        // this.numberList.push({ label: '?????????', value: '' })
      } else {
        Toast(data.msg)
      }
    },
    async findSpreadRateOne () {
      // ??????Spread fee???
      let opts = {
        applies: this.detail.hcrate, // Quote change
        turnover: this.total, //?????????
        unitprice: this.detail.nowPrice, //????????????
        code: this.$route.params.code
      }
      let data = await api.findSpreadRateOne(opts)
      if (data.status === 0) {
        // ??????
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
       async getUserSubaccount () {
        // ??????In operationsub account
        let result = await api.getUserSubaccount()
        if (result.status === 0) {
          if(result.data.list.length>0){
            this.subaccountList = result.data.list
            if(this.$route.query.sub != undefined && this.$route.query.sub != ''){
              this.form.subaccountNumber = this.$route.query.sub
            } else {
              this.form.subaccountNumber = this.subaccountList[0].subaccountNumber
            } 
          } else {
            Toast('Sub account not obtained???')
          }
        } else {
          Toast(result.msg)
        }
      },
    async getDetail() {
      let opts = {
        code: this.$route.query.code
      };

      let [res1, res2] = await Promise.all([
        api.getTwStockData(opts.code),
        api.getTwStockExchange(opts.code)
      ]);

      let data = {};
      let data1 = res1.data[0];
      let data2 = res2.data[0]["??????"];
      data.name = data1["????????????"];
      data.code = opts.code;
      data.spell = "";
      data.gid = opts.code;
      data.nowPrice = data1["???????????????"];
      data.hcrate = data1["Quote change"];
      data.today_max = data1["?????????"];
      data.today_min = data1["?????????"];
      data.business_balance = data1["????????????"];
      data.business_amount = data1["???????????????"];
      data.preclose_px =
        parseFloat(data1["?????????"]) + parseFloat(data1["??????"]);
      data.open_px = data1["?????????"];
      data.buy1 = data2["??????1"].substring(1).replace(/\s+/g, "");
      data.buy2 = data2["??????2"].substring(1).replace(/\s+/g, "");
      data.buy3 = data2["??????3"].substring(1).replace(/\s+/g, "");
      data.buy4 = data2["??????4"].substring(1).replace(/\s+/g, "");
      data.buy5 = data2["??????5"].substring(1).replace(/\s+/g, "");
      data.sell1 = data2["Selling price1"].substring(1).replace(/\s+/g, "");
      data.sell2 = data2["Selling price2"].substring(1).replace(/\s+/g, "");
      data.sell3 = data2["Selling price3"].substring(1).replace(/\s+/g, "");
      data.sell4 = data2["Selling price4"].substring(1).replace(/\s+/g, "");
      data.sell5 = data2["Selling price5"].substring(1).replace(/\s+/g, "");
      data.buy1_num = data2["??????1"];
      data.buy2_num = data2["??????2"];
      data.buy3_num = data2["??????3"];
      data.buy4_num = data2["??????4"];
      data.buy5_num = data2["??????5"];
      data.sell1_num = data2["??????1"];
      data.sell2_num = data2["??????2"];
      data.sell3_num = data2["??????3"];
      data.sell4_num = data2["??????4"];
      data.sell5_num = data2["??????5"];

      let code = data.code;
      this.ucode = code;

      this.detail = data;
      this.findSpreadRateOne();
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
      let day = dataTime.getDay() // ?????????
      let hour = dataTime.getHours() // ??????
      let minute = dataTime.getMinutes() // ??????
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
    async toInquiry() {
      // ??????????????????????????????
      // if(!this.canBuyStatus()){
      //     Toast('????????????????????????')
      //     return
      // }
      // ??????

      if (!this.$store.state.userInfo.idCard) {
        Toast("?????????????????????,??????????????????????????????");
        this.$router.push("/authentication");
        return;
      }
      if (!this.agree) {
        Toast("?????????????????????????????????!");
      } else if (isNull(this.selectNumber) && isNull(this.autoNumber)) {
        Toast("?????????????????????");
      } else if (isNull(this.selectType)) {
        Toast("?????????????????????");
      } else {
        this.buying = true;
        var gCode = this.$route.query;
        let twRes = await api.getTwStockData(gCode.code);
        let nowPrice = twRes.data[0]["???????????????"];
        let hcrate = twRes.data[0]["Quote change"];
        let preClose = twRes.data[0]["?????????"];
        let opts = {
          stockId: parseInt(gCode.code),
          buyNum: this.selectNumber
            ? this.selectNumber * 1000
            : this.autoNumber * 1000, // ????????????
          buyType: this.selectType,
          lever: this.selectCycle ? this.selectCycle : 0,
          nowPrice,
          hcrate,
          preClose
        };
        let data = await api.buyTwStock(opts);
        this.buying = false;
        if (data.status === 0) {
          Toast(data.data);
          this.getUserInfo();
          this.$router.push("/orderlist?index=2");
        } else {
          Toast(data.msg);
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
      // ??????????????????
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
        background-color: #148EB4;
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
      &.special::after{
        top:0.4rem
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
  .set-account {
    padding: .1rem 0;
    width: 100%;
    outline: none;
    background-color: #16161d;
  }
  .red-theme {
    .set-account {
    background-color: #fff;
    }
    .wrapper {
      background-color: #E9E9E9;
    }
    .detail-part .index-name {
      color: #222;
    }
    .detail-part .index-name span.index-name_code {
      background-image: url(../../assets/ico/zhishu-red.png);
    }
    .hangqin-left:after {
      background-color: transparent;
    }
    .box-tab {
      background-color: #fff;
    }
    .box-tab .tab-title {
      border-bottom-color: #E9E9E9;
      color: #222;
    }
    .box-tab .tab-title:after {
      background-color: #222;
    }
    .box-tab .tab-title .notify {
      color: #0008;
    }
    .agree {
      color: #222;
    }
    .radio-group li div {
      background-color: #D9D9D9;
      border: 1px solid #AEAEAE;
      color: #222;
    }
    .radio-group li div.on {
      background-color: #E00202;
      border: 1px solid #E00202;
      color: #fff;
    }
    .footer-btn {
      background-color: #E0E0E0;
      
    }
    .footer-btn .total .pay {
      color: #222;
    }
    .footer-btn .total .pay .protem {
      color: #E00101;
      font-family: lightnumber;
    }
    .footer-btn .right-btn .btn-buy{
      background-color: #E00101;
    }
  }
</style> -->
