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
import org.mule.api.MuleMessage;
import org.mule.api.registry.MuleRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

public class EvaluationContextFactory
{

	protected static final String CONTEXT_VAR_NAME = "context";
	
	protected static final String REGISTRY_VAR_NAME = "registry";
	
	protected static final String MESSAGE_VAR_NAME = "message";
	
	protected static final String ORIGINAL_PAYLOAD_VAR_NAME = "originalPayload";
	
	protected static final String PAYLOAD_VAR_NAME = "payload";
	
	protected static final String SYSTEM_PROPERTIES_VAR_NAME = "systemProperties";
	
	protected static final String ENVIRONMENT_VAR_NAME = "systemEnvironment";
	
	protected static final String ID_VAR_NAME = "id";
	
	protected MuleContext muleContext;
	
	protected MuleRegistry muleRegistry;
	
	protected MuleRegistryBeanResolver beanResolver;
	
	public EvaluationContextFactory(MuleContext muleContext) {
		this.muleContext = muleContext;
		muleRegistry = muleContext.getRegistry();
		beanResolver = new MuleRegistryBeanResolver(muleContext);
	}
	
	public EvaluationContext create(MuleMessage message)
    {
		Assert.notNull(message, "Message cannot be null");
		
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
    	
    	simpleContext.setVariable(CONTEXT_VAR_NAME, muleContext);
    	simpleContext.setVariable(REGISTRY_VAR_NAME, muleRegistry);
    	simpleContext.setVariable(MESSAGE_VAR_NAME, message);
    	simpleContext.setVariable(ORIGINAL_PAYLOAD_VAR_NAME, message.getOriginalPayload());
    	simpleContext.setVariable(PAYLOAD_VAR_NAME, message.getPayload());
    	simpleContext.setVariable(ID_VAR_NAME, message.getUniqueId());

    	/* systemProperties and systemEnvironment are spring beans already registered
    	   in the mule registry, and thus available as @systemEnvironment and @systemProperties.
    	   But this is collateral, so to properly to mimic Spring framework behavior both beans will
    	   be added as variables. 
    	*/
    	
    	if (muleRegistry != null)
    	{
    		simpleContext.setVariable(SYSTEM_PROPERTIES_VAR_NAME, muleRegistry.get(ConfigurableApplicationContext.SYSTEM_PROPERTIES_BEAN_NAME));
    		simpleContext.setVariable(ENVIRONMENT_VAR_NAME, muleRegistry.get(ConfigurableApplicationContext.SYSTEM_ENVIRONMENT_BEAN_NAME));
    	} 
    	
    	simpleContext.setRootObject(message.getPayload());
    	
    	simpleContext.setBeanResolver(beanResolver);
    	return simpleContext;
    }

}
