<template>
  <div
    class=""
    style="background-color: #000;"
    :style="'height:' + windowHeight + 'px'"
  >
    <el-tabs style="margin-top:0px;" v-model="activeName" class="black-style">
      <!-- label="股票" -->
      <el-tab-pane :name="['first','four'].includes(activeName)?activeName:''">
        <div class="table transaction-table">
          <div class="table-search">
            <el-row type="flex" justify="end">
              <el-col :span="24">
                <el-input
                  size="mini"
                  clearable
                  v-model="form.stock"
                  placeholder="Please enter ticker and stock"
                >
                  <el-button
                    size="mini"
                    @click="getList"
                    slot="append"
                    icon="el-icon-search"
                  ></el-button>
                </el-input>
              </el-col>
            </el-row>
          </div>
          <el-table
            v-loading="loading"
            :data="list.list"
            @row-click="toTransaction"
            :height="windowHeight"
            style="width: 100%"
          >
            <el-table-column prop="name" label="Stock Name">
              <template slot-scope="scope">
                <div class="tab-name">
                  <p style="display: flex; width: 100px;">
                    <a
                      v-if="scope.row.isOption == 0"
                      href="javascript:;"
                      @click="tianjiaOptions(scope.row)"
                      ><i class="red-xx iconfont icon-wujiaoxing"></i
                    ></a>
                    <a
                      v-if="scope.row.isOption == 1"
                      href="javascript:;"
                      @click="quxiaoOptions(scope.row)"
                      ><i class="red-xx iconfont icon-wujiaoxing1"></i
                    ></a>
                    <span style="text-overflow: ellipsis;overflow: hidden; white-space:nowrap;">  {{ scope.row.name }}</span>
                  
                  </p>
                  <span class="code">
                    {{ scope.row.code }}
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="nowPrice" align="right" label="Latest price">
              <template slot-scope="scope">
                <div class="price">
                  <div
                    :class="
                      scope.row.hcrate < 0
                        ? 'green'
                        : scope.row.hcrate == 0
                        ? ''
                        : 'red'
                    "
                  >
                    <span v-if="scope.row.nowPrice === 0">-</span>
                    <span v-else>
                      {{ Number(scope.row.nowPrice).toFixed(2) }}
                      <i
                        v-if="scope.row.hcrate > 0"
                        class="iconfont icon-direction-top"
                      ></i>
                      <i
                        v-if="scope.row.hcrate < 0"
                        class="iconfont icon-down"
                      ></i>
                    </span>
                  </div>
                </div>
                <!-- <div v-if="scope.row.now_price" :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
                  <p :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                    {{scope.row.now_price === 0?'-':scope.row.now_price}}
                  </p>
                </div>-->
              </template>
            </el-table-column>
            <el-table-column prop="hcrate" align="right" label="Chg %">
              <template slot-scope="scope">
                <div class="price">
                  <div
                    :class="
                      scope.row.hcrate < 0
                        ? 'green'
                        : scope.row.hcrate == 0
                        ? ''
                        : 'red'
                    "
                  >
                    <span v-if="scope.row.nowPrice === 0">-</span>
                    <span v-else
                      >{{ Number(scope.row.hcrate).toFixed(2) }}%</span
                    >
                    <!-- <i v-if="scope.row.hcrate>0" class="iconfont icon-up"></i>
                    <i v-if="scope.row.hcrate<0" class="iconfont icon-down"></i>-->
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="page-box text-center">
            <a @click="toStock" class="more-btn" href="javascript:;">
              Click to load more
              <i class="iconfont icon-xiasanjiao"></i>
            </a>
            <!-- <el-pagination
            class="pull-right"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="list.pageNum"
                :page-sizes="[10, 20, 30, 40,50]"
                :page-size="list.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="list.total">
            </el-pagination>-->
          </div>
        </div>
      </el-tab-pane>
      <!-- label="自選" -->
      <el-tab-pane v-if="$store.state.haslogin" name="second">
        <div v-show="$store.state.haslogin" class="table transaction-table">
          <div class="table-search">
            <el-row type="flex" justify="end">
              <el-col :span="24">
                <el-input
                  size="mini"
                  clearable
                  v-model="form2.stockMy"
                  placeholder="Please enter ticker and stock"
                >
                  <el-button
                    size="mini"
                    @click="getList"
                    slot="append"
                    icon="el-icon-search"
                  ></el-button>
                </el-input>
              </el-col>
            </el-row>
          </div>
          <el-table
            v-loading="loading"
            :data="list.list"
            @row-click="toTransaction"
            :height="windowHeight"
            style="width: 100%"
          >
            <el-table-column
              prop="name"
              width="20px"
              v-if="$store.state.haslogin"
            >
              <template slot-scope="scope">
                <a
                  v-if="scope.row.isOption == 0"
                  href="javascript:;"
                  @click="tianjiaOptions(scope.row)"
                  ><i class="red-xx iconfont icon-wujiaoxing red-xx"></i
                ></a>
                <a
                  v-if="scope.row.isOption == 1"
                  href="javascript:;"
                  @click="quxiaoOptions(scope.row)"
                  ><i class="red-xx iconfont icon-wujiaoxing1 red-xx"></i
                ></a>
                <!-- <a v-if="isOptionOpt" href="javascript:;" @click="deteleOptions"><i class="iconfont icon-wujiaoxing"></i></a>
								<a v-if="!isOptionOpt" href="javascript:;" @click="addOptions"><i class="iconfont icon-wujiaoxing1"></i></a> -->
              </template>
            </el-table-column>
            <el-table-column prop="name" label="name" >
              <template slot-scope="scope">
                <div class="tab-name">
                  <p style="display: flex; width: 100px;">
                   
                    <span style="text-overflow: ellipsis;overflow: hidden; white-space:nowrap;"> {{ scope.row.stockName }}</span>
                  </p>
                  <span class="code">
                    <i class="iconfont kechuang-stock">US</i
                    >
                    {{ scope.row.stockCode }}
                  </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="nowPrice" align="center" label="Current price">
              <template slot-scope="scope">
                <div class="price">
                  <div
                    :class="
                      scope.row.hcrate < 0
                        ? 'green'
                        : scope.row.hcrate == 0
                        ? ''
                        : 'red'
                    "
                  >
                    <span v-if="scope.row.nowPrice === 0">-</span>
                    <span v-else>{{
                      Number(scope.row.nowPrice).toFixed(2)
                    }}</span>
                    <i
                      v-if="scope.row.hcrate > 0"
                      class="iconfont icon-direction-top"
                    ></i>
                    <i
                      v-if="scope.row.hcrate < 0"
                      class="iconfont icon-down"
                    ></i>
                  </div>
                </div>
                <!-- <div v-if="scope.row.now_price" :class="changeTextClass[scope.$index] === true?'heartBeat  tab-number':' tab-number'">
                  <p :class="scope.row.now_price - scope.row.buyOrderPrice < 0?'green bounceIn':scope.row.now_price - scope.row.buyOrderPrice > 0?'bounceIn red':'bounceIn'">
                    {{scope.row.now_price === 0?'-':scope.row.now_price}}
                  </p>
                </div>-->
              </template>
            </el-table-column>
            <el-table-column prop="hcrate" align="center" label="chg %">
              <template slot-scope="scope">
                <div class="price">
                  <div
                    :class="
                      scope.row.hcrate < 0
                        ? 'green'
                        : scope.row.hcrate == 0
                        ? ''
                        : 'red'
                    "
                  >
                    <span v-if="scope.row.nowPrice === 0">-</span>
                    <span v-else
                      >{{ Number(scope.row.hcrate).toFixed(2) }}%</span
                    >
                    <!-- <i v-if="scope.row.hcrate>0" class="iconfont icon-up"></i>
                    <i v-if="scope.row.hcrate<0" class="iconfont icon-down"></i>-->
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <div class="page-box text-center">
            <a @click="toStock" class="more-btn" href="javascript:;">
              load more
              <i class="iconfont icon-xiasanjiao"></i>
            </a>
            <!-- <el-pagination
            class="pull-right"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="list.pageNum"
                :page-sizes="[10, 20, 30, 40,50]"
                :page-size="list.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="list.total">
            </el-pagination>-->
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <BuyIndex
      :handleOptions2="handleOptions2"
      :indexInfo="indexInfo"
      ref="buyDialog"
    />
    <BuyFutures
      :handleOptions3="handleOptions3"
      :futuresInfo="futuresInfo"
      ref="futuresDialog"
    />
  </div>
