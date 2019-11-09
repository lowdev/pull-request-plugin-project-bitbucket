package ut.eu.lowentropy;

import org.junit.Test;
import eu.lowentropy.api.MyPluginComponent;
import eu.lowentropy.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}