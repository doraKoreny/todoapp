const view = new View();

new Controller(view);


window.addEventListener('load', function () {
    let idToken;
    let accessToken;
    let expiresAt;

    let webAuth = new auth0.WebAuth({
        domain: 'flatmanager.eu.auth0.com',
        clientID: 'nvXbqQH3hb2kJLAXrf5sMtgoncP7FJFL',
        responseType: 'token id_token',
        audience: "https://todoapp/api",
        scope: 'openid read:admin',
        redirectUri: window.location.href
    });

    let loginBtn = document.getElementById('btn-login');


    loginBtn.addEventListener('click', function (e) {
        e.preventDefault();
        webAuth.authorize();
    });

    let loginStatus = document.querySelector('.container h4');

    let loginView = document.getElementById('login-view');
    let homeView = document.getElementById('home-view');

    let homeViewBtn = document.getElementById('btn-home-view');
    let logoutBtn = document.getElementById('btn-logout');
    homeViewBtn.addEventListener('click', function () {
        homeView.style.display = 'inline-block';
        loginView.style.display = 'none';
    });

    logoutBtn.addEventListener('click', logout);

    function handleAuthentication() {

        webAuth.parseHash(function (err, authResult) {
            if (authResult && authResult.accessToken && authResult.idToken) {
                window.location.hash = '';
                localLogin(authResult);
                loginBtn.style.display = 'none';
                homeView.style.display = 'inline-block';
            } else if (err) {
                homeView.style.display = 'inline-block';
                console.log(err);
                alert(
                    'Error: ' + err.error + '. Check the console for further details.'
                );
            }
            displayButtons();
        });
    }
    function localLogin(authResult) {

        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem("accessToken",authResult.accessToken);
        console.log(authResult.accessToken);
        expiresAt = JSON.stringify(
            authResult.expiresIn * 1000 + new Date().getTime()
        );
        accessToken = authResult.accessToken;
        idToken = authResult.idToken;
    }
    function renewTokens() {

        webAuth.checkSession({}, (err, authResult) => {
            if (authResult && authResult.accessToken && authResult.idToken) {
                localLogin(authResult);
            } else if (err) {
                alert(
                    'Could not get a new token ' + err.error + ':' + err.error_description + '.'
                );
                logout();
            }
            displayButtons();
        });
    }
    function logout() {

        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem("accessToken");
        accessToken = '';
        idToken = '';
        expiresAt = 0;
        displayButtons();
    }
    function isAuthenticated() {

        let expiration = parseInt(expiresAt) || 0;
        return localStorage.getItem('isLoggedIn') === 'true' && new Date().getTime() < expiration;
    }
    function displayButtons() {

        if (isAuthenticated()) {
            loginBtn.style.display = 'none';
            logoutBtn.style.display = 'inline-block';
            loginStatus.innerHTML = 'You are logged in!';
        } else {
            loginBtn.style.display = 'inline-block';
            logoutBtn.style.display = 'none';
            loginStatus.innerHTML = 'You are not logged in! Please log in to continue!';
        }
    }

    if (localStorage.getItem('isLoggedIn') === 'true') {
        renewTokens();
    } else {
        handleAuthentication();
    }
});
