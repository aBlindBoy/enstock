<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="管理员姓名">
          <el-input v-model="form.adminName" placeholder="管理员姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.adminPhone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加管理
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            label="id">
          </el-table-column>
          <el-table-column
            prop="adminName"
            label="管理员名字">
          </el-table-column>
          <el-table-column
            prop="adminPhone"
            label="管理员手机">
          </el-table-column>
          <!-- <el-table-column
            prop="adminPwd"
            label="密码">
          </el-table-column> -->
          <el-table-column
            prop="isLock"
            label="管理员状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a class="hide-td" title="正常"><i v-if="scope.row.isLock == 0" class="iconfont icon-zhengchang1"></i></a>
                <a class="hide-td" title="锁定"><i v-if="scope.row.isLock == 1" class="iconfont icon-suoding"></i></a>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="addTime"
            label="注册时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="150px"
            prop="isLock"
            label="操作">
            <template slot-scope="scope">
              <el-button v-if="scope.row.isLock == 0" type="text" title="锁定用户" size="small"
                         @click="toLockUser(scope.row.id)"><i class="iconfont icon-suoding "></i></el-button>
              <el-button v-if="scope.row.isLock == 1" type="text" title="解锁用户" size="small"
                         @click="toUnlockUser(scope.row.id)"><i class="iconfont icon-jiesuo"></i></el-button>
              <el-button type="text" title="修改密码" size="small" @click="toChangePwd(scope.row)"><i
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
    <addDialog :getDate='getList' ref="addAccountDialog"></addDialog>
    <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>
    <EditDialog :info='detail' :getDate='getList' ref="channgeDialog"></EditDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import DetailDialog from './detail-dialog'
import EditDialog from './change-dialog'

export default {
  components: {
    addDialog,
    DetailDialog,
    EditDialog
  },
  props: {},
  data () {
    return {
      form: {
        realName: '',
        userId: '',
        phone: '',
        type: '',
        agentId: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      agentList: [],
      detail: {},
      loading: false,
      timer: null,
      refresh: false,
      changeTextClass: {} // 表格中的数据变化
    }
  },
  watch: {},
  computed: {},
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
        this.agentList = data.data.list
      } else {
        this.$message.error(data.msg)
      }
    },
    async getList () {
      // 获取表格数据
      let opts = {
        adminName: this.form.adminName,
        adminPhone: this.form.adminPhone,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.settingList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    addAccount () {
      // 添加模拟账户
      this.$refs.addAccountDialog.dialogVisible = true
    },
    toDetail (row) {
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
    },
    async toLockUser (val) {
      // 锁定管理员
      let data = await api.lockMan({ adminId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('锁定成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    async toUnlockUser (val) {
      // 解锁管理员
      let data = await api.lockMan({ adminId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('解除锁定成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    toEditAmt (row) {
      // 修改口入款
      this.detail = row
      this.$refs.editDialog.dialogVisible = true
    },
    async toChangePwd (row) {
      // 修改密码
      this.detail = row
      this.$refs.channgeDialog.dialogVisible = true
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }
</style>
