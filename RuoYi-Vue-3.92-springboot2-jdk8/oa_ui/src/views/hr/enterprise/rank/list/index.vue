<!-- <template>
  <div class="divBox">
    <el-card>
      <div class="top">
        <div
          v-for="(item, index) in list"
          :key="index"
          class="active"
          :class="{ actives: color == index }"
          @click="btn(item.id, index)"
        >
          <div>{{ item.name }}</div>
        </div>
      </div>
      <div class="btn-box add">
        <el-button type="primary" @click="addList">{{ $t('hr.addrank') }}</el-button>
      </div>
      <div class="table-box mt20">
        <el-table :data="tableData" style="width: 100%;" :header-cell-style="{ background: '#F2F2F2' }">
          <el-table-column prop="id" label="ID" width="50" style="padding-left: 20px;" />
          <el-table-column prop="name" :label="$t('hr.rankname')" min-width="180" />
          <!--          <el-table-column-->
          <!--            prop="type.name"-->
          <!--            label="职级类型"-->
          <!--            min-width="180"-->
          <!--          />-->
          <el-table-column prop="number" :label="$t('hr.numberranks')" min-width="180" />
          <el-table-column prop="cate.name" :label="$t('hr.prerankrequirements')" min-width="180" />
          <el-table-column :label="$t('hr.state')" width="180">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-text="$t('hr.open')"
                :inactive-text="$t('hr.close')"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatus(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="address" :label="$t('public.operation')" fixed="right" width="160">
            <template slot-scope="scope">
              <el-button type="text" @click="deit(scope.row)">{{ $t('public.edit') }}</el-button>
              <el-button type="text" @click="delet(scope.row)">{{ $t('public.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="page">
        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="count"
          :page-size="pagesize"
          :current-page="page"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import {rankCateListApi, rankCreateApi, rankDeleteApi, rankEditApi, rankListApi, rankStatusApi} from '@/api/setting';
export default {
  name: 'List',
  data() {
    return {
      id: '',
      activeName: '',
      color: 1,
      active: 0,
      list: '',
      count: 1,
      pagesize: 10, // 每页显示的条数
      page: 1, // 当前页数
      tableData: [],
    };
  },
  watch: {},
  created() {
    this.toplist();
    this.Info();
  },
  methods: {
    // 顶部列表
    btn(id, index) {
      if (this.color === index) return;
      this.color = index;
      this.page = 1;
      this.Info(id);
      this.id = id;
    },
    // 顶部数据列表
    async toplist() {
      const result = await rankCateListApi()
      this.list = result.data;
      this.activeName = result.data[0].id;
      this.btn(this.activeName, 0);
    },
    // 添加列表
    addList() {
      this.$modalForm(rankCreateApi()).then(({ message }) => {
        this.Info(this.id);
      });
    },
    // 获取列表
    async Info(id) {
      const result = await rankListApi({ page: this.page, limit: this.pagesize, cate_id: id })
      this.tableData = result.data.list;
      this.count = result.data.count;
    },
    // 编辑状态
    async handleStatus(item) {
      await rankStatusApi(item.id, { status: item.status })
    },
    // 编辑信息
    deit(data) {
      this.$modalForm(rankEditApi(data.id)).then(({ message }) => {
        this.Info(this.id);
      });
    },
    // 删除
    delet(data) {
      this.$modalSure(this.$t('hr.message7')).then(() => {
        this.tableData.splice(data.id, 1);
        rankDeleteApi(data.id).then((res) => {
          this.Info(this.id);
        });
      });
    },
    // 分页
    handleCurrentChange(val) {
      this.page = val;
      this.index = this.color;
      const datas = {
        page: this.page,
        limit: this.pagesize,
        cate_id: this.id,
      };
      this.Info(datas.cate_id);
    },
  },
};
</script>

<style scoped>
.top {
  overflow: hidden;
  width: 100%;
  border-bottom: 1px solid #ccc;
  font-size: 14px;
  padding-bottom: 10px;
}
.add {
  padding-top: 20px;
}
.active {
  float: left;
  padding: 10px 20px;
  cursor: pointer;
}
.actives {
  color: #1890ff;
}
</style> -->
