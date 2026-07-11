<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="鍚嶇О" prop="title">
        <el-input v-model="queryParams.title" placeholder="璇疯緭鍏ュ悕绉? clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="鎻忚堪" prop="desc1">
        <el-input v-model="queryParams.desc1" placeholder="璇疯緭鍏ユ弿杩? clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">鎼滅储</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">閲嶇疆</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['system:common:add']">鏂板</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['system:common:edit']">淇敼</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['system:common:remove']">鍒犻櫎</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['system:common:export']">瀵煎嚭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="bizCommonList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="鍚嶇О" align="center" prop="title" />
      <el-table-column label="鎻忚堪" align="center" prop="desc1" />
      <el-table-column label="鏍囪" align="center" prop="key1" />
      <el-table-column label="鏍囪鍊? align="center" prop="value1" />
      <el-table-column label="鏃ユ湡" align="center" prop="date1" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.date1, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="澶囨敞" align="center" prop="remark" />
      <el-table-column label="鎿嶄綔" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:common:edit']">淇敼</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['system:common:remove']">鍒犻櫎</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="鍚嶇О" prop="title">
          <el-input v-model="form.title" placeholder="璇疯緭鍏ュ悕绉? />
        </el-form-item>
        <el-form-item label="鎻忚堪" prop="desc1">
          <el-input v-model="form.desc1" placeholder="璇疯緭鍏ユ弿杩? />
        </el-form-item>
        <el-form-item label="鏍囪" prop="key1">
          <el-input v-model="form.key1" placeholder="璇疯緭鍏ユ爣璁? />
        </el-form-item>
        <el-form-item label="鏍囪鍊? prop="value1">
          <el-input v-model="form.value1" placeholder="璇疯緭鍏ユ爣璁板€? />
        </el-form-item>
        <el-form-item label="鏃ユ湡" prop="date1">
          <el-date-picker clearable v-model="form.date1" type="date" value-format="yyyy-MM-dd" placeholder="璇烽€夋嫨鏃ユ湡" />
        </el-form-item>
        <el-form-item label="澶囨敞" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="璇疯緭鍏ュ娉? />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">纭?瀹?/el-button>
        <el-button @click="cancel">鍙?娑?/el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBizCommon, getBizCommon, delBizCommon, addBizCommon, updateBizCommon } from "@/api/qixing/bizCommon";

export default {
  name: "BizCommon",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      bizCommonList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        desc1: null
      },
      form: {},
      rules: {
        title: [{ required: true, message: "鍚嶇О涓嶈兘涓虹┖", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listBizCommon(this.queryParams).then(response => {
        this.bizCommonList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        title: null,
        desc1: null,
        key1: null,
        value1: null,
        date1: null,
        remark: null
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "娣诲姞鍏叡淇℃伅";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBizCommon(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "淇敼鍏叡淇℃伅";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBizCommon(this.form).then(response => {
              this.$modal.msgSuccess("淇敼鎴愬姛");
              this.open = false;
              this.getList();
            });
          } else {
            addBizCommon(this.form).then(response => {
              this.$modal.msgSuccess("鏂板鎴愬姛");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('鏄惁纭鍒犻櫎鍏叡淇℃伅缂栧彿涓?' + ids + '"鐨勬暟鎹」锛?).then(function() {
        return delBizCommon(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("鍒犻櫎鎴愬姛");
      }).catch(() => {});
    },
    handleExport() {
      this.download('system/common/export', {
        ...this.queryParams
      }, `common_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>