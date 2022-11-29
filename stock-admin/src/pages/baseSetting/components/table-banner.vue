<template>
  <div>
    <el-card class="box-card">
      <div class="table">
        <p style="margin-bottom:10px;">排序值越大显示越靠前</p>
        <p>pc端图片建议尺寸：1980*500，手机图片建议尺寸：1753*1080</p>
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addBanner">
              <i class="iconfont icon-add"></i>添加banner
            </el-button>
          </el-row>
        </div>

        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="bannerUrl"
            label="链接">
            <template slot-scope="scope">
              <img class="img" :src="scope.row.bannerUrl" alt="bannerUrl">
            </template>
          </el-table-column>
          <el-table-column
            prop="isOrder"
            label="排序">
          </el-table-column>
          <el-table-column
            prop="isPc"
            label="pc图片">
            <template slot-scope="scope">
              {{scope.row.isPc == '0'?'是':'否'}}
            </template>
          </el-table-column>
          <el-table-column
            prop="isM"
            label="移动端图片">
            <template slot-scope="scope">
              {{scope.row.isM == '0'?'是':'否'}}
            </template>
          </el-table-column>

          <el-table-column
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            prop="isM"
            label="操作">
            <template slot-scope="scope">
              <el-button type="text" title="修改" size="small" @click="updateBanner(scope.row)">
                修改
              </el-button>
              <el-button type="text" title="删除" size="small" @click="delBanner(scope.row.id)">
                删除
              </el-button>

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
    <addDialog :getDate='getList' ref="addDialog"></addDialog>
    <updateDialog :info='detail' :getDate='getList' ref="updateDialog"></updateDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import updateDialog from './update-dialog'

export default {
  components: {
    addDialog,
    updateDialog
  },
  props: {},
  data () {
    return {
      form: {
        taskType: '',
        pageNum: 1,
        pageSize: 10
      },
      list: {
        list: []
      },
      detail: {},
      loading: false
    }
  },
  watch: {},
  computed: {},
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
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.getBannerList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    addBanner () {
      this.$refs.addDialog.dialogVisible = true
    },
    updateBanner (row) {
      this.detail = row
      this.$refs.updateDialog.dialogVisible = true
    },
    async delBanner (id) {
      let data = await api.delBanner({ id: id })
      if (data.status === 0) {
        this.$message.success(data.msg)
        this.getList()
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .img {
    max-width: 80%;
    max-height: 120px;
  }
</style>
