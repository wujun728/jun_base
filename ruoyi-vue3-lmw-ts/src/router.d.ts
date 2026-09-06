import {
  _RouteRecordBase,
  RawRouteComponent,
  _RouteLocationBase,
  RouteMeta,
  LocationQuery,
} from 'vue-router'
declare module 'vue-router' {
  interface _RouteRecordBase {
    hidden?: boolean
    permissions?: string[]
    roles?: string[]
    name?: string
    alwaysShow?: boolean
    fullPath?: string
    parentPath?: string
  }
  interface RouteMeta {
    title: string
    icon?: string
    noCache?: boolean
    link?: string
    activeMenu?: string
  }
  interface _RouteLocationBase {
    fullPath: string
    title?: string
    children?: RouteRecordRaw[]
  }
}
