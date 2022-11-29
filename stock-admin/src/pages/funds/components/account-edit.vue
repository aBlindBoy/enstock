<template>
  <div class="wrapper">
    <el-dialog
      title="交易账户"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="120px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="证券机构" prop="dealerInstitutionsName">
            <el-select clearable filterable v-model="form.dealerInstitutionsName" placeholder="证券机构" value-key="id"  @change="currentSel">
              <el-option v-for="item in dealerList" :key="item.dealerInstitutionsName" :label="item.dealerInstitutionsName" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="证券账户名" prop="accountName">
            <el-select clearable filterable v-model="form.accountName" placeholder="证券账户名" >
              <el-option v-for="item in accountList" :key="item.accountName" :label="item.accountName" :value="item.accountName"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="子账户名称" prop="subaccountNumber">
            <el-input v-model="form.subaccountNumber" readonly="readonly" placeholder="子账户名称"></el-input>
            <p>
              <span class="pro red">
                  系统自动生成不允许修改
              </span>
            </p>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select clearable filterable v-model="form.status" placeholder="状态">
              <el-option label="已用" :value="1"></el-option>
              <el-option label="未用" :value="0"></el-option>
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
        subaccountNumber: '',
        dealerInstitutionsId: '',
        tmepdealerInstitutionsName: '',
        dealerInstitutionsName: '',
        accountName: '',
        status: 0,
        remarks: ''
      },
      dealerList: [{
        dealerName: '证券机构',
        id: 0
      }],
      accountList: [{
        accountName: ''
      }],
      rule: {
        dealerInstitutionsName: [
          { required: true, message: '请选择证券机构', trigger: 'blur' }
        ],
        accountName: [
          { required: true, message: '请选择证券账户', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val && this.info.id>0) {
        this.form.dealerInstitutionsId = this.info.dealerInstitutionsId
        this.form.dealerInstitutionsName = this.info.dealerInstitutionsName
        this.form.accountName = this.info.accountName
        this.form.subaccountNumber = this.info.subaccountNumber
        this.form.status = this.info.status
        this.form.remarks = this.info.remarks
        this.form.tmepdealerInstitutionsName = this.info.dealerInstitutionsName
      }
    }
  },
  computed: {},
  created () {},
  mounted () {
    this.getDealerList()
    this.getMaxNumber()
  },
  methods: {
    submit (formName) {
      // 提交
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          
          let opts = {
            id: this.info.id,
            dealerInstitutionsId: this.form.dealerInstitutionsId,
            dealerInstitutionsName: this.form.tmepdealerInstitutionsName,
            accountName: this.form.accountName,
            subaccountNumber: this.form.subaccountNumber,
            status: this.form.status,
            remarks: this.form.remarks
          }
          console.log(opts)
          let data = await api.saveTradingAccount(opts)
          if (data.status === 0) {
            this.$message.success("操作成功")
            this.getMaxNumber()
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
    async getDealerList () {
      // 获取可用券商
      let opts = {}
      let data = await api.getSecuritiesEnabledList(opts)
      if (data.status === 0) {
        this.dealerList = data.data.data.list
      } else {
        this.$message.error(data.msg)
      }
    },
    async getMaxNumber () {
      // 查询最新交易账户编号
      let opts = {}
      let data = await api.getMaxNumber(opts)
      if (data.status === 0) {
        this.form.subaccountNumber = data.data
      } else {
        this.$message.error(data.msg)
      }
    },
    currentSel(selVal) {
      this.form.dealerInstitutionsId = selVal.id
      this.form.tmepdealerInstitutionsName = selVal.dealerInstitutionsName
      this.accountList = [{accountName: selVal.accountName}]
      // console.log("选择的name为：" + this.form.tmepdealerInstitutionsName, "选择的id为:" + this.form.dealerInstitutionsId+",selval="+selVal);
    }
  }
}
</script>
<style lang="less" scoped>
</style>
