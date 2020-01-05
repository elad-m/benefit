package com.benefit.MacroFiles;

import java.util.List;

public class Filter {

    private String name;
    private List<String> options;

    public Filter(String name, List<String> options){
        this.name = name;
        this.options = options;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
