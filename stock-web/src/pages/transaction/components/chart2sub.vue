<template>
  <div class="chart-box-warpper">

    <div class="right" ref="right">
    
      <div class="rightContent" ref="rightContent">
        <div class="contentBox" v-show="chartType == 'minute'">
         

          <div class="hqchart" id="hqchart_minute" ref="kline"></div>

          <div class="indexWrap" ref="minute_indexWrap">
            <div class="btnGroup">
              <div
                class="btn"
                v-for="indexName in MinuteIndexMenu"
                :key="indexName.ID"
                @click="changeChartIndex(indexName.ID)"
              >
                {{ indexName.Name }}
              </div>
            </div>
          </div>

        </div>

        <div class="contentBox" v-show="chartType == 'kline'">
          <div class="hqchart" id="hqchart_kline" ref="kline2"></div>

          <div class="indexWrap" ref="kline_indexWrap">
            <div class="btnGroup">
              <div
                class="btn"
                v-for="item in KLineIndexMenu"
                :key="item.ID"
                @click="ChangeKLineIndex(item)"
              >
                {{ item.Name }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import _ from "lodash";
import HQChart from "hqchart";
import "hqchart/src/jscommon/umychart.resource/css/tools.css";
import "hqchart/src/jscommon/umychart.resource/font/iconfont.css";
import EastMoney from "./HQData.js";

//源码调试用
//import Chart from '../jscommon/umychart.vue/umychart.vue.js'
//import '../jscommon/umychart.resource/css/tools.css'
//import '../jscommon/umychart.resource/font/iconfont.css'
//var HQChart={ Chart:Chart };
HQChart.Chart.MARKET_SUFFIX_NAME.GetMarketStatus = function(symbol) {
  return 2;
}; //一直交易

function DefaultData() {}

DefaultData.GetMinuteOption = function() {
  var option = {
    Type: "分钟走势图", //创建图形类型
    //Type:'分钟走势图横屏',

    //窗口指标
    Windows: [{ Index: "MACD", Modify: false, Change: false, Close: false }],

    Symbol: "AAPL.usa",
    IsAutoUpdate: true, //是自动更新数据
    AutoUpdateFrequency: 20000,
    DayCount: 1, //1 最新交易日数据 >1 多日走势图
    IsShowRightMenu: false, //是否显示右键菜单
    //CorssCursorTouchEnd:true,

    CorssCursorInfo: { Left: 1, Right: 1 },

    MinuteLine: {
      IsDrawAreaPrice: true, //是否画价格面积图
      IsShowAveragePrice: true //不显示均线
    },

    //边框
    Border: {
      Left: 40, //左边间距
      Right: 40, //右边间距
      Top: 25,
      Bottom: 25,
      AutoRight: { Blank: 10, MinWidth: 40 },
      AutoLeft: { Blank: 10, MinWidth: 40 }
    },

    //子框架设置
    Frame: [{ SplitCount: 5 }, { SplitCount: 3 }],

    //扩展图形
    ExtendChart: [
      //{Name:'MinuteTooltip' }  //手机端tooltip
    ]
  };

  return option;
};

DefaultData.GetMinuteIndexMenu = function() {
  var data = [
    { Name: "MACD", ID: "MACD", WindowIndex: 2 },
    { Name: "KDJ", ID: "KDJ", WindowIndex: 2 },
    { Name: "DMI", ID: "DMI", WindowIndex: 2 },
    { Name: "ROC", ID: "ROC", WindowIndex: 2 }
  ];

  return data;
};

DefaultData.GetKLineIndexMenu = function() {
  var data = [
    { Name: "MA", ID: "MA", WindowIndex: 0 },
    { Name: "BOLL", ID: "BOLL", WindowIndex: 0 },
    { Name: "MACD", ID: "MACD", WindowIndex: 1 },
    { Name: "KDJ", ID: "KDJ", WindowIndex: 1 },
    { Name: "DMI", ID: "DMI", WindowIndex: 1 },
    { Name: "ROC", ID: "ROC", WindowIndex: 1 }
  ];

  return data;
};

DefaultData.GetKLineOption = function() {
  var option = {
    Type: "历史K线图", //创建图形类型

    //窗口指标
    Windows: [
      { Index: "MA", Modify: true, Modify: true, Change: true },
      { Index: "VOL", Modify: true, Change: true, Close: false },
      { Index: "MACD", Modify: true, Change: true, Close: false }
    ],

    Symbol: "000001.sh",
    IsAutoUpdate: true, //是自动更新数据
    AutoUpdateFrequency: 15000,
    IsApiPeriod: true,
    IsShowRightMenu: false, //是否显示右键菜单
    //CorssCursorTouchEnd:true,

    KLine: {
      DragMode: 1, //拖拽模式 0 禁止拖拽 1 数据拖拽 2 区间选择
      Right: 0, //复权 0 不复权 1 前复权 2 后复权
      Period: 0, //周期 0 日线 1 周线 2 月线 3 年线
      MaxReqeustDataCount: 1000, //数据个数
      PageSize: 270, //一屏显示多少数据
      KLineDoubleClick: false, //双击分钟走势图
      IsShowTooltip: true, //是否显示K线提示信息
      DrawType: 0,
      RightSpaceCount: 2
    },

    CorssCursorInfo: { Left: 0, Right: 1 },

    //标题设置
    KLineTitle: {
      IsShowName: true, //不显示股票名称
      IsShowSettingInfo: true //不显示周期/复权
    },

    //边框
    Border: {
      Left: 2, //左边间距
      Right: 20, //右边间距
      Top: 25,
      Bottom: 25,
      AutoRight: { Blank: 10, MinWidth: 40 }
    },

    //子框架设置
    Frame: [
      {
        SplitCount: 5,
        IsShowLeftText: false,
        Custom: [
          {
            Type: 0,
            Position: "right"
          }
        ]
      },
      { SplitCount: 3, IsShowLeftText: false },
      { SplitCount: 3, IsShowLeftText: false }
    ],

    //扩展图形
    ExtendChart: [
      //{ Name:'KLineTooltip' },  //手机端tooltip
    ]
  };

  return option;
};

DefaultData.GetMinuteDayMenu = function() {
  var data = [
    { Name: "1日", ID: 1 },
    { Name: "2日", ID: 2 },
    { Name: "3日", ID: 3 },
    { Name: "4日", ID: 4 },
    { Name: "5日", ID: 5 }
  ];

  return data;
};

DefaultData.GetKLinePeriodMenu = function() {
  var data = [
    { Name: "日线", ID: 0 },
    { Name: "周线", ID: 1 },
    { Name: "月线", ID: 2 },

    { Name: "1分钟", ID: 4 },
    { Name: "5分钟", ID: 5 },
    { Name: "15分钟", ID: 6 },
    { Name: "30分钟", ID: 7 },
    { Name: "60分钟", ID: 8 }
  ];

  return data;
};

DefaultData.GetKLineRightMenu = function() {
  var data = [
    { Name: "不复权", ID: 0 },
    { Name: "前复权", ID: 1 },
    { Name: "后复权", ID: 2 }
  ];

  return data;
};

export default {
  data() {
    var data = {
      MinuteDayMenu: DefaultData.GetMinuteDayMenu(),
      MinuteDayIndex: 0,

      KLinePeriodMenu: DefaultData.GetKLinePeriodMenu(),
      KLinePeriodIndex: 0,

      KLineRightMenu: DefaultData.GetKLineRightMenu(),
      KLineRightIndex: 0,
      IsShowRightMenu: true,

      MinuteIndexMenu: DefaultData.GetMinuteIndexMenu(),
      KLineIndexMenu: DefaultData.GetKLineIndexMenu(),

      Symbol: "", //HQChart内部编码美股加后缀.usa AAPL.usa
      Chart: null, //图形控件  分时图
      KLineChart: null, //图形控件  K线图
      //NavMenuAry: DefaultData.GetTestSymbolMenu(),

      VolChartHeight: 10,
      chartType: "minute"
    };

    return data;
  },

  props: ["idx", "ucode", "NarBarArry"],
  watch: {
    ucode(newVal) {
      this.ChangeSymbol(`${newVal}.tw`);
    },
    idx(newVal) {
      this.chartType = "kline";
      switch (newVal) {
        case 0:
          this.chartType = "minute";
          this.Chart.ChangeDayCount(1);
          break;
        case 1: {
          this.KLineChart.ChangePeriod(4);
          break;
        }
        case 2: {
          this.KLineChart.ChangePeriod(5);
          break;
        }
        case 3: {
          this.KLineChart.ChangePeriod(0);
          break;
        }
        case 4: {
          this.KLineChart.ChangePeriod(1);
          break;
        }
        case 5: {
          this.KLineChart.ChangePeriod(2);
          break;
        }
        // case 2: {
        //   this.KLineChart.ChangePeriod(5);
        //   break;
        // }
        // case 3: {
        //   this.KLineChart.ChangePeriod(7);
        //   break;
        // }
        // case 4: {
        //   this.KLineChart.ChangePeriod(8);
        //   break;
        // }
        // case 5: {
        //   this.KLineChart.ChangePeriod(0);
        //   break;
        // }
        // case 6: {
        //   this.KLineChart.ChangePeriod(1);
        //   break;
        // }
        // case 7: {
        //   this.KLineChart.ChangePeriod(2);
        //   break;
        // }
        default:
          break;
      }
    }
  },
  mounted() {
    this.OnSize();
    this.SetChartStyle();
    this.$nextTick(() => {
      this.CreateMinuteChart();
      this.CreateKLineChart();
    });

    window.onresize = _.debounce(this.OnSize, 200);
  },

  methods: {
    OnSize() {
      var width = this.$refs.right.clientWidth;
      // var rightTab = this.$refs.rightTab
      // var periodWrap = this.$refs.minute_periodWrap;
      // var indexWrap = this.$refs.minute_indexWrap;
      // var statementWrap = this.$refs.minute_statementWrap;
      // var chartHeight = window.innerHeight - rightTab.offsetHeight - periodWrap.offsetHeight - indexWrap.offsetHeight - statementWrap.offsetHeight-300;
      var indexWrap = this.$refs.minute_indexWrap;
      var chartHeight = window.innerHeight - indexWrap.offsetHeight - 410;

      var kline = this.$refs.kline;
      kline.style.width = width + "px";
      kline.style.height = chartHeight + "px";
      console.log(width, chartHeight);

      // var width = this.$refs.kline_right.clientWidth;
      // var periodWrap = this.$refs.kline_periodWrap;
      // var indexWrap = this.$refs.kline_indexWrap;
      // var statementWrap = this.$refs.kline_statementWrap;
      // var chartHeight = window.innerHeight - rightTab.offsetHeight - periodWrap.offsetHeight - indexWrap.offsetHeight - statementWrap.offsetHeight-300;
      var indexWrap = this.$refs.minute_indexWrap;
      var chartHeight = window.innerHeight - indexWrap.offsetHeight - 410;
      var kline2 = this.$refs.kline2;
      kline2.style.width = width + "px";
      kline2.style.height = chartHeight + "px";

      if (this.Chart) this.Chart.OnSize();
      if (this.KLineChart) this.KLineChart.OnSize();
    },

    changeRightContent(type) {
      this.chartType = type;

      this.$nextTick(() => {
        this.OnSize();
      });
    },

    SetChartStyle() {
      EastMoney.HQData.SetMinuteChartCoordinate();
      var blackStyle = HQChart.Chart.HQChartStyle.GetStyleConfig(
        HQChart.Chart.STYLE_TYPE_ID.BLACK_ID
      ); //读取黑色风格配置
      HQChart.Chart.JSChart.SetStyle(blackStyle);
      HQChart.Chart.JSChart.GetResource().FrameLogo.Text = null;
    },

    CreateMinuteChart() {
      if (this.Chart) return;

      var option = DefaultData.GetMinuteOption();
      option.Symbol = this.Symbol;
      option.NetworkFilter = (data, callback) => {
        this.NetworkFilter(data, callback);
      }; //网络请求回调函数

      var chart = HQChart.Chart.JSChart.Init(this.$refs.kline);
      chart.SetOption(option);
      this.Chart = chart;
    },

    CreateKLineChart() {
      if (this.KLineChart) return;

      var option = DefaultData.GetKLineOption();
      option.Symbol = this.Symbol;
      option.NetworkFilter = (data, callback) => {
        this.NetworkFilter(data, callback);
      }; //网络请求回调函数

      var chart = HQChart.Chart.JSChart.Init(this.$refs.kline2);
      chart.SetOption(option);
      this.KLineChart = chart;
    },

    ChangeSymbol(
      symbol //切换股票
    ) {
      var symbolUpper = symbol.toUpperCase();
      var isShowVolChart = EastMoney.HQData.IsShowVolChart(symbolUpper);
      var frame = this.Chart.JSChartContainer.Frame.SubFrame[1];
      if (isShowVolChart) {
        if (frame.Height <= 0) frame.Height = this.VolChartHeight;
      } else {
        if (frame.Height > 0) this.VolChartHeight = frame.Height;
        frame.Height = 0;
      }

      var period = this.KLineChart.JSChartContainer.Period;
      var isShowRightMenu = EastMoney.HQData.IsEnableRight(period, symbol); //是否显示复权菜单
      this.IsShowRightMenu = isShowRightMenu;

      this.Symbol = symbol;
      this.Chart.ChangeSymbol(this.Symbol);
      this.KLineChart.ChangeSymbol(this.Symbol);
    },

    OnClickMinuteDayMenu(
      index,
      item //分时图天数
    ) {
      this.MinuteDayIndex = index;
      this.Chart.ChangeDayCount(item.ID);
    },

    OnClickKLinePeriodMenu(
      index,
      item //K线周期
    ) {
      this.KLinePeriodIndex = index;
      this.KLineChart.ChangePeriod(item.ID);
    },

    OnClickKLineRightMenu(
      index,
      item //K线复权
    ) {
      this.KLineRightIndex = index;
      this.KLineChart.ChangeRight(item.ID);
    },

    ChangeMinuteIndex(
      item //切换分时图指标
    ) {
      if (this.Chart) this.Chart.ChangeIndex(item.WindowIndex, item.ID);
    },

    ChangeKLineIndex(
      item //切换K线图指标
    ) {
      if (this.KLineChart)
        this.KLineChart.ChangeIndex(item.WindowIndex, item.ID);
    },

    NetworkFilter(
      data,
      callback //第3方数据替换接口
    ) {
      EastMoney.HQData.Log("[HQChartDemo::NetworkFilter] data", data);
      switch (data.Name) {
        //分时图数据对接
        case "MinuteChartContainer::RequestMinuteData":
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
        case "MinuteChartContainer::RequestHistoryMinuteData":
          EastMoney.HQData.NetworkFilter(data, callback);
          break;

        case "KLineChartContainer::RequestHistoryData": //日线全量数据下载
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
        case "KLineChartContainer::RequestRealtimeData": //日线实时数据更新
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
        case "KLineChartContainer::RequestFlowCapitalData": //流通股本
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
        case "KLineChartContainer::ReqeustHistoryMinuteData": //分钟全量数据下载
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
        case "KLineChartContainer::RequestMinuteRealtimeData": //分钟增量数据更新
          EastMoney.HQData.NetworkFilter(data, callback);
          break;
      }
    },

    handleSelect(key, keyPath) {
      console.log(key, keyPath);
      this.ChangeSymbol(keyPath[1]);
    },

    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },

    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
};
</script>

<style lang="less" scoped>
@animation-duration: 0.3s;
.chart-box-warpper {
  width: 100%;
  // height: 560px;
  height: calc(100vh - 407px);
  display: flex;
  position: relative;
  overflow: hidden;

  .left,
  .right {
    position: absolute;
    top: 0;
  }

  .left {
    width: 240px;
    height: 100%;
    box-sizing: border-box;
    left: 0;
    // padding-top: 17px;
    overflow-x: auto;

    .el-menu {
      min-height: 100%;
      border-right: solid 1px #000;

      .el-submenu__title:hover {
        background-color: #363636 !important;
      }

      .el-menu-item:hover {
        background-color: #363636 !important;
      }
    }
  }

  .right {
    // left: 240px;
    // width: calc(100% - 240px);
    left: 0;
    width: 100%;
    height: 100%;
    @rightTabHeight: 40px;
    display: flex;
    flex-direction: column;
    .btnGroup {
      border: 1px solid #242424;
      color: #bcbfc4;
      display: -ms-flexbox;
      display: flex;
      // background: #191919;
      .btn {
        height: 40px;
        -ms-flex: 1;
        flex: 1;
        display: -ms-flexbox;
        display: flex;
        -ms-flex-align: center;
        align-items: center;
        -ms-flex-pack: center;
        justify-content: center;
        border-right: 1px solid #242424;
        cursor: pointer;
      }
    }
    .rightTab {
      height: @rightTabHeight;
      width: 100%;
      background: #191919;
      border-bottom: 1px solid #000;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      justify-content: center;
      // flex-direction: column;

      > .btn {
        cursor: pointer;
        width: 100%;
        text-align: center;
        color: #bcbfc4;

        &:first-child {
          padding-right: 60px;
          text-align: right;
        }

        &:last-child {
          padding-left: 60px;
          text-align: left;
        }

        &:hover,
        &.active {
          color: #ff6900;
        }
      }
    }

    .rightContent {
      height: 100%;
      width: 100%;

      .kline_periodWrap {
        display: flex;
        justify-content: space-between;
        background: #191919;

        .btnGroup:first-child {
          width: 50%;
        }

        .btnGroup:last-child {
          width: 30%;
        }
      }
    }

    .btnGroup {
      border: 1px solid #242424;
      color: #bcbfc4;
      display: flex;
      // background: #191919;

      .btn {
        height: 40px;
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        border-right: 1px solid #242424;
        cursor: pointer;

        &:last-child {
          border-right: none;
        }

        &:hover,
        &.active {
          color: #ff6900;
        }
      }
    }

    // .el-button-group{
    //   width: 100%;
    //   display: flex;

    //   .el-button{
    //     flex: 1;
    //   }
    // }

    #hqchart_minute {
      // background-color: rgb(0, 0, 0);
      height: 300px;
      position: relative;
    }

    #hqchart_kline {
      // background-color: rgb(0, 0, 0);
      height: 300px;
      position: relative;
    }

    .statementWrap {
      // background: #191919;
      padding: 10px;
      font-size: 12px;
      color: #de432d;
      line-height: 20px;
      text-align: center;
    }
  }
}
</style>
<style lang="less">
.klineframe-toolbar {
  display: none !important;
}
</style>
