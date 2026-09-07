import {
  defineConfig,
  presetTypography,
  presetUno,
  transformerVariantGroup,
  presetIcons,
} from 'unocss';

export default defineConfig({
  /** presetIcons 支持iconify */
  presets: [presetUno(), presetTypography(), presetIcons()],
  /** 支持这样组合的写法 before:(bg-red w-6px h-6px rounded-full inline-block content-['']) */
  transformers: [transformerVariantGroup()],
  shortcuts: [
    [
      /^dot-before-(.*)$/,
      ([, color]) => {
        return `before:(bg-${color} w-6px h-6px rounded-full inline-block content-[''] position-relative top-[-2px] mr-5px) `;
      },
    ],
    [
      /^dot-after-(.*)$/,
      ([, color]) => {
        return `before:(bg-${color} w-6px h-6px rounded-full inline-block content-[''] position-relative top-[-2px] ml-5px) `;
      },
    ],
  ],
});
