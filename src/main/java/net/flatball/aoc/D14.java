package net.flatball.aoc;

import java.util.*;

/**
 * Thought I nailed this one after a quick early morning p1, but bogged down in P2. In AOC, you know you're gonna
 * be looking for cycles when they tell you to do something a billion times with a "spin". I spent a fair amount of
 * time just working through a way to model the tilting. I kept having the nagging feeling that there's just no
 * way actually calculating the rolling rocks is what they want right?? but turns out, that's pretty much what works
 * mostly because more carefully constructed puzzle data makes the cycle show up early so you don't get mega-punished
 * for brute-force code.  I again got bitten by semantics of problem not picking up when load should be
 * getting calculated.  I also cheated to get over the finish line to see some techniques of how to make the jump
 * from cycle detection (memoize FTW) to a final answer. One of those "X CYCLES + (BIG # % DELTA)" that I
 * just couldn't get right when the #s where staring me in the face.   Despite me now being 2 days behind,
 * i think i'm wearing down on this.  I wish the challenge had ended like the song: after 12 days.
 */
public class D14 implements AOC {
  @Override
  public void run(int part, List<String> lines) {
    if (lines.isEmpty()) {
      throw new IllegalStateException("no lines");
    }
    final Dish dish = new Dish(lines);
    int load;
    if (part == 1) {
      dish.north();
      load = dish.load();
    } else if (part == 2) {
      final Map<String, List<Integer>> memo = new HashMap<>();
      final List<Integer> loads = new ArrayList<>();
      for (int cycle = 0;;cycle++) {
        dish.spin();
        loads.add(dish.load());
        final String key = dish.key();
        final List<Integer> matches = memo.compute(key, (k, v) -> v == null ? new ArrayList<>() : v);
        matches.add(cycle);
        if (matches.size() > 1) {
          load = loads.get(matches.getFirst() + (1000000000 - 1 - matches.getFirst()) % (matches.getLast() - matches.getFirst()));
          break;
        }
      }
    } else {
      throw new IllegalStateException("bad part");
    }
    System.out.println("PART " + part + " load is " + load);
  }

  static class Dish {
    int dim;
    char[][] data;

    public Dish(List<String> lines) {
      if (lines.isEmpty()) {
        throw new IllegalStateException("no lines");
      }
      dim = lines.size();
      data = new char[lines.size()][lines.getFirst().length()];
      for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
        final String line = lines.get(lineIndex);
        if (line.length() != dim) {
          throw new IllegalStateException("line length mismatch");
        }
        for (int x = 0; x < line.length(); x++) {
          data[lineIndex][x] = line.charAt(x);
        }
      }
    }

    private Dish(int dim) {
      this.dim = dim;
      this.data = new char[dim][dim];
    }

   void spin() {
     north();
     west();
     south();
     east();
   }

    public void dump() {
      Arrays.stream(data).map(String::new).forEach(System.out::println);
    }

    int load() {
      int v = 0;
      for (int row = 0; row < dim; row++) {
        for (int x = 0; x < dim; x++) {
          if (data[row][x] == 'O') {
            v += (dim - row);
          }
        }
      }
      return v;
    }

    void north() {
      int[] offsets = new int[dim];
      for (int row = 0; row < dim; row++) {
        for (int x = 0; x < dim; x++) {
          final char ch = data[row][x];
          if (ch == '#') {
            offsets[x] = (row + 1);
          } else if (ch == 'O') {
            data[row][x] = '.';
            data[offsets[x]][x] = 'O';
            offsets[x]++;
          } else if (ch != '.') {
            throw new IllegalStateException("bad char: " + row);
          }
        }
      }
    }

    void west() {
      int[] offsets = new int[dim];
      for (int x = 0; x < dim; x++) {
        for (int row = 0; row < dim; row++) {
          final char ch = data[row][x];
          if (ch == '#') {
            offsets[row] = x + 1;
          } else if (ch == 'O') {
            data[row][x] = '.';
            data[row][offsets[row]] = 'O';
            offsets[row]++;
          } else if (ch != '.') {
            throw new IllegalStateException("bad char: " + ch);
          }
        }
      }
    }

    void south() {
      int[] offsets = new int[dim];
      Arrays.fill(offsets, dim - 1);
      for (int row = dim - 1; row >= 0; row--) {
        for (int x = 0; x < dim; x++) {
          final char ch = data[row][x];
          if (ch == '#') {
            offsets[x] = (row - 1);
          } else if (ch == 'O') {
            data[row][x] = '.';
            data[offsets[x]][x] = 'O';
            offsets[x]--;
          } else if (ch != '.') {
            throw new IllegalStateException("bad char: " + row);
          }
        }
      }
    }

    void east() {
      int[] offsets = new int[dim];
      Arrays.fill(offsets, dim - 1);
      for (int x = dim - 1; x >= 0; x--) {
        for (int row = 0; row < dim; row++) {
          final char ch = data[row][x];
          if (ch == '#') {
            offsets[row] = x - 1;
          } else if (ch == 'O') {
            data[row][x] = '.';
            data[row][offsets[row]] = 'O';
            offsets[row]--;
          } else if (ch != '.') {
            throw new IllegalStateException("bad char: " + ch);
          }
        }
      }
    }

    String key() {
      final StringBuilder b = new StringBuilder();
      Arrays.stream(data).forEach(b::append);
      return b.toString();
    }
  }
}
