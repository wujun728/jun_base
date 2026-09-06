<script setup>
const model = defineModel()
const props = defineProps({
    height: {
        type: String,
        default: '200px'
    },
    width: {
        type: String,
        default: '100%'
    },
    mode: {
        type: String,
        default: 'input' // 'show'
    },
})
const height = ref('200px')
const width = ref('100%')
const emit = defineEmits(['change'])
const length = ref(0)
const texts = ref({})
const htmls = ref({})
function checkBrackets(str) {
    let stack = [];
    for (let i = 0; i < str.length; i++) {
        let char = str[i];
        if (char === '(' || char === '{' || char === '[' || char === '<') {
            stack.push(char);
        } else if (char === ')' || char === '}' || char === ']' || char === '>') {
            let top = stack.pop();
            if ((char === ')' && top !== '(') || (char === '}' && top !== '{') || (char === ']' && top !== '[') || (char === '>' && top !== '<')) {
                return false;
            }
        }
    }
    return stack.length === 0;
}
function filterChain(str) {
    const filterList = [
        {
            filter: /(?!=[\s]|[&nbsp;])?(count\(|date_format\(|find_in_set\()(.*?)(\))(?!=[\s]|[&nbsp;])?/gi,
            replace(str) { return str.replace(this.filter, '<span class="code-sql">$1<span class="code-fun-params">$2</span>$3</span>') }
        },
        {
            filter: /(?<=^|&nbsp;|\s)(select|where|insert|update|delete|create|alter|drop|database|table|from|order|group|by|having|join|into|values|like|between|null|not|and|or|in|bigint|varchar|date|timestamp|union|all|as)(?=\s|&nbsp;|$)/gi,
            replace(str) { return str.replace(this.filter, '<span class="code-sql">$1</span>') }
        },
        {
            filter: /(&lt;.*?&gt;)/g,
            replace(str) { return str.replace(this.filter, '<span class="code-tag">$1</span>') }
        },
        {
            filter: /(&lt;\/.*?&gt;)/g,
            replace(str) { return str.replace(this.filter, '<span class="code-tag">$1</span>') }
        },
        {
            filter: /(#{.*?})/g,
            replace(str) { return str.replace(this.filter, '<span class="code-insert-params">$1</span>') }
        },
    ]
    for (let item of filterList) {
        if (item.filter.test(str)) str = item.replace(str)
    }
    return str
}
const Codeformatting = () => {
    const array = ('' + model.value).split("\n");
    let div_id = 0;
    let temp = null;
    function render(temp) {
        texts.value[div_id] = temp
        temp = temp.replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/\n/g, "<br/>")
            .replace(/\s/g, "&nbsp;")
        htmls.value[div_id] = filterChain(temp)
        div_id = div_id + 1
    }

    for (let index = 0; index < array.length; index++) {
        // 初始化当前字符串
        if (temp == null) temp = array[index]
        else temp = temp + '\n' + array[index]
        // 字符串未闭合退出当前循环
        if (!checkBrackets(temp)) continue;
        // 字符串与缓存内容相等退出当前循环
        if (temp == htmls[div_id]) continue
        render(temp)
        temp = null
    }

    if (length.value = div_id) {
        length.value = div_id
    }
}
watch(() => model.value, () => {
    Codeformatting()
    emit('change', {
        texts: texts.value,
        htmls: htmls.value,
        length: length.value,
        scrollTop: sqlInput.value.scrollTop,
        scrollLeft: sqlInput.value.scrollLeft
    })
})

const sqlInput = ref(null)
const sqlShow = ref(null)
function wheelHandler(e) {
    sqlShow.value.scrollTop = sqlInput.value.scrollTop
    sqlShow.value.scrollLeft = sqlInput.value.scrollLeft
    emit('change', {
        texts: texts.value,
        htmls: htmls.value,
        length: length.value,
        scrollTop: sqlInput.value.scrollTop,
        scrollLeft: sqlInput.value.scrollLeft
    })
}


onMounted(() => {
    const observer = new MutationObserver((mutationsList, observer) => {
        height.value = getComputedStyle(sqlInput.value).getPropertyValue('height');
        width.value = getComputedStyle(sqlInput.value).getPropertyValue('width');
    });
    //开始观测
    const config = { attributes: true, childList: false, subtree: true, attributeFilter: ['style'], attributeOldValue: true };
    observer.observe(sqlInput.value, config);
    Codeformatting()
    height.value = props.height
    width.value = props.width

    emit('change', {
        texts: texts.value,
        htmls: htmls.value,
        length: length.value,
        scrollTop: sqlInput.value.scrollTop,
        scrollLeft: sqlInput.value.scrollLeft
    })
})
</script>
<template>
    <div class="sql-wrap" :style="{ height, width }">
        <textarea v-show="mode === 'input'" class="sql-input" ref="sqlInput"
            @scroll="wheelHandler" v-model="model" :style="{ height, width }"></textarea>
        <div class="sql-show" ref="sqlShow" @scroll="wheelHandler" :style="{ height, width }">
            <div class='sql-line' v-for="index in length" :key="index" v-html="htmls[index - 1]"></div>
        </div>
    </div>
</template>
<style lang="scss" scoped>
.sql-wrap {

    $line-height: 25px;
    position: relative;
    width: 100%;
    height: 200px;
    overflow: auto;
    border: 1px solid #ccc;
    overflow: hidden;

    .sql-line {
        min-height: $line-height
    }

    :deep(.sql-show) {
        .code-tag {
            color: green;
        }

        .code-sql {
            color: rgb(1, 176, 176);
        }

        .code-fun-params {
            color: bisque;
        }

        .code-insert-params {
            color: rgb(158, 156, 225);
        }

    }

    .base-font {
        position: absolute;
        top: 0;
        font-size: 18px;
        line-height: $line-height;
        font-family: 'Microsoft YaHei';
        font-feature-settings: normal;
        font-kerning: auto;
        font-optical-sizing: auto;
        font-size: 18px;
        font-stretch: 100%;
        font-style: normal;
        font-variant-alternates: normal;
        font-variant-caps: normal;
        font-variant-east-asian: normal;
        font-variant-ligatures: normal;
        font-variant-numeric: normal;
        font-variant-position: normal;
        font-variation-settings: normal;
        font-weight: 400;
        letter-spacing: normal;
        text-align: start;
        text-indent: 0px;
        text-shadow: rgba(0, 0, 0, 0.5) 0px 0px 0px;
        text-transform: none;
        text-wrap: wrap;
        white-space-collapse: preserve;
        word-break: break-all;
        white-space: nowrap;
    }

    .sql-input {
        @extend .base-font;
        caret-color: black;
        width: 100%;
        z-index: 2;
        mix-blend-mode: multiply;
        color: transparent;
        text-shadow: 0 0 0px rgba(0, 0, 0, 0);
        white-space: nowrap;
    }

    .sql-show {
        @extend .base-font;
        padding: 2px;
        border: 0.8px solid;
        width: 100%;
        color: black;
        overflow: auto;
        scrollbar-color: transparent transparent;
    }
}
</style>