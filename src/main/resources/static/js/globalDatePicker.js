$(document).ready(function () {
    M.Modal.init(document.getElementById("modalDate"), {
        onOpenStart: function f() {
            cleanup();
        }
    });

    $("#global-datepicker-previous").click(function () {
        updateYearGrid(-11, document.getElementById('currentYear').innerHTML);
    });

    $("#global-datepicker-next").click(function () {
        updateYearGrid(11, document.getElementById('currentYear').innerHTML);
    });

    $("#global-datepicker-select-year .global-datepicker-item").click(function () {
        selectYear(this.innerText);
    });

    $("#global-datepicker-select-month .global-datepicker-item").click(function () {
        selectMonth($("#global-datepicker-select-month .global-datepicker-item").index(this) + 1);
    });
});

var year;


function cleanup() {
    year = undefined;
    $("#global-datepicker-select-month").hide();
    $("#global-datepicker-select-year").show();
}

function updateYearGrid(modifier, currentYear) {
    $("#global-datepicker-select-year").fadeOut(200, function () {
        var items = $("#global-datepicker-select-year .global-datepicker-item");
        var firstYear = parseInt(items[0].innerText);
        var newFirstYear = firstYear + modifier;

        for (var i = 0; i < items.length; i++) {
            items[i].innerText = newFirstYear + i;
            if (items[i].innerText === currentYear) {
                items[i].classList.add("global-datepicker-selected");
            }
            else {
                items[i].classList.remove("global-datepicker-selected");
            }
        }

        $("#global-datepicker-select-year").fadeIn(200);
    });
}

function selectYear(selectedYear) {
    year = selectedYear;
    $("#global-datepicker-select-year").fadeOut(200, function () {
        $("#global-datepicker-select-month").fadeIn(200);
    });
}

function selectMonth(selectedMonth) {
    var dateString = "01." + selectedMonth + "." + year;
    document.cookie = "currentDate=" + dateString;
    document.getElementById('buttonChooseDate').click();
}