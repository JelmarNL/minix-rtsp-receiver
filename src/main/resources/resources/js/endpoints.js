$.get("/endpoints/audio/getaudiorepeaterstatus", function(data) {
    $("#audioState").text("State: " + data);
});
