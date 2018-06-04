package xyz.relentlesscrew.util;

public class Path {

    public static class Web {

        public static final String INDEX = "/";
        public static final String ALL_MEMBERS = "members/";

        public static class Api {

            public static final String PATH = "/api/";
            public static final String PRIVATE_PATH = "private/";
            public static final String PUBLIC_PATH = "public/";

            public static final String ALL_MEMBERS = "members/";
            public static final String RANGE_OF_MEMBERS = ":page/";
            public static final String RANGE_OF_MEMBERS_WITH_LIMIT = ":page/:limit/";
            public static final String SINGLE_MEMBER = "members/:id/";

            public static final String ALL_APPLICATIONS = "applications/";
            public static final String SINGLE_APPLICATION = "applications/:id/";
        }
    }

    public static class Template {

        public static final String INDEX = "index.hbs";
        public static final String ALL_MEMBERS = "members.hbs";
        public static final String SINGLE_MEMBER = "member.hbs";
        public static final String NOT_FOUND = "notFound.hbs";
    }
}
