(function ($) {
    "use strict"; // Start of use strict

    // Smooth scrolling using jQuery easing
    $('a.js-scroll-trigger[href*="#"]:not([href="#"])').click(function () {
        if (location.pathname.replace(/^\//, '') === this.pathname.replace(/^\//, '') && location.hostname === this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: (target.offset().top - 54)
                }, 1000, "easeInOutExpo");
                return false;
            }
        }
    });

    // Closes responsive menu when a scroll trigger link is clicked
    $('.js-scroll-trigger').click(function () {
        $('.navbar-collapse').collapse('hide');
    });

    // Activate scrollspy to add active class to navbar items on scroll
    $('body').scrollspy({
        target: '#mainNav',
        offset: 55
    });

    var uri = window.location.pathname.split("/")[1];
    console.log(uri);
    $('nav a[href="/'+ uri +'"]').parent().addClass("active");

    $('nav a').filter(function() {
        return $(this).attr("href").split("/")[1] === uri;
    }).parent().addClass("active");

    $('[data-toggle="tooltip"]').tooltip();

    // toggle lightbox gallery
    $('[data-toggle="lightbox"]').click(function (event) {
        event.preventDefault();
        $(this).ekkoLightbox();
    });

    // Animate loading of masthead background (ease in)
    if ($(window).width() > 768) {
        var images = ['bg1.jpg', 'bg2.jpg', 'bg3.jpg'];
        $('header.masthead')
            .animate({opacity: 0}, 0)
            .css('background-image', 'url(/img/' + images[Math.floor(Math.random() * images.length)] + ')')
            .animate({opacity: 1}, 2500);
    } else {
        $('header.masthead')
            .animate({opacity: 0}, 0)
            .css('background-color', '#343a40')
            .animate({opacity: 1}, 2500);
    }


})(jQuery);
