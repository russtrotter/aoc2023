package net.flatball.aoc;

import java.util.*;
import java.util.stream.IntStream;

// PART 1 61865 ? yes!
public class D18 implements AOC {
  record Point(int y, int x) {}
  record Edge(int dy, int dx, int color) {}

  enum Op {
    U, D, L, R
  }
  final List<Edge> edges = new ArrayList<>();
  final List<Point> poly = new ArrayList<>();
  char[][] raster;
  int xMin;
  int xMax;
  int yMin;
  int yMax;

  @Override
  public void run(int part, List<String> lines) {
    graph(lines);
    System.out.println("Edges " + edges.size());
    System.out.println("Range: y=" + yMin + "->" + yMax + ":x=" + xMin + "->" + xMax);
    raster();
    //dump();
    int count = area();
    System.out.println("PART " + part + " is " + count);
  }



  int area() {
    // parity: intersect count is odd is inside
    int count = 0;
    for (char[] chars : raster) {
      for (char aChar : chars) {
        if (aChar != '.') {
          count++;
        }
      }
    }
    return count;
  }

  void dump() {
    Arrays.stream(raster).map(String::new).forEach(System.out::println);
  }

  void raster() {
    raster = new char[yMax - yMin + 1][xMax - xMin + 1];
    final List<List<Integer>> intersects = new ArrayList<>(raster.length);
    IntStream.range(0, raster.length).forEach(i -> intersects.add(new ArrayList<>()));
    Arrays.stream(raster).forEach(row -> Arrays.fill(row, '.'));
    int x = Math.abs(xMin);
    int y = Math.abs(yMin);
    for (Edge e : edges) {
      if (e.dx > 0) {
        Arrays.fill(raster[y], x + 1, x + e.dx + 1, 'R');
      } else if (e.dx < 0) {
        Arrays.fill(raster[y], x + e.dx, x + 1, 'L');
      }
      if (e.dy > 0) {
        for (int row = y; row < (y + e.dy); row++) {
          raster[row][x] = 'D';
          intersects.get(row).add(x);
        }
      } else if (e.dy < 0) {
        for (int row = (y + e.dy); row < y; row++) {
          raster[row][x] = 'U';
          intersects.get(row).add(x);
        }
      }
      x += e.dx;
      y += e.dy;
    }
    for (int row = 0; row < raster.length; row++) {
      final List<Integer> rowIntersects = intersects.get(row);
      if ((rowIntersects.size() % 2) != 0) {
        throw new IllegalStateException("bad intersects: " + row);
      }
      rowIntersects.sort(Comparator.naturalOrder());
      for (int i = 0; i < rowIntersects.size(); i += 2) {
        Arrays.fill(raster[row], rowIntersects.get(i), rowIntersects.get(i + 1), 'F');
      }
    }
  }

  void graph(List<String> lines) {
    int x = 0;
    int y = 0;
    for (String line: lines) {
      poly.add(new Point(y, x));
      String[] parts = line.split(" ");
      if (parts.length != 3) {
        throw new IllegalStateException("bad line: " + line);
      }
      Op op = Op.valueOf(parts[0]);
      int count = Integer.parseInt(parts[1]);
      int rgb = Integer.parseInt(parts[2].substring(2, 8), 16);
      int dx = 0;
      int dy = 0;
      switch (op) {
        case U -> dy = -count;
        case D -> dy = count;
        case L -> dx = -count;
        case R -> dx = count;
      }
      x += dx;
      y += dy;
      edges.add(new Edge(dy, dx, rgb));

      xMin = Math.min(xMin, x);
      yMin = Math.min(yMin, y);
      xMax = Math.max(xMax, x);
      yMax = Math.max(yMax, y);
    }
  }
}
