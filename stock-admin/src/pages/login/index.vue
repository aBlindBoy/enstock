<template>
  <div class="wrapper">
    <div class="lowin">
      <div class="lowin-brand bounceOut">
        <img :src="siteInfo.siteLogoSm" alt="logo">
      </div>
      <div class="lowin-wrapper">
        <div class="lowin-box lowin-login">
          <div class="lowin-box-inner">
            <!-- <el-form class="demo-form-inline" size="small"> -->
            <p>管理系统登录</p>
            <div class="lowin-group">
              <label>用户名 <a href="#" class="login-back-link">登录?</a></label>
              <input v-model="phone" max="11" maxlength="11" type="name" placeholder="用户名" name="email"
                     class="lowin-input">
            </div>
            <div class="lowin-group password-group">
              <label>密码</label>
              <input v-model="password" type="password" name="password" placeholder="密码" class="lowin-input">
            </div>
            <div class="lowin-group password-group">
              <label>验证码</label>
              <img @click="refreshImg" class='code-img' :src="adminUrl+'/code/getCode.do?time=' + imgCodeTime" alt="验证码">
              <input v-model="code2" @keyup.enter="tologin" type="text" placeholder="验证码" name="password"
                     class="lowin-input">
            </div>
            <button @click="tologin" class="lowin-btn login-btn">
              登录
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
import APIUrl from '@/axios/api.url' // 引入api.url.js
import cookies from 'vue-cookies'

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
      siteInfo: '',
      imgCodeTime: 0
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    // cookies.set('ADMIN_TOKEN','ADMINACAFB6A98F8125F0F177773EB5E77563')
    this.adminUrl = APIUrl.baseURL
    if (this.adminUrl === undefined) {
      this.adminUrl = ''
    }
    this.getSiteInfo()
  },
  methods: {
    toRegister () {
      // 注册

    },
    async tologin () {
      // 登录
      if (isNull(this.phone) || !isPhone(this.phone)) {
        this.$message.error('请输入正确的手机号码')
      } else if (isNull(this.password)) {
        this.$message.error('请输入密码')
        // }else if(!this.checkCode()){
        // 验证图形码是否正确
        //     this.$message.error('请输入正确图形验证码')
        //     return
      } else {
        let opts = {
          adminPhone: this.phone,
          adminPwd: this.password,
          verifyCode: this.code2
        }
        let data = await api.login(opts)
        if (data.status === 0) {
          this.$store.state.userInfo.phone = this.phone
          this.$store.state.userInfo.adminName = data.data.adminName
          this.$store.state.userInfo.id = data.data.id
          window.localStorage.setItem('adminName', data.data.adminName)
          window.localStorage.setItem('id', data.data.id)
          // this.setCookie('USER_TOKEN', data.cookie)
          //cookies.set(data.data.key,data.data.token)
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
        this_.adminUrl = APIUrl.baseURL //process.env.API_HOST
        if (this_.adminUrl === undefined) {
          this_.adminUrl = ''
        }
        this_.dialogImgShow = true
      }, 500)
    },
    async checkCode () {
      let data = await api.checkCode({ code: this.code2 })
      return data
    },
    async getSiteInfo () {
      // 获取站点信息
      let data = await api.getInfo()
      if (data.status === 0) {
        this.siteInfo = data.data
        this.$store.state.siteInfo = data.data
      } else {
        this.$message.error(data.msg)
      }
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
    background: #fff;
  }

</style>
