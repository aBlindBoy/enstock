<template>
  <div class="wrapper">
    <el-dialog
      :title="auditTitle"
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

          <div class="text box-content">
            <el-row class="account-box">
              <el-col :span="8">
                <p class="title">用户应交总费用</p>
                <p class="number">
                  ￥{{model.payAmount}}
                </p>
              </el-col>
              <el-col :span="8">
                <p class="title">审核前配资金额</p>
                <p class="number">￥{{model.fundsAmount}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">审核后配资金额</p>
                <p class="number">￥{{model.fundsAmount}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">审核前操盘总金额</p>
                <p class="number">￥{{model.totalTradingAmount}}</p>
              </el-col>
              <el-col :span="8">
                <p class="title">审核后操盘总金额</p>
                <p class="number">￥{{model.totalTradingAmount}}</p>
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
      model: {},
      form: {
        status: 1
      },
      auditTitle: '',
      rule: {
        status: [
          { required: true, message: '请选择审核状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if(this.info.appendType == 2){
        this.auditTitle = '追加保证金审核'
      } else if(this.info.appendType == 3){
        this.auditTitle = '续期审核'
      } else if(this.info.appendType == 4){
        this.auditTitle = '终止操盘审核'
      } else{
        this.auditTitle = '扩大配资审核'
      }
      this.getModel()
    }
  },
  computed: {},
  created () {},
  mounted () {
    
  },
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            id: this.info.id,
            applyId: this.info.applyId,
            status: this.form.status
          }
          let data = await api.saveAppendApply(opts)
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
    async getModel () {
      // 查询可用交易账户
      let opts = {
        id: this.info.id
      }
      let data = await api.getAppendDetail(opts)
      if (data.status === 0) {
        this.model = data.data
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
