$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

    if($(".datepicker").length)
    {
        var pickerStartDate = $('#transaction-datepicker').pickadate({
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
            formatSubmit: 'dd.mm.yyyy',

            onSet: function()
            {
                var selectedDate = this.get('select').obj;
                if(pickerEndDate.get('select').obj < selectedDate)
                {
                    pickerEndDate.set('select', selectedDate);
                }

                pickerEndDate.set('min', selectedDate);
            }
        });

        pickerStartDate = pickerStartDate.pickadate('picker');

        var pickerEndDate = $('#transaction-repeating-end-date-input').pickadate({
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

        pickerEndDate = pickerEndDate.pickadate('picker');

        // picker end date
        var selectedDate = pickerStartDate.get('select').obj;
        if(pickerEndDate.get('select').obj < selectedDate)
        {
            pickerEndDate.set('select', selectedDate);
        }
        pickerEndDate.set('min', selectedDate);

        pickerEndDate.on({
            set: function() {
                // select corresponding radio button
                var endDate = document.getElementById("repeating-end-date");
                endDate.checked = true;
            }
        })
    }

    if($('#transaction-amount').length)
    {
        $('#transaction-amount').on('change keydown paste input', function() {
            validateAmount($(this).val());
        });
    }

    if($(transactionRepeatingModifierID).length)
    {
        $(transactionRepeatingModifierID).on('change keydown paste input', function() {
            // substr(1) removes "#" at the beginning
            validateNumber($(this).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage);
        });
    }

    if($(transactionRepeatingEndAfterXTimesInputID).length)
    {
        $(transactionRepeatingEndAfterXTimesInputID).on('change keydown paste input', function() {
            validateNumber($(this).val(), transactionRepeatingEndAfterXTimesInputID.substr(1), null, numberValidationMessage);

            // select corresponding radio button
            var endAfterXTimes = document.getElementById("repeating-end-after-x-times");
            endAfterXTimes.checked = true;
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
            placeholder: tagsPlaceholder,
            data: initialTags
        });
    }

    $('#enableRepeating').change(function(){
        if($(this).is(":checked"))
        {
            $('#transaction-repeating-modifier-row').show();
            $('#transaction-repeating-end').show();
        }
        else
        {
            $('#transaction-repeating-modifier-row').hide();
            $('#transaction-repeating-end').hide();
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

var transactionRepeatingModifierID = "#transaction-repeating-modifier";
var transactionRepeatingEndAfterXTimesInputID = "#transaction-repeating-end-after-x-times-input";

AMOUNT_REGEX = new RegExp("^-?\\d+(,\\d+)?(\\.\\d+)?$");
NUMBER_REGEX = new RegExp("^\\d+$");
ALLOWED_CHARACTERS = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "."];

function validateAmount(text)
{
    var id = "transaction-amount";

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

function validateNumber(text, ID, hiddenID, message)
{
    if(text.match(NUMBER_REGEX) == null)
    {
        addTooltip(ID, message);
        if (hiddenID != null)
        {
            document.getElementById(hiddenID).value = "";
        }
        return false;
    }
    else
    {
        removeTooltip(ID);
        if (hiddenID != null)
        {
            document.getElementById(hiddenID).value = parseInt(text);
        }
        return true;
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
    // amount
    validateAmount($('#transaction-amount').val());

    // handle tags
    var tags = $('.chips-autocomplete').material_chip('data');
    var parent = document.getElementById("hidden-transaction-tags");
    for(var i = 0; i < tags.length; i++)
    {
        var input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("name", "tags[" + i + "].name");
        input.setAttribute("value", tags[i].tag);
        parent.appendChild(input);
    }

    if(document.getElementById("enableRepeating").checked)
    {
        if(!validateNumber($(transactionRepeatingModifierID).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage))
        {
            return false;
        }

        // handle repeating end
        var endNever = document.getElementById("repeating-end-never");
        var endAfterXTimes = document.getElementById("repeating-end-after-x-times");
        var endDate = document.getElementById("repeating-end-date");
        var endInput = document.getElementById("hidden-transaction-repeating-end-value");

        if(endNever.checked)
        {
            return true;
        }

        if(endAfterXTimes.checked)
        {
            if(!validateNumber($(transactionRepeatingEndAfterXTimesInputID).val(), transactionRepeatingEndAfterXTimesInputID.substr(1), null, numberValidationMessage))
            {
                return false;
            }

            endInput.value = $(transactionRepeatingEndAfterXTimesInputID).val();
        }

        if(endDate.checked)
        {
            endInput.value = $("#transaction-repeating-end-date-input").val();
        }
    }
    else
    {
        document.getElementById("hidden-" + transactionRepeatingModifierID.substr(1)).value = -1;
    }

    return true;
}