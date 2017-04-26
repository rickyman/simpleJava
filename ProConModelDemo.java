import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by ricky on 2017/4/26.
 */
public class ProConModelDemo {

    static class MyBlockingQueue<E> {
        private Queue<E> queue = null;
        private int limit;

        public MyBlockingQueue(int limit) {
            this.limit = limit;
            queue = new ArrayDeque<>(limit);
        }

        public synchronized void put(E e) throws InterruptedException {
            while (queue.size() == limit) {
                wait();
            }
            queue.add(e);
            notifyAll();
        }

        public synchronized E take() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            E e = queue.poll();
            notifyAll();
            return e;
        }
    }
  static class   Producer extends Thread{
      MyBlockingQueue<String> queue;

      public Producer(MyBlockingQueue<String> queue) {
          this.queue = queue;
      }

      @Override
      public void run() {
          int num = 0;
          try {
              while (true) {
                 String task = String.valueOf(num);
                  queue.put(task);
                  System.out.println("produce task " + task);
                  num++;
                  Thread.sleep((int) (Math.random() * 100));
              }
          } catch (InterruptedException e) {
          }
      }
  }
    static class   Consumer extends Thread{
        MyBlockingQueue<String> queue;

        public Consumer(MyBlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("consumer get " + queue.take());
                    Thread.sleep((int) (Math.random() * 100));
                }
            } catch (InterruptedException e) {
            }
        }
    }
    public static void main(String[] args) {
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(10);
        new Producer(queue).start();
        new Consumer(queue).start();
    }
}
