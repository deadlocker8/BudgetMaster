$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

    $('select').material_select();

    if($(".datepicker").length)
    {
        $('.datepicker').pickadate({
            selectMonths: true,
            selectYears: 100,
            firstDay: 1,

            // Strings and translations
            monthsFull: monthNames,
            monthsShort: monthNamesShort,
            weekdaysFull: weekDays,
            weekdaysShort: weekDaysShort,
            weekdaysLetter: weekDaysLetters,

            // Buttons
            today: buttonToday,
            clear: buttonClear,
            close: buttonClose,

            // Accessibility labels
            labelMonthNext: '',
            labelMonthPrev: '',
            labelMonthSelect: '',
            labelYearSelect: '',

            // Formats
            format: 'dd.mm.yyyy',
            formatSubmit: 'dd.mm.yyyy'
        });
    }

    if($("#payment-amount").length)
    {
        $('#payment-amount').on('change keydown paste input', function() {
            validateAmount($(this).val());
        });
    }
});

AMOUNT_REGEX = new RegExp("^-?\\d+(,\\d+)?(\\.\\d+)?$");
ALLOWED_CHARACTERS = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "."];

function validateAmount(text)
{
    var element = document.getElementById("payment-amount");

    if(text.match(AMOUNT_REGEX) == null)
    {
        removeClass(element, "validate");
        removeClass(element, "valid");
        addClass(element, "tooltipped");
        addClass(element, "invalid");
        element.dataset.tooltip=amountValidationMessage;
        element.dataset.position="bottom";
        element.dataset.delay="50";
        $('#payment-amount').tooltip();
        document.getElementById("hidden-payment-amount").value = "";
    }
    else
    {
        removeClass(element, "validate");
        removeClass(element, "invalid");
        removeClass(element, "tooltipped");
        addClass(element, "valid");
        $('#payment-amount').tooltip('remove');
        var amount = parseInt(parseFloat(text.replace(",", ".")) * 100);
        document.getElementById("hidden-payment-amount").value = amount;
    }
}