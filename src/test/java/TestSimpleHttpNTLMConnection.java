import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;




import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestSimpleHttpNTLMConnection {

    //@Test
    public void testConnection() throws ClientProtocolException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (compatible; MSIE 10.6; Windows NT 6.1; Trident/5.0; InfoPath.2; SLCC1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 2.0.50727) 3gpp-gba UNTRUSTED/1.0");
        List<String> authpref = new ArrayList<String>();
        authpref.add(AuthPolicy.NTLM);
        httpclient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, authpref);
        NTCredentials creds = new NTCredentials("", "", "ess.belkam.com", "ESS");
        httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

        HttpHost target = new HttpHost("ess.belkam.com", 80, "http");

// Make sure the same context is used to execute logically related requests
        HttpContext localContext = new BasicHttpContext();

// Execute a cheap method first. This will trigger NTLM authentication
        HttpGet httpget = new HttpGet("/SpravWeb/show.aspx?uri=/sprav&spravid=003");
        HttpResponse response = httpclient.execute(target, httpget, localContext);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));


    }

}