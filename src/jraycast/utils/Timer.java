package jraycast.utils;

public class Timer {
    private long last;

    public Timer() {
        last = System.currentTimeMillis();
    }

    public double mark() {
        long now = System.currentTimeMillis();
        long elapsed = now - last;
        last = now;
        double seconds = elapsed/1000.0;
        return seconds;
    }
    
    public void wait(double seconds) {
    	long wait = (long)(seconds *  1000L);
    	long now = System.currentTimeMillis();
    	while (System.currentTimeMillis() - now < wait)
    		continue;
    }
}
