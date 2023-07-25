package com.example.gittrackr;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RepositoryDao {
    @Insert
    void insert(Repository repository);

    @Delete
    void delete(Repository repository);

    @Query("SELECT * FROM repositories")
    List<Repository> getAllRepositories();

    @Query("DELETE FROM repositories")
    void deleteAll();
}

