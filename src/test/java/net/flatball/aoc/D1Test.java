package net.flatball.aoc;

import org.junit.jupiter.api.Test;

public class D1Test {
  @Test
  public void huh() {
    System.out.println(D1.findDigit2("1two").map(String::valueOf).orElse("NOT FOUND"));
  }
}
