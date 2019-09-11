module.exports = {
    sendApiError: function (err, req, res) {
        err = checkKnownErrors(err);
        if (!err.statusCode) {
            err.statusCode = 500;
            console.error(err);
        }
        if (!err.message) err.message = "Opps! Something went wrong. The error has been logged for investigation";
        res.status(err.statusCode).json({errorMessage: err.message})
    },

    pageNotFoundCatchAll: function (req, res, next) {
        let err = new Error('Page Not Found');
        err.statusCode = 404;
        next(err)
    }
};

function checkKnownErrors(err) {
    if (err.code) {
        switch (err.code) {
            case "ER_DUP_ENTRY":
                err.statusCode = 409;
            case 11000:
                err.statusCode = 409;
                err.message = err.errmsg;
        }
    }
    return err;
}