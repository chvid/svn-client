# A Subversion client for when everything else fails

Requires Apache Maven 3+ and Java 8.

Usage:

```
mvn compile exec:java -Dexec.mainClass=dk.brightworks.svnclient.SvnClient -Dexec.args="export username password https://svn.apache.org/repos/asf/jakarta/oro/trunk/docs/api/org/apache/oro/text/perl/ tester"
```

Exports the given url into the given destination (here tester) using the provided username and password.
