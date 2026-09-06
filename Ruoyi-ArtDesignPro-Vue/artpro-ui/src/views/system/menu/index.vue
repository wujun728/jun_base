<template>
  <div class="art-full-height">
    <ArtSearchBar
      v-show="showSearchBar"
      v-model="filters"
      :items="searchItems"
      :show-expand="false"
      @search="load"
      @reset="reset"
    />
    <ElCard class="art-table-card">
      <ArtTableHeader
        v-model:columns="columnChecks"
        v-model:show-search-bar="showSearchBar"
        :loading="loading"
        @refresh="load"
      >
        <template #left>
          <ElSpace wrap>
            <ElButton v-if="hasAuth('system:menu:add')" type="primary" @click="openDialog()"
              >新增菜单</ElButton
            >
            <ElButton @click="toggleExpand">{{ expanded ? '收起' : '展开' }}</ElButton>
            <ElButton v-if="hasAuth('system:menu:edit')" @click="saveSort">保存排序</ElButton>
          </ElSpace>
        </template>
      </ArtTableHeader>
      <ArtTable
        ref="tableRef"
        row-key="menuId"
        :loading="loading"
        :data="treeData"
        :columns="columns"
        :pagination="undefined"
      />
    </ElCard>

    <ElDrawer v-model="visible" :title="drawerTitle" size="780px" class="menu-edit-drawer">
      <div class="menu-edit-form-wrap">
        <div class="menu-type-row">
          <div class="menu-type-label">类型</div>
          <div class="menu-type-control">
            <ElRadioGroup v-model="form.displayType" class="menu-type-radio">
              <ElRadioButton v-for="item in typeOptions" :key="item.value" :value="item.value">{{
                item.label
              }}</ElRadioButton>
            </ElRadioGroup>
            <div class="menu-form-tip">新建时可直接选择类型。按钮权限仍需挂在具体菜单下。</div>
          </div>
        </div>
        <ArtForm
          ref="formRef"
          v-model="form"
          :items="drawerFormItems"
          :rules="rules"
          :show-submit="false"
          :show-reset="false"
          :span="12"
          :gutter="16"
          label-width="96px"
        >
          <template #icon>
            <div class="menu-icon-field">
              <div class="menu-icon-preview">
                <ArtSvgIcon :icon="form.icon || 'ri:menu-line'" />
              </div>
              <div class="menu-icon-value">{{ form.icon || '请选择图标' }}</div>
              <ElButton
                class="menu-icon-trigger"
                type="primary"
                text
                :disabled="mode === 'view'"
                @click="openIconPicker"
                >选择图标</ElButton
              >
            </div>
          </template>
        </ArtForm>
      </div>
      <template #footer>
        <ElButton @click="visible = false">取消</ElButton>
        <ElButton v-if="mode !== 'view'" type="primary" @click="submit">确定</ElButton>
      </template>
    </ElDrawer>

    <ElDrawer
      v-model="iconPickerVisible"
      title="选择图标"
      size="min(780px, 92vw)"
      append-to-body
      class="icon-picker-drawer"
    >
      <ElTabs v-model="activeIconSource" class="icon-picker-tabs">
        <ElTabPane label="Remix Icon" name="remix">
          <div class="icon-picker-layout">
            <aside class="icon-picker-sidebar">
              <button
                v-for="category in iconCategoriesWithCount"
                :key="category.key"
                type="button"
                class="icon-category-item"
                :class="{ 'is-active': activeIconCategory === category.key }"
                @click="activeIconCategory = category.key"
              >
                <span>{{ category.label }}</span>
                <span>{{ category.count }}</span>
              </button>
            </aside>
            <section class="icon-picker-main">
              <div class="icon-picker-search">
                <ArtSvgIcon icon="ri:search-line" />
                <ElInput
                  v-model="iconKeyword"
                  clearable
                  :placeholder="`搜索 ${filteredIcons.length} 个图标`"
                />
              </div>
              <div class="icon-picker-grid">
                <button
                  v-for="icon in filteredIcons"
                  :key="icon"
                  type="button"
                  class="icon-picker-item"
                  :class="{ 'is-active': form.icon === icon }"
                  @click="selectIcon(icon)"
                >
                  <ArtSvgIcon
                    :icon="icon"
                    class="icon-picker-svg"
                    :class="{ 'icon-picker-svg--motion': activeIconCategory === 'dynamic' }"
                  />
                  <span>{{ icon }}</span>
                </button>
              </div>
            </section>
          </div>
        </ElTabPane>
        <ElTabPane label="Iconify" name="iconify">
          <div class="icon-picker-layout">
            <aside class="icon-picker-sidebar">
              <button
                v-for="category in iconifyCategoriesWithCount"
                :key="category.key"
                type="button"
                class="icon-category-item"
                :class="{ 'is-active': activeIconifyCategory === category.key }"
                @click="activeIconifyCategory = category.key"
              >
                <span>{{ category.label }}</span>
                <span>{{ category.count }}</span>
              </button>
            </aside>
            <section class="icon-picker-main">
              <div class="icon-picker-search iconify-search">
                <ElRadioGroup v-model="activeIconifySet" class="iconify-set-radio">
                  <ElRadioButton v-for="item in iconifyIconSets" :key="item.key" :value="item.key">
                    {{ item.label }}
                  </ElRadioButton>
                </ElRadioGroup>
                <div class="iconify-search-input">
                  <ArtSvgIcon icon="ri:search-line" />
                  <ElInput
                    v-model="iconifyKeyword"
                    clearable
                    :placeholder="`搜索 ${filteredIconifyIcons.length} 个图标`"
                  />
                </div>
              </div>
              <div class="icon-picker-grid">
                <button
                  v-for="icon in filteredIconifyIcons"
                  :key="icon"
                  type="button"
                  class="icon-picker-item"
                  :class="{ 'is-active': form.icon === icon }"
                  @click="selectIcon(icon)"
                >
                  <ArtSvgIcon
                    :icon="icon"
                    class="icon-picker-svg"
                    :class="{ 'icon-picker-svg--motion': activeIconifyCategory === 'motion' }"
                  />
                  <span>{{ icon }}</span>
                </button>
              </div>
            </section>
          </div>
        </ElTabPane>
      </ElTabs>
    </ElDrawer>
  </div>
