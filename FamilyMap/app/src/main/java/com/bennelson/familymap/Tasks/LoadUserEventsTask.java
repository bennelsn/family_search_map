package com.bennelson.familymap.Tasks;

import android.os.AsyncTask;

import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.HTTPClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BenNelson on 12/1/16.
 */
public class LoadUserEventsTask extends AsyncTask<Void,Void,String>
{

    @Override
    protected String doInBackground(Void... params)
    {
        //CONNECT TO THE SERVER
        HTTPClient httpclient = new HTTPClient();
        FamilyMapModel FMM = FamilyMapModel.getInstance();

        String host = FMM.getHost();
        String port = FMM.getPort();
        String token = FMM.getCurrentAToken();
        String givenURL = FamilyMapTools.getInstance().buildURLEvent(host,port);

        return httpclient.get(givenURL, token); // Return the response
    }

    @Override
    protected void onPostExecute(String data)
    {
        try {
            JSONObject eventsData = new JSONObject(data);
            FamilyMapModel.getInstance().setEventsData(eventsData);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
