<template>
  <div>
    <ul class="list">
      <li v-for="item in processedDirectory" :key="item.id" class="list-item">
        <a @click.prevent="$emit('navigate', item.id); " :class="{ active: activeId === item.id }">
          <span v-if="item.children && item.children.length" class="arrow-icon">
            <i class="el-icon-caret-bottom"></i>
          </span>
          <span class="text-ellipsis">{{ item.text }}</span>
        </a>
        <!-- 递归调用自身 -->
        <outline-item
          v-if="item.children && item.children.length"
          :items="item.children"
          :active-id="activeId"
          @navigate="$emit('navigate', $event)"
        />
      </li>
    </ul>
  </div>
</template>
  
<script>
export default {
  name: "OutlineItem",
  props: {
    items: {
      type: Array,
      required: true,
    },
    activeId: {
      type: [String, Number],
      default: null,
    },
  },
  data() {
    return {};
  },
  computed: {
    /** 处理动态的大纲层级关系 */
    processedDirectory() {
      const directory = [...this.items];
      if (directory.length === 0) return [];
      const firstNode = directory[0];
      const resetLevel = firstNode.level === 1 ? 0 : firstNode.level - 1;
      return directory.map((item) => {
        return {
          ...item,
          level: item.level - resetLevel,
        };
      });
    },
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.list {
  list-style-type: none;
  padding-left: 15px;
  margin-top: 0;
}

.list-item {
  padding-left: 5px;
  line-height: 32px;
  font-size: 12px;
  color: #909399;
  max-width: 100%;
}

.list-item a {
  display: flex;
  align-items: center;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s ease;

  &:hover {
    color: #40a1fc;
    font-size: 13px;
  }

  &.active {
    color: #1890ff;
    font-weight: 600;
  }

  .arrow-icon {
    margin-right: 6px;
    font-size: 12px;
    transition: transform 0.2s ease;
    color: #999;
    flex-shrink: 0;
  }
  .text-ellipsis {
    flex: 1 1 auto;
    min-width: 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
</style>