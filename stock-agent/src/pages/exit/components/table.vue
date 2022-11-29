<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
            <!-- <el-option label="区域二" value="beijing"></el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select filterable v-model="form.status" placeholder="状态">
            <el-option label="审核中" value="0"></el-option>
            <el-option label="出金成功" value="1"></el-option>
            <el-option label="出金失败" value="2"></el-option>
            <el-option label="出金取消" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <!-- <el-form-item label="选择范围">
            <el-date-picker
            v-model="form.time"
            type="datetimerange"
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
            prop="userId"
            width="80px"
            label="用户id">
          </el-table-column>
          <el-table-column
            prop="nickName"
            width="80px"
            label="用户名">
          </el-table-column>
          <el-table-column
            prop="withAmt"
            label="出金金额">
          </el-table-column>
          <el-table-column
            prop="withFee"
            label="手续费">
          </el-table-column>
          <el-table-column
            width="100px"
            prop="withStatus"
            label="状态">
            <template slot-scope="scope">
              <p>
              <span
                :class="scope.row.withStatus==1?'green':scope.row.withStatus==2?'red':scope.row.withStatus==0?'blue':'yellow'">
                <i v-if="scope.row.withStatus==1" class="iconfont icon-zhengchang"></i>
                <i v-if="scope.row.withStatus==2" class="iconfont icon-failure"></i>
                <i v-if="scope.row.withStatus==3" class="iconfont icon-failure"></i>
                <i v-if="scope.row.withStatus==0" class="iconfont icon-dengdai"></i>
                {{scope.row.withStatus==1?'成功':scope.row.withStatus==2?'失败':scope.row.withStatus==0?'审核中':'取消'}}
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="180px"
            prop="applyTime"
            label="申请时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.applyTime | timeFormat}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            width="180px"
            prop="transTime"
            label="出金时间">
            <template slot-scope="scope">
            <span>
              {{scope.row.transTime | timeFormat}}
            </span>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="100px"
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
import DetailDialog from './detail-dialog'

export default {
  components: {
    DetailDialog
  },
  props: {},
  data () {
    return {
      form: {
        realName: '',
        status: '',
        agentId: '',
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
        agentId: this.form.agentId,
        state: this.form.status,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.getUserwithdrawList(opts)
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
        if (column.property === 'withAmt' || column.property === 'withFee') {
          // 需要计算列 ==> 出金金额 浮动盈亏 总盈亏 总市值 手续费 印花税 留仓费
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
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
