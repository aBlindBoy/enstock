<template>
  <div class="wrapper">
    <div class="news-tab">
      <div class="title"> <span>台股新聞</span> </div>
      <mt-navbar v-model="news">
        <mt-tab-item id="tab_0">
          <span class="tab-name" @click="clickNavbar('tab_0')">台股新聞</span>
        </mt-tab-item>
        <mt-tab-item id="tab_1">
          <span class="tab-name" @click="clickNavbar('tab_1')">焦點新聞</span>
        </mt-tab-item>
        <mt-tab-item id="tab_2">
          <span class="tab-name" @click="clickNavbar('tab_2')">盤前必讀</span>
        </mt-tab-item>
        <mt-tab-item id="tab_3">
          <span class="tab-name" @click="clickNavbar('tab_3')">台股盤中</span>
        </mt-tab-item>
        <mt-tab-item id="tab_4">
          <span class="tab-name" @click="clickNavbar('tab_4')">趨勢分析</span>
        </mt-tab-item>
      </mt-navbar>

      <mt-tab-container v-model="news" :swipeable="true">
        <mt-tab-container-item id="tab_0">
          <!-- 台股新聞 -->
          <div class="news-content">
            <div class="news-item" v-for="item of newsContent1" :key="item.id" @click="toNewsDetail(item)">
              <span class="news-status">
               {{ formatDate(item.publishAt * 1000) }}
              </span>
              <p class="news-title" style="-webkit-box-orient: vertical;">
                {{ item.title }}
              </p>
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab_1">
          <!-- 焦點新聞 -->
          <div class="news-content">
            <div class="news-item" v-for="item of newsContent2" :key="item.id" @click="toNewsDetail(item)">
              <span class="news-status">
                {{ formatDate(item.publishAt * 1000) }}
              </span>
              <p class="news-title" style="-webkit-box-orient: vertical;">{{ item.title }}</p>
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab_2">
          <!-- 盤前必讀 -->
          <div class="news-content">
            <div class="news-item" v-for="item of newsContent3" :key="item.id" @click="toNewsDetail(item)">
              <span class="news-status">
                {{ formatDate(item.publishAt * 1000) }}
              </span>
              <p class="news-title" style="-webkit-box-orient: vertical;">{{ item.title }}</p>
              
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab_3">
          <!-- 台股盤中 -->
          <div class="news-content">
            <div class="news-item" v-for="item of newsContent4" :key="item.id" @click="toNewsDetail(item)">
              <span class="news-status">
               {{ formatDate(item.publishAt * 1000) }}
              </span>
              <p class="news-title" style="-webkit-box-orient: vertical;">{{ item.title }}</p>
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab_4">
          <!-- 趨勢分析 -->
          <div class="news-content">
            <div class="news-item" v-for="item of newsContent5" :key="item.id" @click="toNewsDetail(item)">
              <span class="news-status">
               {{ formatDate(item.publishAt * 1000) }}
              </span>
              <p class="news-title" style="-webkit-box-orient: vertical;">{{ item.title }}</p>
            </div>
          </div>
        </mt-tab-container-item>
      </mt-tab-container>

      <div class="title" style="margin-top:1rem"> <span>美股新聞</span> </div>

      <div class="usNews">
        <el-timeline>
          <el-timeline-item v-for="(activity, index) in activities" :key="index" icon="el-icon-timer" type="primary"
            color="greep" size="16">
            <div @click="toNewsDetail(activity.id)" >
              <p class="mainTitle" style="font-weight: 400;letter-spacing:.01rem;">{{ activity.title }}</p>
              <p class="subTitle" style="margin-top:10px">
                <!-- <el-tag type="danger" size="mini"></el-tag> -->
                <span >{{ activity.sourceName }}</span>
                <span style="">{{ dateFormNow(activity.addTime) }}</span>
              </p>
            </div>
          </el-timeline-item>
        </el-timeline>

      </div>

    </div>


    <foot></foot>
    <!-- <a href="https://088992.kefu.easemob.com/webim/im.html?configId=d5d08347-1fa9-4c95-87a8-c6100336ab58"><img class="meiqia" :src="require('../../../static/img/meiqia.png')"/></a> -->
  </div>

