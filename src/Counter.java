/**
 *
 *  @author Shkred Artur
 *
 */

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    AtomicLong value;
    private Thread thread;

    public Counter(AtomicLong value) {
        this.value = value;
        start();
    }

    private void start() {
        thread = new Thread(() -> {
            while (true){
                value.incrementAndGet();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void stop(){
        thread.interrupt();
    }
}
