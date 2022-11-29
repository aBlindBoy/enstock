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
        <el-form-item label="用户Id">
          <el-input v-model="form.userId" placeholder="用户手机"></el-input>
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="form.userName" placeholder="账号类型"></el-input>
        </el-form-item>
        <el-form-item label="持仓Id">
          <el-input v-model="form.positionId" placeholder="持仓订单号"></el-input>
        </el-form-item>
        <el-form-item label="选择范围">
          <el-date-picker
            v-model="form.time"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="agentName"
            label="所属代理">
          </el-table-column>
          <el-table-column
            prop="userName"
            width="100px"
            label="用户名/id">
            <template slot-scope="scope">
              <span>{{scope.row.userName}}/{{scope.row.userId}}</span>
            </template>
          </el-table-column>
          <el-table-column
            width="80px"
            prop="positionId"
            label="持仓id">
            <template slot-scope="scope">
            <span>
              {{scope.row.positionId?scope.row.positionId:'-'}}
            </span>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="userId"
            label="id"
            width="60px">
          </el-table-column>
          <el-table-column
            prop="userName"
            label="用户名">
          </el-table-column> -->
          <el-table-column
            prop="deType"
            label="操作状态">
            <template slot-scope="scope">
              {{scope.row.deType}}
            </template>
          </el-table-column>
          <el-table-column
            prop="deAmt"
            label="操作金额">
          </el-table-column>
          <el-table-column
            prop="buyOrderTime"
            label="操作时间"
            width="180">
            <template slot-scope="scope">
            <span>
              {{scope.row.addTime | timeFormat}}
            </span>
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
    <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>

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
        userId: '',
        userName: '',
        positionId: '',
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
      detail: null // 详情
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
      // console.log(this.timeFormat(this.form.time[0]),'time')
      // 获取表格数据
      let opts = {
        userId: this.form.userId,
        userName: this.form.userName,
        positionId: this.form.positionId,
        agentId: this.form.agentId,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.getUserCapitalList(opts)
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
    }
  }
}
</script>
