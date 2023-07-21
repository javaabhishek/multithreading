package com.soft.multithreading.forkjoin;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class SearchTask extends RecursiveTask<Integer>{

    int arr[];
    int start;
    int end;
    int searchEle;
    public SearchTask(int[] arr, int start, int end, int searchEle) {
        this.arr = arr;
        this.start = start;
        this.end = end;
        this.searchEle=searchEle;
    }

    @Override
    protected Integer compute() {
        System.out.println(Thread.currentThread().getName());
        //for the task
        int size=end-start+1;
        if(size>3){//for the task
            int mid=(end+start)/2;
            SearchTask subTask1=new SearchTask(arr,start,mid,searchEle);
            SearchTask subTask2=new SearchTask(arr,mid+1,end,searchEle);
            subTask1.fork();
            subTask2.fork();
            int result=subTask1.join()+subTask2.join();
            return result;
        }else
            return processSearch();
    }

    private Integer processSearch() {
        int count=0;
        for (int i=start;i<=end;i++){
            if(searchEle==arr[i])
                count++;
        }
        return count;
    }
}
public class ForJoinExample {

    public static void main(String[] args) {
        int[] arr={1,2,3,4,5,6,6,7,8,9,10};
        int start=0;
        int end=arr.length-1;
        int searchEle=6;
        //access the pool
        ForkJoinPool pool=ForkJoinPool.commonPool();
        //submit the task to pool.
        SearchTask task=new SearchTask(arr,start,end,searchEle);
        int result=task.invoke();//invoke will submit the task wait for result, so here main thread will goes into wait
        System.out.printf("%d found %d times:",searchEle,result);
    }
}
