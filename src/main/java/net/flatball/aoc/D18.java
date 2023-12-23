package net.flatball.aoc;

import java.util.*;

/**
 * not sure how to feel about this one.  Come to find out, part1 was essentially brute-forced
 * and it melted when even experimented with p2.  I had tried to various no-raster
 * approaches with the best I knew how, but it just kept coming up off by some bits here and there
 * and just slow.  I finally just googled "grid area algorithm" and hit on Pick's algorithm.  Pretty
 * much exactly what I'm needing, it's just adapting it to the data structures and getting it to work
 * Of course, with that algorithm, the results for both parts is instantaneous.  All i've learned is
 * a new algorithm which i guess is good, but I don't feel any accomplishment in getting this one done.
 * I guess the lesson here is sometimes, the theory behind a solution is beyond you and recognizing
 * applications for your tools can sometimes just be a winner.
 */

public class D18 implements AOC {

  record Point(int y, int x) {
  }

  enum Op {
    U, D, L, R
  }

  final List<Point> poly = new ArrayList<>();

  @Override
  public void run(int part, List<String> lines) {
    graph(part, lines);
    long count = area();
    System.out.println("PART " + part + " is " + count);
  }


  static long cross(long x1, long y1, long x2, long y2) {
    return x1 * y2 - x2 * y1;
  }

  long polygonArea() {
    long ats = 0;
    for (int i = 0; i < poly.size(); i++) {
      final Point pi = poly.get(i);
      final Point pn = poly.get((i + 1) % poly.size());
      ats += cross(
              pi.x,
              pi.y,
              pn.x,
              pn.y);
    }
    return Math.abs(ats / 2);
  }

  static long gcd(long n0, long n1) {
    long a = n0;
    long b = n1;
    while (a != 0) {
      long temp = a;
      a = b % a;
      b = temp;
    }
    return b;
  }

  long boundary() {
    long ats = poly.size();
    for (int i = 0; i < poly.size(); i++) {
      long dx = (poly.get(i).x - poly.get((i + 1) % poly.size()).x);
      long dy = (poly.get(i).y - poly.get((i + 1) % poly.size()).y);
      ats += Math.abs(gcd(dx, dy)) - 1;
    }
    return ats;
  }

  long area() {
    long area = polygonArea();
    long boundary = boundary();
    long bigI = area + 1 - (boundary / 2);
    return bigI + boundary;
  }

  void graph(int part, List<String> lines) {
    int x = 0;
    int y = 0;
    for (String line : lines) {
      poly.add(new Point(y, x));
      String[] parts = line.split(" ");
      if (parts.length != 3) {
        throw new IllegalStateException("bad line: " + line);
      }
      Op op = Op.valueOf(parts[0]);
      int count = Integer.parseInt(parts[1]);
      int rgb = Integer.parseInt(parts[2].substring(2, 8), 16);
      if (part == 2) {
        //0 means R, 1 means D, 2 means L, and 3 means U.
        count = (rgb >> 4);
        op = switch (rgb & 0xf) {
          case 0 -> Op.R;
          case 1 -> Op.D;
          case 2 -> Op.L;
          case 3 -> Op.U;
          default -> throw new IllegalStateException("bad p2 rgb: " + Integer.toHexString(rgb));
        };
      }
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
    }
  }
}
