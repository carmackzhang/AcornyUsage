package test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import officialMap.UrlEncoder;
import officialMap.UrlEncoder.CharSet;

public class EncoderTest {

	public static void main(String[] args) {
		String keyword = "%2522%25B0%25CD%25B1%25B4%25C0%25AD%2522";
//			String res = UrlEncoder.urlEncode(keyword, CharSet.GBK);
		
		try {
			String res = URLDecoder.decode(keyword, "gbk");
			res = URLDecoder.decode(res,"gbk");
			System.out.println(res);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
