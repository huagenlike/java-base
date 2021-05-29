package com.mzl.java8.chap5;

import com.mzl.java8.chap5.*;

import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class PuttingIntoPractice{
    public static void main(String ...args){    
        com.mzl.java8.chap5.Trader raoul = new com.mzl.java8.chap5.Trader("Raoul", "Cambridge");
        com.mzl.java8.chap5.Trader mario = new com.mzl.java8.chap5.Trader("Mario","Milan");
        com.mzl.java8.chap5.Trader alan = new com.mzl.java8.chap5.Trader("Alan","Cambridge");
        com.mzl.java8.chap5.Trader brian = new com.mzl.java8.chap5.Trader("Brian","Cambridge");
		
		List<com.mzl.java8.chap5.Transaction> transactions = Arrays.asList(
            new com.mzl.java8.chap5.Transaction(brian, 2011, 300),
            new com.mzl.java8.chap5.Transaction(raoul, 2012, 1000),
            new com.mzl.java8.chap5.Transaction(raoul, 2011, 400),
            new com.mzl.java8.chap5.Transaction(mario, 2012, 710),
            new com.mzl.java8.chap5.Transaction(mario, 2012, 700),
            new com.mzl.java8.chap5.Transaction(alan, 2012, 950)
        );	
        
        
        // Query 1: Find all transactions from year 2011 and sort them by value (small to high).
        List<com.mzl.java8.chap5.Transaction> tr2011 = transactions.stream()
                                               .filter(transaction -> transaction.getYear() == 2011)
                                               .sorted(comparing(com.mzl.java8.chap5.Transaction::getValue))
                                               .collect(toList());
        System.out.println(tr2011);
        
        // Query 2: What are all the unique cities where the traders work?
        List<String> cities = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getCity())
                        .distinct()
                        .collect(toList());
        System.out.println(cities);

        // Query 3: Find all traders from Cambridge and sort them by name.
        
        List<com.mzl.java8.chap5.Trader> traders =
            transactions.stream()
                        .map(com.mzl.java8.chap5.Transaction::getTrader)
                        .filter(trader -> trader.getCity().equals("Cambridge"))
                        .distinct()
                        .sorted(comparing(com.mzl.java8.chap5.Trader::getName))
                        .collect(toList());
        System.out.println(traders);
        
        
        // Query 4: Return a string of all traders’ names sorted alphabetically.
        
        String traderStr = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getName())
                        .distinct()
                        .sorted()
                        .reduce("", (n1, n2) -> n1 + n2);
        System.out.println(traderStr);
        
        // Query 5: Are there any trader based in Milan?
        
        boolean milanBased =
            transactions.stream()
                        .anyMatch(transaction -> transaction.getTrader()
                                                            .getCity()
                                                            .equals("Milan")
                                 );
        System.out.println(milanBased);
        
        
        // Query 6: Update all transactions so that the traders from Milan are set to Cambridge.
        transactions.stream()
                    .map(com.mzl.java8.chap5.Transaction::getTrader)
                    .filter(trader -> trader.getCity().equals("Milan"))
                    .forEach(trader -> trader.setCity("Cambridge"));
        System.out.println(transactions);
        
        
        // Query 7: What's the highest value in all the transactions?
        int highestValue = 
            transactions.stream()
                        .map(com.mzl.java8.chap5.Transaction::getValue)
                        .reduce(0, Integer::max);
        System.out.println(highestValue);      
    }
}