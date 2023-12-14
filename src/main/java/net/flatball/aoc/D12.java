package net.flatball.aoc;

import java.util.*;

/*
0001     1
0010     2
0100     4
0101     5
1000     8
1001     9
1010    10


???.### 1,1,3
xxx0111        1010111


.??..??...?##. 1,1,3
0xx00xx000x110  1010111
xx0xx0x11



?#?#?#?#?#?#?#? 1,3,1,6
x1x1x1x1x1x1x1x
010111010111111
????.#...#... 4,1,1

????.######..#####. 1,6,5


?###???????? 3,2,1


 */

/*
MAXBITS: 20
MAXRANGE: 14
MAX TOTAL OF DAMAGE BITS: 17


10 150 2250
 */

/**
 * failed AOC on this day. First time didn't get the 2nd star in time for the next puzzle got released.
 * did a bit more work on it over the next day but still struggled getting something fast enough for part2.
 * I'd keep converging towards the same bad idea.  Decided to just google some solutions/hints/tricks for
 * part 2 and saw a nice python impl using a memoized cache of strings+damage-lists.  If i had thought
 * caching I think i could have gotten it done that day, but I didn't, thus failure.  Reflecting back the cache idea
 * makes good sense since there's probably many patterns duplicated.  I think I was also onto something
 * where ? chars in the input get transformed into 2 versions of the puzzle, each branch solved recursively
 * rather than trying to dance with the ? char.  Hopefully some lessons learned, but I'm feeling defeated and
 * yet strangely relieved of the pressure of trying to bag the whole AOC.
 */
public class D12 implements AOC {

  @Override
  public void run(int part, List<String> lines) {

    long total = 0;
    int maxFoo = 0;
    for (String line : lines) {
      int index = line.indexOf(' ');
      if (index == -1) {
        throw new IllegalStateException("Missing space");
      }
      String conditions = line.substring(0, index);
      List<Integer> damaged = Arrays.stream(line.substring(index + 1).split(",")).map(Integer::parseInt).toList();
      if (part == 2) {
        final List<String> cond = new ArrayList<>(5);
        final List<Integer> dmg = new ArrayList<>(damaged.size() * 5);
        for (int i = 0; i < 5; i++) {
          cond.add(conditions);
          dmg.addAll(damaged);
        }
        conditions = String.join("?", cond);
        damaged = dmg;
      }

      long found = solverRec(conditions, damaged);
      System.out.println("COMBOS " + conditions + " " + damaged + "=" + found);
      total += found;

//      this.ranges = new ArrayList<>();
//
//      int tots = damaged.size();
//      this.damageMask = 0;
//      for (int i = 0; i < damaged.size(); i++) {
//        if (i > 0) {
//          this.damageMask <<= 1;
//        }
//        int d = damaged.get(i);
//        tots += d;
//        for (; d > 0; d--) {
//          this.damageMask = (damageMask << 1) | 1;
//        }
//      }
//      maxFoo = Math.max(maxFoo, tots);


//      this.ranges = new ArrayList<>();
//      for (int i = 0; i < damaged.size(); i++) {
//        int d = damaged.get(i);
//        for (; d > 0; d--) {
//          this.ranges.add(1);
//        }
//        this.ranges.add(0);
//      }

//      int times = 1;
//      if (part == 2) {
//        times = 5;
//      }
//      int found = 0;
//      char [] a = conditions.toCharArray();
//      for (int iter = 0; iter < times; iter++) {
//        if (iter > 0) {
//          a = ("?" + conditions).toCharArray();
//        }
//        int f = foo5(0, a, 0);
//        System.out.println("ITER " + iter + "=" + f);
//      }

      // ######
//      int found = foo4(0, conditions, 0, "");
      //System.out.println("LINE " + conditions + ":" + damaged + "=" + found);
      //total += found;
      // ######



//
//      final List<Integer>  queue = new ArrayList<>();
//      int found = foo3(queue, conditions, 0);
//      System.out.println("LINE " + conditions + ":" + damaged + "=" + found);
//      total += found;
    }
    System.out.println("PART " + part + " total is " + total);
    System.out.println("MAXFOO " + maxFoo);
  }


