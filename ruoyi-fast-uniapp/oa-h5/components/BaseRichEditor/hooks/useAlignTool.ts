import { EventHandler, getDataSetValue, runCommand } from "../utils/helper";

type AlignToolHookTuple<T> = [
  Ref<T>,
  EventHandler
]

export const useAlignTool = <T>(datasetKey: string, formatName: string): AlignToolHookTuple<T> => {
  const currentType = ref<T>();

  const handleSetType = (e: Event) => {
    const type = getDataSetValue<T>(e, datasetKey);
    if (!type) return;
    currentType.value = type;
    runCommand(formatName, type);
  }

  return [
    currentType,
    handleSetType
  ];
}