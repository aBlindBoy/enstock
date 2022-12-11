<!-- <template>
  <div class="wrapper">
    <mt-button slot="right" class="search-btn-list" icon="search" @click="getStock">搜索</mt-button>
    <mt-search
      fixed
      show
      autofocus
      v-model="keywords"
      @keyup.enter.native="getStock"
      placeholder="可輸入股票代碼"
    >
      <ul class="table-list">
        <li class="title">
          <div>
            <ul class='clearfix'>
              <li class="li-title">股票</li>
              <li class="li-base">最新</li>
              <li class="li-base">Quote change</li>
              <li class="li-base text-center">
                刪自選
              </li>
            </ul>
          </div>

        </li>
      </ul>
      <ul class="table-list table-list-body"
          v-infinite-scroll="loadMore"
          infinite-scroll-disabled="loading"
          infinite-scroll-distance="10">
        <li class="list-body" v-for="item in list" :key="item.key">
          <div>
            <ul class="clearfix" :class="item.nowPrice-item.preclose_px<0?'green':'red'">
              <li @click='toDetail(item)' :class="item.stock_plate == '科創'?'li-title li-title-kc':'li-title'">
                <p class="name">
                  {{item.stockName}}
                </p>
                <p v-if="item.stock_plate == '科創'" class="code"><i class="iconfont kechuang-mark">科創</i>{{item.stockCode}}
                </p>
                <p v-else class="code">
                  <i :class="item.stock_type == 'sz'?'iconfont shen-mark hushen-mark':'iconfont hushen-mark'">台</i>
                  {{item.stockCode}}
                </p>
              </li>
              <li @click='toDetail(item)' class="li-base">
                <span :class="item.nowPrice-item.preclose_px<0?'green':'red'">{{item.nowPrice?Number(item.nowPrice).toFixed(2):'-'}}</span>
              </li>
              <li @click='toDetail(item)' class="li-base">
                <span v-if="item.nowPrice == 0">-</span>
                <span v-else :class="item.nowPrice-item.preclose_px<0?'green':'red'">{{item.nowPrice-item.preclose_px>0?'+':''}}{{item.hcrate?item.hcrate:'-'}}%</span>
              </li>
              <li class="li-base text-center">
                <mt-button plain @click="toDeleteMy"><i class="iconfont icon-shanchucopy"></i></mt-button>
              </li>
            </ul>
          </div>

        </li>
      </ul>
      <div v-show="loading" class="load-all text-center">
        <mt-spinner type="fading-circle"></mt-spinner>
        {{$t('common.loading')}}...
      </div>
      <div v-show="!loading" class="load-all text-center">
        {{$t('common.allLoaded')}}
      </div>
    </mt-search>
    <foot></foot>
  </div>
</template>

<script>
import foot from '../../components/foot/foot'
import { Toast } from 'mint-ui'
import * as api from '@/axios/api'

export default {
  components: {
    foot
  },
  props: {},
  data () {
    return {
      loading: false,
      list: [],
      timer: '',
      keywords: '', // 查詢
      pageNum: 1,
      pageSize: 15,
      currentNum: 15,
      hasSearch: false // 是否查詢
    }
  },
  watch: {},
  computed: {},
  created () {
    this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    if (this.$state.theme =='red') {
      document.body.classList.remove('red-bg')
      document.body.classList.add('black-bg')
    }
    clearInterval(this.timer)
  },
  mounted () {
    //   this.getStock()
    if (this.$state.theme =='red') {
        document.body.classList.remove('black-bg')
        document.body.classList.add('red-bg')
    }
  },
  methods: {
    async getStock () {
      let opt = {
        keyWords: this.keywords,
        pageNum: this.pageNum,
        pageSize: 15
      }
      this.hasSearch = true
      let data = await api.getMyList(opt)
      if (data.status === 0) {
        this.list = data.data.list
      } else {
        Toast(data.msg)
      }
    },
    async refreshList () {
      // 判斷是不是已經查詢 或者是否正在加載下一頁 是則退出，不進行刷新
      if (!this.hasSearch || this.loading) {
        return
      }
      let opt = {
        keyWords: this.keywords,
        pageNum: 1,
        pageSize: this.currentNum
      }
      let data = await api.getMyList(opt)
      this.list = data.data.list
    },
    async loadMore () {
      if (this.list.length < 10) {
        return
      }
      this.loading = true
      // 加載下一頁
      this.pageNum++
      this.currentNum = this.pageNum * this.pageSize
      await this.getStock()
      this.loading = false
    },
    toDeleteMy () {
      let data = api.delOption({ code: this.$route.query.code })
      if (data.status === 0) {
        Toast(data.msg)
      } else {
        Toast(data.msg)
      }
    },
    toDetail (val) {
      // if(true){
      //     Toast('繫統正在升級，暫關閉交易！')
      //     return
      // }
      // 詳情
      this.$router.push({
        path: '/listdetail',
        query: {
          code: val.code,
          name: val.name
        }
      })
    }
  }
}
</script>
<style lang="less" scoped>
  .table-list-body {
    padding-top: 0.62rem;
  }

  .search-btn-list {
    position: fixed;
    right: 0;
    height: 28px;
    font-size: 0.25rem;
    // background: #2e3138;
    z-index: 10;
    border: none;
    box-shadow: none;
    margin-top: 8px;
  }

  .wrapper /deep/ .mint-searchbar {
    // background: #2e3138;
    position: fixed;
    width: 100%;

    .mint-searchbar-inner {
      background-color: rgba(180, 180, 180, 0.1)
    }
  }

  .red-theme {

  }
</style> -->
