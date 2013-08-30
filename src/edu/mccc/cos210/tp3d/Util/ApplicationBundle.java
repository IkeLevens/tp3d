package edu.mccc.cos210.tp3d.Util;
import edu.mccc.cos210.tp3d.Util.*;
import java.util.TreeMap;
public class ApplicationBundle {
	private TreeMap<String, Object> tm = new TreeMap<String, Object>();
	public ApplicationBundle() {
	}
	public ApplicationBundle(String s, Object o) {
		tm.put(s, o);
	}
	public ApplicationBundle getApplicationBundle() {
		return this;
	}
	public void putApplicationData(String s, Object o) {
		tm.put(s, o);
	}
	public Object getApplicationData(String s) {
		Object o = tm.get(s);
		return o;
	}
}
