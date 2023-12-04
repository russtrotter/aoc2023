package net.flatball.aoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
  public static List<String> lines(String resource) throws IOException {
    try (final InputStream in = Util.class.getResourceAsStream(resource)) {
      if (in == null) {
        throw new IOException("no resource: " + resource);
      }
      return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
    }
  }
}
