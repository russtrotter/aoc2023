package net.flatball.aoc;

import java.util.*;
import java.util.stream.Stream;

public class D11 implements AOC {
  static class State {
    Vertex vertex;
    int dist;
    boolean visited;
  }


  record Vertex(int x, int y) { }

  State[][] spState;
  final PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.dist));

  int[] horzCount;
  int[] vertCount;


  // A utility function to find the vertex with minimum
  // distance value, from the set of vertices not yet included
  // in shortest path tree
  Vertex minDistance() {
    // Initialize min value
    int min = Integer.MAX_VALUE;
    Vertex min_vertex = null;

    for (int y = 0; y < spState.length; y++) {
      for (int x = 0; x < spState[y].length; x++) {
        final State state = spState[y][x];
        if (!state.visited && state.dist <= min) {
          min = spState[y][x].dist;
          min_vertex = new Vertex(x, y);
        }
      }
    }
    return min_vertex;
  }

  void dijkstra(Vertex src, Vertex dst) {
    for (State[] states : spState) {
      for (final State state : states) {
        state.dist = Integer.MAX_VALUE;
        state.visited = false;
      }
    }
    // Distance of source vertex from itself is always 0
    spState[src.y][src.x].dist = 0;

    final int xMin = Math.max(0, Math.min(src.x, dst.x));
    final int xMax = Math.min(spState[0].length, Math.max(src.x, dst.x));
    final int yMin = Math.max(0, Math.min(src.y, dst.y));
    final int yMax = Math.min(spState.length, Math.max(src.y, dst.y));

    for (;;) {
      final Vertex vert = minDistance();
      if (vert.equals(dst)) {
        break;
      }
      final State vertState = spState[vert.y][vert.x];


      final Vertex up = vert.y > yMin ? new Vertex(vert.x, vert.y - 1) : null;
      final Vertex right = vert.x < xMax ? new Vertex(vert.x + 1, vert.y) : null;
      final Vertex down = vert.y < yMax ? new Vertex(vert.x, vert.y + 1) : null;
      final Vertex left = vert.x > xMin ? new Vertex(vert.x - 1, vert.y) : null;

      Stream.of(up, right, down, left).filter(Objects::nonNull).forEach(adj -> {
        final State adjState = spState[adj.y][adj.x];
        if (!adjState.visited) {
          // TODO: add weights for blanks
          int w = 1;
          if (adj.x != vert.x && horzCount[adj.x] == 0) {
            w++;
          }
          if (adj.y != vert.y && vertCount[adj.y] == 0) {
            w++;
          }
          int d = vertState.dist + w;
          if (d < adjState.dist) {
            adjState.dist = d;
          }
        }
      });
      vertState.visited = true;
    }
  }

  @Override
  public void run(int part, List<String> lines) {
    if (lines.isEmpty()) {
      throw new IllegalStateException("no lines");
    }
    final List<Vertex> galaxies = new ArrayList<>();
    final int width = lines.getFirst().length();
    if (width != lines.size()) {
      throw new IllegalStateException("not square");
    }
    horzCount = new int[width];
    vertCount = new int[lines.size()];
    for (int y = 0; y < lines.size(); y++) {
      final String line = lines.get(y);
      if (line.length() != width) {
        throw new IllegalStateException("bad line: " + y);
      }
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          galaxies.add(new Vertex(x, y));
          vertCount[y]++;
          horzCount[x]++;
        }
      }
    }
    if (galaxies.isEmpty()) {
      throw new IllegalStateException("no galaxies");
    }
    System.out.println("GALAXIES: " + galaxies.size());
    spState = new State[lines.size()][width];
    for (int y = 0; y < spState.length; y++) {
      final State[] states = spState[y];
      for (int x = 0; x < states.length; x++) {
        states[x] = new State();
        states[x].vertex = new Vertex(x, y);
      }
    }
    int total = 0;
    for (int g = 0; g < galaxies.size(); g++) {
      //System.out.println("PROCESSING SRC " + (g + 1));
      final Vertex from = galaxies.get(g);
      for (int to = g+1; to < galaxies.size(); to++) {
        final Vertex dst = galaxies.get(to);
        dijkstra(from, dst);
        int d = spState[dst.y][dst.x].dist;
        total += d;
        System.out.println("ASSMONGLER: from " + (g+1) + " to " + (to + 1) + " is " + d);
      }
    }
    System.out.println("PART " + part + " total " + total);
  }
}
