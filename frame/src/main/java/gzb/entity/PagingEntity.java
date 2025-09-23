package gzb.entity;

import gzb.tools.JSONResult;

import java.util.ArrayList;
import java.util.List;

public class PagingEntity {
    List<?> list = null;
    int page;
    int size;
    int total;

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        return toJSONResult().toString();
    }
    public JSONResult toJSONResult() {

        return new JSONResult().paging(list, page, size, total);
    }

    public List<?> getList() {
        return list;
    }

    public PagingEntity setList(List<?> list, int total) {
        this.list = list;
        this.total = total;
        return this;
    }

    public PagingEntity setList(List<?> list, Integer page, Integer size) {
        if (list == null || list.size() == 0) {
            this.list = new ArrayList<>();
            this.total = 0;
        } else {
            int start = 0;
            if (page == null || page < 0) {
                page = 1;
            }
            if (size == null || size < 0) {
                size = 10;
            }
            if (page > 1) {
                start = (page - 1) * size;
            }
            List<Object> listThis = new ArrayList<>();
            for (int i = start; i < list.size(); i++) {
                listThis.add(list.get(i));
                if (size > 0 && listThis.size() == size) {
                    break;
                }
            }
            this.list = listThis;
            this.total = list.size();
        }
        this.page = page;
        this.size = size;
        return this;
    }

    public int getPage() {
        return page;
    }

    public PagingEntity setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public PagingEntity setSize(int size) {
        this.size = size;
        return this;
    }

    public int getTotal() {
        return total;
    }

    public PagingEntity setTotal(int total) {
        this.total = total;
        return this;
    }


}
