package com.soft.multithreading.parallelstream;

import com.soft.multithreading.util.DataSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.soft.multithreading.util.CommonUtil.startTimer;
import static com.soft.multithreading.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamExampleTest {

    ParallelStreamExample parallelStreamExample=new ParallelStreamExample();
    @Test
    void stringTransform() {


        List<String> namesList= DataSet.namesList();
        startTimer();
        namesList=parallelStreamExample.stringTransform(namesList);
        timeTaken();
        assertEquals(4,namesList.size());
        namesList.forEach(name->{
            assertTrue(name.contains("-"));
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {false,true})
    void stringTransform_1(boolean isParallel) {
        List<String> namesList= DataSet.namesList();
        startTimer();
        namesList=parallelStreamExample.stringTransform_1(namesList,isParallel);
        timeTaken();
        assertEquals(4,namesList.size());
        namesList.forEach(name->{
            assertTrue(name.contains("-"));
        });
    }
}