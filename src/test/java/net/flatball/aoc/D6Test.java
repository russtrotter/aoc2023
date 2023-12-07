package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D6Test {
  @Test
  public void part1example() throws IOException {
    new D6().part1(Util.lines("/d6/d6_example.txt"));
  }

  @Test
  public void part1() throws IOException {
    new D6().part1(Util.lines("/d6/d6.txt"));
  }

  @Test
  public void part2example() throws IOException {
    new D6().part2(Util.lines("/d6/d6_example.txt"));
  }

  @Test
  public void part2() throws IOException {
    new D6().part2(Util.lines("/d6/d6.txt"));
  }
}
