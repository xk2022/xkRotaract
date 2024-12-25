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
 *
 *
    ***** 初始化與核心邏輯 *****
    init() {} - Initialize Calendar
    (private)initializeSidebarAndEventList(isTabletOrMobile) {} -
    buildTheBones() {} - Build the bones!
    initEventListener() {} - Initialize event listeners
    resize() {} - Called in every resize

    ***** 側邊欄與主介面管理 *****
    toggleSidebar(show) {} - Toggle Sidebar
    toggleDBlock(showFBlock, showGBlock) {} - Toggle F and G Blocks
    toggleEventList(show) {} - Toggle Event list
    (private)generateHeadbarDistricts() {} - Generate Headbar District Tabs
    (private)fetchDistrictCodes() {} - Fetch District Codes
    (private)generateSidebarMonths() {} - Generate Sidebar Month List
    (private)generateHeaderWeekdays() {} - Generate Weekdays
    (private)generateSidebarClubs() {} - Generate Sidebar Club List
    (private)fetchClubs() {} - Fetch Clubs
    buildSidebarYear() {} - Build Sidebar: Year text
    buildSidebarMonths() {} - Build Sidebar: Months list text

    ***** 日曆生成與更新 *****
    calculateDays() {} - Calculate days
    buildCalendar() {} - Build Calendar
    buildEventList() {} - Build Event
    selectYear(event) {} - Select year
    selectMonth(event) {} - Select month
    selectDate(event) {} - Select specific date

    ***** 事件管理 *****
    addEventIndicator(event) {} - Add event indicator/s (dots)
    removeEventIndicator(event) {} - Remove event indicator/s (dots)
    (此版供display用，未更新過此方法)addCalendarEvent
    (此版供display用，未更新過此方法)removeCalendarEvent
    addEventList(eventData) {} - Add single event to event list
    removeEventList(eventId) {} - Remove single event from event list
    selectEvent(event) {} - Select event
    getActiveEvents() {} - Return active events
    (private)selectDistrict(event) {} - Select District
    (private)selectClub(event) {} - Select District

    ***** 工具函數 *****
    formatDate(date, format) {} - Format date
    parseFormat(format) {} - Parse format (date)
    isBetweenDates(date, range) {} - Check if date is between the passed calendar date
    isValidDate(date) {} - Check if date is valid
    (private)getBetweenDates(dates) {} - Get Dates Between Two Dates
    (private)hasSameDayEventType(date, type) {} - Check Same Day Event Type
    (private)setTheme(themeName) {} - Set Calendar Theme
    (private)buildEventIndicator() {} - Build Event Indicator
    (private)getActiveDate() {} - Get Active Date
    (private)toggleOutside(event) {} - Toggle Sidebar/Event List
    (private)destroyEventListener() {} - Destroy Event Listeners
    (private)destroy() {} - Destroy Plugin
    (private)limitTitle(title, limit) {} - Limit Title
    (private)escapeSpecialCharacters - (stringCheck) Check and filter strings
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
                x: null
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
                        months: ["July", "August", "September", "October", "November", "December", "January", "February", "March", "April", "May", "June"],
                        monthsShort: ["Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"],
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
                        monthsShort: ["Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun"],
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
            _.districtData = null;
            _.windowW = $(window).width();
            // CURRENT 初始化當前日期配置
            const today = new Date();
            const currentMonth = today.getMonth();
            const currentYear = today.getFullYear();
            // 配置當前日期
            _.$current = {
                month: isNaN(this.month) || this.month == null ? currentMonth : this.month,
                year: isNaN(this.year) || this.year == null ? currentYear : this.year,
                date: _.formatDate(new Date(currentYear, currentMonth, today.getDate()), _.options.format)
            };
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
            _.toggleSidebar = $.proxy(_.toggleSidebar, _);
            _.toggleEventList = $.proxy(_.toggleEventList, _);
            
            _.instanceUid = instanceUid++;

            _.init(true);
        }

        return EvoCalendar;

    }());

    /******************************
    *****    初始化與核心邏輯    *****
    ******************************/
    // init() {} - Initialize Calendar
    EvoCalendar.prototype.init = function(init) {
        const _ = this;

        $(document).ready(() => { // 確保 DOM 已載入
            $(_.element).addClass('evo-calendar');
            // 防止重複初始化
            if (!$(_.$elements.calendarEl).hasClass('calendar-initialized')) {
                $(_.$elements.calendarEl).addClass('evo-calendars calendar-initialized');
                // 設置初始狀態（根據螢幕尺寸切換）
                const isTabletOrMobile = (_.windowW <= _.$breakpoints.tablet);
                _.initializeSidebarAndEventList(isTabletOrMobile);
                // 設置主題
                if (_.options.theme) _.setTheme(_.options.theme);
                // 構建日曆和事件監聽器
                _.buildTheBones();
                _.initEventListener();
            }
        });
    };

    // 新增一個輔助方法來初始化側邊欄和事件列表狀態
    EvoCalendar.prototype.initializeSidebarAndEventList = function(isTabletOrMobile) {
        const _ = this;

        if (isTabletOrMobile) {
            _.toggleSidebar(false);
            _.toggleEventList(false);
        } else {
            _.toggleSidebar(_.options.sidebarDisplayDefault);
            _.toggleEventList(_.options.eventDisplayDefault);
        }
    };

    // v1.0.0 - Build the bones! (incl. sidebar, inner, events), called once in every initialization
    EvoCalendar.prototype.buildTheBones = function() {
        const _ = this;
        _.calculateDays();
        
        if (_.$elements.calendarEl.html()) {
            // Build essential components
            _.buildSidebarYear();
            _.buildSidebarMonths();
            _.buildCalendar();
            _.buildEventList();
            _.resize();
            return;
        }
        // --- BUILDING MARKUP BEGINS --- //
        const markup = `
            <!-- A 區塊 (5%) Header Section -->
            <div class="calendar-headbar main-shadow text-center">
                <div class="district-list"> <!-- 電腦版 row district-list -->
                    <!-- generateHeadbarDistricts() -->
                </div>
            </div>

            <!-- B 區塊 (95%) Calendar Body -->
            <div class="calendar-body">
                <!-- C 區塊 (20%) Sidebar Left -->
                <div class="calendar-sidebar calendar-sidebar-left main-shadow">
                    <div class="calendar-year">
                        <div class="years_icon" data-year-val="prev">&lt;</div>
                        <div data-year-val="now"><p>YYYY-YYYY</p></div>
                        <div class="years_icon" data-year-val="next">&gt;</div>
                    </div>
                    <div id="calendar-sidebar-left" class="calendar-months">
                        <!-- generateSidebarMonths() -->
                    </div>
                </div>

                <!-- D 區塊 (60%) Calendar Middle -->
                <div class="calendar-middle">
                    <!-- F 區塊 (60%) generateHeaderWeekdays -->
                    <div class="calendar-inner">
                        <div class="calendar-table">
                            <div class="row align-items-center">
                                <div class="col-2">
                                    <button id="toggleC" class="btn toggleC radius-item">☰</button>
                                </div>
                                <div class="col-8">
                                    <div class="calendar-header calendar-header-dline">十月</div>
                                </div>
                                <div class="col-2">
                                    <button id="toggleE" class="btn toggleE radius-item">＞</button>
                                </div>
                            </div>
                            <div class="calendar-table-inner">
                                <div class="row calendar-table-header">
                                    <!-- generateHeaderWeekdays() -->
                                </div>
                            </div>

                            <div class="calendar-table-end">
                                <div class="calendar-end-actions">
                                    <button id="collapseCalendar" class="btn collapse-calendar calendar-header-dline">︽ 收起</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- G 區塊 (40%) 滾動區塊 -->
                    <div class="calendar-events">
                        <div class="row">
                            <div class="col-xl-12 col-lg-12 col-md-12 col-sm-12">
                                <div class="card bg-white">
                                    <!-- 選中的日期 -->
                                    <div class="card-header event-header">
                                        <div class="row d-flex align-items-center flex-nowrap">
                                            <div class="col-4">
                                                <div id="event-header-year" class="calendar-header-dline">2024</div>
                                            </div>
                                            <div class="col-4">
                                                <div id="event-header-month" class="calendar-header calendar-header-dline toggleF">十月</div>
                                            </div>
                                            <div class="col-4 d-flex align-items-center justify-content-center">
                                                <div id="event-header-day" class="calendar-header-dline me-3">05</div>
                                        <!--        <div class="d-flex flex-column"> -->
                                        <!--            <div class="radius-item"> -->
                                        <!--                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-up-fill" viewBox="0 0 16 16"> -->
                                        <!--                    <path d="m7.247 4.86-4.796 5.481c-.566.647-.106 1.659.753 1.659h9.592a1 1 0 0 0 .753-1.659l-4.796-5.48a1 1 0 0 0-1.506 0z"/> -->
                                        <!--                </svg> -->
                                        <!--            </div> -->
                                        <!--            <div class="radius-item"> -->
                                        <!--                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-caret-down-fill" viewBox="0 0 16 16"> -->
                                        <!--                    <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/> -->
                                        <!--                </svg> -->
                                        <!--            </div> -->
                                        <!--        </div> -->
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 事件列表 -->
                                    <div class="card-body">
                                        <div class="timeline event-list"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- E 區塊 (20%) Sidebar Right -->
                <div class="calendar-sidebar calendar-sidebar-right main-shadow">
                    <div id="calendar-sidebar-right" class="calendar-clubs">
                        <!-- generateSidebarClubs() -->
                    </div>
                </div>
            </div>
        `;
        // --- Insert Markup into DOM --- //
        _.$elements.calendarEl.html(markup);
        _.generateHeadbarDistricts();
        _.generateSidebarMonths();
        _.generateHeaderWeekdays();
        _.generateSidebarClubs();
        // Cache key DOM elements
        _.$elements.headbarEl = _.$elements.calendarEl.find('.calendar-headbar');
        _.$elements.sidebarEl = _.$elements.calendarEl.find('.calendar-sidebar');
        _.$elements.innerEl = _.$elements.calendarEl.find('.calendar-inner');
        _.$elements.eventEl = _.$elements.calendarEl.find('.calendar-events');
        // Build essential components (top)
        _.buildTheBones();
    };

    // v1.0.0 - Initialize event listeners
    EvoCalendar.prototype.initEventListener = function() {
        const _ = this;
        // Resize listener
        $(window)
            .off(`resize.evocalendar.evo-${_.instanceUid}`)
            .on(`resize.evocalendar.evo-${_.instanceUid}`, $.proxy(_.resize, _));

        // Helper function to safely add event listeners
        const safeAddEventListener = (selector, event, handler) => {
            const elements = document.querySelectorAll(selector);
            if (elements.length > 0) {
                elements.forEach(el => el.addEventListener(event, handler));
            } else {
                console.warn(`No elements found for selector: ${selector}`);
            }
        };
        // Add general event listeners
        safeAddEventListener('[data-month-val]', 'click', _.selectMonth.bind(_));
        safeAddEventListener('[data-year-val]', 'click', _.selectYear.bind(_));
        safeAddEventListener('[data-event-index]', 'click', _.selectEvent.bind(_));
        // 修正使用await監聽綁定失效方式
//        safeAddEventListener('[data-district-val]', 'click', _.selectDistrict.bind(_));
        document.addEventListener('click', function(event) {
            const target = event.target.closest('[data-district-val]');
            if (target) {
                _.selectDistrict.call(_, event); // 绑定你的方法
            }
        });
        document.addEventListener('click', function(event) {
            const target = event.target.closest('[data-club-val]');
            if (target) {
                _.selectClub.call(_, event); // 绑定你的方法
            }
        });

        // Sidebar toggle controls
        const toggleSidebar = (selector, sidebarClass, middleClass, collapsedWidth, expandedWidth) => {
            safeAddEventListener(selector, 'click', function() {
                const sidebar = document.querySelector(sidebarClass);
                const middle = document.querySelector(middleClass);
                if (!sidebar || !middle) return;

                // Toggle collapsed state
                sidebar.classList.toggle('collapsed');
                const isCollapsed = sidebar.classList.contains('collapsed');
                sidebar.style.width = isCollapsed ? collapsedWidth : expandedWidth;
                middle.style.width = isCollapsed ? '100%' : '60%'; // Adjust middle section width
            });
        };
        toggleSidebar('#toggleC', '.calendar-sidebar-left', '.calendar-middle', '0', '20%');
        toggleSidebar('#toggleE', '.calendar-sidebar-right', '.calendar-middle', '0', '20%');
        document.querySelector('#toggleE')?.click();

        // F and G Block toggle controls
        const calendarInner = document.querySelector('.calendar-inner');
        const calendarTableInner = document.querySelector('.calendar-table-inner');
        const calendarEvents = document.querySelector('.calendar-events');
        const $toggleText = $('#collapseCalendar'); // 找到按鈕本身
        if (calendarInner && calendarEvents) {
            safeAddEventListener('.day', 'click', function() {
                const isCollapsed = calendarInner.classList.contains('collapsed');
                calendarInner.style.height = isCollapsed ? '0%' : 'auto';
                calendarTableInner.style.height = isCollapsed ? '0%' : 'auto';
                if (_.windowW <= _.$breakpoints.tablet) {
//                    $('#collapseCalendar').click();
//                    calendarTableInner.classList.toggle('collapsed');
                    calendarEvents.style.height = isCollapsed ? '100%' : 'auto';
                } else {
                    calendarEvents.style.height = isCollapsed ? '100%' : '50%';
                }
            });

            safeAddEventListener('.toggleF', 'click', function() {
                calendarTableInner.classList.toggle('collapsed');
                if (!calendarTableInner.classList.contains('collapsed')) {
                    calendarTableInner.style.removeProperty('height');
                }
            });

            safeAddEventListener('#collapseCalendar', 'click', function() {
                $('.toggleF').click();
                // 包装 calendarTableInner 为 jQuery 对象
                const $calendarTableInner = $(calendarTableInner);

                if (!$calendarTableInner.hasClass('collapsed')) {
                    // 如果日历已经展开，折叠它
//                    $calendarTableInner.addClass('collapsed').slideUp(300); // 使用 jQuery 添加折叠状态并添加动画
                    $toggleText.text('＾收起'); // 更新按钮文字或图标
                } else {
                    // 如果日历已折叠，展开它
//                    $calendarTableInner.removeClass('collapsed').slideDown(300); // 移除折叠状态并添加动画
                    $toggleText.text('＞展開'); // 更新按钮文字或图标
                }
            });
        }

        // Year dropdown toggle for smaller screens
        safeAddEventListener('.calendar-year', 'click', function (event) {
            if (_.windowW <= _.$breakpoints.tablet) {
                const calendarMonths = document.querySelector('.calendar-months');
                if (!calendarMonths) return;
                // 检查点击是否在伪元素 `::after` 的范围内
                const calendarYear = event.target.closest('.calendar-year');
                if (!calendarYear) return;

                const rect = calendarYear.getBoundingClientRect();
                const clickX = event.clientX;
                const clickY = event.clientY;
                // 计算伪元素的区域
                const afterFontSize = 20; // 与 CSS 的 font-size 保持一致
                const afterMarginLeft = 5 * parseFloat(getComputedStyle(document.documentElement).fontSize); // 5vh 转换为像素
                const afterStartX = rect.right - afterFontSize - afterMarginLeft;
                const afterEndX = rect.right;
                const afterStartY = rect.top;
                const afterEndY = rect.bottom;

                if (clickX >= afterStartX && clickX <= afterEndX && clickY >= afterStartY && clickY <= afterEndY) {
                    // 点击在 `::after` 区域，触发侧边栏逻辑
                    calendarMonths.classList.toggle('active');
                    calendarMonths.style.maxHeight = calendarMonths.classList.contains('active')
                        ? `${calendarMonths.scrollHeight}px`
                        : '0';
                }
            }
        });
    };

    // v1.0.0 - Called in every resize
    EvoCalendar.prototype.resize = function() {
        const _ = this;
        _.windowW = window.innerWidth;
        // Helper function to toggle elements
        const toggleElement = (condition, element, showCallback, hideCallback) => {
            if (condition) {
                showCallback();
            } else {
                hideCallback();
            }
        };
        // Tablet and mobile view handling
        if (_.windowW <= _.$breakpoints.tablet) { // Tablet view
            _.toggleSidebar(false);
            _.toggleEventList(false);
            // Handle outside click events for tablets
            toggleElement(
                _.windowW > _.$breakpoints.mobile,
                window,
                () => $(window).on(`click.evocalendar.evo-${_.instanceUid}`, $.proxy(_.toggleOutside, _)),
                () => $(window).off(`click.evocalendar.evo-${_.instanceUid}`)
            );
        } else { // Desktop view
            // Toggle sidebar based on default setting
            toggleElement(
                _.options.sidebarDisplayDefault,
                null,
                () => _.toggleSidebar(true),
                () => _.toggleSidebar(false)
            );
            // Toggle event list based on default setting
            toggleElement(
                _.options.eventDisplayDefault,
                null,
                () => _.toggleEventList(true),
                () => _.toggleEventList(false)
            );
            // Remove outside click listener for desktop
            $(window).off(`click.evocalendar.evo-${_.instanceUid}`);
        }
    };



    /******************************
    ****    側邊欄與主介面管理    ****
    ******************************/
    // v1.0.0 - Toggle Sidebar
    EvoCalendar.prototype.toggleSidebar = function(show) {
        var _ = this;

        // 決定是否要切換側邊欄的顯示狀態
        const shouldShow = (show === undefined) ? !_.$UI.hasSidebar : show;
        // 更新側邊欄的顯示狀態
        $(_.$elements.calendarEl).toggleClass('sidebar-hide', !shouldShow);
        _.$UI.hasSidebar = shouldShow;
        // 平板模式下，若側邊欄顯示，則隱藏事件列表
        if (_.windowW <= _.$breakpoints.tablet && _.$UI.hasSidebar && _.$UI.hasEvent) {
            _.toggleEventList(false);
        }
    };

    // Toggle F and G Blocks
    EvoCalendar.prototype.toggleDBlock = function(showFBlock = true, showGBlock = true) {
        const calendarInner = document.querySelector('.calendar-inner.calendar-inner'); // F 區塊
        const calendarEvents = document.querySelector('.calendar-events'); // G 區塊
        if (!calendarInner || !calendarEvents) {
            console.error("Cannot find calendar-inner or calendar-events element.");
            return;
        }
        // 顯示或隱藏 F 區塊
        calendarInner.style.display = showFBlock ? 'block' : 'none';
        // 顯示或隱藏 G 區塊
        calendarEvents.style.display = showGBlock ? 'block' : 'none';
    };

    // v1.0.0 - Toggle Event list
    EvoCalendar.prototype.toggleEventList = function(show) {
        const _ = this;

        // 決定是否要切換事件列表的顯示狀態
        const shouldShow = (show === undefined) ? !_.$UI.hasEvent : show;
        // 更新事件列表的顯示狀態
        $(_.$elements.calendarEl).toggleClass('event-hide', !shouldShow);
        _.$UI.hasEvent = shouldShow;
        // 平板模式下，若事件列表顯示，則隱藏側邊欄
        if (_.windowW <= _.$breakpoints.tablet && _.$UI.hasEvent && _.$UI.hasSidebar) {
            _.toggleSidebar(false);
        }
    };

    // (private) Helper to Generate Headbar District Tabs
    EvoCalendar.prototype.generateHeadbarDistricts = async function() {
        const _ = this;

        // 確保 .district-list 容器存在
        const container = document.querySelector('.district-list');
        if (!container) {
            console.error("未找到 .district-list，請檢查 HTML 結構！");
            return;
        }
        // 初始化內容：添加 "ALL DISTRICT" 預設按鈕
        container.innerHTML = `
            <div class="btn tab-btn active" data-district-val="ALL">ALL DISTRICT</div>
        `;
        try {
            // 獲取地區代碼
            const districtCodes = await _.fetchDistrictCodes();
            // 動態生成地區按鈕
            const districtButtons = districtCodes.map(code => `
                <div class="btn tab-btn" data-district-val="${code}">${code}</div>
            `).join('');
            // 插入按鈕到容器中
            container.insertAdjacentHTML('beforeend', districtButtons);
        } catch (error) {
            // 錯誤處理
            console.error("生成 District Tabs 失敗:", error);
            container.insertAdjacentHTML('beforeend', `
                <div class="btn tab-btn error">無法載入地區資訊</div>
            `);
        }
    };

    // (private) Helper to Fetch District Codes
    EvoCalendar.prototype.fetchDistrictCodes = function() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/xkRotaract/api/manage/organization/findChildren',
                method: 'POST',
                data: JSON.stringify({ 'code': 'RIT' }),
                processData: false,
                contentType: 'application/json',
                success: function(response) {
                    const districtCodes = response.map(item => item.code);
                    resolve(districtCodes);
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 失敗:", error);
                    reject(error);
                }
            });
        });
    };

    // (private) Helper to Generate Sidebar Month List
    EvoCalendar.prototype.generateSidebarMonths = function() {
        const _ = this;

        const container = document.getElementById('calendar-sidebar-left');
        // 確保容器存在
        if (!container) {
            console.error("未找到 #calendar-sidebar-left，請檢查 HTML 結構！");
            return;
        }
        // 生成月份的 HTML 字符串
        let activeNum = _.$activeR.num;
        const monthsMarkup =
            _.initials.dates[_.options.language].months.map((month, i) => {
                if (activeNum === 12) activeNum = 0; // 循環重置月份計數
                const activeClass = activeNum === _.$activeR.month ? ' active' : '';
                const markup = `<div class="calendar-month${activeClass}" role="button" data-month-val="${activeNum}">${month}</div>`;
                activeNum++;
                return markup;
            }).join(''); // 將數組轉換為單一 HTML 字符串
        // 插入按鈕到容器中
        container.insertAdjacentHTML('beforeend', monthsMarkup);
    };

    // (private) Helper to Generate Header Weekdays of calendar-table-header
    EvoCalendar.prototype.generateHeaderWeekdays = function() {
        const _ = this;

        const container = document.querySelector('.calendar-table-header');
        // 確保容器存在
        if (!container) {
            console.error("未找到 .calendar-table-header，請檢查 HTML 結構！");
            return;
        }
        // 逐一插入每個星期標籤
        _.$label.days.forEach(day => {
            const headerClass = (_.initials.weekends.sat === day || _.initials.weekends.sun === day)
                ? 'calendar-header-day --weekend'
                : 'calendar-header-day';
            const markup = `<div class="col ${headerClass}"><div class="week">${day}</div></div>`;
            container.insertAdjacentHTML('beforeend', markup); // 動態插入 HTML
        });
    };

    // (private) Helper to Generate Sidebar Clubs
    EvoCalendar.prototype.generateSidebarClubs = async function() {
        const _ = this;
        // 獲取 Sidebar 容器
        const container = document.querySelector('.calendar-sidebar-right .calendar-clubs');
        if (!container) {
            console.error("未找到 .calendar-clubs，請檢查 HTML 結構！");
            return;
        }

        try {
            // 確保容器為空，避免重複插入
            container.innerHTML = '';
            // 使用異步方法取得社團資料
            const districtData = await _.fetchClubs();
            // 根據 districtData 生成 HTML
            const districtId = _.$active.districtId || "ALL"; // 預設載入 ID 為 "ALL"
            const clubs =
            districtData[districtId] || ["無相關資料"];
            // 生成 HTML 並插入
            const markup = clubs.map(club =>
                `<div class="calendar-club" role="button" data-club-val="${club.code}">${club.name}</div>`
            ).join(''); // 將數組轉換為單一 HTML 字符串
            // 插入生成的 HTML 到容器中
            container.insertAdjacentHTML('beforeend', markup);
        } catch (error) {
            console.error("載入社團資料失敗:", error);
            // 插入預設的錯誤提示
            container.innerHTML = `<div class="calendar-club">無法載入社團資料</div>`;
        }
    };

    // (private) Helper to Fetch District Codes
    EvoCalendar.prototype.fetchClubs = function() {
        const _ = this;

        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/xkRotaract/api/manage/organization/getAllDistricts',
                method: 'POST',
                processData: false,
                contentType: 'application/json',
                success: function(response) {
                    if (typeof response === 'object') {
                        _.districtData = response; // 将数据赋值到实例变量中
//                        console.info("District Data fetched successfully:", response);
                        resolve(response); // 完成 Promise，返回数据
                    } else {
                        console.warn("Unexpected response format:", response);
                        reject("Invalid response format");
                    }
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 请求失败:", error);
                    reject(error); // 将错误传递给 Promise 链
                }
            });
        });
    };

    // v_Rotary - Build Sidebar: Year text
    EvoCalendar.prototype.buildSidebarYear = async function() {
        const _ = this;

        // 獲取當前年份與月份
        const { year: currentYear, month: currentMonth } = _.$active;
        // 計算顯示的年份範圍
        const displayedYears = currentMonth < 6
            ? `${currentYear - 1}-${currentYear}` // 上半年
            : `${currentYear}-${currentYear + 1}`; // 下半年
        // 更新側邊欄年份顯示
        _.$elements.sidebarEl.find('.calendar-year > div > p').text(displayedYears);

        // 使用異步方法取得資料
        await _.fetchCalendarData(_.$active.year);
        // Transforming calendarDistrictMap to calendarEvents
        _.options.calendarEvents = Object.values(_.options.calendarDistrictMap)
            .flat()
            .map(event => {
                // Optional: Add custom transformations if needed
                return {
                    ...event, // Preserve existing properties
                    eventName: event.name // Example transformation: Rename 'name' to 'eventName'
                };
            });
        _.buildEventList();
//        console.info('Transformed Calendar Events:', _.options.calendarEvents);
    };

    // v_Rotary - Build Sidebar: Months list text
    EvoCalendar.prototype.buildSidebarMonths = function() {
        const _ = this;

        // 獲取所有月份節點
        const months = _.$elements.sidebarEl.find('.calendar-months > [data-month-val]');
        // 移除所有 `active-month` 樣式
        months.removeClass('active-month');
        // 添加 `active-month` 樣式到當前月份
        months.filter(`[data-month-val="${_.$active.month}"]`).addClass('active-month');
    };



    /******************************
    *****     日曆生成與更新     *****
    ******************************/
    // v_Rotary - Calculate days (incl. monthLength, startingDays based on :firstDayOfWeekName)
    EvoCalendar.prototype.calculateDays = function() {
        const _ = this;

        // 計算當前年份的扶輪年度
        const rotaryYear = _.$active.month <= 5 ? _.$active.year + 1 : _.$active.year;
        // 獲取當前月份的天數
        _.monthLength = _.$label.days_in_month[_.$active.month];
        // 如果是閏年的二月，設置天數為 29
        if (_.$active.month === 1 && isLeapYear(rotaryYear)) {
            _.monthLength = 29;
        }
        // 判斷是否為閏年的工具函數
        function isLeapYear(year) {
            return (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
        }
        // 根據首日偏移，生成對應的星期排列
        const { daysShort: nameDays } = _.initials.dates[_.options.language];
        const weekStart = _.options.firstDayOfWeek;
        _.$label.days = [...nameDays.slice(weekStart), ...nameDays.slice(0, weekStart)];
        // 計算該月份的第一天是星期幾
        _.startingDay = (new Date(rotaryYear, _.$active.month).getDay() - weekStart + 7) % 7;
    };

    // v_Rotary - Build Calendar: Title, Days
    EvoCalendar.prototype.buildCalendar = function() {
        const _ = this;

        // 計算每月的天數與開始日期
        _.calculateDays();
        // 確定扶輪年度與月份
        const rotaryYear = _.$active.year;
        const rotaryMonth = _.adjustRotaryIndex(_.$active.month);
        // 設置日曆標題
        const title = _.formatDate(
            new Date(_.$label.months[rotaryMonth] + ' 1 ' + rotaryYear),
            _.options.titleFormat,
            _.options.language
        );
        $('.calendar-header').text(title);
        // 清除舊的日曆表格
        _.$elements.innerEl.find('.calendar-table-body').remove();
        // 開始生成日曆表格的 HTML 結構
        let markup = `<div class="row calendar-table-body">`;
        let day = 1, dayClass, thisDay;

        for (let week = 0; week < 6; week++) { // 最多六週
            for (let weekday = 0; weekday < _.$label.days.length; weekday++) {
                // 判斷是否需要填充當前日期
                if (day <= _.monthLength && (week > 0 || weekday >= _.startingDay)) {
                    dayClass = (_.initials.weekends.sat === _.$label.days[weekday] ||
                                _.initials.weekends.sun === _.$label.days[weekday])
                                ? 'calendar-day --weekend' : 'calendar-day';
                    thisDay = _.formatDate(
                        `${_.$label.months[_.$activeR.month]} ${day} ${rotaryYear}`,
                        _.options.format
                    );
                    markup += `
                        <div class="col ${dayClass}">
                            <div class="day" role="button" data-date-val="${thisDay}">${day}</div>
                        </div>
                    `;
                    day++;
                } else {
                    // 空白補充
                    markup += `<div class="col calendar-day"></div>`;
                }
            }
            markup += '</div>';

            if (day > _.monthLength) {
                break; // 當月日期已完成
            } else {
                markup += `</div><div class="row calendar-table-body">`; // 新增下一週
            }
        }
        markup += '</div>';
        // 更新日曆表格內容
        _.$elements.innerEl.find('.calendar-table-inner').append(markup);

        // 高亮今天
        if (_.options.todayHighlight) {
            _.$elements.innerEl.find(`[data-date-val="${_.$current.date}"]`).addClass('calendar-today');
        }
        // 高亮當前選中的日期
        const selectedDateElement = _.$elements.innerEl.find(`[data-date-val="${_.$active.date}"]`);
        if (selectedDateElement) {
            _.$elements.innerEl.find('.calendar-day .day').removeClass('calendar-active');
            selectedDateElement.addClass('calendar-active');
        }
        // 如果有日曆事件，添加事件指示器
        if (_.options.calendarEvents) {
            _.buildEventIndicator();
        }
        // 設置點擊事件監聽器
        _.$elements.innerEl
            .find('.calendar-day .day')
            .off('click.evocalendar')
            .on('click.evocalendar', _.selectDate);
    };

    EvoCalendar.prototype.buildEventHeader = function () {
        const _ = this;

        try {
            // 格式化當前日期為標題格式
            const title = _.formatDate(
                new Date(_.$active.date),
                _.options.eventHeaderFormat,
                _.options.language
            );
            // 假設格式為 "十二月 1, 2024"，將標題分解為年份、月份和日期
            const [month, day, year] = title.split(/[\s,]+/);
            // 更新 DOM 元素
            $('#event-header-year').text(year);
            $('#event-header-month').text(month);
            $('#event-header-day').text(day.padStart(2, '0')); // 左側補零，確保日期為兩位數
            // 記錄日誌
//            console.info(`[EvoCalendar] Event header updated: Year=${year}, Month=${month}, Day=${day.padStart(2, '0')}`);
        } catch (error) {
            console.error('[EvoCalendar] Failed to build event header:', error);
        }
    };

    // v1.0.0 - Build Event: Event list
    EvoCalendar.prototype.buildEventList = function () {
        const _ = this;
        let hasEventToday = false;

        _.buildEventHeader();
        // 清空事件列表
        const eventListEl = document.querySelector('.event-list');
        eventListEl.innerHTML = ''; // 使用原生 JavaScript 清空內容

        // 重置當前活動事件
        _.$active.events = [];
        // 檢查並生成事件列表
        if (_.options.calendarEvents && _.options.calendarEvents.length > 0) {
            _.options.calendarEvents.forEach((event) => {
                if (_.isBetweenDates(_.$active.date, event.date) || isEveryYearMatch(event)) {
                    hasEventToday = true;
                    addEvent(event);
                }
            });
        }
        // 無事件的情況
        if (!hasEventToday) {
            const noEventMessage = (_.$active.date === _.$current.date)
                    ? _.initials.dates[_.options.language].noEventForToday
                    : _.initials.dates[_.options.language].noEventForThisDay;
            const noEventMarkup = `
                <div class="event-empty">
                    <p>${noEventMessage}</p>
                </div>
                `;
            eventListEl.insertAdjacentHTML('beforeend', noEventMarkup);
        }

        // 添加事件到事件列表
        function addEvent(event) {
            _.addEventList(event);
        }
        // 每年事件匹配檢查
        function isEveryYearMatch(event) {
            if (!event.everyYear) return false;

            const activeDate = new Date(_.$active.date);
            const eventDate = new Date(event.date);
            return (
                activeDate.getMonth() === eventDate.getMonth() &&
                activeDate.getDate() === eventDate.getDate()
            );
        }
    };

    // (private) Helper to Fetch Calendar Data
    EvoCalendar.prototype.fetchCalendarData = function(year) {
        const _ = this;

        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/xkRotaract/api/manage/calendar/getAllByYear',
                method: 'POST',
                data: JSON.stringify({ year: year }),
                contentType: 'application/json',
                success: function(response) {
                    _.options.calendarDistrictMap = response;
//                    console.info("Successfully fetched calendar data:", response);
                    resolve(response); // Return the fetched data
                },
                error: function(xhr, status, error) {
                    console.error("Failed to fetch calendar data:", error);
                    reject(error);
                }
            });
        });
    };

    // v1.0.0 - Select year
    EvoCalendar.prototype.selectYear = function (event) {
        const _ = this;
        let yearVal;

        // 判斷輸入是年份值還是事件對象
        if (typeof event === "string" || typeof event === "number") {
            // 檢查年份值是否為有效四位數年份
            yearVal = parseInt(event, 10);
            if (isNaN(yearVal) || yearVal.toString().length !== 4) {
                console.warn("Invalid year value:", event);
                return;
            }
        } else {
            // 從事件目標中提取年份值
            const el = event.target.closest("[data-year-val]");
            if (!el) {
                console.warn("Year element not found:", event);
                return;
            }
            yearVal = el.dataset.yearVal;
        }
        // 處理特殊值（prev、next、now）或具體年份
        switch (yearVal) {
            case "prev":
                _.$active.year--;
                break;
            case "next":
                _.$active.year++;
                break;
            case "now":
                _.$active.year = _.$current.year;
                break;
            default:
                if (typeof yearVal === 'number') {
                    _.$active.year = yearVal;
                } else {
                    console.warn("Unexpected year value:", yearVal);
                    return;
                }
        }

        // 移動設備：自動關閉側邊欄
        if (_.windowW <= _.$breakpoints.mobile && _.$UI.hasSidebar) {
            _.toggleSidebar(false);
        }
        // 觸發年份選擇事件
        $(_.$elements.calendarEl).trigger("selectYear", [_.$active.year]);
        // 重建年份側邊欄和日曆
        _.buildSidebarYear();
        _.buildCalendar();
    };

    // v1.0.0 - Select month
    EvoCalendar.prototype.selectMonth = function(event) {
        const _ = this;

        // 獲取月份值
        let monthVal;
        if (typeof event === 'number' || typeof event === 'string') {
            monthVal = parseInt(event, 10);
        } else if (event && event.currentTarget) {
            monthVal = $(event.currentTarget).data('monthVal');
        } else {
            console.warn("Invalid event type for selecting a month:", event);
            return;
        }
        // 驗證月份範圍
        if (isNaN(monthVal) || monthVal < 0 || monthVal >= _.$label.months.length) {
            console.warn("Invalid month value:", monthVal);
            return;
        }

        // 更新月份數據
        _.$active.year = (monthVal <= 5) ? _.$current.year+1 : _.$current.year
        _.$active.month = monthVal;
        _.$activeR.month = _.adjustRotaryIndex(monthVal);
        const firstDayOfMonth = _.formatDate(
                    new Date(_.$label.months[_.$activeR.month] + ' 1 ' + _.$active.year),
                    _.options.format
                );
        _.$active.date = firstDayOfMonth;
//        _.$active.event_date = firstDayOfMonth;
        // 更新 UI
        _.buildSidebarMonths();
        _.buildCalendar();
        _.buildEventList();
        // 平板或更小的設備：關閉側邊欄
        if (_.windowW <= _.$breakpoints.tablet) {
//            if(_.$UI.hasSidebar) _.toggleSidebar(false);
            $('.calendar-year').click();
        }
        // 觸發自定義事件
        $(_.$elements.calendarEl).trigger("selectMonth", [
            _.initials.dates[_.options.language].months[_.$active.month],
            _.$active.month
        ]);
    };

    // v1.0.0 - Select specific date
    EvoCalendar.prototype.selectDate = function(event) {
        const _ = this;
        const oldDate = _.$active.date;
        let date, year, month, activeDayEl;
        // 統一解析日期
        const parseDate = (input) => {
            if (typeof input === 'string' || typeof input === 'number' || input instanceof Date) {
                return _.formatDate(new Date(input), _.options.format);
            } else if (input && input.currentTarget) {
                return $(input.currentTarget).data('dateVal');
            } else {
                console.warn("Invalid date input:", input);
                return null;
            }
        };
        // 解析並驗證日期
        date = parseDate(event);
        if (!date) return;

        const parsedDate = new Date(date);
        year = parsedDate.getFullYear();
        month = parsedDate.getMonth();
        // 檢查是否需要更新年份或月份
        if (_.$active.year !== year) _.selectYear(year);
        if (_.$active.month !== month) _.selectMonth(month);
        // 更新活動日期
        if (_.$active.date === date) {
            console.info("Selected date is already active:", date);
            return;
        }
        _.$active.date = date;
        _.$active.event_date = date;
        // 更新 UI
        activeDayEl = _.$elements.innerEl.find(`[data-date-val='${date}']`);
        _.$elements.innerEl.find('[data-date-val]').removeClass('calendar-active');
        if (activeDayEl.length > 0) {
            activeDayEl.addClass('calendar-active');
        } else {
            console.warn("Active day element not found for date:", date);
        }
        // 重建事件列表
        _.buildEventList();
        // 觸發事件：selectDate
        $(_.$elements.calendarEl).trigger("selectDate", [_.$active.date, oldDate]);
    };



    /******************************
    *****       事件管理        *****
    ******************************/
    // v1.0.0 - Add event indicator/s (dots)
    EvoCalendar.prototype.addEventIndicator = function(event) {
        const _ = this;

        // 確保事件類型和日期正確
        let eventDates = Array.isArray(event.date) ? [...event.date] : [event.date];
        const type = _.escapeSpecialCharacters(event.type);
        // 如果事件為每年重複，調整年份
        if (event.everyYear != "!0") {
            eventDates = eventDates.map(date =>
                _.formatDate(new Date(date).setFullYear(_.$active.year), _.options.format)
            );
        }
        // 為每個事件日期添加指示點
        eventDates.forEach(date => {
            const formattedDates = Array.isArray(date)
                ? _.getBetweenDates(date)
                : [_.formatDate(date, _.options.format)];

            formattedDates.forEach(appendDot);
        });
        // 子函數：向特定日期添加事件指示點
        function appendDot(date) {
            const thisDate = _.$elements.innerEl.find(`[data-date-val="${date}"]`);
            if (thisDate.length === 0) return; // 找不到對應的日期元素則跳過

            // 確保存在事件指示容器
            let eventIndicator = thisDate.find('span.event-indicator');
            if (eventIndicator.length === 0) {
                eventIndicator = $(`<span class="event-indicator"></span>`);
                thisDate.append(eventIndicator);
            }
            // 檢查是否已存在相同類型的事件
            if (eventIndicator.find(`.type-bullet .type-${type}`).length === 0) {
                const bulletHTML = `
                    <div class="type-bullet">
                        <div class="type-${type}" ${event.color ? `style="background-color:${event.color};"` : ''}></div>
                    </div>
                `;
                eventIndicator.append(bulletHTML);
            }
        }
    };

    // v1.0.0 - Remove event indicator/s (dots)
    EvoCalendar.prototype.removeEventIndicator = function(event) {
        const _ = this;

        // 確保事件類型和日期正確
        let eventDates = Array.isArray(event.date) ? [...event.date] : [event.date];
        const type = _.escapeSpecialCharacters(event.type);
        // 遍歷每個日期並移除指示點
        eventDates.forEach(date => {
            const formattedDates = Array.isArray(date)
                ? _.getBetweenDates(date)
                : [_.formatDate(date, _.options.format)];
            formattedDates.forEach(removeDot);
        });
        // 子函數：移除特定日期的事件指示點
        function removeDot(date) {
            const dateElement = _.$elements.innerEl.find(`[data-date-val="${date}"] span.event-indicator`);
            if (dateElement.length === 0) return; // 如果沒有事件指示點，跳過該日期

            // 移除指定類型的事件指示點
            const typeIndicator = dateElement.find(`.type-bullet > .type-${type}`);
            if (typeIndicator.length > 0) typeIndicator.parent().remove();
            // 如果指示器容器中已無子元素，則移除容器本身
            if (dateElement.children().length === 0) dateElement.remove();
        }
    };

    // v1.0.0 - Add Calendar Event(s)
    EvoCalendar.prototype.addCalendarEvent = function(events) {
        const _ = this;

        // 添加單個事件
        function addEvent(data) {
            if (!data.id) {
                console.warn(`Event named "${data.name}" is missing a unique ID.`);
                return;
            }
            // 處理日期格式
            if (Array.isArray(data.date)) {
                data.date = data.date.map(date => {
                    if (isDateValid(date)) {
                        return _.formatDate(new Date(date), _.options.format);
                    }
                    return null;
                }).filter(Boolean); // 過濾掉無效日期
            } else if (isDateValid(data.date)) {
                data.date = _.formatDate(new Date(data.date), _.options.format);
            } else {
                console.warn(`Event named "${data.name}" has an invalid date.`);
                return;
            }
            // 初始化 calendarEvents
            if (!_.options.calendarEvents) _.options.calendarEvents = [];
            _.options.calendarEvents.push(data);
            // 添加事件指示器
            _.addEventIndicator(data);
            // 如果事件日期與當前激活日期匹配，添加到事件列表
            if (Array.isArray(data.date) && data.date.includes(_.$active.event_date)) {
                _.addEventList(data);
            } else if (data.date === _.$active.event_date) {
                _.addEventList(data);
            }
        }
        // 驗證日期是否有效
        function isDateValid(date) {
            if (_.isValidDate(date)) {
                return true;
            } else {
                console.warn(`Invalid date detected: ${date}`);
                return false;
            }
        }
        // 處理單個事件或事件數組
        if (Array.isArray(events)) {
            events.forEach(event => addEvent(event));
        } else if (typeof events === 'object' && events !== null) {
            addEvent(events);
        } else {
            console.error("Invalid input: Expected an event object or an array of events.");
        }
    };

    // v1.0.0 - Remove Calendar Event(s)
    EvoCalendar.prototype.removeCalendarEvent = function(events) {
        const _ = this;

        // 刪除單個事件
        function deleteEvent(eventId) {
            // 查找事件索引
            const index = _.options.calendarEvents.findIndex(event => event.id === eventId);

            if (index >= 0) {
                const event = _.options.calendarEvents[index];
                // 從事件列表中刪除事件
                _.options.calendarEvents.splice(index, 1);
                // 更新事件指示器和事件列表
                _.removeEventList(eventId);
                _.removeEventIndicator(event);
            } else {
                console.warn(`Event with ID "${eventId}" not found.`);
            }
        }
        // 處理單個或多個事件
        if (Array.isArray(events)) {
            events.forEach(eventId => deleteEvent(eventId));
        } else if (typeof events === 'string' || typeof events === 'number') {
            deleteEvent(events);
        } else {
            console.error("Invalid input: Expected an event ID or an array of event IDs.");
        }
    };

    // v1.0.0 - Add single event to event list
    EvoCalendar.prototype.addEventList = function(eventData) {
        const _ = this;

        // 確保事件列表元素存在
        const eventListEl = _.$elements.eventEl.find('.event-list');
        if (!eventListEl.length) {
            console.error("Event list element not found.");
            return;
        }
        // 如果列表為空，清除任何占位符
        if (!eventListEl.children('[data-event-index]').length) eventListEl.empty();
        // 將事件數據加入活動數據
        _.$active.events.push(eventData);
        // 生成事件的 HTML 標記
        const markup = `
            <div class="timeline-row" role="button" data-event-index="${eventData.id}">
                <div class="timeline-time">${eventData.startTime || 'All day'}</div>
                <div class="timeline-dot fb-bg"></div>
                <div class="timeline-content">
                    <h4>${_.sanitize(eventData.eventName)}</h4>
                    <p>[${_.sanitize(eventData.badge || 'No Badge')}]</p>
                </div>
            </div>
        `;
        // 插入事件並綁定點擊事件
        eventListEl.append(markup);

        const newEventElement = eventListEl.find(`[data-event-index="${eventData.id}"]`);
        newEventElement
            .off('click.evocalendar')
            .on('click.evocalendar', _.selectEvent);
    };

    // v1.0.0 - Remove single event from event list
    EvoCalendar.prototype.removeEventList = function(eventId) {
        const _ = this;

        // 獲取事件列表元素
        const eventListEl = _.$elements.eventEl.find('.event-list');
        if (!eventListEl.length) {
            console.error("Event list element not found.");
            return;
        }
        // 查找要移除的事件元素
        const eventEl = eventListEl.find(`[data-event-index="${eventId}"]`);
        if (!eventEl.length) {
            console.warn(`Event with ID ${eventId} not found in the list.`);
            return;
        }
        // 移除指定事件
        eventEl.remove();

        // 如果列表中已無任何事件
        if (!eventListEl.find('[data-event-index]').length) {
            eventListEl.empty(); // 清空事件列表
            // 設置提示信息
            const isToday = _.$active.date === _.$current.date;
            const message = isToday
                ? _.initials.dates[_.options.language].noEventForToday
                : _.initials.dates[_.options.language].noEventForThisDay;
            // 添加提示信息標記
            const emptyMarkup = `
                <div class="event-empty">
                    <p>${message}</p>
                </div>
            `;
            eventListEl.append(emptyMarkup);
        }
    };

    // v1.0.0 - Select event
    EvoCalendar.prototype.selectEvent = function(event) {
        const _ = this;

        // 獲取事件 DOM 元素
        const el = $(event.target).closest('.event-container');
        if (!el.length) {
            console.warn("No event container found.");
            return; // 防止空元素選取
        }
        // 獲取事件 ID
        const eventId = el.data('eventIndex')?.toString();
        if (!eventId) {
            console.warn("No event ID found in the selected element.");
            return; // 如果沒有事件 ID，退出
        }
        // 查找對應的事件數據
        const selectedEvent = _.options.calendarEvents.find(e => e.id.toString() === eventId);
        if (!selectedEvent) {
            console.warn(`Event with ID ${eventId} not found.`);
            return; // 如果找不到事件，退出
        }
        // 如果事件包含日期範圍，計算範圍內的日期
        if (Array.isArray(selectedEvent.date)) {
            selectedEvent.dates_range = _.getBetweenDates(selectedEvent.date);
        }
        // 觸發自定義事件，並傳遞所選事件的數據
        $(_.$elements.calendarEl).trigger("selectEvent", [selectedEvent]);
        console.info("Event selected:", selectedEvent);
    };

    // v1.0.0 - Select District
    EvoCalendar.prototype.selectDistrict = function(event) {
        const _ = this;
        // 防止默認行為
        event.preventDefault();
        // 獲取點擊的 district ID
        const districtId = $(event.target).closest('[data-district-val]').data('district-val');
        if (!districtId) {
            console.warn("No district ID found in the selected element.");
            return;
        }
        // 更新 Headbar 中的 active 樣式
        _.$elements.headbarEl
            .find(`.district-list > [data-district-val]`)
            .removeClass('active');
        _.$elements.headbarEl
            .find(`.district-list > [data-district-val="${districtId}"]`)
            .addClass('active');
        // 更新 E 區塊 (右側邊欄) 的內容
        const container = document.getElementById("calendar-sidebar-right");
        if (!container) {
            console.error("Calendar sidebar container not found.");
            return;
        }
        container.innerHTML = ""; // 清空內容

        const clubs = _.districtData[districtId] || ["無相關資料"];
        // 使用模板字串生成 HTML
        const markup = clubs.map(
                        club => `
                        <div class="calendar-club" role="button" data-club-val="${club.code}">
                            ${club.name}
                        </div>`
                    )
                    .join(""); // 將數組轉換為單一字串
        container.innerHTML = markup; // 一次性插入到 DOM 中
        // 可選：觸發自定義事件
        $(_.$elements.calendarEl).trigger("selectDistrict", [districtId, clubs]);
//        console.info(`District ${districtId} selected. Clubs updated:`, clubs);
        // 更新事件
        if (districtId === "ALL") {
            _.options.calendarEvents = Object.values(_.options.calendarDistrictMap)
                .flat()
                .map(event => {
                    // Optional: Add custom transformations if needed
                    return {
                        ...event, // Preserve existing properties
                        eventName: event.name // Example transformation: Rename 'name' to 'eventName'
                    };
                });
        } else {
            // 過濾符合條件的活動並重新命名屬性
            _.options.calendarEvents = _.options.calendarDistrictMap
                .filter(event => event.orgCodes && event.orgCodes.includes(districtId)) // 篩選符合條件的活動
                .map(event => {
                    const { name, ...rest } = event; // 解構出 name 並保留其他屬性
                    return {
                        ...rest, // 保留原本的屬性
                        eventName: name // 將 'name' 重新命名為 'eventName'
                    };
                });
            // 檢查結果
            console.log(_.options.calendarEvents);
        }
        _.buildCalendar();
        _.buildEventList();
    };

    // v1.0.0 - Select District
    EvoCalendar.prototype.selectClub = function(event) {
        const _ = this;
        // 防止默認行為
        event.preventDefault();
        // 獲取點擊的 district ID
        const clubId = $(event.target).closest('[data-club-val]').data('club-val');
        if (!clubId) {
            console.warn("No club ID found in the selected element.");
            return;
        }
        // 更新 Headbar 中的 active 樣式
        _.$elements.headbarEl
            .find(`.calendar-clubs > [data-district-val]`)
            .removeClass('active');
        _.$elements.headbarEl
            .find(`.calendar-clubs > [data-district-val="${clubId}"]`)
            .addClass('active');
        // 更新事件
        _.options.calendarEvents = _.options.calendarDistrictMap
            .filter(event => event.orgCodes && event.orgCodes.includes(clubId)) // 篩選符合條件的活動
            .map(event => {
                const { name, ...rest } = event; // 解構出 name 並保留其他屬性
                return {
                    ...rest, // 保留原本的屬性
                    eventName: name // 將 'name' 重新命名為 'eventName'
                };
            });
        // 檢查結果
        console.log(_.options.calendarEvents);

        // 可選：觸發自定義事件，供外部監聽使用
        $(_.$elements.calendarEl).trigger("selectClub", [clubId, _.options.calendarEvents]);

        _.buildCalendar();
        _.buildEventList();
    };

    // v1.0.0 - Return active events
    EvoCalendar.prototype.getActiveEvents = function() {
        return this.$active?.events || [];
    };



    /******************************
    *****   METHODS 工具函數    *****
    ******************************/
    // v1.0.0 - Format date
    EvoCalendar.prototype.formatDate = function(date, format, language) {
        var _ = this;
        if (!date) return '';

        language = language || _.defaults.language;
        // 如果傳入的格式是函數，直接調用 format.toDisplay
        if (typeof format === 'string') format = _.parseFormat(format);
        if (format.toDisplay) return format.toDisplay(date, format, language);

        var ndate = new Date(date);
        // 如果日期無效，嘗試修復格式並重建日期
        if (isNaN(ndate)) {
            ndate = new Date(date.replace(/-/g, '/'));
            if (isNaN(ndate)) return ''; // 如果仍然無效，返回空字串
        }
        // 計算扶輪月份
        var indexOfRotaryMonth = _.adjustRotaryIndex(ndate.getMonth());
        // 構建日期格式的值
        var val = {
            d: ndate.getDate(),
            dd: String(ndate.getDate()).padStart(2, '0'),
            D: _.initials.dates[language].daysShort[ndate.getDay()],
            DD: _.initials.dates[language].days[ndate.getDay()],
            m: ndate.getMonth() + 1,
            mm: String(ndate.getMonth() + 1).padStart(2, '0'),
            M: _.initials.dates[language].monthsShort[indexOfRotaryMonth],
            MM: _.initials.dates[language].months[indexOfRotaryMonth],
            yy: String(ndate.getFullYear()).substring(2),
            yyyy: ndate.getFullYear()
        };

        // 組裝最終的日期字串
        var result = [];
        var seps = [...format.separators];
        for (var i = 0; i < format.parts.length; i++) {
            if (seps.length) result.push(seps.shift());
            if (val[format.parts[i]]) result.push(val[format.parts[i]]);
        }
        if (seps.length) result.push(seps.shift()); // 處理尾部分隔符

        return result.join('');
    };

    // v1.0.0 - Parse format (date)
    EvoCalendar.prototype.parseFormat = function(format) {
        var _ = this;

        // 檢查是否為自定義格式物件
        if (typeof format.toValue === 'function' && typeof format.toDisplay === 'function') return format;
        // IE treats \0 as a string end in inputs (truncating the value),
        // so it's a bad format delimiter, anyway
        try {
            // 提取分隔符
            var separators = format.replace(_.initials.validParts, '\0').split('\0');
            // 提取有效的日期部分
            var parts = format.match(_.initials.validParts);
            // 驗證格式結構是否正確
            if (!separators || separators.length === 0 || !parts || parts.length === 0) {
                console.warn( "%c Invalid date format: Check your input format string.",
                    "color:white;font-weight:bold;background-color:#e21d1d;");
                return { separators: [], parts: [] };
            }
            return { separators: separators, parts: parts };
        } catch (error) {
            console.error("%c Error parsing date format: " + error.message,
                "color:white;font-weight:bold;background-color:red;");
            return { separators: [], parts: [] };
        }
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

    // v1.0.0 - Check if date is valid
    EvoCalendar.prototype.isValidDate = function(d){
        return new Date(d) && !isNaN(new Date(d).getTime());
    };

    // (private) v1.0.0 - Get dates between two dates
    EvoCalendar.prototype.getBetweenDates = function(dates) {
        var _ = this, betweenDates = [];
        for (var x = 0; x < _.monthLength; x++) {
            var activeDate = _.formatDate(`${_.$label.months[_.$active.month]} ${x + 1} ${_.$active.year}`, _.options.format);
            if (_.isBetweenDates(activeDate, dates)) {
                betweenDates.push(activeDate);
            }
        }
        return betweenDates;
    };

    // (private) v1.0.0 - Check if event has the same event type in the same date (Check Same Day Event Type)
    EvoCalendar.prototype.hasSameDayEventType = function(date, type) {
        var _ = this, eventCount = 0;

        _.options.calendarEvents.forEach(event => {
            if (Array.isArray(event.date)) {
                var dateRange = _.getBetweenDates(event.date);
                if (dateRange.some(d => d === date && event.type === type)) {
                    eventCount++;
                }
            } else if (event.date === date && event.type === type) {
                eventCount++;
            }
        });
        return eventCount > 0;
    };

    // (private) v1.0.0 - Set calendar theme
    EvoCalendar.prototype.setTheme = function(themeName) {
        var _ = this;
        var prevTheme = _.options.theme;
        _.options.theme = themeName.toLowerCase().split(' ').join('-');

        if (_.options.theme) $(_.$elements.calendarEl).removeClass(prevTheme);
        if (_.options.theme !== 'default') $(_.$elements.calendarEl).addClass(_.options.theme);
    };

    // (private) v1.0.0 - Build event indicator on each date
    EvoCalendar.prototype.buildEventIndicator = function() {
        var _ = this;
        // prevent duplication 防止重複添加指示器
        _.$elements.innerEl.find('.calendar-day > .day > .event-indicator').empty();

        _.options.calendarEvents.forEach(event => {
            _.addEventIndicator(event);
        });
    };

    // (private) v1.0.0 - Return active date (Get Active Date)
    EvoCalendar.prototype.getActiveDate = function() {
        var _ = this;
        return _.$active?.date || null;
    };

    // (private) v1.0.0 - Hide Sidebar/Event List if clicked outside (Toggle Sidebar/Event List)
    EvoCalendar.prototype.toggleOutside = function(event) {
        var _ = this;
        var isInnerClicked = (event.target === _.$elements.innerEl[0]);

        if (_.$UI.hasSidebar && isInnerClicked) _.toggleSidebar(false);
        if (_.$UI.hasEvent && isInnerClicked) _.toggleEventList(false);
    }

    // (private) v1.0.0 - Destroy event listeners
    EvoCalendar.prototype.destroyEventListener = function() {
        var _ = this;

        $(window).off('resize.evocalendar.evo-' + _.instanceUid);
        $(window).off('click.evocalendar.evo-' + _.instanceUid);

        // IF sidebarToggler: remove event listener: toggleSidebar
        if(_.options.sidebarToggler) _.$elements.sidebarToggler.off('click.evocalendar');
        // IF eventListToggler: remove event listener: toggleEventList
        if(_.options.eventListToggler) _.$elements.eventListToggler.off('click.evocalendar');
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

    // (private) v1.0.0 - Destroy plugin
    EvoCalendar.prototype.destroy = function() {
        var _ = this;
        // code here
        _.destroyEventListener();
        if (_.$elements.calendarEl) {
            _.$elements.calendarEl.classList.remove('calendar-initialized');
            _.$elements.calendarEl.classList.remove('evo-calendar');
            _.$elements.calendarEl.classList.remove('sidebar-hide');
            _.$elements.calendarEl.classList.remove('event-hide');
        }
        _.$elements.calendarEl.empty();
        _.$elements.calendarEl.attr('class', _.initials.default_class);
        $(_.$elements.calendarEl).trigger("destroy", [_])
    };

    // v1.0.0 - Limit title (...)
    EvoCalendar.prototype.limitTitle = function(title, limit) {
        var newTitle = [];
        limit = (limit === undefined) ? 18 : limit;
        if ((title).split(' ').join('').length > limit) {
            var words = title.split(' ');
            for (let word of words) {
                if (word.length + newTitle.join('').length <= limit) {
                    newTitle.push(word);
                }
            }
            return `${newTitle.join(' ')}...`;
        }
        return title;
    }

    // v1.1.2 - Check and filter strings
    EvoCalendar.prototype.escapeSpecialCharacters = function(d) {
        return d.replace(/[^\w]/g, '\\$&');
    };

    // Adjust index for Rotary Year offset
    EvoCalendar.prototype.adjustRotaryIndex = function(index) {
        return (index - 6 < 0) ? index + 6 : index - 6;
    };

    // 安全處理字符串，防止 XSS 攻擊
    EvoCalendar.prototype.sanitize = function(input) {
        const tempDiv = document.createElement('div');
        tempDiv.textContent = input;
        return tempDiv.innerHTML;
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
