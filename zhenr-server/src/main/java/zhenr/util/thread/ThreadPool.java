/**
 * GPL
 */
package zhenr.util.thread;

import java.util.concurrent.Executor;

/**
 * @author FangYun
 *
 */
public interface ThreadPool extends Executor {
	/**
	 * Blocks until the thread pool is {@link LifeCycle#stop stopped}.
	 * 
	 * @throws InterruptedException
	 *             if thread was interrupted
	 */
	public void join() throws InterruptedException;

	/**
	 * @return The total number of threads currently in the pool
	 */
	public int getThreads();

	/**
	 * @return The number of idle threads in the pool
	 */
	public int getIdleThreads();

	/**
	 * @return True if the pool is low on threads
	 */
	public boolean isLowOnThreads();

	public interface SizedThreadPool extends ThreadPool {
		public int getMinThreads();

		public int getMaxThreads();

		public void setMinThreads(int threads);

		public void setMaxThreads(int threads);
	}
}
