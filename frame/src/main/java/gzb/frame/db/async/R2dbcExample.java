package gzb.frame.db.async;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class R2dbcExample {

    // 使用 Builder 模式配置数据库连接参数
    static PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
            // 必需参数
            .host("localhost")            // 数据库服务器地址
            .port(5432)                   // 数据库端口（PostgreSQL 默认是 5432）
            .database("frame") // 要连接的数据库名称
            .username("postgres")    // 数据库用户名
            .password("postgres")    // 数据库密码

            // 可选参数（根据需要添加）
            // .schema("public")          // 默认 schema
            // .connectTimeout(Duration.ofSeconds(3)) // 连接超时时间
            .build();
    static ConnectionFactory connectionFactory = new PostgresqlConnectionFactory(configuration);

    /**
     * 执行带参数的 SQL 查询，并返回结果列表。
     *
     * @param sql    带有占位符 (如 $1, $2, ...) 的 SQL 语句。
     * @param params SQL 语句中占位符对应的值数组。
     * @return 包含查询结果的 Mono，结果是一个 List<Map<String, Object>>，其中每个 Map 代表一行记录。
     */
    public static Mono<List<Map<String, Object>>> queryForList(String sql, Object[] params) {
        // 1. 获取连接的 Mono
        Mono<Connection> connectionMono = Mono.from(connectionFactory.create());

        // 定义执行查询的逻辑
        Function<Connection, Mono<List<Map<String, Object>>>> queryFunction = connection -> {
            try {
                // 2. 创建 Statement
                Statement statement = connection.createStatement(sql);

                // 3. 绑定参数
                for (int i = 0; i < params.length; i++) {
                    // R2DBC 的 bind 方法使用 0-based 索引，对应 SQL 中的 $1, $2, ...
                    if (params[i] != null) {
                        // 绑定非空值
                        statement.bind(i, params[i]);
                    } else {
                        // 绑定空值，需要提供类型信息
                        // 此处使用 Object.class 作为通用类型，实际项目中最好提供精确类型
                        statement.bindNull(i, Object.class);
                    }
                }
                // 4. 执行查询并映射结果
                return Flux.from(statement.execute())
                        // flatMap 处理所有的 Result 对象（例如，存储过程可能返回多个结果集）
                        .flatMap((result) -> {
                                    System.out.println(result);
                                    System.out.println(result.getClass());
                                    return result.map(R2dbcExample::mapRowToMap);
                                }
                        )
                        // 将所有映射后的行 (Map) 收集到一个 List 中
                        .collectList();
            } catch (R2dbcException e) {
                // 捕获 R2DBC 相关的异常
                return Mono.error(e);
            }
        };

        // 定义资源释放（关闭连接）的逻辑
        Function<Connection, Mono<Void>> cleanupFunction = connection -> Mono.from(connection.close());

        // 5. 使用 Mono.usingWhen 确保连接在查询完成后无论成功或失败都会被关闭
        return Mono.usingWhen(
                connectionMono, // 获取资源
                queryFunction,  // 使用资源 (执行查询)
                cleanupFunction // 释放资源 (关闭连接)
        );
    }

    /**
     * 辅助方法：将 R2DBC 的 Row 对象映射为 Java 的 Map<String, Object>。
     *
     * @param row      当前行数据。
     * @param metadata 当前行的元数据。
     * @return 包含列名和对应值的 Map。
     */
    private static Map<String, Object> mapRowToMap(Row row, RowMetadata metadata) {
        Map<String, Object> rowMap = new HashMap<>();
        // 遍历所有列元数据，获取列名和对应的值
        for (ColumnMetadata column : metadata.getColumnMetadatas()) {
            String columnName = column.getName();
            // 通过列名获取值，确保类型安全
            Object value = row.get(columnName);
            rowMap.put(columnName, value);
        }
        return rowMap;
    }

    // 示例用法（在实际应用中，您会在 Spring WebFlux 等框架中使用它）
    public static void main(String[] args) {
        // 假设您的数据库中有一个 user 表
        String sql = "SELECT * from sys_users";
        Object[] params = {};

        System.out.println("开始执行异步查询...");

        // 订阅 Mono，处理查询结果
        queryForList(sql, params)
                .doOnSuccess(list -> {
                    System.out.println("查询成功，结果列表大小: " + list.size());
                    list.forEach(row -> System.out.println(row));
                })
                .doOnError(e -> {
                    System.err.println("查询发生错误: " + e.getMessage());
                })
                // 阻塞等待结果（在实际响应式应用中应该避免使用 block()）
                .block();
    }
}
