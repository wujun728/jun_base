import { EventHandler, getDataSetValue, runCommand } from "../utils/helper";

type Color = string;

type FontColorToolTuple = [
  Ref<Color>,
  EventHandler
]

export const useFontColorTool = (datasetKey: string, command: string): FontColorToolTuple => {
  const currentColor = ref<Color>();

  const handleSetColor = (e: Event) => {
    const color = getDataSetValue<Color>(e, datasetKey);
    if (!color) return;

    const nextColor = currentColor.value === color ? "" : color;

    runCommand(command, nextColor);
    currentColor.value = nextColor;
  }


  return [
    currentColor,
    handleSetColor
  ];
}