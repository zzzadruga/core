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
package com.sun.xacml.cond;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.attr.BooleanAttribute;
import com.sun.xacml.attr.IntegerAttribute;
import com.sun.xacml.cond.xacmlv3.EvaluationResult;
import com.sun.xacml.cond.xacmlv3.Expression;


/**
 * A class that implements the n-of function. It requires
 * at least one argument. The first argument must be an integer
 * and the rest of the arguments must be booleans. If the number of
 * boolean arguments that evaluate to true is at least the value of the
 * first argument, the function returns true. Otherwise, it returns false
 * (or indeterminate, as described in the next paragraph.
 * <p>
 * This function evaluates the arguments one at a time, starting with
 * the first one. As soon as the result of the function can be determined,
 * evaluation stops and that result is returned. During this process, if
 * any argument evaluates to indeterminate, an indeterminate result is
 * returned.
 *
 * @since 1.0
 * @author Steve Hanne
 * @author Seth Proctor
 */
public class NOfFunction extends FunctionBase
{

    /**
     * Standard identifier for the n-of function.
     */
    public static final String NAME_N_OF = FUNCTION_NS + "n-of";

    /**
     * Creates a new <code>NOfFunction</code> object.
     *
     * @param functionName the standard XACML name of the function to be
     *                     handled by this object, including the full namespace
     *
     * @throws IllegalArgumentException if the function is unknown
     */
    public NOfFunction(String functionName) {
        super(NAME_N_OF, 0, BooleanAttribute.identifier, false);

        if (! functionName.equals(NAME_N_OF))
            throw new IllegalArgumentException("unknown nOf function: "
                                               + functionName);
    }

    /**
     * Returns a <code>Set</code> containing all the function identifiers
     * supported by this class.
     *
     * @return a <code>Set</code> of <code>String</code>s
     */
    public static Set getSupportedIdentifiers() {
        Set set = new HashSet();

        set.add(NAME_N_OF);

        return set;
    }

    /**
     * Evaluate the function, using the specified parameters.
     *
     * @param inputs a <code>List</code> of <code>Evaluatable</code>
     *               objects representing the arguments passed to the function
     * @param context an <code>EvaluationCtx</code> so that the
     *                <code>Evaluatable</code> objects can be evaluated
     * @return an <code>EvaluationResult</code> representing the
     *         function's result
     */
    public EvaluationResult evaluate(List inputs, EvaluationCtx context) {

        // Evaluate the arguments one by one. As soon as we can return
        // a result, do so. Return  Indeterminate if any argument
        // evaluated is indeterminate.
        Iterator it = inputs.iterator();
        Evaluatable eval = (Evaluatable)(it.next());

        // Evaluate the first argument
        EvaluationResult result = eval.evaluate(context);
        if (result.indeterminate())
            return result;

        // if there were no problems, we know 'n'
        long n = ((IntegerAttribute)(result.getAttributeValue())).getValue();

        // If the number of trues needed is less than zero, report an error.
        if (n < 0)
            return makeProcessingError("First argument to " + getFunctionName()
                                       + " cannot be negative.");

        // If the number of trues needed is zero, return true.
        if (n == 0)
            return EvaluationResult.getTrueInstance();

        // make sure it's possible to find n true values
        long remainingArgs = inputs.size() - 1;
        if (n > remainingArgs)
            return makeProcessingError("not enough arguments to n-of to " +
                                       "find " + n + " true values");

        // loop through the inputs, trying to find at least n trues
        while (remainingArgs >= n) {
            eval = (Evaluatable)(it.next());
            
            // evaluate the next argument
            result = eval.evaluate(context);
            if (result.indeterminate())
                return result;
            
            // get the next value, and see if it's true
            if (((BooleanAttribute)(result.getAttributeValue())).getValue()) {
                // we're one closer to our goal...see if we met it
                if (--n == 0)
                    return EvaluationResult.getTrueInstance();
            }

            // we're still looking, but we've got one fewer arguments
            remainingArgs--;
        }

        // if we got here then we didn't meet our quota
        return EvaluationResult.getFalseInstance();
    }

    /**
     *
     */
    public void checkInputs(List inputs) throws IllegalArgumentException {
        // check that none of the inputs is a bag
        Object [] list = inputs.toArray();
        for (int i = 0; i < list.length; i++)
            if (((Evaluatable)(list[i])).evaluatesToBag())
                throw new IllegalArgumentException("n-of can't use bags");

        // if we got here then there were no bags, so ask the other check
        // method to finish the checking
        checkInputsNoBag(inputs);
    }

    /**
     *
     */
    public void checkInputsNoBag(List inputs) throws IllegalArgumentException {
        Object [] list = inputs.toArray();
        
        // check that there is at least one arg
        if (list.length == 0)
            throw new IllegalArgumentException("n-of requires an argument");

        // check that the first element is an Integer
        Expression eval = (Expression)(list[0]);
        if (! eval.getType().toString().equals(IntegerAttribute.identifier))
            throw new IllegalArgumentException("first argument to n-of must" +
                                               " be an integer");
        
        // now check that the rest of the args are booleans
        for (int i = 1; i < list.length; i++) {
            if (! ((Expression)(list[i])).getType().toString().
                equals(BooleanAttribute.identifier))
                throw new IllegalArgumentException("invalid parameter in n-of"
                                                   + ": expected boolean");
        }
    }

}
