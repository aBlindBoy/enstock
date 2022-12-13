<template>
  <div class="wrapper">
    <!-- <div class="download">
      <div>
        <a :href="siteInfo.siteAndroidUrl" >
          <img src="../../assets/img/android_logo.png" width="39px">
          <p>安卓</p>
          <p>下載</p>
        </a>
      </div>
      <div style="margin-top: 20px;">
        <a :href="siteInfo.siteIosUrl" >
          <img src="../../assets/img/ios_logo.png" width="39px">
          <p>蘋果</p>
          <p>下載</p>
        </a>
      </div>
    </div> -->

    <!-- 搜索框 -->
    <div class="home-search">
      <div class="home-search-me"
      @click="$router.push('/language')"
      >
        <img src="../../assets/ico/international.png" >
      </div>
      <div class="home-search-input">
        <img src="../../assets/ico/fangdajing.png" alt="">
        <input type="text"
        @focus="$router.push('/Searchlist')"
        :placeholder="$t('common.searchStockCode')">
      </div>
      <div class="home-search-ctl">
        <img class="lingdang" src="../../assets/ico/lingdang.png" alt=""
        @click="$router.push('/notify')"
        >
        <img class="pifu" src="../../assets/ico/pifu.png" alt="" @click="$state.toggleTheme()">
      </div>
    </div>
    <!-- 頭部概況 -->
    <div class="account-box zhishu"
    :style="{backgroundImage:`url(${$state.theme=='red'?r_bg:b_bg})`}"
    >
    
      <div class="content ">
        <!-- market -->
        <div 
        :class="i.floatPoint<0?'tab greenBg text-center':'tab redBg text-center'"
        v-for="(i,index) in realTimeQuotesIndexList"
        :key="index">
            <div :index='index' class="name">{{i.indexName}}</div>
            <p :class="changeTextClass[index] == true?'price heartBeat':'price'">
              {{Number(i.currentPoint).toFixed(2)}}
              </p>
            <div class="status">
              <span >{{Number(i.floatPoint).toFixed(2)}}</span>
              <span >{{Number(i.floatRate).toFixed(2)}}%</span>
            </div>
      </div>
      </div>
    </div>
    <!-- 導航路由 -->
    <div class="icon-router clearfix home-ico-router">
      
      <div class="col-xs-3 text-center">
        <a class='icon-wrap animated zoomIn' @click="goList" href="javascript:;">
          <img
          class="icon-img"
          src="../../assets/ico/hangqing.png" alt="">
        </a>
        <p class="icon-title">{{$t('home.quotes')}}</p>
      </div>
      <div class="col-xs-3 text-center">
        <a class='icon-wrap animated zoomIn' @click="goOrderlist" href="javascript:;">
          <img
          class="icon-img"
          src="../../assets/ico/jiaoyi.png" alt="">
        </a>
        <p class="icon-title">{{$t('home.position')}}</p>
      </div>
      <div class="col-xs-3 text-center">
        <a class='icon-wrap animated zoomIn' @click="goInfo" href="javascript:;">
          <img
          class="icon-img"
          src="../../assets/ico/xuanze.png" alt="">
          <!-- <i class="iconfont icon-xinshou"></i> -->
        </a>
        <p class="icon-title">{{$t('home.briefly')}}</p>
      </div>
      <div class="col-xs-3 text-center">
        <a class='icon-wrap animated zoomIn' @click="goMyinfo" href="javascript:;">
          <img
          class="icon-img"
          src="../../assets/ico/wo.png" alt="">
          <!-- <i class="iconfont icon-xinshou"></i> -->
        </a>
        <p class="icon-title">{{$t('home.mine')}}</p>
      </div>
    </div>
    <div class="shadow-box">
      <img class="shadow-ico" src="../../assets/ico/shadow.png" alt="">
    </div>
    <!-- 公告欄 -->
        <div class="col-xs-24 horseLampModule">
          <div class="horseLamp-box" v-if="artList.artTitle" @click="toAltDetail">
            <img class="tzIco" v-show="$state.theme != 'red'" src="../../assets/ico/horn.png"/>
            <img class="tzIco" v-show="$state.theme == 'red'" src="../../assets/ico/horn-red.png"/>
            <div class="wrap">
              <!-- // 外框，固定寬度 -->
              <div ref="box" id="box">
                <!-- // 內部滾動框 -->
                <div id="marquee">{{artList.artTitle}}</div>
                <!-- //展示的文字 -->
                <div id="copy"></div>
                <!-- // 文字副本，為了實現無縫滾動 -->
              </div>
              <div ref='node' id="node">{{artList.artTitle}}</div>
              <!-- //為了獲取text實際寬度 -->
            </div>
            <span class="right">{{new Date(artList.addTime).getFullYear()}}-{{new Date(artList.addTime).getMonth()+1}}-{{new Date(artList.addTime).getDate()}}</span>
          </div>
        </div>

    <!-- <div class="nav-bg page-part" @click="goList">
      <img class="img" src="../../assets/img/shangpinbg.png" alt="shangpinbg">
    </div> -->
    <!-- 輪播圖 
    <div class="swiper-home">
      <mt-swipe :auto="4000">
        <mt-swipe-item
        v-for="banner in bannerList"
        :key="banner.id"
        >
          <a :href="banner.targetUrl || null">
            <img style="width:100%;height: 100%;" :src="banner.bannerUrl" alt="">
          </a>
        </mt-swipe-item>
      </mt-swipe>   
    </div>    -->
    <!-- 輪播圖 -->
    <AllList/>
    <!-- <div class="swiper-ad">
      <mt-swipe :auto="4000">
        <mt-swipe-item
        v-for="banner in bannerList"
        :key="banner.id"
        >
          <a 
          class="banner-ad"
          :href="banner.targetUrl || null">
            <img style="width:100%;height: 100%;" :src="banner.bannerUrl" alt="">
          </a>
        </mt-swipe-item>
      </mt-swipe>   
    </div> -->
    <!-- <div v-show="true" class="box  page-part">
      <div class="box-title">
        <span class="left"></span>大盤指數
      </div>
      <div class="box-contain clearfix">
        <div :class="index < 3?'animated zoomInDown':index == 3?'animated zoomInLeft':index == 5?'animated zoomInRight':index == 4?'animated zoomIn':'animated zoomInUp'" v-for="(i,index) in market" :key="i.key">
        <div :class="i.floatPoint<0?'tab greenBg':'tab redBg'" v-for="(i,index) in market" :key="i.key">
          <p :index='index' class="name">{{i.indexName}}</p>
          <p :class="changeTextClass[index] == true?'price heartBeat':'price'">{{Number(i.currentPoint).toFixed(2)}}</p>
          <div class="status">
            <span :class="i.floatPoint<0?'pifting green':'pifting red'">{{Number(i.floatPoint).toFixed(2)}}</span>
            <span :class="i.floatPoint<0?'Percentage green':'Percentage red'">{{i.floatRate}}%</span>
          </div>
        </div>
      </div>
    </div> -->
    <!-- tab -->
    <news/>



    <foot></foot>
    <!-- <a href="https://088992.kefu.easemob.com/webim/im.html?configId=d5d08347-1fa9-4c95-87a8-c6100336ab58"><img class="meiqia" :src="require('../../../static/img/meiqia.png')"/></a> -->
  </div>

