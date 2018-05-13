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

    if($('#payment-amount').length)
    {
        $('#payment-amount').on('change keydown paste input', function() {
            validateAmount($(this).val());
        });
    }

    var paymentRepeatingModifierID = "#payment-repeating-modifier";
    if($(paymentRepeatingModifierID).length)
    {
        $(paymentRepeatingModifierID).on('change keydown paste input', function() {
            validateNumber($(this).val(), paymentRepeatingModifierID.substr(1), numberValidationMessage);
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

    $('#enableRepeating').change(function(){
        if($(this).is(":checked"))
        {
            $('#payment-repeating-modifier-row').show();
            $('#payment-repeating-end').show();
        }
        else
        {
            $('#payment-repeating-modifier-row').hide();
            $('#payment-repeating-end').hide();
        }
    });

    // fire change listener on page load
    $('#enableRepeating').trigger("change");

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
NUMBER_REGEX = new RegExp("^\\d+$");
ALLOWED_CHARACTERS = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "."];

function validateAmount(text)
{
    var id = "payment-amount";

    if(text.match(AMOUNT_REGEX) == null)
    {
        addTooltip(id, amountValidationMessage);
        document.getElementById("hidden-" + id).value = "";
    }
    else
    {
        removeTooltip(id);
        var amount = parseInt(parseFloat(text.replace(",", ".")) * 100);
        document.getElementById("hidden-" + id).value = amount;
    }
}

function validateNumber(text, id, message)
{
    if(text.match(NUMBER_REGEX) == null)
    {
        addTooltip(id, message);
        document.getElementById("hidden-" + id).value = "";
    }
    else
    {
        removeTooltip(id);
        document.getElementById("hidden-" + id).value =  parseInt(text);
    }
}

function addTooltip(id, message)
{
    var element = document.getElementById(id);

    removeClass(element, "validate");
    removeClass(element, "valid");
    addClass(element, "tooltipped");
    addClass(element, "invalid");
    element.dataset.tooltip=message;
    element.dataset.position="bottom";
    element.dataset.delay="50";
    $('#' + id).tooltip();
}

function removeTooltip(id)
{
    var element = document.getElementById(id);

    removeClass(element, "validate");
    removeClass(element, "invalid");
    removeClass(element, "tooltipped");
    addClass(element, "valid");
    $('#' + id).tooltip('remove');
}

function validateForm()
{
    // handle tags
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

    // handle repeating end
    var endNever = document.getElementById("repeating-end-never");
    var endAfterXTimes = document.getElementById("repeating-end-after-x-times");
    var endDate = document.getElementById("repeating-end-date");
    var endInput = document.getElementById("hidden-payment-repeating-end-value");

    if(endNever.checked)
    {
        return true;
    }

    if(endAfterXTimes.checked)
    {
        endInput.value = "12";
    }

    if(endDate.checked)
    {
        endInput.value = "13.05.18";
    }

    return true;
}