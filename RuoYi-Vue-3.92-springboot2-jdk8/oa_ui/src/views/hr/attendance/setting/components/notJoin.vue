<template>
  <div>
    <!-- 未加入此考勤组人员 -->
    <el-drawer
      title="选择加入此考勤组"
      :visible.sync="drawer"
      size="700px"
      :wrapperClosable="false"
      :before-close="handleClose"
    >
      <div class="box">
        <div class="tips mb20">以下成员已加入其他考勤组，请选择是否加入考勤组</div>
        <!-- 表格 -->
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column prop="name" label="人员姓名" width="120"> </el-table-column>
          <el-table-column prop="position" label="部门" width="180">
            <template slot-scope="scope">
              <div class="frame-name over-text" v-for="(item, index) in scope.row.frames" :key="index">
                <span class="icon-h">
                  {{ item.name
                  }}<span v-show="item.is_mastart === 1 && scope.row.frames.length > 1" title="主部门">(主)</span>
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="job.name" label="职位" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="group.name" label="所在考勤组" show-overflow-tooltip> </el-table-column>
        </el-table>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" size="small" @click="submitForm">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { repeatCheckMemberApi } from '@/api/config'
import { contractList } from '@/api/enterprise'
export default {
  name: 'CrmebOaEntNotJoin',

  data() {
    return {
      drawer: false,
      tableData: [],
      ids: []
    }
  },

  methods: {
    openBox(data) {
      // 获取部门人员禁用名单

      // repeatCheckMemberApi(data).then((res) => {
      //   this.drawer = true
      //   this.tableData = res.data
      // })
      this.drawer = true
      this.tableData = data
    },
    submitForm() {
      this.$emit('otherFiltersFn', this.ids)
      this.handleClose()
    },
    handleClose() {
      this.drawer = false
    },
    handleSelectionChange(e) {
      this.ids = []
      if (e.length !== 0) {
        e.map((item) => {
          this.ids.push(item.id)
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  padding: 20px;
  height: 100%;
  padding-bottom: 0;
  overflow-y: scroll;
  .tips {
    font-size: 12px;
    color: #909399;
    font-size: 400;
  }
}
.frame-name {
  .iconfont {
    padding-right: 6px;
  }
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
}
</style>
