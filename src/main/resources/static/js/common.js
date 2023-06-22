$(document).ready(function () {
    console.log("Page has loaded.");

    $("input[type='submit']").on("click", function (event) {
        $(this).closest("form").addClass("was-validated");
    })
});