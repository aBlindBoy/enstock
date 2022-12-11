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
            <!-- <span class="stock-title">股票詳情</span> -->
            <span class="stock-title-en">{{$t('stock.title')}}</span>
          </div>
        </backdrop1>
      </div>
      <el-main>
        <div class="fat">
          <div
            class="table-box "
            :style="
              color == 'black-bg' ? 'margin: 50px auto;' : 'margin-top: 200px;'
            "
          >
            <div class="table-cont-box">
              <div class="table-search">
                <el-row type="flex" justify="end">
                  <el-col :span="8">
                    <div class="test">
                      <el-input
                        v-model="form.stock"
                        placeholder="Please enter ticker and stock"
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
              <!-- <div class="table-title">
                <ul class="clearfix">
                  <li class="pull-left">
                    <a class="list" href="javascript:;">股票詳情</a>
                  </li>
                </ul>
                <div></div>
              </div> -->
              <table-box
                :list="list"
                :getData="getList"
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
export default {
  components: {
    HomeHeader,
    TableBox,
    backdrop1,
    newFooter
  },
  props: {},
  data() {
    return {
      timer: null,
      form: {
        stock: "",
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      loading: false,
      refresh: false, // 刷新中
      changeTextClass: {}, // 表格中的数据变化
      siteInfo: {}
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
    async getList() {
      // 获取表格数据
      let opt = {
        keyWords: this.form.stock,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      };
      this.loading = true;
      let res = await api.getStock(opt);
      // debugger
      if (res.status === 0) {
        this.list = res.data
        // this.list = {...res.data,list:[]};
        // let stock_ids = res.data.list.map(item => item.code).join(",");
        // const res1 = await api.getTwStockData(stock_ids);
        
        // res1.data.forEach((item, index) => {
        //   let newItem = {
        //     nowPrice: item["當盤成交價"],
        //     hcrate: item["Quote change"],
        //     today_max: item["最高價"],
        //     today_min: item["最低價"]
        //   };
        //   this.list.list.push({ ...res.data.list[index], ...newItem })
        // });
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
    margin-top: 200px;
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
