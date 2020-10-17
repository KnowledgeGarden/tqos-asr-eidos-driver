/**
 * 
 */
package org.topicquests.os.asr.driver.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topicquests.os.asr.driver.common.api.ISentenceObject;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SentenceObject implements ISentenceObject {
	private JSONObject data;
	/**
	 * 
	 */
	public SentenceObject() {
		data = new JSONObject();
	}

	/**
	 * @param map
	 */
	public SentenceObject(JSONObject map) {
		data = map;
	}

	@Override
	public void setSentenceId(String id) {
		data.put(ISentenceObject.SENTENCEID, id);
	}

	@Override
	public void setParagraphId(String id) {
		data.put(ISentenceObject.PARAGRAPHID, id);
	}

	@Override
	public void setDocumentId(String id) {
		data.put(ISentenceObject.DOCUMENTID, id);
	}

	@Override
	public String getSentenceId() {
		return data.getAsString(ISentenceObject.SENTENCEID);
	}

	@Override
	public String getParagraphId() {
		return data.getAsString(ISentenceObject.PARAGRAPHID);
	}

	@Override
	public String getDocumentId() {
		return data.getAsString(ISentenceObject.DOCUMENTID);

	}

	@Override
	public void addWordObject(JSONObject word) {
		List<JSONObject> l = (List<JSONObject>)data.get(ISentenceObject.WORD_LIST);
		if (l ==  null) l = new ArrayList<JSONObject>();
		l.add(word);
		data.put(ISentenceObject.WORD_LIST, l);
	}

	@Override
	public List<JSONObject> listWordObjects() {
		return (List<JSONObject>)data.get(ISentenceObject.WORD_LIST);
	}

	@Override
	public void addTermObject(JSONObject term) {
		List<JSONObject> l = (List<JSONObject>)data.get(ISentenceObject.TERM_LIST);
		if (l ==  null) l = new ArrayList<JSONObject>();
		l.add(term);
		data.put(ISentenceObject.TERM_LIST, l);
	}

	@Override
	public List<JSONObject> listTermObjects() {
		return (List<JSONObject>)data.get(ISentenceObject.TERM_LIST);

	}

	@Override
	public void addRelationObject(JSONObject relation) {
		List<JSONObject> l = (List<JSONObject>)data.get(ISentenceObject.RELATION_LIST);
		if (l ==  null) l = new ArrayList<JSONObject>();
		l.add(relation);
		data.put(ISentenceObject.RELATION_LIST, l);
	}

	@Override
	public List<JSONObject> listRelationObjects() {
		return (List<JSONObject>)data.get(ISentenceObject.RELATION_LIST);
	}
	
	public JSONObject getData() {
		return data;
	}

	@Override
	public void addWordDependencyObject(JSONObject dep) {
		List<JSONObject> l = (List<JSONObject>)data.get(ISentenceObject.DEPENDENCY_LIST);
		if (l ==  null) l = new ArrayList<JSONObject>();
		l.add(dep);
		data.put(ISentenceObject.DEPENDENCY_LIST, l);
	}

	@Override
	public List<JSONObject> listWordDependencies() {
		return (List<JSONObject>)data.get(ISentenceObject.DEPENDENCY_LIST);
	}

	@Override
	public void setText(String text) {
		data.put(ISentenceObject.TEXT, text);
	}

	@Override
	public String getText() {
		return data.getAsString(ISentenceObject.TEXT);
	}

}
