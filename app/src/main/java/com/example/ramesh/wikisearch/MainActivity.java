package com.example.ramesh.wikisearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ArrayList<DataObject> array_search = new ArrayList<DataObject>();
    SearchView searchview;
    RecyclerView recyclerView;

    GlobalAccess globalobject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){

        globalobject = (GlobalAccess) getApplicationContext();

        searchview = (SearchView)findViewById(R.id.searchview);
        searchview.setOnQueryTextListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public boolean onQueryTextChange(String newText) {

        return false;
    }

    public boolean onQueryTextSubmit(String query) {

        if(!query.equalsIgnoreCase(""))
        {
            array_search.clear();

            if(globalobject.haveNetworkConnection(MainActivity.this))
            {
                Getdatafromwiki getPaymentDataHistoryAsync = new Getdatafromwiki();
                getPaymentDataHistoryAsync.execute(query);
            }
            else
            {
                globalobject.Networkalertmessage(MainActivity.this);
            }
        }

        return true;
    }

    // asynctask for wikisearch
    private class Getdatafromwiki extends AsyncTask<String, Void, String>
    {
        //private ProgressDialog progressdialog;


        protected void onPreExecute() {

            array_search.clear();
          // this.progressdialog = ProgressDialog.show(MainActivity.this, "", "please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            // do all your backgroundtasks

           String result =  WikiApi.getWikiSearchImages(params[0]);

            return result;
        }

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            try
            {
               // this.progressdialog.cancel();

                if(result!=null)
                {
                    JSONObject jsonResponse=null;
                   // JSONArray jarraypages = new JSONArray();
                    try
                    {
                        jsonResponse = new JSONObject(result);
                        // int jobjectcount = jsonResponse.optJSONObject("continue").optInt("gpsoffset");
                        JSONObject jobjectquery = jsonResponse.optJSONObject("query");
                        JSONObject jobjectpages = jobjectquery.optJSONObject("pages");

                       // jarraypages = jobjectpages.toJSONArray(jarraypages);


                        Iterator x = jobjectpages.keys();
                        JSONArray jarraypages = new JSONArray();

                        while (x.hasNext()){
                            String key = (String) x.next();
                            jarraypages.put(jobjectpages.get(key));
                        }

                        System.out.println("size "+jarraypages.length());

                        for(int i=0;i<jarraypages.length();i++)
                        {
                            String image_url = "";
                            DataObject Bo = new DataObject();
                            String title =  jarraypages.optJSONObject(i).optString("title");
                            if(jarraypages.optJSONObject(i).has("thumbnail"))
                             image_url = jarraypages.optJSONObject(i).optJSONObject("thumbnail").optString("source");

                            Bo.setImage_title(title);
                            Bo.setImage_url(image_url);

                            array_search.add(Bo);
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    if(array_search.size()>0)
                    {
                        DataAdapter adapter = new DataAdapter(getApplicationContext(),array_search);
                        recyclerView.setAdapter(adapter);
                    }
                    else
                    {
                        DataAdapter adapter = new DataAdapter(getApplicationContext(),array_search);
                        recyclerView.setAdapter(adapter);
                    }

                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
