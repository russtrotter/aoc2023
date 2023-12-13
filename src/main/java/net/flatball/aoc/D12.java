package net.flatball.aoc;

import java.util.*;
import java.util.stream.IntStream;

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
 */

public class D12 implements AOC {

  @Override
  public void run(int part, List<String> lines) {
    int maxBits = 0;
    int total = 0;
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

      //this.ranges = damaged;

      this.rangeBits = damaged.stream().reduce(0, (a,b)->a + b + 1) - 1;
      System.out.println("RANGEBITS " + rangeBits);
      this.ranges = new ArrayList<>();
      for (int i = 0; i < damaged.size(); i++) {
        if (i > 0) {
          this.ranges.add(0);
        }
        int d = damaged.get(i);
        for (; d > 0; d--) {
          this.ranges.add(1);
        }
      }

      final List<Integer>  queue = new ArrayList<>();
      int found = foo3(queue, conditions, 0);
      System.out.println("LINE " + conditions + ":" + damaged + "=" + found);
      total += found;
    }
    System.out.println("PART " + part + " total is " + total);
  }

  List<Integer> ranges;
  int rangeBits = 0;

  int foo3(List<Integer> stack, String str, int pos) {
    int j = 0;

    List<Integer> b = new ArrayList<>();
    while (j < stack.size()) {
      int v = stack.get(j);

      if (v > 0) {
        b.add(v);
        j++;
      } else {
        for (j++;j < stack.size(); j++) {
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
