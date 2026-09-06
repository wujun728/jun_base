import { EventHandler, getDataSetValue, runCommand } from "../utils/helper";
import { FontSizeConfig, fontSizeList } from "../config";
import { CommandType } from "../constant";


type FontSizeToolHookTuple = [
  Ref<number>,
  FontSizeConfig[],
  EventHandler
]

export const useFontSizeTool = (): FontSizeToolHookTuple => {

  const currentFontSize = ref<number>();

  const handleSetFontSize = (e: Event) => {
    const size = getDataSetValue<string>(e, "size");
    if (!size) return;
    runCommand(CommandType.FONT_SIZE, size);
    currentFontSize.value = Number(size);
  }

  return [
    currentFontSize,
    fontSizeList,
    handleSetFontSize
  ];
}