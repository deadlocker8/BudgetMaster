let chartPickerStartDate;
let chartPickerEndDate;

$(document).ready(function()
{
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

    if($(".datepicker").length)
    {
        chartPickerStartDate = M.Datepicker.init(document.getElementById('chart-datepicker'), {
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
                if(typeof chartPickerEndDate !== "undefined")
                {
                    let selectedDate = chartPickerEndDate.date;
                    chartPickerEndDate.destroy();
                    chartPickerEndDate = createDatePickerEnd(this.date, selectedDate);
                }
            }
        });

        // picker end date
        if(typeof endDate !== "undefined")
        {
            chartPickerEndDate = createDatePickerEnd(chartPickerStartDate.date, endDate);
        }
    }

    $(".filter-button-reset").click(function()
    {
        resetFilter();
        applyFilter();
    });

    $(".quick-date").click(function()
    {
        handleQuickDate(this);
    });

    $('.button-display-type').click(function()
    {
        toggleChartTypeButtons('button-display-type', this);
        document.getElementsByName('displayType')[0].value = this.dataset.value;
        filterChartPreviews();
    });

    $('.button-group-type').click(function()
    {
        toggleChartTypeButtons('button-group-type', this);
        document.getElementsByName('groupType')[0].value = this.dataset.value;
        filterChartPreviews();
    });

    $('.chart-preview-column').click(function()
    {
        unsetActiveChartPreview();

        this.querySelector('.chart-preview').classList.toggle('active', true);
        document.getElementsByName('chartID')[0].value = this.dataset.id;
        checkShowChartButton();
    });

    let filterCheckBoxes = document.querySelectorAll('#filterSettings input[type=checkbox]');
    for(let i = 0; i < filterCheckBoxes.length; i++)
    {
        filterCheckBoxes[i].addEventListener('change', (event) =>
        {
            applyFilter();
        });
    }

    $('#filter-name').on('change keydown paste input', function()
    {
        applyFilter();
    });

    filterChartPreviews();
});

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

function applyFilter()
{
    let badge = document.getElementById("filterActiveBadge");
    badge.classList.toggle("hidden", isDefaultFilter());
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

function handleQuickDate(element)
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
            startDate = moment().subtract(1, 'weeks').startOf('isoWeek');
            endDate = moment().subtract(1, 'weeks').endOf('isoWeek');
            break;
        case '5':
            startDate = moment().subtract(1, 'months').startOf('month');
            endDate = moment().subtract(1, 'months').endOf('month');
            break;
        case '6':
            startDate = moment().subtract(1, 'years').startOf('year');
            endDate = moment().subtract(1, 'years').endOf('year');
            break;
        case '7':
            startDate = moment("2000-01-01");
            endDate = moment().subtract(1, 'years').endOf('year');
            break;
        case '8':
            startDate = moment().subtract(7, 'days');
            endDate = moment();
            break;
        case '9':
            startDate = moment().subtract(30, 'days');
            endDate = moment();
            break;
        case '10':
            startDate = moment().subtract(365, 'days');
            endDate = moment();
            break;
        case '11':
            startDate = moment("2000-01-01");
            endDate = moment();
            break;
    }

    setDateRange(startDate, endDate);
}

function setDateRange(startDate, endDate)
{
    chartPickerStartDate.setDate(startDate.startOf('day').toDate());
    chartPickerStartDate.setInputValue();

    chartPickerEndDate.destroy();
    chartPickerEndDate = createDatePickerEnd(chartPickerStartDate.date, endDate.startOf('day').toDate());
}

function toggleChartTypeButtons(styleClassName, item)
{
    let siblings = document.getElementsByClassName(styleClassName);
    for(let i = 0; i < siblings.length; i++)
    {
        siblings[i].classList.toggle('active', false);
    }

    item.classList.toggle('active', true);
}

function filterChartPreviews()
{
    let displayTypeName = document.querySelector('.button-display-type.active').dataset.value;
    let groupTypeName = document.querySelector('.button-group-type.active').dataset.value;

    let allChartPreviews = document.getElementsByClassName('chart-preview-column');
    for(let i = 0; i < allChartPreviews.length; i++)
    {
        allChartPreviews[i].classList.toggle('hidden', true);
    }

    let chartPreviews = document.querySelectorAll('.chart-preview-column[data-display-type="' + displayTypeName + '"][data-group-type="' + groupTypeName + '"]');
    for(let i = 0; i < chartPreviews.length; i++)
    {
        chartPreviews[i].classList.toggle('hidden', false);
    }

    unsetActiveChartPreview();

    toggleCustomChartButton(displayTypeName === 'CUSTOM');
}

function unsetActiveChartPreview()
{
    let allChartPreviews = document.getElementsByClassName('chart-preview');
    for(let i = 0; i < allChartPreviews.length; i++)
    {
        allChartPreviews[i].classList.toggle('active', false);
    }

    document.getElementsByName('chartID')[0].value = '';
    checkShowChartButton();
}

function checkShowChartButton()
{
    let buttonShowChart = document.getElementsByName('buttonSave')[0];

    let selectedChartID = document.getElementsByName('chartID')[0].value;
    buttonShowChart.disabled = selectedChartID === '';
}

function toggleCustomChartButton(show)
{
    document.getElementById('buttonCustomCharts').classList.toggle('hidden', !show);
}
