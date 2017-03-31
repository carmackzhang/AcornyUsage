package similarity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Similarity {

	private static String[] symbol = { "，", "。", "‘", "“", "”", "’", "）", "（", "￥", "！", "【", "】", "、", "~", "·", "—",
			"；", "：", "？" };
	
	public static void main(String[] args){
		String str1 = "尿失禁的原因";
		String str2 = "绝经期尿失禁";
		
		float score = listSimilarity(StringToList(str1),StringToList(str2));
		System.out.println(score);
	}
	
	public static List<String> StringToList(String str) {
		List<String> listStr = new ArrayList<String>();
		if (str == null)
			return listStr;
		str = str.toLowerCase();
		// for(int i = 0 ; i < str.length(); i++){
		// char tmp = str.charAt(i);
		// if((tmp >= 33 && tmp <= 47) || (tmp >= 58 && tmp <= 64) || (tmp >= 91
		// && tmp <= 96) || (tmp >= 123 && tmp <= 126)){
		// str = str.replace(tmp, ' ');
		// }
		// }
		for (int i = 0; i < symbol.length; i++) {
			if (str.indexOf(symbol[i]) != -1) {
				str = str.replaceAll("\\" + symbol[i], " ");
			}
		}
		while (str.indexOf("  ") != -1) {
			str = str.replaceAll("  ", " ");
		}
		if (str.startsWith(" "))
			str = str.replaceFirst(" ", "");
		if (str.endsWith(" "))
			str = str.substring(0, str.length() - 1);
		for (String item : str.split(" ")) {
			if (isContainChineseChar(item)) {
				for (char char_tmp : item.toCharArray()) {
					listStr.add(String.valueOf(char_tmp));
				}
			} else {
				listStr.add(item);
			}
		}
		return listStr;
	}
	
	public static boolean isContainChineseChar(String str) {
		if (str == null)
			return false;
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}
	
	public static float listSimilarity(List<String> firstList, List<String> secondList) {
		if (firstList == null || secondList == null)
			return 100000;

		List<String> firstTmp = new ArrayList<String>();
		List<String> secondTmp = new ArrayList<String>();
		int m = firstList.size();
		int n = secondList.size();
		if (Math.max(m, n) <= 5) {
			for (String item : firstList) {
				if (item != null) {
					for (char a : item.toCharArray()) {
						firstTmp.add(String.valueOf(a));
					}
				}
			}
			for (String item : secondList) {
				if (item != null) {
					for (char a : item.toCharArray()) {
						secondTmp.add(String.valueOf(a));
					}
				}
			}
			firstList.clear();
			secondList.clear();
			firstList.addAll(firstTmp);
			secondList.addAll(secondTmp);

			if (firstList == null || secondList == null)
				return 100000;

			m = firstList.size();
			n = secondList.size();
		}

		int flag_first = 0;
		int flag_second = 0;
		for (int i = 0; i < firstList.size(); i++) {
			if (firstList.get(i) != null && firstList.get(i).length() != 1) {
				flag_first = 1;
				break;
			}
		}
		for (int i = 0; i < secondList.size(); i++) {
			if (secondList.get(i) != null && secondList.get(i).length() != 1) {
				flag_second = 1;
				break;
			}
		}

		if (flag_first == 1 && flag_second == 0) {
			firstTmp.clear();
			for (String item : firstList) {
				if (item != null) {
					for (char a : item.toCharArray()) {
						firstTmp.add(String.valueOf(a));
					}
				}
			}
			firstList.clear();
			firstList.addAll(firstTmp);
			m = firstList.size();
		} else if (flag_second == 1 && flag_first == 0) {
			secondTmp.clear();
			for (String item : secondList) {
				if (item != null) {
					for (char a : item.toCharArray()) {
						secondTmp.add(String.valueOf(a));
					}
				}
			}
			secondList.clear();
			secondList.addAll(secondTmp);
			n = secondList.size();
		}

		int[][] d = new int[m + 1][n + 1];
		for (int j = 0; j <= n; ++j) {
			d[0][j] = j;
		}
		for (int i = 0; i <= m; ++i) {
			d[i][0] = i;
		}

		for (int i = 1; i <= m; ++i) {
			String ci = firstList.get(i - 1);
			for (int j = 1; j <= n; ++j) {
				String cj = secondList.get(j - 1);
				if (ci.equals(cj)) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					d[i][j] = Math.min(d[i - 1][j - 1] + 1, Math.min(d[i][j - 1] + 1, d[i - 1][j] + 1));
				}
			}
		}
		if (Math.max(m, n) > 0) {
			return 1 - Float.valueOf(d[m][n]) / Math.max(n, m);
		}
		return (float) -1.0;
	}
}
