<template>
  <el-container class="user-center">
    <el-header class="user-header">
      <home-header></home-header>
    </el-header>

    <div class="usercot">
      <el-container class="main-wrapper">
        <el-aside width="178px">
          <menu-box></menu-box>
        </el-aside>
        <el-main style=" min-height: calc(100vh - 150px );">
          <div class="wrapper">
            <div class="header-chi">
              <div class="user-center-title">
                <img src="../../../../assets/image/sanjao.png" alt />
                <span>Recharge</span>
              </div>
            </div>
          
            <el-tabs v-model="activeName">
              <account-box></account-box>
            </el-tabs>
          </div>

          <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
        </el-main>
      </el-container>
    </div>
  </el-container>
</template>

<script>
import HomeHeader from "../../../../components/HeaderOrder";
import AccountBox from "./pay1";
import MenuBox from "../menu";
import * as api from "../../../../axios/api";

export default {
  components: {
    HomeHeader,
    AccountBox,
    MenuBox,
  },
  props: {},
  data() {
    return {
      timer: null,
      activeName: "first",
      form: {
        stock: "",
        pageNum: 1,
        pageSize: 15,
      },
      list: {
        list: [],
      },
      loading: false,
      refresh: false, // 刷新中
      changeTextClass: {}, // 錶格中的數據變化
    };
  },
  watch: {},
  computed: {},
  created() {
    this.$store.state.activeIndex = "user";
  },
  beforeDestroy() {},
  mounted() {
    this.getUserInfo();
    this.$store.state.userMenu = "2-8";
  },
  methods: {
    async getUserInfo() {
      // 獲取用戶信息
      let data = await api.getUserInfo();
      if (data.status === 0) {
        // 判斷是否登入
        this.$store.state.userInfo = data.data;
      } else {
        this.haslogin = false;
        this.$store.state.haslogin = false;
      }
    },

    handleOptions(opts) {
      this.form = { ...this.form, ...opts };
    },
  },
};
</script>
<style lang="less" scoped>
/deep/ .el-table__header-wrapper {
  background-color: #202636;
}

.black-bg {
  .user-center-title {
    font-size: 18px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #ffffff;
  }
  .search-chi {
    position: absolute;
    right: 10px;
    top: 8px;
    color: #fff;
  }
}

.red-bg {
  .user-center-title {
    font-size: 18px;
    font-family: Microsoft YaHei;
    font-weight: 400;
  }
  .search-chi {
    position: absolute;
    right: 10px;
    top: 8px;
    color: #000;
  }
}

.header-chi {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-box {
  padding-top: 65px;
  padding-bottom: 100px;
  width: 1200px;
  margin: 0 auto;
}

.table-search {
  padding: 20px 0;

  /deep/ .el-input-group__append {
    background-color: #353747;
    border-color: #353747;
  }

  /deep/ .el-input__inner {
    color: #fff;
    border-color: #353747;
    background-color: #24252c;
  }
}

.user-center {
  background: #fff;
}

.main-wrapper {
  .wrapper {
    padding: 20px;

    .table-search {
      margin-bottom: 15px;
    }
  }
}
</style>
