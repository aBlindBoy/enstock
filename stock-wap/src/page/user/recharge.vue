<template>
  <div class="wrapper">
    <form
      id="pay_form"
      action="https://zf.flyotcpay.com/payment/"
      method="post"
    >
      <input type="hidden" name="merchantid" v-model="formDate.merchantid" />
      <input type="hidden" name="orderno" v-model="formDate.orderno" />
      <input type="hidden" name="orderamount" v-model="formDate.orderamount" />
      <input type="hidden" name="paytype" v-model="formDate.paytype" />
      <input
        type="hidden"
        name="ordercurrency"
        v-model="formDate.ordercurrency"
      />
      <input
        type="hidden"
        name="serverbackurl"
        v-model="formDate.serverbackurl"
      />
      <input type="hidden" name="callbackurl" v-model="formDate.callbackurl" />
      <input type="hidden" name="signtype" v-model="formDate.signtype" />
      <input type="hidden" name="sign" v-model="formDate.sign" />
    </form>
    <!-- <div class="header">
      <mt-header title="Recharge">
        <router-link to="/user" slot="left">
          <mt-button icon="back">我的</mt-button>
        </router-link>
      </mt-header>
    </div> -->
    <div class="box">
      <div class="box-contain clearfix">
        <div class="account text-center">
          <p class="title">Current Available Balance (USD)</p>
          <p class="red number">{{ $store.state.userInfo.enableAmt }}</p>
        </div>
      </div>
    </div>
    <div class="page-part transaction">
      <div class="box-contain clearfix">
        <div class="back-info">
          <!-- 金融帳戶信息 -->
          <p class="title">Recharge amount (USD)</p>
          <div class="box-tab">
            <input v-model="selectNumber" class="btn-default" type="number" />
            <div class="tab-con">
              <ul class="radio-group clearfix">
                <li
                  v-for="item in numberList"
                  :key="item.key"
                  @click="selectTypeFun(item.value)"
                >
                  <div :class="selectNumber == item.value ? 'on' : ''">
                    {{ item.label }}
                  </div>
                </li>
                <!-- <li v-for="item in numberList" :key="item.key" @click="selectTypeFun(item.value)">
                    <div :class="selectNumber == item.value?'on':''">
                        {{item.label}}
                    </div>
                </li> -->
              </ul>
            </div>
            <p style="padding-bottom: 0.3rem">
              The minimum Recharge amount is {{ settingInfo.chargeMinAmt }}USD
            </p>
          </div>
        </div>
        <div class="back-info" style="display:none">
          <!-- 金融帳戶信息 -->
          <p class="title">Recharge method</p>
          <div class="box-tab">
            <div v-for="i in optionsPay" :key="i.key" class="pay-radio">
              <!-- 1 ==> 支付寶 2 ==> 微信 3 ==> 對公轉帳-->
              <div
                @click="changeType(i)"
                :class="i.id == id ? 'pay-list on' : 'pay-list'"
                style="display: flex"
              >
                <span class="col-md-4 pay-icon">
                  <i
                    v-if="i.ctype == 0"
                    style="color: #1296db"
                    class="iconfont icon-zhifubao"
                  ></i>
                  <i
                    v-else-if="i.ctype == 1"
                    style="color: #36ae55"
                    class="iconfont icon-yinlian"
                  ></i>
                  <i
                    v-else
                    style="color: #009688"
                    class="iconfont icon-chongzhi"
                  ></i>
                  {{ i.channelType }}
                </span>
                <span class="col-md-4 pull-right" style="text-align: right">
                  <i
                    :class="
                      id == i.id
                        ? 'icon-on iconfont icon-xuanzhong'
                        : 'iconfont icon-weixuanze'
                    "
                  ></i>
                </span>
              </div>
            </div>
            <!-- <div class="pay-radio">
                  <div @click="changeType('juhe1')" :class="'juhe1' == type?'pay-list on':'pay-list'">
                    <span class="col-md-4 pay-icon">
                       <i style="color:#009688;" class="iconfont icon-chongzhi"></i>
                       支付寶掃碼
                    </span>
                    <span class="col-md-4 pull-right">
                        <i :class="type == 'juhe1'?'icon-on iconfont icon-xuanzhong':'iconfont icon-weixuanze'"></i>
                    </span>
                  </div>
            </div> -->
          </div>
        </div>
      </div>
      <div class="btnbox">
        <el-button
          type="danger"
          style="
            background: #b60c0d;
            border-color: #b60c0d;
            width: 100%;
            margin-top: 0.28rem;
          "
          @click="toSure"
          >Recharge now</el-button
        >
        <div v-if="dialogShow" class="text-center btnok">
          <form
            method="get"
            ref="formDate"
            :action="formDate.postUrl"
            enctype="multipart/form-data"
          >
            <!--<input type="hidden" name="pay_amount" v-model="selectNumber"/>-->
            <!--<input type="hidden" name="pay_applydate" v-model="formDate.pay_applydate"/>-->
            <!--<input type="hidden" name="pay_bankcode" v-model="formDate.pay_bankcode"/>-->
            <!--<input type="hidden" name="pay_callbackurl" v-model="formDate.pay_callbackurl"/>-->
            <!--<input type="hidden" name="pay_md5sign" v-model="formDate.pay_md5sign"/>-->
            <!--<input type="hidden" name="pay_memberid" v-model="formDate.pay_memberid"/>-->
            <!--<input type="hidden" name="pay_notifyurl" v-model="formDate.pay_notifyurl"/>-->
            <!--<input type="hidden" name="pay_orderid" v-model="formDate.pay_orderid"/>-->
            <!--<input type="hidden" name="pay_productdesc" v-model="formDate.pay_productdesc"/>-->
            <!--<input type="hidden" name="pay_productname" v-model="formDate.pay_productname"/>-->
            <!--<input type="hidden" name="pay_productnum" v-model="formDate.pay_productnum"/>-->
            <!--<input type="hidden" name="pay_producturl" v-model="formDate.pay_producturl"/>-->
          </form>
          <button class="submitBtn" type="submit" @click="onsubmit()">
            Recharge now
          </button>
        </div>
      </div>
      <div class="attention">
        <p><i class="iconfont icon-jinggao1"></i>Precautions:</p>
        <p>
          More than five unpaid orders, the account is prohibited from continuing to place orders
        </p>
        <p>
          The remitter information and financial account information must be consistent with the real-name registration information!
        </p>
        <p>
          The three-party financial supervision account can be changed at any time, please be sure to obtain the latest payment information!
        </p>
      </div>
    </div>

   
    <mt-popup
      pop-transition="popup-fade"
      :closeOnClickModal="false"
      class="mint-popup-white"
      v-model="bankVisible"
    >
      <div class="clearfix">
        <a @click="closePopup" class="pull-right"
          ><i class="iconfont icon-weitongguo"></i
        ></a>
      </div>
      <div class="mint-field-warpper">
        <mt-field
          label="Transferee name"
          placeholder="Please enter the sender's name"
          v-model="username"
          style=""
        ></mt-field>
        <mt-field
          label="Last four digits of financial account"
           type="tel"
          placeholder="Please enter the last four digits of the transfer financial account"
          v-model="card"
          style=""
        ></mt-field>
        <el-button
          type="danger"
          size="mini"
          style="
            background: #b60c0d;
            border-color: #b60c0d;
            margin: 0.28rem 0;
          "
          @click="recharge"
          :loading="isloading"
          >Next step</el-button
        >
      </div>
    </mt-popup>
  </div>
