package com.example.mobile.pnr.activities;
/**
 * Created by pnr on 06/02/2017.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mobile.pnr.Models.User;
import com.example.mobile.pnr.R;
import com.example.mobile.pnr.containers.DatabaseHelper;
import com.example.mobile.pnr.validations.InputValidation;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
 
    private final AppCompatActivity activity = UpdateProfileActivity.this;
 
    private NestedScrollView nestedScrollView;
 
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
 
    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextMobile;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private  LinearLayout layout_password;
 
    private AppCompatButton appCompatButtonUpdate;
    private AppCompatTextView appCompatTextViewLoginLink,text_change_password;
 
    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;
    private int onClick_password=1;
 String emailFromIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        emailFromIntent=getIntent().getStringExtra("emailFromIntent");
//        getSupportActionBar().hide();
 
        initViews();
        initListeners();
        initObjects();
    }
 
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
 
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);

        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
 
        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextMobile = (TextInputEditText) findViewById(R.id.textInputEditTextMobile);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        layout_password = (LinearLayout) findViewById(R.id.layout_password);

        appCompatButtonUpdate = (AppCompatButton) findViewById(R.id.appCompatButtonUpdate);
 
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        text_change_password = (AppCompatTextView) findViewById(R.id.text_change_password);

    }
 
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonUpdate.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
        text_change_password.setOnClickListener(this);

    }
 
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        databaseHelper = new DatabaseHelper(activity);
        user = new User();
 
    }
 
 
    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
 
            case R.id.appCompatButtonUpdate:
                postDataToSQLite();
                break;
 
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
            case R.id.text_change_password:
                if(onClick_password==1){
                    onClick_password=0;
                    layout_password.setVisibility(View.VISIBLE);
                }else {
                    onClick_password=1;
                    layout_password.setVisibility(View.GONE);
                    textInputEditTextPassword.setText(null);
                    textInputEditTextConfirmPassword.setText(null);
                }
                break;
        }
    }
 
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if(onClick_password==0){
            if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
                return;
            }
            if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                    textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
                return;
            }
            if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
                return;
            }

        }

//        if (!databaseHelper.checkUser(textInputEditTextMobile.getText().toString().trim())) {
 
            user.setName(textInputEditTextName.getText().toString().trim());
            if(!textInputEditTextMobile.getText().toString().equalsIgnoreCase("")){
                user.setMobile(textInputEditTextMobile.getText().toString().trim());
            }
            if(!textInputEditTextPassword.getText().toString().equalsIgnoreCase("")){
                user.setPassword(textInputEditTextPassword.getText().toString().trim());
            }
            user.setEmail(emailFromIntent);
            databaseHelper.updateUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
 
 
       /* } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }*/
 
 
    }
 
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextMobile.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}