package com.bubal93.mynotes.screens.main;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bubal93.mynotes.ApiService;
import com.bubal93.mynotes.App;
import com.bubal93.mynotes.R;
import com.bubal93.mynotes.model.Note;
import com.bubal93.mynotes.screens.details.NoteDetailsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {

    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDetailsActivity.start(MainActivity.this, null);
            }
        });

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getNoteLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setItems(notes);
            }
        });

        try {
            ApiService.getNotes(this, this, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            List<Note> serverNotes = new ArrayList<>(response.length());
            for (int i = 0; i < response.length(); i++) {
                JSONObject item = response.getJSONObject(i);
                int id = Integer.parseInt(item.getString("id"));
                String text = item.getString("title");
                Note note = new Note(id, text);
                serverNotes.add(note);

                if (App.getInstance().getNoteDao().findById(id) != null) {
                    App.getInstance().getNoteDao().update(note);
                } else {
                    App.getInstance().getNoteDao().insert(note);
                }
            }

            adapter.setItems(serverNotes);
            Toast.makeText(this, R.string.notes_fetched, Toast.LENGTH_SHORT).show();

        } catch (JSONException error) {
            Toast.makeText(this, R.string.server_down, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, R.string.server_down, Toast.LENGTH_SHORT).show();
    }
}
