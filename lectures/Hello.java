import java.util.*;
class Hello
{
	public static void main(String[] arhs)
	{
		Scanner input = new Scanner(System.in);
	        System.out.println("Enter  fullName!");
		String fullName = input.nextLine();
		Name myName = new Name(fullName);
		System.out.println("Hello!" + myName.getFirstName());

	}
}
