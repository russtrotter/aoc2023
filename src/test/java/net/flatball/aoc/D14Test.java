package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class D14Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException {
    process(new D14(), 1, 14, "_ex1");
  }
  @Test
  public void part1ex1cy1() throws IOException {
    process(new D14(), 1, 14, "_ex1cy1");
  }
  @Test
  public void part1ex1cy2() throws IOException {
    process(new D14(), 1, 14, "_ex1cy2");
  }
  @Test
  public void part1() throws IOException {
    process(new D14(), 1, 14, "");
  }
  @Test
  public void part2ex1() throws IOException {
    process(new D14(), 2, 14, "_ex1");
  }

  @Test
  public void part2() throws IOException {
    process(new D14(), 2, 14, "");
  }

  @Test
  public void west() {
    D14.Dish dish = new D14.Dish(List.of("#...O", "....O", "..#.O", "####O", "....."));
    dish.dump();
    System.out.println("TILT WEST");
    dish.west();
    dish.dump();
    //D14.Dish rot = dish.empty();
    //dish.rotate(rot);
    //rot.dump();
  }

  @Test
  public void south() {
    D14.Dish dish = new D14.Dish(List.of("OOOO#", "O...O", "O.#..", "O.###", "....."));
    dish.dump();
    System.out.println("TILT SOUTH");
    dish.south();
    dish.dump();
  }

  @Test
  public void east() {
    D14.Dish dish = new D14.Dish(List.of("O...#", "O....", "O.#..", "O.###", "....."));
    dish.dump();
    System.out.println("TILT EAST");
    dish.east();
    dish.dump();
  }
}
