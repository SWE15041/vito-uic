package com.jay.vito.storage.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * 作者: huangqj
 * 版本: v1.0
 * 日期: Mar 5, 2012 10:52:50 AM
 * 描述: 分页的工具类
 */
public class Page<T> implements Serializable {

    /**
     * 当前所在页码
     */
    protected int pageNo = 1;
    /**
     * 当前单页显示条目
     */
    protected int pageSize = 10;
    /**
     * 当前请求结果集
     */
    protected List<T> items = null;
    /**
     * 当前数据总数
     */
    protected long totalCount = -1;
    /**
     * 上一页
     */
    protected int lastPage;//上一页

    //排序的
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public Page() {
    }

    public Page(int pageNo) {
        this.pageNo = pageNo;

    }

    public Page(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 获得当前页的页号, 序号从1开始, 默认为1.
     */
    public int getPageNo() {
        int totalPages = getTotalPages();
        if (pageNo > totalPages) {
            this.pageNo = totalPages;
        }
        return pageNo;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * 设置当前页的页号, 序号从1开始, 低于1时自动调整为1.
     */
    public void setPageNo(final int pageNo) {
        this.pageNo = pageNo;

        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    /**
     * 获得每页的记录数量, 默认为10.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量, 低于1时自动调整为1.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;

//		if (pageSize < 1) {
//			this.pageSize = 1;
//		}
    }

    /**
     * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置, 序号从0开始.
     */
    public long getOffset() {
        return ((getPageNo() - 1) * pageSize);
    }

    public long getBeginIndex() {
        return getOffset();
    }

    public long getEndIndex() {
        long endIndex = getPageNo() == getTotalPages() ? totalCount : getPageNo() * pageSize;
        return endIndex;
    }

    /**
     * 获得页内的记录列表.
     */
    public List<T> getItems() {
        return items;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setItems(final List<T> items) {
        this.items = items;
    }

    /**
     * 获得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 实现Iterable接口, 可以for(Object item : page)遍历使用
     */
    public Iterator<T> iterator() {
        return items.iterator();
    }

    /**
     * 根据pageSize与totalItems计算总页数.
     */
    public int getTotalPages() {
        int totalPages = (int) Math.ceil((double) totalCount / (double) getPageSize());
        if (totalPages <= 0) {
            totalPages = 1;
        }
        return totalPages;

    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNextPage() {
        return (getPageNo() + 1 <= getTotalPages());
    }

    /**
     * 是否最后一页.
     */
    public boolean isLastPage() {
        return !isHasNextPage();
    }

    /**
     * 取得下页的页号, 序号从1开始.
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNextPage()) {
            return getPageNo() + 1;
        } else {
            return getPageNo();
        }
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPrePage() {
        return (getPageNo() > 1);
    }

    /**
     * 是否第一页.
     */
    public boolean isFirstPage() {
        return !isHasPrePage();
    }

    /**
     * 取得上页的页号, 序号从1开始.
     * 当前页为首页时返回首页序号.
     */
    public int getPrePage() {
        if (isHasPrePage()) {
            return getPageNo() - 1;
        } else {
            return getPageNo();
        }
    }

}
