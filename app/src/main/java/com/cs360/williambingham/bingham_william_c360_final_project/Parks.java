package com.cs360.williambingham.bingham_william_c360_final_project;

class Parks {
    // instance variables
    private int var_id;
    private String var_name;
    private int var_rate;
    private String var_feature;
    private String var_city;

    // empty constructor
    public Parks()
    {
    }

    //constructor without id
    public Parks(int rate, String name, String feature, String city)
    {
        this.var_rate = rate;
        this.var_name = name;
        this.var_feature = feature;
        this.var_city = city;
    }

    //setter (mutators)
    public void setID(int id) { this.var_id = id; }
    public void setName(String name) { this.var_name = name; }
    public void setFeature(String feature) { this.var_feature = feature; }
    public void setCity(String city) { this.var_city = city; }
    public void setRate(int rate) { this.var_rate = rate; }

    //Getter (mutators)
    public int getID(){
        return var_id;
    }
    public String getName(){
        return var_name;
    }
    public String getFeature(){
        return var_feature;
    }
    public String getCity() {
        return var_city;
    }
    public int getRate(){
        return var_rate;
    }
}