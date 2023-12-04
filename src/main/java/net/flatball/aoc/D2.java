package net.flatball.aoc;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class D2 {
  private static final String LINE_PREFIX = "Game ";

  record Draw(int count, Cube cube) {}
  record Game(int id, List<List<Draw>> data) {}

  enum Cube {
    red,
    green,
    blue,
  }
  void part1(List<String> lines) {
    final Map<Cube, Integer> maxCounts = new EnumMap<>(Cube.class);
    maxCounts.put(Cube.red, 12);
    maxCounts.put(Cube.green, 13);
    maxCounts.put(Cube.blue, 14);
    int total = 0;
    final List<Game> input = parse(lines);
    for (Game game: input) {
      boolean ok = true;
      for (List<Draw> gameSet : game.data()) {
        for (Draw draw: gameSet) {
          if (draw.count() > maxCounts.get(draw.cube())) {
            ok = false;
            break;
          }
        }
        if (!ok) {
          break;
        }
      }
      if (ok) {
        total += game.id();
      }
    }
    System.out.println("PART1 total=" + total);
  }

  void part2(List<String> lines) {
    final List<Game> input = parse(lines);
    int total = 0;
    for (Game game: input) {

      final Map<Cube, Integer> counts = new EnumMap<>(Cube.class);
      for (List<Draw> draws : game.data()) {
        for (Draw draw : draws) {
          counts.compute(draw.cube(), (k, v) ->
             v == null || draw.count() > v ? draw.count() : v
          );
        }
      }
      final int power = counts.values().stream().reduce(1, (x, y) -> x * y);
      System.out.println("GAME " + game.id() + " has power " + power);
      total += power;
    }
    System.out.println("PART2 total=" + total);
  }

  static List<Game> parse(List<String> lines) {
    final List<Game> games = new ArrayList<>();
    lines.forEach(line -> {

      if (!line.startsWith(LINE_PREFIX)) {
        throw new IllegalStateException("Invalid line start: " + line);
      }
      int sep = line.indexOf(": ");
      if (sep == -1) {
        throw new IllegalStateException("No id separator: " + line);
      }
      int id = Integer.parseInt(line.substring(LINE_PREFIX.length(), sep));
      final List<List<Draw>> data = new ArrayList<>();
      for (String gameSet : line.substring(sep + 2).split("; ")) {
        final List<Draw> draws = new ArrayList<>();
        for (String draw : gameSet.split(", ")) {
          final int sp = draw.indexOf(' ');
          final int count = Integer.parseInt(draw.substring(0, sp));
          final Cube cube = Cube.valueOf(draw.substring(sp + 1));
          draws.add(new Draw(count, cube));
        }
        data.add(draws);
      }
      games.add(new Game(id, data));
    });
    return games;
  }
}
