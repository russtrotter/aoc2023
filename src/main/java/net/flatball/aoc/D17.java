package net.flatball.aoc;

import java.util.*;

/**
 * Too many rabbit holes this day.  Had optimism in the early times, but kept beating
 * my head against the wall and that stretched hours into days.  Finally succumbed and looked
 * for a d17 hint and learned that i wasn't far off days prior.  Frustrating for sure.
 * I can at least look back at a major lesson learned here and from prior days
 * that a memoization strategy can make all the difference.  In this case, rather than a
 * distance matrix based on a single point, ths problem requires a multi-dimensional distance
 * matrix keyed by a tuple of (point, direction, streak) rather than by just a point as in the classic
 * Dijkstra's algorithm.  I knew I had tried that at some point in my rabbit hole, but must have had
 * some bug or some issue.  It's weird how once you have solid footing, the next part can be really easy.
 * This particular p2 took about 10 additional minutes of dev and it was done.
 */
public class D17 implements AOC {
  int[][] weights;
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
  }

  record Point(int y, int x) {
    Point go(Dir dir) {
      return new Point(y + dir.dy, x + dir.dx);
    }
  }

  @Override
  public void run(int part, List<String> lines) {
    grid(lines);
    int result = drive(part, new Point(0, 0), new Point(dim - 1, dim - 1));
    System.out.println("PART " + part + " is " + result);
  }

  record Node(Point p, Dir dir, int streak) {
  }

  int drive(int part, Point location, Point destination) {
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
      final List<Node> adjNodes = new ArrayList<>();

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
        if (part == 2 && node.streak < 4 && !dir.equals(node.dir)) {
          continue;
        }
        final int streak = dir.equals(node.dir) ? node.streak + 1 : 1;
        if ((part == 1 && streak > 3) || (streak > 10)) {
          continue;
        }
        adjNodes.add(new Node(adj, dir, streak));
      }


      for (Node adjNode : adjNodes) {
        int ptd = distances.getOrDefault(node, Integer.MAX_VALUE);
        if (ptd == Integer.MAX_VALUE) {
          throw new IllegalStateException("wonk d");
        }
        int d = ptd + weights[adjNode.p.y][adjNode.p.x];
        if (d < distances.getOrDefault(adjNode, Integer.MAX_VALUE)) {
          distances.put(adjNode, d);
          stack.add(adjNode);
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
