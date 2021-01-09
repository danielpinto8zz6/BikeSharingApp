package com.bikesharing.app.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestServiceManager {

    private static RestServiceManager myRestServiceManager = null;

    private RestService myRestService;

    private RestServiceManager(RestService myRestService) {
        this.myRestService = myRestService;
    }

    public static RestServiceManager getInstance() {

        if (myRestServiceManager == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://20.74.10.21:80")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            myRestServiceManager = new RestServiceManager(retrofit.create(RestService.class));
        }

        return myRestServiceManager;
    }

    public RestService getRestService() {
        return this.myRestService;
    }
}
