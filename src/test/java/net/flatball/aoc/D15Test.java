package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D15Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D15(), 1, 15, "_ex1");
  }
  @Test
  public void part1() throws IOException {
    process(new D15(), 1, 15, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D15(), 2, 15, "_ex1");
  }
  @Test
  public void part2() throws IOException {
    process(new D15(), 2, 15, "");
  }

}
