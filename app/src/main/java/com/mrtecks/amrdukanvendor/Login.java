package com.mrtecks.amrdukanvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrtecks.amrdukanvendor.loginPOJO.loginBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.editText);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.button);
        progress = findViewById(R.id.progressBar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String u = username.getText().toString();
                String p = password.getText().toString();

                if (u.length() > 0) {
                    if (p.length() > 0) {

                        progress.setVisibility(View.VISIBLE);


                        Bean b = (Bean) getApplicationContext();

                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.level(HttpLoggingInterceptor.Level.HEADERS);
                        logging.level(HttpLoggingInterceptor.Level.BODY);

                        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .client(client)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Log.d("asdas", SharePreferenceUtils.getInstance().getString("token"));

                        Call<loginBean> call = cr.login(u, p, SharePreferenceUtils.getInstance().getString("token"));

                        call.enqueue(new Callback<loginBean>() {
                            @Override


                            public void onResponse(Call<loginBean> call, Response<loginBean> response) {


                                if (response.body().getStatus().equals("1")) {

                                    SharePreferenceUtils.getInstance().saveString("id", response.body().getData().getId());
                                    SharePreferenceUtils.getInstance().saveString("name", response.body().getData().getName());
                                    SharePreferenceUtils.getInstance().saveString("username", response.body().getData().getUsername());
                                    SharePreferenceUtils.getInstance().saveString("type", response.body().getData().getTyp());

                                    if (response.body().getData().getTyp().equals("admin")) {
                                        Intent i = new Intent(Login.this, MainActivity.class);
                                        startActivity(i);
                                        finishAffinity();
                                    } else {
                                        /*Intent i = new Intent(Login.this, MainActivity2.class);
                                        startActivity(i);
                                        finishAffinity();*/
                                    }


                                }

                                Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<loginBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        Toast.makeText(Login.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}