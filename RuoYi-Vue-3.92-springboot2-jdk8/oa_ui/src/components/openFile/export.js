import { el } from '@fullcalendar/core/internal-common'
import Excel from 'exceljs'
import FileSaver from 'file-saver'
const exportExcel = function (luckysheet, value, getBlob) {
  // 参数为luckysheet.getluckysheetfile()获取的对象
  // 1.创建工作簿，可以为工作簿添加属性
  const workbook = new Excel.Workbook()
  // 2.创建表格，第二个参数可以配置创建什么样的工作表
  if (Object.prototype.toString.call(luckysheet) === '[object Object]') {
    luckysheet = [luckysheet]
  }
  luckysheet.forEach(function (table) {
    if (table.data.length === 0) return true
    // ws.getCell('B2').fill = fills.
    const worksheet = workbook.addWorksheet(table.name)
    const merge = (table.config && table.config.merge) || {}
    const borderInfo = (table.config && table.config.borderInfo) || {}
    const columnlenInfo = (table.config && table.config.columnlen) || {}
    const rowHeight = (table.config && table.config.rowlen) || {}

    // 3.设置单元格合并,设置单元格边框,设置单元格样式,设置值
    setStyleAndValue(table.data, worksheet)
    setMerge(merge, worksheet)
    setBorder(borderInfo, worksheet)
    setColumnlen(columnlenInfo, worksheet)
    setRowHeight(rowHeight, worksheet)
    setImages(table, worksheet, workbook)

    return true
  })
  // 4.写入 buffer
  const buffer = workbook.xlsx.writeBuffer().then((data) => {
    const blob = new Blob([data], {
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    })
    if (getBlob) {
      return blob
    } else {
      FileSaver.saveAs(blob, `${value}.xlsx`)
    }
  })
  return buffer
}

/**
 * 行高
 * @param rowHeight
 * @param worksheet
 * @param excelType
 */
var setRowHeight = function (rowHeight, worksheet, excelType) {
  //导出的文件用wps打开和用excel打开显示的行高大一倍
  for (let key in rowHeight) {
    worksheet.getRow(parseInt(key) + 1).height = rowHeight[key] * 1
  }
}

var setColumnlen = function (columnlenInfo, worksheet) {
  const columnlenArr = Object.values(columnlenInfo)
  columnlenArr.forEach(function (elem, key) {
    if (elem && worksheet.columns[key] != undefined) {
      worksheet.columns[key].width = Math.ceil(elem / 8)
    }
  })
}

var setImages = function (table, worksheet, workbook) {
  let {
    images,
    visibledatacolumn, //所有行的位置
    visibledatarow //所有列的位置
  } = { ...table }
  if (typeof images != 'object') return
  for (let key in images) {
    // 通过 base64  将图像添加到工作簿
    const myBase64Image = images[key].src
    //开始行 开始列 结束行 结束列
    const item = images[key]
    const imageId = workbook.addImage({
      base64: myBase64Image,
      extension: 'png'
    })
    if (item.type == '2') {
      worksheet.addImage(imageId, {
        tl: { col: item.fromCol, row: item.fromRow },
        br: { col: item.toCol, row: item.toRow },
        editAs: 'oneCell'
      })
    } else {
      if (item.hasOwnProperty('fromCol') && item.hasOwnProperty('fromRow')) {
        col_st = item.fromCol
        row_st = item.fromRow
        worksheet.addImage(imageId, {
          tl: { col: item.fromCol, row: item.fromRow },
          br: { col: item.toCol, row: item.toRow },
          editAs: 'oneCell'
        })
      } else {
        const col_st = getImagePosition(item.default.left, visibledatacolumn)
        const row_st = getImagePosition(item.default.top, visibledatarow)
        //模式1，图片左侧与luckysheet位置一样，像素比例保持不变，但是，右侧位置可能与原图所在单元格不一致
        worksheet.addImage(imageId, {
          tl: { col: col_st, row: row_st },
          ext: { width: item.default.width, height: item.default.height }
        })
      }
    }
  }
}

//获取图片在单元格的位置
var getImagePosition = function (num, arr) {
  let index = 0
  let minIndex
  let maxIndex
  for (let i = 0; i < arr.length; i++) {
    if (num < arr[i]) {
      index = i
      break
    }
  }

  if (index == 0) {
    minIndex = 0
    maxIndex = 1
  } else if (index == arr.length - 1) {
    minIndex = arr.length - 2
    maxIndex = arr.length - 1
  } else {
    minIndex = index - 1
    maxIndex = index
  }
  let min = arr[minIndex]
  let max = arr[maxIndex]
  let radio = Math.abs((num - min) / (max - min)) + index
  return radio
}

