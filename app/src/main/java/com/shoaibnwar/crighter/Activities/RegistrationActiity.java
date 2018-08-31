package com.shoaibnwar.crighter.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class RegistrationActiity extends AppCompatActivity {

    TextView tv_back_to_login;
    Button bt_register;
    Spinner sp_relation;
    RadioGroup rg_auth;
    EditText et_auth_phone;
    EditText et_auth_email;
    EditText et_auth_fullname;
    RadioGroup user_rg;
    EditText et_user_password_again;
    EditText et_user_password;
    EditText et_user_phone;
    EditText et_user_email;
    EditText et_user_fullname;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_actiity);

        init();
        backtoLoginClickHandler();
        backtoLoginClickHandler();
        startAuthUserMobileWithOnlyNumber92();
        startMobileWithOnlyNumber92();
    }

    private void init()
    {
        tv_back_to_login = (TextView) findViewById(R.id.tv_back_to_login);
        bt_register = (Button) findViewById(R.id.bt_register);
        sp_relation = (Spinner) findViewById(R.id.sp_relation);
        rg_auth = (RadioGroup) findViewById(R.id.rg_auth);
        et_auth_phone = (EditText) findViewById(R.id.et_auth_phone);
        et_auth_email = (EditText) findViewById(R.id.et_auth_email);
        et_auth_fullname = (EditText) findViewById(R.id.et_auth_fullname);
        et_user_password_again = (EditText) findViewById(R.id.et_user_password_again);
        et_user_password = (EditText) findViewById(R.id.et_user_password);
        et_user_phone = (EditText) findViewById(R.id.et_user_phone);
        et_user_email = (EditText) findViewById(R.id.et_user_email);
        et_user_fullname = (EditText) findViewById(R.id.et_user_fullname);
        user_rg = (RadioGroup) findViewById(R.id.user_rg);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        registerClickHandler();

    }
    private void backtoLoginClickHandler()
    {
        tv_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActiity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void registerClickHandler()
    {

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userFullName = et_user_fullname.getText().toString();
                String userPhone = et_user_phone.getText().toString();
                String userEmail = et_user_email.getText().toString();
                int radioButtonID = user_rg.getCheckedRadioButtonId();
                String authFullname = et_auth_fullname.getText().toString();
                String authPhone = et_auth_phone.getText().toString();
                String authEmail = et_auth_email.getText().toString();
                int radioButtonIDAuth = rg_auth.getCheckedRadioButtonId();
                String password = et_user_password.getText().toString();
                String againPassword = et_user_password_again.getText().toString();
                int itemPositionForRelation = sp_relation.getSelectedItemPosition();
                String realation = sp_relation.getSelectedItem().toString();

                final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                if(userFullName.length()==0){
                    et_user_fullname.setError("Should not be empty");
                    et_user_fullname.setFocusable(true);
                    et_user_fullname.setAnimation(animShake);
                }
                else if (userEmail.length()==0){
                    et_user_email.setError("Should not be empty");
                    et_user_email.setFocusable(true);
                    et_user_email.setAnimation(animShake);
                }
                else if (!emailValidator(userEmail)){
                    et_user_email.setError("invalid email");
                    et_user_email.setFocusable(true);
                    et_user_email.setAnimation(animShake);
                }
                else if (userPhone.length()==0){
                    et_user_phone.setError("Should not be empty");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
                }
                else if (userPhone.length()!=11){
                    et_user_phone.setError("invalid phone");
                    et_user_phone.setFocusable(true);
                    et_user_phone.setAnimation(animShake);
                }
                else if (password.length()==0){
                    et_user_password.setError("Should not be empty");
                    et_user_password.setFocusable(true);
                    et_user_password.setAnimation(animShake);
                }
                else if (password.length()<5){
                    et_user_password.setError("Length should more than 5 cherecters");
                    Toast.makeText(RegistrationActiity.this, "more then 5 cherecters allow", Toast.LENGTH_SHORT).show();
                    et_user_password.setFocusable(true);
                    et_user_password.setAnimation(animShake);
                }
                else if (againPassword.length()==0){
                    et_user_password_again.setError("Should not be empty");
                    et_user_password_again.setFocusable(true);
                    et_user_password_again.setAnimation(animShake);
                }
                else if (!password.equals(againPassword)){
                    Toast.makeText(RegistrationActiity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }

                else if (radioButtonID==-1){
                    user_rg.setAnimation(animShake);
                    Toast.makeText(RegistrationActiity.this, "Please Select Gendar", Toast.LENGTH_SHORT).show();
                }
                else if (authFullname.length()==0){
                    et_auth_fullname.setError("Should not be empty");
                    et_auth_fullname.setFocusable(true);
                    et_auth_fullname.setAnimation(animShake);
                }
                else if (authEmail.length()==0){
                    et_auth_email.setError("Should not be empty");
                    et_auth_email.setFocusable(true);
                    et_auth_email.setAnimation(animShake);
                }
                else if (!emailValidator(authEmail)){
                    et_auth_email.setError("invalid email");
                    et_auth_email.setFocusable(true);
                    et_auth_email.setAnimation(animShake);
                }
                else if (authPhone.length()==0){
                    et_auth_phone.setError("should not be empty");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }
                else if (authPhone.length()!=11){
                    et_auth_phone.setError("inalid phone");
                    et_auth_phone.setFocusable(true);
                    et_auth_phone.setAnimation(animShake);
                }

                else if (radioButtonIDAuth==-1){
                    user_rg.setAnimation(animShake);
                    Toast.makeText(RegistrationActiity.this, "Please select gendar", Toast.LENGTH_SHORT).show();
                }
                else if (itemPositionForRelation == 0){
                    Toast.makeText(RegistrationActiity.this, "Please Select Relation", Toast.LENGTH_SHORT).show();
                }
                else {

                    RadioButton radioButton = (RadioButton) user_rg.findViewById(radioButtonID);
                    String userGender = radioButton.getText().toString();
                    RadioButton radioButtonAuth = (RadioButton) rg_auth.findViewById(radioButtonIDAuth);
                    String userGenderAuth = radioButton.getText().toString();

                    Log.e("TAG", "here is detail to send user userFullName " + userFullName);
                    Log.e("TAG", "here is detail to send user userPhone " + userPhone);
                    Log.e("TAG", "here is detail to send user userPhone " + userPhone);
                    Log.e("TAG", "here is detail to send user userGender " + userGender);
                    Log.e("TAG", "here is detail to send user password " + password);
                    Log.e("TAG", "here is detail to send user authFullname " + authFullname);
                    Log.e("TAG", "here is detail to send user authPhone " + authPhone);
                    Log.e("TAG", "here is detail to send user authEmail " + authEmail);
                    Log.e("TAG", "here is detail to send user userGender " + userGender);
                    Log.e("TAG", "here is detail to send user userGenderAuth " + userGenderAuth);
                    Log.e("TAG", "here is detail to send user realation" + realation);

                    userPhone = userPhone.substring(1);
                    userPhone = "+92"+userPhone;

                    authPhone = authPhone.substring(1);
                    authPhone = "+92"+authPhone;

                        registrationService(userFullName, userEmail, userPhone, userGender, authFullname, authEmail, authPhone, userGenderAuth, realation, password);

                }

            }
        });

    }

    public void startAuthUserMobileWithOnlyNumber92()
    {

        et_user_phone.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_user_phone.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorSignInEmail.setText(x);
                        et_user_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    et_user_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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

    public void startMobileWithOnlyNumber92()
    {

        et_auth_phone.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(RegistrationActiity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_auth_phone.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorSignInEmail.setText(x);
                        et_auth_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    et_auth_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
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

    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    //logining user serverice
    private void registrationService(final String userfullName, final String userEmail, final String userPhone, final String userGendar,
                                     final String authfullName, final String authEmail, final String authPhone, final String authGendar, final String relation, final String password){

        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressbar.setVisibility(View.VISIBLE);


        StringRequest strReq = new StringRequest(Request.Method.POST, APIURLS.REGISTRATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login Response: " + response.toString());
                //hideDialog();
                progressbar.setVisibility(View.GONE);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String mesage = jObj.getString("msg");
                        if (mesage.equals("Register Successfully")) {
                            Toast.makeText(RegistrationActiity.this, "You have register successfully Please login to use your account", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActiity.this, LoginActivity.class));
                            finish();
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
                params.put("userfullname", userfullName);
                params.put("userphone", userPhone);
                params.put("useremail", userEmail);
                params.put("usergender", userGendar);
                params.put("userudid", "asdfasdf3asdf32sadf3");
                params.put("userpassword", password);
                params.put("authfullname", authfullName);
                params.put("authphone", authPhone);
                params.put("authemail", authEmail);
                params.put("authgender", authGendar);
                params.put("realtionwithauth", relation);
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
}
