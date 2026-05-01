package gzb.frame.factory;

import gzb.frame.factory.v4.FactoryImplV2;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassByteTools {
    public static String cachePath = Config.thisPath + "gzbClassCache/";
    public static String outPath = cachePath + "out/";
    public static String outConfigPath = outPath + "META-INF/native-image/";
    public static Map<String, String> namesList = new HashMap<>();

    static {
        String[] arr1 = FileTools.readArray(new File(outPath + "className.config"));
        if (arr1 != null) {
            for (String string : arr1) {
                if (string == null || string.length() < 1) {
                    continue;
                }
                namesList.put(string, "1");
            }
        }

        ClassTools.readSingAll(cachePath + "mapping0.config");
    }

    //编译二进制 部分暂时搁置
    public static void gen_cmd() {
        String delimiter = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            delimiter = ";";
        } else {
            delimiter = ":";
        }
        String[] jars = System.getProperty("java.class.path", "").split(delimiter);
        StringBuilder newData = new StringBuilder();

        for (String jar : jars) {
            if (jar.endsWith(".jar")) { //主要过滤掉原classpath
                newData.append(jar).append(delimiter);
            }
        }
        if (newData.substring(newData.lastIndexOf(delimiter), newData.length()).equals(delimiter)) {
            newData.deleteCharAt(newData.lastIndexOf(delimiter));
        }
        long name = System.currentTimeMillis();
        String cmd = "native-image " +
                "-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 " +
                "-cp " +
                outPath + ";" +
                outPath + delimiter + newData + " " + Tools.getStartClassName() + " " +
                "--allow-incomplete-classpath " +
                "--no-fallback " +
                "--initialize-at-run-time=org.slf4j,com.alibaba,io.netty,org.ow2,com.mysql,org.postgresql,com.zaxxer,redis.clients " +
                "--no-fallback " +
                "-o " + outPath + name;
        File file0 = new File(outPath + "pack.bat");
        FileTools.createFile(file0);
        FileTools.save(file0, cmd);
        File file1 = new File(outPath + "start-log.bat");
        FileTools.save(file1, name + ".exe -Dgraalvm.native.image.logging=verbose -Djava.util.logging.manager=java.util.logging.LogManager > native-debug.log 2>&1");

    }

    public static void saveBytes(String pkg, Map<String, String> sourcesMap, byte[] data) throws Exception {
        String code = sourcesMap.get(pkg.split("\\$")[0]);
        if (code == null) {
            return;
        }
        namesList.put(pkg.split("\\$")[0], "1");
        String md5 = Tools.toMd5(code.getBytes(Config.encoding));
        //保存字节码
        String path0 = pkg.split("\\$")[0].replace(".", "/");//兼容内部类
        String[] arr1 = pkg.split("\\.");
        String name0 = arr1[arr1.length - 1];
        //生成缓存类
        File file = new File(cachePath + path0 + "/" + md5 + "/" + name0 + ".class");
        FileTools.createFile(file);
        FileTools.save(file, data);
        //保存源码
        String path1 = file.getParentFile().getPath() + "/"+name0+".java";
        FileTools.save(new File(path1), code);
  /*
        //生成输出类
        File file0 = new File(outPath + (pkg.replace(".", "/")) + ".class");
        FileTools.createFile(file0);
        FileTools.save(file0, data);
        outReflect();
        outResources();
        outClassName();
        ClassTools.saveSingAll(outPath + "mapping0.config");
*/
    }

    //com.frame.api.Test2$1
    public static Map<String, byte[]> readBytes(String pkg, Map<String, String> sourcesMap) throws Exception {
        String code = sourcesMap.get(pkg.split("\\$")[0]);
        Map<String, byte[]> map0 = new HashMap<>();
        if (code == null || pkg == null) {
            return map0;
        }
        String md5 = Tools.toMd5(code.getBytes(Config.encoding));
        //读取字节码
        String path0 = pkg.split("\\$")[0].replace(".", "/");//兼容内部类
        String[] arr1 = pkg.split("\\.");
        String name0 = arr1[arr1.length - 1];
        String path = cachePath + path0 + "/" + md5 + "/";
        List<File> listFile = FileTools.subFileAll(new File(path), 2, ".class");
        for (File file : listFile) {
            map0.put(pkg.replace(name0, file.getName().substring(0, file.getName().length() - 6)), FileTools.readByte(file));
        }
        return map0;
    }

    public static void outClassName() {
        StringBuilder allData = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : namesList.entrySet()) {
            String string = stringStringEntry.getKey();
            allData.append(string).append("\n");
        }
        FileTools.save(new File(outPath + "className.config"), allData.toString());
    }

    public static void outResources() {
        if (outConfigPath == null) {
            return;
        }
        StringBuilder allData = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : namesList.entrySet()) {
            String string = stringStringEntry.getKey();
            allData.append("{\n" + "    \"pattern\":\"\\\\Q").append(string).append(".class\\\\E\"\n").append("  },");
        }
        String resources = "{\n" +
                "  \"resources\":{\n" +
                "  \"includes\":[{\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/com.sun.source.util.Plugin\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.lang.System$LoggerFinder\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.net.spi.InetAddressResolverProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.nio.channels.spi.SelectorProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.nio.file.spi.FileSystemProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.rmi.server.RMIClassLoaderSpi\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.sql.Driver\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.time.zone.ZoneRulesProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/java.util.spi.ResourceBundleControlProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/javax.annotation.processing.Processor\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\QMETA-INF/services/org.slf4j.spi.SLF4JServiceProvider\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"\\\\Q\\\\E\"\n" +
                "  },\n" + allData +
                "  {\n" +
                "    \"pattern\":\"\\\\Qcom/mysql/cj/TlsSettings.properties\\\\E\"\n" +
                "  }, {\n" +
                "    \"pattern\":\"jdk.jfr:\\\\Qjdk/jfr/internal/query/view.ini\\\\E\"\n" +
                "  }]},\n" +
                "  \"bundles\":[{\n" +
                "    \"name\":\"com.mysql.cj.LocalizedErrorMessages\",\n" +
                "    \"locales\":[\"\"]\n" +
                "  }, {\n" +
                "    \"name\":\"com.sun.tools.javac.resources.compiler\",\n" +
                "    \"classNames\":[\"com.sun.tools.javac.resources.compiler\", \"com.sun.tools.javac.resources.compiler_zh_CN\"]\n" +
                "  }, {\n" +
                "    \"name\":\"com.sun.tools.javac.resources.javac\",\n" +
                "    \"classNames\":[\"com.sun.tools.javac.resources.javac\", \"com.sun.tools.javac.resources.javac_zh_CN\"]\n" +
                "  }]\n" +
                "}\n";
        FileTools.save(new File(outConfigPath + "resource-config.json"), resources);

    }

    public static void outJni() {

        if (outConfigPath == null) {
            return;
        }
        FileTools.save(new File(outConfigPath + "jni-config.json"), "[\n" +
                "{\n" +
                "  \"name\":\"[Lcom.sun.management.internal.DiagnosticCommandArgumentInfo;\"\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"[Lcom.sun.management.internal.DiagnosticCommandInfo;\"\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"com.sun.management.internal.DiagnosticCommandArgumentInfo\",\n" +
                "  \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"boolean\",\"boolean\",\"boolean\",\"int\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"com.sun.management.internal.DiagnosticCommandInfo\",\n" +
                "  \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"java.lang.String\",\"boolean\",\"java.util.List\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"java.lang.Boolean\",\n" +
                "  \"methods\":[{\"name\":\"getBoolean\",\"parameterTypes\":[\"java.lang.String\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"java.lang.InternalError\",\n" +
                "  \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"java.util.Arrays\",\n" +
                "  \"methods\":[{\"name\":\"asList\",\"parameterTypes\":[\"java.lang.Object[]\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"sun.instrument.InstrumentationImpl\",\n" +
                "  \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"long\",\"boolean\",\"boolean\",\"boolean\"] }, {\"name\":\"loadClassAndCallAgentmain\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\"] }, {\"name\":\"loadClassAndCallPremain\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\"] }, {\"name\":\"transform\",\"parameterTypes\":[\"java.lang.Module\",\"java.lang.ClassLoader\",\"java.lang.String\",\"java.lang.Class\",\"java.security.ProtectionDomain\",\"byte[]\",\"boolean\"] }]\n" +
                "},\n" +
                "{\n" +
                "  \"name\":\"sun.management.VMManagementImpl\",\n" +
                "  \"fields\":[{\"name\":\"compTimeMonitoringSupport\"}, {\"name\":\"currentThreadCpuTimeSupport\"}, {\"name\":\"objectMonitorUsageSupport\"}, {\"name\":\"otherThreadCpuTimeSupport\"}, {\"name\":\"remoteDiagnosticCommandsSupport\"}, {\"name\":\"synchronizerUsageSupport\"}, {\"name\":\"threadAllocatedMemorySupport\"}, {\"name\":\"threadContentionMonitoringSupport\"}]\n" +
                "}\n" +
                "]\n");

    }

    public static void outPredefined() {

        if (outConfigPath == null) {
            return;
        }
        FileTools.save(new File(outConfigPath + "predefined-classes-config.json"), "[\n" +
                "  {\n" +
                "    \"type\":\"agent-extracted\",\n" +
                "    \"classes\":[\n" +
                "    ]\n" +
                "  }\n" +
                "]\n" +
                "\n");

    }

    public static void outProxy() {
        if (outConfigPath == null) {
            return;
        }
        FileTools.save(new File(outConfigPath + "proxy-config.json"), "[\n" +
                "  {\n" +
                "    \"interfaces\":[\"java.sql.Connection\"]\n" +
                "  }\n" +
                "]\n");

    }

    public static void outSerialization() {

        if (outConfigPath == null) {
            return;
        }
        FileTools.save(new File(outConfigPath + "serialization-config.json"), "{\n" +
                "  \"types\":[\n" +
                "    {\n" +
                "      \"name\":\"byte[]\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"gzb.tools.cache.GzbCacheMap$CacheEntry\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.io.File\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.lang.Double\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.lang.Long\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.lang.Number\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.lang.String\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.dgc.Lease\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.dgc.VMID\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.server.ObjID\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.server.ObjID[]\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.server.RemoteObject\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.server.RemoteStub\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.rmi.server.UID\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.ArrayList\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.TreeMap\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.ConcurrentHashMap\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.ConcurrentHashMap$Segment\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.ConcurrentHashMap$Segment[]\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.AbstractOwnableSynchronizer\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.AbstractQueuedSynchronizer\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.ReentrantLock\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.ReentrantLock$NonfairSync\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"java.util.concurrent.locks.ReentrantLock$Sync\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.ObjectName\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.openmbean.CompositeDataSupport\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.openmbean.CompositeType\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.openmbean.OpenType\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.openmbean.SimpleType\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.remote.rmi.RMIConnectionImpl_Stub\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\":\"javax.management.remote.rmi.RMIServerImpl_Stub\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"lambdaCapturingTypes\":[\n" +
                "  ],\n" +
                "  \"proxies\":[\n" +
                "  ]\n" +
                "}\n");

    }

    public static void outReflect() {

        if (outConfigPath == null) {
            return;
        }
        StringBuilder allData = new StringBuilder();
        for (Map.Entry<String, String> stringStringEntry : namesList.entrySet()) {
            String string = stringStringEntry.getKey();
            allData.append("  {\n" + "    \"name\":\"").append(string).append("\",\n")
                    .append("    \"queryAllConstructors\":true,\n")
                    .append("    \"allDeclaredMethods\":true,\n")
                    .append("    \"allDeclaredFields\":true,\n" +
                            "\"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }, {\"name\":\"_gzb_call_x01\",\"parameterTypes\":[\"int\",\"java.util.Map\",\"gzb.frame.netty.entity.Request\",\"gzb.frame.netty.entity.Response\",\"java.util.Map\",\"gzb.tools.json.GzbJson\",\"gzb.tools.log.Log\",\"java.lang.Object[]\"] }, {\"name\":\"getData\",\"parameterTypes\":[] }, {\"name\":\"getSysOptionSqlId\",\"parameterTypes\":[] }, {\"name\":\"getSysOptionSqlKey\",\"parameterTypes\":[] }, {\"name\":\"getSysOptionSqlSql\",\"parameterTypes\":[] }, {\"name\":\"getSysOptionSqlTitleName\",\"parameterTypes\":[] }, {\"name\":\"getSysOptionSqlValName\",\"parameterTypes\":[] }, {\"name\":\"setSysOptionSqlId\",\"parameterTypes\":[\"java.lang.Long\"] }, {\"name\":\"setSysOptionSqlKey\",\"parameterTypes\":[\"java.lang.String\"] }, {\"name\":\"setSysOptionSqlSql\",\"parameterTypes\":[\"java.lang.String\"] }, {\"name\":\"setSysOptionSqlTitleName\",\"parameterTypes\":[\"java.lang.String\"] }, {\"name\":\"setSysOptionSqlValName\",\"parameterTypes\":[\"java.lang.String\"] }]\n")
                    .append("  },\n");
        }
        String reflect = "[\n" +
                "  {\n" +
                "    \"name\":\"[B\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[C\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[D\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[F\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[I\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[J\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Lcom.zaxxer.hikari.util.ConcurrentBag$IConcurrentBagEntry;\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Ljava.lang.String;\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Ljava.rmi.server.ObjID;\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Ljava.sql.Statement;\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Ljavax.management.openmbean.CompositeData;\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[S\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"[Z\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.alibaba.fastjson.JSONArray\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.alibaba.fastjson.JSONObject\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.alibaba.fastjson2.JSONFactory$CacheItem\",\n" +
                "    \"fields\":[{\"name\":\"bytes\"}, {\"name\":\"chars\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.alibaba.fastjson2.JSONObject\",\n" +
                "    \"queryAllDeclaredConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.alibaba.fastjson2.util.TypeUtils$Cache\",\n" +
                "    \"fields\":[{\"name\":\"chars\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.intellij.rt.execution.application.AppMainV2$Agent\",\n" +
                "    \"methods\":[{\"name\":\"premain\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.instrument.Instrumentation\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.PerConnectionLRUFactory\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.conf.url.SingleConnectionUrl\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"com.mysql.cj.conf.ConnectionUrlParser\",\"java.util.Properties\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.jdbc.AbandonedConnectionCleanupThread\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.jdbc.Driver\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.log.StandardLogger\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.protocol.StandardSocketFactory\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.mysql.cj.util.PerVmServerConfigCacheFactory\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.GarbageCollectorMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.GcInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.HotSpotDiagnosticMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.OperatingSystemMXBean\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getProcessCpuLoad\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.ThreadMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.VMOption\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.internal.GarbageCollectorExtImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.internal.HotSpotDiagnostic\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.internal.HotSpotThreadImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.sun.management.internal.OperatingSystemImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.zaxxer.hikari.HikariConfig\",\n" +
                "    \"allDeclaredFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"com.zaxxer.hikari.pool.PoolEntry\",\n" +
                "    \"fields\":[{\"name\":\"state\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.frame.db.DataBaseConfig_v1.gzb_frame_db_DataBaseConfig_inner_v1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.frame.db.EventFactoryImpl\",\n" +
                "    \"allDeclaredFields\":true,\n" +
                "    \"allPublicFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.frame.netty.ExceptionHandler\",\n" +
                "    \"methods\":[{\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.frame.netty.HTTPRequestHandlerV4\",\n" +
                "    \"methods\":[{\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.frame.netty.HTTPServerInitializer\"\n" +
                "  },\n" +
                "\n" +
                "  {\n" +
                "    \"name\":\"gzb.tools.cache.GzbCacheMap\",\n" +
                "    \"allDeclaredFields\":true,\n" +
                "    \"allPublicFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.tools.cache.GzbQueueImpl\",\n" +
                "    \"allDeclaredFields\":true,\n" +
                "    \"allPublicFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.tools.json.GzbJsonImpl\",\n" +
                "    \"allDeclaredFields\":true,\n" +
                "    \"allPublicFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"gzb.tools.log.LogImpl\",\n" +
                "    \"allDeclaredFields\":true,\n" +
                "    \"allPublicFields\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.bootstrap.ServerBootstrap$1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.bootstrap.ServerBootstrap$ServerBootstrapAcceptor\",\n" +
                "    \"methods\":[{\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.buffer.AbstractByteBufAllocator\",\n" +
                "    \"queryAllDeclaredMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.buffer.AbstractReferenceCountedByteBuf\",\n" +
                "    \"fields\":[{\"name\":\"refCnt\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.AbstractChannelHandlerContext\",\n" +
                "    \"fields\":[{\"name\":\"handlerState\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.ChannelInboundHandlerAdapter\",\n" +
                "    \"methods\":[{\"name\":\"channelActive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelInactive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"channelReadComplete\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRegistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelUnregistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelWritabilityChanged\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }, {\"name\":\"userEventTriggered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.ChannelInitializer\",\n" +
                "    \"methods\":[{\"name\":\"channelRegistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.ChannelOutboundBuffer\",\n" +
                "    \"fields\":[{\"name\":\"totalPendingSize\"}, {\"name\":\"unwritable\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.CombinedChannelDuplexHandler\",\n" +
                "    \"methods\":[{\"name\":\"bind\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.net.SocketAddress\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"channelActive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelInactive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"channelReadComplete\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRegistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelUnregistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelWritabilityChanged\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"close\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"connect\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.net.SocketAddress\",\"java.net.SocketAddress\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"deregister\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"disconnect\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }, {\"name\":\"flush\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"read\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"userEventTriggered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"write\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\",\"io.netty.channel.ChannelPromise\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.DefaultChannelConfig\",\n" +
                "    \"fields\":[{\"name\":\"autoRead\"}, {\"name\":\"writeBufferWaterMark\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.DefaultChannelPipeline\",\n" +
                "    \"fields\":[{\"name\":\"estimatorHandle\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.DefaultChannelPipeline$HeadContext\",\n" +
                "    \"methods\":[{\"name\":\"bind\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.net.SocketAddress\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"channelActive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelInactive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"channelReadComplete\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRegistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelUnregistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelWritabilityChanged\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"close\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"connect\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.net.SocketAddress\",\"java.net.SocketAddress\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"deregister\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"disconnect\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"io.netty.channel.ChannelPromise\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }, {\"name\":\"flush\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"read\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"userEventTriggered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"write\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\",\"io.netty.channel.ChannelPromise\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.DefaultChannelPipeline$TailContext\",\n" +
                "    \"methods\":[{\"name\":\"channelActive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelInactive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }, {\"name\":\"channelReadComplete\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelRegistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelUnregistered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelWritabilityChanged\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"exceptionCaught\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Throwable\"] }, {\"name\":\"userEventTriggered\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.SimpleChannelInboundHandler\",\n" +
                "    \"methods\":[{\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.channel.socket.nio.NioServerSocketChannel\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.handler.codec.MessageAggregator\",\n" +
                "    \"methods\":[{\"name\":\"channelInactive\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }, {\"name\":\"channelReadComplete\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.handler.codec.MessageToMessageDecoder\",\n" +
                "    \"methods\":[{\"name\":\"channelRead\",\"parameterTypes\":[\"io.netty.channel.ChannelHandlerContext\",\"java.lang.Object\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.handler.codec.http.HttpObjectAggregator\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.handler.codec.http.HttpServerCodec\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.AbstractReferenceCounted\",\n" +
                "    \"fields\":[{\"name\":\"refCnt\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.DefaultAttributeMap\",\n" +
                "    \"fields\":[{\"name\":\"attributes\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.Recycler$DefaultHandle\",\n" +
                "    \"fields\":[{\"name\":\"state\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.ReferenceCountUtil\",\n" +
                "    \"queryAllDeclaredMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.concurrent.DefaultPromise\",\n" +
                "    \"fields\":[{\"name\":\"result\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.concurrent.SingleThreadEventExecutor\",\n" +
                "    \"fields\":[{\"name\":\"state\"}, {\"name\":\"threadProperties\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueueColdProducerFields\",\n" +
                "    \"fields\":[{\"name\":\"producerLimit\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueueConsumerFields\",\n" +
                "    \"fields\":[{\"name\":\"consumerIndex\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.BaseMpscLinkedArrayQueueProducerFields\",\n" +
                "    \"fields\":[{\"name\":\"producerIndex\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.unpadded.MpscUnpaddedArrayQueueConsumerIndexField\",\n" +
                "    \"fields\":[{\"name\":\"consumerIndex\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.unpadded.MpscUnpaddedArrayQueueProducerIndexField\",\n" +
                "    \"fields\":[{\"name\":\"producerIndex\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.netty.util.internal.shaded.org.jctools.queues.unpadded.MpscUnpaddedArrayQueueProducerLimitField\",\n" +
                "    \"fields\":[{\"name\":\"producerLimit\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"io.opentelemetry.api.GlobalOpenTelemetry\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.beans.Transient\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.AutoCloseable\",\n" +
                "    \"methods\":[{\"name\":\"close\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.BaseVirtualThread\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Boolean\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Byte\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Character\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Deprecated\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Double\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Float\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Integer\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Long\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.ProcessHandle\",\n" +
                "    \"methods\":[{\"name\":\"current\",\"parameterTypes\":[] }, {\"name\":\"pid\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Short\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.StackTraceElement\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.String\",\n" +
                "    \"fields\":[{\"name\":\"COMPACT_STRINGS\"}, {\"name\":\"TYPE\"}, {\"name\":\"coder\"}, {\"name\":\"value\"}],\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"byte[]\",\"byte\"] }, {\"name\":\"coder\",\"parameterTypes\":[] }, {\"name\":\"isASCII\",\"parameterTypes\":[\"byte[]\"] }, {\"name\":\"value\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.StringCoding\",\n" +
                "    \"methods\":[{\"name\":\"hasNegatives\",\"parameterTypes\":[\"byte[]\",\"int\",\"int\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Thread\",\n" +
                "    \"fields\":[{\"name\":\"threadLocalRandomProbe\"}],\n" +
                "    \"methods\":[{\"name\":\"isVirtual\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.Void\",\n" +
                "    \"fields\":[{\"name\":\"TYPE\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.invoke.MethodHandles$Lookup\",\n" +
                "    \"fields\":[{\"name\":\"IMPL_LOOKUP\"}],\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.Class\",\"java.lang.Class\",\"int\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.BufferPoolMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.ClassLoadingMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.CompilationMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.LockInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.ManagementFactory\",\n" +
                "    \"methods\":[{\"name\":\"getRuntimeMXBean\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.ManagementPermission\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.MemoryMXBean\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getHeapMemoryUsage\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.MemoryManagerMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.MemoryPoolMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.MemoryUsage\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getCommitted\",\"parameterTypes\":[] }, {\"name\":\"getInit\",\"parameterTypes\":[] }, {\"name\":\"getMax\",\"parameterTypes\":[] }, {\"name\":\"getUsed\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.MonitorInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.PlatformLoggingMXBean\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getLoggerLevel\",\"parameterTypes\":[\"java.lang.String\"] }, {\"name\":\"getLoggerNames\",\"parameterTypes\":[] }, {\"name\":\"getParentLoggerName\",\"parameterTypes\":[\"java.lang.String\"] }, {\"name\":\"setLoggerLevel\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.RuntimeMXBean\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getInputArguments\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.lang.management.ThreadInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.math.BigDecimal\",\n" +
                "    \"fields\":[{\"name\":\"intCompact\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.math.BigInteger\",\n" +
                "    \"fields\":[{\"name\":\"mag\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.nio.Bits\",\n" +
                "    \"fields\":[{\"name\":\"MAX_MEMORY\"}, {\"name\":\"UNALIGNED\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.nio.Buffer\",\n" +
                "    \"fields\":[{\"name\":\"address\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.nio.ByteBuffer\",\n" +
                "    \"methods\":[{\"name\":\"alignedSlice\",\"parameterTypes\":[\"int\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.nio.DirectByteBuffer\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"long\",\"long\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.nio.channels.spi.SelectorProvider\",\n" +
                "    \"methods\":[{\"name\":\"openServerSocketChannel\",\"parameterTypes\":[\"java.net.ProtocolFamily\"] }, {\"name\":\"openSocketChannel\",\"parameterTypes\":[\"java.net.ProtocolFamily\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.rmi.dgc.Lease\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.rmi.dgc.VMID\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.rmi.server.ObjID\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.rmi.server.UID\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.security.SecureRandomParameters\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.sql.Date\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.sql.Timestamp\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.time.LocalDateTime\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.Collections$UnmodifiableCollection\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.Collections$UnmodifiableMap\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.Date\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.HashMap\",\n" +
                "    \"queryAllDeclaredConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.PropertyPermission\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.lang.String\",\"java.lang.String\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.concurrent.atomic.AtomicBoolean\",\n" +
                "    \"fields\":[{\"name\":\"value\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.concurrent.atomic.AtomicReference\",\n" +
                "    \"fields\":[{\"name\":\"value\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.concurrent.atomic.Striped64\",\n" +
                "    \"fields\":[{\"name\":\"base\"}, {\"name\":\"cellsBusy\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.concurrent.atomic.Striped64$Cell\",\n" +
                "    \"fields\":[{\"name\":\"value\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.logging.LogManager\",\n" +
                "    \"methods\":[{\"name\":\"getLoggingMXBean\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"java.util.logging.LoggingMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.MBeanOperationInfo\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getSignature\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.MBeanServerBuilder\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.NotificationEmitter\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.ObjectName\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.StandardEmitterMBean\",\n" +
                "    \"methods\":[{\"name\":\"cacheMBeanInfo\",\"parameterTypes\":[\"javax.management.MBeanInfo\"] }, {\"name\":\"getCachedMBeanInfo\",\"parameterTypes\":[] }, {\"name\":\"getMBeanInfo\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.openmbean.CompositeData\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.openmbean.OpenMBeanOperationInfoSupport\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.openmbean.TabularData\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIConnection\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"addNotificationListener\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"addNotificationListeners\",\"parameterTypes\":[\"javax.management.ObjectName[]\",\"java.rmi.MarshalledObject[]\",\"javax.security.auth.Subject[]\"] }, {\"name\":\"createMBean\",\"parameterTypes\":[\"java.lang.String\",\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"java.lang.String[]\",\"javax.security.auth.Subject\"] }, {\"name\":\"createMBean\",\"parameterTypes\":[\"java.lang.String\",\"javax.management.ObjectName\",\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"java.lang.String[]\",\"javax.security.auth.Subject\"] }, {\"name\":\"createMBean\",\"parameterTypes\":[\"java.lang.String\",\"javax.management.ObjectName\",\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"createMBean\",\"parameterTypes\":[\"java.lang.String\",\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"fetchNotifications\",\"parameterTypes\":[\"long\",\"int\",\"long\"] }, {\"name\":\"getAttribute\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.lang.String\",\"javax.security.auth.Subject\"] }, {\"name\":\"getAttributes\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.lang.String[]\",\"javax.security.auth.Subject\"] }, {\"name\":\"getConnectionId\",\"parameterTypes\":[] }, {\"name\":\"getDefaultDomain\",\"parameterTypes\":[\"javax.security.auth.Subject\"] }, {\"name\":\"getDomains\",\"parameterTypes\":[\"javax.security.auth.Subject\"] }, {\"name\":\"getMBeanCount\",\"parameterTypes\":[\"javax.security.auth.Subject\"] }, {\"name\":\"getMBeanInfo\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"getObjectInstance\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"invoke\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.lang.String\",\"java.rmi.MarshalledObject\",\"java.lang.String[]\",\"javax.security.auth.Subject\"] }, {\"name\":\"isInstanceOf\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.lang.String\",\"javax.security.auth.Subject\"] }, {\"name\":\"isRegistered\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"queryMBeans\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"queryNames\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"removeNotificationListener\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"removeNotificationListener\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }, {\"name\":\"removeNotificationListeners\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.lang.Integer[]\",\"javax.security.auth.Subject\"] }, {\"name\":\"setAttribute\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"setAttributes\",\"parameterTypes\":[\"javax.management.ObjectName\",\"java.rmi.MarshalledObject\",\"javax.security.auth.Subject\"] }, {\"name\":\"unregisterMBean\",\"parameterTypes\":[\"javax.management.ObjectName\",\"javax.security.auth.Subject\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIConnectionImpl_Skel\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIConnectionImpl_Stub\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.rmi.server.RemoteRef\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIServer\",\n" +
                "    \"queryAllPublicMethods\":true,\n" +
                "    \"methods\":[{\"name\":\"getVersion\",\"parameterTypes\":[] }, {\"name\":\"newClient\",\"parameterTypes\":[\"java.lang.Object\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIServerImpl_Skel\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.management.remote.rmi.RMIServerImpl_Stub\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.rmi.server.RemoteRef\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.sql.DataSource\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"javax.sql.RowSet\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.internal.misc.Unsafe\",\n" +
                "    \"methods\":[{\"name\":\"getUnsafe\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.ConfigurationInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.EventTypeInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.FlightRecorderMXBean\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.FlightRecorderMXBeanImpl\",\n" +
                "    \"queryAllPublicConstructors\":true,\n" +
                "    \"methods\":[{\"name\":\"cacheMBeanInfo\",\"parameterTypes\":[\"javax.management.MBeanInfo\"] }, {\"name\":\"getCachedMBeanInfo\",\"parameterTypes\":[] }, {\"name\":\"getMBeanInfo\",\"parameterTypes\":[] }, {\"name\":\"getNotificationInfo\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.RecordingInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"jdk.management.jfr.SettingDescriptorInfo\",\n" +
                "    \"queryAllPublicMethods\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"org.postgresql.Driver\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.ClassLoadingImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.CompilationImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.ManagementFactoryHelper$1\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.ManagementFactoryHelper$PlatformLoggingImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.MemoryImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.MemoryManagerImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.MemoryPoolImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.management.RuntimeImpl\",\n" +
                "    \"queryAllPublicConstructors\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.misc.Unsafe\",\n" +
                "    \"fields\":[{\"name\":\"theUnsafe\"}],\n" +
                "    \"methods\":[{\"name\":\"copyMemory\",\"parameterTypes\":[\"java.lang.Object\",\"long\",\"java.lang.Object\",\"long\",\"long\"] }, {\"name\":\"getAndAddLong\",\"parameterTypes\":[\"java.lang.Object\",\"long\",\"long\"] }, {\"name\":\"getAndSetObject\",\"parameterTypes\":[\"java.lang.Object\",\"long\",\"java.lang.Object\"] }, {\"name\":\"invokeCleaner\",\"parameterTypes\":[\"java.nio.ByteBuffer\"] }, {\"name\":\"storeFence\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.nio.ch.SelectorImpl\",\n" +
                "    \"fields\":[{\"name\":\"publicSelectedKeys\"}, {\"name\":\"selectedKeys\"}]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.rmi.registry.RegistryImpl_Stub\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.rmi.transport.DGCImpl_Skel\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.rmi.transport.DGCImpl_Stub\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.rmi.server.RemoteRef\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.security.provider.DRBG\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[\"java.security.SecureRandomParameters\"] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.security.provider.MD5\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\":\"sun.security.provider.SHA\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  },\n" + allData +
                "  {\n" +
                "    \"name\":\"sun.security.provider.SHA2$SHA256\",\n" +
                "    \"methods\":[{\"name\":\"<init>\",\"parameterTypes\":[] }]\n" +
                "  }\n" +
                "]\n";
        FileTools.save(new File(outConfigPath + "reflect-config.json"), reflect);

    }
}
