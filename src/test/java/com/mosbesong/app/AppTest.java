package com.mosbesong.app;

import org.junit.Assert;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Assert.assertTrue( true );
    }

	@Test
	public void should_ask_user_to_provide_json_description_if_none_is_provided() throws Exception
	{
		String[] arguments = new String[0];
		CrudGenerator crudGenerator = new CrudGenerator(arguments);
		Assert.assertEquals("Please provide a json description file\n", crudGenerator.start());
	}

}
