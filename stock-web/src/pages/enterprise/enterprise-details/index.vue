<template>
  <el-container>
    <el-header>
      <home-header ref="header"></home-header>
    </el-header>
    <el-container class="main-wrapper">
      <div>
        <backdrop1>
          <div style="margin-top:200px;width: 1200px; margin: 240px  auto;font-size: 36px;">
            <span class="stock-title-en">{{$t('enterprise.title')}}</span>
          </div>
        </backdrop1>
      </div>
      <el-main>
        <div class="fat">
          <div
            class="table-box"
            :style="color == 'black-bg'?'margin-top: 50px;':'margin-top: 200px;'"
          >
            <div class="notice">
              <div class="table-search">
                <el-row type="flex" justify="end">
                  <el-col :span="24">
                    <div class="test">
                      <div class="detail">
                        <div class="detail-cont">
                          <div class="detail-title">
                            <h3 v-if="type==1">{{detailsCont.title}}</h3>
                            <h3 v-else>{{detailsCont.artTitle}}</h3>
                          </div>
                          <div class="detail-explain">
                            <div>
                              <span class="iconfont icon-fabuxuqiu date-icon"></span>
                              <span v-if="type==1">{{$t('enterprise.source')}}:{{detailsCont.sourceName}}</span>
                              <span v-else>{{$t('enterprise.source')}}:{{detailsCont.artType}}</span>
                            </div>
                            <div>
                              <span class="iconfont icon-shijian date-icon"></span>
                              <!-- <span class="iconfont icon-shijian"></span> -->
                              <span>{{$t('enterprise.time')}}:{{formatDate(detailsCont.showTime)}}</span>
                            </div>
                            <div>
                              <!-- <span class="iconfont icon-redu"></span> -->
                              <span class="iconfont icon-redu redu"></span>

                              <span>{{$t('enterprise.heat')}}:{{detailsCont.views}}</span>
                            </div>
                          </div>
                        </div>
                        <div class="detail-box">
                          
                          <div v-if="type ==1 " v-html="detailsCont.content"></div>
                          <div v-else v-html="detailsCont.artCnt"></div>
                          <div>{{detailsCont.description}}</div>
                          <div style="display:flex;align-items: center;flex-direction: column;">
                            <img :src="detailsCont.imgurl" />
                          </div>
                        </div>
                      </div>

                      <!-- <el-input v-model="form.stock" placeholder="??????????????????????????????" class="search-public"> -->
                      <!-- <el-button @click="getList" slot="append" icon="iconfont  icon-search"></el-button> -->
                      <!-- </el-input> -->
                      <!-- <span @click="getList" class="iconfont icon-search search-stock"></span> -->
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </div>
        </div>

        <!-- <home-footer :siteInfo="siteInfo"></home-footer> -->
      </el-main>
    </el-container>
    <newFooter></newFooter>
  </el-container>
</template>

<script>
import * as api from "@/axios/api";
import backdrop1 from "@/components/backdrop1.vue";
import newFooter from "@/components/newFooter.vue";
import HomeHeader from "@/components/HeaderOrder";
import { mapState } from "vuex";
const dayjs = require('dayjs')
export default {
  components: {
    HomeHeader,
    backdrop1,
    newFooter,
  },
  mounted() {
    this.selectDetails();
    this.detailsCont();
  },
  computed:{
      ...mapState({
      color:(state)=>state.systemColor
      })
  },
  data() {
    return {
      detailsCont: {},
      type:1,
    };
  },
  methods: {
    // ????????????
    switchData(list, time) {
      list.forEach((item) => {
        var tempStr = item[time] + "";
        var timestamp = tempStr.slice(0, tempStr.length - 3);
        var newDate = new Date();
        newDate.setTime(timestamp * 1000);
        item[time] = newDate.toLocaleDateString();
      });
    },
    async selectDetails() {
      // ????????????
      var { id } = this.$route.query;
      this.type = this.$route.query.type;

      this.cutIndex = 2;
      if (this.type == 1) {
          var data = await api.getNewsDetailList({
          id,
        });
      }else{
          var data = await api.getArtDetail({
          id,
        });
      }
      if (data.status == 0) {
        this.detailsCont = data.data;
      }
      var data = await api.updateNewsViews({
        id: item.id,
      });
      if (data.status == 0) {
      }
    },
    formatDate(date){
       return dayjs(date).format('DD/MM/YYYY') // '25/01/2019'
      }
  },
};
</script>


<style lang="less" scoped>
	.red-bg{
		.fat{
			    width: 100%;
			    background-color: #e9e9e9;
			    position: relative;
			    top: -36px;
		}
	} 
.black-bg {
	
    .detail-explain{
        color: #ccc;
    }
    .detail-box{
        color: #ccc;
    }
  .el-main {
    color: #fff !important;
    background-color: rgb(4, 30, 46);
    margin: 0;
  }
  .fat {
        width: 103%;
    background-color: #041e2e;
    position: relative;
    left: -2%;
    overflow: hidden;
    margin-top: 200px;
  }
  .table-box {
    // margin-bottom: 100px;
    // padding: 50px 0;
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
        width: 70%;
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
          // width: 64px;
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
</style> 