</template>

<script>
import foot from '@/components/foot/foot'
import news from '@/page/home/news'
import AllList from '@/page/list/list-all'
import HomeList from './components/home-list'
import { Toast } from 'mint-ui'
import * as api from '@/axios/api'
import bannerImg from '../../assets/img/banner.png'
import eventBus from '@/event.js'

const dayjs = require('dayjs')
var relativeTime = require('dayjs/plugin/relativeTime')
dayjs.extend(relativeTime)

export default {
  components: {
    foot,
    HomeList,
    AllList,
    news
  },
  props: {},
  data () {
    return {
      news:'tab_0',
      market: [],
      moveStats: false,
      bannerList: '',
      bannerImg: bannerImg,
      // market: {}, // 大盤指數
      changeTextClass: {},
      artList: {}, // 公告列錶
      timer: null,
      timer2: null,
      timer3:null,
      settingForm: {
        futuresDisplay: false,
        indexDisplay: false,
        kcStockDisplay: false,
        stockDisplay: false
      },
      theme: 'black',
      // newsContent1: [], // 財經要聞
      // newsContent2: [], // 經濟數據
      // newsContent3: [], // 全球股市
      // newsContent4: [], // 7*24全球
      // newsContent5: [], // 商品資訊,
      b_bg:require('../../../static/img/bg-zhisu.png'),
      r_bg:require('../../../static/img/bg-zhisu-red.png'),
      siteInfo:null,
      realTimeQuotesIndexList:[],
      optionList:[
          {
              xAxis: {
                type: 'category',
                show: false
              },
              yAxis: {
                type: 'value',
                show: false
              },
              series: [
                {
                  data: [820, 932, 901, 934, 1290, 1330, 1320],
                  type: 'line',
                  smooth: true,
                  areaStyle: {}
                }
              ]
            }

      ],
      activities: [
        // {
        //   content: '支持使用图标',
        //   timestamp: '2018-04-12 20:46',
        //   size: 'large',
        //   type: 'primary',
        //   icon: 'el-icon-more'
        // }, {
        //   content: '支持自定义颜色',
        //   timestamp: '2018-04-03 20:46',
        //   color: '#0bbd87'
        // }, {
        //   content: '支持自定义尺寸',
        //   timestamp: '2018-04-03 20:46',
        //   size: 'large'
        // }, {
        //   content: '默认样式的节点',
        //   timestamp: '2018-04-03 20:46'
        // }
      ]
    }
  },
  created () {
    this.getNewsList()
    this.getProductSetting()
    // this.timer = setInterval(this.getMarket, 10000)
    this.getInfoSite()

    this.getArtList() // 獲取公告
   
  },
  mounted () {
 
    // this.getNewsList(2,1)
    // this.getNewsList(3,7)
    // this.getNewsList(4,5)
    // this.getNewsList(5,1)
    this.getMarket() // 獲取大盤指數
    this.timer3 = setInterval( this.getMarket, 6000)

    // this.getMarket(166) // 獲取大盤指數
    // this.getMarket(8827) // 獲取大盤指數
    // this.getBanner() //獲取banner
 
    // let header = document.querySelector('.header-box')
    let body = document.querySelector('.wrapper')
    // header.style.display = 'none'
    body.style.height = 'calc(100%)'
    body.style.paddingBottom = '0'
  },

  beforeDestroy () {
    // clearInterval(this.timer)
    clearInterval(this.timer2)
    clearInterval(this.timer3)
  },
  computed: {},
  // 更新的時候運動
  updated () {
    if (!this.moveStats) {
      this.move()
    }
  },
  methods: {
    // async getRealTimeQuotesIndex(){
    //   let result = await api.getRealTimeQuotesIndex()
    //   this.realTimeQuotesIndexList = result.data
    // },
    async getInfoSite () {
      // 獲取設定信息
      let result = await api.getInfoSite()
      if (+result.status === 0) {
        this.siteInfo = result.data
      } else {
        this.$message.error(result.msg)
      }
    },
    async getNewsList() {
      // if(type==1 || type==2){
      //   data = await api.getTwNews({limit:30,categoryAll:true});
      // }else{
      //   data = await api.getTwNewsByCategory({category});
      // }
      let params={
        pageNum:1,
        pageSize:10
      }

      /**
       *    content: '支持自定义颜色',
       *  timestamp: '2018-04-03 20:46',
          color: '#0bbd87'
       */
      let data = await api.queryNewsList(params);
      this.activities = data.data.list
      console.log("-----",this.activities);
      // const resultList=data.data||[]
      // switch(type) {
      //   case 1:
      //     this.newsContent1 = resultList
      //     break;
      //   case 2:
      //     this.newsContent2 = resultList.slice(0,30)
      //     break;
      //   case 3:
      //     this.newsContent3 = resultList.slice(0,30)
      //     break;
      //   case 4:
      //     this.newsContent4 = resultList.slice(0,30)
      //     break;
      //   case 5:
      //     this.newsContent5 = resultList.slice(0,30)
      //     break;
      // }
    },
    // 主題切換
    handleChangeThemeClick() {
      if (this.theme === 'black') {
        eventBus.$emit("getTheme", 'red');
        this.theme = 'red'
      } else {
        eventBus.$emit("getTheme", 'black');
        this.theme = 'black'
      }
    },
    move () {
      // 獲取文字text 的計算後寬度  （由於overflow的存在，直接獲取不到，需要獨立的node計算）
      if (!document.getElementById('node')) {
        return
      }
      let width = document.getElementById('node').getBoundingClientRect().width
      // let width = this.$refs.node.getBoundingClientRect().width
      let box = document.getElementById('box')
      let copy = document.getElementById('copy')
      copy.innerText = this.artList.artTitle // 文字副本填充
      let distance = 0 // 位移距離
      // 設定位移
      this.moveStats = true
      clearInterval(this.timer2)
      this.timer2 = setInterval(function () {
        distance = distance - 1
        // 如果位移超過文字寬度，則回到起點
        if (-distance >= width) {
          distance = 16
          // clearInterval(timer)
        }
        box.style.transform = 'translateX(' + distance + 'px)'
      }, 30)
    },
    async getArtList () {
      // 獲取公告列錶
      let opts = {
        pageNum: 1,
        pageSize: 1
      }
      let result = await api.getArtList(opts)
      if (result.status === 0) {
        if (result.data.list.length > 0) {
          this.artList = result.data.list[0]
        }
      } else {
        this.$message.error(result.msg)
      }
    },
    async getMarket () {
      // 獲取大盤指數
      let result = await api.getChats()
      // let floatPoint =  result.candles[result.candles.length-1][1] - result.candles[0][1] 
      // let floatRate =  floatPoint / result.candles[0][1]
      this.realTimeQuotesIndexList= []
      for (let index = 0; index <  result.data.items.length; index++) {
        const element = result.data.items[index];
          let item = {
                currentPoint:  element['6'],
                floatPoint: element['11'],
                floatRate: element['56'],
                indexName:  index == 0?'DJI':element['200009'],
            }
          this.realTimeQuotesIndexList.push(item)
      }

    
    

      
      // let result = await api.getMarket()
      // let resultArr=[]
      // let result = await api.getTwStockListApi({})
      // if (result.status === 0) {
      //   let codes = result.data.slice(0,3).map(item=>item.stockCode).join(',')
      //   const res= await api.getTwStockData(codes)
      //   res.data.forEach((item, index) => {
      //     let newItem = {
      //       currentPoint: item["當盤成交價"],
      //       floatPoint: item["漲跌"],
      //       floatRate: item["Quote change"],
      //       indexName: item["股票名稱"]
      //     };
      //     resultArr.push(newItem)
      //   })
      //   this.market= resultArr
      // } else {
      //   Toast(result.msg)
      // }
    },
    // async refreshList () {
    //   // 刷新大盤指數
    //   let result = await api.getIndexMarket()
    //   this.changeTextClass = {}
    //   if (+result.status === 0) {
    //     // this.market = result.data.market
    //     result.data.forEach((element, i) => {
    //       this.changeTextClass[i] = ''
    //       if (element.currentPoint !== this.market[i].currentPoint) {
    //         this.changeTextClass[i] = true // 設定對應的動畫過濾
    //         this.market[i].currentPoint = element.currentPoint
    //         this.market[i].floatPoint = element.floatPoint
    //         this.market[i].floatRate = element.floatRate
    //       }
    //     })
    //   } else {
    //     Toast(result.msg)
    //   }
    // },
    async getBanner () {
      // 獲取顯示的banner
      let result = await api.getBannerByPlat({ platType: 'm' })
      if (result.status === 0) {
        this.bannerList = result.data
      } else {
        Toast(result.msg)
      }
    },
    async getProductSetting () {
      // 獲取產品配置信息
      let result = await api.getProductSetting()
      if (+result.status === 0) {
        this.settingForm = result.data
      } else {
        Toast(result.msg)
      }
    },

  
    articleList () {
      // 新手
      this.$router.push('/articleList')
    },
    toBuy () {
      // 去購買頁面
      this.$router.push('/buy')
    },
    goList () {
      // 去列錶頁面
      this.$router.push('/list')
    },
    goInfo () {
      this.$router.push('/info')
    },
    goOrderlist () {
      this.$router.push('/orderlist')
    },
    goMyinfo () {
      this.$router.push('/user')
    },
    goIndexList () {
      this.$router.push('/indexlist')
    },
    toDetail () {
      // 去列錶頁面
      this.$router.push('/listdetail')
    },
    toNewsDetail (id) {
      // 详情
      this.$router.push({
        path: 'newsDetail',
        query: {
          id: id
        }
      })
    },
    
    toAltDetail () {
      this.$router.push({
        path: 'alertdetail',
        query: {
          id: this.artList.id
        }
      })
    },
    toAltDetail2 () {
      this.$router.push({
        path: 'newsDetail2',
        query: {
          id: this.artList.id
        }
      })
    },
    toAltDetail3(link){
      document.location.href=link
    },
    // 几分钟前
    dateFormNow(date){
      // return dayjs(date);
     return dayjs().to(dayjs(date)) 
    }
    
  }
}
</script>
<style lang="less" scoped>
  @fontColor: #cfd0d1;
  @fontColor2: #ccc;
  .download{
    right: 10px;
    position: fixed;
    height: calc(45%);
    bottom: 0px;
    z-index: 10000;
    text-align: center;

   p{
    color: dodgerblue;
   }
  }
  .meiqia{
    position: absolute;
    bottom: 210px;
    right: 0px;
    // background-image: url(/static/img/meiqia.png) no repeat;
    width: 60px;
    height: 60px;
  }
  .horseLampModule {
    height: 0.5rem;
    line-height: 0.5rem;
    padding: 0 0.3rem;
    background-color: #16171d;
    .horseLamp-box {
      position: relative;
      // padding-left: 0.3rem;
      // padding-right: .3rem;
      display: flex;
      align-items: center;
      .tzIco{
        width: .24rem;
        height: .24rem;
      }
      .wrap {
        flex: 1;
        margin-left: 0.2rem;
      }
      .right {
        width: 1.45rem;
      }
    }

    .iconfont {
      position: absolute;
      left: 0;
      vertical-align: middle;
      margin-right: 0.1rem;
    }

    .pull-right {
      width: 1.45rem;
    }

    .pull-right::before {
      content: '';
      height: 0.3rem;
      border-left: 0.01rem solid #666;
      padding-left: 0.15rem;
    }

    // 限制外框寬度，隱藏多余的部分
    .wrap {
      letter-spacing: 0.06rem;
      overflow: hidden;
    }
  }
  
  // 移動框寬度設定
  #box {
    width: 80000%;
  }

  // 文字一行顯示
  #box div {
    float: left;
  }

  // 設定前後間隔
  #marquee {
    margin: 0 16px 0 0;
  }

  // 獲取寬度的節點，隱藏掉
  #node {
    position: absolute;
    z-index: -999;
    top: -999999px;
  }

  .banner {
    width: 100%;
    height: 3.74rem;
    height: 4.2rem;
    overflow: hidden;

    .banner-box {
      width: 100%;
      height: 100%;
      position: relative;
      opacity: 0.86;

      .box {
        position: absolute;
        text-align: center;
        top: 30%;
        width: 100%;
        background: none;
      }

      .title {
        color: #ff9600;
        font-size: 0.46rem;
        // font-weight: 600;
        margin-bottom: 0.4rem;
        letter-spacing: 0.08rem;
      }

      .desc {
        font-size: 0.26rem;
        margin-bottom: 0.8rem;
      }

      .target-btn {
        display: inline-block;
        font-size: 0.22rem;
        color: #ff9600;
        padding: 0.1rem 0.3rem;
        border: 0.01rem solid #ff9600;
        border-radius: 0.5rem;
      }

      .img {
        width: 100%;
        height: 100%;
      }
    }

  }

  .tipstexts {
    // height: 0.91rem;
    height: 0.41rem;

    .horseLampModule {
      width: 80%;
      height: .91rem;
      margin: .24rem auto;
      margin-left: 0.16rem;
      padding: 0 .417rem;
      height: .43rem;
      border-radius: 0.72rem;
      position: relative;
      float: left;
    }

    .novice {
      float: right;
      height: 0.3rem;
      font-size: 0.25rem;
      margin-top: 0.2rem;
      display: block;
      color: #b63717;
      padding: .04rem .24rem;
      border-left: .027rem solid #989898
    }
  }

  .nav-bg {
    width: 100%;
    padding: 0 2%;
    height: 1.11rem;
    margin: 0 auto;
    padding-bottom: .14rem;
    overflow: hidden;
    // margin-bottom: 0.12rem;
    .img {
      width: 100%;
      height: 100%;
      display: block;
    }
  }

  .icon-router {
    .col-xs-3 {
      width: 25%;
    }
  }

  /*大盤指數*/

  .dynamic-info {
    padding: .14rem;
    border-bottom: .02rem solid rgba(147, 147, 147, .2);
  }

  .dynamic-info li {
    float: left;
    font-size: .25rem;
    height: auto;
  }

  .dynamic-info li.tips {
    width: 15%;
  }

  .dynamic-info li.tips p {
    font-size: .22rem;
    text-align: center;
    width: .417rem;
    height: .625rem;
    color: #fff;
    padding-top: .04rem;
    background: url(../../assets/img/buyicon.png) no-repeat;
    background-size: contain;
  }

  .dynamic-info li p {
    font-size: .25rem;
  }

  .dynamic-name-code {
    width: 25%;
  }

  .dynamic-phone-win {
    width: 38%;
  }

  .dynamic-name-code p,
  .dynamic-phone-win p {
    font-size: .22rem;
    text-align: center;
    padding-top: .18rem;
    color: @fontColor2;
  }

  .dynamic-name-code p:first-child,
  .dynamic-phone-win p:first-child {
    font-size: .25rem;
    // color: rgb(34, 34, 34);
  }

  .btn-group {
    width: 22%;
    text-align: right;
    padding-top: .2rem;
    padding-right: .14rem;
  }

  .btn-group button {
    color: #fff;
    width: 1.279rem;
    font-size: .22rem;
    padding: .125rem .18rem;
    background: rgb(213, 0, 0);
    border: none;
  }

  .table-list .title {
    ul li {
      width: 33.33333333%;
      padding-right: 15px;
      padding-left: 15px;
    }
  }

