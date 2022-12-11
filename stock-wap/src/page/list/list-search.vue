<template>
  <div class="wrapper">
    <mt-button
      slot="right"
      class="search-btn-list"
      icon="search"
      @click="getStock"
      >{{$t('common.search')}}</mt-button
    >
    <mt-search
      fixed
      show
      autofocus
      v-model="keywords"
      :placeholder="$t('common.searchStockCode2')"
    >
    <!--  @keyup.enter.native="getStock" -->
      <ul class="table-list">
        <li class="title">
          <div>
            <ul class="clearfix">
              <li class="li-title">{{$t('common.stockName')}}</li>
              <li class="li-base">{{$t('common.lastPrice')}}</li>
              <!-- <li class="li-base">Chg</li> -->
              <li class="li-base">{{$t('common.chgRate')}}</li>
            </ul>
          </div>
        </li>
      </ul>
      <ul
        class="table-list table-list-body"
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="false"
        infinite-scroll-distance="10"
      >
        <li class="list-body" v-for="item in list" :key="item.key">
          <div>
            <ul
              class="clearfix"
              :class="{green:item.rate<0,red:item.rate>0}"
              @click="toDetail(item)"
            >
              <li class="li-title">
                <p class="name">{{ item.name }}</p>
                <p class="code">
                  <i
                    :class=" item.stock_type == 'sz'
                        ? 'iconfont shen-mark hushen-mark'
                        : 'iconfont hushen-mark'
                    "
                    >US</i
                  >
                  <!-- <i v-else class="iconfont kechuang-mark">科創</i> -->
                  {{ item.code }}
                </p>
              </li>
              <li class="li-base">
                <span>{{
                  item.nowPrice ? Number(item.nowPrice).toFixed(2) : "-"
                }}</span>
              </li>
              <!-- <li class="li-base">
                <span v-if="item.hcrate == 0">-</span>
                <span v-else>{{ item.hcrate ? item.hcrate : "0" }}</span>
              </li> -->
              <li class="li-base no-bold">
                <span>{{ item.hcrate }}%</span>
              </li>
            </ul>
          </div>
        </li>
      </ul>
      <div v-show="loading" class="load-all text-center">
        <mt-spinner type="fading-circle"></mt-spinner>
        {{$t('common.loading')}}...
      </div>
      <div v-show="!loading && hasSearch" class="load-all text-center">
        {{$t('common.allLoaded')}}
      </div>
      <div v-show="!hasSearch" class="load-all text-center">
        {{$t('listSearch.search')}}
      </div>
    </mt-search>
    <foot></foot>
  </div>
</template>

<script>
import foot from "../../components/foot/foot";
import { Toast } from "mint-ui";
import * as api from "@/axios/api";

export default {
  components: {
    foot
  },
  props: {},
  data() {
    return {
      loading: false,
      keywords: "",
      pageNum: 1,
      pageSize: 15,
      list: [],
      timer: "",
      currentNum: 15,
      hasSearch: false // 是否查詢
    };
  },
  watch: {
    // keywords(newVal, oldVal) {
    //   if (newVal !== oldVal) {
    //     this.getStock();
    //   }
    // }
  },
  computed: {},
  created() {
    // this.timer = setInterval(this.refreshList, 5000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    //   this.getStock()
  },
  methods: {
    async getStock() {
      let opt = {
        keyWords: this.keywords,
        pageNum: this.pageNum,
        pageSize: 15
      };
      this.hasSearch = true;
      let data = await api.getStock(opt);
      this.list = data.data.list
      // if (data.status === 0) {
      //   this.loading=false
      //   this.total = data.data.total;
      //   let codes = data.data.list.map(item => item.code).join(',');
      //   if(!codes){
      //     return 
      //   }
      //   const res = await api.getTwStockData(codes);
      //   res.data.forEach((item, index) => {
      //     let newItem = {
      //       nowPrice: item["當盤成交價"],
      //       preclose_px: item["當盤成交價"] - item["漲跌"],
      //       hcrate: item["Quote change"],
      //       rate: item["漲跌"],
      //       name: item["股票名稱"],
      //       code:item['股票代號'],
      //       stock_type:'tw'
      //     };
      //     this.list.push(newItem);
      //   });
      // } else {
      //   Toast(data.msg);
      // }
    },
    // async refreshList() {
    //   // 判斷是不是已經查詢 或者是否正在加載下一頁 是則退出，不進行刷新
    //   if (!this.hasSearch || this.loading) {
    //     return;
    //   }
    //   let opt = {
    //     keyWords: this.keywords,
    //     pageNum: 1,
    //     pageSize: this.currentNum
    //   };
    //   let data = await api.getTwStockPageList(opt);
    //   this.loading=false
    //   if (data.status === 0) {
    //     this.total = data.data.total;
    //     let codes = data.data.list.map(item => item.code).join(',');
    //     if(!codes){
    //       return 
    //     }
    //     const res = await api.getTwStockData(codes);
        
    //     let result = [];
    //     res.data.forEach((item, index) => {
    //       let newItem = {
    //         nowPrice: item["當盤成交價"],
    //         preclose_px: item["當盤成交價"] + item["漲跌"],
    //         hcrate: item["Quote change"],
    //         rate: item["漲跌"],
    //         name: item["股票名稱"],
    //         code:item['股票代號'],
    //         stock_type:'tw'
    //       };
    //       result.push(newItem);
    //     });
    //     this.list = [...result];
    //   }
    // },
    async loadMore() {
      if (this.list.length < 10) {
        return;
      }
      this.loading = true;
      // 加載下一頁
      this.pageNum++;
      this.currentNum = this.pageNum * this.pageSize;
      await this.getStock();
      this.loading = false;
    },
    toDetail(val) {
      // if(true){
      //     Toast('繫統正在升級，暫關閉交易！')
      //     return
      // }
      // 詳情
      this.$router.push({
        path: "/listdetail",
        query: {
          code: val.code,
          stock_type: val.stock_type
          // name: val.name
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
.table-list-body {
  padding-top: 0.62rem;
}

.table-list {
  .li-title {
    width: 34%;
  }

  .li-base {
    width: 33%;
    text-align: center;

    &.no-bold {
      span {
        font-weight: 400;
      }
    }
  }
}

.search-btn-list {
  position: fixed;
  right: 0;
  height: 42px;
  font-size: 0.25rem;
  z-index: 10;
  border: none;
  box-shadow: none;
}

.wrapper /deep/ .mint-searchbar {
  background: #2e3138;
  position: fixed;
  width: 100%;

  .mint-searchbar-inner {
    background-color: rgba(180, 180, 180, 0.1);
  }
}
</style>
