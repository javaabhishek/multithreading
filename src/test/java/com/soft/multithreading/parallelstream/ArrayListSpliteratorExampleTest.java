package com.soft.multithreading.parallelstream;

import com.soft.multithreading.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest {
    ArrayListSpliteratorExample arrayListSpliteratorExample=new ArrayListSpliteratorExample();
    //@Test
    @RepeatedTest(5)//with this annotation below test case will execute 5 time
    void multiplyEachValue_sequential() {
       int size=1000000;
       List<Integer> inputList= DataSet.generateArrayList(size);
       List<Integer> outputList=arrayListSpliteratorExample.multiplyEachValue(inputList,2,false);
       /*outputList.stream()
               .forEach(ele->{
                   assertEquals((ele/2)*2,ele);
               });*/
        assertEquals(size,outputList.size());
    }

    @RepeatedTest(5)//with this annotation below test case will execute 5 time
    void multiplyEachValue_parallel() {
        int size=1000000;
        List<Integer> inputList= DataSet.generateArrayList(size);
        List<Integer> outputList=arrayListSpliteratorExample.multiplyEachValue(inputList,2,true);
       /*outputList.stream()
               .forEach(ele->{
                   assertEquals((ele/2)*2,ele);
               });*/
        assertEquals(size,outputList.size());
    }
}