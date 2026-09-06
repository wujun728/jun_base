package com.aidex.common.core.page;

import com.aidex.common.utils.ServletUtils;
import com.aidex.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分页数据
 *
 * @author ruoyi
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 当前记录起始索引
     */
    private Integer pageNum = 1;

    /**
     * 每页显示记录数
     */
    private Integer pageSize = Integer.valueOf("10"); // 页面大小，
    // 设置为“-1”表示不进行分页（分页无效）

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    public PageDomain() {

    }

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public PageDomain(HttpServletRequest request, HttpServletResponse response) {
        this(request, response, -2);
    }

    /**
     * 构造方法
     *
     * @param request         传递 repage 参数，来记住页码
     * @param response        用于设置 Cookie，记住页码
     * @param defaultPageSize 默认分页大小，如果传递 -1 则为不分页，返回所有数据
     */
    public PageDomain(HttpServletRequest request, HttpServletResponse response, int defaultPageSize) {
        // 设置页码参数（传递repage参数，来记住页码）
        String pageNum = request.getParameter(PAGE_NUM);
        if (StringUtils.isNumeric(pageNum)) {
            this.setPageNum(Integer.parseInt(pageNum));
        }
        // 设置页面大小参数（传递repage参数，来记住页码大小）
        String pageSize = request.getParameter(PAGE_SIZE);
        if (StringUtils.isNumeric(pageSize)) {
            this.setPageSize(Integer.parseInt(pageSize));
        } else if (defaultPageSize != -2) {
            this.pageSize = defaultPageSize;
        }
        // 设置排序参数
        String orderByColumn = request.getParameter(ORDER_BY_COLUMN);
        if (StringUtils.isNotBlank(orderByColumn)) {
            this.setOrderByColumn(orderByColumn);
        }

        String isAsc = request.getParameter(IS_ASC);
        if (StringUtils.isNotBlank(isAsc)) {
            this.setIsAsc(isAsc);
        }

        this.setReasonable(ServletUtils.getParameterToBool(REASONABLE));

    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (StringUtils.isNotBlank(isAsc) && (isAsc.indexOf("asc") >= 0 || isAsc.indexOf("ASC") >= 0)) {
            this.isAsc = "asc";
        } else if (StringUtils.isNotBlank(isAsc) && (isAsc.indexOf("desc") >= 0 || isAsc.indexOf("DESC") >= 0)) {
            this.isAsc = "desc";
        }

    }

    public Boolean getReasonable() {
        if (StringUtils.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }
}
