package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D2Test {
  @Test
  public void primary() throws IOException {
    new D2().part1(Util.lines("/d2/d2.txt"));
  }

  @Test
  public void example() throws IOException {
    new D2().part1(Util.lines("/d2/d2_example.txt"));
  }

  @Test
  public void part2Primary() throws IOException {
    new D2().part2(Util.lines("/d2/d2.txt"));
  }

  @Test
  public void part2Example() throws IOException {
    new D2().part2(Util.lines("/d2/d2_example.txt"));
  }
}
