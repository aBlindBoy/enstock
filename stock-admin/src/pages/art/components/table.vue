<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <!-- <el-form-item label="请输入渠道名称">
              <el-input v-model="form.channelType" placeholder="请输入渠道名称"></el-input>
          </el-form-item> -->
        <!-- <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item> -->
      </el-form>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加公告
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="id"
            width="80px"
            label="公告id">
          </el-table-column>
          <el-table-column
            width="60px"
            prop="artImg"
            label="图片">
            <template slot-scope="scope">
              <img width="40px" :src="scope.row.artImg" alt="">
            </template>
          </el-table-column>
          <el-table-column
            prop="artTitle"
            label="公告标题">
            <template slot-scope="scope">
              <a class="hide-td" href="javasvript:;" :title="scope.row.artTitle">{{scope.row.artTitle}}</a>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="artType"
            label="公告类型">
          </el-table-column> -->
          <el-table-column
            width="180px"
            prop="author"
            label="来源">
            <template slot-scope="scope">
              <a class="hide-td" href="javasvript:;" :title="scope.row.author">{{scope.row.author}}</a>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="hitTimes"
            label="点击次数">
          </el-table-column> -->

          <!-- <el-table-column
          width="110px"
            prop="artSummary"
            label="摘要">
            <template slot-scope="scope">
              <a class="hide-td" href="javascript:;" :title="scope.row.artSummary">{{scope.row.artSummary}}</a>
            </template>
          </el-table-column> -->
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
                {{scope.row.withStatus==1?'隐藏':'显示'}}
              </span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="180px"
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">
              {{scope.row.addTime | timeFormat}}
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="220px"
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" plain size="small" @click="toDelChannel(scope.row)">删除</el-button>
              <el-button type="primary" plain size="small" @click="tochangeChannel(scope.row)">修改</el-button>
              <el-button type="primary" plain size="small" @click="toDetailChannel(scope.row)">查看</el-button>
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
        <detailDialog :info="detail" :getDate='getList' ref="detailDialog"></detailDialog>
      </div>
    </el-card>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import changeDialog from './change-dialog'
import detailDialog from './detail-dialog'

export default {
  components: {
    addDialog,
    changeDialog,
    detailDialog
  },
  props: {},
  data () {
    return {
      form: {
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
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.artList(opts)
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
    toDetailChannel (row) {
      // 查看详情
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
    },
    addAccount () {
      this.$refs.addDialog.dialogVisible = true
    },
    toDelChannel (row) {
      this.$confirm('确认删除公告？此操作不可恢复', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let data = await api.delArt({ artId: row.id })
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
