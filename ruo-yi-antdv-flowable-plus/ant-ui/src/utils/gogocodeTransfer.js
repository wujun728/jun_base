const eventRegistryMap = new WeakMap()
function getRegistry(instance) {
  let events = eventRegistryMap.get(instance)
  if (!events) {
    eventRegistryMap.set(instance, (events = Object.create(null)))
  }
  return events
}
export function $on(instance, event, fn) {
  if (Array.isArray(event)) {
    event.forEach((e) => $on(instance, e, fn))
  } else {
    const events = getRegistry(instance)
    ;(events[event] || (events[event] = [])).push(fn)
  }
  return instance
}
export function $once(instance, event, fn) {
  const wrapped = (...args) => {
    $off(instance, event, wrapped)
    fn.call(instance, ...args)
  }
  wrapped.fn = fn
  $on(instance, event, wrapped)
  return instance
}
export function $off(instance, event, fn) {
  const vm = instance
  // all
  if (!event) {
    eventRegistryMap.set(instance, Object.create(null))
    return vm
  }
  // array of events
  if (Array.isArray(event)) {
    event.forEach((e) => $off(instance, e, fn))
    return vm
  }
  // specific event
  const events = getRegistry(instance)
  const cbs = events[event]
  if (!cbs) {
    return vm
  }
  if (!fn) {
    events[event] = undefined
    return vm
  }
  events[event] = cbs.filter((cb) => !(cb === fn || cb.fn === fn))
  return vm
}
export function $emit(instance, event, ...args) {
  instance && instance.$emit && instance.$emit(event, ...args)
  const cbs = getRegistry(instance)[event]
  if (cbs) {
    cbs.map((cb) => cb.apply(instance, args))
  }
  return instance
}
export function plantRenderPara(params) {
  const transProps = {
    staticClass: 'class',
    staticStyle: 'style',
    on: '',
    domProps: '',
    props: '',
    attrs: '',
  }
  function obj2arr(obj) {
    return typeof obj == 'string'
      ? [obj]
      : Object.keys(obj).map((pk, index) => {
          return { [pk]: Object.values(obj)[index] }
        })
  }
  let result = {}
  for (let key in params) {
    if (transProps[key] == null) {
      if (typeof params[key] == 'object') {
        result[key] = obj2arr(params[key])
      } else {
        result[key] = params[key]
      }
    }
  }
  for (let key in params) {
    if (transProps[key] === '') {
      if (typeof params[key] == 'object') {
        for (let k in params[key]) {
          result[k] = params[key][k]
        }
      } else {
        result[key] = params[key]
      }
    }
  }
  for (let key in params) {
    if (transProps[key]) {
      result[transProps[key]] = result[transProps[key]] || []
      result[transProps[key]] = result[transProps[key]].concat(
        obj2arr(params[key])
      )
    }
  }
  return result
}
