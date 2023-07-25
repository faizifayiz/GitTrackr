package com.example.gittrackr;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "repositories")
public class Repository {
    @PrimaryKey(autoGenerate = true)

    private int id;

    private String owner;
    private String repoName;
    private String description;
    private String url;

    // Constructor
    public Repository(String owner, String repoName, String description, String url) {
        this.owner = owner;
        this.repoName = repoName;
        this.description = description;
        this.url = url;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
