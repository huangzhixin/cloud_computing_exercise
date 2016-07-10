package mw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class MWClientThreadPool {
	public MWClientThreadPool(MWClient client, String queryId, int poolSize, int queryNum) {
		ExecutorService exec = Executors.newFixedThreadPool(poolSize);
		AtomicLong counter = new AtomicLong(0);
		AtomicLong sumTime = new AtomicLong();
		
		for(int i = 0; i < poolSize; i++) {
			exec.execute(new Worker(client, queryNum, queryId, counter, sumTime));
		}
		exec.shutdown();
		while(!exec.isTerminated()) {
			try {
				Thread.sleep(2000);
				System.out.println("Durchschnittliche Zeit pro Anfrage: " + (double)sumTime.get()/(double)counter.get() + "ms");
				counter.set(0);
				sumTime.set(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	private class Worker implements Runnable{
		private MWClient client;
		private int queryNum;
		private String queryId;
		private AtomicLong counter;
		private AtomicLong sumTime;
		
		
		public Worker(MWClient client, int queryNum, String queryId, AtomicLong counter, AtomicLong sumTime){
			this.client = client;
			this.queryNum = queryNum;
			this.queryId = queryId;
			this.counter = counter;
			this.sumTime = sumTime;
		}
		
		
		public void run() {
			
			for(int i = 0; i < queryNum; i++) {
				long start = System.currentTimeMillis();
				client.searchIDs(queryId);
				long interval = System.currentTimeMillis() - start;
				sumTime.addAndGet(interval);
				counter.addAndGet(1);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}
