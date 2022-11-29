<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="类型">
          <el-select clearable filterable v-model="form.typeName" placeholder="点差类型">
            <el-option label="涨跌幅" value="涨跌幅"></el-option>
            <el-option label="成交额" value="成交额"></el-option>
            <el-option label="开头收取" value="开头收取"></el-option>
            <el-option label="低于收取" value="低于收取"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="primary" size="small" @click="addAgent" plain>添加点差</el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="agentName"
            label="id"
            width="120">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.id}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="typeName"
            label="类型名称"
            width="120">
          </el-table-column>
          <el-table-column
            prop="unit"
            label="单位"
            width="120">
          </el-table-column>
          <el-table-column
            prop="startInterval"
            width="120"
            label="开始区间">
          </el-table-column>
          <el-table-column
            width="120"
            prop="endInterval"
            label="值">
            <template>
              <p class="bounceIn">
                &le; X &lt; 
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="120"
            prop="endInterval"
            label="结束区间">
          </el-table-column>
          <el-table-column
            width="150"
            prop="spreadRate"
            label="点差费率">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.spreadRate>0?'green':'red'">{{scope.row.spreadRate}}</span>
            </p>
          </template>
          </el-table-column>
          <el-table-column
            width="180"
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="100"
            label="修改点差">
            <template slot-scope="scope">
              <el-button type="primary" title="修改点差" size="small" @click="editAgent(scope.row)">修改点差</el-button>
            </template>
          </el-table-column>

        </el-table>
        <div class="page-box">
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40]"
            :page-size="list.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
      </div>
    </el-card>
    <addSpreadDialog :agentList='list.list' :getDate='getList' ref="addSpreadDialog"></addSpreadDialog>
    <editSpreadDialog :agentList='list.list' :info='detail' :getDate='getList' ref="editSpreadDialog"></editSpreadDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addSpreadDialog from './add-spread'
import editSpreadDialog from './edit-spread'

export default {
  components: {
    addSpreadDialog,
    editSpreadDialog
  },
  props: {},
  data () {
    return {
      form: {
        typeName: '',
        pageNum: 1,
        pageSize: 100
      },
      list: {
        list: []
      },
      loading: false, // 表格加载
      detail: {}, // 用户信息
      agentList: []
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getList()
    this.getSiteSpreadList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getList()
    },
    async getSiteSpreadList () {
      // 获取下级代理数据
      let opts = {
        pageNum: 1,
        pageSize: 100,
        typeName: ''
      }
      let data = await api.getSiteSpreadList(opts)
      if (data.status === 0) {
        this.agentList = data.data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        typeName: this.form.typeName
      }
      let data = await api.getSiteSpreadList(opts)
      if (data.status === 0) {
        this.list = data.data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    addAgent () {
      // 添加
      this.$refs.addSpreadDialog.dialogVisible = true
    },
    editAgent (row) {
      // 修改
      this.detail = row
      this.$refs.editSpreadDialog.dialogVisible = true
    }
  }
}
</script>
