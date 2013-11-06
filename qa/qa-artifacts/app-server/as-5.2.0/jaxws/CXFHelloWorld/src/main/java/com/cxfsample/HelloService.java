package com.cxfsample;

public class HelloService implements IHello {

	@Override
	public String sayHello(String name) {
		return "Hello " + name + "!!!";
	}
}
