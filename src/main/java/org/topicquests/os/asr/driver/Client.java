/**
 * 
 */
package org.topicquests.os.asr.driver;

import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;
import java.io.*;
import java.net.*;

/**
 * @author jackpark
 *
 */
public class Client {
	private EDriverEnvironment environment;

	/**
	 * 
	 */
	public Client(EDriverEnvironment env) {
		environment = env;
	}

	/**
	 * Process a sentence {@link queryString}, a JSON string
	 * @param url
	 * @param queryString
	 * @return
	 */
	public IResult put(String url, String queryString) {
		IResult result = new ResultPojo();
		BufferedReader rd = null;
		HttpURLConnection con = null;
		try {
			URL urx = new URL(url);

			con = (HttpURLConnection) urx.openConnection();
			//con.setReadTimeout(TIMEOUT);
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			PrintWriter p = new PrintWriter(os);
			p.print(queryString);
			p.flush();
			p.close();
			//con.connect();
			System.out.println("CODE "+con.getResponseCode());
			rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder buf = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				buf.append(line + '\n');
			}

			result.setResultObject(buf.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.addErrorString(e.getMessage());
		}
		return result;
	}
}
