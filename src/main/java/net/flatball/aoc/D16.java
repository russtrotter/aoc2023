package net.flatball.aoc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Another not-so-bad day.  Thought this one was gonna be P=NP after D15.  I'll take it though.  This one was fun.
 * I think i kinda lucked into not falling into a brute-force trap by adding in cycle detection for encountering
 * a grid location *in the same direction* thus short-circuiting recursion.  My early test runs
 * were revealing infinite-loop behaviors on small data sets.  I don't know for sure because i had
 * some early bugs in the 90-degree deflection pipes so maybe another case where carefully constructed test data
 * had minimal cycles.  The primary dataset for sure hit the detection code so perhaps it just chugs longer if you
 * don't put that detection in?
 */
public class D16 implements AOC {
  Grid grid;

  record Key(int y, int x) {
  }

  final Set<Key> energized = new HashSet<>();
  Dir[][] intersect;

  @Override
  public void run(int part, List<String> lines) {
    int result;
    this.grid = new Grid(lines);
    if (part == 1) {
      result = cast(new Nav());
    } else if (part == 2) {
      result = Integer.MIN_VALUE;
      for (int row = 0; row < grid.dim; row++) {
        if (row == 0) {
          // go down
          for (int x = 0; x < grid.dim; x++) {
            result = Math.max(result, cast(new Nav(row, x, Dir.DOWN)));
          }
        } else if (row == (grid.dim - 1)) {
          // go up
          for (int x = 0; x < grid.dim; x++) {
            result = Math.max(result, cast(new Nav(row, x, Dir.UP)));
          }
        }
        // go right
        result = Math.max(result, cast(new Nav(row, 0, Dir.RIGHT)));
        // go left
        result = Math.max(result, cast(new Nav(row, grid.dim - 1, Dir.LEFT)));
      }
    } else {
      throw new IllegalStateException("bad part: " + part);
    }
    System.out.println("PART " + part + " result is " + result);
  }

  int cast(Nav nav) {
    intersect = new Dir[grid.dim][grid.dim];
    energized.clear();
    beam(nav);
    return energized.size();
  }

  void beam(Nav nav) {
    for (; ; ) {
      if (nav.x < 0 || nav.x >= grid.dim) {
        return;
      }
      if (nav.y < 0 || nav.y >= grid.dim) {
        return;
      }
      if (nav.dir.equals(intersect[nav.y][nav.x])) {
        return;
      }
      intersect[nav.y][nav.x] = nav.dir;
      energized.add(new Key(nav.y, nav.x));

      switch (grid.data[nav.y][nav.x]) {
        case '.' -> nav.space();
        case '\\' -> {
          if (Dir.RIGHT.equals(nav.dir)) {
            nav.down();
          } else if (Dir.DOWN.equals(nav.dir)) {
            nav.right();
          } else if (Dir.UP.equals(nav.dir)) {
            nav.left();
          } else if (Dir.LEFT.equals(nav.dir)) {
            nav.up();
          } else {
            throw new IllegalStateException("BACKSLASH fail: " + nav.dir);
          }
        }
        case '/' -> {
          if (Dir.UP.equals(nav.dir)) {
            nav.right();
          } else if (Dir.LEFT.equals(nav.dir)) {
            nav.down();
          } else if (Dir.RIGHT.equals(nav.dir)) {
            nav.up();
          } else if (Dir.DOWN.equals(nav.dir)) {
            nav.left();
          } else {
            throw new IllegalStateException("SLASH fail: " + nav.dir);
          }
        }
        case '|' -> {
          if (nav.dir.isVertical()) {
            nav.space();
          } else {
            // new beam that goes up
            Nav up = nav.copy();
            up.up();
            beam(up);
            // orig beam goes down
            nav.down();
          }
        }
        case '-' -> {
          if (nav.dir.isHorizontal()) {
            nav.space();
          } else {
            // new beam left
            Nav left = nav.copy();
            left.left();
            beam(left);
            // orig beam goes right
            nav.right();
          }
        }
        default -> throw new IllegalStateException("bad char: " + nav);
      }
    }
  }

  record Dir(int dy, int dx) {
    static Dir LEFT = new Dir(0, -1);
    static Dir RIGHT = new Dir(0, 1);
    static Dir UP = new Dir(-1, 0);
    static Dir DOWN = new Dir(1, 0);

    boolean isVertical() {
      return dx == 0 && dy != 0;
    }

    boolean isHorizontal() {
      return dy == 0 && dx != 0;
    }
  }

  static class Nav {
    int y;
    int x;
    Dir dir;

    Nav() {
      x = y = 0;
      dir = Dir.RIGHT;
    }

    Nav(int y, int x, Dir dir) {
      this.y = y;
      this.x = x;
      this.dir = dir;
    }

    Nav copy() {
      Nav s = new Nav();
      s.x = x;
      s.y = y;
      s.dir = dir;
      return s;
    }

    void space() {
      y += dir.dy;
      x += dir.dx;
    }

    void left() {
      dir = Dir.LEFT;
      x += dir.dx;
    }

    void right() {
      dir = Dir.RIGHT;
      x += dir.dx;
    }

    void down() {
      dir = Dir.DOWN;
      y += dir.dy;
    }

    void up() {
      dir = Dir.UP;
      y += dir.dy;
    }

    @Override
    public String toString() {
      return "Nav{" +
              "y=" + y +
              ", x=" + x +
              ", dir=" + dir +
              '}';
    }
  }

  static class Grid {
    char[][] data;
    int dim;

    Grid(List<String> lines) {
      dim = lines.size();
      data = new char[dim][];
      for (int row = 0; row < lines.size(); row++) {
        final String line = lines.get(row);
        if (line.length() != dim) {
          throw new IllegalStateException("bad dim");
        }
        data[row] = line.toCharArray();
      }
    }
  }
}
