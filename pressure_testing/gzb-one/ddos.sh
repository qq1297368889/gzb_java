echo "###################### 1c"
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/text' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/text' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/text' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/text' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/text' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1

wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/hello' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/hello' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/hello' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/hello' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/hello' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/1' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/1' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/1' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/1' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/1' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/array/1?size=20' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/array/1?size=20' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/array/1?size=20' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/array/1?size=20' -- 64
wrk -t2 -c120 -d10s -s pipeline.lua 'http://127.0.0.1:8081/test/users/array/1?size=20' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1



wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/text'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/text'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/text'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/text'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/text'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/hello'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/hello'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/hello'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/hello'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/hello'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/1'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/1'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/1'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/1'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/1'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/array/1?size=20'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/array/1?size=20'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/array/1?size=20'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/array/1?size=20'
wrk -t2 -c120 -d10s 'http://127.0.0.1:8081/test/users/array/1?size=20'

echo "###################### 2c"


wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/text' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/text' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/text' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/text' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/text' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1

wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/hello' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/hello' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/hello' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/hello' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/hello' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/1' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/1' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/1' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/1' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/1' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/array/1?size=20' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/array/1?size=20' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/array/1?size=20' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/array/1?size=20' -- 64
wrk -t4 -c240 -d10s -s pipeline.lua 'http://127.0.0.1:8082/test/users/array/1?size=20' -- 64
ping -c 5 127.0.0.1 > /dev/null 2>&1



wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/text'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/text'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/text'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/text'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/text'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/hello'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/hello'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/hello'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/hello'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/hello'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/1'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/1'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/1'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/1'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/1'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/3?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/array/1?size=20'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/array/1?size=20'
ping -c 5 127.0.0.1 > /dev/null 2>&1
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/array/1?size=20'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/array/1?size=20'
wrk -t4 -c240 -d10s 'http://127.0.0.1:8082/test/users/array/1?size=20'
