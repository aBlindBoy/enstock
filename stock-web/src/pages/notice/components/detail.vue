<template>
  <el-container>
    <el-header>
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <el-main>

        <div class="introduce">
          <h2 class="title text-center">{{artList.artTitle}}</h2>
          <div class="introduce-body">

            <p class="text-center origin">
              {{artList.author}} {{artList.addTime | timeFormat}}
              <span class="pull-right"><i
                class="iconfont icon-yanjing"></i>{{artList.hitTimes?artList.hitTimes:0}}</span>
            </p>
            <div class="summary">
              <pre>{{artList.artSummary}}</pre>
            </div>
            <div class="risk-box">
              <img class="cert-img" :src="artList.artImg" alt="">
            </div>
            <div class="content">
              <pre>{{artList.artCnt}}</pre>
            </div>
            <div class="bottom-box ">
              <p class="pull-right">{{siteInfo.siteName}}</p>
              <div class="clearfix">
                <p class="pull-right">{{artList.addTime | timeFormat}}</p>
              </div>
            </div>
          </div>
        </div>
        <home-footer :siteInfo="siteInfo"></home-footer>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import HomeHeader from '../../../components/HeaderOrder'
  import HomeFooter from '../../../components/Footer'
  import * as api from '../../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter
    },
    props: {},
    data () {
      return {
        bannerList: [],
        market: {}, // 大盘详情
        artList: {}, // 公告列表
        siteInfo: {},
        timer: null
      }
    },
    watch: {},
    computed: {},
    mounted () {
      this.getArtList()
      this.getInfoSite()
    },
    methods: {
      async getInfoSite () {
        // 获取大盘指数
        let result = await api.getInfoSite()
        if (result.status === 0) {
          this.siteInfo = result.data
        } else {
          this.$message.error(result.msg)
        }
      },
      async getArtList () {
        // 获取公告详情
        let result = await api.getArtDetail({ artId: this.$route.query.id })
        // let result = await api.getArtList()
        if (result.status === 0) {
          this.artList = result.data
        } else {
          this.$message.error(result.msg)
        }
      }
    }
  }
</script>
<style lang="less" scoped>
  .introduce {
    padding: 20px;
    min-height: 400px;
    width: 1200px;
    margin: 40px auto;

    .title {
      font-size: 20px;
      line-height: 38px;
    }

    .introduce-body {
      line-height: 28px;
      padding-top: 20px;
      text-indent: 2em;
      font-size: 14px;
      width: 800px;
      margin: 0 auto;

      p {
        margin-bottom: 10px;
      }

      .origin {
        font-size: 12px;
        color: #9c9c9c;
        margin: 10px 0;
      }

      .content {
        font-size: 18px;
        text-indent: 2em;
        text-indent: 2em;
        margin-top: 20px;
      }

      .bottom-box {
        padding-top: 60px;
      }
    }

    .risk-box {
      margin-top: 25px;
      text-align: center;
    }

    .cert-img {
      max-height: 300px;
    }

  }

  pre {
    white-space: pre-wrap;
    word-wrap: break-word;
  }
</style>
