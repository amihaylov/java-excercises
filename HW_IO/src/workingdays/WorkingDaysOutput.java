//Using the dd.mm convention, i.e. 17.01
package workingdays;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

class IsWorkingDayQuery {
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public static Boolean checkQuery(TemporalAccessor date) {
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		int day = date.get(ChronoField.DAY_OF_MONTH);
		int weekday = date.get(ChronoField.DAY_OF_WEEK);

		File file = new File("holidays.txt");
		Charset charset = Charset.forName("UTF-8");
		Path fileP;
		fileP = file.toPath();

		List<String> holidaysList = new ArrayList<String>();

		try (BufferedReader reader = Files.newBufferedReader(fileP, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				holidaysList.add(line);
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		// Check for weekends
		if (weekday == 7 || weekday == 6)
			return false;

		// Check for holidays
		String check = null;
		if (month > 9)
			check = Integer.toString(day) + "." + Integer.toString(month);
		else if (day > 9)
			check = Integer.toString(day) + ".0" + Integer.toString(month);
		else
			check = "0" + Integer.toString(day) + ".0"
					+ Integer.toString(month);

		if (holidaysList.contains(check))
			return false;

		return true;
	}
}

// Checks if the date represented as string is working date (including W days)
// or not, using queries
public class WorkingDaysOutput {

	public static boolean checkWorkingDates(String storedDate) {
		LocalDate date = null;

		if (storedDate.charAt(0) == 'W')
			return true;

		else {
			date = LocalDate.of(Integer.parseInt(storedDate.substring(6, 10)),
					Integer.parseInt(storedDate.substring(3, 5)),
					Integer.parseInt(storedDate.substring(0, 2)));
			return (date.query(IsWorkingDayQuery::checkQuery)); // Lambda
																// expression to
																// query whether
																// current day
																// is working
		}
	}

	public static void printWorkingDatesRange(LocalDate startDate,
			LocalDate endDate) {
		// List<LocalDate> dateList = new ArrayList<LocalDate>();
		boolean isWorking = false;
		while (!startDate.isAfter(endDate)) {
			isWorking = startDate.query(IsWorkingDayQuery::checkQuery);
			System.out
					.println(startDate + " is " + isWorking + " working day.");
			startDate = startDate.plusDays(1);
		}

	}
	
	public static List<String> listIfWorkingRange(LocalDate startDate,
			LocalDate endDate) {
		List<String> strList = new ArrayList<String>();
		boolean isWorking = false;
		while (!startDate.isAfter(endDate)) {
			isWorking = startDate.query(IsWorkingDayQuery::checkQuery);
			strList.add(startDate + " is " + isWorking + " working day.");
			startDate = startDate.plusDays(1);
		}
		return strList;
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: filename");
			return;
		}
		File file = new File(args[0].toString());
		Charset charset = Charset.forName("UTF-8");
		Path fileP;
		fileP = file.toPath();

		try (BufferedReader reader = Files.newBufferedReader(fileP, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line + " is working day-"
						+ checkWorkingDates(line));
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		System.out
				.println("Type in start date in following format dd.mm.yyyy: ");
		Scanner sc = new Scanner(System.in);
		String strStartDate = sc.next();

		//Could put some try catch for wrong dates
		LocalDate startDate = LocalDate.parse(strStartDate, IsWorkingDayQuery.formatter);
		LocalDate endDate;
		
		do {
			System.out
					.println("Type in end date in following format dd.mm.yyyy, it also must be after or equal start date: ");
			String strEndDate = sc.next();
			endDate = LocalDate.parse(strEndDate, IsWorkingDayQuery.formatter);

		} while (endDate.isBefore(startDate));

		sc.close();

		printWorkingDatesRange(startDate, endDate);
	}
}
