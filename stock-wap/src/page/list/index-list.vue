<template>
  <div class="wrapper">
    <mt-header fixed title="index">
      <router-link to="/list" slot="left">
        <mt-button icon="back">back</mt-button>
      </router-link>
      <!-- <mt-button slot="right" icon="search" @click="toSearch"></mt-button> -->
    </mt-header>
    <ul class="table-list">
      <li class="title">
        <div>
          <ul class='clearfix'>
            <li class="li-title">name</li>
            <li class="li-base">latest price</li>
            <li class="li-base">chg %</li>
            <li class="li-base">chg</li>
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
          <ul class="clearfix" :class="item.floatPoint<0?'green':item.floatPoint==0?'':'red'" @click='toDetail(item)'>
            <li class="li-title">
              <p class="name"><i :class="item.transState == 1?'iconfont icon-jiaoyi':'iconfont icon-jinzhi'"></i>{{item.indexName}}
              </p>
              <p class="code">{{item.indexCode}}</p>
            </li>
            <li class="li-base">
              <span>{{item.currentPoint?Number(item.currentPoint).toFixed(2):'-'}}</span>
            </li>
            <li class="li-base">
              <span>{{item.floatRate>0 ?'+':''}} {{item.floatRate?Number(item.floatRate).toFixed(2):'0'}}%</span>
            </li>
            <li class="li-base no-bold">
              <span>{{item.floatPoint >0 ?'+':''}}{{Number(item.floatPoint).toFixed(2)}}</span>
            </li>
          </ul>
        </div>

      </li>
    </ul>
    <div v-if="list.length<=0" class="load-all text-center">
      <mt-spinner type="fading-circle"></mt-spinner>
    </div>
    <div v-show="loading" class="load-all text-center">
      <mt-spinner type="fading-circle"></mt-spinner>
      {{$t('common.loading')}}...
    </div>
    <div v-show="!loading && list.length>0" class="load-all text-center">
      {{$t('common.allLoaded')}}
    </div>
    <div class="footer-btn">
      <p class="red">*Note: 
        <!-- <i class="iconfont icon-jiaoyi"></i>???????????????????????? -->
        <span><i class="iconfont icon-jiaoyi red"></i>Indicates that the index is tradable</span>
        <span class="pull-right"><i class="iconfont icon-jinzhi yellow"></i>Indicates that the index is not tradable</span>
      </p>
    </div>
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
      pageNum: 1,
      pageSize: 15,
      currentNum: 15,
      list: [],
      timer: ''
    }
  },
  watch: {},
  computed: {},
  created () {
    this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  mounted () {
    this.getListMarket()
  },
  methods: {
    async getListMarket () {
      // ??????????????????
      let result = await api.getListMarket()
      if (result.status === 0) {
        this.list = result.data
      } else {
        Toast(result.msg)
      }
    },
    async getStock () {
      let opt = {
        pageNum: this.pageNum,
        pageSize: 15
      }
      let data = await api.getListMarket(opt)
      if (data.status === 0) {
        data.data.list.forEach(element => {
          this.list.push(element)
        })
      } else {
        Toast(data.msg)
      }
    },
    async refreshList () {
      if (this.loading) {
        return
      }
      let opt = {
        pageNum: 1,
        pageSize: this.currentNum
      }
      let data = await api.getListMarket(opt)
      this.list = data.data
    },
    async loadMore () {
      if (this.list.length < 100) {
        return
      }
      this.loading = true
      // clearInterval(this.timer)
      // ???????????????
      this.pageNum++
      this.currentNum = this.pageNum * this.pageSize
      await this.getListMarket()
      this.loading = false
    },
    async toDetail (val) {
      // ??????????????????????????????
      // let opt = {
      //     indexId:val.id,
      // }
      // let data = await api.getTransMarket(opt)
      // if(data.status == 0){
      // ??????
      if (val.transState === 1) {
        this.$router.push({
          path: '/indexBuy',
          query: {
            info: val
          }
        })
      } else {
        Toast('The index is temporarily unavailable for trading!')
      }
    },
    toSearch () {
      this.$router.push('/indexsearchlist')
    }
  }
}
</script>
<style lang="less" scoped>
  .wrapper {
    padding-bottom: 1.6rem;
  }

  .table-list-body {
    padding-top: 0.62rem;
    margin-top: 40px;
  }

  .table-list {
    ul {
      .li-title {
        width: 28%;

        .name {
          .iconfont {
            background: none;
            color: #d50000;

            &.icon-jinzhi {
              color: #ff9800
            }
          }
        }
      }
    }

    .li-base {
      width: 24%;
      text-align: center;

      &.no-bold {
        span {
          font-weight: 400;
        }
      }
    }
  }

  .footer-btn {
    position: fixed;
    z-index: 1;
    width: 100%;
    padding-right: 0;
    bottom: 0.94rem;
    padding: 0.2rem;
    // height: 1.1rem;
    // line-height: .1rem;
    box-shadow: 0px 0px 4px rgba(6, 0, 1, 0.2);

    .iconfont {
      margin: 0 0.1rem;
      font-size: 0.28rem;

      &.yellow {
        color: #ff9800;
      }
    }
  }
</style>
