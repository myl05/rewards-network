import base.KarateBaseTest;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Acts as the ‘test suite’ for the entire project.
 * Parallel threads can be configured
 * @author mlee
 **/

public class ParallelSuiteTest extends KarateBaseTest {

	@Test
	public void testParallel() {
		Results results = Runner.builder()
								.outputCucumberJson(true)
								.path("classpath:")
								.tags("~@ignore")
								.parallel(1);
		generateEmailableReport("Rewards_Network", results);
		generateReport(results.getReportDir());
		assertEquals(0, results.getFailCount(), results.getErrorMessages());
	}
}
