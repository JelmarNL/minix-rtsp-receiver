//
//DEVICE
//
setInterval(function() {
    $.get("/endpoints/device/getuptime", function (data) {
        $("#uptime").text(data);
    });
}, 1000);

setInterval(function() {
    $.get("/endpoints/device/getconsole", function (data) {
        $("#console").html(data);
    });
}, 5000);

$("#reboot").on("click", function() {
    if (confirm("Are you sure you want to reboot this device?")) {
        $.get("/endpoints/device/restartdevice", function(data) {
            alert(data);
        });
    }
});

//
//AUDIO
//
$.get("/endpoints/audio/getaudioinput", function(data) {
    $("#audioInput").html(data);
});

$.get("/endpoints/audio/getaudiooutput", function(data) {
    $("#audioOutput").html(data);
});

$.get("/endpoints/audio/getbuffer", function(data) {
    $("#audioBuffer").val(data);
});

$("#restartAudio").on("click", function() {
    let input = $("#audioInput").val();
    let output = $("#audioOutput").val();
    let buffer = $("#audioBuffer").val();
    $.get("/endpoints/audio/restartaudio", {"input": input, "output": output, "repeater": buffer}, function(data) {
        alert("Audio restart: \n" + data);
        location.reload();
    });
});

setInterval(function() {
    $.get("/endpoints/audio/getaudiorepeaterstatus", function (data) {
        $("#audioState").text("State: " + data);
    });
}, 5000);

//
//VIDEO
//
$.get("/endpoints/video/getvideoconfig", function(data) {
    data = data.split("||");
    $("#cameraIp").val(atob(data[0]));
    $("#streamOptions").val(atob(data[1]));
    $("#streamUrl").val(atob(data[2]));
});

$("#restartVideo").on("click", function() {
    let cameraIp = btoa($("#cameraIp").val());
    let streamOptions = btoa($("#streamOptions").val());
    let streamUrl = btoa($("#streamUrl").val());
    $.get("/endpoints/video/restartvideo", {"cameraIp": cameraIp, "streamOptions": streamOptions, "streamUrl": streamUrl}, function(data) {
        alert("Video restart: \n" + data);
        location.reload();
    });
});

setInterval(function() {
    $.get("/endpoints/video/getvideostatus", function (data) {
        $("#videoState").text("State: " + data);
    });
}, 5000);
