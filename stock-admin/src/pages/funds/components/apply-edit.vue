<template>
  <div class="wrapper">
    <el-dialog
      title="配资审核"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="120px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="审核状态" prop="status">
            <el-select clearable filterable v-model="form.status" placeholder="状态">
              <el-option label="未通过" :value="2"></el-option>
              <el-option label="审核通过" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="请选择子账户" prop="subaccountNumber" v-if="form.status == 1">
            <el-select clearable filterable v-model="form.subaccountNumber" placeholder="子账户" >
              <el-option v-for="item in accountList" :key="item.id" :label="item.subaccountNumber" :value="item.subaccountNumber"></el-option>
            </el-select>
            <p>
              <span class="pro red">
                  分配给配资人的证券子账户 
              </span>
            </p>
          </el-form-item>
          <el-row>
            <el-col :span="24">
              <el-form-item label="审核意见" prop="auditOpinion">
                <el-input  type="textarea" rows="6" v-model="form.auditOpinion" placeholder="请输入审核意见" style="width:550px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <div class="text box-content">
            <el-row class="account-box">
              <el-col :span="8">
                <p class="title">配资类型</p>
                <p class="number">
                  <span v-if="form.fundsType==2">按周配资</span>
                  <span v-if="form.fundsType==3">按月配资</span>
                  <span v-else>按天配资</span>
                </p>
              </el-col>
              <el-col :span="8">
                <p class="title">总操盘金额</p>
                <p class="number">￥{{form.totalTradingAmount}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">保证金</p>
                <p class="number">￥{{form.margin}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">配资资金</p>
                <p class="number">￥{{form.fundsAmount}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">配资管理费</p>
                <p class="number">￥{{form.manageFee}}</p>
              </el-col>
            </el-row>
          </div>
        </el-form>
        

        </div>
      <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submit('ruleForm')">提  交</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import * as api from '@/axios/api'

export default {
  components: {},
  props: {
    getDate: {
      type: Function,
      default: function () {
      }
    },
    info: {
      type: Object,
      default: function () {
        return {
          id: 0
        }
      }
    },
    agentList: {
      type: Array,
      default: function () {
      }
    }
  },
  data () {
    return {
      dialogVisible: false,
      form: {
        subaccountNumber: '',
        status: 1,
        auditOpinion: '',
        fundsType: '',
        totalTradingAmount: '',
        margin: '',
        fundsAmount: '',
        manageFee: '',
        tradersCycle: ''
      },
      dealerList: [{
        dealerName: '证券机构',
        id: 0
      }],
      accountList: [{
      }],
      rule: {
        status: [
          { required: true, message: '请选择审核状态', trigger: 'change' }
        ],
        auditOpinion: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val && this.info.id>0) {
        this.form.auditOpinion = this.info.auditOpinion
        this.form.fundsType = this.info.fundsType
        this.form.totalTradingAmount = this.info.totalTradingAmount
        this.form.margin = this.info.margin
        this.form.fundsAmount = this.info.fundsAmount
        this.form.manageFee = this.info.manageFee
        this.form.tradersCycle = this.info.tradersCycle
      }
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getEnabledAccount()
  },
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          if(this.form.status == 1 && this.form.subaccountNumber == ''){
            this.$message.error("请选择子账户")
            return
          }
          let opts = {
            id: this.info.id,
            subaccountNumber: this.form.subaccountNumber,
            status: this.form.status,
            auditOpinion: this.form.auditOpinion,
            tradersCycle: this.form.tradersCycle
          }
          let data = await api.auditApply(opts)
          if (data.status === 0) {
            this.$message.success("操作成功")
            this.dialogVisible = false
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
        } else {
          return false
        }
      })
    },
    async getEnabledAccount () {
      // 查询可用交易账户
      let opts = {
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize,
        keyword: this.form.keyword,
        status: 0
      }
      let data = await api.getTradingAccountList(opts)
      if (data.status === 0) {
        if(data.data.data.list.length>0){
          this.accountList = data.data.data.list
        }
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
