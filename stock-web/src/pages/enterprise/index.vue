<template>
  <el-container>
    <el-header>
      <home-header ref="header"></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <!-- <div>
        <backdrop1>
          <div class="title-stock">
            <p class="stock-title-en">STOCK NEWS</p>
          </div>
        </backdrop1>
      </div> -->
      <el-main>
        <div class="fat">
          <div
            class="table-box "
          >
            <div class="notice">
              <div class="table-search">
                <el-row type="flex" justify="end">
                  <el-col :span="8">
                    <div class="test">
                      <div
                        class="new-btn"
                        @click="clickIndexTitle(1)"
                        :class="currIndexTitle == 1?'currIndexTitle':''"
                      >
                        <span>News</span>
                      </div>
                      <div
                        class="notice-btn"
                        @click="clickIndexTitle(2)"
                        :class="currIndexTitle == 2?'currIndexTitle':''"
                      >
                        <span>Notice</span>
                      </div>

                      <!-- <el-input v-model="form.stock" placeholder="请输入股票代码和股票" class="search-public"> -->
                      <!-- <el-button @click="getList" slot="append" icon="iconfont  icon-search"></el-button> -->
                      <!-- </el-input> -->
                      <!-- <span @click="getList" class="iconfont icon-search search-stock"></span> -->
                    </div>
                  </el-col>
                </el-row>
              </div>
              <div v-if="currIndexTitle == 1" class="announcement">
                <div
                  class="special"
                  v-for="(item, index) in noticeList"
                  :key="index"
                  v-if="index < 1 "
                >
                  <div class="left">
                    <h3>{{item.title }}</h3>
                    <p v-html="item.content"></p>
                    <div class="more">
                      <div class="more-cont" @click="toDetails(item,index)">
                        <span>click to see more</span>
                        <span class="iconfont icon-direction-right"></span>
                      </div>
                      <div class="browse">
                        <span class="iconfont icon-liulan"></span>
                        <span>Views:{{item.views}}</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div
                  class="cont-list"
                  @mouseenter="mouseenter(item,index)"
                  @mouseleave="mouseleave(item,index)"
                  v-for="(item, index) in noticeList"
                  @click="toDetails(item,index)"
                  :key="index+1"
                >
                  <div class="left">
                    <div :class="currentIndex == index?'left-dateyes':'left-date'">
                      <div class="left-tian">{{(new Date(item.addTime)).getDate()}}</div>
                      <div
                        class="left-data"
                      >{{(new Date(item.addTime)).getFullYear()}}.{{(new Date(item.addTime)).getMonth()}}</div>
                    </div>
                    <div class="left-cont">
                      <h3>{{item.title}}</h3>
                      <p v-html="item.content"></p>
                      <div class="browse">
                        <span class="iconfont icon-liulan"></span>
                        <span>Views:{{item.views}}</span>
                      </div>
                    </div>
                  </div>
                  <div class="right">
                    <div :class="currentIndex == index?'circleyes':'circle'">
                      <span class="iconfont icon-youjiantou"></span>
                    </div>
                  </div>
                </div>
              </div>

              <div v-if="currIndexTitle == 2" class="announcement">
                <!-- <div
                  class="special"
                  v-for="(item, index) in noticeList"
                  :key="index"
                >
                  <div class="special-cont">
                    <img :src="item.imgurl" alt />
                  </div>
                  <div class="left">
                    <h3>{{ item.artTitle}}</h3>
                    <p>{{item.artSummary}}</p>
                    <div class="more">
                      <div class="more-cont" @click="toDetails(item,index)">
                        <span>click to see more</span>
                        <span class="iconfont icon-direction-right"></span>
                      </div>
                      <div class="browse">
                        <span class="iconfont icon-liulan"></span>
                        <span>Views:{{item.views}}</span>
                      </div>
                    </div>
                  </div>
                </div> -->
                <div
                  class="cont-list"
                  @mouseenter="mouseenter(item,index)"
                  @mouseleave="mouseleave(item,index)"
                  v-for="(item, index) in noticeList"
                  @click="toDetails(item,index)"
                  :key="index+1"
                >
                  <div class="left">
                    <div :class="currentIndex == index?'left-dateyes':'left-date'">
                      <div class="left-tian">{{(new Date(item.addTime)).getDate()}}</div>
                      <div
                        class="left-data"
                      >{{(new Date(item.addTime)).getFullYear()}}.{{(new Date(item.addTime)).getMonth()}}</div>
                    </div>
                    <div class="left-cont">
                      <h3>{{item.artTitle}}</h3>
                      <p>{{item.artSummary}}</p>
                      <div class="browse">
                        <span class="iconfont icon-liulan"></span>
                        <span>Views:{{item.views}}</span>
                      </div>
                    </div>
                  </div>
                  <div class="right">
                    <div :class="currentIndex == index?'circleyes':'circle'">
                      <span class="iconfont icon-youjiantou"></span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="page-box text-center">
                <el-pagination
                  class="pull-right"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="noticeList.pageNum"
                  :page-sizes="[10, 20, 30, 40,50]"
                  :page-size="employPageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="total"
                ></el-pagination>
              </div>
            </div>
          </div>
        </div>

        <newFooter class="newFooter"></newFooter>
        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import HomeHeader from "../../components/HeaderOrder";
