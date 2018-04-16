$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

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

    if($(".chips-autocomplete").length)
    {
        $('.chips-autocomplete').material_chip({
            autocompleteOptions: {
                data: tagAutoComplete,
                limit: Infinity,
                minLength: 1
            },
            placeholder: tagsPlaceholder
        });
    }

    // prevent form submit on enter (otherwise tag functionality will be hard to use)
    $(document).on("keypress", 'form', function (e) {
        var code = e.keyCode || e.which;
        if (code === 13) {
            e.preventDefault();
            return false;
        }
    });
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

function validateForm()
{
    var tags = $('.chips-autocomplete').material_chip('data');
    var parent = document.getElementById("hidden-payment-tags");
    for(var i = 0; i < tags.length; i++)
    {
        var input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("name", "tags[" + i + "].name");
        input.setAttribute("value", tags[i].tag);
        parent.appendChild(input);
    }

    return true;
}