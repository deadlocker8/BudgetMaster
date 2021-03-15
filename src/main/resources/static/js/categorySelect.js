$(document).ready(function()
{
    let selectorCategorySelect = '.category-select-wrapper';

    if($(selectorCategorySelect).length)
    {
        selectedCategoryId = resetCustomSelectSelectedItemId(selectorCategorySelect);

        let customSelectTrigger = document.querySelector(selectorCategorySelect);
        customSelectTrigger.addEventListener('click', function()
        {
            selectedCategoryId = openCustomSelect(selectorCategorySelect);
        });

        customSelectTrigger.addEventListener("keydown", function(event)
        {
            if(event.key === "Escape")
            {
                closeCustomSelect(selectorCategorySelect);
            }

            let resultId = jumpToItemByFirstLetter(selectorCategorySelect, event.key);
            if(resultId !== null)
            {
                selectedCategoryId = resultId;
            }
        });

        for(const option of document.querySelectorAll(selectorCategorySelect + ' .custom-select-option'))
        {
            option.addEventListener('click', function(event)
            {
                selectedCategoryId = confirmCustomSelectItem(selectorCategorySelect, this);
                event.stopPropagation();
            })
        }

        window.addEventListener('click', function(e)
        {
            let categorySelect = document.querySelector('.custom-select')

            if(!categorySelect.contains(e.target))
            {
                closeCustomSelect(selectorCategorySelect);
                selectedCategoryId = resetCustomSelectSelectedItemId(selectorCategorySelect);
                removeSelectionStyleClassFromAll(selectorCategorySelect);
            }
        });

        enableCustomSelectHotKeys(selectorCategorySelect);
    }
});

let selectedCategoryId = null;

function openCustomSelect(selector)
{
    let trigger = document.querySelector(selector);
    trigger.querySelector('.custom-select').classList.toggle('open');
    let items = document.getElementsByClassName(selector + ' .custom-select-option');
    return selectCustomSelectItem(items, getIndexOfCustomSelectItemId(items, resetCustomSelectSelectedItemId(selector)));
}

function closeCustomSelect(selector)
{
    document.querySelector(selector + ' .custom-select').classList.remove('open');
}

function enableCustomSelectHotKeys(selector)
{
    Mousetrap.bind('up', function()
    {
        selectedCategoryId = handleCustomSelectKeyUpOrDown(selector, true);
    });

    Mousetrap.bind('down', function()
    {
        selectedCategoryId = handleCustomSelectKeyUpOrDown(selector, false);
    });

    Mousetrap.bind('enter', function()
    {
        if(isSearchFocused())
        {
            return;
        }

        if(isCustomSelectFocused())
        {
            selectedCategoryId = confirmCustomSelectSelection(selector, selectedCategoryId);
        }
        else
        {
            selectedCategoryId = openCustomSelect(selector);
        }
    });
}

function resetCustomSelectSelectedItemId(selector)
{
    let categorySelector = document.querySelector(selector + ' #custom-select-selected-item');
    return categorySelector.querySelector('.category-circle').dataset.value;
}

function handleCustomSelectKeyUpOrDown(selector, isUp)
{
    removeSelectionStyleClassFromAll(selector);

    let items = document.querySelectorAll(selector + ' .custom-select-option');
    let previousIndex = getIndexOfCustomSelectItemId(items, selectedCategoryId);

    // select next item
    if(isUp)
    {
        return selectNextCustomSelectItemOnUp(items, previousIndex);
    }
    else
    {
       return selectNextCustomSelectItemOnDown(items, previousIndex);
    }
}

function removeSelectionStyleClassFromAll(selector)
{
    let items = document.querySelectorAll(selector + ' .custom-select-option');
    for(let i = 0; i < items.length; i++)
    {
        toggleCustomSelectItemSelection(items[i], false);
    }
}

function getIndexOfCustomSelectItemId(items, id)
{
    for(let i = 0; i < items.length; i++)
    {
        let currentItemId = getCustomSelectItemId(items[i]);
        if(currentItemId === id)
        {
            return i;
        }
    }

    return null;
}

function getCustomSelectItemId(item)
{
    return item.dataset.value;
}

function selectCustomSelectItem(items, index)
{
    toggleCustomSelectItemSelection(items[index], true);
    items[index].scrollIntoView({
        behavior: 'smooth',
        block: 'start'
    });
    return getCustomSelectItemId(items[index]);
}

function toggleCustomSelectItemSelection(item, isSelected)
{
    item.classList.toggle('custom-select-option-hovered', isSelected);
}

function confirmCustomSelectSelection(selector, selectedId)
{
    let items = document.querySelectorAll(selector + ' .custom-select-option');
    let index = getIndexOfCustomSelectItemId(items, selectedId);
    return confirmCustomSelectItem(selector, items[index]);
}

function confirmCustomSelectItem(selector, item)
{
    // remove old selection
    item.parentNode.querySelector('.custom-select-option.selected').classList.remove('selected');

    item.classList.add('selected');

    let itemSelector = document.querySelector(selector + ' #custom-select-selected-item');
    itemSelector.innerHTML = item.innerHTML;

    let itemCircle = itemSelector.querySelector('.category-circle');
    itemCircle.classList.add('no-margin-left');
    itemCircle.dataset.value = item.dataset.value;

    document.querySelector(selector + ' .hidden-input-custom-select').value = item.dataset.value;

    removeSelectionStyleClassFromAll(selector);
    closeCustomSelect(selector);
    return resetCustomSelectSelectedItemId(selector);
}

function selectNextCustomSelectItemOnDown(items, previousIndex)
{
    let isLastItemSelected = previousIndex + 1 === items.length;
    if(isLastItemSelected)
    {
        return selectCustomSelectItem(items, 0);
    }
    else
    {
        return selectCustomSelectItem(items, previousIndex + 1);
    }
}

function selectNextCustomSelectItemOnUp(items, previousIndex)
{
    let isFirstItemSelected = previousIndex === 0;
    if(isFirstItemSelected)
    {
        return selectCustomSelectItem(items, items.length - 1);
    }
    else
    {
        return selectCustomSelectItem(items, previousIndex - 1);
    }
}

function jumpToItemByFirstLetter(selector, firstLetter)
{
    let items = document.querySelectorAll(selector + ' .custom-select-option');
    let index = getIndexOfCustomSelectItemStartingWithLetter(items, firstLetter);
    if(index !== null)
    {
        removeSelectionStyleClassFromAll(selector);
        return selectCustomSelectItem(items, index);
    }

    return null;
}

function getIndexOfCustomSelectItemStartingWithLetter(items, letter)
{
    for(let i = 0; i < items.length; i++)
    {
        let name = items[i].querySelector('.custom-select-item-name').textContent;
        if(name.toLowerCase().startsWith(letter.toLowerCase()))
        {
            return i;
        }
    }

    return null;
}
