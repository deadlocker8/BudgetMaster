package de.deadlocker8.budgetmaster.logic.comparators;

import java.util.Comparator;

public class RatingComparator implements Comparator<Integer> 
{
    @Override
    public int compare(Integer o1, Integer o2) 
    {
        Integer a = o1 > 0 ? 1 : 0;        
        Integer b = o2 > 0 ? 1 : 0;
       
        return a.compareTo(b);
    }
}