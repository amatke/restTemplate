$(function () {

    /*pozivanje editora */
    if ( $("#content").length) {
        ClassicEditor
            .create(document.querySelector("#content"))
            .catch(error => {
                console.log(error);
            });
    }

    /*pozivanje editora */
    if ( $("#description").length) {
        ClassicEditor
            .create(document.querySelector("#description"))
            .catch(error => {
                console.log(error);
            });
    }

});

