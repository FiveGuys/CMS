package jdo;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class defines the user objects and all its attributes.
 * @author 5guys
 */
@PersistenceCapable
public class User {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String UserID;
	public String getID() { return UserID; }
	public void setID(String ID) { this.UserID = ID; }

	@Persistent
	private String UserName;
	public String getUserName() {return UserName;}
	public void setUserName(String UserName) {this.UserName = UserName;}
	
	@Persistent
	private String Password;
	public String getPassword() {return Password;}
	public void setPassword(String Password) {this.Password = Password;}
	
	@Persistent
	private String FirstName;
	public String getFirstName() {return FirstName;}
	public void setFirstName(String FirstName) {this.FirstName = FirstName;}
	
	@Persistent
	private String MiddleName;
	public String getMiddleName() {return MiddleName;}
	public void setMiddleName(String MiddleName) {this.MiddleName = MiddleName;}
	
	@Persistent
	private String LastName;
	public String getLastName() {return LastName;}
	public void setLastName(String LastName) {this.LastName = LastName;}
	
	@Persistent
	private String Location;
	public String getLocation() {return Location;}
	public void setLocation(String Location) {this.Location = Location;}
	
	@Persistent
	private String Email;
	public String getEmail() {return Email;}
	public void setEmail(String Email) {this.Email = Email;}
	
	@Persistent
	private String Phone;
	public String getPhone() {return Phone;}
	public void setPhone(String Phone) {this.Phone = Phone;}
	
	@Persistent
	private String AltPhone;
	public String getAltPhone() {return AltPhone;}
	public void setAltPhone(String AltPhone) {this.AltPhone = AltPhone;}
	
	@Persistent
	private String OfficeHour1;
	public String getOfficeHour1() {return OfficeHour1;}
	public void setOfficeHour1(String OfficeHour1) {this.OfficeHour1 = OfficeHour1;}
	
	@Persistent
	private String OfficeHour2;
	public String getOfficeHour2() {return OfficeHour2;}
	public void setOfficeHour2(String OfficeHour2) {this.OfficeHour2 = OfficeHour2;}
	
	@Persistent
	private String OfficeHour3;
	public String getOfficeHour3() {return OfficeHour3;}
	public void setOfficeHour3(String OfficeHour3) {this.OfficeHour3 = OfficeHour3;}
	
	@Persistent
	private String Access;
	public String getAccess() {return Access;}
	public void setAccess(String Access) {this.Access = Access;}

	@Persistent
	private String Image;
	public String getImage() { return Image; }
	public void setImage(String Image) { this.Image = Image; }
		
	@Persistent
	private String Keyword;
	public String getKeyword() { return this.Keyword; }
	public void setKeyword(String keyword ) { this.Keyword = keyword; }
	
	public User() {}
	
	/**
	 * @param i
	 * @return The i office hour.
	 */
	public String getOfficeHour(int i) {
		
		String[] arr = { OfficeHour1, OfficeHour2, OfficeHour3 };
		
		return arr[i - 1];
	}
}
