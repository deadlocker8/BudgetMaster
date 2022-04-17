$(document).ready(function()
{
    getMigrationStatus();
});

function getMigrationStatus()
{
    console.log("ja")

    $.ajax({
        type: 'GET',
        url: $('#migration-status').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#migration-status').html(data);
        },
        complete: function()
        {
            // schedule the next request when the current one is complete
            setTimeout(getMigrationStatus, 3000);
        }
    });
}