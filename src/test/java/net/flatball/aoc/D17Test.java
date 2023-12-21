package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public void part2() throws IOException {
    process(new D17(), 2, 17, "");
  }

  @Test
  public void tiny1() throws IOException {
    D17 d = new D17();
    d.grid(Util.lines("/d17/d17_tiny1.txt"));
    d.drive(new D17.Point(0, 0), new D17.Point(0, 5));
    //process(new D17(), 1, 17, "_tiny1");
  }
}
