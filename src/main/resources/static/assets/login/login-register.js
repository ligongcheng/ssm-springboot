/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: #
 * 
 */
function showRegisterForm() {
    $('.loginBox').fadeOut('fast', function () {
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast', function () {
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('注册');
    });
    $('.error').removeClass('alert alert-danger').html('');

}

function showLoginForm() {
    $('#loginModal').find('.registerBox').fadeOut('fast', function () {
        $(".loginBox").fadeIn('fast');
        $('.register-footer').fadeOut('fast', function () {
            $('.login-footer').fadeIn('fast');
        });

        $('.modal-title').html('登录');
    });
    $('.error').removeClass('alert alert-danger').html('');
}

function openLoginModal() {
    showLoginForm();
    setTimeout(function () {
        $('#loginModal').modal(
            {backdrop: false},
            'show'
        );
    }, 230);

}

function openRegisterModal() {
    showRegisterForm();
    setTimeout(function () {
        $('#loginModal').modal('show');
    }, 230);

}

function loginAjax() {
    $.ajax({
        url: "/login/userlogin",
        type: "post",
        data: $("#loginForm").serialize(),
        success: function (item) {
            if (item.code == 200) {
                window.location = "/";
            } else {
                alert(item.msg);
            }
        }
    });

}

function registerAjax() {

    $.ajax({
        url: "/login/userregister",
        type: "post",
        data: $("#registerForm").serialize(),
        success: function (item) {
            alert(item.code);
        }
    });
}


$().ready(function () {

    openLoginModal();

    /*
        Fullscreen background
    */
    $.backstretch([
        "assert/img/login/2.jpg"
        , "assert/img/login/3.jpg"
        , "assert/img/login/1.jpg"
    ], {duration: 3000, fade: 750});

    /*
        Form validation
    */

});

   