package com.soft.multithreading.forkjoin;


import com.soft.multithreading.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.soft.multithreading.util.CommonUtil.delay;
import static com.soft.multithreading.util.CommonUtil.stopWatch;
import static com.soft.multithreading.util.LoggerUtil.log;

//https://www.youtube.com/watch?v=fGuvosd-L98 - for better explanation
public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputList;

    public ForkJoinUsingRecursion(List<String> inputList) {
        this.inputList = inputList;
    }

    public static void main(String[] args) {

        stopWatch.start();
        List<String> names = DataSet.namesList();
        log("names : "+ names);

        ForkJoinPool forkJoinPool=new ForkJoinPool();//pool instance OR you can use ForkJoinPool.commonPool()
        ForkJoinUsingRecursion forkJoinUsingRecursion=new ForkJoinUsingRecursion(names);//main task is created
        List<String> resultList=forkJoinPool.invoke(forkJoinUsingRecursion);//submit the task to pool i.e in shared queue and waiting for the result.

        //we can do like below if you don;t want to wait for result after immediate invoking submit
        /*try {
            forkJoinPool.execute(forkJoinUsingRecursion);
            resultList=forkJoinUsingRecursion.get();// wait and get the result
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }*/

        stopWatch.stop();
        log("Final Result : "+ resultList);
        log("Total Time Taken : "+ stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    @Override
    protected List<String> compute() {
        System.out.println(Thread.currentThread().getName()+" is Started");
        //base condition
        if(inputList.size()<=1){
            System.out.println(Thread.currentThread().getName()+" Doing transformation");
            List<String> resultLst=new ArrayList<>();
            inputList.forEach(name->resultLst.add(addNameLengthTransform(name)));
            return resultLst;
        }
        int midPoint=inputList.size()/2;
        //forking the task, invoking .fork will submit the subtask into pool
        System.out.println(Thread.currentThread().getName()+" Forking");
        ForkJoinTask<List<String>> subTask= new ForkJoinUsingRecursion(inputList.subList(0,midPoint)).fork();
        inputList=inputList.subList(midPoint,inputList.size());
        List<String> computedResultOfSubTask=compute();//get the result of subtask
        List<String> otherSubTaskResult=subTask.join();//waiting for other task completes execution
        otherSubTaskResult.addAll(computedResultOfSubTask);
        return otherSubTaskResult;
    }
}
