package com.bikesharing.app.data.Page;

public class Sort {
    private boolean sorted;
    private boolean unsorted;
    private boolean empty;


    // Getter Methods

    public boolean getSorted() {
        return sorted;
    }

    public boolean getUnsorted() {
        return unsorted;
    }

    public boolean getEmpty() {
        return empty;
    }

    // Setter Methods

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
