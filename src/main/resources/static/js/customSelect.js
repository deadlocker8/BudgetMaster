$(document).ready(function()
{
    let allCustomSelects = [];

    let selectorCategorySelect = '.category-select-wrapper';
    if($(selectorCategorySelect).length)
    {
        let categorySelect = new CustomSelect(selectorCategorySelect);
        categorySelect.init();
        allCustomSelects.push(categorySelect);
    }

    let selectorAccountSelect = '.account-select-wrapper';
    if($(selectorAccountSelect).length)
    {
        let accountSelect = new CustomSelect(selectorAccountSelect);
        accountSelect.init();
        allCustomSelects.push(accountSelect);
    }

    let selectorTransferAccountSelect = '.transfer-account-select-wrapper';
    if($(selectorTransferAccountSelect).length)
    {
        let transferAccountSelect = new CustomSelect(selectorTransferAccountSelect);
        transferAccountSelect.init();
        allCustomSelects.push(transferAccountSelect);
    }

    let selectorAccountStateSelect = '.account-state-select-wrapper';
    if($(selectorAccountStateSelect).length)
    {
        let accountStateSelect = new CustomSelect(selectorAccountStateSelect);
        accountStateSelect.init();
        allCustomSelects.push(accountStateSelect);
    }

    let selectorGlobalAccountSelect = '.global-account-select-wrapper';
    if($(selectorGlobalAccountSelect).length)
    {
        let globalAccountSelect = new CustomSelect(selectorGlobalAccountSelect, function()
        {
            let accountID = document.querySelector(globalAccountSelect.getSelector() + ' .hidden-input-custom-select').value;
            window.location = rootURL + "/accounts/" + accountID + "/select";
        });
        globalAccountSelect.init();
        allCustomSelects.push(globalAccountSelect);
    }

    Mousetrap.bind('enter', function(event)
    {
        if(isSearchFocused())
        {
            return;
        }

        for(let i = 0; i < allCustomSelects.length; i++)
        {
            let currentCustomSelect = allCustomSelects[i];
            let currentDomItem = document.querySelector(currentCustomSelect.getSelector());
            if(currentDomItem.contains(event.target))
            {
                if(isCustomSelectFocused())
                {
                    currentCustomSelect.confirmSelection();
                }
                else
                {
                    if(!currentCustomSelect.isDisabled())
                    {
                        currentCustomSelect.open();
                    }
                }

                break;
            }
        }
    });
});

class CustomSelect
{
    constructor(selector, confirmSelectionCallback)
    {
        this.selector = selector;
        this.selectedId = null;
        this.confirmSelectionCallback = confirmSelectionCallback;
    }

    getSelector()
    {
        return this.selector;
    }

    init()
    {
        let self = this;
        let customSelectTrigger = document.querySelector(this.selector);
        customSelectTrigger.addEventListener('click', function()
        {
            if(!self.isDisabled())
            {
                self.open();
            }
        });

        customSelectTrigger.addEventListener("keydown", function(event)
        {
            if(event.key === "Escape")
            {
                self.close();
            }

            self.jumpToItemByFirstLetter(event.key);
        });

        for(const option of document.querySelectorAll(this.selector + ' .custom-select-option'))
        {
            option.addEventListener('click', function(event)
            {
                self.confirmItem(this);
                event.stopPropagation();
            })
        }

        this.resetSelectedItemId()

        customSelectTrigger.addEventListener('focusout', () => {
            this.close();
        });
    }

    isDisabled()
    {
        let customSelectTrigger = document.querySelector(this.selector);
        return customSelectTrigger.classList.contains("disabled");
    }

    open()
    {
        let trigger = document.querySelector(this.selector);
        trigger.querySelector('.custom-select').classList.toggle('open');
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        this.resetSelectedItemId();
        this.selectItem(items, this.getIndexOfItemById(items, this.selectedId));
        this.enableHotkeys();
    }

