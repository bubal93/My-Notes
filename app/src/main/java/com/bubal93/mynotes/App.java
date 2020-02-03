package com.bubal93.mynotes;

import android.app.Application;

import androidx.room.Room;

import com.bubal93.mynotes.data.AppDatabase;
import com.bubal93.mynotes.data.NoteDao;

public class App extends Application {

    private AppDatabase database;
    private NoteDao noteDao;

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        //TODO: move it to background thread
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "app-db-name")
                .allowMainThreadQueries()
                .build();

        noteDao = database.noteDao();
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public void setNoteDao(NoteDao noteDao) {
        this.noteDao = noteDao;
    }
}
