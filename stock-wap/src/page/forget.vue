<template>
  <div class="wrapper">
    <div class="header">
      <mt-header title="Forgot password">
        <router-link to="/login" slot="left">
          <mt-button icon="back"></mt-button>
        </router-link>
      </mt-header>
    </div>
    <div class="form-group">
      <div class="title">
        Enter your mobile number
      </div>
      <div class="page-part">
        <mt-field label="Phone number" type="tel" placeholder="Please enter phone number" v-model="phone"></mt-field>
        <mt-field label="get verification code" type="number" placeholder="enter confirmation code" v-model="code">
          <span v-if="codeshow" class="getcode" @click="checkCodeBox">get verification code</span>
          <span v-if="!codeshow" class="getcode">{{count}}s</span>
        </mt-field>
        <mt-field label="enter password" placeholder="Please enter password" type="password" v-model="password"></mt-field>
        <mt-field label="enter password" placeholder="Confirm the password you entered" type="password" v-model="password2"></mt-field>
      </div>
    </div>
    <div class="btnbox">
      <span class="text-center btnok" @click="gook">Confirm</span>
    </div>
    
    <div style="color:red;width: 100%;text-align: center;margin-top: 50px;">
      Tip: If you need to change your password, please contact customer service
    </div>
    <mt-popup v-model="dialogShow" :closeOnClickModal="false" class="mint-popup-box mint-popup-white">
      <div class="clearfix">
        <a @click="dialogShow = false" class="pull-right"><i class="iconfont icon-weitongguo"></i></a>
      </div>
      <div class="">
        <div class="check-box row">
          <div class="title">
            Enter the verification code on the picture
          </div>
          <mt-field label="verification code" placeholder="please enter verification code" v-model="code2">
            <img @click="refreshImg" :src="adminUrl+'/code/getCode.do?time=' + imgCodeTime" height="45px" width="100px">
          </mt-field>
          <p class="red" v-if="!checkCodeState">The verification code you entered is incorrect, please re-enter</p>
          <div class="text-center" style="width: 100%; padding: 0.2rem;">
            <mt-button type="primary" @click="checkImg">Confirm</mt-button>
            <!-- <mt-button style="margin-left: 10%;width:22%" type="default" @click="dialogShow = false">返回</mt-button> -->
          </div>

        </div>
      </div>
    </mt-popup>
  </div>
</template>

<script>
import { Toast } from 'mint-ui'
import { isNull, isPhone } from '@/utils/utils'
import * as api from '@/axios/api'

