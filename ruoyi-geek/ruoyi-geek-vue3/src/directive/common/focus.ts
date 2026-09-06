import type { Directive } from "vue";
const vFocus: Directive = {
    mounted: (el) => el.focus()
}
export default vFocus


