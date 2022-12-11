<template>
  <div class="wrapper">
    <div class="header">
      <!-- <a href="/homes" class="nav-left pull-left">
          <img :src="siteInfo.siteLogo" alt="">
      </a> -->
      <home-header></home-header>
    </div>
    <div class="loginbg-box">
      <!-- <img class="login-img" src="../../assets/image/login.jpeg" alt=""> -->
    </div>
    <div class="login-box">
      <div class="form-box top-box">
        <p class="animated fadeInDown">{{$t('register.title')}}</p>
      <p class="prompt animated fadeInUp">{{$t('register.subTitle')}}</p>
      </div>
      <div class="form-box">
        <h2 style="text-align: center;">{{$t('commome.register')}}</h2>
        <el-form :hide-required-asterisk='true' :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="" prop="phone">
            <el-input maxlength='24' class="user-phone" max="24" 
            type='text' v-model="form.phone" 
            :placeholder="$t('register.phonePlaceholder')">
              <i slot="prepend" class="iconfont icon-shouji1" style="font-size:14px"></i>
            </el-input>
          </el-form-item>
          <el-form-item label=""  prop="code"
           v-if="$store.state.siteInfo.smsDisplay">
            <el-input type='text' class="user-phone" v-model="form.code" 
              :placeholder="$t('register.verificationCode')">
              <el-button v-if="codeshow" @click="getCode" slot="append">Click for authentication code</el-button>
              <el-button v-if="!codeshow" slot="append">{{count}}S</el-button>
              <i slot="prepend" class="iconfont icon-tongguo5" ></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="pwd">
            <el-input type='password' class="user-phone"
             v-model="form.pwd" 
             :placeholder="$t('register.passwordPlaceholder')">
              <i slot="prepend" class="iconfont icon-zu" style="font-size: 15px;"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="pwd2">
            <el-input type='password' class="user-phone" v-model="form.pwd2"
            :placeholder="$t('register.againPasswordPlaceholder')" >
              <i slot="prepend" class="iconfont icon-zu" style="font-size: 15px;"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="invitecode">
            <el-input type='text' class="user-phone"
             v-model="form.invitecode" 
             :placeholder="$t('register.agentCode')">
              <i slot="prepend" class="iconfont icon-tuijian"></i>
            </el-input>
          </el-form-item>
        </el-form>
        <!-- <el-row class="buy-item text-left agree-box">
          <el-checkbox class="check-box" v-model="agree" name="type"></el-checkbox>
          我已閱讀並同意<a @click="agreeDialogVisible = true" href="javascript:;">《註冊協議》</a>和<a
          @click="tradeDialogVisible = true" href="javascript:;">《{{siteInfo.tradeAgreeTitle}}》</a>
        </el-row> -->
        <div slot="footer" class="dialog-footer">
          <el-button class="box-btn" type="primary" :loading="islogin" @click="submit('ruleForm')">Register</el-button>
        </div>
        <div class="text-left" style="text-align: right;">
          {{$t('register.alreadyAccount')}}?<a @click="toLogin">
            {{$t('register.backToLogin')}}</a>
        </div>
      </div>
    </div>
    <div>

    </div>
    <newFooter></newFooter>
    <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
    <!-- <code-dialog ref="codeDialog"></code-dialog> -->
    <!-- <el-dialog
      title="Verification code"
      class="er-code-dialog"
      :center='true'
      :visible.sync="dialogVisible"
      width="400px">
      <div class="dialog-box">
        <el-form :hide-required-asterisk='true' class="demo-form-inline">
          <el-form-item label="" prop="phone">
            <el-input type='text' v-model="form.code2" placeholder="please enter verification code">
              <img slot="append" @click="refreshImg" :src="adminUrl+'/code/getCode.do?time=' + imgCodeTime" height="45px"
                   width="100px">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="text-center dialog-footer">
          <el-button type="primary" @click="checkImg">Confirm</el-button>
        </div>
      </div>

    </el-dialog> -->
    <!-- <el-dialog
      title="Registration Agreement"
      class="agree-dialog"
      :center='true'
      :visible.sync="agreeDialogVisible"
      width="80%">
      <div class="dialog-iframe">
        <div class="content" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
            <p v-for="item in regAgreeText.split('。')" :key="item">{{item}}。</p>
        </div>
        <div slot="footer" class="text-center dialog-footer">
          <el-button type="primary" @click="agreeRegister">I have read and agree to the registration agreement</el-button>
        </div>
      </div>
    </el-dialog> -->
    <el-dialog
      :title="siteInfo.tradeAgreeTitle"
      class="agree-dialog"
      :center='true'
      :visible.sync="tradeDialogVisible"
      width="80%">
      <div class="dialog-iframe">
        <div class="content" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
            <p v-for="item in tradeAgreeText.split('。')" :key="item">{{item}}。</p>
        </div>
        <!-- <iframe class="iframe-box" :src="$store.state.siteInfo.tradeAgree" frameborder="0"></iframe> -->
        <div slot="footer" class="text-center dialog-footer clearfix">
          <el-button type="primary" @click="agreeTrade">I have read and agree{{siteInfo.tradeAgreeTitle}}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  // import CodeDialog from './code'
  import * as api from '../../axios/api'
