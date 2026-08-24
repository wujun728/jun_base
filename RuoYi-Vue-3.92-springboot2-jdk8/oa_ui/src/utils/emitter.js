/**
 * 广播事件到所有子组件
 * @param {string} componentName - 组件名称
 * @param {string} eventName - 事件名称
 * @param {Array} params - 参数列表
 */
function broadcast(componentName, eventName, params) {
  // 遍历所有子组件
  this.$children.forEach((child) => {
    var name = child.$options.componentName

    // 如果子组件名称与目标组件名称相同，则直接触发事件并广播
    if (name === componentName) {
      child.$emit.apply(child, [eventName].concat(params))
      // 否则递归调用广播函数
      broadcast.apply(child, [componentName, eventName].concat([params]))
    } else {
      // 否则递归调用广播函数
      broadcast.apply(child, [componentName, eventName].concat([params]))
    }
  })
}
export default {
  data() {
    return { vfEvents: {} }
  },
  methods: {
    /**
     * 分发事件到指定组件及其父组件
     * @param {string} componentName - 组件名称
     * @param {string} eventName - 事件名称
     * @param {Array} params - 参数列表
     */
    dispatch(componentName, eventName, params) {
      var parent = this.$parent || this.$root
      var name = parent.$options.componentName

      // 查找目标组件及其父组件
      while (parent && (!name || name !== componentName)) {
        parent = parent.$parent

        if (parent) {
          name = parent.$options.componentName
        }
      }
      if (parent) {
        // 触发事件
        parent.$emit.apply(parent, [eventName].concat(params))
      }
    },
    /**
     * 广播事件到所有子组件及其子孙组件
     * @param {string} componentName - 组件名称
     * @param {string} eventName - 事件名称
     * @param {Array} params - 参数列表
     */
    broadcast(componentName, eventName, params) {
      // 调用广播函数
      broadcast.call(this, componentName, eventName, params)
    },
    emit$(o, e) {
      this.vfEvents[o] &&
        this.vfEvents[o].forEach((n) => {
          n(e)
        })
    },
    on$(o, e) {
      ;(this.vfEvents[o] = this.vfEvents[o] || []), this.vfEvents[o].push(e)
    },
    off$(o, e) {
      if (this.vfEvents[o]) {
        if (e == null) {
          this.vfEvents[o].length = 0
          return
        }
        for (let n = 0; n < this.vfEvents[o].length; n++)
          if (this.vfEvents[o][n] === e) {
            this.vfEvents[o].splice(n, 1)
            break
          }
      }
    }
  }
}
