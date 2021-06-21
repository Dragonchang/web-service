package org.springframework.test;

import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: zhangfl
 * @create: 2021-06-09 16:19
 **/
@Component
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
