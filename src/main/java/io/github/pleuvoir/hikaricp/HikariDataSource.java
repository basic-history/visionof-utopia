package io.github.pleuvoir.hikaricp;

public class HikariDataSource extends HikariConfig {

	// 传统做法
	private HikariConfig hikariConfig;
	
	
	public HikariDataSource(HikariConfig configuration) {
		configuration.copyStateTo(this); // 这行会进行属性拷贝 ，这是作者的做法，优势是当属性值很多时可以不用再去每次都用 别的类点
	//	this.hikariConfig = configuration; // 传统做法
	}


	public void dosomething1() {
		System.out.println("驱动名称有了：" + hikariConfig.getDriverClassName());
	}

	public void dosomething2() {
		System.out.println("驱动名称有了：" + getDriverClassName());
	}

	
}