    close()
    {
        document.querySelector(this.selector + ' .custom-select').classList.remove('open');
        this.disableHotKeys();
        this.resetSelectedItemId();
        this.removeSelectionStyleClassFromAll();
    }

    enableHotkeys()
    {
        let self = this;
        Mousetrap.bind('up', function()
        {
            self.handleKeyUpOrDown(true);
        });

        Mousetrap.bind('down', function()
        {
            self.handleKeyUpOrDown(false);
        });
    }

    disableHotKeys()
    {
        Mousetrap.unbind('up');
        Mousetrap.unbind('down');
    }

    resetSelectedItemId()
    {
        let itemSelector = document.querySelector(this.selector + ' .custom-select-selected-item');
        this.selectedId = itemSelector.querySelector('.category-circle').dataset.value;
    }

    handleKeyUpOrDown(isUp)
    {
        this.removeSelectionStyleClassFromAll();

        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let previousIndex = this.getIndexOfItemById(items, this.selectedId);

        // select next item
        if(isUp)
        {
            this.selectNextItemOnUp(items, previousIndex);
        }
        else
        {
            this.selectNextItemOnDown(items, previousIndex);
        }
    }

    removeSelectionStyleClassFromAll()
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        for(let i = 0; i < items.length; i++)
        {
            this.toggleItemSelection(items[i], false);
        }
    }

    getIndexOfItemById(items, id)
    {
        for(let i = 0; i < items.length; i++)
        {
            let currentItemId = this.getItemId(items[i]);
            if(currentItemId === id)
            {
                return i;
            }
        }

        return null;
    }

    getItemId(item)
    {
        return item.dataset.value;
    }

    selectItem(items, index)
    {
        this.toggleItemSelection(items[index], true);
        items[index].scrollIntoView({
            behavior: 'smooth',
            block: 'nearest'
        });
        this.selectedId = this.getItemId(items[index]);
    }

    toggleItemSelection(item, isSelected)
    {
        item.classList.toggle('custom-select-option-hovered', isSelected);
    }

    confirmSelection()
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let index = this.getIndexOfItemById(items, this.selectedId);
        this.confirmItem(items[index]);
    }

    confirmItem(item)
    {
        // remove old selection
        item.parentNode.querySelector('.custom-select-option.selected').classList.remove('selected');

        item.classList.add('selected');

        let itemSelector = document.querySelector(this.selector + ' .custom-select-selected-item');
        itemSelector.innerHTML = item.innerHTML;

        let itemCircle = itemSelector.querySelector('.category-circle');
        itemCircle.classList.add('no-margin-left');
        itemCircle.dataset.value = item.dataset.value;

        document.querySelector(this.selector + ' .hidden-input-custom-select').value = item.dataset.value;

        this.removeSelectionStyleClassFromAll();
        this.close(this.selector);
        this.selectedId = this.resetSelectedItemId();

        if(this.confirmSelectionCallback && typeof (this.confirmSelectionCallback) == "function")
        {
            this.confirmSelectionCallback();
        }
    }

    selectNextItemOnDown(items, previousIndex)
    {
        let isLastItemSelected = previousIndex + 1 === items.length;
        if(isLastItemSelected)
        {
            this.selectItem(items, 0);
        }
        else
        {
            this.selectItem(items, previousIndex + 1);
        }
    }

    selectNextItemOnUp(items, previousIndex)
    {
        let isFirstItemSelected = previousIndex === 0;
        if(isFirstItemSelected)
        {
            this.selectItem(items, items.length - 1);
        }
        else
        {
            this.selectItem(items, previousIndex - 1);
        }
    }

    jumpToItemByFirstLetter(firstLetter)
    {
        let items = document.querySelectorAll(this.selector + ' .custom-select-option');
        let index = this.getIndexOfCustomSelectItemStartingWithLetter(items, firstLetter);
        if(index !== null)
        {
            this.removeSelectionStyleClassFromAll(this.selector);
            this.selectItem(items, index);
        }
    }

    getIndexOfCustomSelectItemStartingWithLetter(items, letter)
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

}