</template>
  
<script>
import { Toast } from 'mint-ui'
import * as api from '@/axios/api'

const dayjs = require('dayjs')
var relativeTime = require('dayjs/plugin/relativeTime')
dayjs.extend(relativeTime)

export default {
  components: {

  },
  props: {},
  data() {
    return {
      // b_bg:require('../../../static/img/bg-zhisu.png'),
      // r_bg:require('../../../static/img/bg-zhisu-red.png'),
      activities: [
      ],
      news: 'tab_0',
      newsContent1: [],
      newsContent2: [],
      newsContent3: [],
      newsContent4: [],
      newsContent5: [],
    }
  },
  created() {
    this.getNewsList()
    this.getTwNews()
  },
  mounted() {
  },

  beforeDestroy() {

  },
  computed: {},
  // 更新的時候運動
  updated() {
  },
  methods: {

    async getNewsList() {
      let params = {
        pageNum: 1,
        pageSize: 5
      }

      /**
       *  content: '支持自定义颜色',
       *  timestamp: '2018-04-03 20:46',
           color: '#0bbd87'
       */
      let data = await api.queryNewsList(params);
      this.activities = data.data.list
      console.log("-----", this.activities);

    },

    toNewsDetail(id) {
      // 详情
      this.$router.push({
        path: 'newsDetail',
        query: {
          id: id
        }
      })
    },
    // 几分钟前
    dateFormNow(date) {
      // return dayjs(date);
      return dayjs().to(dayjs(date))
    },
    clickNavbar(tab) {
      switch (tab) {
        case 'tab_0':
          this.getTwNews();
          break
        case 'tab_1':
          this.getEsgNews();
          break
        case 'tab_2':
          this.getBeforeNews();
          break
        case 'tab_3':
          this.getLiveNews();
          break
        case 'tab_4':
          this.getAnalysisNews();
          break
      }
      console.log(this.news);
    },
    async getTwNews() {
      let result = await api.getTwNews();
      this.newsContent1 = result.items.data.filter((item, index) => index < 5)
    },
    async getEsgNews() {
      let result = await api.getEsgNews();
      this.newsContent2 = result.items.data.filter((item, index) => index < 5)
    },
    async getBeforeNews() {
      let result = await api.getBeforeNews();
      this.newsContent3 = result.items.data.filter((item, index) => index <5)
    },
    async getLiveNews() {
      let result = await api.getLiveNews();
      this.newsContent4 = result.items.data.filter((item, index) => index < 5)
    },
    async getAnalysisNews() {
      let result = await api.getAnalysisNews();
      this.newsContent5 = result.items.data.filter((item, index) => index < 5)
    },

    formatDate(date) {
      return dayjs(date).format('HH:mm')
    },
      // toNewsDetail(item) {
      //   // 详情
      //   this.$router.push({
      //     path: 'newsDetail',
      //     query: {
      //       content: item.content,
      //       title: item.title,
      //       publishAt: item.publishAt,
      //     }
      //   })
      // },

  }
}
</script>
<style lang="less" scoped>
@fontColor: #cfd0d1;
@fontColor2: #ccc;


.el-timeline-item {
  position: relative;
  padding-bottom: 30px;
  // border-bottom: 1px solid #3c3e42 ;
}

//   .news-tab {
//     min-height: 5rem;
//     padding: 0 .3rem;
//     margin-top: .1rem;
//     /deep/.mint-tab-container {
//       background-color: #1D1E29;
//     }
//     /deep/.mint-tab-item {
//       background-color: #1D1E29;
//     }
//     /deep/.mint-tab-item-label {
//       color: #fff;
//       font-size: .26rem;

