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
package com.sun.xacml.finder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.xacmlv3.AttributeValue;

/**
 * This class is used by the PDP to handle resource scopes other than Immediate.
 * In the case of a scope of Children or Descendants, the PDP needs a list of
 * Resource Ids to evaluate, each of which will get its own Result. Like the
 * PolicyFinder, this is not tied in any way to the rest of the PDP code, and
 * could be provided as a stand-alone resource.
 * <p>
 * This class basically is a coordinator that asks each module in turn if it can
 * handle the given identifier. Evaluation proceeds in order through the given
 * modules, and once a module returns a non-empty response (whether or not it
 * contains any errors or only errors), the evaluation is finished and the
 * result is returned. One of the issues here is ordering, since a given
 * resource may look to several modules like something that they can handle. So,
 * you must be careful when assigning to ordering of the modules in this finder.
 * <p>
 * Note that in release 1.2 the interfaces were updated to include the
 * evaluation context. In the next major release the interfaces without the
 * context information will be removed, but for now both exist. This means that
 * if this finder is called with the context, then only the methods in
 * <code>ResourceFinderModule</code> supporting the context will be called (and
 * likewise only the methods without context will be called when this finder is
 * called without the context). In practice this means that the methods with
 * context will always get invoked, since this is what the default PDP
 * implementation calls.
 * 
 * @since 1.0
 * @author Seth Proctor
 */
public class ResourceFinder {

	// the list of all modules
	private List allModules;

	// the list of child modules
	private List childModules;

	// the list of descendant modules
	private List descendantModules;

	// the logger we'll use for all messages
	private static final Logger logger = Logger.getLogger(ResourceFinder.class
			.getName());

	/**
	 * Default constructor.
	 */
	public ResourceFinder() {
		allModules = new ArrayList();
		childModules = new ArrayList();
		descendantModules = new ArrayList();
	}

	/**
	 * Returns the ordered <code>List</code> of
	 * <code>ResourceFinderModule</code>s used by this class to find resources.
	 * 
	 * @return a <code>List</code> of <code>ResourceFinderModule</code>s
	 */
	public List getModules() {
		return new ArrayList(allModules);
	}

	/**
	 * Sets the ordered <code>List</code> of <code>ResourceFinderModule</code>s
	 * used by this class to find resources. The ordering will be maintained.
	 * 
	 * @param modules
	 *            a code>List</code> of <code>ResourceFinderModule</code>s
	 */
	public void setModules(List modules) {
		Iterator it = modules.iterator();

		allModules = new ArrayList(modules);
		childModules = new ArrayList();
		descendantModules = new ArrayList();

		while (it.hasNext()) {
			ResourceFinderModule module = (ResourceFinderModule) (it.next());

			if (module.isChildSupported()) {
				childModules.add(module);
			}

			if (module.isDescendantSupported()) {
				descendantModules.add(module);
			}
		}
	}

	/**
	 * Finds Resource Ids using the Children scope, and returns all resolved
	 * identifiers as well as any errors that occurred. If no modules can handle
	 * the given Resource Id, then an empty result is returned.
	 * 
	 * @param parentResourceId
	 *            the root of the resources
	 * @param context
	 *            the representation of the request data
	 * 
	 * @return the result of looking for child resources
	 */
	public ResourceFinderResult findChildResources(
			AttributeValue parentResourceId, EvaluationCtx context) {
		Iterator it = childModules.iterator();

		while (it.hasNext()) {
			ResourceFinderModule module = (ResourceFinderModule) (it.next());

			// ask the module to find the resources
			ResourceFinderResult result = module.findChildResources(
					parentResourceId, context);

			// if we found something, then always return that result
			if (!result.isEmpty()) {
				return result;
			}
		}

		// no modules applied, so we return an empty result
		if (logger.isLoggable(Level.INFO))
			logger.info("No ResourceFinderModule existed to handle the "
					+ "children of " + parentResourceId.encode());

		return new ResourceFinderResult();
	}

	/**
	 * Finds Resource Ids using the Children scope, and returns all resolved
	 * identifiers as well as any errors that occurred. If no modules can handle
	 * the given Resource Id, then an empty result is returned.
	 * 
	 * @deprecated As of version 1.2, replaced by
	 *             {@link #findChildResources(AttributeValue,EvaluationCtx)}.
	 *             This version does not provide the evaluation context to the
	 *             modules, and will be removed in a future release.
	 * 
	 * @param parentResourceId
	 *            the root of the resources
	 * 
	 * @return the result of looking for child resources
	 */
	public ResourceFinderResult findChildResources(
			AttributeValue parentResourceId) {
		Iterator it = childModules.iterator();

		while (it.hasNext()) {
			ResourceFinderModule module = (ResourceFinderModule) (it.next());

			// ask the module to find the resources
			ResourceFinderResult result = module
					.findChildResources(parentResourceId);

			// if we found something, then always return that result
			if (!result.isEmpty())
				return result;
		}

		// no modules applied, so we return an empty result
		if (logger.isLoggable(Level.INFO))
			logger.info("No ResourceFinderModule existed to handle the "
					+ "children of " + parentResourceId.encode());

		return new ResourceFinderResult();
	}

	/**
	 * Finds Resource Ids using the Descendants scope, and returns all resolved
	 * identifiers as well as any errors that occurred. If no modules can handle
	 * the given Resource Id, then an empty result is returned.
	 * 
	 * @param parentResourceId
	 *            the root of the resources
	 * @param context
	 *            the representation of the request data
	 * 
	 * @return the result of looking for descendant resources
	 */
	public ResourceFinderResult findDescendantResources(
			AttributeValue parentResourceId, EvaluationCtx context) {
		Iterator it = descendantModules.iterator();

		while (it.hasNext()) {
			ResourceFinderModule module = (ResourceFinderModule) (it.next());

			// ask the module to find the resources
			ResourceFinderResult result = module.findDescendantResources(
					parentResourceId, context);

			// if we found something, then always return that result
			if (!result.isEmpty())
				return result;
		}

		// no modules applied, so we return an empty result
		if (logger.isLoggable(Level.INFO))
			logger.info("No ResourceFinderModule existed to handle the "
					+ "descendants of " + parentResourceId.encode());

		return new ResourceFinderResult();
	}

	/**
	 * Finds Resource Ids using the Descendants scope, and returns all resolved
	 * identifiers as well as any errors that occurred. If no modules can handle
	 * the given Resource Id, then an empty result is returned.
	 * 
	 * @deprecated As of version 1.2, replaced by
	 *             {@link #findDescendantResources(AttributeValue,EvaluationCtx)}
	 *             . This version does not provide the evaluation context to the
	 *             modules, and will be removed in a future release.
	 * 
	 * @param parentResourceId
	 *            the root of the resources
	 * 
	 * @return the result of looking for child resources
	 */
	public ResourceFinderResult findDescendantResources(
			AttributeValue parentResourceId) {
		Iterator it = descendantModules.iterator();

		while (it.hasNext()) {
			ResourceFinderModule module = (ResourceFinderModule) (it.next());

			// ask the module to find the resources
			ResourceFinderResult result = module
					.findDescendantResources(parentResourceId);

			// if we found something, then always return that result
			if (!result.isEmpty())
				return result;
		}

		// no modules applied, so we return an empty result
		if (logger.isLoggable(Level.INFO))
			logger.info("No ResourceFinderModule existed to handle the "
					+ "descendants of " + parentResourceId.encode());

		return new ResourceFinderResult();
	}

}
