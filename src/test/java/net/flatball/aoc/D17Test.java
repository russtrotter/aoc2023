package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class D17Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D17(), 1, 17, "_ex1");
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
  public void part2() throws IOException {
    process(new D17(), 2, 17, "");
  }

  /*

   */
  @Test
  public void spf() {
    D17 d = new D17();
    d.run(1, List.of("1111", "9991", "9991", "1111"));
  }
}
