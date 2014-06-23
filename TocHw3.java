/*################
姓名 : 吳邦宇

學號 : F74006292

簡述 : 
將json檔 "鄉鎮市區" "土地區段" 欄位取出 "交易年月"只保留年的部分
如果和要求相符便將總價加入並增加一筆資料筆數
最後印出 (int)(總價/資料筆數)

參考 : 檔案的讀取 http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
#################*/

import java.io.*;
import java.net.*;
import org.json.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;

public class TocHw3
{
	public static void main(String[] args) throws IOException, JSONException
	{
		JSONArray json = readJsonFromUrl(args[0]); //web url = args[0]
		Pattern pattern1=Pattern.compile(".*"+args[1]+".*"); //district
		Pattern pattern2=Pattern.compile(".*"+args[2]+".*"); //road
		int Year=Integer.valueOf(args[3]); //year
		int Output=0; //total price
		int count=0; //how many data
		for(int i=0 ;i<json.length() ;++i)
		{
			Matcher matcher1=pattern1.matcher(json.getJSONObject(i).getString("鄉鎮市區")); //to match this pattern with object(i) in array
			Matcher matcher2=pattern2.matcher(json.getJSONObject(i).getString("土地區段位置或建物區門牌")); //to match this pattern
			int year_data=json.getJSONObject(i).getInt("交易年月")/100; //get only year (delete month)
			if(matcher1.find() && matcher2.find() && year_data>=Year)
			{
				Output+=json.getJSONObject(i).getInt("總價元");
				++count;
			}
		}
		System.out.println((int)(Output/(count>0?count:1))); //avg = total/count (avoid count is 0)
	}

	private static String readAll(Reader rd) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp=rd.read())!=-1) sb.append((char)cp); //get one char and put into string
		return sb.toString(); //return a string
	}

	public static JSONArray readJsonFromUrl(String url) throws IOException, JSONException
	{
		InputStream is = new URL(url).openStream(); //string of url
		try
		{
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); //use UTF-8 to read the url and save in rd
			String jsonText = readAll(rd);
			JSONArray jsonRealPrice = new JSONArray(jsonText); //save in json array format
			return jsonRealPrice; //return the json array
		}
		finally
		{
			is.close(); //close the InputStream is
		}
	}
}
