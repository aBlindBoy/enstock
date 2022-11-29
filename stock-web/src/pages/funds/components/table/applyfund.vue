<template>
  <el-container class="user-center usercot futuresholdposition">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>
    <el-container class="main-wrapper ">
      <el-aside width="178px">
        <menu-box></menu-box>
      </el-aside>
      <el-main class="futuresholdposition">
        <div class="wrapper user-center-table">
          <div class="user-center-title" style="text-align:left;">
            <span class="iconfont icon-you" style="color:#C11815;font-size:18px;margin-right:10px"></span>
            my allocation
          </div>
          
          <!-- <div class="table-search">
            <el-row type="flex" justify='end'>
              <el-col :span="8">
                <el-input size="small" v-model="stockCode" placeholder="请输入代码">
                  <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                </el-input>
              </el-col>
              <el-col :span="8">
                <el-input size="small" v-model="stockSpell" placeholder="请输入简拼">
                  <el-button @click="getlist" slot="append" icon="el-icon-search"></el-button>
                </el-input>
              </el-col>
            </el-row>
          </div> -->
          <el-table
            :data="list.list"
            stripe
            v-loading="loading"
            style="width: 100%">
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="Order Number">
                    <span>{{scope.row.orderNumber}}</span>
                  </el-form-item>
                  <el-form-item label="Sub account">
                    <span>{{scope.row.subaccountNumber}}</span>
                  </el-form-item>
                  <el-form-item label="Total trading capital">
                    <span>{{ scope.row.totalTradingAmount}}</span>
                  </el-form-item>
                  <el-form-item label="Warning line">
                    <span>{{ scope.row.lineWarning}}</span>
                  </el-form-item>
                  <el-form-item label="Closing line">
                    <span>{{scope.row.lineUnwind}}</span>
                  </el-form-item>
                  <el-form-item label="Starting time">
                    <span>{{scope.row.beginTime | timeFormat}}</span>
                  </el-form-item>
                  <el-form-item label="Stop time">
                    <span>{{scope.row.endTime | timeFormat}}</span>
                  </el-form-item>
                  
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              prop="totalTradingAmount"
              label="Total trading capital"
              width="120px">
            </el-table-column>
            <el-table-column
              prop="fundsAmount"
              width="80px"
              label="Allocating funds">
              <template slot-scope="scope">
                <span>{{scope.row.fundsAmount}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="margin"
              width="80px"
              label="Margin">
            </el-table-column>
            <el-table-column
              prop="manageFee"
              width="90px"
              label="Funding management fee">
            </el-table-column>
            <el-table-column
              prop="totalTradingAmount"
              label="Amount available in stock"
              width="120px">
            </el-table-column>
            <el-table-column
              prop="orderTotalPrice"
              width="120px"
              label="Stock market value">
            </el-table-column>
            <el-table-column
              prop="allProfitAndLose"
              width="120px"
              label="Stock profit and loss">
            </el-table-column>
            <el-table-column
              prop="status"
              width="80px"
              label="State">
              <template slot-scope="scope">
                <p class="bounceIn">
                  <span v-if="scope.row.status==0" class="red">Pending audit</span>
                  <span v-if="scope.row.status==1" class="green">In operation</span>
                  <span v-if="scope.row.status==2" class="red">Did not pass</span>
                  <span v-if="scope.row.status==3" class="red">Be expired</span>
                  <span v-if="scope.row.status==4" class="green">Over</span>
                </p>
              </template>
            </el-table-column>
            <el-table-column
              fixed="right"
              prop="isLock"
              width="80px"
              label="Allotment">
              <template slot-scope="scope">
                <el-button type="success" plain size="small" @click="withFunding(scope.row)">Allotment</el-button>
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
            layout="total, sizes, prev, pager, next, jumper"
            :total="list.total">
          </el-pagination>
        </div>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
  </el-container>

</template>

<script>
  import HomeHeader from '../../../../components/HeaderOrder'
  import HomeFooter from '../../../../components/Footer'
  import MenuBox from '@/pages/user/components/menu'
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
        loading: false,
        pageNum: 1,
        pageSize: 15,
        stockCode: '', // 代码
        stockSpell: '', // 简拼
        list: {
          list: []
        }
      }
    },
    watch: {},
    computed: {},
    created () {
      this.timer = setInterval(this.refreshList, 5000)
    },
    beforeDestroy () {
      clearInterval(this.timer)
    },
    mounted () {
      this.getlist()
      this.$store.state.userMenu = '2-23'
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
        let opt = {
          userId: 0, 
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getUserApplyList(opt)
        if (data.status === 0) {
          this.list = data.data
        } else {
          this.$message.error(data.msg)
        }
      },
      async refreshList () {
        if (this.refresh || this.loading) {
          return
        }
        this.refresh = true
        this.changeTextClass = {}
        // 获取表格数据
        let opts = {
          userId: 0, 
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        let data = await api.getUserApplyList(opts)
        this.refresh = false
        if (data.status === 0) {
          data.data.list.forEach((element, i) => {
            this.changeTextClass[i] = ''
            if (data.data.list[i].now_price !== this.list.list[i].now_price) {
              // this.changeTextClass[i] = true // 设置对应的动画过滤
              // this.list.list[i].now_price = data.data.list[i].now_price
              // this.list.list[i].profitAndLose = data.data.list[i].profitAndLose
              // this.list.list[i].allProfitAndLose = data.data.list[i].allProfitAndLose
            }
          })
        } else {
          if (data.success === false) {

          } else {
            this.$message.error(data.msg)
          }
        }
      },
      withFunding (val) {
        this.$router.push({
            path: '/funding?id='+ val.id
        })
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
