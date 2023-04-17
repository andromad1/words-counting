package ua.com.andromad.misc.wordsCount.analyze;

import java.text.Collator;
import java.util.Objects;

/**
 * Class for storing a word and the number of its occurrences in the text of a file
 */
public class WordOccurrence implements Comparable<WordOccurrence> {
    private final String word;
    private final Long occurrence;
    private final Collator coll = Collator.getInstance();

    public WordOccurrence(String word, Long occurrence) {
        this.word = word;
        this.occurrence = occurrence;
    }

    public String getWord() {
        return word;
    }

    public Long getOccurrence() {
        return occurrence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        WordOccurrence that = (WordOccurrence) o;

        return word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "\nWordOccurrence{" +
                "word='" + word + '\'' +
                ", occurrence=" + occurrence +
                "}";
    }

    @Override
    public int compareTo(WordOccurrence o) {
        int res = Long.compare(o.occurrence, this.occurrence);

        if (res == 0) {
            res = coll.compare(this.word, o.word);
        }

        return res;
    }
}
