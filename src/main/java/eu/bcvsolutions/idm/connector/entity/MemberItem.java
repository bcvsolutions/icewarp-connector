package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Petr Hanák
 *
<item>
	<classname>tpropertymember</classname>
	<val>email@ofmembertoadd</val>
	<default>enumval</default>
	<recieve>enumval</recieve>
	<post>enumval</post>
	<digest>enumval</digest>
	<params>stringval</params>
</item>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddAccountMembers")
public class MemberItem {
	@XmlElement
	private final String classname = "tpropertymember";
	@XmlElement(name = "val")
	private String userUid;
	@XmlElement(name = "default")
	private String def;
	@XmlElement
	private String receive;
	@XmlElement
	private String post;
	@XmlElement
	private String digest;
	@XmlElement
	private String params;

	public String getClassname() {
		return classname;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
