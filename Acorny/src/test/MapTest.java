package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MapTest {

	private static HashMap<String, String> words = new HashMap<String,String>();
	
	public static void main(String[] args) {
//		HashMap<String,User> map = new HashMap<String,User>();
//		mapCopy(map);
//		System.out.println(map.size());
//		mapCopy2(map);
//		System.out.println(map.size());
		mapCost();
	}
	
	public static void mapCost(){
		HashMap<String, String> tmp_map = new HashMap<String, String>();
		int million = 100000000;
		long begin = System.currentTimeMillis();
		addItem2Map(tmp_map,million);
		long end1 = System.currentTimeMillis();
		addItem2Map(words,million);
		long end2 = System.currentTimeMillis();
		System.out.println("time1:"+(end1-begin)+" time2:"+(end2-end1));
	}
	
	public static void addItem2Map(Map map, int num){
		int count = 0;
		for(int i=0; i<num; i++){
			map.put("key"+count, "val"+count);
		}
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

