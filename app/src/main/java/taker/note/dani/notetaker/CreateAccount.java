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

public class CreateAccount extends AppCompatActivity {
    final String URL = "http://172.20.10.8:12345/";
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.newPassword);

        Button signUpButton = findViewById(R.id.signUp);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG_SUCESS", "MESSAGE");
                signUp();

//                if (res[0].equals("true")) {
//                    error.setText(res[1]);
//                } else {
//                   success.setText("New user created");
//                }
            }
        });
    }

    private void signUp() {
        Log.d("TAG_SUCESS2", "MESSAGE");
        final TextView successText = findViewById(R.id.signupError);
        final TextView errorText = findViewById(R.id.signupError);
        final String[] result = new String[2];
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("method", "createAccount");
            jsonBody.put("extra", "Just extra");
            jsonBody.put("password", "david");
            jsonBody.put("first_name", firstName.getText().toString());
            jsonBody.put("last_name", lastName.getText().toString());
            jsonBody.put("email", "betdavid123@gmail.com");
            Log.d("TAG_SUCESS4", "MESSAGE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TAG_SUCESS3", "MESSAGE");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        result[0] = "true";
                        Log.d("TAG_SUCESS123", response.toString());
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

                            if (retMap.get("message").toString().equals("User account already exists")) {
                                Toast.makeText(CreateAccount.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateAccount.this, SignInActivity.class));
                            } else {
                                errorText.setText(retMap.get("message").toString());
                            }

                        } catch (UnsupportedEncodingException errorr) {

                        }
                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);

    }
}
