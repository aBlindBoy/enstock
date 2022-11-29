<template>
  <div class="wrapper">
    <div class="header">
      <mt-header title="帳戶資金互轉">
        <router-link to="/user" slot="left">
          <mt-button icon="back">Mine</mt-button>
        </router-link>
      </mt-header>
    </div>
    <mt-navbar v-model="selected">
      <mt-tab-item v-if="this.$store.state.settingForm.indexDisplay" id="1">Financing to Index</mt-tab-item>
      <mt-tab-item v-if="this.$store.state.settingForm.indexDisplay" id="2">Index refinancing</mt-tab-item>
      <mt-tab-item v-if="this.$store.state.settingForm.futuresDisplay" id="3">Financing to futures</mt-tab-item>
      <mt-tab-item v-if="this.$store.state.settingForm.futuresDisplay" id="4">Futures refinancing</mt-tab-item>
    </mt-navbar>
    <mt-tab-container class="order-list" v-model="selected">
      <mt-tab-container-item id="1">
        <div class="form-block">
          <mt-field label="transferable amount" placeholder="transferable amount" type="text" disabled
                    v-model="this.$store.state.userInfo.enableAmt"></mt-field>
        </div>
        <div class="form-block">
          <mt-field label="Transfer amount" name="amt" v-model="form.account1" placeholder="Please enter the transfer amount" type="text">
            <span @click="selectAll1">all</span>
          </mt-field>
        </div>
        <!-- <div class="form-block">
            <mt-field label="資金密碼" placeholder="資金密碼" type="password" v-model="form.password"></mt-field>
        </div>
        <p class="prompt">資金密碼預設為登入密碼</p> -->
        <div class="btnbox">
          <span class="text-center btnok loginout" @click="tosubmit">Confirm transfer to index account</span>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="2">
        <div class="form-block">
          <mt-field label="transferable amount" placeholder="transferable amount" type="text" disabled
                    v-model="this.$store.state.userInfo.enableIndexAmt"></mt-field>
        </div>
        <div class="form-block">
          <mt-field label="Transfer amount" v-model="form.account2" placeholder="Please enter the transfer amount" type="text">
            <span @click="selectAll2">all</span>
          </mt-field>
        </div>
        <div class="btnbox">
          <span class="text-center btnok loginout" @click="tosubmit">Confirm transfer to financing account</span>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="3">
        <div class="form-block">
          <mt-field label="transferable amount" placeholder="transferable amount" type="text" disabled
                    v-model="this.$store.state.userInfo.enableAmt"></mt-field>
        </div>
        <div class="form-block">
          <mt-field label="Transfer amount" v-model="form.account3" placeholder="Please enter the transfer amount" type="text">
            <span @click="selectAll3">all</span>
          </mt-field>
        </div>
        <div class="btnbox">
          <span class="text-center btnok loginout" @click="tosubmit">Confirm transfer to futures account</span>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="4">
        <div class="form-block">
          <mt-field label="transferable amount" placeholder="transferable amount" type="text" disabled
                    v-model="this.$store.state.userInfo.enableFuturesAmt"></mt-field>
        </div>
        <div class="form-block">
          <mt-field label="Transfer amount" v-model="form.account4" placeholder="Please enter the transfer amount" type="text">
            <span @click="selectAll4">all</span>
          </mt-field>
        </div>
        <div class="btnbox">
          <span class="text-center btnok loginout" @click="tosubmit">Confirm transfer to financing account</span>
        </div>
      </mt-tab-container-item>
    </mt-tab-container>
  </div>
</template>

<script>
import foot from '@/components/foot/foot'
// import '@/assets/style/common.less'
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

export default {
  components: {
    foot
  },
  data () {
    return {
      selected: '1', // 選中
      form: {
        account1: '',
        account2: '',
        account3: '',
        account4: '',
        password: ''
      },
      userInfo: {
        realName: ''
      }
    }
  },
  watch: {},
  computed: {},
  created () {
    this.getProductSetting()
  },
  mounted () {
    if (this.$route.query.type) {
      this.selected = this.$route.query.type + ''
    }
    this.getUserInfo()
  },
  methods: {
    async getProductSetting () {
      let data = await api.getProductSetting()
      if (data.status === 0) {
        this.$store.state.settingForm = data.data
        if (!this.$store.state.settingForm.indexDisplay) {
          this.selected = '3'
        }
      } else {
        this.$message.error(data.msg)
      }
    },
    selectAll1 () {
      // 選擇全部
      this.form.account1 = this.$store.state.userInfo.enableAmt
    },
    selectAll2 () {
      // 選擇全部
      this.form.account2 = this.$store.state.userInfo.enableIndexAmt
    },
    selectAll3 () {
      // 選擇全部
      this.form.account3 = this.$store.state.userInfo.enableAmt
    },
    selectAll4 () {
      // 選擇全部
      this.form.account4 = this.$store.state.userInfo.enableFuturesAmt
    },
    async tosubmit () {
      // 融資轉指數
      let opt = {
        amt: this.selected === '1' ? this.form.account1 : this.selected === '2' ? this.form.account2 : this.selected === '3' ? this.form.account3 : this.form.account4,
        type: this.selected // 1 融資轉指數 2 指數轉融資
      }
      let data = await api.AmtChange(opt)
      if (data.status === 0) {
        Toast(data.msg)
        this.$router.push('/user')
      } else {
        Toast(data.msg)
      }
    },
    async getUserInfo () {
      // 獲取用戶信息
      let data = await api.getUserInfo()
      if (data.status === 0) {
        this.$store.state.userInfo = data.data
      } else {
        Toast(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .is-selected .mint-tab-item-label:hover {
    text-decoration: none;
  }

  .wrapper /deep/ .mint-tab-item-label {
    font-size: 0.264rem;
  }

  .mint-navbar .mint-tab-item.is-selected {
    // color: #d50000;
    // border-bottom: 2px solid #d50000;
    text-decoration: none;
    margin-bottom: 0;
  }

  .prompt {
    padding: 0.3rem 0 0.2rem 0.7rem;
  }
</style>
