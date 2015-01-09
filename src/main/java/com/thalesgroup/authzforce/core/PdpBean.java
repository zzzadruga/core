/**
 * Copyright (C) 2011-2015 Thales Services SAS.
 *
 * This file is part of AuthZForce.
 *
 * AuthZForce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthZForce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AuthZForce.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.thalesgroup.authzforce.core;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.Request;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SystemPropertyUtils;

import com.sun.xacml.PDP;
import com.sun.xacml.PDPConfig;
import com.sun.xacml.ParsingException;
import com.sun.xacml.UnknownIdentifierException;
import com.sun.xacml.ctx.ResponseCtx;

/**
 * JavaBean for the PDP to be used/called as JNDI resource.
 * 
 * In JEE application servers such as Glassfish, you could use class
 * org.glassfish.resources.custom.factory.JavaBeanFactory for registering the custom JNDI resource.
 * More info: http://docs.oracle.com/cd/E26576_01/doc.312/e24930/jndi.htm#giywi
 * 
 * For Tomcat, see http://tomcat.apache.org/tomcat-7.0-doc/jndi-resources-howto.html#
 * Adding_Custom_Resource_Factories.
 * 
 */
public class PdpBean
{
	private final static Logger LOGGER = LoggerFactory.getLogger(PdpBean.class);

	private PDP pdp;

	private String confLocation = null;

	private boolean initialized = false;

	private String extSchemaLocation = null;
	
	private String catalogLocation = null;

	/**
	 * @param request
	 *            XACML Request
	 * @return XACML Response
	 */
	public Response evaluate(Request request)
	{
		if (!initialized)
		{
			final String cause;
			if(confLocation == null) {
				cause = "Missing parameter: configuration file";
			} else if(extSchemaLocation == null) {
				cause = "Missing parameter: extension schema file";
			} else if(catalogLocation == null) {
				cause = "Missing parameter: XML catalog file";
			} else {
				cause = "Check previous errors.";
			}
			
			throw new RuntimeException("PDP not initialized: " + cause);
		}

		final ResponseCtx responseCtx = pdp.evaluate(request);
		// convert sunxacmlResp to JAXB Response type
		final Response jaxbResponse = new Response();
		jaxbResponse.getResults().addAll(responseCtx.getResults());
		return jaxbResponse;
	}

	/**
	 * Configuration file. Only the 'defaultPDP' configuration will be loaded, i.e. 'pdp' element
	 * with 'name' matching the 'defaultPDP' attribute of the root 'config' element
	 * 
	 * @param filePath
	 *            configuration file path used as argument to
	 *            {@link org.springframework.core.io.DefaultResourceLoader#getResource(String)} to
	 *            resolve the resource; any placeholder ${...} in the path will be replaced with the
	 *            corresponding system property value
	 * @throws JAXBException 
	 */
	public void setConfigFile(String filePath) throws JAXBException
	{
		confLocation = SystemPropertyUtils.resolvePlaceholders(filePath);
		init();
	}

	/**
	 * Configuration schema file. Used only for validating XML configurations (enclosed with 'xml'
	 * tag) of PDP extension modules in PDP configuration file set with
	 * {@link #setConfigFile(String)}
	 * 
	 * @param filePath
	 *            configuration file path used as argument to
	 *            {@link org.springframework.core.io.DefaultResourceLoader#getResource(String)} to
	 *            resolve the resource; any placeholder ${...} in the path will be replaced with the
	 *            corresponding system property value
	 * @throws JAXBException 
	 */
	public void setSchemaFile(String filePath) throws JAXBException
	{
		extSchemaLocation = SystemPropertyUtils.resolvePlaceholders(filePath);
		init();
	}
	
	/**
	 * Set XML catalog for resolving XML entities used in XML schema
	 * @param filePath
	 * @throws JAXBException
	 */
	public void setCatalogFile(String filePath) throws JAXBException {
		catalogLocation = SystemPropertyUtils.resolvePlaceholders(filePath);
		init();
	}

	/**
	 * 
	 * @return
	 * @throws JAXBException 
	 * @throws ParsingException
	 * @throws UnknownIdentifierException
	 */
	private boolean init()
	{
		if (!initialized && catalogLocation != null && extSchemaLocation != null && confLocation != null)
		{
			LOGGER.info("Loading PDP configuration from file {} with extension schema location '{}' and XML catalog location '{}'", new Object[] {confLocation, extSchemaLocation, catalogLocation});
//			try
//			{
				final PdpConfigurationManager confMgr;
				try
				{
					confMgr = new PdpConfigurationManager(confLocation, catalogLocation, extSchemaLocation);
				} catch (IOException e)
				{
					throw new RuntimeException("Error parsing PDP configuration from location: " + confLocation, e);
				} catch (JAXBException e)
				{
					throw new RuntimeException("Error parsing PDP configuration from location: " + confLocation, e);
				}
				
				final PDPConfig conf = confMgr.getDefaultPDPConfig();
				pdp = new PDP(conf);
//			} catch (ParsingException e)
//			{
//				throw new IllegalArgumentException("Error parsing PDP configuration from file '" + confLocation + "'", e);
//			} catch (UnknownIdentifierException e)
//			{
//				throw new IllegalArgumentException("No default PDP configuration defined in file '" + confLocation
//						+ "' (there should be one 'pdp' element with 'name' matching the 'defaultPDP' attribute of the root 'config' element)");
//			}

			initialized = true;
		}

		return initialized;
	}

}