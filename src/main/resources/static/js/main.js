/**
 * Pobiera token dostępowy, który jest zapsiany w ciasteczkach przeglądarki przez oAuth2.
 */

function getCookie(name) {
    let value = "; " + document.cookie;
    let parts = value.split("; " + name + "=");
    if (parts.length == 2) return parts.pop().split(";").shift();
}

/**
 * Usuwanie ciasteczka z przeglądarki z tokenem dostępowym - używane podczas wylogowania.
 * Po usunięciu ciasteczka użytkownik przestaje być zalogowany.
 */
function deleteCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}
/**
 * Inicjalizacja nowego komponentu Vue, który będzie zawierać informację o tym czy obecnie zalogowany jest użytkownik.
 */
window.Event = new Vue({
    data: {
        isLoggedIn: false,
    }
});

/**
 * Komponent z nagłówka strony zawierający przyciski logowania i rejestracji.
 */
Vue.component('login-component',{
    template: '<p id="loginButtons" v-if="!isLoggedIn()" style="float:right"><ul><li><a href="/login" class="button">Logowanie</a></li><li><a href="/register" class="button">Rejestracja</a></li></ul></p>'+
'<p v-else id="loggedIn" v-show="isLoggedIn()" style="float:right"><ul><li><p style="margin-top: 2px;" class="welcome_msg">{{loggedInUser}}</p></li><li v-if="matchedRedactor()"><button class="button" v-on:click="goToNewPost()">Dodaj nowy post</button></li><li><button class="button" v-on:click="goToUserProfile()">Pokaż profil</button></li><li><button class="button" v-on:click="logOut">Wyloguj się</button></li></ul></p>',
    data:
        function() {
            return {
                loggedInUser : "",
                roles: []
            }
    },
    mounted() {
        if(getCookie("access_token")) {
            axios.get("/users/getCurrent/roles?access_token=" + getCookie("access_token"))
                .then(function (response) {
                this.roles = response.data;
            }.bind(this));
        }
        if(getCookie("access_token")){
            axios.get("/users/getCurrent/name?access_token=" + getCookie("access_token"))
                .then(function(response) {
                    this.loggedInUser = "Zalogowano: " + response.data;
                    window.Event.isLoggedIn = true;
                }.bind(this))
                .catch(function(error) {
                    deleteCookie("access_token");
                    return error;
                });
        }
    },
    methods : {
        /**
         * Wysyła żądanie „GET” do endpointa /users/logout, aby wylogować się z bloga.
         */
        logOut() {
            axios.get("/users/logout?access_token=" + getCookie("access_token"))
                .then(function (response) {
                    window.Event.isLoggedIn = false;
                    deleteCookie("access_token")
                }.bind(this))
        },
        /**
         * Sprawdza, czy obecnie zalogowany użytkownik ma rolę redaktora.
         */
        matchedRedactor() {
            for (let i = 0; i < this.roles.length; i++) {
                if (this.roles[i].authority == "REDACTOR") {
                    return true;
                }
            }
            return false;
        },
        /**
         * Przejdź do strony profilu użytkownika.
         */
        goToUserProfile() {
            axios({
                method: 'get',
                url: '/user-profile',
                headers: {"Authorization": "Bearer " + getCookie("access_token")},
            }).then((response) => {
                document.location.replace("/user-profile");
            });
        },
        /**
         * Przejdź strony tworzenia nowego posta.
         */
        goToNewPost() {
            if (this.matchedRedactor()) {
                axios({
                    method: 'get',
                    url: '/new-post',
                    headers: {"Authorization": "Bearer " + getCookie("access_token")},
                }).then((response) => {
                        document.location.replace("/new-post");
                    });
            } else {
                window.alert("Wymagane uprawnienia redaktora.");
            }
        },
        /**
         * Sprawdź, czy użytkownik jest aktualnie zalogowany na blogu.
         */
        isLoggedIn() {
            return window.Event.isLoggedIn;
        }
    }
});