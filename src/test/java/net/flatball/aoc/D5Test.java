package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D5Test {
  @Test
  public void part1example() throws IOException {
    new D5().part1(Util.lines("/d5/d5_example.txt"));
  }

  @Test
  public void part1() throws IOException {
    new D5().part1(Util.lines("/d5/d5.txt"));
  }

  @Test
  public void part2example() throws IOException {
    new D5().part2(Util.lines("/d5/d5_example.txt"));
  }

  @Test
  public void part2() throws IOException {
    new D5().part2(Util.lines("/d5/d5.txt"));
  }
}
