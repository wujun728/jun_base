package ginyi.common.mysql;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 分页参数有传，根据分页查询，没传，查询所有
 */
@Data
public class MyPage {

    /**
     * 当前页
     */
    private Long pageNum;
    /**
     * 每页条数
     */
    private Long pageSize;
    /**
     * 是否进行分页
     */
    private boolean isPage;

    public MyPage() {
    }

    public MyPage(Long page, Long pageSize) {
        if ((page != null && page > 0) && (pageSize != null && pageSize > 0)) {
            this.pageNum = page;
            this.pageSize = pageSize;
            this.isPage = true;
        }else {
            this.pageNum = 1L;
            this.pageSize = 10L;
            this.isPage = false;
        }
    }

    public Page getPage(){
        return this.isPage ? new Page(this.pageNum, this.pageSize) : new Page().setSize(10000L);
    }

}