</template>

<script setup lang="ts">
  import { h, nextTick } from 'vue'
  import { ElMessage, ElMessageBox, ElSwitch } from 'element-plus'
  import eosIcons from '@iconify-json/eos-icons/icons.json'
  import epIcons from '@iconify-json/ep/icons.json'
  import lineMdIcons from '@iconify-json/line-md/icons.json'
  import riIcons from '@iconify-json/ri/icons.json'
  import svgSpinnersIcons from '@iconify-json/svg-spinners/icons.json'
  import tablerIcons from '@iconify-json/tabler/icons.json'
  import ArtButtonTable from '@/components/core/forms/art-button-table/index.vue'
  import ArtSvgIcon from '@/components/core/base/art-svg-icon/index.vue'
  import {
    addMenu,
    fetchMenu,
    fetchMenuList,
    fetchMenuTree,
    removeMenu,
    updateMenu,
    updateMenuSort
  } from '@/api/system/menu'
  import { useAuth } from '@/hooks/core/useAuth'
  import { useTableColumns } from '@/hooks/core/useTableColumns'
  import { buildTree, flattenTree } from '@/utils/system/tree'
  import { normalizeLocalIcon } from '@/utils/ui/iconify-loader'
  import type { Entity } from '@/api/system/types'

  defineOptions({ name: 'SystemMenu' })
  const { hasAuth } = useAuth()
  const loading = ref(false),
    showSearchBar = ref(true),
    visible = ref(false),
    expanded = ref(false)
  const mode = ref<'add' | 'edit' | 'view'>('add')
  const iconPickerVisible = ref(false),
    iconKeyword = ref(''),
    activeIconCategory = ref('all'),
    activeIconSource = ref<'remix' | 'iconify'>('remix'),
    iconifyKeyword = ref(''),
    activeIconifySet = ref<IconifySetKey>('ep'),
    activeIconifyCategory = ref('all')
  const rows = ref<Entity[]>([]),
    options = ref<Entity[]>([])
  const statusChangingIds = ref<number[]>([])
  const filters = reactive({ menuName: '', status: '' }),
    form = reactive<Entity>({})
  const tableRef = ref<any>(),
    formRef = ref<any>()
  const treeData = computed(() => buildTree(rows.value, 'menuId', 'parentId'))
  const isLinkType = computed(() => ['iframe', 'external'].includes(String(form.displayType)))
  const typeOptions = [
    { label: '目录', value: 'M' },
    { label: '菜单', value: 'C' },
    { label: '按钮', value: 'F' },
    { label: '内嵌', value: 'iframe' },
    { label: '外链', value: 'external' }
  ]
  type IconifySetKey = 'ep' | 'tabler' | 'line-md' | 'svg-spinners' | 'eos-icons'
  const allIcons = Object.keys(riIcons.icons).map((name) => `ri:${name}`)
  const iconifyIconSets: { key: IconifySetKey; label: string; icons: string[] }[] = [
    {
      key: 'ep',
      label: 'Element Plus',
      icons: Object.keys(epIcons.icons).map((name) => `ep:${name}`)
    },
    {
      key: 'tabler',
      label: 'Tabler',
      icons: Object.keys(tablerIcons.icons).map((name) => `tabler:${name}`)
    },
    {
      key: 'line-md',
      label: 'Line Motion',
      icons: Object.keys(lineMdIcons.icons).map((name) => `line-md:${name}`)
    },
    {
      key: 'svg-spinners',
      label: 'SVG Spinners',
      icons: Object.keys(svgSpinnersIcons.icons).map((name) => `svg-spinners:${name}`)
    },
    {
      key: 'eos-icons',
      label: 'EOS Icons',
      icons: Object.keys(eosIcons.icons).map((name) => `eos-icons:${name}`)
    }
  ]
  const iconifyCategories = [
    { key: 'all', label: '全部', keywords: [] },
    {
      key: 'system',
      label: '系统',
      keywords: ['setting', 'settings', 'menu', 'apps', 'layout', 'dashboard', 'panel', 'control']
    },
    {
      key: 'user',
      label: '用户',
      keywords: ['user', 'users', 'team', 'account', 'avatar', 'id', 'badge', 'login']
    },
    {
      key: 'data',
      label: '数据',
      keywords: ['database', 'server', 'cloud', 'chart', 'analytics', 'table', 'list', 'stack']
    },
    {
      key: 'form',
      label: '表单',
      keywords: ['form', 'input', 'select', 'checkbox', 'radio', 'toggle', 'edit', 'pencil']
    },
    {
      key: 'document',
      label: '文档',
      keywords: ['file', 'folder', 'document', 'book', 'clipboard', 'copy', 'archive', 'paper']
    },
    {
      key: 'business',
      label: '业务',
      keywords: ['briefcase', 'calendar', 'ticket', 'project', 'task', 'clock', 'timeline', 'flag']
    },
    {
      key: 'security',
      label: '安全',
      keywords: ['lock', 'key', 'shield', 'fingerprint', 'scan', 'password', 'safe', 'access']
    },
    {
      key: 'message',
      label: '消息',
      keywords: ['bell', 'message', 'chat', 'mail', 'notification', 'phone', 'send', 'inbox']
    },
    {
      key: 'media',
      label: '媒体',
      keywords: ['image', 'photo', 'camera', 'video', 'player', 'music', 'volume', 'microphone']
    },
    {
      key: 'device',
      label: '设备',
      keywords: ['monitor', 'device', 'mobile', 'phone', 'tablet', 'printer', 'keyboard', 'wifi']
    },
    {
      key: 'navigation',
      label: '导航',
      keywords: ['arrow', 'chevron', 'caret', 'route', 'map', 'location', 'compass', 'direction']
    },
    {
      key: 'commerce',
      label: '交易',
      keywords: ['coin', 'money', 'wallet', 'cart', 'shop', 'credit-card', 'receipt', 'discount']
    },
    {
      key: 'development',
      label: '开发',
      keywords: ['code', 'terminal', 'bug', 'git', 'api', 'webhook', 'braces', 'command']
    },
    {
      key: 'motion',
      label: '动态特效',
      keywords: [
        'loader',
        'loading',
        'refresh',
        'reload',
        'rotate',
        'loop',
        'repeat',
        'transition',
        'transform',
        'sparkles',
        'sparkle',
        'magic',
        'wand',
        'player',
        'play',
        'pause',
        'animation',
        'activity',
        'animate',
        'pulse',
        'spinner',
        'spin',
        'bars',
        'dots',
        'blocks',
        'shuffle',
        'wave',
        'bounce',
        'ring',
        'revolve',
        'eclipse',
        'clock',
        'counting',
        'progress',
        'sync',
        'deploy',
        'bootstrapping',
        'background-tasks',
        'cronjob',
        'lifecycle',
        'modified',
        'new',
        'activate',
        'chains',
        'bolt',
        'flash'
      ]
    },
    {
      key: 'status',
      label: '状态',
      keywords: ['check', 'close', 'circle', 'info', 'warning', 'alert', 'error', 'success', 'ban']
    },
    {
      key: 'other',
      label: '其他',
      keywords: []
    }
  ]
  const iconCategories = [
    { key: 'all', label: '全部', keywords: [] },
    { key: 'arrow', label: '箭头', keywords: ['arrow-', 'corner-', 'expand-', 'collapse-'] },
    {
      key: 'building',
      label: '建筑物',
      keywords: ['building', 'home', 'hotel', 'bank', 'store', 'community', 'government']
    },
    {
      key: 'business',
      label: '商务',
      keywords: [
        'briefcase',
        'calendar',
        'customer',
        'service',
        'profile',
        'project',
        'medal',
        'award'
      ]
    },
    {
      key: 'communication',
      label: '通信',
      keywords: ['chat', 'message', 'mail', 'phone', 'send', 'notification', 'question']
    },
    {
      key: 'design',
      label: '设计',
      keywords: ['palette', 'brush', 'pen', 'edit', 'crop', 'contrast', 'magic', 'layout']
    },
    {
      key: 'development',
      label: '开发',
      keywords: ['code', 'terminal', 'git', 'bug', 'braces', 'command', 'css', 'html']
    },
    {
      key: 'device',
      label: '设备',
      keywords: ['computer', 'device', 'phone', 'tablet', 'mac', 'tv', 'wifi', 'keyboard', 'mouse']
    },
    {
      key: 'document',
      label: '文档',
      keywords: ['file', 'folder', 'article', 'book', 'clipboard', 'todo', 'draft']
    },
    {
      key: 'editor',
      label: '编辑',
      keywords: ['align', 'bold', 'font', 'heading', 'indent', 'list', 'text', 'paragraph']
    },
    {
      key: 'finance',
      label: '金融',
      keywords: ['bank', 'coin', 'coupon', 'currency', 'funds', 'money', 'price', 'wallet']
    },
    {
      key: 'food',
      label: '食物',
      keywords: ['beer', 'cake', 'cup', 'drink', 'restaurant', 'goblet', 'knife', 'bowl']
    },
    {
      key: 'health',
      label: '健康',
      keywords: ['heart', 'medicine', 'mental', 'nurse', 'surgical', 'capsule', 'virus', 'health']
    },
    {
      key: 'media',
      label: '媒体',
      keywords: ['image', 'video', 'music', 'camera', 'play', 'pause', 'volume', 'film']
    },
    {
      key: 'dynamic',
      label: '动态特效',
      keywords: [
        'loader',
        'loading',
        'refresh',
        'restart',
        'loop',
        'repeat',
        'replay',
        'rotate',
        'exchange',
        'shuffle',
        'pulse',
        'flash',
        'spark',
        'sparkling',
        'magic',
        'transition',
        'transform',
        'activity',
        'motion',
        'spinner',
        'spin',
        'sync',
        'progress',
        'bars',
        'dots',
        'wave',
        'bounce',
        'ring',
        'clock',
        'count',
        'upload',
        'download',
        'install',
        'rocket',
        'send',
        'forward',
        'backward',
        'skip',
        'stop',
        'record',
        'eject',
        'cast',
        'bolt',
        'play',
        'pause',
        'timer',
        'time',
        'history',
        'speed',
        'run',
        'walk',
        'live',
        'fire'
      ]
    },
    {
      key: 'system',
      label: '系统',
      keywords: ['settings', 'dashboard', 'menu', 'apps', 'database', 'server', 'terminal', 'code']
    },
    { key: 'user', label: '用户', keywords: ['user', 'team', 'group', 'account', 'admin'] },
    { key: 'map', label: '地图', keywords: ['map', 'pin', 'route', 'compass', 'earth', 'global'] },
    {
      key: 'logo',
      label: '品牌',
      keywords: ['github', 'wechat', 'alipay', 'windows', 'apple', 'chrome']
    },
    {
      key: 'weather',
      label: '天气',
      keywords: ['sun', 'moon', 'cloud', 'rain', 'snow', 'windy', 'mist', 'thunderstorms']
    },
    {
      key: 'other',
      label: '其他',
      keywords: []
    }
  ]
  const getCategoryIcons = (categoryKey: string) => {
    const category = iconCategories.find((item) => item.key === categoryKey)
    if (!category || category.key === 'all') return allIcons
    if (category.key === 'other') {
      const usedKeywords = iconCategories
        .filter((item) => !['all', 'other'].includes(item.key))
        .flatMap((item) => item.keywords)
      return allIcons.filter((icon) => !usedKeywords.some((keyword) => icon.includes(keyword)))
    }
    return allIcons.filter((icon) => category.keywords.some((keyword) => icon.includes(keyword)))
  }
  const iconCategoriesWithCount = computed(() =>
    iconCategories.map((category) => ({
      ...category,
      count: getCategoryIcons(category.key).length
    }))
  )
  const filteredIcons = computed(() => {
    const keyword = iconKeyword.value.trim().toLowerCase()
    const icons = getCategoryIcons(activeIconCategory.value)
    return keyword ? icons.filter((icon) => icon.includes(keyword)) : icons
  })
  const currentIconifySet = computed(
    () => iconifyIconSets.find((item) => item.key === activeIconifySet.value) || iconifyIconSets[0]
  )
  const getIconifyCategoryIcons = (categoryKey: string) => {
    const icons = currentIconifySet.value.icons
    const category = iconifyCategories.find((item) => item.key === categoryKey)
    if (!category || category.key === 'all') return icons
    if (category.key === 'other') {
      const usedKeywords = iconifyCategories
        .filter((item) => !['all', 'other'].includes(item.key))
        .flatMap((item) => item.keywords)
      return icons.filter((icon) => !usedKeywords.some((keyword) => icon.includes(keyword)))
    }
    return icons.filter((icon) => category.keywords.some((keyword) => icon.includes(keyword)))
  }
  const iconifyCategoriesWithCount = computed(() =>
    iconifyCategories.map((category) => ({
      ...category,
      count: getIconifyCategoryIcons(category.key).length
    }))
  )
  const filteredIconifyIcons = computed(() => {
    const keyword = iconifyKeyword.value.trim().toLowerCase()
    const icons = getIconifyCategoryIcons(activeIconifyCategory.value)
    return keyword ? icons.filter((icon) => icon.includes(keyword)) : icons
  })
  const searchItems = [
    { key: 'menuName', label: '菜单名称' },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      props: {
        options: [
          { label: '正常', value: '0' },
          { label: '停用', value: '1' }
        ]
      }
    }
  ]
  const rules = {
    parentId: [{ required: true, message: '请选择上级菜单' }],
    menuName: [{ required: true, message: '请输入名称' }],
    orderNum: [{ required: true, message: '请输入排序' }],
    path: [{ required: true, message: '请输入路由地址' }],
    externalLink: [{ required: true, message: '请输入外部链接' }],
    perms: [{ required: true, message: '请输入权限标识' }]
  }
  const formItems = computed(() => [
    {
      key: 'parentId',
      label: '上级菜单',
      type: 'treeselect',
      span: 12,
      props: {
        data: [{ id: 0, label: '主类目', children: options.value }],
        nodeKey: 'id',
        checkStrictly: true
      }
    },
    { key: 'menuName', label: form.displayType === 'F' ? '权限名称' : '菜单名称', span: 12 },
    {
      key: 'path',
      label: '路由地址',
      span: 12,
      hidden: ['F', 'external'].includes(form.displayType),
      props: {
        placeholder: form.displayType === 'iframe' ? '如：/outside/iframe/docs' : '请输入路由地址'
      }
    },
    {
      key: 'component',
      label: '组件路径',
      span: 12,
      hidden: form.displayType !== 'C',
      props: { placeholder: '如：system/user/index' }
    },
    {
      key: 'externalLink',
      label: '外部链接',
      span: 12,
      hidden: !isLinkType.value,
      props: { placeholder: '如：https://www.example.com' }
    },
    {
      key: 'perms',
      label: '权限标识',
      span: 12,
      hidden: form.displayType === 'M' || isLinkType.value
    },
    { key: 'icon', label: '图标', span: 24, hidden: form.displayType === 'F' },
    {
      key: 'orderNum',
      label: form.displayType === 'F' ? '权限排序' : '菜单排序',
      type: 'number',
      span: 12,
      props: { min: 0 }
    },
    {
      key: 'showTextBadge',
      label: '文本徽章',
      span: 12,
      hidden: form.displayType === 'F',
      props: { clearable: true, placeholder: '如：New、Hot' }
    },
    { key: 'activePath', label: '激活路径', span: 12, hidden: form.displayType !== 'C' },
    {
      key: '__settingsDivider',
      label: '',
      span: 24,
      hidden: form.displayType === 'F',
      render: () => h('div', { class: 'menu-form-section' }, [h('span', '其他设置')])
    },
    { key: 'enabled', label: '是否启用', type: 'switch', span: 12 },
    {
      key: 'hidden',
      label: '隐藏菜单',
      type: 'switch',
      span: 12,
      hidden: form.displayType === 'F'
    },
    { key: 'cache', label: '页面缓存', type: 'switch', span: 12, hidden: form.displayType !== 'C' },
    {
      key: 'showBadge',
      label: '显示徽章',
      type: 'switch',
      span: 12,
      hidden: form.displayType === 'F'
    },
    {
      key: 'fixedTab',
      label: '固定标签',
      type: 'switch',
      span: 12,
      hidden: form.displayType !== 'C'
    },
    {
      key: 'isHideTab',
      label: '标签隐藏',
      type: 'switch',
      span: 12,
      hidden: form.displayType === 'F'
    },
    {
      key: 'isFullPage',
      label: '全屏页面',
      type: 'switch',
      span: 12,
      hidden: form.displayType !== 'C'
    },
    {
      key: 'newTab',
      label: '新标签打开',
      type: 'switch',
      span: 12,
      hidden: form.displayType === 'F'
    }
  ])
  const drawerFormItems = computed(() =>
    formItems.value.map((item) => ({
      ...item,
      props: { ...item.props, disabled: mode.value === 'view' }
    }))
  )
  const drawerTitle = computed(
    () =>
      `${mode.value === 'view' ? '查看' : mode.value === 'edit' ? '编辑' : '新建'}${typeOptions.find((item) => item.value === form.displayType)?.label || '菜单'}`
  )
  const { columns, columnChecks } = useTableColumns(() => [
    { prop: 'menuName', label: '菜单名称', minWidth: 180 },
    {
      prop: 'icon',
      label: '图标',
      width: 80,
      formatter: (row: Entity) => h(ArtSvgIcon, { icon: row.icon, class: 'text-xl' })
    },
    {
      prop: 'orderNum',
      label: '排序',
      width: 90,
      formatter: (row: Entity) =>
        h('input', {
          class: 'w-14 rounded border border-g-300 px-2 py-1',
          type: 'number',
          value: row.orderNum,
          onInput: (e: Event) => (row.orderNum = Number((e.target as HTMLInputElement).value))
        })
    },
    { prop: 'perms', label: '权限标识', minWidth: 180 },
    { prop: 'component', label: '组件路径', minWidth: 170 },
    {
      prop: 'status',
      label: '是否启用',
      width: 120,
      formatter: (row: Entity) =>
        h(ElSwitch, {
          modelValue: row.status === '0',
          inlinePrompt: true,
          activeText: '启用',
          inactiveText: '停用',
          disabled: !hasAuth('system:menu:edit') || isStatusChanging(row),
          loading: isStatusChanging(row),
          onChange: (enabled: string | number | boolean) => toggleMenuStatus(row, Boolean(enabled))
        })
    },
    {
      prop: 'operation',
      label: '操作',
      minWidth: 220,
      fixed: 'right',
      align: 'right',
      formatter: (row: Entity) =>
        h('div', [
          hasAuth('system:menu:query')
            ? h(ArtButtonTable, { type: 'view', onClick: () => openDialog(row, 0, 'view') })
            : null,
          hasAuth('system:menu:add')
            ? h(ArtButtonTable, { type: 'add', onClick: () => openDialog(undefined, row.menuId) })
            : null,
          hasAuth('system:menu:edit')
            ? h(ArtButtonTable, { type: 'edit', onClick: () => openDialog(row) })
            : null,
          hasAuth('system:menu:remove')
            ? h(ArtButtonTable, { type: 'delete', onClick: () => removeRow(row) })
            : null
        ])
    }
  ])
  async function load() {
    loading.value = true
    try {
      rows.value = await fetchMenuList(filters)
      setExpandedRows(true)
    } finally {
      loading.value = false
    }
  }
  function reset() {
    Object.assign(filters, { menuName: '', status: '' })
    load()
  }
  async function openDialog(
    row?: Entity,
    parentId = 0,
    nextMode: 'add' | 'edit' | 'view' = row ? 'edit' : 'add'
  ) {
    Object.keys(form).forEach((key) => delete form[key])
    Object.assign(form, {
      parentId,
      displayType: 'M',
      orderNum: 0,
      enabled: true,
      hidden: false,
      cache: false,
      showBadge: false,
      isHideTab: false,
      fixedTab: false,
      isFullPage: false,
      newTab: false
    })
    options.value = await fetchMenuTree()
    if (row) {
      Object.assign(form, await fetchMenu(row.menuId))
      form.displayType = resolveDisplayType(form)
      Object.assign(form, {
        icon: normalizeLocalIcon(form.icon),
        enabled: form.status !== '1',
        hidden: form.visible === '1',
        cache: form.isCache === '0',
        showBadge: form.showBadge === '1',
        isHideTab: form.isHideTab === '1',
        fixedTab: form.fixedTab === '1',
        isFullPage: form.isFullPage === '1',
        newTab: form.newTab === '1'
      })
    }
    mode.value = nextMode
    visible.value = true
  }
  function resolveDisplayType(data: Entity) {
    if (data.externalLink || /^https?:\/\//.test(String(data.path || '')))
      return data.isFrame === '0' ? 'iframe' : 'external'
    return data.menuType || 'M'
  }
  function toMenuPayload(data: Entity) {
    const payload: Entity = {
      ...data,
      status: data.enabled ? '0' : '1',
      visible: data.hidden ? '1' : '0',
      isCache: data.cache ? '0' : '1',
      showBadge: data.showBadge ? '1' : '0',
      isHideTab: data.isHideTab ? '1' : '0',
      fixedTab: data.fixedTab ? '1' : '0',
      isFullPage: data.isFullPage ? '1' : '0',
      newTab: data.newTab ? '1' : '0',
      icon: data.displayType === 'F' ? '#' : normalizeLocalIcon(data.icon),
      showTextBadge: data.showTextBadge || ''
    }
    if (isLinkType.value) {
      payload.menuType = Number(data.parentId) === 0 ? 'M' : 'C'
      payload.isFrame = data.displayType === 'iframe' ? '0' : '1'
      payload.component = ''
      payload.perms = ''
      if (data.displayType === 'external') payload.path = data.externalLink
    } else {
      payload.menuType = data.displayType
      payload.isFrame = '1'
      payload.externalLink = ''
      if (data.displayType === 'F') {
        payload.path = ''
        payload.component = ''
      }
    }
    ;['displayType', 'enabled', 'hidden', 'cache'].forEach((key) => delete payload[key])
    return payload
  }
  async function submit() {
    await formRef.value?.validate()
    await (form.menuId ? updateMenu(toMenuPayload(form)) : addMenu(toMenuPayload(form)))
    visible.value = false
    load()
  }
  async function removeRow(row: Entity) {
    await ElMessageBox.confirm(`确定删除菜单“${row.menuName}”吗？`, '提示', { type: 'warning' })
    await removeMenu(row.menuId)
    load()
  }
  function isStatusChanging(row: Entity) {
    return statusChangingIds.value.includes(Number(row.menuId))
  }
  function toStatusPayload(row: Entity, status: string) {
    const payload = { ...row }
    delete payload.children
    return { ...payload, status }
  }
  async function toggleMenuStatus(row: Entity, enabled: boolean) {
    const menuId = Number(row.menuId)
    const previousStatus = row.status
    const nextStatus = enabled ? '0' : '1'

    if (previousStatus === nextStatus || statusChangingIds.value.includes(menuId)) {
      return
    }

    statusChangingIds.value = [...statusChangingIds.value, menuId]
    row.status = nextStatus
    try {
      await updateMenu(toStatusPayload(row, nextStatus))
      ElMessage.success(`${row.menuName}已${enabled ? '启用' : '停用'}`)
      await load()
    } catch (error) {
      row.status = previousStatus
      throw error
    } finally {
      statusChangingIds.value = statusChangingIds.value.filter((id) => id !== menuId)
    }
  }
  async function saveSort() {
    const list = flattenTree(treeData.value)
    await updateMenuSort(
      list.map((row) => row.menuId),
      list.map((row) => row.orderNum)
    )
    load()
  }
  function toggleExpand() {
    setExpandedRows(!expanded.value)
  }
  function setExpandedRows(isExpanded: boolean) {
    expanded.value = isExpanded
    nextTick(() =>
      flattenTree(treeData.value).forEach((row) =>
        tableRef.value?.elTableRef?.toggleRowExpansion(row, expanded.value)
      )
    )
  }
  function openIconPicker() {
    const currentIcon = String(form.icon || '')
    if (currentIcon.startsWith('ep:')) {
      activeIconSource.value = 'iconify'
      activeIconifySet.value = 'ep'
    } else if (currentIcon.startsWith('tabler:')) {
      activeIconSource.value = 'iconify'
      activeIconifySet.value = 'tabler'
    } else if (currentIcon.startsWith('line-md:')) {
      activeIconSource.value = 'iconify'
      activeIconifySet.value = 'line-md'
    } else if (currentIcon.startsWith('svg-spinners:')) {
      activeIconSource.value = 'iconify'
      activeIconifySet.value = 'svg-spinners'
      activeIconifyCategory.value = 'motion'
    } else if (currentIcon.startsWith('eos-icons:')) {
      activeIconSource.value = 'iconify'
      activeIconifySet.value = 'eos-icons'
    } else {
      activeIconSource.value = 'remix'
    }
    iconPickerVisible.value = true
  }
  function selectIcon(icon: string) {
    form.icon = icon
    iconPickerVisible.value = false
  }
  load()
</script>

<style scoped>
  .menu-edit-form-wrap {
    max-width: 720px;
    padding-top: 12px;
    margin: 0 auto;
  }

  :global(.menu-edit-drawer .el-drawer__header) {
    margin-bottom: 12px;
  }

  :global(.menu-edit-drawer .el-drawer__body) {
    padding-top: 0;
  }

  :global(.menu-edit-drawer .art-form) {
    padding-top: 0;
  }

  :global(.menu-edit-drawer .el-form .el-row) {
    row-gap: 12px;
  }

  :global(.menu-edit-drawer .el-form-item__content) {
    min-width: 0;
  }

  :global(.menu-edit-drawer .el-input),
  :global(.menu-edit-drawer .el-select),
  :global(.menu-edit-drawer .el-input-number) {
    width: 100%;
  }

  .menu-type-row {
    display: grid;
    grid-template-columns: 96px minmax(0, 1fr);
    align-items: center;
    padding: 0 16px 16px;
  }

  .menu-type-label {
    padding-right: 12px;
    font-size: var(--el-font-size-base);
    line-height: 1;
    color: var(--el-text-color-regular);
    text-align: right;
  }

  .menu-type-control {
    min-width: 0;
    flex: 1;
  }

  .menu-type-radio {
    height: 36px;
  }

  .menu-type-radio :deep(.el-radio-button__inner) {
    display: inline-flex;
    min-width: 72px;
    height: 36px;
    align-items: center;
    justify-content: center;
    padding: 0 16px;
    font-size: var(--el-font-size-base);
    font-weight: 400;
    line-height: 1;
    vertical-align: top;
  }

  .menu-form-tip {
    margin-top: 6px;
    font-size: 12px;
    line-height: 18px;
    color: var(--el-text-color-secondary);
  }

  :global(.menu-form-section) {
    display: flex;
    align-items: center;
    width: 100%;
    gap: 16px;
    padding: 12px 0 0;
    font-size: 15px;
    font-weight: 600;
    color: var(--el-text-color-primary);
  }

  :global(.menu-form-section::before),
  :global(.menu-form-section::after) {
    flex: 1;
    height: 1px;
    content: '';
    background: var(--el-border-color-lighter);
  }

  .menu-icon-field {
    display: flex;
    align-items: center;
    width: min(100%, 520px);
    height: 34px;
    overflow: hidden;
    color: var(--el-text-color-primary);
    border: 1px solid var(--el-border-color);
    border-radius: var(--el-border-radius-base);
  }

  .menu-icon-preview {
    display: flex;
    flex: 0 0 58px;
    align-items: center;
    justify-content: center;
    height: 100%;
    font-size: 18px;
    color: var(--el-text-color-regular);
    border-right: 1px solid var(--el-border-color);
  }

  .menu-icon-value {
    flex: 1 1 auto;
    min-width: 0;
    padding: 0 14px;
    overflow: hidden;
    font-size: var(--el-font-size-base);
    line-height: 34px;
    text-overflow: ellipsis;
    white-space: nowrap;
    border-right: 1px solid var(--el-border-color);
  }

  .menu-icon-trigger {
    flex: 0 0 104px;
    height: 100%;
    font-size: var(--el-font-size-base);
    border-radius: 0;
  }

  .icon-picker-tabs {
    height: calc(100vh - 112px);
  }

  .icon-picker-tabs :deep(.el-tabs__content) {
    height: calc(100% - 54px);
  }

  .icon-picker-tabs :deep(.el-tab-pane) {
    height: 100%;
  }

  .icon-picker-layout {
    display: grid;
    grid-template-columns: 220px minmax(0, 1fr);
    height: 100%;
    overflow: hidden;
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 12px;
    box-shadow: var(--el-box-shadow-light);
  }

  .icon-picker-sidebar {
    padding: 14px;
    overflow: auto;
    border-right: 1px solid var(--el-border-color-lighter);
  }

  .icon-category-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 46px;
    padding: 0 16px;
    margin-bottom: 8px;
    font-size: 15px;
    font-weight: 600;
    color: #2d3a55;
    border-radius: 8px;
  }

  .icon-category-item span:last-child {
    color: #a8afbf;
  }

  .icon-category-item:hover,
  .icon-category-item.is-active {
    color: var(--el-color-primary);
    background: var(--el-color-primary-light-9);
  }

  .icon-category-item.is-active span:last-child {
    color: var(--el-color-primary);
  }

  .icon-picker-main {
    display: flex;
    min-width: 0;
    min-height: 0;
    flex-direction: column;
  }

  .icon-picker-search {
    display: flex;
    flex: 0 0 68px;
    align-items: center;
    gap: 14px;
    padding: 0 26px;
    font-size: 30px;
    color: #bcc3d0;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }

  .icon-picker-search :deep(.el-input__wrapper) {
    box-shadow: none;
  }

  .icon-picker-search :deep(.el-input__inner) {
    height: 44px;
    font-size: 18px;
  }

  .iconify-search {
    flex-wrap: wrap;
    gap: 12px;
    height: auto;
    min-height: 68px;
    padding: 12px 26px;
  }

  .iconify-set-radio {
    flex: 0 0 auto;
  }

  .iconify-search-input {
    display: flex;
    min-width: 260px;
    flex: 1 1 320px;
    align-items: center;
    gap: 14px;
  }

  .icon-picker-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(132px, 1fr));
    gap: 12px;
    min-height: 0;
    padding: 18px;
    overflow: auto;
  }

  .icon-picker-item {
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: center;
    justify-content: center;
    min-height: 92px;
    padding: 10px;
    font-size: 12px;
    color: var(--el-text-color-regular);
    border: 1px solid var(--el-border-color);
    border-radius: 8px;
  }

  .icon-picker-svg {
    font-size: 26px;
  }

  .icon-picker-svg--motion {
    animation: icon-motion-preview 1.4s ease-in-out infinite;
  }

  .icon-picker-item span {
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .icon-picker-item:hover,
  .icon-picker-item.is-active {
    color: var(--el-color-primary);
    border-color: var(--el-color-primary);
    background: var(--el-color-primary-light-9);
  }

  @keyframes icon-motion-preview {
    0%,
    100% {
      transform: rotate(0deg) scale(1);
    }

    50% {
      transform: rotate(8deg) scale(1.08);
    }
  }

  @media (max-width: 768px) {
    .menu-icon-field {
      width: 100%;
    }

    .menu-icon-preview {
      flex-basis: 56px;
    }

    .menu-icon-value {
      padding: 0 12px;
      font-size: 15px;
    }

    .menu-icon-trigger {
      flex-basis: 96px;
      font-size: 15px;
    }

    .icon-picker-layout {
      grid-template-columns: 1fr;
    }

    .icon-picker-sidebar {
      display: flex;
      gap: 8px;
      padding: 10px;
      border-right: 0;
      border-bottom: 1px solid var(--el-border-color-lighter);
    }

    .icon-category-item {
      flex: 0 0 auto;
      width: auto;
      margin-bottom: 0;
      gap: 12px;
    }

    .iconify-search {
      padding: 10px;
    }

    .iconify-set-radio {
      width: 100%;
      overflow-x: auto;
    }

    .iconify-search-input {
      min-width: 100%;
    }
  }
</style>
