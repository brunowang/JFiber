package org.jfiber.common;

public class Fiber {
	private Task task;
	public Fiber() {}
	public Fiber(Task task) {
		this.task = task;
	}
	
	public void start() {
		FiberManager.getInstance().addTask(task);
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
}
