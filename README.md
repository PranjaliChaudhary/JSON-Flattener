# JSON-Flattener
Flattens nested JSON into a list of single level JSONs
Naming convention for child elements becomes parent_child

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

Please note that this is for representation only. Actual JSON should be single line.

# Program output
{"data_colors_color":"red","data_colors_hex":"ff0000","data_item_props_neat":"wow"} {"data_colors_color":"blue","data_colors_hex":"0000ff","data_item_props_neat":"wow"} {"data_colors_color":"red","data_colors_hex":"ff0000","data_item_props_neat":"tubular"} {"data_colors_color":"blue","data_colors_hex":"0000ff","data_item_props_neat":"tubular"}
