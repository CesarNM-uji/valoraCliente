package cliente;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;


public class AuxiliarValoracionesWS {

	// URI del recurso que permite acceder al servicio casa de subastas
	final private String baseURI = "http://localhost:8080/ValoraWS/servicios/valoraciones";
	Client cliente = null;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor de la clase
	 * Crea el cliente
	 */
	public AuxiliarValoracionesWS()  {
		this.cliente = ClientBuilder.newClient();
	}

	/**
	 * Cuando cada cliente cierra su sesión volcamos los datos en el fichero para mantenerlos actualizados
	 */
	public void guardaDatos() {
		Response response = cliente.target(baseURI).request().put(Entity.text(""));
		int estado = response.getStatus();
		if ( estado == 200) {
			// Devuelve la id del nuevo contacto a partir de la información
			// sobre el URI del nuevo recurso en la cabecera 'Location' de la respuesta
			JSONArray jsonArray = response.readEntity(JSONArray.class);
			response.close();
		} else {  response.close();
			throw new WebApplicationException("Error al guardar los datos");
		}
	}

	/**
	 * Devuelve los nombres de los restaurantes
	 *
	 * @return JSONArray de restaurantes. Vacío si no hay ningún restaurante
	 * @throws	WebApplicationException estado no controlado por el cliente (200 o 404)
	 */
	public JSONArray listaRestaurantes() throws WebApplicationException {
		// POR IMPLEMENTAR

		Response response = cliente.target(baseURI).request().get();
		int estado = response.getStatus();
		if ( estado == 200) {
			// Devuelve la id del nuevo contacto a partir de la información
			// sobre el URI del nuevo recurso en la cabecera 'Location' de la respuesta

			JSONArray jsonArray = response.readEntity(JSONArray.class);
			response.close();
			return jsonArray;
		} else if (estado == 404) {response.close();return new JSONArray();

		} else {  response.close();
			throw new WebApplicationException("error al listar los restuarantes");
		}





	}

	/**
	 * Devuelve las valoraciones de un restaurante dado
	 *
	 * @param nomRest	nombre del restaurante
	 * @return JSONArray de valoraciones del restaurante. Vacío si no hay valoraciones de ese restaurante
	 * @throws	WebApplicationException	estado no controlado por el cliente (200 o 404)
	 */
	public JSONArray consultaValoraciones(String nomRest) throws WebApplicationException {
		Response response = cliente.target(baseURI).path(""+nomRest)
				.request().get();

		int estado = response.getStatus();
		if ( estado == 200) {
			JSONArray jsonArray = response.readEntity(JSONArray.class);
			response.close();
			return jsonArray;
		}else if (estado == 404) {
			return new JSONArray();
		}else{
			throw new WebApplicationException("Error al consultar la valoración del restaurante: " + nomRest );
	}



	}

	/**
	 * El cliente codcliente hace una valoración
	 *
	 * @param codcliente	código del cliente que hace la valoración
	 * @param nomRest		nombre del restaurante
	 * @param fecha			fecha en que se hace la valoración
	 * @param estrellas		estrellas otorgadas
	 * @param comentario	comentario con la opinion del cliente
	 * @return JSONObject con la información de la valoración. Vacío si ya hay una para el mismo restaurante y fecha
	 * @throws	WebApplicationException	estado no controlado por el cliente (200 o 404)
	 */
	public JSONObject hazValoracion(String codcliente, String nomRest, String fecha, long estrellas, String comentario) {
		Response response = cliente.target(baseURI).path(""+codcliente).path(""+nomRest).queryParam("fecha"+fecha).queryParam("estrellas"+estrellas).queryParam("comentario"+comentario).request().post(Entity.text(""));
		int estado = response.getStatus();
		if ( estado == 201) {
			JSONObject jsonObject = response.readEntity(JSONObject.class);
			response.close();
			return jsonObject;
		}else if (estado == 404) {
			return new JSONObject();
		}else{
			throw new WebApplicationException("Error al consultar la valoración del restaurante: " + nomRest );
		}

	}

	/**
	 * El cliente codcli borra una valoracion
	 *
	 * @param codcliente	código del cliente que borra la valoración
	 * @param nomRest		nombre del restaurante
	 * @param fecha			fecha de la valoración a borrar
	 * @return	JSON de la valoración borrada. JSON vacío si no se ha borrado
	 * @throws	WebApplicationException	estado no controlado por el cliente (200 o 404)
	 */
	public JSONObject borraValoracion(String codcliente, String nomRest, String fecha) {
		Response response = cliente.target(baseURI).path(""+codcliente).path(""+nomRest).queryParam("fecha"+fecha).request().delete();
		int estado = response.getStatus();
		if ( estado == 200) {
			JSONObject jsonObject = response.readEntity(JSONObject.class);
			response.close();
			return jsonObject;
		}else if (estado == 404) {
			return new JSONObject();
		}else{
			throw new WebApplicationException("Error al borrar la valoracion del restaurante: " + nomRest );
		}

	}

	/**
	 * El cliente codcliente modifica las estrellas y/o el comentario de una valoración propia
	 *
	 * @param codcliente	código del cliente que modifica su valoración
	 * @param nomRest		nombre del restaurante
	 * @param fecha			fecha en que se realizo la valoración
	 * @param estrellas		estrellas otorgadas
	 * @param comentario	comentario con la opinion del cliente
	 * @return	JSON de la valoración modificada. JSON vacío si no se ha modificado
	 * @throws	WebApplicationException	estado no controlado por el cliente (200 o 404)
	 */
	public JSONObject modificaValoracion(String codcliente, String nomRest, String fecha, long estrellas, String comentario) {
		Response response = cliente.target(baseURI).path(""+codcliente).path(""+nomRest).queryParam("fecha"+fecha).queryParam("estrellas"+estrellas).queryParam("comentario"+comentario).request().put(Entity.text(""));
		int estado = response.getStatus();
		if ( estado == 201) {
			JSONObject jsonObject = response.readEntity(JSONObject.class);
			response.close();
			return jsonObject;
		}else if (estado == 404) {
			return new JSONObject();
		}else{
			throw new WebApplicationException("Error al consultar la valoración del restaurante: " + nomRest );
		}

	}


} // fin clase
