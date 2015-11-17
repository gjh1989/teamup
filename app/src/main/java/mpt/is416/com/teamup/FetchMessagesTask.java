package mpt.is416.com.teamup;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by FengXin on 16/10/2015.
 */
public class FetchMessagesTask extends AsyncTask<String,Integer,String> {
    Context c;
    View rootView;
    MessageListAdapter msgListAdapter;
    String deviceID;
    private ListView messageListView;
    private List<String> messageList;

    FetchMessagesTask(Context c, MessageListAdapter msgListAdapter, String deviceID){
        this.c = c;
        this.msgListAdapter = msgListAdapter;
        this.deviceID = deviceID;
    }
    private final String LOG_TAG = FetchMessagesTask.class.getSimpleName();
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String messageJsonStr = null;

        String format = "json";
        String units = "metric";
        HTTPUtil util = new HTTPUtil();
        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String MESSAGE_BASE_URL = "http://teamup-jhgoh.rhcloud.com/messageManager.php?";
            final String CHAT_ID = "cid";
            final String METHOD = "method";

            Uri builtUri = Uri.parse(MESSAGE_BASE_URL).buildUpon()
                    .appendQueryParameter(METHOD,"getLatestMessagesByCid")
                    .appendQueryParameter(CHAT_ID,params[0])
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = util.getConnection(url);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            messageJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
        } finally{
            if (urlConnection != null) {
                //urlConnection.disconnect();
                util.disconnect(urlConnection);
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return messageJsonStr;
    }
    //TO-DO: read the allMessagesFromJSONString and call addMessage
    @Override
    protected void onPostExecute(String messageJSONString){
        try{
            loadMessagesFromJSONString(messageJSONString);
        }catch(JSONException e){
            e.printStackTrace();
        }

    }
    //TO-DO: return allMessagePairs in loadMessagesFromJSONString
    private void loadMessagesFromJSONString(String jsonString) throws JSONException{
        JSONArray allMessages = new JSONArray(jsonString);

        if(jsonString != null) {
            for (int i = 0; i < allMessages.length(); i++) {
                JSONObject eachMessage = allMessages.getJSONObject(i);
                String sid = eachMessage.getString("sid");
                String cid = eachMessage.getString("cid");
                String msg = eachMessage.getString("message");
                Timestamp sendTime = prepareTimestampFromJSONObject(eachMessage.getString("TIMESTAMP"));
                Message message = new Message(sid, cid, sendTime, msg);
                if (sid == deviceID) {
                    msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_OUTGOING);

                } else {
                    msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_INCOMING);
                }
                msgListAdapter.notifyDataSetChanged();
            }
        }
    }

    private Timestamp prepareTimestampFromJSONObject(String timestampString){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(timestampString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        }catch(Exception e){//this generic but you can control another types of exception
            e.printStackTrace();
        }
        return null;
    }

}