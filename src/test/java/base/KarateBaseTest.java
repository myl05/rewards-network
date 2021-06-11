package base;

import com.intuit.karate.Results;
import com.intuit.karate.core.Step;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is intended for Karate Rest Services testing.
 * Karate Parallel test class must extend this class.
 * <ul>
 * <li>Creates Karate report</li>
 * <li>Creates Detailed Cucumber report</li>
 * <li>Creates Custom Emailable Report</li>
 * </ul>
 *
 * @author mlee
 */

public class KarateBaseTest {
	public void generateReport(String karateOutputPath) {
		Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] { "json" }, true);
		List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
		jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
		Configuration config = new Configuration(new File("target"), "Rewards_Network");
		ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
		reportBuilder.generateReports();
	}

	public void generateEmailableReport(String title, Results results) {
		String fileSeparator = File.separator;
		String suiteSummary = getSuiteSummary(results);
		String methodSummary = getTestMethodSummary(results);
		String emailBody = getSuccessEmailTemplate();
		if (methodSummary.length() > 4) {
			emailBody = getFailureEmailTemplate();
			emailBody = emailBody.replace("$Test_Case_Detail$", methodSummary);
		}
		emailBody = emailBody.replace("$Custom_Report_Title$", title);
		emailBody = emailBody.replace("$Test_Case_Summary$", suiteSummary);
		String reportPath = results.getReportDir() + fileSeparator + "custom-emailable-report.html";
		File targetFile = new File(reportPath);
		try {
			FileWriter fw = new FileWriter(targetFile);
			fw.write(emailBody);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(reportPath);
	}

	private String getTestMethodSummary(Results results) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		results.getScenarioResults().forEach(scenarioResult -> {
			String testName = scenarioResult.getScenario().getName();
			String failureMessage = scenarioResult.getFailureMessageForDisplay();
			long startTime = scenarioResult.getStartTime();
			long endTime = scenarioResult.getEndTime();
			if (failureMessage != null) {
				String resultStatus = scenarioResult.getFailedStep().getResult().toString();
				Step failedStep = scenarioResult.getFailedStep().getStep();
				String failurePath = failedStep.getFeature().getNameForReport();
				int failureLineNum = failedStep.getLine();
				String testAssertion = failedStep.getText();
				String failureResponse = scenarioResult.getFailedStep().getResult().getError().getMessage();
				sb.append("<td>").append(testName).append("</td>");
				sb.append("<td>").append(resultStatus).append("</td>");
				sb.append("<td>").append(failurePath + " : " + failureLineNum).append("</td>");
				sb.append("<td>").append(testAssertion).append("</td>");
				sb.append("<td>").append(failureResponse).append("</td>");
				sb.append("<td>").append(formatDate(startTime)).append("</td>");
				sb.append("<td>").append(formatDate(endTime)).append("</td>");
				sb.append("</tr>");
			}
		});
		return sb.toString();
	}

	private String getSuiteSummary(Results results) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		int featureCount = results.getFeaturesTotal();
		int scenarioCount = results.getScenariosTotal();
		int passCount = results.getScenariosPassed();
		int failCount = results.getFailCount();
		int threadCount = results.getSuite().threadCount;
		long startTime = results.getStartTime();
		long endTime = results.getEndTime();
		sb.append("<td>").append(featureCount).append("</td>");
		sb.append("<td>").append(scenarioCount).append("</td>");
		sb.append("<td bgcolor=seagreen>").append(passCount).append("</td>");
		sb.append("<td bgcolor=Tomato>").append(failCount).append("</td>");
		sb.append("<td>").append(threadCount).append("</td>");
		sb.append("<td>").append(formatDate(startTime)).append("</td>");
		sb.append("<td>").append(formatDate(endTime)).append("</td>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String getFailureEmailTemplate() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\""
				+ "><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"content-type\" content=\"appl"
				+ "ication/xhtml+xml; charset=gbk\" /><title>Custom Emailable Report</title>"
				+ "<style type=\"text/css\">table {margin-bottom:10px;"
				+ "border-collapse:collapse;empty-cells:show}th,td {border:1px solid #009;padding:.25em "
				+ ".5em}th {vertical-align:bottom}td {vertical-align:top}table a {font-weight:bold}.stripe td "
				+ "{background-color: #E6EBF9}.num {text-align:right}.passedodd td {background-color: #3F3}.passedeven"
				+ " td {background-color: #0A0}.skippedodd td {background-color: #DDD}.skippedeven td {background-color"
				+ ": #CCC}.failedodd td,.attn {background-color: #F33}.failedeven td,.stripe .attn {background-color:"
				+ " #D00}.stacktrace {white-space:pre;font-family:monospace}.totop {font-size:85%;text-align:center;"
				+ "border-bottom:2px solid #000}</style></head><body><table><tr><center><b>$Custom_Report_Title$"
				+ "</b></center></tr><thead><tr><th># Total Features</th><th># Total Scenarios</th><th># Passed</th>"
				+ "<th># Failed</th><th># Threads</th><th>Start Time</th><th>End Time</th>"
				+ "</tr></thead>$Test_Case_Summary$"
				+ "</table><table id=\"summary\"><thead><tr><th>Test Description</th><th>Result</th>"
				+ "<th>Failure Path : Line Num</th><th>Test Assertion</th><th>Failure Response</th>"
				+ "<th>Start Time</th><th>End Time</th>"
				+ "</tr></thead>$Test_Case_Detail$</table></body></html>\n";
	}

	private String getSuccessEmailTemplate() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\""
				+ "><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"content-type\" content=\"appl"
				+ "ication/xhtml+xml; charset=gbk\" /><title>Custom Emailable Report</title>"
				+ "<style type=\"text/css\">table {margin-bottom:10px;"
				+ "border-collapse:collapse;empty-cells:show}th,td {border:1px solid #009;padding:.25em "
				+ ".5em}th {vertical-align:bottom}td {vertical-align:top}table a {font-weight:bold}.stripe td "
				+ "{background-color: #E6EBF9}.num {text-align:right}.passedodd td {background-color: #3F3}.passedeven"
				+ " td {background-color: #0A0}.skippedodd td {background-color: #DDD}.skippedeven td {background-color"
				+ ": #CCC}.failedodd td,.attn {background-color: #F33}.failedeven td,.stripe .attn {background-color:"
				+ " #D00}.stacktrace {white-space:pre;font-family:monospace}.totop {font-size:85%;text-align:center;"
				+ "border-bottom:2px solid #000}</style></head><body><table><tr><center><b>$Custom_Report_Title$"
				+ "</b></center></tr><thead><tr><th># Total Features</th><th># Total Scenarios</th><th># Passed</th>"
				+ "<th># Failed</th><th># Threads</th><th>Start Time</th><th>End Time</th>"
				+ "</tr></thead>$Test_Case_Summary$</table><table id=\"summary\"></body></html>\n";
	}

	private String formatDate(long millisecondsSinceEpoch) {
		Instant instant = Instant.ofEpochMilli(millisecondsSinceEpoch);
		return instant.atZone(ZoneId.of("America/Chicago"))
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " CT";
	}
}
