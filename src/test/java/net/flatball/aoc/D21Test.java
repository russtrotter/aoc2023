package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D21Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D21(), 1, 21, "_ex1");
  }
  @Test
  public void part1() throws IOException {
    process(new D21(), 1, 21, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D21(), 2, 21, "_ex1");
  }
  @Test
  public void part2() throws IOException {
    process(new D21(), 2, 21, "");
  }

  @Test
  public void part21Other() throws IOException {
    process(new Day21(), 2, 21, "");
  }
  @Test
  public void part21CSharp() throws IOException {
    process(new Day21CS(), 2, 21, "");
  }
}
