<template>
  <div class="theme-selector">
    <div class="theme-title">主题选择</div>
    <div class="theme-list">
      <div
        v-for="theme in themes"
        :key="theme.id"
        :class="['theme-item', { active: currentTheme === theme.id }]"
        @click="changeTheme(theme.id)"
      >
        <div class="theme-preview">
          <div class="preview-sidebar" :style="{ backgroundColor: theme.colors.sidebar }"></div>
          <div class="preview-main">
            <div class="preview-navbar" :style="{ backgroundColor: theme.colors.navbar }"></div>
            <div class="preview-content">
              <div class="preview-tag" :style="{ backgroundColor: theme.colors.active }"></div>
            </div>
          </div>
        </div>
        <div class="theme-name">{{ theme.name }}</div>
        <i v-if="currentTheme === theme.id" class="el-icon-check theme-check"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ThemeSelector',
  data() {
    return {
      currentTheme: 'theme-0',
      themes: [
        {
          id: 'theme-0',
          name: '默认蓝色',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#2D8CF0'
          }
        },
        {
          id: 'theme-1',
          name: '经典蓝色',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#2D8CF0'
          }
        },
        {
          id: 'theme-2',
          name: '清新绿色',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#009688'
          }
        },
        {
          id: 'theme-3',
          name: '白色清爽',
          colors: {
            sidebar: '#FFF',
            navbar: '#FFF',
            active: '#409EFF'
          }
        },
        {
          id: 'theme-4',
          name: '灰绿风格',
          colors: {
            sidebar: '#EEE',
            navbar: '#222',
            active: '#009688'
          }
        },
        {
          id: 'theme-5',
          name: '活力红色',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#F56C6C'
          }
        },
        {
          id: 'theme-6',
          name: '沉稳棕色',
          colors: {
            sidebar: '#222',
            navbar: '#222',
            active: '#805322'
          }
        },
        {
          id: 'theme-7',
          name: '沉淀黑蓝',
          colors: {
            sidebar: '#222',
            navbar: '#222',
            active: '#2D8CF0'
          }
        },
        {
          id: 'theme-8',
          name: '简约灰蓝',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#4E5465'
          }
        },
        {
          id: 'theme-9',
          name: '优雅紫色',
          colors: {
            sidebar: '#222',
            navbar: '#FFF',
            active: '#A906B3'
          }
        },
        {
          id: 'theme-10',
          name: '简约草绿',
          colors: {
            sidebar: '#FFF',
            navbar: '#FFF',
            active: '#73D13D'
          }
        }
      ]
    }
  },
  mounted() {
    // 从 localStorage 读取保存的主题
    const savedTheme = localStorage.getItem('saAdminTheme') || 'theme-0'
    this.currentTheme = savedTheme
    this.applyTheme(savedTheme)
  },
  methods: {
    changeTheme(themeId) {
      this.currentTheme = themeId
      this.applyTheme(themeId)

      // 保存到 localStorage
      localStorage.setItem('saAdminTheme', themeId)

      // 触发事件通知父组件
      this.$emit('theme-change', themeId)

      // 显示成功提示
      this.$message.success('主题切换成功')
    },
    applyTheme(themeId) {
      // 移除所有主题 class
      const body = document.body
      const classList = body.className.split(' ')
      const filteredClasses = classList.filter(cls => !cls.startsWith('theme-'))

      // 添加新主题 class
      body.className = [...filteredClasses, themeId].join(' ')
    }
  }
}
</script>

<style lang="scss" scoped>
.theme-selector {
  padding: 10px 0;
}

.theme-title {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #333;
}

.theme-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 15px;
}

.theme-item {
  cursor: pointer;
  border: 2px solid #e4e7ed;
  border-radius: 4px;
  padding: 8px;
  transition: all 0.3s;
  position: relative;

  &:hover {
    border-color: #409EFF;
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  &.active {
    border-color: #409EFF;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
  }
}

.theme-preview {
  width: 100%;
  height: 60px;
  border-radius: 2px;
  overflow: hidden;
  display: flex;
  margin-bottom: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.preview-sidebar {
  width: 30%;
  height: 100%;
}

.preview-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.preview-navbar {
  height: 20%;
  border-bottom: 1px solid #f0f0f0;
}

.preview-content {
  flex: 1;
  background-color: #f5f5f5;
  padding: 4px;
}

.preview-tag {
  width: 60%;
  height: 6px;
  border-radius: 2px;
}

.theme-name {
  font-size: 12px;
  text-align: center;
  color: #606266;
  line-height: 1.4;
}

.theme-check {
  position: absolute;
  top: 8px;
  right: 8px;
  color: #409EFF;
  font-size: 16px;
  font-weight: bold;
}
</style>
