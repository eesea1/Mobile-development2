package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText get_city;
    String city;
    Button button;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.out_deg);
        get_city = findViewById(R.id.get_city);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        get_city.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    get_weather(v);
                    return false;
                }
                return false;
            }
        });

    }

    public void loadImageFromAsset(String icon) {
        try {
            // получаем входной поток
            InputStream ims = getAssets().open(icon + ".png");
            // загружаем как Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // выводим картинку в ImageView
            imageView.setImageDrawable(d);
            imageView.setVisibility(View.VISIBLE);
        }
        catch(IOException ex) {
            return;
        }
    }

    public void clear(View c){
        get_city.setText("");
    }


    public void get_weather(View v){
        city=get_city.getText().toString().replace(" ", "");
        String userAPI = "c341e34f9b7c327502cde34aa7817c5f";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&units=metric&lang=ru&appid=" + userAPI;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = (JSONObject) response.get("main");
                    String s = main.getString("temp");
                    int i = (int)Float.parseFloat(s.trim());
                    s = String.valueOf(i);
                    tv.setText(s + "°C");

                    JSONArray coord = (JSONArray) response.get("weather");
                    JSONObject sec = (JSONObject) coord.get(0);
                    String icon = sec.getString("icon");
                    loadImageFromAsset(icon);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Ошибка!",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Ошибка!",Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
}
