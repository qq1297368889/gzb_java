package gzb.start;

import gzb.frame.annotation.*;
import gzb.frame.factory.ContentType;
import gzb.tools.Config;
import gzb.tools.log.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("test")
@Header(item = {@HeaderItem(key = "Content-Type", val = "application/json")})
public class Test {

    public static final Users users = new Users();

    static {
        users.setUsersId(8215438L);
        users.setUsersName("vpeGb2SNo8Xk");
        users.setUsersPassword("az1Amb2aVJsP");
        users.setUsersEmail("nWV3qO6qW0zZ@gzb.com");
        users.setUsersTime(LocalDateTime.now());
        users.setUsersAge(12);
    }

    public static final byte[] HELLO_WORD = "Hello, World!".getBytes(Config.encoding);
    /// 测试基本性能
    @EventLoop
    @RequestMapping("hello")
    @Header(item = {@HeaderItem(key = "Content-Type", val = "text/html")})
    public Object hello() {

        return HELLO_WORD;
    }

    /// 测试传参性能  + 序列化性能
    @EventLoop
    @RequestMapping("users/1")
    public Object users() {
        return users;
    }

    @EventLoop
    @RequestMapping("users/2")
    public Object users(Long usersId, String usersName, String usersPassword, String usersEmail, Integer usersAge) {
        return new Users(
                usersId == null ? 8215438L : usersId,
                usersName == null ?"vpeGb2SNo8Xk" : usersName,
                usersPassword == null ? "az1Amb2aVJsP": usersPassword,
                usersEmail == null ? "nWV3qO6qW0zZ@gzb.com" : usersEmail,
                usersAge == null ? 12: usersAge,
                null //not time
        );
    }

    @EventLoop
    @RequestMapping("users/3")
    public Object users(Users users) {
        return users;
    }

    /// 序列化性能
    @EventLoop
    @RequestMapping("users/array/1")
    public Object usersArray1(int size) {
        List<Users> usersList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            usersList.add(users);
        }
        return usersList;
    }


}
