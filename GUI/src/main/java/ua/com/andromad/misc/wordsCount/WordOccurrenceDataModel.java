package ua.com.andromad.misc.wordsCount;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * It is a helper class to show the results in a table of GUI window
 */
public class WordOccurrenceDataModel {
    private final SimpleStringProperty word;
    private final SimpleLongProperty occurrence;

    public WordOccurrenceDataModel(String word, Long occurrence) {
        this.word = new SimpleStringProperty(word);
        this.occurrence = new SimpleLongProperty(occurrence);
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public Long getOccurrence() {
        return occurrence.get();
    }

    public void setOccurrence(int occurrence) {
        this.occurrence.set(occurrence);
    }
}
