<template>
  <div class="wrapper">
    <el-dialog
      title="修改点差"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="100px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="类型">
            <el-select clearable filterable v-model="form.typeName" placeholder="点差类型">
              <el-option label="涨跌幅" value="涨跌幅"></el-option>
              <el-option label="成交额" value="成交额"></el-option>
              <el-option label="股票价格" value="股票价格"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="单位" prop="unit">
            <el-input v-model="form.unit" placeholder="单位"></el-input>
          </el-form-item>
          <el-form-item label="开始区间" prop="startInterval">
            <el-input v-model="form.startInterval" placeholder="开始区间"></el-input>
          </el-form-item>
          <el-form-item label="结束区间" prop="endInterval">
            <el-input v-model="form.endInterval" placeholder="结束区间"></el-input>
          </el-form-item>
          <el-form-item label="点差费率" prop="spreadRate">
            <el-input v-model="form.spreadRate" placeholder="点差费率"></el-input>
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
          id: ''
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
        typeName: '',
        unit: '',
        startInterval: '',
        endInterval: '',
        spreadRate: ''
      },
      rule: {
        typeName: [
          { required: false, message: '请选择点差类型', trigger: 'change' }
        ],
        unit: [
          { required: true, message: '请输入单位', trigger: 'blur' }
        ],
        startInterval: [
          { required: true, message: '请输入开始区间', trigger: 'blur' }
        ],
        endInterval: [
          { required: true, message: '请输入结束区间', trigger: 'blur' }
        ],
        spreadRate: [
          { required: true, message: '请输入点差费率', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.typeName = this.info.typeName
        this.form.unit = this.info.unit
        this.form.startInterval = this.info.startInterval
        this.form.endInterval = this.info.endInterval
        this.form.spreadRate = this.info.spreadRate
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
            typeName: this.form.typeName,
            unit: this.form.unit,
            startInterval: this.form.startInterval,
            endInterval: this.form.endInterval,
            spreadRate: this.form.spreadRate
          }
          let data = await api.updateSiteSpread(opts)
          if (data.status === 0) {
            this.$message.success("操作成功")
            this.dialogVisible = false
            this.getDate()
          } else {
            alert(2)
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
