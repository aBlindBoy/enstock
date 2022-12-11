<template>
  <div
    :class="
      `list-content-wrapper ${
        $state.theme === 'red' ? 'red-theme' : 'black-theme'
      }`
    "
  >
    <!-- <mt-header  title="">
        <router-link to="/list" slot="left"></router-link>
        <mt-button slot="left" icon="search" @click="toSearch"></mt-button>
    </mt-header> -->
    <!-- <mt-search
        style="height:auto;"
        fixed
        @click.enter.native="toSearch"
        placeholder="可輸入股票代碼"
      >
      </mt-search> -->
   
<!-- <mt-navbar v-model="selected" style="margin-top:50px">
  <mt-tab-item id="1">option A</mt-tab-item>
  <mt-tab-item id="2">option B</mt-tab-item>
  <mt-tab-item id="3">option C</mt-tab-item>
</mt-navbar> -->
   


    <div class="list-table-title">
       <ul class="table-list">
        <li class="title">
          <div>
            <ul class="clearfix">
              <li class="li-title">{{$t('common.stockName')}}</li>
              <li class="li-base" @click="soltPrice()">{{$t('common.lastPrice')}}</li>
              <li class="li-base"  @click="soltAmplitude()">{{$t('common.chgRate')}}</li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
    <div class="list-table-body" style="margin-top: 10px;">
      <ul
        class="table-list table-list-body"
        infinite-scroll-distance="10"
      >
        <li class="list-body" v-for="item in dataList" :key="item.key">
          <div>
            <ul
              class="clearfix green"
              :class="
                item.hcrate < 0
                  ? 'green'
                  : item.nowPrice - item.preclose_px == 0
                  ? ''
                  : 'red'
              "
              @click="toDetail(item)"
            >
              <li class="li-title">
                <p class="name">
                  <img
                    @click.stop="toDeleteMy(item)"
                    v-if="item.isOption"
                    :src="
                      require(`../../../static/img/list/${
                        $state.theme === 'red' ? 'red-' : ''
                      }loved-icon.png`)
                    "
                    alt=""
                  />
                  <img
                    v-else
                    @click.stop="addOptions(item)"
                    :src="
                      require(`../../../static/img/list/${
                        $state.theme === 'red' ? 'red-' : ''
                      }love-icon.png`)
                    "
                    alt=""
                  />
                 <span style="font-size: 0.25rem;text-overflow: ellipsis;overflow: hidden; white-space:nowrap;"> {{ item.stockName }}</span>
                </p>
                <p class="code">
                  <span class="code-wra">{{ item.stockCode }}</span>
                   <i
                    class="iconfont shen-mark hushen-mark"
                  >
                      US
                  </i>
                 <!-- <i v-else class="iconfont kechuang-mark">科創</i> -->
                </p>
              </li>
              <li class="li-base">
                <span>{{
                  item.latestPrice ? Number(item.latestPrice).toFixed(2) : "-"
                }}</span>
              </li>
              <li class="li-base">
                <span v-if="item.chgRate == 0">-</span>
                <span v-else> {{ item.chgRate ? item.chgRate : "0" }}%</span>
              </li>
              <!-- <li class="li-base no-bold">
                <span v-if="item.nowPrice == 0">-</span>
                <span
                  v-else>{{item.nowPrice-item.preclose_px>0 ?'+':''}}{{(item.nowPrice-item.preclose_px).toFixed(2)}}</span>
              </li> -->
            </ul>
          </div>
        </li>
      </ul>
   
      <div v-show="loading" class="load-all text-center">
        <mt-spinner type="fading-circle"></mt-spinner>
        {{$t('common.loading')}}...
      </div>
      <!-- <div v-show="!loading && list.length > 0" class="load-all text-center">
        已全部加載
      </div> -->
    </div>
  
    <foot></foot>
  </div>
</template>

<script>
import foot from "../../components/foot/foot";
import { Toast } from "mint-ui";
import * as api from "@/axios/api";
// import * as htmlparser2 from "htmlparser2";
// var cheerio = require('/node_modules/cheerio/lib/cheerio');
// const cheerio = require('cheerio');


export default {
  components: {
    foot
  },
  props: {
    selectedNumber: {
      immediate: true,  
      type: String
    }
  },
  data() {
    return {
      loading: false,
      pageNum: 1,
      pageSize: 15,
      currentNum: 15,
      // list: [],
      timer: "",
      market: [],
      changeTextClass: {},
      total: 0,
      sortIcon: require("../../../static/img/list/sort-icon.png"),
      sort:{
      },
      dataList:[]
    };
  },
  watch: {
    // selectedNumber(val) {
    //   if (val === "2" ) {
    //     // let opt={pageNum:1}
    //     this.getStock('TSE');
    //     // this.timer = setInterval(this.refreshList, 5000);
    //   } else  if(val =='5'){
    //     this.getStock('OTC');
    //   }
    //   else {
    //     clearInterval(this.timer);
    //   }
    // }
  },
  computed: {

  },
  created() {
    this.getStock();
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    // this.getStock('TSE');
  },
  methods: {
    // forceUpdate(){
    //   this.list = []
    //   this.$forceUpdate()
    // },
    async addOptions(val) {
      let data = await api.addOption({ code: val.stockCode });
      if (data.status === 0) {
        val.isOption=true
        Toast(data.msg);
      } else {
        Toast(data.msg);
      }
    },
    async toDeleteMy(val) {
      let data = await api.delOption({ code: val.stockCode });
      if (data.status === 0) {
        val.isOption=false
        Toast(data.msg);
        // this.refreshList();
      } else {
        Toast(data.msg);
      }
    },
   
    async getStock() {
      this.dataList = []
       let allOptionResult = await  api.allOption();
       let allOption = allOptionResult.data

      let res =   await api.getRanking(this.selectedNumber);
      for (let index = 0; index < res.data.length; index++) {
          let item = res.data[index]
          item.code = res.data.stockCode
          item.isOption = allOption.some(option=>{
            return option.stockCode == item.stockCode
          })
          this.dataList.push(item)
      }
     
   
    },
    async refreshList() {
      this.getStock();
     
    },
    async loadMore() {
      if (this.list.length < 10 || this.pageNum * this.pageSize >= this.total) {
        return;
      }
      // clearInterval(this.timer)
      // 加載下一頁
      this.pageNum++;
      this.currentNum = this.pageNum * this.pageSize;
      // await this.getStock();
      this.loading = false;
    },
    toDetail(val) {
      debugger
      // 詳情
      this.$router.push({
        path: "/listdetail",
        query: {
          code: val.stockCode,
          stock_type: val.stock_type
          // name: val.name
        }
      });
    },
    toSearch() {
      this.$router.push("/searchlist");
    },
    toIndexList() {
      this.$router.push("/indexlist");
    },
    soltPrice(){

    }
  }
};
</script>
<style lang="less" scoped>
@fontColor: #cfd0d1;

