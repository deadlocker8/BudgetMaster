$( document ).ready(function() {
    $('#modalConfirmDelete').modal('open');

    // open filter modal if corresponding anchor is in url (originating from hotkeys.js)
    if(window.location.href.endsWith('#modalFilter'))
    {
        $('#modalFilter').modal('open');
    }

    if($(".datepicker").length)
    {
        var pickerStartDate = M.Datepicker.init(document.getElementById('transaction-datepicker'), {
            yearRange: 25,
            firstDay: 1,
            showClearBtn: false,
            setDefaultDate: true,
            defaultDate: startDate,

            i18n: {
                // Strings and translations
                months: monthNames,
                monthsShort: monthNamesShort,
                weekdays: weekDays,
                weekdaysShort: weekDaysShort,
                weekdaysAbbrev: weekDaysLetters,

                // Buttons
                done: buttonClose,

                // Accessibility labels
                labelMonthNext: '>',
                labelMonthPrev: '<'
            },

            // Formats
            format: 'dd.mm.yyyy',
            formatSubmit: 'dd.mm.yyyy',

            onSelect: function()
            {
                if(typeof pickerEndDate !== "undefined")
                {
                    pickerEndDate.destroy();
                    pickerEndDate = createDatePickerEnd(this.date, pickerEndDate.date);
                }
            }
        });

        // picker end date
        if(typeof endDate !== "undefined")
        {
            var pickerEndDate = createDatePickerEnd(pickerStartDate.date, endDate);
        }
    }

    function createDatePickerEnd(minDate, selectedDate)
    {
        if(selectedDate < minDate)
        {
            selectedDate = minDate;
        }

        return M.Datepicker.init(document.getElementById('transaction-repeating-end-date-input'), {
            yearRange: 50,
            firstDay: 1,
            showClearBtn: false,
            setDefaultDate: true,
            minDate: minDate,
            defaultDate: selectedDate,

            i18n: {
                // Strings and translations
                months: monthNames,
                monthsShort: monthNamesShort,
                weekdays: weekDays,
                weekdaysShort: weekDaysShort,
                weekdaysAbbrev: weekDaysLetters,

                // Buttons
                done: buttonClose,

                // Accessibility labels
                labelMonthNext: '>',
                labelMonthPrev: '<'
            },

            // Formats
            format: 'dd.mm.yyyy',
            formatSubmit: 'dd.mm.yyyy',

            onSelect: function ()
            {
                // select corresponding radio button
                var endDate = document.getElementById("repeating-end-date");
                endDate.checked = true;
            }
        });
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
        $('.chips-autocomplete').chips({
            autocompleteOptions: {
                data: tagAutoComplete,
                limit: Infinity,
                minLength: 1
            },
            placeholder: tagsPlaceholder,
            data: initialTags
        });
    }

    // prevent form submit on enter (otherwise tag functionality will be hard to use)
    $(document).on("keypress", 'form', function (e) {
        var code = e.keyCode || e.which;
        if(code === 13) {
            if(e.target.nodeName === 'TEXTAREA' || e.target.id === 'search') {
                return true;
            }

            e.preventDefault();
            return false;
        }
    });

    $('.buttonIncome').click(function()
    {
        $('.buttonIncome').each(function () {
            $(this).removeClass("budgetmaster-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("budgetmaster-green");
        });

        $('.buttonExpenditure').each(function () {
            $(this).removeClass("budgetmaster-red");
            $(this).addClass("budgetmaster-grey");
            $(this).addClass("budgetmaster-text-isPayment");
        });

        document.getElementById("input-isPayment").value = 0;
    });

    $('.buttonExpenditure').click(function()
    {
        $('.buttonExpenditure').each(function () {
            $(this).removeClass("budgetmaster-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("budgetmaster-red");
        });

        $('.buttonIncome').each(function () {
            $(this).removeClass("budgetmaster-green");
            $(this).addClass("budgetmaster-grey");
            $(this).addClass("budgetmaster-text-isPayment");
        });

        document.getElementById("input-isPayment").value = 1;
    });

    M.FloatingActionButton.init(document.querySelectorAll('.fixed-action-btn'), {
        direction: 'bottom',
        hoverEnabled: false
    });

    // scroll to highlighted transaction
    var highlightedSmall = document.getElementById("highlighted-small");
    var highlightedLarge = document.getElementById("highlighted-large");
    if(highlightedSmall !== undefined  && highlightedSmall != null && !isHidden(highlightedSmall))
    {
        $('html, body').animate({
            scrollTop: $(highlightedSmall).offset().top
        }, 500);
    }
    else if(highlightedLarge !== undefined && highlightedLarge != null && !isHidden(highlightedLarge))
    {
        $('html, body').animate({
            scrollTop: $(highlightedLarge).offset().top
        }, 500);
    }
});

function isHidden(el) {
    var style = window.getComputedStyle(el);
    return (style.display === 'none' || style.display === 'none !important')
}

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
        var amount = parseFloat(text.replace(",", ".")) * 100;
        document.getElementById("hidden-" + id).value = amount.toFixed(0);
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
    M.Tooltip.init(element);
}

function removeTooltip(id)
{
    var element = document.getElementById(id);

    removeClass(element, "validate");
    removeClass(element, "invalid");
    removeClass(element, "tooltipped");
    addClass(element, "valid");
    var tooltip = M.Tooltip.getInstance(element);
    if(tooltip !== undefined)
    {
        tooltip.destroy();
    }
}

function validateForm()
{
    // amount
    validateAmount($('#transaction-amount').val());

    // handle tags
    if($(".chips-autocomplete").length)
    {
        var tags = M.Chips.getInstance(document.querySelector('.chips-autocomplete')).chipsData;
        var parent = document.getElementById("hidden-transaction-tags");
        for (var i = 0; i < tags.length; i++)
        {
            var input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", "tags[" + i + "].name");
            input.setAttribute("value", tags[i].tag);
            parent.appendChild(input);
        }
    }

    if($(transactionRepeatingModifierID).length)
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

    return true;
}
