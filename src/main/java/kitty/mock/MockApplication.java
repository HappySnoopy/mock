package kitty.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 提供http的mock挡板
 *
 * 有个小问题是，这个应用必须发布在根路径下。如http://localhost:8080/下，不能在后面加自己的项目路径
 *
 * 否则……可能会访问不到。除非配置Nginx、网关等加上项目路径才行。
 *
 * @author Lovely Kitty
 */
@SpringBootApplication
public class MockApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MockApplication.class, args);
	}


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MockApplication.class);
    }



}
