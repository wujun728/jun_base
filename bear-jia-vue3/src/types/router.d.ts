import 'vue-router';
import type { Component } from 'vue';
import type { RouteMeta } from 'vue-router';

declare module 'vue-router' {
  interface RouteMeta {
    title?: string;
    icon?: string;
    roles?: string[];
    permissions?: string[];
    hidden?: boolean;
    breadcrumb?: boolean;
    activeMenu?: string;
    noCache?: boolean;
    affix?: boolean;
  }
}

export interface AppRouteRecordRaw {
  path: string;
  name?: string;
  meta?: RouteMeta;
  redirect?: string;
  component: Component | string;
  children?: AppRouteRecordRaw[];
  hidden?: boolean;
  alwaysShow?: boolean;
}

export interface RouteState {
  routes: AppRouteRecordRaw[];
  addRoutes: AppRouteRecordRaw[];
  defaultRoutes: AppRouteRecordRaw[];
  topbarRouters: AppRouteRecordRaw[];
  sidebarRouters: AppRouteRecordRaw[];
} 
