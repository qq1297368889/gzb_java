/*
package gzb.frame.db;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
//数据库瓶颈不在发包速度 异步会引入很多负面因素 缓存解决数据库瓶颈才是王道  废弃
public class VertxDbTest {
    public static void main(String[] args) throws InterruptedException {
        io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("127.0.0.1")
                .setDatabase("frame")
                .setUser("root")
                .setPassword("root");
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        MySQLPool client = MySQLPool.pool(vertx, connectOptions, poolOptions);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            client.query("select sys_users_id from sys_users limit 1") // 建议先用最简单的 SQL 测试
                    .execute()
                    .onComplete(ar -> {
                        if (ar.succeeded()) {
                            for (Row row : ar.result()) {
                                // 确认这里是否打印
                                System.out.println("收到 ID: " + row.getLong(0));
                            }
                        } else {
                            System.err.println("失败: " + ar.cause().getMessage());
                        }
                        latch.countDown();
                    });
        }
        if (!latch.await(10, java.util.concurrent.TimeUnit.SECONDS)) {
            System.err.println("超时");
        }

        client.close();
        vertx.close();
    }
}*/
