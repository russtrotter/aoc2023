package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D9Test extends AOCRunner {
  @Test
  public void part1example() throws IOException {
    process(new D9(), 1, 9, "_example");
  }

  @Test
  public void part1() throws IOException {
    process(new D9(), 1, 9, "");
  }

  @Test
  public void part2example() throws IOException {
    process(new D9(), 2, 9, "_example");
  }

  @Test
  public void part2() throws IOException {
    process(new D9(), 2, 9, "");
  }
}