</template>

<script>
import * as api from "@/axios/api";
import QRCode from "qrcodejs2";
import { Toast } from "mint-ui";

export default {
  components: {},
  props: {},
  data() {
    return {
      isloading: false,
      dialogShow: false, // 掃碼支付
      numberList: [
        // {label:'10000',value:10000},
        { label: "50000", value: 50000 },
        { label: "80000", value: 80000 },
        { label: "100000", value: 100000 },
        { label: "500000", value: 500000 },
        { label: "1000000", value: 1000000 },
      ],
      selectNumber: 50000,
      username:'',
      card:'',
      type: "", // 選擇的通路類型
      optionsPay: [
        {
          label: "對公轉帳",
          value: "3",
        }
      ],
      popupVisible: false, // 二維碼倒計時
      bankVisible: false, //填寫轉帳信息
      minutes: 10,
      seconds: 0,
      time: {
        minutes: 10,
        seconds: "00",
      },
      formh5Date: {},
      formCode: 1,
      stopTime: false, // 倒計時結束提示
      timer: null, // 定時器
      settingInfo: {
        chargeMinAmt: 500,
      }, // 設定信息
      id: "", // 選擇通路的id
      formDate: {
        merchantid: "",
        orderno: "",
        orderamount: "",
        paytype: "",
        ordercurrency: "",
        serverbackurl: "",
        callbackurl: "",
        signtype: "",
        sign: "",
      },
      code: "",
      formUrl: "",
      formCode: "",
    };
  },
  computed: {},
  created() {},
  mounted() {
    if (this.$state.theme == "red") {
      document.body.classList.remove("black-bg");
      document.body.classList.add("red-bg");
    }
    this.getSettingInfo();
    if (!this.$store.state.userInfo.idCard) {
      this.getUserInfo();
    }
    this.getPayInfo();
  },
  beforeDestroy() {
    if (this.$state.theme == "red") {
      document.body.classList.remove("red-bg");
      document.body.classList.add("black-bg");
    }
    window.clearInterval(this.timer);
  },
  watch: {},
  methods: {
    async onsubmit() {
      // 解決金額不變的問題
      if (this.type === 2) {
        let data2 = await api.getjuhe1({
          payType: this.formCode,
          payAmt: this.selectNumber,
        });
        if (data2.status === 0) {
          // 成功
          this.formDate = data2.data;
          //console.log(document.getElementById("pay_form"))
          this.dialogShow = true;
          // 支付跳轉
          setTimeout(() => {
            document.getElementById("pay_form").submit();
            this.isloading = false;
          }, 1500);
        } else {
          Toast(data2.msg);
        }
      } else {
        setTimeout(() => {
          this.$refs.formDate.submit();
        }, 1000);
      }
    },
    async getUserInfo() {
      // 獲取用戶信息
      let data = await api.getUserInfo();
      if (data.status === 0) {
        this.$store.state.userInfo = data.data;
      } else {
        Toast(data.msg);
      }
    },
    async getPayInfo() {
      // 獲取支付通路
      let data = await api.getPayInfo();
      if (data.status === 0) {
        this.optionsPay = data.data;
        this.id = data.data[0].id;
        this.type = data.data[0].ctype;
        if (data.data[0].ctype === 2) {
          this.formCode = data.data[0].formCode;
          let data2 = await api.getjuhe1({
            payType: data.data[0].formCode,
            payAmt: this.selectNumber,
          });
          if (data2.status === 0) {
            // 成功
            this.formDate = data2.data;
            this.dialogShow = true;
          } else {
            Toast(data2.msg);
          }
        }
      } else {
        Toast(data.msg);
      }
    },
    selectTypeFun(value) {
      // 選擇Recharge金額
      this.selectNumber = value;
    },
    async changeType(value) {
      this.id = value.id;
      // 支付寶掃碼通路單獨分開
      //  if(value == 'juhe1'){
      if (
        value.formUrl !== undefined &&
        value.formUrl !== "" &&
        value.formUrl.indexOf("yunpay.waa.cn") !== -1
      ) {
        this.type = value.ctype;
        this.formDate = value;
        this.formCode = value.formCode;
        this.dialogShow = false;
      } else if (value.ctype === 2) {
        this.type = value.ctype;
        // let data  = await api.getjuhe1({payType:903,payAmt:this.selectNumber})
        let data = await api.getjuhe1({
          payType: value.formCode,
          payAmt: this.selectNumber,
        });
        if (data.status === 0) {
          // 成功
          this.formCode = value.formCode;
          this.formDate = data.data;
          this.dialogShow = true;
        } else {
          Toast(data.msg);
        }
      } else {
        this.dialogShow = false;
        if (value.isLock === 1) {
          Toast("This channel is temporarily unavailable");
        } else {
          if (value.ctype === 2) {
            // 其他通路 保存code & url
            this.code = value.formCode;
            this.formUrl = value.formUrl;
            this.type = value.ctype;
          } else {
            // 選擇支付方式
            this.type = value.ctype;
            this.id = value.id;
          }
        }
      }
    },
    async toSure() {
      // Recharge 先判斷是否實名認證
      if(!this.selectNumber){
        Toast("Please select the recharge amount")
        return
      }
      await this.getCardDetail();

      // if (!this.$store.state.userInfo.idCard) {
      //   Toast("您還未實名認證,請先實名認證");
      //   this.$router.push("/authentication");
      //   // else if(this.type == 2){
      //   //     Toast('微信支付暫未開通')
      //   // }else if(this.type == 3){
      //   //     Toast('對公轉帳暫未開通')
      //   // }
      // } else if (!this.$store.state.bankInfo.bankNo) {
      //   Toast("請先綁定金融帳戶");
      //   this.$router.push("/addCard");
      //   return;
      // } else {
      //   this.bankVisible=true
      // }
      this.recharge()
    },
    async getSettingInfo() {
      let data = await api.getSetting();
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data;
        // this.selectNumber = this.settingInfo.chargeMinAmt // 設定預設金額為最小金額
        // Recharge金額快捷選擇
        // this.numberList = []
        // for(var i = 0;i<10;i++){
        //     let item = {
        //         label:(this.settingInfo.chargeMinAmt+i*this.settingInfo.chargeMinAmt)  ,
        //         value:this.settingInfo.chargeMinAmt+i*this.settingInfo.chargeMinAmt
        //     }
        //     this.numberList.push(item)
        // }
      } else {
        Toast(data.msg);
      }
    },
    async recharge() {
      //H5支付
      if (
        this.formDate.formUrl !== undefined &&
        this.formDate.formUrl !== "" &&
        this.formDate.formUrl.indexOf("yunpay.waa.cn") !== -1
      ) {
        let data5 = await api.getjuheH5({
          payType: this.formDate.formCode,
          payAmt: this.selectNumber,
        });
        if (data5.status === 0) {
          this.formh5Date = data5.data;
          this.$nextTick(() => {
            this.qrcode(this.formh5Date.qrcode);
          });

          this.popupVisible = true;
          // console.log(document.getElementById("payh5_form"))
          // return;
          // setTimeout(() => {
          //   this.isloading = false
          // }, 180000)
        } else {
          this.$message.error(data5.msg);
        }
      } else if (this.type === 2) {
        // 其他通路
        let opts = {
          payType: this.code,
          payAmt: this.selectNumber
        };
        let data = await api.payLInk(this.formUrl, opts);
        if (data.status === 0) {
          // 成功
          this.formDate = data.data;
        } else {
          Toast(data.msg);
        }
      } else {
        this.bankVisible = false;
        let opts = {
          amt: this.selectNumber,
          payType: this.type,
          username:this.username,
          card:this.card,
          productId: "8000",
          returnUrl: "", //回調頁面
          imgUrl: null,
        };
        let realName=this.$store.state.bankInfo.realName
        let realCard=this.$store.state.bankInfo.bankNo.substring(this.$store.state.bankInfo.bankNo.length-4)
        // if(this.username!=realName||this.card!=realCard){
        //   Toast("轉帳人信息與實名信息不一致")
        //   return
        // }
        let data = await api.inMoney(opts);
        this.isloading=false
        if (data.status === 0) {
          // 成功
          Toast(data.msg?data.msg:'Get information successfully!')
          this.$router.push({
            path: "/rechargeSure",
            query: {
              type: this.type,
              id: this.id,
              selectNumber: this.selectNumber,
              data:data.data
            },
          });
        } else {
          Toast(data.msg ? data.msg : "Recharge failed, please recharge");
        }
      }
    },
    /**
     *  獲取金融帳戶信息
     */
    async getCardDetail() {
      let data = await api.getBankCard();
      if (data.status === 0) {
        this.$store.state.bankInfo = data.data;
      }
    },
    // 生成二維碼
    qrcode(url) {
      document.getElementById("qrcode").innerHTML = "";
      let qrcode = new QRCode("qrcode", {
        width: 200, // 二維碼寬度，單位像素
        height: 200, // 二維碼高度，單位像素
        text: url, // 生成二維碼的鏈接
      });
    },
    closePopup() {
      // 關閉彈窗
      this.popupVisible = false;
      this.bankVisible=false
      window.clearInterval(this.timer);
    },
    goBack() {
      this.$router.back(-1);
    },
    num(n) {
      return n < 10 ? "0" + n : "" + n;
    },
    timerInterval() {
      var _this = this;
      _this.timer = window.setInterval(function () {
        if (_this.seconds === 0 && _this.minutes !== 0) {
          _this.seconds = 59;
          _this.minutes -= 1;
        } else if (_this.minutes === 0 && _this.seconds === 0) {
          // 倒計時結束
          _this.seconds = 0;
          _this.stopTime = true;
          window.clearInterval(_this.timer);
        } else {
          _this.seconds -= 1;
        }
        _this.time.minutes = _this.num(_this.minutes);
        _this.time.seconds = _this.num(_this.seconds);
      }, 1000);
    },
  },
};
</script>
<style lang="less" scoped>
.mint-field-warpper {
  text-align: center;
  /deep/.mint-cell ,.mint-field {
    padding: 0;
    background: #fff;
    .mint-cell-wrapper {
      border-bottom: 0.01rem solid #d9d9d9!important;
      .mint-cell-title{
        width: 105px;
        color: #222;
      }
      .mint-cell-text {
        color: #222;
      }
    }
  }
}

