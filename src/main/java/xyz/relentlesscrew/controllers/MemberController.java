package xyz.relentlesscrew.controllers;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;
import xyz.relentlesscrew.persistence.DAO.MemberDAO;
import xyz.relentlesscrew.persistence.model.Member;
import xyz.relentlesscrew.util.JsonUtil;
import xyz.relentlesscrew.util.Path;
import xyz.relentlesscrew.util.ViewUtil;

import java.util.HashMap;
import java.util.List;


public class MemberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    private static MemberDAO memberDAO = new MemberDAO();

    public static Route serveMembersPage = (request, response) ->
            ViewUtil.render(new HashMap<>(), Path.Template.ALL_MEMBERS);

    public static Route getRangeOfMembers = (request, response) -> {
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(request.params(":page"));
            limit = Integer.parseInt(request.params(":limit"));
        } catch (Exception ignored) {}

        List<Member> memberList = memberDAO.findRange((limit * page) - limit, limit * page);

        response.header("totalPages", String.valueOf((int) Math.ceil(memberDAO.countRows() / (double) limit)));
        return JsonUtil.responseJson(response, memberList);
    };

    public static Route addMember = (request, response) -> {
        Member member;
        try {
            member = new Gson().fromJson(request.body(), Member.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + " Caused by: " + e.getCause());
            return JsonUtil.responseJson(response, "There was an error. Check the logs");
        }

        memberDAO.add(member);

        return JsonUtil.responseJson(response, member);
    };

    public static Route updateMember = (request, response) -> {
        Member member;
        try {
            member = new Gson().fromJson(request.body(), Member.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + " Caused by: " + e.getCause());
            return JsonUtil.responseJson(response, "There was an error. Check the logs");
        }

        memberDAO.update(member);

        return JsonUtil.responseJson(response, member);
    };

    public static Route removeMember = (request, response) -> {
        Long id = Long.parseLong(request.params(":id"));
        Member member = memberDAO.findById(id);

        String responseString;
        if (memberDAO.remove(member)) {
            responseString = "Member " + member.getDauntlessUsername() + "(" + member.getId() + ") was successfully removed.";
        } else {
            responseString = "Could not remove the member. Please check the logs.";
        }
        return JsonUtil.responseJson(response, responseString);
    };

    public static Route getAllMembers = (request, response) ->
            JsonUtil.responseJson(response, memberDAO.findAll());
}