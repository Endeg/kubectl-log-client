var currentPod = null;

function assignEventsToPodItems() {
    $("#pod-item").click(
        function (event) {
            var podName = $(event.target).text();
            $("#selected-pod").html(podName)
            $("#output").html("Loading...");
            $.ajax({
                url: "/log/pods/" + podName,
                success: function(result) {
                    $("#output").html(result.join("\n"));
                }
            });
        }
    );
}

$(function() {

    $("#refresh-pods").click(function (event) {
        $("#pod-list").html("Loading...");
        $.ajax({
            url: "/log/pods",
            success: function(result) {
                var resHtml = '<ul id="pod-item">' +
                    result.map(function(pod) { return "<li>" + pod + "</li>" }).join("") +
                    "</ul>";

                $("#pod-list").html(resHtml);

                assignEventsToPodItems();
            }
        });
    });

});
