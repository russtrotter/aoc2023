package net.flatball.aoc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class D16 implements AOC {
  Grid grid;
  Dir[][] intersect;

  @Override
  public void run(int part, List<String> lines) {
    int result;
    this.grid = new Grid(lines);
    if (part == 1) {
      intersect = new Dir[grid.dim][grid.dim];
      Nav nav = new Nav();
      beam(nav);
      result = 0;
      for (Dir[] dirs : intersect) {
        for (Dir dir : dirs) {
          result += dir == null ? 0 : 1;
        }
      }
    } else {
      result = -1;
    }
    System.out.println("PART " + part + " result is " + result);
  }

  void beam(Nav nav) {
    for (;;) {
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
