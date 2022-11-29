<!-- <template>
    <div class="wrapper">
      <div style="padding:20px">
        <div class="htmlTitle" v-html="htmlTitle"></div>
        <div class="publishTime">{{publishTime}}</div>

        <div class="htmlContent" v-html="htmlContent"></div>
      </div>
    </div>
  </template>
    
    <script>
  import { Toast } from "mint-ui";
  import * as api from '@/axios/api'
  import * as htmlparser2 from "htmlparser2";

  export default {
    components: {},
    props: {},
    data() {
      return {
        id: null,
        htmlTitle:null,
        publishTime:null,
        htmlContent:null
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
      //  const dom = htmlparser2.parseDocument(result);
      this.htmlTitle = result.split('<div class="p-4 px-4 sm:px-8 text-2xl sm:text-3xl mt-4 sm:mt-8 mb-2 font-medium">')[1]
      this.htmlTitle = this.htmlTitle.split('-')[0]

      this.publishTime = result.split('<div class="text-gray-500">')[1]
      this.publishTime = this.publishTime.split('</div>')[0]
     
      this.htmlContent = result.split('<div class="p-0 sm:p-4 nstock-content">')[1]
        
      if(this.htmlContent == null){
        this.htmlContent = result.split('<pre>')[1]
        this.htmlContent = this.htmlContent.split('</pre>')[0]
      }else{
        this.htmlContent = this.htmlContent.split('</div>')[0]

      }
      


 
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
  // h4 {
  //   margin-top: 50px;
  //   margin-bottom: 10px;
  //   line-height: 30px;
  //   border-left: 5px solid red;
  //   span {
  //     margin-left: 20px;
  //   }
  // }
  // p {
  //   letter-spacing:1px;
  //   line-height: 24px;
  // }
  
  #app.red-theme{
      h4{
          color: #000;
      }
      p{
          color: #000;
      }
  
  }
  </style>
     -->