import backdrop1 from "@/components/backdrop1.vue";
import newFooter from "@/components/newFooter.vue";

import { mapState } from "vuex";
import * as api from "@/axios/api.js";

export default {
  components: {
    HomeHeader,
    backdrop1,
    newFooter,
  },
  computed: {
    ...mapState({
      color: (state) => state.systemColor,
    }),
  },
  data() {
    return {
      employPageSize: 0,
      employPageNum: 0,
      total: 0,
      currentIndex: -1,
      noticeList: [],
      currIndexTitle: 1,
      pageNum: 1,
      pageSize: 10,
    };
  },
  created() {
    this.getNoticeList();
  },
  methods: {
    toDetails(item, index) {
      this.$router.push({
        path: "/enterprise-details",
        query: {
          id: item.id,
        },
      });
    },
    async handleSizeChange(size) {
      console.log(size);
      if (this.currIndexTitle == 1) {
          let opts = {
          pageNum: this.pageNum,
          pageSize: size
        };
        let data = await api.getNewsList(opts);
        if (data.status == 0) {
          this.noticeList = data.data.list;
        }
        // 新闻资讯
      } else {
        // 通知公告
        let opts = {
          pageNum: this.pageNum,
          pageSize: size,
        };
        let data = await api.getArtList(opts);
        if (data.status == 0) {
          this.noticeList = data.data.list;
        }
      }
      // this.handleOptions({ pageSize: size })
      // this.getData()
    },
    async handleCurrentChange(page) {
      if (this.currIndexTitle == 1) {
        // 新闻资讯
         let opts = {
          pageNum: page,
          pageSize: this.pageSize
        };
        let data = await api.getNewsList(opts);
        if (data.status == 0) {
          this.noticeList = data.data.list;
        }
      } else {
        // 通知公告
        let opts = {
          pageNum: page,
          pageSize: this.pageSize,
        };
        let data = await api.getArtList(opts);
        if (data.status == 0) {
          // this.employPageSize = data.data.pageSize
          // this.employPageNum = data.data.pageNum
          // this.total = data.data.total
          // this.switchData(data.data.list, "addTime");
          this.noticeList = data.data.list;
        }
      }
      console.log(page);
      // this.handleOptions({ pageNum: page })
      // this.getData()
    },
    // 时间转换
    switchData(list, time) {
      list.forEach((item) => {
        var tempStr = item[time] + "";
        var timestamp = tempStr.slice(0, tempStr.length - 3);
        var newDate = new Date();
        newDate.setTime(timestamp * 1000);
        item[time] = newDate.toLocaleDateString();
      });
    },

    async clickIndexTitle(type) {
      this.currIndexTitle = type;
      if (this.currIndexTitle == 1) {
        // 新闻资讯
        var query = {
          pageSize: 10,
        };
        let data = await api.getTransactionNewList(query);
        if (data.status == 0) {
          this.switchData(data.data.list, "addTime");
          this.noticeList = data.data.list;
        }
      } else if (this.currIndexTitle == 2) {
        // 通知公告
        let opts = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        };
        let data = await api.getArtList(opts);
        if (data.status == 0) {
          this.employPageSize = data.data.pageSize;
          this.employPageNum = data.data.pageNum;
          this.total = data.data.total;
          // this.switchData(data.data.list, "addTime");
          this.noticeList = data.data.list;
        }
      }
    },
    mouseenter(item, index) {
      this.currentIndex = index;
    },
    mouseleave(item, index) {
      this.currentIndex = -1;
    },
    async getNoticeList() {
      // 获取新闻列表列表
      var query = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      };
      var data = await api.getNewsList(query);
      if (data.status == 0) {
        this.employPageSize = data.data.pageSize;
        this.employPageNum = data.data.pageNum;
        this.total = data.data.total;
        // this.switchData(data.data.list, "addTime");
        this.noticeList = data.data.list;
      }
    },
  },
};
</script>

