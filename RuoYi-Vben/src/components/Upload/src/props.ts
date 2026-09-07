import type { PropType } from 'vue';
import { BaseFileItem, FileBasicColumn } from './types/typing';

import type { Options } from 'sortablejs';

import { Merge } from '@/utils/types';
import { propTypes } from '@/utils/propTypes';
import { BasicColumn } from '@/components/Table';
import { IconEnum } from '@/enums/appEnum';

type SortableOptions = Merge<
  Omit<Options, 'onEnd'>,
  {
    onAfterEnd?: <T = any, R = any>(params: T) => R;
    // ...可扩展
  }
>;
export type handleFnKey = 'record' | 'valueKey' | 'uidKey';
export type previewColumnsFnType = {
  handleRemove: (record: Record<handleFnKey, any>) => any;
  handleAdd: (record: Record<handleFnKey, any>) => any;
};
export const previewType = {
  previewColumns: {
    type: [Array, Function] as PropType<
      BasicColumn[] | ((arg: previewColumnsFnType) => BasicColumn[])
    >,
    required: false,
  },
  beforePreviewData: {
    type: Function as PropType<(arg: BaseFileItem[] | any) => Recordable<any>>,
    default: null,
    required: false,
  },
  // 是否显示旁边的预览按钮
  preview: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  btnText: {
    type: String as PropType<string>,
    default: '上传文件',
  },
  btnIcon: {
    type: String as PropType<string>,
    default: IconEnum.UPLOAD,
  },
};

type ListType = 'text' | 'picture' | 'picture-card';

export const basicProps = {
  disabled: { type: Boolean, default: false },
  listType: {
    type: String as PropType<ListType>,
    default: 'picture-card',
  },
  helpText: {
    type: String as PropType<string>,
    default: '',
  },
  // 文件最大多少MB
  maxSize: {
    type: Number as PropType<number>,
    default: 2,
  },
  // 最大数量的文件，Infinity不限制
  maxNumber: {
    type: Number as PropType<number>,
    default: 1,
  },
  /**
   * 建议使用拓展名(不带.)
   * 或者文件头 image/png等(测试判断不准确)  不支持image/*类似的写法
   * 需自行改造 ./helper/checkFileType方法
   */
  accept: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  multiple: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  uploadParams: {
    type: Object as PropType<any>,
    default: () => ({}),
  },
  api: {
    type: Function as PropType<PromiseFn>,
    default: null,
    required: true,
  },
  name: {
    type: String as PropType<string>,
    default: 'file',
  },
  filename: {
    type: String as PropType<string>,
    default: null,
  },
  fileListOpenDrag: {
    type: Boolean,
    default: true,
  },

  fileListDragOptions: {
    type: Object as PropType<SortableOptions>,
    default: () => ({}),
  },
  // support xxx.xxx.xx
  resultField: propTypes.string.def(''),
};

export const uploadContainerProps = {
  value: {
    type: Array as PropType<string[]>,
    default: () => [],
  },
  ...basicProps,
  showPreviewNumber: {
    type: Boolean as PropType<boolean>,
    default: true,
  },
  emptyHidePreview: {
    type: Boolean as PropType<boolean>,
    default: false,
  },
  ...previewType,
};

export const previewProps = {
  value: {
    type: Array as PropType<BaseFileItem[] | any[]>,
    default: () => [],
  },
  maxNumber: {
    type: Number as PropType<number>,
    default: 1,
  },
  ...previewType,
};

export const fileListProps = {
  columns: {
    type: Array as PropType<BasicColumn[] | FileBasicColumn[]>,
    default: null,
  },
  actionColumn: {
    type: Object as PropType<FileBasicColumn>,
    default: null,
  },
  dataSource: {
    type: Array as PropType<any[]>,
    default: null,
  },
  openDrag: {
    type: Boolean,
    default: false,
  },
  dragOptions: {
    type: Object as PropType<SortableOptions>,
    default: () => ({}),
  },
};
