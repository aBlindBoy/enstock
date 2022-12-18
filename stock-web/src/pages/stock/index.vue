<template>
  <el-container>
    <el-header>
      <home-header ref="header"></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <!-- <div v-if="color == 'red-bg'">
        <div style="margin-top:150px;width: 1200px; margin: 200px auto;" >
            <span class="stock-title">股票详情</span>
            <span class="stock-title-en">STOCK DETAILS</span>
       </div>
      </div>-->

      <div>
        <backdrop1>
          <div class="title-stock" style="">
            <span class="stock-title-en">{{$t('stock.title')}}</span>
          </div>
        </backdrop1>
      </div>

      <el-main>
        <div class="fat">
          <div >
            <marquee-text :duration="30" :paused="paused" @mouseenter="mouseenter" @mouseleave="mouseleave">
          <span v-for="quotes in realTimeQuotesIndexList" style="margin-left: 20px;line-height: 50px;" >
            <b> {{quotes.indexName}}</b>
            <span> {{quotes.currentPoint}}</span>
            <span v-if="quotes.floatRate>0">
                <i class="el-icon-top red"></i>
                <span style="background-color:rgb(177 59 81 / 50%);"> +{{quotes.floatPoint}}</span>
                <span style="background-color:rgb(177 59 81 / 50%);"> +{{quotes.floatRate}}%</span>
            </span>  
            <!---->
            <span v-else  >
              <i class="el-icon-bottom green"></i>
              <span style="background-color:rgb(75 160 123 / 50%)"> {{quotes.floatPoint}}</span>
              <span style="background-color:rgb(75 160 123 / 50%)"> {{quotes.floatRate}}%</span>
            </span>
          </span>
        </marquee-text>
          </div>
     

          <div
            class="table-box "
          >
          <el-menu
              :default-active="activeIndex"
              mode="horizontal"
              @select="handleSelect"
              text-color="#fff"
              active-text-color="#ffd04b">
              <el-menu-item  class="banner-menu first" index="1">美股</el-menu-item>
              <el-menu-item  class="banner-menu first" index="2">台股</el-menu-item>
            </el-menu>
            <div class="table-cont-box" v-if="activeIndex==1">
              <div class="table-search">
                <el-row type="flex" justify="end">
                  <el-col :span="8">
                    <div class="test">
                      <el-input
                        v-model="form.stock"
                        :placeholder="$t('stock.search')"
                        class="search-public"
                      >
                        <!-- <el-button @click="getList" slot="append" icon="iconfont  icon-search"></el-button> -->
                      </el-input>
                      <span
                        @click="getList"
                        class="iconfont icon-search search-stock"
                      ></span>
                    </div>
                  </el-col>
                </el-row>
              </div>
              <table-box
                :key="us"
                :list="list"
                :getData="getList"
                :handleOptions="handleOptions"
              ></table-box>
            </div>
            <div class="table-cont-box" v-if="activeIndex==2">
              <div class="table-search">
                <el-row type="flex" justify="end">
                  <el-col :span="8">
                    <div class="test">
                      <el-input
                        v-model="form.stock"
                        :placeholder="$t('stock.search')"
                        class="search-public"
                      >
                        <!-- <el-button @click="getList" slot="append" icon="iconfont  icon-search"></el-button> -->
                      </el-input>
                      <span
                        @click="getTwStockList"
                        class="iconfont icon-search search-stock"
                      ></span>
                    </div>
                  </el-col>
                </el-row>
              </div>
              <table-box
                :key="tw"
                :list="twList"
                :getData="getTwStockList"
                :handleOptions="handleOptions"
              ></table-box>
            </div>
          </div>
        </div>
        <newFooter></newFooter>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import HomeHeader from "../../components/HeaderOrder";
