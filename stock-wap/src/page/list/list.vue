<template>
  <div :class="`wrapper ${$state.theme === 'red' ? 'red-theme' : 'black-theme'}`">
    <!-- <mt-header v-if="selected == '2'" fixed  title="">
        <router-link to="/" slot="left">
        </router-link>
        <mt-button slot="right" icon="search" @click="toSearch"></mt-button>
    </mt-header> -->
    <mt-search
        style="height:auto;top: 0;position: absolute;width: 100%;"
        fixed
        @click.enter.native="toSearch"
        :placeholder="$t('common.searchStockCode')"
      >
      </mt-search>
      <!-- :fixed="selected != '2'?true:false" -->
    <mt-navbar class="top-navbar" v-model="currSelectd">
      <mt-tab-item id="1" > <div @click="clickCurrTab('1')">美股排行</div> </mt-tab-item>
      <mt-tab-item  id="2"> <div  @click="clickCurrTab('2')">台股排行</div> </mt-tab-item>
    </mt-navbar>
      <mt-tab-container class="order-list" v-model="currSelectd" >
        <mt-tab-container-item   :id="['1'].includes(currSelectd)?currSelectd:''">
          <mt-navbar  v-model="selected" style="margin-top:.1rem">
            <mt-tab-item id="6" > <div @click="clickTab('6')">NYSE</div> </mt-tab-item>
            <mt-tab-item  id="7"> <div  @click="clickTab('7')">AMEX</div> </mt-tab-item>
            <mt-tab-item  id="8"> <div  @click="clickTab('8')">NASDAQ</div> </mt-tab-item>
          </mt-navbar>
          <mt-tab-container v-model="selected">
              <!-- <mt-tab-container-item id="0">
                  <List0 :changeNavOptions='changeNavOptions'/>
              </mt-tab-container-item> -->
              <!-- <mt-tab-container-item v-if="this.$store.state.settingForm.indexDisplay" id="1">
                <List1 :selectedNumber='selected'/>
              </mt-tab-container-item> -->
              <!-- v-if="this.$store.state.settingForm.stockDisplay"  -->
              <mt-tab-container-item   :id="['6','7','8'].includes(selected)?selected:''">
                <listUsStock ref="listUsStock" :selectedNumber='selected'/>
              </mt-tab-container-item>
              <!-- <mt-tab-container-item v-if="this.$store.state.settingForm.kcStockDisplay" id="3">
                <List3 :selectedNumber='selected'/>
              </mt-tab-container-item>
              <mt-tab-container-item v-if="this.$store.state.settingForm.futuresDisplay" id="4">
                <List4 :selectedNumber='selected'/>
              </mt-tab-container-item> -->
            </mt-tab-container>
        </mt-tab-container-item>

        <mt-tab-container-item   :id="['2'].includes(currSelectd)?currSelectd:''">
          <mt-navbar v-model="twSelected" style="margin-top:.1rem">
            <mt-tab-item id="TSE"> <div>上市排行</div> </mt-tab-item>
            <mt-tab-item  id="OTC"> <div >上櫃排行</div> </mt-tab-item>
          </mt-navbar>
          <mt-tab-container  v-model="twSelected">
              <!-- <mt-tab-container-item id="0">
                  <List0 :changeNavOptions='changeNavOptions'/>
              </mt-tab-container-item> -->
              <!-- <mt-tab-container-item v-if="this.$store.state.settingForm.indexDisplay" id="1">
                <List1 :selectedNumber='selected'/>
              </mt-tab-container-item> -->
              <!-- v-if="this.$store.state.settingForm.stockDisplay"  -->
              <mt-tab-container-item   :id="['TSE','OTC'].includes(twSelected)?twSelected:''">
                <listTwStock ref="listTwStock" :selectedNumber='twSelected'/>
              </mt-tab-container-item>
              <!-- <mt-tab-container-item v-if="this.$store.state.settingForm.kcStockDisplay" id="3">
                <List3 :selectedNumber='selected'/>
              </mt-tab-container-item>
              <mt-tab-container-item v-if="this.$store.state.settingForm.futuresDisplay" id="4">
                <List4 :selectedNumber='selected'/>
              </mt-tab-container-item> -->
            </mt-tab-container>
        </mt-tab-container-item>
    </mt-tab-container>
   
   
    <foot></foot>
  </div>
</template>

<script>
import foot from '@/components/foot/foot'
// import '@/assets/style/common.less'
// import List0 from './list-all'
// import List1 from './list-index'
import listUsStock from './list-us-stock'
import listTwStock from './list-tw-stock'
// import List3 from './list-kechuang'
// import List4 from './list-futures'
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'