var setImages = function (table, worksheet, workbook) {
  let {
    images,
    visibledatacolumn, //所有行的位置
    visibledatarow //所有列的位置
  } = { ...table }
  if (typeof images != 'object') return
  for (let key in images) {
    // 通过 base64  将图像添加到工作簿
    const myBase64Image = images[key].src
    //开始行 开始列 结束行 结束列
    const item = images[key]
    const imageId = workbook.addImage({
      base64: myBase64Image,
      extension: 'png'
    })

    const col_st = getImagePosition(item.default.left, visibledatacolumn)
    const row_st = getImagePosition(item.default.top, visibledatarow)

    //模式1，图片左侧与luckysheet位置一样，像素比例保持不变，但是，右侧位置可能与原图所在单元格不一致
    worksheet.addImage(imageId, {
      tl: { col: col_st, row: row_st },
      ext: { width: item.default.width, height: item.default.height }
    })
    //模式2,图片四个角位置没有变动，但是图片像素比例可能和原图不一样
    // const w_ed = item.default.left+item.default.width;
    // const h_ed = item.default.top+item.default.height;
    // const col_ed = getImagePosition(w_ed,visibledatacolumn);
    // const row_ed = getImagePosition(h_ed,visibledatarow);
    // worksheet.addImage(imageId, {
    //   tl: { col: col_st, row: row_st},
    //   br: { col: col_ed, row: row_ed},
    // });
  }
}

