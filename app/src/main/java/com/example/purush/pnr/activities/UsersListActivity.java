package com.example.purush.pnr.activities;
/**
 * Created by pnr on 06/02/2017.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.purush.pnr.Models.AddValue;
import com.example.purush.pnr.Models.User;
import com.example.purush.pnr.R;
import com.example.purush.pnr.adapters.UsersRecyclerAdapter;
import com.example.purush.pnr.containers.DbHelper;
import com.example.purush.pnr.utilities.Networkutils;
import com.example.purush.pnr.validations.ApiClient;
import com.example.purush.pnr.validations.ApiInterface;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersListActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
 
    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    String encodedImage,user_address;
    String mobile_number;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss ", Locale.getDefault());
    private List<User> listUsers;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private UsersRecyclerAdapter usersRecyclerAdapter;
    private DbHelper databaseHelper;
    RelativeLayout accounts_layout;
    String emailFromIntent;
    RatingBar ratingBar;
    Toolbar toolbar;
    int visibility=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initViews();
        new CaptureImage().execute();

    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        accounts_layout=(RelativeLayout)findViewById(R.id.accounts_layout);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Purushotham Reddy");
        accounts_layout.setOnClickListener(this);
        ratingBar.setOnTouchListener(this);
        if (toolbar != null) {
            toolbar.setTitle("Purushotham Reddy");
            setSupportActionBar(toolbar);
        }
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }*/
    }
 
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        usersRecyclerAdapter = new UsersRecyclerAdapter(listUsers);
 
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(usersRecyclerAdapter);
        databaseHelper = new DbHelper(activity);
 
        emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        getDataFromSQLite();
    }
 
    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());
 
                return null;
            }
 
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usersRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accounts_layout:
                if(visibility==1){
                    recyclerViewUsers.setVisibility(View.VISIBLE);
                    initObjects();
                    visibility=0;
                }else {
                    recyclerViewUsers.setVisibility(View.GONE);
                    visibility=1;
                }

                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        menu.findItem(R.id.edit_profile).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit_profile:
                Intent intent=new Intent(UsersListActivity.this,UpdateProfileActivity.class);
                intent.putExtra("emailFromIntent",emailFromIntent);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.ratingBar:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = ratingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    ratingBar.setRating(stars);
                    v.setPressed(false);
                }else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                break;
        }
        return true;
    }
    private void add_Country(String countryValue) {
        if (new Networkutils(this).isConnectingToInternet()) {
           ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<AddValue> call = apiService.addCountry(countryValue,"",1);
            call.enqueue(new Callback<AddValue>() {
                @Override
                public void onResponse(Call<AddValue> call, Response<AddValue> response) {
                    try {
                        if (response.code() == 200) {
                            Log.i("response", response.body().toString());
                            AddValue AddValue = response.body();

                            if(AddValue.getStatus().equalsIgnoreCase("1")) {
                                Toast.makeText(UsersListActivity.this, AddValue.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UsersListActivity.this, AddValue.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            //Global.showToast(mContext, "out of 200");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getMessage();
                        Log.d("excepiton: ", e.getMessage());

//                        Global.showToast(mContext, getString(R.string.unable_to_reach));
                    }
                }

                @Override
                public void onFailure(Call<AddValue> call, Throwable t) {
                    Log.e("Error", t.toString());
//                    Global.showToast(mContext, getString(R.string.unable_to_reach));
                }


            });
        } else {
            finish();
        }
    }

    protected class CaptureImage extends AsyncTask<Void, Void, String> {


        private String SOAP_ACTION = "http://tempuri.org/pnr";

        private String NAMESPACE = "http://tempuri.org/";//namespace
        private String METHOD_NAME = "ImageCapture";    //method name
        private String URL = "http://192.168.1.11:8080/purush/pnr/HttpHandler/ImageUploader.asmx";
        StringBuffer result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewName.setText("Processing");
        }

        protected String doInBackground(Void... params) {

            try {
                // Capturing Image Uploading
             Calendar c = Calendar.getInstance();
                String strDate = sdf.format(c.getTime());
                SoapObject requestvalue = new SoapObject(NAMESPACE, METHOD_NAME);
                requestvalue.addProperty("userDetails", encodedImage);
                requestvalue.addProperty("packet", "$UserPackets," + strDate + "," + mobile_number + "," + user_address);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(requestvalue);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 40000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive result1 = (SoapPrimitive) envelope.getResponse();
                result = new StringBuffer(result1.toString());
                if (androidHttpTransport != null) {
                    androidHttpTransport.reset();
                    try {
                        androidHttpTransport.getServiceConnection().disconnect();
                    } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return String.valueOf(result);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.contains("null")) {
                textViewName.setText("Try Again");
            } else if (s.contains("Exeption")) {
                textViewName.setText("Retry");
            } else {
                textViewName.setText("" + s);
            }
        }
    }
}