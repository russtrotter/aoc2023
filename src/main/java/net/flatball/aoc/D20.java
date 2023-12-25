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

  record Module (String name, Type type, List<String> destinations, Map<String, Boolean> state) {}

  final Map<String, Module> modules = new HashMap<>();
  final Deque<Pulse> queue = new ArrayDeque<>();
  long lowCount = 0L;
  long highCount = 0L;

  @Override
  public void run(int part, List<String> lines) {
    parseConfiguration(lines);
    for (int press = 0; press < 1000; press++) {
      queue.addLast(new Pulse("button", "broadcaster", false));
      while (!queue.isEmpty()) {
        final Pulse pulse = queue.remove();
        onPulse(pulse);
      }
    }
    System.out.println("PART " + part + " is " + (lowCount * highCount));
  }

  void onPulse(final Pulse pulse) {
    if (pulse.value) {
      highCount++;
    } else {
      lowCount++;
    }
    //System.out.println(pulse.source + " -" + (pulse.value ? "high":"low") + "-> " + pulse.destination);
    final Module module = modules.get(pulse.destination);
    if (module == null) {
      throw new IllegalStateException("no module: " + pulse.destination);
    }
    if (module.type == Type.BROADCAST) {
      pulse(module, false);
    } else if (module.type == Type.FLIPFLOP) {
      module.state.compute(module.name, (n, state) -> {
        if (state == null) {
          throw new IllegalStateException("flipflop should not have null state");
        }
        if (!pulse.value) {
          state = !state;
          pulse(module, state);
        }
        return state;
      });
    } else if (module.type == Type.CONJUNCTION) {
      if (module.state.put(pulse.source, pulse.value) == null) {
        throw new IllegalStateException("should not have null state for flipflop " + module.name + " on input " + pulse.destination);
      }
      long highCount = module.state.values().stream().filter(s -> s).count();
      pulse(module, module.state.size() != highCount);
    }
  }

  void pulse(final Module module, boolean pulse) {
    module.destinations.forEach(d -> queue.addLast(new Pulse(module.name, d, pulse)));
  }

  void parseConfiguration(List<String> lines) {
    for (String line : lines) {
      int idx = line.indexOf(' ');
      if (idx == -1) {
        throw new IllegalStateException("missing name terminator");
      }
      Type type = Type.NONE;
      String name = line.substring(0, idx);
      if (name.equals("broadcaster")) {
        type = Type.BROADCAST;
      } else if (name.startsWith("%")) {
        type = Type.FLIPFLOP;
        name = name.substring(1);
      } else if (name.startsWith("&")) {
        type = Type.CONJUNCTION;
        name = name.substring(1);
      }
      final List<String> destinations = Arrays.stream(line.substring(idx + 3).split(",")).map(String::trim).toList();
      final Module module = new Module(name, type, destinations, new HashMap<>());
      if (modules.put(name, module) != null) {
        throw new IllegalStateException("dupe module: " + name);
      }
      if (type == Type.FLIPFLOP) {
        module.state.put(name, false);
      }
    }
    Set<String> untyped = new HashSet<>();
    modules.forEach((name,module)->
      module.destinations.forEach(d -> {
        final Module destModule = modules.get(d);
        if (destModule == null) {
          untyped.add(d);
        } else if (destModule.type == Type.CONJUNCTION) {
          if (destModule.state.put(name, false) != null) {
            throw new IllegalStateException("dupe input module " + name + " on input " + d);
          }
        }
      })
    );
    System.out.println("Creating " + untyped.size() + " untyped modules");
    untyped.forEach(name -> modules.put(name, new Module(name, Type.NONE, Collections.emptyList(), new HashMap<>(Map.of(name, false)))));
  }

}
