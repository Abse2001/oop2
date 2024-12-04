import java.util.*;
public class Name
{
	String firstName;
	String middleName;
	String lastName;
 
	public Name(String fName,String mName, String lName)
 	{
		 firstName = fName;
	 	middleName = mName;
	 	lastName = lName;
 	}
	public Name(String fullName)
	{
           String[] name = fullName.split(" ");
		   firstName = name[0];
	           middleName = name[1];
		   lastName = name[2];
	}

       	public String getFirstName()
 	{
   		return firstName;
	 }
	
	public String getMiddleName()
 	{
   		return middleName;
	 }

	public String getLastName()
 	{
  		 return lastName;
 	}
}

