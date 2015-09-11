Success!

Job Stats (time in seconds):
JobId   Maps    Reduces MaxMapTime      MinMapTime      AvgMapTime      MedianMapTime   MaxReduceTime   MinReduceTime   AvgReduceTime   MedianReducetime       Alias    Feature Outputs
job_local1054144072_0007        2       1       n/a     n/a     n/a     n/a    n/a      n/a     n/a     n/a     K,L,M,users     HASH_JOIN
job_local1916128732_0002        2       1       n/a     n/a     n/a     n/a    n/a      n/a     n/a     n/a     A,B,E,movies    HASH_JOIN
job_local1950674568_0006        2       1       n/a     n/a     n/a     n/a    n/a      n/a     n/a     n/a     J,ratings       HASH_JOIN
job_local1959631394_0004        1       1       n/a     n/a     n/a     n/a    n/a      n/a     n/a     n/a     F       ORDER_BY,COMBINER
job_local2059151139_0008        1       1       n/a     n/a     n/a     n/a    n/a      n/a     n/a     n/a             DISTINCT        file:/tmp/temp1711111659/tmp-1684217630,
job_local633894468_0005 1       1       n/a     n/a     n/a     n/a     n/a    n/a      n/a     n/a     F,G,I
job_local826804171_0001 1       1       n/a     n/a     n/a     n/a     n/a    n/a      n/a     n/a     C,D,ratings     MULTI_QUERY,COMBINER
job_local987712450_0003 1       1       n/a     n/a     n/a     n/a     n/a    n/a      n/a     n/a     F       SAMPLER

Input(s):
Successfully read 1000209 records from: "/usr/lib/hue/inputHw3/ratings.dat"
Successfully read 3883 records from: "/usr/lib/hue/inputHw3/movies.dat"
Successfully read 6040 records from: "/usr/lib/hue/inputHw3/users.dat"

Output(s):
Successfully stored 0 records in: "file:/tmp/temp1711111659/tmp-1684217630"

Counters:
Total records written : 0
Total bytes written : 0
Spillable Memory Manager spill count : 0
Total bags proactively spilled: 0
Total records proactively spilled: 0

Job DAG:
job_local826804171_0001 ->      job_local1916128732_0002,job_local1950674568_0006,
job_local1916128732_0002        ->      job_local987712450_0003,
job_local987712450_0003 ->      job_local1959631394_0004,
job_local1959631394_0004        ->      job_local633894468_0005,
job_local633894468_0005 ->      job_local1950674568_0006,
job_local1950674568_0006        ->      job_local1054144072_0007,
job_local1054144072_0007        ->      job_local2059151139_0008,
job_local2059151139_0008


2015-02-11 19:33:30,695 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,697 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,699 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,719 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,720 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,887 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,901 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,903 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,904 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,916 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,919 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,921 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,934 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,936 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,937 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,956 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,957 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,958 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,970 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,971 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,972 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,983 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,984 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:30,985 [main] INFO  org.apache.hadoop.metrics.jvm.JvmMetrics - Cannot initialize JVM Metrics with processName=JobTracker, sessionId= - already initialized
2015-02-11 19:33:31,004 [main] INFO  org.apache.pig.backend.hadoop.executionengine.mapReduceLayer.MapReduceLauncher - Success!
2015-02-11 19:33:31,058 [main] WARN  org.apache.pig.data.SchemaTupleBackend - SchemaTupleBackend has already been initialized
2015-02-11 19:33:31,075 [main] INFO  org.apache.hadoop.mapreduce.lib.input.FileInputFormat - Total input paths to process : 1
2015-02-11 19:33:31,075 [main] INFO  org.apache.pig.backend.hadoop.executionengine.util.MapRedUtil - Total input paths to process : 1
