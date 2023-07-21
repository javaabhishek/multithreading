package com.soft.multithreading.parallelstream;

import com.soft.multithreading.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.soft.multithreading.util.CommonUtil.*;
import static com.soft.multithreading.util.LoggerUtil.log;

public class ParallelStreamExample {

    public List<String> stringTransform(List<String> namesList){
        return
                namesList
                        //.stream()
                        .parallelStream()
                        .map(this::addNameLengthTransform)
                        .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<String> namesList= DataSet.namesList();
        ParallelStreamExample parallelStreamExample=new ParallelStreamExample();
        startTimer();
        namesList=parallelStreamExample.stringTransform(namesList);
        timeTaken();
        log("Output:"+namesList);
    }
    private String addNameLengthTransform(String name) {
        delay(500);
        return name.length()+" - "+name ;
    }

    public List<String> stringTransform_1(List<String> namesList, boolean isParallel) {
        Stream<String> names= namesList.stream();
        if(isParallel)//based on this vairable we are deciding whether we have to execute piple line in sequential or parallel
            names=names.parallel();

        return names.map(this::addNameLengthTransform).collect(Collectors.toList());
    }
}
