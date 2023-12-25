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

  int part;
  final Map<String, Module> modules = new HashMap<>();
  final Deque<Pulse> queue = new ArrayDeque<>();
  long lowCount = 0L;
  long highCount = 0L;
  int clock = 0;

  String rxInput;
  boolean p2done = false;
  final Map<String, Integer> visited = new HashMap<>();
  final Map<String, Integer> lengths = new HashMap<>();

  @Override
  public void run(int part, List<String> lines) {
    this.part = part;
    parseConfiguration(lines);
    if (part == 1) {
      for (int press = 0; press < 1000; press++) {
        buttonPress();
      }
      System.out.println("PART " + part + " is " + (lowCount * highCount));
    } else if (part == 2) {
      rxInput = modules.entrySet().stream()
              .filter(e -> e.getValue().destinations.contains("rx"))
              .map(Map.Entry::getKey)
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("no rx module dude"));
      System.out.println("RX input is " + rxInput);

      modules.entrySet().stream()
              .filter(e -> e.getValue().destinations.contains(rxInput))
              .map(Map.Entry::getKey)
              .forEach(name -> visited.put(name, 0));

      while (!p2done) {
        buttonPress();
      }
    }
  }

  void buttonPress() {
    queue.addLast(new Pulse("button", "broadcaster", false));
    clock++;
    while (!queue.isEmpty()) {
      final Pulse pulse = queue.remove();
      onPulse(pulse);
    }
  }

  void onPulse(final Pulse pulse) {
    //System.out.println(pulse.source + " -" + (pulse.value ? "high":"low") + "-> " + pulse.destination);
    final Module module = modules.get(pulse.destination);
    if (module == null) {
      throw new IllegalStateException("no module: " + pulse.destination);
    }
    if (part == 1) {
      if (pulse.value) {
        highCount++;
      } else {
        lowCount++;
      }
    } else if (part == 2) {
      if (pulse.destination.equals(rxInput) && pulse.value) {
        visited.compute(pulse.source, (k,v) -> {
          if (v == null) {
            throw new IllegalStateException("should not have unknown RX input");
          }
          v++;
          return v;
        });
        lengths.computeIfAbsent(pulse.source, (k) -> clock);
        final List<Integer> values = visited.values().stream().filter(v -> v != 0).toList();
        if (values.size() == visited.size()) {
          System.out.println("PART " + part + " is " + lcm(new ArrayList<>(lengths.values())));
          p2done = true;
        }
      }
    }

    if (module.type == Type.BROADCAST) {
      pulse(module, pulse.value);
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
      boolean p = module.state.values().stream().allMatch(s -> s);
      pulse(module, !p);
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
    System.out.println("Configuration is " + modules.size() + " # of modules");
  }

  // adapted from some online C++ source
  static long lcm(List<Integer> values) {
    long lcm = 1L;
    int divisor = 2;

    while (true) {
      int counter = 0;
      boolean divisible = false;

      for (int i = 0; i < values.size(); i++) {
        // lcm_of_array_elements (n1, n2, ... 0) = 0.
        // For negative number we convert into
        // positive and calculate lcm_of_array_elements.
        if (values.get(i) == 0) {
          return 0;
        } else if (values.get(i) < 0) {
          values.set(i, values.get(i) * -1);
        }
        if (values.get(i) == 1) {
          counter++;
        }
        // Divide element_array by devisor if complete
        // division i.e. without remainder then replace
        // number with quotient; used for find next factor
        if ((values.get(i) % divisor) == 0) {
          divisible = true;
          values.set(i, values.get(i) / divisor);
        }
      }

      // If divisor able to completely divide any number
      // from array multiply with lcm_of_array_elements
      // and store into lcm_of_array_elements and continue
      // to same divisor for next factor finding.
      // else increment divisor
      if (divisible) {
        lcm *= divisor;
      } else {
        divisor++;
      }

      // Check if all element_array is 1 indicate
      // we found all factors and terminate while loop.
      if (counter == values.size()) {
        return lcm;
      }
    }
  }
}
