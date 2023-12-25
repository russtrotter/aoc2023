package net.flatball.aoc;

import java.util.*;

public class D20 implements AOC {

  record Pulse(String source, String destination, boolean value) {
  }

  enum Type {
    BROADCAST,
    FLIPFLOP,
    CONJUNCTION,
    NONE
  }

  record Module (String name, Type type, List<String> destinations) {}

  final Map<String, Module> modules = new HashMap<>();


  @Override
  public void run(int part, List<String> lines) {
    parseConfiguration(lines);
    System.out.println(modules);
  }

  void parseConfiguration(List<String> lines) {
    for (String line : lines) {
      int idx = line.indexOf(' ');
      if (idx == -1) {
        throw new IllegalStateException("missing name terminator");
      }
      Type type = Type.NONE;
      String name = line.substring(0, idx);
      if (name.equals("broadcast")) {
        type = Type.BROADCAST;
      } else if (name.startsWith("%")) {
        type = Type.FLIPFLOP;
        name = name.substring(1);
      } else if (name.startsWith("&")) {
        type = Type.CONJUNCTION;
        name = name.substring(1);
      }
      final List<String> destinations = Arrays.stream(line.substring(idx + 3).split(",")).map(String::trim).toList();
      final Module module = new Module(name, type, destinations);
      if (modules.put(name, module) != null) {
        throw new IllegalStateException("dupe module: " + name);
      }
    }
  }
}
