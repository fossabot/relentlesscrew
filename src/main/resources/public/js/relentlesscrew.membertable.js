$.getScript("jquery.twbsPagination.min.js", function() {
    $.getJSON("/api/members/").
    $('#pagination').twbsPagination({
        totalPages: totalPages,
        visiblePages: 7,
            onPageClick: function (event, page) {
            $('#page-content').text('Page ' + page);
        }
    });
});
