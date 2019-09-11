module.exports = {

    throwError: function(errorIn, callback) {
        let err = {
            statusCode: 500,
            response: "Opps! Something went wrong. We logged it for investigation"
        };

        if (errorIn.code === 11000) { //mongo error
            err.statusCode = 409;
            err.response = errorIn.errmsg;
        } else if (errorIn.message === "Invalid password") {
            err.statusCode = 400;
            err.response = errorIn.message;
        } else {
            console.log(errorIn)
        }
        callback(err);
    },

    throwNoTokenProvidedError: function (callback) {
        let err = {
            statusCode: 403,
            response: "No token provided"
        };
        callback(err);
    },

    throwMongoNotFoundError: function () {
        let err = new Error("The requested resource does not exist");
        err.statusCode = 404;
        return err;
    },

    throwInvalidAuthentication: function () {
        let err = new Error("Invalid credentials");
        err.statusCode = 401;
        return err;
    }
};