<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot">
      <el-container class="main-wrapper">
        <el-aside width="200px">
          <menu-box></menu-box>
        </el-aside>
        <el-main style=" min-height: calc(100vh - 150px );">
          <div class="header-chi" style="text-align: left;">
            <div class="user-center-title">
              <img src="../../../../assets/image/sanjao.png" alt />
              <span>{{$t('withdrawal.title')}}</span>
            </div>
          </div>
          <div class="wrapper">
            <div class="chongzhi-cont" style="display: flex;justify-content: center;">
              <div style="width:100%">
                <el-form>
                  <div class="left">
                    <div class="chongzhi-input">
                      <span>{{$t('withdrawal.withdrawalAmount')}}:</span>
                      <el-input type="text" v-model="form.number" 
                      :placeholder="$t('withdrawal.pleaseAmount')"></el-input>
                      <!-- <el-input v-model="form.amt" type="number" placeholder="最小Recharge金額為100USD"></el-input> -->
                    </div>
                    <el-form
                      label-width="100px"
                      v-model="form"
                      ref="ruleForm"
                      :rules="rule"
                      class="demo-form-inline"
                    >
                      <div class="chongzhi-type">
                        <p class="enable">
                          {{$t('withdrawal.availableBalance')}}:
                          <span class="qian">{{$store.state.userInfo.enableAmt}}</span>
                          <a @click="selectAll" href="javascript:;">
                          {{$t('withdrawal.allWithdrawal')}}
                          </a>
                        </p>
                      </div>
                    </el-form>

                    <div class="money-cont">
                      <p class="enable">
                        {{$t('withdrawal.available')}}:
                        <span class="k">{{$store.state.userInfo.enableAmt}}</span>
                      </p>
                      <p class="enable">
                        {{$t('withdrawal.freeze')}}:
                        <span class="k">{{$store.state.hide?'****':$store.state.userInfo.allFreezAmt}}</span>
                      </p>
                    </div>
                    <!-- @click="chongzhi" -->
                    <div class="chongzhi-btn-cont" @click="Onsubmit('ruleForm')">
                      <div class="chongzhi-btn">{{$t('withdrawal.title')}}</div>
                    </div>
                  </div>
                </el-form>

                <!-- <div class="chongzhi-bizhi">
                  <div class="chongzhi-bizhi-cont">
                    <div>Deposit notice</div>
                    <div class="chongzhi-item">
                      <span class="circle">1</span>
                      <span>Click "Click to Recharge" to jump to the payment page;</span>
                    </div>
                    <div class="chongzhi-item">
                      <span class="circle">2</span>
                      <span>Open the mobile phone to scan and complete the payment.</span>
                    </div>
                    <div class="chongzhi-item">
                      <span class="circle">3</span>
                      <span>Due to the upper limit of payment and collection, the public account of each payment may be different. Please obtain the latest QR code before each recharge to complete the payment.</span>
                    </div>
                    <div class="chongzhi-item">
                      <span class="circle">4</span>
                      <span>Recharge presets Recharge in the financing account. If you need to recharge the index account, you can transfer from the financing account to the index account.</span>
                    </div>
                  </div>
                  <div class="right">
                    <img
                      src="../../../../assets/image/youqingtishi.png"
                      v-if="color == 'black-bg'"
                      alt
                    />
                    <img
                      src="../../../../assets/image/youqingtishi1.png"
                      v-if="color == 'red-bg'"
                      alt
                    />
                  </div>
                </div> -->
              </div>
            </div>

            <!-- <div class="box page-part transaction">
            <div class="box-contain clearfix">
              <div class="user-center-title">
                用戶Withdrawal
              </div>
              <div class="text-center">
                <i style="color:green;font-size: 1.5rem;" class="iconfont icon-tixian1"></i>
              </div>
              <div class="auth-box">
                <el-form label-width="100px" v-model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
                  <el-form-item label="Withdrawal金額" prop="number">
                    <el-input type='text' v-model="form.number" placeholder="請輸入Withdrawal金額">
                    </el-input>
                  </el-form-item>
                </el-form>
                <p class="enable">當前帳戶可用餘額 <span>{{$store.state.userInfo.enableAmt}}</span> <a @click="selectAll"
                                                                                               href="javascript:;">全部Withdrawal</a>
                </p>
                <div slot="footer" class="dialog-footer">
                  <el-button type="success" :loading="isloading" @click="Onsubmit('ruleForm')">Withdrawal</el-button>
                </div>
              </div>
            </div>
            <div class="rule-box">
              <div class="attention" style="margin-bottom:10px;">
                <p>註意: Withdrawal預設提取融資帳戶中的可用資金,如需提取指數帳戶中的金額請先轉入至融資帳戶再提取 。</p>
              </div>
              <div class="title">提示：</div>
              <div class="attention">
                <p>1、當前有持仓訂單不能出金 。</p>
                <p>2、出金請先在官網通過實名認證和綁定金融帳戶 。</p>
                <p>3、出金時間工作日 {{settingInfo.withTimeBegin}} : 00 到 {{settingInfo.withTimeEnd}} : 00 之間。</p>
                <p>4、每筆出金扣除 {{settingInfo.withFeeSingle}} USD手續費<span v-if="settingInfo.withFeePercent != 0">，加上出金金額 * {{settingInfo.withFeePercent}}</span>。
                </p>
                <p>5、每筆Withdrawal金額最小 {{settingInfo.withMinAmt}} USD。</p>
                <p>6、<span class="red">出金時段內出金一般2小時到帳，出金時間收金融間清算時間影響，各家金融到帳時間不同，最遲T+1次日24點前到帳</span></p>
              </div>
            </div>
            </div>-->
          </div>
          <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
        </el-main>
      </el-container>
    </div>
  </el-container>
