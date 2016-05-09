package com.example.ramesh.wikisearch;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;


/**
 * Created by ramesh on 5/7/2016.
 */
public class WikiApi {

    // API integration
    /*https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&
    pilimit=50&generator=prefixsearch&gpssearch=<your search term>*/

    public static HttpClient getHttpsClient(HttpClient client) {

        try{

            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

				/*Protocol API Levels
				Default 9+
				SSL 9+
				SSLv3 9+
				TLS 1+
				TLSv1 1+
				TLSv1.1 16+
				TLSv1.2 16+ */

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager},  null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);

            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));

            return new DefaultHttpClient(clientConnectionManager, client.getParams());

        } catch (Exception ex) {
            return null;
        }
    }

    public static InputStream executeWikiAPI(String baseurl, String searchedtext) throws Exception
    {
        String result = "";
        try
        {
            StringBuffer sb = new StringBuffer(baseurl);

            if(!searchedtext.equalsIgnoreCase(""))
            {
                sb.append(searchedtext);
            }

            System.out.println("url to post data: " + sb.toString());

            HttpGet request = new HttpGet(sb.toString());

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            final int CONN_WAIT_TIME = 45000;
            final int CONN_DATA_WAIT_TIME = 120000;

            // for https
            HttpClient client = getHttpsClient(new DefaultHttpClient());
            //HttpClient client = getNewHttpClient();
            //HttpClient httpclient = createHttpClient();
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, CONN_WAIT_TIME);
            HttpConnectionParams.setSoTimeout(params, CONN_DATA_WAIT_TIME);
            request.setParams(params);

            HttpResponse response = client.execute(request);

            if(response!=null && response.getStatusLine().getStatusCode()==200)
            {
                ////System.out.println("response from mail :"+ response);
                return response.getEntity().getContent();
            }
            else
            {
                return new ByteArrayInputStream(result.getBytes());
            }

        }
        catch (Exception e)
        {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
            return new ByteArrayInputStream(result.getBytes());
        }

        //return result;
    }


    public static String convertStreamToString(InputStream is) throws IOException
    {
        StringBuffer writer = new StringBuffer();
        if (is != null)
        {
            try
            {
                int iChar = -1;
                do
                {
                    iChar = is.read();
                    if (iChar != -1)
                        writer.append( (char) iChar);
                }
                while(iChar != -1);
            }
            catch (Exception e) {
            }
        }
        return writer.toString();
    }


    public static String getWikiSearchImages(String searchedtext)
    {
        InputStream response = null;
        String l_string = "";
        String url = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=100&pilimit=50&generator=prefixsearch&gpssearch=";

        try
        {
            response = WikiApi.executeWikiAPI(url, searchedtext);
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        try
        {
            l_string = convertStreamToString(response);
            System.out.println("Return String From Server : " + l_string);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return l_string;
    }
}
