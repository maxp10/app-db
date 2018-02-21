package com.sorint.openshift;

import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RequestScoped
@Path("/db")
public class ServiceApi1 {

	@GET()
	@Produces("application/json")
	public HashMap<String, String> getInsult() {
		HashMap<String, String> theInsult = new HashMap<String, String>();
		theInsult.put("DB generate: ", new ServiceDb().generate());
		return theInsult;
	}
}
