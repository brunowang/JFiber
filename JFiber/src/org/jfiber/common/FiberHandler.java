package org.jfiber.common;

import com.lmax.disruptor.EventHandler;

public class FiberHandler implements EventHandler<Fiber> {

	@Override
	public void onEvent(Fiber fiber, long sequence, boolean endOfBatch) throws Exception {
		assert(fiber != null);
		if (fiber.getTask() == null) {
			System.out.println("this fiber has no task, skip");
			return;
		}
		fiber.getTask().execute();
	}

}
