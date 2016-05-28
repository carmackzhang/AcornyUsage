package test;

import java.util.HashMap;

public class MapTest {

	public static void main(String[] args) {
		HashMap<String,User> map = new HashMap<String,User>();
		mapCopy(map);
		System.out.println(map.size());
		mapCopy2(map);
		System.out.println(map.size());
	}
	
	
	public static void mapCopy(HashMap<String,User> map){
		HashMap<String,User> oriMap = new HashMap<String,User>();
		User user = new User();
		user.setName("corny");
		user.setAge(11);
		oriMap.put("1", user);
		map = oriMap;
	}
	
	public static void mapCopy2(HashMap<String,User> map){
		User user = new User();
		user.setName("corny");
		user.setAge(11);
		map.put("1", user);
	}

}

class User{
	private String name;
	private int age;
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
	public String toString(){
		return name+"\t"+age;
	}
}

