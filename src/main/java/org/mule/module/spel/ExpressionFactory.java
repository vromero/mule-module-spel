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
package org.mule.module.spel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mule.api.lifecycle.Disposable;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

public class ExpressionFactory implements Disposable 
{
	// SpEL parser instances are reusable and thread-safe.
	protected final static ExpressionParser PARSER = new SpelExpressionParser();
		
	Map<String, Expression> expressions = new ConcurrentHashMap<String, Expression>(4);
	
	public Expression create(String expression)
	{
		Assert.hasLength(expression, "Expression cannot be null or empty");
			
		Expression exp = expressions.get(expression);
		if(exp==null)
		{
			exp = PARSER.parseExpression(expression);
			expressions.put(expression,  exp);
	    }
	        
	    return exp;
	}

	public void dispose() 
	{
		expressions.clear();
	}
}
