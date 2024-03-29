package com.mrtecks.amrdukanvendor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrtecks.amrdukanvendor.ordersPOJO.Datum;
import com.mrtecks.amrdukanvendor.ordersPOJO.ordersBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class Orders41 extends Fragment {

    ProgressBar progress;
    RecyclerView grid;
    OrdersAdapter adapter;
    GridLayoutManager manager;

    List<Datum> list;

    TextView date;
    String dd;

    TextView ordersamount, cashcollected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_orders2, container, false);

        list = new ArrayList<>();

        progress = view.findViewById(R.id.progressBar3);
        grid = view.findViewById(R.id.grid);
        date = view.findViewById(R.id.date);
        cashcollected = view.findViewById(R.id.textView18);
        ordersamount = view.findViewById(R.id.textView17);


        adapter = new OrdersAdapter(list, getActivity());

        manager = new GridLayoutManager(getActivity(), 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.date_dialog);
                dialog.show();


                final DatePicker picker = dialog.findViewById(R.id.date);
                Button ok = dialog.findViewById(R.id.ok);

                long now = System.currentTimeMillis() - 1000;
                picker.setMaxDate(now);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(calendar.getTime());

                        dialog.dismiss();

                        date.setText("Date - " + strDate + " (click to change)");

                        dd = strDate;

                        loadCart(dd);

                    }
                });


            }
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Log.d("dddd", formattedDate);

        date.setText("Date - " + formattedDate + " (click to change)");

        dd = formattedDate;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadCart(dd);

    }

    void loadCart(String dd) {
        progress.setVisibility(View.VISIBLE);


        Bean b = (Bean) getActivity().getApplicationContext();

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


        Call<ordersBean> call = cr.getFoodCompleted2(dd, SharePreferenceUtils.getInstance().getString("id"));
        call.enqueue(new Callback<ordersBean>() {
            @Override
            public void onResponse(Call<ordersBean> call, Response<ordersBean> response) {

                if (response.body().getData().size() > 0) {


                    adapter.setgrid(response.body().getData());

                } else {
                    adapter.setgrid(response.body().getData());
                    /*getActivity().navigation.setSelectedItemId(R.id.action_home);
                    Toast.makeText(mainActivity, "No order found", Toast.LENGTH_SHORT).show();
*/
                }

                cashcollected.setText("" + response.body().getDelivered());
                ordersamount.setText("₹ " + response.body().getCollect());

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ordersBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        OrdersAdapter(List<Datum> list, Context context) {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.order_list_item2, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);


            viewHolder.txn.setText("#" + item.getTxn());
            viewHolder.date.setText(item.getCreated());
            viewHolder.status.setText(item.getStatus());
            viewHolder.name.setText(item.getName());
            viewHolder.address.setText(item.getAddress());
            viewHolder.pay.setText(item.getPay_mode());
            viewHolder.slot.setText(item.getSlot());
            viewHolder.phone.setText(item.getPhone());
            viewHolder.amount.setText("\u20B9 " + item.getAmount());

            viewHolder.deldate.setText(item.getDelivery_date());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , OrderDetails3.class);
                    intent.putExtra("oid" , item.getId());
                    intent.putExtra("status" , item.getStatus());
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txn, date, status, name, address, amount, pay, slot, deldate, phone;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                txn = itemView.findViewById(R.id.textView27);
                date = itemView.findViewById(R.id.textView28);
                status = itemView.findViewById(R.id.textView35);
                name = itemView.findViewById(R.id.textView32);
                address = itemView.findViewById(R.id.textView34);
                amount = itemView.findViewById(R.id.textView30);
                pay = itemView.findViewById(R.id.textView40);
                slot = itemView.findViewById(R.id.textView62);
                deldate = itemView.findViewById(R.id.textView42);
                phone = itemView.findViewById(R.id.textView5);


            }
        }
    }

}
