package com.bubal93.mynotes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bubal93.mynotes.model.Note;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ApiService {

    private static final String baseUrl = "http://private-9aad-note10.apiary-mock.com";

    public static void getNotes(final Context context, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) throws JSONException {
        String requestUrl = baseUrl + "/notes";
        JsonArrayRequest request = new JsonArrayRequest(requestUrl, listener, errorListener);
        Volley.newRequestQueue(context).add(request);
    }

    public static void postNote(Note note, final Context context) throws JSONException {
        String noteId = Integer.toString(note.getUid());
        String requestUrl = baseUrl + "/notes";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", noteId);
        jsonBody.put("title", note.getText());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonBody,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static void updateNote(Note note, final Context context) throws JSONException {
        String noteId = Integer.toString(note.getUid());
        String requestUrl = baseUrl + "/notes" + noteId;

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id", noteId);
        jsonBody.put("title", note.getText());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, requestUrl, jsonBody,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(context).add(request);
    }

    public static void deleteNote(Note note, final Context context) throws JSONException {
        String noteId = Integer.toString(note.getUid());
        String requestUrl = baseUrl + "/notes" + noteId;

        StringRequest request = new StringRequest(Request.Method.DELETE, requestUrl,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        Volley.newRequestQueue(context).add(request);
    }
}

