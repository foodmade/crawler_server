package com.spider;

import com.spider.taskPool.TaskData;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.*;


public class ClassTest<K,V>{

    String str = "good";
    char[] h = {1,2,3};

    public static void main(String[] args) throws Exception {
        HashMap<String,String> map = new HashMap<>(33,0.7f);
        map.put("javaMap","2");
        map.get("1");

        ConcurrentHashMap<String,Object> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("1","2");
//        binaryToDecimal("javaMap".hashCode());

        Hashtable hashtable = new Hashtable();

        hashtable.put("1","2");

//        System.out.println("结果:"+(2^4));
      /*  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,3,5000L, TimeUnit.SECONDS,new LinkedBlockingDeque(),new ThreadPoolExecutor.DiscardPolicy());

        new Listener(threadPoolExecutor).start();

        for (int i = 0;i<1000;i++){
            TestThread testThread = new TestThread();

            testThread.setCnt(i);

            threadPoolExecutor.execute(testThread);
        }*/
        System.out.println(148776&((1<<8)-1));
    }

    public static void binaryToDecimal(int n){
        for(int i = 31;i >= 0; i--)
            System.out.print(n >>> i & 1);
    }

    static class TestThread implements Runnable{

        private Integer cnt;

        public void setCnt(Integer cnt) {
            this.cnt = cnt;
        }

        @Override
        public void run() {
            if(cnt == 9){
                int c = 1/0;
            }
//            System.out.println("execute thread:" + cnt);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Listener extends Thread{
        private ThreadPoolExecutor threadPoolExecutor;

        Listener(ThreadPoolExecutor threadPoolExecutor){
            this.threadPoolExecutor = threadPoolExecutor;
        }

        @Override
        public void run() {
            while (true){
                System.out.println("剩余任务:"+threadPoolExecutor.getQueue().size());

                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
