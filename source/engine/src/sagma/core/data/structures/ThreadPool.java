package sagma.core.data.structures;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private final static int MAX_THREADS_PER_CORE = 8;
	private final static int CORES = Runtime.getRuntime().availableProcessors();
	private final static int POOL_SIZE = MAX_THREADS_PER_CORE * CORES;
	private static ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);
	
	public static void execute(Runnable r) {
		service.execute(r);
	}
}