  record Key(String puz, List<Integer> scores) {}
  private final Map<Key, Long> memo = new HashMap<>();

  long solverRec(String puz, List<Integer> scores) {
    final Key key = new Key(puz, scores);
    Long m = memo.get(key);
    if (m != null) {
      return m;
    }
    long value = solverRecInner(puz, scores);
    memo.put(key, value);
    return value;
  }

  long solverRecInner(String puz, List<Integer> scores) {
    int hashesLeft = scores.stream().reduce(0, Integer::sum);
    if (puz.length() < hashesLeft) {
      return 0;
    }
    if (puz.isEmpty()) {
      return hashesLeft > 0 ? 0 : 1;
    }
    if (puz.charAt(0) == '.') {
      return solverRec(puz.substring(1), scores);
    }
    if (puz.charAt(0) == '#') {
      if (scores.isEmpty()) {
        return 0;
      }
      int curScore = scores.getFirst();
      List<Integer> rest = scores.subList(1, scores.size());
      for (int i = 0; i < curScore; i++) {
        if (puz.charAt(i) == '.') {
          return 0;
        }
      }
      if (puz.length() == curScore && rest.isEmpty()) {
        return 1;
      }
      if (puz.charAt(curScore) == '#') {
        return 0;
      }
      return solverRec(puz.substring(curScore + 1), rest);
    }
    if (puz.charAt(0) == '?') {
      int hashCount = 0;
      int qCount = 0;
      for (int i = 0; i < puz.length(); i++) {
        switch (puz.charAt(i)) {
          case '?' -> qCount++;
          case '#' -> hashCount++;
        }
      }
      if ((qCount + hashCount) > hashesLeft) {
        return solverRec("#" + puz.substring(1), scores)
                + solverRec("." + puz.substring(1), scores);
      } else {
        return solverRec("#" + puz.substring(1), scores);
      }
    }
    throw new IllegalStateException("unreachable");
  }


  int damageMask;

  int foo5(int bits, char[] b, int pos) {
    //String stuff = new String(b);
    //System.out.println("STUFF: " + stuff);
    //String dmgStr = Integer.toBinaryString(damageMask);
    //String bitStr = Integer.toBinaryString(bits);
    //System.out.println("DMG " + dmgStr + " : " + bitStr);
    if (pos == b.length) {
      if (bits == 0) {
        return 0;
      }
      while ((bits & 1) == 0) {
        bits >>>= 1;
      }
      if ((bits ^ damageMask) == 0) {
        return 1;
      }
      return 0;
    }
    int count = 0;
    char ch = b[pos];
    if (ch == '#') {
      bits = (bits << 1) | 1;
      count = foo5(bits, b, pos + 1);
    } else if (ch == '.') {
      if ((bits & 1) != 0) {
        bits = (bits << 1);
      }
      count = foo5(bits, b, pos + 1);
    } else if (ch == '?') {
      b[pos] = '#';
      count += foo5(bits, b, pos);
      b[pos] = '.';
      count += foo5(bits, b, pos);
      b[pos] = '?';
    } else {
      throw new IllegalStateException("bad: " + ch);
    }
    return count;
  }

  int foo4(int r, String str, int pos, String chain) {
    if (pos == str.length()) {
      if (r == ranges.size()) {
        System.out.println("WINNER BEANS " + chain);
        return 1;
      }
      return 0;
    }
    char ch = str.charAt(pos);
    if (ch == '#') {
      if (r == ranges.size() || ranges.get(r) != 1) {
        return 0;
      }
      return foo4(r + 1, str, pos + 1, chain + "#");
    } else if (ch == '.') {
      if (r < ranges.size() && ranges.get(r) == 0) {
        r++;
      }
      return foo4(r, str, pos + 1, chain + "a");
    } else if (ch == '?') {
      int count = 0;
      if (r < ranges.size() && ranges.get(r) == 1) {
        count += foo4(r + 1, str, pos + 1, chain + "#");
      }
      if (r < ranges.size() && ranges.get(r) == 0) {
        r++;
      }
      if (count == 0) {
        count += foo4(r, str, pos + 1, chain + "b");
      }

      return count;
    } else throw new IllegalStateException("bad char: " + str);
  }


