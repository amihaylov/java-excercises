package workingdays;
 /**
  * CheckRegex is the class to check whether the string
  * matches the allowed characters - numbers, dot and 
  * capital W for working day.
  * @author Tonko
  *
  */
public class CheckRegex {

	public static void main(String[] args) {
		String str = "W12";
		if (str.matches("W[0-9]"))
			System.out.println("string starts with number.");
		else System.out.println("Error");
	}

}
