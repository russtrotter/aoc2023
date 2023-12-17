package net.flatball.aoc;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class D17 implements AOC {
  int[][] weights;
  boolean[][] visits;
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
      result = path(new Point(0, 0), new Point(dim - 1, dim - 1));
    }
    System.out.println("PART " + part + " is " + result);
  }

  int path(Point start, Point destination) {
    visits = new boolean[dim][dim];
    minPath = Integer.MAX_VALUE;
    memo.clear();
    final List<Dir> path = new ArrayList<>();

    drive(start, destination, path, 0);
    if (minPath == Integer.MAX_VALUE) {
      throw new IllegalStateException("never found a path");
    }
    return minPath;
  }



  static void status(List<Dir> paths) {
    final StringBuilder b = new StringBuilder("PATH: ");
    paths.stream().map(dir -> switch (dir) {
      case UP -> "^";
      case DOWN -> "v";
      case LEFT -> "<";
      case RIGHT -> ">";
    }).forEach(b::append);
    System.out.println(b);
  }

  final Map<Point, Integer> distances = new HashMap<>();

  void drive2(Point location, Point destination, List<Dir> path, int total) {
    final PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(p -> weights[p.y][p.x]));
    distances.put(location, 0);
    queue.add(location);
    final Set<Point> v = new HashSet<>();
    final Map<Point, Point> prev = new HashMap<>();
    for (;;) {
      if (queue.isEmpty()) {
        throw new IllegalStateException("PQ is empty");
      }
      final Point pt = queue.remove();
      if (pt.equals(destination)) {
        break;
      }
      for (Dir dir : Dir.values()) {
        status(path);
        final Point adj = pt.go(dir);
        if (adj.x < 0 || adj.x >= dim) {
          continue;
        }
        if (adj.y < 0 || adj.y >= dim) {
          continue;
        }
        if (!path.isEmpty() && dir.opposite(path.getLast())) {
          continue;
        }
//        if (!v.add(adj)) {
//          continue;
//        }
        if (path.size() > 2 && dir.equals(path.getLast()) && dir.equals(path.get(path.size() - 2)) && dir.equals(path.get(path.size() - 3))) {
          continue;
        }
        int d = distances.get(pt) + weights[adj.y][adj.x];
        int adjDist = distances.getOrDefault(adj, Integer.MAX_VALUE);
        if (d < adjDist) {
          total += d;
          distances.put(adj, d);
          if (!queue.contains(adj)) {
            queue.add(adj);
            //System.out.println("PI " + pt + "=" + d);
            path.addLast(dir);
            if (prev.put(adj, pt) != null) {
              System.out.println("ADJ COLLISION");
            }
          }
        }
      }
    }
    /*
        S ← empty sequence
      2 u ← target
      3  if prev[u] is defined or u = source:          // Do something only if the vertex is reachable
    4      while u is defined:                       // Construct the shortest path with a stack S
5            insert u at the beginning of S        // Push the vertex onto the stack
6            u ← prev[u]                           // Traverse from target to source
         */
    Point p = destination;
    int cost = 0;
    for(;;) {
      cost += weights[p.y][p.x];
      //System.out.println("DOGSHIT " + p);
      if (p == location) {
        break;
      }
      p = prev.get(p);
    }
    System.out.println("DOG SHIT COSTS " + cost);
    //System.out.println(" PREV IS " + prev);
    //prev.stream().forEach(pp -> System.out.println("PREV " + pp + "=" + weights[pp.y][pp.x]));
    minPath = cost;
  }

  record Node(Point p, Dir dir, Node parent) {}
  record MemoKey(Point p, List<Dir> path)  {}
  final Set<MemoKey> memo = new HashSet<>();

  void drive(Point location, Point destination, List<Dir> honk, int turf) {
    final Deque<Node> stack = new ArrayDeque<>();
    stack.push(new Node(location, null,null));
    while (!stack.isEmpty()) {
      final Node node = stack.pop();
      final Point pt = node.p;
      // TODO: short circuit total?
      if (pt.y < 0 || pt.y >= dim) {
        continue;
      }
      if (pt.x < 0 || pt.x >= dim) {
        continue;
      }
      if (visits[pt.y][pt.x]) {
        continue;
      }
      visits[pt.y][pt.x] = true;

      int total = 0;
      final List<Dir> path = new ArrayList<>();
      Node link = node;
      while (link != null) {
        System.out.print("LINK " + link.p + "->");
        if (link.dir != null) {
          path.addLast(link.dir);
        }
        total += weights[link.p.y][link.p.x];
        link = link.parent;
      }
      System.out.println();
      status(path);

//    final List<Dir> foo = new ArrayList<>(path.subList(Math.max(0, path.size() - 2), path.size()));
//    if (!memo.add(new MemoKey(location, foo))) {
//      return;
//    }

//      final int loss = weights[location.y][location.x];
//      total += loss;
//      if (total > minPath) {
//        continue;
//      }
      if (pt.equals(destination)) {
        minPath = Math.min(minPath, total);
      }

      for (Dir dir : Dir.values()) {
        if (!path.isEmpty() && dir.opposite(path.getLast())) {
          continue;
        }
        if (path.size() > 2 && dir.equals(path.getLast()) && dir.equals(path.get(path.size() - 2)) && dir.equals(path.get(path.size() - 3))) {
          continue;
        }
        stack.push(new Node(pt.go(dir), dir, node));
//        path.addLast(dir);
//        drive(location.go(dir), destination, path, total);
//        path.removeLast();
      }
    }
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
