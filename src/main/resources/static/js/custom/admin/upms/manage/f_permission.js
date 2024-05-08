"use strict";

// Class definition
var KTDataTablesList = function () {
    // Define shared variables
    var datatable;
    var table;
    var toolbarBase;
    var toolbarSelected;
    var selectedCount;

    // Private functions
    // Init add schedule modal
    var initTable = () => {
        // Set date data order
        const tableRows = table.querySelectorAll('tbody tr');

        tableRows.forEach(row => {
            const dateRow = row.querySelectorAll('td');
//            const realDate = moment(dateRow[2].innerHTML, "DD MMM YYYY, LT").format(); // select date from 2nd column in table
//            dateRow[2].setAttribute('data-order', realDate);
        });

        // Init datatable --- more info on datatables: https://datatables.net/manual/
        datatable = $(table).DataTable({
            "bProcessing": true,
            "bPaginate": true, //翻頁功能
            "bLengthChange": true, //改變每頁顯示資料數量
            "bFilter": true, //過濾功能
            "bSort": false, //排序功能

            /* 設定屬性(預設功能)區塊 */
            "searching": false, // 預設為true 搜尋功能，若要開啟不用特別設定
            "paging": false, // 預設為true 分頁功能，若要開啟不用特別設定
            // "ordering": false, // 預設為true 排序功能，若要開啟不用特別設定
            "sPaginationType": "full_numbers", // 分頁樣式 預設為"full_numbers"，若需其他樣式才需設定
            // "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]], //顯示筆數設定 預設為[10, 25, 50, 100]
            // "pageLength":'50'// 預設為'10'，若需更改初始每頁顯示筆數，才需設定
            "processing": true, // 預設為false 是否要顯示當前資料處理狀態資訊
            "serverSide": true, // 預設為false 是否透過Server端處理分頁…等
            "stateSave": true, // 預設為false 在頁面刷新時，是否要保存當前表格資料與狀態
            "destroy": true, // 預設為false 是否銷毀當前暫存資料
            "info": false, // 預設為true　是否要顯示"目前有 x  筆資料"
            // "autoWidth": false, // 預設為true　設置是否要自動調整表格寬度(false代表不要自適應)　　　　
            "scrollCollapse": true, // 預設為false 是否開始滾軸功能控制X、Y軸
            "scrollY": 750, // 若有設置為Y軸(垂直)最大高度
            "dom": 'lrtip',// 設置搜尋div、頁碼div...等基本位置/外觀..等，詳細可看官網

            'order': [],
            "lengthChange": false,
            // select: true,
//            "scrollX": true,

            // searchDelay: 500,
            processing: true,
//            serverSide: true,
            // order: [[5, 'desc']],
            select: {
                style: 'multi',
                selector: 'td:first-child input[type="checkbox"]',
                className: 'row-selected'
            },

            /* 設定資料來源區塊(data or ajax….等) */
            "ajax": {
                "type" : "GET",
                "url" : "http://localhost:8000/api/manage/permission/list",
                "dataSrc": function ( json ) {
                    //Make your callback here.
                    alert("Done!");
                    return json.data;
                }
            },
//            ajax: {
//                "url": "/api/manage/permission/list",
//                "type": "GET"
//            },
//            "fnServerData": function (sSource, aoData, fnCallback) {
//                var json = {
//                    "page": {
//                        "start": aoData[3].value,
//                        "length": aoData[4].value,
//                    },
//                    "search": {
//                        "xb": $("#searchTitle").val()
//                    }
//                };
//                $.ajax({
//                    "dataType": 'json',
//                    "type": "POST",
//                    "url": "/api/manage/permission/list",
//                    "contentType": "application/json; charset=utf-8",
//                    "data": JSON.stringify(json),
//                    "success": function (data) {
//                        data.recordsTotal = data.page.recordsTotal;
//                        data.recordsFiltered = data.page.recordsTotal;
//                        fnCallback(data);
//                    }
//                });
//            },
            /* 設定資料欄位區塊(columns) */
            columns: [
                { data: 'id' },
                { data: 'systemId' },
                { data: 'pid' },
                { data: 'name' },
                { data: 'type' },
                { data: 'permissionValue' },
                { data: 'uri' },
                { data: 'icon' },
                { data: 'status' },
                { data: 'orders' },
                { data: null },
            ],
            /* 設定語言區塊(language) */
            "oLanguage": {
                "sLengthMenu": "每頁顯示 _MENU_ 條記錄",
                "sZeroRecords": "抱歉， 沒有找到",
                "sInfoEmpty": "沒有資料",
                "sInfoFiltered": "(從 _MAX_ 條資料中檢索)",
                "oPaginate": {
                    "sFirst": "首頁",
                    "sPrevious": "前一頁",
                    "sNext": "後一頁",
                    "sLast": "尾頁"
                },
                "sZeroRecords": "沒有檢索到資料",
            },
            /* 設定欄位元素定義區塊(columnDefs) */
            'columnDefs': [
                { orderable: false, targets: 0 }, // Disable ordering on column 0 (assigned)
                { orderable: false, targets: 1 }, // Disable ordering on column 1 (assigned)
                { orderable: false, targets: 3 }, // Disable ordering on column 3 (actions)
            ],
            /* 設定列元素區塊(rowCallback) */


            // Add data-filter attribute
//            createdRow: function (row, data, dataIndex) {
//                $(row).find('td:eq(4)').attr('data-filter', data.CreditCardType);
//            }
        });

        table = datatable.$;

        // Re-init functions on every table re-draw -- more info: https://datatables.net/reference/event/draw
        datatable.on('draw', function () {
            initToggleToolbar();
            toggleToolbars();
            handleDeleteRows();
            KTMenu.createInstances();
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
                const id = parent.querySelectorAll('input')[0].value;

                // SweetAlert2 pop up --- official docs reference: https://sweetalert2.github.io/
                Swal.fire({
                    text: "Are you sure you want to delete " + id + "?",
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
                            text: "You have deleted " + id + "!.",
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


    return {
        // Public functions
        init: function () {
            table = document.querySelector('#kt_table');
            
            if (!table) {
                return;
            }

            initTable();
            initToggleToolbar();
            handleSearchDatatable();
            handleUpdateRows();
            handleDeleteRows();
        }
    };
}();

// On document ready
KTUtil.onDOMContentLoaded(function () {
    KTDataTablesList.init();
});