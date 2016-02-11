package me.zipstream.criminalintent.database;

/**
 * 数据库架构模式/纲目, 方便调用, 比如找到 date 列: CrimeTable.Cols.DATE
 */
public class CrimeDbSchema {

    // 数据库中的 crimes 表结构
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
