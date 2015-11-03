package mpt.is416.com.teamup;

import java.net.HttpURLConnection;
import android.net.Uri;
import java.net.URL;

/**
 * Created by User on 27/10/2015.
 */
public class HTTPUtil {

    public HttpURLConnection getConnection(URL url){
        HttpURLConnection urlConnection = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();

        }catch(Exception e){
            e.printStackTrace();
        }
        return urlConnection;
    }

    public void disconnect(HttpURLConnection urlConnection){
        if(urlConnection != null){
            urlConnection.disconnect();
        }
    }

    public URL buildURL(String baseURL, String[] keys, String[] values){
        URL url = null;
        Uri.Builder URLBuilder = Uri.parse(baseURL).buildUpon();
        if(keys!=null){
            for(int i=0; i<keys.length; i++){
                URLBuilder.appendQueryParameter(keys[i], values[i]);
            }
        }

        Uri builtUri = URLBuilder.build();

        try{
            url = new URL(builtUri.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        return url;
    }
}
