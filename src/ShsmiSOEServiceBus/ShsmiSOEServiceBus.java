package ShsmiSOEServiceBus;

/*
COPYRIGHT 1995-2012 ESRI
TRADE SECRETS: ESRI PROPRIETARY AND CONFIDENTIAL
Unpublished material - all rights reserved under the 
Copyright Laws of the United States and applicable international
laws, treaties, and conventions.
 
For additional information, contact:
Environmental Systems Research Institute, Inc.
Attn: Contracts and Legal Services Department
380 New York Street
Redlands, California, 92373
USA
 
email: contracts@esri.com
*/
import java.io.IOException;
import java.net.UnknownHostException;

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.interop.extn.ArcGISExtension;
import com.esri.arcgis.server.IServerObjectExtension;
import com.esri.arcgis.server.IServerObjectHelper;
import com.esri.arcgis.system.ILog;
import com.esri.arcgis.system.ServerUtilities;
import com.esri.arcgis.interop.extn.ServerObjectExtProperties;
import com.esri.arcgis.carto.ILayerDescriptions;
import com.esri.arcgis.carto.IMapLayerInfos;
import com.esri.arcgis.carto.IMapServer3;
import com.esri.arcgis.carto.IMapServerDataAccess;
import com.esri.arcgis.carto.IMapServerInfo;
import com.esri.arcgis.carto.IMapServerInfo3;
import com.esri.arcgis.carto.LayerDescription;
import com.esri.arcgis.carto.LayerResultOptions;
import com.esri.arcgis.carto.QueryResult;
import com.esri.arcgis.carto.QueryResultOptions;
import com.esri.arcgis.carto.esriQueryResultFormat;
import com.esri.arcgis.geodatabase.FeatureClass;
import com.esri.arcgis.geodatabase.FeatureCursor;
import com.esri.arcgis.geodatabase.Field;
import com.esri.arcgis.geodatabase.Fields;
import com.esri.arcgis.geodatabase.GeometryResultOptions;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.RecordSet;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.esriFeatureType;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.server.json.JSONArray;
import com.esri.arcgis.server.json.JSONException;
import com.esri.arcgis.server.json.JSONObject;
import com.esri.arcgis.system.IRESTRequestHandler;
import java.util.HashMap;

@ArcGISExtension
@ServerObjectExtProperties(displayName = "ShsmiSOEServiceBus", description = "ShsmiSOEServiceBus", defaultSOAPCapabilities = {
		"findqxjdjwhinformationbylocation" }, allSOAPCapabilities = { "findqxjdjwhinformationbylocation" })

public class ShsmiSOEServiceBus implements IServerObjectExtension, IRESTRequestHandler {
    private static final long serialVersionUID = 1L;
    private IServerObjectHelper soHelper;
	private ILog serverLog;
	private IMapServerDataAccess mapServerDataAccess;

	public ShsmiSOEServiceBus() throws Exception {
		super();
	}

	/****************************************************************************************************************************
	 * IServerObjectExtension methods:
	 * This is a mandatory interface that must be supported by all SOEs. 
	 * This interface is used by the Server Object to manage the lifecycle of the SOE and includes 
	 * two methods: init() and shutdown(). 
	 * The Server Object cocreates the SOE and calls the init() method handing it a back reference 
	 * to the Server Object via the Server Object Helper argument. The Server Object Helper implements 
	 * a weak reference on the Server Object. The extension can keep a strong reference on the Server 
	 * Object Helper (for example, in a member variable) but should not. 
	 *    
	 * The log entries are merely informative and completely optional. 
	 ****************************************************************************************************************************/
	/**
	 * init() is called once, when the instance of the SOE is created. 
	 */
	public void init(IServerObjectHelper soh) throws IOException, AutomationException {
		/*
		  * An SOE should retrieve a weak reference to the Server Object from the Server Object Helper in
		  * order to make any method calls on the Server Object and release the
		  * reference after making the method calls.
		 */
		this.soHelper = soh;
		this.serverLog = ServerUtilities.getServerLogger();
		this.mapServerDataAccess = (IMapServerDataAccess) soh.getServerObject();
		this.serverLog.addMessage(3, 200, "Initialized " + this.getClass().getName() + " SOE.");
	}

