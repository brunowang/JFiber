package org.jfiber.common;

import com.lmax.disruptor.EventFactory;

public class FiberFactory implements EventFactory<Fiber>{

	@Override
	public Fiber newInstance() {
		return new Fiber();
	}

}
