<template>
  <div class="divBox">
    <el-card>
      <div class="btn-box">
        <el-button type="primary" @click="addcate">{{ $t('hr.addcategorys') }}</el-button>
      </div>
      <div class="table-box mt10">
        <el-table
          :data="tableData"
          :header-cell-style="{ background: '#F2F2F2', color: 'black' }"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          border
          default-expand-all
          row-key="id"
          style="width: 100%"
        >
          <el-table-column label="ID" min-width="80" prop="id" style="padding-left: 20px" />
          <el-table-column :label="$t('hr.rankcategoryname')" min-width="180" prop="name" />
          <el-table-column :label="$t('hr.ranknumber')" min-width="180" prop="number" />
          <el-table-column :label="$t('hr.prerankcategories')" min-width="180" prop="cate.name" />
          <el-table-column :label="$t('public.operation')" flxed="right" prop="address" width="160">
            <template slot-scope="scope">
              <el-button type="text" @click="edit(scope.row)">{{ $t('public.edit') }}</el-button>
              <el-button type="text" @click="delet(scope.row)">{{ $t('public.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { rankCateCreateApi, rankCateListApi, rankCateEditApi, rankCateDeleteApi } from '@/api/setting'

export default {
  name: 'Cate',
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.Info()
  },
  methods: {
    // 获取列表
    async Info() {
      const result = await rankCateListApi()
      this.tableData = result.data
    },
    // 添加类别
    addcate() {
      this.$modalForm(rankCateCreateApi()).then(() => {
        this.Info()
      })
    },
    // 编辑
    edit(data) {
      this.$modalForm(rankCateEditApi(data.id)).then(() => {
        this.Info()
      })
    },
    // 删除
    delet(data) {
      this.$modalSure(this.$t('hr.message6')).then(() => {
        this.tableData.splice(data.id, 1)
        rankCateDeleteApi(data.id).then(() => {
          this.Info()
        })
      })
    }
  }
}
</script>
