module.exports = {
    throwMongoError: function (err) {
        let responseCode = 500;
        let response = "Opps! Something went wrong. We logged it for investigation";
        if (err.code === 11000) {
            responseCode = 409;
            response = err.errmsg;
        } else {
            console.log(err)
        }
        return [responseCode, response];
    },

    throwMongoNotFound: function () {
        let responseCode = 404;
        let response = "The requested resource does not exist";
        return [responseCode, response];
    }
};