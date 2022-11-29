<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <!-- <el-form-item  label="首页显示状态">
           <el-select clearable v-model="form.homeShow" placeholder="显示状态">
             <el-option label="全部" value=""></el-option>
             <el-option label="显示" value="1"></el-option>
             <el-option label="不显示" value="0"></el-option>
           </el-select>
       </el-form-item> -->
        <el-form-item label="列表显示状态">
          <el-select clearable v-model="form.listShow" placeholder="列表显示状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="显示" value="1"></el-option>
            <el-option label="不显示" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态">
          <el-select clearable v-model="form.transState" placeholder="交易状态">
            <el-option label="可交易" value="1"></el-option>
            <el-option label="不可交易" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="代码">
          <el-input v-model="form.code" placeholder="代码"></el-input>
        </el-form-item>
        <el-form-item label="期货名">
          <el-input v-model="form.name" placeholder="期货名"></el-input>
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
              <i class="iconfont icon-add"></i>添加期货
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="期货名字">
                  <span>{{ props.row.futuresName }}</span>
                  <span>{{ props.row.futuresCode }}</span>
                </el-form-item>
                <el-form-item label="每手保证金">
                  <span>{{ props.row.depositAmt }}{{props.row.coinCode}}</span>
                </el-form-item>
                <el-form-item label="每标准手">
                  <span>{{ props.row.futuresStandard}}{{props.row.futuresUnit}}</span>
                </el-form-item>
                <el-form-item label="手续费/手">
                  <span>{{ props.row.transFee }}{{props.row.coinCode}}</span>
                </el-form-item>
                <el-form-item label="汇率">
                  <span>1{{props.row.coinCode}} ≈ {{props.row.coinRate}} USD</span>
                </el-form-item>
                <el-form-item label="交易时间">
                  <span>上午：{{ props.row.transAmBegin }}点~{{ props.row.transAmEnd }}点</span>
                  <span>下午：{{ props.row.transPmBegin }}点~{{ props.row.transPmEnd }}点</span>
                </el-form-item>
                <el-form-item label="买入范围">
                  <span>{{ props.row.minNum }}手~{{ props.row.maxNum }}手</span>
                </el-form-item>
                <el-form-item label="添加时间">
                  <span>{{props.row.addTime | timeFormat}}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="futuresName"
            label="期货名字">
            <template slot-scope="scope">
              <p>
                <span v-if="scope.row.transState == 1" class="show red">可交易</span>
                <span v-if="scope.row.homeShow == 1" class="show">首</span>
                <span v-if="scope.row.listShow == 1" class="show">列</span>
              </p>
              <p class="big-font">
                {{scope.row.futuresName}}
                <span class="pro-code">{{scope.row.futuresCode}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="currentPoint"
            label="最新价">
            <template slot-scope="scope">
              <p :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">
                {{Number(scope.row.now_price).toFixed(2)}}
                <span class="prompt">{{scope.row.coinCode}}</span>
              </p>
              <p class="prompt">
                ≈ {{(scope.row.coinRate * scope.row.now_price).toFixed(2)}}
                <span>USD</span>
              </p>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="last_close"
            label="昨收">
            <template slot-scope="scope">
              <p :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">{{Number(scope.row.last_close).toFixed(2)}}</p>
            </template>
          </el-table-column> -->
          <el-table-column
            prop="floatRate"
            label="涨跌幅">
            <template slot-scope="scope">
              <p v-if="scope.row.last_close>0" :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">
                {{((scope.row.now_price - scope.row.last_close)/scope.row.last_close*100).toFixed(2)}}%</p>
              <p v-else>0</p>
            </template>
          </el-table-column>

          <!-- <el-table-column
            prop="transFee"
            label="双边手续费/手">
          </el-table-column> -->
          <el-table-column
            prop="depositAmt"
            label="每手保证金">
            <template slot-scope="scope">
              {{scope.row.depositAmt}}{{scope.row.coinCode}}
            </template>
          </el-table-column>
          <el-table-column
            prop="futuresStandard"
            label="每标准手">
            <template slot-scope="scope">
              {{scope.row.futuresStandard}}{{scope.row.futuresUnit}}
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="time"
            label="交易时间">
            <template slot-scope="scope">
              {{scope.row.time}}点-{{scope.row.time}}点
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            prop="minNum"
            label="最小买入手数">
          </el-table-column>
          <el-table-column
            prop="maxNum"
            label="最大买入手数">
          </el-table-column> -->
          <!-- <el-table-column
          width="180px"
            prop="regTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column> -->
          <el-table-column
            fixed="right"
            width="60px"
            prop="isLock"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" title="修改期货" size="small" @click="tounShowStock(scope.row)"><i
                class="iconfont icon-xiugai"></i></el-button>
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
    <!-- <addDialog :getDate='getList' ref="addSimulatedAccountDialog"></addDialog> -->
    <editDialog :info='detail' :getDate='getList' ref="editIndexDialog"></editDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
// import addDialog from './indexadd-dialog'
import editDialog from './futuresedit-dialog'

export default {
  components: {
    // addDialog,
    editDialog
  },
  props: {},
  data () {
    return {
      form: {
        code: '',
        name: '',
        homeShow: '',
        listShow: '',
        transState: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      detail: {},
      loading: false,
      refresh: false,
      changeTextClass: {}
    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.getList()
  },
  created () {
    this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
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
    onSubmit () {
      this.form.pageNum = 1
      // 查询表格
      this.getList()
    },
    async getList () {
      // 获取表格数据
      let opts = {
        futuresCode: this.form.code,
        futuresName: this.form.name,
        homeShow: this.form.homeShow,
        listShow: this.form.listShow,
        transState: this.form.transState,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.futuresList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    async setInterval () {
      if (this.refresh || this.loading) {
        return
      }
      this.refresh = true
      let opts = {
        futuresCode: this.form.code,
        futuresName: this.form.name,
        homeShow: this.form.homeShow,
        listShow: this.form.listShow,
        transState: this.form.transState,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      let data = await api.futuresList(opts)
      this.refresh = false
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.changeTextClass = {}
    },
    addAccount () {
      // 添加期货
      this.$router.push('/product/addfutures')
      // this.$refs.addSimulatedAccountDialog.dialogVisible = true
    },
    async tounShowStock (val) {
      this.detail = val
      this.$refs.editIndexDialog.dialogVisible = true
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .table {
    .prompt {
      color: #666;
      font-size: 12px;
    }

    .demo-table-expand {
      font-size: 0;
    }

    .demo-table-expand label {
      width: 90px;
      color: #999;
    }

    .demo-table-expand .el-form-item {
      margin-right: 0;
      margin-bottom: 0;
      width: 50%;
    }

    .show {
      border: 1px solid #2196F3;
      margin-right: 8px;
      padding: 0 2px;
      font-size: 12px;
      border-radius: 4px;
      color: #2196F3;

      &.red {
        border-color: #ff0000;
        color: #ff0000;
      }
    }
  }
</style>
