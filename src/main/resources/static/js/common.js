$(document).ready(function () {
    console.log("Page has loaded.");

    $("input[type='submit']").on("click", function (event) {
        $(this).closest("form.needs-validation").addClass("was-validated");
    })

    // function for .countdown to process auto countdown jump
    function startCountdown() {
        var duration = parseInt($(this).data('duration'), 10);
        var countdownElement = $(this).find('.countdown-time');
        var targetUrl = $(this).data('target-url');
        console.log(targetUrl);

        // Display initial countdown value
        countdownElement.text(duration);

        // Start the countdown
        var countdownInterval = setInterval(function () {
            duration--;

            if (duration >= 0) {
                countdownElement.text(duration);
            } else {
                clearInterval(countdownInterval);

                if (targetUrl) {
                    var newUrl = new URL(targetUrl, window.location.href);
                    console.log(newUrl);
                    window.location.href = newUrl;
                }
            }
        }, 1000); // Update countdown every 1 second
    }
    $(".countdown").each(startCountdown);

    function sha256(message) {
        return CryptoJS.SHA256(message).toString();
    }

    // function for .login_form class to auto hash password
    $(".login_form").each(function () {
        $(this).submit(function (e) {
            e.preventDefault();

            const pwdInput = $(this).find("#password");
            console.log(pwdInput);
            var password = pwdInput.val(); // Use val() method to get the input value
            if (password) {
                var hashedPassword = sha256(password + 'WonderfulProject');
                pwdInput.val(hashedPassword); // Use val() method to set the input value
            }
            $(this).unbind('submit').submit(); // Unbind the submit event handler and submit the form
        });
    });



});