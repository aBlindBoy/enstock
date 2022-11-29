<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <div class="usercot message">
      <el-container class="main-wrapper">
      <el-aside width="200px">
        <menu-box></menu-box>
      </el-aside>
      <el-main style=" min-height: calc(100vh - 150px );">
        <div class="wrapper">
          <div class="user-center-title" style="text-align:left;">
            <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
            Station news
          </div>
          <el-table
            :data="list.list"
            style="width: 100%">
            <el-table-column
              prop="id"
              width="60px"
              label="id">
            </el-table-column>
            <el-table-column
              prop="typeName"
              width="90px"
              label="type">
            </el-table-column>
            <el-table-column
              prop="status"
              width="100px"
              label="state">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status<=1?'danger':scope.row.status>1?'success':''">
                  <span v-if="scope.row.status == 2">Have read</span>
                  <span v-else>unread</span>
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="content"
              label="content">
            </el-table-column>
            <el-table-column
              prop="addTime"
              width="166px"
              label="time">
              <template slot-scope="scope">
                <b v-if="scope.row.addTime">{{scope.row.addTime | timeFormat}}</b>
                <b v-else></b>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            class="pull-right"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="list.pageNum"
            :page-sizes="[10, 20, 30, 40,50]"
            :page-size="list.pageSize"
            layout="sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
    </div>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../../components/HeaderOrder'
  import HomeFooter from '../../../../components/Footer'
  import MenuBox from '../menu'
  import * as api from '../../../../axios/api'

  export default {
    components: {
      HomeHeader,
      HomeFooter,
      MenuBox
    },
    props: {},
    data () {
      return {
        pageNum: 1,
        pageSize: 11,
        list: {
          list: []
        }
      }
    },
    watch: {},
    computed: {},
    created () {},
    mounted () {
      this.getlist()
      this.$store.state.userMenu = '2-13'
    },
    methods: {
      handleSizeChange (size) {
        this.pageSize = size
        this.getlist()
      },
      handleCurrentChange (page) {
        this.pageNum = page
        this.getlist()
      },
      async getlist () {
        // 获取 提现记录
        let opts = {
          pageNum: this.pageNum,
          pageSize: 10,
          userId: 0
        }
        let data = await api.getMessagelist(opts)
        if (data.status === 0) {
          this.list = data.data
          console.log(this.list)
          this.updateMessageStatus()
        } else {
          this.$message.error(data.msg)
        }
      },
      async updateMessageStatus () {
        // 获取 提现记录
        let opts = {}
        let data = await api.updateMessageStatus(opts)
        if (data.status === 0) {
        } else {
        }
      }
    }
  }
</script>
<style lang="less" scoped>
  .code {
    font-size: 12px;
    color: #999;
  }

  .main-wrapper {
    .wrapper {
      padding: 20px;

      .table-search {
        margin-bottom: 15px;
      }
    }
  }
</style>
