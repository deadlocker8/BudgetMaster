$(document).ready(function()
{
    if($('.category-select-wrapper').length)
    {
        let categorySelectTrigger = document.querySelector('.category-select-wrapper');
        categorySelectTrigger.addEventListener('click', function()
        {
            openCategorySelect('.category-select-wrapper');
        });

        categorySelectTrigger.addEventListener("keydown", function(event)
        {
            if(event.key === "Escape")
            {
                closeCustomSelect('.category-select-wrapper');
            }

            let resultId = jumpToItemByFirstLetter('.category-select-wrapper', event.key);
            if(resultId !== null)
            {
                selectedCategoryId = resultId;
            }
        });

        for(const option of document.querySelectorAll(".category-select-option"))
        {
            option.addEventListener('click', function(event)
            {
                confirmCustomSelectItem(this);
                event.stopPropagation();
            })
        }

        window.addEventListener('click', function(e)
        {
            let categorySelect = document.querySelector('.custom-select')

            if(!categorySelect.contains(e.target))
            {
                closeCustomSelect('.category-select-wrapper');
                resetSelectedCategoryId();
                removeSelectionStyleClassFromAll('.category-select-wrapper');
            }
        });

        enableCategorySelectHotKeys();
    }
});

function openCategorySelect(selector)
{
    let categorySelectTrigger = document.querySelector(selector);
    categorySelectTrigger.querySelector('.custom-select').classList.toggle('open');
    let categoryItems = document.getElementsByClassName('category-select-option');
    selectedCategoryId = selectCustomSelectItem(categoryItems, getIndexOfCustomSelectItemId(categoryItems, resetSelectedCategoryId()));
}

function closeCustomSelect(selector)
{
    document.querySelector(selector + ' .custom-select').classList.remove('open');
}

function enableCategorySelectHotKeys()
{
    Mousetrap.bind('up', function()
    {
        selectedCategoryId = handleCategorySelectKeyUpOrDown('.category-select-wrapper', true);
    });

    Mousetrap.bind('down', function()
    {
        selectedCategoryId = handleCategorySelectKeyUpOrDown('.category-select-wrapper', false);
    });

    Mousetrap.bind('enter', function()
    {
        if(isSearchFocused())
        {
            return;
        }

        if(isCategorySelectFocused())
        {
            confirmCustomSelectSelection('.category-select-wrapper', selectedCategoryId);
        }
        else
        {
            openCategorySelect('.category-select-wrapper');
        }
    });
}

let selectedCategoryId = null;
if($('.category-select-wrapper').length)
{
    resetSelectedCategoryId();
}

function resetSelectedCategoryId()
{
    let categorySelector = document.querySelector('#category-select-selected-category');
    let categoryId = categorySelector.querySelector('.category-circle').dataset.value;
    selectedCategoryId = categoryId
    return categoryId;
}

function handleCategorySelectKeyUpOrDown(selector, isUp)
{
    removeSelectionStyleClassFromAll(selector);

    let items = document.querySelectorAll(selector + ' .category-select-option');
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
    let items = document.querySelectorAll(selector + ' .category-select-option');
    for(let i = 0; i < items.length; i++)
    {
        toggleCustomSelectItemSelection(items[i], false);
    }
}

function getIndexOfCustomSelectItemId(items, id)
{
    for(let i = 0; i < items.length; i++)
    {
        let currentCategoryId = getCustomSelectItemId(items[i]);
        if(currentCategoryId === id)
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
    let items = document.querySelectorAll(selector + ' .category-select-option');
    let index = getIndexOfCustomSelectItemId(items, selectedId);
    confirmCustomSelectItem(items[index]);
}

function confirmCustomSelectItem(item)
{
    // remove old selection
    item.parentNode.querySelector('.category-select-option.selected').classList.remove('selected');

    item.classList.add('selected');

    let categorySelector = document.querySelector('#category-select-selected-category');
    categorySelector.innerHTML = item.innerHTML;

    let categoryCircle = categorySelector.querySelector('.category-circle');
    categoryCircle.classList.add('no-margin-left');
    categoryCircle.dataset.value = item.dataset.value;

    document.getElementById('hidden-input-category').value = item.dataset.value;

    resetSelectedCategoryId();
    removeSelectionStyleClassFromAll('.category-select-wrapper');
    closeCustomSelect('.category-select-wrapper');
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
    let items = document.querySelectorAll(selector + ' .category-select-option');
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
