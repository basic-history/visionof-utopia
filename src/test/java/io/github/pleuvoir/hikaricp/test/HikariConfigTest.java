package io.github.pleuvoir.hikaricp.test;

import java.util.Properties;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import io.github.pleuvoir.hikaricp.HikariConfig;
import io.github.pleuvoir.tookit.configuration.object.CopyStateUtil;
import io.github.pleuvoir.tookit.configuration.object.PropLoaderUtil;

/**
 * 测试 #{HikariConfig} 中让我眼前一亮的方法
 * @author pleuvoir
 *
 */
@SuppressWarnings("unused")
public class HikariConfigTest {

	/**
	 * 这个方法是根据属性文件名称加载属性文件
	 */
	@Test
	public void loadPropertiesTest(){
		String propertyFileName = "/HikariConfigTest.properties";
		HikariConfig hikariConfig = new HikariConfig(propertyFileName);
		System.out.println("==========豪华午餐==========");
		// 获得的豪华午餐
		Properties props = PropLoaderUtil.loadProperties(propertyFileName);
		System.out.println(JSON.toJSON(props));
	}
	
	/**
	 * 复制状态，该方法通过反射将本类 HikariConfig自身的属性值赋值给一个新创建的 HikariConfig
	 */
	@Test
	public void copyStateToTest(){
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setAutoCommit(true);
		hikariConfig.setDriverClassName("oracle.jdbc.pool.OracleDataSource");
		hikariConfig.setInitializationFailTimeout(5L);
		// 开始复制
		HikariConfig other = new HikariConfig();
		System.out.println("未复制前： " + JSON.toJSONString(other));
		hikariConfig.copyStateTo(other);
		System.out.println("复制后： " + JSON.toJSONString(other));
		// 操作复制源属性发现新对象并没有受到影响
		hikariConfig.setDriverClassName("驱动被修改");
		System.out.println("复制源被修改后： " + JSON.toJSONString(other));
		
		System.out.println("==========豪华午餐==========");
		// 获得的豪华午餐
		User source = new User();
		Address address = new Address();
		address.setAddressId("地址编号-101");
		source.setAddress(address);
		source.setAge(18);
		source.setName("pleuvoir");
		User target = new User();
		CopyStateUtil.copyStateTo(source, target);
		// 发现内部的复杂对象也被拷贝了
		System.out.println("target user： " + JSON.toJSONString(target));
		// 再次修改地址，发现居然复制对象的属性被改了？？
		address.setAddressId("呵呵地址被修改了");
		System.out.println("user 复制源被修改后： " + JSON.toJSONString(target));
		System.out.println("使用 Cloneable 接口..");
		
		User clone = source.clone();
		System.out.println("使用 Cloneable 接口 ：" + JSON.toJSONString(clone));
		System.out.println("修改原对象的属性 Address");
		address.setAddressId("克隆接口会变吗");
		// 结果是依然发生了改变
		System.out.println("使用 Cloneable 接口修改原对象的属性后 ：" + JSON.toJSONString(clone));
		
		// 尝试使用多层 clone 深拷贝，不过显然这种方式过于麻烦，可以选择对象序列化的形式来实现
		System.out.println("深拷贝.......");
		DeepUser deepUser = new DeepUser();
		DeepAddress deepAddress = new DeepAddress();
		deepAddress.setAddressId("deepAddress");
		deepUser.setAddress(deepAddress);
		DeepUser deepClone = deepUser.clone();
		System.out.println("深拷贝修改前： " + JSON.toJSONString(deepClone));
		System.out.println("深拷贝修改原对象属性 deepAddress ..");
		deepAddress.setAddressId("deepAddress修改后");
		System.out.println("深拷贝修改后： " + JSON.toJSONString(deepClone));
	}
	
	// 用于演示拷贝时内部复杂的对象的变化
	static class User implements Cloneable {
		private String name;
		private int age;
		private Address address;

		@Override
		protected User clone() {
			User user = null;
			try {
				user = (User) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return user;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}
	}

	static class Address {
		private String addressId;

		public String getAddressId() {
			return addressId;
		}

		public void setAddressId(String addressId) {
			this.addressId = addressId;
		}
	}
	
	
	// 用于演示多 clone 实现深拷贝
	static class DeepUser implements Cloneable {
		private String name;
		private int age;
		private DeepAddress address;

		@Override
		protected DeepUser clone() {
			DeepUser user = null;
			try {
				user = (DeepUser) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			// 重新克隆一个，这行代码是关键，如果有多个内在嵌套属性，则每层都需要这样做，使用很麻烦不实用
			user.setAddress(user.getAddress().clone());
			return user;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public DeepAddress getAddress() {
			return address;
		}

		public void setAddress(DeepAddress address) {
			this.address = address;
		}
	}

	static class DeepAddress implements Cloneable {
		private String addressId;

		public String getAddressId() {
			return addressId;
		}

		public void setAddressId(String addressId) {
			this.addressId = addressId;
		}
		@Override
		protected DeepAddress clone() {
			DeepAddress deepAddress = null;
			try {
				deepAddress = (DeepAddress) super.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			return deepAddress;
		}
	}
}
