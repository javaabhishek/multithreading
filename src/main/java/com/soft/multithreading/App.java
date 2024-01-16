package com.soft.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        List<InventoryHistory> inventoryHistory=new ArrayList<>();
        InventoryHistory obj=new InventoryHistory();
        obj.setSysId(1);
        obj.setQtyAmount(2);
        inventoryHistory.add(obj);
        Map<Integer, Integer> invHistoryToQtyMapping=inventoryHistory.stream()
                .collect(Collectors.toMap(InventoryHistory::getSysId, InventoryHistory::getQtyAmount));

        System.out.println(invHistoryToQtyMapping.size());
    }


}
