package xyz.icoding168.flyboot.common;

import java.io.Serializable;

public class PaginationRequest extends CommonRequest implements Serializable {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Boolean findAll = false;
    private Integer start = 0;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if(page != null && page <= 0){
            throw new UncheckedException("页码不能小于等于0");
        }
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize != null && pageSize <= 0){
            throw new UncheckedException("每页大小不能小于等于0");
        }

        this.pageSize = pageSize;
    }

    public Boolean getFindAll() {
        return findAll;
    }

    public void setFindAll(Boolean findAll) {
        if(findAll != null){
            this.findAll = findAll;
        }
    }

    public void setStart(Integer from) {
        this.start = from;
    }

    public Integer getStart() {
        int start = (page * pageSize) - pageSize;
        return start;
    }
}
