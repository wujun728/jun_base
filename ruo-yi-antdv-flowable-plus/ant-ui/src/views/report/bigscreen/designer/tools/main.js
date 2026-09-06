/*
 * @Descripttion: json 入口文件
 * @version:
 * @Author: qianlishi
 * @Date: 2021-08-29 07:46:46
 * @LastEditors: qianlishi
 * @LastEditTime: 2021-09-28 13:33:47
 */

import {widgetText} from './echartsConfigJson/widget-text'
import {widgetMarquee} from './echartsConfigJson/widget-marquee'
import {widgetHref} from './echartsConfigJson/widget-href'
import {widgetTime} from './echartsConfigJson/widget-time'
import {widgetImage} from './echartsConfigJson/widget-image'
import {widgetSliders} from './echartsConfigJson/widget-slider'
import {widgetVideo} from './echartsConfigJson/widget-video'
import {widgetTable} from './echartsConfigJson/widget-table'
import {widgetIframe} from './echartsConfigJson/widget-iframe'
import {widgetUniversal} from './echartsConfigJson/widget-universal'
import {widgetBarchart} from './echartsConfigJson/widget-barchart'
import {widgetGradientBarchart} from './echartsConfigJson/widget-gradient-barchart'
import {widgetLinechart} from './echartsConfigJson/widget-linechart'
import {widgetBarlinechart} from './echartsConfigJson/widget-barlinechart'
import {widgetPiechart} from './echartsConfigJson/widget-piechart'
import {widgetFunnel} from './echartsConfigJson/widget-funnel'
import {widgetGauge} from './echartsConfigJson/widget-gauge'
import {widgetMap} from './echartsConfigJson/widget-map'
import {WidgetPieNightingale} from './echartsConfigJson/widget-pie-nightingale'
import {widgetPiePercentage} from './echartsConfigJson/widget-pie-percentage'
import {widgetAirbubbleMap} from './echartsConfigJson/widget-airbubble-map'
import {widgetBarStack} from './echartsConfigJson/widget-bar-stack'
import {widgetLineStack} from './echartsConfigJson/widget-line-stack'
import {widgetBarCompare} from './echartsConfigJson/widget-bar-compare'
import {widgetLineCompare} from './echartsConfigJson/widget-line-compare'
import {widgetDecoratePie} from './echartsConfigJson/widget-decorate-pie'

export const widgetTool = [
  // type=html类型的组件
  widgetText,
  widgetMarquee,
  widgetHref,
  widgetTime,
  widgetImage,
  //  widgetSliders,
  widgetVideo,
  widgetTable,
  widgetIframe,
  //  widgetUniversal,
  widgetBarchart,
  widgetGradientBarchart,
  widgetLinechart,
  widgetBarlinechart,
  widgetPiechart,
  widgetFunnel,
  widgetGauge,
  widgetMap,
  WidgetPieNightingale,
  widgetPiePercentage,
  widgetAirbubbleMap,
  widgetBarStack,
  widgetLineStack,
  widgetBarCompare,
  widgetLineCompare,
  widgetDecoratePie
]
