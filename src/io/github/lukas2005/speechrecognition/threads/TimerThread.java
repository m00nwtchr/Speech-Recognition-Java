package io.github.lukas2005.speechrecognition.threads;

/**
 * 
 * @author lukas2005
 * Thread that will execute the supplied unnable after certain amout of time
 */
public class TimerThread extends Thread {

	Runnable runnable;
	Long millis;
	
	/**
	 * 
	 * @param runnable runnable o run after certain amout of time
	 * @param millis time in miliseconds
	 */
	public TimerThread(Runnable runnable, Long millis) {
		this.runnable = runnable;
		this.millis = millis;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(millis);
			runnable.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
