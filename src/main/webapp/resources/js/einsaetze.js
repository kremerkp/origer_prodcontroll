/* Erfassung */

var first_responder_tabindex = 8;
var brandschutz_tabindex = 6;
var homepage_tabindex = 7;
var material_tabindex = 5;
var arbeitsbericht_tabindex = 1;

function calculateDuration() {
    var anfang = new Date($('div.datum-anfang').val());
    var ende = new Date($('div.datum-ende').val());
    var diffMs = (ende - anfang); // milliseconds between now & Christmas
    var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000); // minutes
}

function checkForFirstResponder() {
    var selectedValue = PF('w_berichtsart').jq.find(':selected').val();
    console.log(selectedValue);
    if (selectedValue === 'lu.cimw.models.EinsaetzeBerichtsArten[ id=3 ]') {
    	console.log('yes');
        PF('w_tabView').enable(first_responder_tabindex);
        PF('w_tabView').disable(arbeitsbericht_tabindex);
        PF('w_tabView').disable(material_tabindex);
        PF('w_tabView').disable(homepage_tabindex);
        PF('w_tabView').disable(brandschutz_tabindex);
        PF('w_tabView').select(first_responder_tabindex);
        PF('w_tabView').enable(first_responder_tabindex);
        console.log(selectedValue);
    } else {
    	console.log('no');
        PF('w_tabView').disable(first_responder_tabindex);
        PF('w_tabView').enable(arbeitsbericht_tabindex);
        PF('w_tabView').enable(material_tabindex);
        PF('w_tabView').enable(homepage_tabindex);
        PF('w_tabView').enable(brandschutz_tabindex);
        if (PF('w_tabView').getActiveIndex() === first_responder_tabindex) {
            PF('w_tabView').select(0);
        }
    }
}

function maximazeTab(index) {
    if (index === 7) {
        $('#upper-content').hide();
    } else {
        $('#upper-content').show();
    }
}

$(document).ready(function () {
    if ($('div.datum-anfang').val() !== '' && $('div.datum-ende').val() !== '') {
        calculateDuration();
    }
});