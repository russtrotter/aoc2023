package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D20Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D20(), 1, 20, "_ex1");
  }
  @Test
  public void part1ex2() throws IOException {
    process(new D20(), 1, 20, "_ex2");
  }
  @Test
  public void part1() throws IOException {
    process(new D20(), 1, 20, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D20(), 2, 20, "_ex1");
  }
  @Test
  public void part2ex2() throws IOException {
    process(new D20(), 2, 20, "_ex2");
  }
  @Test
  public void part2() throws IOException {
    process(new D20(), 2, 20, "");
  }
}
