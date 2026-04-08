package gzb.frame.language;

public class Template {
    public static void main(String[] args) {
        for (int i = 16; i < 100; i++) {
            System.out.println("        zh_language[" + i + "]=\"def_val\";");
        }
    }

    public static String[] THIS_LANGUAGE = null;
    public static String[] zh_language = new String[100];
    public static String[] en_language = new String[100];

    static {
        zh_language[0] = "数据库";
        zh_language[1] = "配置信息";
        zh_language[2] = "连接成功";
        zh_language[3] = "数据表信息抓取成功";
        zh_language[4] = "异步服务启动成功";
        zh_language[5] = "后台异步线程数";
        zh_language[6] = "连接失败";
        zh_language[7] = "储存对象";
        zh_language[8] = "编译耗时";
        zh_language[9] = "无需编译";
        zh_language[10] = "异步批量执行SQL";
        zh_language[11] = "条";
        zh_language[12] = "耗时";
        zh_language[13] = "毫秒";
        zh_language[14] = "异步执行成功,但回调失败，原因是回调函数抛出错误";
        zh_language[15] = "异步执行成功,但回调失败，原因是回调队列满了";
        zh_language[16] = "关闭连接 无事物";
        zh_language[17] = "关闭连接 真实事务";
        zh_language[18] = "关闭连接 模拟事务";
        zh_language[19] = "关闭连接 数据连接为空跳过";
        zh_language[20] = "获取连接耗时";
        zh_language[21] = "数据库执行耗时";
        zh_language[22] = "框架组装耗时";
        zh_language[23] = "全流程耗时";
        zh_language[24] = "执行进度";
        zh_language[25] = "影响行数";
        zh_language[26] = "执行数量";
        zh_language[27] = "事务开启时不支持异步执行，会打破事务原子性，如需异步请关闭事务";
        zh_language[28] = "运行时错误";
        zh_language[29] = "SQL执行失败";
        zh_language[30] = "收集SQL";
        zh_language[31] = "的事务中调用了数据库";
        zh_language[32] = "回滚事务 无事物";
        zh_language[33] = "回滚事务 真实事务";
        zh_language[34] = "回滚事务 模拟事务";
        zh_language[35] = "提交事务 无事物";
        zh_language[36] = "提交事务 真实事务";
        zh_language[37] = "提交事务 转化真实事务";
        zh_language[38] = "关闭事务";
        zh_language[39] = "开启事物";
        zh_language[40] = "模拟事务";
        zh_language[41] = "真实事务";
        zh_language[42] = "事务已被开启，无法开启多次事务，如需要多次事务请改为多段事务，而不是同时开启多个事务\n" +
                "1.开启事务 openTransaction\n" +
                "2.提交事务 commit\n" +
                "3.回滚事务 rollback\n" +
                "4.关闭事务 endTransaction 如果手动开启事务那么必须调用本方法，框架开启 无需手动调用\n";
        zh_language[43] = "外部 指定连接";
        zh_language[44] = "获取连接 新的";
        zh_language[45] = "事务类型异常";
        zh_language[46] = "获取连接 缓存";
        zh_language[47] = "数据库类型不正确";
        zh_language[48] = "转换为数据库类型失败";
        zh_language[49] = "转换的值为";
        zh_language[50] = "类型为";
        zh_language[51] = "线程池不自动扩容";
        zh_language[52] = "积压数量";
        zh_language[53] = "线程数量";
        zh_language[54] = "处理器核心";
        zh_language[55] = "小于线程数";
        zh_language[56] = "线程池缩容";
        zh_language[57] = "启动线程";
        zh_language[58] = "停止恶化";
        zh_language[59] = "积压小于线程数";
        zh_language[60] = "快速扩张";
        zh_language[61] = "处理器使用率预估";
        zh_language[62] = "遵从缩容指令";
        zh_language[63] = "空闲线程结束";
        zh_language[64] = "线程被中断";
        zh_language[65] = "线程运行出错";
        zh_language[66] = "主线程数量";
        zh_language[67] = "IO线程数量";
        zh_language[68] = "业务线程数量";
        zh_language[69] = "业务队列上限";
        zh_language[70] = "服务端口";
        zh_language[71] = "自动扩容";
        zh_language[72] = "HTTP服务器";
        zh_language[73] = "储存对象";
        zh_language[74] = "开始加载类";
        zh_language[75] = "结束加载类";
        zh_language[76] = "即将加载-单个类";
        zh_language[77] = "加载完毕-单个类";
        zh_language[78] = "添加映射端点";
        zh_language[79] = "启动总耗时";
        zh_language[80] = "初始化耗时";
        zh_language[81] = "映射端点 异常替换";
        zh_language[82] = "替换为";
        zh_language[83] = "类字段注入";
        zh_language[84] = "注入到对象";
        zh_language[85] = "的类变量";
        zh_language[86] = "类变量的类型为";
        zh_language[87] = "注册数据库事件";
        zh_language[88] = "注册方法";
        zh_language[89] = "获取Windows CPU负载失败";
        zh_language[90] = "def_val";
        zh_language[91] = "def_val";
        zh_language[92] = "def_val";
        zh_language[93] = "def_val";
        zh_language[94] = "def_val";
        zh_language[95] = "def_val";
        zh_language[96] = "def_val";
        zh_language[97] = "def_val";
        zh_language[98] = "def_val";
        zh_language[99] = "def_val";

    }

