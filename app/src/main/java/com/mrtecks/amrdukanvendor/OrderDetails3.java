package com.mrtecks.amrdukanvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrtecks.amrdukanvendor.driverPOJO.driverBean;
import com.mrtecks.amrdukanvendor.orderDetailsPOJO.Datum;
import com.mrtecks.amrdukanvendor.orderDetailsPOJO.orderDetailsBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderDetails3 extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    List<Datum> list;
    CategoryAdapter adapter;
    String oid, status;
    Button assign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details3);

        oid = getIntent().getStringExtra("oid");
        status = getIntent().getStringExtra("status");

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar2);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar2);
        assign = findViewById(R.id.button3);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Order Details");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        adapter = new CategoryAdapter(this, list);
        GridLayoutManager manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        if (status.equals("pending")) {
            assign.setVisibility(View.VISIBLE);
        } else {
            assign.setVisibility(View.GONE);
        }

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(OrderDetails3.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.assign_dialog);
                dialog.show();

                Spinner spinner = dialog.findViewById(R.id.spinner);
                Button ok = dialog.findViewById(R.id.button5);
                ProgressBar bar = dialog.findViewById(R.id.progressBar4);

                List<String> did = new ArrayList<>();
                List<String> dname = new ArrayList<>();
                final String[] drid = new String[1];

                bar.setVisibility(View.VISIBLE);


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

                Call<driverBean> call = cr.getDrivers(SharePreferenceUtils.getInstance().getString("id"));

                call.enqueue(new Callback<driverBean>() {
                    @Override
                    public void onResponse(Call<driverBean> call, Response<driverBean> response) {

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            did.add(response.body().getData().get(i).getId());
                            dname.add(response.body().getData().get(i).getFullName());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OrderDetails3.this, android.R.layout.simple_list_item_1, dname);
                        spinner.setAdapter(dataAdapter);


                        bar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<driverBean> call, Throwable t) {
                        bar.setVisibility(View.GONE);
                    }
                });

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        drid[0] = did.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Call<driverBean> call2 = cr.assign_order(drid[0], oid);
                        call2.enqueue(new Callback<driverBean>() {
                            @Override
                            public void onResponse(Call<driverBean> call, Response<driverBean> response) {
                                bar.setVisibility(View.GONE);
                                Toast.makeText(OrderDetails3.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                dialog.dismiss();

                                finish();
                            }

                            @Override
                            public void onFailure(Call<driverBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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

        Call<orderDetailsBean> call = cr.getFoodOrderDetails(oid);
        call.enqueue(new Callback<orderDetailsBean>() {
            @Override
            public void onResponse(Call<orderDetailsBean> call, Response<orderDetailsBean> response) {


                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getData());
                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<orderDetailsBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public CategoryAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model5, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.quantity.setText("Quantity - " + item.getQuantity());
            holder.title.setText(item.getName());
            holder.price.setText("Price - ₹ " + item.getPrice());

            if (item.getRequest().length() > 0) {
                holder.request.setText("(Special Request: " + item.getRequest() + " )");
            }

            if (item.getAddon().length() > 0) {
                holder.addon.setText("Addon: " + item.getAddon());
                holder.addon.setVisibility(View.VISIBLE);
            } else {
                holder.addon.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView quantity, title, price, addon, request;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView5);
                title = itemView.findViewById(R.id.textView17);
                quantity = itemView.findViewById(R.id.textView18);
                price = itemView.findViewById(R.id.textView19);
                addon = itemView.findViewById(R.id.textView81);
                request = itemView.findViewById(R.id.textView82);

            }
        }
    }

}