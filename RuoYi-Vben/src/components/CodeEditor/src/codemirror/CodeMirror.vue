<template>
  <div
    class="relative !h-full w-full overflow-hidden"
    :class="{ 'ant-input': props.bordered, 'css-dev-only-do-not-override-kqecok': props.bordered }"
    ref="el"
  ></div>
</template>

<script lang="ts" setup>
  import { type PropType, ref, onMounted, onUnmounted, watch, unref, nextTick } from 'vue';
  import type { Nullable } from '@vben/types';
  import { useWindowSizeFn } from '@vben/hooks';
  import { useDebounceFn } from '@vueuse/core';
  import { useAppStore } from '@/store/modules/app';
  import CodeMirror from 'codemirror';
  import { MODE } from './../typing';
  // css
  import './codemirror.css';
  import 'codemirror/theme/idea.css';
  import 'codemirror/theme/material-palenight.css';
  // modes
  import 'codemirror/mode/javascript/javascript';
  import 'codemirror/mode/css/css';
  import 'codemirror/mode/htmlmixed/htmlmixed';
  import 'codemirror/mode/xml/xml';
  import 'codemirror/mode/sql/sql';
  // 类C语言统一使用这个  通过mode区分
  import 'codemirror/mode/clike/clike';
  import 'codemirror/mode/vue/vue';

  /**
   * 所有支持的语言mode
   * 查看: https://codemirror.net/5/mode/
   * 源码: https://github.com/codemirror/codemirror5/blob/master/mode
   */
  const props = defineProps({
    mode: {
      type: String as PropType<MODE>,
      default: MODE.JSON,
      validator(value: any) {
        // 这个值必须匹配下列字符串中的一个
        return Object.values(MODE).includes(value);
      },
    },
    value: { type: String, default: '' },
    readonly: { type: Boolean, default: false },
    bordered: { type: Boolean, default: false },
  });

  const emit = defineEmits(['change']);

  const el = ref();
  let editor: Nullable<CodeMirror.Editor>;

  const debounceRefresh = useDebounceFn(refresh, 100);
  const appStore = useAppStore();

  watch(
    () => props.value,
    async (value) => {
      await nextTick();
      const oldValue = editor?.getValue();
      if (value !== oldValue) {
        editor?.setValue(value ? value : '');
      }
    },
    { flush: 'post' },
  );

  // watchEffect有问题 用下面的watch来做
  // watchEffect(() => {
  //   true && props.mode;
  //   editor?.setOption('mode', props.mode);
  // });

  watch(
    () => props.mode,
    (newMode) => {
      editor?.setOption('mode', newMode);
    },
  );

  watch(
    () => appStore.getDarkMode,
    async () => {
      setTheme();
    },
    {
      immediate: true,
    },
  );

  function setTheme() {
    unref(editor)?.setOption(
      'theme',
      appStore.getDarkMode === 'light' ? 'idea' : 'material-palenight',
    );
  }

  function refresh() {
    editor?.refresh();
  }

  async function init() {
    const addonOptions = {
      autoCloseBrackets: true,
      autoCloseTags: true,
      foldGutter: true,
      gutters: ['CodeMirror-linenumbers'],
    };

    editor = CodeMirror(el.value!, {
      value: '',
      mode: props.mode,
      readOnly: props.readonly,
      tabSize: 2,
      theme: 'material-palenight',
      lineWrapping: true,
      lineNumbers: true,
      ...addonOptions,
    });
    editor?.setValue(props.value);
    setTheme();
    editor?.on('change', () => {
      emit('change', editor?.getValue());
    });
  }

  onMounted(async () => {
    await nextTick();
    init();
    useWindowSizeFn(debounceRefresh);
  });

  onUnmounted(() => {
    editor = null;
  });
</script>
