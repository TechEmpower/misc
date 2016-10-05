package rnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.SplittableRandom;
import java.util.TreeSet;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link StreamConcatenation}.
 *
 * @author Michael Hixson
 */
public final class StreamConcatenationTest {

  static Stream<String> refStream(int numberOfStreams,
                                  int elementsPerStream,
                                  boolean sorted) {
    @SuppressWarnings("unchecked")
    Stream<String>[] streams = (Stream<String>[]) new Stream<?>[numberOfStreams];
    for (int i = 0; i < numberOfStreams; i++) {
      Collection<String> elements;
      if (sorted) {
        elements = new TreeSet<>();
      } else {
        elements = new ArrayList<>();
      }
      for (int j = 0; j < elementsPerStream; j++) {
        elements.add(i + "," + j);
      }
      streams[i] = elements.stream();
    }
    return StreamConcatenation.concat(streams);
  }

  static IntStream intStream(int numberOfStreams,
                             int elementsPerStream,
                             boolean sorted) {
    IntStream[] streams = new IntStream[numberOfStreams];
    for (int i = 0; i < numberOfStreams; i++) {
      int[] elements = new int[elementsPerStream];
      for (int j = 0; j < elements.length; j++) {
        elements[j] = i * elementsPerStream + j;
      }
      IntStream stream = IntStream.of(elements);
      if (sorted) {
        stream = stream.distinct().sorted();
      }
      streams[i] = stream;
    }
    return StreamConcatenation.concat(streams);
  }

  static LongStream longStream(int numberOfStreams,
                               int elementsPerStream,
                               boolean sorted) {
    LongStream[] streams = new LongStream[numberOfStreams];
    for (int i = 0; i < numberOfStreams; i++) {
      long[] elements = new long[elementsPerStream];
      for (int j = 0; j < elements.length; j++) {
        elements[j] = i * elementsPerStream + j;
      }
      LongStream stream = LongStream.of(elements);
      if (sorted) {
        stream = stream.distinct().sorted();
      }
      streams[i] = stream;
    }
    return StreamConcatenation.concat(streams);
  }

  static DoubleStream doubleStream(int numberOfStreams,
                                   int elementsPerStream,
                                   boolean sorted) {
    DoubleStream[] streams = new DoubleStream[numberOfStreams];
    for (int i = 0; i < numberOfStreams; i++) {
      double[] elements = new double[elementsPerStream];
      for (int j = 0; j < elements.length; j++) {
        elements[j] = i * elementsPerStream + j;
      }
      DoubleStream stream = DoubleStream.of(elements);
      if (sorted) {
        stream = stream.distinct().sorted();
      }
      streams[i] = stream;
    }
    return StreamConcatenation.concat(streams);
  }

