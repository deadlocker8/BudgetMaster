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
        M.Autocomplete.init(elements, {
            data: transactionNameSuggestions,
        });
        document.getElementById('transaction-name').focus();
    }

    if($("#transaction-description").length)
    {
        $("#transaction-description").characterCounter();
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
            validateNumber($(this).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage);
        });
    }

    if($(transactionRepeatingEndAfterXTimesInputID).length)
    {
        $(transactionRepeatingEndAfterXTimesInputID).on('change keydown paste input', function()
        {
            validateNumber($(this).val(), transactionRepeatingEndAfterXTimesInputID.substr(1), null, numberValidationMessage);

            // select corresponding radio button
            let endAfterXTimes = document.getElementById("repeating-end-after-x-times");
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
    $(document).on("keypress", 'form', function(e)
    {
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
            $(this).removeClass("budgetmaster-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("budgetmaster-green");
        });

        $('.buttonExpenditure').each(function()
        {
            $(this).removeClass("budgetmaster-red");
            $(this).addClass("budgetmaster-grey");
            $(this).addClass("budgetmaster-text-isPayment");
        });

        document.getElementById("input-isPayment").value = 0;
    });

    $('.buttonExpenditure').click(function()
    {
        $('.buttonExpenditure').each(function()
        {
            $(this).removeClass("budgetmaster-grey");
            $(this).removeClass("budgetmaster-text-isPayment");
            $(this).addClass("budgetmaster-red");
        });

        $('.buttonIncome').each(function()
        {
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

function validateAmount(text, allowEmpty=false)
{
    let id = "transaction-amount";

    if(allowEmpty && text.length === 0)
    {
        removeTooltip(id);
        document.getElementById("hidden-" + id).value = 0;
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

function validateForm(allowEmptyAmount=false)
{
    // amount
    let isValidAmount = validateAmount($('#transaction-amount').val(), allowEmptyAmount);
    if(!isValidAmount)
    {
        return false;
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
        if(!validateNumber($(transactionRepeatingModifierID).val(), transactionRepeatingModifierID.substr(1), "hidden-" + transactionRepeatingModifierID.substr(1), numberValidationMessage))
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
