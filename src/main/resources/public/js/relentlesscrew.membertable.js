(function ($) {
    var table = $("#memberlist");
    var totalPages;
    $.getJSON("/api/members/1", function (data, status, xhr) {
        var response = data["response"];
        totalPages = xhr.getResponseHeader('totalpages');

        populateTable(table, response);

        $.getScript("/js/jquery.twbsPagination.min.js", function () {
            $('#pagination').twbsPagination({
                totalPages: totalPages,
                onPageClick: function (event, page) {
                    $.getJSON("/api/members/" + page, function (data) {
                        populateTable(table, data["response"]);
                    });
                }
            });
        });
    });

    function populateTable(table, data) {
        table.find("tr").remove();

        var tableData = $.map(data, function (member) {
            return $("<tr>").append(
                $("<td>").text(atob(member.dauntlessUsername)),
                $("<td>").text(member.rank.name)
            )
        });

        table.append(tableData);
    }
})(jQuery);