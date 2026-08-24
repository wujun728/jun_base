<template>
  <div class="divBox">
    <el-card :body-style="{ padding: '20px 20px 0 20px' }" class="card-head normal-page" shadow="never">
      <oaFromBox
        :isTotal="false"
        :isViewSearch="false"
        :search="searchData"
        :sortSearch="false"
        :title="$route.meta.title"
        btnText="添加菜单"
        @addDataFn="addMenu"
        @confirmData="confirmData"
      ></oaFromBox>
      <div class="mb10"></div>
      <div class="table-box">
        <el-table
          :data="tableData"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          row-key="id"
          style="width: 100%"
        >
          <el-table-column label="菜单名称" prop="menu_name"> </el-table-column>
          <el-table-column label="类型" prop="type" width="200">
            <template slot-scope="props">
              <el-tag v-if="props.row.type === 'A'" type="success">接口</el-tag>
              <el-tag v-if="props.row.type === 'M'" type="info">菜单</el-tag>
              <el-tag v-if="props.row.type === 'B'">按钮</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="显示状态" prop="is_show" width="150">
            <template slot-scope="props">
              <el-switch
                v-if="!['A', 'B'].includes(props.row.type)"
                v-model="props.row.is_show"
                :active-value="1"
                :inactive-value="0"
                active-text="显示"
                inactive-text="隐藏"
                @change="changeStatus(props.row)"
              >
              </el-switch>
            </template>
          </el-table-column>
          <el-table-column label="排序" prop="sort" width="150"> </el-table-column>
          <el-table-column fixed="right" label="操作" prop="desc" width="150">
            <template slot-scope="scope">
              <el-button type="text" @click="editMenu(scope.row.id)">编辑</el-button>
              <el-button type="text" @click="delMenu(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <rightDrawer ref="rightBox" :config="drawerConfig" @changge="getAllMenus"></rightDrawer>
  </div>
</template>

<script>
import rightDrawer from '@/components/setting/rightDrawer'
import oaFromBox from '@/components/common/oaFromBox'
import formCreate from '@form-create/element-ui'
import { menuListApi, menuDeleteitApi, menuShowApi } from '@/api/system'
import Tips from '@/utils/tips'

export default {
  name: 'list',
  components: { formCreate: formCreate.$form(), rightDrawer, oaFromBox },
  data() {
    return {
      formData: {
        menu_name: ''
      },
      tableData: [],
      drawerConfig: {
        title: '添加菜单',
        api: 'system/menus/create'
      },
      searchData: [
        {
          field_name: '菜单名称',
          field_name_en: 'menu_name',
          form_value: 'input'
        }
      ]
    }
  },
  mounted() {
    this.getAllMenus()
  },
  methods: {
    async getAllMenus() {
      const result = await menuListApi(this.formData)
      this.tableData = result.data
    },
    // 添加菜单
    addMenu() {
      this.drawerConfig.title = '新增菜单'
      this.drawerConfig.api = 'system/menus/create'
      this.$refs.rightBox.handelOpen()
    },
    // 编辑菜单
    async editMenu(id) {
      this.drawerConfig.title = '编辑菜单'
      this.drawerConfig.api = 'system/menus/' + id + '/edit'
      this.$refs.rightBox.handelOpen()
    },
    async delMenu(id) {
      await Tips.confirm({ message: '确定删除该菜单吗,删除后将不可恢复？' })
      await menuDeleteitApi(id)
      await this.getAllMenus()
    },
    async changeStatus(item) {
      await menuShowApi(item.id, { is_show: item.is_show })
    },
    search() {
      this.getAllMenus()
    },
    reset() {
      this.formData.menu_name = ''
      this.getAllMenus()
    },
    confirmData(data) {
      if (data == 'reset') {
        this.formData = { menu_name: '' }
      } else {
        this.formData = { ...this.formData, ...data }
      }
      this.getAllMenus()
    }
  }
}
</script>

<style scoped></style>
