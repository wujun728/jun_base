import { defineCustomElement } from 'vue'
declare module 'vue' {
  export interface GlobalComponents {
    SvgIcon: (typeof import('@/components/SvgIcon/index.vue'))['default']
    BaseForm: (typeof import('@/BaseComponent/BaseForm/index.ts'))['default']
    BaseTable: (typeof import('@/BaseComponent/BaseTable/index.ts'))['default']
    PageContent: (typeof import('@/components/PageContent/index.ts'))['default']
    PageSearch: (typeof import('@/components/PageSearch/index.ts'))['default']
    PageDialog: (typeof import('@/components/PageDialog/index.ts'))['default']
    DictTag: typeof import('@/components/DictTag/index.vue')
  }
  interface ComponentCustomProperties {
    getWidth: (typeof import('@/utils/hsj/utils'))['getDialogWidth']
    hasPermi: (typeof import('@/utils/hasPermi'))['default']
  }
}
