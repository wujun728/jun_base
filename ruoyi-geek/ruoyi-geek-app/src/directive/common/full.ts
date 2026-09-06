import type { Directive } from "vue";
interface ElType extends HTMLElement {
    $oldStyle: CSSStyleDeclaration;
    $fullStyle: CSSStyleDeclaration;
}
const vFull: Directive = {
    mounted: (el: ElType, binding) => {
        el.$oldStyle = { ...el.style }
        if (binding.arg === 'screen') {
            el.$fullStyle = {
                ...el.style,
                left: '0',
                top: '0',
                zIndex: '8',
                position: 'fixed',
                height: '100vh',
                width: '100vw',
            }
        } else {
            el.$fullStyle = {
                ...el.style,
                left: '0',
                top: '0',
                zIndex: '8',
                position: 'absolute',
                height: '100%',
                width: '100%',
            }
        }

    },
    updated: (el: ElType, binding) => {
        function setStyle(el: CSSStyleDeclaration, style: CSSStyleDeclaration) {
            el.position = style.position
            el.left = style.left
            el.top = style.top
            el.zIndex = style.zIndex
            el.height = style.height
            el.width = style.width
        }
        if (binding.value) {
            setStyle(el.style, el.$fullStyle)
        } else {
            setStyle(el.style, el.$oldStyle)
        }
    }
}
export default vFull