	/**
	 * shutdown() is called once when the Server Object's context is being shut down and is 
	 * about to go away.
	 */
	public void shutdown() throws IOException, AutomationException {
		/*
		 * The SOE should release its reference on the Server Object Helper.
		 */
		this.serverLog.addMessage(3, 200, "Shutting down " + this.getClass().getName() + " SOE.");
		this.soHelper = null;
		this.serverLog = null;
		this.mapServerDataAccess = null;
	}

	/**
	* Method for implementing REST operation "findqxjdjwhinformationbylocation"'s functionality.
	* @param String operationInput JSON representation of input
	* @return String JSON representation of output
	*/
	private byte[] findqxjdjwhinformationbylocationRESTop(JSONObject operationInput, String outputFormat,
			JSONObject requestPropertiesJSON, java.util.Map<String, String> responsePropertiesMap) throws Exception {
		//Please set output format and response properties for this operation, as appropriate.
		JSONObject locJSON = operationInput.getJSONObject("location");
		Point location = new Point();
		location.setX(locJSON.getDouble("x"));
		location.setY(locJSON.getDouble("y"));
		SpatialReferenceEnvironment sre = new SpatialReferenceEnvironment();
		location.setSpatialReferenceByRef(sre.createSpatialReference(Integer
			.parseInt("102100")));
		
		responsePropertiesMap.put("Content-Type", "application/json");
		
		return findqxjdjwhinformationbylocation(location).toString().getBytes("utf-8");
	}
	
