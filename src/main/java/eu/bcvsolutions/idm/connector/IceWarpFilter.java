package eu.bcvsolutions.idm.connector;

/**
 * @author Petr Hanák
 * This implementation depends on the logic that this connector follows.
 *
 */
public class IceWarpFilter {

	private String attr;
	private Object value;

	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
