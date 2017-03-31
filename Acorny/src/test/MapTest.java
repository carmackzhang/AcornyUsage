package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

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

	public static void treeMapTest(){
		TreeMap<Integer,String> tmap = new TreeMap<Integer,String>();
		tmap.put(0,"abcd");
		tmap.put(1, "aaa");
		tmap.put(2, "bbb");
		tmap.put(4, "ddd");
		tmap.put(3, "ccc");
		Iterator<Entry<Integer,String>> iter = tmap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer,String> entry = (Entry<Integer,String>)iter.next();
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
		
		for(int i=0;i<10;i++){
			System.out.println("start:"+i);
			if(i==3){
				i--;
				continue;
			}
			System.out.println("end:"+i);
		}
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