	 private JSONObject findqxjdjwhinformationbylocation(Point location) throws Exception {
		 
			IMapServer3 ms =  (IMapServer3) this.mapServerDataAccess;
			IMapServerInfo mapServerInfo = ms.getServerInfo(ms.getDefaultMapName());
			String mapName = ms.getDefaultMapName();
			//String jwhName=null;
			//String jdname= null;
			JSONObject jsonxq = new JSONObject();
			FeatureClass fcxq = new FeatureClass(this.mapServerDataAccess.getDataSource(mapName, 2));
			if (fcxq.getFeatureType() == esriFeatureType.esriFTSimple) {
				if(location!=null)
				{
				SpatialFilter spatialFilter = new SpatialFilter();
				spatialFilter.setGeometryByRef(location);
				spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
				
				QueryResultOptions xqqResultOptions = new QueryResultOptions();
				xqqResultOptions.setFormat(esriQueryResultFormat.esriQueryResultRecordSetAsObject);

				QueryResult queryResult = (QueryResult) ms.queryData(mapName,getTableDescription(mapServerInfo, 2),spatialFilter, xqqResultOptions);
				RecordSet xqrs = (RecordSet) queryResult.getObject();

				
				FeatureCursor xqcursor = new FeatureCursor(xqrs.getCursor(true));
				IFeature xqfeature = xqcursor.nextFeature();
				
				if (xqfeature != null) {
					Fields fields = (Fields) xqfeature.getFields();
				    int fieldCount = fields.getFieldCount();
				    while (xqfeature != null) {
				    	for (int i = 2; i < fieldCount; i++) {
							  Field field = (Field) fields.getField(i);
							  String fieldName = field.getName();
							  jsonxq.put(fieldName, xqfeature.getValue(fields.findField(fieldName)));
						  }
				     //jwhName = feature.getValue(fcjwh.findField("jwhname")).toString();
				    	xqfeature = xqcursor.nextFeature();
				    }
				}
			   }
			}
			
			JSONObject jsonjd = new JSONObject();
			FeatureClass fcjd= new FeatureClass(this.mapServerDataAccess.getDataSource(mapName, 1));
			if (fcjd.getFeatureType() == esriFeatureType.esriFTSimple) {
				if(location!=null)
				{
				SpatialFilter jdspatialFilter = new SpatialFilter();
				jdspatialFilter.setGeometryByRef(location);
				jdspatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
				
				QueryResultOptions jdqResultOptions = new QueryResultOptions();
				jdqResultOptions.setFormat(esriQueryResultFormat.esriQueryResultRecordSetAsObject);

				QueryResult jdqueryResult = (QueryResult) ms.queryData(mapName,getTableDescription(mapServerInfo, 1),jdspatialFilter, jdqResultOptions);
				RecordSet jdrs = (RecordSet) jdqueryResult.getObject();

				FeatureCursor jdcursor = new FeatureCursor(jdrs.getCursor(true));
				IFeature jdfeature = jdcursor.nextFeature();
				
				if (jdfeature != null) {
					Fields fields = (Fields) jdfeature.getFields();
				    int fieldCount = fields.getFieldCount();
				    
				    while (jdfeature != null) { 
				    	
					for (int i = 2; i < fieldCount; i++) {
						  Field field = (Field) fields.getField(i);
						  String fieldName = field.getName();
						  jsonjd.put(fieldName, jdfeature.getValue(fields.findField(fieldName)));
					  }
                  //jdname = jdfeature.getValue(fcjd.findField("jdname")).toString();
				     jdfeature = jdcursor.nextFeature();
				    }
				}
			    }
			}
			
			JSONObject jsonjwh = new JSONObject();
			FeatureClass fcjwh = new FeatureClass(this.mapServerDataAccess.getDataSource(mapName, 0));
			if (fcjwh.getFeatureType() == esriFeatureType.esriFTSimple) {
				if(location!=null)
				{
				SpatialFilter spatialFilter = new SpatialFilter();
				spatialFilter.setGeometryByRef(location);
				spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
				
				QueryResultOptions qResultOptions = new QueryResultOptions();
				qResultOptions.setFormat(esriQueryResultFormat.esriQueryResultRecordSetAsObject);

				QueryResult queryResult = (QueryResult) ms.queryData(mapName,getTableDescription(mapServerInfo, 0),spatialFilter, qResultOptions);
				RecordSet rs = (RecordSet) queryResult.getObject();

				
				FeatureCursor jwhcursor = new FeatureCursor(rs.getCursor(true));
				IFeature jwhfeature = jwhcursor.nextFeature();
				
				if (jwhfeature != null) {
					Fields fields = (Fields) jwhfeature.getFields();
				    int fieldCount = fields.getFieldCount();
				    while (jwhfeature != null) {
				    	for (int i = 2; i < fieldCount; i++) {
							  Field field = (Field) fields.getField(i);
							  String fieldName = field.getName();
							  jsonjwh.put(fieldName, jwhfeature.getValue(fields.findField(fieldName)));
						  }
				     //jwhName = feature.getValue(fcjwh.findField("jwhname")).toString();
				    	jwhfeature = jwhcursor.nextFeature();
				    }
				}
			   }
			}

			
			JSONObject json = new JSONObject();
			json.put("xq", jsonxq);
			json.put("jd", jsonjd);
			json.put("jwh", jsonjwh);
			return json;
  }

	 
	 private LayerDescription getTableDescription(IMapServerInfo mapServerInfo,int layerID) {
			try {
			    IMapLayerInfos layerInfos = mapServerInfo.getMapLayerInfos();
			    IMapServerInfo3 mapServerInfo3 = (IMapServerInfo3) mapServerInfo;
			    ILayerDescriptions layerDescriptions = mapServerInfo3
				    .getDefaultMapDescription().getLayerDescriptions();
			    int ldCount = layerInfos.getCount();
			    for (int i = 0; i < ldCount; i++) {
				LayerDescription layerDescription = (LayerDescription) layerDescriptions
					.getElement(i);

				if (layerDescription.getID() == layerID) {
				    LayerResultOptions resultOptions = new LayerResultOptions();

				    GeometryResultOptions geomResultOptions = new GeometryResultOptions();
				    geomResultOptions.setDensifyGeometries(true);

				    resultOptions
					    .setGeometryResultOptionsByRef(geomResultOptions);

				    layerDescription.setLayerResultOptionsByRef(resultOptions);

				    return layerDescription;
				}
			    }
			} catch (AutomationException e) {
			    e.printStackTrace();
			} catch (UnknownHostException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			return null;
		    }
	/**
	* Returns JSON representation of the root resource.
	* @return String JSON representation of the root resource.
	*/
	private byte[] getRootResource(String outputFormat, JSONObject requestPropertiesJSON,
			java.util.Map<String, String> responsePropertiesMap) throws Exception {
		JSONObject json = new JSONObject();
		json.put("name", "root resource");
		json.put("description", "上海测绘院SOE 服务");
		return json.toString().getBytes("utf-8");
	}

	/**
	* Returns JSON representation of specified resource.
	* @return String JSON representation of specified resource.
	*/
	private byte[] getResource(String capabilitiesList, String resourceName, String outputFormat,
			JSONObject requestPropertiesJSON, java.util.Map<String, String> responsePropertiesMap) throws Exception {
		if (resourceName.equalsIgnoreCase("") || resourceName.length() == 0) {
			return getRootResource(outputFormat, requestPropertiesJSON, responsePropertiesMap);
		}

		return null;
	}

	/**
	* Invokes specified REST operation on specified REST resource
	* @param capabilitiesList
	* @param resourceName
	* @param operationName
	* @param operationInput
	* @param outputFormat
	* @param requestPropertiesMap
	* @param responsePropertiesMap
	* @return byte[]
	*/
	private byte[] invokeRESTOperation(String capabilitiesList, String resourceName, String operationName,
			String operationInput, String outputFormat, JSONObject requestPropertiesJSON,
			java.util.Map<String, String> responsePropertiesMap) throws Exception {
		JSONObject operationInputAsJSON = new JSONObject(operationInput);
		byte[] operationOutput = null;

		//permitted capabilities list can be used to allow/block access to operations

		if (resourceName.equalsIgnoreCase("") || resourceName.length() == 0) {
			if (operationName.equalsIgnoreCase("findqxjdjwhinformationbylocation")) {
				operationOutput = findqxjdjwhinformationbylocationRESTop(operationInputAsJSON, outputFormat,
						requestPropertiesJSON, responsePropertiesMap);
			}
		} else //if non existent sub-resource specified, report error
		{
			return ServerUtilities
					.sendError(0, "No sub-resource by name " + resourceName + " found.", new String[] { "" })
					.getBytes("utf-8");
		}

		return operationOutput;
	}

	/**
	 * Handles REST request by determining whether an operation or resource has been invoked and then forwards the
	 * request to appropriate Java methods, along with request and response properties
	 */
	public byte[] handleRESTRequest(String capabilities, String resourceName, String operationName,
			String operationInput, String outputFormat, String requestProperties, String[] responseProperties)
			throws IOException, AutomationException {
		// parse request properties, create a map to hold request properties
		JSONObject requestPropertiesJSON = new JSONObject(requestProperties);

		// create a response properties map to hold properties of response
		java.util.Map<String, String> responsePropertiesMap = new HashMap<String, String>();

		try {
			// if no operationName is specified send description of specified
			// resource
			byte[] response;
			if (operationName.length() == 0) {
				response = getResource(capabilities, resourceName, outputFormat, requestPropertiesJSON,
						responsePropertiesMap);
			} else
			// invoke REST operation on specified resource
			{
				response = invokeRESTOperation(capabilities, resourceName, operationName, operationInput, outputFormat,
						requestPropertiesJSON, responsePropertiesMap);
			}

			// handle response properties
			JSONObject responsePropertiesJSON = new JSONObject(responsePropertiesMap);
			responseProperties[0] = responsePropertiesJSON.toString();

			return response;
		} catch (Exception e) {
			String message = "Exception occurred while handling REST request for SOE " + this.getClass().getName() + ":"
					+ e.getMessage();
			this.serverLog.addMessage(1, 500, message);
			return ServerUtilities.sendError(0, message, null).getBytes("utf-8");
		}
	}

	/**
	 * This method returns the resource hierarchy of a REST based SOE in JSON format.
	 */
	public String getSchema() throws IOException, AutomationException {
		try {
			JSONObject _ShsmiSOEServiceBus = ServerUtilities.createResource("ShsmiSOEServiceBus", "上海测绘院SOE 服务", false,
					false);
			JSONArray _ShsmiSOEServiceBus_OpArray = new JSONArray();
			_ShsmiSOEServiceBus_OpArray.put(
					ServerUtilities.createOperation("findqxjdjwhinformationbylocation", "location", "json", false));
			_ShsmiSOEServiceBus.put("operations", _ShsmiSOEServiceBus_OpArray);
			return _ShsmiSOEServiceBus.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

}