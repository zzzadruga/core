/**
 * Copyright (C) 2012-2016 Thales Services SAS.
 *
 * This file is part of AuthZForce CE.
 *
 * AuthZForce CE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthZForce CE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AuthZForce CE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ow2.authzforce.core.pdp.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.Result;

import org.ow2.authzforce.core.pdp.api.AttributeGUID;
import org.ow2.authzforce.core.pdp.api.DecisionResult;
import org.ow2.authzforce.core.pdp.api.EvaluationContext;
import org.ow2.authzforce.core.pdp.api.IndividualDecisionRequest;
import org.ow2.authzforce.core.pdp.api.value.Bag;
import org.ow2.authzforce.core.pdp.impl.policy.RootPolicyEvaluator;

/**
 * Individual decision request evaluator
 *
 * @version $Id: $
 */
public abstract class IndividualDecisionRequestEvaluator
{

	private interface RequestAndPdpIssuedNamedAttributesMerger
	{
		Map<AttributeGUID, Bag<?>> merge(final Map<AttributeGUID, Bag<?>> pdpIssuedAttributes, final Map<AttributeGUID, Bag<?>> requestAttributes);
	}

	private static final RequestAndPdpIssuedNamedAttributesMerger REQUEST_OVERRIDES_ATTRIBUTES_MERGER = new RequestAndPdpIssuedNamedAttributesMerger()
	{

		@Override
		public Map<AttributeGUID, Bag<?>> merge(final Map<AttributeGUID, Bag<?>> pdpIssuedAttributes, final Map<AttributeGUID, Bag<?>> requestAttributes)
		{
			assert pdpIssuedAttributes != null && requestAttributes != null;

			/*
			 * Request attribute values override PDP issued ones Do not modify pdpIssuedAttributes directly as this may be used for other requests (Multiple Decision Profile) as well
			 */
			final Map<AttributeGUID, Bag<?>> mergedAttributes = new HashMap<>(pdpIssuedAttributes);
			mergedAttributes.putAll(requestAttributes);
			return mergedAttributes;
		}

	};

	private static final RequestAndPdpIssuedNamedAttributesMerger PDP_OVERRIDES_ATTRIBUTES_MERGER = new RequestAndPdpIssuedNamedAttributesMerger()
	{

		@Override
		public Map<AttributeGUID, Bag<?>> merge(final Map<AttributeGUID, Bag<?>> pdpIssuedAttributes, final Map<AttributeGUID, Bag<?>> requestAttributes)
		{
			assert pdpIssuedAttributes != null && requestAttributes != null;

			// PDP issued attribute values override request attribute values
			/*
			 * Do not modify pdpIssuedAttributes directly as this may be used for other requests (Multiple Decision Profile) as well
			 */
			final Map<AttributeGUID, Bag<?>> mergedAttributes = new HashMap<>(requestAttributes);
			mergedAttributes.putAll(pdpIssuedAttributes);
			return mergedAttributes;
		}

	};

	private final RootPolicyEvaluator rootPolicyEvaluator;
	private final RequestAndPdpIssuedNamedAttributesMerger reqAndPdpIssuedAttributesMerger;

	/**
	 * Creates an evaluator
	 *
	 * @param rootPolicyEvaluator
	 *            root policy evaluator that this request evaluator uses to evaluate individual decision request
	 * @param pdpStdTimeEnvOverrides
	 *            True iff the PDP's values for the standard environment attributes specified in §10.2.5 (current-time, current-date and current-dateTime) must always be set and override values from
	 *            the Request, if any. WARNING: note that the XACML standard (§10.2.5) says: "If values for these attributes are not present in the decision request, then their values MUST be supplied
	 *            by the context handler" but it does NOT say "If AND ONLY IF values..." So setting this flag to true could still be considered XACML compliant in a strict sense. Besides, what if the
	 *            decision request only specifies current-time but not current-dateTime, and the policy requires both? Should the PDP provides its own value for current-dateTime? This could cause some
	 *            inconsistencies since current-time and current-dateTime would come from two different sources/environments. So BEWARE.
	 */
	protected IndividualDecisionRequestEvaluator(final RootPolicyEvaluator rootPolicyEvaluator, final boolean pdpStdTimeEnvOverrides)
	{
		assert rootPolicyEvaluator != null;
		this.rootPolicyEvaluator = rootPolicyEvaluator;
		this.reqAndPdpIssuedAttributesMerger = pdpStdTimeEnvOverrides ? PDP_OVERRIDES_ATTRIBUTES_MERGER : REQUEST_OVERRIDES_ATTRIBUTES_MERGER;
	}

	/**
	 * <p>
	 * evaluate
	 * </p>
	 *
	 * @param request
	 *            a {@link org.ow2.authzforce.core.pdp.api.IndividualDecisionRequest} object.
	 * @param pdpIssuedAttributes
	 *            a {@link java.util.Map} of PDP-issued attributes including at least the standard environment attributes: current-time, current-date, current-dateTime.
	 * @param returnUsedAttributes
	 *            true iff the list of attributes used for evaluation must be included in the result
	 * @return a {@link oasis.names.tc.xacml._3_0.core.schema.wd_17.Result} object.
	 */
	protected final DecisionResult evaluate(final IndividualDecisionRequest request, final Map<AttributeGUID, Bag<?>> pdpIssuedAttributes, final boolean returnUsedAttributes)
	{
		assert request != null && pdpIssuedAttributes != null;

		// convert to EvaluationContext
		/*
		 * The pdpIssuedAttributes may be re-used for many individual requests, so we must not modify it but clone it before individual decision request processing
		 */
		final Map<AttributeGUID, Bag<?>> mergedNamedAttributes;
		final Map<AttributeGUID, Bag<?>> reqNamedAttributes = request.getNamedAttributes();
		if (reqNamedAttributes == null)
		{
			mergedNamedAttributes = new HashMap<>(pdpIssuedAttributes);
		}
		else
		{
			mergedNamedAttributes = reqAndPdpIssuedAttributesMerger.merge(pdpIssuedAttributes, reqNamedAttributes);
		}

		final EvaluationContext ctx = new IndividualDecisionRequestContext(mergedNamedAttributes, request.getExtraContentsByCategory(), request.isApplicablePolicyIdListReturned(),
				returnUsedAttributes);
		return rootPolicyEvaluator.findAndEvaluate(ctx);
	}

	/**
	 * <p>
	 * evaluate
	 * </p>
	 *
	 * @param individualDecisionRequests
	 *            a {@link java.util.List} of individual decision requests.
	 * @param pdpIssuedAttributes
	 *            a {@link java.util.Map} of PDP-issued attributes including at least the standard environment attributes: current-time, current-date, current-dateTime.
	 * @return a {@link java.util.List} of evaluation results (one per individual decision request).
	 */
	protected abstract <INDIVIDUAL_DECISION_REQ_T extends IndividualDecisionRequest> List<Result> evaluate(List<INDIVIDUAL_DECISION_REQ_T> individualDecisionRequests,
			final Map<AttributeGUID, Bag<?>> pdpIssuedAttributes);
}
