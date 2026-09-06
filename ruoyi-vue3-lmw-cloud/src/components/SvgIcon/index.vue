<script>
import { createVNode, resolveComponent, defineComponent, computed } from 'vue'
import svg from './useSvg.vue'
export default defineComponent({
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
      default: '14',
    },
  },
  setup(props) {
    const iconStyle = computed(() => {
      return {
        fontSize: props.size + 'px',
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
          size: props.size,
          color: props.color,
        })
    }
  },
})
</script>
