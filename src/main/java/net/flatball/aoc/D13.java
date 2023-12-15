package net.flatball.aoc;

import java.util.List;

/**
 * 27587: WINNER
 *
 * This one sucked because the wording of the problem was so nuanced to me, that I couldn't make out the
 * right logic for whether p1 ref lines should be used if new ones for p2 were different.  It was so
 * frustrated thatw I googled for p2's description being ambiguous and in reading the subreddit thread,
 * kinda got a spoiler where p2 is just a slight special case for p1 where if there's only 1 single
 * different bit/cell then it must be the smudged.  I at least didn't copy another implementation (unlike D12)
 * so I could at least implement that logic, but it's still frustrating to have wording keep snagging me
 * when I'm just barely treading water getting these done.
 */
public class D13 implements AOC {
  @Override
  public void run(int part, List<String> lines) {
    int start = 0;
    int total = 0;
    for (int lineIndex = 0; ; ) {
      while (lineIndex < lines.size()) {
        if (lines.get(lineIndex).isEmpty()) {
          break;
        }
        lineIndex++;
      }
      total += solve(part, lines.subList(start, lineIndex));
      if (lineIndex == lines.size()) {
        break;
      }
      lineIndex++;
      start = lineIndex;
    }

    System.out.println("PART " + part + " total is " + total);
  }


  static int[][] pattern(List<String> lines) {
    if (lines.isEmpty()) {
      throw new IllegalStateException("no empty pattern");
    }
    final int width = lines.getFirst().length();

    final int[][] view = new int[lines.size()][width];
    for (int row = 0; row < lines.size(); row++) {
      final String line = lines.get(row);
      for (int i = 0; i < line.length(); i++) {
        view[row][i] = switch (line.charAt(i)) {
          case '#' -> 1;
          case '.' -> 0;
          default -> throw new IllegalStateException("bad char");
        };
      }
    }
    return view;
  }

  static int[][] rotate(int[][] view) {
    final int[][] rot = new int[view[0].length][view.length];
    for (int row = 0; row < view[0].length; row++) {
      for (int i = 0; i < view.length; i++) {
        rot[row][i] = view[view.length - i - 1][row];
      }
    }
    return rot;
  }

  static int eq(int[] a, int[] b) {
    int count = 0;
    for (int i = 0; i < a.length; i++) {
      if (a[i] != b[i]) {
        count++;
      }
    }
    return count;
  }

  static int ref(int[][] v, int diff) {
    int line = 0;
    for (int i = 0; i < (v.length - 1); i++) {
      int f = i + 1;
      int b = i;
      int d = 0;
      while (b >= 0 && f < v.length) {
        d += eq(v[b], v[f]);
        if (d > diff) {
          break;
        }
        b--;
        f++;
      }
      if (d == diff) {
        line = i + 1;
        break;
      }
    }
    return line;
  }

  static void dump(int[][] v) {
    for (int row = 0; row < v.length; row++) {
      for (int i = 0; i < v[row].length; i++) {
        System.out.print(v[row][i] > 0 ? '#' : '.');
      }
      System.out.println();
    }
  }

  int solve(int part, List<String> pattern) {
    int[][] view = pattern(pattern);

    int diffs = part == 2 ? 1 : 0;

    int v = 0;
    int h = ref(view, diffs);
    if (h == 0) {
      final int[][] rotView = rotate(view);
      v = ref(rotView, diffs);
    }
    return (h * 100) + v;
  }
}