.pay-img {
  padding: 0 0.2rem;
  padding-top: 0.417rem;

  img {
    width: 100%;
  }
}

.submitBtn {
  background: none;
  border: none;
  display: inline-block;
  width: 100%;
}

.pay-radio {
  /* padding: 0.2rem 0.1rem; */
  margin-bottom: 0.2rem;
  height: 0.8rem;
  line-height: 0.75rem;

  .pay-icon {
    .iconfont {
      margin-right: 0.2rem;
    }
  }

  .pay-list {
    border-radius: 0.2rem;

    .pay-miniimg {
      width: 18px;
      vertical-align: middle;
      margin-right: 8px;
    }
  }

  .pay-weixin {
    border-color: #36ae55;
  }

  // .pay-zhifubao{
  // border-color:#1296db;
  // }
  .icon-on {
    color: #b60c0d;
  }
}

.btn-default {
  border: 0.02rem solid #4e4d4d;
  border-radius: 0.2rem;
  display: inline-block;
  height: 0.8rem;
  width: 100%;
  text-indent: 0.2rem;
  background: none;
  color: #ddd;
}

.tips-group {
  padding: 0.417rem;
  margin-top: 0.417rem;

  p {
    line-height: 2;
    font-size: 0.25rem;
  }

  .tip-text {
    text-indent: 0.28rem;
  }
}

