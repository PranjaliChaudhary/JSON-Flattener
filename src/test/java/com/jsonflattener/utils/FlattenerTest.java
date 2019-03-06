package com.jsonflattener.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FlattenerTest {
	
	@Test
	public void testFlattening(){
		String sampleJson="{\"id\":\"1\", \"details\":{\"name\":\"test\", \"age\":\"20\"}}";
		String flatJson="{\"details_name\":\"test\",\"details_age\":\"20\",\"id\":\"1\"}";
		List<String> listOfJsons = new ArrayList<String>();
		listOfJsons.add(flatJson);
		assertEquals(listOfJsons, FlattenerUtil.flattenJson(sampleJson));
	}

}
