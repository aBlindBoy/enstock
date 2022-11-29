<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="下级代理">
          <el-select clearable filterable v-model="form.agentId" placeholder="下级代理">
            <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
          </el-select>
        </el-form-item>
          <el-form-item label="认证状态">
          <el-select clearable v-model="form.isActive" placeholder="认证状态">
            <el-option label="全部" value=""></el-option>
            <el-option label="待认证" value="0"></el-option>
            <el-option label="待审核" value="1"></el-option>
            <el-option label="审核成功" value="2"></el-option>
            <el-option label="驳回" value="3"></el-option>
          </el-select>
        </el-form-item>
       
        <!-- <el-form-item label="账号类型">
          <el-select clearable v-model="form.type" placeholder="账号类型">
            <el-option label="正式" value="0"></el-option>
            <el-option label="模拟" value="1"></el-option>
          </el-select>
        </el-form-item> -->
        <!-- <el-form-item label="用户id">
            <el-input v-model="form.userId" placeholder="用户id"></el-input>
        </el-form-item> -->
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" placeholder="真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户手机">
          <el-input v-model="form.phone" placeholder="用户手机"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
      <el-alert
        v-if="list.list.length>0 && list.list[0].now_price == 0"
        style="margin-bottom:10px"
        title="集合竞价中，'现价'、'浮动盈亏'、'总盈亏'暂不能查看"
        type="warning"
        close-text="知道了">
      </el-alert>
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="text" size="small" @click="addAccount">
              <i class="iconfont icon-add"></i>添加账户
            </el-button>
          </el-row>
        </div>
        <!--        :default-expand-all='true' -->
        <el-table
          v-loading="loading"
          :data="list.list"
          show-summary
          :summary-method="getSummaries"
          :row-class-name="tableRowClassName"
   
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <!-- <el-form-item label="总资金">
                  <span>{{(Number(props.row.userAmt) + Number(props.row.userIndexAmt)).toFixed(2)}}</span>
                </el-form-item> -->
                <el-form-item label="融资资金">
                  <!-- <span>{{ props.row.userAmt}}</span> -->
                  <el-tag>{{ props.row.userAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="融资可用">
                  <!-- <span>{{ props.row.enableAmt}}</span> -->
                  <el-tag>{{ props.row.enableAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="指数资金">
                  <!-- <span>{{ props.row.userIndexAmt}}</span> -->
                  <el-tag>{{ props.row.userIndexAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="指数可用">
                  <!-- <span>{{ props.row.enableIndexAmt}}</span> -->
                  <el-tag>{{ props.row.enableIndexAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="期货资金">
                  <!-- <span>{{ props.row.userFutAmt}}</span> -->
                  <el-tag>{{ props.row.userFutAmt}}</el-tag>
                </el-form-item>
                <el-form-item label="期货可用">
                  <!-- <span>{{ props.row.enableFutAmt}}</span> -->
                  <el-tag>{{ props.row.enableFutAmt}}</el-tag>
                </el-form-item>
                <!-- <el-form-item label="注册时间">
                  <span>{{props.row.regTime | timeFormat}}</span>
                </el-form-item> -->
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            width="120px"
            prop="agentName"
            label="所属代理/id">
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
            prop="realName"
            width="130px"
            label="真实姓名">
            <template slot-scope="scope">
              <p>
                {{scope.row.realName}}/<span class="small">{{scope.row.id}}</span>
                <span class="small" v-if="scope.row.accountType == 1">(模拟)</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="phone"
            width="120px"
            label="手机号">
          </el-table-column>
          <el-table-column
            prop="userAmt"
            label="总资金">
            <template slot-scope="scope">
              <p class="number proColor bounceIn">
                {{(Number(scope.row.userAmt) + Number(scope.row.userIndexAmt) +
                Number(scope.row.userFutAmt)).toFixed(2)}}
              </p>
            </template>
          </el-table-column>
          <!-- <el-table-column
            width="110px"
            prop="userAmt"
            label="融资资金">
            <template slot-scope="scope">
              <p class="number proColor bounceIn">
                {{scope.row.userAmt}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="110px"
            prop="enableAmt"
            label="融资可用资金">
            <template slot-scope="scope">
              <p class="number proColor">
                {{scope.row.enableAmt}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="110px"
            prop="userAmt"
            label="期货资金">
            <template slot-scope="scope">
              <p class="number proColor bounceIn">
                {{scope.row.userAmt}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="110px"
            prop="enableAmt"
            label="期货可用资金">
            <template slot-scope="scope">
              <p class="number proColor">
                {{scope.row.enableAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="userIndexAmt"
            label="指数资金">
            <template slot-scope="scope">
              <p class="number proColor bounceIn">
                {{scope.row.userIndexAmt}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            width="110px"
            prop="enableIndexAmt"
            label="指数可用资金">
            <template slot-scope="scope">
              <p class="number proColor">
                {{scope.row.enableIndexAmt}}
              </p>
            </template>
          </el-table-column> -->
          <!-- <el-table-column
            width="110px"
            prop="sumBuyAmt"
            label="买入资金">
            <template slot-scope="scope">
              <p class="proColor">
                {{scope.row.enableAmt}}
              </p>
            </template>
          </el-table-column> -->
          <el-table-column
            prop="isActive"
            label="认证信息">
            <template slot-scope="scope">
              <div>
                <!-- 0 ==》 注册 1==》 提交激活 2==》 成功 3==》 激活打回 -->
                <a v-if="scope.row.isActive == 0" class="blue" title="待认证"><i
                  class="iconfont icon-iconfontweitongguo"></i>待认证</a>
                <a v-if="scope.row.isActive == 1" class="red" title="待审核"><i class="iconfont icon-dengdai"></i>待审核</a>
                <a v-if="scope.row.isActive == 2" class="green" title="认证成功"><i
                  class="iconfont icon-tongguo4"></i>成功</a>
                <a v-if="scope.row.isActive == 3" class="yellow" title="驳回"><i class="iconfont icon-audi1Reject"></i>驳回</a>
              </div>
              <!-- {{scope.row.isLock == 0?'未锁定':'锁定'}} -->
            </template>
          </el-table-column>
          <el-table-column
            prop="isLock"
            label="交易状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <span v-if="scope.row.isLock == 0" class="green">可交易</span>
                <span v-if="scope.row.isLock == 1" class="red">不可交易</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="isLogin"
            label="登录状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <span v-if="scope.row.isLogin == 0" class="green">可登陆</span>
                <span v-if="scope.row.isLogin == 1" class="red">不可登陆</span>
                <!-- <a v-if="scope.row.isLogin == 0" class="hide-td" title="正常">
                  正常
                </a>
                <a v-if="scope.row.isLogin == 1" class="hide-td yellow" title="不可登陆">
                  不可登陆
                </a> -->
              </div>
            </template>
          </el-table-column>
          <el-table-column
            width="170px"
            prop="regTime"
            label="注册时间">
            <template slot-scope="scope">{{scope.row.regTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="250px"
            prop="isLock"
            label="操作">
            <template slot-scope="scope">
              <el-button style="color:#606262;" type="text" title="查看详情" size="small" @click="toDetail(scope.row)"><i
                class="iconfont icon-chakan"></i></el-button>
              <el-button type="text" title="修改用户信息" size="small" @click="toEditInfo(scope.row)"><i
                class="iconfont icon-xiugai"></i></el-button>
              <el-button type="text" title="修改银行卡" size="small" @click="toEditBankInfo(scope.row)"><i
                class="iconfont icon-xiugaiyinhangqia- "></i></el-button>
              <el-button type="text" title="修改资金" size="small" @click="toEditAmt(scope.row)"><i
                class="iconfont icon-zijinguanli2"></i></el-button>
              <el-button style="color:red;" type="text" title="审核" size="small" @click="toChange(scope.row)"><i
                class="iconfont icon-shenhe_shangpinfenlei"></i></el-button>
              <el-button style="color:red;" type="text" title="删除" size="small" @click="deleteUser(scope.row.id)"><i
                style="font-size: 18px;" class="el-icon-delete"></i></el-button>
              <!-- <el-button style="color:#FF9800;" v-if="scope.row.isLock == 0" type="text" title="锁定交易状态" size="small" @click="toLockUser(scope.row.id)"><i class="iconfont icon-jiesuo"></i></el-button> -->
              <!-- <el-button style="color:#FF9800;" v-if="scope.row.isLock == 1" type="text" title="解锁交易状态" size="small" @click="toUnlockUser(scope.row.id)"><i class="iconfont icon-suoding"></i></el-button> -->
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
    <addDialog :agentList='agentList' :getDate='getList' ref="addSimulatedAccountDialog"></addDialog>
    <DetailDialog :info='detail' ref="detailDialog"></DetailDialog>
    <EditDialog :info='detail' :getDate='getList' ref="editDialog"></EditDialog>
    <EditInfoDialog :info='detail' :agentList='agentList' :getDate='getList' ref="editInfoDialog"></EditInfoDialog>
    <ChangeAuthDialog :info='detail' :getDate='getList' ref="changeAuthDialog"></ChangeAuthDialog>
    <EditBankInfoDialog :info='detail' :getDate='getList' ref="editBankInfoDialog"></EditBankInfoDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import DetailDialog from './detail-dialog'
import EditDialog from './amt-dialog'
import EditInfoDialog from './update-dialog'
import ChangeAuthDialog from './auditing-dialog'
import EditBankInfoDialog from './updatebank-dialog'

export default {
  components: {
    addDialog,
    DetailDialog,
    EditDialog,
    EditInfoDialog,
    ChangeAuthDialog,
    EditBankInfoDialog
  },
  props: {},
  data () {
    return {
      form: {
        realName: '',
        userId: '',
        phone: '',
        type: '0',
        agentId: '',
        isActive:'',
        pageNum: 1,
        pageSize: 10,
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
      detail: {},
      loading: false,
      timer: null,
      refresh: false,
      usertype: 0,
      changeTextClass: {} // 表格中的数据变化
    }
  },
  watch: {
    $route(){
      this.usertype = this.$route.query.type
    },
    usertype() {
      this.getList()
    },
  },
  computed: {},
  created () {
    // this.timer = setInterval(this.refreshList, 5000)
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
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
    async refreshList () {
      if (this.refresh || this.loading) {
        return
      }
      this.refresh = true
      // 获取表格数据
      let opts = {
        userId: this.form.userId,
        realName: this.form.realName,
        phone: this.form.phone,
        agentId: this.form.agentId,
        accountType: this.usertype,
        isActive:this.form.isActive,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.changeTextClass = {}
      let data = await api.getUserManList(opts)
      this.refresh = false
      if (data.status === 0) {
        data.data.list.forEach((element, i) => {
          // 判断刷新需要刷新的值
          this.changeTextClass[i] = ''
          if (this.list.list[i].profitAndLose !== data.data.list[i].profitAndLose) {
            this.changeTextClass[i] = true // 设置对应的动画过滤
            this.list.list[i].profitAndLose = data.data.list[i].profitAndLose
            this.list.list[i].userAmt = data.data.list[i].userAmt
          }
        })
      } else {
        this.$message.error(data.msg)
      }
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
        userId: this.form.userId,
        realName: this.form.realName,
        phone: this.form.phone,
        agentId: this.form.agentId,
        accountType: this.usertype,
        isActive:this.form.isActive,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.getUserManList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    addAccount () {
      // 添加模拟账户
      this.$refs.addSimulatedAccountDialog.dialogVisible = true
    },
    toDetail (row) {
      this.detail = row
      this.$refs.detailDialog.dialogVisible = true
    },
    toChange (row) {
      // 审核信息
      this.detail = row
      this.$refs.changeAuthDialog.dialogVisible = true
    },
    async toLockUser (val) {
      // 锁定用户
      let data = await api.updateLock({ userId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('锁定成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    async toUnlockUser (val) {
      // 解锁用户
      let data = await api.updateLock({ userId: val })
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
    toEditInfo (row) {
      // 修改用户信息
      this.detail = row
      this.$refs.editInfoDialog.dialogVisible = true
    },
    toEditBankInfo (row) {
      // 修改银行卡信息
      this.detail = row
      this.$refs.editBankInfoDialog.dialogVisible = true
    },
    tableRowClassName ({ row, rowIndex }) {
      // 设置表格行高亮
      if (row.allProfitAndLose < 0 && ((-row.allProfitAndLose) / row.forceLine) >= 0.8) {
        return 'warning-row'
      }
      return ''
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
        if (column.property === 'userAmt' || column.property === 'enableAmt' || column.property === 'allFreezAmt' || column.property === 'allProfitAndLose') {
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
    },
    deleteUser (val) {
      // 删除用户所有信息
      this.$confirm('确认删除该用户吗？此操作不可恢复，将删除该用户所有的充值、提现与持仓记录！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        let data = await api.deleteDetail({ userId: val })
        if (data.status === 0) {
          await this.getList()
          this.$message.success('删除成功！')
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
<style lang="less" scoped>
  .table .el-table .warning-row {
    background: rgba(245, 108, 108, .1);
  }

  .table .demo-table-expand {
    /deep/ label {
      display: block;
      text-align: center;
      line-height: 20px;
      width: 100%;
    }

    /deep/ .el-form-item {
      width: 16.6%;
      text-align: center;
    }
  }
</style>
