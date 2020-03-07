package xyz.icoding168.flyboot.request;


import xyz.icoding168.flyboot.common.PaginationRequest;

public class BlogRequest extends PaginationRequest {

    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
