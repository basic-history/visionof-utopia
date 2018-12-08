package io.github.pleuvoir.hikaricp.bestpractice;

import io.github.pleuvoir.tookit.CopyStateUtil;
import io.github.pleuvoir.tookit.PropLoaderUtil;

public class AppConfig {

	private static final AppConfig INSTANCE = new AppConfig();

	private String name;
	private String name2;
	private String name3;
	private String name4;
	private String name5;
	private String name6;
	private String name7;
	private String name8;
	private String version;
	private boolean complete;

	
	public static AppConfig load(String propPath) {
		return PropLoaderUtil.setTargetFromProperties(INSTANCE, propPath, "app.");
	}

	public void copyStateTo(Object target) {
		CopyStateUtil.copyStateTo(this, target);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public String getName5() {
		return name5;
	}

	public void setName5(String name5) {
		this.name5 = name5;
	}

	public String getName6() {
		return name6;
	}

	public void setName6(String name6) {
		this.name6 = name6;
	}

	public String getName7() {
		return name7;
	}

	public void setName7(String name7) {
		this.name7 = name7;
	}

	public String getName8() {
		return name8;
	}

	public void setName8(String name8) {
		this.name8 = name8;
	}
}
