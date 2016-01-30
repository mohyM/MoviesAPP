package com.example.movies;

import java.util.List;

/**
 * Created by لا اله الا الله on 27/01/2016.
 */
public interface AsyncResponse {
    void processFinish(List<String> output);
    void processFinishTrailers(List<String> output);

}