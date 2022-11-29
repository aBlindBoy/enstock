<template>
  <div
    class="nav-left" 
    :unique-opened='false' 
    :style="contentStyleObj"
  >
    <el-scrollbar style="height: 100%">
        <el-menu 
            :default-openeds="index" 
            :default-active="itemMenu" 
            :collapse="isCollapse"
            background-color="#1f2636"
            text-color="rgb(191, 203, 217)"
            active-text-color="#409eff"
        >
            <el-menu-item index="1" @click="$router.push('/home')">
                <i class="el-icon-share"></i>
                <span slot="title">代理中心</span>
            </el-menu-item>
            <el-submenu index="2">
                <template slot="title">
                    <i class="el-icon-goods"></i>
                    <span slot="title">数据统计</span>
                </template>
                <el-menu-item-group>
                    <router-link to="/statistics?type=1">
                        <el-menu-item index="2-1">股票统计</el-menu-item>
                    </router-link>
                    <router-link to="/statistics?type=2">
                        <el-menu-item index="2-2">指数统计</el-menu-item>
                    </router-link>
                    <router-link to="/statistics?type=3">
                        <el-menu-item index="2-3">期货统计</el-menu-item>
                    </router-link>
                </el-menu-item-group>
            </el-submenu>
            <el-menu-item index="3" @click="$router.push('/userMan')">
                <i class="el-icon-user"></i>
                <span slot="title">用户管理</span>
            </el-menu-item>
            <el-menu-item index="4" @click="$router.push('/agent')">
                <i class="el-icon-help"></i>
                <span slot="title">代理管理</span>
            </el-menu-item>
            <el-menu-item index="5" @click="$router.push('/agentcyFee')">
                <i class="el-icon-edit-outline"></i>
                <span slot="title">利润明细</span>
            </el-menu-item>
            <el-submenu index="6">
                <template slot="title">
                    <i class="el-icon-folder-opened"></i>
                    <span slot="title">持仓管理</span>
                </template>
                <el-menu-item-group>
                    <router-link to="/holdPositions?type=1">
                        <el-menu-item index="6-1">融资持仓单</el-menu-item>
                    </router-link>
                    <router-link to="/holdPositions?type=2">
                        <el-menu-item index="6-2">融资平仓单</el-menu-item>
                    </router-link>
                    <router-link to="/holdPositions?type=3">
                        <el-menu-item index="6-3">指数持仓单</el-menu-item>
                    </router-link>
                    <router-link to="/holdPositions?type=4">
                        <el-menu-item index="6-4">指数平仓单</el-menu-item>
                    </router-link>
                    <router-link to="/holdPositions?type=5">
                        <el-menu-item index="6-5">期货持仓单</el-menu-item>
                    </router-link>
                    <router-link to="/holdPositions?type=6">
                        <el-menu-item index="6-6">期货平仓单</el-menu-item>
                    </router-link>
                </el-menu-item-group>
            </el-submenu>
            <el-menu-item index="7" @click="$router.push('/capitalDetail')">
                <i class="el-icon-tickets"></i>
                <span slot="title">资金明细</span>
            </el-menu-item>
            <el-menu-item index="8" @click="$router.push('/entry')">
                <i class="el-icon-view"></i>
                <span slot="title">入金记录</span>
            </el-menu-item>
            <el-menu-item index="9" @click="$router.push('/exit')">
                <i class="el-icon-takeaway-box"></i>
                <span slot="title">
                    <el-badge :value="outMoneyOrder" class="item-mark">
                    出金记录
                    </el-badge>
                </span>
            </el-menu-item>
        </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import * as api from '@/axios/api'
import { Toast } from 'mint-ui'
export default {
    name: "NavMenu",
    components: {},
    props: {
        index: {
            type: Array
        },
        itemMenu: {
            type: String
        },
        isCollapse: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            contentStyleObj: {
                height: ''
            },
            outMoneyOrder: 0, // 出金待审核金额
        };
    },
    created () {
        window.addEventListener('resize', this.getHeight)
        this.getHeight()
        this.getOutMoneyOrderNum()
    },
    methods: {
        getHeight () {
            this.contentStyleObj.height = window.innerHeight - 62 + 'px'
        },
        async getOutMoneyOrderNum () {
          // 获取出金订单未处理的条数
          let opts = {
            state: 0,
            pageNum: 1,
            pageSize: 15
          }
          let data = await api.getUserwithdrawList(opts)
          if (data.status === 0) {
            // 出金待审核金额
            this.outMoneyOrder = data.data.total
          } else {
            this.$message.error(data.msg)
          }
        },
    },
};
</script>
<style lang="less" scoped>
  .logo-img {
    padding: 10px 0;

    img {
      max-width: 80%;
      max-height: 50px;
    }
  }

  .nav-left {
    overflow: hidden;
    min-height: 500px;
    background-color: #1f2636;
    height: calc(100% - 60px);
    .iconfont {
      vertical-align: middle;
      margin-right: 8px;
      color: #fff;
    }
    /deep/.el-submenu__title{
      padding: 0 10px;
    }
  }

  .item-mark {
    /deep/ .el-badge__content.is-fixed {
      top: 19px;
      left: 35px;
      right: auto;
      border: 0;
    }
  }
</style>
