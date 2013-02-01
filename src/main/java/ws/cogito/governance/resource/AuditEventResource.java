package ws.cogito.governance.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import ws.cogito.governance.model.AuditEvent;
import ws.cogito.governance.service.AuditingServices;


/**
 * Handles requests for Audit Event Resources
 * @author jeremydeane
 */
@Path ("/audit")
public final class AuditEventResource {
	
	private static Logger logger = Logger.getLogger(AuditEventResource.class);

	/**
	 * Retrieve a specific Audit Event Resource Representation
	 * @param auditEventKey
	 * @return Response
	 * @throws WebApplicationException
	 */
	@GET
	@Path ("/event/{auditEventKey}")	
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAuditEvent(@PathParam("auditEventKey") 
		String auditEventKey) throws WebApplicationException {
		
		logger.debug("Audit Event Key: " + auditEventKey);
		
		AuditEvent event = AuditingServices.retrieveAuditEvent(auditEventKey);
		
		if (event == null) {
			
			//404 Not Found
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}

		//return resource representation OK 200
		return Response.ok(event).build();
	}
	

	/**
	 * Create or Update an 'Identified' Audit Event Resource with Representation
	 * @param auditEventKey
	 * @param uriInfo	
	 * @param auditEvent
	 * @return Response
	 * @throws WebApplicationException
	 */
	@PUT
	@Path ("/event/{auditEventKey}")	
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })	
	public Response putAuditEvent(@PathParam("auditEventKey") 
		String auditEventKey, @Context UriInfo uriInfo, AuditEvent auditEvent) 
				throws WebApplicationException {
	
		//method variables
		ResponseBuilder builder = null;
		
		try {
			
			//created a new audit event - otherwise just an update 200 Ok
			if (AuditingServices.storeAuditEvent(auditEvent, auditEventKey) == null) {
				
				//Set status to 201 Created
				builder = Response.created(uriInfo.getRequestUri());
			
			} else {
			
				//set status to 200 Ok
				builder = Response.ok(auditEvent);
			}
			
		} catch (Exception e) {
			
			logger.error(e);
			
			//500 Internal Server Error
			throw new WebApplicationException(500);
		}
	
		return builder.build();
	}
	
	/**
	 * Create or update 'Unidentified' Audit Event Resource with Representation
	 * @param auditEvent
	 * @param uriInfo
	 * @return Response
	 * @throws WebApplicationException
	 */
	@POST
	@Path ("/event")	
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })	
	public Response postAuditEvent(AuditEvent auditEvent, 
			@Context UriInfo uriInfo) throws WebApplicationException {
		
		//method variables
		ResponseBuilder builder = null;
		
		try {
			
			//created a new audit event - otherwise just an update 200 Ok
			if (AuditingServices.storeAuditEvent(auditEvent) == null) {
				
				//Set status to 201 Created
				builder = Response.created(uriInfo.getRequestUri());
			
			} else {
			
				//set status to 200 Ok
				builder = Response.ok(auditEvent);
			}

		} catch (Exception e) {
			
			logger.error(e);
			
			//500 Internal Server Error
			throw new WebApplicationException(500);
		}
	
		return builder.build();
	}
	
	/**
	 * Delete an Audit Event Resource
	 * @param auditEventKey
	 * @return Response
	 * @throws WebApplicationException
	 */
	@DELETE
	@Path ("/event/{auditEventKey}")
	public Response deleteResource(@PathParam("auditEventKey") 
	String auditEventKey) 
			throws WebApplicationException {
		
		//delete the audit event
		AuditingServices.deleteAuditEvent(auditEventKey);
		
		return Response.status(Response.Status.ACCEPTED).build();
	}	
}