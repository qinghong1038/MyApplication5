package com.example.pei.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        txtV = (TextView) findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ServletPostAsyncTask().execute();
            }
        });
    }
    class ServletPostAsyncTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("https://backend-project-1378.appspot.com/hello");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

               /* // Build name data request params
                Map<String, String> nameValuePairs = new HashMap<>();
                nameValuePairs.put("name", name);
                String postParams = buildPostDataString(nameValuePairs);*/

                // Execute HTTP Post
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
              //  writer.write(postParams);
                writer.flush();
                writer.close();
                outputStream.close();
                urlConnection.connect();

                // Read response
                int responseCode = urlConnection.getResponseCode();
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }
                reader.close();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    return response.toString();
                }
                return "Error: " + responseCode + " " + urlConnection.getResponseMessage();

            } catch (IOException e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  Log.d("Ketqua",s);
            txtV.setText(s.toString());
        }
    }
}
