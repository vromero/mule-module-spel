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

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class SpELFunctionalTestCase extends FunctionalTestCase
{
    protected String getConfigResources()
    {
        return "spel-functional-test-config.xml";
    }

    public void testExpressionTransformer() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("vm://in-expression-transformer", "some data", null);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        Object[] resultArray = (Object[]) result.getPayload();
        assertEquals("hello", resultArray[0]);
        assertTrue(resultArray[1] instanceof String);
    }
    
    public void testPropertiesTransformer() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("vm://in-properties-transformer", "some data", null);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        Object one = result.getInboundProperty("1");
        Object two = result.getInboundProperty("2");
        assertEquals("hello", one);
        assertTrue(two instanceof String);
    }
    
    public void testExpressionFilter() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("vm://in-expression-filter", StringUtils.EMPTY, null);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        Object resultPayload = result.getPayload();
        assertTrue(resultPayload instanceof String);
        assertEquals("passed", resultPayload);
    }
    
    public void testRegistryResolution() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("vm://in-bean-resolution", StringUtils.EMPTY, null);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        Object resultPayload = result.getPayload();
        assertTrue(resultPayload instanceof Boolean);
        assertEquals(true, resultPayload);
    }
    
    public void testSystemProperties() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage result = client.send("vm://in-system-properties", StringUtils.EMPTY, null);
        assertNotNull(result);
        assertNull(result.getExceptionPayload());
        assertFalse(result.getPayload() instanceof NullPayload);

        Object[] resultPayload = (Object[]) result.getPayload();
        assertNotNull(resultPayload);
        assertEquals(resultPayload.length, 4);
        assertNotNull(resultPayload[0]);
        assertNotNull(resultPayload[1]);
        assertNotNull(resultPayload[2]);
        assertNotNull(resultPayload[3]);
    }
    
}
