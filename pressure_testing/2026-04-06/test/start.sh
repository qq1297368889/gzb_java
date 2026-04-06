#!/bin/bash

# 采样与计算核心方法
monitor_resource() {
    local pid=$1
    local duration=$2
    local stage=$3
    
    sleep 1
    local sample_seconds=$((duration - 2))
    
    if [ -z "$pid" ] || [ $sample_seconds -le 0 ]; then
        echo "-|-|-|-|-|-" > "/tmp/res_${stage}.tmp"
        return
    fi

    local cpu_log=()
    local mem_log=()

    for ((i=0; i<sample_seconds; i++)); do
        local line=$(top -b -n 1 -p "$pid" | grep "$pid" | tail -n 1)
        if [ -n "$line" ]; then
            local cpu=$(echo "$line" | awk '{print $9}')
            local mem=$(echo "$line" | awk '{print $6}')
            
            local mem_kb=0
            if [[ $mem == *g ]]; then
                mem_kb=$(echo "${mem%g} * 1024 * 1024" | bc)
            elif [[ $mem == *m ]]; then
                mem_kb=$(echo "${mem%m} * 1024" | bc)
            else
                mem_kb=$mem
            fi

            cpu_log+=("$cpu")
            mem_log+=("$mem_kb")
        fi
        sleep 1
    done

    calc() {
        local arr=("${@}")
        if [ ${#arr[@]} -eq 0 ]; then echo "-|-|-"; return; fi
        local min=$(printf "%s\n" "${arr[@]}" | sort -n | head -n 1)
        local max=$(printf "%s\n" "${arr[@]}" | sort -n | tail -n 1)
        local avg=$(printf "%s\n" "${arr[@]}" | awk '{sum+=$1} END {printf "%.1f", sum/NR}')
        echo "$min|$max|$avg"
    }

    local cpu_stat=$(calc "${cpu_log[@]}")
    local mem_stat=$(calc "${mem_log[@]}")
    echo "$cpu_stat|$mem_stat" > "/tmp/res_${stage}.tmp"
}

# 核心入口方法
start() {
    local url=$1
    local target_qps=$2
    local threads=$3
    local connections=$4
    local decay_list=(${5//,/ })
    local time_list=(${6//,/ })
    local cores=$7
    local is_warmup=$8

    local port=$(echo "$url" | awk -F'[:/]' '{for(i=1;i<=NF;i++) if($i ~ /^[0-9]+$/) {print $i; exit}}')
    local pid=$(sudo lsof -t -i:$port | head -n 1)
    
    [ -z "$pid" ] && echo "警告: 端口 $port 未找到对应进程" >&2

    local table_rows=""

    # 封装执行单个步骤的函数
    # 返回格式: "实际QPS|Markdown行数据"
    execute_step() {
        local name=$1
        local dur=$2
        local qps=$3
        
        monitor_resource "$pid" "$dur" "$name" &
        local monitor_pid=$!

        local wrk_cmd="taskset -c $cores wrk2 -t$threads -c$connections -d${dur}s -R$qps '$url' --latency"
       
        local output=$(eval "$wrk_cmd" 2>/dev/null)
        wait $monitor_pid
    	sleep 3
        
        IFS='|' read -r c_min c_max c_avg m_min m_max m_avg < "/tmp/res_${name}.tmp"
        fmt_m() { [[ "$1" == "-" ]] && echo "-" || echo "$(echo "scale=1; $1/1024" | bc)MB"; }

        local act_qps=$(echo "$output" | grep "Requests/sec:" | awk '{print $2}')
        local p50=$(echo "$output" | grep "50.000%" | awk '{print $2}')
        local p99=$(echo "$output" | grep "99.000%" | awk '{print $2}')
        local p999=$(echo "$output" | grep "99.900%" | awk '{print $2}')

        local row="| $name | ${dur}s | $qps | ${act_qps:-0} | $p50 | $p99 | $p999 | $c_min / $c_max / $c_avg | $(fmt_m $m_min) / $(fmt_m $m_max) / $(fmt_m $m_avg) |"
        
        echo "${act_qps:-0}|${row}"
        
    }

sec_wu=60
    # --- 1. 热身 ---
    if [ "$is_warmup" = "true" ]; then
        echo ">>> 正在热身 (${sec_wu}s)..." >&2
        res=$(execute_step "Warmup" ${sec_wu} "88888")
        table_rows+="${res#*|}\n"
    fi 
    # --- 2. 极限压测 (Max_Load) ---
    res=$(execute_step "Max_Load" 10 "$target_qps")
    echo ">>> 寻找极限吞吐量 (10s)..." >&2
    res=$(execute_step "Max_Load" 10 "$target_qps")
    actual_max_qps=${res%%|*}
    table_rows+="${res#*|}\n"
    
    local base_qps=$(echo "$actual_max_qps" | awk '{printf "%.0f", $1}')

    # --- 3. 衰减压测 ---
    for i in "${!decay_list[@]}"; do
        local rate=${decay_list[$i]}
        local duration=${time_list[$i]:-30}
        local current_target=$(echo "$base_qps * $rate" | bc | cut -d'.' -f1)
        
        echo ">>> 阶梯负载压测: ${rate} (目标: $current_target)..." >&2
        res=$(execute_step "${rate}_Load" "$duration" "$current_target")
        table_rows+="${res#*|}\n" 
    done
# --- 固定低压 稳定性测试
        echo ">>> 低压稳定性测试(目标: 20000)..." >&2
        res=$(execute_step "低压测试" "60" "20000")
        table_rows+="${res#*|}\n" 
    # 最终输出
    echo -e "\n### 性能压测报告汇总"
    echo -e "| 阶段 | 时长 | 目标QPS | 实际QPS | P50 | P99 | P99.9 | CPU (Min / Max / Avg) | 内存 (Min / Max / Avg) |"
    echo -e "| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |"
    echo -e "$table_rows"
}
  
   
ulimit -n 1048576

# 定义框架与端口映射
declare -A FW_PORTS=( ["gzb-one"]=8081 ["spring-webflux"]=8082 ["quarkus"]=8083 )
FRAMEWORKS=("gzb-one" "spring-webflux" "quarkus")

# 定义压测路径
urls=(
    "/test/hello"
    "/test/users/1"
    "/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99"
    "/test/users/array/1?size=10"
)

# 清理指定端口的进程函数
cleanup_ports() {
    local ports=("$@")
    echo ">>> [System] 正在强制清理端口: ${ports[*]}" >&2
    for port in "${ports[@]}"; do
        # 查找监听该端口的 PID 并清理
        local pids=$(sudo lsof -t -i:"$port")
        if [ -n "$pids" ]; then
            echo "$pids" | xargs sudo kill -9
        fi
    done
    sleep 2
}

# 等待端口启动完成函数
wait_for_port() {
    local port=$1
    local timeout=60
    local elapsed=0
    echo ">>> [System] 等待端口 $port 响应..." >&2
    while ! nc -z 127.0.0.1 "$port" >/dev/null 2>&1; do
        sleep 1
        ((elapsed++))
        if [ $elapsed -ge $timeout ]; then
            echo "Error: 端口 $port 启动超时，跳过。" >&2
            return 1
        fi
    done
    return 0
}

# 自动化执行流
for fw in "${FRAMEWORKS[@]}"; do
    current_port=${FW_PORTS[$fw]}
    
    echo -e "\n\n********************************************************"
    echo ">>> [Framework Start] 准备测试框架: $fw (端口: $current_port)"
    echo "********************************************************"

    # 1. 环境清理 (启动前先清空 8081-8083，确保环境纯净)
    cleanup_ports 8081 8082 8083

    # 2. 启动服务器 (使用子 Shell 解决当前目录依赖问题)
    if [ -d "$fw" ] && [ -f "$fw/start.sh" ]; then
        echo ">>> [System] 进入目录 $fw 并执行 start.sh ..." >&2
        (cd "$fw" && sh start.sh) > /dev/null 2>&1 &
    else
        echo "Error: 找不到目录 $fw 或启动脚本 $fw/start.sh" >&2
        continue
    fi

    # 3. 等待启动完成并静置
    if wait_for_port "$current_port"; then
        echo ">>> [System] 服务已就绪，静置 5s 避震..." >&2
        sleep 5

        # 4. 遍历所有 URL 路径进行压测
        for url_path in "${urls[@]}"; do
            echo -e "\n--------------------------------------------------------"
            echo ">>> [Target] $fw -> $url_path"
            echo "--------------------------------------------------------"
 
            start \
                "http://127.0.0.1:${current_port}${url_path}" \
                500000 \
                9 \
                900 \
                "0.9,0.8,0.7,0.1" \
                "60,60,60,60" \
                "6-11" \
                "true"
        done
    fi

    # 5. 该框架所有 URL 测试完毕，清理端口，准备下一个框架
    echo ">>> [System] $fw 所有路径测试完成，正在清理进程..." >&2
    cleanup_ports "$current_port"
done

echo -e "\n>>> [Finish] 所有框架测试任务已完成。"