var setMerge = function (luckyMerge = {}, worksheet) {
  const mergearr = Object.values(luckyMerge)
  mergearr.forEach(function (elem) {
    // elem格式：{r: 0, c: 0, rs: 1, cs: 2}
    // 按开始行，开始列，结束行，结束列合并（相当于 K10:M12）
    worksheet.mergeCells(elem.r + 1, elem.c + 1, elem.r + elem.rs, elem.c + elem.cs)
  })
}
var setBorder = function (luckyBorderInfo, worksheet) {
  if (!Array.isArray(luckyBorderInfo)) {
    return
  }
  luckyBorderInfo.forEach(function (elem) {
    // 现在只兼容到borderType 为range的情况
    if (elem.rangeType === 'range') {
      let border = borderConvert(elem.borderType, elem.style, elem.color)
      let rang = elem.range[0]
      let row = rang.row
      let column = rang.column
      for (let i = row[0] + 1; i < row[1] + 2; i++) {
        for (let y = column[0] + 1; y < column[1] + 2; y++) {
          worksheet.getCell(i, y).border = border
        }
      }
    }
    if (elem.rangeType === 'cell') {
      // col_index: 2
      // row_index: 1
      // b: {
      //   color: '#d0d4e3'
      //   style: 1
      // }
      const { col_index, row_index } = elem.value
      const borderData = Object.assign({}, elem.value)
      delete borderData.col_index
      delete borderData.row_index
      let border = addborderToCell(borderData, row_index, col_index)
      worksheet.getCell(row_index + 1, col_index + 1).border = border
    }
  })
}
var setStyleAndValue = function (cellArr, worksheet) {
  if (!Array.isArray(cellArr)) {
    return
  }
  cellArr.forEach(function (row, rowid) {
    row.every(function (cell, columnid) {
      if (!cell) {
        return true
      }
      let fill = fillConvert(cell.bg)
      let font = fontConvert(cell.ff, cell.fc, cell.bl, cell.it, cell.fs, cell.cl, cell.ul)
      let alignment = alignmentConvert(cell.vt, cell.ht, cell.tb, cell.tr)
      let value = ''
      if (cell.f) {
        value = { formula: cell.f, result: cell.v }
      } else if (!cell.v && cell.ct && cell.ct.s) {
        // xls转为xlsx之后，内部存在不同的格式，都会进到富文本里，即值不存在与cell.v，而是存在于cell.ct.s之后
        // value = cell.ct.s[0].v
        cell.ct.s.forEach((arr) => {
          value += arr.v
        })
      } else {
        value = cell.v
      }
      //  style 填入到_value中可以实现填充色
      let letter = createCellPos(columnid)
      let target = worksheet.getCell(letter + (rowid + 1))
      for (const key in fill) {
        target.fill = fill
        break
      }
      target.font = font
      target.alignment = alignment
      target.value = value
      return true
    })
  })
}
var fillConvert = function (bg) {
  if (!bg) {
    return {}
  }
  // const bgc = bg.replace('#', '')
  let fill = {
    type: 'pattern',
    pattern: 'solid',
    fgColor: { argb: bg.replace('#', '') }
  }
  return fill
}
var fontConvert = function (ff = 0, fc = '#000000', bl = 0, it = 0, fs = 10, cl = 0, ul = 0) {
  // luckysheet：ff(样式), fc(颜色), bl(粗体), it(斜体), fs(大小), cl(删除线), ul(下划线)
  const luckyToExcel = {
    0: '微软雅黑',
    1: '宋体（Song）',
    2: '黑体（ST Heiti）',
    3: '楷体（ST Kaiti）',
    4: '仿宋（ST FangSong）',
    5: '新宋体（ST Song）',
    6: '华文新魏',
    7: '华文行楷',
    8: '华文隶书',
    9: 'Arial',
    10: 'Times New Roman ',
    11: 'Tahoma ',
    12: 'Verdana',
    num2bl: function (num) {
      return num === 0 ? false : true
    }
  }
  // 出现Bug，导入的时候ff为luckyToExcel的val
  let font = {
    name: typeof ff === 'number' ? luckyToExcel[ff] : ff,
    family: 1,
    size: fs,
    color: { argb: fc.replace('#', '') },
    bold: luckyToExcel.num2bl(bl),
    italic: luckyToExcel.num2bl(it),
    underline: luckyToExcel.num2bl(ul),
    strike: luckyToExcel.num2bl(cl)
  }
  return font
}
var alignmentConvert = function (vt = 'default', ht = 'default', tb = 'default', tr = 'default') {
  // luckysheet:vt(垂直), ht(水平), tb(换行), tr(旋转)
  const luckyToExcel = {
    vertical: {
      0: 'middle',
      1: 'top',
      2: 'bottom',
      default: 'top'
    },
    horizontal: {
      0: 'center',
      1: 'left',
      2: 'right',
      default: 'left'
    },
    wrapText: {
      0: false,
      1: false,
      2: true,
      default: false
    },
    textRotation: {
      0: 0,
      1: 45,
      2: -45,
      3: 'vertical',
      4: 90,
      5: -90,
      default: 0
    }
  }
  let alignment = {
    vertical: luckyToExcel.vertical[vt],
    horizontal: luckyToExcel.horizontal[ht],
    wrapText: luckyToExcel.wrapText[tb],
    textRotation: luckyToExcel.textRotation[tr]
  }
  return alignment
}
var borderConvert = function (borderType, style = 1, color = '#000') {
  // 对应luckysheet的config中borderinfo的的参数
  if (!borderType) {
    return {}
  }
  const luckyToExcel = {
    type: {
      'border-all': 'all',
      'border-top': 'top',
      'border-right': 'right',
      'border-bottom': 'bottom',
      'border-left': 'left'
    },
    style: {
      0: 'none',
      1: 'thin',
      2: 'hair',
      3: 'dotted',
      4: 'dashDot', // 'Dashed',
      5: 'dashDot',
      6: 'dashDotDot',
      7: 'double',
      8: 'medium',
      9: 'mediumDashed',
      10: 'mediumDashDot',
      11: 'mediumDashDotDot',
      12: 'slantDashDot',
      13: 'thick'
    }
  }
  let template = {
    style: luckyToExcel.style[style],
    color: { argb: color.replace('#', '') }
  }
  let border = {}
  if (luckyToExcel.type[borderType] === 'all') {
    border['top'] = template
    border['right'] = template
    border['bottom'] = template
    border['left'] = template
  } else {
    border[luckyToExcel.type[borderType]] = template
  }
  return border
}
function addborderToCell(borders, row_index, col_index) {
  let border = {}
  const luckyExcel = {
    type: {
      l: 'left',
      r: 'right',
      b: 'bottom',
      t: 'top'
    },
    style: {
      0: 'none',
      1: 'thin',
      2: 'hair',
      3: 'dotted',
      4: 'dashDot', // 'Dashed',
      5: 'dashDot',
      6: 'dashDotDot',
      7: 'double',
      8: 'medium',
      9: 'mediumDashed',
      10: 'mediumDashDot',
      11: 'mediumDashDotDot',
      12: 'slantDashDot',
      13: 'thick'
    }
  }
  for (const bor in borders) {
    if (borders[bor].color.indexOf('rgb') === -1) {
      border[luckyExcel.type[bor]] = {
        style: luckyExcel.style[borders[bor].style],
        color: { argb: borders[bor].color.replace('#', '') }
      }
    } else {
      border[luckyExcel.type[bor]] = {
        style: luckyExcel.style[borders[bor].style],
        color: { argb: borders[bor].color }
      }
    }
  }
  return border
}
function createCellPos(n) {
  let ordA = 'A'.charCodeAt(0)
  let ordZ = 'Z'.charCodeAt(0)
  let len = ordZ - ordA + 1
  let s = ''
  while (n >= 0) {
    s = String.fromCharCode((n % len) + ordA) + s
    n = Math.floor(n / len) - 1
  }
  return s
}
export { exportExcel }
