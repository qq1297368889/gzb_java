package gzb.test_spring_mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TestSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSpringMvcApplication.class, args);
    }

}
