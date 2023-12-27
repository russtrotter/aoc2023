package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

  @Test
  public void lcm1() {
    System.out.println(D20.lcm(new ArrayList<>(List.of(1,5,15))));
  }
}
