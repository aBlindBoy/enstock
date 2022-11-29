<template>
  <div class="wrapper">
    <el-dialog
      title="配置杠杆"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="110px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="配置周期类型" prop="cycleType">
            <el-select clearable filterable v-model="form.cycleType" placeholder="配置周期类型">
              <el-option label="按天配置" :value="1"></el-option>
              <!-- <el-option label="按周配置" value="按周配置"></el-option>
              <el-option label="按月配置" value="按月配置"></el-option> -->
            </el-select>
          </el-form-item>
          <el-form-item label="杠杆" prop="lever">
            <el-select clearable filterable v-model="form.lever" placeholder="杠杆">
              <el-option label="1multiple" :value="1"></el-option>
              <el-option label="2multiple" :value="2"></el-option>
              <el-option label="3multiple" :value="3"></el-option>
              <el-option label="4multiple" :value="4"></el-option>
              <el-option label="5multiple" :value="5"></el-option>
              <el-option label="6multiple" :value="6"></el-option>
              <el-option label="7multiple" :value="7"></el-option>
              <el-option label="8multiple" :value="8"></el-option>
              <el-option label="9multiple" :value="9"></el-option>
              <el-option label="10multiple" :value="10"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="管理费率" prop="manageRate">
            <el-input v-model="form.manageRate" placeholder="管理费率%,如：0.25"></el-input>
          </el-form-item>
          <el-form-item label="单股持仓比例" prop="singleHoldingRatio">
            <el-input v-model="form.singleHoldingRatio" placeholder="单股持仓比例,如：0.85"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select clearable filterable v-model="form.status" placeholder="状态">
              <el-option label="启用" :value="1"></el-option>
              <el-option label="停用" :value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
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
        cycleType: '',
        lever: '',
        manageRate: '',
        singleHoldingRatio: '',
        status: ''
      },
      rule: {
        cycleType: [
          { required: true, message: '请选择配资周期类型', trigger: 'change' }
        ],
        lever: [
          { required: true, message: '请选择杠杆', trigger: 'blur' }
        ],
        manageRate: [
          { required: true, message: '请输入管理费率', trigger: 'blur' }
        ],
        singleHoldingRatio: [
          { required: true, message: '请输入单股持仓比例', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.cycleType = this.info.cycleType
        this.form.lever = this.info.lever
        this.form.manageRate = this.info.manageRate
        this.form.singleHoldingRatio = this.info.singleHoldingRatio
        this.form.status = this.info.status
      }
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
            cycleType: this.form.cycleType,
            lever: this.form.lever,
            manageRate: this.form.manageRate,
            singleHoldingRatio: this.form.singleHoldingRatio,
            status: this.form.status
          }
          let data = await api.saveFundsLever(opts)
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
    }
  }
}
</script>
<style lang="less" scoped>
</style>
