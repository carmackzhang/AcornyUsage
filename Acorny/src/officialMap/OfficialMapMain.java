package officialMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import officialMap.UrlEncoder.CharSet;

public class OfficialMapMain {

	public static void main(String[] args) {
		
		String keyword = "广州市番禺区南浦沿沙路联发工业区A8栋";
		try {
			keyword = UrlEncoder.urlEncode(keyword, CharSet.UTF8);
			keyword = UrlEncoder.urlEncode(keyword, CharSet.UTF8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String url = "http://map.sogou.com/EngineV6/search/json?what=keyword:"+keyword.toLowerCase()+"&range=city:全国&exact=1&resultTypes=poi,busline";
		
		String res = readerPageByUrl(url);
		System.out.println(res);
		ObjectMapper mapper = new ObjectMapper();
		try {
			MapResponse mapResponse = mapper.readValue(res, MapResponse.class);
//			System.out.println(mapResponse.toString());
			Feature feature = mapResponse.getResponse().getData().getFeature().get(0);
			String txt = feature.getPoints().getTxt();
			String[] txts = txt.split(",");
			String positionx = txts[0];
			String positiony = txts[1];
			System.out.println(txt+"--(posx,posy):"+positionx+","+positiony);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			connection.setConnectTimeout(50000);
			connection.setReadTimeout(50000);
			InputStream is = connection.getInputStream();
//			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			br = new BufferedReader(new InputStreamReader(is,"gb2312"));
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
//			System.out.println("CLOSE CONNECTION TO SERVER");
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
