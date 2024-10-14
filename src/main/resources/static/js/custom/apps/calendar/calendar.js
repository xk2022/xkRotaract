"use strict";

// Class definition
var KTAppCalendar = function () {
    // Shared variables
    // Calendar variables
    var calendar; // 存儲 FullCalendar 實例，用於渲染日曆和管理事件。
    var calendarData;
    var data = {
        id: '',
        eventName: '',
        eventDescription: '',
        eventLocation: '',
        startDate: '',
        endDate: '',
        allDay: false,
        type: '',
        district_id: '',
        rotaract_id: ''
    }; // 用來存儲日曆事件的屬性（如事件名稱、描述、開始時間、結束時間等）。
    var popover; // 用來控制事件懸停時的彈出預覽框。
    var popoverState = false; // 用來控制事件懸停時的彈出預覽框。

    // Add event variables
    var eventName;
    var eventDescription;
    var eventLocation;
    var startDatepicker;
    var startFlatpickr;
    var endDatepicker;
    var endFlatpickr;
    var startTimepicker;
    var startTimeFlatpickr;
    var endTimepicker
    var endTimeFlatpickr;
    var modal;
    var modalTitle;
    var form;
    var validator;
    var addButton;
    var submitButton;
    var cancelButton;
    var closeButton;

    // View event variables
    var viewEventName;
    var viewAllDay;
    var viewEventDescription;
    var viewEventLocation;
    var viewStartDate;
    var viewEndDate;
    var viewModal;
    var viewEditButton;
    var viewDeleteButton;

    var formattedEvents;
    // 取得 PermissionAction_CREATE 的值，並轉換為布林值
    var PermissionAction_CREATE = $('#PermissionAction_CREATE').val() === 'true'; // 預設為 false。



    var initData = function () {
        $.ajax({
            url: baseUrl + '/api/manage/calendar/list',
            method: 'POST',
            data: JSON.stringify({
                    // 傳遞 JSON 格式的數據
                    calendar_range: $('#calendar_range').val(),
                    district_id: $('#district_id').val(),
                    rotaract_id: $('#rotaract_id').val()
                }),
            processData: false,
            contentType: 'application/json',
            success: function(response) {
                console.log('AJAX 请求成功：', response);

                calendarData = response; // 将数据存储在全局变量中
                // 將資料轉換為 FullCalendar 所需的格式
                formattedEvents = calendarData.map(function(event) {
                    return {
                        id: event.id,
                        title: event.title,
                        start: event.start,  // 移除不需要的時間部分
                        end: event.end,
                        className: event.className || "fc-event-primary",  // 如果 className 是 null, 則設置一個預設的 class
                        description: event.description || '',  // 如果 description 缺失, 則設置為空字串
                        location: event.location
                    };
                });
                initCalendarApp();
            },
            error: function(xhr, status, error) {
                console.error('AJAX 请求失败：', error);
            }
        });
    }

    // Private functions
    var initCalendarApp = function () {
        // Define variables
        var calendarEl = document.getElementById('kt_calendar_app');
        var todayDate = moment().startOf('day');
        var YM = todayDate.format('YYYY-MM');
        var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
        var TODAY = todayDate.format('YYYY-MM-DD');
        var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');

        // Init calendar --- more info: https://fullcalendar.io/docs/initialize-globals
        calendar = new FullCalendar.Calendar(calendarEl, {
            timeZone: 'Asia/Taipei',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
            },
            initialDate: TODAY,
            navLinks: true, // can click day/week names to navigate views
            selectable: true,
            selectMirror: true,

            height: 800,
            contentHeight: 780,
            aspectRatio: 3,  // see: https://fullcalendar.io/docs/aspectRatio

            nowIndicator: true,
            now: TODAY + 'T09:25:00', // just for demo

            initialView: 'dayGridMonth',
            initialDate: TODAY,

            views: {
                dayGridMonth: { buttonText: '月份' },
                timeGridWeek: { buttonText: '星期' },
                timeGridDay: { buttonText: '日期' },
                listMonth: { buttonText: '列表' }
            },

            editable: PermissionAction_CREATE,
            // Select dates action --- more info: https://fullcalendar.io/docs/select-callback
            select: function (arg) {
                if (PermissionAction_CREATE) {
                    hidePopovers();
                    formatArgs(arg);
                    handleNewEvent();
                }
            },

            // Click event --- more info: https://fullcalendar.io/docs/eventClick
            eventClick: function (arg) {
                hidePopovers();

                formatArgs({
                    id: arg.event.id,
                    title: arg.event.title,
                    description: arg.event.extendedProps.description,
                    location: arg.event.extendedProps.location,
                    startStr: arg.event.startStr,
                    endStr: arg.event.endStr,
                    allDay: arg.event.allDay
                });
                handleViewEvent();
            },

            // MouseEnter event --- more info: https://fullcalendar.io/docs/eventMouseEnter
            eventMouseEnter: function (arg) {
                formatArgs({
                    id: arg.event.id,
                    title: arg.event.title,
                    description: arg.event.extendedProps.description,
                    location: arg.event.extendedProps.location,
                    startStr: arg.event.startStr,
                    endStr: arg.event.endStr,
                    allDay: arg.event.allDay
                });

                // Show popover preview
                initPopovers(arg.el);
            },

            dayMaxEvents: true, // allow "more" link when too many events
            events: formattedEvents,

            // Reset popovers when changing calendar views --- more info: https://fullcalendar.io/docs/datesSet
            datesSet: function(){
                hidePopovers();
            }
        });
        /**
            events: formattedEvents.concat(
            [
                {
                    id: uid(),
                    title: 'All Day Event',
                    start: YM + '-01',
                    end: YM + '-02',
                    description: 'Toto lorem ipsum dolor sit incid idunt ut',
                    className: "fc-event-danger fc-event-solid-warning",
                    location: 'Federation Square'
                },
                {
                    id: uid(),
                    title: 'Reporting',
                    start: YM + '-14T13:30:00',
                    description: 'Lorem ipsum dolor incid idunt ut labore',
                    end: YM + '-14T14:30:00',
                    className: "fc-event-success",
                    location: 'Meeting Room 7.03'
                },
                {
                    id: uid(),
                    title: 'Company Trip',
                    start: YM + '-02',
                    description: 'Lorem ipsum dolor sit tempor incid',
                    end: YM + '-03',
                    className: "fc-event-primary",
                    location: 'Seoul, Korea'

                },
                {
                    id: uid(),
                    title: 'ICT Expo 2021 - Product Release',
                    start: YM + '-03',
                    description: 'Lorem ipsum dolor sit tempor inci',
                    end: YM + '-05',
                    className: "fc-event-light fc-event-solid-primary",
                    location: 'Melbourne Exhibition Hall'
                },
                {
                    id: uid(),
                    title: 'Dinner',
                    start: YM + '-12',
                    description: 'Lorem ipsum dolor sit amet, conse ctetur',
                    end: YM + '-13',
                    location: 'Squire\'s Loft'
                },
                {
                    id: uid(),
                    title: 'Repeating Event',
                    start: YM + '-09T16:00:00',
                    end: YM + '-09T17:00:00',
                    description: 'Lorem ipsum dolor sit ncididunt ut labore',
                    className: "fc-event-danger",
                    location: 'General Area'
                },
                {
                    id: uid(),
                    title: 'Repeating Event',
                    description: 'Lorem ipsum dolor sit amet, labore',
                    start: YM + '-16T16:00:00',
                    end: YM + '-16T17:00:00',
                    location: 'General Area'
                },
                {
                    id: uid(),
                    title: 'Conference',
                    start: YESTERDAY,
                    end: TOMORROW,
                    description: 'Lorem ipsum dolor eius mod tempor labore',
                    className: "fc-event-primary",
                    location: 'Conference Hall A'
                },
                {
                    id: uid(),
                    title: 'Meeting',
                    start: TODAY + 'T10:30:00',
                    end: TODAY + 'T12:30:00',
                    description: 'Lorem ipsum dolor eiu idunt ut labore',
                    location: 'Meeting Room 11.06'
                },
                {
                    id: uid(),
                    title: 'Lunch',
                    start: TODAY + 'T12:00:00',
                    end: TODAY + 'T14:00:00',
                    className: "fc-event-info",
                    description: 'Lorem ipsum dolor sit amet, ut labore',
                    location: 'Cafeteria'
                },
                {
                    id: uid(),
                    title: 'Meeting',
                    start: TODAY + 'T14:30:00',
                    end: TODAY + 'T15:30:00',
                    className: "fc-event-warning",
                    description: 'Lorem ipsum conse ctetur adipi scing',
                    location: 'Meeting Room 11.10'
                },
                {
                    id: uid(),
                    title: 'Happy Hour',
                    start: TODAY + 'T17:30:00',
                    end: TODAY + 'T21:30:00',
                    className: "fc-event-info",
                    description: 'Lorem ipsum dolor sit amet, conse ctetur',
                    location: 'The English Pub'
                },
                {
                    id: uid(),
                    title: 'Dinner',
                    start: TOMORROW + 'T18:00:00',
                    end: TOMORROW + 'T21:00:00',
                    className: "fc-event-solid-danger fc-event-light",
                    description: 'Lorem ipsum dolor sit ctetur adipi scing',
                    location: 'New York Steakhouse'
                },
                {
                    id: uid(),
                    title: 'Birthday Party',
                    start: TOMORROW + 'T12:00:00',
                    end: TOMORROW + 'T14:00:00',
                    className: "fc-event-primary",
                    description: 'Lorem ipsum dolor sit amet, scing',
                    location: 'The English Pub'
                },
                {
                    id: uid(),
                    title: 'Site visit',
                    start: YM + '-28',
                    end: YM + '-29',
                    className: "fc-event-solid-info fc-event-light",
                    description: 'Lorem ipsum dolor sit amet, labore',
                    location: '271, Spring Street'
                }
            ]),**/

        calendar.render();
    }

    // Initialize popovers --- more info: https://getbootstrap.com/docs/4.0/components/popovers/
    const initPopovers = (element) => {
        hidePopovers();

        // Generate popover content
        const startDate = data.allDay ? moment(data.startDate).format('Do MMM, YYYY') : moment(data.startDate).format('Do MMM, YYYY - h:mm a');
//        const endDate = data.allDay ? moment(data.endDate).format('Do MMM, YYYY') : moment(data.endDate).format('Do MMM, YYYY - h:mm a');
        const endDate = startDate;
        const popoverHtml = '<div class="fw-bolder mb-2">' + data.eventName + '</div><div class="fs-7"><span class="fw-bold">Start:</span> ' + startDate + '</div><div class="fs-7 mb-4"><span class="fw-bold">End:</span> ' + endDate + '</div><div id="kt_calendar_event_view_button" type="button" class="btn btn-sm btn-light-primary">View More</div>';

        // Popover options
        var options = {
            container: 'body',
            trigger: 'manual',
            boundary: 'window',
            placement: 'auto',
            dismiss: true,
            html: true,
            title: 'Event Summary',
            content: popoverHtml,
        }

        // Initialize popover
        popover = KTApp.initBootstrapPopover(element, options);

        // Show popover
        popover.show();

        // Update popover state
        popoverState = true;

        // Open view event modal
        handleViewButton();
    }

    // Hide active popovers
    const hidePopovers = () => {
        if (popoverState) {
            popover.dispose();
            popoverState = false;
        }
    }

    // Init validator
    const initValidator = () => {
        // Init form validation rules. For more info check the FormValidation plugin's official documentation:https://formvalidation.io/
        validator = FormValidation.formValidation(
            form,
            {
                fields: {
                    'eventName': {
                        validators: {
                            notEmpty: {
                                message: 'Event name is required'
                            }
                        }
                    },
                    'startDate': {
                        validators: {
                            notEmpty: {
                                message: 'Start date is required'
                            }
                        }
                    },
                    'endDate': {
                        validators: {
                            notEmpty: {
                                message: 'End date is required'
                            }
                        }
                    }
                },

                plugins: {
                    trigger: new FormValidation.plugins.Trigger(),
                    bootstrap: new FormValidation.plugins.Bootstrap5({
                        rowSelector: '.fv-row',
                        eleInvalidClass: '',
                        eleValidClass: ''
                    })
                }
            }
        );
    }

    // Initialize datepickers --- more info: https://flatpickr.js.org/
    const initDatepickers = () => {
        startFlatpickr = flatpickr(startDatepicker, {
            enableTime: false,
            dateFormat: "Y-m-d",
        });

        endFlatpickr = flatpickr(endDatepicker, {
            enableTime: false,
            dateFormat: "Y-m-d",
        });

        startTimeFlatpickr = flatpickr(startTimepicker, {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
        });

        endTimeFlatpickr = flatpickr(endTimepicker, {
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
        });
    }

    // Handle add button
    const handleAddButton = () => {
        if (addButton) {
            addButton.addEventListener('click', e => {
                hidePopovers();

                // Reset form data
                data = {
                    id: '',
                    eventName: '',
                    eventDescription: '',
                    startDate: new Date(),
                    endDate: new Date(),
                    allDay: false
                };
                handleNewEvent();
            });
        }
    }

    // Input Control Setting
    const HandleInputControl = () => {
        // Select datepicker wrapper elements
        const datepickerWrappers = form.querySelectorAll('[data-kt-calendar="datepicker"]');

        // Handle all day toggle
        const allDayToggle = form.querySelector('#kt_calendar_datepicker_allday');
        const dayMorningToggle = form.querySelector('#kt_calendar_datepicker_dayMorning');
        const dayAfternoonToggle = form.querySelector('#kt_calendar_datepicker_dayAfternoon');
        const dayNightToggle = form.querySelector('#kt_calendar_datepicker_dayNight');

        allDayToggle.addEventListener('click', e => {
            dayMorningToggle.checked = false;
            dayAfternoonToggle.checked = false;
            dayNightToggle.checked = false;
            if (e.target.checked) {
                // 如果勾選 allDay，清空時間並隱藏時間選擇
                $("[name='startTime']").val('');
                $("[name='endTime']").val('');
                datepickerWrappers.forEach(dw => {
                    dw.classList.add('d-none'); // 隱藏時間選擇框
                });
            } else {
                // 如果未勾選 allDay，顯示時間選擇框
                endFlatpickr.setDate(data.startDate, true, 'Y-m-d');
                datepickerWrappers.forEach(dw => {
                    dw.classList.remove('d-none');
                });
            }
        });
        dayMorningToggle.addEventListener('click', e => {
            allDayToggle.checked = false;
            dayAfternoonToggle.checked = false;
            dayNightToggle.checked = false;
            if (e.target.checked) {
                // 如果勾選
                $("[name='startTime']").val('09:00');
                $("[name='endTime']").val('12:00');
                datepickerWrappers.forEach(dw => {
                    dw.classList.remove('d-none'); // 隱藏時間選擇框
                });
            }
        });
        dayAfternoonToggle.addEventListener('click', e => {
            allDayToggle.checked = false;
            dayMorningToggle.checked = false;
            dayNightToggle.checked = false;
            if (e.target.checked) {
                // 如果勾選
                $("[name='startTime']").val('13:00');
                $("[name='endTime']").val('16:00');
                datepickerWrappers.forEach(dw => {
                    dw.classList.remove('d-none'); // 隱藏時間選擇框
                });
            }
        });
        dayNightToggle.addEventListener('click', e => {
            allDayToggle.checked = false;
            dayMorningToggle.checked = false;
            dayAfternoonToggle.checked = false;
            if (e.target.checked) {
                // 如果勾選
                $("[name='startTime']").val('18:00');
                $("[name='endTime']").val('22:00');
                datepickerWrappers.forEach(dw => {
                    dw.classList.remove('d-none'); // 隱藏時間選擇框
                });
            }
        });
    }

    // Handle add new event
    const handleNewEvent = () => {
        $('#modal_name').text("新增事件");

        modal.show();
        HandleInputControl();

        populateForm(data);
    }

    // Handle edit event
    const handleEditEvent = () => {
        // Update modal title
        modal.show();
        HandleInputControl();

        // data.id
        $.ajax({
            url: '/xkRotaract/api/manage/calendar/findById',
            method: 'POST',
            data: JSON.stringify({ 'id': data.id }), // 修改为适合你的代码
            processData: false,
            contentType: 'application/json',
            success: function(response) {
                console.log('AJAX 请求成功：', response);
                readyToEdit(response);
            },
            error: function(xhr, status, error) {
                console.error('AJAX 请求失败：', error);
            }
        });
    }

    // Handle view event
    const handleViewEvent = () => {
        viewModal.show();

        // Detect all day event
        var eventNameMod;
        var startDateMod;
        var endDateMod;

        // Generate labels
        if (data.allDay) {
            eventNameMod = 'All Day';
            startDateMod = moment(data.startDate).format('Do MMM, YYYY');
            endDateMod = moment(data.endDate).format('Do MMM, YYYY');
        } else {
            eventNameMod = '';
            startDateMod = moment(data.startDate).format('Do MMM, YYYY - h:mm a');
            endDateMod = moment(data.endDate).format('Do MMM, YYYY - h:mm a');
        }

        $('#modal_name').text("新增事件");
        $("[name='id']").val(null);
        $("[name='viewEventName']").text(data.eventName);
        $("[name='viewAllDay']").text(eventNameMod);
        $("[name='viewEventDescription']").text(data.eventDescription ? data.eventDescription : '--');
        $("[name='viewEventLocation']").text(data.eventLocation ? data.eventLocation : '--');

        // 取得當前日期，並設置為 ISO 格式的日期字符串
        const today = new Date().toISOString().slice(0, 10); // 格式為 YYYY-MM-DDTHH:mm
        const now = new Date().toISOString().slice(11, 16); // 格式為 YYYY-MM-DDTHH:mm
        $("[name='viewStartDate']").text(startDateMod);
        $("[name='viewEndDate']").text(endDateMod);
    }

    // Handle delete event
    const handleDeleteEvent = () => {
        viewDeleteButton.addEventListener('click', e => {
            e.preventDefault();

            Swal.fire({
                text: "Are you sure you would like to delete this event?",
                icon: "warning",
                showCancelButton: true,
                buttonsStyling: false,
                confirmButtonText: "Yes, delete it!",
                cancelButtonText: "No, return",
                customClass: {
                    confirmButton: "btn btn-primary",
                    cancelButton: "btn btn-active-light"
                }
            }).then(function (result) {
                if (result.value) {
                    calendar.getEventById(data.id).remove();

                    viewModal.hide(); // Hide modal				
                } else if (result.dismiss === 'cancel') {
                    Swal.fire({
                        text: "Your event was not deleted!.",
                        icon: "error",
                        buttonsStyling: false,
                        confirmButtonText: "Ok, got it!",
                        customClass: {
                            confirmButton: "btn btn-primary",
                        }
                    });
                }
            });
        });
    }

    // Handle edit button
    const handleEditButton = () => {
        viewEditButton.addEventListener('click', e => {
            e.preventDefault();

            viewModal.hide();
            handleEditEvent();
        });
    }

    // Handle view button
    const handleViewButton = () => {
        const viewButton = document.querySelector('#kt_calendar_event_view_button');
        viewButton.addEventListener('click', e => {
            e.preventDefault();

            hidePopovers();
            handleViewEvent();
        });
    }

    // Helper functions

    // Reset form validator on modal close
    const resetFormValidator = (element) => {
        // Target modal hidden event --- For more info: https://getbootstrap.com/docs/5.0/components/modal/#events
        element.addEventListener('hidden.bs.modal', e => {
            if (validator) {
                // Reset form validator. For more info: https://formvalidation.io/guide/api/reset-form
                validator.resetForm(true);
            }
        });
    }

    // Populate form 
    const populateForm = () => {

        $("[name='id']").val(data.id);
        $("[name='eventName']").val(data.eventName ? data.eventName : '');
        $("[name='eventDescription']").val(data.eventDescription ? data.eventDescription : '');
        $("[name='eventLocation']").val(data.eventLocation ? data.eventLocation : '');

        startFlatpickr.setDate(data.startDate, true, 'Y-m-d');
        // Handle null end dates
        // const endDate = data.endDate ? data.endDate : moment(data.startDate).format();
        endFlatpickr.setDate(data.startDate, true, 'Y-m-d');

//        const datepickerWrappers = form.querySelectorAll('[data-kt-calendar="datepicker"]');
//        if (data.allDay) {
//            $("[name='allDay']").prop('checked', true);
//            // 如果勾選 allDay，清空時間並隱藏時間選擇
//            $("[name='startTime']").val('');
//            $("[name='endTime']").val('');
//            datepickerWrappers.forEach(dw => {
//                dw.classList.add('d-none');
//            });
//        } else {
//            const startDate = new Date(data.startDate).toISOString().slice(0, 10); // 格式為 YYYY-MM-DDTHH:mm
//            const startTime = new Date(data.startDate).toISOString().slice(11, 16); // 格式為 YYYY-MM-DDTHH:mm
//            const endDate = new Date(data.endDate).toISOString().slice(0, 10); // 格式為 YYYY-MM-DDTHH:mm
//            const endTime = new Date(data.endDate).toISOString().slice(11, 16); // 格式為 YYYY-MM-DDTHH:mm
//            $("[name='startDate']").val(startDate);
//            $("[name='endDate']").val(endDate);
//            $("[name='startTime']").val(startTime);
//            $("[name='endTime']").val(endTime);
//            $("[name='allDay']").prop('checked', false);
            // 如果未勾選 allDay，顯示時間選擇框
//            datepickerWrappers.forEach(dw => {
//                dw.classList.remove('d-none');
//            });
//        }
    }

    // Format FullCalendar reponses
    const formatArgs = (res) => {
        data.id = res.id;
        data.eventName = res.title;
        data.eventDescription = res.description;
        data.eventLocation = res.location;
        data.startDate = res.startStr;
        data.endDate = res.endStr;
        data.allDay = res.allDay;
    }

    // Generate unique IDs for events
    const uid = () => {
        return Date.now().toString() + Math.floor(Math.random() * 1000).toString();
    }

    return {
        // Public Functions
        init: function () {
            // Define variables
            // Add event modal
            const element = document.getElementById('kt_modal_input_add_event');
            form = element.querySelector('#kt_modal_add_event_form');
            startDatepicker = form.querySelector('#kt_calendar_datepicker_start_date');
            endDatepicker = form.querySelector('#kt_calendar_datepicker_end_date');
            startTimepicker = form.querySelector('#kt_calendar_datepicker_start_time');
            endTimepicker = form.querySelector('#kt_calendar_datepicker_end_time');
            addButton = document.querySelector('[name="add_btn"]');
            modalTitle = form.querySelector('[data-kt-calendar="title"]');
            modal = new bootstrap.Modal(element);
        handleCancelButton(element, form, modal);
        handleCloseButton(element, modal);
//        submitButton = element.querySelector('[data-kt-modal-action="submit"]');
        handleFormSubmit(element, form, validator, modal);
        // 初始化加载地区
        loadDistricts('dropdown_DISTRICT');
        dropdown('dropdown_CALENDAR_TYPE', 'inputGroupSelect_type', 'type');

            // View event modal
            const viewElement = document.getElementById('kt_modal_input_view_event');
            viewModal = new bootstrap.Modal(viewElement);
            viewEditButton = viewElement.querySelector('#kt_modal_view_event_edit');
            viewDeleteButton = viewElement.querySelector('#kt_modal_view_event_delete');

            initData();
            // initCalendarApp();
            initValidator();
            initDatepickers();
            handleAddButton();
            handleEditButton();
            handleDeleteEvent();
            resetFormValidator(element);
        }
    };
}();

// On document ready
KTUtil.onDOMContentLoaded(function () {
    KTAppCalendar.init();
});


