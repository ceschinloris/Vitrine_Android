package com.ceschin.loris.vitrine.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ceschin.loris.vitrine.R;
import com.ceschin.loris.vitrine.model.User;
import com.ceschin.loris.vitrine.service.RequestQueueSingleton;
import com.ceschin.loris.vitrine.utils.JWTUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.ceschin.loris.vitrine.user.validators.isEmailValid;
import static com.ceschin.loris.vitrine.user.validators.isPasswordValid;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private String mToken;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        // Check if there is already a valid authentication token
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if(token != null) {
            validateToken(token);
        }


    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            /*
            mAuthTask = new UserLoginTask(this, email, password);
            mAuthTask.execute((Void) null);
            */
            loginTask(email, password);
        }
    }

    private void signUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void goToMainActivity() {

        User user = JWTUtils.getUser(mToken);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token", mToken);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void loginTask (final String email, final String password) {

        String url ="http://10.0.2.2:3000/login";

        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("NETWORK", "Analysing the network response");

                        try {

                            if(response.getBoolean("success")){
                                mToken = response.getString("token");

                                // Save the token to skip future logins
                                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_name), MODE_PRIVATE).edit();
                                editor.putString("token", mToken);
                                editor.apply();

                                goToMainActivity();
                            }
                            else {
                                String error = response.getString("message");
                                mEmailView.setError(error);
                                showProgress(false);
                            }
                        }
                        catch (JSONException e)
                        {
                            mEmailView.setError(e.toString());
                            Log.e("JSON ERROR", e.toString());
                            showProgress(false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("NETWORK ERROR", error.toString());
                    }
                })
        {
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };

        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void validateToken (final String token) {

        String url ="http://10.0.2.2:3000/validateToken";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    // Token is valid
                    @Override
                    public void onResponse(String response) {
                        mToken = token;
                        goToMainActivity();
                    }
                },
                new Response.ErrorListener() {

                    // Token is not valid
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOGIN", "Error validating token :\n " + error.toString());
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("authorization", "bearer " + token);

                return headers;
            }
        };

        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}

