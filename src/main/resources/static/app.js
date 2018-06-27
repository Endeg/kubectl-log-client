var currentPod = null;

function pollForLogs() {
    if (currentPod != null) {
        setTimeout(function() {
            $.ajax({
                url: "/log/pods/" + currentPod,
                success: function(result) {
                    var currentText = $("#output").text() + "\n"
                    $("#output").html(currentText + result.join("\n"));
                    pollForLogs();
                }
            });
        }, 1000);
    }
}

function assignEventsToPodItems() {
    $("#pod-item").click(
        function (event) {
            currentPod = $(event.target).text();
            $("#selected-pod").html(currentPod)
            $("#output-indicator").html("Loading...");
            $("#output").html("");
            pollForLogs();
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
