package main.journeytoTheMoon.Map;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Solution {
  /**
   * 'connectedAstronauts' maps the connections bewteen all astronauts, in th cases when there are
   * more than one per coutry. Consequantly, single atronauts per country are not stored in the map.
   */
  private static Map<Integer, List<Integer>> connectedAstronauts;

  private static List<Integer> astronautsPerCountry;

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    int numberOfAstronauts = scanner.nextInt();
    int numberOfPairs = scanner.nextInt();

    connectedAstronauts = new HashMap<Integer, List<Integer>>();
    for (int i = 0; i < numberOfPairs; i++) {
      int first = scanner.nextInt();
      int second = scanner.nextInt();
      add_toMap_connectedAstronauts(first, second);
      add_toMap_connectedAstronauts(second, first);
    }
    scanner.close();

    System.out.println(numberOfWays_toChoose_uniquePairsOfAstronauts(numberOfAstronauts));
  }

  /**
   * Calculates the number of ways to choose a unique pair of astronauts, where each astronaut in
   * the pair is from a different country.
   *
   * @return A long value, representing the total number of the described unique pairs.
   */
  private static long numberOfWays_toChoose_uniquePairsOfAstronauts(int numberOfAstronauts) {
    boolean[] visited = new boolean[numberOfAstronauts];
    astronautsPerCountry = new ArrayList<Integer>();

    for (int i = 0; i < numberOfAstronauts; i++) {
      if (connectedAstronauts.containsKey(i) && visited[i] == false) {
        astronautsPerCountry.add(breadthFirstSearch_findAstronautsPerCountry(i, visited));
      }
    }

    return calculate_allPossibleUniquePairs(numberOfAstronauts);
  }

  /**
   * Searches through connected astronauts to calculate the total numbere of astronauts per country,
   * in the case when there is more than one astronaut per country.
   *
   * @return An integer, representing the total astronauts for the searched country.
   */
  private static int breadthFirstSearch_findAstronautsPerCountry(int start, boolean[] visited) {
    LinkedList<Integer> queue = new LinkedList<Integer>();
    queue.add(start);
    visited[start] = true;
    int countAstronautsPerCountry = 1;

    while (!queue.isEmpty()) {
      int current = queue.removeFirst();

      for (int i = 0; i < connectedAstronauts.get(current).size(); i++) {

        if (visited[connectedAstronauts.get(current).get(i)] == false) {
          visited[connectedAstronauts.get(current).get(i)] = true;
          queue.add(connectedAstronauts.get(current).get(i));
          countAstronautsPerCountry++;
        }
      }
    }
    return countAstronautsPerCountry;
  }

  /**
   * Calculates all possible combinations that could form a unique pair, where each astronaut in the
   * pair is from a different country.
   *
   * @return A long value, representing the total number of the described combinations.
   */
  private static long calculate_allPossibleUniquePairs(int numberOfAstronauts) {
    long totalPairs = 0;

    for (int i = 0; i < astronautsPerCountry.size() - 1; i++) {
      for (int j = i + 1; j < astronautsPerCountry.size(); j++) {
        // Adds all possible unique pairs between the groups of connected astronauts.
        totalPairs =
            totalPairs + (long) astronautsPerCountry.get(i) * (long) astronautsPerCountry.get(j);
      }
    }
    // Single astronauts per country.
    long singleAstronauts = (long) numberOfAstronauts - connectedAstronauts.size();

    // Adds all possible unique pairs between single astronauts and connected astronauts.
    totalPairs = totalPairs + singleAstronauts * connectedAstronauts.size();

    // Adds all possible unique pairs among single astronauts themselves.
    totalPairs = totalPairs + (long) (singleAstronauts * (singleAstronauts - 1)) / 2;

    return totalPairs;
  }

  private static void add_toMap_connectedAstronauts(int first, int second) {
    if (!connectedAstronauts.containsKey(first)) {
      List<Integer> list = new ArrayList<Integer>();
      list.add(second);
      connectedAstronauts.put(first, list);
    } else {
      connectedAstronauts.get(first).add(second);
    }
  }
}
