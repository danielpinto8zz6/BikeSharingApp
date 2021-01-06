package com.bikesharing.app.data.Page;

public class Pageable {
    Sort SortObject;
    private float pageNumber;
    private float pageSize;
    private float offset;
    private boolean paged;
    private boolean unpaged;


    // Getter Methods

    public Sort getSort() {
        return SortObject;
    }

    public float getPageNumber() {
        return pageNumber;
    }

    public float getPageSize() {
        return pageSize;
    }

    public float getOffset() {
        return offset;
    }

    public boolean getPaged() {
        return paged;
    }

    public boolean getUnpaged() {
        return unpaged;
    }

    // Setter Methods

    public void setSort(Sort sortObject) {
        this.SortObject = sortObject;
    }

    public void setPageNumber(float pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(float pageSize) {
        this.pageSize = pageSize;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }
}
