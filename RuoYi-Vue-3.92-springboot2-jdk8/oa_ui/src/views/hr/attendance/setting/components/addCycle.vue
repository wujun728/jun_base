<template>
  <div>
    <el-drawer
      :title="type == 'add' ? '新增排班周期' : '编辑排班周期'"
      :visible.sync="drawer"
      size="700px"
      :before-close="handleClose"
      :wrapperClosable="false"
    >
      <el-form ref="ruleForm" label-width="90px" class="demo-ruleForm">
        <el-form-item>
          <span slot="label"><span class="color-tab">* </span>周期名称：</span>
          <el-input v-model="name" size="small" clearable placeholder="请输入周期名称" />
        </el-form-item>

        <el-form-item>
          <span slot="label"><span class="color-tab">* </span>周期天数：</span>
          <el-input-number v-model="cycle" controls-position="right" :min="1" :max="31" style="width: 180px">
          </el-input-number>
          天
          <span class="tips">（以此数为周期进行循环，最大周期天数为31天）</span>
        </el-form-item>

        <div class="from-item-title mb15">
          <span>设置班次</span>
        </div>

        <el-form-item v-for="index of cycle" :key="index">
          <span slot="label"><span class="color-tab">* </span>第{{ index }}天：</span>
          <el-select v-model="shifts[index]" placeholder="请选择班次" style="width: 100%">
            <el-option v-for="item in shiftList" :label="item.name" :value="item.id" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button @click="handleClose" size="small">取消</el-button>
        <el-button type="primary" :loading="loading" size="small" @click="submitForm">保存</el-button>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { attendanceShiftSelectApi, saveRosterCycleApi, rosterCycleDetailApi, putCycleListApi } from '@/api/config'
export default {
  name: 'CrmebOaEntAddCycle',
  props: {
    group_id: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      drawer: false,
      name: '',
      cycle: 2,
      shifts: [],
      shiftList: [],
      loading: false,
      type: 'add',
      id: null
    }
  },

  mounted() {
    this.getList()
  },

  methods: {
    // 提交
    async submitForm() {
      if (!this.name) {
        return this.$message.error('请输入周期名称')
      }

      if (this.cycle !== this.shifts.length - 1) {
        return this.$message.error('请选择班次')
      }

      let data = {
        name: this.name,
        group_id: this.group_id,
        cycle: this.cycle,
        shifts: this.shifts
      }
      data.shifts = data.shifts.slice(1)
      if (this.type == 'add') {
        await saveRosterCycleApi(data)
        await this.$emit('cycleList')
        this.drawer = false
      } else {
        await putCycleListApi(this.id, data)
        await this.$emit('cycleList')
        this.drawer = false
      }
    },
    async getList() {
      const result = await attendanceShiftSelectApi({ group_id: this.group_id, id: this.$route.query.id })
      this.shiftList = result.data
    },
    async openBox(data, type) {
      this.type = type
      this.group_id = data.group_id ? data.group_id : data

      if (this.type == 'edit') {
        this.shifts = [null]
        this.id = data.id
        const result = await rosterCycleDetailApi(data.group_id, data.id)
        this.name = result.data.name
        this.cycle = result.data.cycle
        result.data.shifts.map((item) => {
          this.shifts.push(item.id)
        })
      }
      this.drawer = true
    },
    handleClose() {
      this.drawer = false
      this.name = ''
      this.group_id = null
      this.cycle = 2
      this.shifts = []
    }
  }
}
</script>

<style lang="scss" scoped>
.item {
  padding-left: 30px;
  font-size: 13px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
  margin-bottom: 20px;
}
.tips {
  font-size: 13px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #909399;
  margin-left: 20px;
}
.from-item-title {
  border-left: 3px solid #1890ff;
  span {
    padding-left: 10px;
    font-weight: 600;
    font-size: 14px;
  }
}
.demo-ruleForm {
  margin: 20px;
  padding-bottom: 50px;
}
</style>
