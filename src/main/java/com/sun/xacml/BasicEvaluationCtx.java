<<<<<<< HEAD
/*
 * @(#)BasicEvaluationCtx.java
 *
 * Copyright 2004-2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistribution of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 * 
 *   2. Redistribution in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 */

package com.sun.xacml;

import com.sun.xacml.attr.AttributeDesignator;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.BagAttribute;
import com.sun.xacml.attr.DateAttribute;
import com.sun.xacml.attr.DateTimeAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.attr.TimeAttribute;

import com.sun.xacml.cond.EvaluationResult;
import com.sun.xacml.ctx.Attribute;

import com.sun.xacml.finder.AttributeFinder;

import java.net.URI;
import java.net.URISyntaxException;
=======
/**
 * Copyright (C) 2011-2013 Thales Services - ThereSIS - All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.sun.xacml;

import java.net.URI;
>>>>>>> 3.x
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
<<<<<<< HEAD

import java.util.logging.Level;
import java.util.logging.Logger;
=======
import java.util.logging.Level;

>>>>>>> 3.x
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
<<<<<<< HEAD
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.w3c.dom.Document;

import org.w3c.dom.Node;

=======

import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributeValueType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AttributesType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.RequestType;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.xacml.attr.BagAttribute;
import com.sun.xacml.attr.DateAttribute;
import com.sun.xacml.attr.DateTimeAttribute;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.attr.TimeAttribute;
import com.sun.xacml.attr.xacmlv3.AttributeDesignator;
import com.sun.xacml.attr.xacmlv3.AttributeValue;
import com.sun.xacml.cond.xacmlv3.EvaluationResult;
import com.sun.xacml.finder.AttributeFinder;
import com.thalesgroup.authzforce.xacml.schema.XACMLAttributeId;

>>>>>>> 3.x
/**
 * A basic implementation of <code>EvaluationCtx</code> that is created from an
 * XACML Request and falls back on an AttributeFinder if a requested value isn't
 * available in the Request.
 * <p>
 * Note that this class can do some optional caching for current date, time, and
 * dateTime values (defined by a boolean flag to the constructors). The XACML
 * specification requires that these values always be available, but it does not
 * specify whether or not they must remain constant over the course of an
 * evaluation if the values are being generated by the PDP (if the values are
 * provided in the Request, then obviously they will remain constant). The
 * default behavior is for these environment values to be cached, so that (for
 * example) the current time remains constant over the course of an evaluation.
 * 
 * @since 1.2
 * @author Seth Proctor
 */
public class BasicEvaluationCtx implements EvaluationCtx {
	// the finder to use if a value isn't in the request
	private AttributeFinder finder;
	RequestType request = null;
	Node requestRoot = null;

	// the 4 maps that contain the attribute data
	private Map<String, Map<String, Set<AttributeType>>> subjectMap;
	private Map<String, Set<AttributeType>> resourceMap;
	private Map<String, Set<AttributeType>> actionMap;
	private Map<String, Set<AttributeType>> environmentMap;
<<<<<<< HEAD

	// the resource and its scope
	private AttributeValue resourceId;
	private int scope;

=======
	private Map<String, Set<AttributeType>> customMap;

	// Attributes that needs to be included in the result
	private List<AttributesType> includeInResults;

	// the resource and its scope
	private List<AttributeValue> resourceId;
	private int scope;

	// Version of the standard used: 1.0, 1.1, 2.0 or 3.0
	private int version;

>>>>>>> 3.x
	// the cached current date, time, and datetime, which we may or may
	// not be using depending on how this object was constructed
	private DateAttribute currentDate;
	private TimeAttribute currentTime;
	private DateTimeAttribute currentDateTime;
	private boolean useCachedEnvValues;

<<<<<<< HEAD
	// the logger we'll use for all messages
	private static final Logger logger = Logger
			.getLogger(BasicEvaluationCtx.class.getName());
=======
	/**
	 * Logger used for all classes
	 */
	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(BasicEvaluationCtx.class);
>>>>>>> 3.x

