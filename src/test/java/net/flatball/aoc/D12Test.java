package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class D12Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D12(), 1, 12, "_ex1");
  }
  @Test
  public void part1other() throws IOException {
    process(new D12(), 1, 12, "_ex2");
  }
  @Test
  public void part1() throws IOException {
    process(new D12(), 1, 12, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D12(), 2, 12, "_ex1");
  }

  @Test
  public void part2other() throws IOException {
    process(new D12(), 2, 12, "_ex2");
  }



  @Test
  public void part2() throws IOException {
    process(new D12(), 2, 12, "");
  }
}
