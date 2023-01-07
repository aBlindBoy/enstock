<template>
  <div
    :class="
      `list-content-wrapper ${
        $state.theme === 'red' ? 'red-theme' : 'black-theme'
      }`
    "
  >
    <!-- 漲幅榜 -->
    <div class="list-table-title">
      <h5 style=" margin-bottom: 10px;line-height: 20px;border-left: 5px solid red;"><span style="margin-left:20px">漲幅榜</span></h5>
       <ul class="table-list">
        <li class="title">
          <div>
            <ul class="clearfix">
              <li class="li-title">股票</li>
              <li class="li-base" @click="soltPrice()">最新</li>
              <li class="li-base"  @click="soltAmplitude()">漲幅</li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
    <div class="list-table-body" style="margin-top: 80px;">
      <ul
        class="table-list table-list-body"
        infinite-scroll-distance="10"
      >
        <li class="list-body" v-for="item in upList" :key="item.key">
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
                  {{ item.name }}
                </p>
                <p class="code">
                  <span class="code-wra">{{ item.code }}</span>
                   <i
                    class="iconfont shen-mark hushen-mark"
                  >
                    台
                  </i>
                 <!-- <i v-else class="iconfont kechuang-mark">科創</i> -->
                </p>
              </li>
              <li class="li-base">
                <span>{{
                  item.nowPrice ? Number(item.nowPrice).toFixed(2) : "-"
                }}</span>
              </li>
              <li class="li-base">
                <span v-if="item.nowPrice == 0">-</span>
                <span v-else> {{ item.hcrate ? item.hcrate : "0" }}%</span>
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
        加載中...
      </div>
      <!-- <div v-show="!loading && list.length > 0" class="load-all text-center">
        已全部加載
      </div> -->
    </div>

  
    <!-- 跌幅榜 -->
    <div class="list-table-title" style="margin-top: 20px;">
      <h5 style=" margin-bottom: 10px;line-height: 20px;border-left: 5px solid red;"><span style="margin-left:20px">跌幅榜</span></h5>
       <ul class="table-list">
        <li class="title">
          <div>
            <ul class="clearfix">
              <li class="li-title">股票</li>
              <li class="li-base" @click="soltPrice()">最新</li>
              <li class="li-base"  @click="soltAmplitude()">跌幅</li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
    
    <div class="list-table-body" style="margin-top: 80px;">
      <ul
        class="table-list table-list-body"
        infinite-scroll-distance="10"
      >
        <li class="list-body" v-for="item in downList" :key="item.key">
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
                  {{ item.name }}
                </p>
                <p class="code">
                  <span class="code-wra">{{ item.code }}</span>
                  <i
                    class="iconfont shen-mark hushen-mark"
                  >
                    台
                  </i>
                  <!-- <i v-else class="iconfont kechuang-mark">科創</i> -->
                </p>
              </li>
              <li class="li-base">
                <span>{{
                  item.nowPrice ? Number(item.nowPrice).toFixed(2) : "-"
                }}</span>
              </li>
              <li class="li-base">
                <span v-if="item.nowPrice == 0">-</span>
                <span v-else> {{ item.hcrate ? item.hcrate : "0" }}%</span>
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
        加載中...
      </div>
      <!-- <div v-show="!loading && list.length > 0" class="load-all text-center">
        已全部加載
      </div> -->
    </div>

    <!-- 成交量榜 -->
    <div class="list-table-title" style="margin-top: 20px;">
      <h5 style=" margin-bottom: 10px;line-height: 20px;border-left: 5px solid red;"><span style="margin-left:20px">成交量榜</span></h5>
       <ul class="table-list">
        <li class="title">
          <div>
            <ul class="clearfix">
              <li class="li-title">股票</li>
              <li class="li-base" @click="soltPrice()">最新</li>
              <li class="li-base"  @click="soltAmplitude()">成交量</li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
    
    <div class="list-table-body" style="padding-top: 80px;padding-bottom: 80px;">
      <ul
        class="table-list table-list-body"
        infinite-scroll-distance="10"
      >
        <li class="list-body" v-for="item in volumeList" :key="item.key">
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
                    v-if="item.isOption "
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
                  {{ item.name }}
                </p>
                <p class="code">
                  <span class="code-wra">{{ item.code }}</span>
                   <i
                    class="iconfont shen-mark hushen-mark"
                  >
                    台
                  </i>
                  <!-- <i v-else class="iconfont kechuang-mark">科創</i> -->
                </p>
              </li>
              <li class="li-base">
                <span>{{
                  item.nowPrice ? Number(item.nowPrice).toFixed(2) : "-"
                }}</span>
              </li>
              <li class="li-base">
                <!-- <span v-if="item.nowPrice == 0">-</span>
                <span v-else> {{ item.hcrate ? item.hcrate : "0" }}%</span> -->
                <span>{{item.volume}}張</span>
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
        加載中...
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
      list: [],
      timer: "",
      market: [],
      changeTextClass: {},
      total: 0,
      sortIcon: require("../../../static/img/list/sort-icon.png"),
      sort:{
      },
      upList:[],
      downList:[],
      volumeList:[],
    };
  },
  watch: {
    selectedNumber(val) {
        this.getStock(val);
    }
  },
  computed: {

  },
  created() {
    this.getStock('TSE');
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    // this.getStock('TSE');
  },
  methods: {
    forceUpdate(){
      this.list = []
      this.$forceUpdate()
    },
    async addOptions(val) {
      let data = await api.addOption({ code: val.code,marketType:"TW" });
      if (data.status === 0) {
        val.isOption=true
        Toast("添加自選成功");
      } else {
        Toast(data.msg);
      }
    },
    async toDeleteMy(val) {
      
      let data = await api.delOption({ code: val.code });
      if (data.status === 0) {
        val.isOption=false
        Toast("刪除自選股成功");
        // this.refreshList();
      } else {
        Toast(data.msg);
      }
    },
    // async getMarket() {
    //   // 獲取大盤指數
    //   let result = await api.getIndexMarket();
    //   if (result.status === 0) {
    //     this.market = result.data;
    //   } else {
    //     Toast(result.msg);
    //   }
    // },
    async getStock(exchange) {
      this.upList=[]
      this.downList=[]
      this.volumeList=[]
      
       let allOptionResult = await  api.allOption();
       let allOption = allOptionResult.data
        //漲幅榜
       const res = await api.getTwRanking('up',exchange);
        for (let index = 0; index < res.data.length; index++) {
            let item = {}
            item.name = res.data[index]['200009'] //股票名稱
            item.code = res.data[index]['200010'] //股票代號
            item.nowPrice = res.data[index]['6'] //最新价格
            item.hcrate = res.data[index]['56'] //漲跌幅
            item.preclose_px =  res.data[index]['11'] //漲跌价格
            item.stock_type = 'tw'
            item.isOption = allOption.some(option=>{
             return option.stockCode == item.code
            })
            this.upList.push(item)
        }
        //跌幅榜
        const res1 = await api.getTwRanking('down',exchange);
        for (let index = 0; index < res1.data.length; index++) {
            let item = {}
            item.name = res1.data[index]['200009'] //股票名稱
            item.code = res1.data[index]['200010'] //股票代號
            item.nowPrice = res1.data[index]['6'] //最新价格
            item.hcrate = res1.data[index]['56'] //漲跌幅
            item.preclose_px =  res1.data[index]['11'] //漲跌价格
            item.stock_type = 'tw'
            item.isOption = allOption.some(option=>{
             return option.stockCode == item.code
            })
            this.downList.push(item)
        }
        //跌幅榜
        const res2 = await api.getTwRanking('volume',exchange);
        for (let index = 0; index < res2.data.length; index++) {
            let item = {}
            item.name = res2.data[index]['200009'] //股票名稱
            item.code = res2.data[index]['200010'] //股票代號
            item.nowPrice = res2.data[index]['6'] //最新价格
            item.hcrate = res2.data[index]['56'] //漲跌幅
            item.preclose_px =  res2.data[index]['11'] //漲跌价格
            item.volume =  res2.data[index]['800001']//成交量
            item.isOption = allOption.some(option=>{
             return option.stockCode == item.code
            })
            item.stock_type = 'tw'
            this.volumeList.push(item)
        }
        this.loading = false;
   
    },
    async refreshList() {
      this.getStock('up','TSE');
      // if (this.loading) {
      //   return;
      // }
      // let opt = {
      //   stockPlate: "",
      //   pageNum: 1,
      //   pageSize: this.currentNum
      // };
      // if(this.selectedNumber=='2'){
      //   opt.stockPlate='上市'
      // }
      // if(this.selectedNumber=='5'){
      //   opt.stockPlate='上櫃'
      // }
      // let data = await api.getTwStockPageList(opt);
      // let result=[]
      // if (data.status === 0) {
      //   this.total = data.data.total;
      //   let codes = data.data.list.map(item => item.code).join(',');
      //   if(!codes){
      //     return 
      //   }
      //   const res = await api.getTwStockData(codes);
      //   res.data.forEach((item, index) => {
      //     let newItem = {
      //       ...data.data.list[index],
      //       nowPrice: item["當盤成交價"],
      //       preclose_px: item["當盤成交價"] + item["漲跌"],
      //       hcrate: item["漲跌幅"],
      //       name: item["股票名稱"],
      //       code:item['股票代號'],
      //       stock_type:'tw',
      //     };
      //     result.push(newItem);
      //   });
      // } 
      // this.list=result
      // 刷新大盤指數
      // let result = await api.getIndexMarket()
      // this.changeTextClass = {}
      // if(result.status == 0){
      //     // this.market = result.data.market
      //     result.data.forEach((element,i) => {
      //     this.changeTextClass[i] = ''
      //     if(element.currentPoint != this.market[i].currentPoint){
      //         this.changeTextClass[i] = true // 設定對應的動畫過濾
      //         this.market[i].currentPoint = element.currentPoint
      //         this.market[i].floatPoint = element.floatPoint
      //         this.market[i].floatRate = element.floatRate
      //     }
      //     });
      // }else{
      //     Toast(result.msg)
      // }
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
      // 詳情
      this.$router.push({
        path: "/listdetail",
        query: {
          code: val.code,
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
  overflow-y: auto;
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
    position: absolute;
    left: 0;
    // top: 50px;
    margin-top: 20px;
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
          width: 0.28rem;
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
