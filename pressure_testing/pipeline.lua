-- pipeline.lua
init = function(args)
   -- 获取流水线深度参数，例如 -- 32
   local depth = tonumber(args[1]) or 16
   local r = {}
   
   -- 重要：wrk.path 会自动获取命令行中 http://.../xxx 的路径部分
   local path = wrk.path or "/"
   
   for i=1,depth do
      -- 动态使用命令行传入的 path
      r[i] = wrk.format(nil, path)
   end
   
   -- 将多个请求拼接成一个大的 TCP 发送块
   req = table.concat(r)
end

request = function()
   return req
end
