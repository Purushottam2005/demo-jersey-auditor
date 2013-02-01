package ws.cogito.governance.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import ws.cogito.governance.model.AuditEvents;
import ws.cogito.governance.service.AuditingServices;

/**
 * Handles Audit Events Resource Requests
 * @author jeremydeane
 */
@Path ("/")
public final class AuditEventsResource {
	
	private static Logger logger = Logger.getLogger(AuditEventResource.class);

	/**
	 * Handles requests for Audit Events Resource Representations
	 * @param application
	 * @return Response
	 * @throws WebApplicationException
	 */
	@GET
	@Path ("{application}/events")	
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAuditEvents(@PathParam("application") 
		String application) throws WebApplicationException {
		
		logger.debug("Application: " + application);
		
		AuditEvents auditEvents;
		
		try {
		
			auditEvents = AuditingServices.retrieveAuditEvents
					(application, "localhost", 8080, "/jersey-auditor");
		
		} catch (Exception e) {
			
			logger.error(e);
			
			//500 Internal Server Error
			throw new WebApplicationException(500);
		}


		//return resource representation OK 200
		return Response.ok(auditEvents).build();
	}
}