package com.ruoyi.common.utils.poi;

import com.ruoyi.common.annotation.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通用合并导出工具类
 * 功能：
 * 1. 根据实体类中字段上的 @Excel 注解生成表头
 * 2. 若实体包含一个 List 明细字段，则按“主表 + 明细表”形式导出：
 *    - 主表字段在每个明细行重复填充
 *    - 同一主表对应的多行在主表字段列进行纵向单元格合并
 * 3. 若实体不包含 List 明细字段，则按普通列表导出
 * 说明：
 * - 仅识别第一个泛型为实体的 List 字段作为明细（常见场景：如 Plan.planDetailList）
 * - 表头顺序：先主表被 @Excel 标注的字段，后明细实体被 @Excel 标注的字段
 * - 日期格式优先取 @Excel.dateFormat 配置；码值转换支持 @Excel.readConverterExp
 * 注意：该实现基于 Apache POI 的 SXSSFWorkbook（xlsx），适合大数据量导出。
 */
public class ExcelMergeExportUtil {

    /**
     * 导出并根据是否存在明细 List 自动进行单元格纵向合并
     *
     * @param response  HttpServletResponse，直接输出文件流
     * @param data      数据列表（主表实体列表）
     * @param sheetName 工作表名和导出文件名（会导出为 .xlsx）
     * @param <T>       主表实体类型
     */
    public static <T> void exportWithMerge(HttpServletResponse response, List<T> data, String sheetName) {
        if (data == null) {
            data = Collections.emptyList();
        }
        // 若数据为空，尝试从调用泛型上推断类型，不可得则直接结束
        Class<?> clazz = data.isEmpty() ? null : data.get(0).getClass();
        if (clazz == null) {
            writeEmptyWorkbook(response, sheetName);
            return;
        }

        // 收集主表字段（被 @Excel 标注）
        List<ExcelField> masterFields = collectExcelFields(clazz).stream()
                .filter(f -> !isListType(f.field.getType()))
                .toList();

        // 识别一个明细 List 字段（如果存在）
        Field listField = findFirstListField(clazz);
        Class<?> detailClass = resolveListGenericType(listField);
        List<ExcelField> detailFields = detailClass == null ? Collections.emptyList() : collectExcelFields(detailClass);

        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sheet = wb.createSheet(sheetName == null ? "Sheet1" : sheetName);
        int rowIndex = 0;

        // 表头样式
        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 创建表头
        Row headerRow = sheet.createRow(rowIndex++);
        int colIndex = 0;
        for (ExcelField ef : masterFields) {
            createCell(headerRow, colIndex++, ef.excel.name(), headerStyle);
        }
        for (ExcelField ef : detailFields) {
            createCell(headerRow, colIndex++, ef.excel.name(), headerStyle);
        }

        // 内容样式（简单居中）
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 数据区
        for (T master : data) {
            int startRow = rowIndex; // 记录该主表开始行
            List<?> details = extractDetails(master, listField);
            if (details.isEmpty()) {
                // 没有明细时也输出一行
                Row row = sheet.createRow(rowIndex++);
                int c = 0;
                // 主表字段
                for (ExcelField ef : masterFields) {
                    Object val = getFieldValue(master, ef.field);
                    writeCell(row, c++, val, ef, bodyStyle);
                }
                // 明细字段留空
                for (int i = 0; i < detailFields.size(); i++) {
                    createCell(row, c++, "", bodyStyle);
                }
            } else {
                for (Object detail : details) {
                    Row row = sheet.createRow(rowIndex++);
                    int c = 0;
                    // 主表字段（每一行重复写入，随后合并）
                    for (ExcelField ef : masterFields) {
                        Object val = getFieldValue(master, ef.field);
                        writeCell(row, c++, val, ef, bodyStyle);
                    }
                    // 明细字段
                    for (ExcelField ef : detailFields) {
                        Object val = detail == null ? null : getFieldValue(detail, ef.field);
                        writeCell(row, c++, val, ef, bodyStyle);
                    }
                }
                // 对主表字段列进行纵向合并
                if (rowIndex - startRow > 1 && !masterFields.isEmpty()) {
                    for (int col = 0; col < masterFields.size(); col++) {
                        sheet.addMergedRegion(new CellRangeAddress(startRow, rowIndex - 1, col, col));
                    }
                }
            }
        }

        // 自适应列宽（适度）
        int totalCols = masterFields.size() + detailFields.size();
        // SXSSF 模式下需要先开启自动列宽计算的跟踪，否则 autoSizeColumn 不生效
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < totalCols; i++) {
            try {
                sheet.autoSizeColumn(i);
                // 设置一个最大宽度，避免过宽
                int width = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.min(width + 1024, 10000));
            } catch (Exception ignored) {}
        }
        sheet.untrackAllColumnsForAutoSizing();

        // 输出
        try {
            String fileName = (sheetName == null ? "export" : sheetName) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        } finally {
            try { wb.close(); } catch (Exception ignored) {}
        }
    }

    // ============= 内部辅助方法 =============

    /**
     * 收集类中被 @Excel 标注的字段（包含父类），按声明顺序返回
     */
    private static List<ExcelField> collectExcelFields(Class<?> clazz) {
        List<ExcelField> list = new ArrayList<>();
        for (Field f : getAllFields(clazz)) {
            Excel excel = f.getAnnotation(Excel.class);
            if (excel != null) {
                f.setAccessible(true);
                list.add(new ExcelField(f, excel));
            }
        }
        return list;
    }

    /**
     * 递归获取所有字段（包含父类）
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        // 保持原始声明顺序（反射通常已按声明顺序返回）
        return fields;
    }

    /**
     * 查找第一个被 @Excel 标注的 List 字段（明细集合）
     * 用途：用于识别主实体内作为“明细”的集合字段，只有当该 List 字段本身也被 @Excel 标注时才视为可参与导出合并。
     */
    private static Field findFirstListField(Class<?> clazz) {
        for (Field f : getAllFields(clazz)) {
            if (isListType(f.getType()) && f.getAnnotation(Excel.class) != null) {
                f.setAccessible(true);
                return f;
            }
        }
        return null;
    }

    /** 是否是 List 类型 */
    private static boolean isListType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    /** 解析 List 字段的泛型类型 */
    private static Class<?> resolveListGenericType(Field listField) {
        if (listField == null) {
            return null;
        }
        Type g = listField.getGenericType();
        if (g instanceof ParameterizedType) {
            Type[] actual = ((ParameterizedType) g).getActualTypeArguments();
            if (actual.length == 1 && actual[0] instanceof Class) {
                return (Class<?>) actual[0];
            }
        }
        return null;
    }

    /** 提取明细列表 */
    private static List<?> extractDetails(Object master, Field listField) {
        if (master == null || listField == null) {
            return Collections.emptyList();
        }
        try {
            Object v = listField.get(master);
            if (v instanceof List) {
                return (List<?>) v;
            }
        } catch (IllegalAccessException ignored) {}
        return Collections.emptyList();
    }

    /** 获取字段值 */
    private static Object getFieldValue(Object obj, Field field) {
        if (obj == null || field == null) {
            return null;
        }
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /**
     * 写入单元格并根据 @Excel 配置格式化
     * - 日期格式：优先按 @Excel.dateFormat
     * - 码值转换：支持 @Excel.readConverterExp，如 "P01=一般物资,P02=商城选购"
     */
    private static void writeCell(Row row, int col, Object value, ExcelField ef, CellStyle style) {
        String text = formatValue(value, ef.excel);
        createCell(row, col, text, style);
    }

    /** 创建单元格（字符串） */
    private static void createCell(Row row, int col, String text, CellStyle style) {
        Cell cell = row.createCell(col, CellType.STRING);
        cell.setCellValue(text == null ? "" : text);
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    /**
     * 按 @Excel 注解进行基本格式化：
     * - 如果配置了 readConverterExp，则优先进行码值转换
     * - 如果配置了 dateFormat 且值为 Date，则按指定格式输出
     * - 其它类型转为字符串（BigDecimal 保留原样 toString）
     */
    private static String formatValue(Object value, Excel excel) {
        if (value == null) {
            return "";
        }
        if (excel != null) {
            String exp = excel.readConverterExp();
            if (!isBlank(exp)) {
                String mapped = convertByExp(String.valueOf(value), exp);
                if (mapped != null) {
                    return mapped;
                }
            }
            if (value instanceof Date) {
                String pattern = excel.dateFormat();
                if (!isBlank(pattern)) {
                    try { return new SimpleDateFormat(pattern).format((Date) value); } catch (Exception ignored) {}
                }
            }
        }
        return String.valueOf(value);
    }

    /**
     * 码值转换：将原始值通过 readConverterExp 映射为中文含义
     * 例如：exp = "P01=一般物资,P02=商城选购"，value=P01 => 一般物资
     * @return 找不到映射时返回 null
     */
    private static String convertByExp(String value, String exp) {
        if (isBlank(value) || isBlank(exp)) {
            return null;
        }
        String[] mappings = exp.split(",");
        for (String m : mappings) {
            String[] kv = m.split("=");
            if (kv.length == 2) {
                if (Objects.equals(kv[0].trim(), value)) {
                    return kv[1].trim();
                }
            }
        }
        return null;
    }

    /** 输出空工作簿（仅一个空白 sheet） */
    private static void writeEmptyWorkbook(HttpServletResponse response, String sheetName) {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        try (wb) {
            try {
                wb.createSheet(sheetName == null ? "Sheet1" : sheetName);
                String fileName = (sheetName == null ? "export" : sheetName) + ".xlsx";
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                wb.write(response.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException("导出失败", e);
            }
        } catch (Exception ignored) {
        }
    }

    /** 是否空白字符串 */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** 简单包装：字段 + 注解 */
    private static class ExcelField {
        final Field field;
        final Excel excel;
        ExcelField(Field field, Excel excel) { this.field = field; this.excel = excel; }
    }
}