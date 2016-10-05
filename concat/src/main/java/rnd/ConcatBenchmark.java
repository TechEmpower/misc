package rnd;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Measurement(iterations = 20)
@Warmup(iterations = 20)
public class ConcatBenchmark {

  public static void main(String[] args) throws Exception {
    Options options = new OptionsBuilder()
        .include(ConcatBenchmark.class.getName())
        .build();
    new Runner(options).run();
  }

  @SafeVarargs
  static <T> Stream<T> concat(Stream<T>... in) {
    return concat(in, 0, in.length);
  }

  static <T> Stream<T> concat(Stream<T>[] in, int low, int high) {
    switch (high - low) {
      case 0: return Stream.empty();
      case 1: return in[low];
      default:
        int mid = (low + high) >>> 1;
        Stream<T> left = concat(in, low, mid);
        Stream<T> right = concat(in, mid, high);
        return Stream.concat(left, right);
    }
  }

  static final String[] STUFF = new String[5000];
  static {
    Arrays.setAll(STUFF, String::valueOf);
  }

  @Param({ "2", "3", "4", "5", "6", "7", "8", "16", "32", "64", "128", "256" })
  private int p1_numberOfStreams;

  @Param({ "1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2056" })
  private int p2_elementsPerStream;

  private Stream<String>[] makeStreams() {
    @SuppressWarnings("unchecked")
    Stream<String>[] streams =
        (Stream<String>[]) new Stream<?>[p1_numberOfStreams];
    Arrays.setAll(streams, i -> IntStream
        .range(0, p2_elementsPerStream)
        .mapToObj(x -> STUFF[x % STUFF.length]));
    return streams;
  }

  @Benchmark
  public void reduce(Blackhole bh) {
    Stream<String>[] in = makeStreams();
    try (Stream<String> out = Arrays.stream(in)
        .reduce(Stream::concat)
        .orElseGet(Stream::empty)) {
      out.forEach(bh::consume);
    }
  }

  @Benchmark
  public void balance(Blackhole bh) {
    Stream<String>[] in = makeStreams();
    try (Stream<String> out = concat(in)) {
      out.forEach(bh::consume);
    }
  }

  @Benchmark
  public void flatmap(Blackhole bh) {
    Stream<String>[] in = makeStreams();
    try (Stream<String> out = Arrays.stream(in).flatMap(s -> s)) {
      out.forEach(bh::consume);
    }
  }

  @Benchmark
  public void overboard(Blackhole bh) {
    Stream<String>[] in = makeStreams();
    try (Stream<String> out = StreamConcatenation.concat(in)) {
      out.forEach(bh::consume);
    }
  }
}
