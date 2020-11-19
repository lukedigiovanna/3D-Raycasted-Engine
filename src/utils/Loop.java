package utils;

public class Loop {
    private Thread thread;
    private boolean running = false;
    private Timer timer;

    public Loop(double targetIterationsPerSecond, Loopable loop) {
        timer = new Timer();
        double targetWait = 1.0/targetIterationsPerSecond;
        thread = new Thread(new Runnable() {
            public void run() {
                while (running) {
                    double dt = timer.mark();
                    long before = System.currentTimeMillis();
                    loop.loop(dt);
                    double addt = (System.currentTimeMillis() - before)/1000.0;
                    timer.wait(targetWait - addt);
                }
            }
        });
    }   

    public void start() {
        running = true;
        thread.start();
    }

    public void terminate() {
        running = false;
    }

    public boolean isActive() {
        return this.running;
    }
}
