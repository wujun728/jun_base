<!-- @FileDescription: 富文本组件wangeditorV5 -->
<template>
  <div>
    <Toolbar
      v-if="headers && !readOnly"
      :defaultConfig="toolbarConfig"
      :editor="editor"
      :mode="mode"
      :style="{ borderLeft: type === 'notepad' ? '' : '1px solid #dcdfe6ff' }"
    />
    <div :style="{ '--width': mainWidth }" class="main" :class="training ? 'train-spac' : ''">
      <Editor
        ref="wang-editor"
        v-model="contentVal"
        :defaultConfig="editorConfig"
        :mode="mode"
        :style="{ '--height': height, border: editorBorder ? '1px solid #dcdfe6ff' : 'none' }"
        @onChange="onChange"
        @onCreated="onCreated"
      />
    </div>
  </div>
</template>
<script>
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { uploader } from '@/utils/uploadCloud'

export default Vue.extend({
  components: { Editor, Toolbar },
  props: {
    content: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请输入内容...'
    },
    height: {
      type: String,
      default: '400px'
    },
    type: {
      // simple 简约版  notepad 记事本版
      type: String,
      default: 'all'
    },
    headers: {
      type: Boolean,
      default: true
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    mainWidth: {
      type: String,
      default: '100%'
    },
    editorBorder: {
      type: Boolean,
      default: true
    },
    // 员工培训样式需要单独处理
    training: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      editor: null,
      contentVal: this.content,
      toolbarConfig: {},
      editorConfig: { MENU_CONF: {}, placeholder: this.placeholder, readOnly: this.readOnly },
      mode: 'default' // or 'simple'
    }
  },
  watch: {
    content: function (val) {
      this.contentVal = val
    },
    disabled(val) {
      if (val) {
        this.editor.disable()
      } else {
        this.editor.enable()
      }
    },
    readOnly(val) {
      if (val) {
        this.editor.disable()
      } else {
        this.editor.enable()
      }
    }
  },
  mounted() {
    console.log('wangeditor')
    if (this.content && this.editor) {
      setTimeout(() => {
        this.editor.setHtml = this.content
        this.contentVal = this.content
        if (this.disabled) {
          this.editor.disable()
        } else {
          this.editor.enable()
        }
      }, 1000)
    }
  },
  created() {
    let that = this
    that.editorConfig.MENU_CONF['uploadImage'] = {
      customUpload(file, insertFn) {
        let options = {
          way: 2,
          relation_type: '',
          relation_id: 0,
          eid: 0
        }
        uploader(file, 1, options).then((res) => {
          insertFn(res.data.url, '图片上传', res.data.url)
        })
      }
    }

    // 自定义富文本菜单栏 type='simple'简约版本  type='notepad'记事本专用菜单
    if (that.type == 'simple') {
      that.toolbarConfig.toolbarKeys = [
        'headerSelect',
        'bold',
        'italic',
        'insertLink',
        'uploadImage',
        'bulletedList',
        'numberedList',
        'codeBlock',
        'blockquote'
      ]
    } else if (that.type == 'notepad') {
      that.toolbarConfig.toolbarKeys = [
        'headerSelect',
        'blockquote',
        'bold',
        'underline',
        'italic',

        {
          key: 'group-more-style',
          title: '更多',
          iconSvg:
            '<svg viewBox="0 0 1024 1024"><path d="M204.8 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z"></path><path d="M505.6 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z"></path><path d="M806.4 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z"></path></svg>',
          menuKeys: ['fontSize', 'fontFamily', 'lineHeight', 'code', 'clearStyle', 'through', 'sup', 'sub']
        },
        '|',
        'color',
        'bgColor',
        'bulletedList',
        'numberedList',
        'todo',
        {
          key: 'group-justify',
          title: '对齐',
          iconSvg:
            '<svg viewBox="0 0 1024 1024"><path d="M768 793.6v102.4H51.2v-102.4h716.8z m204.8-230.4v102.4H51.2v-102.4h921.6z m-204.8-230.4v102.4H51.2v-102.4h716.8zM972.8 102.4v102.4H51.2V102.4h921.6z"></path></svg>',
          menuKeys: ['justifyLeft', 'justifyRight', 'justifyCenter', 'justifyJustify']
        },
        '|',
        'emotion',
        {
          key: 'group-image',
          title: '图片',
          iconSvg:
            '<svg viewBox="0 0 1024 1024"><path d="M959.877 128l0.123 0.123v767.775l-0.123 0.122H64.102l-0.122-0.122V128.123l0.122-0.123h895.775zM960 64H64C28.795 64 0 92.795 0 128v768c0 35.205 28.795 64 64 64h896c35.205 0 64-28.795 64-64V128c0-35.205-28.795-64-64-64zM832 288.01c0 53.023-42.988 96.01-96.01 96.01s-96.01-42.987-96.01-96.01S682.967 192 735.99 192 832 234.988 832 288.01zM896 832H128V704l224.01-384 256 320h64l224.01-192z"></path></svg>',
          menuKeys: ['insertImage', 'uploadImage']
        },
        'insertTable',
        'codeBlock',
        'divider',
        'insertLink',
        'undo',
        'redo'
      ]
    } else {
      that.toolbarConfig.excludeKeys = ['fullScreen', 'insertVideo', 'uploadVideo', 'group-video']
    }
  },
  methods: {
    getValue() {
      return this.editor.getHtml()
    },

    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    onChange(edit) {
      if (this.disabled) {
        this.editor.disable()
      } else {
        this.editor.enable()
      }
      this.$emit('input', this.contentVal)
    },

    clear() {
      this.contentVal = ''
    }
  },

  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
     // 组件销毁时，及时销毁编辑器
    if (this.training) {
      // 由于员工培训具有单独的css样式，销毁组件后容器尚未销毁，会造成页面上下跳动，所以需要延迟销毁
      setTimeout(() => {
        editor.destroy()
      }, 500)
    } else {
      editor.destroy()
    }
  }
})
</script>
<style src="@wangeditor/editor/dist/css/style.css"></style>
<style lang="scss" scoped>
::v-deep.w-e-toolbar {
  border: 1px solid #dcdfe6;
}
::v-deep.w-e-text-container {
  height: var(--height);
  border-top: none;
  overflow: hidden;
  padding: 12px;
}
::v-deep .w-e-text-placeholder {
  padding: 12px;
}
::v-deep .w-e-scroll {
  overflow-y: auto;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
}
.main {
  background: #fff;
  width: var(--width);
  margin: 0 auto;
}
.train-spac {
  padding: 40px 86px;
  margin-top: 30px;
}
</style>
