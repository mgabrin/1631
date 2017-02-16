package com.example.michaelgabrin.cs1631_semester_project;

import java.util.Vector;

/**
 * Created by michaelgabrin on 2/16/17.
 */
public class KeyValueList {
    private Vector Keys;
    private Vector Values;

    /* Constructor */

    public KeyValueList(){
        Keys=new Vector();
        Values=new Vector();
    }

/* Look up the value given key, used in getValue() */

    private int lookupKey(String strKey){
        for(int i=0;i<Keys.size();i++){
            String k=(String) Keys.elementAt(i);
            if (strKey.equals(k)) return i;
        }
        return -1;
    }

/* add new (key,value) pair to list */

    public boolean addPair(String strKey,String strValue){
        return (Keys.add(strKey)&& Values.add(strValue));
    }

/* get the value given key */

    public String getValue(String strKey){
        int index=lookupKey(strKey);
        if (index==-1) return null;
        return (String) Values.elementAt(index);
    }

    /* Show whole list */
    public String toString(){
        String result = new String();
        for(int i=0;i<Keys.size();i++){
            result+=(String) Keys.elementAt(i)+":"+(String) Values.elementAt(i)+"\n";
        }
        return result;
    }

    public int size(){ return Keys.size(); }

    /* get Key or Value by index */
    public String keyAt(int index){ return (String) Keys.elementAt(index);}
    public String valueAt(int index){ return (String) Values.elementAt(index);}
}
