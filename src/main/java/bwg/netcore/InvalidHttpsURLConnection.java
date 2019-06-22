package bwg.netcore;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class InvalidHttpsURLConnection {
    public HttpsURLConnection getConnection(URL url) throws KeyManagementException, NoSuchAlgorithmException, IOException{
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new InvalidCertificateTrustManager() }, null);
        SSLContext.setDefault(ctx);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setHostnameVerifier(new InvalidCertificateHostVerifier());

        return connection;
    }
}
