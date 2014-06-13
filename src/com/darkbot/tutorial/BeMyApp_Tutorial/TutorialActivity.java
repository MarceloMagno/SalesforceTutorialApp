package com.darkbot.tutorial.BeMyApp_Tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;
import com.salesforce.androidsdk.ui.sfnative.SalesforceActivity;
import com.salesforce.androidsdk.util.EventsObservable;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabled on 6/11/2014.
 */
public class TutorialActivity extends SalesforceActivity {
    private RestClient client;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Setup view
        setContentView(R.layout.main);
        resultText = (TextView) findViewById(R.id.data_display);
        resultText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onResume() {
        // Hide everything until we are logged in
        findViewById(R.id.root).setVisibility(View.INVISIBLE);


        super.onResume();
    }

    @Override
    public void onResume(RestClient client) {
        // Keeping reference to rest client
        this.client = client;

        // Show everything
        findViewById(R.id.root).setVisibility(View.VISIBLE);
    }

    private void sendRequest(RestRequest restRequest) {
        client.sendAsync(restRequest, new RestClient.AsyncRequestCallback() {

            @Override
            public void onSuccess(RestRequest request, RestResponse result) {
                try {

                    //Do something with JSON result.
                    println(result);  //Use our helper function, to print out our JSON response.

                } catch (Exception e) {
                    e.printStackTrace();
                }

                EventsObservable.get().notifyEvent(EventsObservable.EventType.RenditionComplete);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                EventsObservable.get().notifyEvent(EventsObservable.EventType.RenditionComplete);
            }
        });
    }

    public void onFetchClick(View view) {
        RestRequest feedRequest = generateRequest("GET", "chatter/feeds/news/me/feed-items", null);
        sendRequest(feedRequest);
    }


    /**
     * @param httpMethod
     * @param resource
     * @param jsonPayloadString
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private RestRequest generateRequest(String httpMethod, String resource, String jsonPayloadString) {
        RestRequest request = null;

        if (jsonPayloadString == null) {
            jsonPayloadString = "";
        }
        String url = String.format("/services/data/%s/" + resource, getString(R.string.api_version)); // The IDE might highlight this line as having an error. This is a bug, the code will compile just fine.
        try {
            HttpEntity paramsEntity = getParamsEntity(jsonPayloadString);
            RestRequest.RestMethod method = RestRequest.RestMethod.valueOf(httpMethod.toUpperCase());
            request = new RestRequest(method, url, paramsEntity);
            return request;
        } catch (UnsupportedEncodingException e) {
            Log.e("ERROR", "Could not build request");
            e.printStackTrace();
        }
        return request;
    }


    /**
     * @param requestParamsText
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    private HttpEntity getParamsEntity(String requestParamsText)
            throws UnsupportedEncodingException {
        Map<String, Object> params = parseFieldMap(requestParamsText);
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            paramsList.add(new BasicNameValuePair(param.getKey(),
                    (String) param.getValue()));
        }
        return new UrlEncodedFormEntity(paramsList);
    }

    /**
     * Helper to read json string representing field name-value map
     *
     * @param jsonText
     * @return
     */
    private Map<String, Object> parseFieldMap(String jsonText) {
        String fieldsString = jsonText;
        if (fieldsString.length() == 0) {
            return null;
        }

        try {
            JSONObject fieldsJson = new JSONObject(fieldsString);
            Map<String, Object> fields = new HashMap<String, Object>();
            JSONArray names = fieldsJson.names();
            for (int i = 0; i < names.length(); i++) {
                String name = (String) names.get(i);
                fields.put(name, fieldsJson.get(name));
            }
            return fields;

        } catch (Exception e) {
            Log.e("ERROR", "Could not build request");
            e.printStackTrace();
            return null;
        }
    }



/**
 * Helper method to print object in the result_text field
 *
 * @param object
 */

    private void println(Object object) {
        if (resultText == null)
            return;

        StringBuffer sb = new StringBuffer(resultText.getText());
        String text;
        if (object == null) {
            text = "null";
        } else {
            text = object.toString();
        }
        sb.append(text).append("\n");
        resultText.setText(sb);
    }


/**
 * Dump info about app and rest client
 *//*

    private void printInfo() {
        printHeader("Info");
        println(SalesforceSDKManager.getInstance());
        println(client);
    }

*/

}