package code.util;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 线程队列
 */
public class ThreadQueue extends Thread {
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    public static ThreadQueue createAndStart() {
        ThreadQueue wxSocketMessageQueue = new ThreadQueue();
        wxSocketMessageQueue.start();
        return wxSocketMessageQueue;
    }

    public void post(Runnable runnable) {
        queue.add(runnable);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Runnable data = queue.take();
                data.run();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}