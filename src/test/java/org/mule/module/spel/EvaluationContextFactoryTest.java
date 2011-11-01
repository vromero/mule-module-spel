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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.registry.MuleRegistry;
import org.mule.tck.AbstractMuleTestCase;
import org.springframework.expression.EvaluationContext;

public class EvaluationContextFactoryTest extends AbstractMuleTestCase
{
	
	public void testExceptionOnNull() 
	{
		try 
		{
			MuleContext context = mock(MuleContext.class);
			EvaluationContextFactory contextFactory = new EvaluationContextFactory(context);
			contextFactory.create(null);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Create should throw IllegalArgumentException on null message");
	}
	
	public void testCreate() 
	{
		MuleRegistry muleRegistry = mock(MuleRegistry.class);
		MuleContext context = mock(MuleContext.class);
		when(context.getRegistry()).thenReturn(muleRegistry);
		
		MuleMessage message = new DefaultMuleMessage(null, context);
		message.setPayload("a payload");
		
		EvaluationContextFactory contextFactory = new EvaluationContextFactory(context);
		EvaluationContext evaluationContext = contextFactory.create(message);
		
		Object resultMuleContext = evaluationContext.lookupVariable("context");
		assertNotNull(resultMuleContext);
		assertTrue(resultMuleContext instanceof MuleContext);
		assertEquals(context, resultMuleContext);
		
		Object resultMuleRegistry = evaluationContext.lookupVariable("registry");
		assertNotNull(resultMuleRegistry);
		assertTrue(resultMuleRegistry instanceof MuleRegistry);
		assertEquals(muleRegistry, resultMuleRegistry);
		
		Object resultMuleMessage = evaluationContext.lookupVariable("message");
		assertNotNull(resultMuleMessage);
		assertTrue(resultMuleMessage instanceof MuleMessage);
		
		Object resultOriginalPayload = evaluationContext.lookupVariable("originalPayload");
		assertNull(resultOriginalPayload);
		
		Object resultPayload = evaluationContext.lookupVariable("payload");
		assertNotNull(resultPayload);
		assertEquals("a payload", resultPayload);
		
		Object resultId = evaluationContext.lookupVariable("id");
		assertNotNull(resultId);
	}
}
