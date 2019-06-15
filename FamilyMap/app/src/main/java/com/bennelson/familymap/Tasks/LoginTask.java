package com.bennelson.familymap.Tasks;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;

import com.bennelson.familymap.Activities.MainActivity;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.HTTPClient;
import com.bennelson.familymap.Fragments.LoginFragment;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.concurrent.ExecutionException;

/**
 *
 * Created by BenNelson on 11/29/16.
 */
public class LoginTask extends AsyncTask<Void,Void,Boolean> {

    private LoginFragment loginFragment;
    private MainActivity mainActivity;


    public LoginTask(LoginFragment loginF, MainActivity ma)
    {
        loginFragment = loginF;
        mainActivity = ma;
    }


    @Override
    protected Boolean doInBackground(Void... params)
    {
        //CONNECT TO THE SERVER
        HTTPClient httpclient = new HTTPClient();

        return httpclient.post(); // Return the response
    }

    @Override
    protected void onPostExecute(Boolean response) //Param here is the result from doInbackground
    {
        //If response is true, that means we have a valid user.
        if(response)
        {
            //Loads in the data of all the people associated with the user
            new LoadUserFamilyTask().execute();
            //Loads in the data of all the evenets associated with the user
            new LoadUserEventsTask().execute();
            //Loads and displays the user's name as a toast.
            DisplayWelcomeToUser();
            //Switch to maps
            mainActivity.switchToMapFragment();


        }
        else
        {
            loginFragment.loginFailToast();
        }

    }

    private void DisplayWelcomeToUser() {
        try {
            new WelcomeUserTask(loginFragment).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