<style lang="less" scoped>
.newFooter{
  position: relative;
  z-index: 9999;
}
/deep/.red-bg .el-container {
  background: #e9e9e9;
}

/deep/ .el-main {
  // background-color: rgb(233, 233, 233);
  padding: 0;
  margin-left: 0;
}
.red-bg{
	.stock-title{
		font-size: 36px;
	}
	.stock-title-en{
		font-size: 36px;
	}
}
.black-bg {
	.title-stock{
		width: 1200px;
		margin: 250px auto;
		margin-top:250px;
	}
	.stock-title{
		font-size: 36px;
		font-family: Microsoft YaHei;
		font-weight: 400;
		color: #FFFFFF;
	}
	.stock-title-en{
		font-size: 22px;
		font-weight: 400;
		color: #E6003E;
		line-height: 36px;
	}
  .table-box {
    margin-bottom: 100px;
    width: 1200px;
    margin: 0 auto;
    // box-shadow: inset 20px 20px 20px -10px #000;
    background-image: radial-gradient(#15414d 5%, #103046, #103046);
    color: #fff;
    .page-box {
      background: none;
    }
    .cont-list {
      cursor: pointer;
      margin-top: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .circle {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        border: 2px solid rgb(133, 133, 133);
        display: flex;
        align-items: center;
        justify-content: center;
        span {
          color: rgb(193, 24, 21);
          font-size: 13px;
        }
      }
      .circleyes {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        //   border: 2px solid #;
        border: 2px solid rgb(193, 24, 21);
        background-color: rgb(193, 24, 21);
        display: flex;
        align-items: center;
        justify-content: center;
        span {
          color: #fff;
          font-size: 13px;
        }
      }
      .left {
        display: flex;
        width: 100%;
        .left-date {
          width: 130px;
          height: 80px;
          border: 3px solid rgba(142, 142, 142, 0.24);
          margin-right: 20px;
          .left-tian {
            margin-top: 5px;
            font-size: 38px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;

            text-align: center;
            margin-bottom: 8px;
          }
          .left-data {
            text-align: center;
            margin-top: 5px;
          }
        }
        .left-dateyes {
          width: 130px;
          height: 80px;
          background-color: rgb(193, 24, 21);
          border: 3px solid rgba(193, 24, 21);

          margin-right: 20px;
          color: #fff;
          .left-tian {
            margin-top: 5px;
            font-size: 38px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;
            text-align: center;
            margin-bottom: 8px;
          }
          .left-data {
            text-align: center;
            margin-top: 5px;
          }
        }
        .left-cont {
          width: 80%;
          h3 {
            font-weight: bold;
          }
          p {
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            line-height: 22px;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;

            line-height: 21px;
            opacity: 0.7;
            margin-top: 10px;
          }
          .browse {
            display: flex;
            align-items: center;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;
          }
        }
      }
    }
    .notice {
      padding: 0 30px 40px 40px;
    }
    .special {
      display: flex;
      // border: 1px solid #ccc;
      background-color: rgba(32, 67, 90, 0.5);
      .left {
        padding: 20px 10px;
        box-sizing: border-box;
        width: 100%;
        .more {
          display: flex;
          margin-top: 20px;
          align-items: center;
          .browse {
            display: flex;
            align-items: center;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;

            line-height: 36px;
          }
          .more-cont {
            background: #c11815;
            width: 139px;
            height: 34px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 13px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #ffffff;
            line-height: 36px;
            text-align: center;
            margin-right: 20px;
          }
        }
        h3 {
          font-weight: bold;
        }
        p {
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          line-height: 22px;
          font-size: 12px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #fff;

          line-height: 28px;
          opacity: 0.7;
        }
      }
      .special-cont {
        width: 300px;
        height: 170px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .test {
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;

      .new-btn {
        width: 189px;
        height: 46px;
        border-radius: 23px;
        text-align: center;
        line-height: 46px;
        background: #000000;

        position: relative;
        right: 20px;
        cursor: pointer;

        span {
          width: 64px;
          height: 16px;
          font-size: 16px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
          line-height: 36px;
          text-align: center;
        }
      }
      .notice-btn {
        width: 189px;
        height: 46px;
        background: #000000;
        border-radius: 23px;
        text-align: center;
        line-height: 46px;
        margin-left: 50px;
        position: relative;
        left: 20px;
        cursor: pointer;
        span {
          display: inline-flex;
          width: 64px;
          height: 15px;
          font-size: 16px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
        }
      }
      .currIndexTitle {
        background: #c11815;
      }
    }
  }
}
.red-bg {
	.title-stock{
		width: 1200px;
		margin: 250px auto;
		margin-top:200px;
	}
  .table-box {
    margin-bottom: 100px;
    width: 1200px;
    margin: 0 auto;
    .page-box {
      background: none;
    }
    .cont-list {
      cursor: pointer;
      margin-top: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .circle {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        border: 2px solid #ccc;
        display: flex;
        align-items: center;
        justify-content: center;
        span {
          color: rgb(193, 24, 21);
          font-size: 13px;
        }
      }
      .circleyes {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        //   border: 2px solid #;
        border: 2px solid rgb(193, 24, 21);
        background-color: rgb(193, 24, 21);
        display: flex;
        align-items: center;
        justify-content: center;
        span {
          color: #fff;
          font-size: 13px;
        }
      }
      .left {
        display: flex;
        width: 100%;
        .left-date {
          width: 130px;
          height: 80px;
          border: 3px solid rgba(142, 142, 142, 0.24);
          margin-right: 20px;
          .left-tian {
            margin-top: 5px;
            font-size: 38px;
            font-weight: 400;
            color: #000;
            text-align: center;
            margin-bottom: 8px;
          }
          .left-data {
            text-align: center;
            margin-top: 5px;
          }
        }
        .left-dateyes {
          width: 130px;
          height: 80px;
          background-color: rgb(193, 24, 21);
          border: 3px solid rgba(193, 24, 21);

          margin-right: 20px;
          color: #fff;
          .left-tian {
            margin-top: 5px;
            font-size: 38px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #fff;
            text-align: center;
            margin-bottom: 8px;
          }
          .left-data {
            text-align: center;
            margin-top: 5px;
          }
        }
        .left-cont {
          width: 80%;
          h3 {
            font-weight: bold;
          }
          p {
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            line-height: 22px;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #000000;
            line-height: 21px;
            opacity: 0.7;
            margin-top: 10px;
          }
          .browse {
            display: flex;
            align-items: center;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #000000;
          }
        }
      }
    }
    .notice {
      padding: 0 30px 40px 40px;
    }
    .special {
      display: flex;
      border: 1px solid #ccc;
      .left {
        padding: 20px 10px;
        box-sizing: border-box;
        width: 100%;
        .more {
          display: flex;
          margin-top: 20px;
          align-items: center;
          .browse {
            display: flex;
            align-items: center;
            font-size: 12px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #000000;
            line-height: 36px;
          }
          .more-cont {
            background: #c11815;
            width: 139px;
            height: 34px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 13px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #ffffff;
            line-height: 36px;
            text-align: center;
            margin-right: 20px;
          }
        }
        h3 {
          font-weight: bold;
        }
        p {
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          line-height: 22px;
          font-size: 12px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #000000;
          line-height: 28px;
          opacity: 0.7;
        }
      }
      .special-cont {
        width: 300px;
        height: 170px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    .test {
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;

      .new-btn {
        width: 189px;
        height: 46px;
        border-radius: 23px;
        text-align: center;
        line-height: 46px;
        background: #000000;

        position: relative;
        right: 20px;
        cursor: pointer;

        span {
          width: 64px;
          height: 16px;
          font-size: 16px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
          line-height: 36px;
          text-align: center;
        }
      }
      .notice-btn {
        width: 189px;
        height: 46px;
        background: #000000;
        border-radius: 23px;
        text-align: center;
        line-height: 46px;
        margin-left: 50px;
        position: relative;
        left: 20px;
        cursor: pointer;
        span {
          display: inline-flex;
          width: 64px;
          height: 15px;
          font-size: 16px;
          font-family: Microsoft YaHei;
          font-weight: 400;
          color: #ffffff;
        }
      }
      .currIndexTitle {
        background: #c11815;
      }
    }
  }
}

.table-search {
  padding: 20px 0;
}
.table-search > div {
  justify-content: center !important;
}
// /deep/ .el-main{
//   padding: 0;
//   .table-box[data-v-ebd8dd76] {
//     width: 100%;
//     .table-search{
//       width: 1200px;
//       margin: 0 auto;
//     }
//     .table-title{

//     }
//   }
//   div[data-v-ebd8dd76]{
//     position: relative;
//     background-color: pink;
//   }
// }
</style>
