<template>
  <div class="wrapper">
    <el-dialog
      title="券商机构"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="120px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="券商ID" prop="dealerNumber">
            <el-input v-model="form.dealerNumber" placeholder="券商ID,如:27代表银河证券"></el-input>
          </el-form-item>
          <el-form-item label="券商名称" prop="dealerName">
            <el-input v-model="form.dealerName" placeholder="券商名称,如:银河证券"></el-input>
          </el-form-item>
          <el-form-item label="客户端版本号" prop="clientVersionNumber">
            <el-input v-model="form.clientVersionNumber" placeholder="客户端版本号"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select clearable filterable v-model="form.status" placeholder="状态">
              <el-option label="启用" :value="1"></el-option>
              <el-option label="停用" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-row>
            <el-col :span="24">
              <el-form-item label="备注" prop="remarks">
                <el-input  type="textarea" rows="6" v-model="form.remarks" placeholder="备注" style="width:550px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
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
        dealerNumber: '',
        dealerName: '',
        clientVersionNumber: '',
        remarks: '',
        status: ''
      },
      rule: {
        dealerNumber: [
          { required: false, message: '请输入券商ID', trigger: 'blur' }
        ],
        dealerName: [
          { required: true, message: '请输入券商名称', trigger: 'blur' }
        ],
        clientVersionNumber: [
          { required: true, message: '请输入客户端版本号', trigger: 'blur' }
        ],
        remarks: [
          { required: true, message: '请输入备注', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val && this.info.id>0) {
        this.form.dealerNumber = this.info.dealerNumber
        this.form.dealerName = this.info.dealerName
        this.form.clientVersionNumber = this.info.clientVersionNumber
        this.form.remarks = this.info.remarks
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
            dealerNumber: this.form.dealerNumber,
            dealerName: this.form.dealerName,
            clientVersionNumber: this.form.clientVersionNumber,
            remarks: this.form.remarks,
            status: this.form.status
          }
          let data = await api.saveFundsDealerInstitutions(opts)
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
