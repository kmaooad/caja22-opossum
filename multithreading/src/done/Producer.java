package done;

import java.util.ArrayDeque;
import java.util.Queue;

class Producer implements Runnable {
    private MyQueue myQueue;

    public Producer(MyQueue myQueue) {
        this.myQueue = myQueue;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {
            myQueue.put(i++);
        }
    }
}

class Consumer implements Runnable {
    private MyQueue queue;

    public Consumer(MyQueue myQueue) {
        this.queue = myQueue;
    }

    @Override
    public void run() {

        while (true) {

            queue.get();
        }
    }
}

class MyQueue {
    private final Queue<Integer> integerQueue = new ArrayDeque<>();

    public synchronized int get() {
        while (integerQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int result = this.integerQueue.remove();
        System.out.println("RECEIVED: " + result);
        notify();
        return result;
    }

    public synchronized void put(int n) {
        while (integerQueue.size() > 10) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.integerQueue.add(n);
        System.out.println("SENT: " + n);
        notify();
    }
}

 class ProducerDemo {
    public static void main(String[] args) {
        MyQueue myQueue = new MyQueue();

        Consumer consumer = new Consumer(myQueue);
        Consumer consumer2 = new Consumer(myQueue);
        Producer producer = new Producer(myQueue);

        Thread t1 = new Thread(consumer);
        Thread t2 = new Thread(consumer2);
        Thread t3 = new Thread(producer);

        t1.start();
        t2.start();
        t3.start();
    }
}