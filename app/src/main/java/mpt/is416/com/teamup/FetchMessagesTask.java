package mpt.is416.com.teamup;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by FengXin on 16/10/2015.
 */
public class FetchMessagesTask extends AsyncTask<String,Integer,String> {
    Context c;
    MessageListAdapter msgListAdapter;
    String regId, cid;
    Boolean resume;
    Date currentTime;
    Long currentTimeInLong;
    Long lastRetrieveTimeInLong;
    String oldMsgs;
    FetchMessagesTask(Context c, MessageListAdapter msgListAdapter, String regId, Boolean resume){
        this.c = c;
        this.msgListAdapter = msgListAdapter;
        this.regId = regId;
        this.resume = resume;
        currentTime = new Date();
        currentTimeInLong = currentTime.getTime();

    }
    private final String LOG_TAG = FetchMessagesTask.class.getSimpleName();
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        oldMsgs = PreferenceManager.getDefaultSharedPreferences(c).getString(params[0], "noOldMsgs");

        // Will contain the raw JSON response as a string.
        String messageJsonStr = null;
        String resultsJsonStr = null;
        HTTPUtil util = new HTTPUtil();
        try {
            final String MESSAGE_BASE_URL = "http://teamup-jhgoh.rhcloud.com/messageManager.php?";
            final String CHAT_ID = "cid";
            final String LAST_RETRIEVE_TIME = "lastRetrieveTime";
            final String METHOD = "method";
            lastRetrieveTimeInLong = PreferenceManager.getDefaultSharedPreferences(c).getLong(params[0] + "latestRetrieveTime", 0);
            if(lastRetrieveTimeInLong == 0){
                lastRetrieveTimeInLong = currentTimeInLong;
            }
            Uri builtUri = Uri.parse(MESSAGE_BASE_URL).buildUpon()
                    .appendQueryParameter(METHOD,"getLatestMessagesByCid")
                    .appendQueryParameter(CHAT_ID,params[0])
                    .appendQueryParameter(LAST_RETRIEVE_TIME, lastRetrieveTimeInLong.toString())
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
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            messageJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e("PlaceholderFragment", "Error ", e);
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
            PreferenceManager.getDefaultSharedPreferences(c).edit().putLong(params[0] + "latestRetrieveTime", currentTimeInLong).commit();
        }
        //append the new messages to the old messages
        try{
            if(!oldMsgs.equals("noOldMsgs")){
                resultsJsonStr = appendLatestMsgJsonString(oldMsgs, messageJsonStr);
            }else{
                resultsJsonStr = messageJsonStr;

            }
            PreferenceManager.getDefaultSharedPreferences(c).edit().putString(params[0], resultsJsonStr).commit();
        }catch(JSONException e){
            e.printStackTrace();
        }

        return resultsJsonStr;


    }
    //TO-DO: read the allMessagesFromJSONString and call addMessage
    @Override
    protected void onPostExecute(String messageJSONString){
        try{
            if(messageJSONString != null){
                loadMessagesFromJSONString(messageJSONString);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

    }
    //TO-DO: return allMessagePairs in loadMessagesFromJSONString
    private void loadMessagesFromJSONString(String jsonString) throws JSONException{
            JSONArray allMessages = new JSONArray(jsonString);

            if(jsonString != null) {
                msgListAdapter.clearArrayList();
                for (int i = 0; i < allMessages.length(); i++) {
                    JSONObject eachMessage = allMessages.getJSONObject(i);
                    String sid = eachMessage.getString("sid");
                    String cid = eachMessage.getString("cid");
                    String senderName = eachMessage.getString("dname");
                    String msg = eachMessage.getString("message");
                    //TO-DO get the sender name of the message, modify the Message object accordingly
                    Timestamp sendTime = prepareTimestampFromJSONObject(eachMessage.getString("TIMESTAMP"));

                    if (sid.equals(regId)) {
                        Message message = new Message(sid, cid, null, sendTime, msg);
                        msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_OUTGOING);

                    } else {
                        Message message = new Message(sid, cid, senderName, sendTime, msg);
                        msgListAdapter.addMessage(message, msgListAdapter.DIRECTION_INCOMING);
                    }
                    msgListAdapter.notifyDataSetChanged();
                }
            }
    }

    public static Timestamp prepareTimestampFromJSONObject(String timestampString){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = dateFormat.parse(timestampString);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        }catch(Exception e){//this generic but you can control another types of exception
            e.printStackTrace();
        }
        return null;
    }

    private String appendLatestMsgJsonString (String oldMsgJsonStr, String newMsgJsonStr) throws JSONException{
        String allMsgs;
        JSONArray newMessages = new JSONArray(newMsgJsonStr);
        JSONArray oldMessages = new JSONArray(oldMsgJsonStr);

        if(newMessages != null){
            for(int i=0; i<newMessages.length(); i++){
                JSONObject eachNewMsg = newMessages.getJSONObject(i);
                oldMessages.put(eachNewMsg);
            }
        }

        allMsgs = oldMessages.toString();

        return allMsgs;
    }

}
