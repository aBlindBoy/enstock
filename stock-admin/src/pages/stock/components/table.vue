<template>
  <div>
    <el-card class="box-card">
      <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-row>
          <el-radio-group v-model="form.stockPlate" @change="onSubmit">
            <!-- <el-radio-button label="全部"></el-radio-button> -->
            <el-radio-button label="A股"></el-radio-button>
            <el-radio-button label="科创"></el-radio-button>
          </el-radio-group>
        </el-row>
        <el-form-item label="沪深股" prop="stockType">
          <el-select v-model="form.stockType" placeholder="沪深股">
            <el-option label="沪股" value="sh"></el-option>
            <el-option label="深股" value="sz"></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item  label="股票类型">
          <el-cascader
            :options="options"
            v-model="selectedOptions"
            @change="handleChange">
          </el-cascader>
        </el-form-item> -->
        <el-form-item label="显示状态">
          <el-select clearable v-model="form.showState" placeholder="显示状态">
            <el-option label="显示" value="0"></el-option>
            <el-option label="隐藏" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="锁定状态">
          <el-select clearable v-model="form.lockState" placeholder="锁定状态">
            <el-option label="正常" value="0"></el-option>
            <el-option label="锁定" value="1"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="代码">
          <el-input v-model="form.code" placeholder="代码"></el-input>
        </el-form-item>
        <el-form-item label="股票名">
          <el-input v-model="form.name" placeholder="股票名"></el-input>
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
              <i class="iconfont icon-add"></i>添加股票
            </el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="股票gid">
                  <span>{{ props.row.stockGid }}</span>
                </el-form-item>
                <el-form-item label="股票简拼">
                  <span>{{ props.row.stockSpell }}</span>
                </el-form-item>
                <el-form-item label="添加时间">
                  <span>{{props.row.addTime | timeFormat}}</span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
            prop="stockName"
            label="股票名字">
            <template slot-scope="scope">
              <p>
                <span class="show red">{{scope.row.stockType == 'sz'?'深':'沪'}}</span>
                <span v-if="scope.row.stockPlate == '科创'" class="show">科创</span>
              </p>
              <p class="big-font">
                {{scope.row.stockName}}
                <span class="pro-code">{{scope.row.stockCode}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="nowPrice"
            label="现价">
            <template slot-scope="scope">
              <p :class="scope.row.hcrate<0?'green':scope.row.hcrate == 0?'':'red'">
                {{Number(scope.row.nowPrice).toFixed(2)}}
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="hcrate"
            label="涨跌幅">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;" :class="scope.row.hcrate<0?'green':scope.row.hcrate == 0?'':'red'">{{scope.row.hcrate}}%</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="day3Rate"
            label="最近3天涨跌">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;" :class="scope.row.day3Rate<0?'green':scope.row.day3Rate == 0?'':'red'">{{scope.row.day3Rate}}%</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="spreadRate"
            label="点差费率">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;" :class="scope.row.spreadRate<0?'green':scope.row.spreadRate == 0?'':'red'">{{scope.row.spreadRate*100}}%</span>
              </p>
            </template>
          </el-table-column>
          <!-- <el-table-column
            prop="stockSpell"
            width="120px"
            label="简拼">
          </el-table-column>
          <el-table-column
            width="110px"
            prop="stockGid"
            label="股票id">
          </el-table-column> -->
          <el-table-column
            prop="isShow"
            label="显示状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.isShow == 0" class="hide-td" title="显示">
                  <i class="iconfont icon-xianshi"></i>显示
                </a>
                <a v-if="scope.row.isShow == 1" class="hide-td" title="隐藏">
                  <i class="iconfont icon-yanjing"></i>隐藏
                </a>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="isLock"
            label="股票状态">
            <template slot-scope="scope">
              <div class="lock-status">
                <a v-if="scope.row.isLock == 0" class="hide-td" title="正常">
                  <i class="iconfont icon-zhengchang1"></i>正常
                </a>
                <a v-if="scope.row.isLock == 1" class="hide-td" title="锁定">
                  <i class="iconfont icon-suoding"></i>锁定
                </a>
              </div>
            </template>
          </el-table-column>
          <!-- <el-table-column
          width="180px"
            prop="regTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column> -->
          <el-table-column
            fixed="right"
            width="150px"
            prop="isLock"
            label="操作">
            <template slot-scope="scope">
              <el-button v-if="scope.row.isShow == 0" type="text" title="隐藏股票" size="small"
                         @click="tounShowStock(scope.row.id)"><i class="iconfont icon-yanjing"></i></el-button>
              <el-button v-if="scope.row.isShow == 1" type="text" title="显示股票" size="small"
                         @click="toShowStock(scope.row.id)"><i class="iconfont icon-xianshi"></i></el-button>
              <el-button v-if="scope.row.isLock == 0" type="text" title="锁定股票" size="small"
                         @click="toLockUser(scope.row.id)"><i class="iconfont icon-suoding "></i></el-button>
              <el-button v-if="scope.row.isLock == 1" type="text" title="解锁股票" size="small"
                         @click="toUnlockUser(scope.row.id)"><i class="iconfont icon-jiesuo"></i></el-button>
              <el-button type="text" title="修改股票" size="small" @click="toEditInfo(scope.row)"><i
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
    <addDialog :agentList="agentList" :getDate='getList' ref="addSimulatedAccountDialog"></addDialog>
    <editstockDialog :info='detail' :agentList='agentList' :getDate='getList' ref="editstockDialog"></editstockDialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'
import addDialog from './add-dialog'
import editstockDialog from './editstock-dialog'

export default {
  components: {
    addDialog,
    editstockDialog
  },
  props: {},
  data () {
    return {
      form: {
        code: '',
        name: '',
        stockPlate: '',
        stockType: '',
        showState: '',
        lockState: '',
        pageNum: 1,
        pageSize: 10
      },
      selectedOptions: [],
      options: [
        {
          value: '科创',
          label: '科创'
        }, {
          value: '',
          label: 'A股',
          children: [{
            value: 'sh',
            label: '沪股'
          }, {
            value: 'sz',
            label: '深股'
          }]
        }
      ],
      list: {
        list: []
      },
      agentList: [],
      detail: {},
      loading: false
    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.getList()
    this.getAgentList()
  },
  methods: {
    handleChange (value) {
      if (value.length === 1) {
        this.form.stockPlate = value[value.length - 1]
      } else {
        this.form.stockType = value[value.length - 1]
      }
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
    },
    toEditInfo (row) {
      // 修改股票信息
      this.detail = row
      this.$refs.editstockDialog.dialogVisible = true
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getList()
    },
    onSubmit () {
      this.form.pageNum = 1
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
        code: this.form.code,
        name: this.form.name,
        showState: this.form.showState,
        lockState: this.form.lockState,
        stockPlate: this.form.stockPlate === '科创' ? '科创' : '',
        stockType: this.form.stockType,
        pageSize: this.form.pageSize,
        pageNum: this.form.pageNum
      }
      this.loading = true
      let data = await api.getStockList(opts)
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
      this.loading = false
    },
    addAccount () {
      // 添加股票
      this.$refs.addSimulatedAccountDialog.dialogVisible = true
    },
    async toLockUser (val) {
      // 锁定
      let data = await api.updateStockLock({ stockId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('锁定成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    async toUnlockUser (val) {
      // 解锁
      let data = await api.updateStockLock({ stockId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('解除锁定成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    async toShowStock (val) {
      // 显示股票
      let data = await api.updateStockShow({ stockId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('显示股票成功！')
      } else {
        this.$message.error(data.msg)
      }
    },
    async tounShowStock (val) {
      // 隐藏股票
      let data = await api.updateStockShow({ stockId: val })
      if (data.status === 0) {
        await this.getList()
        this.$message.success('隐藏股票成功！')
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

  .table {
    .show {
      border: 1px solid #2196F3;
      margin-right: 8px;
      padding: 0 2px;
      font-size: 12px;
      border-radius: 4px;
      color: #2196F3;

      &.red {
        border-color: #ff0000;
        color: #ff0000;
      }
    }
  }
</style>
