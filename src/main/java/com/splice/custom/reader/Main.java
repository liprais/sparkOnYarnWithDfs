package com.splice.custom.reader;

import com.amazonaws.services.applicationdiscovery.AWSApplicationDiscoveryClient;
import com.amazonaws.services.applicationdiscovery.AbstractAWSApplicationDiscovery;
import com.splicemachine.derby.impl.SpliceSpark;
import com.splicemachine.spark.splicemachine.SplicemachineContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.api.impl.pb.client.ApplicationClientProtocolPBClientImpl;
import org.apache.hadoop.yarn.api.protocolrecords.SubmitApplicationRequest;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.server.utils.BuilderUtils;
import org.apache.hadoop.yarn.util.Records;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.deploy.SparkHadoopUtil;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.hadoop.conf.Configuration;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.ExecutorLauncher;
import org.apache.hadoop.yarn.client.api.impl.YarnClientImpl;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class Main {
    private static org.apache.hadoop.yarn.client.api.YarnClient yarnClient;

    public static void main(String[] args) throws Exception {



        //LogManager.getLogger("org").setLevel(Level.INFO);
        //LogManager.getLogger("org.apache.spark.deploy.yarn.Client").setLevel(Level.DEBUG);
        System.setProperty("java.security.krb5.conf", "/Users/liuxiao/spliceengine/platform_it/target/krb5.conf");
        System.setProperty("YARN_CONF_DIR", "/Users/liuxiao/spliceengine/platform_it/target/classes/");
        System.setProperty("HADOOP_CONF_DIR","/Users/liuxiao/spliceengine/platform_it/target/classes/");

        String dbUrl = "jdbc:splice://localhost:1527/splicedb;user=splice;password=admin";
        /*

        SparkConf sparkConfiguration = new SparkConf();
        sparkConfiguration.set("spark.yarn.jars", "local:/Users/liuxiao/spliceengine/platform_it/target/dependency/*");
        sparkConfiguration.set("spark.submit.deployMode","client");
        sparkConfiguration.set("spark.yarn.access.hadoopFileSystem","hdfs://localhost:58878");
        sparkConfiguration.set("spark.authenticate","true");
        sparkConfiguration.set("spark.master","yarn");
        sparkConfiguration.set("spark.hadoop.fs.hdfs.impl.disable.cache","true");
        sparkConfiguration.set("spark.yarn.security.credentials.hadoopfs.enabled","true");


        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        String coreSiteXml = "/Users/liuxiao/spliceengine/platform_it/target/classes/core-site.xml";
        String KRB5_CONF_PATH = "/Users/liuxiao/spliceengine/platform_it/target/krb5.conf";
        conf.addResource(new Path(coreSiteXml));
        System.setProperty("java.security.krb5.conf", KRB5_CONF_PATH);
        SparkSession spark = SparkSession.builder().config(sparkConfiguration).getOrCreate();




        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("hbase/example.com@EXAMPLE.COM", "/Users/liuxiao/spliceengine/platform_it/target/splice.keytab");
            System.out.println("I am in!");
            UserGroupInformation.getCurrentUser().doAs(new PrivilegedExceptionAction<Void>() {
                @Override
                public Void run() throws Exception {

                    return null;
                }


         });
        } catch (IOException e) {
            e.printStackTrace();
        }*/







        SparkSession sparkSession = SparkSession.builder().master("yarn").getOrCreate();
        SpliceSpark.setContext(sparkSession.sparkContext());
        SplicemachineContext splicemachineContext = new SplicemachineContext(dbUrl);
        splicemachineContext.df("select * from sys.systables").printSchema();
        sparkSession.stop();

        //System.exit(0);


    }

}
