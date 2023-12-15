package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class D13Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D13(), 1, 13, "_ex1");
  }
  @Test
  public void part1other() throws IOException {
    process(new D13(), 1, 13, "_ex2");
  }
  @Test
  public void part1() throws IOException {
    process(new D13(), 1, 13, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D13(), 2, 13, "_ex1");
  }
  @Test
  public void part2other() throws IOException {
    process(new D13(), 2, 13, "_ex2");
  }
  @Test
  public void part2() throws IOException {
    process(new D13(), 2, 13, "");
  }


  @Test
  public void pat1() {
    //D13.Pattern p = new D13.Pattern(List.of("abc","def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz0"), 0);
    int[][] view = D13.pattern(List.of("#.#", "..#", "###", "#..", "##."));
    System.out.println("NORMAL");
    D13.dump(view);
    System.out.println("ROTATED");
    int[][] rotView = D13.rotate(view);
    D13.dump(rotView);
  }
}
