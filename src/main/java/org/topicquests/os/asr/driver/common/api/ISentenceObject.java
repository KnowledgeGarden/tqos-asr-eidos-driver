/**
 * 
 */
package org.topicquests.os.asr.driver.common.api;

import java.util.List;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ISentenceObject {
	public static final String
		SENTENCEID			= "sentId",
		PARAGRAPHID			= "paraId",
		DOCUMENTID			= "docId",
		TEXT				= "text",
		WORD_LIST			= "wordList",
		DEPENDENCY_LIST		= "depList",
		TERM_LIST			= "termList",
		RELATION_LIST		= "relationList";
	
	void setSentenceId(String id);
	void setParagraphId(String id);
	void setDocumentId(String id);
	void setText(String text);
	
	String getSentenceId();
	String getParagraphId();
	String getDocumentId();
	String getText();
	
	void addWordObject(JSONObject word);
	List<JSONObject> listWordObjects();
	
	void addWordDependencyObject(JSONObject dep);
	List<JSONObject> listWordDependencies();
	
	
	void addTermObject(JSONObject term);
	List<JSONObject> listTermObjects();
	
	void addRelationObject(JSONObject relation);
	List<JSONObject> listRelationObjects();
	
	JSONObject getData();

}
