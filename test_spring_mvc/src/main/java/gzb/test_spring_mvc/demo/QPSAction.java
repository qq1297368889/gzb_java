package gzb.test_spring_mvc.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QPSAction {

    // http://127.0.0.1:8080/test/test1?name=lisi
    @RequestMapping("/test/test1")
    @ResponseBody
    public String test1(String name) {
        return "{\"code\":\"1\"\"name\":\""+name+"\"}";
    }
}
