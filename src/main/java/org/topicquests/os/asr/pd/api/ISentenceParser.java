/**
 * 
 */
package org.topicquests.os.asr.pd.api;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface ISentenceParser {

	/**
	 * <p>Fundamentally, this returns a JSONObject with the results.</p>
	 * <p>Results will vary according to the agent used. For instance,
	 *  SpaCy, Linkgrammar, and Eidos return sentence POS information and related,
	 *  whereas Indra returns Statements (triples)</p>
	 * @param sentence
	 * @return
	 */
	IResult processSentence(String sentence);
}
