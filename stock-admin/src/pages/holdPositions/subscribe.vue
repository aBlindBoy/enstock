<template>
  <div>
    <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
      <!-- <el-form-item label="类型">
        <el-select filterable v-model="form.positionType" placeholder="持仓单">
          <el-option label="全部" value=""></el-option>
          <el-option label="正式持仓" value="0"></el-option>
          <el-option label="模拟持仓" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="下级代理">
        <el-select filterable clearable v-model="form.agentId" placeholder="下级代理">
          <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="用户Id">
        <el-input v-model="form.userId" placeholder="用户Id"></el-input>
      </el-form-item>
      <!-- <el-form-item label="持仓订单号">
        <el-input v-model="form.positionSn" placeholder="持仓订单号"></el-input>
      </el-form-item> -->
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
          prop="id"
          width="70px"
          label="编号">

        </el-table-column>
        <el-table-column
          prop="userId"
          label="用户编号">
        </el-table-column>
        <el-table-column
          prop="realName"
          width="100px"
          label="真实姓名">
          <template slot-scope="scope">
            <span>{{scope.row.realName}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="submitAmount"
          label="提交金额">
        </el-table-column>
        <el-table-column
          prop="submitSheets"
          label="提交张数">
        </el-table-column>
        <el-table-column
          prop="submitTime"
          label="提交时间"
          width="160px"
          >
          <template slot-scope="scope">
            <span>{{scope.row.submitTime | timeFormat}} </span>
          </template>
        </el-table-column>
       
        <el-table-column
          prop="status"
          label="状态">
          <template slot-scope="scope">
            <el-tag>{{statusType[scope.row.status]}} </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="tradeAmount"
          label="中签金额">
        </el-table-column>
        <el-table-column
          prop="tradeSheets"
          label="中签张数">
        </el-table-column>
        <el-table-column
          prop="drawDate"
          width="110px"
          label="抽签日期">
        </el-table-column>
        <el-table-column
          prop="stockCode"
          label="股票代号">
        </el-table-column>
        <el-table-column
          prop="stockName"
          label="股票名称">
        </el-table-column>
        <el-table-column
          prop="stockPlate"
          label="发行市场">
        </el-table-column>
        <el-table-column
          prop="subscriptionTime"
          label="申购时间">
        </el-table-column>
        <el-table-column
          prop="ticketingDate"
          label="发券日期">
        </el-table-column>
        <el-table-column
          prop="underwritingSheet"
          label="承销张数">
        </el-table-column>
        <el-table-column
          prop="underwritingPrice"
          label="承銷价格">
        </el-table-column>
        <el-table-column
          prop="marketPrice"
          label="市价">
        </el-table-column>
        <el-table-column
          prop="remarks"
          label="备注">
        </el-table-column>
     
        <el-table-column
          fixed="right"
          prop="isLock"
          width="180px"
          label="操作">
          <template slot-scope="scope">
            <!-- <el-button v-if="scope.row.isLock == 0" type="primary" plain size="small" @click="positionLock(scope.row)">
              锁仓
            </el-button> -->
            <el-button  type="primary" plain size="small"
                       @click="showDialog(scope.row)">修改张数
            </el-button>
            <!-- <el-button type="danger" plain size="small" @click="toSell(scope.row)">强制平仓</el-button> -->
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


    <el-dialog
      title="新股申购"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <el-row>
              <el-col :span="12">
                <span>
                  订单编号 {{dialogForm.id}}
                </span>
                <!-- <span>
                    （{{info?info.sellOrderId?'平仓':'持仓中':'-'}}）
                </span> -->
              </el-col>
              <!-- <el-col v-if="info && info.allProfitAndLose" :span="12" class="text-right">
                总盈亏：
                <span v-if="info.now_price == 0 && !info.sellOrderPrice">
                    -
                </span>
                <span v-else :class="info.allProfitAndLose>0?'red number':'green number'">{{info?info.allProfitAndLose:'-'}}</span>
              </el-col> -->
            </el-row>
          </div>
          <div class="text box-content">
            <h2><b>订单信息</b></h2>
            <el-row>
              <el-col  :span="8">
                用户编号： <span>{{dialogForm.userId}}</span>
              </el-col>
              <el-col :span="8" >
                真实姓名: <span>{{dialogForm.realName}}</span>
              </el-col>
              <el-col  :span="8">
                提交金额: <span>{{dialogForm.submitAmount}}</span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                提交张数: <span>{{dialogForm.submitSheets}}</span>
              </el-col>
              <el-col  :span="8">
                提交时间:  <span>{{dialogForm.submitTime | timeFormat}} </span>
              </el-col>
          
            </el-row>

            <h2 style="margin-top:50px">股票信息</h2>
            <el-row>
              <el-col :span="8">
                抽签日期:  <span>{{dialogForm.drawDate}} </span>
              </el-col>
              <el-col  :span="8">
                股票代号:  <span>{{dialogForm.stockCode}} </span>
              </el-col>
              <el-col  :span="8">
                股票名称:  <span>{{dialogForm.stockName}} </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                发行市场:  <span>{{dialogForm.stockPlate}} </span>
              </el-col>
              <el-col  :span="8" >
                申购时间:  <span>{{dialogForm.subscriptionTime}} </span>
              </el-col>
              <el-col :span="8" >
                发券日期:  <span>{{dialogForm.ticketingDate}} </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                承销张数:  <span>{{dialogForm.underwritingSheet}} </span>
              </el-col>
              <el-col :span="8">
                承銷价格:  <span>{{dialogForm.underwritingPrice}} </span>
              </el-col>
              <el-col :span="8">
                市价:  <span>{{dialogForm.marketPrice}} </span>
              </el-col>
            </el-row>
            
            <h2 style="margin-top:50px;;">修改订单</h2>
            <el-row>
            <el-col :span="8">
                <!-- 状态：1、预约成功，2、已中签，3、未中签，4：部分中签 -->
                <el-select  v-model="dialogForm.status" placeholder="状态">
                  <el-option label="预约成功" :value="1"></el-option>
                  <el-option label="已中签" :value="2"></el-option>
                  <el-option label="未中签" :value="3"></el-option>
                  <!-- <el-option label="部分中签" :value="4"></el-option> -->
                </el-select>
              </el-col>

              <el-col :span="8">
                  <el-input-number v-model="dialogForm.tradeSheets"
                     :min="1" :max="dialogForm.submitSheets" label="中签张数"></el-input-number>
              </el-col>
            </el-row>
          </div>
          <div style="line-height:100px">
              &nbsp;
          </div>
        </el-card>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="updateSubscribe()">确 定</el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {
  },
  props: {
    type: {
      type: Number,
      default: 1
    }
  },
  data () {
    return {
      loading: false,
      form: {
        positionType: '',
        state: '0',
        userId: '',
        agentId: '',
        positionSn: '',
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
      // detail: null, // 详情数据
      timer: null,
      refresh: false, // 刷新中
      changeTextClass: {}, // 表格中的数据变化
      dialogVisible:false,
      dialogForm:{},
      statusType:["","预约成功","已中签","未中签"],//,"部分中签"
    }
  },
  watch: {},
  computed: {},
  created () {
  },
  beforeDestroy () {
    // clearInterval(this.timer)
  },
  mounted () {
    this.getList()
  },
  methods: {
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getList()
      console.log(`每页 ${val} 条`)
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      console.log(`当前页: ${val}`)
      this.getList()
    },
    async refreshList () {
      if (this.refresh || this.loading) {
        return
      }
      this.refresh = true
      this.changeTextClass = {}
      // 获取表格数据
      let opts = {
        positionType: this.form.positionType, // 正式 0 模拟 1
        state: this.form.state, // 持仓 0 平仓 1
        userId: this.form.userId,
        agentId: this.form.agentId,
        positionSn: this.form.positionSn,
        beginTime: this.form.time ? this.form.time[0] : '',
        endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      let data = await api.indexPositionList(opts)
      this.refresh = false
      if (data.status === 0) {
        data.data.list.forEach((element, i) => {
          this.changeTextClass[i] = ''
          if (data.data.list[i].now_price !== this.list.list[i].now_price) {
            // this.changeTextClass = true
            this.changeTextClass[i] = true // 设置对应的动画过滤
            this.list.list[i].now_price = data.data.list[i].now_price
            // this.list.list[i].now_price = data.data.list[i].now_price
            this.list.list[i].profitAndLose = data.data.list[i].profitAndLose
            this.list.list[i].allProfitAndLose = data.data.list[i].allProfitAndLose
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
    async getList () {
      // 获取表格数据
      let opts = {
        // positionType: this.form.positionType, // 正式 0 模拟 1
        // state: this.form.state, // 持仓 0 平仓 1
        keyword: this.form.userId,
        // agentId: this.form.agentId,
        // positionSn: this.form.positionSn,
        // beginTime: this.form.time ? this.form.time[0] : '',
        // endTime: this.form.time ? this.form.time[1] : '',
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      }
      this.loading = true
      let data = await api.getStockSubscribeList(opts)
      this.loading = false
      if (data.status === 0) {
        this.list = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    showDialog (row){
      this.dialogForm = row
      this.dialogForm.tradeSheets = 0
      this.dialogVisible = true
    },
    async updateSubscribe(){
        let params = {
          tradeAmount: this.dialogForm.tradeSheets * this.dialogForm.underwritingPrice * 1000,
          tradeSheets: this.dialogForm.tradeSheets,
          id: this.dialogForm.id,
          status:this.dialogForm.status,
        }
        let data = await api.updateStockSubscribe(params)
        debugger
        if (data.status === 0) {
          this.$message.info(data.data)
          this.dialogVisible = false
          this.getList()
        } else {
          this.$message.error(data.data)
        }
      
    }
  }
}
</script>
