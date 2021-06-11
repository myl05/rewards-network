package services.searchRepository.tests;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class is intended to run all tests in the searchRepository package that are identified with the tag: @searchRepo
 * This is useful when testing if you don't want to run all scenarios, and narrow down the scope
 *
 * Not using the *Test.java convention for the JUnit classes (e.g. searchRepositoryRunner.java)
 * ensures that these tests will not be picked up when invoking mvn test (for the whole project) from the command line.
 * But you can still invoke these tests from the IDE, which is convenient when in development mode.
 *
 * @author mlee
**/

public class searchRepositoryRunner {
    @Test
    public void productTests() {
        Results results = Runner.path("classpath:product")
                                .tags("@searchRepo", "~@ignore")
                                .parallel(1);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}