/**
 * 
 */
package devtests;

import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class FirstTest extends TestRoot {
	public final String
		S1 = "The pandemic of obesity, type 2 diabetes mellitus (T2DM) and nonalcoholic fatty liver disease (NAFLD) has frequently been associated with dietary intake of saturated fats (1) and specifically with dietary palm oil (PO) (2).";

	/**
	 * 
	 */
	public FirstTest() {
		IResult r = environment.processSentence(S1);
		System.out.println("A "+r.getErrorString());
		System.out.println("B "+r.getResultObject());
		environment.shutDown();
		System.exit(0);
	}

}
