package org.springframework.test;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @program: spring
 * @description:
 * @author: zhangfl
 * @create: 2021-06-09 15:45
 **/
@Component
public class TestMain {
	@Autowired
	public  MyTestBean myTestBean;

	@Autowired
	public ApplicationContext applicationContext;

	public static void main(String[] args) {
		System.out.println("======test start======");
//		TestMain testMain = new TestMain();
//		System.out.println(testMain.testBean.getName());

//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
//		applicationContext.addBeanFactoryPostProcessor((CustomBeanFactoryPostProcessor)applicationContext.getBean("customBeanFactoryPostProcessor"));
//		MyTestBean myTestBean = (MyTestBean) applicationContext.getBean("myTestBean");
//		System.out.println(myTestBean.getName());

		MyTestGenericApplicationContext myTestGenericApplicationContext = new MyTestGenericApplicationContext();
		MyTestBean myTestBean = (MyTestBean)myTestGenericApplicationContext.getBean("myTestBean");
//		GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
//		genericApplicationContext.refresh();
//		Object myTestBean = genericApplicationContext.getBean("TestMain");
//		System.out.println("test");
	}
}
