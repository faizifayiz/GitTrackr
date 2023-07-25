package com.example.gittrackr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private List<Repository> repositoryList;
    private Context context;
    private OnItemClickListener clickListener;

        public RepositoryAdapter(List<Repository> repositoryList, Context context, OnItemClickListener clickListener) {
            this.repositoryList = repositoryList;
            this.context = context; // Initialize the context
            this.clickListener = clickListener;
        }

        public void updateRepositoryList(List<Repository> repositories) {
            repositoryList.clear();
            repositoryList.addAll(repositories);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_repository, parent, false);
            return new RepositoryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
            Repository repository = repositoryList.get(position);
            holder.repoNameTextView.setText(repository.getRepoName());
            holder.descriptionTextView.setText(repository.getDescription());

            // Set click listener for the repository item
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(position);
                    }
                }
            });

            // Set click listener for the "Share" button
            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call the method to handle the share functionality
                    shareRepository(repository);
                }
            });
        }

        private void shareRepository(Repository repository) {
            String repoName = repository.getRepoName();
            String repoUrl = repository.getUrl();

            // Create an Intent to share the repository details
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this GitHub repository!");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Repository: " + repoName + "\nURL: " + repoUrl);

            // Start the Intent chooser
            Intent chooser = Intent.createChooser(shareIntent, "Share via");
            if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(chooser);
            }
        }

        @Override
        public int getItemCount() {
            return repositoryList.size();
        }

        public class RepositoryViewHolder extends RecyclerView.ViewHolder {
            TextView repoNameTextView;
            TextView descriptionTextView;
            Button shareButton;

            public RepositoryViewHolder(@NonNull View itemView) {
                super(itemView);
                repoNameTextView = itemView.findViewById(R.id.repoNameTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                shareButton = itemView.findViewById(R.id.shareButton);
            }
        }

        // Define an interface for item click events
        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
