
$(document).ready(function() {


    $("#downloadSingleResult").click(function (event){

        event.preventDefault();

        ajaxGetSingleResults();
    });

    $("#downloadResults").click(function (event){

        event.preventDefault();

        ajaxGetResults();
    });

    $("#downloadDirectory").click(function (event) {

        event.preventDefault();

        ajaxGetDirectory();
    });

    $("#downloadUploads").click(function (event){

        event.preventDefault();

        ajaxGetUploads();
    });

    $("#externalCallButton").click(function(event){

        event.preventDefault();

        ajaxSubmitExternalForm();

    });
    $("#submitUpload").click(function(event) {

        event.preventDefault();

        ajaxSubmitUploadForm();

    });

    $("#submitDirectory").click(function(event) {

        event.preventDefault();

        ajaxSubmitDirectoryForm();
    });

    $("#submitCalculate").click(function(event) {

        event.preventDefault();

        ajaxSubmitCalculateForm();
    });

    $("#deleteEntryButton").click(function(event) {

        event.preventDefault();

        ajaxSubmitDeleteEntryForm();
    });

    $("#showUploadList").click(function(event) {

        event.preventDefault();

        ajaxGetUploadList();

    });

    $("#showDirectoryList").click(function(event) {

        event.preventDefault();

        ajaxGetDirectoryList();
    });

    $("#showScriptList").click(function (event) {

        event.preventDefault();

        ajaxGetScriptList();

    });

    $("#showResultsList").click(function (event) {

        event.preventDefault();

        ajaxGetResultList();

    });

    $("body").delegate("#clearDirectory", "click", function(event) {
        event.preventDefault();

        ajaxSubmitClearDirectory();
    });
});


function ajaxSubmitExternalForm(){

    $("#externalCallButton").prop("disabled", true);
    var form = $('#externalForm')[0];
    var data = new FormData(form);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/externalCall",
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 10000,
        success: function(data) {
            console.log("SUCCESS: ", data);
            $("#externalCallButton").prop("disabled", false);
            $('#externalForm')[0].reset();
        }, error: function () {

        }
    })
}

function download(filename, data) {
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(data));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}

function downloadZip(data, fileName){
    var link = document.createElement("a");
    document.body.appendChild(link);
    link.setAttribute("type", "hidden");
    link.href = "data:text/plain;base64," + data;
    link.download = fileName;
    link.click();
    document.body.removeChild(link);
}
function ajaxGetDirectory(){
    $("#downloadDirectory").prop("disabled", true);
    $.ajax({
        url: "/getDirectory",
        type: "GET",
        success: function(data){

            var link = document.createElement("a");
            document.body.appendChild(link);
            link.setAttribute("type", "hidden");
            link.href = "data:text/plain;base64," + data;
            link.download = "directory.zip";
            link.click();
            document.body.removeChild(link);

            $("#downloadDirectory").prop("disabled", false);

        }, error: function(err){
            alert("There are no calculated Filed to download");
            $("#downloadDirectory").prop("disabled", true);
        }
    });
}
function ajaxGetResults(){

    $("#downloadResults").prop("disabled", true);

    $.ajax({
        url: "/getResults",
        type: "GET",
        success: function(data){

            var link = document.createElement("a");
            document.body.appendChild(link);
            link.setAttribute("type", "hidden");
            link.href = "data:text/plain;base64," + data;
            link.download = "results.zip";
            link.click();
            document.body.removeChild(link);

            $("#downloadResults").prop("disabled", false);

        }, error: function(err){
            alert("There are no calculated Filed to download");
            $("#downloadResults").prop("disabled", true);
        }
    });
}


function ajaxGetSingleResults() {

    var name = $('#getResultFileName').val();

    $("#downloadSingleResult").prop("disabled", true);

    $.ajax({
        type: "GET",
        dataType:'html',
        url: "/getResult/" + name,
        success: function (data) {
            download(name,data);
            $("#downloadSingleResult").prop("disabled", false);
        },
        error: function (err) {
            alert(err /*'Could not create the Zip file'*/);
            console.log(err);
        }

    });
}


function ajaxGetUploads(){

    $("#downloadUploads").prop("disabled", true);

    $.ajax({
        url: "/getUploads",
        type: "GET",
        success: function(data){

            downloadZip(data, "Uploads.zip");
            $("#downloadUploads").prop("disabled", false);

        }, error: function(err){
            alert("The Upload directory is empty");
            $("#downloadUploads").prop("disabled", true);
        }
    });
}

function ajaxGetResultList(){
    $("#showResultsList").prop("disabled", true);
    $("#downloadResults").prop("disabled", false);

    $.ajax({
        type: "GET",
        url: "/getResultList",
        success: function (data) {
            $("#showResultsList").prop("disabled", false);
            changeResultsList(data)
        },
        error: function (err) {
            alert('did not find the list');
            console.log(err);
        }
    });
}

