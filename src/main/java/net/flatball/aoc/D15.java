package net.flatball.aoc;

import java.util.*;
import java.util.stream.IntStream;

/**
 * finally a relatively easy one.  I only waffled a bit on impl strategy to use some existing collection
 * or manually making a hash table.  I opted for the latter as I could really make sure the
 * slot order of each lens would be easy with a list.  After grafting the problem spec on any attempt
 * with an existing collection i found myself doing so much manual stuff anyway, just go with the straightfoward
 * strategy.
 */
public class D15 implements AOC {
  record Lens(String label, int focus) {
    @Override
    public boolean equals(Object obj) {
      if (obj instanceof Lens other) {
        return label.equals(other.label);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return label.hashCode();
    }
  }

  @Override
  public void run(int part, List<String> lines) {
    if (lines.size() != 1) {
      throw new IllegalStateException("wrong line count");
    }
    int result;
    if (part == 1) {
      result = Arrays.stream(lines.getFirst().split(",")).map(D15::hash).reduce(0, Integer::sum);
    } else {
      final List<List<Lens>> table = new ArrayList<>(256);
      IntStream.range(0, 256).forEach(i -> table.add(null));
      Arrays.stream(lines.getFirst().split(",")).forEach(str -> {
        char op = 0;
        String label = null;
        String valStr = null;
        for (int i = 0; i < str.length(); i++) {
          final char ch = str.charAt(i);
          if (ch == '=' || ch == '-') {
            op = ch;
            label = str.substring(0, i);
            valStr = str.substring(i + 1);
            break;
          }
        }
        switch (op) {
          case '=' -> {
            final int box = hash(label);
            List<Lens> slots = table.get(box);
            if (slots == null) {
              slots = new ArrayList<>();
              table.set(box, slots);
            }
            final Lens lens = new Lens(label, Integer.parseInt(valStr));
            int exists = slots.indexOf(lens);
            if (exists == -1) {
              slots.addLast(lens);
            } else {
              slots.set(exists, lens);
            }
          }
          case '-' -> {
            final int box = hash(label);
            List<Lens> slots = table.get(box);
            if (slots != null) {
              slots.remove(new Lens(label, 0));
            }
          }
          default -> throw new IllegalStateException("Illegal op: " + op);
        }
      });
      result = 0;
      for (int box = 0; box < table.size(); box++) {
        final List<Lens> slots = table.get(box);
        if (slots == null) {
          continue;
        }
        for (int slot = 0; slot < slots.size(); slot++) {
          result += ((box + 1) * (slot + 1) * slots.get(slot).focus());
        }
      }
    }
    System.out.println("PART " + part + " result is " + result);
  }

  static int hash(String str) {
    int v = 0;
    for (int i = 0; i < str.length(); i++) {
      final char ch = str.charAt(i);
      v += ch;
      v *= 17;
      v %= 256;
    }
    return v;
  }
}
