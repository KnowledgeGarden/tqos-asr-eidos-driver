/**
 * 
 */
package devtests;

import org.topicquests.support.api.IResult;
import org.topicquests.support.util.TextFileHandler;

import net.minidev.json.JSONObject;


/**
 * @author jackpark
 *
 */
public class ParagraphTest extends TestRoot {
	private final String S1=
			"The metabolism of oxygen in aerobic organisms leads to generation of reactive oxygen species (ROS). These entities are able to oxidize almost all classes of macromolecules, including proteins, lipids and nucleic acids. The physiological level of ROS is usually regulated by antioxidant defense mechanisms. There are at least three groups of antioxidant enzymes: superoxide dismutases, catalases and glutathione peroxidases (GSH-Pxs) which neutralize ROS. The trace elements (copper, zinc and selenium) bound to the active sites of the above listed enzymes play an important role in the antioxidant defense system. In mammals, a major function of selenium (Se) and Se-dependent GSH-Pxs is to protect cells from oxidative stress. Selenium concentrations and GSH-Px activities are altered in blood components of chronic kidney disease (CKD) patients. The Se level is frequently lower than in healthy subjects and the concentration very often decreases gradually with advancing stage of the disease. Studies on red cell GSH-Px activity in CKD patients reported its values significantly lower, significantly higher and lower or higher, but not significantly as compared with healthy subjects. On the other hand, all authors who studied plasma GSH-Px activity have shown significantly lower values than in healthy subjects. The degree of the reduction decreases gradually with the progression of the disease. High inverse correlations were seen between plasma GSH-Px activity and creatinine level. A gradual decrease in plasma GSH-Px activity in CKD patients is due to the fact that this enzyme is synthesized predominantly in the kidney and thus the impairment of this organ is the cause of the enzyme's lower activity. Se supplementation to CKD patients has a slightly positive effect in the incipient stage of the disease, but usually no effect was observed in end-stage CKD. Presently, kidney transplantation is the only treatment that may restore plasma Se level and GSH-Px activity in patients suffering from end-stage CKD. A few studies have shown that in kidney recipients, plasma Se concentration and GSH-Px activity are restored to normal values within a period of 2 weeks to 3 months following surgery and thus it can be acknowledged that Se supplementation to those patients has a positive effect on plasma GSH-Px activity. ";
	/**
	 * 
	 */
	public ParagraphTest() {
		super();
		IResult r = environment.processSentence(S1);
		System.out.println("A "+r.getErrorString());
		System.out.println("B "+r.getResultObject());
		TextFileHandler h = new TextFileHandler();
		h.writeFile("eidos2.json", (String)r.getResultObject());
		
		environment.shutDown();

	}

}
