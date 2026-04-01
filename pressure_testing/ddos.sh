#!/bin/bash

# ================= 配置区 =================
DURATION="10s"
LUA_SCRIPT="pipeline.lua"
PIPELINE_DEPTH="64"
REPEAT_COUNT=6
COOL_DOWN=1

# 格式: "路径|备注|参数"
ENDPOINTS=(
    "/text|netty 性能基准|"
    "/test/hello|框架 性能基准|"
    "/test/users/1|序列化|"
    "/test/users/2|参数解析+序列化|?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99"
    "/test/users/3|参数实体+序列化|?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99"
    "/test/users/array/1|指定长度的序列化|?size=10"
)

# 测试模式 (线程数|连接数|端口|标识符)
# 这里的顺序决定了表格列的归属
MODES=(
    "2|120|8081|1c"
    "4|240|8082|2c"
)

LOG_FILE="raw_wrk_$(date +%Y%m%d_%H%M%S).log"
# ==========================================

# 存储结果的关联数组 (Key: PATH|MODE|TYPE)
declare -A RESULT_MAP

extract_qps() {
    echo "$1" | grep "Requests/sec:" | awk '{print $2}' | cut -d. -f1
}

echo "🚀 开始压测 (gzb one 模式)..."
echo "详细日志记录在: $LOG_FILE"

for ep_info in "${ENDPOINTS[@]}"; do
    IFS='|' read -r EP_PATH EP_NOTE EP_PARAMS <<< "$ep_info"

    for mode_info in "${MODES[@]}"; do
        IFS='|' read -r THREADS CONNS PORT MODE_ID <<< "$mode_info"

        for TYPE in "Pipeline" "Request"; do
            URL="http://127.0.0.1:${PORT}${EP_PATH}${EP_PARAMS}"
            max_qps=0

            echo -n "正在测试 [$MODE_ID] [$TYPE] $EP_PATH : "

            for ((i=1; i<=REPEAT_COUNT; i++)); do
                if [ "$TYPE" == "Pipeline" ]; then
                    OUT=$(wrk -t"$THREADS" -c"$CONNS" -d"$DURATION" -s "$LUA_SCRIPT" "$URL" -- "$PIPELINE_DEPTH")
                else
                    OUT=$(wrk -t"$THREADS" -c"$CONNS" -d"$DURATION" "$URL")
                fi

                qps=$(extract_qps "$OUT")
                [[ -n "$qps" && "$qps" -gt "$max_qps" ]] && max_qps=$qps

                echo -n "."
                echo -e "\n--- $EP_PATH | $MODE_ID | $TYPE | Iter $i ---\n$OUT" >> "$LOG_FILE"
                sleep $COOL_DOWN
            done

            RESULT_MAP["${EP_PATH}|${MODE_ID}|${TYPE}"]=$max_qps
            echo " [峰值: $max_qps]"
        done
    done
done

echo -e "\n\n### 性能数据汇总 (Markdown 格式)\n"

echo "| 测试端点 (Endpoint)       | 备注         | 1线程 QPS (Pipeline) | 1线程 QPS (Request) | 2线程 QPS (Pipeline) | 2线程 QPS (Request) |"
echo "|:----------------------|:-----------|:---------|:---------|:---------|:---------|"

for ep_info in "${ENDPOINTS[@]}"; do
    IFS='|' read -r EP_PATH EP_NOTE EP_PARAMS <<< "$ep_info"

    # 从 Map 中提取各列数据
    Q1_P=${RESULT_MAP["${EP_PATH}|1c|Pipeline"]}
    Q1_R=${RESULT_MAP["${EP_PATH}|1c|Request"]}
    Q2_P=${RESULT_MAP["${EP_PATH}|2c|Pipeline"]}
    Q2_R=${RESULT_MAP["${EP_PATH}|2c|Request"]}

    printf "| \`%-20s\` | %-10s | %-7s | %-7s | %-7s | %-7s |\n" \
        "$EP_PATH" "$EP_NOTE" "$Q1_P" "$Q1_R" "$Q2_P" "$Q2_R"
done

echo -e "\n压测结束。数据已根据 ${REPEAT_COUNT} 次循环取峰值。🏆"
