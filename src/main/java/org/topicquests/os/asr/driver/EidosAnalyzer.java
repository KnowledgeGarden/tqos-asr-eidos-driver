/**
 * 
 */
package org.topicquests.os.asr.driver;

import java.util.*;

import org.topicquests.os.asr.driver.common.SentenceObject;
import org.topicquests.os.asr.driver.common.api.ISentenceObject;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class EidosAnalyzer {
	private EDriverEnvironment environment;
	private final float REL_THRESHOLD = 0.3f; //TODO make config value
	/**
	 * 
	 */
	public EidosAnalyzer(EDriverEnvironment env) {
		environment = env;
	}
	
	/**
	 * <p>{@code r} brings in a raw JSON string which must
	 * be analyzed into a structure useful for ASR reading</p>
	 * <p>Structure<br/>
	 * 	documents array
	 * 	for each documents array element
	 * 		text
	 * 		sentences array
	 *			for each sentences array element
	 *	  			text
	 *	  			rawText
	 *	  			words array
	 *		  			for each words array element
	 *		  				"endOffset": 3,
	 *						"chunk": "B-NP",
	 *						"lemma": "the",
	 *						"tag": "DT",
	 *						"text": "The",
	 *						"norm": "O",
	 *						"entity": "O",
	 *						"startOffset": 0,
	 *						"@type": "Word",
	 *						"@id":
	 *				dependencies array
	 *					for each dependencies array element
	 *						"@type": "Dependency",
	 *						"source" 
	 *							"@id": "_:Word_24"
	 *						"destination"
	 *							"@id": "_:Word_34"
	 *						"relation": "cc"
	 * 	extractions array
	 *  for each extractions array element
	 *  	subtype (e.g. entity)
	 *  		entity, correlation, causation, ???
	 *  		can have internal (nested) extractions of types
	 *  			relation which can have further internal structures
	 *  	rule
	 *  	text
	 *  	canonicalName
	 *  	labels array e.g. concept, entity
	 *  	relevance
	 *  	provenance array
	 *  		for each provenance array element
	 *  			documentCharPositions
	 *  			@type
	 *  			start
	 *  			end
	 *  	document
	 *  		@id  which doc this element belongs to
	 *  	sentenceWordPositions array
	 *  		for each sentenceWordPositions array element
	 *  			@type
	 *  			start
	 *  			end
	 *  	sentence
	 *  		@id which sentence this element belongs to
	 *  	type e.g. concept
	 *  	groundings array
	 *  		for each groundings array element
	 *  			name
	 *  			versionDate
	 *  			version
	 *  			values array
	 *  				for each values array element
	 *  					@type
	 *  					"ontologyConcept": "wm/concept/causal_factor/health_and_life/disease/"
	 *  					value  a number
	 * </p>
	 * <p>
	 * State types for the field "states" (some may not have a "type" field:
	 * 	HEDGE e.g. believe, may,
	 *  INC
	 *  QUANT
	 *  LocationExp
	 *  DEC
	 *  </p>
	 * @param r
	 */
	public void analyzeEidos(IResult r) {
		String json = (String)r.getResultObject();
		JSONObject raw = null;
		try {
			JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			raw = (JSONObject)p.parse(json);
		} catch (Exception e) {
			e.printStackTrace();
			environment.logError(e.getLocalizedMessage(), e);
			r.addErrorString(e.getMessage());
			return;
		}
		List<JSONObject> documents = (List<JSONObject>)raw.get("documents");
		Iterator<JSONObject> itr = documents.iterator();
		JSONObject workingMap = new JSONObject();
		while (itr.hasNext())
			processDocument(itr.next(), r, workingMap);
		//reuse object
		documents = (List<JSONObject>)raw.get("extractions");
		itr = documents.iterator();
		while (itr.hasNext())
			processExtraction(itr.next(), r, workingMap);
		environment.logDebug("D+ "+workingMap);
	}
	
	/**
	 * We must populate {@code workingMap} with sufficient sentence and word @id values
	 * that later extraction references will be possible
	 * @param doc
	 * @param r
	 * @param workingMap
	 */
	private void processDocument(JSONObject doc, IResult r, JSONObject workingMap) {
		System.out.println("D "+doc);
		List<JSONObject> sentences = (List<JSONObject>)doc.get("sentences");
		Iterator<JSONObject>itr = sentences.iterator();
		String id = doc.getAsString("@id");
		while (itr.hasNext())
			processSentence(itr.next(), id, workingMap);
	}
	
	/**
	 * {@code workingMap} needs to know sentenceId with this {@code sentence}
	 * and all the word objects indexed against their @id for extraction processing
	 * @param sentence
	 * @param docId
	 * @param workingMap
	 */
	private void processSentence(JSONObject sentence, String docId, JSONObject workingMap) {
		System.out.println("X "+sentence);
		ISentenceObject sent = new SentenceObject();
		sent.setDocumentId(docId);
		sent.setSentenceId(sentence.getAsString("@id"));
		sent.setText(sentence.getAsString("text"));
		//word objects
		List<JSONObject> words = (List<JSONObject>)sentence.get("words");
		// word-word dependencies: source, target, relation
		List<JSONObject> deps = (List<JSONObject>)sentence.get("dependencies");
		JSONObject jo;
		Iterator<JSONObject> itr = words.iterator();
		while (itr.hasNext() ) {
			jo = itr.next();
			sent.addWordObject(jo);
			workingMap.put(jo.getAsString("@id"), jo);
		}
		if (deps != null && !deps.isEmpty()) {
			itr = deps.iterator();
			while (itr.hasNext()) {
				jo = itr.next();
				sent.addWordDependencyObject(jo);
			}
		}
		jo = (JSONObject)workingMap.get("sentences");
		if (jo == null) jo = new JSONObject();
		jo.put(sentence.getAsString("@id"), sent.getData());
		workingMap.put("sentences", jo);
	}
	
	private void processExtraction(JSONObject ext, IResult r, JSONObject workingMap) {
		System.out.println("E "+ext);
		float relevance = ext.getAsNumber("relevance").floatValue();
		if (relevance < REL_THRESHOLD)
			return;
		//otherwise, processs this puppy
		//get the sentence related to this extraction
		JSONObject jo = (JSONObject)ext.get("sentence");
		String sentenceId = jo.getAsString("@id");
		jo = (JSONObject)workingMap.get("sentences");
		JSONObject ts = (JSONObject)jo.get(sentenceId);
		ISentenceObject thisSentence = new SentenceObject(ts);
		String type = ext.getAsString("type");
		String subType = ext.getAsString("subType");
		if (type.equals("relation")) {
			//make a relation
			processRelation(thisSentence, ext, relevance, subType);
		} else if (type.equals("concept")) {
			
		}
		
	}
	
	void processRelation(ISentenceObject so, JSONObject reln, float relevance, String subType) {
		JSONObject triple = new JSONObject();
		triple.put("relevance", relevance);
		triple.put("pred", subType);
		JSONObject jo;
		//triple.put("trigger", remappingFunction)
		List<JSONObject> states = (List<JSONObject>)reln.get("states");
		Iterator<JSONObject> itx;
		if (states != null) {
			itx = states.iterator();
			while (itx.hasNext()) {
				jo = itx.next();
				
			}
		}
	}

}
