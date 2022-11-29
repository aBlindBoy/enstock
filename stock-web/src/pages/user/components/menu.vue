<template>
  <div class="nav-left menu" style="">
    <el-menu class="h-nav-menu" :open='openedMenu' :collapse-transition='false' :default-openeds="['2']"
             :default-active="$store.state.userMenu" :unique-opened='true'>

      <el-menu-item class="banner-menu first" index="disabled" disabled>
        <i class="iconfont icon-user"></i>
        personal center
      </el-menu-item>
      <router-link to="/user" tag="li">
        <el-menu-item index="2-1"><span class="iconfont icon-yuandianxiao"></span>account assets</el-menu-item>
      </router-link>
      <router-link to="/message" tag="li">
        <el-menu-item index="2-13">
          <div class="el-badge item-mark"><span class="iconfont icon-yuandianxiao"></span>Station news
            <!-- <sup class="el-badge__content el-badge__content--undefined is-fixed" style="position:absolute;transform: translateY(-50%) translateX(100%);top: 19px;left: 35px;right: auto;">
              {{unreadCount}}</sup> -->
          </div>
        </el-menu-item>
      </router-link>
       <router-link to="/xingu" tag="li">
        <el-menu-item index="2-14"><span class="iconfont icon-yuandianxiao"></span>IPO subscription</el-menu-item>
      </router-link>

      <!-- <template v-if="$store.state.productSetting.fundsDisplay">
        <el-menu-item class="banner-menu" index="disabled" disabled  v-if="$store.state.productSetting.fundsDisplay">
          <i class="iconfont icon-jiaoyi"></i>
          Distribution allocation
        </el-menu-item>
        <router-link to="/funds" tag="li" v-if="$store.state.productSetting.fundsDisplay">
          <el-menu-item  index="2-21"><span class="iconfont icon-yuandianxiao"></span>Allotment</el-menu-item>
        </router-link>
        <router-link to="/days" tag="li" v-if="$store.state.productSetting.fundsDisplay">
          <el-menu-item index="2-22"><span class="iconfont icon-yuandianxiao"></span>Daily allocation</el-menu-item>
        </router-link>
        <router-link to="/applyfund" tag="li" v-if="$store.state.productSetting.fundsDisplay">
          <el-menu-item index="2-23"><span class="iconfont icon-yuandianxiao"></span>My allocation</el-menu-item>
        </router-link>
        <router-link to="/fundsholdposition" tag="li" v-if="$store.state.productSetting.fundsDisplay">
          <el-menu-item index="2-24"><span class="iconfont icon-yuandianxiao"></span>Funding positions</el-menu-item>
        </router-link>
        <router-link to="/fundssellorder" tag="li" v-if="$store.state.productSetting.fundsDisplay">
          <el-menu-item index="2-25"><span class="iconfont icon-yuandianxiao"></span>Allotment and liquidation</el-menu-item>
        </router-link>
      </template> -->
     

      <el-menu-item class="banner-menu" index="disabled" disabled>
        <i class="iconfont icon-jiaoyi1"></i>
        Trade
      </el-menu-item>
      <router-link to="/holdposition" tag="li">
        <el-menu-item index="2-2"><span class="iconfont icon-yuandianxiao"></span>US stock holdings</el-menu-item>
      </router-link>
      <router-link to="/sell" tag="li">
        <el-menu-item index="2-3"><span class="iconfont icon-yuandianxiao"></span>US stock closing</el-menu-item>
      </router-link>
      <router-link v-show="indexDisplay" to="/indexholdposition" tag="li">
        <el-menu-item index="indexhold"><span class="iconfont icon-yuandianxiao"></span>Index positions</el-menu-item>
      </router-link>
      <router-link v-show="indexDisplay" to="/indexsell" tag="li">
        <el-menu-item index="indexsell"><span class="iconfont icon-yuandianxiao"></span>Index close</el-menu-item>
      </router-link>
      <router-link v-show="futuresDisplay" to="/futuresholdposition" tag="li">
        <el-menu-item index="futureshold"><span class="iconfont icon-yuandianxiao"></span>futures position</el-menu-item>
      </router-link>
      <router-link v-show="futuresDisplay" to="/futuressell" tag="li">
        <el-menu-item index="futuressell"><span class="iconfont icon-yuandianxiao"></span>futures close</el-menu-item>
      </router-link>


      <el-menu-item class="banner-menu" index="disabled" disabled>
        <i class="iconfont icon-zichanshuoming"></i>
        asset Management
      </el-menu-item>
      <router-link to="/recharge" tag="li">
        <el-menu-item index="2-8"><span class="iconfont icon-yuandianxiao"></span>Recharge</el-menu-item>
      </router-link>
      <router-link to="/withdraw" tag="li">
        <el-menu-item index="2-7"><span class="iconfont icon-yuandianxiao"></span>Withdrawal</el-menu-item>
      </router-link>
      <!--   -->
      <router-link
      v-show="indexDisplay || futuresDisplay"
                   to="/changeCash" tag="li">
        <el-menu-item index="change"><span class="iconfont icon-yuandianxiao"></span>Asset transfer</el-menu-item>
      </router-link>
      <router-link to="/rechargelist" tag="li">
        <el-menu-item index="2-5"><span class="iconfont icon-yuandianxiao"></span>Recharge records</el-menu-item>
      </router-link>
      <router-link to="/withdrawlist" tag="li">
        <el-menu-item index="2-6"><span class="iconfont icon-yuandianxiao"></span>Withdrawal records</el-menu-item>
      </router-link>
      <router-link to="/detaillist" tag="li">
        <el-menu-item index="2-4"><span class="iconfont icon-yuandianxiao"></span>Funding Details</el-menu-item>
      </router-link>
      

      
      <el-menu-item class="banner-menu" index="disabled" disabled>
        <i class="iconfont icon-anquan"></i> Security center
      </el-menu-item>
      <router-link to="/auth" tag="li">
        <el-menu-item index="2-9"><span class="iconfont icon-yuandianxiao"></span>Verified</el-menu-item>
      </router-link>
      <router-link to="/bank" tag="li">
        <el-menu-item index="2-10"><span class="iconfont icon-yuandianxiao"></span>financial account</el-menu-item>
      </router-link>
      <router-link to="/changepwd" tag="li">
        <el-menu-item index="2-11"><span class="iconfont icon-yuandianxiao"></span>change Password</el-menu-item>
      </router-link>
      <!-- <el-menu-item class="book" index="2-12">
        <a style="margin-left:-40px;"  href="javascript:;" @click="tradeDialogVisible = true">{{$store.state.siteInfo.tradeAgreeTitle}}</a>
      </el-menu-item> -->
    </el-menu>
    <el-dialog
      :title="$store.state.siteInfo.tradeAgreeTitle"
      class="agree-dialog"
      :center='true'
      :visible.sync="tradeDialogVisible"
      width="80%">
      <div class="dialog-iframe">
        <div class="content" style="margin:20px;background:#fff;border-radius:5px;margin-top:60px;line-height:200%;">
            <p v-for="item in $store.state.siteInfo.tradeAgreeText.split('。')" :key="item">{{item}}。</p>
        </div>
        <!-- <iframe class="iframe-box" :src="$store.state.siteInfo.tradeAgree" frameborder="0"></iframe> -->
        <div slot="footer" class="text-center dialog-footer clearfix">
          <el-button type="primary" @click="agreeTrade">I have read and agree{{$store.state.siteInfo.tradeAgreeTitle}}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as api from '../../../axios/api'

  export default {
    name: 'MenuNav',
    components: {},
    props: {
      // index:{
      //   type:Array
      // }
    },
    data () {
      return {
        contentStyleObj: {
          height: ''
        },
        adminUrl: '',
        index: ['2'],
        unreadCount: 0,
        tradeDialogVisible: false,
        indexDisplay:this. $store.state.productSetting.indexDisplay,
        futuresDisplay:$store.state.productSetting.futuresDisplay
      }
    },
    watch: {},
    computed: {
     
    },
    created () {
      // 零時解決報錯
      let data = {
        'indexDisplay': true
      }
      this.$store.state.productSetting = data

      // window.addEventListener('resize', this.getHeight)
      // this.getHeight()
      if (!this.$store.state.userInfo.adminName) {
        this.$store.state.userInfo.adminName = window.localStorage.getItem('adminName')
      }
      if (!this.$store.state.getProductSetting) {
        this.getProductSetting()
      }
    },
    destroyed () {
      window.removeEventListener('resize', this.getHeight)
    },
    mounted () {
      this.adminUrl = process.env.API_HOST
      if (this.adminUrl === undefined) {
        this.adminUrl = ''
      }
      // this.getInfoSite()
      this.getUnreadCount()
    },
    methods: {
      getHeight () {
        this.contentStyleObj.height = window.innerHeight - 2 + 'px'
      },
      async getInfoSite () {
        // 獲取網站信息
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.$store.state.siteInfo = this.siteInfo
        } else {
          this.$message.error(result.msg)
        }
      },
      openedMenu (index) {
      },
      async getProductSetting () {
        // 獲取網站産品設置信息
        let result = await api.getProductSetting()
        if (result.status === 0) {
          this.$store.state.productSetting = result.data
        } else {
          this.$message.error(result.msg)
        }
      },
      async getUnreadCount () {
        // 查詢用戶未讀消息數
        let result = await api.getUnreadCount()
        if (result.status === 0) {
          this.unreadCount = result.data
        } else {
          //this.$message.error(result.msg)
        }
      },
      agreeTrade () {
        // this.agree = true
        this.tradeDialogVisible = false
      }
    }
  }
</script>
<style lang="less" scoped>
  .logo-img {
    padding: 10px 0;
  }
  .nav-left {
    overflow-y: auto;
    overflow-x: hidden;
    min-height: 500px;

    .iconfont {
      vertical-align: middle;
      margin-right: 8px;
      font-size: 15px;
    }
  }

  .el-menu-item.is-active {
    background: #fff;
  }

  .el-submenu .el-menu {
    display: block !important;
  }

  .h-nav-menu .el-menu-item {
    padding-left: 50px !important;
  }

  .h-nav-menu .el-menu-item.banner-menu {
    padding-left: 20px !important;
  }
</style>
