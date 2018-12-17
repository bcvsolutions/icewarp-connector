package eu.bcvsolutions.idm.connector.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 *
<item>
	<val>jmeno@email.do</val>
	<default>0</default>
	<recieve>0</recieve>
	<post>0</post>
	<digest>0</digest>
	<params/>
</item>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item")
public class MemberResponse {
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
