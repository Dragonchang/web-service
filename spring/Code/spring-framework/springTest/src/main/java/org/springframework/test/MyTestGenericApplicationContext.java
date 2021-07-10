package org.springframework.test;

import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @program: spring
 * @description:
 * @author: zhangfl
 * @create: 2021-07-09 16:43
 **/
public class MyTestGenericApplicationContext extends GenericApplicationContext {

	private final AnnotatedBeanDefinitionReader reader;

	private final ClassPathBeanDefinitionScanner scanner;

	private final Set<Class<?>> annotatedClasses = new LinkedHashSet<>();

	private String[] basePackages;

	public MyTestGenericApplicationContext() {
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
		refresh();
	}
}
