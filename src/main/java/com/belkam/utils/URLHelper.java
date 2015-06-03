package com.belkam.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tsg on 19.03.2015.
 */
public class URLHelper {
    public static String getSimple(String URL)  {
        HttpGet req = new HttpGet(URL);
        req.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
        CloseableHttpClient httpclient = null;
        try   {
            httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(req);
            return IOUtils.toString(response.getEntity().getContent());
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


//-----------------------------------------------------------------------------
    public static String getNTLM(String URL, String pUser, String pPassword, String pDomain) {
        HttpGet req = new HttpGet(URL);
        req.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)");
        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                .register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory())
                .register(AuthSchemes.BASIC, new BasicSchemeFactory())
                .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
                .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory())
                .register(AuthSchemes.KERBEROS, new KerberosSchemeFactory())
                .build();
        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                .build();
        NTCredentials creds = new NTCredentials(pUser, pPassword, URL, pDomain);
        AuthCache authCache = new BasicAuthCache();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, creds);
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);
        CloseableHttpResponse response;
        try {
            response = httpclient.execute(req, context);
            InputStream inputStream = response.getEntity().getContent();
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
