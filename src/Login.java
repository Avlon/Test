import org.apache.http.client.CookieStore;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Login {
	static List<Cookie> cookies=null;
	static CookieStore	cookiestore;
	static public class User{
		String	email;
		String	password;
	}
	static public class Analysiser{
		Document document;
		ArrayList<String> names=new ArrayList<String>();
		ArrayList<String> value=new ArrayList<String>();
		String URL=null;
		public void init(String html){
			document=Jsoup.parse(html);
		}
		public void submit(User user){
			Element body=document.body();
			Element form=body.select("form").first();
			URL=form.attr("action").toString();
			Elements input=form.select("input");
			//System.out.println(form.toString());
			System.out.println(URL);
			for(Element ele : input){
				String temp=ele.attr("name").toString();
				if(!temp.equals("")){
					names.add(temp);
					//System.out.println(ele.attr("name").toString());
				}
				temp=ele.attr("value").toString();
				if(temp!=null){
					value.add(temp);
				}
			}
			value.set(0, user.email);
			value.set(1,user.password);
			for(String temp:names){
				System.out.println(temp);
			}
			for(String temp:value){
				System.out.println(temp);
			}
		}
	}
	static public String GetHomePage(HttpClient	httpclient){
		String html = null;
		try{
			httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
			HttpGet	httpget=new HttpGet("https://www.linkedin.com/");
			httpget.setHeader("Accept", "text/html, application/xhtml+xml, */*");
			//httpget.setHeader("Accept-Encoding", "gzip, deflate"); 
			httpget.setHeader("Accept-Language", "zh-CN");
			httpget.setHeader("Connection", "Keep-Alive");
			httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
			HttpResponse response=httpclient.execute(httpget);
			HttpEntity	entity=response.getEntity();
			if(cookies==null){
				cookies=((AbstractHttpClient)httpclient).getCookieStore().getCookies();
			}
			if(cookies.isEmpty()){
				System.out.println("nove");
			}
			else{
				for(Cookie cookie:cookies){
					System.out.println(cookie.toString());
				}
			}
			cookiestore=((AbstractHttpClient)httpclient).getCookieStore();
			html=EntityUtils.toString(entity,"GBK");
			//System.out.println(html);
			httpget.releaseConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return html;
	}
	static public void	PostLogin(Analysiser analy,HttpClient	httpclient){
		String html=null;
		try{
//			BasicClientCookie cookie1 = new BasicClientCookie("_lipt","CwEAAAFamgqjclipkL-u0mMBSuA0Wwbzf5af76wgC190G5QpOqmtJWTWgob4QsYSAiKtM556uPiMMTcLHmU3sMbWYmxuTwpMXr9mh6s_zSd1hU0n-jq_Fbj_OVKNhLixjKMkzOcGvzTK0W8gg61i_kQGDnYQaR0XyD3ZPlc2PfUeIQEhWuFI6r2WyYcwCpY");
//	        cookie1.setDomain(".linkedin.com");
//	        cookie1.setPath("/");
//	        cookiestore.addCookie(cookie1);
//	        BasicClientCookie cookie2 = new BasicClientCookie("lidc","\"b=SGST04:g=2:u=1:i=1488643080:t=1488729480:s=AQH2skkYP4FWvyaAkv1mOc4NP86curdJ\"");
//	        cookie2.setDomain(".linkedin.com");
//	        cookie2.setPath("/");
//	        cookiestore.addCookie(cookie2);
//	        BasicClientCookie cookie3 = new BasicClientCookie("RT","s=1488677181068&r=https://www.linkedin.com/");
//	        cookie3.setDomain(".linkedin.com");
//	        cookie3.setPath("/");
//	        cookiestore.addCookie(cookie3);
//	        BasicClientCookie cookie4 = new BasicClientCookie("leo_auth_token","\"GST:UcQXaSPcnwjPQlWVGGlWsn-gupdFMLyV228Xd3V01Hjui1tYyAZ3He:1488677157:cd1a3334a2b18ba1692ff085a97baf187d852244\"");
//	        cookie4.setDomain(".www.linkedin.com");
//	        cookie4.setPath("/");
//	        cookiestore.addCookie(cookie4);
//			((AbstractHttpClient)httpclient).setCookieStore(cookiestore);
//			cookies=((AbstractHttpClient)httpclient).getCookieStore().getCookies();
//			for(Cookie cookie:cookies){
//				System.out.println(cookie.toString());
//			}
			HttpPost	httppost=new HttpPost(analy.URL);
			httppost.setHeader("Accept", "text/html, application/xhtml+xml, */*");
			httppost.setHeader("Accept-Language", "zh-CN");
			httppost.setHeader("Connection", "Keep-Alive");
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; LCJB; rv:11.0) like Gecko");
			
			ArrayList<NameValuePair> parms=new ArrayList<NameValuePair>();
			for(int i=0;i<analy.names.size();i++){
				BasicNameValuePair bnvp=new BasicNameValuePair(analy.names.get(i),analy.value.get(i));
			}
			httppost.setEntity(new UrlEncodedFormEntity(parms,"UTF-8"));
			HttpResponse response=httpclient.execute(httppost);
			System.out.println(response.getStatusLine().getStatusCode());
			HttpEntity	entity=response.getEntity();
			if(entity!=null){
					cookies=((AbstractHttpClient)httpclient).getCookieStore().getCookies();
				if(cookies.isEmpty()){
					System.out.println("nove");
				}
				else{
					for(Cookie cookie:cookies){
						System.out.println(cookie.toString());
					}
				}
				html=EntityUtils.toString(entity,"UTF-8");
			}else{
				System.out.println("entity null");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			File fp=new File("D:\\test\\output.txt");
			PrintWriter pfp= new PrintWriter(fp);
			pfp.write(html);
			pfp.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(html);
	}
	public static void main(String args[]){
		HttpClient	httpclient=new DefaultHttpClient();
		User user=new User();
		user.email="b14020121@njupt.edu.cn";
		user.password="zhao960129";
		Analysiser ana=new Analysiser();
		ana.init(GetHomePage(httpclient));
		ana.submit(user);
		PostLogin(ana,httpclient);
	}
}
