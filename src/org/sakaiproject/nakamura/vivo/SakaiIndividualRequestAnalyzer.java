package org.sakaiproject.nakamura.vivo;

import java.util.List;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.individual.IndividualRequestAnalysisContext;
import edu.cornell.mannlib.vitro.webapp.controller.individual.IndividualRequestAnalyzer;
import edu.cornell.mannlib.vitro.webapp.dao.IndividualDao;

public class SakaiIndividualRequestAnalyzer extends IndividualRequestAnalyzer {

  private VitroRequest vReq;

  public SakaiIndividualRequestAnalyzer(VitroRequest vreq,
      IndividualRequestAnalysisContext analysisContext) {
    super(vreq, analysisContext);

    this.vReq = vreq;
  }

  public Individual getIndividualFromRequest() {
    Individual individual = super.getIndividualFromRequest();
    if (individual != null) {
      return individual;
    }

    // Get it via a data property.
    String by = this.vReq.getParameter("by");
    String value = this.vReq.getParameter("value");
    IndividualDao individualDao = this.vReq.getWebappDaoFactory().getIndividualDao();
    List<Individual> ind = individualDao.getIndividualsByDataProperty(by, value);
    if (ind != null && ind.size() == 1) {
      return ind.get(0);
    }

    // Nothing found :(
    return null;

  };

}
