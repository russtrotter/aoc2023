package net.flatball.aoc;

import java.io.IOException;

public class AOCRunner {
  protected void process(AOC aoc, int part, int day, String suffix) throws IOException {
    aoc.run(part, Util.lines(String.format("/d%d/d%d%s.txt", day, day, suffix)));
  }
}