.transaction {
  .title {
    padding: 0.2rem;
  }

  .input-btn {
    border: 0.02rem solid #4e4d4d;
    height: 0.6rem;
    line-height: 0.6rem;
    border-radius: 0.05rem;
    width: 100%;
  }
}
.radio-group {
  display: flex;
  justify-content: space-between;
  li {
    // width: 19%;

    margin-right: 1%;
  }
}

.account {
  padding-bottom: 0.4rem;

  .title {
    height: 1.4rem;
    line-height: 1.4rem;
    font-size: 0.29rem;
    // color: rgb(51, 51, 51);
    text-align: center;
    font-weight: 700;
  }

  .number {
    font-size: 0.466rem;
  }
}

.mint-popup-white {
  color: #333;

  // bottom: 30%;
  border-radius: 0.1rem;
  box-shadow: 0.014rem 0.014rem 0.014rem rgba(103, 107, 111, 0.38);
  // .popup-silide-bottom-leave-active{
  //     bottom: -10%;
  // }
  .box-block {
    .qrCode {
      border: 1px solid #f3f3f3;
      padding: 0.1rem;
      height: 3rem;
      width: 3rem;
      margin: 0.3rem auto;
      position: relative;

      img {
        width: 100%;
        height: 100%;
      }

      .alert-box {
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.9);
        position: absolute;
        left: 0;
        top: 0;
        color: #333;
        text-align: center;

        .iconfont {
          color: #f98700;
          font-size: 0.6rem;
          display: block;
          margin-top: 0.8rem;
          margin-bottom: 0.4rem;
        }
      }
    }

    .prompt-box {
      padding: 0.2rem;
      margin: 0.2rem 0;
      color: #666;
    }

    .money {
      font-weight: bold;
      font-size: 0.5rem;

      .number {
        margin-left: 0.1rem;
      }
    }

    .timer-box {
      text-align: center;
      font-size: 0.5rem;
    }

    .number {
      font-size: 0.6rem;
    }
  }

  .scan {
    border-top: 0.02rem dashed #ddd;
    margin-top: 0.5rem;
    padding: 0.3rem;
    text-align: center;
    color: #ff1100;
  }

  // 微信支付寶icon設定
  .icon-umidd17 {
    color: #1296db;
    font-size: 0.6rem;
  }

  .icon-02 {
    color: #36ae55;
    font-size: 0.6rem;
  }
}

.attention {
  line-height: 0.4rem;
  color: rgb(187, 187, 187);
  padding: 0.347rem;
}
</style>
