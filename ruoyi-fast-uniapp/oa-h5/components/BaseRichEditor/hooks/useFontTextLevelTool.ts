import { CommandType } from "../constant";
import { EventHandler, getDataSetValue, getEnumKeyByValue, runCommand } from "../utils/helper";

enum FontTextLevel {
  HEADER1 = "H1",
  HEADER2 = "H2",
  HEADER3 = "H3",
  PARAGRAPH = "正文"
}

interface FontTextLevelConfig {
  level: string,
  text: FontTextLevel,
  classes: string[]
}

type TextLevelToolTuple = [
  ComputedRef<FontTextLevelConfig[]>,
  EventHandler
];

const normalLevelKey = getEnumKeyByValue(FontTextLevel, FontTextLevel.PARAGRAPH);

export const useFontTextLevelTool = (): TextLevelToolTuple => {
  const currentFontTextLevel = ref<keyof typeof FontTextLevel>();

  const handleSetTextLevel = (e: Event) => {
    const level = getDataSetValue<keyof typeof FontTextLevel>(e, "level");
    if (!level) return;
    // 要设置的级别为正文时，传入的命令值为空白字符串，即清除格式
    // 如果当前要设置的级别已被设置，则取消设置
    const isEqualLevel = level === currentFontTextLevel.value;
    const isNormalLevel = level === normalLevelKey;

    const isClearLevel = isEqualLevel || isNormalLevel;

    const nextLevel = isClearLevel ? normalLevelKey : level;

    runCommand(CommandType.HEADER_LEVEL, nextLevel.toLocaleLowerCase());

    currentFontTextLevel.value = isEqualLevel ? undefined : level;
  }

  const fontTextLevelConfig = computed<FontTextLevelConfig[]>(() => {
    return Object.entries(FontTextLevel)
      .map(([level, text]) => {
        return {
          level,
          text,
          classes: [
            currentFontTextLevel.value === level ? "active" : null
          ]
        };
      });
  });

  return [
    fontTextLevelConfig,
    handleSetTextLevel
  ];
}