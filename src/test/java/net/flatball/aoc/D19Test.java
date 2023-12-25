package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D19Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D19(), 1, 19, "_ex1");
  }

  @Test
  public void part1() throws IOException {
    process(new D19(), 1, 19, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D19(), 2, 19, "_ex1");
  }
  @Test
  public void part2ex2() throws IOException {
    process(new D19(), 2, 19, "_ex2");
  }
  @Test
  public void part2() throws IOException {
    process(new D19(), 2, 19, "");
  }
}
