package com.mzl.java8.chap6;


import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @program: java-base
 * @description:
 * @author: may
 * @create: 2021-05-22 21:35
 **/
public class SpliteratorInAction {

    private static String text = "Jaguar is an endangered animal. \n" +
            "It is said that there are less than 20 jaguars in the world currently, one of which is now living in the national zoo of Peru. In order to protect this jaguar, Peruvians singled out a pitch of land in the zoo for it, where there are herds of cattle, sheep and deer for the jaguar to eat. \n" +
            "Anyone who has visited the zoo praised it to be the \"Heaven of Tiger\". \n" +
            "However, strange enough, no one has ever seen the jaguar prey on the cattle and sheep. \n" +
            "What we could see is its lying in its house eating and sleeping.\n" +
            "Actually, this principle does not only apply to animals, but also apply to human beings. \n" +
            "Here is another story. There was a rich man who was selecting a husband for his only child among a multitude of pursuers. \n" +
            "The man led all the pursuers to a river and pointed to the crocodiles, saying, \"Anyone who can swim across the river safe and sound will marry my daughter.\" \n" +
            "Those pursuers looked at each other and no one dare take the initiative. At that moment, a man plunged into the river bravely and swam at a staggering speed to the other side. \n" +
            "AII the people applauded for his courage with great sense of admiration. Nevertheless, the man, after landing on the bank, shouted angrily, \"Who pushed me into the river just now?\"\n" +
            "Some people thought that the jaguar felt toolonely， so they collected money and rented a female tiger to accompany it. \n" +
            "Nonetheless, it did not make too much sense. \n" +
            "The jaguar just sometimes went out of its house with its \"girlfriend\" and stayed in the sun for a while before it came back to its house again.";

    public static void main(String[] args) {
        IntStream intStream = IntStream.rangeClosed(0, 10);
        Spliterator.OfInt spliterator = intStream.spliterator();
        // Consumer<Integer> consumer = i -> System.out.println(i);
        // spliterator.forEachRemaining(consumer);

        MySpliteratorText mySpliteratorText = new MySpliteratorText(text);
        Optional.ofNullable(mySpliteratorText.stream().count()).ifPresent(System.out::println);
        mySpliteratorText.stream().forEach(System.out::println);

        Optional.ofNullable(mySpliteratorText.parallelStream().count()).ifPresent(System.out::println);
        mySpliteratorText.parallelStream().forEach(System.out::println);
    }

    static class MySpliteratorText {
        private final String[] data;

        public MySpliteratorText(String text) {
            Objects.requireNonNull(text, "The parameter can not be null");
            this.data = text.split("\n");
        }

        public Stream<String> stream() {
            // false 表示不是一个并行的
            return StreamSupport.stream(new MySpliterator(), false);
        }

        // 并行流
        public Stream<String> parallelStream() {
            return StreamSupport.stream(new MySpliterator(), true);
        }

        private class MySpliterator implements Spliterator<String> {

            private int start, end;

            public MySpliterator() {
                this.start = 0;
                this.end = MySpliteratorText.this.data.length - 1;
            }

            public MySpliterator(int start, int end) {
                this.start = start;
                this.end = end;
            }

            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if (start <= end) {
                    action.accept(MySpliteratorText.this.data[start++]);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<String> trySplit() {
                int mid = (end - start) / 2;
                if (mid <= 1) {
                    return null;
                }
                int left = start;
                int right = start + mid;
                start = start + mid + 1;
                return new MySpliterator(left, right);
            }

            @Override
            public long estimateSize() {
                return end - start;
            }

            @Override
            public long getExactSizeIfKnown() {
                return estimateSize();
            }

            @Override
            public int characteristics() {
                return IMMUTABLE | SIZED | SUBSIZED;
            }

        }
    }
}
