package net.flatball.aoc;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class D10Test extends AOCRunner {
  @Test
  public void part1ex1() throws IOException  {
    process(new D10(), 1, 10, "_ex1");
  }

  @Test
  public void part1() throws IOException {
    process(new D10(), 1, 10, "");
  }
  @Test
  public void part2ex1() throws IOException  {
    process(new D10(), 2, 10, "_p2ex1");
  }
  @Test
  public void part2ex2() throws IOException  {
    process(new D10(), 2, 10, "_p2ex2");
  }
  @Test
  public void part2ex3() throws IOException  {
    process(new D10(), 2, 10, "_p2ex3");
  }
  @Test
  public void part2ex4() throws IOException {
    process(new D10(), 2, 10, "_p2ex4");
  }

  @Test
  public void part2() throws IOException {
    process(new D10(), 2, 10, "");
  }

  @Test
  public void raster1() {
    final D10.Raster raster = new D10.Raster(20, 20);
    final List<D10.Point> loop = List.of(
            new D10.Point(1, 1),
            new D10.Point(5, 1),
            new D10.Point(18, 1),
            new D10.Point(18, 7),
            new D10.Point(15, 7),
            new D10.Point(15, 18),
            new D10.Point(12, 18),
            new D10.Point(12, 7),
            new D10.Point(1, 7)
    );
    raster.points(loop);
    raster.dump();
    raster.fill(loop);
    raster.dump();
  }
  
  @Test
  public void raster2() {
    final D10.Raster raster = new D10.Raster(10, 9);
    final List<D10.Point> loop = List.of(
            new D10.Point(1, 1),
            new D10.Point(2, 1),
            new D10.Point(3, 1),
            new D10.Point(4, 1),
            new D10.Point(5, 1),
            new D10.Point(6, 1),
            new D10.Point(7, 1),
            new D10.Point(8, 1),
            new D10.Point(9, 1),
            new D10.Point(9, 2),
            new D10.Point(9, 3),
            new D10.Point(9, 4),
            new D10.Point(9, 5),
            new D10.Point(9, 6),
            new D10.Point(9, 7),
            new D10.Point(8, 7),
            new D10.Point(7, 7),
            new D10.Point(6, 7),
            new D10.Point(6, 6),
            new D10.Point(6, 5),
            new D10.Point(7, 5),
            new D10.Point(8, 5),
            new D10.Point(8, 4),
            new D10.Point(8, 3),
            new D10.Point(8, 2),
            new D10.Point(7, 2),
            new D10.Point(6, 2),
            new D10.Point(5, 2),
            new D10.Point(4, 2),
            new D10.Point(3, 2),
            new D10.Point(2, 2),
            new D10.Point(2, 3),
            new D10.Point(2, 4),
            new D10.Point(2, 5),
            new D10.Point(3, 5),
            new D10.Point(4, 5),
            new D10.Point(4, 6),
            new D10.Point(4, 7),
            new D10.Point(3, 7),
            new D10.Point(2, 7),
            new D10.Point(1, 7),
            new D10.Point(1, 6),
            new D10.Point(1, 5),
            new D10.Point(1, 4),
            new D10.Point(1, 3)
    );
    raster.points(loop);
    raster.dump();
    raster.fill(loop);
    raster.dump();
  }

  @Test
  public void raster4() {
    final D10.Raster raster = new D10.Raster(10, 9);
    final List<D10.Point> loop = List.of(
            new D10.Point(1, 1),
            new D10.Point(2, 1),
            new D10.Point(3, 1),
            new D10.Point(4, 1),
            new D10.Point(5, 1),
            new D10.Point(6, 1),
            new D10.Point(7, 1),
            new D10.Point(8, 1),
            new D10.Point(9, 1),
            new D10.Point(9, 2),
            new D10.Point(9, 3),
            new D10.Point(9, 4),
            new D10.Point(9, 5),
            new D10.Point(9, 6),
            new D10.Point(9, 7),
            new D10.Point(8, 7),
            new D10.Point(7, 7),
            new D10.Point(6, 7),
            new D10.Point(6, 6),
            new D10.Point(6, 5),
            new D10.Point(7, 5),
            new D10.Point(8, 5),
            new D10.Point(8, 4),
            new D10.Point(8, 3),
            new D10.Point(8, 2),
            new D10.Point(7, 2),
            new D10.Point(6, 2),
            new D10.Point(5, 2),
            new D10.Point(4, 2),
            new D10.Point(3, 2),
            new D10.Point(2, 2),
            new D10.Point(2, 3),
            new D10.Point(2, 4),
            // ##
            new D10.Point(2, 5),
            new D10.Point(3, 5),
            new D10.Point(4, 5),
            // ###
            new D10.Point(4, 6),
            new D10.Point(4, 7),
            new D10.Point(3, 7),
            new D10.Point(2, 7),
            new D10.Point(1, 7),
            new D10.Point(1, 6),
            new D10.Point(1, 5),
            new D10.Point(1, 4),
            new D10.Point(1, 3)
    );
    raster.points(loop);
    raster.dump();
    raster.fill(loop);
    raster.dump();
  }
}
