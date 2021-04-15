package pc_with_interrupt;

import java.util.Queue;
import java.util.concurrent.Callable;

class Consumer implements Callable<Integer> {
    private Queue<Integer> queue;
    
    public Consumer(Queue<Integer> queue, int maxSize){
        this.queue = queue;
    }

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub
		
		//long startTime = System.nanoTime();
		
		try {
			while (true){//(System.nanoTime() - startTime)/1000000000.0 < 2000) {
				
				/*
				 * entering the critical section now; wrap in synchronized block
				 * if queue is empty, consumer needs to wait
				 * else consume the value in queue
				 */
				
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
