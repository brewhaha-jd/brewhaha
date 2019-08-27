module.exports = {
    throwMongoError: function (err) {
        let responseCode = 404;
        let response = "";
        if (err.code === 11000) {
            responseCode = 409;
            response = err.errmsg;
        }
        return [responseCode, response];
    }
};