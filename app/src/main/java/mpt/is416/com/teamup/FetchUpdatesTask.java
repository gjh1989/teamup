package mpt.is416.com.teamup;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Feng Xin on 16/10/2015.
 * Modified by Elyza on 21/10/2015.
 */
public class FetchUpdatesTask extends AsyncTask<String, Void, String> {
    private final String TAG = FetchUpdatesTask.class.getSimpleName();
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String rawJsonStr = null;

        try {
            // Construct the URL
            final String BASE_URL = "http://teamup-jhgoh.rhcloud.com/";
            final String MANAGER_POSTFIX = ".php";
            final String METHOD = "method";
            // Declare all managers...
            final String CHAT_MANAGER = "chatManager";
            final String MILESTONE_MANAGER = "milestoneManager";
            final String USER_MANAGER = "userManager";
            // Declare all parameters...
            final String USER_ID = "uid";
            final String SENDER_ID = "sid";
            final String DISPLAY_NAME = "dname";
            final String CHAT_ID = "cid";
            final String CHAT_NAME = "cname";
            final String CHAT_IMAGE = "cimage";
            final String SELFDESTRUCT = "cdate";
            final String CHAT_PARTICIPANT = "participants";
            final String TIMESTAMP = "timestamp";
            final String MESSAGE = "message";
            final String MILESTONE_ID = "msid";
            final String MILESTONE_TITLE = "title";
            final String MILESTONE_DESCRIPTION = "desc";
            final String MILESTONE_WEEK = "week";
            final String MILESTONE_DATETIME = "datetime";
            final String MILESTONE_LOCATION = "location";
            final String MILESTONE_CREATED_BY = "createdby";
            final String MILESTONE_LAST_MODIFIED_BY = "lastmodifiedby";

            Uri builtUri;
            Uri.Builder builder;
            // decide the uri to build
            switch (params[0]) {
                case "createChat":
                    builder = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(CHAT_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(CHAT_NAME, params[1]);
                    if(!params[2].isEmpty()){
                        builder.appendQueryParameter(CHAT_IMAGE, params[2]);
                    }
                    if(!params[3].isEmpty()){
                        builder.appendQueryParameter(CHAT_PARTICIPANT, params[3]);
                    }
                    if(!params[4].isEmpty()){
                        builder.appendQueryParameter(SELFDESTRUCT, params[4]);
                    }
                    builtUri = builder.build();
                    break;
                case "getChatsByUid":
                    builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(CHAT_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(USER_ID, params[1])
                            .build();
                    break;
                case "getMilestoneByCid":
                    builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(MILESTONE_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(CHAT_ID, params[1])
                            .build();
                    break;
                case "getMilestoneByUid":
                    builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(MILESTONE_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(USER_ID, params[1])
                            .build();
                    break;
                case "insertMilestone":
                    builder = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(MILESTONE_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(CHAT_ID, params[1])
                            .appendQueryParameter(MILESTONE_TITLE, params[2])
                            .appendQueryParameter(MILESTONE_WEEK, params[4])
                            .appendQueryParameter(MILESTONE_CREATED_BY, params[7]);
                    if (!params[3].isEmpty())
                        builder.appendQueryParameter(MILESTONE_DESCRIPTION, params[3]);
                    if (!params[5].isEmpty())
                        builder.appendQueryParameter(MILESTONE_DATETIME, params[5]);
                    if (!params[6].isEmpty())
                        builder.appendQueryParameter(MILESTONE_LOCATION, params[6]);
                    builtUri = builder.build();
                    break;
                case "insertUser":
                    builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(USER_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(USER_ID, params[1])
                            .appendQueryParameter(SENDER_ID, params[2])
                            .build();
                    break;
                case "updateUser":
                    builtUri = Uri.parse(BASE_URL).buildUpon()
                            .appendPath(USER_MANAGER + MANAGER_POSTFIX)
                            .appendQueryParameter(METHOD, params[0])
                            .appendQueryParameter(USER_ID, params[1])
                            .appendQueryParameter(DISPLAY_NAME, params[2])
                            .build();
                    break;
                default:
                    Log.e(TAG, "method not defined in FetchUpdatesTask");
                    // for now just initialize builtUri
                    builtUri = Uri.parse(BASE_URL).buildUpon().build();
                    break;
            }
            Log.i(TAG, builtUri.toString());
            URL url = new URL(builtUri.toString());

            // Create the request to Webservice, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                Log.i(TAG, "nothing to do");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                Log.i(TAG, "string was empty");
                return null;
            }
            rawJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return rawJsonStr;
    }

    @Override
    protected void onPostExecute(String results) {
        if (delegate != null) {
            delegate.processFinish(results);
        }
        Log.i(TAG + "PostExecute", results);
    }

}
