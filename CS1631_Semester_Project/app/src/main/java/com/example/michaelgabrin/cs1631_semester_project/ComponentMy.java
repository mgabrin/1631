package com.example.michaelgabrin.cs1631_semester_project;

/**
 * Created by michaelgabrin on 2/16/17.
 */
public class ComponentMy implements ComponentBase {
    public ComponentMy(){

    }

    public KeyValueList processMsg(KeyValueList kvList){
        System.out.println("In the component!");
        return kvList;
    }
}
