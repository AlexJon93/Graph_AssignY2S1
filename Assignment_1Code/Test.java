import java.io.*;
import java.util.*;

public class Test
{
    public static void main(String[] args) 
    {
        Map test = new HashMap();
        test.put("One", 1);
        test.put("Two", 2);
        test.put("Three", 3);

        for(String s : test.keySet())
            System.out.println(s);
    }
}