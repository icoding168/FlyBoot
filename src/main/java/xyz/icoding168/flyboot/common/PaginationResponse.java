package xyz.icoding168.flyboot.common;

import java.io.Serializable;
import java.util.List;

public class PaginationResponse<T> implements Serializable {

    private Integer page = 1;
    private Integer pageSize = 10;

    private Integer totalPage = 0;
    private Integer totalRecord = 0;

    private Integer nextPage = 0;
    private Boolean hasNext =  false;

    private List<T> list = null;

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

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }



}
