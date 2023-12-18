package net.flatball.aoc;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    costs = new int[dim][dim];
    Arrays.stream(costs).forEach(row -> Arrays.fill(row, Integer.MAX_VALUE));
    minPath = Integer.MAX_VALUE;
    memo.clear();
    final List<Dir> path = new ArrayList<>();

    drive(start, destination, path, 0);
    return costs[destination.y][destination.x];
  }

  final Map<Point, Integer> distances = new HashMap<>();



  void drive2(Point location, Point destination, List<Dir> path, int total) {
    final PriorityQueue<Point> queue = new PriorityQueue<>(Comparator.comparingInt(p -> weights[p.y][p.x]));
    distances.put(location, 0);
    queue.add(location);
    final Set<Point> v = new HashSet<>();
    final Map<Point, Point> prev = new HashMap<>();
    for (; ; ) {
      if (queue.isEmpty()) {
        throw new IllegalStateException("PQ is empty");
      }
      final Point pt = queue.remove();
      if (pt.equals(destination)) {
        break;
      }
      for (Dir dir : Dir.values()) {
        final Point adj = pt.go(dir);
        if (adj.x < 0 || adj.x >= dim) {
          continue;
        }
        if (adj.y < 0 || adj.y >= dim) {
          continue;
        }
//        if (!path.isEmpty() && dir.opposite(path.getLast())) {
//          continue;
//        }
//        if (!v.add(adj)) {
//          continue;
//        }
//        if (path.size() > 2 && dir.equals(path.getLast()) && dir.equals(path.get(path.size() - 2)) && dir.equals(path.get(path.size() - 3))) {
//          continue;
//        }
        int d = distances.get(pt) + weights[adj.y][adj.x];
        int adjDist = distances.getOrDefault(adj, Integer.MAX_VALUE);
        if (d < adjDist) {
          distances.put(adj, d);
          if (!queue.contains(adj)) {
            queue.add(adj);
            //System.out.println("PI " + pt + "=" + d);
//            path.addLast(dir);
//            if (prev.put(adj, pt) != null) {
//              System.out.println("ADJ COLLISION");
//            }
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
    for (; ; ) {
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

  record Node(Point p, Dir dir, int streak, Node parent) {
  }

  record MemoKey(Point p, Dir dir, int streak) {
  }

  final Set<MemoKey> memo = new HashSet<>();

  void drive4(Point location, Point destination, List<Dir> honk, int barf) {
    final Deque<Node> stack = new ArrayDeque<>();
    stack.push(new Node(location, null, 0, null));
    Set<MemoKey> memo = new HashSet<>();
    while (!stack.isEmpty()) {
      final Node node = stack.pop();
      final Point pt = node.p;
      if (pt.y < 0 || pt.y >= dim) {
        continue;
      }
      if (pt.x < 0 || pt.x >= dim) {
        continue;
      }
      if (pt.equals(destination)) {
        System.out.println("SP");
        plot(node);
        continue;
      }
      if (!memo.add(new MemoKey(pt, node.dir, 0))) {
        continue;
      }
      System.out.println("VISITING " + pt);
      for (Dir dir : Dir.values()) {
        stack.push(new Node(pt.go(dir), dir, 0, node));
      }
    }
  }

  record Frame(Point p, Dir dir, int streak, Frame parent, int cost) {
  }

  void drive(Point location, Point destination, List<Dir> honk, int turf) {
    //recur(new Node(location, null, 0, null), destination, 0);
    //final Deque<Frame> stack = new ArrayDeque<>();
    final PriorityQueue<Frame> stack = new PriorityQueue<>(Comparator.comparingInt(f -> weights[f.p.y][f.p.x]));
    stack.add(new Frame(location, null, 0, null, 0));
    while (!stack.isEmpty()) {
      final Frame node = stack.remove();
      final Point pt = node.p;

      final int cost = node.cost + weights[pt.y][pt.x];
      if (cost > costs[pt.y][pt.x]) {
        continue;
      }
      costs[pt.y][pt.x] = cost;

      if (pt.equals(destination)) {
        continue;
      }
      if (visits[pt.y][pt.x]) {
        continue;
      }
      visits[pt.y][pt.x] = true;
      for (Dir dir : Dir.values()) {
        if (node.dir != null && dir.opposite(node.dir)) {
          continue;
        }
        final Point adj = pt.go(dir);
        if (adj.y < 0 || adj.y >= dim) {
          continue;
        }
        if (adj.x < 0 || adj.x >= dim) {
          continue;
        }
        final int streak = dir.equals(node.dir) ? node.streak + 1 : 1;
        if (streak > 3) {
          continue;
        }
        stack.add(new Frame(adj, dir, streak, node, cost));
        //recur(new Node(pt.go(dir), dir, dir.equals(node.dir) ? node.streak + 1 : 1, node), destination, cost);
      }
      visits[pt.y][pt.x] = false;
    }
  }



  void recur(Node node, Point destination, int cost) {
    final Point pt = node.p;
    if (pt.y < 0 || pt.y >= dim) {
      return;
    }
    if (pt.x < 0 || pt.x >= dim) {
      return;
    }
    if (node.streak > 3) {
      return;
    }
    cost += weights[pt.y][pt.x];
    if (cost > costs[pt.y][pt.x]) {
      return;
    }
    costs[pt.y][pt.x] = cost;

    if (pt.equals(destination)) {
      minPath = Math.min(cost, minPath);
      //System.out.println("SP: " + cost);
      //plot(node);
      return;
    }
    if (visits[pt.y][pt.x]) {
      return;
    }
    visits[pt.y][pt.x] = true;
    for (Dir dir : Dir.values()) {
      if (node.dir != null && dir.opposite(node.dir)) {
        continue;
      }
      recur(new Node(pt.go(dir), dir, dir.equals(node.dir) ? node.streak + 1 : 1, node), destination, cost);
    }
    visits[pt.y][pt.x] = false;
  }

  void drive1(Point location, Point destination, List<Dir> honk, int turf) {
    final Deque<Node> stack = new ArrayDeque<>();
    stack.push(new Node(location, Dir.RIGHT, 0, null));
    while (!stack.isEmpty()) {
      final Node node = stack.pop();
      if (node.streak >= 3) {
        continue;
      }
      final Point pt = node.p;
      // TODO: short circuit total?
      if (pt.y < 0 || pt.y >= dim) {
        continue;
      }
      if (pt.x < 0 || pt.x >= dim) {
        continue;
      }
      if (pt.equals(destination)) {
        int total = 0;
        Node link = node;

        while (link != null) {
          total += weights[link.p.y][link.p.x];
          link = link.parent;
        }
        minPath = Math.min(minPath, total);
        System.out.println("SUM PATH: total=" + total);
        plot(node);
        continue;
      }
      if (visits[pt.y][pt.x]) {
        continue;
      }
      visits[pt.y][pt.x] = true;

      for (Dir dir : Dir.values()) {
//        if (!path.isEmpty() && dir.opposite(path.getLast())) {
//          continue;
//        }
//        if (path.size() > 2 && dir.equals(path.getLast()) && dir.equals(path.get(path.size() - 2)) && dir.equals(path.get(path.size() - 3))) {
//          continue;
//        }
        if (node.dir != null && dir.opposite(node.dir)) {
          continue;
        }
        stack.push(new Node(pt.go(dir), dir, dir.equals(node.dir) ? node.streak + 1 : 1, node));
//        path.addLast(dir);
//        drive(location.go(dir), destination, path, total);
//        path.removeLast();
      }
    }
  }

  void plot(Node node) {
    char[][] plot = new char[dim][dim];
    for (char[] row : plot) {
      Arrays.fill(row, '.');
    }
    StringBuilder path = new StringBuilder();
    while (node != null) {
      plot[node.p.y][node.p.x] = '#';
      if (node.dir == null) {
        path.insert(0, "R");
      } else {
        path.insert(0, switch (node.dir) {
          case UP -> "^";
          case DOWN -> "v";
          case LEFT -> "<";
          case RIGHT -> ">";
        });
      }
      node = node.parent;
    }
    System.out.println("PATH: " + path);
    Arrays.stream(plot).map(String::new).forEach(System.out::println);
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
