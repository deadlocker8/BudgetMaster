$(document).ready(function()
{
    getMigrationStatus();
});

function getMigrationStatus()
{
    if(typeof migrationStatus !== 'undefined')
    {
        if(migrationStatus === 'SUCCESS' || migrationStatus === 'ERROR')
        {
            return;
        }
    }

    $.ajax({
        type: 'GET',
        url: $('#migration-status').attr('data-url'),
        data: {},
        success: function(data)
        {
            $('#migration-status').html(data);
            $('.collapsible').collapsible();
        },
        complete: function()
        {
            // schedule the next request when the current one is complete
            setTimeout(getMigrationStatus, 3000);
        }
    });
}