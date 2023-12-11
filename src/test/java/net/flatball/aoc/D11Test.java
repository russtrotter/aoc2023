package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D11Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D11(), 1, 11, "_ex1");
  }

  @Test
  public void part1() throws IOException {
    process(new D11(), 1, 11, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D11(), 2, 11, "_ex1");
  }

  @Test
  public void part2() throws IOException {
    process(new D11(), 2, 11, "");
  }
}
