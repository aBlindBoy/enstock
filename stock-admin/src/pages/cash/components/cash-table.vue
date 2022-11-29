<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName+ '(' +i.id + ')'"
                       :value="i.id"></el-option>
            <!-- <el-option label="区域二" value="beijing"></el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item label="持仓id">
          <el-input v-model="form.positionId" placeholder="持仓id"></el-input>
        </el-form-item>
        <el-form-item label="用户id">
          <el-input v-model="form.userId" placeholder="用户id"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <!-- <el-form-item label="选择范围">
            <el-date-picker
            v-model="form.time"
            type="datetimerange"
            value-format="yyyy-MM-dd HH:mm:ss"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <el-table
          v-loading="loading"
          :data="list.list"
          show-summary
          :summary-method="getSummaries"
          style="width: 100%">
          <!-- <el-table-column
            prop="bankNo"
            label="订单号">
          </el-table-column> -->
          <el-table-column
            prop="agentId"
            width="80px"
            label="代理/id">
            <template slot-scope="scope">
              <p>
                {{scope.row.agentName}}
                <span class="small">
                ({{scope.row.agentId}})
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="nickName"
            width="110px"
            label="用户名/id">
            <template slot-scope="scope">
              <p>
                {{scope.row.userName}}
                <span class="small">
                ({{scope.row.userId}})
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="100px"
            prop="positionId"
            label="持仓id">
          </el-table-column>
          <el-table-column
            width="100px"
            prop="deType"
            label="类型">
          </el-table-column>
          <el-table-column
            width="80px"
            prop="deAmt"
            label="金额">
            <template slot-scope="scope">
            <span :class="scope.row.deAmt>0?'red':scope.row.deAmt<0?'green':''">
              {{scope.row.deAmt}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="deSummary"
            label="描述">
          </el-table-column>
          <el-table-column
            width="180px"
            prop="addTime"
            label="创建时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.addTime | timeFormat}}
            </span>
            </template>
          </el-table-column>
          <!-- <el-table-column
            fixed="right"
            width="100px"
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" plain size="small" @click="toDetail(scope.row.id)">查看详情</el-button>
            </template>
          </el-table-column> -->
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
        <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>
      </div>
    </el-card>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import DetailDialog from './change-dialog'

export default {
  components: {
    DetailDialog
  },
  props: {},
  data () {
    return {
      form: {
        userName: null,
        positionId: null,
        agentId: null,
        userId: null,
        time: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      agentList: [
        {
          'id': 2,
          'agentName': '下级1',
          'agentRealName': '下级1',
          'agentPhone': '18888888888',
          'agentCode': '8888'
        }
      ],
      loading: false, // 表格加载
      detail: null
    }
  },
  watch: {},
  computed: {},
  created () {},
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
    onSubmit () {
      // 查询表格
      this.getList()
    },
    async getAgentList () {
      // 获取下级代理数据
      let opts = {
        pageNum: 1,
        pageSize: 100
      }
      let data = await api.getSecondAgent(opts)
      if (data.status === 0) {
        this.agentList = data.data.list
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      // 获取表格数据
      let opts = {
        agentId: this.form.agentId,
        userName: this.form.userName,
        positionId: this.form.positionId,
        userId: this.form.userId,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.cashList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    toDetail (row) {
      // 查看详情
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
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
        if (column.property === 'deAmt') {
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
      return sums
    }
  }
}
</script>
