<template>
  <div class="wrapper">
    <!-- <div class="header">
      <mt-header title="Withdrawal">
        <router-link to="" slot="left">
          <mt-button @click="goBack" icon="back">返回</mt-button>
        </router-link>
      </mt-header>
    </div> -->
    <!-- <div class="text-center">
        <div class="btn-group">
            <a href="javascript:;" class="with-draw-btn on">Withdrawal</a>
            <a href="#/cashlist" class="with-draw-detai-btn ">記錄</a>
        </div>
    </div> -->
    <div>
      <div class="box">
        <div class="box-contain clearfix">
          <div class="account text-center">
            <p class="title">{{$t('withdrawal.withdrawalAmount')}} (USD)</p>
            <p class="red number">{{$store.state.userInfo.enableAmt}}</p>
          </div>
        </div>
      </div>
      <div class="form-block page-part">
        <mt-field :label="$t('withdrawal.amount')" 
        :placeholder="$t('withdrawal.pleaseAmount')" type="number" v-model="number">
          <span @click="changeAllNumber">{{$t('withdrawal.all')}}</span>
        </mt-field>
        <!-- <mt-field label="到帳金融" placeholder="請輸入Withdrawal金額" type="number" v-model="card"></mt-field> -->
        <!-- <mt-field label="手機號" placeholder="請輸入手機號" type="number" v-model="phone"></mt-field> -->
      </div>
      <div class="btnbox">
        <span class="text-center btnok" @click="toSure">{{$t('common.confirm')}}</span>
      </div>
      <!-- <div v-if="!$store.state.bankInfo.bankNo" class="addcard back text-center">
      沒有金融帳戶？<a href="#/addCard">點選添加</a> -->
      <!-- </div> -->
      <div class="attention" style="margin-bottom:10px;">
        <p>{{$t('withdrawal.note')}}</p>
      </div>
      <div class="attention">
        <p>1. {{$t('withdrawal.note1')}}</p>
        <p>2. {{$t('withdrawal.note2')}}</p>
        <p>3. {{$t('withdrawal.note3')}} {{settingInfo.withTimeBegin}} : 00  - {{settingInfo.withTimeEnd}}:00 .</p>
        <p>4. {{$t('withdrawal.note4')}} {{settingInfo.withFeeSingle}} USD. {{$t('withdrawal.note41')}} <span v-if="settingInfo.withFeePercent != 0">, {{$t('withdrawal.note42')}} * {{settingInfo.withFeePercent}}</span>
        </p>
        <p>5.{{$t('withdrawal.note5')}} {{settingInfo.withMinAmt}} USD .</p>
        <!-- <p>6、<span class="red">出金時段內出金一般2小時到帳，出金時間受金融間清算時間影響，各家金融到帳時間不同，最遲T+1次日24點前到帳</span></p> -->
      </div>
      <!-- <div @click="toCashList">
          檢視Withdrawal記錄
      </div> -->
    </div>

  </div>
</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

export default {
  components: {},
  props: {},
  data () {
    return {
      number: '',
      card: '',
      phone: '',
      settingInfo: {
        withMinAmt: 1000,
        withTimeBegin: 13, // WithdrawalStarting time
        withTimeEnd: 15, // Withdrawal結束時間
        withFeeSingle: 3, // Withdrawal單筆手續費
        withFeePercent: 0.008 // Withdrawal單筆手續費
      }
    }
  },
  watch: {},
  computed: {},
  created () {},
  beforeDestroy () {
    if (this.$state.theme =='red') {
      document.body.classList.remove('red-bg')
      document.body.classList.add('black-bg')
    }
  },
  mounted () {
    if (this.$state.theme =='red') {
        document.body.classList.remove('black-bg')
        document.body.classList.add('red-bg')
    }
    this.getSettingInfo()
  },
  methods: {
    changeAllNumber () {
      this.number = this.$store.state.userInfo.enableAmt
    },
    async getSettingInfo () {
      let data = await api.getSetting()
      if (data.status === 0) {
        // 成功
        this.settingInfo = data.data
      } else {
        Toast(data.msg)
      }
    },
    async toSure () {
      // 確定Withdrawal
      //   未實名認證和添加金融帳戶不能Withdrawal
      if (!this.$store.state.userInfo.idCard) {
        Toast(this.$t('withdrawal.verifyRealName'))
        this.$router.push('/authentication')
        return
      }
      if (!this.$store.state.bankInfo.bankNo) {
        Toast(this.$t('withdrawal.verifyFinancial'))
        this.$router.push('/addCard')
        return
      }
      if (!this.number || this.number <= 0) {
        Toast(this.$t('withdrawal.verifyAmount'))
      } else if (this.number - this.settingInfo.withMinAmt < 0) {
        Toast(this.$t('withdrawal.verifyMinAmount') + this.settingInfo.withMinAmt)
      } else {
        let opts = {
          amt: this.number
        }
        let data = await api.outMoney(opts)
        if (data.status === 0) {
          // 成功
          Toast(this.$t('withdrawal.pendingReview') )
          this.$router.push('/cashlist')
        } else {
          Toast(data.msg ? data.msg : this.$t('withdrawal.withdrawalFailed'))
        }
      }
    },
    toCashList () {
      // 檢視Withdrawal記錄
      this.$router.push('/cashlist')
    },
    goBack () {
      this.$router.back(-1)
    }
  }
}
</script>
<style lang="less" scoped>

  .btn-group {
    // background: #f4f4f4;
    text-align: center;
    margin: 0 auto;
    margin-bottom: 0.5rem;

    a {
      display: inline-block;
      text-align: center;
      font-size: 0.29rem;
      height: 0.7rem;
      line-height: 0.6rem;
      width: 1.44rem;
      margin: 0;
      margin-top: 0;
      padding: 0;
      border: 2px solid rgb(182, 12, 13);
      color: rgb(182, 12, 13);
    }

    .with-draw-btn {
      position: relative;
      right: -10px;
      width: 1.51rem;
      border-top-left-radius: 0.695rem;
      border-bottom-left-radius: 0.695rem;
    }

    .with-draw-detai-btn {
      border-top-right-radius: 0.695rem;
      border-bottom-right-radius: 0.695rem;
    }

    .on {
      background: rgb(182, 12, 13);
      color: #fff;
    }
  }

  .account {
    padding-bottom: 0.2rem;

    .title {
      height: 1.4rem;
      line-height: 1.4rem;
      font-size: 0.29rem;
      // color: rgb(51, 51, 51);
      text-align: center;
      // font-weight: 700;
    }

    .number {
      font-size: 0.566rem;
      font-weight: 600;
    }
  }

  .addcard {
    height: 0.834rem;
    line-height: 0.834rem;
    font-size: 0.29rem;
    color: rgb(187, 187, 187);

    a {
      color: #177be1;
    }
  }

  .attention {
    // height: 0.417rem;
    line-height: 0.417rem;
    padding: 0.347rem;
  }
</style>
