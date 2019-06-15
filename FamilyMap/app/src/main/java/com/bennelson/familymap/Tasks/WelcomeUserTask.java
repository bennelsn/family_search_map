package com.bennelson.familymap.Tasks;

import android.os.AsyncTask;

import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.HTTPClient;
import com.bennelson.familymap.Fragments.LoginFragment;

/**
 * Created by BenNelson on 11/29/16.
 */
public class WelcomeUserTask extends AsyncTask<Void,Void,String>
{
    private LoginFragment loginFragment;

    public WelcomeUserTask(LoginFragment loginF)
    {
        loginFragment = loginF;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        //CONNECT TO THE SERVER
        HTTPClient httpclient = new HTTPClient();
        FamilyMapModel FMM = FamilyMapModel.getInstance();

        String host = FMM.getHost();
        String port = FMM.getPort();
        String token = FMM.getCurrentAToken();
        String personID = FMM.getPersonId();
        String givenURL = FamilyMapTools.getInstance().buildURLPersonId(host,port,personID);

        return httpclient.get(givenURL, token); // Return the response
    }

    @Override
    protected void onPostExecute(String result)
    {
        loginFragment.loginSuccessToast(result);

    }
}
