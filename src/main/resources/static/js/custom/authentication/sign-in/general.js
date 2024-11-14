"use strict";

// Class definition
var KTSigninGeneral = function() {
    // Elements
    var form;
    var submitButton;
    var validator;

    // Handle form
    var handleForm = function(e) {
        // Init form validation rules. For more info check the FormValidation plugin's official documentation:https://formvalidation.io/
        validator = FormValidation.formValidation(
			form,
			{
				fields: {					
					'account': {
                        validators: {
							notEmpty: {
								message: 'Email address is required'
							}
						}
					},
                    'password': {
                        validators: {
                            notEmpty: {
                                message: 'The password is required'
                            }
                        }
                    } 
				},
				plugins: {
					trigger: new FormValidation.plugins.Trigger(),
					bootstrap: new FormValidation.plugins.Bootstrap5({
                        rowSelector: '.fv-row'
                    })
				}
			}
		);		

        // Handle form submit
        submitButton.addEventListener('click', function (e) {
            // 停止默認表單提交行為，以便我們可以控制記住帳號的處理
            event.preventDefault();

            const submitButton = this;
            const form = $(this).closest('form')[0];
            const accountInput = form.querySelector('[name="account"]');
            const passwordInput = form.querySelector('[name="password"]');
            const rememberMeCheckbox = form.querySelector('#rememberMe');
            // 記住帳號
            if (rememberMeCheckbox.checked) {
                // 記住帳號
                localStorage.setItem("rotaract.xplorekaleido.com.rememberedAccount", accountInput.value);
            } else {
                // 移除記住的帳號
                localStorage.removeItem("rotaract.xplorekaleido.com.rememberedAccount");
            }

            // Validate form
            validator.validate().then(function (status) {
                if (status == 'Valid') {
                    // Show loading indication
                    submitButton.setAttribute('data-kt-indicator', 'on');
                    // Disable button to avoid multiple click 
                    submitButton.disabled = true;

//                    // 虛擬 API 請求，將其替換為實際 API 呼叫
//                    $.ajax({
//                        url: '/xkRotaract/api/auth/checkLogin', // 更換為實際的 API URL
//                        method: 'POST',
//                        data: {
//                            account: accountInput.value,
//                            password: md5(passwordInput.value) // 使用加密的密碼
//                        },
//                        success: function(response) {
//                            // 顯示成功訊息
//                            Swal.fire({
//                                text: "你已成功登入，歡迎來到萬花筒!",
//                                icon: "success",
//                                buttonsStyling: false,
//                                confirmButtonText: "馬上開始!",
//                                customClass: {
//                                    confirmButton: "btn btn-primary"
//                                }
//                            }).then(function (result) {
//                                if (result.isConfirmed) {
//                                    passwordInput.value = md5(passwordInput.value); // 再次加密密碼以進行後續處理
//                                    form.submit(); // 最後提交表單
//                                }
//                            });
//                        },
//                        error: function(error) {
//                            // 顯示錯誤訊息
//                            Swal.fire({
//                                text: "登入失敗，請檢查您的帳號和密碼。",
//                                icon: "error",
//                                buttonsStyling: false,
//                                confirmButtonText: "重新嘗試",
//                                customClass: {
//                                    confirmButton: "btn btn-primary"
//                                }
//                            });
//                            // Show loading indication
//                            submitButton.setAttribute('data-kt-indicator', 'off');
//                            // Disable button to avoid multiple click
//                            submitButton.disabled = false;
//                        }
//                    });

                    // Simulate ajax request
                    setTimeout(function() {
                        // Hide loading indication
                        submitButton.removeAttribute('data-kt-indicator');

                        // Enable button
                        submitButton.disabled = false;

                        // Show message popup. For more info check the plugin's official documentation: https://sweetalert2.github.io/
                        Swal.fire({
                            text: "歡迎來到萬花筒!系統正在為您確認身份信息...",
                            icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "馬上開始!",
                            customClass: {
                                confirmButton: "btn btn-primary"
                            }
                        }).then(function (result) {
                            if (result.isConfirmed) {
//                                form.querySelector('[name="account"]').value= "";
                                form.querySelector('[name="password"]').value= md5(form.querySelector('[name="password"]').value);

                                form.submit(); // submit form
//                                var redirectUrl = form.getAttribute('data-kt-redirect-url');
//                                if (redirectUrl) {
//                                    location.href = redirectUrl;
//                                }
                            }
                        });
                    }, 2000);
                } else {
                    // Show error popup. For more info check the plugin's official documentation: https://sweetalert2.github.io/
                    Swal.fire({
                        text: "Sorry, looks like there are some errors detected, please try again.",
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

    // Public functions
    return {
        // Initialization
        init: function() {
            form = document.querySelector('#kt_sign_in_form');
            submitButton = document.querySelector('#kt_sign_in_submit');
            
            handleForm();
        }
    };
}();

// On document ready
KTUtil.onDOMContentLoaded(function() {
    KTSigninGeneral.init();
});
