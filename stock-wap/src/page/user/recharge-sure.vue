<template>
  <div class="wrapper">
    <!-- <div class="header">
      <mt-header title="Recharge說明">
        <router-link to="/recharge" slot="left">
          <mt-button icon="back">Recharge</mt-button>
        </router-link>
      </mt-header>
    </div> -->
    <div class="box1">
      <div class="form-block page-part">
        <mt-field
          label="Recharge amount"
          placeholder="Recharge amount"
          disabled
          type="number"
          v-model="selectNumber"
        ></mt-field>
        <mt-field
          label="訂單號"
          placeholder="訂單號"
          disabled
          v-model="orderSn">
          <span
              v-clipboard:copy="orderSn"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>copy
            </span>
        </mt-field>
        <!-- <div v-if="type == 0">
          <mt-field
            label="支付寶名稱"
            placeholder="支付寶名稱"
            disabled
            type="text"
            v-model="payInfo.channelName"
          >
          </mt-field>
          <mt-field
            label="支付寶帳號"
            placeholder="支付寶帳號"
            disabled
            type="text"
            v-model="payInfo.channelAccount"
          >
            <span
              v-clipboard:copy="payInfo.channelAccount"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
          <div
            class="text-right"
            style="padding: 0.2rem 0.3rem 0"
            @click="popupVisible2 = true"
          >
            檢視支付寶限額說明
          </div>
        </div>
        <div v-else-if="type == 1">
          <mt-field
            label="收款名稱"
            placeholder="收款名稱"
            disabled
            type="text"
            v-model="payInfo.channelName"
          >
            <span
              v-clipboard:copy="payInfo.channelName"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
          <mt-field
            label="收款金融"
            placeholder="收款金融"
            disabled
            type="text"
            v-model="payInfo.channelDesc"
          >
            <span
              v-clipboard:copy="payInfo.channelDesc"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
  
          <mt-field
            label="收款帳號"
            placeholder="收款帳號"
            disabled
            type="text"
            v-model="payInfo.channelAccount"
          >
            <span
              v-clipboard:copy="payInfo.channelAccount"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
        </div>
        <div v-else>
          <mt-field
            label="收款方"
            placeholder="收款方"
            disabled
            type="text"
            v-model="payInfo.channelName"
          >
          </mt-field>
          <mt-field
            label="收款帳號"
            placeholder="收款帳號"
            disabled
            type="text"
            v-model="payInfo.channelAccount"
          >
            <span
              v-clipboard:copy="payInfo.channelAccount"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
          <mt-field
            v-if="payInfo.channelDesc"
            label="備註"
            placeholder="備註"
            disabled
            type="text"
            v-model="payInfo.channelDesc"
          >
            <span
              v-clipboard:copy="payInfo.channelDesc"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
            >
              <i class="iconfont icon-fuzhi"></i>復制
            </span>
          </mt-field>
          <div v-if="payInfo.channelImg" class="eq-code-img">
            <img :src="payInfo.channelImg" alt="" />
          </div>
        </div> -->
      </div>
      <div class="tips-group red">
          <p><i class="iconfont icon-jinggao1"></i>Precautions:</p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou"></i
            >In order to facilitate the timely arrival of your funds, please contact customer service to reserve a Recharge channel every time you submit a Recharge.
          </p>
        </div>
      <!-- <div v-if="type == 0">
        <div class="tips-group">
          <p><i class="iconfont icon-jinggao1"></i>註意事項：</p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou"></i
            >由於支付收款上限限制，每次收款公戶可能不一樣，<span class="red"
              >請每次Recharge前獲取最新的二維碼完成支付</span
            >
          </p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou3"></i
            >如果您的網路環境不穩定，或由於設備、環境、偏好、行為、關繫、帳戶、身份等維度問題，<span
              class="red"
              >可能導致支付寶風控繫統提示風險，請您在安全的環境下發起支付</span
            >
          </p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou2"></i>為確保入金及時到帳，<span
              class="red"
              >請確認您輸入的金額和提交的Recharge金額一致</span
            >.
          </p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou1"></i
            >受支付寶到帳通知時間影響,入金時間到帳時間可能會延遲，請耐心等待.
          </p>
        </div>
        <div v-if="payInfo.channelImg" class="btnbox">
          <span class="text-center btnok" @click="toSure">點選獲取二維碼</span>
        </div>
        <div v-if="false" class="tips-group">
          <p><i class="iconfont icon-liucheng"></i>Recharge方式：</p>
          <p class="tip-text">
            <i class="iconfont icon-buzhou"></i
            >將二維碼保存到在地，打開支付寶掃描二維碼，轉帳到平臺指定對公帳戶
          </p>
          <p class="tip-text">
            <i class="iconfont icon-buzhou2"></i
            >點選“復制”，復制支付寶帳號，打開支付寶點選轉帳，轉帳到平臺指定對公帳戶
          </p>
        </div>
      </div>
      <div v-else-if="type == 1">
        <div class="tips-group">
          <p><i class="iconfont icon-liucheng"></i>操作流程：</p>
          <p class="tip-text"><i class="iconfont icon-buzhou"></i>點選“復制”，復制信息</p>
          <p class="tip-text">
            <i class="iconfont icon-buzhou2"></i>完成復制，打開貨幣錢包進行轉帳
          </p>
        </div>
        <div class="tips-group red">
          <p><i class="iconfont icon-jinggao1"></i>註意事項：</p>
          <p class="tip-text">
            <i class="iconfont icon-jingpaibuzhou"></i
            >為了及時到帳，Recharge成功後務必聯繫線上客服提交核對轉帳明細，確保入金安全！
          </p>
          
        </div>
      </div>
      <div v-else></div> -->
    </div>

    <!-- 倒計時彈框 -->
    <mt-popup
      v-model="popupVisible2"
      pop-transition="popup-fade"
      :closeOnClickModal="false"
      class="mint-popup-white mint-popup-white1"
    >
      <div class="clearfix">
        <a @click="popupVisible2 = false" class="pull-right"
          ><i class="iconfont icon-weitongguo"></i
        ></a>
      </div>
      <img width="100%" src="../../assets/img/xiane.png" alt="" />
    </mt-popup>

    <!-- <el-divider></el-divider>
    <div class="upload-box clearfix">
      <div class="upload-btn">
        <el-upload
          :with-credentials="true"
          class="avatar-uploader"
          :action="admin"
          list-type="picture-card"
          name="upload_file"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :on-error="handleError"
          :before-upload="beforeAvatarUpload"
        >
          <img style="width:100%;" v-if="imgUrl" :src="imgUrl" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          <span v-if="!imgUrl && !imgStatus" class="btn-title">匯款憑證</span>
          <span v-if="imgStatus" class="btn-title">正在上載中...</span>
        </el-upload>
      </div>
    </div> -->
  </div>
