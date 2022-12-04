<template>
    <div class="wrapper">
      <div style="padding:20px">
        <div class="htmlTitle" v-html="detail.title"></div>
        <div class="publishTime"> <el-tag type="danger" size="mini">{{detail.sourceName}}</el-tag>  {{formatDate(detail.addTime)}}</div>

        <div class="htmlContent" v-html="detail.content"></div>
      </div>
    </div>
  </template>
    
    <script>
  // import { Toast } from "mint-ui";
  import * as api from '@/axios/api'
  // import * as htmlparser2 from "htmlparser2";
  const dayjs = require('dayjs')
  export default {
    components: {},
    props: {},
    data() {
      return {
        id: null,
        detail:null
      };
    },
    created() {
      this.id = this.$route.query.id
      this.getNewDetail(this.id)
    },
    mounted () {
    },
    
    beforeDestroy() {},
    computed: {},
    updated() {},
    methods: {
      async getNewDetail(id){
       let result = await api.getNewDetail(id)
        this.detail =  result.data
      },
      formatDate(date){
       return dayjs(date).format('DD/MM/YYYY') // '25/01/2019'
      }
    },
    mounted() {},
  };
  </script>
  <style lang="less" scoped>
     @fontColor: #cfd0d1;
    @fontColor2: #ccc;


    .htmlTitle{
      text-align: center;
      font-size: 20px;
      line-height: 30px;
    }
    .publishTime{
      margin-top: 10px;
      line-height: 30px;
      text-align: right;
    }

    .htmlContent{
      margin-top: 20px;
      line-height: 30px;
}
  
  #app.red-theme{
      div{
          color: #000;
      }
      p{
          color: #000;
      }
  
  }
  </style>
    