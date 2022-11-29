<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="请输入通道名称">
          <!-- <el-select filterable v-model="form.channelType" placeholder="通道类型">
            <el-option label="支付宝" value="0"></el-option>
            <el-option label="对公转账" value="1"></el-option>
            <el-option label="微信" value="2"></el-option>
          </el-select> -->
          <el-input v-model="form.channelType" placeholder="请输入通道名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加支付通道
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="channelType"
            width="80px"
            label="通道类型">
          </el-table-column>
          <el-table-column
            width="60px"
            prop="channelImg"
            label="图片">
            <template slot-scope="scope">
              <img width="40px" :src="scope.row.channelImg" alt="">
            </template>
          </el-table-column>
          <el-table-column
            prop="channelName"
            width="100px"
            label="通道名字">
          </el-table-column>
          <el-table-column
            width="200px"
            prop="channelAccount"
            label="账号">
          </el-table-column>
          <el-table-column
            prop="channelDesc"
            label="描述">
          </el-table-column>

          <el-table-column
            width="110px"
            prop="channelMinLimit"
            label="最小充值金额">
          </el-table-column>
          <el-table-column
            width="110px"
            prop="channelMaxLimit"
            label="最大充值金额">
          </el-table-column>
          <el-table-column
            prop="isShow"
            width="80px"
            label="是否显示">
            <template slot-scope="scope">
              <p>
              <span>
                <!-- 隐藏  -->
                <i v-if="scope.row.isShow==0" class="iconfont icon-xianshi"></i>
                <!-- 显示 -->
                <i v-if="scope.row.isShow==1" class="iconfont icon-yanjing"></i>
                {{scope.row.isShow==1?'隐藏':'显示'}}
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="200px"
            label="修改通道">
            <template slot-scope="scope">
              <el-button type="primary" plain size="small" @click="tochangeChannel(scope.row)">修改通道</el-button>
              <el-button type="darge" plain size="small" @click="deleteChannel(scope.row)">删除</el-button>
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
        <addDialog :getDate='getList' ref="addDialog"></addDialog>
        <changeDialog :info="detail" :getDate='getList' ref="changeDialog"></changeDialog>
      </div>
    </el-card>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import changeDialog from './change-dialog'

export default {
  components: {
    addDialog,
    changeDialog
  },
  props: {},
  data () {
    return {
      form: {
        channelType: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      loading: false, // 表格加载
      detail: null
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getList()
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
    async getList () {
      // 获取表格数据
      let opts = {
        channelType: this.form.channelType,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.payChannelList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    tochangeChannel (row) {
      // 查看详情
      this.detail = row
      this.$refs.changeDialog.dialogVisible = true
    },
    addAccount () {
      this.$refs.addDialog.dialogVisible = true
    },
    deleteChannel (row) {
      // 删除
      this.$confirm('确认删除该通道？此操作不可恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let data = await api.delChannel({ cId: row.id })
        if (data.status === 0) {
          this.$message.success(data.msg)
          this.getList()
        } else {
          this.$message.error(data.msg)
        }
      }).catch(() => {

      })
    }
  }
}
</script>
