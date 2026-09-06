import { postcssIsolateStyles } from 'vitepress'

export default {
  plugins: [
    postcssIsolateStyles({
      includeFiles: [/vp-doc\.css/], // defaults to /base\.css/
    }),
  ],
}
