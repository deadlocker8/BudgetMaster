let searchPickerStartDate;
let searchPickerEndDate;

$(document).ready(function()
{
    $('.page-link').click(function()
    {
        if(!this.parentElement.classList.contains('disabled'))
        {
            let page = this.dataset.page;
            let inputPageNumber = document.getElementById('inputPageNumber');
            inputPageNumber.value = page;
            document.getElementById('searchForm').submit();
        }
    });

    if($(".datepicker").length)
    {
        searchPickerStartDate = M.Datepicker.init(document.getElementById('search-datepicker'), {
            yearRange: 50,
            firstDay: 1,
            showClearBtn: false,
            setDefaultDate: startDate !== null,
            defaultDate: startDate,
            autoClose: true,

            i18n: {
                // Strings and translations
                months: monthNames,
                monthsShort: monthNamesShort,
                weekdays: weekDays,
                weekdaysShort: weekDaysShort,
                weekdaysAbbrev: weekDaysLetters,

                // Buttons
                cancel: buttonCancel,
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
                if(typeof searchPickerEndDate !== "undefined")
                {
                    let selectedDate = searchPickerEndDate.date;
                    searchPickerEndDate.destroy();
                    searchPickerEndDate = createSearchDatePickerEnd(this.date, selectedDate);
                }
            }
        });

        // picker end date
        if(typeof endDate !== "undefined")
        {
            searchPickerEndDate = createSearchDatePickerEnd(searchPickerStartDate.date, endDate);
        }
    }
});

function createSearchDatePickerEnd(minDate, selectedDate)
{
    if(selectedDate < minDate)
    {
        selectedDate = minDate;
    }



    return M.Datepicker.init(document.getElementById('search-datepicker-end'), {
        yearRange: 50,
        firstDay: 1,
        showClearBtn: false,
        setDefaultDate: true,
        minDate: minDate,
        defaultDate: selectedDate,
        autoClose: true,

        i18n: {
            // Strings and translations
            months: monthNames,
            monthsShort: monthNamesShort,
            weekdays: weekDays,
            weekdaysShort: weekDaysShort,
            weekdaysAbbrev: weekDaysLetters,

            // Buttons
            cancel: buttonCancel,
            done: buttonClose,

            // Accessibility labels
            labelMonthNext: '>',
            labelMonthPrev: '<'
        },

        // Formats
        format: 'dd.mm.yyyy',
        formatSubmit: 'dd.mm.yyyy'
    });
}