export default {
  components: {},
  props: {},
  data () {
    return {
      phone: '',
      password: '',
      password2: '',
      code: '',
      code2: '',
      codeshow: true,
      count: '', // 倒計時
      clickFalg: 0, //  點選次數
      imgCode: '',
      adminUrl: '',
      dialogShow: false, // 顯示彈窗
      ischeckImg: false,
      checkCodeState: true, // 驗證碼的狀態
      dialogImgShow: false, // 圖片顯示
      imgCodeTime: 0
    }
  },
  watch: {
    code2 (newval) {
      if (newval) {
        this.checkCodeState = true
      }
    }
  },
  computed: {},
  methods: {
    checkCodeBox () {
      if (isNull(this.phone) || !isPhone(this.phone)) {
        Toast('please enter a valid phone number')
      } else {
        this.checkPhone()
      }
    },
    async checkCode () {
      let data = await api.checkCode({ code: this.code2 })
      this.ischeckImg = data
    },
    async checkImg () {
      if (!this.code2) {
        this.checkCodeState = false
        Toast('The verification code you entered is incorrect, please re-enter')
        return
      }
      // await this.checkCode()
      let data = await api.checkCode({ code: this.code2 })
      if (data === 'true' || data === true) {
        this.getcode()
        this.dialogShow = false
        this.checkCodeState = true
      } else {
        this.checkCodeState = false
        Toast('The verification code you entered is incorrect, please re-enter')
        this.adminUrl = process.env.API_HOST + '1'
        this.adminUrl = process.env.API_HOST
        if (this.adminUrl === undefined) {
          this.adminUrl = ''
        }
      }
    },
    async getcode () {
      if (this.clickFalg !== 0) {
        this.clickFalg = 0
        return
      }
      this.clickFalg++
      // var reg = 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/
      let reg = /^[0-9]{11}$/ // 手機號碼驗證
      if (isNull(this.phone)) {
        Toast('Mobile number cannot be empty')
      } else {
        if (!reg.test(this.phone)) {
          Toast('Please enter the correct mobile number or email')
        } else {
          let result = await api.sendForgetSms({ phoneNum: this.phone })
          if (result.status === 0) {
            const TIME_COUNT = 60
            if (!this.timer) {
              this.count = TIME_COUNT
              this.codeshow = false
              this.clickFalg = 0
              this.timer = setInterval(() => {
                if (this.count > 0 && this.count <= TIME_COUNT) {
                  this.count--
                } else {
                  this.codeshow = true
                  clearInterval(this.timer)
                  this.timer = null
                }
              }, 1000)
            } else {
              Toast(result.msg)
            }
          }
        }
      }
    },
    async checkPhone () {
      // 先驗證是否已經註冊
      let data = await api.checkPhone({ phoneNum: this.phone })
      if (data.status === 0) {
        // 如果用戶已存在返回 0
        this.dialogShow = true
        this.adminUrl = process.env.API_HOST
        if (this.adminUrl === undefined) {
          this.adminUrl = ''
        }
        // this.gook()
      } else {
        Toast('User is not registered, please register')
        this.$router.push('/register')
      }
    },
    async gook () {
      if (isNull(this.phone)) {
        Toast('Please enter the correct mobile number or email')
      } else if (isNull(this.password)) {
        Toast('Please enter password')
      } else if (isNull(this.password2)) {
        Toast('Please confirm your password')
      } else if (isNull(this.code)) {
        Toast('please enter verification code')
      } else if (this.password !== this.password2) {
        Toast('The two entered passwords do not match')
        this.password = 0
        this.password2 = 0
      } else {
        let opts = {
          phoneNum: this.phone,
          code: this.code,
          newPwd: this.password
        }
        let data = await api.forgetPas(opts)
        if (data.status === 0) {
          Toast('The modification is successful, please log in!')
          this.$router.push('/login')
        } else {
          Toast(data.msg ? data.msg : 'Modify failed, please modify again')
        }
      }
    },
    refreshImg () {
      this.adminUrl = ''
      this.imgCodeTime = Date.now()
      this.dialogImgShow = false
      let this_ = this
      setTimeout(function () {
        this_.adminUrl = process.env.API_HOST
        if (this_.adminUrl === undefined) {
          this_.adminUrl = ''
        }
        this_.dialogImgShow = true
      }, 500)
    }
  },
  created () {},
  mounted () {}
}
</script>
<style lang="less" scoped>
  // body {
  //   background-color: #fff;
  // }

  body /deep/ .mint-toast {
    z-index: 1000000;
  }

  .form-group {
    padding: 0rem 0.97rem;
    font-size: 0.29rem;

    .title {
      font-size: 0.43rem;
      margin-bottom: 0.34rem;
      margin-top: 1.40rem;
    }

    .mint-cell {
      padding: 0;
    }

    /deep/ .mint-cell-title {
      text-align: left;
    }

    /deep/ .mint-cell-wrapper {
      height: 1.19rem;
      line-height: 1.19rem;
    }
  }

  .btnbox {
    width: 35%;
    padding-top: 0.28rem;
  }

  .mint-popup-box {
    // width: 100%;
    // height: 100%;
    // background:#fff;
    .title {
      font-size: 0.43rem;
      margin-bottom: 0.34rem;
      // margin-top: 1.40rem;
      padding: 0.1rem 0.4rem;
      color: #333;
    }

    .mint-cell {
      background: none;
      color: #000;
      width: 100%;
    }

    /deep/ .mint-cell-text {
      color: #000;
    }

    .mint-cell-wrapper {
      border: 0.02rem solid #ddd;
    }

    .mint-button {
      margin-top: 0.2rem;
      width: 60%;
    }

    .check-box {
      p {
        padding: 0.2rem 0.4rem;
      }

      padding-bottom: 0.3rem;
    }
  }

  // #app.red-theme{
  //   .form-group{
  //     background-color: white;
  //     width: 90%;
  //     margin:auto;
  //       // backgroup-color: #000;
  //   }
  //   p{
  //       color: #000;
  //   }

// }

</style>
