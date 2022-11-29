<template>
  <div class="wrapper">
    <div class="lowin">
      <div class="lowin-brand bounceOut">
        <!-- <img src="../../assets/image/logo-mini.png" alt="logo"> -->
        <img class="logo-img" :src="$store.state.siteInfo.siteLogoSm" alt="logo">
      </div>
      <div class="lowin-wrapper">
        <div class="lowin-box lowin-login">
          <div class="lowin-box-inner">
            <!-- <el-form class="demo-form-inline" size="small"> -->
            <p>代理後台登錄</p>
            <div class="lowin-group">
              <label>手機號 <a href="#" class="login-back-link">登录?</a></label>
              <input v-model="phone" max="11" maxlength="11" type="name" name="email" placeholder="手機號"
                     class="lowin-input">
            </div>
            <div class="lowin-group password-group">
              <label>密碼</label>
              <input v-model="password" type="password" name="password" placeholder="密碼" class="lowin-input">
            </div>
            <!--
            <div class="lowin-group password-group">
              <label>验证码</label>
              <img @click="refreshImg" class='code-img' :src="adminUrl+'/code/getCode.do?time=' + imgCodeTime" alt="验证码">
              <input v-model="code2" type="text" @keyup.enter="tologin" placeholder="验证码" name="code"
                     class="lowin-input">
            </div>
            -->
            <button @click="tologin" class="lowin-btn login-btn">
              登錄
            </button>
            <!-- <div class="text-foot">
                            还没有账户? <a href="" class="register-link">注册</a>
                        </div> -->
            <!-- </el-form> -->
          </div>
        </div>
      </div>

      <!-- <footer class="lowin-footer">

          </footer> -->
    </div>
  </div>
</template>

<script>
// import '@/assets/style/login.css'
import * as api from '@/axios/api'
import { isNull, isPhone } from '@/utils/utils'

export default {
  components: {},
  props: {},
  data () {
    return {
      adminUrl: null,
      code2: '',
      name: '',
      phone: '',
      password: '',
      imgCodeTime: 0
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.adminUrl = process.env.API_HOST
    if (this.adminUrl === undefined) {
      this.adminUrl = ''
    }
    this.getSiteInfo()
  },
  methods: {
    async getSiteInfo () {
      // 获取站点信息
      let data = await api.getSiteInfo()
      if (data.status === 0) {
        this.$store.state.siteInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async tologin () {
      // 登录
      if (isNull(this.phone) || !isPhone(this.phone)) {
        this.$message.error('請輸入正確的手機號碼')
      } else if (isNull(this.password)) {
        this.$message.error('請輸入密碼')
        // }else if(!this.checkCode()){
        // 验证图形码是否正确
        //     this.$message.error('请输入正确图形验证码')
        //     return
      } else {
        let opts = {
          agentPhone: this.phone,
          agentPwd: this.password,
          verifyCode: this.code2
        }
        let data = await api.login(opts)
        if (data.status === 0) {
          this.$store.state.userInfo.phone = this.phone
          // this.setCookie('USER_TOKEN',data.cookie)
          this.$router.push('/home')
        } else {
          this.$message.error(data.msg)
        }
      }
    },
    refreshImg () {
      this.adminUrl = ''
      this.imgCodeTime = Date.now()
      this.dialogImgShow = false
      let this_ = this
      setTimeout(() => {
        this_.adminUrl = process.env.API_HOST
        if (this_.adminUrl === undefined) {
          this_.adminUrl = ''
        }
        this_.dialogImgShow = true
      }, 500)
    },
    async checkCode () {
      let data = await api.checkCode({ code: this.code2 })
      return data
    }
  }
}
</script>
<style scoped>
  .lowin .lowin-group.password-group {
    position: relative;
  }

  .code-img {
    float: right;
    width: 100px;
    height: 45px;
    line-height: 45px;
    position: absolute;
    right: 0;
    top: 19px;
    background: #fff;
  }

</style>
