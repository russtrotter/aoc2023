package net.flatball.aoc;

import java.util.*;
import java.util.stream.Stream;

/**
 * Felt pretty good about the approach on this one.  The impl started off rough, but a pass after getting the
 * stars when the pressure is off to clean it up makes it much better.  Knew off the bat
 * some kinda of SPF algo is in play.  My key insight on my own was the expansion weighting factor.  My hunch
 * said to make it quickly computable without actually growing the # of vertices or some other "physical"
 * thing in the graph.  This was critical for part 2.  Probably more critical was the usage of a priority queue
 * for keeping adjacent edges managed during the SPF (thanks wikipedia!) algo to really make it fast.  It's one of those
 * cases where the linear search though a 2d array loses to a data structure.  Yay CS!
 */
public class D11 implements AOC {
  record Vertex(int x, int y) {
  }

  int width;
  int height;

  int[] horzCount;
  int[] vertCount;


  int dijkstra(Vertex src, Vertex dst, int weight) {
    final int[][] dist = new int[height][width];
    final PriorityQueue<Vertex> queue = new PriorityQueue<>(Comparator.comparingInt(v -> dist[v.y][v.x]));
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        dist[y][x] = Integer.MAX_VALUE;
      }
    }
    // Distance of source vertex from itself is always 0
    dist[src.y][src.x] = 0;
    queue.add(src);

    final int xMin = Math.max(0, Math.min(src.x, dst.x));
    final int xMax = Math.min(width, Math.max(src.x, dst.x));
    final int yMin = Math.max(0, Math.min(src.y, dst.y));
    final int yMax = Math.min(height, Math.max(src.y, dst.y));


    for (; ; ) {
      if (queue.isEmpty()) {
        throw new IllegalStateException("queue should not empty before finding dest");
      }
      final Vertex vert = queue.remove();
      if (vert.equals(dst)) {
        return dist[vert.y][vert.x];
      }

      final Vertex up = vert.y > yMin ? new Vertex(vert.x, vert.y - 1) : null;
      final Vertex right = vert.x < xMax ? new Vertex(vert.x + 1, vert.y) : null;
      final Vertex down = vert.y < yMax ? new Vertex(vert.x, vert.y + 1) : null;
      final Vertex left = vert.x > xMin ? new Vertex(vert.x - 1, vert.y) : null;

      Stream.of(up, right, down, left).filter(Objects::nonNull).forEach(adj -> {
        int w = 1;
        if (adj.x != vert.x && horzCount[adj.x] == 0) {
          w *= weight;
        }
        if (adj.y != vert.y && vertCount[adj.y] == 0) {
          w *= weight;
        }
        int d = dist[vert.y][vert.x] + w;
        final int adjDist = dist[adj.y][adj.x];
        if (d < adjDist) {
          dist[adj.y][adj.x] = d;
          if (!queue.contains(adj)) {
            queue.add(adj);
          }
        }
      });
    }
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
      System.out.println("PROCESSING SRC " + (g + 1));
      final Vertex from = galaxies.get(g);
      for (int to = g + 1; to < galaxies.size(); to++) {
        final Vertex dst = galaxies.get(to);
        int d = dijkstra(from, dst, part == 2 ? 1000000 : 2);
        total += d;
      }
    }
    System.out.println("PART " + part + " total " + total);
  }
}
