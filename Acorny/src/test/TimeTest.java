package test;
import java.util.Calendar;

public class TimeTest {
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		System.out.println(time);
		Calendar cal = Calendar.getInstance();
//		long millis = 1467778598000L;
//		cal.setTimeInMillis(millis);
		String tmp = "1489011416";
		long time1 = Long.parseLong(tmp);
		cal.setTimeInMillis(time1*1000);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minit = cal.get(Calendar.MINUTE);
		System.out.println(year+"-"+month+"-"+day+"-"+hour+":"+minit);
		
		long qianwan = 100000000L;
		
		long date = year*qianwan+month*1000000+day*10000+hour*100+minit;
		
		System.out.println("date:"+date);
		
		int date1 = Integer.parseInt("07050906");
		int date2 = Integer.parseInt("07060906");
//		System.out.println(date1<date2);
//		System.out.println(date<date2);
		 
		int dateStart = -1;
		String t1 = "<timeorder sort=\"1\"><![CDATA[07201735]]></timeorder>";
		String t2 = "<timeorder sort=\"1\">07201735</timeorder>";
		dateStart = t2.indexOf("<timeorder");
		dateStart = t2.indexOf(">", dateStart+1);
		int timeLength = t2.indexOf("</timeorder>", dateStart)-dateStart;
		System.out.println("len:"+timeLength);
		String tmpDate = "";
		if(timeLength==21){
			tmpDate = t2.substring(dateStart+10,dateStart+18);//07061623
		}else if(timeLength==9){
			tmpDate = t2.substring(dateStart+1,dateStart+9);
		}
//		tmpDate = "2016"+tmpDate;
		System.out.println("date:"+tmpDate);
		System.out.println(isAllDigit("11a"));
		Long d1 = Long.parseLong(tmpDate);
		System.out.println(d1);
		
		boolean t = hasTimeliness("包子油价怎么样");
		System.out.println(t);
	}
	
	public static boolean isAllDigit(String query)
    {
    	for(int i = 0; i < query.length(); i++)
    		if (query.charAt(i) > 57 || query.charAt(i) < 48){//ascii码比较
    			System.out.println("char:"+query.charAt(i));
    			return false;
    		}
    	return true;
    }
	
	public static boolean hasTimeliness(String query){
		String[] midEnd = {"价格","多少钱","排名","费用","有多少"};
		String[] end = {"标准","规模","数量","榜","政策","多大","流程","规定","电话","报名时间"};
		String[] all = {"最新","招聘","报价","现状","近况","行情","分数线","时刻表","名单","待遇","录取结果","走势","绯闻","款式","恋情","工资","财报","发布会"
						,"油价","金价","涨价","汇率","年终奖","市值","市场份额","男女比例","模拟考试","年薪","裁员","利息","利率","手续费","现在","前景"};
		boolean t1 = false;
		boolean t2 = false;
		boolean t3 = false;
		
		for(int i=0;i<midEnd.length;i++){
			if(query.contains(midEnd[i]) && !query.startsWith(midEnd[i])){
				t1 = true;
			}
		}
		for(int i=0;i<end.length;i++){
			if(query.endsWith(end[i])){
				t2 = true;
			}
		}
		for(int i=0;i<all.length;i++){
			if(query.contains(all[i])){
				t3 = true;
			}
		}
		return t1||t2||t3;
	}
}
