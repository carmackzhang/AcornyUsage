package officialMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import officialMap.UrlEncoder.CharSet;

public class OfficialMapMain {

	public static void main(String[] args) {
//		String subtitleOld = "C:/Users/wujunjie/Downloads/officialmapWords";
//		String subtitleNew = "C:/Users/wujunjie/Downloads/officialmapWordsNew";
//		if(args.length==2){
//			updateOfficialMap(args[0],args[1]);
//		}else{
//			System.err.println("Usage:(java -jar ** subtitleOldFileName subtitleNewFileName)");
//		}
		
		String keyword= "青岛聚创环保设备有限公司";//"北京指南针科技发展股份有限公司";
		System.out.println(sendQuery(keyword));
	}

	public static void updateOfficialMap(String from,String to) {
//		String keywordFile = "conf/subtitle_new1k";
//		String subtitleNew = "conf/subtitleNew";
		
		String keywordFile = from;
		String subtitleNew = to;
		
		FileReader fr;
		BufferedReader br;
		FileWriter fw;
		try {
			fr = new FileReader(new File(keywordFile));
			br = new BufferedReader(fr);
			fw = new FileWriter(new File(subtitleNew));
			int total = 0;
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				total++;
				if(total%10000==0)
					System.out.println(total);
				
				int index = tmp.indexOf("<officialmap>");
				
				if (index > 0) {
					StringBuilder beforeMap = new StringBuilder();
					beforeMap.append(tmp.substring(0, index));

					String oritmp = tmp;
					tmp = tmp.substring(index);
					int start = tmp.indexOf("<name>");
					int end = tmp.indexOf("</name>");
					String keyword = tmp.substring(start + 6, end);
					// System.out.println(keyword);
					SubtitleMap subMap = sendQuery(keyword);
					if (subMap != null) {
						// System.out.println(subMap.getName() + "\t" +
						// subMap.getAddress() + "\t" + subMap.getPositionx()
						// + "\t" + subMap.getPositiony());
						// System.out.println(tmp);

						int urlBegin = tmp.indexOf("<url>");
						int urlEnd = tmp.indexOf("</url>");
						String url = tmp.substring(urlBegin + 5, urlEnd);

						int phoneBegin = tmp.indexOf("<phone>");
						int phoneEnd = -1;
						String phone1 = "";
						if (phoneBegin > 0) {
//							phoneEnd = tmp.indexOf("</phone>");
							phoneEnd = tmp.lastIndexOf("</phone>");
							if ((phoneBegin + 7) < phoneEnd)
								phone1 = tmp.substring(phoneBegin + 7, phoneEnd);
						}

						int posxBegin = tmp.indexOf("<positionx>");
						int posxEnd = tmp.indexOf("</positionx>");
						String posx = tmp.substring(posxBegin + 11, posxEnd);

						int posyBegin = tmp.indexOf("<positiony>");
						int posyEnd = tmp.indexOf("</positiony>");
						String posy = tmp.substring(posyBegin + 11, posyEnd);

						int fromUrlBegin = tmp.indexOf("<fromurl>");
						int fromUrlEnd = tmp.indexOf("</fromurl>");
						String fromurl = tmp.substring(fromUrlBegin + 9, fromUrlEnd);

						subMap.setUrl(url);
						subMap.setPhone(phone1);
						subMap.setPositionx(posx);
						subMap.setPositiony(posy);
						subMap.setFromurl(fromurl);

//						System.out.println(subMap.toString());
						beforeMap.append(subMap.toString()+"\n");
						fw.write(beforeMap.toString());
					}else{
						fw.write(oritmp+"\n");
					}
				}else{
					fw.write(tmp+"\n");
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SubtitleMap sendQuery(String keyword) {

		String oriword = keyword;
		// keyword =
		// "浙江开山压缩机股份有限公司";//广州市番禺区南浦沿沙路联发工业区A8栋,大同市御河西路北都街口,广州市罗诚电子有限公司(1.2611015E7,2620311.0)
		try {
			keyword = UrlEncoder.urlEncode(keyword, CharSet.UTF8);
			keyword = UrlEncoder.urlEncode(keyword, CharSet.UTF8);

			String url = "http://map.sogou.com/EngineV6/search/json?what=keyword:" + keyword.toLowerCase()
					+ "&range=city:北京&exact=1&resultTypes=poi,busline";

			String res = readerPageByUrl(url);
			if (res.contains("error")) {
				System.out.println("ERROR!!  "+res);
				return null;
			}
			// System.out.println(res);
			ObjectMapper mapper = new ObjectMapper();
			MapResponse mapResponse;

			mapResponse = mapper.readValue(res, MapResponse.class);

			// System.out.println(mapResponse.toString());
			Feature feature = mapResponse.getResponse().getData().getFeature().get(0);
			String txt = feature.getPoints().getTxt();
			String address = feature.getDetail().getAddress();
			String phone = feature.getDetail().getPhone();
			
			String[] txts = txt.split(",");
			String positionx = "";
			String positiony = "";
			if(txts.length==2){
				positionx = txts[0];
				positiony = txts[1];
			}
			// System.out.println("address:" + address + "\t(posx,posy):" +
			// positionx + "," + positiony);
			// return address;
			SubtitleMap a = new SubtitleMap();
			a.setAddress(address);
			a.setPhone(phone);
			a.setName(oriword);
			a.setPositionx(positionx);
			a.setPositiony(positiony);
			return a;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readerPageByUrl(String pageUrl) {
		URL url;
		HttpURLConnection connection = null;
		BufferedReader br = null;
		String pageString = "";
		try {
			url = new URL(pageUrl);
			connection = (HttpURLConnection) url.openConnection();
			// connection.setRequestProperty("User-agent","Mozilla/5.0 (Windows
			// NT 6.1; rv:12.0) Gecko/20100101 Firefox/12.0");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			InputStream is = connection.getInputStream();
			// br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			br = new BufferedReader(new InputStreamReader(is, "gb2312"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			pageString = sb.toString();
			if (br != null)
				br.close();
			if (connection != null)
				connection.disconnect();
			// System.out.println("CLOSE CONNECTION TO SERVER");
		} catch (java.net.SocketTimeoutException e) {
			System.out.println("------ HTTP SERVER CONNECTION TIMEOUT!!!!! ----------");
			try {
				if (br != null)
					br.close();
				if (connection != null)
					connection.disconnect();

				Thread.sleep(10000);
			} catch (Exception e1) {
				e.printStackTrace();
			}
			return new String("  ");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return pageString;
	}
}

class SubtitleMap {
	private String url;
	private String name;
	private String address;
	private String phone;
	private String positionx;
	private String positiony;
	private String fromurl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPositionx() {
		return positionx;
	}

	public void setPositionx(String positionx) {
		this.positionx = positionx;
	}

	public String getPositiony() {
		return positiony;
	}

	public void setPositiony(String positiony) {
		this.positiony = positiony;
	}

	public String getFromurl() {
		return fromurl;
	}

	public void setFromurl(String fromurl) {
		this.fromurl = fromurl;
	}

	public String toString() {
		return "<officialmap><url>" + url + "</url><name>" + name + "</name><address>" + address + "</address><phone>"
				+ phone + "</phone><positionx>" + positionx + "</positionx><positiony>" + positiony
				+ "</positiony><fromurl>" + fromurl + "</fromurl></officialmap>";
	}
}

class Company {
	private String name;
	private String address;
	private String positionx;
	private String positiony;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPositionx() {
		return positionx;
	}

	public void setPositionx(String positionx) {
		this.positionx = positionx;
	}

	public String getPositiony() {
		return positiony;
	}

	public void setPositiony(String positiony) {
		this.positiony = positiony;
	}
}