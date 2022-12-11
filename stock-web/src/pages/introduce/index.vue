<template>
  <el-container class="introduceIndex">
    <el-header>
      <home-header></home-header>
    </el-header>
    <backdrop1>
      <slot>
        <div class="bancot">
          <div class="description">
            <div class="tit">
              <!-- <span class="zh">公司簡介</span> -->
              <span class="en">{{$t('introduce.companyProfile')}}</span></div>
          </div>
        </div>
      </slot>
    </backdrop1>
    <div class="main">
      <div class="cot">
        <div class="one">
          <layout>
            <div slot='left'><div class="img"><img src="../../../static/newimg/jianjie1.png" alt=""></div></div>
            <div slot='right'>
              <!-- {{siteInfo.siteIntro}} -->
              <div class="text"><div class="hongtiao"></div><div class="word">
                {{$t('introduce.one')}}</div></div>
              </div>
          </layout>
        </div>
        <div class="two">
          <layout>
            <div slot='left'><div class="text"><div class="hongtiao"></div><div class="word">
              {{$t('introduce.two')}}</div></div></div>
            <div slot='right'><div class="img"><img src="../../../static/newimg/jianjie2.png" alt="">
            
            </div></div>
          </layout>
        </div>
        <div class="three">
          <layout>
            <div slot='left'><div class="img"><img src="../../../static/newimg/jianjie3.png" alt=""></div></div>
            <div slot='right'><div class="text"><div class="hongtiao"></div><div class="word">
              {{$t('introduce.three')}}</div></div></div>
          </layout>
        </div>
      </div>
    </div>
    <newFooter></newFooter>
  </el-container>
</template>

<script>
  import HomeHeader from '../../components/HeaderOrder'
  import newFooter from '../../components/newFooter'
  import backdrop1 from '../../components/backdrop1'
  import layout from '../../components/layout'
  import * as api from '../../axios/api'

  export default {
    components: {
      HomeHeader,
      newFooter,
      backdrop1,
      layout
    },
    props: {},
    data () {
      return {
        bannerList: [],
        market: {}, // 大盘详情
        siteInfo: {}, // 站点信息
        timer: null,
        // 分隔信息
        companyInfoOne:'',
        companyInfoTwo:''
      }
    },
    watch: {},
    computed: {},
    created () {
      // this.timer = setInterval(this.refreshList, 5000)
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getInfoSite()
    },
    methods: {
      async getInfoSite () {
        // 获取大盘指数
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
          var str = this.siteInfo.companyInfo
          this.companyInfoOne = str.substring(str.indexOf('自'),str.indexOf('嘉信投資國際有限公司'))
          this.companyInfoTwo = str.substring(str.indexOf('嘉信投資國際有限公司'),99999)
          console.log(this.companyInfoTwo)
        } else {
          this.$message.error(result.msg)
        }
      }
    }
  }
</script>
<style lang="less" scoped>
</style>
