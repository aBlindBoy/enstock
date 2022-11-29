<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="首页显示状态">
          <el-select clearable v-model="form.homeShow" placeholder="显示状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="显示" value="1"></el-option>
            <el-option label="不显示" value="0"></el-option>
          </el-select>
        </el-form-item>
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
        <el-form-item label="指数名">
          <el-input v-model="form.name" placeholder="指数名"></el-input>
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
              <i class="iconfont icon-add"></i>添加指数
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
                <el-form-item label="指数名字">
                  <p>
                    {{props.row.indexName}}
                    <span style="font-size:12px;color:#959595;">{{props.row.indexCode}}</span>
                  </p>
                </el-form-item>

                <el-form-item label="买入范围">
                  <span>{{ props.row.minNum }}手~{{ props.row.maxNum }}手</span>
                </el-form-item>
                <el-form-item label="手续费/手">
                  <span>{{ props.row.transFee }}</span>
                </el-form-item>
                <el-form-item label="每手保证金">
                  <span>{{ props.row.depositAmt }}</span>
                </el-form-item>
                <el-form-item label="点浮动价">
                  <span>{{ props.row.eachPoint }}</span>
                </el-form-item>

                <el-form-item label="添加时间">
                  <span>{{props.row.addTime | timeFormat}}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="stockName"
            label="指数名字">
            <template slot-scope="scope">
              <p>
                <span v-if="scope.row.transState == 1" class="show red">可交易</span>
                <span v-if="scope.row.homeShow == 1" class="show">首</span>
                <span v-if="scope.row.listShow == 1" class="show">列</span>
              </p>
              <p class="big-font">
                {{scope.row.indexName}}
                <span class="pro-code">{{scope.row.indexCode}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="currentPoint"
            label="当前点数">
            <template slot-scope="scope">
              <p :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">
                {{Number(scope.row.currentPoint).toFixed(2)}}</p>
            </template>
          </el-table-column>
          <el-table-column
            prop="floatPoint"
            label="涨跌点数">
            <template slot-scope="scope">
              <p :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">
                {{Number(scope.row.floatPoint).toFixed(2)}}</p>
            </template>
          </el-table-column>
          <el-table-column
            prop="floatRate"
            label="涨跌幅">
            <template slot-scope="scope">
              <p :class="scope.row.floatRate<0?'green':scope.row.floatRate == 0?'':'red'">{{scope.row.floatRate}}%</p>
            </template>
          </el-table-column>
          <el-table-column
            prop="transState"
            label="交易状态">
            <template slot-scope="scope">
              <el-tag :type="scope.row.transState==1?'':'warning'">{{ scope.row.transState==1?'可交易':'不可交易' }}</el-tag>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="transFee"
            label="双边手续费/手">
          </el-table-column>
          <el-table-column
            prop="depositAmt"
            label="每手保证金">
          </el-table-column> -->
          <!-- <el-table-column
            prop="eachPoint"
            label="点浮动价">
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
              <el-button type="text" title="修改指数" size="small" @click="tounShowStock(scope.row)"><i
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
import editDialog from './indexedit-dialog'

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
        indexCode: this.form.code,
        indexName: this.form.name,
        homeShow: this.form.homeShow,
        listShow: this.form.listShow,
        transState: this.form.transState,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.indexList(opts)
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
        indexCode: this.form.code,
        indexName: this.form.name,
        homeShow: this.form.homeShow,
        listShow: this.form.listShow,
        transState: this.form.transState,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      let data = await api.indexList(opts)
      this.refresh = false
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.changeTextClass = {}
    },
    addAccount () {
      // 添加指数
      this.$router.push('/product/addindex')
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
