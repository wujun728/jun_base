<template>
    <div ref="codeEditBox" class="codeEditBox"></div>
</template>

<script lang="ts">
import {defineComponent, onBeforeUnmount, onMounted, ref, watch} from 'vue'
import {editorProps} from './monacoEditorType'
import JsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker'
import CssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker'
import HtmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker'
import TsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker'
import EditorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker'
import * as monaco from 'monaco-editor'


export default defineComponent({
    name: 'monacoEditor',
    props: editorProps,
    emits: ['update:modelValue', 'change', 'editorMounted'],
    setup(props, {emit}) {
        self.MonacoEnvironment = {
            getWorker(_: string, label: string) {
                if (label === 'json') return new JsonWorker()
                if (['css', 'scss', 'less'].includes(label)) return new CssWorker()
                if (['html', 'handlebars', 'razor'].includes(label)) return new HtmlWorker()
                if (['typescript', 'javascript'].includes(label)) return new TsWorker()
                return new EditorWorker()
            },
        }
        let editor: monaco.editor.IStandaloneCodeEditor
        const codeEditBox = ref()

        // 初始化
        const init = () => {
            monaco.languages.typescript.javascriptDefaults.setDiagnosticsOptions({
                noSemanticValidation: true,
                noSyntaxValidation: false,
            })
            monaco.languages.typescript.javascriptDefaults.setCompilerOptions({
                target: monaco.languages.typescript.ScriptTarget.ES2020,
                allowNonTsExtensions: true,
            })

            editor = monaco.editor.create(codeEditBox.value, {
                value: props.modelValue,
                language: props.language,
                theme: props.theme,
                ...props.options,
            })

            // 监听值的变化
            editor.onDidChangeModelContent(() => {
                const value = editor.getValue()
                //给父组件实时返回最新文本
                emit('update:modelValue', value)
                emit('change', value)
            })

            emit('editorMounted', editor)
        }
        watch(() => props.modelValue, newValue => {
                if (editor) {
                    const value = editor.getValue()
                    if (newValue !== value) {
                        editor.setValue(newValue)
                    }
                }
            }
        )

        watch(() => props.options, newValue => editor.updateOptions(newValue), {deep: true})
        watch(() => props.language, newValue => monaco.editor.setModelLanguage(editor.getModel()!, newValue))

        onBeforeUnmount(() => {
            editor.dispose()
        })

        onMounted(() => {
            init()
        })

        return {
            codeEditBox
        }
    },
})
</script>

<style lang="less" scoped>
.codeEditBox {
    width: 100%;
    height: v-bind(height);
}
</style>
