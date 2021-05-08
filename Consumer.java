import java.util.Queue;
import java.util.concurrent.Callable;

class Consumer implements Callable<Integer> {
	private Queue<String> queue;

	public Consumer(Queue<String> queue, int maxSize){
		this.queue = queue;
	}

	@Override
	public Integer call() throws Exception {
		try {
			while (true){

				/* entering the critical section now; wrap in synchronized block
				 * if queue is empty, consumer needs to wait
				 * else consume the value in queue */
				synchronized (queue) {
					while (queue.isEmpty()) {
						System.out.println("Consumer -- " + this.toString() +" Queue is empty");
						System.out.println("Consumer -- " + this.toString() + " is Waiting");
						queue.wait();
					}

					System.out.println("Consumer -- "  + this.toString() + " Consuming value : " + queue.remove());
					queue.notifyAll();
				}
			}
		}	catch(Exception e) {

			return -1;
		}
	}
}