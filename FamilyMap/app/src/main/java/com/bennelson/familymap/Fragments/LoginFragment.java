package com.bennelson.familymap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bennelson.familymap.Activities.MainActivity;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Tasks.LoginTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Created by BenNelson on 11/25/16.
 */
public class LoginFragment extends Fragment
{
    private EditText userEdit;
    private EditText passwordEdit;
    private EditText hostEdit;
    private EditText portEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.login_frag, container, false);
        userEdit = (EditText) view.findViewById(R.id.username);
        passwordEdit = (EditText) view.findViewById(R.id.password);
        hostEdit = (EditText) view.findViewById(R.id.host);
        portEdit = (EditText) view.findViewById(R.id.port);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        if(FamilyMapModel.getInstance().isResync()) {
            FamilyMapModel.getInstance().setResync(false);
            loginButton.callOnClick();
        }

        return view;
    }

    private void onLogin()
    {
        FamilyMapModel FMM = FamilyMapModel.getInstance();

        FMM.setUsername(userEdit.getText().toString());
        FMM.setPassword(passwordEdit.getText().toString());
        FMM.setHost(hostEdit.getText().toString());
        FMM.setPort(portEdit.getText().toString());

        MainActivity ma = (MainActivity) getActivity();
        new LoginTask(this,ma).execute();

    }

    public void loginFailToast()
    {
        Context context = getActivity();
        CharSequence text = "Login Failed.\nSomething must be wrong.";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void loginSuccessToast(String result)
    {
        try {
            JSONObject personInfo = new JSONObject(result);

            String firstName = personInfo.getString("firstName");
            String lastName = personInfo.getString("lastName");

            StringBuilder sb = new StringBuilder("Welcome\n");
            sb.append(firstName);
            sb.append(" ");
            sb.append(lastName);

            String displayText = sb.toString();

            Context context = getActivity();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, displayText, duration);
            toast.show();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
