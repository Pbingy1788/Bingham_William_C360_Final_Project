package com.cs360.williambingham.bingham_william_c360_final_project;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class LinkedInn extends AppCompatActivity
{
    //LinkedIn Stuff
    private static final String LINKED_IN_PROFILE = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))&oauth2_access_token=";
    private static final String API_KEY = "783w9z4ybsxbug";
    private static final String SECRET_KEY = "DLINnZSaKYD9pByc";
    private static final String STATE = "111";
    private static final String REDIRECT_URI = "http://www.three19.com/linkedin/auth/callback";
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE_PARAM = "scope";
    private static final String LITE_PROFILE = "r_liteprofile";
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private String TAG = LinkedInn.class.getSimpleName();
    private WebView loginWebView;
    private ProgressDialog progressDialog;
    private String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_inn);
        login();
    }

    // Login Information Logic
    protected void login()
    {
        loginWebView = (WebView) findViewById(R.id.loginWebView);
        loginWebView.requestFocus(View.FOCUS_DOWN);
        loginWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String authorizationUrl, Bitmap favicon) {
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                removeProcessDialog();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl){
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "1");

                    Uri uri = Uri.parse(authorizationUrl);

                    String stateToken = uri.getQueryParameter(STATE_PARAM);

                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);

                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }

                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);

                    new LinkedInn.PostRequestAsyncTask().execute(accessTokenUrl);
                } else {
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    loginWebView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        loginWebView.loadUrl(authUrl);
    }

    //LinkedIn Access token for authorization
    private static String getAccessTokenUrl(String authorizationToken)
    {
        return ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
    }

    // Authorization URL for LinkedIn
    private static String getAuthorizationUrl()
    {
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +STATE_PARAM+EQUALS+STATE
                +AMPERSAND+
                REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND+
                SCOPE_PARAM+EQUALS+LITE_PROFILE;
    }

    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LinkedInn.this, "", "Loading...", true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];
                URL sourceUrl = null;
                try {
                    sourceUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) sourceUrl.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    int responsecode = conn.getResponseCode();

                    Log.d(TAG, "Response Code is :" + responsecode);

                    if (responsecode == 200) {
                        String result = "", line = "";
                        InputStream inputStream = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                        while ((line = br.readLine()) != null) {
                            result = result + line;
                        }

                        JSONObject resultJson = new JSONObject(result);

                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;
                        accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;

                        Log.e("Token", "" + accessToken);

                        if (expiresIn > 0 && accessToken != null) {
                            Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            return true;
                        }
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (ParseException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override

        // Uses Access Token when program executes
        protected void onPostExecute(Boolean status)
        {
            if (status) {
                Log.e(TAG, "Access Token : " + accessToken);
                loginWebView.setVisibility(View.GONE);
                loadProfile();
            } else {
                removeProcessDialog();
            }
        }
    }

    // Loads LinkedIn Profile after execute
    protected void loadProfile()
    {
        String url = LINKED_IN_PROFILE + accessToken;
        Log.e("Profile", url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        removeProcessDialog();
                        displayProfile(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.toString());
                        removeProcessDialog();
                    }
                });

        requestQueue.add(objectRequest);
    }

    // Displays LinkedIn Profile after Profile loads
    protected void displayProfile(JSONObject response)
    {
        try {
            String firstName = response.getJSONObject("firstName").getJSONObject("localized").getString("en_US");
            String lastName = response.getJSONObject("lastName").getJSONObject("localized").getString("en_US");

            JSONArray pictureURL = response.getJSONObject("profilePicture").getJSONObject("displayImage~").getJSONArray("elements");
            JSONArray identifiers = pictureURL.getJSONObject(pictureURL.length() - 1).getJSONArray("identifiers");
            String profilePhotoURL = identifiers.getJSONObject(0).getString("identifier");

            TextView profileTextView = (TextView) findViewById(R.id.profileTextView);
            profileTextView.setText("Name : " + firstName + " " + lastName);

            ImageView profileImageView = (ImageView) findViewById(R.id.profileImageView);

            Picasso.with(getApplicationContext()).load(profilePhotoURL)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(profileImageView);

        } catch (JSONException e) {
            Log.e("Response",  e.toString());
        }
    }

    protected void removeProcessDialog()
    {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
