<template>
  <div class="wrapper">
    <div class="detail-part">
      <div class="index-name">
        <p>
          {{ detail.name }}
          <span class="index-name_code">{{ detail.code }}</span>
        </p>
      </div>
      <div class="clearfix">
        <div class="pull-left hangqin-left col-xs-4" style="display: flex; align-items: center; line-height: 100%;">
          <p
            style="line-height: 71px;"
            :class="
              detail.hcrate > 0
                ? 'price red'
                : detail.hcrate < 0
                ? 'green price'
                : 'price'
            "
          >
            {{ Number(detail.nowPrice).toFixed(2) }}
          </p>
          <!-- <p
            :class="
              detail.hcrate > 0
                ? 'gain red'
                : detail.hcrate < 0
                ? 'green gain'
                : 'gain'
            "
          >
            <span>{{
              Number(detail.hcrate * detail.nowPrice).toFixed(2)
            }}</span>
            <span style="margin-left: .1rem;"
              >{{ Number(detail.hcrate).toFixed(2) }}%</span
            >
          </p> -->
        </div>
        <div class="pull-right hangqin-right col-xs-8">
          <ul class="price-detail text-center">
            <li>
              <!-- <p class="title"></p> -->
              <p :class="detail.hcrate < 0 ? 'number green' : 'number red'">
                <span class="title">{{$t('common.chgPirce')}}</span>
                {{ Number(detail.hcrate * detail.nowPrice).toFixed(2) }}
              </p>
            </li>
            <li>
              <p class="number red">
                <span class="title red">{{$t('common.highest')}}</span>
                {{
                  (
                    Number(detail.nowPrice) * settingIndexInfo.riseLimit +
                    Number(detail.nowPrice)
                  ).toFixed(2)
                }}
              </p>
            </li>
            <li>
              <p :class="detail.hcrate < 0 ? 'number green' : 'number red'">
                <span class="title">{{$t('common.chgRate')}}</span>
                {{ Number(detail.hcrate).toFixed(2) }}%
              </p>
            </li>
            <li>
              <!-- <p class="title">最低</p> -->
              <p class="green">
                <span class="title green">{{$t('common.lowest')}} </span>
                {{
                  (
                    detail.nowPrice -
                    Number(detail.nowPrice) * settingIndexInfo.downLimit
                  ).toFixed(2)
                }}
              </p>
            </li>
          </ul>
        </div>
      </div>
      <!-- <div class="clearfix">
          <div class="col-xs-4 red">漲停限制 </div>
          <div class="col-xs-4 green">跌停限制 </div>
      </div> -->
    </div>
    <!-- <div v-if="false" class="box-tab">
      <div class="tab-title"><span class="circle"></span>Stock Details</div>
      <div class="tab-con">
        <ul class="first clearfix">
          <li class="pull-left">
            {{ detail.indexName }}
          </li>
          <li
            :class="
              detail.hcrate < 0
                ? 'pull-left green'
                : detail.hcrate == 0
                ? 'pull-left'
                : 'pull-left red'
            "
          >
          Current price:
            <span>{{ Number(detail.currentPoint).toFixed(2) }}</span>
          </li>
        </ul>
        <ul class="first clearfix">
          <li class="pull-left">
            {{ detail.indexCode }}
          </li>
          <li
            :class="
              detail.hcrate < 0
                ? 'pull-left green'
                : detail.hcrate == 0
                ? 'pull-left'
                : 'pull-left red'
            "
          >
            <span>{{ detail.hcrate }}%</span>
          </li>
        </ul>
      </div>
    </div> -->
    <div class="box-tab">
      <div class="tab-title special">
        <!-- <div class="circle"></div>選擇張數 -->
        <div class="notify">
          <p>{{$t('trade.minBuy')}}  {{settingInfo.buyMinNum}}</p>
          <p>{{$t('trade.maxBuy')}}  {{settingInfo.buyMaxNum}}</p>
           </div>
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li v-if="marketType=='us'"
            v-for="item in usNumberList"
            :key="item.key"
            @click="selectNumberFun(item.value)"
          >
            <div :class="[selectNumber == item.value ? 'on' : '']">
              {{ item.label }}
            </div>
          </li>
          <li v-if="marketType=='tw'"
            v-for="item in twNumberList"
            :key="item.key"
            @click="selectNumberFun(item.value)"
          >
            <div :class="[selectNumber == item.value ? 'on' : '']">
              {{ item.label }}
            </div>
          </li>
          <li v-show="!selectNumber">
            <input
              style="width: 100%;"
              @keyup="changeAutoNumber"
              v-model="autoNumber"
              type="number"
            />
            <!-- share -->
          </li>
        </ul>
        <!-- <p class="clearfix">
          <span class="pull-left"
            >最小購買股數{{ Number(settingInfo.buyMinNum) }}手</span
          >
          <span class="protem pull-right"
            >最大可購買Quantity{{ Number(settingInfo.buyMaxNum) }}手</span
          >
        </p> -->
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title">
       
        <p> <span class="circle"></span>{{$t('trade.directionDesc')}}</p>
        <span class="notify"
          >{{$t('trade.maximumPurchaseAmount')}}:{{
            (
              settingInfo.buyMaxAmtPercent * $store.state.userInfo.enableAmt
            ).toFixed(2)
          }}</span
        >
      </div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li
            v-for="item in type"
            :key="item.key"
            @click="selectTypeFun(item.value)"
          >
            <div :class="selectType == item.value ? 'on' : ''">
              {{ item.label }}
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="box-tab">
      <div class="tab-title"><span class="circle"></span>{{$t('trade.chooseLeverage')}}</div>
      <div class="tab-con">
        <ul class="radio-group clearfix">
          <li
            v-for="item in siteLeverList"
            :key="item.key"
            @click="selectCycleFun(item.value)"
          >
            <div :class="selectCycle == item.value ? 'on' : ''">
              {{ item.label }}
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!-- <div class="box-tab">
      <div class="tab-con">
       <p class="text-left page-part">
            <span class="">{{selectNumber?selectNumber*100:autoNumber*100}}股</span>
            <span class="pull-right">買入金額:{{price?price:0}}USD</span>
        </p> -->
    <!-- <p class="clearfix">
         <span class="pull-right">最小購買金額{{settingInfo.buyMinAmt}}USD</span> -->
    <!-- <span class="pull-right">最大購買金額:{{settingInfo.buyMaxPercent * $store.state.userInfo.enableIndexAmt}}</span>
        </p>

      </div>
    </div> -->
    <div class="agree">
      <p style="line-height: 0.4rem;padding: 0 0.2rem;">
        {{$t('trade.chooseLeverage')}} <span class="red"> {{$t('trade.dailyLimit')}}
         </span>, {{$t('trade.canNotBullish')}}; {{$t('trade.reach')}} <span
          class="green"
          > {{$t('trade.limitDown')}} </span
        > {{$t('trade.notBearish')}} 
      </p>
    </div>
    <div class="footer-btn">
      <div class="total">
        <p class="pay">
          {{$t('trade.payMargin')}}  <span class="protem">{{ total ? total : 0 }}</span>
        </p>
        <p v-if="marketType == 'us'" class="account" >
          ({{$t('trade.accountBalance')}}:{{ $store.state.userInfo.enableAmt }} USD)
        </p>
        <p v-else  class="account">
          ({{$t('trade.accountBalance')}}:{{ $store.state.userInfo.twEnableAmt }} TWD)
        </p>
      </div>
      <!-- <mt-button :disabled="buying" class="btn-red" size="small" type="danger" @click="toInquiry">下單</mt-button> -->
      <div class="right-btn">
        <div class="btn-buy" @click="toInquiry">
          <img src="../../assets/ico/hangqing-btn.png" alt="" srcset="" />
          {{$t('trade.trade')}}
        </div>
      </div>
    </div>

    <foot></foot>
  </div>