	/**
	 * Constructs a new <code>BasicEvaluationCtx</code> based on the given
	 * request. The resulting context will cacheManager current date, time, and
	 * dateTime values so they remain constant for this evaluation.
	 * 
	 * @param request
	 *            the request
	 * 
	 * @throws ParsingException
	 *             if a required attribute is missing, or if there are any
	 *             problems dealing with the request data
<<<<<<< HEAD
	 */
	public BasicEvaluationCtx(RequestType request) throws ParsingException {
		this(request, null, true);
=======
	 * @throws UnknownIdentifierException
	 * @throws NumberFormatException
	 */
	public BasicEvaluationCtx(RequestType request) throws ParsingException,
			NumberFormatException, UnknownIdentifierException {
		this(request, null, true, Integer
				.parseInt(XACMLAttributeId.XACML_VERSION_3_0.value()));
>>>>>>> 3.x
	}

	/**
	 * Constructs a new <code>BasicEvaluationCtx</code> based on the given
	 * request.
	 * 
	 * @param request
	 *            the request
	 * @param cacheEnvValues
	 *            whether or not to cacheManager the current time, date, and
	 *            dateTime so they are constant for the scope of this evaluation
	 * 
	 * @throws ParsingException
	 *             if a required attribute is missing, or if there are any
	 *             problems dealing with the request data
<<<<<<< HEAD
	 */
	public BasicEvaluationCtx(RequestType request, boolean cacheEnvValues)
			throws ParsingException {
		this(request, null, cacheEnvValues);
=======
	 * @throws UnknownIdentifierException
	 */
	public BasicEvaluationCtx(RequestType request, boolean cacheEnvValues,
			int version) throws ParsingException, UnknownIdentifierException {
		this(request, null, cacheEnvValues, version);
>>>>>>> 3.x
	}

	/**
	 * Constructs a new <code>BasicEvaluationCtx</code> based on the given
	 * request, and supports looking outside the original request for attribute
	 * values using the <code>AttributeFinder</code>. The resulting context will
	 * cacheManager current date, time, and dateTime values so they remain
	 * constant for this evaluation.
	 * 
	 * @param request
	 *            the request
	 * @param finder
	 *            an <code>AttributeFinder</code> to use in looking for
	 *            attributes that aren't in the request
	 * 
	 * @throws ParsingException
	 *             if a required attribute is missing, or if there are any
	 *             problems dealing with the request data
<<<<<<< HEAD
	 */
	public BasicEvaluationCtx(RequestType request, AttributeFinder finder)
			throws ParsingException {
		this(request, finder, true);
=======
	 * @throws UnknownIdentifierException
	 */
	public BasicEvaluationCtx(RequestType request, AttributeFinder finder,
			int version) throws ParsingException, UnknownIdentifierException {
		this(request, finder, true, version);
>>>>>>> 3.x
	}

	/**
	 * Constructs a new <code>BasicEvaluationCtx</code> based on the given
	 * request, and supports looking outside the original request for attribute
	 * values using the <code>AttributeFinder</code>.
	 * 
	 * @param request
	 *            the request
	 * @param finder
	 *            an <code>AttributeFinder</code> to use in looking for
	 *            attributes that aren't in the request
	 * @param cacheEnvValues
	 *            whether or not to cacheManager the current time, date, and
	 *            dateTime so they are constant for the scope of this evaluation
	 * 
	 * @throws ParsingException
	 *             if a required attribute is missing, or if there are any
	 *             problems dealing with the request data
<<<<<<< HEAD
	 */
	public BasicEvaluationCtx(RequestType request, AttributeFinder finder,
			boolean cacheEnvValues) throws ParsingException {
=======
	 * @throws UnknownIdentifierException
	 */
	public BasicEvaluationCtx(RequestType request, AttributeFinder finder,
			boolean cacheEnvValues, int version) throws ParsingException,
			UnknownIdentifierException {
>>>>>>> 3.x

		this.request = request;

		// keep track of the finder
		this.finder = finder;

		// initialize the cached date/time values so it's clear we haven't
		// retrieved them yet
		this.useCachedEnvValues = cacheEnvValues;
		currentDate = null;
		currentTime = null;
		currentDateTime = null;

<<<<<<< HEAD
		// get the subjects, make sure they're correct, and setup tables
		subjectMap = new HashMap();
		setupSubjects(request.getSubject());

		// next look at the Resource data, which needs to be handled specially
		resourceMap = new HashMap();
		setupResources(request.getResource());

		// setup the action data, which is generic
		actionMap = new HashMap();
		mapAttributes(request.getAction().getAttribute(), actionMap);

		// finally, set up the environment data, which is also generic
		environmentMap = new HashMap();
		mapAttributes(request.getEnvironment().getAttribute(), environmentMap);
=======
		this.version = version;
		actionMap = new HashMap();
		resourceMap = new HashMap();
		subjectMap = new HashMap();
		environmentMap = new HashMap();
		customMap = new HashMap();
		
		for (AttributesType myAttributeTypes : request.getAttributes()) {
			/* Searching for action */
			if (myAttributeTypes.getCategory().equalsIgnoreCase(
					XACMLAttributeId.XACML_3_0_ACTION_CATEGORY_ACTION.value())) {
				mapAttributes(myAttributeTypes.getAttribute(), actionMap);
			}
			/* Searching for resource */
			else if (myAttributeTypes.getCategory().equalsIgnoreCase(
					XACMLAttributeId.XACML_3_0_RESOURCE_CATEGORY_RESOURCE
							.value())) {
				// next look at the Resource data, which needs to be handled
				// specially
				setupResources(myAttributeTypes.getAttribute());
			}/* Searching for subject */
			else if (myAttributeTypes.getCategory()
					.equalsIgnoreCase(
							XACMLAttributeId.XACML_1_0_SUBJECT_CATEGORY_SUBJECT
									.value())) {
				// get the subjects, make sure they're correct, and setup tables
				setupSubjects(myAttributeTypes.getAttribute());

			}/* Searching for environment */
			else if (myAttributeTypes.getCategory().equalsIgnoreCase(
					XACMLAttributeId.XACML_3_0_ENVIRONMENT_CATEGORY_ENVIRONMENT
							.value())) {
				// finally, set up the environment data, which is also generic
				mapAttributes(myAttributeTypes.getAttribute(), environmentMap);
			}
			// Attribute category didn't match any known category so we store
			// the attributes in an custom list
			else {
				// finally, set up the environment data, which is also generic
				mapAttributes(myAttributeTypes.getAttribute(), customMap);
			}
			// Store attributes who needs to be included in the result
			for (AttributeType attr : myAttributeTypes.getAttribute()) {
				storeAttrIncludeInResult(attr, myAttributeTypes.getCategory());
			}
		}
>>>>>>> 3.x
	}

	/**
	 * This is quick helper function to provide a little structure for the
	 * subject attributes so we can search for them (somewhat) quickly. The
	 * basic idea is to have a map indexed by SubjectCategory that keeps Maps
	 * that in turn are indexed by id and keep the unique ctx.Attribute objects.
	 */
<<<<<<< HEAD
	private void setupSubjects(List<SubjectType> subjects)
			throws ParsingException {
		// make sure that there is at least one Subject
		if (subjects.size() == 0)
			throw new ParsingException("Request must a contain subject");

		// now go through the subject attributes
		for (SubjectType subject : subjects) {
			String category = subject.getSubjectCategory();
=======
	private void setupSubjects(List<AttributeType> subjects)
			throws ParsingException {
		// make sure that there is at least one Subject
		if (subjects.size() == 0) {
			throw new ParsingException("Request must a contain subject");
		}
		/*
		 * FIXME: RF
		 */
		// now go through the subject attributes
		for (AttributeType subject : subjects) {
			String category = XACMLAttributeId.XACML_1_0_SUBJECT_CATEGORY_SUBJECT
					.value();
>>>>>>> 3.x
			Map<String, Set<AttributeType>> categoryMap = null;

			// see if we've already got a map for the category
			if (subjectMap.containsKey(category)) {
				categoryMap = subjectMap.get(category);
			} else {
				categoryMap = new HashMap();
				subjectMap.put(category, categoryMap);
			}

			// iterate over the set of attributes
<<<<<<< HEAD
			List<AttributeType> subjectAttributes = subject.getAttribute();
=======
			List<AttributeType> subjectAttributes = subjects;
>>>>>>> 3.x

			for (AttributeType attr : subjectAttributes) {
				String id = attr.getAttributeId();

				if (categoryMap.containsKey(id)) {
					// add to the existing set of Attributes w/this id
					Set<AttributeType> attributes = categoryMap.get(id);
					attributes.add(attr);
				} else {
					// this is the first Attr w/this id
					Set<AttributeType> attributes = new HashSet<AttributeType>();
					attributes.add(attr);
					categoryMap.put(id, attributes);
				}
			}
		}
	}

	/**
	 * This basically does the same thing that the other types need to do,
	 * except that we also look for a resource-id attribute, not because we're
	 * going to use, but only to make sure that it's actually there, and for the
	 * optional scope attribute, to see what the scope of the attribute is
<<<<<<< HEAD
	 */
	private void setupResources(List<ResourceType> resources)
			throws ParsingException {
		if (resources.size() != 1) {
			// TODO
			throw new ParsingException(
					"XACML 2.0 support for multiple resources not yet supported");
		}
		ResourceType resource = resources.get(0);
		mapAttributes(resource.getAttribute(), resourceMap);
=======
	 * 
	 * @throws UnknownIdentifierException
	 */
	private void setupResources(List<AttributeType> list)
			throws ParsingException, UnknownIdentifierException {
		mapAttributes(list, resourceMap);
>>>>>>> 3.x

		// make sure there resource-id attribute was included
		if (!resourceMap.containsKey(RESOURCE_ID)) {
			System.err.println("Resource must contain resource-id attr");
			throw new ParsingException("resource missing resource-id");
		} else {
<<<<<<< HEAD
			// make sure there's only one value for this
			Set<AttributeType> set = resourceMap.get(RESOURCE_ID);
			if (set.size() > 1) {
				System.err.println("Resource may contain only one "
						+ "resource-id Attribute");
				throw new ParsingException("too many resource-id attrs");
			} else {
				try {
					// keep track of the resource-id attribute
					AttributeType attr = set.iterator().next();
					/*
					 * FIXME: possible NPE, check if
					 * attr.getAttributeValue().get(0) exists
					 */
					resourceId = AttributeValue.convertFromJAXB(
							attr.getAttributeValue(),
							new URI(attr.getDataType())).get(0);
				} catch (URISyntaxException ex) {
					throw new RuntimeException(ex);
				}
=======
			if (resourceId == null) {
				resourceId = new ArrayList<AttributeValue>();
			}
			resourceId.clear();
			for (AttributeType attributeType : resourceMap.get(RESOURCE_ID)) {
				resourceId.addAll(AttributeValue.convertFromJAXB(attributeType
						.getAttributeValue()));
>>>>>>> 3.x
			}
		}

		// see if a resource-scope attribute was included
		if (resourceMap.containsKey(RESOURCE_SCOPE)) {
			Set<AttributeType> set = resourceMap.get(RESOURCE_SCOPE);

			// make sure there's only one value for resource-scope
			if (set.size() > 1) {
				System.err.println("Resource may contain only one "
						+ "resource-scope Attribute");
				throw new ParsingException("too many resource-scope attrs");
			}

			AttributeType attr = set.iterator().next();
<<<<<<< HEAD
			AttributeValueType attrValue = attr.getAttributeValue().get(0); // TODO:
																			// XACML2
																			// can
																			// have
																			// multiple
																			// AttributeValues
																			// in
																			// an
																			// Attribute

			// scope must be a string, so throw an exception otherwise
			if (!attr.getDataType().equals(StringAttribute.identifier))
				throw new ParsingException("scope attr must be a string");
=======
			AttributeValueType attrValue = attr.getAttributeValue().get(0);
			// scope must be a string, so throw an exception otherwise
			if (!attrValue.getDataType().equals(StringAttribute.identifier)) {
				throw new ParsingException("scope attr must be a string");
			}
>>>>>>> 3.x

			String value = attrValue.getContent().get(0).toString();

			if (value.equals("Immediate")) {
				scope = SCOPE_IMMEDIATE;
			} else if (value.equals("Children")) {
				scope = SCOPE_CHILDREN;
			} else if (value.equals("Descendants")) {
				scope = SCOPE_DESCENDANTS;
			} else {
<<<<<<< HEAD
				System.err.println("Unknown scope type: " + value);
=======
				LOGGER.error("Unknown scope type: " + value);
>>>>>>> 3.x
				throw new ParsingException("invalid scope type: " + value);
			}
		} else {
			// by default, the scope is always Immediate
			scope = SCOPE_IMMEDIATE;
		}
	}

<<<<<<< HEAD
=======
	private void storeAttrIncludeInResult(AttributeType attr, String category) {
		if (includeInResults == null) {
			includeInResults = new ArrayList<AttributesType>();
		}
		if (attr.isIncludeInResult()) {
			boolean alreadyPresent = false;
			AttributesType myAttr = new AttributesType();
			myAttr.getAttribute().add(attr);
			myAttr.setCategory(category);
			for (AttributesType attrs : includeInResults) {
				if (attrs.getCategory().equalsIgnoreCase(category)) {
					alreadyPresent = true;
					attrs.getAttribute().add(attr);
					break;
				}
			}
			if (!alreadyPresent) {
				includeInResults.add(myAttr);
			}
		}
	}

>>>>>>> 3.x
	/**
	 * Generic routine for resource, attribute and environment attributes to
	 * build the lookup map for each. The Form is a Map that is indexed by the
	 * String form of the attribute ids, and that contains Sets at each entry
	 * with all attributes that have that id
	 */
	private void mapAttributes(List<AttributeType> attributes,
			Map<String, Set<AttributeType>> map) throws ParsingException {
		for (AttributeType attribute : attributes) {
			String id = attribute.getAttributeId();

			if (id == null) {
				throw new ParsingException(
						"No AttributeId defined for attribute");
			}

			if (map.containsKey(id)) {
				Set<AttributeType> set = (Set<AttributeType>) (map.get(id));
				set.add(attribute);
			} else {
				Set<AttributeType> set = new HashSet<AttributeType>();
				set.add(attribute);
				map.put(id, set);
			}
		}
	}

	/**
<<<<<<< HEAD
=======
	 * @return the request
	 */
	public RequestType getRequest() {
		return request;
	}

	/**
>>>>>>> 3.x
	 * Returns the resource scope of the request, which will be one of the three
	 * fields denoting Immediate, Children, or Descendants.
	 * 
	 * @return the scope of the resource in the request
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * Returns the resource named in the request as resource-id.
	 * 
<<<<<<< HEAD
	 * @return the resource
	 */
	public AttributeValue getResourceId() {
		return resourceId;
=======
	 * @return the resourceMap
	 */
	public Map<String, Set<AttributeType>> getResourceMap() {
		return resourceMap;
	}

	/**
	 * Returns the resource named in the request as resource-id. Using
	 * resourceId as a pointer to the evaluated resource
	 * 
	 * @return the resource
	 */
	public AttributeValue getResourceId() {
		if (resourceId != null && resourceId.size() >= 1) {
			return resourceId.get(0);
		} else {
			return null;
		}
	}

	public List<AttributesType> getIncludeInResults() {
		return includeInResults;
>>>>>>> 3.x
	}

	/**
	 * Changes the value of the resource-id attribute in this context. This is
	 * useful when you have multiple resources (ie, a scope other than
	 * IMMEDIATE), and you need to keep changing only the resource-id to
	 * evaluate the different effective requests.
	 * 
	 * @param resourceId
	 *            the new resource-id value
	 */
	public void setResourceId(AttributeValue resourceId) {
<<<<<<< HEAD
		this.resourceId = resourceId;
=======
		this.resourceId.add(resourceId);
>>>>>>> 3.x
		// there will always be exactly one value for this attribute
		Set<AttributeType> attrSet = resourceMap.get(RESOURCE_ID);
		AttributeType attr = (AttributeType) (attrSet.iterator().next());
		AttributeValueType attrValue = attr.getAttributeValue().get(0);
		attrValue.getContent().clear();
		attrValue.getContent().add(resourceId.encode());
	}

<<<<<<< HEAD
=======
	public void setResourceId(List<AttributeValue> resourceId) {
		for (AttributeValue avts : resourceId) {
			this.setResourceId(avts);
		}
	}

>>>>>>> 3.x
	/**
	 * Returns the value for the current time. The current time, current date,
	 * and current dateTime are consistent, so that they all represent the same
	 * moment. If this is the first time that one of these three values has been
	 * requested, and caching is enabled, then the three values will be resolved
	 * and stored.
	 * <p>
	 * Note that the value supplied here applies only to dynamically resolved
	 * values, not those supplied in the Request. In other words, this always
	 * returns a dynamically resolved value local to the PDP, even if a
	 * different value was supplied in the Request. This is handled correctly
	 * when the value is requested by its identifier.
	 * 
	 * @return the current time
	 */
	public synchronized TimeAttribute getCurrentTime() {
		long millis = dateTimeHelper();

<<<<<<< HEAD
		if (useCachedEnvValues)
			return currentTime;
		else
			return new TimeAttribute(new Date(millis));
=======
		if (useCachedEnvValues) {
			return currentTime;
		} else {
			return new TimeAttribute(new Date(millis));
		}
>>>>>>> 3.x
	}

	/**
	 * Returns the value for the current date. The current time, current date,
	 * and current dateTime are consistent, so that they all represent the same
	 * moment. If this is the first time that one of these three values has been
	 * requested, and caching is enabled, then the three values will be resolved
	 * and stored.
	 * <p>
	 * Note that the value supplied here applies only to dynamically resolved
	 * values, not those supplied in the Request. In other words, this always
	 * returns a dynamically resolved value local to the PDP, even if a
	 * different value was supplied in the Request. This is handled correctly
	 * when the value is requested by its identifier.
	 * 
	 * @return the current date
	 */
	public synchronized DateAttribute getCurrentDate() {
		long millis = dateTimeHelper();

<<<<<<< HEAD
		if (useCachedEnvValues)
			return currentDate;
		else
			return new DateAttribute(new Date(millis));
=======
		if (useCachedEnvValues) {
			return currentDate;
		} else {
			return new DateAttribute(new Date(millis));
		}
>>>>>>> 3.x
	}

	/**
	 * Returns the value for the current dateTime. The current time, current
	 * date, and current dateTime are consistent, so that they all represent the
	 * same moment. If this is the first time that one of these three values has
	 * been requested, and caching is enabled, then the three values will be
	 * resolved and stored.
	 * <p>
	 * Note that the value supplied here applies only to dynamically resolved
	 * values, not those supplied in the Request. In other words, this always
	 * returns a dynamically resolved value local to the PDP, even if a
	 * different value was supplied in the Request. This is handled correctly
	 * when the value is requested by its identifier.
	 * 
	 * @return the current dateTime
	 */
	public synchronized DateTimeAttribute getCurrentDateTime() {
		long millis = dateTimeHelper();

<<<<<<< HEAD
		if (useCachedEnvValues)
			return currentDateTime;
		else
			return new DateTimeAttribute(new Date(millis));
=======
		if (useCachedEnvValues) {
			return currentDateTime;
		} else {
			return new DateTimeAttribute(new Date(millis));
		}
>>>>>>> 3.x
	}

	/**
	 * Private helper that figures out if we need to resolve new values, and
	 * returns either the current moment (if we're not caching) or -1 (if we are
	 * caching)
	 */
	private long dateTimeHelper() {
		// if we already have current values, then we can stop (note this
		// always means that we're caching)
<<<<<<< HEAD
		if (currentTime != null)
			return -1;
=======
		if (currentTime != null) {
			return -1;
		}
>>>>>>> 3.x

		// get the current moment
		Date time = new Date();
		long millis = time.getTime();

		// if we're not caching then we just return the current moment
		if (!useCachedEnvValues) {
			return millis;
		} else {
			// we're caching, so resolve all three values, making sure
			// to use clean copies of the date object since it may be
			// modified when creating the attributes
			currentTime = new TimeAttribute(time);
			currentDate = new DateAttribute(new Date(millis));
			currentDateTime = new DateTimeAttribute(new Date(millis));
		}

		return -1;
	}

	/**
	 * Returns attribute value(s) from the subject section of the request that
	 * have no issuer.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param category
	 *            the category the attribute value(s) must be in
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getSubjectAttribute(URI type, URI id, URI category) {
		return getSubjectAttribute(type, id, null, category);
	}

	/**
	 * Returns attribute value(s) from the subject section of the request.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param issuer
	 *            the issuer of the attribute value(s) to find or null
	 * @param category
	 *            the category the attribute value(s) must be in
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getSubjectAttribute(URI type, URI id, URI issuer,
			URI category) {
		// This is the same as the other three lookups except that this
		// has an extra level of indirection that needs to be handled first
<<<<<<< HEAD
		Map<String, Set<AttributeType>> map = subjectMap.get(category
				.toString());
=======
		Map<String, Set<AttributeType>> map = null;
		if (subjectMap != null) {
			map = subjectMap.get(category.toString());
		}
>>>>>>> 3.x

		if (map == null) {
			// the request didn't have that category, so we should try asking
			// the attribute finder
			return callHelper(type, id, issuer, category,
					AttributeDesignator.SUBJECT_TARGET);
		}

		return getGenericAttributes(type, id, issuer, map, category,
				AttributeDesignator.SUBJECT_TARGET);
	}

	/**
	 * Returns attribute value(s) from the resource section of the request.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param issuer
	 *            the issuer of the attribute value(s) to find or null
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getResourceAttribute(URI type, URI id, URI issuer) {
		return getGenericAttributes(type, id, issuer, resourceMap, null,
				AttributeDesignator.RESOURCE_TARGET);
	}

	/**
	 * Returns attribute value(s) from the action section of the request.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param issuer
	 *            the issuer of the attribute value(s) to find or null
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getActionAttribute(URI type, URI id, URI issuer) {
		return getGenericAttributes(type, id, issuer, actionMap, null,
				AttributeDesignator.ACTION_TARGET);
	}

	/**
	 * Returns attribute value(s) from the environment section of the request.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param issuer
	 *            the issuer of the attribute value(s) to find or null
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getEnvironmentAttribute(URI type, URI id, URI issuer) {
		return getGenericAttributes(type, id, issuer, environmentMap, null,
				AttributeDesignator.ENVIRONMENT_TARGET);
	}
<<<<<<< HEAD

	
	/**
     * Helper function for the resource, action and environment methods
     * to get an attribute.
     */
    private EvaluationResult getGenericAttributes(URI type, URI id, URI issuer,
                                                  Map<String, Set<AttributeType>> map, URI category,
                                                  int designatorType) {
        // try to find the id
        Set<AttributeType> attrSet = map.get(id.toString());
        if (attrSet == null) {
            // the request didn't have an attribute with that id, so we should
            // try asking the attribute finder
            return callHelper(type, id, issuer, category, designatorType);
        }

        // now go through each, considering each Attribute object
        List<AttributeValue> attributeValues = new ArrayList();
        Iterator it = attrSet.iterator();

        for (AttributeType attr: attrSet) {
            // make sure the type and issuer are correct
            if ((attr.getDataType().equals(type.toString())) &&
                ((issuer == null) ||
                 ((attr.getIssuer() != null) &&
                  (attr.getIssuer().equals(issuer.toString()))))) {
                try {
                    // if we got here, then we found a match, so we want to pull
                    // out the values and put them in out list
//                    attributeValues.add(AttributeValue.convertFromJAXB(attr.getAttributeValue(), type));
                	final List<AttributeValue> newAttrVals = AttributeValue.convertFromJAXB(attr.getAttributeValue(), type);
					attributeValues.addAll(newAttrVals);
                } catch (ParsingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        // see if we found any acceptable attributes
        if (attributeValues.size() == 0) {
            // we failed to find any that matched the type/issuer, or all the
            // Attribute types were empty...so ask the finder
            if (logger.isLoggable(Level.FINE))
                logger.fine("Attribute not in request: " + id.toString() +
                            " ... querying AttributeFinder");

            /*
             * Code Updated [romain.guignard[at]thalesgroup.com]
             * Allow to store value retrieves by an attributeFinder for a specific evaluationContext
             * see also http://jira.theresis.thales:8180/browse/APPSEC-167
             */
            EvaluationResult result = callHelper(type, id, issuer, category, designatorType);
            String issuerString = null;
            if (issuer != null){
            	issuerString = issuer.toString();
            }
            if (result.getAttributeValue() instanceof BagAttribute){
            	BagAttribute bagAttribute = (BagAttribute) result.getAttributeValue();
            	Iterator iit = bagAttribute.getValue().iterator();
            	HashSet<AttributeType> attrset = new HashSet<AttributeType>();
                
            	 while (iit.hasNext()) {
            		 AttributeValueType attributeValue = (AttributeValueType) iit.next();
            		 AttributeType attribute = new AttributeType();
            		 attribute.setAttributeId(id.toASCIIString());
            		 attribute.setIssuer(issuerString);
            		 /*
            		  * TODO: fetch datatype
            		  */            
            		 attribute.setDataType(null);
            		 attribute.getAttributeValue().add(attributeValue);            		 
            		 attrset.add(attribute);
            	 }
            	 map.put(id.toString(), attrset);
            	 return result;
            }
            else {
            	throw new IllegalArgumentException("CallHelper didn't return BagAttribute");
            }                         
            /*
             * 
             */
        }
                
        // if we got here, then we found at least one useful AttributeValue
        return new EvaluationResult(new BagAttribute(type, attributeValues));
    }

=======
	
	/**
	 * Returns attribute value(s) from the environment section of the request.
	 * 
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param id
	 *            the id of the attribute value(s) to find
	 * @param issuer
	 *            the issuer of the attribute value(s) to find or null
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getCustomAttribute(URI type, URI id, URI issuer) {
		return getGenericAttributes(type, id, issuer, customMap, null,
				-1);
	}

	/**
	 * Helper function for the resource, action and environment methods to get
	 * an attribute.
	 */
	private EvaluationResult getGenericAttributes(URI type, URI id, URI issuer,
			Map<String, Set<AttributeType>> map, URI category,
			int designatorType) {
		// try to find the id
		Set<AttributeType> attrSet = map.get(id.toString());
		if (attrSet == null) {
			// the request didn't have an attribute with that id, so we should
			// try asking the attribute finder
			return callHelper(type, id, issuer, category, designatorType);
		}

		// now go through each, considering each Attribute object
		List<AttributeValue> attributeValues = new ArrayList();
		Iterator it = attrSet.iterator();

		for (AttributeType attr : attrSet) {
			for (AttributeValueType attributeValue : attr.getAttributeValue()) {
				// make sure the type and issuer are correct
				if ((attributeValue.getDataType().equals(type.toString()))
						&& ((issuer == null) || ((attr.getIssuer() != null) && (attr
								.getIssuer().equals(issuer.toString()))))) {
					// if we got here, then we found a match, so we want to
					// pull
					// out the values and put them in out list
					try {
						attributeValues.addAll(AttributeValue
								.convertFromJAXB(attr.getAttributeValue()));
					} catch (ParsingException e) {
						LOGGER.error(e);
					} catch (UnknownIdentifierException e) {
						LOGGER.error(e);
					}
				}
			}
		}

		// see if we found any acceptable attributes
		if (attributeValues.size() == 0) {
			// we failed to find any that matched the type/issuer, or all the
			// Attribute types were empty...so ask the finder
			LOGGER.debug("Attribute not in request: " + id.toString()
					+ " ... querying AttributeFinder");

			/*
			 * Code Updated [romain.guignard[at]thalesgroup.com] Allow to store
			 * value retrieves by an attributeFinder for a specific
			 * evaluationContext see also
			 * http://jira.theresis.thales:8180/browse/APPSEC-167
			 */
			EvaluationResult result = callHelper(type, id, issuer, category,
					designatorType);
			String issuerString = null;
			if (issuer != null) {
				issuerString = issuer.toString();
			}
			if (result.getAttributeValue() instanceof BagAttribute) {
				BagAttribute bagAttribute = (BagAttribute) result
						.getAttributeValue();
				Iterator iit = bagAttribute.getValue().iterator();
				HashSet<AttributeType> attrset = new HashSet<AttributeType>();

				int i = 0;
				while (iit.hasNext()) {
					AttributeValueType attributeValue = (AttributeValueType) iit
							.next();
					AttributeType attribute = new AttributeType();
					attribute.setAttributeId(id.toASCIIString());
					attribute.setIssuer(issuerString);
					attribute.getAttributeValue().add(attributeValue);
					attribute.getAttributeValue().get(i)
							.setDataType(type.toASCIIString());
					attrset.add(attribute);
					i++;
				}
				i = 0;
				map.put(id.toString(), attrset);
				return result;
			} else {
				throw new IllegalArgumentException(
						"CallHelper didn't return BagAttribute");
			}
			/*
             * 
             */
		}

		// if we got here, then we found at least one useful AttributeValue
		return new EvaluationResult(new BagAttribute(type, attributeValues));
	}
>>>>>>> 3.x

	/**
	 * Private helper that calls the finder if it's non-null, or else returns an
	 * empty bag
	 */
	private EvaluationResult callHelper(URI type, URI id, URI issuer,
			URI category, int adType) {
		if (finder != null) {
			return finder.findAttribute(type, id, issuer, category, this,
					adType);
		} else {
<<<<<<< HEAD
			logger.warning("Context tried to invoke AttributeFinder but was "
=======
			LOGGER.warn("Context tried to invoke AttributeFinder but was "
>>>>>>> 3.x
					+ "not configured with one");

			return new EvaluationResult(BagAttribute.createEmptyBag(type));
		}
	}

	/**
	 * Returns the attribute value(s) retrieved using the given XPath
	 * expression.
	 * 
	 * @param contextPath
	 *            the XPath expression to search
	 * @param namespaceNode
	 *            the DOM node defining namespace mappings to use, or null if
	 *            mappings come from the context root
	 * @param type
	 *            the type of the attribute value(s) to find
	 * @param xpathVersion
	 *            the version of XPath to use
	 * 
	 * @return a result containing a bag either empty because no values were
	 *         found or containing at least one value, or status associated with
	 *         an Indeterminate result
	 */
	public EvaluationResult getAttribute(String contextPath,
			Node namespaceNode, URI type, String xpathVersion) {
		if (finder != null) {
			return finder.findAttribute(contextPath, namespaceNode, type, this, // XXX
					xpathVersion);
		} else {
<<<<<<< HEAD
			logger.warning("Context tried to invoke AttributeFinder but was "
=======
			LOGGER.warn("Context tried to invoke AttributeFinder but was "
>>>>>>> 3.x
					+ "not configured with one");

			return new EvaluationResult(BagAttribute.createEmptyBag(type));
		}
	}

	public Node getRequestRoot() {
		try {
			if (requestRoot == null) {
				Marshaller m = BindingUtility.createMarshaller();
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.newDocument();
				JAXBElement<RequestType> element = BindingUtility.contextFac
						.createRequest(request);
				m.marshal(element, doc);
				requestRoot = doc.getDocumentElement();
			}
		} catch (JAXBException ex) {
			throw new RuntimeException(ex);
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}

		return requestRoot;
	}

<<<<<<< HEAD
=======
	public int getVersion() {
		return version;
	}
>>>>>>> 3.x
}
