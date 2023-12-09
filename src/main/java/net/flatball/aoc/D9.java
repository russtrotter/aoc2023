package net.flatball.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * huh, I don't know if this one was easy or if I just got lucky.  I'm a little hesitant whenever the instructions
 * basically spell out an algorithm for what to do.  While correct, it's meant to teach a concept and the actual
 * code that finishes before the heat death of the universe takes some creativity.  Just to get something down
 * I implement the technique and run it on the example data and it works, give it a rip on the main input
 * with my mouse cursor hovering the "kill" button in case it just never comes back.  To my surprise, it
 * runs quickly to completion with an answer.  Part 1 is correct.   Surely I'll get crushed on part 2, right?
 * On a whim, I think, huh, part 2 kinda just looks like starting the processing in reverse? could it be that simple?
 * At least for this particular input apparently it was.  Part 2 also complete.  Probably the quickest day (for me)
 * yet.
 */
public class D9 implements AOC {
  @Override
  public void run(int part, List<String> lines) {

    int total = 0;
    for (String line : lines) {
      List<Integer> tails = new ArrayList<>();
      List<Integer> list = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
      if (part == 2) {
        list = list.reversed();
      }
      for (;;) {
        int zeros = 0;
        List<Integer> n = new ArrayList<>(list.size() - 1);
        tails.add(list.getLast());
        for (int i = 0; i < (list.size() - 1); i++) {
          int delta = list.get(i + 1) - list.get(i);
          if (delta == 0) {
            zeros++;
          }
          n.add(delta);
        }
        list = n;
        if (zeros == n.size()) {
          break;
        }
      }
      int next = 0;
      for (int i = tails.size() - 1; i >= 0; i--) {
        next += tails.get(i);
      }
      total += next;
      System.out.println("TAILS: " + tails + "=" + next);
    }
    System.out.println("PART " + part + " total " + total);
  }
}