function ajaxGetDirectoryList(){
    $("#showDirectoryList").prop("disabled", true);
    $("#downloadDirectory").prop("disabled", false)

    $.ajax({
        type: "GET",
        url: "/getDirectoryList",
        success: function (data) {
            $("#showDirectoryList").prop("disabled", false);
            changeDirectoryList(data)
        },
        error: function (err) {
            alert('did not find the list');
            console.log(err);
        }
    })
}

function ajaxGetScriptList(){

    $("#showScriptList").prop("disabled", true);

    $.ajax({
        type: "GET",
        url: "/getScriptList",
        success: function (data) {
            $("#showScriptList").prop("disabled", false);
            changeScriptList(data)
        },
        error: function (err) {
            alert('did not find the list');
            console.log(err);
        }
    });
}

function ajaxGetUploadList(){
    $("#downloadUploads").prop("disabled", false);
    $("#showUploadList").prop("disabled", true);

    $.ajax({
        type: "GET",
        url: "/getUploadList",
        success: function (data) {
            $("#showUploadList").prop("disabled", false);
            changeUploadList(data)
        },
        error: function (err) {
            alert('did not find the list');
            console.log(err);
        }

    });

}

function changeResultsList(items){
    var list = $('#resultsList');
    list.empty();
    for (var i = 0; i < items.length; i++) {
        var entry = document.createElement('li');
        entry.appendChild(document.createTextNode(items[i]));
        entry.id = "entry_" + i;
        list.append(entry);
    }
}

function changeUploadList(items){
    var list = $('#uploadList');
    list.empty();
    for (var i = 0; i < items.length; i++) {
        //var entry = $.createElement('li');
        var entry = document.createElement('li');
        entry.appendChild(document.createTextNode(items[i]));
        list.append(entry);
    }
}

function changeDirectoryList(items){
    var list = $('#directoryList');
    list.empty();
    for (var i = 0; i < items.length; i++) {
        var entry = document.createElement('li');
        entry.appendChild(document.createTextNode(items[i]));
        list.append(entry);
    }
}

function changeScriptList(items){
    var list = $('#scriptList');
    list.empty();
    for (var i = 0; i < items.length; i++) {
        var entry = document.createElement('li');
        entry.appendChild(document.createTextNode(items[i]));
        list.append(entry)
    }

}


function ajaxSubmitDeleteEntryForm(){

    var form = $('#deleteEntryForm')[0];
    var data = new FormData(form);

    $("#deleteEntryButton").prop("disabled", true);

    $.ajax({
        type: "DELETE",
        enctype: 'multipart/form-data',
        url: "/deleteEntry/" + $("#deleteFileName"),
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function (data, textStatus, jqXHR) {
            console.log("SUCCESS :", data);
            $("#deleteEntryButton").prop("disabled", false);
            $('#deleteEntryForm')[0].reset();
        }, error: function () {
            window.write("nope");
        }

    });
}

function ajaxSubmitClearDirectory(){

    var form = $('#clearForm')[0];
    var data = new FormData(form);

    $("#clearDirectory").prop("disabled", true);

    $.ajax({
        type: "DELETE",
        enctype: 'multipart/form-data',
        url: "/clearDirectory",
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR){
            $("#result").html(data);
            console.log("SUCCESS :", data);
            $("#clearDirectory").prop("disabled", false);
            $('#clearForm')[0].reset();
        }, error: function () {

        }
    });
}


function ajaxSubmitCalculateForm() {

    var form = $('#calculateForm')[0];
    var data = new FormData(form);

    $("#submitCalculate").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/fileChecker",
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR){
            console.log("SUCCESS : ", data);
            $("#submitCalculate").prop("disabled", false);
            $('#calculateForm')[0].reset();
        },error: function () {

        }
    });
}

function ajaxSubmitDirectoryForm() {

    var form = $('#directoryUploadForm')[0];

    var data = new FormData(form);

    $("#submitDirectory").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/addMultipleToDirectory",
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR){
            console.log("SUCCESS : ", data);
            $("#submitDirectory").prop("disabled", false);
            $('#directoryUploadForm')[0].reset();
        },error: function(){
            console.log("Error: Could't store data");
        }
    });
}

function ajaxSubmitUploadForm() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    $("#submitUpload").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/uploadMultipleFiles",
        data: data,

        processData: false,
        contentType: false,
        cache: false,
        timeout: 1000000,
        success: function(data, textStatus, jqXHR) {

            console.log("SUCCESS : ", data);
            $("#submitUpload").prop("disabled", false);
            $('#fileUploadForm')[0].reset();
        },
        error: function(jqXHR, textStatus, errorThrown) {

            $("#result").html(jqXHR.responseText);
            console.log("ERROR : ", jqXHR.responseText);
            $("#submitButton").prop("disabled", false);

        }
    });

}