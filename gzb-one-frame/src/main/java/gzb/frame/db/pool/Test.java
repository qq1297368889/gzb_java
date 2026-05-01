package gzb.frame.db.pool;
import java.sql.*;
import java.util.LinkedList;
import java.util.Properties;

public class Test {
}

/**
 * Java原生JDBC连接MySQL工具类 - 带自动重连+轻量连接池
 * 适配Hugging Face Spaces、免费服务器，断连自动恢复，无需框架
 */
 class DBUtil {
    // 数据库配置 - 改这里即可
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/你的数据库名?useSSL=false&serverTimezone=UTC&allowMultiQueries=true&autoReconnect=true";
    private static final String DB_USER = "你的数据库账号";
    private static final String DB_PWD = "你的数据库密码";

    // 轻量连接池配置 (内存友好，按需调整)
    private static final int POOL_SIZE = 3;
    private static final LinkedList<Connection> connPool = new LinkedList<>();

    // 静态加载驱动+初始化连接池
    static {
        try {
            Class.forName(DRIVER_CLASS);
            // 初始化连接池
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = createConn();
                if (conn != null) {
                    connPool.add(conn);
                }
            }
            System.out.println("数据库连接池初始化完成，可用连接数：" + connPool.size());
        } catch (Exception e) {
            System.err.println("数据库驱动加载失败！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建单个数据库连接（核心：带超时配置，防止卡死）
     */
    private static Connection createConn() {
        try {
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PWD);
            props.setProperty("connectTimeout", "3000");  // 连接超时3秒
            props.setProperty("socketTimeout", "5000");   // 查询超时5秒
            props.setProperty("autoReconnect", "true");   // 开启自动重连
            props.setProperty("maxReconnects", "3");      // 最大重连次数3次
            return DriverManager.getConnection(DB_URL, props);
        } catch (SQLException e) {
            System.err.println("创建数据库连接失败，准备重试！" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取连接 - 核心：连接失效自动重建
     */
    public static Connection getConnection() {
        try {
            synchronized (connPool) {
                while (connPool.isEmpty()) {
                    // 连接池空了，创建新连接
                    Connection conn = createConn();
                    if (conn != null) {
                        connPool.add(conn);
                    } else {
                        Thread.sleep(500); // 创建失败，休眠重试
                    }
                }
                // 取出连接，校验是否有效
                Connection conn = connPool.removeFirst();
                if (conn == null || conn.isClosed() || !conn.isValid(2)) {
                    System.err.println("发现无效连接，自动重建！");
                    conn = createConn();
                }
                return conn;
            }
        } catch (Exception e) {
            System.err.println("获取数据库连接失败！" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 归还连接到连接池
     */
    public static void closeConn(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    synchronized (connPool) {
                        connPool.addLast(conn);
                    }
                }
            } catch (SQLException e) {
                System.err.println("归还连接失败，连接已失效！");
            }
        }
    }

    // 重载关闭方法 - 增删改时用
    public static void closeConn(Connection conn, PreparedStatement pstmt) {
        closeConn(conn, pstmt, null);
    }

    /**
     * 测试数据库连接是否正常 - 心跳检测用
     */
    public static boolean testConn() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("心跳检测：数据库连接异常，自动重连中！");
            return false;
        } finally {
            closeConn(conn, null);
        }
    }
}