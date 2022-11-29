<template>
  <div class="wrapper">
    <!-- <div class="header">
      <mt-header :title="type+'金融帳戶'">
        <router-link to="/card" slot="left">
          <mt-button icon="back">返回</mt-button>
        </router-link>
      </mt-header>
    </div> -->
    <div class="box transaction">
      <div class="box-contain clearfix">
        <div class="empty text-center" style="margin-top: 0.3rem;">
          <!-- 您passed實名認證 -->
          <i style="color:red;font-size: 1.2rem;margin-top:0.2rem" class="iconfont icon-chongzhi2"></i>
        </div>
      </div>
    </div>
    <div class="form-block page-part">
      <mt-field label="financial name" placeholder="Please enter financial name (full name)" type="text" v-model="bankName"></mt-field>
      <mt-field label="Account opening branch" placeholder="Need to be accurate to the branch or sub-branch" type="text" v-model="bankAddress"></mt-field>
      <mt-field label="financial account number" placeholder="Please enter financial account number" v-model="bankNo"></mt-field>
    </div>
    <!-- <div class="form-block page-part">
        <mt-field label="持卡人姓名" placeholder="和金融帳戶綁定一致" type="text" v-model="username"></mt-field>
        <mt-field label="金融預留手機號" placeholder="請輸入持卡人手機號" type="text" v-model="username"></mt-field>
    </div> -->
    <div class="rule-box">
      <div class="title">hint:</div>
      <ul>
        <li>1、New users must register by adding a financial account.</li>
        <li>2、Users can only add one personal financial account.</li>
        <li>3、The real name must be the same as the account name of the withdrawal financial account.</li>
      </ul>
    </div>
    <div class="btnbox">
      <span class="text-center btnok" @click="toSure">Confirm</span>
    </div>

  </div>
</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'
import { isNull, bankNoReg, isName } from '@/utils/utils'

export default {
  components: {},
  props: {},
  data () {
    return {
      bankName: '',
      bankNo: '',
      bankAddress: '', // 支行地址
      type: 'Add to'
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.type = this.$route.query.type ? 'Revise' : 'Add to'
    if (this.$store.state.bankInfo) {
      this.bankName = this.$store.state.bankInfo.bankName
      this.bankNo = this.$store.state.bankInfo.bankNo
      this.bankAddress = this.$store.state.bankInfo.bankAddress
    }
  },
  methods: {
    async toSure () {
      // 添加金融帳戶
      if (isNull(this.bankNo) ) {
        Toast('Please enter financial account number')
      } else if (isNull(this.bankName) || !isName(this.bankName)) {
        Toast('Please enter financial name')
      } else if (isNull(this.bankAddress) || !isName(this.bankAddress)) {
        Toast('Please enter the account opening branch')
      } else {
        let opts = {
          bankName: this.bankName,
          bankNo: this.bankNo,
          bankAddress: this.bankAddress
        }
        if (this.$route.query.type === 'edit') {
          let data = await api.updateBankCard(opts)
          if (data.status === 0) {
            Toast('Successfully modified!')
            this.$router.push('/card')
          } else {
            Toast(data.msg)
          }
        } else {
          let data = await api.addBankCard(opts)
          if (data.status === 0) {
            Toast('Added successfully!')
            this.$router.push('/card')
          } else {
            Toast(data.msg)
          }
        }
      }
    },
    goBack () {
      this.$router.back(-1)
    }
  }
}
</script>
<style lang="less" scoped>
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

  .transaction {
    // padding-bottom: 0.2rem;
  }
</style>
