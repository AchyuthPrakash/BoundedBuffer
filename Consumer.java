import java.util.Queue;
import java.util.concurrent.Callable;

class Consumer implements Callable<Integer> {
	private final Queue<String> queue;

	public Consumer(Queue<String> queue){
		this.queue = queue;
	}

	@Override
	public Integer call() {
		try {
			while (true){
				/* entering the critical section now; wrap in synchronized block
				 * if queue is empty, consumer needs to wait
				 * else consume the value in queue */
				synchronized (queue) {
					while (queue.isEmpty()) {
						System.out.println("Consumer -- " + this +" Queue is empty");
						System.out.println("Consumer -- " + this + " is Waiting");
						queue.wait();
					}

					System.out.println("Consumer -- "  + this + " Consuming value : " + queue.remove());
					queue.notifyAll();
				}
			}
		}	catch(Exception e) {
			return -1;
		}
	}
}