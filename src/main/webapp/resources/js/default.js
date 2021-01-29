var max_dialog_height;

$(document).ready(function () {
    $('body').on('keydown', 'input.ui-state-error', function () {
        $(this).removeClass('ui-state-error');
    });

    // Open SelectOneMenu on focus
    $('frmEinsaetzeMaterialTypen\:j_id_t_input').focus(function () {
        console.log("Trigger");
    });

    $("form input").bind("keypress", function (e) {
        if (e.keyCode === 13) {
            return false;
        }
    });

    $('input.default-focus').focus().select();

    max_dialog_height = $(window).height() - 200;
    $('div.ui-dialog div.ui-dialog-content').css('max-height', max_dialog_height);

    $(window).resize(function () {
        max_dialog_height = $(window).height() - 200;
        $('div.ui-dialog div.ui-dialog-content').css('max-height', max_dialog_height);
    });

});

function markTopMenuActive(className) {
    $('#top ul li a.' + className).parent('li').addClass('active');
}

function markLeftMenuActive(className) {
    $('#left ul li a.' + className).parent('li').addClass('active');
}

function resetSelectOneComponent(listBox) {
    listBox.filterInput.val('');
    listBox.listElement.find('li').show();
    listBox.filterInput.focus();
}

function triggerButtonForListBox(listBox, button, event) {
    if (event.keyCode === 13 && listBox.listElement.find('li:visible').length === 1) {
        listBox.selectItem(listBox.listElement.find('li:visible'));
        button.jq.click();
        resetSelectOneComponent();
    }
}

function addPickListEnterEvent(className) {
    var table = $('table.' + className);
    var sourceFilterInput = table.find('input[id$=source_filter]');
    sourceFilterInput.keypress(function (event) {
        if (event.keyCode === 13 && table.find('ul.ui-picklist-source li:visible').length === 1 && sourceFilterInput.val() !== '') {
            table.find('ul.ui-picklist-source li:visible').addClass('ui-state-highlight');
            table.find('button.ui-picklist-button-add').click();
            sourceFilterInput.val('');
        }
    });
}

function deactivateTopMenu() {
    $('#top ul li a').css('cursor', 'no-drop').css('background', 'inherit').click(function () {
        return false;
    });
    $('#top ul li:last-child').hide();
}

function deactivateLeftMenu() {
    $('#left ul li a').css('cursor', 'no-drop').css('background', 'inherit').click(function () {
        return false;
    });
}



PrimeFaces.locales['de'] = {
    closeText: 'Schließen',
    prevText: 'Zurück',
    nextText: 'Weiter',
    monthNames: ['Januar', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
    monthNamesShort: ['Jan', 'Feb', 'März', 'Apr', 'Mai', 'Juni', 'Juli', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
    dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
    dayNamesShort: ['Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam'],
    dayNamesMin: ['S', 'M', 'D', 'M ', 'D', 'F ', 'S'],
    weekHeader: 'KW',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: true,
    yearSuffix: '',
    timeOnlyTitle: 'Nur Zeit',
    timeText: 'Zeit',
    hourText: 'Stunde',
    minuteText: 'Minute',
    secondText: 'Sekunde',
    currentText: 'Aktuelles Datum',
    ampm: false,
    month: 'Monat',
    week: 'KW',
    day: 'Tag',
    allDayText: 'Ganzer Tag',
    titleFormat: {
        month: 'YYYY'
    }
};

