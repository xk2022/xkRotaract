"use strict";

// Class definition
var KTDataTablesList = function () {
    // Define shared variables
    var datatable;
    var table;
    var toolbarBase;
    var toolbarSelected;
    var selectedCount;
    var template;

    // Init add schedule modal
    var initTable = () => {
        // Set date data order
        const tableRows = table.querySelectorAll('tbody tr');

        tableRows.forEach(row => {
            const dateRow = row.querySelectorAll('td');

        });

        // Get subtable template
        const subtable = document.querySelector('[data-kt-docs-datatable-subtable="subtable_template"]');
        if(subtable) {
            template = subtable.cloneNode(true);
            template.classList.remove('d-none');

            // Remove subtable template
            subtable.parentNode.removeChild(subtable);
        }

//        const init_order = $(".init_order").attr('id').replace('order_', '');
        // Init datatable --- more info on datatables: https://datatables.net/manual/
        datatable = $(table).DataTable({
            "info": false,
            // error
            "order": [
//                [init_order, 'desc']
            ], // Initial ordering by column 0 ascending and then by column 1 descending
            "pageLength": 10,
            "lengthChange": false,
            'ordering': false,
            'paging': false,
            // select: true,
            "scrollY": 750,
            "scrollX": true,
            'columnDefs': [
                { orderable: false, targets: 0 }, // Disable ordering on column 0 (assigned)
                { orderable: false, targets: 1 }, // Disable ordering on column 1 (assigned)
                { orderable: false, targets: 3 }, // Disable ordering on column 3 (actions)
            ]
        });

        // Re-init functions on every table re-draw -- more info: https://datatables.net/reference/event/draw
        datatable.on('draw', function () {
            initToggleToolbar();
            handleDeleteRows();
            toggleToolbars();
            handleChangeSysDatatable();
            resetSubtable();
            handleActionButton();
        });
    }

    // Search Datatable --- official docs reference: https://datatables.net/reference/api/search()
    var handleChangeSysDatatable = () => {
        $('.selectMode').change(function (e) {
            // 讓 index selectmode 對準 columns
            const column_index = $(this).attr('id').replace('selectMode_', '');

            datatable.columns(column_index).search($(this).val()).draw();
        });
    }

    // Search Datatable --- official docs reference: https://datatables.net/reference/api/search()
    var handleSearchDatatable = () => {
        const filterSearch = document.querySelector('[data-kt-table-filter="search"]');
        filterSearch.addEventListener('keyup', function (e) {
            datatable.search(e.target.value).draw();
        });
    }

    // Update
    var handleUpdateRows = () => {
        // Select all update buttons
        const updateButtons = table.querySelectorAll('[data-kt-table-filter="update_row"]');

        updateButtons.forEach(d => {
            // Update button on click
            d.addEventListener('click', function (e) {
                e.preventDefault();
                // Select parent row
                const parent = e.target.closest('tr');
                const row = parent.querySelectorAll('td');

                readyToEdit(row);
            })
        });
    }

    // Delete user
    var handleDeleteRows = () => {
        // Select all delete buttons
        const deleteButtons = table.querySelectorAll('[data-kt-table-filter="delete_row"]');

        deleteButtons.forEach(d => {
            // Delete button on click
            d.addEventListener('click', function (e) {
                e.preventDefault();

                // Select parent row
                const parent = e.target.closest('tr');

                // Get permission name
                const id = parent.querySelectorAll('td')[1].textContent;
                const rowSec = parent.querySelectorAll('td')[2].textContent;

                // SweetAlert2 pop up --- official docs reference: https://sweetalert2.github.io/
                Swal.fire({
                    text: "Are you sure you want to delete data 「 " + rowSec + " 」 ?",
                    icon: "warning",
                    showCancelButton: true,
                    buttonsStyling: false,
                    confirmButtonText: "Yes, delete!",
                    cancelButtonText: "No, cancel",
                    customClass: {
                        confirmButton: "btn fw-bold btn-danger",
                        cancelButton: "btn fw-bold btn-active-light-primary"
                    }
                }).then(function (result) {
                    if (result.value) {
                        Swal.fire({
                            text: "You have deleted data " + id + " !.",
                            icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Ok, got it!",
                            customClass: {
                                confirmButton: "btn fw-bold btn-primary",
                            }
                        }).then(function () {
                            // Remove current row
//                            datatable.row($(parent)).remove().draw();
                            $.ajax({
                                url: deleteUrl + id ,
                                type: 'GET',
                                success: function(result) {
                                    // Do something with the result
                                    location.reload();
                                }
                            });
                        });
                    } else if (result.dismiss === 'cancel') {
                        Swal.fire({
                            text: customerName + " was not deleted.",
                            icon: "error",
                            buttonsStyling: false,
                            confirmButtonText: "Ok, got it!",
                            customClass: {
                                confirmButton: "btn fw-bold btn-primary",
                            }
                        });
                    }
                });
            })
        });
    }

    // Init toggle toolbar
    var initToggleToolbar = () => {
        // Toggle selected action toolbar
        // Select all checkboxes
        const checkboxes = table.querySelectorAll('[type="checkbox"]');

        // Select elements
        toolbarBase = document.querySelector('[data-kt-table-toolbar="base"]');
        toolbarSelected = document.querySelector('[data-kt-table-toolbar="selected"]');
        selectedCount = document.querySelector('[data-kt-table-select="selected_count"]');
        const deleteSelected = document.querySelector('[data-kt-table-select="delete_selected"]');

        // Toggle delete selected toolbar
        checkboxes.forEach(c => {
            // Checkbox on click event
            c.addEventListener('click', function () {
                setTimeout(function () {
                    toggleToolbars();
                }, 50);
            });
        });

        if (deleteSelected) {
            // Deleted selected rows
            deleteSelected.addEventListener('click', function () {
                    // SweetAlert2 pop up --- official docs reference: https://sweetalert2.github.io/
                    Swal.fire({
                        text: "Are you sure you want to delete selected customers?",
                        icon: "warning",
                        showCancelButton: true,
                        buttonsStyling: false,
                        confirmButtonText: "Yes, delete!",
                        cancelButtonText: "No, cancel",
                        customClass: {
                            confirmButton: "btn fw-bold btn-danger",
                            cancelButton: "btn fw-bold btn-active-light-primary"
                        }
                    }).then(function (result) {
                        if (result.value) {
                            Swal.fire({
                                text: "You have deleted all selected customers!.",
                                icon: "success",
                                buttonsStyling: false,
                                confirmButtonText: "Ok, got it!",
                                customClass: {
                                    confirmButton: "btn fw-bold btn-primary",
                                }
                            }).then(function () {
                                // Remove all selected customers
                                checkboxes.forEach(c => {
                                    if (c.checked) {
                                        datatable.row($(c.closest('tbody tr'))).remove().draw();
                                    }
                                });

                                // Remove header checked box
                                const headerCheckbox = table.querySelectorAll('[type="checkbox"]')[0];
                                headerCheckbox.checked = false;
                            }).then(function () {
                                toggleToolbars(); // Detect checked checkboxes
                                initToggleToolbar(); // Re-init toolbar to recalculate checkboxes
                            });
                        } else if (result.dismiss === 'cancel') {
                            Swal.fire({
                                text: "Selected customers was not deleted.",
                                icon: "error",
                                buttonsStyling: false,
                                confirmButtonText: "Ok, got it!",
                                customClass: {
                                    confirmButton: "btn fw-bold btn-primary",
                                }
                            });
                        }
                    });
            });
        }
    }

    // Toggle toolbars
    const toggleToolbars = () => {
        // Select refreshed checkbox DOM elements
        const allCheckboxes = table.querySelectorAll('tbody [type="checkbox"]');

        // Detect checkboxes state & count
        let checkedState = false;
        let count = 0;

        // Count checked boxes
        allCheckboxes.forEach(c => {
            if (c.checked) {
                checkedState = true;
                count++;
            }
        });

        // Toggle toolbars
        if (checkedState) {
            selectedCount.innerHTML = count;
            toolbarBase.classList.add('d-none');
            toolbarSelected.classList.remove('d-none');
        } else {
            toolbarBase.classList.remove('d-none');
            toolbarSelected.classList.add('d-none');
        }
    }

    // Handle action button
        const handleActionButton = () => {
            const buttons = document.querySelectorAll('[data-kt-docs-datatable-subtable="expand_row"]');

            // Sample row items counter --- for demo purpose only, remove this variable in your project
            const rowItems = [4, 1, 5, 1, 4, 2];

            buttons.forEach((button, index) => {
                button.addEventListener('click', e => {
                    e.stopImmediatePropagation();
                    e.preventDefault();

                    const row = button.closest('tr');
                    const rowClasses = ['isOpen', 'border-bottom-0'];
                    const rowId = row.querySelector('.id input[type="checkbox"]').value;

                    // Get total number of items to generate --- for demo purpose only, remove this code snippet in your project
//                    const demoData = [];
//                    for (var j = 0; j < rowItems[index]; j++) {
//                        demoData.push(data[j]);
//                    }
                    // End of generating demo data

                    // Handle subtable expanded state
                    if (row.classList.contains('isOpen')) {
                        // Remove all subtables from current order row
//                        while (row.nextSibling && row.nextSibling.getAttribute('data-kt-docs-datatable-subtable') === 'subtable_template') {
////                            row.nextSibling.parentNode.removeChild(row.nextSibling);
//                        }
                        $('.subtable_template_'+rowId).css('display', 'none');
                        row.classList.remove(...rowClasses);
                        button.classList.remove('active');
                    } else {
//                        populateTemplate(demoData, row);
                        $('.subtable_template_'+rowId).css('display', '');
                        row.classList.add(...rowClasses);
                        button.classList.add('active');
                    }
                });
            });
        }

        // Reset subtable
        const resetSubtable = () => {
            const subtables = document.querySelectorAll('[data-kt-docs-datatable-subtable="subtable_template"]');
//            subtables.forEach(st => {
//                st.parentNode.removeChild(st);
//            });

            const rows = table.querySelectorAll('tbody tr');
            rows.forEach(r => {
                r.classList.remove('isOpen');
                if (r.querySelector('[data-kt-docs-datatable-subtable="expand_row"]')) {
                    r.querySelector('[data-kt-docs-datatable-subtable="expand_row"]').classList.remove('active');
                }
            });
        }


    return {
        // Public functions
        init: function () {
            table = document.querySelector('#kt_table');
            
            if (!table) {
                return;
            }

            initTable();
            initToggleToolbar();
            handleChangeSysDatatable();
            handleSearchDatatable();
            handleUpdateRows();
            handleDeleteRows();
            handleActionButton();
        }
    };
}();

// Webpack support
if (typeof module !== 'undefined') {
    module.exports = KTDocsDatatableSubtable;
}

// On document ready
KTUtil.onDOMContentLoaded(function () {
    KTDataTablesList.init();
});