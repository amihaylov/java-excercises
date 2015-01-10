package workingdays;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkingDaysInput {

	final static String DATE_FORMAT = "dd.MM.yyyy";

	public static boolean isDateValid(String date) {
		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: filename");
			return;
		}

		Scanner sc = new Scanner(System.in);
		File file = new File(args[0].toString());
		String s = "0";
		List<String> strList = new ArrayList<String>();

		while (s != null && !s.equals("")) {
			s = sc.nextLine();
			if (s.matches("[W]?[0-9.]*"))
					strList.add(s);
			else
				System.out.println("Includes invalid characters or is not a valid date.");
		}
		Path fileP;
		fileP = file.toPath();
		Charset charSet = Charset.forName("utf-8");

		try (BufferedWriter writer = Files.newBufferedWriter(fileP, charSet)) {

			for (String line : strList) {
				writer.write(line, 0, line.length());
				writer.newLine();
			}
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		sc.close();

	}
}
