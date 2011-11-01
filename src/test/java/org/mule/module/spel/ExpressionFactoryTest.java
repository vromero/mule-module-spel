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
import org.springframework.expression.Expression;


public class ExpressionFactoryTest extends AbstractMuleTestCase
{

	public void testExceptionOnNull() {
		try {
			ExpressionFactory contextFactory = new ExpressionFactory();
			contextFactory.create(null);
		} catch (IllegalArgumentException e) {
			return;
		}
		fail("Create should throw IllegalArgumentException on null message");
	}
	
	public void testExceptionOnInvalid() {
		try {
			ExpressionFactory contextFactory = new ExpressionFactory();
			contextFactory.create("'");
		} catch (Exception e) {
			return;
		}
		fail("Create should throw ExpressionRuntimeException on invalid expression");
	}
	
	public void testCreate() {
		MuleRegistry muleRegistry = mock(MuleRegistry.class);
		MuleContext context = mock(MuleContext.class);
		when(context.getRegistry()).thenReturn(muleRegistry);
		
		MuleMessage message = new DefaultMuleMessage(null, context);
		message.setPayload("a payload");
		
		ExpressionFactory expressionFactory = new ExpressionFactory();
		Expression expression = expressionFactory.create("'hello'");
		expressionFactory.dispose();
		
		assertEquals("'hello'", expression.getExpressionString());
	}
	
}