</template>

<script>
import * as api from "../../../axios/api";
import BuyIndex from "./indexbuy-dialog";
import BuyFutures from "./futuresbuy-dialog";

export default {
  components: {
    BuyIndex,
    BuyFutures
  },
  props: {
    code: {
      type: String
    },
    handleOptions2: {
      type: Function,
      default: function() {}
    },
    handleOptions3: {
      type: Function,
      default: function() {}
    }
  },
  data() {
    return {
      loading: false,
      form: {
        stock: "",
        stockMy: "",
        pageNum: 1,
        pageSize: 9
      },
      form2: {
        stockMy: "",
        pageNum: 1,
        pageSize: 9
      },
      list: {
        list: []
      },
      timer: null,
      refresh: false, // 刷新中
      changeTextClass: {}, // 表格中的数据变化
      isOptionOpt: false, // 是否添加自选
      activeName: "first",
      total: 0, // 总数
      indexInfo: {},
      futuresInfo: {}, // 期货信息\
      height: 0,
      windowWidth: document.documentElement.clientWidth, //实时屏幕宽度
      windowHeight: document.documentElement.clientHeight - 60 - 120 //实时屏幕高度
    };
  },
  watch: {
    activeName(newVal, oldVal) {
      console.log(newVal);
      if (newVal !== oldVal) {
        this.form.pageNum = 1;
        this.getList();
      }
    }
  },
  computed: {},
  created() {
    this.form.pageSize = Math.ceil(this.windowHeight / 63);
    this.form2.pageSize = Math.ceil(this.windowHeight / 63);
    this.timer = setInterval(this.refreshList, 60000);
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  mounted() {
    setInterval(() => {
      this.windowHeight = document.documentElement.clientHeight - 60 - 120; //实时屏幕高度
      this.form.pageSize = Math.ceil(this.windowHeight / 63);
      this.form2.pageSize = Math.ceil(this.windowHeight / 63);
    }, 500);
    this.getList();
    if (this.$store.state.haslogin) {
      this.getOpation();
    }
    if (this.$route.query.type === "index") {
      this.activeName = "three";
    }
  },
  methods: {
    async getlistStart() {
      opt = {
        // stockPlate: "上櫃",
        keyWords: this.form.stock,
        pageNum: this.form.pageNum,
        pageSize: this.form.pageSize
      };
      var data = await api.getStock(opt);
      if (data.status == 0) {
        this.list.list = data.data;
      }
    },
    async tianjiaOptions(row) {
      let data = await api.addOption({
        code: row.code
      });

      if (data.status === 0) {
        this.$message.success("Add optional successfully");
        row.isOption = row.isOption == "1" ? "0" : "1";
      } else {
        this.$message.error(data.msg);
      }
    },
    async quxiaoOptions(row) {
      let data = await api.delOption({
        code: row.code
      });
      if (data.status === 0) {
        this.$message.success("Deleting self-selected stocks succeeded");
        row.isOption = row.isOption == "1" ? "0" : "1";
      } else {
        this.$message.error(data.msg);
      }
    },
    handleSizeChange(val) {
      this.form.pageSize = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.form.pageNum = val;
      this.getList();
    },
    async refreshList() {
      // 正在刷新或者正在加载的时候不进行刷新
      if (this.refresh || this.loading) {
        return;
      }

      this.refresh = true;
      this.changeTextClass = {};
      // 获取表格数据
      // let opt = {
      //     keyWords:this.activeName === 'first'?this.form.stock:this.form2.stockMy,
      //     pageNum:1,
      //     pageSize:this.form.pageSize * this.form.pageNum
      // }
      let opt = null;
      if (this.activeName === "first") {
        // 普通A股
        opt = {
          // stockPlate: "上市",
          keyWords: this.form.stock,
          pageNum: 1,
          pageSize: this.form.pageSize * this.form.pageNum
        };
      } else if (this.activeName === "four") {
        // 科创
        opt = {
          // stockPlate: "上櫃",
          keyWords: this.form.stock,
          pageNum: 1,
          pageSize: this.form.pageSize * this.form.pageNum
        };
      } else if (this.activeName === "three" || this.activeName === "five") {
        // 指数 期货
      } else if (this.activeName == "start") {
        opt = {
          // stockPlate: "创业",
          keyWords: this.form.stock,
          pageNum: 1,
          pageSize: this.form.pageSize * this.form.pageNum
        };
      } else {
        // 自选
        opt = {
          keyWords: this.form2.stockMy,
          pageNum: 1,
          pageSize: this.form2.pageSize * this.form2.pageNum
        };
      }
      let data = null;
      if (
        this.activeName === "first" ||
        this.activeName === "four" ||
        this.activeName == "start"
      ) {
        this.formatTwStockList(opt)
		return
      } else if (this.activeName === "three") {
        data = await api.getListMarket(opt);
      } else if (this.activeName === "five") {
        // 期货
        data = await api.getFutures(opt);
      } else {
        data = await api.getMyList(opt);
        this.formatTwStockList(opt,data)
      }
      this.refresh = false;
      if (data.status === 0) {
        if (this.activeName === "three" || this.activeName === "five") {
          this.list.list = data.data;
          return;
        }
        if (data.data.list.length === this.list.list.length) {
          data.data.list.forEach((element, i) => {
            this.changeTextClass[i] = "";
            if (data.data.list[i].nowPrice !== this.list.list[i].nowPrice) {
              this.changeTextClass[i] = true; // 设置对应的动画过滤
              this.list.list[i].nowPrice = data.data.list[i].nowPrice; // 现价
              this.list.list[i].hcrate = data.data.list[i].hcrate; // 涨跌幅
            }
          });
        } else {
          this.list = data.data;
        }
      } else {
        this.$message.error(data.msg);
      }
    },
    onSubmit() {
      // 查询表格
      // 正在刷新或者正在加载的时候 延迟
      if (this.refresh || this.loading) {
      } else {
        this.getList();
      }
    },
    async getList() {
      // 获取表格数据
      // let opt = {
      //     keyWords:this.activeName === 'first'?this.form.stock:this.form2.stockMy,
      //     pageNum:this.form.pageNum,
      //     pageSize:this.form.pageSize
      // }
      if (this.activeName === "three") {
        // 指数
        this.getMarketList();
        return;
      }
      if (this.activeName === "five") {
        // 期货
        this.getFutures();
        return;
      }
      let opt = null;
      if (this.activeName === "first") {
        // 上市
        opt = {
          // stockPlate: "上市",
          keyWords: this.form.stock,
          pageNum: this.form.pageNum,
          pageSize: this.form.pageSize
        };
      } else if (this.activeName === "four") {
        // 上櫃
        opt = {
          // stockPlate: "上櫃",
          keyWords: this.form.stock,
          pageNum: this.form.pageNum,
          pageSize: this.form.pageSize
        };
      }else if (this.activeName === "start") {
        opt = {
          // stockPlate: "创业",
          keyWords: this.form.stock,
          pageNum: this.form.pageNum,
          pageSize: this.form.pageSize
        };
      } else {
        // 自选
        opt = {
          keyWords: this.form2.stockMy,
          pageNum: this.form2.pageNum,
          pageSize: this.form2.pageSize
        };
    
      }
      this.loading = true;
      let data = null;
      if (this.activeName === "first" || this.activeName === "four") {
        this.formatTwStockList(opt)
        this.loading = false;
      } else if (this.activeName === "start") {
        data = await api.getStock(opt);
      } else {
        data = await api.getMyList(opt);
        this.formatTwStockList(opt,data)
      }
      
      
      this.loading = false;
    },
    // 点击股票item
    async toTransaction(row, column, event) {
      // console.log(row, column, event)
      // let data = await api.addOption({ code: this.$route.query.code });
      // if (data.status === 0) {
      //   this.$message.success("添加自选成功");
      // } else {
      //   this.$message.error(data.msg);
      // }
      // let code =
      //   this.activeName === "first" ||
      //   this.activeName === "four" ||
      //   this.activeName == "start"
      //     ? row.code
      //     : row.stockCode;
      // if (row.stockCode != undefined && row.stockGid.indexOf("hf_") != -1) {
      //   code = row.stockGid;
      // } else if (
      //   row.stockCode !== undefined &&
      //   row.stockCode.substring(0, 3) == "000"
      // ) {
      //   code = row.stockGid;
      // }
      this.$emit("changeActiveName", "first");
      // 股票交易
      this.$router.push({
        path: "/transaction",
        query: {
          code: row.stockCode ==undefined?row.code:row.stockCode
        }
      });
      this.$emit("toTransaction", row);
    },
    // 期货2.0
    toTransaction5(row, column, event) {
      // 出仓数据
      // console.log(row)
      api
        .findUserFuturesPositionByCode({
          futuresGid: row.futuresGid
        })
        .then(data => {
          if (data.status == 0) {
            this.$store.commit("setUserPositionData", data.data.list[0]);
          }
        });

      // 股票交易
      this.$router.push({
        path: "/transaction",
        query: {
          code: row.futuresGid,
          futuresInfo: row
        }
      });
    },
    async getMarketList() {
      // 获取指数列表
      this.loading = true;
      let result = await api.getListMarket();
      if (result.status === 0) {
        this.list.list = result.data;
        this.loading = false;
      } else {
        this.$message.error(result.msg);
      }
      this.loading = false;
    },
    async getFutures() {
      // 获取期货列表
      this.loading = true;
      let result = await api.getFutures();
      if (result.status === 0) {
        this.list.list = result.data;
      } else {
        this.$message.error(result.msg);
      }
      this.loading = false;
    },
    toTransaction2(row, column, event) {
      api
        .findUserIndexPositionByCode({
          indexGid: row.indexGid
        })
        .then(data => {
          console.log(data);
          if (data.status == 0) {
            this.$store.commit("setUserPositionData", data.data.list[0]);
          }
        });

      // 指数交易
      if (this.$store.state.haslogin) {
        if (row.transState === 1) {
          // this.indexInfo = row
          // this.$refs.buyDialog.DialogVisible = true
          this.$router.push({
            path: "/transaction",
            query: {
              code: row.indexGid
            }
          });
        } else {
          this.$message.error("The index is temporarily unavailable for trading!");
        }
      } else {
        this.$store.state.loginIsShow = true;
      }
    },
    toTransaction3(row) {
      // 期貨交易 先判斷是否登錄
      if (this.$store.state.haslogin) {
        if (row.transState === 1) {
          this.futuresInfo = row;
          this.$refs.futuresDialog.DialogVisible = true;
        } else {
          this.$message.error("The futures are temporarily unavailable!");
        }
      } else {
        this.$store.state.loginIsShow = true;
      }
    },
    async toStock() {
      // this.$router.push('/stock')
      // 查看更多
      if (this.form.pageNum * this.form.pageSize < this.total) {
        this.form.pageNum++;
        this.loading = true;
        let opt = null;
        if (this.activeName === "first") {
          opt = {
            // stockPlate: "上市",
            keyWords: this.form.stock,
            pageNum: 1,
            pageSize: this.form.pageSize * this.form.pageNum
          };
        } else if (this.activeName === "four") {
          opt = {
            // stockPlate: "上櫃",
            keyWords: this.form.stock,
            pageNum: 1,
            pageSize: this.form.pageSize * this.form.pageNum
          };
        } else if (this.activeName === "start") {
          opt = {
            // stockPlate: "创业",
            keyWords: this.form.stock,
            pageNum: 1,
            pageSize: this.form.pageSize * this.form.pageNum
          };
        } else {
          // 自选
          opt = {
            keyWords: this.form2.stockMy,
            pageNum: 1,
            pageSize: this.form2.pageSize * this.form2.pageNum
          };
        }
        // let opt = {
        //     keyWords:this.activeName === 'first'?this.form.stock:this.form2.stockMy,
        //     pageNum:1,
        //     pageSize:this.form.pageSize * this.form.pageNum
        // }
        let data = null;
        if (this.activeName === "first" || this.activeName === "four") {
          this.formatTwStockList(opt)

          this.loading = false;
        } else if (this.activeName == "start") {
          data = await api.getStock(opt);
        } else {
          data = await api.getMyList(opt);
          this.formatTwStockList(opt,data)
        }
        if (data.status === 0) {
          // data.data.list.forEach(element => {
          //     this.list.push(element)
          // });
          //this.list = data.data;
        } else {
          this.$message.error(data.msg);
        }
        this.loading = false;
      } else {
        this.$message.error("all loaded");
      }
    },

    async getOpation() {
      // 獲取是不是已添加自選
      let opts = {
        code: this.$route.query.code
      };
      let data = await api.isOption(opts);
      if (data.status === 0) {
        // 0 --> 未添加
        this.isOptionOpt = false;
      } else {
        this.isOptionOpt = true;
      }
    },

    async addOptions() {
      let data = await api.addOption({
        code: this.$route.query.code
      });
      if (data.status === 0) {
        this.$message.success("Add optional successfully");
        this.isOptionOpt = true;
      } else {
        this.$message.error(data.msg);
      }
    },
    async deteleOptions() {
      let data = await api.delOption({
        code: this.$route.query.code
      });
      if (data.status === 0) {
        this.$message.success("Deleting self-selected stocks succeeded");
        this.isOptionOpt = false;
      } else {
        this.$message.error(data.msg);
      }
    },
    async formatTwStockList(opt,resultData) {
      let data={}
      if(resultData){
        data=resultData
      }else{
        // data = await api.getTwStockPageList(opt);
				 data = await api.getStock(opt);
      }
      this.list = { ...data.data};
	    this.total=this.list.total
      // let stock_ids = data.data.list.map(item => item.code||item.stockCode).join(",");
      
      // const res1 = await api.getTwStockData(stock_ids);
      // let arr=[]
      // res1.data.forEach((item, index) => {
      //   let newItem = {
      //     nowPrice: item["當盤成交價"],
      //     hcrate: item["Quote change"],
      //     today_max: item["最高價"],
      //     today_min: item["最低價"]
      //   };
      //   let obj = data.data.list[index];
      //   arr.push({
      //     name: obj.name||obj.stockName,
      //     code: obj.code||obj.stockCode,
      //     stock_type: 'tw',
      //     isOption:obj.isOption||'1',
      //     ...newItem
      //   });
      // });
      // this.list.list=arr
      console.log(this.list);
	  // return data
    }
  }
};
</script>
<style lang="less" scoped>
/deep/ .el-tabs__header {
  display: none;
}

.red-bg {
  .red-xx {
    color: #c11815;
  }
}

.table {
  .code {
    color: #6d718b;
    font-size: 12px;
    line-height: 16px;
  }

  .more-btn {
    text-align: center;
    color: #fff;
  }

  .el-table .cell {
    line-height: 18px;
  }

  .page-box {
    // background: #171f2e;
    padding-top: 0px;
  }

  .price {
    font-size: 12px;
  }
}

.el-table /deep/ .cell {
  padding: 0 8px;
}

.black-style.el-tabs .el-tab-pane {
  padding: 0;
}

.el-tabs /deep/ .el-tabs__item {
  color: #8b97b0;
  height: 25px;
  line-height: 25px;
  font-size: 12px;
}

.transaction-table /deep/ th.is-leaf {
  // background: #1a2231;
  padding: 0px 0;
}
/deep/ .el-table td.el-table__cell, .el-table th.el-table__cell.is-leaf {
  border-bottom: 0px solid #dfe6ec !important;
  }
</style>
