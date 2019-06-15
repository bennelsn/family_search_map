package com.bennelson.familymap.Utilities;

import com.bennelson.familymap.Model_Classes.FamilyMapModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * Created by BenNelson on 11/29/16.
 */
public class HTTPClient
{

    public Boolean post()
    {
        //Get the right info from the family map model singleton
        FamilyMapModel FMM = FamilyMapModel.getInstance();

        String username = FMM.getUsername();
        String password = FMM.getPassword();

        String postData = FamilyMapTools.getInstance().buildUserPasswordString(username,password);


        try {

            String host = FMM.getHost();
            String port = FMM.getPort();

            String givenURL = FamilyMapTools.getInstance().buildURLUserLogin(host,port);

            URL url = new URL(givenURL);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.connect();

            // Write post data to request body
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(postData.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();

                // TODO: Process response body data
                // handle RESPONSE HERE, if it was successful return a boolean

                //Check the responseBodyData string.
                if(responseBodyData.equals("{\"message\":\"User name or password is wrong\"}"))
                {
                    return false;
                }
                else
                {
                    JSONObject loginData = new JSONObject(responseBodyData);
                    FMM.setPersonId(loginData.getString("personId"));
                    FMM.setCurrentAToken(loginData.getString("Authorization"));
                    return true;
                }


            }
            //else {
                // SERVER RETURNED AN HTTP ERROR
            //}
        }
        catch (IOException e) {
            // IO ERROR
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String get(String givenURL, String token)
    {
        FamilyMapModel FMM = FamilyMapModel.getInstance();

        String username = FMM.getUsername();
        String password = FMM.getPassword();

        String postData = FamilyMapTools.getInstance().buildUserPasswordString(username,password);

        try {

            URL url = new URL(givenURL);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            connection.addRequestProperty("Authorization", token);

            connection.connect();

            // Write post data to request body
            OutputStream requestBody = connection.getOutputStream();
            requestBody.write(postData.getBytes());
            requestBody.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();

                return responseBodyData;

            }

        }
        catch (IOException e) {
            // IO ERROR
            e.printStackTrace();
        }
        return null;
    }


}
