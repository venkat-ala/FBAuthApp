package common;

import java.io.Serializable;

import response.Response;

public class ServiceResponseUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Response serviceResponse;

	/**
	 * This method used to get dataResponse as output.
	 * 
	 * @param code
	 *            code
	 * @param description
	 *            description
	 * @param type
	 *            type
	 * @return serviceResponse
	 */
	public static Response dataResponse(String code, String description,
			Object type) {
		serviceResponse = new Response();
		serviceResponse.setStatusCode(code);
		serviceResponse.setDescription(description);
		serviceResponse.setData(type);
		return serviceResponse;

	}

}