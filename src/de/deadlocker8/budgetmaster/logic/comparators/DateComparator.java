package de.deadlocker8.budgetmaster.logic.comparators;

import java.util.Comparator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateComparator implements Comparator<String> 
{
    @Override
    public int compare(String o1, String o2) 
    {
        DateTime a = DateTime.parse(o1, DateTimeFormat.forPattern("dd.MM.YYYY"));
        DateTime b = DateTime.parse(o2, DateTimeFormat.forPattern("dd.MM.YYYY"));
       
        return a.compareTo(b);
    }
}