</template>

<script>
import foot from "../../components/foot/foot";
import { Toast } from "mint-ui";
import { isNull } from "@/utils/utils";
import * as api from "@/axios/api";

export default {
  components: {
    foot
  },
  props: {},
  data() {
    return {
      detail: {
        name: "",
        code: "",
        gid: "",
        stockType: ""
      }, //
      cycle: [
        // 杠桿倍數
        { label: "10", value: "10" },
        { label: "20", value: "20" },
        { label: "30", value: "30" }
      ],
      selectCycle: "20",
      // numberList: [
      //   { label: "100 share", value: "100" },
      //   { label: "200 share", value: "200" },
      //   { label: "500 share", value: "500" },
      //   { label: "1000 share", value: "1000" },
      //   { label: "2000 share", value: "2000" },
      //   { label: "5000 share", value: "5000" },
      //   { label: "Input", value: "" }
      // ],
      siteLeverList: [],
      selectNumber: "",
      autoNumber: "",
      type: [
        { label: "bullish", value: "0" },
        { label: "bearish", value: "1" }
      ],
      selectType: "",
      // number:0,// 股
      // price:0,// 股價格
      // total:0, // 總價
      agree: true,
      settingInfo: {
        buyMaxNum: 1000, // 最大買入股數
        buyMinNum: 100 // 最小買入股數
      }, // 設定規則信息
      settingIndexInfo: {
        riseLimit: 0.1,
        downLimit: 0.1
      },

      dialogShow: false,
      timer: null,
      buying: false,
      focePromptPopup: false, // 總手續費提示框
      settingSpreadRate: { spreadRate: 0 },
      marketType:"",
    };
  },
  created() {
    this.marketType = this.$route.query.stock_type;
    // this.timer = setInterval(this.getDetail, 5000)
  },
  watch: {},
  computed: {
    poundage() {
      //手續費= 買入手續費+Stamp duty+Spread fee
      if (this.autoNumber) {
        let payfee = 0
        if (this.marketType == "us") {
           payfee = (this.detail.nowPrice * this.autoNumber).toFixed(2); // / this.selectCycle
        }else{
           payfee = (this.detail.nowPrice * this.autoNumber*1000).toFixed(2); // / this.selectCycle
        }
        return (
          payfee * this.settingInfo.buyFee +
          payfee * this.settingInfo.dutyFee +
          payfee * this.settingSpreadRate.spreadRate
        ).toFixed(2);
      } else if (this.selectNumber) {

        let payfee = 0
        if (this.marketType == "us") {
           payfee = (this.detail.nowPrice * this.selectNumber).toFixed(2); // / this.selectCycle
        }else{
           payfee = (this.detail.nowPrice * this.selectNumber*1000).toFixed(2); // / this.selectCycle
        }

        // let payfee = (this.detail.nowPrice * this.selectNumber ).toFixed(
        //   2
        // );
        return (
          payfee * this.settingInfo.buyFee +
          payfee * this.settingInfo.dutyFee +
          payfee * this.settingSpreadRate.spreadRate
        ).toFixed(2);
      } else {
        return 0;
      }
    },
    total() {
      if (
        this.settingSpreadRate == undefined ||
        this.settingSpreadRate.spreadRate == undefined
      ) {
        this.settingSpreadRate.spreadRate = 0;
      }
      if (this.autoNumber) {
        
        let payfee = 0
        if (this.marketType == "us") {
          (this.detail.nowPrice * this.autoNumber) / this.selectCycle;
        }else{
          (this.detail.nowPrice * this.autoNumber * 1000) / this.selectCycle;
        }

        return (
          payfee +
          payfee * this.settingInfo.buyFee +
          payfee * this.settingInfo.dutyFee +
          payfee * this.settingSpreadRate.spreadRate
        ).toFixed(2);
        //return (this.detail.nowPrice * this.autoNumber * 100 / this.selectCycle).toFixed(2)
      } else if (this.selectNumber) {
        // alert("bb"+this.detail.nowPrice+"cc==="+this.selectNumber+"ff==="+this.selectCycle+"==="+this.settingSpreadRate.spreadRate)
        // let payfee =
        //   (this.detail.nowPrice * this.selectNumber) / this.selectCycle;
        let payfee = 0
        if (this.marketType == "us") {
          payfee =  (this.detail.nowPrice * this.selectNumber) / this.selectCycle;
        }else{
          payfee =  (this.detail.nowPrice * this.selectNumber * 1000) / this.selectCycle;
        }

        return (
          payfee +
          payfee * this.settingInfo.buyFee +
          payfee * this.settingInfo.dutyFee +
          payfee * this.settingSpreadRate.spreadRate
        ).toFixed(2);
        //return (this.detail.nowPrice * this.selectNumber * 100 / this.selectCycle).toFixed(2)
      } else {
        return 0;
      }
      // 需支付總價 = Current price * 股（1手 = 100股）/ 杠桿倍數
    },
    price() {
      if (this.autoNumber) {
        if (this.marketType == "us") {
          return (this.detail.nowPrice * this.autoNumber).toFixed(2);
        }else{
          return (this.detail.nowPrice * this.autoNumber * 1000).toFixed(2);
        }
      } else if (this.selectNumber) {
        if (this.marketType == "us") {
          return (this.detail.nowPrice * this.selectNumber).toFixed(2);
        }else{
          return (this.detail.nowPrice * this.selectNumber * 1000).toFixed(2);
          }
      } else {
        return 0;
      }
      // 市值價 = Current price * 股（1手 = 100股）
    },
    usNumberList(){
      return [
        { label: "100 "+ this.$t('common.shares'), value: "100" },
        { label: "200 "+this.$t('common.shares'), value: "200" },
        { label: "500 "+this.$t('common.shares'), value: "500" },
        { label: "1000 "+this.$t('common.shares'), value: "1000" },
        { label: "2000 "+this.$t('common.shares'), value: "2000" },
        { label: "5000 "+this.$t('common.shares'), value: "5000" },
        { label: "Input", value: "" }
      ]
    },
    twNumberList(){
      return [
        { label: "1 "+ this.$t('common.boardLot'), value: "1" },
        { label: "10 "+this.$t('common.boardLot'), value: "10" },
        { label: "20 "+this.$t('common.boardLot'), value: "20" },
        { label: "50 "+this.$t('common.boardLot'), value: "50" },
        { label: "100 "+this.$t('common.boardLot'), value: "100" },
        { label: "200 "+this.$t('common.boardLot'), value: "200" },
        { label: "自定義", value: "" }
      ]
    }
  },

  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    this.getDetail();
    this.selectNumber = 0;
    // this.getSettingIndexInfo();
    this.getSettingInfo();
    if (!this.$store.state.userInfo.enableAmt) {
      this.getUserInfo();
    }
    // this.detail = this.$route.query.info
  },
  methods: {
    changeAutoNumber() {
      // 自定義手數
      this.selectNumber = "";
    },
    async getSettingIndexInfo() {
      // 網站設定信息 指數
      let data = await api.getIndexSetting();
      if (data.status === 0) {
        // 成功
        this.settingIndexInfo = data.data;
      } else {
        Toast(data.msg);
      }
    },
    async getSettingInfo() {
      // 網站設定信息
      let data = await api.getSetting();
      if (data.status === 0) {
        this.settingInfo = data.data;
        // 成功
        // 杠桿倍數
        this.selectCycle = data.data.siteLever;
        // console.log(this.$store.state.userInfo)
        if (
          this.$store.state.userInfo !== undefined &&
          this.$store.state.userInfo !== null &&
          this.$store.state.userInfo.phone !== "" &&
          this.$store.state.userInfo.siteLever !== null
        ) {
          this.selectCycle = data.data.siteLever.split("/")[0];
          this.siteLeverList = [];
          for (let i = 0; i < data.data.siteLever.split("/").length; i++) {
            let val = data.data.siteLever.split("/")[i];
            let item = { label: val + " multiple", value: val };
            this.siteLeverList.push(item);
          }
        } else {
          this.selectCycle = data.data.siteLever.split("/")[0];
          this.siteLeverList = [];
          for (let i = 0; i < data.data.siteLever.split("/").length; i++) {
            let val = data.data.siteLever.split("/")[i];
            let item = { label: val + " multiple", value: val };
            this.siteLeverList.push(item);
          }
        }
      } else {
        Toast(data.msg);
      }
    },
    async findSpreadRateOne() {
      // 查詢Spread fee率
      let opts = {
        applies: this.detail.hcrate, // Quote change
        turnover: this.total, //成交額
        unitprice: this.detail.nowPrice, //股票單價
        code: this.$route.params.code
      };
      let data = await api.findSpreadRateOne(opts);
      if (data.status === 0) {
        // 成功
        if (data.data != undefined) {
          this.settingSpreadRate = data.data;
        }
      } else {
        this.$message.error(data.msg);
      }
    },
    // isAgree() {
    //   let i = false;
    //   let j = true;
    //   this.agree = this.agree ? i : j;
    // },
    totrageUrl() {
      this.$router.push("/trade");
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
      };
      if (this.marketType=="us") {
        let res1 = await api.getUsStockData(opts.code)
        let stock = res1.data[0];
        let stockDetail ={}
        stockDetail.code = opts.code
        stockDetail.name = stock["200024"];//股票名稱
        stockDetail.date = stock["200007"];//最近交易日期
        // stockDetail.time = stock[""];//最近成交時刻
        stockDetail.nowPrice = stock["6"];//最新價格
        stockDetail.rate = stock["11"];//漲跌
        stockDetail.hcrate = stock["56"];//漲跌幅
        stockDetail.high = stock["12"];//最高價
        stockDetail.low = stock["13"];//最低價
        stockDetail.volumn = stock["800001"];//累積成交量
        stockDetail.amount = stock[""];//成交金額
        stockDetail.yes = stock["21"];//昨收
        stockDetail.open = stock["19"];//開盤價
        if (stockDetail.rate > 0) {
          stockDetail.color = "upColor";
        }
        if (stockDetail.rate < 0) {
          stockDetail.color = "lowColor";
        }
        this.detail = stockDetail;
      } else {
        let res1 = await api.getTwStockData(opts.code)
        let stock = res1.data[0];
        let stockDetail ={}
        stockDetail.code = opts.code
        stockDetail.name = stock["200009"];//股票名稱
        stockDetail.date = stock["200007"];//最近交易日期
        stockDetail.nowPrice = stock["6"];//最新價格
        stockDetail.rate = stock["11"];//漲跌
        stockDetail.hcrate = stock["56"];//漲跌幅
        stockDetail.high = stock["12"];//最高價
        stockDetail.low = stock["13"];//最低價
        stockDetail.volumn = stock["800001"];//累積成交量
        stockDetail.amount = stock[""];//成交金額
        stockDetail.yes = stock["21"];//昨收
        stockDetail.open = stock["19"];//開盤價
        this.detail = stockDetail;
      }
      
      this.findSpreadRateOne();
    },
    selectCycleFun(value) {
      this.selectCycle = value;
    },
    selectNumberFun(value) {
      this.selectNumber = value;
      if (value !== 0) {
        this.autoNumber = "";
      }
    },
    selectTypeFun(value) {
      this.selectType = value;
    },
    canBuyStatus() {
      let dataTime = new Date();
      let day = dataTime.getDay(); // 星期幾
      let hour = dataTime.getHours(); // 小時
      let minute = dataTime.getMinutes(); // 分鐘
      if (day === 6 || day === 7) {
        return false;
      }
      if (hour < 9 || (hour >= 12 && hour < 13) || hour >= 15) {
        return false;
      }
      if (hour === 9 && minute < 32) {
        return false;
      }
      if (hour === 11 && minute >= 30) {
        return false;
      }
      if (hour === 14 && minute > 57) {
        return false;
      }
      return true;
    },
    async toInquiry() {
      // 判斷在不在開盤時間內
      // if(!this.canBuyStatus()){
      //     Toast('不在交易時段內！')
      //     return
      // }
      // 下單

      if (!this.$store.state.userInfo.idCard) {
        Toast("You have not been authenticated by real name");
        this.$router.push("/authentication");
        return;
      }
      if (!this.agree) {
        Toast("You need to agree to the cooperation agreement before trading!");
      } else if (isNull(this.selectNumber) && isNull(this.autoNumber)) {
        Toast("Please select the number of shares to purchase");
      } else if (isNull(this.selectType)) {
        Toast("Please select the buying and selling direction");
      } else {
        this.buying = true;
        var gCode = this.$route.query;
        // let res1 = await api.getUsStockData(gCode.code)
        // let stock = res1.data[0];
        var gCode = this.$route.query
        let stock ={}
        let buyNum = 0
      if (this.marketType == "usa") {
            let res1 = await api.getUsStockData(gCode.code)
              stock = res1.data[0];
            buyNum =  this.selectNumber == ''?this.autoNumber:this.selectNumber
          } else {
            let res1 = await api.getTwStockData(gCode.code)
              stock = res1.data[0];
              buyNum =  this.selectNumber == ''?this.autoNumber:this.selectNumber * 1000
          }
        let nowPrice = stock["6"];//最新價格
        let hcrate = stock["56"];//漲跌幅
        // let res2 = await api.getUsOpenClose(gCode.code)
        // let preClose = res2.data["o"][0];//開盤價

        let opts = {
          stockId: gCode.code,
          buyNum: buyNum,
          buyType: this.selectType,
          lever: this.selectCycle ? this.selectCycle : 0,
          nowPrice,
          hcrate,
        };

        let data = {}
        if (this.marketType == "usa") {
            data = await api.buyUsStock(opts);
        } else {
            data = await api.buyTwStock(opts);	
        }
        // let data = await api.buyUsStock(opts);
        this.buying = false;
        if (data.status === 0) {
          Toast(data.data);
          this.getUserInfo();
          this.$router.push("/orderlist?index=1");
        } else {
          Toast(data.msg);
        }
      }
    },
    toDetail() {
      this.$router.push("/listdetail");
    },
    goBack() {
      this.$router.go(-1);
    },
    async getUserInfo() {
      // 獲取用戶信息
      let data = await api.getUserInfo();
      if (data.status === 0) {
        // Toast(data.msg)
        this.$store.state.userInfo = data.data;
      } else {
        Toast(data.msg);
      }
    }
  }
};
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
      font-size: 0.32rem;
      .protem {
        margin-left: 0.1rem;
      }
    }

    .account {
      line-height: 0.3rem;
      font-size: 0.24rem;
      color: #999;
    }
  }

  .right-btn {
    flex: 2;
    display: flex;
    flex-direction: column;
    justify-content: center;
    .btn-buy {
      width: 2.4rem;
      height: 0.76rem;
      border-radius: 0.38rem;
      background-color: #7266ba;
      line-height: 0.76rem;
      text-align: center;
      font-size: 0.28rem;
      img {
        width: 0.26rem;
        height: 0.26rem;
        margin-right: 0.1rem;
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
      padding: 0.02rem 0.05rem 0.02rem 0.15rem;
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
    font-size: 0.24rem;
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
      width: 50%;
      float: left;
      margin-bottom: 0.15rem;
      margin-top: 0.15rem;
      text-align: right;
      div {
        background-color: #2d2e3b;
      }
      &:nth-child(odd) {
        width: 50%;
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
  padding: 0 0.1rem 0 0.3rem;
  position: relative;
  .price {
    // padding-bottom: 0.35rem;
  }
  &:after {
    display: block;
    position: absolute;
    content: "";
    width: 2px;
    height: 0.77rem;
    background-color: #2e2f34;
    top: 0.2rem;
    right: 0;
  }
}
.hangqin-right {
  padding: 0 0.3rem 0 0.1rem;
}
.box-tab {
  margin: 0.15rem 0.3rem;
  width: 6.9rem;
  background-color: #1b1c25;
  border-bottom: none;
  border-radius: 0.1rem;
  .tab-title {
    margin-bottom: 0;
    margin-top: 0.14rem;
    border-bottom: 1px solid #32333b;
    font-size: 0.32rem;
    font-weight: bold;
    padding-top: 0.12rem;
    padding-bottom: 0.12rem;
    height: auto;
    .notify {
      font-size: 0.24rem;
      color: #fff8;
    }
    &:after {
      background: #138eb4;
    }
    &.special::after{
      top:0.4rem
    }
  }
}
.radio-group li div {
  background-color: #2d2e3b;
  border-radius: 0.03rem;
  border: none;
}
.radio-group li div.on {
  background-color: #e00101;
}
.radio-group li input {
  background-color: #2d2e3b;
  border-radius: 0.03rem;
  border: none;
  width: 1rem;
  margin-right: 0.15rem;
  padding: 0 0.2rem;
}
.agree {
  font-size: 0.24rem;
  padding-bottom: 1.32rem;
}
.red-theme {
  .wrapper {
    background-color: #e9e9e9;
  }
  .detail-part .index-name {
    color: #222222;
  }
  .detail-part .index-name span.index-name_code {
    background-image: url(../../assets/ico/zhishu-red.png);
  }
  .hangqin-left .price {
    color: #e00202;
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
    background-color: #d9d9d9;
    border: 1px solid #aeaeae;
    color: #222222;
  }
  .radio-group li div.on {
    background-color: #e00202;
    border: 1px solid #e00202;
    color: #ffffff;
  }
  .box-tab .tab-title {
    border-bottom-color: #e9e9e9;
  }
  .agree {
    color: #000;
  }
  .footer-btn {
    background-color: #e0e0e0;
  }
  .footer-btn .total .pay {
    color: #000;
  }
  .footer-btn .total .pay .protem {
    color: #e00202;
    font-family: lightnumber;
  }
  .footer-btn .right-btn .btn-buy {
    background-color: #e00202;
  }
}
</style>
