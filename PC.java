package pc_with_interrupt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class PC{

    public static void main(String args[]) {
        System.out.println("In Producer Consumer -- Main Method");
        
        Queue<Integer> buffer = new LinkedList<>();
        
        int maxSize = 10;	// max size of the buffer (or queue)
        
        int n = 10; 	// number of threads in pool
        
        // executor -- create a thread pool of n threads
        ExecutorService executor = Executors.newFixedThreadPool(n);   
        
        // create a single producer a task 
        Producer prodTask = new Producer(buffer, maxSize);
        
        // create a list of consumer tasks
        ArrayList<Consumer> consTasks = new ArrayList<Consumer>(n-1);
        
        for(int i=0; i<n-1; i++)
        	consTasks.add(new Consumer(buffer, maxSize));
        
        /*
         * submitting the producer and consumer tasks
         */

        Future<Integer> fp = executor.submit(prodTask);
        
        ArrayList<Future<Integer>> fCs = new ArrayList<Future<Integer>>(n);
        
        for(int i=0; i<n-1; i++)
        	fCs.add(executor.submit(consTasks.get(i)));      
        
        /*
         * set a time-limit for the execution of the tasks -- 2 seconds for the producer
         * and 1 second for each consumer
         */
        
        try {
			int k = -1;
			
			k = fp.get(2, TimeUnit.SECONDS);	// wait for 2 seconds then terminate producer
			
			System.out.println(k);
			
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for(int i=0; i<n-1; i++) {
        	
        	int j = -1;
			
        	try {
				j = fCs.get(i).get(1, TimeUnit.SECONDS);	// terminate each consumer 
				
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
			System.out.println(j);
        }
        
        //System.out.println("Reached Here");
        
        /*
         * ensure the executor is terminated
         */
        
        if(!executor.isShutdown())
        	executor.shutdownNow();
        
        /*
        try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		*/

        //System.out.println("Reached Here !! ");
        
    }

}