  /**
   * Tests that {@link Spliterator#characteristics()} returns the
   * characteristics of the empty spliterator for a concatenation of zero input
   * streams.
   */
  @Test
  public void testSpliteratorCharacteristicsNoInputs() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    Assert.assertEquals(
        "ref",
        Spliterators.emptySpliterator().characteristics(),
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int",
        Spliterators.emptySpliterator().characteristics(),
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long",
        Spliterators.emptySpliterator().characteristics(),
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double",
        Spliterators.emptySpliterator().characteristics(),
        doubleStream.spliterator().characteristics());
  }

  /**
   * Tests that {@link Spliterator#estimateSize()} returns {@code 0} for a
   * concatenation of zero input streams.
   */
  @Test
  public void testSpliteratorEstimateSizeNoInputs() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    Assert.assertEquals("ref", 0, stream.spliterator().estimateSize());
    Assert.assertEquals("int", 0, intStream.spliterator().estimateSize());
    Assert.assertEquals("long", 0, longStream.spliterator().estimateSize());
    Assert.assertEquals("double", 0, doubleStream.spliterator().estimateSize());
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} does not pass any
   * elements to the consumer for a concatenation of zero input streams.
   */
  @Test
  public void testSpliteratorForEachRemainingNoInputs() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    stream.spliterator().forEachRemaining(x -> Assert.fail("ref"));
    intStream.spliterator().forEachRemaining((int x) -> Assert.fail("int"));
    longStream.spliterator().forEachRemaining((long x) -> Assert.fail("long"));
    doubleStream.spliterator().forEachRemaining((double x) -> Assert.fail("double"));
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} does not pass any
   * elements to the consumer for a concatenation of zero input streams.
   */
  @Test
  public void testSpliteratorTryAdvanceNoInputs() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    boolean advanced;
    advanced = stream.spliterator().tryAdvance(
        x -> Assert.fail("ref element"));
    Assert.assertFalse("ref advanced", advanced);
    advanced = intStream.spliterator().tryAdvance(
        (int x) -> Assert.fail("int element"));
    Assert.assertFalse("int advanced", advanced);
    advanced = longStream.spliterator().tryAdvance(
        (long x) -> Assert.fail("long element"));
    Assert.assertFalse("long advanced", advanced);
    advanced = doubleStream.spliterator().tryAdvance(
        (double x) -> Assert.fail("double element"));
    Assert.assertFalse("double advanced", advanced);
  }

  /**
   * Tests that {@link Spliterator#trySplit()} returns {@code null} for a
   * concatenation of zero input streams.
   */
  @Test
  public void testSpliteratorTrySplitNoInputsIsNotSplittable() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    Assert.assertNull("ref", stream.spliterator().trySplit());
    Assert.assertNull("int", intStream.spliterator().trySplit());
    Assert.assertNull("long", longStream.spliterator().trySplit());
    Assert.assertNull("double", doubleStream.spliterator().trySplit());
  }

  /**
   * Tests that {@link Spliterator#trySplit()} does not return {@code null} for
   * a concatenation of multiple input streams, even when the input streams are
   * all empty.
   */
  @Test
  public void testSpliteratorTrySplitMultipleInputsIsSplittable() {
    Stream<String> stream = refStream(3, 0, false);
    IntStream intStream = intStream(3, 0, false);
    LongStream longStream = longStream(3, 0, false);
    DoubleStream doubleStream = doubleStream(3, 0, false);
    Assert.assertNotNull("ref 0", stream.spliterator().trySplit());
    Assert.assertNotNull("int 0", intStream.spliterator().trySplit());
    Assert.assertNotNull("long 0", longStream.spliterator().trySplit());
    Assert.assertNotNull("double 0", doubleStream.spliterator().trySplit());
    stream = refStream(3, 1, false);
    intStream = intStream(3, 1, false);
    longStream = longStream(3, 1, false);
    doubleStream = doubleStream(3, 1, false);
    Assert.assertNotNull("ref 1", stream.spliterator().trySplit());
    Assert.assertNotNull("int 1", intStream.spliterator().trySplit());
    Assert.assertNotNull("long 1", longStream.spliterator().trySplit());
    Assert.assertNotNull("double 1", doubleStream.spliterator().trySplit());
    stream = refStream(3, 3, false);
    intStream = intStream(3, 3, false);
    longStream = longStream(3, 3, false);
    doubleStream = doubleStream(3, 3, false);
    Assert.assertNotNull("ref many", stream.spliterator().trySplit());
    Assert.assertNotNull("int many", intStream.spliterator().trySplit());
    Assert.assertNotNull("long many", longStream.spliterator().trySplit());
    Assert.assertNotNull("double many", doubleStream.spliterator().trySplit());
  }

  /**
   * Tests that {@link Spliterator#estimateSize()} returns {@code 0} for a
   * concatenation of sized, empty input streams.
   */
  @Test
  public void testSpliteratorEstimateSizeEmptyInputs() {
    Stream<String> stream = refStream(1, 0, false);
    IntStream intStream = intStream(1, 0, false);
    LongStream longStream = longStream(1, 0, false);
    DoubleStream doubleStream = doubleStream(1, 0, false);
    Assert.assertEquals("ref 1", 0, stream.spliterator().estimateSize());
    Assert.assertEquals("int 1", 0, intStream.spliterator().estimateSize());
    Assert.assertEquals("long 1", 0, longStream.spliterator().estimateSize());
    Assert.assertEquals("double 1", 0, doubleStream.spliterator().estimateSize());
    stream = refStream(3, 0, false);
    intStream = intStream(3, 0, false);
    longStream = longStream(3, 0, false);
    doubleStream = doubleStream(3, 0, false);
    Assert.assertEquals("ref many", 0, stream.spliterator().estimateSize());
    Assert.assertEquals("int many", 0, intStream.spliterator().estimateSize());
    Assert.assertEquals("long many", 0, longStream.spliterator().estimateSize());
    Assert.assertEquals("double many", 0, doubleStream.spliterator().estimateSize());
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} for a
   * concatenation of empty input streams will not invoke the provided action.
   */
  @Test
  public void testSpliteratorForEachRemainingEmptyInputs() {
    Stream<String> stream = refStream(1, 0, false);
    IntStream intStream = intStream(1, 0, false);
    LongStream longStream = longStream(1, 0, false);
    DoubleStream doubleStream = doubleStream(1, 0, false);
    stream.spliterator().forEachRemaining(x -> Assert.fail("ref 1"));
    intStream.spliterator().forEachRemaining((int x) -> Assert.fail("int 1"));
    longStream.spliterator().forEachRemaining((long x) -> Assert.fail("long 1"));
    doubleStream.spliterator().forEachRemaining((double x) -> Assert.fail("double 1"));
    stream = refStream(3, 0, false);
    intStream = intStream(3, 0, false);
    longStream = longStream(3, 0, false);
    doubleStream = doubleStream(3, 0, false);
    stream.spliterator().forEachRemaining(x -> Assert.fail("ref many"));
    intStream.spliterator().forEachRemaining((int x) -> Assert.fail("int many"));
    longStream.spliterator().forEachRemaining((long x) -> Assert.fail("long many"));
    doubleStream.spliterator().forEachRemaining((double x) -> Assert.fail("double many"));
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} for a concatenation of
   * empty input streams will not invoke the provided action and will not report
   * that it has advanced.
   */
  @Test
  public void testSpliteratorTryAdvanceEmptyInputs() {
    Stream<String> stream = refStream(1, 0, false);
    IntStream intStream = intStream(1, 0, false);
    LongStream longStream = longStream(1, 0, false);
    DoubleStream doubleStream = doubleStream(1, 0, false);
    boolean advanced;
    advanced = stream.spliterator().tryAdvance(
        x -> Assert.fail("ref 1 element"));
    Assert.assertFalse("ref 1 advanced", advanced);
    advanced = intStream.spliterator().tryAdvance(
        (int x) -> Assert.fail("int 1 element"));
    Assert.assertFalse("int 1 advanced", advanced);
    advanced = longStream.spliterator().tryAdvance(
        (long x) -> Assert.fail("long 1 element"));
    Assert.assertFalse("long 1 advanced", advanced);
    advanced = doubleStream.spliterator().tryAdvance(
        (double x) -> Assert.fail("double 1 element"));
    Assert.assertFalse("double 1 advanced", advanced);
    stream = refStream(3, 0, false);
    intStream = intStream(3, 0, false);
    longStream = longStream(3, 0, false);
    doubleStream = doubleStream(3, 0, false);
    advanced = stream.spliterator().tryAdvance(
        x -> Assert.fail("ref many element"));
    Assert.assertFalse("ref many advanced", advanced);
    advanced = intStream.spliterator().tryAdvance(
        (int x) -> Assert.fail("int many element"));
    Assert.assertFalse("int many advanced", advanced);
    advanced = longStream.spliterator().tryAdvance(
        (long x) -> Assert.fail("long many element"));
    Assert.assertFalse("long many advanced", advanced);
    advanced = doubleStream.spliterator().tryAdvance(
        (double x) -> Assert.fail("double many element"));
    Assert.assertFalse("double many advanced", advanced);
  }

  /**
   * Tests that {@link Spliterator#estimateSize()} for a concatenation of sized,
   * non-empty input streams reports the combined size of its inputs.
   */
  @Test
  public void testSpliteratorEstimateSizeNonEmptyInputs() {
    Stream<String> stream = refStream(1, 1, false);
    IntStream intStream = intStream(1, 1, false);
    LongStream longStream = longStream(1, 1, false);
    DoubleStream doubleStream = doubleStream(1, 1, false);
    Assert.assertEquals("ref 1x1", 1, stream.spliterator().estimateSize());
    Assert.assertEquals("int 1x1", 1, intStream.spliterator().estimateSize());
    Assert.assertEquals("long 1x1", 1, longStream.spliterator().estimateSize());
    Assert.assertEquals("double 1x1", 1, doubleStream.spliterator().estimateSize());
    stream = refStream(1, 3, false);
    intStream = intStream(1, 3, false);
    longStream = longStream(1, 3, false);
    doubleStream = doubleStream(1, 3, false);
    Assert.assertEquals("ref 1xmany", 3, stream.spliterator().estimateSize());
    Assert.assertEquals("int 1xmany", 3, intStream.spliterator().estimateSize());
    Assert.assertEquals("long 1xmany", 3, longStream.spliterator().estimateSize());
    Assert.assertEquals("double 1xmany", 3, doubleStream.spliterator().estimateSize());
    stream = refStream(3, 1, false);
    intStream = intStream(3, 1, false);
    longStream = longStream(3, 1, false);
    doubleStream = doubleStream(3, 1, false);
    Assert.assertEquals("ref manyx1", 3, stream.spliterator().estimateSize());
    Assert.assertEquals("int manyx1", 3, intStream.spliterator().estimateSize());
    Assert.assertEquals("long manyx1", 3, longStream.spliterator().estimateSize());
    Assert.assertEquals("double manyx1", 3, doubleStream.spliterator().estimateSize());
    stream = refStream(3, 3, false);
    intStream = intStream(3, 3, false);
    longStream = longStream(3, 3, false);
    doubleStream = doubleStream(3, 3, false);
    Assert.assertEquals("ref manyxmany", 9, stream.spliterator().estimateSize());
    Assert.assertEquals("int manyxmany", 9, intStream.spliterator().estimateSize());
    Assert.assertEquals("long manyxmany", 9, longStream.spliterator().estimateSize());
    Assert.assertEquals("double manyxmany", 9, doubleStream.spliterator().estimateSize());
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} for a
   * concatenation of one input stream with one element iterates through the
   * expected element.
   */
  @Test
  public void testSpliteratorForEachRemainingOneByOne() {
    Stream<String> stream = refStream(1, 1, false);
    IntStream intStream = intStream(1, 1, false);
    LongStream longStream = longStream(1, 1, false);
    DoubleStream doubleStream = doubleStream(1, 1, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    List<String> out = new ArrayList<>();
    List<Integer> intOut = new ArrayList<>();
    List<Long> longOut = new ArrayList<>();
    List<Double> doubleOut = new ArrayList<>();
    spliterator.forEachRemaining(out::add);
    intSpliterator.forEachRemaining((IntConsumer) intOut::add);
    longSpliterator.forEachRemaining((LongConsumer) longOut::add);
    doubleSpliterator.forEachRemaining((DoubleConsumer) doubleOut::add);
    Assert.assertEquals("ref contents", Collections.singletonList("0,0"), out);
    Assert.assertEquals("int contents", Collections.singletonList(0), intOut);
    Assert.assertEquals("long contents", Collections.singletonList(0L), longOut);
    Assert.assertEquals("double contents", Collections.singletonList(0d), doubleOut);
    spliterator.forEachRemaining(x -> Assert.fail("ref element"));
    intSpliterator.forEachRemaining((int x) -> Assert.fail("int element"));
    longSpliterator.forEachRemaining((long x) -> Assert.fail("long element"));
    doubleSpliterator.forEachRemaining((double x) -> Assert.fail("double element"));
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} for a
   * concatenation of one input stream with many elements iterates through the
   * expected elements.
   */
  @Test
  public void testSpliteratorForEachRemainingOneByMany() {
    Stream<String> stream = refStream(3, 1, false);
    IntStream intStream = intStream(3, 1, false);
    LongStream longStream = longStream(3, 1, false);
    DoubleStream doubleStream = doubleStream(3, 1, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    List<String> out = new ArrayList<>();
    List<Integer> intOut = new ArrayList<>();
    List<Long> longOut = new ArrayList<>();
    List<Double> doubleOut = new ArrayList<>();
    spliterator.forEachRemaining(out::add);
    intSpliterator.forEachRemaining((IntConsumer) intOut::add);
    longSpliterator.forEachRemaining((LongConsumer) longOut::add);
    doubleSpliterator.forEachRemaining((DoubleConsumer) doubleOut::add);
    Assert.assertEquals("ref contents", Arrays.asList("0,0", "1,0", "2,0"), out);
    Assert.assertEquals("int contents", Arrays.asList(0, 1, 2), intOut);
    Assert.assertEquals("long contents", Arrays.asList(0L, 1L, 2L), longOut);
    Assert.assertEquals("double contents", Arrays.asList(0d, 1d, 2d), doubleOut);
    spliterator.forEachRemaining(x -> Assert.fail("ref element"));
    intSpliterator.forEachRemaining((int x) -> Assert.fail("int element"));
    longSpliterator.forEachRemaining((long x) -> Assert.fail("long element"));
    doubleSpliterator.forEachRemaining((double x) -> Assert.fail("double element"));
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} for a
   * concatenation of many input streams with one element each iterates through
   * the expected elements.
   */
  @Test
  public void testSpliteratorForEachRemainingManyByOne() {
    Stream<String> stream = refStream(1, 3, false);
    IntStream intStream = intStream(1, 3, false);
    LongStream longStream = longStream(1, 3, false);
    DoubleStream doubleStream = doubleStream(1, 3, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    List<String> out = new ArrayList<>();
    List<Integer> intOut = new ArrayList<>();
    List<Long> longOut = new ArrayList<>();
    List<Double> doubleOut = new ArrayList<>();
    spliterator.forEachRemaining(out::add);
    intSpliterator.forEachRemaining((IntConsumer) intOut::add);
    longSpliterator.forEachRemaining((LongConsumer) longOut::add);
    doubleSpliterator.forEachRemaining((DoubleConsumer) doubleOut::add);
    Assert.assertEquals("ref contents", Arrays.asList("0,0", "0,1", "0,2"), out);
    Assert.assertEquals("int contents", Arrays.asList(0, 1, 2), intOut);
    Assert.assertEquals("long contents", Arrays.asList(0L, 1L, 2L), longOut);
    Assert.assertEquals("double contents", Arrays.asList(0d, 1d, 2d), doubleOut);
    spliterator.forEachRemaining(x -> Assert.fail("ref element"));
    intSpliterator.forEachRemaining((int x) -> Assert.fail("int element"));
    longSpliterator.forEachRemaining((long x) -> Assert.fail("long element"));
    doubleSpliterator.forEachRemaining((double x) -> Assert.fail("double element"));
  }

  /**
   * Tests that {@link Spliterator#forEachRemaining(Consumer)} for a
   * concatenation of many input streams with many elements each iterates
   * through the expected elements.
   */
  @Test
  public void testSpliteratorForEachRemainingManyByMany() {
    Stream<String> stream = refStream(3, 3, false);
    IntStream intStream = intStream(3, 3, false);
    LongStream longStream = longStream(3, 3, false);
    DoubleStream doubleStream = doubleStream(3, 3, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    List<String> out = new ArrayList<>();
    List<Integer> intOut = new ArrayList<>();
    List<Long> longOut = new ArrayList<>();
    List<Double> doubleOut = new ArrayList<>();
    spliterator.forEachRemaining(out::add);
    intSpliterator.forEachRemaining((IntConsumer) intOut::add);
    longSpliterator.forEachRemaining((LongConsumer) longOut::add);
    doubleSpliterator.forEachRemaining((DoubleConsumer) doubleOut::add);
    Assert.assertEquals(
        "ref contents",
        Arrays.asList(
            "0,0", "0,1", "0,2",
            "1,0", "1,1", "1,2",
            "2,0", "2,1", "2,2"),
        out);
    Assert.assertEquals(
        "int contents",
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8),
        intOut);
    Assert.assertEquals(
        "long contents",
        Arrays.asList(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L),
        longOut);
    Assert.assertEquals(
        "double contents",
        Arrays.asList(0d, 1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d),
        doubleOut);
    spliterator.forEachRemaining(
        x -> Assert.fail("ref element"));
    intSpliterator.forEachRemaining(
        (int x) -> Assert.fail("int element"));
    longSpliterator.forEachRemaining(
        (long x) -> Assert.fail("long element"));
    doubleSpliterator.forEachRemaining(
        (double x) -> Assert.fail("double element"));
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} for a concatenation of
   * one input stream with one element iterates through the expected element.
   */
  @Test
  public void testSpliteratorTryAdvanceOneByOne() {
    Stream<String> stream = refStream(1, 1, false);
    IntStream intStream = intStream(1, 1, false);
    LongStream longStream = longStream(1, 1, false);
    DoubleStream doubleStream = doubleStream(1, 1, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    LongAdder counter = new LongAdder();
    LongAdder intCounter = new LongAdder();
    LongAdder longCounter = new LongAdder();
    LongAdder doubleCounter = new LongAdder();
    boolean advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents", "0,0", x);
      counter.increment();
    });
    boolean intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents", 0, x);
      intCounter.increment();
    });
    boolean longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents", 0L, x);
      longCounter.increment();
    });
    boolean doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents", 0d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter", 1, counter.sum());
    Assert.assertEquals("int counter", 1, intCounter.sum());
    Assert.assertEquals("long counter", 1, longCounter.sum());
    Assert.assertEquals("double counter", 1, doubleCounter.sum());
    Assert.assertTrue("ref advanced first", advanced);
    Assert.assertTrue("int advanced first", intAdvanced);
    Assert.assertTrue("long advanced first", longAdvanced);
    Assert.assertTrue("double advanced first", doubleAdvanced);
    advanced = spliterator.tryAdvance(
        x -> Assert.fail("ref element"));
    intAdvanced = intSpliterator.tryAdvance(
        (int x) -> Assert.fail("int element"));
    longAdvanced = longSpliterator.tryAdvance(
        (long x) -> Assert.fail("long element"));
    doubleAdvanced = doubleSpliterator.tryAdvance(
        (double x) -> Assert.fail("double element"));
    Assert.assertFalse("ref advanced second", advanced);
    Assert.assertFalse("int advanced second", intAdvanced);
    Assert.assertFalse("long advanced second", longAdvanced);
    Assert.assertFalse("double advanced second", doubleAdvanced);
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} for a concatenation of
   * one input stream with many elements iterates through the expected elements.
   */
  @Test
  public void testSpliteratorTryAdvanceOneByMany() {
    Stream<String> stream = refStream(1, 3, false);
    IntStream intStream = intStream(1, 3, false);
    LongStream longStream = longStream(1, 3, false);
    DoubleStream doubleStream = doubleStream(1, 3, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    LongAdder counter = new LongAdder();
    LongAdder intCounter = new LongAdder();
    LongAdder longCounter = new LongAdder();
    LongAdder doubleCounter = new LongAdder();
    boolean advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 1", "0,0", x);
      counter.increment();
    });
    boolean intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 1", 0, x);
      intCounter.increment();
    });
    boolean longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 1", 0L, x);
      longCounter.increment();
    });
    boolean doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 1", 0d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 1", 1, counter.sum());
    Assert.assertEquals("int counter 1", 1, intCounter.sum());
    Assert.assertEquals("long counter 1", 1, longCounter.sum());
    Assert.assertEquals("double counter 1", 1, doubleCounter.sum());
    Assert.assertTrue("ref advanced 1", advanced);
    Assert.assertTrue("int advanced 1", intAdvanced);
    Assert.assertTrue("long advanced 1", longAdvanced);
    Assert.assertTrue("double advanced 1", doubleAdvanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 2", "0,1", x);
      counter.increment();
    });
    intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 2", 1, x);
      intCounter.increment();
    });
    longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 2", 1L, x);
      longCounter.increment();
    });
    doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 2", 1d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 2", 2, counter.sum());
    Assert.assertEquals("int counter 2", 2, intCounter.sum());
    Assert.assertEquals("long counter 2", 2, longCounter.sum());
    Assert.assertEquals("double counter 2", 2, doubleCounter.sum());
    Assert.assertTrue("ref advanced 2", advanced);
    Assert.assertTrue("int advanced 2", intAdvanced);
    Assert.assertTrue("long advanced 2", longAdvanced);
    Assert.assertTrue("double advanced 2", doubleAdvanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 3", "0,2", x);
      counter.increment();
    });
    intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 3", 2, x);
      intCounter.increment();
    });
    longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 3", 2L, x);
      longCounter.increment();
    });
    doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 3", 2d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 3", 3, counter.sum());
    Assert.assertEquals("int counter 3", 3, intCounter.sum());
    Assert.assertEquals("long counter 3", 3, longCounter.sum());
    Assert.assertEquals("double counter 3", 3, doubleCounter.sum());
    Assert.assertTrue("ref advanced 3", advanced);
    Assert.assertTrue("int advanced 3", intAdvanced);
    Assert.assertTrue("long advanced 3", longAdvanced);
    Assert.assertTrue("double advanced 3", doubleAdvanced);
    advanced = spliterator.tryAdvance(
        x -> Assert.fail("ref element"));
    intAdvanced = intSpliterator.tryAdvance(
        (int x) -> Assert.fail("int element"));
    longAdvanced = longSpliterator.tryAdvance(
        (long x) -> Assert.fail("long element"));
    doubleAdvanced = doubleSpliterator.tryAdvance(
        (double x) -> Assert.fail("double element"));
    Assert.assertFalse("ref advanced 4", advanced);
    Assert.assertFalse("int advanced 4", intAdvanced);
    Assert.assertFalse("long advanced 4", longAdvanced);
    Assert.assertFalse("double advanced 4", doubleAdvanced);
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} for a concatenation of
   * many input streams with one element each iterates through the expected
   * elements.
   */
  @Test
  public void testSpliteratorTryAdvanceManyByOne() {
    Stream<String> stream = refStream(3, 1, false);
    IntStream intStream = intStream(3, 1, false);
    LongStream longStream = longStream(3, 1, false);
    DoubleStream doubleStream = doubleStream(3, 1, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    LongAdder counter = new LongAdder();
    LongAdder intCounter = new LongAdder();
    LongAdder longCounter = new LongAdder();
    LongAdder doubleCounter = new LongAdder();
    boolean advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 1", "0,0", x);
      counter.increment();
    });
    boolean intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 1", 0, x);
      intCounter.increment();
    });
    boolean longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 1", 0L, x);
      longCounter.increment();
    });
    boolean doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 1", 0d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 1", 1, counter.sum());
    Assert.assertEquals("int counter 1", 1, intCounter.sum());
    Assert.assertEquals("long counter 1", 1, longCounter.sum());
    Assert.assertEquals("double counter 1", 1, doubleCounter.sum());
    Assert.assertTrue("ref advanced 1", advanced);
    Assert.assertTrue("int advanced 1", intAdvanced);
    Assert.assertTrue("long advanced 1", longAdvanced);
    Assert.assertTrue("double advanced 1", doubleAdvanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 2", "1,0", x);
      counter.increment();
    });
    intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 2", 1, x);
      intCounter.increment();
    });
    longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 2", 1L, x);
      longCounter.increment();
    });
    doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 2", 1d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 2", 2, counter.sum());
    Assert.assertEquals("int counter 2", 2, intCounter.sum());
    Assert.assertEquals("long counter 2", 2, longCounter.sum());
    Assert.assertEquals("double counter 2", 2, doubleCounter.sum());
    Assert.assertTrue("ref advanced 2", advanced);
    Assert.assertTrue("int advanced 2", intAdvanced);
    Assert.assertTrue("long advanced 2", longAdvanced);
    Assert.assertTrue("double advanced 2", doubleAdvanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("ref contents 3", "2,0", x);
      counter.increment();
    });
    intAdvanced = intSpliterator.tryAdvance((int x) -> {
      Assert.assertEquals("int contents 3", 2, x);
      intCounter.increment();
    });
    longAdvanced = longSpliterator.tryAdvance((long x) -> {
      Assert.assertEquals("long contents 3", 2L, x);
      longCounter.increment();
    });
    doubleAdvanced = doubleSpliterator.tryAdvance((double x) -> {
      Assert.assertEquals("double contents 3", 2d, x, 0.1);
      doubleCounter.increment();
    });
    Assert.assertEquals("ref counter 3", 3, counter.sum());
    Assert.assertEquals("int counter 3", 3, intCounter.sum());
    Assert.assertEquals("long counter 3", 3, longCounter.sum());
    Assert.assertEquals("double counter 3", 3, doubleCounter.sum());
    Assert.assertTrue("ref advanced 3", advanced);
    Assert.assertTrue("int advanced 3", intAdvanced);
    Assert.assertTrue("long advanced 3", longAdvanced);
    Assert.assertTrue("double advanced 3", doubleAdvanced);
    advanced = spliterator.tryAdvance(
        x -> Assert.fail("ref element"));
    intAdvanced = intSpliterator.tryAdvance(
        (int x) -> Assert.fail("int element"));
    longAdvanced = longSpliterator.tryAdvance(
        (long x) -> Assert.fail("long element"));
    doubleAdvanced = doubleSpliterator.tryAdvance(
        (double x) -> Assert.fail("double element"));
    Assert.assertFalse("ref advanced 4", advanced);
    Assert.assertFalse("int advanced 4", intAdvanced);
    Assert.assertFalse("long advanced 4", longAdvanced);
    Assert.assertFalse("double advanced 4", doubleAdvanced);
  }

  /**
   * Tests that {@link Spliterator#tryAdvance(Consumer)} for a concatenation of
   * many input streams with many elements each iterates through the expected
   * elements.
   */
  @Test
  public void testSpliteratorTryAdvanceManyByMany() {
    // TODO: Add assertions for primitive streams.
    Stream<String> stream = refStream(3, 3, false);
    Spliterator<String> spliterator = stream.spliterator();
    LongAdder counter = new LongAdder();
    boolean advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("0,0", x);
      counter.increment();
    });
    Assert.assertEquals(1, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("0,1", x);
      counter.increment();
    });
    Assert.assertEquals(2, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("0,2", x);
      counter.increment();
    });
    Assert.assertEquals(3, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("1,0", x);
      counter.increment();
    });
    Assert.assertEquals(4, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("1,1", x);
      counter.increment();
    });
    Assert.assertEquals(5, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("1,2", x);
      counter.increment();
    });
    Assert.assertEquals(6, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("2,0", x);
      counter.increment();
    });
    Assert.assertEquals(7, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("2,1", x);
      counter.increment();
    });
    Assert.assertEquals(8, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> {
      Assert.assertEquals("2,2", x);
      counter.increment();
    });
    Assert.assertEquals(9, counter.sum());
    Assert.assertTrue(advanced);
    advanced = spliterator.tryAdvance(x -> Assert.fail());
    Assert.assertFalse(advanced);
  }

  /**
   * Test that {@link Spliterator#estimateSize()} reports the combined
   * characteristics of the spliterators of the input streams for a
   * concatenation of multiple input streams, each with multiple elements.
   */
  @Test
  public void testSpliteratorCharacteristicsMultipleInputsMultipleElements() {
    // TODO: Add assertions for primitive streams.
    // TODO: Make one of the spliterators sorted, confirm the result has the
    //       cross-section of characteristics of the inputs.
    Stream<String> stream = refStream(3, 3, false);
    Assert.assertEquals(
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        stream.spliterator().characteristics());
  }

  /**
   * Test that {@link Spliterator#estimateSize()} reports the characteristics of
   * the spliterator of the one input stream for a concatenation of that one
   * input stream.
   */
  @Test
  public void testSpliteratorCharacteristicsOneInput() {
    Stream<String> stream = refStream(1, 0, false);
    IntStream intStream = intStream(1, 0, false);
    LongStream longStream = longStream(1, 0, false);
    DoubleStream doubleStream =  doubleStream(1, 0, false);
    Assert.assertEquals(
        "ref 1x0",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int 1x0",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long 1x0",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double 1x0",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        doubleStream.spliterator().characteristics());
    stream = refStream(1, 1, false);
    intStream = intStream(1, 1, false);
    longStream = longStream(1, 1, false);
    doubleStream =  doubleStream(1, 1, false);
    Assert.assertEquals(
        "ref 1x1",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int 1x1",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long 1x1",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double 1x1",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        doubleStream.spliterator().characteristics());
    stream = refStream(1, 3, false);
    intStream = intStream(1, 3, false);
    longStream = longStream(1, 3, false);
    doubleStream =  doubleStream(1, 3, false);
    Assert.assertEquals(
        "ref 1x3",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int 1x3",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long 1x3",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double 1x3",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        doubleStream.spliterator().characteristics());
  }

  /**
   * Tests that {@link Spliterator#characteristics()} for a concatenation of
   * streams reports the same characteristics before and after iteration.
   */
  @Test
  public void testSpliteratorCharacteristicsUnchangedByIteration() {
    Stream<String> stream = refStream(3, 1, false);
    IntStream intStream = intStream(3, 1, false);
    LongStream longStream = longStream(3, 1, false);
    DoubleStream doubleStream =  doubleStream(3, 1, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    Assert.assertEquals(
        "ref before",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        spliterator.characteristics());
    Assert.assertEquals(
        "int before",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        intSpliterator.characteristics());
    Assert.assertEquals(
        "long before",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        longSpliterator.characteristics());
    Assert.assertEquals(
        "double before",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        doubleSpliterator.characteristics());
    spliterator.forEachRemaining(x -> {});
    intSpliterator.forEachRemaining((int x) -> {});
    longSpliterator.forEachRemaining((long x) -> {});
    doubleSpliterator.forEachRemaining((double x) -> {});
    Assert.assertEquals(
        "ref after",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED,
        spliterator.characteristics());
    Assert.assertEquals(
        "int after",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        intSpliterator.characteristics());
    Assert.assertEquals(
        "long after",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        longSpliterator.characteristics());
    Assert.assertEquals(
        "double after",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.IMMUTABLE,
        doubleSpliterator.characteristics());
  }

  /**
   * Tests that {@link Spliterator#characteristics()} for a concatenation of
   * streams does not report the {@link Spliterator#SIZED} or
   * {@link Spliterator#SUBSIZED} characteristics when the input streams each
   * have a known size but the sum of their sizes overflows a {@code long}.
   */
  @Test
  public void testSpliteratorCharacteristicsSizeOverflow() {
    SplittableRandom random = new SplittableRandom();
    Stream<Integer> s1;
    Stream<Integer> s2;
    Stream<Integer> stream;
    IntStream intS1;
    IntStream intS2;
    IntStream intStream;
    LongStream longS1;
    LongStream longS2;
    LongStream longStream;
    DoubleStream doubleS1;
    DoubleStream doubleS2;
    DoubleStream doubleStream;
    s1 = random.ints(Long.MAX_VALUE / 2).boxed();
    s2 = random.ints(Long.MAX_VALUE / 2).boxed();
    stream = StreamConcatenation.concat(s1, s2);
    intS1 = random.ints(Long.MAX_VALUE / 2);
    intS2 = random.ints(Long.MAX_VALUE / 2);
    intStream = StreamConcatenation.concat(intS1, intS2);
    longS1 = random.longs(Long.MAX_VALUE / 2);
    longS2 = random.longs(Long.MAX_VALUE / 2);
    longStream = StreamConcatenation.concat(longS1, longS2);
    doubleS1 = random.doubles(Long.MAX_VALUE / 2);
    doubleS2 = random.doubles(Long.MAX_VALUE / 2);
    doubleStream = StreamConcatenation.concat(doubleS1, doubleS2);
    Assert.assertEquals(
        "ref no overflow",
        Spliterator.SIZED
            | Spliterator.SUBSIZED,
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int no overflow",
        Spliterator.SIZED
            | Spliterator.SUBSIZED
            | Spliterator.IMMUTABLE
            | Spliterator.NONNULL,
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long no overflow",
        Spliterator.SIZED
            | Spliterator.SUBSIZED
            | Spliterator.IMMUTABLE
            | Spliterator.NONNULL,
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double no overflow",
        Spliterator.SIZED
            | Spliterator.SUBSIZED
            | Spliterator.IMMUTABLE
            | Spliterator.NONNULL,
        doubleStream.spliterator().characteristics());
    s1 = random.ints(Long.MAX_VALUE).boxed();
    s2 = random.ints(Long.MAX_VALUE).boxed();
    stream = StreamConcatenation.concat(s1, s2);
    intS1 = random.ints(Long.MAX_VALUE);
    intS2 = random.ints(Long.MAX_VALUE);
    intStream = StreamConcatenation.concat(intS1, intS2);
    longS1 = random.longs(Long.MAX_VALUE);
    longS2 = random.longs(Long.MAX_VALUE);
    longStream = StreamConcatenation.concat(longS1, longS2);
    doubleS1 = random.doubles(Long.MAX_VALUE);
    doubleS2 = random.doubles(Long.MAX_VALUE);
    doubleStream = StreamConcatenation.concat(doubleS1, doubleS2);
    Assert.assertEquals(
        "ref overflow",
        0,
        stream.spliterator().characteristics());
    Assert.assertEquals(
        "int overflow",
        Spliterator.IMMUTABLE | Spliterator.NONNULL,
        intStream.spliterator().characteristics());
    Assert.assertEquals(
        "long overflow",
        Spliterator.IMMUTABLE | Spliterator.NONNULL,
        longStream.spliterator().characteristics());
    Assert.assertEquals(
        "double overflow",
        Spliterator.IMMUTABLE | Spliterator.NONNULL,
        doubleStream.spliterator().characteristics());
  }

  /**
   * Tests that {@link Spliterator#estimateSize()} for a concatenation of
   * streams returns {@link Long#MAX_VALUE} when the input streams each have a
   * known size but the sum of their sizes overflows a {@code long}.
   */
  @Test
  public void testSpliteratorEstimateSizeOverflow() {
    SplittableRandom random = new SplittableRandom();
    Stream<Integer> s1;
    Stream<Integer> s2;
    Stream<Integer> stream;
    IntStream intS1;
    IntStream intS2;
    IntStream intStream;
    LongStream longS1;
    LongStream longS2;
    LongStream longStream;
    DoubleStream doubleS1;
    DoubleStream doubleS2;
    DoubleStream doubleStream;
    s1 = random.ints(Long.MAX_VALUE / 2).boxed();
    s2 = random.ints(Long.MAX_VALUE / 2).boxed();
    stream = StreamConcatenation.concat(s1, s2);
    intS1 = random.ints(Long.MAX_VALUE / 2);
    intS2 = random.ints(Long.MAX_VALUE / 2);
    intStream = StreamConcatenation.concat(intS1, intS2);
    longS1 = random.longs(Long.MAX_VALUE / 2);
    longS2 = random.longs(Long.MAX_VALUE / 2);
    longStream = StreamConcatenation.concat(longS1, longS2);
    doubleS1 = random.doubles(Long.MAX_VALUE / 2);
    doubleS2 = random.doubles(Long.MAX_VALUE / 2);
    doubleStream = StreamConcatenation.concat(doubleS1, doubleS2);
    Assert.assertEquals(
        "ref no overflow",
        Long.MAX_VALUE - 1,
        stream.spliterator().estimateSize());
    Assert.assertEquals(
        "int no overflow",
        Long.MAX_VALUE - 1,
        intStream.spliterator().estimateSize());
    Assert.assertEquals(
        "long no overflow",
        Long.MAX_VALUE - 1,
        longStream.spliterator().estimateSize());
    Assert.assertEquals(
        "double no overflow",
        Long.MAX_VALUE - 1,
        doubleStream.spliterator().estimateSize());
    s1 = random.ints(Long.MAX_VALUE).boxed();
    s2 = random.ints(Long.MAX_VALUE).boxed();
    stream = StreamConcatenation.concat(s1, s2);
    intS1 = random.ints(Long.MAX_VALUE);
    intS2 = random.ints(Long.MAX_VALUE);
    intStream = StreamConcatenation.concat(intS1, intS2);
    longS1 = random.longs(Long.MAX_VALUE);
    longS2 = random.longs(Long.MAX_VALUE);
    longStream = StreamConcatenation.concat(longS1, longS2);
    doubleS1 = random.doubles(Long.MAX_VALUE);
    doubleS2 = random.doubles(Long.MAX_VALUE);
    doubleStream = StreamConcatenation.concat(doubleS1, doubleS2);
    Assert.assertEquals(
        "ref overflow",
        Long.MAX_VALUE,
        stream.spliterator().estimateSize());
    Assert.assertEquals(
        "int overflow",
        Long.MAX_VALUE,
        intStream.spliterator().estimateSize());
    Assert.assertEquals(
        "long overflow",
        Long.MAX_VALUE,
        longStream.spliterator().estimateSize());
    Assert.assertEquals(
        "double overflow",
        Long.MAX_VALUE,
        doubleStream.spliterator().estimateSize());
  }

  /**
   * Tests that multiple invocations of {@link Spliterator#trySplit()} for a
   * concatenation of streams may be used to produce spliterators equivalent to
   * the spliterators of the input streams.
   */
  @Test
  public void testSpliteratorTrySplitBackToInputs() {
    Stream<String> stream = refStream(3, 3, false);
    IntStream intStream = intStream(3, 3, false);
    LongStream longStream = longStream(3, 3, false);
    DoubleStream doubleStream = doubleStream(3, 3, false);
    Spliterator<String> right = stream.spliterator();
    Spliterator<String> left = right.trySplit();
    Spliterator<String> mid = right.trySplit();
    Spliterator.OfInt intRight = intStream.spliterator();
    Spliterator.OfInt intLeft = intRight.trySplit();
    Spliterator.OfInt intMid = intRight.trySplit();
    Spliterator.OfLong longRight = longStream.spliterator();
    Spliterator.OfLong longLeft = longRight.trySplit();
    Spliterator.OfLong longMid = longRight.trySplit();
    Spliterator.OfDouble doubleRight = doubleStream.spliterator();
    Spliterator.OfDouble doubleLeft = doubleRight.trySplit();
    Spliterator.OfDouble doubleMid = doubleRight.trySplit();
    left.tryAdvance(x -> Assert.assertEquals("ref left 1", "0,0", x));
    left.tryAdvance(x -> Assert.assertEquals("ref left 2", "0,1", x));
    left.tryAdvance(x -> Assert.assertEquals("ref left 3", "0,2", x));
    left.tryAdvance(x -> Assert.fail("ref left end"));
    mid.tryAdvance(x -> Assert.assertEquals("ref mid 1", "1,0", x));
    mid.tryAdvance(x -> Assert.assertEquals("ref mid 2", "1,1", x));
    mid.tryAdvance(x -> Assert.assertEquals("ref mid 3", "1,2", x));
    mid.tryAdvance(x -> Assert.fail("ref mid end"));
    right.tryAdvance(x -> Assert.assertEquals("ref right 1", "2,0", x));
    right.tryAdvance(x -> Assert.assertEquals("ref right 2", "2,1", x));
    right.tryAdvance(x -> Assert.assertEquals("ref right 3", "2,2", x));
    right.tryAdvance(x -> Assert.fail("ref right end"));
    intLeft.tryAdvance((int x) -> Assert.assertEquals("int left 1", 0, x));
    intLeft.tryAdvance((int x) -> Assert.assertEquals("int left 2", 1, x));
    intLeft.tryAdvance((int x) -> Assert.assertEquals("int left 3", 2, x));
    intLeft.tryAdvance((int x) -> Assert.fail("int left end"));
    intMid.tryAdvance((int x) -> Assert.assertEquals("int mid 1", 3, x));
    intMid.tryAdvance((int x) -> Assert.assertEquals("int mid 2", 4, x));
    intMid.tryAdvance((int x) -> Assert.assertEquals("int mid 3", 5, x));
    intMid.tryAdvance((int x) -> Assert.fail("int mid end"));
    intRight.tryAdvance((int x) -> Assert.assertEquals("int right 1", 6, x));
    intRight.tryAdvance((int x) -> Assert.assertEquals("int right 2", 7, x));
    intRight.tryAdvance((int x) -> Assert.assertEquals("int right 3", 8, x));
    intRight.tryAdvance((int x) -> Assert.fail("int right end"));
    longLeft.tryAdvance((long x) -> Assert.assertEquals("int left 1", 0L, x));
    longLeft.tryAdvance((long x) -> Assert.assertEquals("int left 2", 1L, x));
    longLeft.tryAdvance((long x) -> Assert.assertEquals("int left 3", 2L, x));
    longLeft.tryAdvance((long x) -> Assert.fail("int left end"));
    longMid.tryAdvance((long x) -> Assert.assertEquals("int mid 1", 3L, x));
    longMid.tryAdvance((long x) -> Assert.assertEquals("int mid 2", 4L, x));
    longMid.tryAdvance((long x) -> Assert.assertEquals("int mid 3", 5L, x));
    longMid.tryAdvance((long x) -> Assert.fail("int mid end"));
    longRight.tryAdvance((long x) -> Assert.assertEquals("int right 1", 6L, x));
    longRight.tryAdvance((long x) -> Assert.assertEquals("int right 2", 7L, x));
    longRight.tryAdvance((long x) -> Assert.assertEquals("int right 3", 8L, x));
    longRight.tryAdvance((long x) -> Assert.fail("int right end"));
    doubleLeft.tryAdvance((double x) -> Assert.assertEquals("int left 1", 0d, x, 0.1));
    doubleLeft.tryAdvance((double x) -> Assert.assertEquals("int left 2", 1d, x, 0.1));
    doubleLeft.tryAdvance((double x) -> Assert.assertEquals("int left 3", 2d, x, 0.1));
    doubleLeft.tryAdvance((double x) -> Assert.fail("int left end"));
    doubleMid.tryAdvance((double x) -> Assert.assertEquals("int mid 1", 3d, x, 0.1));
    doubleMid.tryAdvance((double x) -> Assert.assertEquals("int mid 2", 4d, x, 0.1));
    doubleMid.tryAdvance((double x) -> Assert.assertEquals("int mid 3", 5d, x, 0.1));
    doubleMid.tryAdvance((double x) -> Assert.fail("int mid end"));
    doubleRight.tryAdvance((double x) -> Assert.assertEquals("int right 1", 6d, x, 0.1));
    doubleRight.tryAdvance((double x) -> Assert.assertEquals("int right 2", 7d, x, 0.1));
    doubleRight.tryAdvance((double x) -> Assert.assertEquals("int right 3", 8d, x, 0.1));
    doubleRight.tryAdvance((double x) -> Assert.fail("int right end"));
  }

  /**
   * Tests that {@link Spliterator#getComparator()} for a concatenation of zero
   * input streams throws an {@link IllegalStateException}.
   */
  @Test
  public void testSpliteratorGetComparatorInvalidForNoInputs() {
    Stream<String> stream = refStream(0, 0, false);
    IntStream intStream = intStream(0, 0, false);
    LongStream longStream = longStream(0, 0, false);
    DoubleStream doubleStream = doubleStream(0, 0, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    try {
      spliterator.getComparator();
      Assert.fail("ref");
    } catch (IllegalStateException expected) {}
    try {
      intSpliterator.getComparator();
      Assert.fail("int");
    } catch (IllegalStateException expected) {}
    try {
      longSpliterator.getComparator();
      Assert.fail("long");
    } catch (IllegalStateException expected) {}
    try {
      doubleSpliterator.getComparator();
      Assert.fail("double");
    } catch (IllegalStateException expected) {}
  }

  /**
   * Tests that {@link Spliterator#getComparator()} for a concatenation of one
   * input stream that is not {@link Spliterator#SORTED} throws an
   * {@link IllegalStateException}.
   */
  @Test
  public void testSpliteratorGetComparatorInvalidForOneUnsortedInput() {
    Stream<String> stream = refStream(1, 0, false);
    IntStream intStream = intStream(1, 0, false);
    LongStream longStream = longStream(1, 0, false);
    DoubleStream doubleStream = doubleStream(1, 0, false);
    Spliterator<String> spliterator = stream.spliterator();
    Spliterator.OfInt intSpliterator = intStream.spliterator();
    Spliterator.OfLong longSpliterator = longStream.spliterator();
    Spliterator.OfDouble doubleSpliterator = doubleStream.spliterator();
    try {
      spliterator.getComparator();
      Assert.fail("ref 1x0");
    } catch (IllegalStateException expected) {}
    try {
      intSpliterator.getComparator();
      Assert.fail("int 1x0");
    } catch (IllegalStateException expected) {}
    try {
      longSpliterator.getComparator();
      Assert.fail("long 1x0");
    } catch (IllegalStateException expected) {}
    try {
      doubleSpliterator.getComparator();
      Assert.fail("double 1x0");
    } catch (IllegalStateException expected) {}
    stream = refStream(1, 1, false);
    intStream = intStream(1, 1, false);
    longStream = longStream(1, 1, false);
    doubleStream = doubleStream(1, 1, false);
    spliterator = stream.spliterator();
    intSpliterator = intStream.spliterator();
    longSpliterator = longStream.spliterator();
    doubleSpliterator = doubleStream.spliterator();
    try {
      spliterator.getComparator();
      Assert.fail("ref 1x1");
    } catch (IllegalStateException expected) {}
    try {
      intSpliterator.getComparator();
      Assert.fail("int 1x1");
    } catch (IllegalStateException expected) {}
    try {
      longSpliterator.getComparator();
      Assert.fail("long 1x1");
    } catch (IllegalStateException expected) {}
    try {
      doubleSpliterator.getComparator();
      Assert.fail("double 1x1");
    } catch (IllegalStateException expected) {}
    stream = refStream(1, 3, false);
    intStream = intStream(1, 3, false);
    longStream = longStream(1, 3, false);
    doubleStream = doubleStream(1, 3, false);
    spliterator = stream.spliterator();
    intSpliterator = intStream.spliterator();
    longSpliterator = longStream.spliterator();
    doubleSpliterator = doubleStream.spliterator();
    try {
      spliterator.getComparator();
      Assert.fail("ref 1xmany");
    } catch (IllegalStateException expected) {}
    try {
      intSpliterator.getComparator();
      Assert.fail("int 1xmany");
    } catch (IllegalStateException expected) {}
    try {
      longSpliterator.getComparator();
      Assert.fail("long 1xmany");
    } catch (IllegalStateException expected) {}
    try {
      doubleSpliterator.getComparator();
      Assert.fail("double 1xmany");
    } catch (IllegalStateException expected) {}
  }

  /**
   * Tests that {@link Spliterator#getComparator()} for a concatenation of one
   * input stream that is {@link Spliterator#SORTED} does not throw an
   * {@link IllegalStateException}.
   */
  @Test
  public void testSpliteratorGetComparatorValidForOneSortedInput() {
    Stream<String> stream = refStream(1, 0, true);
    IntStream intStream = intStream(1, 0, true);
    LongStream longStream = longStream(1, 0, true);
    DoubleStream doubleStream = doubleStream(1, 0, true);
    stream.spliterator().getComparator();
    intStream.spliterator().getComparator();
    longStream.spliterator().getComparator();
    doubleStream.spliterator().getComparator();
    stream = refStream(1, 1, true);
    intStream = intStream(1, 1, true);
    longStream = longStream(1, 1, true);
    doubleStream = doubleStream(1, 1, true);
    stream.spliterator().getComparator();
    intStream.spliterator().getComparator();
    longStream.spliterator().getComparator();
    doubleStream.spliterator().getComparator();
    stream = refStream(1, 3, true);
    intStream = intStream(1, 3, true);
    longStream = longStream(1, 3, true);
    doubleStream = doubleStream(1, 3, true);
    stream.spliterator().getComparator();
    intStream.spliterator().getComparator();
    longStream.spliterator().getComparator();
    doubleStream.spliterator().getComparator();
  }

  /**
   * Tests that {@link Spliterator#getComparator()} for a concatenation of
   * multiple input streams throws an {@link IllegalStateException}, even when
   * all of those input streams are {@link Spliterator#SORTED}.  However,
   * invocations of {@link Spliterator#trySplit()} should eventually produce
   * spliterators that report their comparators without throwing.
   */
  @Test
  public void testSpliteratorGetComparatorInvalidForMultipleInputs() {
    Stream<String> stream = refStream(3, 1, true);
    IntStream intStream = intStream(3, 1, true);
    LongStream longStream = longStream(3, 1, true);
    DoubleStream doubleStream = doubleStream(3, 1, true);
    Spliterator<String> right = stream.spliterator();
    Spliterator.OfInt intRight = intStream.spliterator();
    Spliterator.OfLong longRight = longStream.spliterator();
    Spliterator.OfDouble doubleRight = doubleStream.spliterator();
    try {
      right.getComparator();
      Assert.fail("ref");
    } catch (IllegalStateException expected) {}
    try {
      intRight.getComparator();
      Assert.fail("int");
    } catch (IllegalStateException expected) {}
    try {
      longRight.getComparator();
      Assert.fail("long");
    } catch (IllegalStateException expected) {}
    try {
      doubleRight.getComparator();
      Assert.fail("double");
    } catch (IllegalStateException expected) {}
    Spliterator<String> left = right.trySplit();
    Spliterator<String> mid = right.trySplit();
    Spliterator.OfInt intLeft = intRight.trySplit();
    Spliterator.OfInt intMid = intRight.trySplit();
    Spliterator.OfLong longLeft = longRight.trySplit();
    Spliterator.OfLong longMid = longRight.trySplit();
    Spliterator.OfDouble doubleLeft = doubleRight.trySplit();
    Spliterator.OfDouble doubleMid = doubleRight.trySplit();
    left.getComparator();
    mid.getComparator();
    right.getComparator();
    intLeft.getComparator();
    intMid.getComparator();
    intRight.getComparator();
    longLeft.getComparator();
    longMid.getComparator();
    longRight.getComparator();
    doubleLeft.getComparator();
    doubleMid.getComparator();
    doubleRight.getComparator();
  }

  /**
   * Tests that {@link Spliterator#characteristics()} for a concatenation of
   * multiple input streams will <em>not</em> report
   * {@link Spliterator#DISTINCT} or {@link Spliterator#SORTED}, even when all
   * of those input spliterators report those characteristics.  However,
   * invocations of {@link Spliterator#trySplit()} should eventually produce
   * spliterators with those characteristics.
   */
  @Test
  public void testSpliteratorCharacteristicsNotDistinctOrSortedForMultipleInputs() {
    Stream<String> stream = refStream(3, 1, true);
    IntStream intStream = intStream(3, 1, true);
    LongStream longStream = longStream(3, 1, true);
    DoubleStream doubleStream = doubleStream(3, 1, true);
    Spliterator<String> right = stream.spliterator();
    Spliterator.OfInt intRight = intStream.spliterator();
    Spliterator.OfLong longRight = longStream.spliterator();
    Spliterator.OfDouble doubleRight = doubleStream.spliterator();
    Assert.assertEquals(
        "ref all",
        Spliterator.ORDERED | Spliterator.SIZED,
        right.characteristics());
    Assert.assertEquals(
        "int all",
        Spliterator.ORDERED,
        intRight.characteristics());
    Assert.assertEquals(
        "long all",
        Spliterator.ORDERED,
        longRight.characteristics());
    Assert.assertEquals(
        "double all",
        Spliterator.ORDERED,
        doubleRight.characteristics());
    Spliterator<String> left = right.trySplit();
    Spliterator<String> mid = right.trySplit();
    Spliterator.OfInt intLeft = intRight.trySplit();
    Spliterator.OfInt intMid = intRight.trySplit();
    Spliterator.OfLong longLeft = longRight.trySplit();
    Spliterator.OfLong longMid = longRight.trySplit();
    Spliterator.OfDouble doubleLeft = doubleRight.trySplit();
    Spliterator.OfDouble doubleMid = doubleRight.trySplit();
    Assert.assertEquals(
        "ref right",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.SORTED,
        right.characteristics());
    Assert.assertEquals(
        "ref left",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.SORTED,
        left.characteristics());
    Assert.assertEquals(
        "ref mid",
        Spliterator.ORDERED | Spliterator.SIZED | Spliterator.DISTINCT | Spliterator.SORTED,
        mid.characteristics());
    Assert.assertEquals(
        "int right",
        Spliterator.ORDERED | Spliterator.SORTED,
        intRight.characteristics());
    Assert.assertEquals(
        "int left",
        Spliterator.ORDERED | Spliterator.SORTED,
        intLeft.characteristics());
    Assert.assertEquals(
        "int mid",
        Spliterator.ORDERED | Spliterator.SORTED,
        intMid.characteristics());
    Assert.assertEquals(
        "long right",
        Spliterator.ORDERED | Spliterator.SORTED,
        longRight.characteristics());
    Assert.assertEquals(
        "long left",
        Spliterator.ORDERED | Spliterator.SORTED,
        longLeft.characteristics());
    Assert.assertEquals(
        "long mid",
        Spliterator.ORDERED | Spliterator.SORTED,
        longMid.characteristics());
    Assert.assertEquals(
        "double right",
        Spliterator.ORDERED | Spliterator.SORTED,
        doubleRight.characteristics());
    Assert.assertEquals(
        "double left",
        Spliterator.ORDERED | Spliterator.SORTED,
        doubleLeft.characteristics());
    Assert.assertEquals(
        "double mid",
        Spliterator.ORDERED | Spliterator.SORTED,
        doubleMid.characteristics());
  }

  /**
   * Tests that a concatenation of streams is not parallel when none of the
   * input streams is parallel.
   */
  @Test
  public void testIsNotParallelWhenNonParallel() {
    Stream<String> s1 = Stream.of("a");
    Stream<String> s2 = Stream.of("b");
    Stream<String> s3 = Stream.of("c");
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intS1 = IntStream.of(1);
    IntStream intS2 = IntStream.of(2);
    IntStream intS3 = IntStream.of(3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longS1 = LongStream.of(1L);
    LongStream longS2 = LongStream.of(2L);
    LongStream longS3 = LongStream.of(3L);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleS1 = DoubleStream.of(1d);
    DoubleStream doubleS2 = DoubleStream.of(2d);
    DoubleStream doubleS3 = DoubleStream.of(3d);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    Assert.assertFalse("ref", stream.isParallel());
    Assert.assertFalse("int", intStream.isParallel());
    Assert.assertFalse("long", longStream.isParallel());
    Assert.assertFalse("double", doubleStream.isParallel());
  }

  /**
   * Tests that a concatenation of streams is parallel when one of the input
   * streams is parallel.
   */
  @Test
  public void testIsParallelWhenOneParallel() {
    Stream<String> s1 = Stream.of("a");
    Stream<String> s2 = Stream.of("b").parallel();
    Stream<String> s3 = Stream.of("c");
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intS1 = IntStream.of(1);
    IntStream intS2 = IntStream.of(2).parallel();
    IntStream intS3 = IntStream.of(3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longS1 = LongStream.of(1L);
    LongStream longS2 = LongStream.of(2L).parallel();
    LongStream longS3 = LongStream.of(3L);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleS1 = DoubleStream.of(1d);
    DoubleStream doubleS2 = DoubleStream.of(2d).parallel();
    DoubleStream doubleS3 = DoubleStream.of(3d);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    Assert.assertTrue("ref", stream.isParallel());
    Assert.assertTrue("int", intStream.isParallel());
    Assert.assertTrue("long", longStream.isParallel());
    Assert.assertTrue("double", doubleStream.isParallel());
  }

  /**
   * Tests that concatenation is a terminal operation for the input streams.
   */
  @Test
  public void testIsTerminal() {
    Stream<String> s1 = Stream.of("a");
    Stream<String> s2 = Stream.of("b");
    Stream<String> s3 = Stream.of("c");
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intS1 = IntStream.of(1);
    IntStream intS2 = IntStream.of(2);
    IntStream intS3 = IntStream.of(3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longS1 = LongStream.of(1L);
    LongStream longS2 = LongStream.of(2L);
    LongStream longS3 = LongStream.of(3L);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleS1 = DoubleStream.of(1d);
    DoubleStream doubleS2 = DoubleStream.of(2d);
    DoubleStream doubleS3 = DoubleStream.of(3d);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    try {
      s1.forEach(x -> {});
      Assert.fail("ref 1");
    } catch (IllegalStateException expected) {}
    try {
      s2.forEach(x -> {});
      Assert.fail("ref 2");
    } catch (IllegalStateException expected) {}
    try {
      s3.forEach(x -> {});
      Assert.fail("ref 3");
    } catch (IllegalStateException expected) {}
    try {
      intS1.forEach(x -> {});
      Assert.fail("int 1");
    } catch (IllegalStateException expected) {}
    try {
      intS2.forEach(x -> {});
      Assert.fail("int 2");
    } catch (IllegalStateException expected) {}
    try {
      intS3.forEach(x -> {});
      Assert.fail("int 3");
    } catch (IllegalStateException expected) {}
    try {
      longS1.forEach(x -> {});
      Assert.fail("long 1");
    } catch (IllegalStateException expected) {}
    try {
      longS2.forEach(x -> {});
      Assert.fail("long 2");
    } catch (IllegalStateException expected) {}
    try {
      longS3.forEach(x -> {});
      Assert.fail("long 3");
    } catch (IllegalStateException expected) {}
    try {
      doubleS1.forEach(x -> {});
      Assert.fail("double 1");
    } catch (IllegalStateException expected) {}
    try {
      doubleS2.forEach(x -> {});
      Assert.fail("double 2");
    } catch (IllegalStateException expected) {}
    try {
      doubleS3.forEach(x -> {});
      Assert.fail("double 3");
    } catch (IllegalStateException expected) {}
  }

  /**
   * Tests that a concatenation of zero input streams contains the expected
   * elements (none).
   */
  @Test
  public void testContainsExpectedElementsWhenNoStreams() {
    @SuppressWarnings("unchecked")
    Stream<String> stream = StreamConcatenation.concat((Stream<String>[]) new Stream<?>[0]);
    IntStream intStream = StreamConcatenation.concat(new IntStream[0]);
    LongStream longStream = StreamConcatenation.concat(new LongStream[0]);
    DoubleStream doubleStream = StreamConcatenation.concat(new DoubleStream[0]);
    List<String> out = stream.collect(Collectors.toList());
    List<Integer> intOut = intStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Long> longOut = longStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Double> doubleOut = doubleStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    Assert.assertEquals("ref", Collections.<String>emptyList(), out);
    Assert.assertEquals("int", Collections.<Integer>emptyList(), intOut);
    Assert.assertEquals("long", Collections.<Long>emptyList(), longOut);
    Assert.assertEquals("double", Collections.<Double>emptyList(), doubleOut);
  }

  /**
   * Tests that a concatenation of a single input stream contains the expected
   * elements (the elements of that input stream).
   */
  @Test
  public void testContainsExpectedElementsWhenOneStream() {
    Stream<String> s1 = Stream.of("a", "b", "c");
    IntStream intS1 = IntStream.of(1, 2, 3);
    LongStream longS1 = LongStream.of(1L, 2L, 3L);
    DoubleStream doubleS1 = DoubleStream.of(1d, 2d, 3d);
    Stream<String> stream = StreamConcatenation.concat(s1);
    IntStream intStream = StreamConcatenation.concat(intS1);
    LongStream longStream = StreamConcatenation.concat(longS1);
    DoubleStream doubleStream = StreamConcatenation.concat(doubleS1);
    List<String> out = stream.collect(Collectors.toList());
    List<Integer> intOut = intStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Long> longOut = longStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Double> doubleOut = doubleStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    Assert.assertEquals("ref", Arrays.asList("a", "b", "c"), out);
    Assert.assertEquals("int", Arrays.asList(1, 2, 3), intOut);
    Assert.assertEquals("long", Arrays.asList(1L, 2L, 3L), longOut);
    Assert.assertEquals("double", Arrays.asList(1d, 2d, 3d), doubleOut);
  }

  /**
   * Tests that a concatenation of multiple input streams contains the expected
   * elements (the elements of each of the input streams).
   */
  @Test
  public void testContainsExpectedElementsWhenMultipleStreams() {
    Stream<String> s1 = Stream.of("a", "b", "c");
    Stream<String> s2 = Stream.of("d", "e");
    Stream<String> s3 = Stream.of("f");
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intS1 = IntStream.of(1, 2, 3);
    IntStream intS2 = IntStream.of(4, 5);
    IntStream intS3 = IntStream.of(6);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longS1 = LongStream.of(1L, 2L, 3L);
    LongStream longS2 = LongStream.of(4L, 5L);
    LongStream longS3 = LongStream.of(6L);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleS1 = DoubleStream.of(1d, 2d, 3d);
    DoubleStream doubleS2 = DoubleStream.of(4d, 5d);
    DoubleStream doubleS3 = DoubleStream.of(6d);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    List<String> out = stream.collect(Collectors.toList());
    List<Integer> intOut = intStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Long> longOut = longStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    List<Double> doubleOut = doubleStream.collect(
        ArrayList::new, ArrayList::add, ArrayList::addAll);
    Assert.assertEquals("ref", Arrays.asList("a", "b", "c", "d", "e", "f"), out);
    Assert.assertEquals("int", Arrays.asList(1, 2, 3, 4, 5, 6), intOut);
    Assert.assertEquals("long", Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L), longOut);
    Assert.assertEquals("double", Arrays.asList(1d, 2d, 3d, 4d, 5d, 6d), doubleOut);
  }

  /**
   * Tests that closing a concatenation of zero input streams completes without
   * throwing an exception.
   */
  @Test
  public void testCloseNone() {
    @SuppressWarnings("unchecked")
    Stream<String> stream = StreamConcatenation.concat((Stream<String>[]) new Stream<?>[0]);
    IntStream intStream = StreamConcatenation.concat(new IntStream[0]);
    LongStream longStream = StreamConcatenation.concat(new LongStream[0]);
    DoubleStream doubleStream = StreamConcatenation.concat(new DoubleStream[0]);
    stream.close();
    intStream.close();
    longStream.close();
    doubleStream.close();
  }

  /**
   * Tests that closing a concatenation of a single input stream closes that
   * input stream.
   */
  @Test
  public void testCloseOne() {
    LongAdder counter = new LongAdder();
    LongAdder intCounter = new LongAdder();
    LongAdder longCounter = new LongAdder();
    LongAdder doubleCounter = new LongAdder();
    Stream<String> s1 = Stream.of("a").onClose(counter::increment);
    IntStream intS1 = IntStream.of(1).onClose(intCounter::increment);
    LongStream longS1 = LongStream.of(1L).onClose(longCounter::increment);
    DoubleStream doubleS1 = DoubleStream.of(1d).onClose(doubleCounter::increment);
    Stream<String> stream = StreamConcatenation.concat(s1);
    IntStream intStream = StreamConcatenation.concat(intS1);
    LongStream longStream = StreamConcatenation.concat(longS1);
    DoubleStream doubleStream = StreamConcatenation.concat(doubleS1);
    stream.close();
    intStream.close();
    longStream.close();
    doubleStream.close();
    Assert.assertEquals("ref", 1, counter.sum());
    Assert.assertEquals("int", 1, intCounter.sum());
    Assert.assertEquals("long", 1, longCounter.sum());
    Assert.assertEquals("double", 1, doubleCounter.sum());
  }

  /**
   * Tests that closing a concatenation of multiple input streams closes each of
   * the input streams.
   */
  @Test
  public void testCloseMultiple() {
    LongAdder c1 = new LongAdder();
    LongAdder c2 = new LongAdder();
    LongAdder c3 = new LongAdder();
    LongAdder intC1 = new LongAdder();
    LongAdder intC2 = new LongAdder();
    LongAdder intC3 = new LongAdder();
    LongAdder longC1 = new LongAdder();
    LongAdder longC2 = new LongAdder();
    LongAdder longC3 = new LongAdder();
    LongAdder doubleC1 = new LongAdder();
    LongAdder doubleC2 = new LongAdder();
    LongAdder doubleC3 = new LongAdder();
    Stream<String> s1 = Stream.of("a").onClose(c1::increment);
    Stream<String> s2 = Stream.of("b").onClose(c2::increment);
    Stream<String> s3 = Stream.of("c").onClose(c3::increment);
    IntStream intS1 = IntStream.of(1).onClose(intC1::increment);
    IntStream intS2 = IntStream.of(2).onClose(intC2::increment);
    IntStream intS3 = IntStream.of(3).onClose(intC3::increment);
    LongStream longS1 = LongStream.of(1L).onClose(longC1::increment);
    LongStream longS2 = LongStream.of(2L).onClose(longC2::increment);
    LongStream longS3 = LongStream.of(3L).onClose(longC3::increment);
    DoubleStream doubleS1 = DoubleStream.of(1d).onClose(doubleC1::increment);
    DoubleStream doubleS2 = DoubleStream.of(2d).onClose(doubleC2::increment);
    DoubleStream doubleS3 = DoubleStream.of(3d).onClose(doubleC3::increment);
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    stream.close();
    intStream.close();
    longStream.close();
    doubleStream.close();
    Assert.assertEquals("ref counter 1", 1, c1.sum());
    Assert.assertEquals("ref counter 2", 1, c2.sum());
    Assert.assertEquals("ref counter 3", 1, c3.sum());
    Assert.assertEquals("int counter 1", 1, intC1.sum());
    Assert.assertEquals("int counter 2", 1, intC2.sum());
    Assert.assertEquals("int counter 3", 1, intC3.sum());
    Assert.assertEquals("long counter 1", 1, longC1.sum());
    Assert.assertEquals("long counter 2", 1, longC2.sum());
    Assert.assertEquals("long counter 3", 1, longC3.sum());
    Assert.assertEquals("double counter 1", 1, doubleC1.sum());
    Assert.assertEquals("double counter 2", 1, doubleC2.sum());
    Assert.assertEquals("double counter 3", 1, doubleC3.sum());
  }

  static final class E1 extends RuntimeException {}
  static final class E2 extends RuntimeException {}
  static final class E3 extends RuntimeException {}

  /**
   * Tests that closing a concatenation of streams closes each of the input
   * streams even when the close handlers throw exceptions, and that the first
   * exception thrown is rethrown, and subsequent exceptions are suppressed in
   * the first.
   */
  @Test
  public void testCloseWithExceptions() {
    LongAdder c1 = new LongAdder();
    LongAdder c2 = new LongAdder();
    LongAdder c3 = new LongAdder();
    LongAdder intC1 = new LongAdder();
    LongAdder intC2 = new LongAdder();
    LongAdder intC3 = new LongAdder();
    LongAdder longC1 = new LongAdder();
    LongAdder longC2 = new LongAdder();
    LongAdder longC3 = new LongAdder();
    LongAdder doubleC1 = new LongAdder();
    LongAdder doubleC2 = new LongAdder();
    LongAdder doubleC3 = new LongAdder();
    Stream<String> s1 = Stream.of("a").onClose(() -> {
      c1.increment();
      throw new StreamConcatenationTest.E1();
    });
    Stream<String> s2 = Stream.of("b").onClose(() -> {
      c2.increment();
      throw new StreamConcatenationTest.E2();
    });
    Stream<String> s3 = Stream.of("c").onClose(() -> {
      c3.increment();
      throw new StreamConcatenationTest.E3();
    });
    IntStream intS1 = IntStream.of(1).onClose(() -> {
      intC1.increment();
      throw new StreamConcatenationTest.E1();
    });
    IntStream intS2 = IntStream.of(2).onClose(() -> {
      intC2.increment();
      throw new StreamConcatenationTest.E2();
    });
    IntStream intS3 = IntStream.of(3).onClose(() -> {
      intC3.increment();
      throw new StreamConcatenationTest.E3();
    });
    LongStream longS1 = LongStream.of(1L).onClose(() -> {
      longC1.increment();
      throw new StreamConcatenationTest.E1();
    });
    LongStream longS2 = LongStream.of(2L).onClose(() -> {
      longC2.increment();
      throw new StreamConcatenationTest.E2();
    });
    LongStream longS3 = LongStream.of(3L).onClose(() -> {
      longC3.increment();
      throw new StreamConcatenationTest.E3();
    });
    DoubleStream doubleS1 = DoubleStream.of(1d).onClose(() -> {
      doubleC1.increment();
      throw new StreamConcatenationTest.E1();
    });
    DoubleStream doubleS2 = DoubleStream.of(2d).onClose(() -> {
      doubleC2.increment();
      throw new StreamConcatenationTest.E2();
    });
    DoubleStream doubleS3 = DoubleStream.of(3d).onClose(() -> {
      doubleC3.increment();
      throw new StreamConcatenationTest.E3();
    });
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    try {
      stream.close();
      Assert.fail("ref close did not throw");
    } catch (Throwable expected) {
      Assert.assertTrue("ref thrown type", expected instanceof StreamConcatenationTest.E1);
      Throwable[] suppressed = expected.getSuppressed();
      Assert.assertEquals("ref suppressed length", 2, suppressed.length);
      Assert.assertTrue("ref suppressed type 1", suppressed[0] instanceof StreamConcatenationTest.E2);
      Assert.assertTrue("ref suppressed type 2", suppressed[1] instanceof StreamConcatenationTest.E3);
    }
    try {
      intStream.close();
      Assert.fail("int close did not throw");
    } catch (Throwable expected) {
      Assert.assertTrue("int thrown type", expected instanceof StreamConcatenationTest.E1);
      Throwable[] suppressed = expected.getSuppressed();
      Assert.assertEquals("int suppressed length", 2, suppressed.length);
      Assert.assertTrue("int suppressed type 1", suppressed[0] instanceof StreamConcatenationTest.E2);
      Assert.assertTrue("int suppressed type 2", suppressed[1] instanceof StreamConcatenationTest.E3);
    }
    try {
      longStream.close();
      Assert.fail("long close did not throw");
    } catch (Throwable expected) {
      Assert.assertTrue("long thrown type", expected instanceof StreamConcatenationTest.E1);
      Throwable[] suppressed = expected.getSuppressed();
      Assert.assertEquals("long suppressed length", 2, suppressed.length);
      Assert.assertTrue("long suppressed type 1", suppressed[0] instanceof StreamConcatenationTest.E2);
      Assert.assertTrue("long suppressed type 2", suppressed[1] instanceof StreamConcatenationTest.E3);
    }
    try {
      doubleStream.close();
      Assert.fail("double close did not throw");
    } catch (Throwable expected) {
      Assert.assertTrue("double thrown type", expected instanceof StreamConcatenationTest.E1);
      Throwable[] suppressed = expected.getSuppressed();
      Assert.assertEquals("double suppressed length", 2, suppressed.length);
      Assert.assertTrue("double suppressed type 1", suppressed[0] instanceof StreamConcatenationTest.E2);
      Assert.assertTrue("double suppressed type 2", suppressed[1] instanceof StreamConcatenationTest.E3);
    }
    Assert.assertEquals("ref counter 1", 1, c1.sum());
    Assert.assertEquals("ref counter 2", 1, c2.sum());
    Assert.assertEquals("ref counter 3", 1, c3.sum());
    Assert.assertEquals("int counter 1", 1, intC1.sum());
    Assert.assertEquals("int counter 2", 1, intC2.sum());
    Assert.assertEquals("int counter 3", 1, intC3.sum());
    Assert.assertEquals("long counter 1", 1, longC1.sum());
    Assert.assertEquals("long counter 2", 1, longC2.sum());
    Assert.assertEquals("long counter 3", 1, longC3.sum());
    Assert.assertEquals("double counter 1", 1, doubleC1.sum());
    Assert.assertEquals("double counter 2", 1, doubleC2.sum());
    Assert.assertEquals("double counter 3", 1, doubleC3.sum());
  }

  /**
   * Tests that a concatenation of streams that are infinite results in a stream
   * whose {@link Stream#findAny()} operation terminates successfully.
   *
   * <p>This test is meant to highlight an advantage over
   * {@code Stream.of(inputs).flatMap(x -> x)}, which would enter an infinite
   * loop upon the call to {@link Stream#findAny()}}.
   */
  @Test
  public void testFindAnyTerminatesWhenInfiniteStreams() {
    Stream<String> s1 = Stream.generate(() -> "a");
    Stream<String> s2 = Stream.generate(() -> "b");
    Stream<String> s3 = Stream.generate(() -> "c");
    Stream<String> stream = StreamConcatenation.concat(s1, s2, s3);
    IntStream intS1 = IntStream.generate(() -> 1);
    IntStream intS2 = IntStream.generate(() -> 2);
    IntStream intS3 = IntStream.generate(() -> 3);
    IntStream intStream = StreamConcatenation.concat(
        intS1, intS2, intS3);
    LongStream longS1 = LongStream.generate(() -> 1L);
    LongStream longS2 = LongStream.generate(() -> 2L);
    LongStream longS3 = LongStream.generate(() -> 3L);
    LongStream longStream = StreamConcatenation.concat(
        longS1, longS2, longS3);
    DoubleStream doubleS1 = DoubleStream.generate(() -> 1d);
    DoubleStream doubleS2 = DoubleStream.generate(() -> 2d);
    DoubleStream doubleS3 = DoubleStream.generate(() -> 3d);
    DoubleStream doubleStream = StreamConcatenation.concat(
        doubleS1, doubleS2, doubleS3);
    Assert.assertTrue("ref", stream.findAny().isPresent());
    Assert.assertTrue("int", intStream.findAny().isPresent());
    Assert.assertTrue("long", longStream.findAny().isPresent());
    Assert.assertTrue("double", doubleStream.findAny().isPresent());
  }

  /**
   * Tests that a concatenation of an extremely large number of input streams
   * results in a stream whose terminal operations do not incur a
   * {@link StackOverflowError}.
   *
   * <p>This test is meant to highlight an advantage of over
   * {@code Stream.of(inputs).reduce(Stream::concat).orElseGet(Stream::empty)},
   * which would throw a {@link StackOverflowError} if given enough inputs.
   */
  @Test
  public void testNoStackOverflowWhenSoManyStreams() {
    int numberOfStreams = 100_000;
    String[] elements= { "one", "two" };
    int[] intElements = { 1, 2 };
    long[] longElements = { 1L, 2L };
    double[] doubleElements = { 1d, 2d };
    @SuppressWarnings("unchecked")
    Stream<String>[] inputs = (Stream<String>[]) new Stream<?>[numberOfStreams];
    IntStream[] intInputs = new IntStream[numberOfStreams];
    LongStream[] longInputs = new LongStream[numberOfStreams];
    DoubleStream[] doubleInputs = new DoubleStream[numberOfStreams];
    Arrays.setAll(inputs, i -> Stream.of(elements));
    Arrays.setAll(intInputs, i -> IntStream.of(intElements));
    Arrays.setAll(longInputs, i -> LongStream.of(longElements));
    Arrays.setAll(doubleInputs, i -> DoubleStream.of(doubleElements));
    Stream<String> stream = StreamConcatenation.concat(inputs);
    IntStream intStream = StreamConcatenation.concat(intInputs);
    LongStream longStream = StreamConcatenation.concat(longInputs);
    DoubleStream doubleStream = StreamConcatenation.concat(doubleInputs);
    Assert.assertEquals(
        "ref",
        numberOfStreams * elements.length,
        stream.count());
    Assert.assertEquals(
        "int",
        numberOfStreams * intElements.length,
        intStream.count());
    Assert.assertEquals(
        "long",
        numberOfStreams * longElements.length,
        longStream.count());
    Assert.assertEquals(
        "double",
        numberOfStreams * doubleElements.length,
        doubleStream.count());
  }
}
