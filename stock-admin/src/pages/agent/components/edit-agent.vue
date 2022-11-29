<template>
  <div class="wrapper">
    <el-dialog
      title="修改代理"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <div>
        <el-form :inline="true" label-width="100px" :model="form" ref="ruleForm" :rules="rule" class="demo-form-inline">
          <el-form-item label="代理id" prop="id">
            <el-input disabled v-model="info.id" placeholder="代理id"></el-input>
          </el-form-item>
          <!-- <el-form-item label="上级代理" prop="parentId">
            <el-select v-model="form.parentId" placeholder="上级代理">
              <el-option label="总代理" value=""></el-option>
              <el-option v-for="i in agentList" :key="i.key" :label="i.agentName" :value="i.id"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item label="代理名" prop="agentName">
            <el-input v-model="form.agentName" placeholder="代理名"></el-input>
          </el-form-item>
          <el-form-item label="代理手机" prop="agentPhone">
            <el-input v-model="form.agentPhone" placeholder="代理手机"></el-input>
          </el-form-item>
          <el-form-item label="真实姓名" prop="agentRealName">
            <el-input v-model="form.agentRealName" placeholder="真实姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="agentPwd">
            <el-input v-model="form.agentPwd" placeholder="密码"></el-input>
          </el-form-item>
          <el-form-item label="代理状态" prop="isLock">
            <el-select v-model="form.isLock" placeholder="代理状态">
              <el-option label="锁定" :value="1"></el-option>
              <el-option label="正常" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="杠杆倍数（例:100/50）" prop="siteLever">
            <el-input v-model="form.siteLever" placeholder="杠杆倍数,多个用/分割"></el-input>
          </el-form-item>
          <!-- <el-form-item label="上级代理" prop="parentId">
             <el-input v-model="form.parentId" placeholder="上级代理"></el-input>
         </el-form-item> -->
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
        agentName: '',
        agentPwd: '',
        agentPhone: '',
        agentRealName: '',
        parentId: '',
        id: '',
        isLock: '', // 锁定状态
        siteLever: ''
      },
      rule: {
        id: [
          { required: false, message: '请输入代理id', trigger: 'blur' }
        ],
        parentId: [
          { required: false, message: '请输入上级代理id', trigger: 'blur' }
        ],
        isLock: [
          { required: true, message: '请输入锁定状态', trigger: 'change' }
        ],
        // agentName: [
        //     { required: true, message: '请输入代理名称', trigger: 'blur' },
        //     { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        // ],
        // agentPwd: [
        //     { required: true, message: '请输入密码', trigger: 'blur' },
        //     { min: 6, message: '密码最少为6位', trigger: 'blur' }
        // ],
        agentPhone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { min: 11, max: 11, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        agentRealName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    info (val) {
      if (val) {
        this.form.agentName = this.info.agentName
        this.form.agentPhone = this.info.agentPhone
        this.form.agentRealName = this.info.agentRealName
        this.form.parentId = this.info.parentId
        // this.form.agentPwd = this.info.agentPwd
        this.form.isLock = this.info.isLock
        this.form.siteLever = this.info.siteLever
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
            parentId: this.form.parentId,
            agentPhone: this.form.agentPhone,
            agentRealName: this.form.agentRealName,
            isLock: this.form.isLock,
            siteLever: this.form.siteLever
          }
          if (this.form.agentPwd) {
            opts.agentPwd = this.form.agentPwd
          }
          let data = await api.updateAgent(opts)
          if (data.status === 0) {
            this.$message.success(data.msg)
            this.getDate()
            this.dialogVisible = false
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
