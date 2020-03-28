$(document).ready(function()
{
    let modalFilter;
    let pickerStartDate;
    let pickerEndDate;

    if($("#chart-script").length)
    {
        let editor = CodeMirror.fromTextArea(document.getElementById('chart-script'), {
            mode: "javascript",
            lineNumbers: 50,
            viewportMargin: Infinity
        });
        editor.save();
    }

    if($("#modalConfirmDelete").length)
    {
        $('#modalConfirmDelete').modal('open');
    }

    if($("#modalFilter").length)
    {
        modalFilter = $('#modalFilter').modal();
    }

    if($(".datepicker").length)
    {
        pickerStartDate = M.Datepicker.init(document.getElementById('chart-datepicker'), {
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
            pickerEndDate = createDatePickerEnd(pickerStartDate.date, endDate);
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

    $(".filter-button-close").click(function()
    {
        applyFilter(modalFilter);
    });

    $(".filter-button-reset").click(function()
    {
        resetFilter();
        applyFilter(modalFilter);
    });

    $(".quick-date").click(function()
    {
        handleQuickDate(this, pickerStartDate, pickerEndDate);
    });
});

function applyFilter(modal)
{
    let filterButton = document.getElementById("modalFilterTrigger");

    if(isDefaultFilter())
    {
        filterButton.classList.toggle("budgetmaster-blue", true);
        filterButton.classList.toggle("budgetmaster-red", false);
        filterButton.childNodes[1].nodeValue = filterNotActive;
    } else
    {
        filterButton.classList.toggle("budgetmaster-blue", false);
        filterButton.classList.toggle("budgetmaster-red", true);
        filterButton.childNodes[1].nodeValue = filterActive;
    }

    modal.modal('close');
}

function isDefaultFilter()
{
    let allCheckBoxesChecked = $("#filterSettings input[type=checkbox]:checked").length === $("#filterSettings input[type=checkbox]").length;
    let textInputEmpty = $("#filter-name").val().length === 0;
    return allCheckBoxesChecked && textInputEmpty
}

function resetFilter()
{
    $("#filterSettings input[type=checkbox]").prop('checked', true);
    $("#filter-name").val('');
}

function handleQuickDate(element, pickerStartDate, pickerEndDate)
{
    let quickType = element.dataset.quick;
    let startDate;
    let endDate;

    switch(quickType)
    {
        case '0':
            startDate = moment().startOf('isoWeek');
            endDate = moment().endOf('isoWeek');
            break;
        case '1':
            startDate = moment().startOf('month');
            endDate = moment().endOf('month');
            break;
        case '2':
            startDate = moment().startOf('year');
            endDate = moment().endOf('year');
            break;
        case '3':
            startDate = moment("2000-01-01");
            endDate = moment("2100-01-01");
            break;
        case '4':
            startDate = moment().subtract(7, 'days');
            endDate = moment();
            break;
        case '5':
            startDate = moment().subtract(30, 'days');
            endDate = moment();
            break;
        case '6':
            startDate = moment().subtract(365, 'days');
            endDate = moment();
            break;
        case '7':
            startDate = moment("2000-01-01");
            endDate = moment();
            break;
    }

    setDateRange(startDate, endDate, pickerStartDate, pickerEndDate);
}

function setDateRange(startDate, endDate, pickerStartDate, pickerEndDate)
{
    pickerStartDate.setDate(startDate.toDate());
    pickerStartDate.setInputValue();

    pickerEndDate.setDate(endDate.toDate());
    pickerEndDate.setInputValue();
}