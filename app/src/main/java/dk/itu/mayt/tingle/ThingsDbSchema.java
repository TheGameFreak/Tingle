package dk.itu.mayt.tingle;

/**
 * Created by May Ji on 06-04-2016.
 */
public class ThingsDbSchema {
    public static final class ThingTable {
        public static final String NAME = "things";

        public static final class Cols {
            public static final String WHAT = "what";
            public static final String WHERE = "location";
            public static final String COUNT = "count";
        }
    }
}
