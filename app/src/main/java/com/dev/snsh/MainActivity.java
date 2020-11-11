package com.dev.snsh;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dev.snsh.model.ConfigItem;
import com.dev.snsh.model.ConfigModel;
import com.dev.snsh.model.PetItem;
import com.dev.snsh.model.PetModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private TextView tvClinicList;
    private TextView tvPetList;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPet;
    private ClinicAdapter clinicAdapter;
    private ArrayList<ConfigItem> configItems = new ArrayList();
    private PetAdapter petAdapter;
    private ArrayList<PetItem> petItems = new ArrayList();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvClinicList = (TextView) findViewById(R.id.tvClinicList);
        tvPetList = (TextView) findViewById(R.id.tvPetList);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewPet = (RecyclerView) findViewById(R.id.recyclerViewPet);
        recyclerViewPet.setHasFixedSize(true);
        recyclerViewPet.setLayoutManager(new LinearLayoutManager(this));
        new JsonTask1(this).execute("https://raw.githubusercontent.com/nanglao/imageLoadMoreVIew/master/config.json");

    }

    public static boolean checkIsOpeningHour(ConfigModel obj, Integer position) {
        String openFrom = obj.getConfig().get(position).getOpenHourFrom();

        DateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dateFrom = null;
        try {
            dateFrom = sdf.parse(openFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateFrom.getHours(); // int
        dateFrom.getMinutes(); // int

        String openTo = obj.getConfig().get(position).getOpenHourTo();

        Date dateTo = null;
        try {
            dateTo = sdf.parse(openTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTo.getHours(); // int
        dateTo.getMinutes(); // int

        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());

        Date dateNow = null;
        try {
            dateNow = sdf.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateNow.getHours(); // int
        dateNow.getMinutes(); // int

        if (!dateFrom.after(dateNow) && !dateTo.before(dateNow)) {
            return true;//open
        } else {
            return false;//close
        }
    }

    private class JsonTask1 extends AsyncTask<Object, String, AsyncTaskResult<JSONObject>> {
        private Context mContext;

        public JsonTask1(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected AsyncTaskResult<JSONObject> doInBackground(Object... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0].toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

//                return buffer.toString();
                JSONObject jsonObject = new JSONObject(buffer.toString());
                return new AsyncTaskResult<JSONObject>(jsonObject);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new AsyncTaskResult<JSONObject>(e);
            } catch (IOException e) {
                return new AsyncTaskResult<JSONObject>(e);
            } catch (Exception exception) {
                return new AsyncTaskResult<JSONObject>(exception);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<JSONObject> result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (result.getError() != null) {
                showDialog(mContext, result.getError().getLocalizedMessage());
            } else {
                tvClinicList.setVisibility(View.VISIBLE);

                Gson gson = new Gson();
                ConfigModel obj = gson.fromJson(result.getResult().toString(), ConfigModel.class);

                configItems = new ArrayList();
                configItems.addAll(obj.getConfig());
                clinicAdapter = new ClinicAdapter(configItems, new MyRecyclerViewRowClickListener2() {

                    @Override
                    public void onViewClicked(View v, int position) {
                        String message = "";
                        if (checkIsOpeningHour(obj, position)) {
                            message = "Thank you for getting in touch with us. We’ll get back to you as soon as possible";
                        } else {
                            message = "Work hours has ended. Please contact us again on the next work day";
                        }
                        showDialog(v.getContext(), message);
                    }

                    @Override
                    public void onRowClicked(int position) {

                    }
                },obj);
                recyclerView.setAdapter(clinicAdapter);
                new JsonTask2(mContext).execute("https://raw.githubusercontent.com/nanglao/imageLoadMoreVIew/master/pet.json");
            }
        }
    }


    private void showDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("Alert")
                .setMessage(message)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                })
                .show();
    }

    private class JsonTask2 extends AsyncTask<Object, String, AsyncTaskResult<JSONObject>> {

        private Context mContext;

        public JsonTask2(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected AsyncTaskResult<JSONObject> doInBackground(Object... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0].toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                JSONObject jsonObject = new JSONObject(buffer.toString());
                return new AsyncTaskResult<JSONObject>(jsonObject);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception exception) {
                return new AsyncTaskResult<JSONObject>(exception);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(AsyncTaskResult<JSONObject> result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            if (result.getError() != null) {
                showDialog(mContext, result.getError().getLocalizedMessage());
            } else {
                tvPetList.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                PetModel obj = gson.fromJson(result.getResult().toString(), PetModel.class);

                petItems = new ArrayList();
                petItems.addAll(obj.getPet());
                petAdapter = new PetAdapter(petItems, new MyRecyclerViewRowClickListener2() {

                    @Override
                    public void onViewClicked(View v, int position) {
                    }

                    @Override
                    public void onRowClicked(int position) {

                        Intent intent = new Intent(getBaseContext(), PetDetailActivity.class);
                        intent.putExtra("url", petItems.get(position).getImage());
                        startActivity(intent);
                    }
                });
                recyclerViewPet.setAdapter(petAdapter);
            }
        }
    }

}