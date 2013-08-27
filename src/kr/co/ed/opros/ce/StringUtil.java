package kr.co.ed.opros.ce;


public class StringUtil {
	
	public static String[] stringSeparation(String type) {
		int dataTypeIndex = type.indexOf("<");
		if(dataTypeIndex > -1) {
			String portType = type.substring(0, type.indexOf("<"));
			String dataType = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
			return new String[]{portType, dataType};
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		StringUtil util = new StringUtil();
		String[] str = util.stringSeparation("OutputEventPort<std::vector<int>>");
		
		System.out.println(str[0] + ", " + str[1]);
	}
}
