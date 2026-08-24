<template>
  <div v-show="menuVisible" id="contextmenu" class="menu">
    <div v-if="configData.type === 1" class="right-item-list">
      <div class="right-item" @click="rightItemClick()">设置职级</div>
      <div v-if="configData.value" class="right-item" @click="deleteRankItem()">移除职级</div>
    </div>
    <div v-if="configData.type === 2" class="right-item-list">
      <div
        class="right-item"
        :class="configData.value === item.alias ? 'active' : ''"
        v-for="item in configData.tableData"
        :key="item.id"
        @click="rankItemClick(item)"
      >
        {{ item.alias }}
      </div>
    </div>
  </div>
</template>

<script>
import { rankLevelRelationApi, rankLevelRelationDeleteApi } from '@/api/setting';

export default {
  name: 'RightClick',
  props: {
    configData: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      menuVisible: false,
      tableData: [],
      clickData: {},
    };
  },
  mounted() {},
  methods: {
    rightItemClick() {
      this.clickData.type = 1;
      this.clickData.id = this.configData.cate_id;
      this.clickData.row = this.configData.data;
      this.$emit('handleRightClick', this.clickData);
    },
    rightClick(event) {
      this.menuVisible = false; // 先把模态框关死，目的是 第二次或者第n次右键鼠标的时候 它默认的是true
      this.menuVisible = true; // 显示模态窗口，跳出自定义菜单栏
      event.preventDefault(); // 关闭浏览器右键默认事件
      var menu = document.querySelector('#contextmenu');
      this.$nextTick(() => {
        var menuH = menu.clientHeight;
        var cha = document.body.clientHeight - event.clientY;
        // 防止菜单太靠底，根据可视高度调整菜单出现位置
        if (cha < menuH) {
          menu.style.top = event.clientY - menuH + 'px';
        } else {
          menu.style.top = event.clientY - 10 + 'px';
        }
        menu.style.left = event.clientX + 10 + 'px';
      });
      document.addEventListener('click', this.foo); // 给整个document添加监听鼠标事件，点击任何位置执行foo方法
    },
    foo() {
      // 监听，除了点击自己，点击其他地方将自身隐藏
      document.addEventListener('click', (e) => {
        const contextMenuBox = document.getElementById('contextmenu');
        if (contextMenuBox) {
          if (!contextMenuBox.contains(e.target)) {
            this.menuVisible = false;
          }
        }
      });
    },
    rankItemClick(item) {
      const id = this.configData.rank_item.info ? this.configData.rank_item.info.id : 0;
      const data = {
        cate_id: this.configData.cate_id,
        rank_id: item.id,
        level_id: this.configData.data.id,
      };
      this.rankLevelRelation(id, data);
    },
    async deleteRankItem() {
      await rankLevelRelationDeleteApi(this.configData.rank_item.info.id)
      this.clickData.type = 2;
      this.menuVisible = false;
      await this.$emit('handleRightClick', this.clickData);
    },
    async rankLevelRelation(id, data) {
      await rankLevelRelationApi(id, data)
      this.clickData.type = 2;
      this.menuVisible = false;
      await this.$emit('handleRightClick', this.clickData);
    },
  },
};
</script>

<style scoped lang="scss">
.menu {
  position: fixed;
  background: #fff;
  min-width: 120px;
  border-radius: 4px;
  border: 1px solid #e6ebf5;
  padding: 12px;
  z-index: 9999;
  color: #606266;
  line-height: 1.5;
  text-align: justify;
  font-size: 13px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  word-break: break-all
}
</style>