.account-box {
  position: relative;
  background-color: #000C16;
  padding-top: .2rem;
  .content {
    width:6.86rem;
    height: 1.72rem;
    margin: 0 auto;
    padding: 0 0.14rem;
    display: flex;
    justify-content: space-between;
    .tab {
      width: 2.04rem;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;
      padding: .25rem 0 .15rem;
      .name {
        width: 1.52rem;
        height: .4rem;
        line-height: .4rem;
        border:1px solid rgba(255, 255, 255, .2);
        border-radius: .2rem;
        font-size: .24rem;
      }
      .price {
        font-size: .24rem;
      }
      .status {
        position: relative;
        width: 100%;
        display: flex;
        justify-content: space-between;
        padding: 0 .1rem;
        color: #fff8;
        &::before{
          display: block;
          position: absolute;
          content: '';
          width: 100%;
          height: 1px;
          left: 0%;
          top: -.1rem;
          background-color: #fff3;
        }
      }
    }
  }
}
.icon-img{
  width: .4rem;
  height: .4rem;
}
.icon-title {
  font-size: .24rem;
  font-weight: 400;
}
.icon-router.home-ico-router {
  padding: .4rem 0;
  background-color: #16171d !important; 
}
.zhishu {
  // background-image: url(../../../static/img/bg-zhisu.png);
  background-size: cover;
}
.shadow-box {
  width: 100%;
  height: .4rem;
  background-color: #16171d;
  overflow: hidden;
  .shadow-ico {
    width: 100%;
    transform: translateY(-1px);
  }
}
.home-search {
  padding: 0 .3rem;
  height: .8rem;
  background-color: #000C16;
  display: flex;
  align-items: center;
  &-me {
    width: .6rem;
    height: .6rem;
    border-radius: .3rem;
    // border: 1px solid #234B6E;
    display: flex;
    align-items: center;
    justify-content: center;
    >img{
      width: .6rem;
      height: .6rem;
    }
  }
  &-input {
    width: 5.4rem;
    height: .6rem;
    border-radius: .3rem;
    border: 1px solid #234B6E;
    margin-left: .27rem;
    padding: 0 .27rem;
    display: flex;
    align-items: center;
    >img{
      width: .26rem;
      height: .26rem;
      margin-right: .26rem;
    }
    >input {
      font-size: .24rem;
      &::-webkit-input-placeholder {
        color: #363636;
      }
      // color: 
    }
  }
  &-ctl {
    width: 1rem;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    .lingdang{
      width: .25rem;
      height: .32rem;
    }
    .pifu {
      width: .34rem;
      height: .28rem;
      margin-left: .18rem;
    }
  }
}
.swiper-home{
  padding: 0.1rem .3rem;
  height: 2.67rem;
}
.mint-swipe-indicators {
  left: .4rem !important;
  bottom: .3rem !important;
}
.swiper-ad{
  padding: 0.1rem .3rem;
  height: 1.2rem;
  .banner-ad {
    display: block;
    border-radius: .1rem;
    overflow: hidden;
    height: 100%;
  }
}


