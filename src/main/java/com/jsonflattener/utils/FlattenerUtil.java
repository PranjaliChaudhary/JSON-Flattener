package com.jsonflattener.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FlattenerUtil {

	public static List<String> flattenJson(String json){
		List<Map<String, String>> listOfMapOfJsons = flattenJson(null, json);
		List<String> listOfFlatJsons = new ArrayList<String>();
		for(Map<String, String> mapOfJson : listOfMapOfJsons){
			ObjectMapper mapper = new ObjectMapper();
			try{
				String jsonStr = mapper.writeValueAsString(mapOfJson);
				listOfFlatJsons.add(jsonStr);
			} catch(JsonProcessingException e){
				e.printStackTrace();
			}
		}
		return listOfFlatJsons;
	}
	
	private static List<Map<String, String>> flattenJson(String parent, String json){
		Map<String, String> levelMap = new HashMap<String, String>();
		parent = parent != null && parent.length() > 0 ? parent + "_" :"";
		Map<String, JSONArray> childNestedArrays = new HashMap<String, JSONArray>();
		Map<String, JSONObject> childNestedObjects = new HashMap<String, JSONObject>();
		List<Map<String, String>> flattenedList = new ArrayList<Map<String,String>>();
		
		JSONObject jsonObj = new JSONObject(json);
		Iterator jsonIterator = jsonObj.keys();
		
		//segregating simple fields, JSON objects and JSON arrays
		while(jsonIterator.hasNext()){
			Object key = jsonIterator.next();
			Object value = jsonObj.isNull(key.toString())?"":jsonObj.get(key.toString());
			if(value instanceof JSONArray){
				childNestedArrays.put(parent + key.toString(), (JSONArray) value); 
			} else if (value instanceof JSONObject){
				childNestedObjects.put(parent + key.toString(), (JSONObject) value);
			} else{
				levelMap.put(parent + key.toString(), value.toString());
			}
		}
		
		//flattening JSON Objects and adding them to a map
		Map<String, List<Map<String, String>>> nestedObjFlattenedMap = new HashMap<String, List<Map<String,String>>>();
		if(!childNestedObjects.isEmpty()){
			for(String nestedObjKey : childNestedObjects.keySet()){
				JSONObject nestedObj = childNestedObjects.get(nestedObjKey);
				List<Map<String, String>> flattenJson = flattenJson(nestedObjKey, nestedObj.toString());
				nestedObjFlattenedMap.put(nestedObjKey, flattenJson);
			}
		}
		
		//flattening JSON Arrays and adding them to a map
		Map<String, List<Map<String, String>>> nestedArrayFlattenedMap = new HashMap<String, List<Map<String,String>>>();
		if(!childNestedArrays.isEmpty()){
			for(String nestedArrKey : childNestedArrays.keySet()){
				JSONArray nestedArr = childNestedArrays.get(nestedArrKey);
				for(int i=0; i<nestedArr.length(); i++){
					List<Map<String, String>> flattenJson = flattenJson(nestedArrKey, nestedArr.get(i).toString());
					if(nestedArrayFlattenedMap.isEmpty() || !nestedArrayFlattenedMap.containsKey(nestedArrKey)){
						nestedArrayFlattenedMap.put(nestedArrKey, flattenJson);
					} else{
						List<Map<String, String>> tempList = nestedArrayFlattenedMap.get(nestedArrKey);
						tempList.addAll(flattenJson);
						nestedArrayFlattenedMap.put(nestedArrKey, tempList);
					}
				}
			}
		}
		
		//adding simple fields of a level first to the final flattened list
		if(!levelMap.isEmpty()){
			flattenedList.add(levelMap);
		}
		
		//merging all objects of the same level into a list
		List<Map<String, String>> mergedObjLevelList = new ArrayList<Map<String,String>>();
		if(nestedObjFlattenedMap.size() == 1){
			for(String objKey : nestedObjFlattenedMap.keySet()){
				mergedObjLevelList.addAll(nestedObjFlattenedMap.get(objKey));
			}
		} else{
			for(String objKey : nestedObjFlattenedMap.keySet()){
				if(mergedObjLevelList.isEmpty()){
					mergedObjLevelList.addAll(nestedObjFlattenedMap.get(objKey));
				} else{
					mergedObjLevelList = merge(mergedObjLevelList, nestedObjFlattenedMap.get(objKey));
				}
			}
		}
		
		//merging all arrays of the same level into a list
		List<Map<String, String>> mergedArrLevelList = new ArrayList<Map<String,String>>();
		if(nestedArrayFlattenedMap.size() == 1){
			for(String arrKey : nestedArrayFlattenedMap.keySet()){
				mergedArrLevelList.addAll(nestedArrayFlattenedMap.get(arrKey));
			}
		} else{
			for(String arrKey : nestedArrayFlattenedMap.keySet()){
				if(mergedArrLevelList.isEmpty()){
					mergedArrLevelList.addAll(nestedArrayFlattenedMap.get(arrKey));
				} else{
					mergedArrLevelList = merge(mergedArrLevelList, nestedArrayFlattenedMap.get(arrKey));
				}
			}
		}
		
		//merging arrays and objects of same level
		List<Map<String, String>> levelObjArrMapList = new ArrayList<Map<String,String>>();
		levelObjArrMapList = merge(mergedObjLevelList, mergedArrLevelList);
		
		//merging simple fields, arrays and objects of same level
		flattenedList = flattenedList.isEmpty()? levelObjArrMapList : merge(levelObjArrMapList, flattenedList);
		
		return flattenedList;
		
	}
	
	private static List<Map<String, String>> merge(List<Map<String, String>> list1, List<Map<String, String>> list2){
		List<Map<String, String>> mergedList = new ArrayList<Map<String,String>>();
		if(!list1.isEmpty() && !list2.isEmpty()){
			for(Map<String, String> mapFromList1 : list1){
				for(Map<String, String> mapFromList2 : list2){
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.putAll(mapFromList2);
					tempMap.putAll(mapFromList1);
					mergedList.add(tempMap);
				}
			}
		} else if(list1.isEmpty()){
			mergedList.addAll(list2);
		} else{
			mergedList.addAll(list1);
		}
		
		return mergedList;
	}
}
