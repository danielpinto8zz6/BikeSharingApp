package com.bikesharing.app.home;

public interface HomeFragment {

    String FRAGMENT_TAG = "HOME_FRAGMENT";

    int FRAGMENT_TYPE_DOCK_LIST         = 0;
    int FRAGMENT_TYPE_PAYMENT_HISTORY   = 1;
    int FRAGMENT_TYPE_BIKE_HISTORY      = 2;
    int FRAGMENT_TYPE_SETTINGS          = 3;

    boolean allowBackPressed();
    int getFragmentType();
}
