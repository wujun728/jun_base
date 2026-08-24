<!-- @FileDescription: 办公-新建考核-指标模板库弹窗左边的指标导航 -->
<template>
  <div class="assess-left">
    <ul class="assess-left-ul">
      <li
        v-for="(item, index) in department"
        :key="index"
        :class="index == tabIndex ? 'active' : ''"
        @click="clickDepart(index, item.id)"
      >
        <span>{{ item.name }}</span>
      </li>
    </ul>
  </div>
</template>

<script>
import {
  assessTargetCateApi,
  assessTargetCateEditApi,
  assessTargetCateListApi,
  targetCateDeleteApi
} from '@/api/enterprise'
export default {
  name: 'DepartmentNot',
  props: {
    type: {
      type: Number,
      default: 0
    },
    isTemplate: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      department: [],
      tabIndex: 0,
      activeValue: '',
      optionValue: {
        id: 'template',
        option: []
      }
    }
  },
  mounted() {
    this.getTargetCate()
  },
  methods: {
    clickDepart(index, id) {
      this.tabIndex = index
      this.optionValue.id = id
      this.$emit('eventOptionData', this.optionValue)
    },
   
  
    getTargetCate() {
      const data = {
        types: this.type
      }
      assessTargetCateListApi(data).then((res) => {
        res.data === undefined ? (this.department = []) : (this.department = res.data)
        if (this.isTemplate == false) this.department.unshift({ name: '全部', id: '' })
        if (this.type == 1 && this.isTemplate) {
          this.department.unshift({ name: '我的模板', id: 'template' }, { name: '我的收藏', id: 'collect' })
        }
        this.optionDate()
      })
    },
    addCate() {
      this.$modalForm(assessTargetCateApi(this.type)).then(({ message }) => {
        this.getTargetCate()
      })
    },
    addDivsion() {
      this.$modalForm(assessTargetCateEditApi(this.activeValue)).then(({ message }) => {
        this.getTargetCate()
      })
    },
    hanleDelete() {
      this.$modalSure('你确定要删除这条指标模板分类吗').then(() => {
        targetCateDeleteApi(this.activeValue).then((res) => {
          this.getTargetCate()
        })
      })
    },
    optionDate() {
      const option = []
      if (this.department.length <= 0) {
        this.optionValue.option = option
        this.$emit('eventOptionData', this.optionValue)
      } else {
        this.department.map((value) => {
          option.push({ label: value.name, id: value.id })
        })
        this.optionValue.option = option
        this.$emit('eventOptionData', this.optionValue)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.assess-left {
  min-height: 460px;
  max-height: 510px;
  margin-left: -20px;
  overflow: auto;
  border-right: 1px solid #f3f3f3;
  .assess-left-ul {
    list-style: none;
    margin: 0;
    padding: 0;
    position: relative;
    li {
      font-size: 13px;
      padding: 14px 10px 14px 40px;
      color: #000000;
      cursor: pointer;
      .assess-left-icon {
        color: #aaaaaa;
        font-size: 13px;
      }
      .assess-left-more {
        color: #333333;
        text-align: right;
        position: absolute;
        right: 10px;
        transform: rotate(90deg);
      }
    }
    li.active {
      background-color: #f0fafe;
      border-right: 2px solid #1890ff;
      color: #1890ff;
      .assess-left-icon {
        color: #1890ff;
      }
    }
  }
}
.assess-left::-webkit-scrollbar {
  width: 5px;
  height: 1px;
}
.assess-left::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #f5f5f5;
}
.assess-left::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  background: #f0f2f5;
}
.right-item {
  line-height: 25px;
  cursor: pointer;
  margin-top: 10px;
  &:first-child {
    margin-top: 0;
  }
}
</style>
