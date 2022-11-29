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
        <p class="animated fadeInDown">Dedicated line direct connection Institutional trading</p>
        <p class="prompt animated fadeInUp">Institutional investment software tailored for investors</p>
      </div>
      <div class="form-box">
        <h2 style="text-align: center;">Forgot password</h2>
        <el-form :hide-required-asterisk='true' :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="" prop="phone">
            <el-input maxlength="24" max="24" type='text' class="user-phone" v-model="form.phone" placeholder="Please enter your mobile number or email">
              <i slot="prepend" class="iconfont icon-shouji1"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="code">
            <el-input type='text' v-model="form.code"  class="user-phone"  placeholder="Verification code">
              <el-button v-if="codeshow" @click="getCode" slot="append">Click for authentication code</el-button>
              <el-button v-if="!codeshow" slot="append">{{count}}S</el-button>
              <i slot="prepend" class="iconfont icon-tongguo5"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="pwd">
            <el-input type='password' class="user-phone" v-model="form.pwd" placeholder="The password is 6~12 digits, letters or symbols">
              <i slot="prepend" class="iconfont icon-zu" style="font-size: 15px;"></i>
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="pwd2">
            <el-input type='password' class="user-phone" v-model="form.pwd2" placeholder="Please confirm the password again">
              <i slot="prepend" class="iconfont icon-zu" style="font-size: 15px;"></i>
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button class="box-btn" type="primary" :loading="islogin" @click="submit('ruleForm')">Confirm</el-button>
        </div>
        <div class="text-left">
          <!-- 已有帳號？ -->
          <a @click="toLogin">Back to login</a>
        </div>
      </div>
    </div>
    <div>

    </div>
    <newFooter></newFooter>
    <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
    <!-- <code-dialog ref="codeDialog"></code-dialog> -->
    <el-dialog
      title="驗證碼"
      class="er-code-dialog"
      :center='true'
      :visible.sync="dialogVisible"
      width="40%">
      <div class="dialog-box">
        <el-form :hide-required-asterisk='true' class="demo-form-inline">
          <el-form-item label="" prop="phone">
            <el-input type='text' v-model="form.code2" placeholder="Please enter verification code">
              <img slot="append" v-show="adminUrl" @click="refreshImg" :src="adminUrl+'/code/getCode.do?time=' + imgCodeTime" height="45px"
                   width="100px">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="text-center dialog-footer">
          <el-button type="primary" @click="checkImg">Confirm</el-button>
        </div>
      </div>

    </el-dialog>
  </div>
</template>

<script>
  import HomeFooter from '@/components/Footer'
  import HomeHeader from '@/components/HeaderOrder'
import newFooter from '@/components/newFooter'

  // import CodeDialog from './code'
  import * as api from '@/axios/api'

  export default {
    components: {
      HomeFooter,
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
        callback(new Error("Please enter your mobile number or email"));
      } 
      callback()
    };
      let validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('Please enter password'))
        } else {
          let val = value.replace(/\s*/g, '')
          console.log(val)
          let valuereg = value.replace(/\s*/g, '')
          let myreg = /^[a-zA-Z0-9!@#$%^&*.]{6,12}$/ // 密碼
          if (!myreg.test(valuereg)) {
            callback(new Error('Please enter the correct password'))
          }
          callback()
        }
      }
      let validatePass3 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('Please confirm your password'))
        } else {
          if (value !== this.form.pwd) {
            callback(new Error('The two entered passwords do not match'))
          }
          callback()
        }
      }
      return {
        agree: true,
        dialogVisible: false,
        islogin: false,
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
            { required: true, validator: validatePass, message: 'Please enter your mobile number or email', trigger: 'blur' }
          ],
          pwd: [
            { required: true, validator: validatePass2, message: 'Please enter password', trigger: 'blur' }
            // { min: 6,max:12, message: '密碼最少爲6到12位數', trigger: 'blur' },
          ],
          code: [
            { required: true, message: 'please enter verification code', trigger: 'blur' }
          ],
          pwd2: [
            { required: true, validator: validatePass3, message: 'Please confirm your password', trigger: 'blur' }
          ],
          invitecode: [
            { required: true, message: 'Please enter institution code', trigger: 'blur' }
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
    mounted () {
      this.getInfoSite()
      this.form.invitecode = this.$route.query.code
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
            this.adminUrl = ''
          }
          this_.dialogImgShow = true
        }, 500)
      },
      async getInfoSite () {
        // 获取网站信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
          this.$store.state.siteInfo = this.siteInfo
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
              phoneNum: this.form.phone,
              code: this.form.code,
              newPwd: this.form.pwd
            }
            this.islogin = true
            let data = await api.forgetPas(opts)
            if (data.status === 0) {
              this.$message.success('Password changed successfully, please log in')
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
          this.getcode()
          this.adminUrl = process.env.API_HOST
          if (this.adminUrl === undefined) {
            this.adminUrl = ''
          }
          this.dialogVisible = false
        } else {
          // 如果用戶已存在返回 0
          this.$message.error('The phone number is not registered')
          // this.$router.push('/register')
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
        }
      },
      toLogin () {
        // 去注册
        this.$store.state.loginIsShow = false
        this.$router.push('/login')
      }
    }
  }
</script>
<style lang="less" scoped>
/deep/ .login-box .el-input-group__append{
  line-height: 30px;
}
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
        margin-bottom: 20px;
      }

      .el-form {
        padding-top: 20px;

      }
    }

    .dialog-footer {
      margin-top: 40px;
    }
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
