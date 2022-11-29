<template>
  <div class="con-box account futuresholdposition">
    <div class="header-top">
      <div class="account-all">
        <p class="title">
          Total assets
        </p>
        <div class="pull-right" style="margin-top:0">
          <el-button type="primary" @click="toRechange()">Recharge</el-button>
          <el-button type="success" @click="toWithdraw()">Withdrawal</el-button>
        </div>

        <p class="number" style="background-color:rgba(250,250,250,0)">
          
          <span>{{(Number($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)).toFixed(2)}}</span>
        </p>
      </div>
      <el-row class="box-account" :gutter="20">
        <el-row>
          <el-col class="progress-box" :span="24">
            <el-progress :text-inside="true" :stroke-width="10"
                         :percentage="$store.state.userInfo.userAmt/($store.state.userInfo.userAmt+$store.state.userInfo.userIndexAmt) * 100 > 100?100:$store.state.userInfo.userAmt/($store.state.userInfo.userAmt+$store.state.userInfo.userIndexAmt) * 100 > 100?$store.state.userInfo.userAmt/($store.state.userInfo.userAmt+$store.state.userInfo.userIndexAmt) * 100 > 100?100:$store.state.userInfo.userAmt/($store.state.userInfo.userAmt+$store.state.userInfo.userIndexAmt) * 100:100"></el-progress>
          </el-col>
        </el-row>
        <el-collapse v-model="accountActiveNames">
          <el-collapse-item title="Account assets" name="1">
            <el-col :span="24">
              <el-row class="Assets-box" :gutter="20">
                <el-col :span="6">
                  <div class="box box1">
                    <i class="color3 iconfont icon-zijin1"></i>
                    <p class="title">Allotment total assets</p>
                    <p :class="refresh?'number heartBeat':'number'">
                      <!-- $store.state.userInfo.userAmt -->
                      {{$store.state.hide?'****':(Number($store.state.userInfo.enableAmt + $store.state.userInfo.allFreezAmt)).toFixed(2)}}
                    </p>
                  </div>
                </el-col>
                <!-- <el-col :span="6">
                  <div class="box box1">
                    <i class="color2 iconfont icon-rongzi2"></i>
                    <p class="title">滬深可用資金:</p>
                    <p class="number">{{$store.state.hide?'****':$store.state.userInfo.enableAmt}}</p>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="box box1">
                    <i class="color1 iconfont icon-dongjiezijin"></i>
                    <p class="title">滬深凍結Margin</p>
                    <p>
                      <span class="number">{{$store.state.hide?'****':$store.state.userInfo.allFreezAmt}}</span>
                    </p>
                  </div>
                </el-col> -->
                
                <el-col :span="6">
                  <div class="box box1">
                    <i class="color4 iconfont icon-yingkuixuanzhong"></i>
                    <p class="title">Trading amount</p>
                    <p :class="refresh?'heartBeat':''">
                    <span
                      :class="$store.state.userInfo.allProfitAndLose>0?'red number':$store.state.userInfo.allProfitAndLose<0?'green number':'number'">
                        {{$store.state.hide?'****':$store.state.userInfo.tradingAmount}}
                    </span>
                    </p>
                  </div>
                </el-col>
              </el-row>
             
            </el-col>
          </el-collapse-item>
       
        </el-collapse>
      </el-row>
      <el-row v-if="false">
        <el-col :span="12">
          <div class="box-btn text-center">
            <el-button type="primary" @click="toRechange()">Recharge</el-button>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="box-btn text-center">
            <el-button type="success" @click="toWithdraw()">Withdrawal</el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-tabs v-model="activeName">
      <el-tab-pane label="My allocation" name="first">
        <div class="user-info">
          <el-table
            :data="list.list"
            style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="Order number">
                    <span>{{scope.row.orderNumber}}</span>
                  </el-form-item>
                  <el-form-item label="Sub account">
                    <span>{{scope.row.subaccountNumber}}</span>
                  </el-form-item>
                  <el-form-item label="Total trading capital">
                    <span>{{ scope.row.totalTradingAmount}}</span>
                  </el-form-item>
                  <el-form-item label="Warning line">
                    <span>{{ scope.row.lineWarning}}</span>
                  </el-form-item>
                  <el-form-item label="Closing Line">
                    <span>{{scope.row.lineUnwind}}</span>
                  </el-form-item>
                  <el-form-item label="Starting time">
                    <span>{{scope.row.beginTime | timeFormat}}</span>
                  </el-form-item>
                  <el-form-item label="Stop time">
                    <span>{{scope.row.endTime | timeFormat}}</span>
                  </el-form-item>
                  
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              prop="totalTradingAmount"
              label="Total trading capital">
            </el-table-column>
            <el-table-column
              prop="fundsAmount"
              width="80px"
              label="Allocating funds">
              <template slot-scope="scope">
                <span>{{scope.row.fundsAmount}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="margin"
              width="80px"
              label="Margin">
            </el-table-column>
            <el-table-column
              prop="manageFee"
              width="90px"
              label="Funding management fee">
            </el-table-column>
            <el-table-column
              prop="totalTradingAmount"
              label="Amount available in stock">
            </el-table-column>
            <el-table-column
              prop="orderTotalPrice"
              width="80px"
              label="Stock market value">
            </el-table-column>
            <el-table-column
              prop="allProfitAndLose"
              width="80px"
              label="Stock profit and loss">
            </el-table-column>
            <el-table-column
              prop="status"
              width="80px"
              label="State">
              <template slot-scope="scope">
                <p class="bounceIn">
                  <span v-if="scope.row.status==0" class="red">Pending review</span>
                  <span v-if="scope.row.status==1" class="green">In operation</span>
                  <span v-if="scope.row.status==2" class="red">Did not pass</span>
                  <span v-if="scope.row.status==3" class="red">Be expired</span>
                  <span v-if="scope.row.status==4" class="green">Over</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              prop="isLock"
              width="80px"
              label="Allotment">
              <template slot-scope="scope">
                <el-button type="success" plain size="small" @click="withFunding(scope.row)">Allotment</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>

</template>

<script>
  import * as api from '../../../axios/api'
  import ChartBox from './chart'
  // import DetailTable from './table/detail'
  // import DetailTable from './table/detail'

  export default {
    components: {
      ChartBox
      // ,DetailTable
    },
    props: {
      list: {
        type: Object,
        default () {
          return {
            list: []
          }
        }
      },
      getData: {
        type: Function,
        default: function () {}
      },
      handleOptions: {
        type: Function,
        default: function () {}
      },
      refresh: {
        type: Boolean,
        default: function () {}
      }
    },
    data () {
      return {
        loading: false,
        activeName: 'first',
        settingInfo: {}, // 设置信息
        indexSettingInfo: {},
        futuresSettingInfo: {},
        bankInfo: {},
        accountActiveNames: ['1'],
        pageNum: 1,
        pageSize: 8,
      }
    },
    watch: {},
    computed: {
      progressNnum () {
        if (this.$store.state.userInfo.userAmt !== 0) {
          return (this.$store.state.userInfo.enableAmt / this.$store.state.userInfo.userAmt) * 100
        } else {
          return 0
        }
      }
    },
    created () {
      this.getSettingInfo()
      this.getlist()
    },
    methods: {
      async getSettingInfo () {
        // 网站设置信息
        let data = await api.getSetting()
        if (data.status === 0) {
          // 成功
          this.settingInfo = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      handleSizeChange (size) {
        this.handleOptions({ pageSize: size })
        this.getData()
      },
      handleCurrentChange (page) {
        this.handleOptions({ pageNum: page })
        this.getData()
      },
      async getlist () {
        // 获取持仓列表
        let opt = {
          userId: 0, 
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        console.log(opt)
        let data = await api.getUserApplyList(opt)
        if (data.status === 0) {
          this.list = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      toRechange () {
        // Recharge
        this.$router.push('/recharge')
      },
      toWithdraw () {
        // 提现
        this.$router.push('/withdraw')
      },
      withFunding (val) {
        this.$router.push({
            path: '/funding?id='+ val.id
        })
      }
    }
  }
</script>
<style lang="less" scoped>
.number{
  background-color: rgba(250,250,250,0) !important;
}
  .table {
    min-height: 500px;

    .code {
      color: #6d718b;
      font-size: 12px;
    }

    .more-btn {
      text-align: center;
      color: #8f92a3;
    }

    /deep/ th.el-table_1_column_1 {
      padding-left: 50px;
    }
  }

  .con-box {
    // color: #fff;
    padding: 0 20px;

    .box-account {
      padding: 20px 10px 0;

      .name {
        // font-size: 16px;
      }

      .account {
        // font-size: 46px;
        font-weight: 400;
      }

      .el-col {
        padding: 10px;
      }

      .box {
        padding: 10px;
        padding-left: 50px;
        border-bottom: 1px solid rgba(230, 230, 230, 0.6);

        .title {
          // font-size: 16px;
          color: #333;
          margin-bottom: 10px;
        }
      }

      .box1 {
        border-bottom: none;
      }

      .box-btn {
        padding: 12px;
      }

      .number {
        // font-size: 18px;
      }
    }
  }

  .user-info {
    padding: 20px 0;

    .el-row {
      margin-bottom: 15px;
      line-height: 30px;
      height: 30px;
    }

    .name {
      width: 96px;
      text-align: right;
      display: inline-block;
      color: #6e6e6e;
    }

    .info {
      font-size: 16px;
    }

    .btn-statue {
      margin-left: 100px;
      margin-bottom: 20px;
    }
  }

  .progress-box {
    position: relative;
    margin-bottom: 20px;

    .item {
      position: absolute;
      width: 46%;
      height: 30px;
      top: 38%;
      left: 27%;
      background: #fff;
    }

    .progress-title {
      font-size: 16px;
      padding: 0 12px;
      margin-top: 10px;
    }

    /deep/ .el-progress {
      .el-progress-bar__outer {
        background-color: #FF9800;
      }

      .el-progress-bar__inner {
        background-color: #FF5722;
      }

      .el-progress-bar__innerText {
        color: #FF5722;
        font-size: 0;
      }
    }
  }

  .el-tabs {
    margin-top: 30px;
  }

  .force-line {
    // margin-top: 30px;
    // background-color: #fdf6ec;
    color: #e6a23c;

    p {
      padding: 8px 16px;
    }

    .number {
      font-size: 18px;
    }
  }

  .box-btn {
    margin-top: 20px;

    .el-button {
      padding-left: 50px;
      padding-right: 50px;
    }
  }

  .Assets-box {
    // border: 1px solid #f1f1f1;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, .1);
    padding: 10px 0;
    margin: 0 !important;

    .box {
      position: relative;

      .iconfont {
        // position: absolute;
        // margin-right: 5px;
        font-size: 30px;
        // top: 17px;

        &.color1 {
          color: #2f97fc;
        }

        &.color2 {
          color: #17b780;
        }

        &.color3 {
          color: #ff7602;
        }

        &.color3 {
          color: #fd4256;
        }

        &.color4 {
          color: #fda822;
        }
      }
    }
  }

  .account-all {
    margin-top: 20px;

    .title {
      font-size: 16px;
      line-height: 30px;

      span {
        font-size: 12px;
        color: #777;
      }
    }

    .number {
      font-size: 22px;
      margin-top: 4px;
      text-shadow: 1px 2px 2px rgba(24, 24, 24, 0.3);
    }
  }
</style>
