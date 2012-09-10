package org.sakaiproject.nakamura.vivo;

import edu.cornell.mannlib.vitro.webapp.beans.Individual;
import edu.cornell.mannlib.vitro.webapp.beans.IndividualImpl;
import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.TemplateProcessingHelper;
import edu.cornell.mannlib.vitro.webapp.controller.freemarker.TemplateProcessingHelper.TemplateProcessingException;
import edu.cornell.mannlib.vitro.webapp.controller.individual.IndividualRequestAnalysisContextImpl;
import edu.cornell.mannlib.vitro.webapp.controller.individual.IndividualRequestInfo;
import edu.cornell.mannlib.vitro.webapp.dao.IndividualDao;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SakaiProfileServlet extends HttpServlet {

  private static final long serialVersionUID = -6915802898293096669L;
  private static final String TEMPLATE = "sakai-full-profile-to-json.ftl";

  /**
   * Returns a VIVO profile in json format.
   * 
   * 
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      // Get the individual
      VitroRequest vreq = new VitroRequest(req);
      Individual individual = getIndividualFromRequest(vreq);
      if (individual == null) {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Couldn't locate individual.");
        return;
      }

      // Pass the individual to the template and render it.
      Map<String, Object> body = wrapIndividual(vreq, individual);
      TemplateProcessingHelper helper = new TemplateProcessingHelper(vreq,
          getServletContext());
      StringWriter sw = helper.processTemplate(TEMPLATE, body);

      // Write everything back out.
      resp.setHeader("Content-Type", "application/json");
      resp.getWriter().write(sw.toString());
      resp.getWriter().flush();
    } catch (TemplateProcessingException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Couldn't render template.");
    } catch (TemplateModelException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
          "Couldn't render template.");
    }
  }

  /**
   * Creates a VIVO profile.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Get some data from the request.
    String firstName = req.getParameter("firstName");
    String lastName = req.getParameter("lastName");
    String primaryEmail = req.getParameter("email");
    String userID = req.getParameter("userid");

    // Create the individual
    IndividualImpl ind = new IndividualImpl();

    // Store it.
    VitroRequest vreq = new VitroRequest(req);

    IndividualDao dao = vreq.getWebappDaoFactory().getIndividualDao();
  }

  /**
   * 
   * @param vreq
   * @return
   * @throws TemplateModelException
   */
  private Map<String, Object> wrapIndividual(VitroRequest vreq, Individual individual)
      throws TemplateModelException {
    DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
    wrapper.setExposureLevel(BeansWrapper.EXPOSE_SAFE);

    Map<String, Object> body = new HashMap<String, Object>();
    body.put("individual",
        wrapper.wrap(new SakaiIndividualTemplateModel(individual, vreq)));
    return body;
  }

  /**
   * Determines the individual that's associated with this request in a couple of ways:
   * 
   * <pre>
   *  Checks the request parameters:
   *   - uri
   *   - netid
   *   - by=..&value=.. where by is a dataproperty set on the person.
   *  for the following constructs:
   *     /individual?uri=urlencodedURI
   *     /individual?netId=bdc34
   *     /individual?netid=bdc34
   *     /individual/localname         
   *     /display/localname
   *     /individual/localname/localname.rdf
   *     /individual/localname/localname.n3
   *     /individual/localname/localname.ttl
   *     /individual/nsprefix/localname
   * </pre>
   * 
   * @param vreq
   * @return
   */
  private Individual getIndividualFromRequest(VitroRequest vreq) {
    IndividualRequestInfo requestInfo = new SakaiIndividualRequestAnalyzer(vreq,
        new IndividualRequestAnalysisContextImpl(vreq)).analyze();
    return requestInfo.getIndividual();
  }

}
