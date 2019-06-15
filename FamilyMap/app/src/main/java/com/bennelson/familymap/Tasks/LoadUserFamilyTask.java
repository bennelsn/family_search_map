package com.bennelson.familymap.Tasks;

import android.os.AsyncTask;

import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.HTTPClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by BenNelson on 11/29/16.
 */
public class LoadUserFamilyTask extends AsyncTask<Void,Void,String>
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

        String givenURL = FamilyMapTools.getInstance().buildURLPerson(host,port);

        return httpclient.get(givenURL, token); // Return the response
    }

    @Override
    protected void onPostExecute(String data)
    {
        try {
            JSONObject famData = new JSONObject(data);
            FamilyMapModel.getInstance().setFamilyData(famData);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
