package eu.bcvsolutions.idm.connector.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Petr Hanák
 *
<result>
	<item>
		<val>jmeno@email.do</val>
		<default>0</default>
		<recieve>0</recieve>
		<post>0</post>
		<digest>0</digest>
		<params/>
	</item>
</result>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "result")
public class GetAccountMemberInfoListResponse {
	@XmlElement(name = "item")
	private List<MemberResponse> items;
	@XmlElement
	private String offset;
	@XmlElement
	private String overallcount;

	public GetAccountMemberInfoListResponse() {
		if (items == null) {
			items = new ArrayList<>();
		}
	}

	public List<MemberResponse> getItems() {
		return items;
	}

	public void setItems(List<MemberResponse> items) {
		this.items = items;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getOverallcount() {
		return overallcount;
	}

	public void setOverallcount(String overallcount) {
		this.overallcount = overallcount;
	}
}
