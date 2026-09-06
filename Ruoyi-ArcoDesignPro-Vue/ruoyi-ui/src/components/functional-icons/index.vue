<script>
import {h, resolveDynamicComponent} from "vue"

function Icon(props, ctx) {
  const Component = resolveDynamicComponent(props.icon)
  return h(Component)
}

export default Icon
</script>