</template>

<script>
import * as api from "@/axios/api";
import { Toast } from "mint-ui";
import url from "@/axios/api.url";

export default {
  components: {},
  props: {},
  data() {
    return {
      type: "0", // 0 支付寶掃碼 1 對公轉帳
      selectNumber: 500,
      popupVisible: false, // 二維碼倒計時
      minutes: 5,
      seconds: 0,
      orderSn: null,
      imgUrl: null,
      imgStatus: false,
      admin: "",
      time: {
        minutes: 5,
        seconds: "00",
      },
      stopTime: false, // 倒計時結束提示
      timer: null, // 定時器
      info: {
        account: "收款帳號",
        payName: "收款名稱",
        payBank: "收款金融",
        payBankAddress: "收款支行",
        zhifubaoAccount: "jintongxue2018@163.com",
        zhifubaoName: "中山市金有米信息咨詢有限公司",
      },
      popupVisible2: false, // 限額說明
      payInfo: {
        channelImg: "",
      }, // 支付通路的相關信息
    };
  },
  computed: {},
  created() {
    this.type = this.$route.query.type;
    this.selectNumber = this.$route.query.selectNumber;
    this.orderSn = this.$route.query.data;
  },
  mounted() {
    this.getPayInfo();
    this.admin = process.env.API_HOST;
    if (this.admin === undefined) {
      this.admin = url.baseURL + '/user/upload.do';
    }
  },
  beforeDestroy() {
    window.clearInterval(this.timer);
  },
  watch: {},
  methods: {
    handleAvatarSuccess(res, file) {
      this.imgUrl = res.data.url;
      this.imgStatus = false;
    
      this.updateMoney()
    },
    async updateMoney(){
      let param = {
        orderSn: this.orderSn,
        imgUrl: this.imgUrl,
      };
      let data = await api.updateMoney(param);
      if (data.status === 0) {
        // 成功
        Toast(data.msg ? data.msg : "Recharge成功,等Pending review!");
      } else {
        Toast(data.msg ? data.msg : "Recharge失敗,請重新Recharge");
      }
    },
    handleError() {
      this.imgStatus = false;
    },
    beforeAvatarUpload() {
      this.imgStatus = true;
    },
    async getPayInfo() {
      // 獲取支付通路 詳細信息
      let data = await api.getPayInfoDetail({ payId: this.$route.query.id });
      if (data.status === 0) {
        this.payInfo = data.data;
      } else {
        Toast(data.msg);
      }
    },
    onCopy: function (e) {
      Toast("復制成功！");
    },
    onError: function (e) {
      Toast("復制失敗，請重試！");
    },
    toSure() {
      // Recharge
      if (
        this.selectNumber > this.payInfo.channelMaxLimit ||
        this.selectNumber < this.payInfo.channelMinLimit
      ) {
        Toast(
          "一次最高Recharge" +
            this.payInfo.channelMaxLimit +
            ",最低Recharge" +
            this.payInfo.channelMinLimit
        );
      } else {
        this.popupVisible = true;
        this.minutes = 5;
        this.seconds = 0;
        this.time.minutes = 5;
        this.time.seconds = "00";
        this.stopTime = false;
        this.timerInterval();
        // this.recharge()
      }
    },
    async recharge() {
      let opts = {
        amt: this.selectNumber,
        payType: this.type,
      };
      let data = await api.inMoney(opts);
      if (data.status === 0) {
        // 成功
        Toast(data.msg ? data.msg : "Recharge成功!");
      } else {
        Toast(data.msg ? data.msg : "Recharge失敗,請重新Recharge");
      }
    },
    closePopup() {
      // 關閉彈窗
      this.popupVisible = false;
      this.$router.push("/recharge");
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
          this.$router.push("/recharge");
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
.pay-img {
  padding: 0 0.2rem;
  padding-top: 0.417rem;

  img {
    width: 100%;
  }
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
    border: 0.02rem solid #4e4d4d;
    border-radius: 0.2rem;
  }

  .pay-weixin {
    border-color: #36ae55;
  }

  .pay-zhifubao {
    // border-color:#1296db;
  }

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
  padding: 0.2rem 0.417rem;

  .iconfont {
    margin-right: 0.2rem;
  }

  p {
    line-height: 2;
    font-size: 0.25rem;
  }

  .tip-text {
    text-indent: 0.28rem;
  }
}

.radio-group li {
  width: 19%;
  margin-right: 1%;
}

.mint-popup-white {
  color: #333;
  width: 80%;
  padding: 0.2rem 0.3rem 0;
  // bottom: 30%;
  border-radius: 0.1rem;
  box-shadow: 0.014rem 0.014rem 0.014rem rgba(103, 107, 111, 0.38);

  .popup-silide-bottom-leave-active {
    // bottom: -10%;
  }

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

.mint-popup-white1 {
  background: #eaeaea;
  padding-bottom: 0.3rem;
}

.box1 {
  // margin-top: 0.4rem;
  .icon-fuzhi {
    font-size: 0.24rem;
    margin-right: 0.1rem;
  }
}

.eq-code-img {
  text-align: center;
  margin-top: 0.2rem;

  img {
    width: 60%;
  }
}

.transaction {
  color: rgba(100, 100, 100, 0.78);

  .empty {
    width: 100%;
    // height: 1.34rem;
    font-size: 0.43rem;
    color: #888888;
    text-align: center;
    line-height: 2rem;
    // background: url('../../assets/img/thingsOk.png') no-repeat center center;
    background-size: 70%;
  }
}

.rule-box {
  padding: 0.2rem 0.3rem;

  .title {
    font-size: 0.3rem;
    height: 0.5rem;
    line-height: 0.5rem;
    margin-bottom: 0.2rem;
  }

  ul {
    li {
      color: #999;
      line-height: 0.5rem;
    }
  }
}

.upload-box {
  padding: 0.5rem;

  .upload-btn {
    // border: 1px solid #ddd;
    border-radius: 4px;
    width: 50%;
    height: 1.6rem;
    margin-bottom: 10px;
    float: left;
    margin: 0.5rem 25%;
    text-align: center;
    position: relative;

    .btn-hidden {
      height: 100%;
      width: 100%;
      position: absolute;
      top: 0;
      left: 0;
      z-index: 3;
      opacity: 0;
    }

    .id-img {
      max-width: 100%;
      max-height: 100%;
    }

    /deep/ .el-upload--picture-card {
      background: none;
      width: 100%;
      height: 1.6rem;
      line-height: 1.6rem;
    }

    .btn-title {
      position: absolute;
      top: 23px;
      left: 0;
      width: 100%;
    }

    /deep/ .el-upload__input {
      display: none;
    }
  }
}

.auth-msg {
  padding: 0.2rem 0.6rem;
  line-height: 0.4rem;

  p {
    color: red;
  }

  div {
    color: #ddd;
  }
}
</style>
