/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package org.vivoweb.reasoner.plugin;

import edu.cornell.mannlib.vitro.webapp.reasoner.ReasonerPlugin;

public class DCCreatorForDocuments extends SimpleBridgingRule implements ReasonerPlugin {
	
	private final static String DCTERMS = "http://purl.org/dc/terms/";
	private final static String VIVOCORE = "http://vivoweb.org/ontology/core#";
	
	public DCCreatorForDocuments() {
		super(VIVOCORE + "informationResourceInAuthorship", 
			  VIVOCORE + "linkedAuthor",
			  DCTERMS + "creator");
	}

}
