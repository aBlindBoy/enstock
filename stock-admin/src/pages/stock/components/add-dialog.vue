<template>
  <div class="wrapper">
    <el-dialog
      title="添加股票"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" :model="form" ref="ruleForm" size="mini" label-width="100px" :rules="rule">
          <el-form-item label="股票名称" prop="stockName">
            <el-input v-model="form.stockName" placeholder="股票名称"></el-input>
          </el-form-item>
          <el-form-item label="股票代码" prop="stockCode">
            <el-input v-model="form.stockCode" placeholder="股票代码"></el-input>
          </el-form-item>
          <el-form-item label="股票类型" prop="stockType">
            <el-select v-model="form.stockType" placeholder="股票类型">
              <el-option v-for="i in stockTypeList" :key="i.key" :label="i.type" :value="i.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="科创板股票" prop="stockPlate">
            <el-select v-model="form.stockPlate" placeholder="是否为科创板股票">
              <el-option label="否" value=""></el-option>
              <el-option label="是" value="科创"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="锁定状态" prop="isLock">
            <el-select v-model="form.isLock" placeholder="锁定状态">
              <el-option label="未锁定" value="0"></el-option>
              <el-option label="锁定" value="1"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="显示状态" prop="isShow">
            <el-select v-model="form.isShow" placeholder="显示状态">
              <el-option label="显示" value="0"></el-option>
              <el-option label="不显示" value="1"></el-option>
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
    }
  },
  data () {
    return {
      dialogVisible: false,
      stockTypeList: [
        { type: '沪股', value: 'sh' },
        { type: '深股', value: 'sz' }
      ],
      form: {
        stockName: '',
        stockCode: '',
        stockType: 'sh',
        isLock: '0',
        isShow: '0',
        stockPlate: ''
      },
      rule: {
        stockName: [
          { required: true, message: '请输入股票名称', trigger: 'blur' }
        ],
        // stockPlate: [
        //     { required: true, message: '是否为科创板', trigger: 'change' },
        // ],
        stockCode: [
          { required: true, message: '请输入股票代码', trigger: 'blur' },
          { min: 6, max: 6, message: '请输入正确的股票代码', trigger: 'blur' }
        ],
        stockType: [
          { required: true, message: '股票类型', trigger: 'change' }
        ],
        isLock: [
          { required: true, message: '是否锁定', trigger: 'change' }
        ],
        isShow: [
          { required: true, message: '是否显示', trigger: 'change' }
        ]
      }
    }
  },
  watch: {},
  computed: {},
  created () {},
  mounted () {},
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let opts = {
            stockCode: this.form.stockCode,
            stockName: this.form.stockName,
            isLock: this.form.isLock,
            isShow: this.form.isShow,
            stockType: this.form.stockType,
            stockPlate: this.form.stockPlate // 默认不传，科创板传入科创板
          }
          let data = await api.addStock(opts)
          if (data.status === 0) {
            this.$message.success('添加成功')
            this.clearForm()
            this.getDate()
          } else {
            this.$message.error(data.msg)
          }
          this.dialogVisible = false
        } else {
          return false
        }
      })
    },
    clearForm () {
      this.form = {
        stockName: '',
        stockCode: '',
        stockType: '',
        isLock: '0',
        isShow: '0',
        stockPlate: ''
      }
    }
  }
}
</script>
<style lang="less" scoped>
</style>
