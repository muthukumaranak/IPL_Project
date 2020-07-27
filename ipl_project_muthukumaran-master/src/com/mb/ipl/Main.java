package com.mb.ipl;

import java.io.*;
import java.util.*;

public class Main {
    public static int yearID = 1;
    public static int winnerID = 10;
    public static int extra_Runs_ID = 11;
    public static String season = "2016";
    public static int match_ID = 0;
    public static int bowler_ID = 8;
    public static int totalRuns_ID = 17;
    public static int year_Start_index = 0;
    public static int year_End_Index = 60;

    public static void main(String[] args) {
        List<Match> matches = getMatchData();
        List<Delivery> deliveries = getDeliveriesData();

        findMatchesPlayedPerYear(matches);
        findMatchesWonPerTeam(matches);
        findExtraRunsScoredPerTeam(matches);
        findBowlerEconomy(deliveries);
    }

    private static void findBowlerEconomy(List<Delivery> deliveries) {
        System.out.println("\nBowler Economy\n");
        List<String> bowlersList = new ArrayList<>();
        Map<String,Integer> bowlersBalls = new TreeMap<>();
        Map<String,Integer> bowlersRunsGiven = new TreeMap<>();
        Map<String,Double> bowlerEconomy = new TreeMap<>();
        for(Delivery it : deliveries) {
            if (Integer.parseInt(it.getMatchId()) > year_Start_index && Integer.parseInt(it.getMatchId()) < year_End_Index) {
                bowlersList.add(it.getBowler());
                bowlersBalls.put(it.getBowler(), Collections.frequency(bowlersList, it.getBowler()));
                if (bowlersRunsGiven.containsKey(it.getBowler()))
                    bowlersRunsGiven.put(it.getBowler(), bowlersRunsGiven.get(it.getBowler()) + Integer.parseInt(it.getTotalRuns()));
                else
                    bowlersRunsGiven.put(it.getBowler(), Integer.parseInt(it.getTotalRuns()));
            }
        }
        for (String it : bowlersList) {
            int balls = bowlersBalls.get(it);
            int runs = bowlersRunsGiven.get(it);
            double economy = (runs * 1.0) / (balls / 6 + (balls % 6) / 10.0);
            bowlerEconomy.put(it, economy);
        }
        Iterator it = bowlerEconomy.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            System.out.printf("%s  :  %.2f\n",mapElement.getKey(),mapElement.getValue());
        }
    }

    private static void findExtraRunsScoredPerTeam(List<Match> matches) {
        System.out.println("\nExtra Runs Scored Per Team\n");
        Map<String, Integer> ScoreMap = new TreeMap<>();
        for (Match it : matches) {
            if(it.getSeason().equals(season)){
            if(ScoreMap.containsKey(it.getWinner())){
                int score = ScoreMap.get(it.getWinner()) + Integer.parseInt(it.getWinByRuns());
                ScoreMap.put(it.getWinner(),score);
            }
            else
                ScoreMap.put(it.getWinner(),Integer.parseInt(it.getWinByRuns()));
        }}
        Iterator it = ScoreMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry mapElement = (Map.Entry) it.next();
            System.out.println(mapElement.getKey() + " : " + mapElement.getValue());
        }
    }

    private static void findMatchesWonPerTeam(List<Match> matches) {
        System.out.println("\nMatches Won Per Team\n");
        List<String> matchList = new ArrayList<>();
        TreeSet<String> matchSet = new TreeSet<>();
        for (Match i : matches) {
            matchList.add(i.getWinner());
            matchSet.add(i.getWinner());
        }
        for (String it : matchSet)
            System.out.println(it + " " + Collections.frequency(matchList, it));
    }

    private static void findMatchesPlayedPerYear(List<Match> matches) {
        System.out.println("\nMatches Played Per Year\n");
        List<String> yearList = new ArrayList<>();
        TreeSet<String> yearSet = new TreeSet<>();
        for (Match i : matches) {
            yearList.add(i.getSeason());
            yearSet.add(i.getSeason());
        }
        for (String it : yearSet)
            System.out.println(it + " " + Collections.frequency(yearList, it));
    }

    private static List<Delivery> getDeliveriesData() {
        List<Delivery> deliveries = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("delivery.csv"));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String fields[] = line.split(",");
                Delivery delivery = new Delivery();
                delivery.setMatchId(fields[match_ID]);
                delivery.setBowler(fields[bowler_ID]);
                delivery.setTotalRuns(fields[totalRuns_ID]);
                deliveries.add(delivery);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deliveries;
    }

    private static List<Match> getMatchData() {
        List<Match> matches = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("dataset.csv"));
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String fields[] = line.split(",");
                Match match = new Match();
                match.setSeason(fields[yearID]);
                match.setWinner(fields[winnerID]);
                match.setWinByRuns(fields[extra_Runs_ID]);
                matches.add(match);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matches;
    }
}
