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
