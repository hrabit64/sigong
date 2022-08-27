
var configs = {
    'overlayBackgroundColor': '#333333',
    'overlayOpacity': 0.6,
    'spinnerIcon': 'ball-fall',
    'spinnerColor': '#00bc8c',
    'spinnerSize': '1x',
    'overlayIDName': 'overlay',
    'spinnerIDName': 'spinner',
    'offsetY': 0,
    'offsetX': 0,
    'lockScroll': true,
    'containerID': null,
};

var main = {
    init : function () {
        var _this = this;

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
                xhr.responseType = "blob";
        };


        $('#btn-upload').on('click', function () {
            JsLoadingOverlay.show(configs);
            _this.disableAleart();
            _this.upload();

        });

        $('#btn-info').on('click', function () {
            JsLoadingOverlay.show(configs);
            _this.disableAleart();
            _this.info();

        });

        $('#btn-close').on('click', function () {
            $("#infoModal").modal('hide');

        });

        $('#btn-delete').on('click', function () {
            JsLoadingOverlay.show(configs);
            _this.disableAleart();
            _this.delete();
        });

        $('#btn-download').on('click', function () {
            JsLoadingOverlay.show(configs);
            _this.disableAleart();
            _this.download();
        });
    },
    upload : function () {
        var form = $('#fileForm')[0];
        var formData = new FormData(form);
        $.ajax({
            type: 'POST',
            url: "/api/v1/file",
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            data: formData
        }).done(function(result) {
            $("#code-success").show();
            $("#code-out").text(result.download_code);
            JsLoadingOverlay.hide();
        }).fail(function (xhr, status, error) {
            $("#code-waring").show();
            var err = JSON.parse(xhr.responseText);
            document.getElementById('code-waring-message').innerText = err.detail;
            JsLoadingOverlay.hide();
        });
    },
    disableAleart : function (){
        $("#code-success").hide();
        $("#code-waring").hide();
        $("#code-delete").hide();
    },
    xhr : function () {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            //response 데이터를 바이너리로 처리한다. 세팅하지 않으면 default가 text
            xhr.responseType = "arraybuffer";
        };
        return xhr;
    } ,
    getUnicode : function (num){
        num = num.toString(16);
        if (num.length < 3) {
            for ( var i = num.length; i < 4; i++) {
                num = '0' + num;
            }
        }
        return ( "&#" + num + ";" );
    },
    info : function () {
        var code = $('#code').val();
        $.ajax({
            type: 'GET',
            url: "/api/v1/file/"+code,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
        }).done(function(res) {
            console.log(res)
            document.getElementById('fileName').innerText = res.file_name;
            document.getElementById('fileInfo-md5').innerText = res.md5;
            document.getElementById('fileInfo-sha1').innerText = res.sha1;
            document.getElementById('fileInfo-sha256').innerText = res.sha256;
            document.getElementById('fileInfo-create').innerText = res.created_date_time;
            document.getElementById('fileInfo-virus').href = res.virus_scan_result_link;

            $("#infoModal").modal('show');

            JsLoadingOverlay.hide();
        }).fail(function (xhr, status, error) {
            $("#code-waring").show();
            var err = JSON.parse(xhr.responseText);
            document.getElementById('code-waring-message').innerText = err.detail;
            JsLoadingOverlay.hide();
        });
    },
    delete : function () {
        var code = $('#code').val();
        $.ajax({
            type: 'DELETE',
            url: "/api/v1/file/"+code,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
        }).done(function(res) {
            $("#code-delete").show();
            $("#infoModal").modal('hide');
            JsLoadingOverlay.hide();
        }).fail(function (xhr, status, error) {
            $("#code-waring").show();
            var err = JSON.parse(xhr.responseText);
            $("#infoModal").modal('hide');
            document.getElementById('code-waring-message').innerText = err.detail;
            JsLoadingOverlay.hide();
        });
    },
    download : function () {
        var code = $('#code').val();
        $.ajax({
            type: 'GET',
            url: "/api/v1/file/"+code+"/download",
            contentType: "application/octet-stream;charset=utf-8",
            xhrFields: {
                responseType: 'blob' ,// to avoid binary data being mangled on charset conversion
            },
        }).done(function(blob, status, xhr) {
            // check for a filename
            var disposition = xhr.getResponseHeader('Content-Disposition');
            console.log(disposition)

            var filename = disposition.split('"')[1];

            if (typeof window.navigator.msSaveBlob !== 'undefined') {
                // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
                window.navigator.msSaveBlob(blob, filename);
            } else {
                var URL = window.URL || window.webkitURL;
                var downloadUrl = URL.createObjectURL(blob);

                if (filename) {
                    // use HTML5 a[download] attribute to specify filename
                    var a = document.createElement("a");
                    // safari doesn't support this yet
                    if (typeof a.download === 'undefined') {
                        window.location.href = downloadUrl;
                    } else {
                        a.href = downloadUrl;
                        a.download = filename;
                        document.body.appendChild(a);
                        a.click();
                    }
                } else {
                    window.location.href = downloadUrl;
                }

                setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100); // cleanup
            }
            $("#infoModal").modal('hide');
            JsLoadingOverlay.hide();
        }).fail(function (xhr, status, error) {
            $("#code-waring").show();
            $("#infoModal").modal('hide');
            document.getElementById('code-waring-message').innerText = "다운로드에 문제가 생겼습니다!";
            JsLoadingOverlay.hide();
        });
    }
};

main.init();