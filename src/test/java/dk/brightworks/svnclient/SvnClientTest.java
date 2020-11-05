package dk.brightworks.svnclient;

import org.junit.Ignore;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Ignore
public class SvnClientTest {
    @Test
    public void testExport() throws SVNException, NoSuchAlgorithmException, KeyManagementException {
        SvnClient.main(new String[] { "export", "-", "-", "https://svn.apache.org/repos/asf/jakarta/oro/trunk/docs/api/org/apache/oro/text/perl/", "./tester"});
    }
}
