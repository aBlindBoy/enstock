<template>
  <div>
    <el-card class="box-card">
      <!-- <el-form :inline="true" :model="form" class="demo-form-inline" size="small">
        <el-form-item label="关键字查询">
          <el-input v-model="form.code" placeholder="代码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form> -->
      <div class="table">
        <div class="table-top-btn">
          <el-row class="text-right">
            <el-button type="primary" size="small" @click="editAgent({id:0})" plain>添加杠杆</el-button>
          </el-row>
        </div>
        <el-table
          v-loading="loading"
          :data="list.list"
          style="width: 100%">
          <el-table-column
            prop="agentName"
            label="id">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.id}}</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="cycleType"
            label="周期类型">
            <template slot-scope="scope">
              <p>
                <span v-if="scope.row.cycle_type==2">按周配资</span>
                <span v-if="scope.row.cycle_type==3">按月配资</span>
                <span v-else>按天配资</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="lever"
            label="杠杆">
            <template slot-scope="scope">
              <p>
                <span style="font-size:12px;color:#959595;">{{scope.row.lever}}倍</span>
              </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="manageRate"
            label="管理费率">
          </el-table-column>
          <el-table-column
            prop="singleHoldingRatio"
            label="单股持仓比例">
          </el-table-column>
          <el-table-column
            prop="status"
            label="状态">
            <template slot-scope="scope">
            <p class="bounceIn">
              <span v-if="scope.row.status==1" class="green">启用</span>
              <span v-if="scope.row.status==0" class="red">停用</span>
            </p>
            </template>
          </el-table-column>
          <el-table-column
            prop="addTime"
            label="添加时间">
            <template slot-scope="scope">{{scope.row.addTime | timeFormat}}</template>
          </el-table-column>
          <el-table-column
            fixed="right"
            width="100"
            label="操作">
            <template slot-scope="scope">
              <el-button type="primary" title="修改" size="small" @click="editAgent(scope.row)">修改</el-button>
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
    <editInfoDialog :info='detail' :getDate='getFundsLeverList' ref="editInfoDialog"></editInfoDialog> 
  </div>

</template>

<script>
import * as api from '@/axios/api'
import editInfoDialog from './lever-edit'

export default {
  components: {
    editInfoDialog
  },
  props: {},
  data () {
    return {
      form: {
        typeName: '',
        pageNum: 1,
        pageSize: 100
      },
      list: {
        list: []
      },
      loading: false, // 表格加载
      detail: {} // 用户信息
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {
    this.getFundsLeverList()
  },
  methods: {
    onSubmit () {
      // 查询表格
      this.getFundsLeverList()
    },
    handleSizeChange (val) {
      this.form.pageSize = val
      this.getFundsLeverList()
    },
    handleCurrentChange (val) {
      this.form.pageNum = val
      this.getFundsLeverList()
    },
 
    async getFundsLeverList () {
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        typeName: this.form.typeName
      }
      let data = await api.getFundsLeverList(opts)
      if (data.status === 0) {
        this.list = data.data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    
    editAgent (row) {
      // 修改
      this.detail = row
      this.$refs.editInfoDialog.dialogVisible = true
    }
  }
}
</script>
