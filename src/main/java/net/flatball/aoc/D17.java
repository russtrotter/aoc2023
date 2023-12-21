package net.flatball.aoc;

import java.util.*;

public class D17 implements AOC {
  int[][] weights;
  boolean[][] visits;
  int[][] costs;
  int minPath;
  int dim;


  enum Dir {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    final int dy;
    final int dx;

    Dir(int dy, int dx) {
      this.dy = dy;
      this.dx = dx;
    }

    boolean opposite(Dir dir) {
      return switch (dir) {
        case UP -> DOWN.equals(this);
        case DOWN -> UP.equals(this);
        case LEFT -> RIGHT.equals(this);
        case RIGHT -> LEFT.equals(this);
      };
    }
    boolean isHorizontal() {
      return this == LEFT || this == RIGHT;
    }
    boolean isVertical() {
      return this == UP || this == DOWN;
    }
  }

  record Point(int y, int x) {
    Point go(Dir dir) {
      return new Point(y + dir.dy, x + dir.dx);
    }
  }

  @Override
  public void run(int part, List<String> lines) {
    grid(lines);
    int result = 0;
    if (part == 1) {
      result = drive(new Point(0, 0), new Point(dim - 1, dim - 1));
    }
    System.out.println("PART " + part + " is " + result);
  }


  record Node(Point p, Dir dir, int streak) {}


  int drive(Point location, Point destination) {
    final Map<Node, Integer> distances = new HashMap<>();
    final PriorityQueue<Node> stack = new PriorityQueue<>(dim * dim, Comparator.comparingInt(n -> distances.getOrDefault(n, Integer.MAX_VALUE)));
    final Node root = new Node(location, Dir.RIGHT, 0);
    stack.add(root);
    distances.put(root, 0);

    while (!stack.isEmpty()) {
      final Node node = stack.remove();
      final Point pt = node.p;
      if (pt.equals(destination)) {
        return distances.get(node);
      }
      for (Dir dir : Dir.values()) {
        final Point adj = pt.go(dir);
        if (adj.y < 0 || adj.y >= dim) {
          continue;
        }
        if (adj.x < 0 || adj.x >= dim) {
          continue;
        }
        if (node.dir != null && dir.opposite(node.dir)) {
          continue;
        }
        final int streak = dir.equals(node.dir) ? node.streak + 1 : 1;
        if (streak > 3) {
          continue;
        }
        final Node adjNode = new Node(adj, dir, streak);
        int ptd = distances.getOrDefault(node, Integer.MAX_VALUE);
        if (ptd == Integer.MAX_VALUE) {
          throw new IllegalStateException("wonk d");
        }
        int d = ptd + weights[adj.y][adj.x];
        if (d < distances.getOrDefault(adjNode, Integer.MAX_VALUE)) {
          distances.put(adjNode, d);
          if (!stack.add(adjNode)) {
            System.out.println("DUPE ADJ IN PQ");
          }
        }
      }
    }
    throw new IllegalStateException("never reached destination");
  }

  void grid(List<String> lines) {
    this.dim = lines.size();
    weights = new int[dim][dim];

    for (int row = 0; row < lines.size(); row++) {
      final String line = lines.get(row);
      if (line.length() != this.dim) {
        throw new IllegalStateException("bad dim");
      }
      for (int x = 0; x < line.length(); x++) {
        weights[row][x] = line.charAt(x) - '0';
      }
    }
  }
}
