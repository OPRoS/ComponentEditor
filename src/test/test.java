package test;

import java.util.ArrayList;
import java.util.List;

public class test {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("ccccc");
		list.add("rrr");
		
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<list.size(); i++) {
			buffer.append(list.get(i));
			
			if(i!=list.size() -1) {
				buffer.append(",");
			}
		}
		
		System.out.println(buffer.toString());
	}
}
