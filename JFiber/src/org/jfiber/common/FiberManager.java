package org.jfiber.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class FiberManager {
	private static volatile FiberManager instance;
	private FiberManager() {}
	public static FiberManager getInstance() {
		if (instance == null) {
			synchronized (FiberManager.class) {
				if (instance == null) {
					instance = new FiberManager();
					instance.init();
				}
			}
		}
		return instance;
	}
	
	ExecutorService  executor = Executors.newSingleThreadExecutor();
	FiberFactory factory = new FiberFactory();
	int ringBufferSize = 1024 * 1024;
	Disruptor<Fiber> disruptor;
	
	public void init() {
		disruptor = new Disruptor<Fiber>(factory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
		// 连接消费事件方法
		EventHandler<Fiber> fiberHandler = new FiberHandler();
		disruptor.handleEventsWith(fiberHandler);
		// 启动
		disruptor.start();
	}
	
	public void addTask(Task task) {
		//发布事件
		RingBuffer<Fiber> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();//请求下一个事件序号；
	    
		try {
			Fiber fiber = ringBuffer.get(sequence);//获取该序号对应的事件对象；
		    fiber.setTask(task);
		} finally{
		    ringBuffer.publish(sequence);//发布事件；
		}
	}
	
	public void shutdown() {
		disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
		executor.shutdown();//关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；
	}
}
