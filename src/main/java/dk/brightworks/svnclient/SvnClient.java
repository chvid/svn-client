package dk.brightworks.svnclient;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import javax.net.ssl.*;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class SvnClient {
    private void killSslChecks() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier validHosts = (a, b) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
    }

    private SVNClientManager clientManager;

    SvnClient(String username, String password) throws KeyManagementException, NoSuchAlgorithmException {
        killSslChecks();
        DAVRepositoryFactory.setup();
        clientManager = SVNClientManager.newInstance(new DefaultSVNOptions(), username, password);
    }

    private void shutdown() {
        clientManager.shutdownConnections(true);
    }

    private void export(String source, String destination) throws SVNException {
        SVNUpdateClient updateClient = clientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        updateClient.doExport(SVNURL.parseURIEncoded(source), new File(destination), SVNRevision.HEAD, SVNRevision.HEAD, "", false, SVNDepth.INFINITY);
    }

    public static void main(String[] args) throws SVNException, NoSuchAlgorithmException, KeyManagementException {
        String command = args[0];

        SvnClient client = new SvnClient(args[1], args[2]);

        try {
            switch (command) {
                case "export":
                    client.export(args[3], args[4]);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        } finally {
            client.shutdown();
        }
    }
}
