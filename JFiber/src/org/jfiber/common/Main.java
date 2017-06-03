package org.jfiber.common;

public class Main {
	public static void main(String[] args) {
		new Fiber(new Task() {
			@Override
			public void execute() {
				System.out.println("fiber running");
			}
		}).start();
		
		System.out.println("program end!");
		FiberManager.getInstance().shutdown();
	}
}
