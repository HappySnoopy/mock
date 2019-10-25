package net.loyintea.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 提供http的mock挡板
 *
 * 有个小问题是，这个应用必须发布在根路径下。即http://localhost:8080/下，不能在后面加自己的项目路径
 *
 * 否则……可能会访问不到。除非配置Nginx、网关等加上项目路径才行。
 */
@SpringBootApplication
public class MockApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockApplication.class, args);
	}

}
