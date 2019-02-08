export SPARK_KAFKA_VERSION=0.10

TargetTable=LINEITEM
TargetSchema=user5
RSHostName=srv083
SpliceConnectPort=1527
UserName=user5
UserPassword=splice
KrbPrincipal=user5@SPLICEMACHINE.COLO
KrbKeytab=/tmp/user5.keytab
HdfsHostName=srv082
HdfsPort=8020
#CsvFilePath=/TPCH/1/lineitem/lineitem.csv
CsvFilePath=/data/TPCH/1/lineitem

spark2-submit --conf "spark.driver.extraJavaOptions=-Dsplice.spark.yarn.principal=user5@SPLICEMACHINE.COLO \
-Dsplice.spark.yarn.keytab=/tmp/user5.keytab \
-Dsplice.spark.enabled=true \
-Dsplice.spark.app.name=SpliceETLApp \
-Dsplice.spark.master=yarn-client \
-Dsplice.spark.logConf=true \
-Dsplice.spark.yarn.maxAppAttempts=1 \
-Dsplice.spark.driver.maxResultSize=3g \
-Dsplice.spark.driver.cores=4 \
-Dsplice.spark.yarn.am.memory=2g \
-Dsplice.spark.dynamicAllocation.enabled=true \
-Dsplice.spark.dynamicAllocation.executorIdleTimeout=30 \
-Dsplice.spark.dynamicAllocation.cachedExecutorIdleTimeout=30 \
-Dsplice.spark.dynamicAllocation.minExecutors=8 \
-Dsplice.spark.dynamicAllocation.maxExecutors=17 \
-Dsplice.spark.memory.fraction=0.6 \
-Dsplice.spark.scheduler.mode=FAIR \
-Dsplice.spark.serializer=org.apache.spark.serializer.KryoSerializer \
-Dsplice.spark.shuffle.service.enabled=true \
-Dsplice.spark.yarn.am.extraLibraryPath=/opt/cloudera/parcels/CDH/lib/hadoop/lib/native \
-Dsplice.spark.driver.extraJavaOptions=-Dlog4j.configuration=file:/etc/spark/conf/log4j.properties \
-Dsplice.spark.driver.extraLibraryPath=/opt/cloudera/parcels/CDH/lib/hadoop/lib/native \
-Dsplice.spark.driver.extraClassPath=/opt/cloudera/parcels/CDH/lib/hbase/conf:/opt/cloudera/parcels/CDH/jars/htrace-core-3.2.0-incubating.jar \
-Dsplice.spark.executor.extraLibraryPath=/opt/cloudera/parcels/CDH/lib/hadoop/lib/native \
-Dsplice.spark.executor.extraClassPath=/opt/cloudera/parcels/CDH/lib/hbase/conf:/opt/cloudera/parcels/CDH/jars/htrace-core-3.2.0-incubating.jar \
-Dsplice.spark.eventLog.enabled=true \
-Dsplice.spark.eventLog.dir=hdfs:///user/spark/spark2ApplicationHistory \
-Dsplice.spark.local.dir=/tmp \
-Dsplice.spark.yarn.jars=/opt/cloudera/parcels/SPLICEMACHINE/lib/* \
-Dsplice.spark.ui.port=4042" \
--conf "spark.dynamicAllocation.enabled=false" \
--conf "spark.streaming.stopGracefullyOnShutdown=true" \
--conf "spark.streaming.kafka.maxRatePerPartition=500" \
--conf "spark.streaming.kafka.consumer.cache.enabled=false" \
--conf "spark.streaming.concurrentJobs=1" \
--conf "spark.task.maxFailures=2" \
--conf "spark.driver.memory=4g" \
--conf "spark.driver.cores=1" \
--conf "spark.kryoserializer.buffer=1024" \
--conf "spark.kryoserializer.buffer.max=2047" \
--conf "spark.io.compression.codec=org.apache.spark.io.SnappyCompressionCodec" \
--conf "spark.driver.extraJavaOptions=-Djava.security.krb5.conf=/etc/krb5.conf -Dspark.yarn.principal=user5@SPLICEMACHINE.COLO -Dspark.yarn.keytab=/tmp/user5.keytab -Dlog4j.configuration=log4j-spark.properties -XX:+UseCompressedOops -XX:+UseG1GC -XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark -XX:InitiatingHeapOccupancyPercent=35 -XX:ConcGCThreads=12" \
--conf "spark.executor.extraJavaOptions=-Djava.security.krb5.conf=krb5.conf -Dlog4j.configuration=log4j-spark.properties -XX:+UseCompressedOops -XX:+UseG1GC -XX:+PrintFlagsFinal -XX:+PrintReferenceGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions -XX:+G1SummarizeConcMark -XX:InitiatingHeapOccupancyPercent=35 -XX:ConcGCThreads=12" \
--conf "spark.executor.extraClassPath=/etc/hadoop/conf/:/etc/hbase/conf/:/opt/cloudera/parcels/SPLICEMACHINE/lib/*:/opt/cloudera/parcels/SPARK2/lib/spark2/jars/*:/opt/cloudera/parcels/CDH/lib/hbase/lib/*" \
--conf "spark.driver.extraClassPath=/etc/hadoop/conf/:/etc/hbase/conf/:/opt/cloudera/parcels/SPLICEMACHINE/lib/*:/opt/cloudera/parcels/SPARK2/lib/spark2/jars/*:/opt/cloudera/parcels/CDH/lib/hbase/lib/*" \
--files "/etc/spark/conf/log4j.properties,/etc/krb5.conf"  \
--keytab "/tmp/user5.keytab"  \
--principal "user5@SPLICEMACHINE.COLO" \
--name "Ingest" \
--jars "splicemachine-cdh5.13.3-2.2.0.cloudera2_2.11-2.5.0.1826.jar" \
--class com.splice.custom.reader.Main \
--master yarn --deploy-mode client --num-executors 4 --executor-memory 10G --executor-cores 1 reader_cdh5133.jar \
$TargetTable $TargetSchema $RSHostName $SpliceConnectPort $UserName $UserPassword $HdfsHostName $HdfsPort $CsvFilePath false