</template>

<script>
import HomeHeader from "../../../../components/HeaderOrder";
import MenuBox from "../menu";
import * as api from "../../../../axios/api";
import { mapState } from "vuex";

export default {
  components: {
    HomeHeader,
    MenuBox,
  },
  props: {},
  data() {
    // let validatePass = (rule, value, callback) => {
    //   if (value === '') {
    //     callback(new Error('請輸入金融帳戶號'))
    //   } else {
    //     let myreg = /^([1-9]{1})(\d{14,18})$/ // 卡號校驗
    //     if (!myreg.test(value)) {
    //       callback(new Error('請輸入正確的金融帳戶號'))
    //     }
    //     callback()
    //   }
    // }
    return {
      hasAuth: false,
      isloading: false,
      form: {
        number: "",
      },
      rule: {
        number: [
          { required: true, message: this.$t('withdrawal.pleaseAmount'), trigger: "blur" },
        ],
      },
      settingInfo: {
        withTimeBegin: "",
        withTimeEnd: "",
        withFeeSingle: "",
        withFeePercent: "",
        withMinAmt: "",
      },
    };
  },
  watch: {},
  computed: {
    ...mapState({
      color: (state) => state.systemColor,
    }),
  },
  created() {
    this.getSettingInfo();
    if (!this.$store.state.bankInfo.bankInfo) {
      this.getCardDetail();
    }
    if (!this.$store.state.userInfo.idCard) {
      this.getUserInfo();
    }
  },
  mounted() {
    this.$store.state.userMenu = "2-7";
  },
  methods: {
    selectAll() {
      this.form.number = this.$store.state.userInfo.enableAmt;
    },
    async getCardDetail() {
      // 獲取金融帳戶信息
      let data = await api.getBankCard();
      if (data.status === 0) {
        this.$store.state.bankInfo = data.data;
      } else {
        this.$message.error(this.$t('withdrawal.notBank'));
        this.$router.push("/bank");
        // this.$message.error(data.msg)
      }
    },
    async getUserInfo() {
      // 獲取用戶信息
      let data = await api.getUserInfo();
      if (data.status === 0) {
        // 判斷是否登入
        this.$store.state.userInfo = data.data;
      } else {
      }
    },
    async getSettingInfo() {
      // 網站設定信息
      let data = await api.getSetting();
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data;
      } else {
        this.$message.error(data.msg);
      }
    },
    async Onsubmit(formName) {
      // 提交
      //   this.$refs[formName].validate(async(valid) => {
      //     if (valid) {
        if (!this.form.number) {
          this.$message.error(this.$t('withdrawal.pleaseAmount'));
          this.$router.push("/bank");
          return;
        }
        let opts = {
          amt: this.form.number,
        };
        this.isloading = true;
        let data = await api.outMoney(opts);
        if (data.status === 0) {
          // 成功
          this.$message.success(this.$t('withdrawal.withdrawalSuccess'));
          this.$router.push("/withdrawlist");
        } else {
          this.$message.error(
            data.msg ? data.msg : this.$t('withdrawal.withdrawalFailed')
          );
        }
        this.isloading = false;
      //     } else {
      //         return false;
      //     }
      // })
    },
  },
};
</script>
<style lang="less" scoped>
.user-center-title {
  text-align: left;
}
.wrapper .demo-form-inline[data-v-f8f69ec6] {
  margin: 0;
}
.red-bg {
  .chongzhi-cont {
    position: relative;
    width: 199px;
    .money-cont {
      display: flex;
    }
    .money-cont > p {
      margin-top: 10px;
      margin-right: 10px;
    }
    .money-cont > p .k {
      color: #c11815;
    }
    .qian {
      font-size: 20px;
      font-family: MyriadSetPro;
      color: #c11815;
      font-weight: bold;
    }
    .left {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }
    .xianshi-cont {
      display: flex;
      justify-content: space-between;
    }
    .chongzhi-bizhi {
      margin-top: 430px;
      background-color: rgb(229, 229, 229);
      display: flex;
      padding: 40px;
      justify-content: space-between;
      .chongzhi-bizhi-cont .chongzhi-item {
        margin: 15px 0;
        color: rgb(121, 121, 121);
        .circle {
          display: inline-block;
          width: 15px;
          text-align: center;
          height: 15px;
          border: 1px solid rgb(121, 121, 121);
          border-radius: 50%;
          line-height: 15px;
        }
      }
    }
    .chongzhi-img {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .chongzhi-type {
      display: flex;
      margin-top: 20px;
      margin-bottom: 20px;
      .chongzhi-type-item {
        margin-right: 20px;
      }
    }
    .chongzhi-type > span {
      display: inline-block;
      width: 85px;
    }
    .chongzhi-btn-cont {
      display: flex;
      justify-content: center;
      margin-top: 10px;
      .chongzhi-btn {
		cursor: pointer;
        width: 80%;
        border-radius: 50px;
        text-align: center;
        height: 40px;
        background-color: #c11815;
        color: #fff;
        line-height: 40px;
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        margin-top: 20px;
      }
    }

    .chongzhi-input {
      margin-top: 20px;
      display: flex;
      align-items: center;
      width: 90%;
      background: none;
    }
    .chongzhi-input > span {
      display: inline-block;
      width: 100px;
    }
  }
}
.black-bg {
  .chongzhi-cont {
    position: relative;
    .money-cont > p .k {
      color: #86cbd2;
    }
    .qian {
      font-size: 20px;
      font-family: MyriadSetPro;
      color: #009c46;
      font-weight: bold;
    }
    .money-cont {
      display: flex;
    }
    .money-cont > p {
      margin-top: 10px;
      margin-right: 10px;
    }
    .left {
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
    }
    .xianshi-cont {
      display: flex;
      justify-content: space-between;
    }
    .chongzhi-bizhi {
      margin-top: 430px;
      background-color: #191e2b;
      display: flex;
      padding: 40px;
      justify-content: space-between;
      .chongzhi-bizhi-cont .chongzhi-item {
        margin: 15px 0;
        color: #7e8086;
        .circle {
          display: inline-block;
          width: 15px;
          text-align: center;
          height: 15px;
          border: 1px solid rgb(121, 121, 121);
          border-radius: 50%;
          line-height: 15px;
        }
      }
    }
    .chongzhi-img {
      display: flex;
      align-items: center;
      justify-content: center;
    }
    .chongzhi-type {
      display: flex;
      margin-top: 20px;
      margin-bottom: 20px;
      .chongzhi-type-item {
        margin-right: 20px;
      }
    }
    .chongzhi-type > span {
      display: inline-block;
      width: 85px;
    }
    .chongzhi-btn-cont {
      display: flex;
      justify-content: center;
      margin-top: 10px;
      .chongzhi-btn {
		  cursor: pointer;
        width: 80%;
        border-radius: 50px;
        text-align: center;
        height: 40px;
        background-color: #c11815;
        color: #fff;
        line-height: 40px;
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        margin-top: 20px;
      }
    }

    .chongzhi-input {
      margin-top: 20px;
      display: flex;
      align-items: center;
      width: 90%;
      background: none;
    }
    .chongzhi-input > span {
      display: inline-block;
      width: 100px;
    }
  }
}

.code {
  font-size: 12px;
  color: #999;
}

.auth-box {
  margin-top: 20px;

  .el-row {
    margin-bottom: 10px;
  }

  .enable {
    margin-bottom: 20px;
    margin-left: -50px;
    color: #666;
    text-align: center;

    span {
      font-weight: 600;
      font-size: 16px;
    }

    a {
      margin-left: 20px;
    }
  }
}

.authed-box {
  .el-row {
    padding-top: 40px;
    width: 400px;
    margin: 0 auto;
  }
}

.wrapper {
  padding: 40px 100px;

  .demo-form-inline {
    margin: 20px auto;
  }

  .auth-box {
    .dialog-footer {
      width: 400px;
      margin: 0 auto;

      .el-button {
        margin-left: 100px;
        width: 300px;
      }
    }
  }

  .prompt-box {
    padding-bottom: 20px;
    color: #f44336;

    p {
      line-height: 24px;
    }
  }

  .rule-box {
    margin-top: 30px;
    line-height: 26px;
    margin-left: 70px;
  }
}
</style>
