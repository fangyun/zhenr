/**
 * GPL
 */
package zhenr.util.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import zhenr.log.Log;
import zhenr.log.LogFactory;
import zhenr.util.ConcurrentHashSet;
import zhenr.util.thread.ThreadPool.SizedThreadPool;

/**
 * @author FangYun
 *
 */
public class QueuedThreadPool implements SizedThreadPool {
	private static final Log LOG = LogFactory.getLog(QueuedThreadPool.class);

	private final AtomicInteger _threadsStarted = new AtomicInteger();
	private final AtomicInteger _threadsIdle = new AtomicInteger();
	private final AtomicLong _lastShrink = new AtomicLong();
	private final ConcurrentHashSet<Thread> _threads = new ConcurrentHashSet<>();
	private final Object _joinLock = new Object();
	private final BlockingQueue<Runnable> _jobs;
	private final ThreadGroup _threadGroup;
	private String _name = "qtp" + hashCode();
	private int _idleTimeout;
	private int _maxThreads;
	private int _minThreads;
	private int _priority = Thread.NORM_PRIORITY;
	private boolean _daemon = false;
	private boolean _detailedDump = false;
	private int _lowThreadsThreshold = 1;
}
