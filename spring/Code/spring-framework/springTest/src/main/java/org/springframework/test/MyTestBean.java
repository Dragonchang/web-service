package org.springframework.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: zhangfl
 * @create: 2021-06-09 16:19
 **/
@Component
@Configuration
@Order(3)
public class MyTestBean {
	private String name = "ChenHao";

	public MyTestBean(String name) {
		this.name = name;
	}

	public MyTestBean() {
	}

	@Override
	public String toString() {
		return "MyTestBean{" +
				"name='" + name + '\'' +
				'}';
	}
	@Bean
	public String testBean() {
		return "";
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
