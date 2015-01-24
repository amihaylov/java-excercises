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

/**
 * Class, implementing queries to check whether a date is holiday or in weekend.
 * 
 * @author Tonko
 *
 */
class IsWorkingDayQuery {
	static DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("dd.MM.yyyy");
	static DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("dd.MM");

	/**
	 * Query method to check dates.
	 * 
	 * @param date
	 *            Date to be checked whether it is working or not.
	 * @return Returns false if not working, true if working.
	 */
	public static Boolean checkQuery(TemporalAccessor date) {
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		int day = date.get(ChronoField.DAY_OF_MONTH);

		File file = new File("holidays.txt");
		Charset charset = Charset.forName("UTF-8");
		Path fileP;
		fileP = file.toPath();

		List<MonthDay> holidaysList = new ArrayList<MonthDay>();

		try (BufferedReader reader = Files.newBufferedReader(fileP, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				holidaysList.add(MonthDay.parse(line, dateOnly));
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		// Check if date is in weekend
		if (date.get(ChronoField.DAY_OF_WEEK) == 7
				|| date.get(ChronoField.DAY_OF_WEEK) == 6)
			return false;

		MonthDay tempComp = MonthDay.of(month, day);
		if (holidaysList.contains(tempComp))
			return false;

		return true;
	}
}

/**
 * Class to determine whether a list of dates, contained in file (args[0]), is
 * working or not. The file 'holidays.txt', located in root directory, contains
 * list of holidays. If the date is on holidays or weekends it is not working
 * day, unless it is recorded in 'Wdd.MM.yyyy' format. Then it is working.
 * 
 * @author Tonko
 *
 */
public class WorkingDaysOutput {

	/**
	 * Method to check date as string
	 * 
	 * @param storedDate
	 *            Date to be checked in a string format (Wdd.MM.yyyy)
	 * @return If working (starts with W or query returns true) returns true
	 */
	public static boolean checkWorkingDates(String storedDate) {
		LocalDate date = null;

		if (storedDate.charAt(0) == 'W')
			return true;

		else {
			date = LocalDate.parse(storedDate, IsWorkingDayQuery.formatter);
			return (date.query(IsWorkingDayQuery::checkQuery)); // Lambda
																// expression to
																// query whether
																// current day
																// is working
		}
	}

	/**
	 * Method to check range of dates and return list of all dates with
	 * description. It uses 'working.txt' file to check whether a date is
	 * specifically stated as working even if it is on weekends or holidays.
	 * Returns a list of String.
	 * 
	 * @param startDate
	 *            Start date of range.
	 * @param endDate
	 *            End date of range.
	 * @return List of all dates as string and whether they are working or not.
	 */

	// TODO Reconstruct method so it takes filepath as argument and use it in
	// main and in GUI
	// instead of printWorkingDatesRange. Make GUI so it selects files.
	public static List<String> listIfWorkingRange(LocalDate startDate,
			LocalDate endDate, String filePathStr) {
		List<String> strList = new ArrayList<String>();
		List<MonthDay> workDaysFromFileList = new ArrayList<MonthDay>();
		boolean isWorking = false;

		File file = new File(filePathStr);
		Charset charset = Charset.forName("UTF-8");
		Path fileP;
		fileP = file.toPath();

		try (BufferedReader reader = Files.newBufferedReader(fileP, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				workDaysFromFileList.add(MonthDay.parse(line,
						IsWorkingDayQuery.dateOnly));
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		int countWorkingDays = 0, countFreeDays = 0;
		while (!startDate.isAfter(endDate)) {
			isWorking = startDate.query(IsWorkingDayQuery::checkQuery);
			MonthDay tempComp = MonthDay.from(startDate);
			if (isWorking)
				countWorkingDays++;
			else
				countFreeDays++;
			if (workDaysFromFileList.contains(tempComp)) {
				strList.add(startDate + " is true working day.");
				countWorkingDays++;
				countFreeDays--;
			} else
				strList.add(startDate + " is " + isWorking + " working day.");
			startDate = startDate.plusDays(1);
		}
		strList.add("The number of working days is " + countWorkingDays);
		strList.add("The number of free days is " + countFreeDays);
		return strList;
	}

	/**
	 * Method to print all dates in a range and whether they are working or not.
	 * 
	 * @param startDate
	 *            Start date of range
	 * @param endDate
	 *            End date of range
	 */
	public static void printWorkingDatesRange(LocalDate startDate,
			LocalDate endDate) {
		boolean isWorking = false;
		while (!startDate.isAfter(endDate)) {
			isWorking = startDate.query(IsWorkingDayQuery::checkQuery);
			System.out
					.println(startDate + " is " + isWorking + " working day.");
			startDate = startDate.plusDays(1);
		}

	}

	/**
	 * Testing methods
	 * 
	 * @param args
	 *            File to read some dates
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage: filename");
			return;
		}
		File file = new File(args[0]);
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

		LocalDate startDate = LocalDate.parse(strStartDate,
				IsWorkingDayQuery.formatter);
		LocalDate endDate;

		do {
			System.out
					.println("Type in end date in following format dd.mm.yyyy, it also must be after or equal start date: ");
			String strEndDate = sc.next();
			endDate = LocalDate.parse(strEndDate, IsWorkingDayQuery.formatter);
			if (endDate.isBefore(startDate))
				System.out.println("End date must be after start date.");

		} while (endDate.isBefore(startDate));

		sc.close();

		printWorkingDatesRange(startDate, endDate);
	}
}
