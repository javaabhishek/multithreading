package com.soft.multithreading.parallelstream;

import com.soft.multithreading.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample linkedListSpliteratorExample=new LinkedListSpliteratorExample();
    //@Test
    @RepeatedTest(1)//with this annotation below test case will execute 5 time
    void multiplyEachValue_sequential() {
        int size=1000000;
        List<Integer> inputList= DataSet.generateIntegerLinkedList(size);
        List<Integer> outputList=linkedListSpliteratorExample.multiplyEachValue(inputList,2,false);
       /*outputList.stream()
               .forEach(ele->{
                   assertEquals((ele/2)*2,ele);
               });*/
        assertEquals(size,outputList.size());
    }

    @RepeatedTest(1)//with this annotation below test case will execute 5 time
    void multiplyEachValue_parallel() {
        int size=1000000;
        List<Integer> inputList= DataSet.generateIntegerLinkedList(size);
        List<Integer> outputList=linkedListSpliteratorExample.multiplyEachValue(inputList,2,true);
        assertEquals(size,outputList.size());
    }
}