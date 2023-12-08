package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D8Test extends AOCRunner{

  @Test
  public void part1example() throws IOException {
    process(new D8(), 1, 8, "_ex1");
  }

  @Test
  public void part1ex2() throws IOException {
    process(new D8(), 1, 8, "_ex2");
  }

  @Test
  public void part1() throws IOException {
    process(new D8(), 1, 8, "");
  }

  @Test
  public void part2example() throws IOException {
    process(new D8(), 2, 8, "_p2ex1");
  }

  @Test
  public void part2() throws IOException {
    process(new D8(), 2, 8, "");
  }
}
