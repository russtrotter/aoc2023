package net.flatball.aoc;

import java.util.ArrayList;
import java.util.List;

public class D13 implements AOC {
  int vTotal = 0;
  int hTotal = 0;


  @Override
  public void run(int part, List<String> lines) {
    int start = 0;
    int total = 0;
    for (int lineIndex = 0;;) {
      while (lineIndex < lines.size()) {
        if (lines.get(lineIndex).isEmpty()) {
          break;
        }
        lineIndex++;
      }
      total += solve(lines.subList(start, lineIndex));
      if (lineIndex == lines.size()) {
        break;
      }
      lineIndex++;
      start = lineIndex;
    }
    System.out.println("MAX PATTERN HEIGHT: " + maxLength);
    System.out.println("MAX PATTERN WIDTH: " + maxWidth);
    System.out.println("HONK " + ((hTotal * 100) + vTotal));
    System.out.println("PART " + part + " total is " + total);
  }

  static List<String> rotate(List<String> pattern) {
    List<String> r = new ArrayList<>();

    for (int x = 0; x < pattern.getFirst().length(); x++) {
      StringBuilder b = new StringBuilder();
      for (int y = pattern.size() - 1; y >= 0; y--) {
        b.append(pattern.get(y).charAt(x));
      }
      r.add(b.toString());
    }
    return r;
  }

  static int ref(List<String> pattern) {
    boolean found = false;
    int line = 0;
    for (int i = 0; i < (pattern.size() - 1);i++) {
      int f = i + 1;
      int b = i;
      int match = 0;
      while (b >= 0 && f < pattern.size()) {
        if (!pattern.get(b).equals(pattern.get(f))) {
          match = 0;
          break;
        }
        match++;
        b--;
        f++;
      }
      if (match > 0) {
        if (found) {
          throw new IllegalStateException("still dupe");
        }
        found = true;
        line = i + 1;
      }
    }
    return line;
  }

  int maxLength = 0;
  int maxWidth = 0;

  int solve(List<String> pattern) {
    this.maxLength = Math.max(this.maxLength, pattern.size());
    this.maxWidth = Math.max(maxWidth, pattern.stream().map(String::length).reduce(0, Math::max));
    int h = ref(pattern);
    hTotal += h;
    //pattern.forEach(System.out::println);
    System.out.println("H LINE " + h);
    final List<String> rot = rotate(pattern);
    int v = ref(rot);
    vTotal += v;
    //rot.forEach(System.out::println);
    System.out.println("V LINE " + v);
    if (h > 0 && v > 0) {
      throw new IllegalStateException("we got problems");
    }
    if (h == 0 && v == 0) {
      throw new IllegalStateException("NO ref lines");
    }
    return (h * 100) + v;
  }
}
