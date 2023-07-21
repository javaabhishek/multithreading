package com.soft.multithreading.parallelstream;

import com.soft.multithreading.util.DataSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.soft.multithreading.util.LoggerUtil.log;

public class ParallelStreamResultOrder {

    public static void main(String[] args) {
        /*List<Integer> inputList= List.of(8,7,6,5,4,3,2,1);
        log("input list:"+inputList);
        List<Integer> outputList=processList(inputList);
        log("Output List:"+outputList);*/

        Set<Integer> inputSet= DataSet.generateIntegerSet(5);
        log("input Set:"+inputSet);
        Set<Integer> outputSet=processSet(inputSet);
        log("Output Set:"+outputSet);
    }

    private static List<Integer> processList(List<Integer> inputList) {
        return inputList.parallelStream()
                .map(ele->ele*2)
                .collect(Collectors.toList());
    }
    private static Set<Integer> processSet(Set<Integer> inputSet) {
        return inputSet
                .parallelStream()
                .map(ele->ele*2)
                .collect(Collectors.toSet());
    }
}
