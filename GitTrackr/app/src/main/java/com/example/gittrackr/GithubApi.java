package com.example.gittrackr;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApi {
    @GET("repos/{owner}/{repo}")
    Call<Repository> getUserRepository(
            @Path("owner") String owner,
            @Path("repo") String repoName
    );
}