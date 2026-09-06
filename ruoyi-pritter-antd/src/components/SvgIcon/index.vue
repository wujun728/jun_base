<script>
import { createVNode, defineComponent, computed } from 'vue'
import { resolveAntdIcon } from '@/utils/iconfont'
import svg from './useSvg.vue'

export default defineComponent({
  name: 'SvgIcon',
  props: {
    iconClass: {
      type: String,
      required: true,
    },
    className: {
      type: String,
      default: '',
    },
    color: {
      type: String,
      default: '',
    },
    size: {
      type: [String, Number],
      default: 14,
    },
  },
  setup(props) {
    const iconStyle = computed(() => ({
      fontSize: `${props.size}px`,
      color: props.color || undefined,
    }))

    return () => {
      const antIcon = resolveAntdIcon(props.iconClass)
      if (antIcon) {
        return createVNode(antIcon, {
          key: props.iconClass,
          class: ['ant-icon', props.className].filter(Boolean).join(' '),
          style: iconStyle.value,
        })
      }

      return createVNode(svg, {
        key: props.iconClass,
        iconClass: props.iconClass,
        size: props.size,
        color: props.color,
        className: props.className,
      })
    }
  },
})
</script>
