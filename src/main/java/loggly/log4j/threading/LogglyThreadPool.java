package loggly.log4j.threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogglyThreadPool {
	private final BlockingQueue<Runnable> workerQueue;
	private final Thread[] workerThreads;
	private volatile boolean shutdown;

	public LogglyThreadPool(final int N) {

		workerQueue = new LinkedBlockingQueue<>();
		workerThreads = new Thread[N];
		// Start N Threads and keep them running
		for (int i = 0; i < N; i++) {

			workerThreads[i] = new Worker("Worker" + i);
			workerThreads[i].start();
		}
	}

	public void addTask(final Runnable r) {

		try {
			workerQueue.put(r);

		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public synchronized void shutdown() {

		while (!workerQueue.isEmpty()) {
			try {
				Thread.sleep(1_000);
			} catch (final InterruptedException e) {
				// interruption
			}
		}
		shutdown = true;
		for (final Thread workerThread : workerThreads) {
			workerThread.interrupt();
		}
	}

	private class Worker extends Thread {

		public Worker(final String name) {
			super(name);
		}

		@Override
		public void run() {

			while (!shutdown) {
				try {
					final Runnable r = workerQueue.take();
					r.run();
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
