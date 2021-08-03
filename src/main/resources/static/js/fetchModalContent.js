function fetchAndShowModalContent(url, containerID, modalID, callback)
{
    let modal = $(modalID).modal();
    if(modal.isOpen)
    {
        return;
    }

    $.ajax({
        type: 'GET',
        url: url,
        data: {},
        success: function(data)
        {
            $(containerID).html(data);
            $(modalID).modal();
            $(modalID).modal('open');
            callback();
        }
    });
}