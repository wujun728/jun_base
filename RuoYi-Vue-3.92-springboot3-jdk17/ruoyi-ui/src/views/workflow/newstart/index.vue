<template>
  <!-- 新启页面 -->
  <div class="main">
    <div class="curr">
      <p>
        <span>最近使用</span>
      </p>
      <el-row class="tip-group" :gutter="10">
        <el-col :span="4" v-for="(item, index) in recentlyused" :key="index">
          <div class="tip" @click="goToTemplateDetail(item)" :title="item.name">
            <span>{{ item.name }}</span>
          </div>
        </el-col>
      </el-row>
      <el-empty v-if="emptyRecently" :image-size="50"></el-empty>
    </div>
    <div class="new">
      <p>
        <span>新启流程</span>
        <el-input size="medium" placeholder="请输入关键字搜索" v-model="searchInput" @input="searchFn">
          <i slot="suffix" @click="searchFn" class="el-input__icon el-icon-search"></i>
        </el-input>
      </p>
      <div v-for="(temp, index) in templateArr" :key="index">
        <div class="tem">
          <div>
            <i class="el-icon-caret-bottom"></i>
            <span>{{ temp.typeName }} ( {{ temp.templates.length }} )</span>
          </div>
          <div class="line"></div>
        </div>
        <FlowTemplate :key="key">
          <el-row class="tip-group" :gutter="10">
            <el-col :span="4" v-for="(item, index) in temp.templates" :key="index">
              <div class="tip" @click="goToTemplateDetail(item)" :title="item.name">
                <span>{{ item.name }}</span>
              </div>
            </el-col>
          </el-row>
        </FlowTemplate>
      </div>
      <el-empty v-if="emptyTemplate" :image-size="50"></el-empty>
    </div>
  </div>
</template>

<script>
import FlowTemplate from "./flow-template.vue";
import { getNewStartTemplateList } from "@/api/workflow/template";
import { getReceTemplateList } from "@/api/workflow/receTemplate";

export default {
  components: {
    FlowTemplate,
  },
  created() {
    this.getRecentlyUsedList();
    this.getList();
  },
  data() {
    return {
      emptyRecently: true,
      emptyTemplate: true,
      recentlyused: [], // 最近使用数据数组
      searchInput: "",
      templateArr: [], // 新启数组数据
      templateSearchArr: [], // 临时新启数组数据
      key: 0,
      showItem: "",
    };
  },

  methods: {
    // 查询最近使用模板列表
    getRecentlyUsedList() {
      getReceTemplateList().then((res) => {
        if (res.code == 200 && res.data) {
          this.recentlyused = res.data;
          this.emptyRecently = res.data && res.data.length > 0 ? false : true;
        }
      });
    },
    // 查询模板列表
    getList() {
      getNewStartTemplateList().then((res) => {
        if (res.code == 200 && res.data) {
          this.templateArr = res.data;
          this.templateSearchArr = res.data;
          this.emptyTemplate = res.data && res.data.length > 0 ? false : true;
        }
      });
    },
    // 查找模板
    searchFn() {
      this.templateArr = this.searchInput
        ? this.templateSearchArr
            .map((group) => ({
              ...group,
              templates: group.templates.filter((t) => t.name.includes(this.searchInput)),
            }))
            .filter((group) => group.templates.length > 0)
        : this.templateSearchArr;

      this.key++;
    },
    // 跳转模板表单
    async goToTemplateDetail(item) {
      this.$router.push({
        path: "/workflow/flowForm/" + new Date().getTime(),
        query: {
          pageType: "0",
          templateName: item.name,
          templateId: item.id,
        },
      });
    },
  },
};
</script>
 
<style rel="stylesheet/scss" lang = "scss" scoped>
.main {
  display: flex;
  flex-direction: column;
}

.curr,
.new {
  margin-top: 24px;
  padding: 13px 30px 20px 30px;
  background: #ffffff;
  box-shadow: 0px 22px 24px -20px rgba(238, 238, 243, 1);
  border-radius: 4px;
}
.new {
  flex: 1;
}

p {
  height: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    width: 80px;
    font-size: 16px;
    color: #262626;
    letter-spacing: 0;
    font-weight: 600;
  }

  ::v-deep .el-input__inner {
    width: 166px;
    &::placeholder {
      color: #bcbec3;
      font-size: 14px;
    }
  }

  .el-input {
    width: 166px;
  }
}
.tip-group {
  margin-top: 17px;

  &.col {
    display: none;
  }
}
::v-deep .el-col:hover .tip {
  background: url("../../../assets/images/flow2.png") center no-repeat content-box;
  background-origin: border-box;
  background-size: cover;
  color: #ffffff;
  font-weight: 600;
  box-shadow: 0 4px 12px 0 rgba(71, 139, 239, 0.4);
  top: -5px;
}
.tip {
  height: 60px;
  background: url("../../../assets/images/flow1.png") center no-repeat;
  background-origin: border-box;
  background-size: cover;
  border: 1px solid rgba(244, 245, 247, 1);
  border-radius: 5px;
  color: #636363;
  font-weight: 600;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  margin-bottom: 10px;
  span {
    padding: 0 20px 0 20px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  transition: all 0.3s;
  position: relative;
  box-shadow: none;
}

.tem {
  display: flex;
  font-weight: 600;
  font-size: 14px;
  align-items: center;
  color: #38699e;
  padding: 0 0 10px 0;
  span {
    padding-left: 10px;
    padding-right: 20px;
  }

  & span,
  i {
    cursor: pointer;
    user-select: none;
    color: #1c84c6;
  }

  .line {
    height: 1px;
    flex: 1;
    background-color: #f4f5f7;
  }
}

::v-deep .el-empty {
  padding: 0;
}
::v-deep .el-empty__description {
  margin-top: 10px;
  font-size: 12px;
}
</style>