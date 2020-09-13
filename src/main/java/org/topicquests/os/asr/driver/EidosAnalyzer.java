/**
 * 
 */
package org.topicquests.os.asr.driver;

import java.util.*;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 *
 */
public class EidosAnalyzer {
	private EDriverEnvironment environment;

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
		while (itr.hasNext())
			processDocument(itr.next(), r);
		//reuse object
		documents = (List<JSONObject>)raw.get("extractions");
		itr = documents.iterator();
		while (itr.hasNext())
			processDocument(itr.next(), r);
	}
	
	private void processDocument(JSONObject doc, IResult r) {
		System.out.println("D "+doc);
	}
	
	private void processExtraction(JSONObject ext, IResult r) {
		System.out.println("E "+ext);
	}

}
