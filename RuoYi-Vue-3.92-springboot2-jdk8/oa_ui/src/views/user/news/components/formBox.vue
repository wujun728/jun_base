<template>
  <el-form :inline="true" class="from-s">
    <el-row class="flex-row">
      <el-form-item label="" class="select-bar">
        <el-cascader
          v-model="types"
          :options="options"
          placeholder="请选择消息类型"
          size="small"
          :props="{ checkStrictly: true }"
          clearable
          @change="handleTypes"
        ></el-cascader>
      </el-form-item>

      <el-form-item class="select-bar">
        <el-input
          v-model="tableFrom.name"
          prefix-icon="el-icon-search"
          clearable
          size="small"
          @change="handleConfirm"
          @keyup.native.stop.prevent.enter="handleConfirm"
          placeholder="请输入标题/内容"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-tooltip effect="dark" content="重置搜索条件" placement="top">
          <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
        </el-tooltip>
      </el-form-item>

      <!-- <el-col :span="8">
          <el-button type="primary" size="small" @click="handleConfirm">搜索</el-button>
          <el-button size="small" @click="reset">{{ $t('public.reset') }}</el-button>
        </el-col> -->
    </el-row>
  </el-form>
</template>

<script>
import { messageCateApi } from '@/api/setting'

export default {
  name: 'FormBox',
  data() {
    return {
      tableFrom: {
        types: '',
        name: ''
      },
      types: [],
      options: []
    }
  },
  mounted() {
    this.getMessageCate()
  },
  methods: {
    selectPeriod() {
      this.confirmData()
    },
    async getMessageCate() {
      const result = await messageCateApi()
      this.options = result.data
    },
    handleTypes(e) {
      if (e.length === 0) {
        this.tableFrom.types = ''
      } else {
        this.tableFrom.types = e[e.length - 1]
      }
      this.confirmData()
    },
    // 重置
    reset() {
      this.tableFrom = {
        types: '',
        name: ''
      }
      this.types = []
      this.confirmData('reset')
    },
    // 确认
    handleConfirm() {
      this.confirmData()
    },
    confirmData(val) {
      this.$emit('confirmData', this.tableFrom, val)
    }
  }
}
</script>

<style lang="scss" scoped>
.from-s {
  display: inline-block;
}
::v-deep .el-form-item {
  margin-bottom: 20px;
}
</style>
