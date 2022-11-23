package PathQuery;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author DELL
 * @Date 2022/11/2 19:08
 * @Version 1.0
 */
public class Queue {
    private LinkedList<Object> list = new LinkedList<Object>() ;

    private final int minSize = 0 ; ;

    private final int maxSize ;

    private AtomicInteger count = new AtomicInteger(0) ;

    public Queue(int size){
        this.maxSize = size ;
    }
    private final Object lock = new Object() ;

    public void put(Object o){
        synchronized(lock){
            while(size() == this.maxSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(o) ;
            //计数器增加
            count.incrementAndGet() ;
            //通知唤醒
            lock.notify();
        }
    }


    private int size(){
        return count.get() ;
    }

    public Object take(){
        Object res = null ;
        synchronized(lock){
            while(size() == this.minSize){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            res = list.removeFirst();
            //计数器减1
            count.decrementAndGet() ;
            lock.notify();
        }
        return res ;

    }

    public static void main(String[] args) {

        final Queue mq = new Queue(3) ;

//        mq.put("a");
//        mq.put("b");
//        mq.put("c");

        new Thread(new Runnable() {

            @Override
            public void run() {
                mq.put("g");
                System.out.println("put1 secceseful");
                mq.put("f");
                System.out.println("put2 secceseful");
            }
        }).start();


        new Thread(new Runnable() {

            @Override
            public void run() {
                mq.put("a");
                System.out.println("put3 secceseful");
                System.out.println("take value = "+mq.take() );
            }
        }).start();


    }
}
