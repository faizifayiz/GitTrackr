package com.example.gittrackr;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RepositoryAdapter.OnItemClickListener     {
    private RecyclerView recyclerView;
    private RepositoryAdapter adapter;
    private List<Repository> repositoryList = new ArrayList<>();
    private AppDatabase appDatabase;

    private RepositoryDao repositoryDao;

    private TextView emptyTextView;
    private Button addRepoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);
        addRepoButton = findViewById(R.id.addRepoButton);

        // Initialize the Room database and RepositoryDao
        appDatabase = AppDatabase.getInstance(this);
        repositoryDao = appDatabase.repositoryDao();

        // Set up the RecyclerView and Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RepositoryAdapter(repositoryList, this, this);
        recyclerView.setAdapter(adapter);

        // Load data from the Room database
        new LoadRepositoriesAsyncTask().execute();

        // Set up click listener for the "Add Repo" button
        addRepoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRepoActivityy.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Repository repository = repositoryList.get(position);
        String repoUrl = repository.getUrl();
        // Open the URL in the browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repoUrl));
        startActivity(intent);
    }

    // AsyncTask to load repositories from the Room database
    private class LoadRepositoriesAsyncTask extends AsyncTask<Void, Void, List<Repository>> {
        @Override
        protected List<Repository> doInBackground(Void... voids) {
            return appDatabase.repositoryDao().getAllRepositories();
        }

        @Override
        protected void onPostExecute(List<Repository> repositories) {
            super.onPostExecute(repositories);
            repositoryList.clear();
            repositoryList.addAll(repositories);
            adapter.notifyDataSetChanged();
            updateEmptyView();
        }
    }

    // Helper method to update the empty view visibility
    private void updateEmptyView() {
        if (repositoryList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            addRepoButton.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
            addRepoButton.setVisibility(View.GONE);
        }
    }

    // Inflate options menu with "Clear All" action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle options menu item clicks
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all) {
            // Clear all repositories from the database and update the UI
            new ClearRepositoriesAsyncTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // AsyncTask to clear all repositories from the Room database
    private class ClearRepositoriesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.repositoryDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            repositoryList.clear();
            adapter.notifyDataSetChanged();
            updateEmptyView();
        }
    }
}
