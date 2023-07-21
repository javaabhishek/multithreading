package com.soft.multithreading.parallelstream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.soft.multithreading.util.CommonUtil.startTimer;
import static com.soft.multithreading.util.CommonUtil.timeTaken;

public class ArrayListSpliteratorExample {

    //check test case
    public List<Integer> multiplyEachValue(List<Integer> inputList, int multplyValue,boolean isParallel){
        startTimer();
        Stream<Integer> integerStream=inputList.stream();//sequential

        if(isParallel)
            integerStream.parallel();

        List<Integer> resultList=integerStream
                .map(ele->ele*multplyValue)
                .collect(Collectors.toList());
        timeTaken();
        return resultList;
    }

}
