package com.example.gittrackr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;


public class AddRepoActivityy extends AppCompatActivity {
    private EditText ownerEditText;
    private EditText repoNameEditText;
    private AppDatabase appDatabase;
    private GithubApi githubApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repo_activityy);

        // Initialize the Room database
        appDatabase = AppDatabase.getInstance(this);

        githubApi = GithubApiClient.getGithubApi();

        ownerEditText = findViewById(R.id.ownerEditText);
        repoNameEditText = findViewById(R.id.repoNameEditText);
        Button addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String owner = ownerEditText.getText().toString().trim();
                                             String repoName = repoNameEditText.getText().toString().trim();

                                             // Call the GithubApi to get the repository details
                                             Call<Repository> call = githubApi.getUserRepository(owner, repoName);
                                             call.enqueue(new Callback<Repository>() {
                                                 @Override
                                                 public void onResponse(Call<Repository> call, Response<Repository> response) {
                                                     if (response.isSuccessful() && response.body() != null) {
                                                         Repository repository = response.body();
                                                         // Insert the fetched Repository object into the Room database
                                                         new InsertRepositoryAsyncTask().execute(repository);
                                                     } else {
                                                         Toast.makeText(AddRepoActivityy.this, "failed"+response, Toast.LENGTH_SHORT).show();
                                                     }
                                                 }

                                                 @Override
                                                 public void onFailure(Call<Repository> call, Throwable t) {
                                                     // Show an error message or handle the failure as needed
                                                 }
                                             });
            }
        });
    }

    // AsyncTask to insert the new repository into the Room database
    private class InsertRepositoryAsyncTask extends AsyncTask<Repository, Void, Void> {
        @Override
        protected Void doInBackground(Repository... repositories) {
            appDatabase.repositoryDao().insert(repositories[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Finish the activity and go back to MainActivity or any other screen
            onResume();
        }
    }
}
