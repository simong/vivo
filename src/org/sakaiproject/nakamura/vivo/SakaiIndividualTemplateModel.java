package org.sakaiproject.nakamura.vivo;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.web.templatemodels.individual.IndividualTemplateModel;

public class SakaiIndividualTemplateModel extends IndividualTemplateModel {

  public SakaiIndividualTemplateModel(Individual individual, VitroRequest vreq) {
    super(individual, vreq);
  }

  public String getFirstName() {
    return individual.getDataValue("http://xmlns.com/foaf/0.1/firstName");
  }

  public String getLastName() {
    return individual.getDataValue("http://xmlns.com/foaf/0.1/lastName");
  }

  public String getPrimaryEmail() {
    return individual.getDataValue("http://vivoweb.org/ontology/core#primaryEmail");
  }

}
