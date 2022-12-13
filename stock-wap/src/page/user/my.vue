<template>
  <div class="wrapper">
    <div class="header">
      <mt-header :title="$t('setting.title')">
        <router-link to="/user" slot="left">
          <mt-button icon="back">{{$t('setting.back')}}</mt-button>
        </router-link>
      </mt-header>
    </div>
    <div class="form-block">
      <mt-field :label="$t('setting.actualName')" :placeholder="$t('setting.actualName')" type="text" disabled
                v-model="$store.state.userInfo.realName"></mt-field>
      <mt-field :label="$t('setting.cellphoneNumber')" type="text" disabled v-model="$store.state.userInfo.phone"></mt-field>
    </div>
    <div class="form-block">
      <mt-field :label="$t('setting.loginPassword')" @click.native="changeLogin"
       autocomplete="new-password" 
                type="password" disabled>
        <span @click="changeLogin"><i class="iconfont icon-xiugai"></i>{{$t('setting.changePassword')}}</span>
      </mt-field>
    </div>
    <div class="btnbox">
      <span class="text-center btnok loginout" @click="toRegister">{{$t('setting.exitSystem')}}</span>
    </div>
    <!-- 修改密碼 -->
    <mt-popup v-model="changeLoginPsdBox" position="bottom" class="mint-popup-wrap">
      <div class="clearfix">
        <a @click="changeLoginPsdBox = false" class="pull-right"><i class="iconfont icon-weitongguo"></i></a>
      </div>
      <div class="form-block">
        <mt-field :label="$t('setting.oldPassword')" type="password"
         :placeholder="$t('setting.pleasePassword')" v-model="nextPsd"></mt-field>
        <mt-field :label="$t('setting.newPassword')" 
        :placeholder="$t('setting.passwordFormat')" type="password" v-model="newPsd"></mt-field>
      </div>
      <div class="text-center">
        <mt-button class="btn-sure" type="default" @click="changeLoginPsd">{{$t('common.confirm')}}</mt-button>
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
        Toast(this.$t('setting.newPasswordIsNull'))
      } else if (!pwdReg(this.newPsd)) {
        Toast(this.$t('setting.passwordNotFormat'))
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
