package xyz.relentlesscrew;

import xyz.relentlesscrew.controllers.ApiController;
import xyz.relentlesscrew.controllers.ApplicationController;
import xyz.relentlesscrew.controllers.IndexController;
import xyz.relentlesscrew.controllers.MemberController;
import xyz.relentlesscrew.util.Filters;
import xyz.relentlesscrew.util.Path;
import xyz.relentlesscrew.util.ViewUtil;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        final String portNumber = System.getenv("PORT");
        if (portNumber != null) {
            port(Integer.parseInt(portNumber));
        }

        staticFiles.location("/public");
        staticFiles.expireTime(600L);

        before("*", Filters.addTrailingSlashes);

        get(Path.Web.INDEX, IndexController.serveIndexPage);
        post(Path.Web.INDEX, ApplicationController.handleApplication);

        get(Path.Web.ALL_MEMBERS, MemberController.serveMembersPage);

        path(Path.Web.Api.PATH, () -> {
            before("*", ApiController.beforeApiCall);

            get(Path.Web.Api.ALL_APPLICATIONS, ApplicationController.getAllApplications);
            get(Path.Web.Api.SINGLE_APPLICATION, ApplicationController.getApplication);
            post(Path.Web.Api.ALL_APPLICATIONS, ApplicationController.addApplication);
            put(Path.Web.Api.SINGLE_APPLICATION, ApplicationController.updateApplication);
            delete(Path.Web.Api.SINGLE_APPLICATION, ApplicationController.removeApplication);

            path(Path.Web.Api.ALL_MEMBERS, () -> {
                get("/", MemberController.getAllMembers);
                get(Path.Web.Api.RANGE_OF_MEMBERS, MemberController.getRangeOfMembers);
                get(Path.Web.Api.RANGE_OF_MEMBERS_WITH_LIMIT, MemberController.getRangeOfMembers);
            });

            post(Path.Web.Api.ALL_MEMBERS, MemberController.addMember);
            put(Path.Web.Api.SINGLE_MEMBER, MemberController.updateMember);
            delete(Path.Web.Api.SINGLE_MEMBER, MemberController.removeMember);

            get("*", ViewUtil.notFoundApi);
        });

        get("*", ViewUtil.notFound);

        after("*", Filters.CORSFilter);
    }
}