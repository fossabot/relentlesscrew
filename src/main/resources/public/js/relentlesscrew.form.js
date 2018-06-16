(function ($) {
    var discordUsername = $('#discordusername');
    var dauntlessUsername = $('#dauntlessusername');
    var discordReg = /.*#\d{4}$/;
    var dauntlessReg = /^[a-zA-Z0-9]{3,32}$/;

    discordUsername.focusout(function () {
        validateInput($(this), discordReg);
    });

    dauntlessUsername.focusout(function () {
        validateInput($(this), dauntlessReg);
    });

    // Handle DAO form response
    $('#application_form').submit(function(event) {
        // Stop form from submitting normally
        event.preventDefault();

        var result = $('#result');

        result.addClass("alert alert-primary text-center");

        if (!validateInput(discordUsername, discordReg)) {
            result.text("Discord username must have a form of someUser#1234");
            return;
        }

        if (!validateInput(dauntlessUsername, dauntlessReg)) {
            result.text("Dauntless username must be between 3 and 32 characters long and only contain lowercase and uppercase letters and numbers.");
            return;
        }

        var grResponse = grecaptcha.getResponse();
        if (grResponse === '') {
            result.text("Please verify that you are not a bot.");
            return;
        }

        result.text("loading...");

        var formData = {
            'discordusername': $.trim(discordUsername.val()),
            'dauntlessusername': $.trim(dauntlessUsername.val()),
            'g-recaptcha-response': grResponse
        };

        var request = $.ajax({
            url: $(this).attr("action"),
            type: "post",
            data: formData
        });

        request.done(function (response) {
            var responseJson = jQuery.parseJSON(JSON.stringify(response));
            result.html(responseJson.response);
            discordUsername.val("");
            dauntlessUsername.val("");
            grecaptcha.reset();
        });

    });

    function validateInput(element, reg) {
        if(reg.test(element.val())) {
            if (element.hasClass('invalid-form')) {
                element.removeClass('invalid-form');
            }
            element.addClass("valid-form");

            return true;
        } else {
            if (element.hasClass('valid-form')) {
                element.removeClass('valid-form');
            }
            element.addClass("invalid-form");

            return false;
        }
    }
})(jQuery);
