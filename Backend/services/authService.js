module.exports = {
    // getAll: function (callback) {
    //     userRepo.getAll(function (entities) {
    //         callback(entities)
    //     })
    // },
    //
    // getById: function (id, callback) {
    //     userRepo.getById(id, function (entity) {
    //         callback(entity)
    //     })
    // },
    create: function (authResource, callback) {
        authResource.password = hasher.generate(authResource.password);
        authRepo.create(authResource, function (authEntity) {
            callback(authEntity)
        })
    }
};

const authRepo = require('../models/auth/authRepository');
const hasher = require('password-hash');