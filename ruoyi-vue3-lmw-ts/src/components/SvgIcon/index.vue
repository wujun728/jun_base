<script lang="ts">
import { createVNode, resolveComponent, defineComponent, computed } from 'vue'
import svg from './useSvg.vue'
type Props = {
  iconClass: string
  className?: string
  color?: string
  size?: StrNum
}
export default defineComponent(
  (props: Props) => {
    const { size = 14 } = props
    const iconStyle = computed(() => {
      return {
        fontSize: size + 'px',
        color: props.color,
      }
    })

    if (props.iconClass.indexOf('el-icon-') === 0) {
      const names = props.iconClass.split('el-icon-')
      return () =>
        createVNode(
          'el-icon',
          { class: 'icon el-icon', style: iconStyle.value },
          [createVNode(resolveComponent(names[1]))]
        )
    } else {
      return () =>
        createVNode(svg, {
          iconClass: props.iconClass,
          size: size,
          color: props.color,
          className: props.className,
        })
    }
  },
  // 目前仍然需要手动声明运行时的 props
  {
    props: ['className', 'color', 'iconClass', 'size'],
  }
)
</script>
