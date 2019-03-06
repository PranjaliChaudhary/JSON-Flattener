package com.jsonflattener.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.jsonflattener.utils.FlattenerUtil;

/**
 * Hello world!
 *
 */
public class FlattenerMain 
{
    public static void main( String[] args )
    {
        try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(new FlattenerMain().getClass().getClassLoader()
				      .getResource("sampleJson.txt").toURI())));
			String json;
			List<String> flattenedJsons = new ArrayList<String>();
			while((json = reader.readLine()) != null){
				flattenedJsons = FlattenerUtil.flattenJson(json);
				for(String flatJson : flattenedJsons){
					System.out.println(flatJson);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
