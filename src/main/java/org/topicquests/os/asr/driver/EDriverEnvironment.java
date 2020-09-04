/**
 * 
 */
package org.topicquests.os.asr.driver;

import org.topicquests.os.asr.pd.api.ISentenceParser;
import org.topicquests.support.RootEnvironment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class EDriverEnvironment extends RootEnvironment implements ISentenceParser {
	private EidosClient client;
	private final String
		EIDOS_HOST,
		EIDOS_PORT,
		EIDOS_SERVICE;
	/**
	 */
	public EDriverEnvironment() {
		super("ed-props.xml", "logger.properties");
		client = new EidosClient(this);
		EIDOS_HOST = getStringProperty("EidosHost");
		EIDOS_PORT = getStringProperty("EidosPort");
		EIDOS_SERVICE = "http://"+EIDOS_HOST+":"+EIDOS_PORT+"/process_text";
	}

	@Override
	public IResult processSentence(String sentence) {
		JSONObject query = new JSONObject();
		query.put("text", sentence);
		return client.put(EIDOS_SERVICE, query.toJSONString());
	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

}
