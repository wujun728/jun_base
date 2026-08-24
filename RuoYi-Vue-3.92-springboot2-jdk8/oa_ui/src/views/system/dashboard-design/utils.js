import {
  COMMON_PROPERTIES$1,
  ADVANCED_PROPERTIES$1,
  EVENT_PROPERTIES$1,
  zhLocale_extension,
  enLocale_extension
} from './charts/configData'

export function isNull(o) {
  return o == null
}
export function isNotNull(o) {
  return o != null
}
export function isEmptyStr(o) {
  return o === void 0 || (!o && o !== 0 && o !== '0') || !/[^\s]/.test(o)
}
export function generateId() {
  return Math.floor(Math.random() * 1e5 + Math.random() * 2e4 + Math.random() * 5e3)
}
export function deepClone(o) {
  if (o !== void 0) return JSON.parse(JSON.stringify(o))
}
export function overwriteObj(o, e) {
  Object.keys(e).forEach((n) => {
    o[n] = e[n]
  })
}
export function evalFn(o, e = null, n = null, l = null) {
  return new Function('DSV', 'VFR', 'LS', 'formulaJs', 'return ' + o)(e, n, localStorage, l)
}
export function trimEx(o, e, n) {
  return e
    ? n === 'left'
      ? o.replace(new RegExp('^\\' + e + '+', 'g'), '')
      : n === 'right'
      ? o.replace(new RegExp('\\' + e + '+$', 'g'), '')
      : o.replace(new RegExp('^\\' + e + '+|\\' + e + '+$', 'g'), '')
    : o.replace(/^\s+|\s+$/g, '')
}
export function hasPropertyOfObject(o, e) {
  const n = e.split('.')
  let l = o,
    s = !0
  for (const c of n)
    if (l.hasOwnProperty(c)) l = l[c]
    else {
      s = !1
      break
    }
  return s
}
export function objectKeysToArray(o) {
  if (!o) return []
  const e = []
  return (
    Object.keys(o).forEach((n) => {
      e.push(n)
    }),
    e
  )
}
export function isDef(o) {
  return o != null
}
export function getObjectValue(o, e) {
  const n = e.split('.')
  let l = o
  return (
    n.forEach((s) => {
      l = isDef(l) && isDef(l[s]) ? l[s] : null
    }),
    l
  )
}
export function setObjectValue(o, e, n) {
  const l = e.split('.')
  let s = o
  l.forEach((c, u) => {
    if (!!c) {
      if (u === l.length - 1) {
        s[c] = n
        return
      }
      s[c] === void 0 && (s[c] = {}), (s = s[c])
    }
  })
}
export function addWindowResizeHandler(o) {
  let e = window.onresize
  typeof window.onresize != 'function'
    ? (window.onresize = o)
    : (window.onresize = function () {
        e(), o()
      })
}
export function insertCustomCssToHead(o, e = '') {
  const n = document.getElementsByTagName('head')[0]
  let l = document.getElementById('vform-custom-css')
  l && n.removeChild(l), e && ((l = document.getElementById('vform-custom-css-' + e)), !!l && n.removeChild(l))
  let s = document.createElement('style')
  ;(s.type = 'text/css'), (s.rel = 'stylesheet'), (s.id = e ? 'vform-custom-css-' + e : 'vform-custom-css')
  try {
    s.appendChild(document.createTextNode(o))
  } catch {
    s.styleSheet.cssText = o
  }
  n.appendChild(s)
}
export function insertGlobalFunctionsToHtml(o, e = '') {
  const n = document.getElementsByTagName('body')[0]
  let l = document.getElementById('v_form_global_functions')
  !!l && n.removeChild(l), e && ((l = document.getElementById('v_form_global_functions-' + e)), !!l && n.removeChild(l))
  let s = document.createElement('script')
  ;(s.id = e ? 'v_form_global_functions-' + e : 'v_form_global_functions'),
    (s.type = 'text/javascript'),
    (s.innerHTML = o),
    n.appendChild(s)
}
export function deleteCustomStyleAndScriptNode(o, e) {
  const n = document.getElementsByTagName('head')[0]
  let l = document.getElementById('vform-custom-css-' + e)
  o && (l = document.getElementById('vform-custom-css')), l && n.removeChild(l)
  const s = document.getElementsByTagName('body')[0]
  let c = document.getElementById('v_form_global_functions-' + e)
  o && (c = document.getElementById('v_form_global_functions')), c && s.removeChild(c)
}
export function optionExists(o, e) {
  return o ? Object.keys(o).indexOf(e) > -1 : !1
}
export function loadRemoteScript(o, e) {
  let n = encodeURIComponent(o)
  if (!document.getElementById(n)) {
    let s = document.createElement('script')
    ;(s.src = o),
      (s.id = n),
      document.body.appendChild(s),
      (s.onload = s.onreadystatechange =
        function (c, u) {
          ;(u || !s.readyState || s.readyState === 'loaded' || s.readyState === 'complete') &&
            ((s = s.onload = s.onreadystatechange = null), u || e())
        })
  }
}
export function traverseFieldWidgets(o, e, n = null, l = !1) {
  !o ||
    o.map((s) => {
      s.formItemFlag || (s.formItemFlag === !1 && l)
        ? e(s, n)
        : s.type === 'grid'
        ? s.cols.map((c) => {
            traverseFieldWidgets(c.widgetList, e, s, l)
          })
        : s.type === 'table'
        ? s.rows.map((c) => {
            c.cols.map((u) => {
              traverseFieldWidgets(u.widgetList, e, s, l)
            })
          })
        : s.type === 'tab'
        ? s.tabs.map((c) => {
            traverseFieldWidgets(c.widgetList, e, s, l)
          })
        : (s.type === 'sub-form' || s.type === 'grid-sub-form' || s.category === 'container') &&
          traverseFieldWidgets(s.widgetList, e, s, l)
    })
}
export function traverseContainerWidgets(o, e) {
  !o ||
    o.map((n) => {
      n.category === 'container' && e(n),
        n.type === 'grid'
          ? n.cols.map((l) => {
              traverseContainerWidgets(l.widgetList, e)
            })
          : n.type === 'table'
          ? n.rows.map((l) => {
              l.cols.map((s) => {
                traverseContainerWidgets(s.widgetList, e)
              })
            })
          : n.type === 'tab'
          ? n.tabs.map((l) => {
              traverseContainerWidgets(l.widgetList, e)
            })
          : (n.type === 'sub-form' || n.type === 'grid-sub-form' || n.category === 'container') &&
            traverseContainerWidgets(n.widgetList, e)
    })
}
export function traverseAllWidgets(o, e) {
  !o ||
    o.map((n) => {
      e(n),
        n.type === 'grid'
          ? n.cols.map((l) => {
              e(l), traverseAllWidgets(l.widgetList, e)
            })
          : n.type === 'table'
          ? n.rows.map((l) => {
              l.cols.map((s) => {
                e(s), traverseAllWidgets(s.widgetList, e)
              })
            })
          : n.type === 'tab'
          ? n.tabs.map((l) => {
              traverseAllWidgets(l.widgetList, e)
            })
          : (n.type === 'sub-form' || n.type === 'grid-sub-form' || n.category === 'container') &&
            traverseAllWidgets(n.widgetList, e)
    })
}
export function handleWidgetForTraverse(o, e) {
  !!o.category && o.category === 'container' ? traverseFieldWidgetsOfContainer(o, e) : o.formItemFlag && e(o)
}
export function traverseFieldWidgetsOfContainer(o, e) {
  o.type === 'grid'
    ? o.cols.forEach((n) => {
        n.widgetList.forEach((l) => {
          handleWidgetForTraverse(l, e)
        })
      })
    : o.type === 'table'
    ? o.rows.forEach((n) => {
        n.cols.forEach((l) => {
          l.widgetList.forEach((s) => {
            handleWidgetForTraverse(s, e)
          })
        })
      })
    : o.type === 'tab'
    ? o.tabs.forEach((n) => {
        n.widgetList.forEach((l) => {
          handleWidgetForTraverse(l, e)
        })
      })
    : o.type === 'sub-form' || o.type === 'grid-sub-form'
    ? o.widgetList.forEach((n) => {
        handleWidgetForTraverse(n, e)
      })
    : o.category === 'container' &&
      o.widgetList.forEach((n) => {
        handleWidgetForTraverse(n, e)
      })
}
export function handleContainerTraverse(o, e, n, l, s) {
  !!o.category && o.category === 'container' ? traverseWidgetsOfContainer(o, e, n, l, s) : (o.formItemFlag || s) && e(o)
}
export function traverseWidgetsOfContainer(o, e, n, l, s) {
  o.category === 'container' && n(o),
    o.type === 'grid'
      ? o.cols.forEach((c) => {
          l && n(c),
            c.widgetList.forEach((u) => {
              handleContainerTraverse(u, e, n, l, s)
            })
        })
      : o.type === 'table'
      ? o.rows.forEach((c) => {
          l && n(c),
            c.cols.forEach((u) => {
              l && n(u),
                u.widgetList.forEach(($) => {
                  handleContainerTraverse($, e, n, l, s)
                })
            })
        })
      : o.type === 'tab'
      ? o.tabs.forEach((c) => {
          l && n(c),
            c.widgetList.forEach((u) => {
              handleContainerTraverse(u, e, n, l, s)
            })
        })
      : o.type === 'sub-form' || o.type === 'grid-sub-form'
      ? o.widgetList.forEach((c) => {
          handleContainerTraverse(c, e, n, l, s)
        })
      : o.category === 'container' &&
        o.widgetList.forEach((c) => {
          handleContainerTraverse(c, e, n, l, s)
        })
}
export function traverseWidgetsOfGridCol(o, e, n) {
  o.type === 'grid-col' &&
    o.widgetList.forEach((l) => {
      handleContainerTraverse(l, e, n)
    })
}
export function getAllFieldWidgets(o, e = !1) {
  if (!o) return []
  let n = []
  return (
    traverseFieldWidgets(
      o,
      (s) => {
        n.push({ type: s.type, name: s.options.name, field: s })
      },
      null,
      e
    ),
    n
  )
}
export function getAllContainerWidgets(o) {
  if (!o) return []
  let e = []
  return (
    traverseContainerWidgets(o, (l) => {
      e.push({ type: l.type, name: l.options.name, container: l })
    }),
    e
  )
}
export function getFieldWidgetByName(o, e, n) {
  if (!o) return null
  let l = null
  return (
    traverseFieldWidgets(
      o,
      (c) => {
        c.options.name === e && (l = c)
      },
      null,
      n
    ),
    l
  )
}
export function getFieldWidgetById(o, e, n) {
  if (!o) return null
  let l = null
  return (
    traverseFieldWidgets(
      o,
      (c) => {
        c.id === e && (l = c)
      },
      null,
      n
    ),
    l
  )
}
export function getSubFormNameByFieldId(o, e) {
  let n = null
  return (
    getAllContainerWidgets(o).forEach((s) => {
      const c = (u) => {
        u.id === e && (n = s.name)
      }
      ;(s.type === 'sub-form' || s.type === 'grid-sub-form') && traverseFieldWidgetsOfContainer(s.container, c)
    }),
    n
  )
}
export function getContainerWidgetByName(o, e) {
  if (!o) return null
  let n = null
  return (
    traverseContainerWidgets(o, (s) => {
      s.options.name === e && (n = s)
    }),
    n
  )
}
export function getContainerWidgetById(o, e) {
  if (!o) return null
  let n = null
  return (
    traverseContainerWidgets(o, (s) => {
      s.id === e && (n = s)
    }),
    n
  )
}
export function copyToClipboard(o, e, n, l, s) {
  const c = new Clipboard(e.target, { text: () => o })
  c.on('success', () => {
    n.success(l), c.destroy()
  }),
    c.on('error', () => {
      n.error(s), c.destroy()
    }),
    c.onClick(e)
}
export function getQueryParam(o) {
  let n = window.location.search.substring(1).split('&')
  for (let l = 0; l < n.length; l++) {
    let s = n[l].split('=')
    if (s[0] == o) return s[1]
  }
}
export function getDefaultFormConfig() {
  return {
    modelName: 'formData',
    refName: 'vForm',
    rulesName: 'rules',
    labelWidth: 80,
    labelPosition: 'left',
    size: '',
    labelAlign: 'label-left-align',
    cssCode: '',
    customClass: [],
    functions: '',
    layoutType: 'PC',
    jsonVersion: 3,
    dataSources: [],
    onFormCreated: '',
    onFormMounted: '',
    onFormDataChange: '',
    onFormValidate: ''
  }
}
export function buildDefaultFormJson() {
  return {
    widgetList: [],
    formConfig: deepClone(getDefaultFormConfig())
  }
}
export function cloneFormConfigWithoutEventHandler(o) {
  let e = deepClone(o)
  return (e.onFormCreated = ''), (e.onFormMounted = ''), (e.onFormDataChange = ''), (e.onFormValidate = ''), e
}
export function translateOptionItems(o, e, n, l) {
  if (e === 'cascader') return deepClone(o)
  let s = []
  return (
    !!o &&
      o.length > 0 &&
      o.forEach((c) => {
        c.hasOwnProperty('disabled')
          ? s.push({
              label: c[n],
              value: c[l],
              disabled: c.disabled
            })
          : s.push({ label: c[n], value: c[l] })
      }),
    s
  )
}
export function assembleAxiosConfig(o, e, n) {
  let l = {}
  return (
    !o ||
      o.length <= 0 ||
      (o.map((s) => {
        s.type === 'String'
          ? (l[s.name] = String(s.value))
          : s.type === 'Number'
          ? (l[s.name] = Number(s.value))
          : s.type === 'Boolean'
          ? s.value.toLowerCase() === 'false' || s.value === '0'
            ? (l[s.name] = !1)
            : s.value.toLowerCase() === 'true' || s.value === '1'
            ? (l[s.name] = !0)
            : (l[s.name] = null)
          : s.type === 'Variable' && (l[s.name] = evalFn(s.value, e, n))
      }),
      console.log('test DSV: ', e),
      console.log('test VFR: ', n)),
    l
  )
}
export function buildRequestConfig(o, e, n, l) {
  let s = {}
  return (
    o.requestURLType === 'String' ? (s.url = o.requestURL) : (s.url = evalFn(o.requestURL, e, n)),
    (s.method = o.requestMethod),
    (s.headers = assembleAxiosConfig(o.headers, e, n)),
    (s.params = assembleAxiosConfig(o.params, e, n)),
    (s.data = assembleAxiosConfig(o.data, e, n)),
    new Function('config', 'isSandbox', 'DSV', 'VFR', o.configHandlerCode).call(null, s, l, e, n)
  )
}
export async function runDataSourceRequest(o, e, n, l, s) {
  try {
    let c = buildRequestConfig(o, e, n, l),
      u = await axios.request(c)
    return new Function('result', 'isSandbox', 'DSV', 'VFR', o.dataHandlerCode).call(null, u, l, e, n)
  } catch (c) {
    let u = new Function('error', 'isSandbox', 'DSV', '$message', 'VFR', o.errorHandlerCode)
    return console.error(c), u.call(null, c, l, e, s, n)
  }
}
export function getDSByName(o, e) {
  let n = null
  return (
    !!e &&
      !!o.dataSources &&
      o.dataSources.forEach((l) => {
        l.uniqueName === e && (n = l)
      }),
    n || console.error('DS not found: ' + e),
    n
  )
}
export function addZHExtensionLang(o) {
  !!o.extension &&
    !!o.extension.widgetLabel &&
    overwriteObj(zhLocale_extension.extension.widgetLabel, o.extension.widgetLabel),
    !!o.extension && !!o.extension.setting && overwriteObj(zhLocale_extension.extension.setting, o.extension.setting)
}
export function addENExtensionLang(o) {
  !!o.extension &&
    !!o.extension.widgetLabel &&
    overwriteObj(enLocale_extension.extension.widgetLabel, o.extension.widgetLabel),
    !!o.extension && !!o.extension.setting && overwriteObj(enLocale_extension.extension.setting, o.extension.setting)
}
var WidgetProperties = {
  COMMON_PROPERTIES: COMMON_PROPERTIES$1,
  ADVANCED_PROPERTIES: ADVANCED_PROPERTIES$1,
  EVENT_PROPERTIES: EVENT_PROPERTIES$1
}
export const PERegister = Object.freeze(
  Object.defineProperty(
    {
      __proto__: null,
      registerCommonProperty,
      registerAdvancedProperty,
      registerEventProperty,
      propertyRegistered,
      registerCPEditor,
      registerAPEditor,
      registerEPEditor,
      default: WidgetProperties
    },
    Symbol.toStringTag,
    { value: 'Module' }
  )
)
function registerCommonProperty(o, e) {
  COMMON_PROPERTIES$1[o] = e
}
function registerAdvancedProperty(o, e) {
  ADVANCED_PROPERTIES$1[o] = e
}
function registerEventProperty(o, e) {
  EVENT_PROPERTIES$1[o] = e
}
export function propertyRegistered(o) {
  return !!COMMON_PROPERTIES$1[o] || !!ADVANCED_PROPERTIES$1[o] || !!EVENT_PROPERTIES$1[o]
}
function registerCPEditor(o, e, n, l) {
  o.component(n, l), registerCommonProperty(e, n)
}
function registerAPEditor(o, e, n, l) {
  o.component(n, l), registerAdvancedProperty(e, n)
}
function registerEPEditor(o, e, n, l) {
  o.component(n, l), registerEventProperty(e, n)
}

export function addChartContainerSchema(o) {
  chartContainers.push(o);
}
