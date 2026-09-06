# 表单构建器 (Form Builder)

基于 Vue 3 + Element Plus + TypeScript 的拖拽式表单构建器，采用**配置驱动架构**，新增组件只需添加配置即可。

## 目录结构

```
src/views/tool/build/
├── index.vue                        # 主页面：三栏布局（组件库 / 设计区 / 属性面板）
├── README.md                        # 本文件
│
├── types/
│   └── form.ts                      # 核心类型定义（FormNode, ComponentConfig, FormConfig 等）
│
├── config/
│   ├── index.ts                     # 配置导出聚合
│   ├── componentConfig.ts           # 组件元数据配置（18个基础 + 2个布局 + 1个按钮）
│   └── commonProps.ts               # 公共属性配置
│
├── composables/
│   └── useDesigner.ts               # 设计器全局状态管理（单例模式）
│
├── utils/
│   ├── schema.ts                    # Schema 工具函数（创建/查找/克隆/移动/删除节点）
│   ├── generator.ts                 # 代码生成器（生成 Vue3 + Element Plus 完整代码）
│   └── clipboard.ts                 # 剪贴板工具
│
└── components/
    ├── Toolbar.vue                  # 顶部工具栏
    ├── LeftPanel.vue                # 左侧组件库面板
    ├── DesignPanel.vue              # 中间设计区域容器
    ├── FormCanvas.vue               # 拖拽画布（vue-draggable-plus）
    ├── CanvasItem.vue               # 画布单项（递归渲染，支持无限嵌套）
    ├── RightPanel.vue               # 右侧属性面板
    ├── PropertyPanel.vue            # 动态属性编辑器
    ├── OptionEditor.vue             # 选项编辑器
    ├── PreviewDialog.vue            # 预览弹窗
    ├── PreviewItem.vue              # 预览渲染项
    └── CodeDialog.vue               # 代码展示弹窗
```

## 架构设计

### 配置驱动

整个设计器围绕 `ComponentConfig` 接口构建，**新增组件只需三步**：

1. `types/form.ts` — 在 `ComponentType` 中添加新类型
2. `config/componentConfig.ts` — 添加一条组件配置（含默认属性、属性面板配置）
3. `components/PreviewItem.vue` — 添加预览渲染分支

属性面板、代码生成器、画布渲染均自动适配。

### 数据流

```
LeftPanel (拖入) → useDesigner.addNode() → designerNodes (响应式数组)
                                                    ↓
CanvasItem (递归渲染) ← designerNodes ← FormCanvas (vue-draggable)
     ↓ (点击选中)
selectedId → selectedNode (计算属性) → PropertyPanel (动态编辑)
                                                    ↓
                                        generator.ts (代码生成)
```

### 节点数据结构

```typescript
interface FormNode {
  id: string
  type: ComponentType
  label: string
  field: string
  props: Record<string, unknown>
  children?: FormNode[] // 支持无限嵌套
}
```

布局组件（Row/Col）通过 `children` 实现无限嵌套。

## 支持的组件

### 基础组件（18个）

Input, Textarea, InputNumber, Password, Select, MultiSelect, Radio, Checkbox, Switch, Date, DateRange, Time, DateTime, Cascader, TreeSelect, Slider, Rate, ColorPicker, Upload

### 布局组件（2个）

Row（支持 gutter/justify/align），Col（支持 span/offset/push/pull）

### 操作组件（1个）

Button（支持 type/size/icon/loading/plain/round/circle/disabled）

## 表单属性

| 属性           | 说明                  |
| -------------- | --------------------- |
| labelWidth     | 标签宽度              |
| labelPosition  | 标签位置（左/右/顶）  |
| size           | 尺寸（大/默认/小）    |
| inline         | 行内模式              |
| disabled       | 全局禁用              |
| showFormButton | 是否生成提交/重置按钮 |
| formRefName    | 自定义表单 ref 名称   |
| formModelName  | 自定义表单 model 名称 |
| rulesName      | 自定义校验规则名称    |

## 代码生成

生成的代码特点：

- Vue 3 `<script setup>` + TypeScript
- Element Plus 组件
- 自动生成 `formModel`（reactive）
- 自动生成 `rules`（校验规则）
- 自动生成 `options`（选项数据）
- 自动生成 `submitForm` / `resetForm` 方法
- 支持自定义变量命名
