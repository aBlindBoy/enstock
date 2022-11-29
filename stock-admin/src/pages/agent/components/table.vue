<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <!-- <el-form-item label="代理id">
            <el-input v-model="form.id" placeholder="代理id"></el-input>
        </el-form-item> -->
        <el-form-item label="代理">
          <el-select clearable filterable v-model="form.id" placeholder="代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName + ' /' + i.id "
                       :value="i.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="代理真实名称">
          <el-input v-model="form.realName" placeholder="代理真实名称"></el-input>
        </el-form-item>
        <el-form-item label="代理手机">
          <el-input v-model="form.phone" placeholder="代理手机"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="primary" size="small" @click="addAgent" plain>添加下级代理</el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="agentName"
            label="代理名称/id"
            width="120">
            <template slot-scope="scope">
              <p>
                {{scope.row.agentName}}
                <span style="font-size:12px;color:#959595;">({{scope.row.id}})</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="agentCode"
            label="代码"
            width="100">
          </el-table-column>
          <el-table-column
            prop="parentName"
            label="上级代理"
            width="100">
            <template slot-scope="scope">
              {{scope.row.parentName}}
              <span v-if="scope.row.parentId" style="font-size:12px;color:#959595;">({{scope.row.parentId}})</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="agentRealName"
            label="真实姓名">
          </el-table-column>
          <el-table-column
            width="120px"
            prop="agentPhone"
            label="代理手机号">
          </el-table-column>
          <el-table-column
            width="100px"
            prop="totalMoney"
            label="总资金">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span :class="scope.row.totalMoney>0?'green':'red'">{{scope.row.totalMoney}}</span>
            </p>
          </template>
          </el-table-column>
          <!-- <el-table-column
          width="120px"
            prop="agentPwd"
            label="代理密码">
          </el-table-column> -->
          <el-table-column
            prop="isLock"
            label="代理状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.isLock == 0" class="hide-td" title="正常"><i
                  class="iconfont icon-zhengchang1"></i>正常</a>
                <a v-if="scope.row.isLock == 1" class="hide-td" title="锁定"><i class="iconfont icon-suoding"></i>锁定</a>
              </div>
              <!-- {{scope.row.isLock == 0?'未锁定':'锁定'}} -->
            </template>
          </el-table-column>
          <el-table-column
            width="180px"
            prop="addTime"
            label="注册时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="250px"
            label="修改代理">
            <template slot-scope="scope">
              <el-button type="text" title="修改资金" size="small" @click="toEditAmt(scope.row)"><i
                class="iconfont icon-zijinguanli2"></i></el-button>
              <el-button type="primary" title="删除代理" size="small" @click="delAgent(scope.row)">删除代理</el-button>
              <el-button type="primary" title="修改代理" size="small" @click="editAgent(scope.row)">修改代理</el-button>
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
    <addAgentDialog :agentList='list.list' :getDate='getList' ref="addAgentDialog"></addAgentDialog>
    <ChangeDialog :agentList='list.list' :info='detail' :getDate='getList' ref="changeDialog"></ChangeDialog>
    <editAmtDialog :info='detail' :getDate='getList' ref="editAmtDialog"></editAmtDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addAgentDialog from './add-agent'
import ChangeDialog from './edit-agent'
import editAmtDialog from './amt-dialog'

export default {
  components: {
    addAgentDialog,
    ChangeDialog,
    editAmtDialog
  },
  props: {},
  data () {
    return {
      form: {
        id: '',
        phone: '',
        realName: '',
        pageNum: 1,
        pageSize: 20
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
    this.getAgentList()
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
      let opts = {
        id: this.form.id,
        phone: this.form.phone,
        realName: this.form.realName,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      let data = await api.getSecondAgent(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    addAgent () {
      // 添加代理
      this.$refs.addAgentDialog.dialogVisible = true
    },
    toEditAmt (row) {
      // 修改口入款
      this.detail = row
      this.$refs.editAmtDialog.dialogVisible = true
    },
    editAgent (row) {
      // 添加代理
      this.detail = row
      this.$refs.changeDialog.dialogVisible = true
    },
    delAgent (row) {
      this.$confirm('确认删除代理？此操作不可恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let data = await api.delAgent({ agentId: row.id })
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.getList()
        } else {
          this.$message.error(data.msg)
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消'
        })
      })
    }
  }
}
</script>
