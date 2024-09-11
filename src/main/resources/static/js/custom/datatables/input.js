"use strict";

/*
 * Shared variables
 */
const element = document.getElementById('kt_modal_input');
const form = element.querySelector('#kt_modal_input_form');
const modal = new bootstrap.Modal(element);
/*
 * Class definition
 */
var KTInputForm = function () {

    // Init add schedule modal
    var initInputForm = () => {
        handleCloseButton(element, modal);
        handleCancelButton(element, form, modal);
        handleFormSubmit(element, form, validator, modal);

        // Define variables
        if (form) {
            const selectAll = form.querySelector('#kt_select_all');
            if (selectAll) {
                const allCheckboxes = form.querySelectorAll('[type="checkbox"]');
                // Handle check state
                selectAll.addEventListener('change', e => {

                    // Apply check state to all checkboxes
                    allCheckboxes.forEach(c => {
                        c.checked = e.target.checked;
                    });
                });
            }
        }
    };

    return {
        // Public functions
        init: function () {
            initInputForm();
        }
    };
}();

// On document ready
KTUtil.onDOMContentLoaded(function () {
    KTInputForm.init();
});