package com.shoaibnwar.crighter.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidhiddencamera.HiddenCameraUtils;
import com.shoaibnwar.crighter.Preferences.SPref;
import com.shoaibnwar.crighter.R;
import com.shoaibnwar.crighter.URLS.APIURLS;
import com.shoaibnwar.crighter.VolleyLibraryFiles.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView tv_sign_up;
    EditText  et_email, et_password;
    TextView tv_forgot_passowrd;
    Button bt_login;
    ProgressBar progressbar;
    private final int ACCESS_FINE_LOCATION = 11;
    private final int CAMERA_PER = 12;
    private final int RECORD_AUDIO_PER = 13;
    private final int STORAGE_PERMISSION = 14;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String userID = SPref.getStringPref(sharedPreferences, SPref.USER_ID);
        String username = SPref.getStringPref(sharedPreferences, SPref.AUTH_FULLNAME);

        Log.e("TAG", "the current boolean is " + username);
        if (!userID.isEmpty()){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        init();
        signupClickHandler();
        loginClickHandler();
        forgotPassword();
        startMobileWithOnlyNumber92(et_email);
        //startService(new Intent(getApplicationContext(), LockService.class));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            cameraPermission();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            audioRocordingPermission();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermission();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            storagePermission();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
            } else {
                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {
            //TODO Ask your parent activity for providing runtime permission
        }
    }

    private void init()
    {
        tv_sign_up = (TextView) findViewById(R.id.tv_sign_up);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_passowrd = (TextView) findViewById(R.id.tv_forgot_passowrd);
        bt_login = (Button) findViewById(R.id.bt_login);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

    }

    private void signupClickHandler()
    {
        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActiity.class));
                finish();
            }
        });
    }

    private void loginClickHandler()
    {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (email.length()==0){
                    et_email.setError("Please Enter Email");
                }
                else if(email.length()>3){
                   if (email.startsWith("03") || email.startsWith("+92")){
                       if (email.length()!=11){
                           et_email.setError("Invalid Mobile Number");
                       }
                       else if (password.length()<5){
                           et_password.setError("should be more than 5 cherecters");
                       }
                       else {
                           //calling login server here
                           email = email.substring(1);
                           email = "+92"+email;
                           Log.e("TAG", "the text from field is " + email);
                           Log.e("TAG", "the password is " + password);

                           loginingUserService(email, password);
                       }
                   }
                   else {
                       if (!emailValidator(email)){
                           et_email.setError("Invalid Email");
                       }
                       else if (password.length()<5){
                           et_password.setError("should be more than 5 cherecters");
                       }
                       else {
                           //calling login server here
                           Log.e("TAG", "the text from field is " + email);
                           Log.e("TAG", "the password is " + password);

                           loginingUserService(email, password);
                       }
                   }
                }
            }
        });
    }

    private void forgotPassword()
    {
        tv_forgot_passowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //caliing forgot password screen

                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.forgot_password_mobile_email_dialog);
                final EditText etForgotPass = (EditText) dialog.findViewById(R.id.et_forgot_pass_email);
                Button btForgotPassSubmit = (Button) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
                startMobileWithOnlyNumber92(etForgotPass);
                btForgotPassSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String textFromEt = etForgotPass.getText().toString();
                        if (textFromEt.startsWith("03")){
                            if (textFromEt.length()<10){
                                Toast.makeText(LoginActivity.this, "Please enter valid number", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (textFromEt.startsWith("03")){
                                    textFromEt = textFromEt.substring(1);
                                    textFromEt = "+92"+textFromEt;
                                }
                                dialog.dismiss();
                                Log.e("TAG", "the use text is " + textFromEt);
                                forgotPassword(textFromEt);
                            }

                        }
                        else if (!textFromEt.startsWith("03")){
                            if (!emailValidator(textFromEt)){
                                Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }else {
                                dialog.dismiss();
                                Log.e("TAG", "the use text is " + textFromEt);
                                forgotPassword(textFromEt);
                            }
                        }
                    }
                });
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                dialog.show();

            }
        });
    }

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    //logining user serverice
    private void loginingUserService(final String usertext, final String userphone){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");

                        String userdata =  jObj.getString("userdata");
                        JSONObject innerObnject = new JSONObject(userdata);
                        String user_id =  innerObnject.getString("user_id");
                        String userfullname =  innerObnject.getString("userfullname");
                        String userphone =  innerObnject.getString("userphone");
                        String useremail =  innerObnject.getString("useremail");
                        String usergender =  innerObnject.getString("usergender");
                        String authfullname =  innerObnject.getString("authfullname");
                        String authphone =  innerObnject.getString("authphone");
                        String authemail =  innerObnject.getString("authemail");
                        String authgender =  innerObnject.getString("authgender");
                        String realtionwithauth =  innerObnject.getString("realtionwithauth");

                        Log.e("TAG", "user_id " + user_id);
                        Log.e("TAG", "user userfullname " + userfullname);
                        Log.e("TAG", "user userphone " + userphone);
                        Log.e("TAG", "user useremail " + useremail);
                        Log.e("TAG", "user usergender " + usergender);
                        Log.e("TAG", "user authfullname " + authfullname);
                        Log.e("TAG", "user authphone " + authphone);
                        Log.e("TAG", "user authemail " + authemail);
                        Log.e("TAG", "user authgender " + authgender);
                        Log.e("TAG", "user realtionwithauth " + realtionwithauth);

                        Toast.makeText(LoginActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                            //adding data to preff
                        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_USER_CRED, 0);
                        SPref.StoreStringPrefAll(sharedPreferences, user_id, userfullname, userphone, useremail, usergender, authfullname, authphone, authemail, authgender, realtionwithauth);


                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        finish();

                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                progressbar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("usercredentials", usertext);
                params.put("userpassword", userphone);
                params.put("userudid", "asdfasdf3asdf32sadf3");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of registration service

    //forgot_password user serverice
    private void forgotPassword(final String usertext){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.FORGOT_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "forgotpassword Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.contains("Verification Code")){
                            final String code = jObj.getString("code");
                            final Dialog dialog = new Dialog(LoginActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.forgot_password_code);
                            final EditText et_verification_code = (EditText) dialog.findViewById(R.id.et_verification_code);
                            final Button bt_submit_for_forgot_pass = (Button) dialog.findViewById(R.id.bt_submit_for_forgot_pass);
                            bt_submit_for_forgot_pass.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String mCode = et_verification_code.getText().toString();

                                    Log.e("Tag", "the code is here " + mCode);

                                    if (mCode.length()==0)
                                    {
                                        et_verification_code.setError("Should not be empty");
                                    }
                                    else if (!mCode.matches(code))
                                    {
                                        et_verification_code.setError("Code Not Match");
                                    }
                                    else {
                                        dialog.dismiss();
                                        dialogSettingNewPassword(usertext);
                                    }
                                }
                            });

                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
                            dialog.show();
                        }

                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                progressbar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("usercredentials", usertext);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of forgotpassword service

    //reset password user serverice
    private void resetPasswored(final String usertext, final String password){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.UPDATE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "update password Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("TAG", "json objec is " + jObj);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        Toast.makeText(LoginActivity.this, "Password reseted please login", Toast.LENGTH_SHORT).show();

                    } else {
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
                progressbar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();

                params.put("usercredentials", usertext);
                params.put("userpassword", password);

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }//end of update password service

    public void startMobileWithOnlyNumber92(final EditText et_email)
    {

        et_email.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(LoginActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_email.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorSignInEmail.setText(x);
                        et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    et_email.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });//end for login editText

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void locationPermission(){
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
        return;
    }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void storagePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void cameraPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PER);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void audioRocordingPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PER);
            return;
        }
    }

    private void dialogSettingNewPassword(final String usertext)
    {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.set_new_password);
        final EditText et_set_password = (EditText) dialog.findViewById(R.id.et_set_password);
        final EditText et_set_password_again = (EditText) dialog.findViewById(R.id.et_set_password_again);
        Button bt_reset_save = (Button) dialog.findViewById(R.id.bt_reset_save);
        bt_reset_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = et_set_password.getText().toString();
                String password2 = et_set_password_again.getText().toString();
                if (password1.length()==0){
                    et_set_password.setError("Should not be empty");
                }
                else if (password2.length()==0){
                    et_set_password_again.setError("Should not be empty");
                }
                else if (!password1.matches(password2)){
                    Toast.makeText(LoginActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                }
                else {

                    dialog.dismiss();
                    resetPasswored(usertext, password1);

                }
            }
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();
    }
}
