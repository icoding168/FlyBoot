package xyz.icoding168.flyboot.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class PaginationResponseBuilder<T> implements Serializable {

    public abstract int count();

    public abstract List<T> find();

    public PaginationResponse<T> handlePaginationRequest(PaginationRequest paginationRequest){
        if(paginationRequest == null){
            throw new UncheckedException();
        }

        if(paginationRequest.getPage() == null || paginationRequest.getPage().intValue() <= 0){
            throw new UncheckedException();
        }

        if(paginationRequest.getPageSize() == null || paginationRequest.getPageSize().intValue() <= 0){
            throw new UncheckedException();
        }

        Integer totalRecord = count();

        if(totalRecord == 0){
            return buildDefaultPaginationResponse();
        }

        List list = find();

        return buildPaginationResponse(paginationRequest,totalRecord,list);
    }

    public static PaginationResponse buildDefaultPaginationResponse(){
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setList(new ArrayList());
        return paginationResponse;
    }

    public static PaginationResponse buildPaginationResponse(PaginationRequest request,Integer totalRecord,List list){
        PaginationResponse paginationResponse = compute(request.getPage(),request.getPageSize(),totalRecord);
        paginationResponse.setList(list == null ? new ArrayList():list);
        return paginationResponse;
    }

    public static PaginationResponse copyPaginationInfo(PaginationResponse source,List list){
        PaginationResponse copy = compute(source.getPage(),source.getPageSize(),source.getTotalRecord());
        copy.setList(list == null ? new ArrayList():list);
        return copy;
    }


    private static PaginationResponse compute(Integer page,Integer pageSize,Integer totalRecord){
        if(page == null || page.intValue() <= 0){
            throw new UncheckedException();
        }

        if(pageSize == null || pageSize.intValue() <= 0){
            throw new UncheckedException();
        }

        if(totalRecord == null || totalRecord.intValue() <= 0){
            throw new UncheckedException();
        }

        PaginationResponse paginationResponse = new PaginationResponse();

        paginationResponse.setPage(page);
        paginationResponse.setPageSize(pageSize);
        paginationResponse.setTotalRecord(totalRecord);

        int totalPage = totalRecord / pageSize;

        if (totalRecord % pageSize != 0) {
            totalPage = totalPage + 1;
        }

        paginationResponse.setTotalPage(totalPage);

        if(page < totalPage){
            paginationResponse.setHasNext(true);
        }

        if(paginationResponse.getHasNext()){
            paginationResponse.setNextPage(page + 1);
        }

        if(page > 1){
            paginationResponse.setPrevPage(page - 1);
            paginationResponse.setHasPrev(true);
        }

        return paginationResponse;
    }

    public static void main(String[] args){
        System.out.println(85 / 10);
    }

}
