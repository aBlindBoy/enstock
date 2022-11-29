<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>产品配置</span>
      </div>
      <div class="text item">
        <div class="explain red">
          <p>1、
            <el-tag size="mini" type="danger">融资融券</el-tag>
            为本系统基础交易，其状态必须为开启状态！
          </p>
          <p>2、请在系统上线前确认产品需求，如需关闭某个产品显示配置，请确认该产品所有用户的持仓和资金都已清空。关闭产品配置状态，系统不会修改用户的资金和持仓！</p>
          <p>3、该配置只针对实盘用户，模拟用户可以看到所有产品。</p>
        </div>
        <el-form :inline="true" class="demo-form-inline" size="small" ref="logoForm">
          <div class="title">
            A股
            <span class="sub-tit">
                            状态为开启即表示用户可以进行该产品的交易
                        </span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="融资融券交易">
                <el-switch v-model="settingForm.stockDisplay"></el-switch>
                {{settingForm.stockDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="科创板交易">
                <el-switch v-model="settingForm.kcStockDisplay"></el-switch>
                {{settingForm.kcStockDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="指数交易">
                <el-switch v-model="settingForm.indexDisplay"></el-switch>
                {{settingForm.indexDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
          </el-row>
          <div class="title">
            期货
            <span class="sub-tit">
                            状态为开启即表示用户可以进行该产品的交易
                        </span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="外盘期货">
                <el-switch v-model="settingForm.futuresDisplay"></el-switch>
                {{settingForm.futuresDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
          </el-row>
          <div class="title">
            用户实名认证
            <span class="sub-tit">
                            状态为开启即表示用户实名认证后可以进行交易，关闭则不需要实名认证就可以正常交易
                        </span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="实名认证">
                <el-switch v-model="settingForm.realNameDisplay"></el-switch>
                {{settingForm.realNameDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
          </el-row>

          <div class="title">
            分仓配资
            <span class="sub-tit">
                            状态为开启即表示分仓配资板块全部可用，关闭则不需要分仓配资
                        </span>
          </div>
          <el-row>
            <el-col :span="8">
              <el-form-item label="分仓配资">
                <el-switch v-model="settingForm.fundsDisplay"></el-switch>
                {{settingForm.fundsDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="续期审核">
                <el-switch v-model="settingForm.delayDisplay"></el-switch>
                {{settingForm.delayDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="扩大配资审核">
                <el-switch v-model="settingForm.expandDisplay"></el-switch>
                {{settingForm.expandDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="追加保证金审核">
                <el-switch v-model="settingForm.marginDisplay"></el-switch>
                {{settingForm.marginDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="终止操盘审核">
                <el-switch v-model="settingForm.endDisplay"></el-switch>
                {{settingForm.endDisplay?'开启':'关闭'}}
              </el-form-item>
            </el-col>
            
          </el-row>
        </el-form>
      </div>
      <el-row class="text-center submit-btn">
        <el-button type="primary" @click="updateProductSetting">保存设置信息</el-button>
      </el-row>
    </el-card>

  </div>

</template>

<script>
import * as api from '@/axios/api'
import DetailDialog from './add-dialog'

export default {
  components: {
    DetailDialog
  },
  props: {},
  data () {
    return {
      settingForm: {
        futuresDisplay: false,
        indexDisplay: false,
        kcStockDisplay: false,
        stockDisplay: true,
        realNameDisplay: true
      }
    }
  },
  watch: {},
  computed: {},
  mounted () {
    this.admin = process.env.API_HOST
    if (this.admin === undefined) {
      this.admin = ''
    }
    this.getProductSetting()
  },
  methods: {
    async getProductSetting () {
      let data = await api.getProductSetting()
      if (data.status === 0) {
        if (data.data) {
          this.settingForm = data.data
        } else {

        }
      } else {
        this.$message.error(data.msg)
      }
    },
    async updateProductSetting () {
      let opts = {
        id: this.settingForm.id,
        futuresDisplay: this.settingForm.futuresDisplay,
        indexDisplay: this.settingForm.indexDisplay,
        kcStockDisplay: this.settingForm.kcStockDisplay,
        stockDisplay: this.settingForm.stockDisplay,
        realNameDisplay: this.settingForm.realNameDisplay,
        fundsDisplay: this.settingForm.fundsDisplay,
        delayDisplay: this.settingForm.delayDisplay,
        expandDisplay: this.settingForm.expandDisplay,
        marginDisplay: this.settingForm.marginDisplay,
        endDisplay: this.settingForm.endDisplay,
      }
      let data = await api.updateProductSetting(opts)
      if (data.status === 0) {
        // logo
        this.$message.success('修改成功')
      } else {
        this.$message.error(data.msg)
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .box-card {
    margin-bottom: 10px;

    .img {
      max-width: 80%;
      background: #eee;
      max-height: 100px;
    }
  }

  .submit-btn {
    margin: 20px 0;

    .el-button {
      width: 200px;
    }
  }

  .item {
    .title {
      border-bottom: 1px solid #eceff6;
      color: #606266;
      margin: 10px 0;
      text-indent: 1px;
      line-height: 35px;

      span {
        color: #999;
        font-size: 12px;
        margin-left: 10px;
      }

      &:before {
        content: "";
        width: 4px;
        height: 4px;
        background: #606266;
        display: inline-block;
        border-radius: 4px;
        margin-right: 5px;
        vertical-align: middle;
      }
    }
  }

  .explain {
    line-height: 25px;
  }
</style>
