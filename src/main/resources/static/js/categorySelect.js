$( document ).ready(function() {
    if($("#transaction-category").length)
    {
        beautifyCategorySelect();
    }
});

function beautifyCategorySelect() {
    var counter = 0;

    var select = M.FormSelect.init(document.getElementById('transaction-category'), {
        dropdownOptions: {
            onCloseStart: function () {
                var listItems = select.dropdownOptions.childNodes;
                var selectedItem;
                for(var i = 0; i < listItems.length; i++)
                {
                    var currentItem = listItems[i];
                    if(currentItem.classList.contains("selected"))
                    {
                        selectedItem = currentItem.textContent;
                        break;
                    }
                }
                select.input.value = selectedItem;
            }
        }
    });

    select.dropdownOptions.childNodes.forEach(function (item) {
        var currentSpan = jQuery(item.querySelector('span'));
        var categoryInfo = currentSpan.text().split("@@@");
        var categoryName = categoryInfo[0];
        var firstLetter = capitalizeFirstLetter(categoryName);
        var categoryColor = categoryInfo[1];
        var appropriateTextColor = categoryInfo[2];

        currentSpan.text(categoryName);
        currentSpan.data("infos", categoryInfo);
        currentSpan.addClass("category-select");
        currentSpan.parent().prepend('<div class="category-circle-small category-select" id="category-' + counter + '" style="background-color: ' + categoryColor + '"><span></span></div>');
        $('#categoryWrapper').parent().append('<style>#category-' + counter + ':after{content: "' + firstLetter + '"; color: ' + appropriateTextColor + ';}</style>');

        currentSpan.click(function () {
            select.input.value = categoryName;
        });
        counter++;
    });

    // select current category from code again in order to avoid showing the full infos text (e.g. Test@@@#FFFFFF@#000000@@@1) in the input field by materialize
    if(typeof selectedCategory !== 'undefined')
    {
        var listItems = select.dropdownOptions.childNodes;
        for(var i = 0; i < listItems.length; i++)
        {
            var currentSpan = jQuery(listItems[i].querySelector('span.category-select'));
            var categoryID = currentSpan.data("infos")[3];
            if(categoryID === selectedCategory)
            {
                currentSpan.trigger("click");
                break;
            }
        }
    }
}

function capitalizeFirstLetter(text)
{
    return text.charAt(0).toUpperCase();
}