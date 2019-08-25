$(document).ready(function()
{
    $('.page-link').click(function()
    {
        if(!this.parentElement.classList.contains('disabled'))
        {
            var page = this.dataset.page;
            var inputPageNumber = document.getElementById('inputPageNumber');
            inputPageNumber.value = page;
            document.getElementById('searchForm').submit();
        }
    });
});