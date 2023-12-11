package net.flatball.aoc;

import java.util.*;
import java.util.stream.Stream;

/**
 * Another klunky one.  Got 2 stars out of it, but I don't like my solution, don't
 * like the organization of it, don't like the kludgery.  Blech Blech.  I *think* building
 * a 2d array was the right idea but again, i feel like there's good AOCers doing this in a
 * single pass or something inventive.  I spent many little blocks over the day fiddling
 * with polyfill algorithm I googled, but not really understanding and being frustrated at
 * myself for that.  Had to settle on a 2d raster thing to "render" the path onto and then
 * run the fill algo on it and don't overwrite the path edges to achieve the "exclusive" fill
 * that the problem was asking for.
 */
public class D10 implements AOC {
  static class Point {
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    int x;
    int y;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }

    @Override
    public String toString() {
      return "Point{" +
              "x=" + x +
              ", y=" + y +
              '}';
    }
  }

  static class Pipe {
    Point loc;
    final List<Point> joins = new ArrayList<>(2);

    public Pipe(Point loc) {
      this.loc = loc;
    }

    Point through(Point from) {
      if (joins.isEmpty()) {
        throw new IllegalStateException("HOW WE GET HERE");
      }
      if (from.equals(joins.getFirst())) {
        return joins.getLast();
      } else if (from.equals(joins.getLast())) {
        return joins.getFirst();
      } else {
        throw new IllegalStateException("no go through");
      }
    }

    void join(Point... points) {
      for (Point p : points) {
        if (p != null) {
          joins.add(p);
        }
      }
    }

    public boolean has(Point p) {
      return joins.contains(p);
    }
  }

  static class Map2 {
    Pipe[][] grid;
    Point start;

    public Map2(List<String> lines) {
      grid = new Pipe[lines.size()][];

      for (int y = 0; y < lines.size(); y++) {
        final String line = lines.get(y);
        grid[y] = new Pipe[line.length()];
        for (int x = 0; x < line.length(); x++) {
          final Point up = y > 0 ? new Point(x, y - 1) : null;
          final Point right = x < (line.length() - 1) ? new Point(x + 1, y) : null;
          final Point down = y < (lines.size() - 1) ? new Point(x, y + 1) : null;
          final Point left = x > 0 ? new Point(x - 1, y) : null;
          final Pipe pipe = new Pipe(new Point(x, y));
          grid[y][x] = pipe;
          switch (line.charAt(x)) {
            case '|' -> pipe.join(up, down);
            case '-' -> pipe.join(left, right);
            case 'L' -> pipe.join(up, right);
            case 'J' -> pipe.join(left, up);
            case '7' -> pipe.join(left, down);
            case 'F' -> pipe.join(right, down);
            case 'S' -> {
              if (start != null) {
                throw new IllegalStateException("Duplicate start: " + x + " " + y);
              }
              start = new Point(x, y);
            }
            case '.' -> {
              // ignore ground tile
            }
            default -> throw new IllegalStateException("Unexpected char at " + x + " " + y);
          }
        }
      }
      if (start == null) {
        throw new IllegalStateException("NO start");
      }
      System.out.println("START POINT " + start);

    }

    List<Pipe> begin() {
      return
              Stream.of(new Point(start.x, start.y - 1), new Point(start.x + 1, start.y), new Point(start.x, start.y + 1), new Point(start.x - 1, start.y))
                      .filter(p -> p.x >= 0 && p.y >= 0 && p.x < grid[0].length && p.y < grid.length)
                      .map(p -> grid[p.y][p.x])
                      .filter(pipe -> pipe != null && pipe.has(start))
                      .toList();
    }
  }

  static class Raster {
    char[][] m;

    public Raster(int width, int height) {
      m = new char[height][];
      for (int y = 0; y < height; y++) {
        m[y] = new char[width];
        for (int x = 0; x < width; x++) {
          m[y][x] = ' ';
        }
      }
    }

    void points(List<Point> loop) {
      for (Point p : loop) {
        m[p.y][p.x] = 'X';
      }
    }

    void dump() {
      for (char[] chars : m) {
        System.out.println(chars);
      }
    }

    void fill(List<Point> loop) {
      int i, j;
      int ymin = Integer.MAX_VALUE, ymax = 0;

      // Find the minimum and maximum x-coordinates of the polygon
      for (Point p : loop) {
        ymin = Math.min(p.y, ymin);
        ymax = Math.max(p.y, ymax);
      }
      int area = 0;

      // Scan each scan-line within the polygon's vertical extent
      for (i = ymin + 1; i <= ymax; i++) {
        // Initialize an array to store the intersection points
        final List<Integer> interPoints = new ArrayList<>(loop.size());

        for (j = 0; j < loop.size(); j++) {
          final Point next = loop.get((j + 1) % loop.size());
          final Point jp = loop.get(j);

          // Check if the current edge intersects with the scan line
          if ((jp.y > i && next.y <= i) || (next.y > i && jp.y <= i)) {
            interPoints.add(jp.x + (i - jp.y) * (next.x - jp.x) / (next.y - jp.y));
          }
        }

        // Sort the intersection points in ascending order
        interPoints.sort(Comparator.naturalOrder());

        // Fill the pixels between pairs of intersection points
        for (j = 0; j < interPoints.size(); j += 2) {
          for (int x = interPoints.get(j) + 1; x < interPoints.get(j + 1); x++) {
            if (m[i][x] != 'X') {
              m[i][x] = 'F';
              area++;
            } else {
              System.out.println("COLLI " + x + "," + i);
            }
          }
          //System.out.println("FILE line=" + i + " x=" + interPoints.get(j) + "-" + interPoints.get(j + 1));
          //line(interPoints[j], i, interPoints[j + 1], i);
        }
      }
      System.out.println("AREA: " + area);
    }
  }



  @Override
  public void run(int part, List<String> lines) {
    final Map2 map = new Map2(lines);
    List<Pipe> begin = map.begin();
    System.out.println("BEGIN " + begin);
    if (begin.size() != 2) {
      throw new IllegalStateException("No begins");
    }
    if (part == 1) {
      Point p1 = map.start;
      Point p2 = map.start;
      Pipe dir1 = begin.getFirst();
      Pipe dir2 = begin.getLast();
      int distance = 0;
      do {
        Point next1 = dir1.through(p1);
        if (next1 == null) {
          throw new IllegalStateException("not good 1");
        }
        p1 = dir1.loc;
        dir1 = map.grid[next1.y][next1.x];

        Point next2 = dir2.through(p2);
        if (next2 == null) {
          throw new IllegalStateException("not good 2");
        }
        p2 = dir2.loc;
        dir2 = map.grid[next2.y][next2.x];
        distance++;
      } while (!p1.equals(p2));
      System.out.println("PART " + part + " result " + distance);
    } else {
      Point p = map.start;
      Pipe dir = begin.getFirst();
      final List<Point> loop = new ArrayList<>();
      do {
        loop.add(p);
        Point next = dir.through(p);
        if (next == null) {
          throw new IllegalStateException("not good dude");
        }
        p = dir.loc;
        dir = map.grid[next.y][next.x];
        if (next.equals(map.start)) {
          break;
        }
      } while (!p.equals(map.start));
      final Raster raster = new Raster(map.grid[0].length, map.grid.length);
      System.out.println("LOOP IS LOOP " + loop);
      raster.points(loop);
      raster.dump();
      System.out.println("LOOP SIZE: " + loop.size());
      raster.fill(loop);
      raster.dump();
    }
  }
}
