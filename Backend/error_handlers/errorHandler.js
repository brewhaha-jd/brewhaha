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

    throwMongoNotFound: function (callback) {
        let err = {
            statusCode: 404,
            response: "The requested resource does not exist"
        };
        callback(err);
    },

    throwInvalidAuthentication: function (callback) {
        let err = {
            statusCode: 401,
            response: "Invalid credentials"
        };
        callback(err);
    },

    throwNoTokenProvidedError: function (callback) {
        let err = {
            statusCode: 403,
            response: "No token provided"
        };
        callback(err);
    },
};