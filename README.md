This is a simple JavaFx project that is the solution to a test task.

<b>Task description</b>

*** Scenario ***

-	A customer needs a small utility for processing a text file.
-	The user interface shall allow the user to specify the text file to be processed.
-	This file shall be analyzed and all the words within the file shall be extracted and counted.
-	As a result, a simple two-column table shall be shown.
-	The first column shall contain words, while the second one shall contain their occurrences.
-	The table shall be sorted by the number of occurrences.
-	The user shall be able to cancel the processing of the file.

*** Technical requirements ***

-	The program shall read an ANSI text file. The file to be read shall be specified by the user.
-	Separating the words out of the text file is only based on white space characters 
(space, LR, CR, ...). Handling punctuation marks is beyond the scope of this assignment.
-	It shall be considered that large text files (~50 MB) will be processed, and as such, 
the user interface shall show a progress bar.
-	It shall be possible to abort the program while parsing the text file.
-	The file parsing logic, responsible for extracting the words out of the input file should be 
modular and reusable. This is because this logic is intended to be reused in a future project.
-	It is desired that the parsing of the file offers decent performance.
-	The user interface is required to be responsive even during parsing the file so that the user
 can still interact with the application (e.g. when he/she wants to cancel the file processing).

As an example, considering following input:

1:1 Adam Seth Enos<br>
1:2 Cainan Adam Seth Iared

The program should display the table content below<br>
<table>
<thead>
<tr><th>Word</th><th>Occurrence</th></tr>
</thead>
<tbody>
<tr><td>Adam</td><td>2</td></tr>
<tr><td>Seth</td><td>2</td></tr>
<tr><td>1:1</td><td>1</td></tr>
<tr><td>Enos</td><td>1</td></tr>
<tr><td>1:2</td><td>1</td></tr>
<tr><td>Cainan</td><td>1</td></tr>
<tr><td>Iared</td><td>1</td></tr>
</tbody>
</table>

<b>Solution</b><br><br>
The project consists of two modules - `analyzing` and `gui`. The `analyzing` module produces the `words-count-analyzing-1.0-SNAPSHOT.jar` file and the `gui` module produces the `words-count-gui-1.0-SNAPSHOT.jar` file. `words-count-analyzing-1.0-SNAPSHOT.jar` is responsible for reading and parsing text from a file, counting unique words and sorting them by number of occurrences, while `words-count-gui-1.0-SNAPSHOT.jar` is responsible for displaying a graphical user interface that allows the user to select a file, start the analysis process, interrupt the analysis (if necessary) and view the analysis results.

<b>Building and running</b><br><br>
Prerequisites: Maven, JDK/JRE, JavaFX.<br><br>
Build:<br>
<code>mvn package</code><br><br>
Running in Windows:<br>
<code>java --module-path "<PATH_TO_JAVAFX_LIB_DIR>" --add-modules javafx.controls,javafx.fxml -cp ".\analyzing\target\words-count-analyzing-1.0-SNAPSHOT.jar;.\gui\target\words-count-gui-1.0-SNAPSHOT.jar" ua.com.andromad.misc.wordsCount.GUIwordsCount</code>
<br><br>
Running in Linux:<br>
<code>java --module-path <PATH_TO_JAVAFX_LIB_DIR> --add-modules javafx.controls,javafx.fxml -cp ./analyzing/target/words-count-analyzing-1.0-SNAPSHOT.jar:./gui/target/words-count-gui-1.0-SNAPSHOT.jar ua.com.andromad.misc.wordsCount.GUIwordsCount</code>

<b>Quick run</b><br><br>
You can just download the appropriate pre-built jar-files from the Packages section of the project's main page on GitHub and run the program in the same way as described in the Build and Run section.
