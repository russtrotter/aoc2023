package net.flatball.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * no lie, D5-part2 sucked.  I hate the solution i brute forced, but i was just spending too much
 * time thinking and tinkering on it over the day.  It took about 10 mins to run which maybe is ok
 * but "feels" wrong (tm), at least in the spirit of AOC.
 * <p>
 * It feels like some range-based checks could/should be used but i just couldn't land on a generalized
 * approach that would get it done.
 * <p>
 * Might tinker on it a bit for the rest of the evening.
 * <p>
 *   as it got later into evening, just ended up googling some solutions.. found a CPP one that
 *   could do a single pass of the input data.  Did a concept of "piping" where the ranges were tested
 *   for intersections and the overlap was a "pipe" that would translate, and the non-overlap on either side
 *   of pipe would fall through.  didn't try to reimplement but impressed at this elegant solution.
 * </p>
 */
public class D5 {
  record Range(long dest, long source, long range) {
  }

  private static final String HEADER = "seeds: ";
  List<Long> seeds;
  final List<List<Range>> maps = new ArrayList<>();

  void parse(List<String> lines) {
    this.seeds = Stream.of(lines.get(0).substring(HEADER.length()).split(" ")).map(Long::parseLong).toList();

    for (int lineIndex = 2; lineIndex < lines.size(); ) {
      String line = lines.get(lineIndex);
      if (line.charAt(line.length() - 1) != ':') {
        throw new IllegalStateException("expecting EOS here: " + line);
      }
      final List<Range> map = new ArrayList<>();
      maps.add(map);
      for (lineIndex++; lineIndex < lines.size(); lineIndex++) {
        line = lines.get(lineIndex);
        if (line.isEmpty()) {
          lineIndex++;
          break;
        }
        final List<Long> parts = Stream.of(line.split(" ")).map(Long::parseLong).toList();
        map.add(new Range(parts.get(0), parts.get(1), parts.get(2)));
      }
    }
  }

  long translate(long value) {
    for (List<Range> map : maps) {
      for (Range range : map) {
        long offset = value - range.source();
        if (offset >= 0 && offset < range.range()) {
          value = range.dest() + offset;
          break;
        }
      }
    }
    return value;
  }


  public void part1(List<String> lines) {
    parse(lines);
    long min = Long.MAX_VALUE;
    for (long seed : this.seeds) {
      long value = translate(seed);
      if (value < min) {
        min = value;
      }
    }
    System.out.println("PART1 lowest location: " + min);
  }

  /*
  as,ae  ?  bs,be

  if (bs > ae || as > be) {
    return null; // no intersect
  }
  os = max(as,bs);
  oe = min(ae, be);
   */

  record RX(long begin, long end) {
  }

  RX intersect(Range a, RX b) {
    long as = a.source();
    long ae = a.source() + a.range();

    if (b.begin() > ae || as > b.end()) {
      return null;
    }
    return new RX(Math.max(as, b.begin()), Math.min(ae, b.end()));
  }


  // x1->y1 ? x2->y2
  // 0-5 ? 6-10 =  0 <= 10 && 5 <= 6
  // 0-5 ? 0-5:
  // x1 <= y2 && y1 <= x2

  /*
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
   */


  public void part2(List<String> lines) {
    parse(lines);
    if ((seeds.size() % 2) != 0) {
      throw new IllegalStateException("expecting even # of seeds");
    }
    long min = Long.MAX_VALUE;
    for (int i = 0; i < seeds.size(); i += 2) {
      long seed = seeds.get(i);
      long length = seeds.get(i + 1);
      for (long l = 0; l < length; l++) {
        long value = translate(seed + l);
        if (value < min) {
          min = value;
        }
      }
      System.out.println("SEED COMPLETE: " + seed);
    }
    System.out.println("PART2 min: " + min);
  }
}
