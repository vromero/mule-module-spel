/*
 * Copyright (c) 2011, Victor Romero. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.mule.module.spel.expression;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.context.MuleContextAware;
import org.mule.api.expression.ExpressionEvaluator;
import org.mule.api.expression.ExpressionRuntimeException;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.config.i18n.CoreMessages;
import org.mule.module.spel.EvaluationContextFactory;
import org.mule.module.spel.ExpressionFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;


/**
 * An expression evaluator that uses SpEL as the expression language
 */
public class SpELExpressionEvaluator  implements ExpressionEvaluator, MuleContextAware, Initialisable
{

	public static final String NAME = "spel";
    
    /**
     * logger used by this class
     */
    protected transient final Log logger = LogFactory.getLog(SpELExpressionEvaluator.class);
    
    protected ExpressionFactory expressionFactory;
    
    protected EvaluationContextFactory contextFactory;
    
    protected MuleContext muleContext;
    
    @Override
	public void initialise() throws InitialisationException {
    	expressionFactory = new ExpressionFactory();
    	contextFactory = new EvaluationContextFactory(muleContext);
	}
    
	public String getName() 
	{
		return NAME;
	}

	public void setName(String arg0) 
	{
		throw new UnsupportedOperationException("setName");
	}

	public Object evaluate(String expression, MuleMessage muleMessage) 
	{
		
		if (StringUtils.isEmpty(expression)) 
		{
			return expression;
		}
		
		Expression exp;
        try
        {
			exp = expressionFactory.create(expression);
        }
        catch (Exception e)
        {
            throw new ExpressionRuntimeException(CoreMessages.expressionMalformed(expression,  NAME), e);
        }
        
        try
        {
        	EvaluationContext evaluationContext = contextFactory.create(muleMessage);
    		return exp.getValue(evaluationContext);
        }
        catch (Exception e)
        {
            //Only log the exceptions so that the behavior is consistent with the Expression API
            logger.warn(e.getMessage(), e);
            return null;
        }
	}

	@Override
	public void setMuleContext(MuleContext muleContext) {
		this.muleContext = muleContext;
	}

}
