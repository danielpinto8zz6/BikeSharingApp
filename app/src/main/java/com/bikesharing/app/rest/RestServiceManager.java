package com.bikesharing.app.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://34.78.122.193/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            myRestServiceManager = new RestServiceManager(retrofit.create(RestService.class));
        }

        return myRestServiceManager;
    }

    public RestService getRestService() {
        return this.myRestService;
    }
}
