package dk.itu.mayt.tingle;

/**
 * Created by May Ji & Micki on 11-02-2016.
 */
public class Thing {
    private String mWhat = null;
    private String mWhere = null;
    private Integer mSQLId = null;
    private Integer mCount = 0;

    public Thing(String what, String where) {
        mWhat = what;
        mWhere = where;
    }
    @Override
    public String toString() { return oneLine("Item: ","is here: "); }
    public String getWhat() { return mWhat; }
    public void setWhat(String what) { mWhat = what; }
    public String getWhere() { return mWhere; }
    public void setWhere(String where) { mWhere = where; }
    public String oneLine(String pre, String post) {
        return pre+mWhat + " "+post + mWhere;
    }

    //used for finding item in database using _id
    public void setId (Integer newId) { mSQLId = newId;}
    public Integer getId () {return mSQLId;}

    public void increaseCount(){this.mCount++;}
    public Integer getCount(){return this.mCount;}
    public void setCount(Integer count){this.mCount = count;}
}