    static {
        en_language[0] = "Database";
        en_language[1] = "Config";
        en_language[2] = "Connected successfully";
        en_language[3] = "Table metadata loaded";
        en_language[4] = "Async services started";
        en_language[5] = "Async worker threads";
        en_language[6] = "Connection failed";
        en_language[7] = "Store object";
        en_language[8] = "Compilation cost";
        en_language[9] = "No compilation needed";
        en_language[10] = "Async batch SQL execution";
        en_language[11] = "records";
        en_language[12] = "cost";
        en_language[13] = "ms";
        en_language[14] = "Async exec success, but callback failed: Exception in callback function";
        en_language[15] = "Async exec success, but callback failed: Callback queue is full";
        en_language[16] = "Close connection (No transaction)";
        en_language[17] = "Close connection (Real transaction)";
        en_language[18] = "Close connection (Simulated transaction)";
        en_language[19] = "Close connection skipped: Connection is null";
        en_language[20] = "Acquire connection cost";
        en_language[21] = "DB execution cost";
        en_language[22] = "Framework assembly cost";
        en_language[23] = "Total workflow cost";
        en_language[24] = "Execution progress";
        en_language[25] = "Rows affected";
        en_language[26] = "Execution count";
        en_language[27] = "Async execution is not supported when transaction is open as it breaks atomicity. Please close transaction for async tasks.";
        en_language[28] = "Runtime error";
        en_language[29] = "SQL execution failed";
        en_language[30] = "Collecting SQL";
        en_language[31] = "called database within a transaction";
        en_language[32] = "Rollback (No transaction)";
        en_language[33] = "Rollback (Real transaction)";
        en_language[34] = "Rollback (Simulated transaction)";
        en_language[35] = "Commit (No transaction)";
        en_language[36] = "Commit (Real transaction)";
        en_language[37] = "Commit (Convert to real transaction)";
        en_language[38] = "End transaction";
        en_language[39] = "Open transaction";
        en_language[40] = "Simulated transaction";
        en_language[41] = "Real transaction";
        en_language[42] = "Transaction already active. Multiple nested transactions are not supported. Use sequential transactions instead.\n" +
                "1. Open: openTransaction\n" +
                "2. Commit: commit\n" +
                "3. Rollback: rollback\n" +
                "4. End: endTransaction (Required for manual transactions; handled automatically by framework otherwise)\n";
        en_language[43] = "External specified connection";
        en_language[44] = "Acquire connection (New)";
        en_language[45] = "Invalid transaction type";
        en_language[46] = "Acquire connection (Cache)";
        en_language[47] = "Incorrect database type";
        en_language[48] = "Failed to convert to database type";
        en_language[49] = "Value to convert";
        en_language[50] = "Type is";
        en_language[51] = "ThreadPool auto-scaling disabled";
        en_language[52] = "Backlog count";
        en_language[53] = "Threads count";
        en_language[54] = "CPU cores";
        en_language[55] = "less than thread count";
        en_language[56] = "ThreadPool scaling down";
        en_language[57] = "Start thread";
        en_language[58] = "Stop deterioration";
        en_language[59] = "Backlog less than thread count";
        en_language[60] = "Rapid expansion";
        en_language[61] = "Estimated CPU usage";
        en_language[62] = "Following scale-down instruction";
        en_language[63] = "Idle thread terminated";
        en_language[64] = "Thread interrupted";
        en_language[65] = "Thread execution error";
        en_language[66] = "Main threads";
        en_language[67] = "IO threads";
        en_language[68] = "Biz threads";
        en_language[69] = "Biz queue limit";
        en_language[70] = "Server port";
        en_language[71] = "Auto-scaling";
        en_language[72] = "HTTP Server";
        en_language[73] = "Store object";
        en_language[74] = "Start loading classes";
        en_language[75] = "Finished loading classes";
        en_language[76] = "Ready to load - Single class";
        en_language[77] = "Load complete - Single class";
        en_language[78] = "Add mapping endpoint";
        en_language[79] = "Startup completed in";
        en_language[80] = "Initialization time";
        en_language[81] = "Mapping endpoint exception replacement";
        en_language[82] = "Replaced with";
        en_language[83] = "Class field injection";
        en_language[84] = "Injected into object";
        en_language[85] = "field";
        en_language[86] = "Field type is";
        en_language[87] = "Register database event";
        en_language[88] = "Register method";
        en_language[89] = "Failed to obtain Windows CPU load";
    }
    static {
        boolean isZh = java.util.Locale.getDefault().getLanguage().equals("zh");
        if (isZh){
            useChinese();
        }else{
            useEnglish();
        }
    }
    public static void useEnglish(){
        specified(en_language);
    }
    public static void useChinese(){
        specified(zh_language);
    }
    public static void specified(String[]language){
        if (language==null) {
            throw new NullPointerException("language is null");
        }
        if (language.length<zh_language.length) {
            throw new ArrayIndexOutOfBoundsException("language length is less than zh_language.length");
        }
        THIS_LANGUAGE=language;
        for (int i = 0; i < THIS_LANGUAGE.length; i++) {
            if (THIS_LANGUAGE[i]==null || THIS_LANGUAGE[i].trim().isEmpty() || "def_val".equals(THIS_LANGUAGE[i])) {
                THIS_LANGUAGE[i]="Text missing，index"+i;
            }
        }
    }
}
