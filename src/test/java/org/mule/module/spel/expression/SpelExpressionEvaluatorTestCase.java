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

import static org.mockito.Mockito.mock;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.expression.ExpressionRuntimeException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.testmodels.fruit.Apple;
import org.mule.tck.testmodels.fruit.Banana;
import org.mule.tck.testmodels.fruit.FruitBowl;

public class SpelExpressionEvaluatorTestCase extends AbstractMuleTestCase
{
    private MuleContext muleContext = mock(MuleContext.class);
    
    public void testGetName() throws InitialisationException
    {
        SpELExpressionEvaluator e = new SpELExpressionEvaluator();
        e.setMuleContext(muleContext);
        e.initialise();
        
        assertEquals("spel", e.getName());
    }
    
    public void testUnsupportedSetName()
    {
    	try {
    		SpELExpressionEvaluator e = new SpELExpressionEvaluator();
    		e.setName("wontWork");
    	} 
    	catch (UnsupportedOperationException e) 
    	{
    		return;
    	}
    	
    	fail("UnsupportedOperationException should be trown for setName");
    }
    
    public void testEmptyExpression() throws InitialisationException
    {
        DefaultMuleMessage msg = new DefaultMuleMessage(null, muleContext);
        
        SpELExpressionEvaluator e = new SpELExpressionEvaluator();
        Object value = e.evaluate("", msg);
        e.setMuleContext(muleContext);
        e.initialise();
        
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertEquals("", (String) value);
    }
    
    public void testInvalidExpression() throws InitialisationException
    {
        DefaultMuleMessage msg = new DefaultMuleMessage(null, muleContext);
        
        try {
        	SpELExpressionEvaluator e = new SpELExpressionEvaluator();
            e.setMuleContext(muleContext);
            e.initialise();
            e.evaluate("'", msg);
    	} 
        catch (ExpressionRuntimeException e) 
        {
    		return;
    	}
    	
    	fail("ExpressionRuntimeException should be trown for invalid expressions");
    }
    
    public void testGetValueReturnsNullOnException() throws InitialisationException
    {
        DefaultMuleMessage msg = new DefaultMuleMessage(null, muleContext);
        
        SpELExpressionEvaluator e = new SpELExpressionEvaluator();
        e.setMuleContext(muleContext);
        e.initialise();
        Object value = e.evaluate("#nonExistentVar.nonExistentProperty", msg);
    	assertNull(value);
    }
    
    public void testSimpleExpressions() throws InitialisationException
    {
        DefaultMuleMessage msg = new DefaultMuleMessage(null, muleContext);
        
        SpELExpressionEvaluator e = new SpELExpressionEvaluator();
        e.setMuleContext(muleContext);
        e.initialise();
        Object value = e.evaluate("'Hello World'.concat('!')", msg);
        
        assertNotNull(value);
        assertTrue(value instanceof String);
        assertEquals("Hello World!", (String) value);
        
        value = e.evaluate("'Hello World'.bytes", msg);
        assertTrue(value instanceof byte[]);
        
        value = e.evaluate("'Hello World'.bytes.length", msg);
        assertTrue(value instanceof Integer);
        
        value = e.evaluate("new String('hello world').toUpperCase()", msg);
        assertTrue(value instanceof String);
        assertEquals("HELLO WORLD", (String) value);
    }
    
    public void testExpressionsWithVars() throws InitialisationException
    {
        Apple apple = new Apple();
        apple.wash();
        
        Banana banana = new Banana();
        banana.bite();
        
        FruitBowl payload = new FruitBowl(apple, banana);
        
        DefaultMuleMessage msg = new DefaultMuleMessage(payload, muleContext);
        
        SpELExpressionEvaluator e = new SpELExpressionEvaluator();
        e.setMuleContext(muleContext);
        e.initialise();
        
        Object value = e.evaluate("#payload.hasApple() and #payload.hasBanana()", msg);
        assertNotNull(value);
        assertTrue((Boolean) value);
    }
    
}