export default {
  components: {
    foot,
    // List0,
    // List1,
    listUsStock,
    listTwStock
    // List3,
    // List4
  },
  props: {},
  data () {
    return {
      selected: '6', // 選中,
      currSelectd:"1",
      twSelected:"TSE",
    }
  },
  watch: {
    // selected(newVal,oldVal){
    //   this.selected = newVal
    //   this.$refs.List2.forceUpdate() 
    // }
  },
  computed: {},
  created () {
    console.log(this.selected);
    this.getProductSetting()
    if (!this.$store.state.userInfo.phone) {
      this.getUserInfo()
      // this.$refs.List2.getStock('TSE');
    }
  },
  mounted () {
    if (this.$route.query.index) {
      this.selected = this.$route.query.index
    }
  },
  methods: {
    clickTab(newVal){
      console.log(newVal);
      // this.$refs.listStock.getStock();
    },
    clickCurrTab(newVal){
      this.currSelectd = newVal
    },
    toSearch () {
      this.$router.push('/searchlist')
    },
    changeNavOptions (opts) {
      this.selected = opts
      this.$route.query.index = opts
      console.log( this.selected );
    },
    async getUserInfo () {
      // 獲取用戶信息
      let data = await api.getUserInfo()
      if (data.status === 0) {
        this.$store.state.userInfo = data.data
      } else {
        Toast(data.msg)
      }
    },
    async getProductSetting () {
      let data = await api.getProductSetting()
      // if (data.status === 0) {
      //   this.$store.state.settingForm = data.data
      //   // 1 指數 2 台股 3科創 4 期貨
      //   if (this.$store.state.settingForm.indexDisplay) {
      //     this.selected = '1'
      //   } else if (this.$store.state.settingForm.stockDisplay) {
      //     this.selected = '2'
      //   } else if (this.$store.state.settingForm.kcStockDisplay) {
      //     this.selected = '3'
      //   } else {
      //     this.selected = '4'
      //   }
      // } else {
      //   this.$message.error(data.msg)
      // }
    },
      toSearch() {
      this.$router.push("/searchlist");
    },
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
    border-bottom: 2px solid #d50000;
    text-decoration: none;
  }

  .nav-wrapper {
    padding-top: 0.8rem;
  }

  .mint-tab-container-item {
    // padding-top: 1.2rem;

    .mint-button--default {
      padding: 0 0.2rem;
      font-size: 0.24rem;
      height: 0.5rem;
      margin: 0.2rem 0.2rem 0;
      color: #607d8b;
      box-shadow: 0 0 1px #3b71b9;
      background: none;
    }
  }

  .mint-navbar {
    box-shadow: 0px 0px 4px rgba(6, 0, 1, 0.2);

    .mint-tab-item {
      // background: #21252a;
      padding: 0.2rem 0;

      &.is-selected {
        border: none;
        margin-bottom: 0;
      }

      .mint-tab-item-label {
        font-size: 0.22rem;
      }

      .iconfont {
        display: block;
        font-size: 0.46rem;
        margin-bottom: 0.12rem;
      }
    }
  }

  .top-navbar {
    .mint-tab-item {
      padding: 0.2rem 0;
      width: 1.42rem;
      height: 0.44rem;
      margin: 0.3rem 0.1rem 0 0.1rem;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: 0.01rem;
    }
  }
  .wrapper{
    width: 100%;
    height: 100%;
    position: relative;
    box-sizing: border-box;
    padding-top: 1rem;
    .top-navbar{
      // position: absolute;
      // top: 40;
      // left: 50%;
      // width: 70%;
      // margin-left: -35%;
      background: none;
      box-shadow: none;
      /deep/.mint-tab-item{
        .mint-tab-item-label{
          font-size:0.28rem;
          font-family:MicrosoftYaHeiLight;
          font-weight:400;
          color:rgba(255,255,255,1);
        }
        &.is-selected{
          position: relative;
          background:linear-gradient(0deg,rgba(27,166,208,1),rgba(2,116,150,1));
          &::after{
            position: absolute;
            content: '';
            display: block;
            width: 0.67rem;
            height: 0.06rem;
            background-color: #138EB4;
            bottom: 0;
            left: 50%;
            margin-left: -0.335rem;
            display: none;
          }
        }
      }
    }
    .order-list{
      width: 100%;
      // height: 100%;
      /deep/.mint-tab-container-wrap{
        width: 100%;
        // height: 100%;
      }
    }
  }
  .top-search{
    padding: .2rem;
    .top-search-btn {
      background-color: #16171d;
      padding: .1rem .2rem;
      width: 1.60rem;
      text-align: center;
      border-radius: .2rem;
      color: #fff;
      border: 1px solid rgb(96, 125, 139);
      margin: 0 auto;
    }
  }
  .red-theme{
     .top-search-btn {
       border-color: #000;
       color: #000;
       background-color: #fff;
     }
    .top-navbar{
      /deep/.mint-tab-item{
        background-color: #CBCBCB;
        .mint-tab-item-label{
          color: #000;
        }
        &.is-selected{
          background: #BB1715;
          .mint-tab-item-label{
            color: #fff;
          }
        }
      }
    }
  }
</style>
