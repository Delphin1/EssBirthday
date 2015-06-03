package com.belkam.utils;

import com.belkam.utils.JCIFSEngine;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.protocol.HttpContext;

/**
 * Created by tsg on 16.03.2015.
 */
public class JCIFSNTLMSchemeFactory implements AuthSchemeProvider {

    public AuthScheme create(final HttpContext context) {
        return new NTLMScheme(new JCIFSEngine());
    }
}