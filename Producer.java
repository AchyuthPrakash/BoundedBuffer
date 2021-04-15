package pc_with_interrupt;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;

public class Producer implements Callable<Integer> {
    private Queue<Integer> queue;
    private int maxSize;
    public Producer(Queue<Integer> queue, int maxSize){
        this.queue = queue;
        this.maxSize = maxSize;
    }

	@Override
	public Integer call() throws Exception {
		// TODO Auto-generated method stub

		//long startTime = System.nanoTime();
		
		try {
			
			while (true) {//(System.nanoTime() - startTime)/1000000000.0 < 2000) {
				
				/*
				 * entering the critical section now; wrap in synchronized block
				 * if queue is full, producer needs to wait
				 * else add entry to the queue
				 */
				
	            synchronized (queue) {
	                while (queue.size() == maxSize) {
	                	
	                	System.out .println("Producer -- " + this.toString() + " Queue is full, will wait now");
	                    
	                    queue.wait();
	                	
	                }
	                
	                /*
	                 * producer generates a random number and puts this into the queue
	                 * need to replace this with the following:
	                 * get some data from the web-page and put it into the queue 
	                 */
	                
	                Random random = new Random();
	                int j = random.nextInt();
	                System.out.println("Producer -- " +  this.toString() + " Producing value : " + j);
	                
	                queue.add(j);
	                queue.notifyAll();
	                
	            }
	
	        }
		}	catch(Exception e) {
			
			return -1;
		}
		
	}
}