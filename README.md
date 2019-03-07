# JSON-Flattener
Flattens nested JSON into a list of single level JSONs
Naming convention for child elements becomes parent_child

# Problem statement
The input is a deeply nested JSON that contains simple fields, objects and arrays. 
Example: {"id" : "1", "name" : {"first" : "Harry" , "last" : "Potter"}, "contact" : [{"type" : "home", "number" : "12345"},{"type" : "office", "number" : "67890"}]}

Available flattening solutions flatten the array with the index
{"id" : "1", "name_first" : "Harry" , "name_last" : "Potter" , "contact_type[0]" : "home", "contact_number[0]" : "12345",
"contact_type[1]" : "office", "contact_number[1]" : "67890"}

Our usecase required rule application on JSONs with dynamic structure. Applying rules was difficult on fields from a list since we did not know how many items there would be in the list.

# Solution
We decided to create a list of JSONs, each flattened JSON containing the simple fields, objects and one element per array. If the JSON had multiple lists on the same level, one with m objects, other with n, the resultant JSON list would have total of mXn flattened JSONs.

By our solution, the above JSON would be flattened as : 
{"id" : "1", "name_first" : "Harry" , "name_last" : "Potter" , "contact_type" : "home", "contact_number" : "12345"}
{"id" : "1", "name_first" : "Harry" , "name_last" : "Potter" , "contact_type" : "office", "contact_number" : "67890"}

# Sample input
{ "data": { 
              "item": { 
                          "props": [
                                      { "neat": "wow" }, 
                                      { "neat": "tubular" }
                                    ] },
              "colors": [
                          { 
                              "color": "red",
                              "hex": "ff0000" 
                           }, 
                           { 
                              "color": "blue", 
                              "hex": "0000ff" 
                            }
                           ]
              }
}


# Program output
{"data_colors_color":"red","data_colors_hex":"ff0000","data_item_props_neat":"wow"} 
{"data_colors_color":"blue","data_colors_hex":"0000ff","data_item_props_neat":"wow"} 
{"data_colors_color":"red","data_colors_hex":"ff0000","data_item_props_neat":"tubular"} 
{"data_colors_color":"blue","data_colors_hex":"0000ff","data_item_props_neat":"tubular"}