import newFooter from '@/components/newFooter'

  export default {
    components: {
      HomeHeader,
      newFooter
      // CodeDialog
    },
    props: {},
    data () {
      // let validatePass = (rule, value, callback) => {
      //   if (value === '') {
      //     callback(new Error('請輸入手機號碼或電子郵箱'))
      //   } else {
      //     // let myreg = /^[1][3,4,5,7,8][0-9]{9}$/  //手機號碼驗證
      //     let myreg = /^[0-9]{11}$/ // 手機號碼驗證
      //     if (!myreg.test(value)) {
      //       callback(new Error('請輸入正確的手機號碼或電子郵箱'))
      //     }
      //     callback()
      //   }
      // }
      let validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error( this.$t('register.phonePlaceholder')));
      } 
      callback()
    };
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error( this.$t('register.passwordPlaceholder')))
        } else {
          // let val = value.replace(/\s*/g, '')
          let valuereg = value.replace(/\s*/g, '')
          let myreg = /^[a-zA-Z0-9!@#$%^&*.]{6,12}$/ // 密碼
          if (!myreg.test(valuereg)) {
            callback(new Error( this.$t('register.passwordPlaceholder')))
          }
          callback()
        }
      }
      let validatePass3 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error( this.$t('register.confirmPassword')))
        } else {
          if (value !== this.form.pwd) {
            callback(new Error( this.$t('register.passwordNotMatch')))
          }
          callback()
        }
      }
      return {
        agree: true,
        tradeDialogVisible: false, // 风险揭示
        agreeDialogVisible: false, // 协议弹窗
        dialogVisible: false,
        islogin: false,
        regAgreeText: '',
        tradeAgreeText: '',
        siteInfo: {},
        form: {
          phone: '',
          code: '',
          code2: '',
          pwd: '',
          pwd2: '',
          invitecode: ''
        },
        rule: {
          phone: [
            { required: true, validator: validatePass, message: this.$t('register.passwordNotMatch'), trigger: 'blur' }
          ],
          pwd: [
            { required: true, validator: validatePass2, message: this.$t('register.passwordPlaceholder'), trigger: 'blur' }
            // { min: 6,max:12, message: '密碼最少爲6到12位數', trigger: 'blur' },
          ],
      
          pwd2: [
            { required: true, validator: validatePass3, message: this.$t('register.confirmPassword'), trigger: 'blur' }
          ],
          invitecode: [
            { required: true, message:  this.$t('register.agentCode'), trigger: 'blur' }
          ]
        },
        adminUrl: '',
        timer: null,
        count: 0,
        codeshow: true,
        imgCodeTime: 0
      }
    },
    watch: {},
    computed: {},
    created () {},
    async mounted () {
      await this.getInfoSite()
      if (this.$route.query.code) {
        this.form.invitecode = this.$route.query.code
      }

    },
    methods: {
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
      },
      async getInfoSite () {
        // 獲取網站信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
          if(this.siteInfo.smsDisplay === false){
              this.form.code = '6666'
          }
          this.regAgreeText = this.siteInfo.regAgreeText
          this.tradeAgreeText = this.siteInfo.tradeAgreeText
          this.$store.state.siteInfo = this.siteInfo
          this.form.invitecode = this.siteInfo.agentCode
        } else {
          this.$message.error(result.msg)
        }
      },
      submit (formName) {
        // 提交
        this.$refs[formName].validate(async (valid) => {
          if (valid) {
         
            let opts = {
              // agentCode:'4023', // SR330001
              phone: this.form.phone,
              yzmCode: this.form.code,
              userPwd: this.form.pwd,
              agentCode: this.form.invitecode
            }
            this.islogin = true
            let data = await api.register(opts)
            if (data.status === 0) {
              this.$message.success(this.$t('register.registerSuccess'))
              this.$router.push('/login')
            } else {
              this.$message.error(data.msg)
            }
            this.islogin = false
          } else {
            return false
          }
        })
      },
      getCode () {
        if (!this.form.phone) {
          this.$message.error('Please enter your phone number')
        } else {
          // let myreg = /^[1][3,4,5,7,8][0-9]{9}$/  //手機號碼驗證
          let myreg = /^[0-9]{11}$/ // 手機號碼驗證
          if (!myreg.test(this.form.phone)) {
            this.$message.error('Please enter the correct phone number')
          } else {
            this.checkPhone()
          }
        }
      },
      async checkPhone () {
        // 先驗證是否已經註冊
        let data = await api.checkPhone({ phoneNum: this.form.phone })
        if (data.status === 0) {
          // 如果用戶已存在返回 0
          this.$message.error(this.$t('register.registered'))
          this.$router.push('/login')
        } else {
          this.adminUrl = process.env.API_HOST
          if (this.adminUrl === undefined) {
            this.adminUrl = ''
          }
          // this.dialogVisible = true
          this.getcode()
        }
      },
      async checkImg () {
        if (!this.form.code2) {
          this.$message.error('The verification code you entered is incorrect, please re-enter')
          return
        }
        // await this.checkCode()
        let data = await api.checkCode({ code: this.form.code2 })
        if (data === 'true' || data === true) {
          this.getcode()
          this.dialogVisible = false
        } else {
          this.refreshImg()
          this.$message.error('The verification code you entered is incorrect, please re-enter')
        }
      },
      async getcode () {
        //   var sign  = this.$md5(this.phone+'W&WzL42v').toUpperCase()
        let result = await api.getCode({ phoneNum: this.form.phone })
        if (result.status === 0) {
          const TIME_COUNT = 60
          if (!this.timer) {
            this.count = TIME_COUNT
            this.codeshow = false
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
            this.$message.error(result.msg)
          }
        } else {
          this.$message.error(result.msg)
        }
      },
      toLogin () {
        // 去注册
        this.$store.state.loginIsShow = false
        this.$router.push('/login')
      },
      agreeRegister () {
        // this.agree = true
        this.agreeDialogVisible = false
      },
      agreeTrade () {
        // this.agree = true
        this.tradeDialogVisible = false
      }
    }
  }
