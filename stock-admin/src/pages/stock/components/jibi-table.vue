<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="基币名">
          <el-input v-model="form.name" placeholder="基币名"></el-input>
        </el-form-item>
        <el-form-item label="基币代码">
          <el-input v-model="form.code" placeholder="基币代码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加基币
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="coinName"
            width="139px"
            label="基币名字/代码">
            <template slot-scope="scope">
              <p class="big-font">
                {{scope.row.coinName}}
                <span class="pro-code">{{scope.row.coinCode}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="coinGid"
            label="基币gid">
          </el-table-column>
          <el-table-column
            prop="nowPrice"
            label="当前汇率">
            <template slot-scope="scope">
              <span class="red">{{scope.row.nowPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="defaultRate"
            label="默认汇率">
            <template slot-scope="scope">
              <span class="yellow">{{scope.row.defaultRate }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="dynamicRate"
            label="汇率使用">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.dynamicRate == 1" type="info">{{scope.row.dynamicRate == 1?'默认':'动态'}}</el-tag>
              <el-tag v-else type="success">{{scope.row.dynamicRate == 1?'默认':'动态'}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            width="100px"
            prop="isUse"
            label="是否可用">
            <template slot-scope="scope">
              <i v-if="scope.row.isUse == 1" class="iconfont icon-zhengchang"></i>
              <i v-else class="iconfont icon-failure"></i>
              <span>{{scope.row.isUse == 1?'可用':'不可用'}}</span>
            </template>
          </el-table-column>
          <el-table-column
            width="170px"
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">
              {{scope.row.addTime | timeFormat}}
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="60px"
            prop="isLock"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" title="修改基币" size="small" @click="tounShowStock(scope.row)"><i
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
    <addDialog :getDate='getList' ref="addSimulatedAccountDialog"></addDialog>
    <editDialog :info='detail' :getDate='getList' ref="editDialog"></editDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './coinadd-dialog'
import editDialog from './coinedit-dialog'

export default {
  components: {
    addDialog,
    editDialog
  },
  props: {},
  data () {
    return {
      form: {
        name: '',
        code: '',
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
        coinCode: this.form.code,
        coinName: this.form.name,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.coinList(opts)
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
        coinCode: this.form.code,
        coinName: this.form.name,
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
      // 添加基币
      // this.$router.push('/addfutures')
      this.$refs.addSimulatedAccountDialog.dialogVisible = true
    },
    async tounShowStock (val) {
      this.detail = val
      this.$refs.editDialog.dialogVisible = true
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .table {
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