  List<Integer> ranges;


  int foo3(List<Integer> stack, String str, int pos) {
    int j = 0;

    List<Integer> b = new ArrayList<>();
    while (j < stack.size()) {
      int v = stack.get(j);

      if (v > 0) {
        b.add(v);
        j++;
      } else {
        for (j++; j < stack.size(); j++) {
          if (stack.get(j) != 0) {
            break;
          }
        }
        if (!b.isEmpty() && j < stack.size()) {
          b.add(v);
        }
      }
    }

    /*
    j = 0;

    for (; j < stack.size(); j++) {
      if (stack.get(j) != 0) {
        break;
      }
    }
    while (j < stack.size() && k < ranges.size()) {
       int vj = stack.get(j++);
       if (vj == 0) {
         for (; j < stack.size(); j++) {
           if (stack.get(j) != 0) {
             break;
           }
         }
       }
       int vk = ranges.get(k++);
       if (vj != vk) {
         return 0;
       }
    }
    for (; j < stack.size(); j++) {
      if (stack.get(j) != 0) {
        break;
      }
    }
    if (j < stack.size() && k == ranges.size()) {
      return 0;
    }
     */

    if (b.size() > ranges.size()) {
      return 0;
    }

    if (!ranges.subList(0, b.size()).equals(b)) {
      return 0;
    }
    if (pos == str.length()) {
      if (b.size() != ranges.size()) {
        //System.out.println("WTF");
        return 0;
      }
      return 1;
    }
    int found;
    final char c = str.charAt(pos);
    if (c == '#') {
      stack.addLast(1);
      found = foo3(stack, str, pos + 1);
      stack.removeLast();
    } else if (c == '.') {
      stack.addLast(0);
      found = foo3(stack, str, pos + 1);
      stack.removeLast();
    } else if (c == '?') {
      stack.addLast(1);
      found = foo3(stack, str, pos + 1);
      stack.removeLast();
      stack.addLast(0);
      found += foo3(stack, str, pos + 1);
      stack.removeLast();
      return found;
    } else {
      throw new IllegalStateException("bad char: " + str);
    }
    return found;
  }

  int foo2(int m, String str, int pos, int range) {
    if (pos == str.length()) {
      if (range == ranges.size() || (range == (ranges.size() - 1) && m == ranges.get(range))) {
        return 1;
      }
      return 0;
    }
    char c = str.charAt(pos);
    if (c == '#') {
      return foo2(m + 1, str, pos + 1, range);
    } else if (c == '.' || c == '?') {
      int found = 0;
      if (m == 0) {
        found += foo2(0, str, pos + 1, range);
      } else if (m > 0 && range < ranges.size() && m == ranges.get(range)) {
        found += foo2(0, str, pos + 1, range + 1);
      }
      if (c == '?') {
        found += foo2(m + 1, str, pos + 1, range);
      }
      return found;
    } else {
      throw new IllegalStateException("bad input: " + str);
    }
  }

  int foo(int value, int m, String str, int pos, int range) {
    if (pos == str.length()) {
      if ((value & 1) == 0) {
        value >>= 1;
      }
      return range == value ? 1 : 0;
    }
    char c = str.charAt(pos);
    if (c == '#') {
      value = (value << 1) | 1;
      return foo(value, m + 1, str, pos + 1, range);
    } else if (c == '.' || c == '?') {
      int found = 0;
      if (c == '?') {
        found = foo((value << 1) | 1, m + 1, str, pos + 1, range);
      }
      if ((value & 1) > 0) {
        value <<= 1;
        m++;
      }
      found += foo(value, m, str, pos + 1, range);
      return found;
    } else {
      throw new IllegalStateException("bad input: " + str);
    }
  }
}