</script>
<style lang="less" scoped>
/deep/ .user-phone input {
  border: none;
  margin-left: 10px;
  
}
/deep/ .login-box .el-input-group__prepend {
  left: 12px;
}
.login-box .text-right[data-v-23b3be22]{
      margin: 25px 0 22px;
      
}
.login-box .form-box[data-v-23b3be22]{
  
    height: 400px;
    border-radius: 5px;
}
.black-bg {
  .user-phone {
    background-color: #2f2f2f;
    border-radius: 50px;
  }
  .el-button--primary{
    background-color: rgb(2, 77, 161) !important;
    border: none;
    border-radius: 50px;
  }
}
.red-bg {
  .user-phone {
    
    border: 1px solid rgb(150, 150, 150);
    border-radius: 50px;
  }
  .el-button--primary{
    border: none;
    border-radius: 50px;
  }
}
  .header {
    height: 60px;
    // background: #fff;
    // padding: 0 20px;

    img {
      height: 58px;
      line-height: 58px;
    }
  }

  .login-box {
    width: 80%;
    margin: 0 auto;
    height: 540px;
    position: relative;

    .form-box {
      width: 320px;
      height: 500px;
      text-align: center;
      position: absolute;
      right: 0;
      top: 32px;
      // background: #fff;
      z-index: 2;
      padding: 0 40px;
      border-radius: 4px;

      h2 {
        height: 30px;
        line-height: 30px;
        text-align: left;
        font-size: 26px;
        padding-top: 30px;
      }

      .el-form {
        padding-top: 20px;

      }
    }

    .dialog-footer {
      margin-top: 15px;
    }
  }

  .login-box .text-left {
    padding: 10px 0;
  }

  .er-code-dialog {
    /deep/ .el-input-group__append {
      padding: 0;
      border: none;
    }

    /deep/ .el-input__inner {
      height: 45px;
      line-height: 45px;
    }

    /deep/ .el-button--primary {
      width: 80%;

    }
  }

  .dialog-iframe {
    .iframe-box {
      width: 100%;
      height: 460px;
    }

    .dialog-footer {
      //  height: 300px;
      margin-top: 20px;
    }
  }

  .form-box {
    .agree-box {
      padding: 10px 0px 0px;
      font-size: 12px;
      line-height: 20px;
    }

    .dialog-footer[data-v-94470b58] {
      margin-top: 10px;
    }
  }

  .login-box .top-box {
    position: absolute;
    top: 34%;
    width: 50%;
    height: 150px;
    left: 0;
    right: auto;
    background: none;

    .prompt {
      color: #b6b6b6;
      font-size: 18px;
    }

    p {
      color: #efbb53;
      font-size: 34px;
      margin-bottom: 40px;
    }
  }
</style>
