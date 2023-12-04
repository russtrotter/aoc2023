package net.flatball.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class D1 {
  private int total = 0;

  public static void main(String[] args) throws IOException {
    System.out.println(System.getProperty("user.dir"));
    new D1().doMain("/Users/rtrotter/intraedge/src/aoc/d1.txt");
  }

  private void doMain(String path) throws IOException {
    try (Stream<String> lines = Files.lines(Path.of(path))) {
      lines.forEach(this::processLine);
    }
    System.out.println("RESULT: " + total);
  }

  void processLine(String line) {
    var left = findDigit(line);
    if (left.isEmpty()) {
      throw new IllegalStateException("No left for: " + line);
    }
    var right = findDigit2(line);
    if (right.isEmpty()) {
      throw new IllegalStateException("No right for: " + line);
    }
    int value = ((left.get()) * 10) + (right.get());
    System.out.println(line + "=" + value);
    total += value;
  }

  Optional<Integer> findDigit(String line) {
    int begin = 0;
    for (int i = begin; ;i += 1) {
      String pre = line.substring(begin, i);
      var match = word(pre, String::endsWith);
      if (match.isPresent()) {
        return match;
      }
      if (i == line.length()) {
        break;
      }
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
        return Optional.of(c - '0');
      }
    }
    return Optional.empty();
  }

  static Optional<Integer> findDigit2(String line) {
    for (int i = line.length() - 1; i >= 0 ; i -= 1) {
      String pre = line.substring(i);
      var match = word(pre, String::startsWith);
      if (match.isPresent()) {
        return match;
      }
      char c = line.charAt(i);
      if (Character.isDigit(c)) {
         return Optional.of(c - '0');
      }
    }
    return Optional.empty();
  }

  static Optional<Integer> word(String str, BiFunction<String, String, Boolean> f) {
    for (int i = 0; i < words.size(); i++) {
      if (f.apply(str, words.get(i))) {
        return Optional.of(i + 1);
      }
    }
    return Optional.empty();
  }

  static List<String> words = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

}

