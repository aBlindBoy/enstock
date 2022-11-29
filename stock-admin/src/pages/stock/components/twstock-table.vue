<template>
  <div class="twstock-table">
    <el-card class="box-card">
      <el-row class="text-left">
        <el-form
          :inline="true"
          :model="searchForm"
          label-width="100px"
          class="demo-form-inline"
          size="small"
        >
          <el-form-item label="股票种类">
            <el-select
              v-model="searchForm.stockPlate"
              placeholder="选择股票种类"
              @change="search"
              clearable
            >
              <el-option label="上市" value="上市"></el-option>
              <el-option label="上櫃" value="上櫃"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="名称或code">
            <el-input
              v-model="searchForm.keyWords"
              placeholder="股票名称或code"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="search"
              >查询</el-button
            >
          </el-form-item>
        </el-form>
      </el-row>
      <el-row class="text-right">
        <el-button type="text" size="small" @click="addStock">
          <i class="iconfont icon-add"></i>添加股票
        </el-button>
      </el-row>
      <el-table border :data="list">
        <el-table-column prop="code" label="股票code"> </el-table-column>
        <el-table-column prop="name" label="股票名称"> </el-table-column>
        <el-table-column prop="stock_type" label="股票类型"> </el-table-column>
        <el-table-column prop="stock_plate" label="股票种类"> </el-table-column>
        <el-table-column label="操作">
          <template v-slot="{ row }">
            <el-button
              type="text"
              size="small"
              @click="updateStock(row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="removeStock(row)"
              icon="el-icon-delete"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page-box">
        <el-pagination
          class="pull-right"
          @size-change="sizeChange"
          @current-change="pageChange"
          :current-page="searchForm.pageNum"
          :page-sizes="[10, 20, 30, 50, 100]"
          :page-size="searchForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        >
        </el-pagination>
      </div>
    </el-card>
    <el-dialog
      :title="title"
      :visible.sync="visible"
      width="30%"
      @close="visible = false"
    >
      <el-form ref="form" :model="form" label-width="100px" :rules="rules">
        <el-form-item label="股票Code" prop="stockCode">
          <el-input v-model="form.stockCode"></el-input>
        </el-form-item>
        <el-form-item label="股票名称" prop="stockName">
          <el-input v-model="form.stockName"></el-input>
        </el-form-item>
        <el-form-item label="股票类型" prop="stockType">
          <el-input v-model="form.stockType" disabled></el-input>
        </el-form-item>
        <el-form-item label="股票种类">
          <el-select v-model="form.stockPlate" placeholder="是否为上柜股票">
            <el-option label="上市" value="上市"></el-option>
            <el-option label="上櫃" value="上櫃"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">确认</el-button>
          <el-button @click="visible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import * as api from "@/axios/api";
export default {
  data() {
    return {
      list: [],
      title: "新增",
      visible: false,
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        stockPlate: ""
      },
      form: {
        stockType: "tw",
        stockPlate: ""
      },
      total: 0,
      rules: {
        stockCode: {
          required: true,
          message: "请输入正确的台股code！",
          triggle: "blur"
        },
        stockName: {
          required: true,
          message: "请输入正确的台股名称！",
          triggle: "blur"
        },
        stockType: {
          required: true,
          message: "请输入正确的台股类型！",
          triggle: "blur"
        }
      }
    };
  },
  methods: {
    search() {
      this.searchForm.pageNum = 1;
      this.getTwStockList();
    },
    async getTwStockList() {
      const res = await api.getTwStockPageList(this.searchForm);
      this.list = res.data.list;
      this.total = res.data.total;
    },
    sizeChange(size) {
      this.searchForm.pageSize = size;
      this.getTwStockList();
    },
    pageChange(page) {
      this.searchForm.pageNum = page;
      this.getTwStockList();
    },
    addStock() {
      this.visible = true;
      this.form = {
        stockType: "tw",
        stockPlate: "上市"
      };
      this.title = "新增";
    },
    onSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return false;
        let res = {};
        if (this.form.id) {
          res = await api.updateTwStockApi(this.form);
        } else {
          res = await api.addTwStockApi(this.form);
        }
        if (res.status == 0) {
          this.$message.success(res.msg);
          this.visible = false;
          this.getTwStockList();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    updateStock(row) {
      debugger
      this.form = { ...row };
      this.visible = true;
      this.title = "编辑";
    },
    async removeStock(row) {
      await api.delTwStockApi(row.code);
      this.$message.success("删除成功");
      this.searchForm={
        pageNum: 1,
        pageSize: 10,
        stockPlate: ""
      }
      this.getTwStockList();
    }
  },
  mounted() {
    this.getTwStockList();
  }
};
</script>

<style></style>
