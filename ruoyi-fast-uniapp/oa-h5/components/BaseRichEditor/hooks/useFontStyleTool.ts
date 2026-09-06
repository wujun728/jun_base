import { CommandType } from "../constant";
import { EventHandler, getDataSetValue, runCommand } from "../utils/helper";

enum FontStyles {
  BOLD = "bold",
  UNDERLINE = "underline",
  THROUGH = "through",
  ITALIC = "italic"
}

interface FontStyleConfig {
  style: FontStyles,
  classes: string[]
}

export const useFontStyleTool = (): [ComputedRef<FontStyleConfig[]>, EventHandler] => {
  const currentFontStyleSet = ref(new Set<FontStyles>());

  const fontStylesIconRecord = {
    [FontStyles.BOLD]: "icon-cuti",
    [FontStyles.UNDERLINE]: "icon-xiahuaxian",
    [FontStyles.THROUGH]: "icon-shanchuxian",
    [FontStyles.ITALIC]: "icon-xieti",
  };

  const fontStyleConfig = computed<FontStyleConfig[]>(() => {
    const set = currentFontStyleSet.value;

    return Object.entries(fontStylesIconRecord)
      .map(([style, icon]) => {
        return {
          style: (style as FontStyles),
          classes: [
            icon,
            set.has(style as FontStyles) ? "active" : null
          ]
        };
      });
  });

  const handleSetFontStyle = (e: Event) => {
    const style = getDataSetValue<FontStyles>(e, "style");
    if (!style) return;
    runCommand(CommandType.FONT_STYLE, style, !!currentFontStyleSet.value.has(style));
    if (currentFontStyleSet.value.has(style)) {
      currentFontStyleSet.value.delete(style);
    } else {
      currentFontStyleSet.value.add(style);
    }
  }

  return [
    fontStyleConfig,
    handleSetFontStyle
  ];
}