import TableBox from "./components/table";
import * as api from "../../axios/api";
import backdrop1 from "@/components/backdrop1.vue";
import newFooter from "../../components/newFooter";
import { mapState } from "vuex";
import MarqueeText from 'vue-marquee-text-component'
// import MarqueeText from 'vue-marquee-text-component/src/components/MarqueeText.vue';
export default {
  components: {
    HomeHeader,
    TableBox,
    backdrop1,
    newFooter,
    MarqueeText
  },
  props: {},
  data() {
    return {
      activeIndex:'1',
      timer: null,
      form: {
        stock: "",
        pageNum: 1,
        pageSize: 10
      },
      list: {
      },
      twList:[],
      loading: false,
      refresh: false, // 刷新中
      changeTextClass: {}, // 表格中的数据变化
      siteInfo: {},
      realTimeQuotesIndexList:[],
      paused:false,
    };
  },
  watch: {},
  computed: {
    ...mapState({
      color: state => state.systemColor
    })
  },
  created() {
    this.timer = setInterval(this.refreshList, 60000);
    this.getMarket();
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    this.getList();
    this.getInfoSite();
  },
  methods: {
    async getInfoSite() {
      // 获取站点详情
      let result = await api.getInfoSite();
      if (result.status === 0) {
        this.siteInfo = result.data;
      } else {
        this.$message.error(result.msg);
      }
    },
    handleSelect(event){
      this.activeIndex = event
      if (event == 1) {
        this.getList()
      } else {
        this.getTwStockList()
      }
    },
    mouseenter(){
      console.log(this.paused);
   
      this.paused = true

    },
    mouseleave(){
      this.paused = false
      console.log(this.paused);
    },
    async getMarket () {
      // 獲取大盤指數
      let result = await api.getChats()
      this.realTimeQuotesIndexList= []
      for (let index = 0; index <  result.data.items.length; index++) {
        const element = result.data.items[index];
          let item = {
                currentPoint:  element['6'],
                floatPoint: element['11'],
                floatRate: element['56'],
                indexName: element['200009'],
            }
            this.realTimeQuotesIndexList.push(item)
        }
    },
    async getList() {
      // 获取表格数据
      let opt = {
        keyWords: this.form.stock,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      };
      this.loading = true;
      let res = await api.getStock(opt);
      if (res.status === 0) {
        this.list = res.data
      } else {
        this.$message.error(data.msg);
      }
      this.loading = false;
    },
    async getTwStockList() {
      // 获取表格数据
      let opt = {
        keyWords: this.form.stock,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      };
      this.loading = true;
      let res = await api.getTwStockList(opt);
      if (res.status === 0) {
        this.twList = res.data
      } else {
        this.$message.error(data.msg);
      }
      this.loading = false;
    },
    async refreshList() {
      if (this.refresh || this.loading) {
        return;
      }
      this.refresh = true;
      this.changeTextClass = {};
      // 获取表格数据
      let opt = {
        keyWords: this.form.stock,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      };
      let data = await api.getStock(opt);
      this.refresh = false;
      if (data.status === 0) {
        data.data.list.forEach((element, i) => {
          this.changeTextClass[i] = "";
          if (data.data.list[i].nowPrice !== this.list.list[i].nowPrice) {
            this.changeTextClass[i] = true; // 设置对应的动画过滤
            this.list.list[i].nowPrice = data.data.list[i].nowPrice; // 现价
            this.list.list[i].hcrate = data.data.list[i].hcrate; // 涨跌幅
            this.list.list[i].today_max = data.data.list[i].today_max; // 最高
            this.list.list[i].today_min = data.data.list[i].today_min; // 最低
          }
        });
      } else {
        this.$message.error(data.msg);
      }
    },
    handleOptions(opts) {
      this.form = { ...this.form, ...opts };
    }
  }
};
</script>
<style lang="less" scoped>
// .el-main{
//   margin-top: 200px;
// }

 .table-box .page-box {
  margin-top: 50px !important;
}



.red-bg {
  .stock-title {
    font-size: 36px;
  }
  .stock-title-en {
    font-size: 22px;
  }
}
.black-bg {
  .stock-title {
    font-size: 36px;
    font-weight: 400;
    color: #ffffff;
  }
  .stock-title-en {
    font-size: 22px;
    font-weight: 400;
    color: #e6003e;
    line-height: 36px;
  }
  .title-stock {
    width: 1200px;
    margin: 250px auto;
    margin-top: 250px;
  }
  .fat {
    overflow: hidden;
    // margin-top: 200px;
  }
  .table-search {
    background-color: #0a1c27;
  }

}
.red-bg {
  .red-bg-bgd {
    background-color: rgb(233, 233, 233);
  }
  .title-stock {
    width: 1200px;
    margin: 250px auto;
    margin-top: 200px;
  }
}
/deep/.red-bg .el-container {
  background: #e9e9e9;
}
.test {
  display: flex;
  position: relative;
}
.test > span {
  position: absolute;
  right: 20px;
  top: 10px;
  font-size: 20px;
}
/deep/ .el-main {
  padding: 0;
}
.table-box {
  margin-bottom: 100px;
  width: 1200px;
  margin: 0 auto;
}

.table-search {
  padding: 20px 0;
}
.table-search > div {
  justify-content: center !important;
}
// /deep/ .el-main{
//   padding: 0;
//   .table-box[data-v-ebd8dd76] {
//     width: 100%;
//     .table-search{
//       width: 1200px;
//       margin: 0 auto;
//     }
//     .table-title{

//     }
//   }
//   div[data-v-ebd8dd76]{
//     position: relative;
//     background-color: pink;
//   }
// }
</style>
