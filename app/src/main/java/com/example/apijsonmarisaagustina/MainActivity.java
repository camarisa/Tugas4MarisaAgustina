package com.example.apijsonmarisaagustina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ArrayList<unitActivity> UnitActivitys = new ArrayList();
    protected final String urlAPI = "https://ewinsutriandi.github.io/mockapi/operating_system.json";
    JSONObject UnitActivity;
    ListView listView;
    FloatingActionButton btnRefresh;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(view -> getDataOperatingSistem());
        loadingIndicator = findViewById(R.id.loading);
        getDataOperatingSistem();
    }

    void setupListview() {
        ListView listView = findViewById(R.id.listView);
        unitAdapter adapter = new unitAdapter(this, UnitActivitys);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClick);
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           unitActivity fSELECTED = UnitActivitys.get(position);
            Toast.makeText(MainActivity.this, fSELECTED.getWeb(), Toast.LENGTH_LONG).show();
            toLink(fSELECTED);
        }
    };


    void getDataOperatingSistem() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlAPI, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UnitActivity = response;
                        refresView();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("erorr", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    void refresView() {
        Iterator<String> key = UnitActivity.keys();
        while(key.hasNext()) {
            String nameunitAcitiy = key.next();
            try {
                JSONObject data = UnitActivity.getJSONObject(nameunitAcitiy);
                String lates_version = data.getString("latest_version");
                String description = data.getString("description");
                String logo_url= data.getString("logo_url");
                String developer= data.getString( "developer");
                String website= data.getString("website");
                String source_model= data.getString("source_model");

                UnitActivitys.add(new unitActivity(nameunitAcitiy,description, lates_version, logo_url, developer, website, source_model));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setupListview();
    }

    void toLink(unitActivity data) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(data.getWeb()));
        startActivity(intent);
    }
}