/*!
 * Evo Calendar - Simple and Modern-looking Event Calendar Plugin
 *
 * Licensed under the MIT License
 * 
 * Version: 1.1.3
 * Author: Edlyn Villegas
 * Docs: https://edlynvillegas.github.com/evo-calendar
 * Repo: https://github.com/edlynvillegas/evo-calendar
 * Issues: https://github.com/edlynvillegas/evo-calendar/issues
 * 
 */

;(function(factory) {
    'use strict';
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else if (typeof exports !== 'undefined') {
        module.exports = factory(require('jquery'));
    } else {
        factory(jQuery);
    }

}(function($) {
    'use strict';
    var EvoCalendar = window.EvoCalendar || {};
    
    EvoCalendar = (function() {
        var instanceUid = 0;
        function EvoCalendar(element, settings) {
            var _ = this;
            _.defaults = {
                theme: null,
                format: 'mm/dd/yyyy',
                titleFormat: 'MM yyyy',
                eventHeaderFormat: 'MM d, yyyy',
                firstDayOfWeek: 0,
                language: 'en',
                todayHighlight: false,
                sidebarDisplayDefault: true, // 默認顯示側邊欄
                sidebarToggler: true,
                eventDisplayDefault: true, // 默認顯示事件列表
                eventListToggler: true,
                calendarEvents: null
            };
            _.options = $.extend({}, _.defaults, settings);

            _.initials = {
                default_class: $(element)[0].classList.value,
                validParts: /dd?|DD?|mm?|MM?|yy(?:yy)?/g,
                dates: {
                    en: {
                        days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
                        daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
                        daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
                        months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                        monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                        noEventForToday: "No event for today.. so take a rest! :)",
                        noEventForThisDay: "No event for this day.. so take a rest! :)",
                        previousYearText: "往前一年度",
                        nextYearText: "Next year",
                        closeSidebarText: "Close sidebar",
                        closeEventListText: "Close event list"
                    },
                    tw: {
                        days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
                        daysShort: ["週日", "週ㄧ", "週二", "週三", "週四", "週五", "週六"],
                        daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
                        months: ["七月", "八月", "九月", "十月", "十一月", "十二月", "一月", "二月", "三月", "四月", "五月", "六月"],
                        monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                        noEventForToday: "No event for today.. so take a rest! :)",
                        noEventForThisDay: "No event for this day.. so take a rest! :)",
                        previousYearText: "往前一年度",
                        nextYearText: "往後一年度",
                        closeSidebarText: "Close sidebar",
                        closeEventListText: "Close event list"
                    }
                }
            }
            _.initials.weekends = {
                sun: _.initials.dates[_.options.language].daysShort[0],
                sat: _.initials.dates[_.options.language].daysShort[6]
            }


            // Format Calendar Events into selected format
            if(_.options.calendarEvents != null) {
                for(var i=0; i < _.options.calendarEvents.length; i++) {
                    // If event doesn't have an id, throw an error message
                    if(!_.options.calendarEvents[i].id) {
                        console.log("%c Event named: \""+_.options.calendarEvents[i].name+"\" doesn't have a unique ID ", "color:white;font-weight:bold;background-color:#e21d1d;");
                    }
                    if(_.isValidDate(_.options.calendarEvents[i].date)) {
                        _.options.calendarEvents[i].date = _.formatDate(_.options.calendarEvents[i].date, _.options.format)
                    }
                }
            }

            // Global variables
            _.startingDay = null;
            _.monthLength = null;
            _.windowW = $(window).width();
            
            // CURRENT
            _.$current = {
                month: (isNaN(this.month) || this.month == null) ? new Date().getMonth() : this.month,
                year: (isNaN(this.year) || this.year == null) ? new Date().getFullYear() : this.year,
                date: _.formatDate(_.initials.dates[_.defaults.language].months[new Date().getMonth()]+' '+new Date().getDate()+' '+ new Date().getFullYear(), _.options.format)
            }

            // ACTIVE 原本邏輯運算用
            _.$activeBase = {
                month: _.$current.month,
                year: _.$current.year,
                date: _.$current.date,
                event_date: _.$current.date,
                events: []
            }
            // ACTIVE 原本邏輯運算用
            _.$active = {
                month: _.$current.month,
                year: _.$current.year,
                date: _.$current.date,
                event_date: _.$current.date,
                events: []
            }
            // ACTIVE for Rotary Year Show
            _.$activeR = {
                num: 6,
                month: _.$current.month-6,
                year: _.$current.year,
                date: _.$current.date,
                event_date: _.$current.date,
                events: []
            }

            // LABELS
            _.$label = {
                days: [],
                months: _.initials.dates[_.defaults.language].months,
                days_in_month: [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
            }

            // HTML Markups (template)
            _.$markups = {
                calendarHTML: '',
                mainHTML: '',
                sidebarHTML: '',
                eventHTML: ''
            }
            // HTML DOM elements
            _.$elements = {
                calendarEl: $(element),
                innerEl: null,
                sidebarEl: null,
                headbarEl: null,
                eventEl: null,

                sidebarToggler: null,
                eventListToggler: null,

                activeDayEl: null,
                activeMonthEl: null,
                activeYearEl: null
            }
            _.$breakpoints = {
                tablet: 768,
                mobile: 425
            }
            _.$UI = {
                hasSidebar: true,
                hasEvent: true
            }

            _.formatDate = $.proxy(_.formatDate, _);
            _.selectDate = $.proxy(_.selectDate, _);
            _.selectMonth = $.proxy(_.selectMonth, _);
            _.selectYear = $.proxy(_.selectYear, _);
            _.selectDistrict = $.proxy(_.selectDistrict, _);
            _.selectEvent = $.proxy(_.selectEvent, _);
            _.toggleHeaderDistrict = $.proxy(_.toggleHeaderDistrict, _);
            _.toggleSidebar = $.proxy(_.toggleSidebar, _);
            _.toggleEventList = $.proxy(_.toggleEventList, _);
            
            _.instanceUid = instanceUid++;

            _.init(true);
        }

        return EvoCalendar;

    }());


    // v1.0.0 - Build the bones! (incl. sidebar, inner, events), called once in every initialization
    EvoCalendar.prototype.buildTheBones = function() {
        var _ = this;
        _.calculateDays();
        
        if (!_.$elements.calendarEl.html()) {
            var markup;

            // --- BUILDING MARKUP BEGINS --- //
            //headbar
            var headerTabs = ['全部資料', '3461', '3462', '3481', '3482', '3470', '3490', '3501', '3502', '3510', '3521', '3522', '3523'];
            $.ajax({
                url: '/xkRotaract/api/manage/dictionary/listDictionaryData',
                method: 'POST',
                data: JSON.stringify({ 'code': 'dropdown_DISTRICT' }), // 修改为适合你的代码
                processData: false,
                contentType: 'application/json',
                success: function(response) {
                    console.log('AJAX 请求成功：', response);

                    headerTabs = response;
                },
                error: function(xhr, status, error) {
                    console.error('AJAX 请求失败：', error);
                }
            });
            // TODO loc.
            headerTabs = [{        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 1,        "parentId": 1,        "code": "3461",        "description": "3461地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 2,        "parentId": 1,        "code": "3462",        "description": "3462地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 3,        "parentId": 1,        "code": "3470",        "description": "3470地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 4,        "parentId": 1,        "code": "3481",        "description": "3481地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 5,        "parentId": 1,        "code": "3482",        "description": "3482地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 6,        "parentId": 1,        "code": "3490",        "description": "3490地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 7,        "parentId": 1,        "code": "3501",        "description": "3501地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 8,        "parentId": 1,        "code": "3502",        "description": "3502地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 9,        "parentId": 1,        "code": "3510",        "description": "3510地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 10,        "parentId": 1,        "code": "3521",        "description": "3521地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 11,        "parentId": 1,        "code": "3522",        "description": "3522地區"    },    {        "createBy": null,        "createTime": 1724314936000,        "updateBy": null,        "updateTime": 1724314936000,        "id": 12,        "parentId": 1,        "code": "3523",        "description": "3523地區"    }];
            // v_Rotary - Append Calendar Header with Tabs
            markup = '<!-- A 區塊 (5%) -->'+
                    '<div class="calendar-headbar main-shadow text-center">';

            // 電腦版 row district-list
            markup +=   '<div class="district-list">'+
                            '<div class="btn tab-btn active" data-district-val="0">ALL DISTRICT</div>'
                            for (var i = 0; i < headerTabs.length; i++) {
                                markup += '<div class="btn tab-btn" data-district-val="'+headerTabs[i].code+'">D' + headerTabs[i].code + '</div>';
                            }
            markup +=   '</div>'+
                    '</div>';

            // .calendar-body
            markup += '<!-- B 區塊 (95%) -->'+
                    '<div class="calendar-body">';
            // sidebar
            markup += '<!-- C 區塊 (20%) -->'+
                        '<div class="calendar-sidebar calendar-sidebar-left main-shadow">'+
                            '<div class="calendar-year">'+
                                '<div class="years_icon" data-year-val="prev"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-left-fill" viewBox="0 0 16 16"><path d="m3.86 8.753 5.482 4.796c.646.566 1.658.106 1.658-.753V3.204a1 1 0 0 0-1.659-.753l-5.48 4.796a1 1 0 0 0 0 1.506z"/></svg></div>'+
                                '<div data-year-val="now"><p>2024-2025</p></div>'+
                                '<div class="years_icon" data-year-val="next"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-right-fill" viewBox="0 0 16 16"><path d="m12.14 8.753-5.482 4.796c-.646.566-1.658.106-1.658-.753V3.204a1 1 0 0 1 1.659-.753l5.48 4.796a1 1 0 0 1 0 1.506z"/></svg></div>'+
                            '</div>'+
                            '<div class="calendar-months">';
                                for(var i = 0; i < _.$label.months.length; i++) {
                                    if (_.$activeR.num === 12) _.$activeR.num = 0;
                                    var activeClass = (_.$activeR.month === i) ? ' active' : '';
                                    markup += '<div class="calendar-month'+activeClass+'" role="button" data-month-val="'+_.$activeR.num+'">'+_.initials.dates[_.options.language].months[i]+'</div>';
                                    _.$activeR.num = _.$activeR.num+1;
                                }
            markup +=      '</div>'+
                        '</div>';
        
            // .calendar-middle
            markup += '<!-- D 區塊 (60%) -->'+
                        '<div class="calendar-middle">';
            // inner
            markup += '<!-- F 區塊 (60%) -->'+
                            '<div class="calendar-inner">'+
                                '<div class="calendar-table">'+
                                    '<div class="row align-items-center">'+
                                        '<div class="col-2"><button id="toggleC" class="btn toggleC radius-item">☰</button></div>'+
                                        '<div class="col-8"><div class="calendar-header calendar-header-dline">十月</div></div>'+
                                        '<div class="col-2"><button id="toggleE" class="btn toggleE radius-item">＞</button></div>'+
                                    '</div>'+
                                    '<div class="calendar-table-inner">'+
                                        '<div class="row calendar-table-header">';
                                        for(var i = 0; i < _.$label.days.length; i++ ){
                                            var headerClass = "calendar-header-day";
                                            if (_.$label.days[i] === _.initials.weekends.sat || _.$label.days[i] === _.initials.weekends.sun) {
                                                headerClass += ' --weekend';
                                            }
                                            markup += '<div class="col '+headerClass+'"><div class="week">'+_.$label.days[i]+'</div></div>';
                                        }
            markup +=                  '</div>'+
                                    '</div>'+
                                '</div>'+
                            '</div>';

            // events
            markup += '<!-- G 區塊 (40%) 滾動區塊 -->'+
                            '<div class="calendar-events">'+
                                '<div class="row">'+
                                    '<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">'+
                                        '<div class="card bg-white">';
            // 選中的日期
            markup +=                       '<div class="card-header event-header">'+
                                                '<div class="row d-flex align-items-center flex-nowrap">'+
                                                    '<div class="col-4">'+
                                                        '<div class="calendar-header-dline">2024</div>'+
                                                    '</div>'+
                                                    '<div class="col-4">'+
                                                        '<div class="calendar-header calendar-header-dline toggleF">十月</div>'+
                                                    '</div>'+
                                                    '<div class="col-4 d-flex align-items-center justify-content-center">'+
                                                        '<div class="calendar-header-dline me-3">05</div>'+
                                                        '<div class="d-flex flex-column">'+
                                                            '<div class="radius-item"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-up-fill" viewBox="0 0 16 16"><path d="m7.247 4.86-4.796 5.481c-.566.647-.106 1.659.753 1.659h9.592a1 1 0 0 0 .753-1.659l-4.796-5.48a1 1 0 0 0-1.506 0z"/></svg></div>'+
                                                            '<div class="radius-item"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-down-fill" viewBox="0 0 16 16"><path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/></svg></div>'+
                                                        '</div>'+
                                                    '</div>'+
                                                '</div>'+  
                                            '</div>';
            // 事件列表
            markup +=                       '<div class="card-body">'+
                                                '<div class="timeline event-list">'+
                                                '</div>'
                                            '</div>';
            markup +=                   '</div>'+
                                    '</div>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>';
            // .calendar-sidebar-right
            markup += '<!-- E 區塊 (20%) -->'+
                    '<div class="calendar-sidebar calendar-sidebar-right main-shadow collapsed">'+
                        '<div class="calendar-months">'+
                            '<div class="calendar-month" role="button">台北西門扶青社</div><div class="calendar-month" role="button">三重扶青社</div><div class="calendar-month" role="button">土城扶青社</div><div class="calendar-month" role="button">桃園扶青社</div><div class="calendar-month" role="button">台北西區扶青社</div><div class="calendar-month" role="button">台北延平扶青社</div><div class="calendar-month" role="button">中和扶青社</div><div class="calendar-month" role="button">台中扶青社</div><div class="calendar-month" role="button">東區扶青社</div><div class="calendar-month" role="button">台中東南扶青社</div><div class="calendar-month" role="button">台北艋舺扶青社</div><div class="calendar-month" role="button">大甲扶青社</div><div class="calendar-month" role="button">北區扶青社</div><div class="calendar-month" role="button">員林扶青社</div><div class="calendar-month" role="button">基隆東南扶青社</div><div class="calendar-month" role="button">首都扶青社</div><div class="calendar-month" role="button">圓山扶青社</div><div class="calendar-month" role="button">忠孝扶青社</div><div class="calendar-month" role="button">高雄東北扶青社</div><div class="calendar-month" role="button">宜蘭扶青社</div><div class="calendar-month" role="button">楊梅扶青社</div><div class="calendar-month" role="button">台中港扶青社</div><div class="calendar-month" role="button">永和扶青社</div><div class="calendar-month" role="button">銘傳扶青社</div><div class="calendar-month" role="button">雙溪扶青社</div><div class="calendar-month" role="button">台北城中扶青社</div><div class="calendar-month" role="button">百齡扶青社</div><div class="calendar-month" role="button">新東扶青社</div><div class="calendar-month" role="button">陽光扶青社</div>'+
                        '</div>'+
                    '</div>';

            markup += '</div>';
            // --- Finally, build it now! --- //
            _.$elements.calendarEl.html(markup);

            if (!_.$elements.headbarEl) _.$elements.headbarEl = $(_.$elements.calendarEl).find('.calendar-headbar');
            if (!_.$elements.sidebarEl) _.$elements.sidebarEl = $(_.$elements.calendarEl).find('.calendar-sidebar');
            if (!_.$elements.innerEl) _.$elements.innerEl = $(_.$elements.calendarEl).find('.calendar-inner');
            if (!_.$elements.eventEl) _.$elements.eventEl = $(_.$elements.calendarEl).find('.calendar-events');

            // if: _.options.sidebarToggler
            // if(_.options.sidebarToggler) {
            //     $(_.$elements.sidebarEl).append('<span id="sidebarToggler" role="button" aria-pressed title="'+_.initials.dates[_.options.language].closeSidebarText+'"><button class="icon-button"><span class="bars"></span></button></span>');
            //     if(!_.$elements.sidebarToggler) _.$elements.sidebarToggler = $(_.$elements.sidebarEl).find('span#sidebarToggler');
            // }
            // if(_.options.eventListToggler) {
            //     $(_.$elements.calendarEl).append('<span id="eventListToggler" role="button" aria-pressed title="'+_.initials.dates[_.options.language].closeEventListText+'"><button class="icon-button"><span class="chevron-arrow-right"></span></button></span>');
            //     if(!_.$elements.eventListToggler) _.$elements.eventListToggler = $(_.$elements.calendarEl).find('span#eventListToggler');
            // }
        }

        _.buildSidebarYear();
        _.buildSidebarMonths();
        _.buildCalendar();
        _.buildEventList();
        _.initEventListener(); // test

        _.resize();
    }

    // v1.0.0 - Initialize plugin
    EvoCalendar.prototype.init = function(init) {
        var _ = this;
        
        if (!$(_.$elements.calendarEl).hasClass('calendar-initialized')) {
            $(_.$elements.calendarEl).addClass('evo-calendars calendar-initialized');
            if (_.windowW <= _.$breakpoints.tablet) { // tablet/mobile
                _.toggleHeaderDistrict(true);
                _.toggleSidebar(false);
                _.toggleEventList(false);
            } else {
                if (!_.options.sidebarDisplayDefault) _.toggleSidebar(false);
                else _.toggleSidebar(true);

                if (!_.options.eventDisplayDefault) _.toggleEventList(false);
                else _.toggleEventList(true);
            }
            if (_.options.theme) _.setTheme(_.options.theme); // set calendar theme
            _.buildTheBones(); // start building the calendar components
        }
    };

    // v_Rotary - 切換 Header 區域按鈕
    EvoCalendar.prototype.toggleHeaderDistrict = function(event) {
        var _ = this;

        // 檢查事件是否未定義或是由實際事件觸發
        if (event === undefined || event.originalEvent) {
            // 切換 'header-hide' 類別，來控制 header 顯示或隱藏
            $(_.$elements.calendarEl).toggleClass('header-hide');
            _.$UI.hasHeaderDistrict = !_.$UI.hasHeaderDistrict; // 切換狀態
        } else {
            // 如果事件為 true，顯示 header
            if(event) {
                $(_.$elements.calendarEl).removeClass('header-hide');
                _.$UI.hasHeaderDistrict = true;
            } else {
                // 如果事件為 false，隱藏 header
                $(_.$elements.calendarEl).addClass('header-hide');
                _.$UI.hasHeaderDistrict = false;
            }
        }

        // 如果螢幕寬度小於平板裝置的斷點，則處理相應的動作
        if (_.windowW <= _.$breakpoints.tablet) {
            if (_.$UI.hasHeaderDistrict && _.$UI.hasSidebar) _.toggleSidebar(false);
        }
    };

    // v1.0.0 - Toggle Sidebar
    EvoCalendar.prototype.toggleSidebar = function(event) {
        var _ = this;

        if (event === undefined || event.originalEvent) {
            $(_.$elements.calendarEl).toggleClass('sidebar-hide');
            _.$UI.hasSidebar = !_.$UI.hasSidebar;
        } else {
            if(event) {
                $(_.$elements.calendarEl).removeClass('sidebar-hide');
                _.$UI.hasSidebar = true;
            } else {
                $(_.$elements.calendarEl).addClass('sidebar-hide');
                _.$UI.hasSidebar = false;
            }
        }

        if (_.windowW <= _.$breakpoints.tablet) {
            if (_.$UI.hasSidebar && _.$UI.hasEvent) _.toggleEventList();
        }
    };

    // v1.0.0 - Calculate days (incl. monthLength, startingDays based on :firstDayOfWeekName)
    EvoCalendar.prototype.calculateDays = function() {
        var _ = this, nameDays, weekStart, firstDay, rotaryYear;
        _.monthLength = _.$label.days_in_month[_.$active.month]; // find number of days in month
        if (_.$active.month <= 5 ) {
            rotaryYear = _.$active.year + 1;
        } else {
            rotaryYear = _.$active.year
        }
        if (_.$active.month == 1) { // compensate for leap year - february only!
            if((rotaryYear % 4 == 0 && rotaryYear % 100 != 0) || rotaryYear % 400 == 0){
                _.monthLength = 29;
            }
        }
        nameDays = _.initials.dates[_.options.language].daysShort;
        weekStart = _.options.firstDayOfWeek;
        
        while (_.$label.days.length < nameDays.length) {
            if (weekStart == nameDays.length) {
                weekStart=0;
            }
            _.$label.days.push(nameDays[weekStart]);
            weekStart++;
        }
        firstDay = new Date(rotaryYear, _.$active.month).getDay() - weekStart;
        _.startingDay = firstDay < 0 ? (_.$label.days.length + firstDay) : firstDay;
    }

    // v1.0.0 - Build Calendar: Title, Days
    EvoCalendar.prototype.buildCalendar = function() {
        var _ = this, markup, title, rotaryYear, rotaryMonth;
        
        _.calculateDays();

        if (_.$active.month <= 5 ) {
            rotaryYear = _.$active.year + 1;
            rotaryMonth = _.$active.month+6;
        } else {
            rotaryYear = _.$active.year;
            rotaryMonth = _.$active.month-6;
        }

        title = _.formatDate(new Date(_.$label.months[rotaryMonth] +' 1 '+ rotaryYear), _.options.titleFormat, _.options.language);
        // _.$elements.innerEl.find('.calendar-inner > div > div > div > div').text(title);
        $('.calendar-header').text(title);

        _.$elements.innerEl.find('.calendar-table-body').remove(); // Clear days
        
        markup = '<div class="row calendar-table-body">';
                    var day = 1;
                    for (var i = 0; i < 9; i++) { // this loop is for is weeks (rows)
                        for (var j = 0; j < _.$label.days.length; j++) { // this loop is for weekdays (cells)
                            if (day <= _.monthLength && (i > 0 || j >= _.startingDay)) {
                                var dayClass = "col calendar-day";
                                if (_.$label.days[j] === _.initials.weekends.sat || _.$label.days[j] === _.initials.weekends.sun) {
                                    dayClass += ' --weekend'; // add '--weekend' to sat sun
                                }
                                markup += '<div class="'+dayClass+'">';

                                var thisDay = _.formatDate(_.$label.months[_.$active.month]+' '+day+' '+rotaryYear, _.options.format);
                                markup += '<div class="day" role="button" data-date-val="'+thisDay+'">'+day+'</div>';
                                day++;
                            } else {
                                markup += '<div class="col calendar-day">';
                            }
                            markup += '</div>';
                        }
                        if (day > _.monthLength) {
                            break; // stop making rows if we've run out of days
                        } else {
                            markup += '</div><div class="row calendar-table-body">'; // add if not
                        }
                    }
                    markup += '</div>';
        _.$elements.innerEl.find('.calendar-table-inner').append(markup);

        if(_.options.todayHighlight) {
            _.$elements.innerEl.find("[data-date-val='" + _.$current.date + "']").addClass('calendar-today');
        }
        
        // set event listener for each day
        _.$elements.innerEl.find('.calendar-day').children()
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectDate)

        var selectedDate = _.$elements.innerEl.find("[data-date-val='" + _.$active.date + "']");
        
        if (selectedDate) {
            // Remove active class to all
            _.$elements.innerEl.children().removeClass('calendar-active');
            // Add active class to selected date
            selectedDate.addClass('calendar-active');
        }
        if(_.options.calendarEvents != null) { // For event indicator (dots)
            _.buildEventIndicator();
        }
    };

    // v1.0.0 - Build Event: Event list
    EvoCalendar.prototype.buildEventList = function() {
        var _ = this, markup, hasEventToday = false, rotaryYear, rotaryMonth;
        
        if (_.$active.month <= 5 ) {
            rotaryYear = _.$active.year + 1;
            rotaryMonth = _.$active.month+6;
        } else {
            rotaryYear = _.$active.year;
            rotaryMonth = _.$active.month-6;
        }
        
        _.$active.events = [];
        // Event date
        var title = _.formatDate(new Date(_.$label.months[rotaryMonth] +' 1 '+ rotaryYear), _.options.eventHeaderFormat, _.options.language);
        _.$elements.eventEl.find('.event-header > p').text(title);
        // Event list
        var eventListEl = _.$elements.eventEl.find('.event-list');
        // Clear event list item(s)
        if (eventListEl.children().length > 0) eventListEl.empty();
        if (_.options.calendarEvents) {
            for (var i = 0; i < _.options.calendarEvents.length; i++) {
                if(_.isBetweenDates(_.$active.date, _.options.calendarEvents[i].date)) {
                    eventAdder(_.options.calendarEvents[i])
                }
                else if (_.options.calendarEvents[i].everyYear) {
                    var d = new Date(_.$active.date).getMonth() + 1 + ' ' + new Date(_.$active.date).getDate();
                    var dd = new Date(_.options.calendarEvents[i].date).getMonth() + 1 + ' ' + new Date(_.options.calendarEvents[i].date).getDate();
                    // var dates = [_.formatDate(_.options.calendarEvents[i].date[0], 'mm/dd'), _.formatDate(_.options.calendarEvents[i].date[1], 'mm/dd')];

                    if(d==dd) {
                        eventAdder(_.options.calendarEvents[i])
                    }
                }
            };
        }
        function eventAdder(event) {
            hasEventToday = true;
            _.addEventList(event)
        }
        // IF: no event for the selected date
        if(!hasEventToday) {
            markup = '<div class="event-empty">';
            if (_.$active.date === _.$current.date) {
                markup += '<p>'+_.initials.dates[_.options.language].noEventForToday+'</p>';
            } else {
                markup += '<p>'+_.initials.dates[_.options.language].noEventForThisDay+'</p>';
            }
            markup += '</div>';
        }
        eventListEl.append(markup)
    };

    // v1.0.0 - Add single event to event list
    EvoCalendar.prototype.addEventList = function(event_data) {
        var _ = this, markup;
        var eventListEl = _.$elements.eventEl.find('.event-list');
        if (eventListEl.find('[data-event-index]').length === 0) eventListEl.empty();
        _.$active.events.push(event_data);
        markup =    '<div class="timeline-row" role="button" data-event-index="'+(event_data.id)+'">';
        // markup +=       '<div class="timeline-time">7:45PM</div>'
        markup +=       '<div class="timeline-time">'+event_data.time+'</div>';
        // markup += '<div class="event-icon"><div class="event-bullet-'+event_data.type+'"';
        markup +=       '<div class="timeline-dot fb-bg"></div>';
        markup +=       '<div class="timeline-content">';
        // markup +=           '<h4>No.191 帶你進入不動產世界</h4>';
        markup +=           '<h4>'+_.limitTitle(event_data.name)+'</h4>';
        // markup +=           '<p>[八德陽德扶青社]</p>';
        markup +=           '<p>['+event_data.club+']</p>';
        markup +=       '</div>';
        markup +=   '</div>';
        eventListEl.append(markup);
        // markup += '<div class="event-icon"><div class="event-bullet-'+event_data.type+'"';
        // if (event_data.color) {
        //     markup += 'style="background-color:'+event_data.color+'"'
        // }
        // markup += '></div></div><div class="event-info"><p class="event-title">'+_.limitTitle(event_data.name);
        // if (event_data.badge) markup += '<span>'+event_data.badge+'</span>';
        // markup += '</p>'
        // if (event_data.description) markup += '<p class="event-desc">'+event_data.description+'</p>';
        // markup += '</div>';
        // markup += '</div>';
        // eventListEl.append(markup);

        _.$elements.eventEl.find('[data-event-index="'+(event_data.id)+'"]')
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectEvent);
    };













    // r1.0.0 - Select Dirtrict
    EvoCalendar.prototype.selectDistrict = function(event) {
        var _ = this;
        var el, districtId;

        // 防止默認行為
        event.preventDefault();
        // 使用 jQuery 獲取 data-district-val 屬性值
        districtId = $(event.currentTarget).data('district-val');

        _.$elements.headbarEl.find('.district-list > [data-district-val]').removeClass('active');
        _.$elements.headbarEl.find('.district-list > [data-district-val="'+districtId+'"]').addClass('active');



        $.ajax({
            url: '/xkRotaract/api/manage/club/list',
            method: 'POST',
            data: JSON.stringify({ 'district': districtId }),
            processData: false,
            contentType: 'application/json',
            success: function(response) {
                console.log('AJAX 请求成功：', response);

//                _.options.calendarEvents = response;
                // 找到 E 區塊中的 .calendar-months 容器
                const calendarMonthsContainer = document.querySelector('.calendar-sidebar-right .calendar-months');

                // 清空現有內容
                calendarMonthsContainer.innerHTML = '';

                // 動態插入新內容
                response.forEach(item => {
                    const calendarMonthDiv = document.createElement('div');
                    calendarMonthDiv.className = 'calendar-month';
                    calendarMonthDiv.setAttribute('role', 'button');
                    calendarMonthDiv.setAttribute('data-rotaract-id', item.id); // 可根據需求加上其他屬性
                    calendarMonthDiv.textContent = item.name;

                    // 插入到容器中
                    calendarMonthsContainer.appendChild(calendarMonthDiv);
                });
            },
            error: function(xhr, status, error) {
                console.error('AJAX 请求失败：', error);
            }
        });

//
//        if(yearVal == "prev") {
//            --_.$active.year;
//        } else if (yearVal == "next") {
//            ++_.$active.year;
//        } else if (yearVal == "now") {
//            _.$active.year = _.$current.year;
//        } else if (typeof yearVal === 'number') {
//            _.$active.year = yearVal;
//        }
//
//        if (_.windowW <= _.$breakpoints.mobile) {
//            if(_.$UI.hasSidebar) _.toggleSidebar(false);
//        }
//
//        $(_.$elements.calendarEl).trigger("selectYear", [_.$active.year])
//
//        _.buildSidebarYear();
//        _.buildCalendar();
    };

    // v1.0.0 - Select year
    EvoCalendar.prototype.selectYear = function(event) {
        var _ = this;
        var el, yearVal;

        if (typeof event === 'string' || typeof event === 'number') {
            if ((parseInt(event)).toString().length === 4) {
                yearVal = parseInt(event);
            }
        } else {
            el = $(event.target).closest('[data-year-val]');
            yearVal = $(el).data('yearVal');
        }

        if(yearVal == "prev") {
            --_.$active.year;
        } else if (yearVal == "next") {
            ++_.$active.year;
        } else if (yearVal == "now") {
            _.$active.year = _.$current.year;
        } else if (typeof yearVal === 'number') {
            _.$active.year = yearVal;
        }
        
        if (_.windowW <= _.$breakpoints.mobile) {
            if(_.$UI.hasSidebar) _.toggleSidebar(false);
        }
        
        $(_.$elements.calendarEl).trigger("selectYear", [_.$active.year])

        _.buildSidebarYear();
        _.buildCalendar();
    };

    // v1.0.0 - Build Sidebar: Year text
    // EvoCalendar.prototype.buildSidebarYear = function() {
    //     var _ = this;
        
    //     _.$elements.sidebarEl.find('.calendar-year > p').text(_.$active.year);
    // }
    EvoCalendar.prototype.buildSidebarYear = function() {
        var _ = this;

        var currentYear = _.$active.year;  // 獲取當前年份
        var currentMonth = _.$active.month;    // 獲取當前月份 (0: January, 11: December)

        var displayedYears;

        // 根據當前年份和月份來決定顯示的年份範圍
        if (currentMonth < 6) { // 上半年 (0-5 月)
            displayedYears = [currentYear - 1, currentYear];
        } else { // 下半年 (6-11 月)
            displayedYears = [currentYear, currentYear + 1];
        }

        // 更新側邊欄顯示的年份     
        _.$elements.sidebarEl.find('.calendar-year > div > p').text(displayedYears[0] + "-" + displayedYears[1]);
    };

    // v1.0.0 - Select month
    EvoCalendar.prototype.selectMonth = function(event) {
        var _ = this;
        
        if (typeof event === 'string' || typeof event === 'number') {
            if (event >= 0 && event <=_.$label.months.length) {
                // if: 0-11
                _.$active.month = (event).toString();
            }
        } else {
            // if month is manually selected
            _.$active.month = $(event.currentTarget).data('monthVal');
        }
        
        _.buildSidebarMonths();
        _.buildCalendar();
        
        if (_.windowW <= _.$breakpoints.tablet) {
            if(_.$UI.hasSidebar) _.toggleSidebar(false);
        }

        // EVENT FIRED: selectMonth
        $(_.$elements.calendarEl).trigger("selectMonth", [_.initials.dates[_.options.language].months[_.$active.month], _.$active.month])
    };

    // v1.0.0 - Build Sidebar: Months list text
    // EvoCalendar.prototype.buildSidebarMonths = function() {
    //     var _ = this;
        
    //     _.$elements.sidebarEl.find('.calendar-months > [data-month-val]').removeClass('active-month');
    //     _.$elements.sidebarEl.find('.calendar-months > [data-month-val="'+_.$active.month+'"]').addClass('active-month');
    // }
    EvoCalendar.prototype.buildSidebarMonths = function() {
        var _ = this;

        _.$elements.sidebarEl.find('.calendar-months > [data-month-val]').removeClass('active-month');
        _.$elements.sidebarEl.find('.calendar-months > [data-month-val="'+_.$active.month+'"]').addClass('active-month');
    };












    
    // v1.0.0 - Parse format (date)
    EvoCalendar.prototype.parseFormat = function(format) {
        var _ = this;
        if (typeof format.toValue === 'function' && typeof format.toDisplay === 'function')
            return format;
        // IE treats \0 as a string end in inputs (truncating the value),
        // so it's a bad format delimiter, anyway
        var separators = format.replace(_.initials.validParts, '\0').split('\0'),
            parts = format.match(_.initials.validParts);
        if (!separators || !separators.length || !parts || parts.length === 0){
            console.log("%c Invalid date format ", "color:white;font-weight:bold;background-color:#e21d1d;");
        }
        return {separators: separators, parts: parts};
    };
    
    // v1.0.0 - Format date
    EvoCalendar.prototype.formatDate = function(date, format, language) {
        var _ = this;
        if (!date)
            return '';
        language = language ? language : _.defaults.language
        if (typeof format === 'string')
            format = _.parseFormat(format);
        if (format.toDisplay)
            return format.toDisplay(date, format, language);

        var ndate = new Date(date);
        // if (!_.isValidDate(ndate)) { // test
        //     ndate = new Date(date.replace(/-/g, '/'))
        // }
        
        var val = {
            d: ndate.getDate(),
            D: _.initials.dates[language].daysShort[ndate.getDay()],
            DD: _.initials.dates[language].days[ndate.getDay()],
            m: ndate.getMonth() + 1,
            M: _.initials.dates[language].monthsShort[ndate.getMonth()],
            MM: _.initials.dates[language].months[ndate.getMonth()],
            yy: ndate.getFullYear().toString().substring(2),
            yyyy: ndate.getFullYear()
        };
        
        val.dd = (val.d < 10 ? '0' : '') + val.d;
        val.mm = (val.m < 10 ? '0' : '') + val.m;
        date = [];
        var seps = $.extend([], format.separators);
        for (var i=0, cnt = format.parts.length; i <= cnt; i++){
            if (seps.length)
                date.push(seps.shift());
            date.push(val[format.parts[i]]);
        }
        return date.join('');
    };

    // v1.0.0 - Get dates between two dates
    EvoCalendar.prototype.getBetweenDates = function(dates) {
        var _ = this, betweenDates = [];
        for (var x = 0; x < _.monthLength; x++) {
            var active_date = _.formatDate(_.$label.months[_.$active.month] +' '+ (x + 1) +' '+ _.$active.year, _.options.format);
            if (_.isBetweenDates(active_date, dates)) {
                betweenDates.push(active_date);
            }
        }
        return betweenDates;
    };
    
    // v1.0.0 - Check if event has the same event type in the same date
    EvoCalendar.prototype.hasSameDayEventType = function(date, type) {
        var _ = this, eventLength = 0;

        for (var i = 0; i < _.options.calendarEvents.length; i++) {
            if (_.options.calendarEvents[i].date instanceof Array) {
                var arr = _.getBetweenDates(_.options.calendarEvents[i].date);
                for (var x = 0; x < arr.length; x++) {
                    if(date === arr[x] && type === _.options.calendarEvents[i].type) {
                        eventLength++;
                    }
                }
            } else {
                if(date === _.options.calendarEvents[i].date && type === _.options.calendarEvents[i].type) {
                    eventLength++;
                }
            }
        }

        if (eventLength > 0) {
            return true;
        }
        return false;
    }
    
    // v1.0.0 - Set calendar theme
    EvoCalendar.prototype.setTheme = function(themeName) {
        var _ = this;
        var prevTheme = _.options.theme;
        _.options.theme = themeName.toLowerCase().split(' ').join('-');

        if (_.options.theme) $(_.$elements.calendarEl).removeClass(prevTheme);
        if (_.options.theme !== 'default') $(_.$elements.calendarEl).addClass(_.options.theme);
    }

    // v1.0.0 - Called in every resize
    EvoCalendar.prototype.resize = function() {
        var _ = this;
        _.windowW = $(window).width();

        if (_.windowW <= _.$breakpoints.tablet) { // tablet
            _.toggleSidebar(false);
            _.toggleEventList(false);

            if (_.windowW <= _.$breakpoints.mobile) { // mobile
                $(window)
                    .off('click.evocalendar.evo-' + _.instanceUid)
            } else {
                $(window)
                    .on('click.evocalendar.evo-' + _.instanceUid, $.proxy(_.toggleOutside, _));
            }
        } else {
            if (!_.options.sidebarDisplayDefault) _.toggleSidebar(false);
            else _.toggleSidebar(true);

            if (!_.options.eventDisplayDefault) _.toggleEventList(false);
            else _.toggleEventList(true);
            
            $(window)
                .off('click.evocalendar.evo-' + _.instanceUid);
        }
    }

    // v1.0.0 - Initialize event listeners
    EvoCalendar.prototype.initEventListener = function() {
        var _ = this;

        // resize
        $(window)
            .off('resize.evocalendar.evo-' + _.instanceUid)
            .on('resize.evocalendar.evo-' + _.instanceUid, $.proxy(_.resize, _));

        // IF sidebarToggler: set event listener: toggleSidebar
        // if(_.options.sidebarToggler) {
        //     _.$elements.sidebarToggler
        //     .off('click.evocalendar')
        //     .on('click.evocalendar', _.toggleSidebar);
        // }
        
        // IF eventListToggler: set event listener: toggleEventList
        // if(_.options.eventListToggler) {
        //     _.$elements.eventListToggler
        //     .off('click.evocalendars')
        //     .on('click.evocalendars', _.toggleEventList);
        // }

        // set event listener for each month
        _.$elements.sidebarEl.find('[data-month-val]')
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectMonth);

        // set event listener for year
        _.$elements.sidebarEl.find('[data-year-val]')
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectYear);

        // set event listener for every event listed
        _.$elements.eventEl.find('[data-event-index]')
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectEvent);

        // set event listener for district
        _.$elements.headbarEl.find('[data-district-val]')
        .off('click.evocalendar')
        .on('click.evocalendar', _.selectDistrict);


        // 控制 C 區塊的開關
        document.querySelectorAll('.toggleC').forEach(function(element) {
            element.addEventListener('click', function () {
                var calendarSidebar = document.querySelector('.calendar-sidebar-left');
                var calendarMiddle = document.querySelector('.calendar-middle');

                // 切換 C 區塊的折疊狀態
                calendarSidebar.classList.toggle('collapsed');

                // 根據 C 區塊是否折疊，調整 D 區塊的寬度
                if (calendarSidebar.classList.contains('collapsed')) {
                    calendarSidebar.style.width = '0';  // 折疊 C 區塊
                    calendarMiddle.style.width = '80%';  // 增加 D 區塊的寬度
                } else {
                    calendarSidebar.style.width = '20%';  // 還原 C 區塊寬度
                    calendarMiddle.style.width = '60%';  // 還原 D 區塊寬度
                }
            });
        });


        // 控制 E 區塊的開關
        document.getElementById('toggleE').addEventListener('click', function () {
            var calendarSidebar = document.querySelector('.calendar-sidebar-right');
            var calendarMiddle = document.querySelector('.calendar-middle');

            // 切換 E 區塊的折疊狀態
            calendarSidebar.classList.toggle('collapsed');

            // 根據 E 區塊是否折疊，調整 D 區塊的寬度
            if (calendarSidebar.classList.contains('collapsed')) {
                calendarSidebar.style.width = '0';  // 折疊 E 區塊
                calendarMiddle.style.width = '80%';  // 增加 D 區塊的寬度
            } else {
                calendarSidebar.style.width = '20%';  // 還原 E 區塊的寬度
                calendarMiddle.style.width = '60%';  // 還原 D 區塊的寬度
            }
        });


        // 控制 F 區塊的開關
        // 選擇所有的 day 元素
        const dayElements = document.querySelectorAll('.day');

        // 選擇 calendar-table 元素
        const calendarInner = document.querySelector('.calendar-inner');
        const calendarEvents = document.querySelector('.calendar-events');
        // 添加點擊事件給每個 day 元素
        dayElements.forEach(day => {
            day.addEventListener('click', function() {
                // 切換 calendar-table 的折疊狀態
                calendarInner.classList.toggle('collapsed');

                if (calendarInner.classList.contains('collapsed')) {
                    calendarInner.style.height = '0%';
                    calendarEvents.style.height = '100%';
                }
            });
        });
        document.querySelectorAll('.toggleF').forEach(function(element) {
            element.addEventListener('click', function() {
                // 切換 calendar-table 的折疊狀態
                calendarInner.classList.toggle('collapsed');

                if (!calendarInner.classList.contains('collapsed')) {
                    calendarInner.style.removeProperty('height'); // 移除高度設置
                    // calendarInner.style.height = '100%';
                    // calendarEvents.style.height = '50%';
                }
            });
        });
        document.querySelector('.calendar-year').addEventListener('click', function () {
            const calendarMonths = document.querySelector('.calendar-months');

            // 檢查視窗寬度，僅當寬度小於768px時生效
            if (window.innerWidth < 768) {
                // 切換 active 類
                calendarMonths.classList.toggle('active');

                // 根據 active 類設置最大高度
                if (calendarMonths.classList.contains('active')) {
                    calendarMonths.style.maxHeight = calendarMonths.scrollHeight + 'px';
                } else {
                    calendarMonths.style.maxHeight = '0';
                }
            }
        });
    };

    // v1.0.0 - Select specific date
    EvoCalendar.prototype.selectDate = function(event) {
        var _ = this;
        var oldDate = _.$active.date;
        var date, year, month, activeDayEl, isSameDate;

        if (typeof event === 'string' || typeof event === 'number' || event instanceof Date) {
            date = _.formatDate(new Date(event), _.options.format)
            year = new Date(date).getFullYear();
            month = new Date(date).getMonth();
            
            if (_.$active.year !== year) _.selectYear(year);
            if (_.$active.month !== month) _.selectMonth(month);
            activeDayEl = _.$elements.innerEl.find("[data-date-val='" + date + "']");
        } else {
            activeDayEl = $(event.currentTarget);
            date = activeDayEl.data('dateVal')
        }
        isSameDate = _.$active.date === date;
        // Set new active date
        _.$active.date = date;
        _.$active.event_date = date;
        // Remove active class to all
        _.$elements.innerEl.find('[data-date-val]').removeClass('calendar-active');
        // Add active class to selected date
        activeDayEl.addClass('calendar-active');
        // Build event list if not the same date events built
        if (!isSameDate) _.buildEventList();

        // EVENT FIRED: selectDate
        $(_.$elements.calendarEl).trigger("selectDate", [_.$active.date, oldDate])
    };

    // v1.0.0 - Check if date is between the passed calendar date 
    EvoCalendar.prototype.isBetweenDates = function(active_date, dates) {
        var sd, ed;
        if (dates instanceof Array) {
            sd = new Date(dates[0]);
            ed = new Date(dates[1]);
        } else {
            sd = new Date(dates);
            ed = new Date(dates);
        }
        if (sd <= new Date(active_date) && ed >= new Date(active_date)) {
            return true;
        }
        return false;
    };




    // v1.0.0 - Toggle Event list
    EvoCalendar.prototype.toggleEventList = function(event) {
        var _ = this;

        if (event === undefined || event.originalEvent) {
            $(_.$elements.calendarEl).toggleClass('event-hide');
            _.$UI.hasEvent = !_.$UI.hasEvent;
        } else {
            if(event) {
                $(_.$elements.calendarEl).removeClass('event-hide');
                _.$UI.hasEvent = true;
            } else {
                $(_.$elements.calendarEl).addClass('event-hide');
                _.$UI.hasEvent = false;
            }
        }

        if (_.windowW <= _.$breakpoints.tablet) {
            if (_.$UI.hasEvent && _.$UI.hasSidebar) _.toggleSidebar();
        }
    };



   


    // v1.0.0 - Limit title (...)
    EvoCalendar.prototype.limitTitle = function(title, limit) {
        var newTitle = [];
        limit = limit === undefined ? 18 : limit;
        if ((title).split(' ').join('').length > limit) {
            var t = title.split(' ');
            for (var i=0; i<t.length; i++) {
                if (t[i].length + newTitle.join('').length <= limit) {
                    newTitle.push(t[i])
                }
            }
            return newTitle.join(' ') + '...'
        }
        return title;
    }

    // v1.0.0 - Remove single event to event list
    EvoCalendar.prototype.removeEventList = function(event_data) {
        var _ = this, markup;
        var eventListEl = _.$elements.eventEl.find('.event-list');
        if (eventListEl.find('[data-event-index="'+event_data+'"]').length === 0) return; // event not in active events
        eventListEl.find('[data-event-index="'+event_data+'"]').remove();
        if (eventListEl.find('[data-event-index]').length === 0) {
            eventListEl.empty();
            if (_.$active.date === _.$current.date) {
                markup += '<p>'+_.initials.dates[_.options.language].noEventForToday+'</p>';
            } else {
                markup += '<p>'+_.initials.dates[_.options.language].noEventForThisDay+'</p>';
            }
            eventListEl.append(markup)
        }
    }
    
    // v1.1.2 - Check and filter strings
    EvoCalendar.prototype.stringCheck = function(d) {
        return d.replace(/[^\w]/g, '\\$&');
    }

    // v1.0.0 - Add event indicator/s (dots)
    EvoCalendar.prototype.addEventIndicator = function(event) {
        var _ = this, htmlToAppend, thisDate;
        var event_date = event.date;
        var type = _.stringCheck(event.type);
        
        if (event_date instanceof Array) {
            if (event.everyYear) {
                for (var x=0; x<event_date.length; x++) {
                    event_date[x] = _.formatDate(new Date(event_date[x]).setFullYear(_.$active.year), _.options.format);
                }
            }
            var active_date = _.getBetweenDates(event_date);
            
            for (var i=0; i<active_date.length; i++) {
                appendDot(active_date[i]);
            }
        } else {
            if (event.everyYear) {
                event_date = _.formatDate(new Date(event_date).setFullYear(_.$active.year), _.options.format);
            }
            appendDot(event_date);
        }

        function appendDot(date) {
            thisDate = _.$elements.innerEl.find('[data-date-val="'+date+'"]');

            if (thisDate.find('span.event-indicator').length === 0) {
                thisDate.append('<span class="event-indicator"></span>');
            }
            
            if (thisDate.find('span.event-indicator > .type-bullet > .type-'+type).length === 0) {
                htmlToAppend = '<div class="type-bullet"><div ';
                
                htmlToAppend += 'class="type-'+event.type+'"'
                if (event.color) { htmlToAppend += 'style="background-color:'+event.color+'"' }
                htmlToAppend += '></div></div>';
                thisDate.find('.event-indicator').append(htmlToAppend);
            }
        }      
    };
    
    // v1.0.0 - Remove event indicator/s (dots)
    EvoCalendar.prototype.removeEventIndicator = function(event) {
        var _ = this;
        var event_date = event.date;
        var type = _.stringCheck(event.type);

        if (event_date instanceof Array) {
            var active_date = _.getBetweenDates(event_date);
            
            for (var i=0; i<active_date.length; i++) {
                removeDot(active_date[i]);
            }
        } else {
            removeDot(event_date);
        }

        function removeDot(date) {
            // Check if no '.event-indicator', 'cause nothing to remove
            if (_.$elements.innerEl.find('[data-date-val="'+date+'"] span.event-indicator').length === 0) {
                return;
            }

            // // If has no type of event, then delete 
            if (!_.hasSameDayEventType(date, type)) {
                _.$elements.innerEl.find('[data-date-val="'+date+'"] span.event-indicator > .type-bullet > .type-'+type).parent().remove();
            }
        }
    };
    
    /****************
    *    METHODS    *
    ****************/

    // v1.0.0 - Build event indicator on each date
    EvoCalendar.prototype.buildEventIndicator = function() {
        var _ = this;
        
        // prevent duplication
        _.$elements.innerEl.find('.calendar-day > day > .event-indicator').empty();
        
        for (var i = 0; i < _.options.calendarEvents.length; i++) {
            _.addEventIndicator(_.options.calendarEvents[i]);
        }
    };

    // v1.0.0 - Select event
    EvoCalendar.prototype.selectEvent = function(event) {
        var _ = this;
        var el = $(event.target).closest('.event-container');
        var id = $(el).data('eventIndex').toString();
        var index = _.options.calendarEvents.map(function (event) { return (event.id).toString() }).indexOf(id);
        var modified_event = _.options.calendarEvents[index];
        if (modified_event.date instanceof Array) {
            modified_event.dates_range = _.getBetweenDates(modified_event.date);
        }
        $(_.$elements.calendarEl).trigger("selectEvent", [_.options.calendarEvents[index]])
    }

    
    // v1.0.0 - Return active date
    EvoCalendar.prototype.getActiveDate = function() {
        var _ = this;
        return _.$active.date;
    }
    
    // v1.0.0 - Return active events
    EvoCalendar.prototype.getActiveEvents = function() {
        var _ = this;
        return _.$active.events;
    }

    // v1.0.0 - Hide Sidebar/Event List if clicked outside
    EvoCalendar.prototype.toggleOutside = function(event) {
        var _ = this, isInnerClicked;
        
        isInnerClicked = event.target === _.$elements.innerEl[0];

        if (_.$UI.hasSidebar && isInnerClicked) _.toggleSidebar(false);
        if (_.$UI.hasEvent && isInnerClicked) _.toggleEventList(false);
    }

    // v1.0.0 - Add Calendar Event(s)
    EvoCalendar.prototype.addCalendarEvent = function(arr) {
        var _ = this;

        function addEvent(data) {
            if(!data.id) {
                console.log("%c Event named: \""+data.name+"\" doesn't have a unique ID ", "color:white;font-weight:bold;background-color:#e21d1d;");
            }

            if (data.date instanceof Array) {
                for (var j=0; j < data.date.length; j++) {
                    if(isDateValid(data.date[j])) {
                        data.date[j] = _.formatDate(new Date(data.date[j]), _.options.format);
                    }
                }
            } else {
                if(isDateValid(data.date)) {
                    data.date = _.formatDate(new Date(data.date), _.options.format);
                }
            }
            
            if (!_.options.calendarEvents) _.options.calendarEvents = [];
            _.options.calendarEvents.push(data);
            // add to date's indicator
            _.addEventIndicator(data);
            // add to event list IF active.event_date === data.date
            if (_.$active.event_date === data.date) _.addEventList(data);
            // _.$elements.innerEl.find("[data-date-val='" + data.date + "']")

            function isDateValid(date) {
                if(_.isValidDate(date)) {
                    return true;
                } else {
                    console.log("%c Event named: \""+data.name+"\" has invalid date ", "color:white;font-weight:bold;background-color:#e21d1d;");
                }
                return false;
            }
        }
        if (arr instanceof Array) { // Arrays of events
            for(var i=0; i < arr.length; i++) {
                addEvent(arr[i])
            }
        } else if (typeof arr === 'object') { // Single event
            addEvent(arr)
        }
    };

    // v1.0.0 - Remove Calendar Event(s)
    EvoCalendar.prototype.removeCalendarEvent = function(arr) {
        var _ = this;

        function deleteEvent(data) {
            // Array index
            var index = _.options.calendarEvents.map(function (event) { return event.id }).indexOf(data);
            
            if (index >= 0) {
                var event = _.options.calendarEvents[index];
                // Remove event from calendar events
                _.options.calendarEvents.splice(index, 1);
                // remove to event list
                _.removeEventList(data);
                // remove event indicator
                _.removeEventIndicator(event);
            } else {
                console.log("%c "+data+": ID not found ", "color:white;font-weight:bold;background-color:#e21d1d;");
            }
        }
        if (arr instanceof Array) { // Arrays of index
            for(var i=0; i < arr.length; i++) {
                deleteEvent(arr[i])
            }
        } else { // Single index
            deleteEvent(arr)
        }
    };

    // v1.0.0 - Check if date is valid
    EvoCalendar.prototype.isValidDate = function(d){
        return new Date(d) && !isNaN(new Date(d).getTime());
    }

    // v1.0.0 - Destroy event listeners
    EvoCalendar.prototype.destroyEventListener = function() {
        var _ = this;
        
        $(window).off('resize.evocalendar.evo-' + _.instanceUid);
        $(window).off('click.evocalendar.evo-' + _.instanceUid);
        
        // IF sidebarToggler: remove event listener: toggleSidebar
        // if(_.options.sidebarToggler) {
        //     _.$elements.sidebarToggler
        //     .off('click.evocalendar');
        // }
        
        // IF eventListToggler: remove event listener: toggleEventList
        if(_.options.eventListToggler) {
            _.$elements.eventListToggler
            .off('click.evocalendar');
        }

        // remove event listener for each day
        _.$elements.innerEl.find('.calendar-day').children()
        .off('click.evocalendar')

        // remove event listener for each month
        _.$elements.sidebarEl.find('[data-month-val]')
        .off('click.evocalendar');

        // remove event listener for year
        _.$elements.sidebarEl.find('[data-year-val]')
        .off('click.evocalendar');

        // remove event listener for every event listed
        _.$elements.eventEl.find('[data-event-index]')
        .off('click.evocalendar');
    };

    // v1.0.0 - Destroy plugin
    EvoCalendar.prototype.destroy = function() {
        var _ = this;
        // code here
        _.destroyEventListener();
        if (_.$elements.calendarEl) {
            _.$elements.calendarEl.removeClass('calendar-initialized');
            _.$elements.calendarEl.removeClass('evo-calendar');
            _.$elements.calendarEl.removeClass('sidebar-hide');
            _.$elements.calendarEl.removeClass('event-hide');
        }
        _.$elements.calendarEl.empty();
        _.$elements.calendarEl.attr('class', _.initials.default_class);
        $(_.$elements.calendarEl).trigger("destroy", [_])
    };

    $.fn.evoCalendar = function() {
        var _ = this,
            opt = arguments[0],
            args = Array.prototype.slice.call(arguments, 1),
            l = _.length,
            i,
            ret;
        for (i = 0; i < l; i++) {
            if (typeof opt == 'object' || typeof opt == 'undefined')
                _[i].evoCalendar = new EvoCalendar(_[i], opt);
            else
                ret = _[i].evoCalendar[opt].apply(_[i].evoCalendar, args);
            if (typeof ret != 'undefined') return ret;
        }
        return _;
    };

}));
