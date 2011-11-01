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

import org.mule.api.MuleContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

/**
 * BeanResolver that relies on the Mule«s registry to resolve beans.
 *
 */
public class MuleRegistryBeanResolver implements BeanResolver 
{
	MuleContext context;
	
	public MuleRegistryBeanResolver(MuleContext context) 
	{
		this.context = context;
	}
	
	@Override
	public Object resolve(EvaluationContext evaluationContext, String beanname)
			throws AccessException 
	{
		return context.getRegistry().lookupObject(beanname);
	}
}
