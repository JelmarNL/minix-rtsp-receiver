$.get("/endpoints/audio/getaudioinput", function(data) {
    $("#audioInput").html(data);
});

$.get("/endpoints/audio/getaudiooutput", function(data) {
    $("#audioOutput").html(data);
});

$("#audioInput").on("change", function() {
    this.value;
    $.get("/endpoints/audio/setaudioinput", {input: this.value}, function(data) {
        alert("Set audio input to: \n" + data);
    });
});

$("#audioOutput").on("change", function() {
    this.value;
    $.get("/endpoints/audio/setaudiooutput", {input: this.value}, function(data) {
        alert("Set audio output to: \n" + data);
    });
});

$("#restartAudio").on("click", function() {
    $.get("/endpoints/audio/restartaudio", function(data) {
        alert("Restart: \n" + data);
        location.reload();
    });
});

$.get("/endpoints/audio/getaudiorepeaterstatus", function(data) {
    $("#audioState").text("State: " + data);
});

//TODO: Lock and unlock save button till set request is done
