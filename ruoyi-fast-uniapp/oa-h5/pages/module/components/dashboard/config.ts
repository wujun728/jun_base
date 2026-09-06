export const baseOptions = {
  color: [
    "#1890FF",
    "#4BCAD5",
    "#FFCD27"
  ],
  dataPointShape: false,
  fontSize: uni.upx2px(22),
  fontColor: "#909399",
  padding: [3, uni.upx2px(24), 20, uni.upx2px(18)],
  dataLabel: false,
  pixelRatio: 2,
  title: {
    name: "",
  },
  subtitle: {
    name: ""
  },
  legend: {
    position: "top",
    float: "right",

    // uchart没有以下选项，通过修改 u-charts.js 4963行实现
    itemBorderRadius: uni.upx2px(4),
    itemWidth: uni.upx2px(24),
    itemHeight: uni.upx2px(20)
  },
  extra: {
    tooltip: {
      showArrow: false,
      borderRadius: uni.upx2px(8),
      bgColor: "#fff",
      bgOpacity: 1,
      boxPadding: uni.upx2px(16),
      fontSize: uni.upx2px(24),
      fontColor: "#909399",
      legendShape: "hollowCircle",
      categoryColor: "#606266",
      showCategory: true,

      boxShadow: {
        shadowOffsetX: 0,
        shadowOffsetY: 2,
        shadowBlur: 6,
        shadowColor: "rgba(0,0,0,0.12)"
      }
    },
    funnel: {
      type: "funnel",
      border: false,
      labelAlign: "right",
      minSize: 30,
      activeWidth: uni.upx2px(80) / 2,
      labelValueColor: "#606266",
    },
    pie: {
      borderWidth: 4,
      labelValueShow: true,
      labelValueColor: "#606266"
    },
    ring: {
      ringWidth: uni.upx2px(40),
      border: false
    },
    radar: {
      gridColor: "#eaeaea",
      gridCount: 3,
      opacity: 0.2,
      border: true,
      borderWidth: 1
    },
    bar: {
      width: uni.upx2px(8),
      seriesGap: uni.upx2px(4),
      barBorderCircle: true,
    },
    column: {
      barBorderCircle: true,
      width: uni.upx2px(10),
      seriesGap: uni.upx2px(6)
    },
    line: {
      type: "straight"
    },
    area: {
      type: "curve",
      opacity: 0.12,
      width: uni.upx2px(2),
      gradient: true
    },
    arcbar: {
      type: "circle",
      width: uni.upx2px(24),
      direction: "ccw",
      backgroundColor: "#f1f1f1",
      startAngle: 1.5,
      radius: uni.upx2px(284) / 2
    }
  },
  xAxis: {
    axisLineColor: "#C9CDD4",
    fontColor: "#909399",
    marginTop: uni.upx2px(14),
  },
  yAxis: {
    gridType: "dash",
    gridColor: "#E5E6EB",
    dashLength: 10,
    showTitle: true,
  }
}