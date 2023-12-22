package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D18Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D18(), 1, 18, "_ex1");
  }
  @Test
  public void part1ex2() throws IOException {
    process(new D18(), 1, 18, "_ex2");
  }
  @Test
  public void part1() throws IOException {
    process(new D18(), 1, 18, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D18(), 2, 18, "_ex1");
  }
  @Test
  public void part2() throws IOException {
    process(new D18(), 2, 18, "_ex1");
  }
}
