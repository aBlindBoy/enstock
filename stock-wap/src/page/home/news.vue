<template>
    <div class="wrapper">
      <div class="news-tab">
        <el-timeline>
        <el-timeline-item 
            v-for="(activity, index) in activities"
            :key="index"
            icon="el-icon-timer"
            type="primary"
            color="greep"
            size="16"
        
            >
            <div   @click="toNewsDetail(activity.id)">
                <p style="font-weight: 400;letter-spacing:.01rem;">{{activity.title}}</p>
            <p style="margin-top:10px">
                <el-tag type="danger" size="mini">{{activity.sourceName}}</el-tag>
                {{dateFormNow(activity.addTime) }}</p>
            </div>
      </el-timeline-item>
    </el-timeline>
  
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
    data () {
      return {
        news:'tab_0',
        b_bg:require('../../../static/img/bg-zhisu.png'),
        r_bg:require('../../../static/img/bg-zhisu-red.png'),
        activities: [
        ]
      }
    },
    created () {
      this.getNewsList()
    },
    mounted () {
    },
  
    beforeDestroy () {
    
    },
    computed: {},
    // 更新的時候運動
    updated () {
    },
    methods: {
     
      async getNewsList() {
        let params={
          pageNum:1,
          pageSize:10
        }
  
        /**
         *  content: '支持自定义颜色',
         *  timestamp: '2018-04-03 20:46',
             color: '#0bbd87'
         */
        let data = await api.queryNewsList(params);
        this.activities = data.data.list
        console.log("-----",this.activities);
        
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
  .news-tab {
    background-color: #16171d;
    min-height: 5rem;
    padding: 0.3rem;
    margin-top: .2rem;
    p{
      color:  #cfd0d1 !important;
    }
  }
  
  #app.red-theme{
   
    .news-tab{
        background-color: #fff;
        min-height: 5rem;
        padding: 0.3rem;
        margin-top: .2rem;
        p{
          color: #000 !important;
        }
    }
}
  .el-timeline-item {
      position: relative;
      padding-bottom: 20px;
  }
  </style>
  