#app.red-theme{
  .home-search {
    background-color: #C01815;
    &-me {
      border-color: #fff;
    }
    &-input {
      border-color: #fff;
      input::-webkit-input-placeholder {
        color: #fff8;
      }
    }
  }
  .zhishu {
    background-color: #c01815;
    background-image: url(../../../static/img/bg-zhisu-red.png);
  }
  .icon-router.home-ico-router {
    background-color: #fff !important;
    .icon-title {
      color: #000;
    }
  }
  .shadow-box {
    background-color: #fff;
    .shadow-ico {
      transform:translateY(0);
      opacity: .5;
    }
  }
  .horseLampModule{
    background-color: #fff;
    .wrap {
      color: #000;
    }
    .right {
      color: #000;
    }
  }
  .swiper-home{
    background-color: #fff;
  }
 
  .account-box .content .tab .name {
    border-color: #E9E9E9;
    color: #222222;
  }
  .account-box .content .tab .price {
    color: #C21816;
  }
  .account-box .content .tab .status {
    color: #565656;
  }
  .account-box .content .tab .status::before {
    background-color: #565656;
    opacity: .3;
  }
  .account-box .content {
    position: relative;
  }
  .account-box .content::after {
    display: block;
    content: '';
    position: absolute;
    width: 100%;
    height: .4rem;
    bottom: -.4rem;
    left: 0;
    background-image: url(../../assets/ico/shadow.png);
    background-size: cover;
    opacity: .5;
  }

 
}

.el-timeline-item {
    position: relative;
    padding-bottom: 20px;
}
</style>
