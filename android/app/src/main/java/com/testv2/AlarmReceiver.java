package com.testv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.provider.Settings.Secure;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "Tarea programada ejecutada...");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentDate = dateFormat.format(new Date());
        String text = "app";
        String apiUrl = "https://test--apiv2--hvyxp7fk8yms.code.run";
        // String requestBody = "{\"text\": \"postman\", \"date\": \"2023-05-10 17:40\"}";
        String requestBody = "{\"text\":\"" + text + "\",\"date\":\"" + currentDate + "\"}";

        new PostRequestTask().execute(apiUrl, requestBody);
    }

    private static class PostRequestTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String apiUrl = params[0];
            String requestBody = params[1];

            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            try {
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Enviar el cuerpo de la solicitud POST
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(requestBody);
                outputStream.flush();

                return connection.getResponseCode();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            Log.d("AlarmReceiver", "Código de respuesta: " + responseCode);
        }
    }
}
