package net.flatball.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * finally felt a little ok with this solution.  table lookups and computation of scalar values
 * for comparison as much as I could think of.  Some uber programmer might be able to do the whole thing
 * in a single pass, but i still used a multi-rank sort at the end.
 * had my first incorrect submissions on this one.  First one was due to the wrong sort order, easy fix.
 * Part 2 had several submit rejects as I fumbled with the basic joker handling a few times and then had to re-read
 * instructions to realize the joker ranking gets set all the way to lowest (less than 2) for part 2.
 */
public class D7 {

  record Hand(String orig, int cards, int bid, int rank) {
  }

  static final int[] pow10 = {0, 10, 100, 1000, 10000, 100000};

  static int[] makeRanks(String cards) {
    final int[] ranks = new int[128];
    for (int i = 0; i < cards.length(); i++) {
      ranks[cards.charAt(i)] = i;
    }
    return ranks;
  }

  public void part(int part, List<String> lines) {
    // part 2 set jokers to lowest rank card
    final int[] CARD_RANKS = makeRanks(part == 2 ? "J23456789TQKA" : "23456789TJQKA");

    final List<Hand> all = new ArrayList<>();
    for (String line : lines) {
      final int[] hist = new int[13];

      int cards = 0;
      for (int i = 0; i < 5; i++) {
        int index = CARD_RANKS[line.charAt(i)];
        hist[index]++;
        // we can't use lexical card hand comparison, so we translate
        // each card rank into a 4-bit quantity so that way the hand
        // becomes a scalar quantity of 4-bit values we can just compare, not a string.
        cards = (cards << 4) | index;
      }
      if (part == 2) {
        final int ji = CARD_RANKS['J'];
        // see if this hand has any jokers
        final int jc = hist[ji];
        if (jc > 0) {
          hist[ji] = 0;
          if (jc == 5) {
            // all jokers mean we can just treat it as 5-of-a-kind aces
            hist[CARD_RANKS['A']] = jc;
          } else {
            // otherwise, use our histogram to find the highest occurring card
            int max = Arrays.stream(hist).reduce(0, (a, b)-> Math.max(b, a));
            // search from highest rank to low for that histogram entry
            // and give all the jokers to it to give us the highest ranking
            for (int i = hist.length - 1; i >= 0; i--) {
              if (hist[i] == max) {
                hist[i] += jc;
                break;
              }
            }
          }
        }
      }
      // compute ranking where higher histogram counts translate to exponential higher ranks
      int rank = 0;
      for (int h : hist) {
        rank += pow10[h];
      }

      int bid = Integer.parseInt(line.substring(6));
      final Hand hand = new Hand(line.substring(0, 5), cards, bid, rank);
      all.add(hand);
    }
    // sort using rank first, then on ties use card value as comparison
    all.sort(Comparator.comparingInt((Hand hand) -> hand.rank).thenComparingInt(hand -> hand.cards));
    int total = 0;
    for (int i = 0; i < all.size(); i++) {
      total += (all.get(i).bid() * (i + 1));
      System.out.println(all.get(i));
    }
    System.out.println("PART " + part + " total: " + total);
  }
}
