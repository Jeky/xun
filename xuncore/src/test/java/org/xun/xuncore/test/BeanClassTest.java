package org.xun.xuncore.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.xun.xuncore.reflect.BeanClass;
import org.xun.xuncore.reflect.BeanMethod;
import org.xun.xuncore.reflect.Decorating;
import org.xun.xuncore.reflect.Decorator;
import org.xun.xuncore.reflect.LocalVariables;

class TestTarget {
	
	@Decorating(TargetDecorator.class)
	public String foo() {
		return "";
	}

	public void bar() {

	}
}

public class BeanClassTest {
	
	@BeforeClass
	public void setup(){
		targetClass = new BeanClass<>(TestTarget.class);
	}
	
	@Test
	public void testGetAnnotation() {
		try {
			BeanMethod foo = targetClass.getMethod("foo");
			Decorating d = foo.getAnnotation(Decorating.class);
			assertArrayEquals(d.value(), new Class[]{TargetDecorator.class});
		} catch (NoSuchMethodException e) {
			fail(e.getMessage());
		}
	}

	private BeanClass<TestTarget> targetClass;
}
