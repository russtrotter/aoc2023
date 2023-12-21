package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D17Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D17(), 1, 17, "_ex1");
  }
  @Test
  public void part1ex2() throws IOException {
    process(new D17(), 1, 17, "_ex2");
  }
  @Test
  public void part1() throws IOException {
    process(new D17(), 1, 17, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D17(), 2, 17, "_ex1");
  }
  @Test
  public void part2ex2() throws IOException {
    process(new D17(), 2, 17, "_ex2");
  }
  @Test
  public void part2() throws IOException {
    process(new D17(), 2, 17, "");
  }

  @Test
  public void tiny1() throws IOException {
    process(new D17(), 1, 17, "_tiny1");
  }
}
