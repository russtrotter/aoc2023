package net.flatball.aoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Felt pretty good about the approach on this one.  The impl started off rough, but a pass after getting the
 * stars when the pressure is off to clean it up makes it much better.  Knew off the bat
 * some kinda of SPF algo is in play.  My key insight on my own was the expansion weighting factor.  My hunch
 * said to make it quickly computable without actually growing the # of vertices or some other "physical"
 * thing in the graph.  This was critical for part 2.  Probably more critical was the usage of a priority queue
 * for keeping adjacent edges managed during the SPF (thanks wikipedia!) algo to really make it fast.  It's one of those
 * cases where the linear search though a 2d array loses to a data structure.  Yay CS!
 * <p>
 * Edit: after reviewing some stuff on the subreddit, man, i kinda feel dumb.  Some fancy-pants SP algo is
 * totally unnecessary since the "Manhattan Distance" is equivalent for this case.  I spent way too much time
 * working that out and it was really for nothing.  Removed the SP function and just made a distance method
 * that computes the manhattan distance and adjusts for the expansion using the same 2 arrays for horz/vert
 * and it's crazy faster than what I had before.  There's still tons of optimizations that the real weens can do,
 * but for me, but I'm happy with this update.
 */
public class D11 implements AOC {
  record Vertex(int x, int y) {
  }

  int width;
  int height;

  int[] horzCount;
  int[] vertCount;

  int distance(Vertex src, Vertex dst, int weight) {
    final int xMin = Math.min(src.x, dst.x);
    final int xMax = Math.max(src.x, dst.x);
    final int yMin = Math.min(src.y, dst.y);
    final int yMax = Math.max(src.y, dst.y);

    int d = xMax - xMin;
    for (int x = xMin + 1; x < xMax; x++) {
      if (horzCount[x] == 0) {
        d += (weight - 1);
      }
    }
    d += (yMax - yMin);
    for (int y = yMin + 1; y < yMax; y++) {
      if (vertCount[y] == 0) {
        d += (weight - 1);
      }
    }
    return d;
  }


  @Override
  public void run(int part, List<String> lines) {
    if (lines.isEmpty()) {
      throw new IllegalStateException("no lines");
    }
    final List<Vertex> galaxies = new ArrayList<>();
    width = lines.getFirst().length();
    height = lines.size();
    if (width != height) {
      throw new IllegalStateException("not square");
    }
    horzCount = new int[width];
    vertCount = new int[height];
    for (int y = 0; y < lines.size(); y++) {
      final String line = lines.get(y);
      if (line.length() != width) {
        throw new IllegalStateException("bad line: " + y);
      }
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          galaxies.add(new Vertex(x, y));
          vertCount[y]++;
          horzCount[x]++;
        }
      }
    }
    if (galaxies.isEmpty()) {
      throw new IllegalStateException("no galaxies");
    }
    System.out.println("GALAXIES: " + galaxies.size());

    long total = 0;
    for (int g = 0; g < galaxies.size(); g++) {
      //System.out.println("PROCESSING SRC " + (g + 1));
      final Vertex from = galaxies.get(g);
      for (int to = g + 1; to < galaxies.size(); to++) {
        final Vertex dst = galaxies.get(to);
        int d = distance(from, dst, part == 2 ? 1000000 : 2);
        //System.out.println("ASMONGLER " + (g + 1) + " to " + (to + 1) + "=" + d);
        total += d;
      }
    }
    System.out.println("PART " + part + " total " + total);
  }
}
