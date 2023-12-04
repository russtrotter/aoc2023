package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D3Test {
  @Test
  public void part1example() throws IOException  {
    new D3().part1(Util.lines("/d3/d3_example.txt"));
  }

  @Test
  public void part1() throws IOException {
    new D3().part1(Util.lines("/d3/d3.txt"));
  }

  @Test
  public void part2example() throws IOException {
    new D3().part2(Util.lines("/d3/d3_example.txt"));
  }

  @Test
  public void part2() throws IOException {
    new D3().part2(Util.lines("/d3/d3.txt"));
  }
}
