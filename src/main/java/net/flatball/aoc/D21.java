package net.flatball.aoc;

import java.util.*;

/**
 * At this point i'm basically detached from meaningful progress on AOC 2023.  I had to leave p2 unsolved for 2 weeks
 * even with a cheat/solution on how to solve it sitting there ready and waiting.  The catch was, once I took
 * time to implement that solution it produced the wrong result (at least for the input i was given).
 * I dug through the reddit solution thread to find any info if there was something I was missing and stumbled
 * on a C# solution that I could adapt pretty easily that sure enough did work for my input file.  I'm not
 * sure at all how either approach really works so I can't even claim getting tipped off to finish it off myself
 * it just feels like writing somebody else's code to give an answer that I didn't find and don't know how they got
 * there.  I don't expect the final 4 days to be any easier than this, but i'll see if i can at least
 * muster some part 1 solutions (but maybe those will prove too difficult for me).
 */
public class D21 implements AOC {
  enum Dir {
    UP, DOWN, LEFT, RIGHT
  }
  record Point (int y, int x) {
    Point go(Dir dir) {
      return switch (dir) {
        case UP -> new Point(y - 1, x);
        case DOWN -> new Point(y + 1, x);
        case LEFT -> new Point(y, x - 1);
        case RIGHT -> new Point(y, x + 1);
      };
    }
  }

  char[][] map;
  int dim;
  Point start;
  int stepCount = 0;
  int stepTarget = 6;

  record Memo(Point pt, Dir dir) {}
  record Node(Point pt, Dir dir, int distance) {}

  @Override
  public void run(int part, List<String> lines) {
    grid(lines);

    final Map<Point, Integer> distances = new HashMap<>(dim * dim);
    final Deque<Node> queue = new ArrayDeque<>();
    final Node startNode = new Node(start, null, 0);
    queue.addLast(startNode);
    //distances.put(start, 0);
    while (!queue.isEmpty()) {
      final Node node = queue.removeFirst();
      final Point pt = node.pt;
      if (distances.putIfAbsent(pt, node.distance) != null) {
        continue;
      }
//      if (part == 1 && node.distance > stepTarget) {
//        continue;
//      }
//      if ((node.distance % 2) == 0) {
//        if (map[pt.y][pt.x] != 'O') {
//          map[pt.y][pt.x] = 'O';
//          stepCount++;
//        }
//      }

      for (final Dir dir : Dir.values()) {
        final Point adj = pt.go(dir);
        if (adj.x < 0 || adj.x >= dim) {
            continue;
        }
        if (adj.y < 0 || adj.y >= dim) {
            continue;
        }
        if (map[adj.y][adj.x] == '#') {
          continue;
        }
        final int d = node.distance + 1;
        if (distances.containsKey(adj)) {
          continue;
        }
//        if (!visited.add(new Memo(adj, dir))) {
//          continue;
//        }
        final Node adjNode = new Node(adj, dir, d);
        queue.addLast(adjNode);
      }
    }

    long p1 = distances
            .values()
            .stream()
            .filter(d -> d <= stepTarget && (d % 2) == 0)
        .count();
    System.out.println("ASSHART PART 1 = " + p1);

    // Visited is a HashMap<Coord, usize> which maps tiles in the input-square to their distance from the starting tile
    // So read this as "even_corners is the number of tiles which have a distance that is even and greater than 65"
    long even_corners = distances.values().stream().filter(d -> (d % 2) == 0 && d > 65).count();
    long odd_corners = distances.values().stream().filter(d -> (d % 2) == 1 && d > 65).count();

    long even_full = distances.values().stream().filter(d -> (d % 2) == 0).count();
    long odd_full = distances.values().stream().filter(d ->  (d % 2) == 1).count();

    // This is 202300 but im writing it out here to show the process
    long n = ((26501365L - (dim / 2)) / dim);
    if (n != 202300L) {
      throw new IllegalStateException("BAD N bro");
    }
    // too big: 609585229458384
    //          609585229458384
    //          609585229458384

    // right is 609585229256084

    long p2 = ((n+1)*(n+1)) * odd_full + (n*n) * even_full - (n+1) * odd_corners + n * even_corners;

    System.out.println("PART 2 flurf is " + p2);

    // This is 202300 but im writing it out here to show the process
    long even = n * n;
    long odd = (n + 1) * (n + 1);

    long p2again = odd * odd_full
            + even * even_full
            - ((n + 1) * odd_corners)
            + (n * even_corners);
    System.out.println("PART 2 again is " + p2again);

    //dump();
    System.out.println("PART " + part + " plot count is " + stepCount);
  }

  void grid(List<String> lines) {
    map = new char[lines.size()][];
    dim = lines.size();
    for (int y = 0; y < lines.size(); y++) {
      final String line = lines.get(y);
      int sx = line.indexOf('S');
      if (sx != -1) {
        if (start != null) {
          throw new IllegalStateException("multiple starts");
        }
        start = new Point(y, sx);
      }
      map[y] = line.toCharArray();
      if (line.length() != dim) {
        throw new IllegalStateException("not a square");
      }
    }
    if (start == null) {
      throw new IllegalStateException("no start specified");
    }
    if (dim == 131) {
      stepTarget = 64;
    }
    System.out.println("Loaded grid size " + dim + "x" + dim + " with start " + start + " and " + stepTarget + " steps to target");

    int unreachable = 0;
    for (var y = 1; y < dim- 2; y++) {
      for (var x = 1; x < dim - 2; x++) {
        if (map[y][x - 1] =='#' && map[y][x + 1] =='#' && map[y-1][x] =='#' && map[y+1][x] =='#')
        {
          map[y][x] ='#';
          unreachable++;
        }
      }
    }
    System.out.println("MAP has " + unreachable + " unreachables");
  }

  void dump() {
    for (char[] row : map) {
      System.out.println(new String(row));
    }
  }
}
