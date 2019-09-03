module.exports = {
    checkErrors: function (req, res) {
        if(res.locals.err){
            res.locals.statusCode = res.locals.err.statusCode;
            res.locals.response = res.locals.err.response;
        }
        res.status(res.locals.statusCode).send(res.locals.response)
    }
};