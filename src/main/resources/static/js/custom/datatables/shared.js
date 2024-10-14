// shared.js

// Function to handle closing the modal
function handleCloseButton(element, modal) {
    const closeButton = element.querySelector('[data-kt-modal-action="close"]');
    if (closeButton) {
        closeButton.addEventListener('click', e => {
            e.preventDefault();
            modal.hide();
        });
    }
}

// Function to handle cancel button with SweetAlert2 confirmation
function handleCancelButton(element, form, modal) {
    const cancelButton = element.querySelector('[data-kt-modal-action="cancel"]');
    if (cancelButton) {
        cancelButton.addEventListener('click', e => {
            e.preventDefault();
            Swal.fire({
                text: "你確定要取消嗎？",
                icon: "warning",
                showCancelButton: true,
                buttonsStyling: false,
                confirmButtonText: "是的，取消！",
                cancelButtonText: "不，返回",
                customClass: {
                    confirmButton: "btn btn-primary",
                    cancelButton: "btn btn-active-light"
                }
            }).then(function (result) {
                if (result.value) {
                    form.reset(); // Reset form
                    modal.hide(); // Hide modal
                } else if (result.dismiss === 'cancel') {
                    Swal.fire({
                        text: "你的表單未被取消!",
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
}

// Function to handle form submission with validation
function handleFormSubmit(element, form, validator, modal) {
    const submitButton = element.querySelector('[data-kt-modal-action="submit"]');
    if (submitButton && validator) {
        submitButton.addEventListener('click', function (e) {
            e.preventDefault();

            validator.validate().then(function (status) {
                if (status === 'Valid') {
                    submitButton.setAttribute('data-kt-indicator', 'on');
                    submitButton.disabled = true;
                    setTimeout(function () {
                        submitButton.removeAttribute('data-kt-indicator');
                        submitButton.disabled = false;
                        Swal.fire({
                            text: "表單已成功提交！",
                            icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Ok, got it!",
                            customClass: {
                                confirmButton: "btn btn-primary"
                            }
                        }).then(function (result) {
                            if (result.isConfirmed) {
                                form.submit();
                                modal.hide();
                            }
                        });
                    }, 2000);
                } else {
                    Swal.fire({
                        text: "發現一些錯誤，請重試。",
                        icon: "error",
                        buttonsStyling: false,
                        confirmButtonText: "Ok, got it!",
                        customClass: {
                            confirmButton: "btn btn-primary"
                        }
                    });
                }
            });
        });
    }
}

/**
 * ajax
 */
// 假设 dropdown_DISTRICT 变量的值是用于获取数据的 code
var categoryCode = 'dropdown_DISTRICT'; // 需要替换为实际的 code 值

// 加载地区选项的函数
function loadDistricts(categoryCode) {
    $.ajax({
        url: '/xkRotaract/api/manage/dictionary/listDictionaryData',
        method: 'POST',
        data: JSON.stringify({ 'code': categoryCode }), // 修改为适合你的代码
        processData: false,
        contentType: 'application/json',
        success: function(response) {
            console.log('AJAX 请求成功：', response);

            // 获取下拉选单元素
            var rotaractSelect = $('#inputGroupSelect_district');
            // 清空现有选项
            rotaractSelect.empty();
            // 添加默认选项
            rotaractSelect.append('<option value="0">請選擇</option>');

            // 遍历响应数据并添加到下拉选单中
            if(response) {
                response.forEach(function(district) {
                    rotaractSelect.append(
                        `<option value="${district.code}">${district.description}</option>`
                    );
                });
            }

            // 设置默认选中项
            var selectedDistrictId = $('input[name="district_id"]').val();
            if (selectedDistrictId) {
                $('#inputGroupSelect_district').val(selectedDistrictId).trigger('change'); // 触发变化以加载扶青社地區
            }
        },
        error: function(xhr, status, error) {
            console.error('AJAX 请求失败：', error);
        }
    });
}

// 加载扶青社选项的函数
function loadRotaracts(districtId) {
    $.ajax({
        url: '/xkRotaract/api/manage/club/list',
        method: 'POST',
        data: JSON.stringify({ 'district': districtId }),
        processData: false,
        contentType: 'application/json',
        success: function(response) {
            console.log('AJAX 请求成功：', response);

            // 获取下拉选单元素
            var clubSelectElement = $('#inputGroupSelect_club');
            // 清空现有选项
            clubSelectElement.empty();
            // 添加默认选项
            clubSelectElement.append('<option value="0">請選擇</option>');

            // 遍历响应数据并添加到下拉选单中
            if(response) {
                response.forEach(function(club) {
                    clubSelectElement.append(
                        `<option value="${club.id}">${club.name}</option>`
                    );
                });
            }

            // 设置默认选中项
            var selectedRotaractId = $('input[name="rotaract_id"]').val();
            if (selectedRotaractId) {
                $('#inputGroupSelect_club').val(selectedRotaractId);
            }
        },
        error: function(xhr, status, error) {
            console.error('AJAX 请求失败：', error);
        }
    });
}


// 监听地区下拉选单变化事件
$('#inputGroupSelect_district').change(function() {
    var selectedDistrictId = $(this).val();
    $('input[name="district_id"]').val(selectedDistrictId);

    if (selectedDistrictId !== '0') {
        loadRotaracts(selectedDistrictId);
    } else {
        $('#inputGroupSelect_club').empty().append('<option value="0">請選擇</option>');
    }
});
// 监听地区下拉选单变化事件
$('#inputGroupSelect_club').change(function() {
    var selectedRotaractId = $(this).val();
    $('input[name="rotaract_id"]').val(selectedRotaractId);
});


/**
 * unit
 */

// 加载地区选项的函数
function dropdown(categoryCode, selectElementId, inputHiddenName) {
    $.ajax({
        url: '/xkRotaract/api/manage/dictionary/listDictionaryData',
        method: 'POST',
        data: JSON.stringify({ 'code': categoryCode }), // 修改为适合你的代码
        processData: false,
        contentType: 'application/json',
        success: function(response) {
            console.log('AJAX 请求成功：', response);

            // 获取下拉选单元素
            var elementSelect = $('#' + selectElementId);
            // 清空现有选项
            elementSelect.empty();
            // 添加默认选项
            elementSelect.append('<option value="0">請選擇</option>');

            // 遍历响应数据并添加到下拉选单中
            if(response) {
                response.forEach(function(option) {
                    elementSelect.append(
                        `<option value="${option.code}">${option.description}</option>`
                    );
                });
            }

            // 设置默认选中项
            var selected = $('input[name="' + inputHiddenName + '"]').val();
            if (selected) {
                $('#' + selectElementId).val(selected).trigger('change'); // 触发变化以加载扶青社地區
            }
        },
        error: function(xhr, status, error) {
            console.error('AJAX 请求失败：', error);
        }
    });
}