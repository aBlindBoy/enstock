<template>
  <div class="wrapper">
    <div class="header">
      <mt-header title="帳戶資金互轉">
        <router-link to="/user" slot="left">
          <mt-button icon="back">Mine</mt-button>
        </router-link>
      </mt-header>
    </div>

    <div class="form-block">
    
      <mt-field label="" type="number" @input="inputFrom"  v-model="fromAmount">
                <!-- <span @click="selectAll1">all</span> -->
                <mt-picker :slots="slots" @change="formCoinCodeChange" :disableClear=true ></mt-picker>
        </mt-field>
    </div>
    <div class="form-block">
      <mt-field label="" name="amt" v-model="toAmount" disabled readonly type="number">
        <!-- <span @click="selectAll1">all</span> -->
        <span style="font-size: .36rem;    margin-right: 0.2rem;">{{toCoinCode}}</span>
      </mt-field>
    </div>
    <div style="text-align:center;margin-top: .5rem;">
      <p >美股可用资金 <span class="assets">{{ formartAmount($store.state.userInfo.enableAmt) }}</span> </p>
      <p style="margin-top:.2rem">台股可用资金 <span class="assets">{{ formartAmount($store.state.userInfo.twEnableAmt) }}</span> </p>
    </div>

    <div class="btnbox">
      <span class="text-center btnok loginout" @click="submit">兑换</span>
    </div>

    <div class="rule-box">
      <div class="title">友情提示</div>
      <ul>
        <li style="">
          <div class="number">1</div>
          当前美元和新台币的汇率1:{{exchangeRate.defaultRate}}</li>
        <li>  <div class="number">2</div> 资金兑换,免手续费,立即到账</li>
        <li><div class="number">3 </div> 兑换成功转入到对应的股票账户</li>
      </ul>
    </div>
    <!-- <mt-navbar v-model="selected">
      <mt-tab-item id="1">Financing to Index</mt-tab-item>
      <mt-tab-item id="2">Index refinancing</mt-tab-item>
      <mt-tab-item  id="3">Financing to futures</mt-tab-item>
      <mt-tab-item id="4">Futures refinancing</mt-tab-item>
    </mt-navbar> -->
    <!-- <mt-tab-container class="order-list" v-model="selected">
      <mt-tab-container-item id="1">
        
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
    </mt-tab-container> -->
  </div>
</template>

<script>
import foot from '@/components/foot/foot'
// import '@/assets/style/common.less'
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

import numeral from 'numeral'

export default {
  components: {
    foot
  },
  data () {
    return {
      selected: '1', // 選中
      // form: {
      //   toAmount: 0,
      //   account2: '',
      //   account3: '',
      //   account4: '',
      //   password: ''
      // },
      userInfo: {
        realName: ''
      },
      slots: [
        {
          flex: 1,
          values: ['USD', 'TWD'],
          className: 'slot1',
          textAlign: 'left'
        }, 
      ],
      sheetVisible:true,
      clickIconFlag:false,
      formCoinCode: 'USD',
      toCoinCode:'TWD',
      fromAmount:0,
      toAmount:0,
      exchangeRate:{},
    }
  },
  watch: {},
  computed: {},
  created () {
    this.getExchangeRate()
  },
  mounted () {
    if (this.$route.query.type) {
      this.selected = this.$route.query.type + ''
    }
    this.getUserInfo()
  },
  methods: {
    
    formartAmount(amount){
        return numeral(amount).format('0,0.00')
      },
    async getExchangeRate(){
        let params={
          coinCode:"TWD"
        }
        let data = await api.getExchangeRate(params);
        if (data.status === 0) {
          this.exchangeRate  = data.data;
        } 
      },
      formCoinCodeChange(picker, values) {
          this.formCoinCode  = values[0];
          this.toCoinCode = this.formCoinCode=="TWD"?"USD":"TWD"
          this.inputFrom()
     },
     inputFrom(){
        if (this.fromAmount == 0) {
          return
        }
        if (this.formCoinCode=="USD") {
          this.toAmount =(this.fromAmount * this.exchangeRate.defaultRate).toFixed(2)
        } else {
          this.toAmount =(this.fromAmount / this.exchangeRate.defaultRate).toFixed(2)
        }
      },
    // changeFromCoin(){
    //     this.clickIconFlag = true
    //     setTimeout(()=>{
    //       this.clickIconFlag = false
    //     },1000)
    //     let formCoinCode = this.formCoinCode 
    //     let toCoinCode =   this.toCoinCode 
    //     this.formCoinCode = toCoinCode
    //     this.toCoinCode = formCoinCode
    //     this.inputFrom()
    // },
    // async getProductSetting () {
    //   let data = await api.getProductSetting()
    //   if (data.status === 0) {
    //     this.$store.state.settingForm = data.data
    //     if (!this.$store.state.settingForm.indexDisplay) {
    //       this.selected = '3'
    //     }
    //   } else {
    //     this.$message.error(data.msg)
    //   }
    // },

    async submit(){
        let params ={
          fromCode:this.formCoinCode,
          fromAmount:this.fromAmount,
          toCode:this.toCoinCode
        }
        let data = await api.transfer(params);
        if (data.status === 0) {  
          this.getUserInfo()
          Toast(data.data)
        } else {
          Toast(data.data)
        }
      },
  
    // selectAll1 () {
    //   // 選擇全部
    //   this.form.account1 = this.$store.state.userInfo.enableAmt
    // },
    // selectAll2 () {
    //   // 選擇全部
    //   this.form.account2 = this.$store.state.userInfo.enableIndexAmt
    // },
    // selectAll3 () {
    //   // 選擇全部
    //   this.form.account3 = this.$store.state.userInfo.enableAmt
    // },
    // selectAll4 () {
    //   // 選擇全部
    //   this.form.account4 = this.$store.state.userInfo.enableFuturesAmt
    // },
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
        display:flex;
        align-items: center;
        .number{
          border: 1px solid rgb(233 21 21);
          width: 0.3rem;
          height: 0.3rem;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 0.15rem;
          color: brown;
          margin-right: .1rem;
          font-style:italic;
        }
      }
    }
  }

  .clickIcon{
      animation:fadenum 1s infinite;
  }
  @keyframes fadenum{
    100%{transform:rotate(180deg);}
  }

  .assets{
    color: #b60c0d;
    font-size: 0.4rem;
    font-weight: 600;
    font-family: lightnumber;
  }
</style>
