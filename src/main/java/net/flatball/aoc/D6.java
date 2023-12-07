package net.flatball.aoc;

import java.util.List;
import java.util.stream.Stream;

/**
 * another day (like day 5) where a O(1)-ish solution eluded me but I at least was on to the right approach
 * thinking a quadratic solve of the basic equations: x + y = T, x * y = D would find the range, but there were
 * some edge cases in the computation that stumped me.  Googling some solutions revealed the right sequence of
 * floor/ceil/ and off by one adjusts.  I had submitted my correct brute forcing earlier in the day
 * and just spent the rest of time gaps to do the right
 */
public class D6 {

  private List<Long> times;
  private List<Long> distances;


  long find() {
    long total = 1;
    for (int i = 0; i < this.times.size(); i++) {
     // System.out.println("RACE: " + i);
      long t = this.times.get(i);
      long d = this.distances.get(i);

      // x + y = t
      // x * y = d

      // x(t - x) = d
      // tx - x^2 = d
      // x^x - tx + d = 0
      // a = 1
      // b = -t
      // c = d
      double b = t;
      double c = d;
      /*
      const sqrt = Math.sqrt(ct ** 2 - 4 * cd);
      const p2 = Math.ceil((ct + sqrt) / 2) - Math.floor((ct - sqrt) / 2) - 1;
       */
      double disc = (b * b) - (4.0d * c);
      double sqrt = Math.sqrt(disc);
      // here's the critical piece of the math solution.  ceil on the upper bound, floor the lower bound and subtract one from the range to get the count
      double p2 = Math.ceil((b + sqrt) / 2.0) - Math.floor((b - sqrt) /2.0) - 1.0;
      System.out.println("RACE " + i + ":compute=" + p2);

      int count = 0;
      for (long dt = 1; dt < t; dt++) {
        long m = (t - dt);
        if ((dt * m) > d) {
          //System.out.println("\t race h=" + dt + " m=" + m);
          count++;
        }
      }
      System.out.println("\t race t=" + t + " d=" + d + "=" + count + " winners");
      total *= count;
    }
    return total;
  }

  Stream<String> parseLine(String line) {
    return Stream.of(line.substring(11).split(" ")).filter(s -> !s.isEmpty());
  }

  public void part1(List<String> lines) {
    this.times = parseLine(lines.get(0)).map(Long::parseLong).toList();
    this.distances = parseLine(lines.get(1)).map(Long::parseLong).toList();

    // x + y = 30
    // x * y = 201
    // y = 201 - x
    // x + y = 30
    // x(30 - x) = 201
    // 30x - x^2 = 201
    // -x^2 + 30x - 201 = 0

    System.out.println("PART1 total: " + find());
    // h * (t - h) = (d + 1)
    // x(t-x) = (d + 1)
    // x * y >= 10
    // x + y = 7
    // x = 7 - y
    // (7 - y)y = 10
    // 7x - x^2 = 10
    // -x^2 + 7x - 10 = 0
    // -2x + 7 = 0
    // x = 7/2

    // r = 7 - x
    // x * r = 10

    // R1: t = 7, d = 9
    // h * (7 - h) = 10 7h - h^2 - 10
    // min move time = 10/6 = (10+5)/6 = 2
    // 1ms + 6ms = 6mm
    // 2ms + 5ms = 10mm
    // 3ms + 4ms = 12mm
    // 4ms + 3ms = 12mm
    // 5ms + 2ms = 10mm
    // 6ms + 1ms = 6mm


    // R2: t = 15, d = 40
    // h * (15 - h) = 41
    // min move time = 41 / 14 = (41+13)/14 =
    // 1 * 14 = 14
    // 2 * 13 = 26
    // 3 * 12 = 36
    // 4 * 11 = 44 W
    // 5 * 10 = 50 W
    // 6 * 9 = 54  W
    // 7 * 8 = 56  W
    // 8 * 7 = 56  W
    // 9 * 6 = 56  W
    // 10 * 5 =    W
    // 11 * 4 = 44 W
    // 12 * 3 = 36
    // 13 * 2 = 26
    // 14 * 1 = 14


    // R2: t=30, d = 200
    // 10 * 20 = 200
    // 11 * 19 = 209 W
    // 12
    // 13
    // 14
    // 15
    // 16
    // 17
    // 18
    // 19 * 11 = 209 W
    // 20 * 10 = 200


    // h * m = 10
    // h + m = 7
    // 10 / m > 1
    // 7 - h = 10
    // 10 / h = 6
    // t = d

    //
    // h (7 - h) = 10
    // d / h = (t - 1)
  }

  public void part2(List<String> lines) {
    this.times = List.of(Long.parseLong(parseLine(lines.get(0)).reduce("", (a, b) -> a + b)));
    this.distances = List.of(Long.parseLong(parseLine(lines.get(1)).reduce("", (a, b) -> a + b)));

    System.out.println("PART2 total: " + find());
  }
}
