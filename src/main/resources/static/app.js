$(function() {

    $("#refresh-pods").click(function(event) {
        $("#pod-list").html("Loading...");
        $.ajax({
            url: "/proc",
            success: function(result) {
                var resHtml = "<ul>" +
                    result.map(function(pod) { return "<li>" + pod + "</li>" }).join("") +
                    "</ul>";

                $("#pod-list").html(resHtml);
            }
        });
    });

});
