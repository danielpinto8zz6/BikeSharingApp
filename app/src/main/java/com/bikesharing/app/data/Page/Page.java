package com.bikesharing.app.data.Page;

import java.util.ArrayList;

public class Page<T> {

    ArrayList<T> content = new ArrayList <T> ();
    Pageable PageableObject;
    private boolean last;
    private float totalPages;
    private float totalElements;
    Sort SortObject;
    private float numberOfElements;
    private boolean first;
    private float size;
    private float number;
    private boolean empty;

    public ArrayList<T> getContent() {
        return content;
    }

    public void setContent(ArrayList<T> content) {
        this.content = content;
    }

    // Getter Methods

    public Pageable getPageable() {
        return PageableObject;
    }

    public boolean getLast() {
        return last;
    }

    public float getTotalPages() {
        return totalPages;
    }

    public float getTotalElements() {
        return totalElements;
    }

    public Sort getSort() {
        return SortObject;
    }

    public float getNumberOfElements() {
        return numberOfElements;
    }

    public boolean getFirst() {
        return first;
    }

    public float getSize() {
        return size;
    }

    public float getNumber() {
        return number;
    }

    public boolean getEmpty() {
        return empty;
    }

    // Setter Methods

    public void setPageable(Pageable pageableObject) {
        this.PageableObject = pageableObject;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public void setTotalPages(float totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalElements(float totalElements) {
        this.totalElements = totalElements;
    }

    public void setSort(Sort sortObject) {
        this.SortObject = sortObject;
    }

    public void setNumberOfElements(float numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
