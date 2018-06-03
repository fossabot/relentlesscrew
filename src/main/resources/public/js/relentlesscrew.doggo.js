(function ($) {
    $.getJSON("https://random.dog/woof.json", function (json) {
        var url = json.url;

        if (url.match(".mp4$")) {
            $("<video autoplay controls>").attr("src", url).appendTo("#doggo-img");
        } else {
            $("<img>").attr({
                src: url,
                alt: "A random doggo image",
                class: "mx-auto d-block doggo-img"
            }).appendTo("#doggo-img");
        }
    })
})(jQuery);