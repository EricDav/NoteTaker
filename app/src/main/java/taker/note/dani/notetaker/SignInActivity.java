package taker.note.dani.notetaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    final String URL = "http://172.20.10.8:12345/";
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final TextView error = findViewById(R.id.signInError);

        Button signInButton = findViewById(R.id.signInButton);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        Button createUserButton = findViewById(R.id.newUserButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }

    private String[] signIn() {
        final String[] result = new String[2];
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        Log.d("EMAOL", email.getText().toString());
        Log.d("PASSWORD", password.getText().toString());
        try {
            jsonBody.put("method", "authenticate");
            jsonBody.put("password", password.getText().toString());
            jsonBody.put("time_span", "10");
            jsonBody.put("time_unit", "SECONDS");
            jsonBody.put("email", email.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("SAMPLE", response.get("token").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("TAG_NAME",error.toString());
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            Map<String, Object> retMap = new Gson().fromJson(
                                    responseBody, new TypeToken<HashMap<String, Object>>() {}.getType()
                            );

                            Log.d("FOOL", retMap.get("message").toString());

                        } catch (UnsupportedEncodingException errorr) {

                        }
                    }
                });

            // Access the RequestQueue through your singleton class.
            queue.add(jsonObjectRequest);

            return result;
    }
}
