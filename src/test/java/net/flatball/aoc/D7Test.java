package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D7Test {
  @Test
  public void part1example() throws IOException {
    new D7().part(1, Util.lines("/d7/d7_example.txt"));
  }

  @Test
  public void part1() throws IOException {
    new D7().part(1, Util.lines("/d7/d7.txt"));
  }

  @Test
  public void part2example() throws IOException {
    new D7().part(2, Util.lines("/d7/d7_example.txt"));
  }

  @Test
  public void part2() throws IOException {
    new D7().part(2, Util.lines("/d7/d7.txt"));
  }

  @Test
  public void part2test() throws IOException {
    new D7().part(2, Util.lines("/d7/d7_test.txt"));
  }
}