//     }
//     /deep/.is-selected .tab-name{
//       position: relative;
//     }
//     /deep/.mint-navbar .mint-tab-item.is-selected {
//       border-bottom:none;
//     }
//     /deep/.is-selected .tab-name:after{
//         position: absolute;
//         display: block;
//         content: '';
//         height: .07rem;
//         background-color: #1381A4;
//         width: 100%;
//         left: 0;
//         bottom: -.25rem;
//       }
// }
// 黑色
#app.black-theme {
  .news-tab {
    background-color: #16171d;
    min-height: 5rem;
    padding: 0.2rem;
    margin-top: .1rem;
    p {
      color: #cfd0d1;
    }
    .title {
      margin-top: .1rem;
      margin-bottom: .1rem;
      line-height: .8rem;
      border-left: 5px solid red;
      color: #fff;
      span {
        margin-left: 20px;
        font-size: .3rem;
      }
    }
  }

  .news-content {
    position: relative;

    .news-item {
      padding: .3rem;
      display: flex;
      border-bottom: .01rem solid #32333B;
      .news-title {
        position: relative;
        width: 100%;
        font-size: .3rem;
        line-height: .46rem;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        display: -webkit-box;
      }

      .news-status {
        // display: block;
        font-size: .3rem;
        line-height: .46rem;
        color: #606167;
        margin-top: .2rem;
        padding-bottom: .25rem;
        width: 1rem;
      }
    }
  }

  .usNews{
    margin-top: .3rem;
    /deep/ .el-timeline-item__tail{
      border-left: 2px solid #3c3e42;
    }
    /deep/.el-timeline-item__node--primary{
      background-color:#000000;
    }
    .mainTitle{
      color:#cfd0d1;
    }
    .subTitle{
      color:#606167;
    }
  }
}
// 黑色

// 紅色
#app.red-theme {
  // .news-tab {
  //   background-color: #16171d;
  //   min-height: 5rem;
  //   padding: 0.3rem;
  //   margin-top: .2rem;

  //   p {
  //     color: #cfd0d1 !important;
  //   }
  // }

  .news-tab {
    background-color: #fff;
    min-height: 5rem;
    padding: 0.3rem;
    margin-top: .2rem;

    .title {
      margin-top: 20px;
      margin-bottom: 10px;
      line-height: 30px;
      border-left: 5px solid red;

      span {
        margin-left: 20px;
        font-size: .3rem;
        color: #000;
      }
    }

    p {
      color: #000 ;
    }
    /deep/.mint-navbar {
      background: white;

      .mint-tab-item {
        background: white;

        .mint-tab-item-label {
          color: #000000;
        }

        &.is-selected {
          .mint-tab-item-label {
            color: #BB1815;

            .tab-name {
              &::after {
                background-color: #BB1815;
              }
            }
          }
        }
      }
    }

    .mint-tab-container {
      background: white;

      .news-title {
        color: #656565;
      }

      .news-status {
        border-color: #DFDFDF;
      }
    }

    .usNews{
       margin-top: .3rem;
       /deep/ .el-timeline-item__tail{
        border-left: 2px solid #3c3e42;
      }
      /deep/ .el-timeline-item__node--primary{
        background-color:#000000;
      }
      .mainTitle{
        color:#656565;
      }
      .subTitle{
        color:#656565;
      }
   }
  }

  .news-content {
    position: relative;

    .news-item {
      padding: .3rem;
      display: flex;
      border-bottom: .001rem solid #32333B;
      .news-title {
        position: relative;
        width: 100%;
        font-size: .3rem;
        line-height: .46rem;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        display: -webkit-box;
      }

      .news-status {
        display: block;
        margin-top: .1rem;
        font-size: .3rem;
        line-height: .36rem;
        color: #606167;
        margin-top: .1rem;
        width: 1rem;
        // padding-bottom: .25rem;
      
      }
    }
  }

}
// 紅色
</style>
  