package com.bikesharing.app.home;

public interface HomeFragment {

    String FRAGMENT_TAG = "HOME_FRAGMENT";

    int FRAGMENT_TYPE_DOCK_LIST = 0;
    int FRAGMENT_TYPE_SETTINGS = 1;
    int FRAGMENT_TYPE_DOCK_DETAILS = 2;
    int FRAGMENT_TYPE_TRAVEL = 3;

    boolean allowBackPressed();
    int getFragmentType();
}
