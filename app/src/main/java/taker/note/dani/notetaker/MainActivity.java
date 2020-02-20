package taker.note.dani.notetaker;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue queue = Volley.newRequestQueue(this);
        final TextView txt = findViewById(R.id.test);
        String url = "http://172.20.10.8:12345/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("method", "authenticate");
            jsonBody.put("password", "david1");
            jsonBody.put("time_span", "10");
            jsonBody.put("time_unit", "MINUTES");
            jsonBody.put("email", "bot@graderthan.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        txt.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("TAG_NAME",error.toString());
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            Log.d("TAG_NAME2", responseBody);
                            Map<String, Object> retMap = new Gson().fromJson(
                                    responseBody, new TypeToken<HashMap<String, Object>>() {}.getType()
                            );
                            Log.d("TAG2", retMap.get("message").toString());

                        } catch (UnsupportedEncodingException errorr) {

                        }

                        // retMap.get("message");

                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);
    }
}
