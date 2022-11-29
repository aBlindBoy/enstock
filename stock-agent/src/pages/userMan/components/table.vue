<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="账号类型">
          <el-select clearable v-model="form.type" placeholder="账号类型">
            <el-option label="正式" value="0"></el-option>
            <el-option label="模拟" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户手机">
          <el-input v-model="form.phone" placeholder="用户手机"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <el-alert
        v-if="list.list.length>0 && list.list[0].now_price == 0"
        style="margin-bottom:10px"
        title="集合竞价中，'现价'、'浮动盈亏'、'总盈亏'暂不能查看"
        type="warning"
        close-text="知道了">
      </el-alert>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加账户
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          show-summary
          :summary-method="getSummaries"
          :row-class-name="tableRowClassName"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <!-- <el-form-item label="总资金">
                  <span>{{(Number(props.row.userAmt) + Number(props.row.userIndexAmt)).toFixed(2)}}</span>
                </el-form-item> -->
                <el-form-item label="融资总资金">
                  <span>{{ props.row.userAmt}}</span>
                </el-form-item>
                <el-form-item label="融资可用">
                  <span>{{ props.row.enableAmt}}</span>
                </el-form-item>
                <el-form-item label="融资冻结保证金">
                  <el-tag>{{ props.row.allFreezAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="融资总盈亏">
                  <div class="bounceIn tab-number">
                    <p :class="changeTextClass[props.$index] == true?'heartBeat':''">
                      <span :class="props.row.allProfitAndLose<0?'green':props.row.allProfitAndLose==0?'':'red'">{{props.row.allProfitAndLose}}</span>
                    </p>
                  </div>
                </el-form-item>
                <el-form-item label="融资平仓线">
                  <el-tag type="warning">{{props.row.forceLine}}</el-tag>
                </el-form-item>

                <el-form-item label="指数总资金">
                  <span>{{ props.row.userIndexAmt}}</span>
                </el-form-item>
                <el-form-item label="指数可用">
                  <span>{{ props.row.enableIndexAmt}}</span>
                </el-form-item>
                <el-form-item label="指数冻结保证金">
                  <el-tag>{{ props.row.allIndexFreezAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="指数总盈亏">
                  <div class="bounceIn tab-number">
                    <p :class="changeTextClass[props.$index] == true?'heartBeat':''">
                      <span
                        :class="props.row.allIndexProfitAndLose<0?'green':props.row.allIndexProfitAndLose==0?'':'red'">{{props.row.allIndexProfitAndLose}}</span>
                    </p>
                  </div>
                </el-form-item>
                <el-form-item label="指数平仓线">
                  <el-tag type="warning">{{ props.row.indexForceLine}}</el-tag>
                </el-form-item>

                <el-form-item label="期货总资金">
                  <span>{{ props.row.userFuturesAmt}}</span>
                </el-form-item>
                <el-form-item label="期货可用">
                  <span>{{ props.row.enableFuturesAmt}}</span>
                </el-form-item>
                <el-form-item label="期货冻结保证金">
                  <el-tag>{{ props.row.allFuturesFreezAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="期货总盈亏">
                  <div class="bounceIn tab-number">
                    <p :class="changeTextClass[props.$index] == true?'heartBeat':''">
                      <span
                        :class="props.row.allFuturesProfitAndLose<0?'green':props.row.allFuturesProfitAndLose==0?'':'red'">{{props.row.allFuturesProfitAndLose}}</span>
                    </p>
                  </div>
                </el-form-item>
                <el-form-item label="期货平仓线">
                  <el-tag type="warning">{{ props.row.futuresForceLine}}</el-tag>
                </el-form-item>

              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="accountType"
            width="80px"
            label="账号类型">
            <template slot-scope="scope">
              <el-tag :type="scope.row.accountType == 1?'info':'success'">{{scope.row.accountType == 1?'模拟':'实盘'}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="agentName"
            label="所属代理">
          </el-table-column>
          <el-table-column
            prop="realName"
            label="真实姓名">
            <template slot-scope="scope">
              <p>
                {{scope.row.realName}}
                <!-- <span style="font-size:12px;color:#959595;" v-if="scope.row.accountType == 1">(模拟)</span> -->
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="phone"
            label="手机号">
          </el-table-column>
          <el-table-column
            prop="userAmt"
            label="总资金">
            <template slot-scope="scope">
              <p class="proColor bounceIn">
                {{(scope.row.userAmt + scope.row.userIndexAmt + scope.row.userFuturesAmt).toFixed(2)}}
              </p>
            </template>
          </el-table-column>
          <!-- <el-table-column
            width="110px"
            prop="userAmt"
            label="融资总资金">
            <template slot-scope="scope">
              <p class="proColor bounceIn">
                {{scope.row.userAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="enableAmt"
            label="融资可用资金">
            <template slot-scope="scope">
              <p class="proColor">
                {{scope.row.enableAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="130px"
            prop="allFreezAmt"
            label="融资冻结保证金">
            <template slot-scope="scope">
              <p class="proColor">
                {{scope.row.allFreezAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="allProfitAndLose"
            label="融资总盈亏">
            <template slot-scope="scope">
              <div class="bounceIn tab-number">
                <p :class="changeTextClass[scope.$index] == true?'heartBeat':''">
                  <span :class="scope.row.allProfitAndLose<0?'green':scope.row.allProfitAndLose==0?'':'red'">{{scope.row.allProfitAndLose}}</span>
                </p>
              </div>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            prop="forceLine"
            width="100px"
            label="融资平仓线">
            <template slot-scope="scope">
              <el-tag type="warning">{{scope.row.forceLine}}</el-tag>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="userIndexAmt"
            label="指数总资金">
            <template slot-scope="scope">
              <p class="proColor bounceIn">
                {{scope.row.userIndexAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="enableAmt"
            label="指数可用资金">
            <template slot-scope="scope">
              <p class="proColor">
                {{scope.row.enableIndexAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="130px"
            prop="allIndexFreezAmt"
            label="指数冻结保证金">
            <template slot-scope="scope">
              <p class="proColor">
                {{scope.row.allIndexFreezAmt}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="110px"
            prop="allIndexProfitAndLose"
            label="指数总盈亏">
            <template slot-scope="scope">
              <div class="bounceIn tab-number">
                <p :class="changeTextClass[scope.$index] == true?'heartBeat':''">
                  <span :class="scope.row.allIndexProfitAndLose<0?'green':scope.row.allIndexProfitAndLose==0?'':'red'">{{scope.row.allIndexProfitAndLose}}</span>
                </p>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="indexForceLine"
            width="100px"
            label="指数平仓线">
            <template slot-scope="scope">
              <el-tag type="warning">{{scope.row.indexForceLine}}</el-tag>
            </template>
          </el-table-column> -->
          <el-table-column
            prop="isLock"
            label="交易状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.isLock == 0" class="hide-td" title="正常">
                  <!-- <i class="iconfont icon-zhengchang1"></i> -->
                  正常
                </a>
                <a v-if="scope.row.isLock == 1" class="hide-td" title="不可交易">
                  <!-- <i v-if="scope.row.isLock == 1" class="iconfont icon-suoding"></i> -->
                  不可交易
                </a>
              </div>
              <!-- {{scope.row.isLock == 0?'未锁定':'锁定'}} -->
            </template>
          </el-table-column>
          <el-table-column
            prop="isLogin"
            label="登录状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.isLogin == 0" class="hide-td" title="正常">
                  正常
                </a>
                <a v-if="scope.row.isLogin == 1" class="hide-td" title="不可登录">
                  不可登录
                </a>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="isLock"
            label="查看详情">
            <template slot-scope="scope">
              <el-button type="text" title="查看详情" size="small" @click="toDetail(scope.row)"><i
                class="iconfont icon-chakan"></i></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-box">
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40,50]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
      </div>
    </el-card>
    <addDialog :getDate='getList' ref="addSimulatedAccountDialog"></addDialog>
    <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import DetailDialog from './detail-dialog'

export default {
  components: {
    addDialog,
    DetailDialog
  },
  props: {},
  data () {
    return {
      form: {
        realName: '',
        phone: '',
        type: '',
        agentId: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      agentList: [
        {
          'id': -1,
          'agentName': '下级代理',
          'agentRealName': '下级代理',
          'agentPhone': '18888888888',
          'agentCode': '8888'
        }
      ],
      detail: {},
      loading: false,
      timer: null,
      refresh: false,
      changeTextClass: {} // 表格中的数据变化
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
    this.getList()
    this.getAgentList()
  },
  methods: {
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getList()
    },
    async refreshList () {
      if (this.refresh || this.loading) {
        return
      }
      this.refresh = true
      // 获取表格数据
      let opts = {
        realName: this.form.realName,
        phone: this.form.phone,
        agentId: this.form.agentId,
        accountType: this.form.type,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.changeTextClass = {}
      let data = await api.getUserManList(opts)
      this.refresh = false
      if (data.status === 0) {
        //console.log(data.data)
        data.data.list.forEach((element, i) => {
          // 判断刷新需要刷新的值
          this.changeTextClass[i] = ''
          if (this.list.list[i].profitAndLose !== data.data.list[i].profitAndLose) {
            this.changeTextClass[i] = true // 设置对应的动画过滤
            this.list.list[i].profitAndLose = data.data.list[i].profitAndLose
            this.list.list[i].userAmt = data.data.list[i].userAmt
          }
        })
      } else {
        this.$message.error(data.msg)
      }
    },
    onSubmit () {
      // 查询表格
      this.getList()
    },
    async getAgentList () {
      // 获取下级代理数据
      let data = await api.getSecondAgent()
      if (data.status === 0) {
        if(data.data.list.length > 0){
          this.agentList = data.data.list
        }
        
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      // 获取表格数据
      let opts = {
        realName: this.form.realName,
        phone: this.form.phone,
        agentId: this.form.agentId,
        accountType: this.form.type,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.getUserManList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    addAccount () {
      // 添加模拟账户
      this.$refs.addSimulatedAccountDialog.dialogVisible = true
    },
    toDetail (row) {
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
    },
    tableRowClassName ({ row, rowIndex }) {
      // 设置表格行高亮
      if (row.allProfitAndLose < 0 && ((-row.allProfitAndLose) / row.forceLine) >= 0.8) {
        return 'warning-row'
      }
      return ''
    },
    getSummaries (param) {
      // 底部计算
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        // 第一行 不统计
        if (index === 0) {
          sums[index] = '统计'
          return
        }
        if (column.property === 'userAmt' || column.property === 'enableAmt' || column.property === 'allFreezAmt' || column.property === 'allProfitAndLose') {
          // 需要计算列 ==>    userAmt enableAmt  allFreezAmt allProfitAndLose forceLine
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                let num = prev + curr
                return num
              } else {
                return prev
              }
            }, 0)
            sums[index] = sums[index].toFixed(2)
          } else {
            sums[index] = 'N/A'
          }
        }
      })
      // let newSums = []
      // sums.forEach((item)=>{
      //   console.log(item)
      //    if (!isNaN(item)){
      //     return newSums.push(item.toFixed(2))
      //    }else{
      //      return newSums.push(item)
      //    }
      // })
      return sums
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .table .demo-table-expand {
    /deep/ label {
      display: block;
      text-align: center;
      line-height: 20px;
      width: 100%;
    }

    /deep/ .el-form-item {
      width: 20%;
      text-align: center;
    }
  }
</style>
