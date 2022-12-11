<template>
  <div class="wrapper table-chart2-cont" >
    <div class="panel-box">
      <div class="panel-title clearfix">
        <div class="detail-box pull-left">
          <span class="name">{{detail.name}}</span>
          <span class="code">{{detail.code}}</span>
        </div>
        <ul class="chart-title pull-right">
          <li v-for='(navItem,idx) in NarBarArry' :key='idx' :class="navIndex == idx ? 'on li-type':'li-type'">
            <a class="chart-type" href="javascript:;" @click='ChangeChartByNav(idx,$event)'>{{navItem}}</a>
          </li>
        </ul>
      </div>
      <div class="panel-body">
        <chart2sub :ucode="ucode" :idx="navIndex" :NarBarArry="NarBarArry"></chart2sub>
      </div>
    </div>
    <div class="table">
      <div class="panel">
      </div>
    </div>
  </div>
</template>

<script>
  import chart2sub from "./chart2sub";

  export default {
    components: {chart2sub},
    watch: {},
    props: {
      code: {
        type: String,
        default() {

        }
      },
      ucode: {
        type: String,
        default() {

        }
      },
      detail: {
        type: Object,
        default() {

        }
      }
    },
    data() {
      return {
        //NarBarArry: ['分時', '1分', '5分', '30分', '60分', '日K', '周K', '月K'],
        // NarBarArry: ,
        navIndex: 0,
        event: {},
        emptyShow: true,
        ChartsTime: [], // 分時圖時間
        ChartstFuturesTime: [], // 分時圖時間
        timer: null,
        isOptionOpt: false, // 是否自選
        type: '0', // 類型
        styleName: 'red',
        k_top_closing: 99999999999
      }
    },
    computed: {
      change() {
        return this.$route.query.code
      },
      NarBarArry(){
        return [this.$t('tradingFloor.real'), this.$t('tradingFloor.oneMinute'),
        this.$t('tradingFloor.fiveMinute'), this.$t('tradingFloor.dayLine'), 
        this.$t('tradingFloor.weekLine'), this.$t('tradingFloor.monthLine')]
      }
    },
    created() {
    },
    beforeDestroy() {
    },
    mounted() {
      this.styleName = window.localStorage.getItem('styleName')
    },
    methods: {
      ChangeChartByNav(idx, event) {
        this.navIndex = idx
        this.event = event
        console.log(idx, event)
      }
    }
  }
</script>
<style lang="less" scoped>
  .panel-box {
    // background: #1a202c;
    // padding: 0 5px;

    .panel-title {
      padding: 3px 10px;
    }

    .panel-body {
      // background: #171e2a;
      position: relative;

      .chart-box {
        height: 480px;
      }
    }
  }

  .chart-title {
    line-height: 35px;

    .li-type {
      float: left;

      &.on {
        .chart-type {
          color: #efbb53;
        }
      }
    }

    .chart-type {
      color: #5a657e;
      font-size: 12px;
      padding: 7px 5px;
      height: 14px;
      font-weight: 800;
    }
  }

  .detail-box {
    height: 40px;
    line-height: 40px;

    .name {
      color: #efbb53;
      font-size: 16px;
      margin-left: 8px;
    }

    .code {
      color: #9e9e9e;
      margin-left: 6px;
    }
  }

  .empty {
    padding-top: 100px;
    color: #5a657e;

    .iconfont {
      display: block;
      font-size: 120px;
      color: #5a657e;
      margin-bottom: 20px;
    }
  }
</style>
