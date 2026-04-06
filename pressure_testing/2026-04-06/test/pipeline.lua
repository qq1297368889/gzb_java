-- pipeline.lua
init = function(args)
   -- 获取流水线深度参数，例如 -- 32
   local depth = tonumber(args[1]) or 16
   local r = {}
   local path = wrk.path or "/"
   
   for i=1,depth do
      r[i] = wrk.format(nil, path)
   end
   req = table.concat(r)
end

request = function()
   return req
end

done = function(summary, latency, requests)
   print("\n" .. string.rep("-", 30))
   print(string.format("GZB-ONE 压测简报"))
   print(string.rep("-", 30))
   print(string.format("总请求数:  %10d", summary.requests))
   print(string.format("实际 QPS:  %10.2f", summary.requests / (summary.duration / 1000000)))
   print(string.format("平均延迟:  %10.2f ms", latency.mean / 1000))
   print(string.format("标准差:    %10.2f ms", latency.stdev / 1000))
   print(string.format("P50 延迟:  %10.2f ms", latency:percentile(50.0) / 1000))
   print(string.format("P99 延迟:  %10.2f ms", latency:percentile(99.0) / 1000))
   print(string.format("P99.9 延迟:%10.2f ms", latency:percentile(99.9) / 1000))
   print(string.format("最大延迟:  %10.2f ms", latency.max / 1000))
   print(string.format("错误数:    %10d", summary.errors.status + summary.errors.read + summary.errors.write + summary.errors.timeout))
   print(string.rep("-", 30))
end
