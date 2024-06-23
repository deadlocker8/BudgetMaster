$(document).ready(function()
{
    if($('#modalAccountNotDeletable').length)
    {
        $('#modalAccountNotDeletable').modal('open');
    }

    if($('#account-name').length)
    {
        document.getElementById('account-name').focus();
    }

    $('#account-name').on('change keydown paste input', function()
    {
        let accountName = $(this).val();
        document.getElementById('item-icon-fallback-name').innerText = accountName.charAt(0).toUpperCase();
    });

    $('.button-request-delete-account').click(function()
    {
        fetchAndShowModalContent(this.dataset.url, '#deleteModalContainerOnDemand', '#modalConfirmDelete', function(){});
    });

    if($(".datepicker").length)
    {
        M.Datepicker.init(document.getElementById('account-datepicker'), {
            firstDay: 1,
            showClearBtn: true,
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
        });
    }
});