.table-list-body {
  // padding-top:0.62rem;
  // margin-top: 40px;
}
.list-content-wrapper{
  // overflow-y: auto;
}
.table-list {
  .li-title {
    width: 34%;
  }

  .li-base {
    width: 22%;
    text-align: center;

    &.no-bold {
      span {
        font-weight: 400;
      }
    }
  }
}

.table-list .title {
  position: static;
}

.page-part {
  margin-top: 40px;
  margin-bottom: 0px;
  border-bottom: 0.027rem solid #202020;

  .box-title {
    border-bottom: 0.027rem solid #202020;
    background-color: #1f523c;
    animation: obgFadeOut 0.8s ease forwards;
  }
}

@keyframes obgFadeOut {
  to {
    background: rgba(0, 0, 0, 0);
  }
}

.box-title {
  background-color: #5c1d29;
}

/*大盤指數*/
.box-contain {
  .more {
    position: absolute;
    right: 0;
    padding-top: 0.5rem;
    padding-right: 0.2rem;
    cursor: pointer;
  }

  .tab {
    float: left;
    width: 31%;
    margin: 0.05rem 0.5%;
    margin-top: 0;
    text-align: center;
    padding: 0.1rem 0;
    background: none !important;

    p {
      margin-top: 0.1rem;
    }

    .name {
      color: @fontColor;
      font-size: 0.22rem;
    }

    .price {
      font-size: 0.34rem;
    }

    .status {
      margin-top: 0.1rem;
      font-size: 0.22rem;
    }
  }
}
.list-content-wrapper {
  width: 100%;
  // height: 100%;
  position: relative;
  .list-table-title {
    width: 100%;
    // position: absolute;
    // left: 0;
    // top: 50px;
    margin-top: 10px;
    .title {
      position: relative;
      top: 0;
      left: 0;
      ul {
        li {
          font-size: 0.28rem;
          font-family: MicrosoftYaHeiLight;
          font-weight: 400;
          color: rgba(255, 255, 255, 0.5);
          text-align: center;
          display: flex;
          justify-content: center;
          align-items: center;
          img {
            width: 0.15rem;
            margin-left: 0.06rem;
          }
          &.li-title {
            width: 40%;
          }
          &.li-base {
            width: 30%;
          }
        }
      }
    }
  }
  .list-table-body {
    width: 100%;
    // height: 100%;
    box-sizing: border-box;
    // overflow-y: auto;
 
    ul {
      .li-base {
        width: 30%;
        span {
          font-size: 0.26rem;
          font-family: MicrosoftYaHeiLight;
          font-weight: 400;
        }
      }
      .li-title {
        width: 40%;
        .name {
          color: #fff;
          width: 100%;
          display: flex;
          align-items: center;
          margin-top: 0.05rem;
          img {
            width: 0.32rem;
            margin-right: 0.05rem;
          }
        }
        .code {
          width: 100%;
          display: flex;
          align-items: center;
        }
        .shen-mark {
          color: #138eb4;
          background: none;
          margin-left: 0.1rem;
          border: 1px solid rgba(20, 142, 180, 1);
          width: 0.48rem;
          height: 0.28rem;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 0.1rem;
        }
        .code-wra {
          // width: 1.35rem;
          height: 0.38rem;
          // display: block;
          // background: url(/static/img/list/code-bg.png) no-repeat;
          // background-size: contain;
          // background-color: #138EB4;
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 0.05rem;
          color: white;
          position: relative;
          margin-left: 0.37rem;
          &::before {
            content: "";
            display: block;
            width: 0;
            height: 0;
            border-top: 0.1rem solid transparent;
            border-right: 0.15rem solid #138eb4;
            border-bottom: 0.1rem solid transparent;
            position: absolute;
            left: 0;
            top: 50%;
            margin-top: -0.095rem;
            left: -0.1rem;
            display: none;
          }
        }
      }
    }
  }
}
.red-theme {
  background: white;
  .table-list {
    ul {
      li {
        border-color: #f7f7f7;
      }
    }
  }
  .list-table-title {
    .title {
      background: white;
      ul {
        li {
          color: #212121;
        }
      }
    }
  }
  .list-table-body {
    li.li-title {
      .name {
        color: #000;
      }
      .code {
        i {
          color: #bb1715 !important;
          border-color: #bb1715 !important;
        }
      }
      .code-wra {
        color: #bb1715 !important;
      }
      .futures-code {
        color: #bb1715 !important;
      }
    }
  }
  .load-all {
    background-color: #e9e9e9;
    color: #000;
  }
}
</style>
