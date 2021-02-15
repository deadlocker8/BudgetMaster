$(document).ready(function()
{
    $('#modalConfirmDelete').modal('open');

    // open filter modal if corresponding anchor is in url (originating from hotkeys.js)
    if(window.location.href.endsWith('#modalFilter'))
    {
        $('#modalFilter').modal('open');
    }

    if($("#transaction-name").length)
    {
        let elements = document.querySelectorAll('#transaction-name');
        let autoCompleteInstances = M.Autocomplete.init(elements, {
            data: transactionNameSuggestions,
        });

        // prevent tab traversal for dropdown (otherwise "tab" needs to be hit twice to jump from name input to amount input)
        autoCompleteInstances[0].dropdown.dropdownEl.tabIndex = -1;

        document.getElementById('transaction-name').focus();
    }

    if($("#transaction-description").length)
    {
        $("#transaction-description").characterCounter();
    }

    if($(".datepicker-simple".length) && $("#transaction-repeating-end-date-input").length)
    {
        let pickerEndDate = document.getElementById('transaction-repeating-end-date-input');

        // select corresponding radio button
        let endDate = document.getElementById("repeating-end-date");

        pickerEndDate.addEventListener('input', function()
        {
            endDate.checked = true;
        });

        pickerEndDate.addEventListener('focus', function()
        {
            endDate.checked = true;
        });
    }

    if($(".datepicker").length)
    {
        let pickerStartDate = M.Datepicker.init(document.getElementById('transaction-datepicker'), {
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
            let pickerEndDate = createDatePickerEnd(pickerStartDate.date, endDate);
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

            onSelect: function()
            {
                // select corresponding radio button
                let endDate = document.getElementById("repeating-end-date");
                endDate.checked = true;
            }
        });
    }

    if($('#transaction-amount').length)
    {
        $('#transaction-amount').on('change keydown paste input', function()
        {
            validateAmount($(this).val());
        });
    }

    if($(transactionRepeatingModifierID).length)
    {
        $(transactionRepeatingModifierID).on('change keydown paste input', function()
        {
            // substr(1) removes "#" at the beginning
            validateNumber($(this).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage, REGEX_NUMBER);
        });
    }

    if($(transactionRepeatingEndAfterXTimesInputID).length)
    {
        $(transactionRepeatingEndAfterXTimesInputID).on('change keydown paste input', function()
        {
            validateNumber($(this).val(), transactionRepeatingEndAfterXTimesInputID.substr(1), null, numberValidationMessage, REGEX_NUMBER);

            // select corresponding radio button
            let endAfterXTimes = document.getElementById("repeating-end-after-x-times");
            endAfterXTimes.checked = true;
        });
    }

    if($(".chips-autocomplete").length)
    {
        let elements = document.querySelectorAll('.chips-autocomplete');
        let instances = M.Chips.init(elements, {
            autocompleteOptions: {
                data: tagAutoComplete,
                limit: Infinity,
                minLength: 1
            },
            placeholder: tagsPlaceholder,
            data: initialTags
        });

        // prevent tab traversal for dropdown (otherwise "tab" needs to be hit twice to jump from tag input to account input)
        instances[0].autocomplete.dropdown.dropdownEl.tabIndex = -1;
    }

    // prevent form submit on enter (otherwise tag functionality will be hard to use)
    $(document).on("keypress", 'form', function(e)
    {
        if(e.ctrlKey)
        {
            return true;
        }

        let code = e.keyCode || e.which;
        if(code === 13)
        {
            if(e.target.nodeName === 'TEXTAREA' || e.target.id === 'search')
            {
                return true;
            }

            e.preventDefault();
            return false;
        }
    });

    $('.buttonIncome').click(function()
    {
        $('.buttonIncome').each(function()
        {
            $(this).removeClass("background-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("background-green");
        });

        $('.buttonExpenditure').each(function()
        {
            $(this).removeClass("background-red");
            $(this).addClass("background-grey");
            $(this).addClass("budgetmaster-text-isPayment");
        });

        document.getElementById("input-isPayment").value = 0;
    });

    $('.buttonExpenditure').click(function()
    {
        $('.buttonExpenditure').each(function()
        {
            $(this).removeClass("background-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("background-red");
        });

        $('.buttonIncome').each(function()
        {
            $(this).removeClass("background-green");
            $(this).addClass("background-grey");
            $(this).addClass("budgetmaster-text-isPayment");
        });

        document.getElementById("input-isPayment").value = 1;
    });

    M.FloatingActionButton.init(document.querySelectorAll('.new-transaction-button'), {
        direction: 'bottom',
        hoverEnabled: false
    });

    // scroll to highlighted transaction
    let highlightedSmall = document.getElementById("highlighted-small");
    let highlightedLarge = document.getElementById("highlighted-large");
    if(highlightedSmall !== undefined && highlightedSmall != null && !isHidden(highlightedSmall))
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

function isHidden(el)
{
    let style = window.getComputedStyle(el);
    return (style.display === 'none' || style.display === 'none !important')
}

let transactionRepeatingModifierID = "#transaction-repeating-modifier";
let transactionRepeatingEndAfterXTimesInputID = "#transaction-repeating-end-after-x-times-input";

AMOUNT_REGEX = new RegExp("^-?\\d+(,\\d+)?(\\.\\d+)?$");
ALLOWED_CHARACTERS = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "."];
DATE_REGEX_SHORT_NO_DOTS = new RegExp("^\\d{6}$");
DATE_REGEX_LONG_NO_DOTS = new RegExp("^\\d{8}$");
DATE_REGEX_SHORT = new RegExp("^(\\d{2}.\\d{2}.)(\\d{2})$");
DATE_REGEX_LONG = new RegExp("^\\d{2}.\\d{2}.\\d{4}$");

function validateAmount(text, allowEmpty = false)
{
    let id = "transaction-amount";

    if(allowEmpty && text.length === 0)
    {
        removeTooltip(id);
        document.getElementById("hidden-" + id).value = "";
        return true;
    }

    if(text.match(AMOUNT_REGEX) == null)
    {
        addTooltip(id, amountValidationMessage);
        document.getElementById("hidden-" + id).value = "";
        return false;
    }
    else
    {
        removeTooltip(id);
        let amount = parseFloat(text.replace(",", ".")) * 100;
        document.getElementById("hidden-" + id).value = amount.toFixed(0);
        return true;
    }
}

function validateDate(inputId)
{
    let dateInput = document.getElementById(inputId);
    dateInput.value = dateInput.value.trim();
    let date = dateInput.value;

    date = convertDateWithoutDots(date);
    dateInput.value = date;

    if(date.match(DATE_REGEX_LONG) != null)
    {
        removeTooltip(inputId);
        return true;
    }

    let match = date.match(DATE_REGEX_SHORT);
    if(match != null)
    {
        let dayAndMonth = match[1];
        let year = match[2];

        let currentYear = new Date().getFullYear();
        currentYear = currentYear.toString().substr(0, 2);

        dateInput.value = dayAndMonth + currentYear + year;
        removeTooltip(inputId);
        return true;
    }
    else
    {
        addTooltip(inputId, dateValidationMessage);
        return false;
    }
}

function convertDateWithoutDots(dateString)
{
    let yearLength = 2;
    if(dateString.match(DATE_REGEX_SHORT_NO_DOTS) != null)
    {
        yearLength = 2;
    }
    else if(dateString.match(DATE_REGEX_LONG_NO_DOTS) != null)
    {
        yearLength = 4;
    }
    else
    {
        return dateString;
    }

    return dateString.substr(0, 2) + '.' + dateString.substr(2, 2) + '.' + dateString.substr(4, yearLength);
}

function validateForm(allowEmptyAmount = false)
{
    // amount
    let isValidAmount = validateAmount($('#transaction-amount').val(), allowEmptyAmount);
    if(!isValidAmount)
    {
        return false;
    }

    // start date
    let datePickerId = 'transaction-datepicker';
    if(document.getElementById(datePickerId) !== null)
    {
        let isValidDate = validateDate();
        if(!isValidDate)
        {
            return false;
        }
    }

    // description
    let description = document.getElementById('transaction-description').value;
    if(description.length > 250)
    {
        return false;
    }

    // handle tags
    if($(".chips-autocomplete").length)
    {
        let tags = M.Chips.getInstance(document.querySelector('.chips-autocomplete')).chipsData;
        let parent = document.getElementById("hidden-transaction-tags");
        for(let i = 0; i < tags.length; i++)
        {
            let input = document.createElement("input");
            input.setAttribute("type", "hidden");
            input.setAttribute("name", "tags[" + i + "].name");
            input.setAttribute("value", tags[i].tag);
            parent.appendChild(input);
        }
    }

    if($(transactionRepeatingModifierID).length)
    {
        if(!validateNumber($(transactionRepeatingModifierID).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage, REGEX_NUMBER))
        {
            return false;
        }

        // handle repeating end
        let endNever = document.getElementById("repeating-end-never");
        let endAfterXTimes = document.getElementById("repeating-end-after-x-times");
        let endDate = document.getElementById("repeating-end-date");
        let endInput = document.getElementById("hidden-transaction-repeating-end-value");

        if(endNever.checked)
        {
            return true;
        }

        if(endAfterXTimes.checked)
        {
            if(!validateNumber($(transactionRepeatingEndAfterXTimesInputID).val(), transactionRepeatingEndAfterXTimesInputID.substr(1), null, numberValidationMessage, REGEX_NUMBER))
            {
                return false;
            }

            endInput.value = $(transactionRepeatingEndAfterXTimesInputID).val();
        }

        if(endDate.checked)
        {
            // start date
            let isValidDate = validateDate('transaction-repeating-end-date-input');
            if(!isValidDate)
            {
                return false;
            }

            endInput.value = $("#transaction-repeating-end-date-input").val();
        }
    }

    return true;
}
