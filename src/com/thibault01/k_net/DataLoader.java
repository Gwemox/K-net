package com.thibault01.k_net;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.client.methods.HttpGet;
public class DataLoader {
private DefaultHttpClient sslClient;

	public HttpResponse secureLoadData(String url) throws KeyManagementException, UnrecoverableKeyException, ClientProtocolException, NoSuchAlgorithmException, KeyStoreException, IOException, URISyntaxException
	{
		return secureLoadData(url, null);
	}
    public HttpResponse secureLoadData(String url, String PHPSESSID )
            throws ClientProtocolException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            URISyntaxException, KeyStoreException, UnrecoverableKeyException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new CustomX509TrustManager() },
                new SecureRandom());

        HttpClient client = new DefaultHttpClient();
        
        SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        //sr.register(new Scheme("https", ssf, 443));
        
        // use our own, SNI-capable LayeredSocketFactory for https://
        //SchemeRegistry schemeRegistry = client.getConnectionManager().getSchemeRegistry();
        sr.register(new Scheme("https", new TlsSniSocketFactory(), 443));
        
        
         sslClient = new DefaultHttpClient(ccm,
                client.getParams());


        HttpGet get = new HttpGet(new URI(url));
        if(PHPSESSID != null)
        {
        	get.setHeader("Cookie", "PHPSESSID=" + PHPSESSID + ";");
        }
        HttpResponse response = sslClient.execute(get);
        
        return response;
    }
    
    public List<Cookie> getCookies()
    {
    	return sslClient.getCookieStore().getCookies();
    }

    public CookieStore getCookieStore()
    {
    	return sslClient.getCookieStore();
    }
}