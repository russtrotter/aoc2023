package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D4Test {
  @Test
  public void part1example() throws IOException {
    new D4().part1(Util.lines("/d4/d4_example.txt"), 6, 23);
  }

  @Test
  public void part1() throws IOException  {
    new D4().part1(Util.lines("/d4/d4.txt"), 8, 40);
  }

  @Test
  public void part2example() throws IOException {
    new D4().part2(Util.lines("/d4/d4_example.txt"), 6, 23);
  }

  @Test
  public void part2() throws IOException  {
    new D4().part2(Util.lines("/d4/d4.txt"), 8, 40);
  }
}
