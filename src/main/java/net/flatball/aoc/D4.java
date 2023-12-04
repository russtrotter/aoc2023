package net.flatball.aoc;

import java.util.List;

public class D4 {

  static int[] buildSet(String line, int begin, int end) {
    final int[] values = new int[100];
    while (begin < end) {
      int to = begin + 3;
      if (line.charAt(begin) != ' ') {
        throw new IllegalStateException("expecting space: " + line);
      }
      char c1 = line.charAt(begin + 1);
      final int d1 = c1 == ' ' ? 0 : (c1 - '0');
      final int d2 = line.charAt(begin + 2) - '0';
      final int d = (d1 * 10) + d2;
      values[d] = 1;
      begin = to;
    }
    return values;
  }

  int intersect(int[] s1, int[] s2) {
    if (s1.length != s2.length) {
      throw new IllegalStateException("Invalid set size");
    }
    int count = 0;
    for (int i = 0; i < s1.length; i++) {
      count += (s1[i] & s2[i]);
    }
    return count;
  }

  int[] lineCounts(final List<String> lines, int colon, int bar) {
    final int[] counts = new int[lines.size()];
    for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
      final String line = lines.get(lineIndex);
      if (line.charAt(colon) != ':') {
        throw new IllegalStateException("Expecting colon: " + line);
      }
      if (line.charAt(bar) != '|') {
        throw new IllegalStateException("Expecting bar: " + line);
      }
      final int[] winning = buildSet(line, colon + 1, bar - 1);
      final int[] hand = buildSet(line, bar + 1, line.length());
      final int wc = intersect(winning, hand);
      counts[lineIndex] = wc;
    }
    return counts;
  }

  public void part1(final List<String> lines, int colon, int bar) {
    int total = 0;
    int[] counts = lineCounts(lines, colon, bar);
    for (int wc : counts) {
      if (wc > 0) {
        total += (1 << (wc - 1));
      }
    }
    System.out.println("PART1 total: " + total);
  }

  public void part2(final List<String> lines, int colon, int bar) {
    final int[] counts = lineCounts(lines, colon, bar);
    int count = 0;
    for (int c = 0; c < counts.length; c++) {
      count += recurse(counts, c);
    }
    System.out.println("PART 2 card count: " + count);
  }

  static int recurse(final int[] counts, int card) {
    int count = 1;
    final int wc = counts[card];
    if (wc > 0) {
      for (int c = card + 1; c <= (card + wc); c++) {
        count += recurse(counts, c);
      }
    }
    return count;
  }
}
