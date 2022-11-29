<template>
  <div class="wrapper">
    <div class="header">
      <mt-header title="User Info">
        <router-link to="/user" slot="left">
          <mt-button icon="back">Mine</mt-button>
        </router-link>
      </mt-header>
    </div>
    <div class="form-block">
      <mt-field label="actual name" placeholder="actual name" type="text" disabled
                v-model="$store.state.userInfo.realName"></mt-field>
      <mt-field label="cellphone number" placeholder="手機cellphone number號碼" type="text" disabled v-model="$store.state.userInfo.phone"></mt-field>
    </div>
    <div class="form-block">
      <mt-field label="login password" @click.native="changeLogin" autocomplete="new-password" placeholder="Click to modify the login password"
                type="password" disabled>
        <span @click="changeLogin"><i class="iconfont icon-xiugai"></i>change Password</span>
      </mt-field>
    </div>
    <div class="btnbox">
      <span class="text-center btnok loginout" @click="toRegister">Exit system</span>
    </div>
    <!-- 修改密碼 -->
    <mt-popup v-model="changeLoginPsdBox" position="bottom" class="mint-popup-wrap">
      <div class="clearfix">
        <a @click="changeLoginPsdBox = false" class="pull-right"><i class="iconfont icon-weitongguo"></i></a>
      </div>
      <div class="form-block">
        <mt-field label="Old Password" type="password" placeholder="Please enter old password" v-model="nextPsd"></mt-field>
        <mt-field label="new password" placeholder="The password is 6~12 digits, numbers, letters or symbols" type="password" v-model="newPsd"></mt-field>
      </div>
      <div class="text-center">
        <mt-button class="btn-sure" type="default" @click="changeLoginPsd">Confirm</mt-button>
      </div>
    </mt-popup>
  </div>
</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'
import { isNull, pwdReg } from '@/utils/utils'

export default {
  components: {},
  props: {},
  data () {
    return {
      username: '',
      changeLoginPsdBox: false,
      nextPsd: '',
      newPsd: ''
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {},
  methods: {
    async toRegister () {
      // 登出登陸
      this.clearCookie()
      let data = await api.logout()
      if (data.status === 0) {
        // Toast(data.msg)
        this.$router.push('/login')
      } else {
        Toast(data.msg)
      }
      this.$router.push('/login')
    },
    changeLogin () {
      this.changeLoginPsdBox = true
    },
    async changeLoginPsd () {
      if (isNull(this.nextPsd) || isNull(this.newPsd)) {
        Toast('Please enter old and new password')
      } else if (!pwdReg(this.newPsd)) {
        Toast('The password is 6~12 digits, numbers, letters or symbols')
      } else {
        // 修改密碼
        let opts = {
          oldPwd: this.nextPsd,
          newPwd: this.newPsd
        }
        let data = await api.changePassword(opts)
        if (data.status === 0) {
          this.changeLoginPsdBox = false
          Toast(data.msg)
        } else {
          Toast(data.msg)
        }
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .loginout {
    color: #999;
    border: 0.015rem solid #606060;
    font-size: 0.3rem;
    background: none;
  }

  .mint-popup-wrap {
    width: 100%;
    padding: 0.3rem 0.3rem 0.6rem;

    .btn-sure {
      margin-top: 0.5rem;
      width: 80%;
      color: #fff;
      border: none;
    }
  }

  .btnbox .btnok {
    background: none;
  }
</style>
