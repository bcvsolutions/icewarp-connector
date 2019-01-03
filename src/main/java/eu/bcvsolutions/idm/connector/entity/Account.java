package eu.bcvsolutions.idm.connector.entity;

/**
 * Created by admin on 13/12/18.
 */
public class Account {
	private String objectId;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private String displayemail;
	private String accounttype;
	private String accountstate;
	private String admintype = "0";
	private String groupName;
	private String groupAlias;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupAlias() {
		return groupAlias;
	}

	public void setGroupAlias(String groupAlias) {
		this.groupAlias = groupAlias;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
		// if display email is same as email
		setDisplayemail(email);
	}

	public String getDisplayemail() {
		return displayemail;
	}

	public void setDisplayemail(String displayemail) {
		this.displayemail = displayemail;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getAccountstate() {
		return accountstate;
	}

	public void setAccountstate(String accountstate) {
		this.accountstate = accountstate;
	}

	public String getAdmintype() {
		return admintype;
	}

	public void setAdmintype(String admintype) {
		this.admintype = admintype;
	}

	public String getEmail() {
		return email;
	}
}
