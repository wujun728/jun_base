declare module "*.svg";
declare module "*.png";
declare module "*.jpg";
declare module "*.jpeg";
declare module "*.gif";
declare module "*.bmp";
declare module "*.tiff";


declare module "file-saver";
declare module "js-cookie";

declare module "*.vue" {
  import { DefineComponent } from "vue";
  const component: DefineComponent<object, object, any>;
  export default component;
}

declare module '*.glsl' {
  const value: string;
  export default value;
}

declare module 'particles.vue3';
declare module 'jsencrypt/bin/jsencrypt.min';

declare module '*.mjs' {
  const value: any;
  export default value;
}