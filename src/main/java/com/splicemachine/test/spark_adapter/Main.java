package com.splicemachine.test.spark_adapter;

import com.splicemachine.derby.impl.SpliceSpark;
import com.splicemachine.spark.splicemachine.SplicemachineContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class Main {
    private static org.apache.hadoop.yarn.client.api.YarnClient yarnClient;

    public static void main(String[] args) throws Exception {

        String spliceengineHome = "/Users/liuxiao/spliceengine/";
        String platformITHome = spliceengineHome + "platform_it/";
        String krb5confPath = platformITHome + "target/krb5.conf";
        String keytabFilePath = platformITHome + "target/splice.keytab";
        String coreSiteXml = platformITHome + "target/classes/core-site.xml";
        String yarnSiteXml = platformITHome + "target/classes/yarn-site.xml";
        String hbaseSiteXml = platformITHome + "target/hbase-site.xml";

        String sparkExtraJavaOptions = "-Djava.security.krb5.conf=" + krb5confPath +
                " -Dsplice.spark.yarn.principal=hbase/example.com@EXAMPLE.COM" +
                " -Dsplice.spark.yarn.keytab=" + keytabFilePath;
        String sparkYarnJarsPath = "local:" + platformITHome + "target/dependency/*";

        LogManager.getLogger("org").setLevel(Level.OFF);
        System.setProperty("java.security.krb5.conf", krb5confPath);

        String dbUrl = "jdbc:splice://localhost:1527/splicedb;user=splice;password=admin";

        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.set("spark.yarn.jars", sparkYarnJarsPath);
        sparkConfiguration.set("spark.master", "yarn");

        sparkConfiguration.set("spark.yarn.principal", "hbase/example.com@EXAMPLE.COM");
        sparkConfiguration.set("spark.yarn.keytab", keytabFilePath);
        sparkConfiguration.set("spark.yarn.am.extraJavaOptions", sparkExtraJavaOptions);// -Dsun.security.spnego.debug=true")
        sparkConfiguration.set("spark.executor.extraJavaOptions", sparkExtraJavaOptions);

        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        File coreSiteResourceFile = new File("src/main/resources/core-site.xml");
        File yarnSiteResourceFile = new File("src/main/resources/yarn-site.xml");
        File hbaseSiteResourceFile = new File("src/main/resources/hbase-site.xml");

        File coreSiteFile = new File(coreSiteXml);
        File yarnSiteFile = new File(yarnSiteXml);
        File hbaseSiteFile = new File(hbaseSiteXml);

        Files.copy(coreSiteFile.toPath(), coreSiteResourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(yarnSiteFile.toPath(), yarnSiteResourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(hbaseSiteFile.toPath(), hbaseSiteResourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        conf.addDefaultResource("core-site.xml");
        conf.addDefaultResource("hbase-site.xml");
        conf.addDefaultResource("yarn-site.xml");

        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("hbase/example.com@EXAMPLE.COM", keytabFilePath);
            SparkSession sparkSession = SparkSession.builder().config(sparkConfiguration).getOrCreate();
            SpliceSpark.setContext(sparkSession.sparkContext());
            SplicemachineContext splicemachineContext = new SplicemachineContext(dbUrl);
            splicemachineContext.df("select * from sys.systables").printSchema();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
