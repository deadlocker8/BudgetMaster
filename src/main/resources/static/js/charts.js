$( document ).ready(function() {
    if($("#modalConfirmDelete").length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    if($(".datepicker").length)
    {
        var pickerStartDate = M.Datepicker.init(document.getElementById('chart-datepicker'), {
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

        return M.Datepicker.init(document.getElementById('chart-datepicker-end'), {
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
            formatSubmit: 'dd.mm.yyyy'
        });
    }
});