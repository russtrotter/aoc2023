package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class D16Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D16(), 1, 16, "_ex1");
  }
  @Test
  public void part1() throws IOException {
    process(new D16(), 1, 16, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D16(), 2, 16, "_ex1");
  }
  @Test
  public void part2() throws IOException {
    process(new D16(), 2, 16, "");
  }

  @Test
  public void grid1() {
    D16 p = new D16();
    p.run(1, List.of("\\..", "...", "\\.."));
  }
}
