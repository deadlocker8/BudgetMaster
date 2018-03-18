$(document).ready(function () {
    $(document).ready(function() {
        $('select').material_select();
    });

    $("#buttonChooseDate").click(function () {
        var month = $("#selectMonth").val();
        var year = $("#selectYear").val();
        var dateString = "01." + month + "." + year;
        document.cookie = "currentDate=" + dateString;
    });
});