$(document).ready(function () {
    console.log("Page has loaded.");

    // $("form").on("submit", function(event) {
    //     if (!this.checkValidity()) {
    //       event.preventDefault();
    //       event.stopPropagation();
    //     }
    //   });
    
    $("input[type='submit']").on("click", function (event) {
        $(this).closest("form").addClass("was-validated");
    })
});