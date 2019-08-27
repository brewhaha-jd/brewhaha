module.exports = {

    // getAll: function (callback) {
    //     User.find().exec(function (err, entities) {
    //         if (err) {
    //             console.log(err)
    //         }
    //         callback([200, entities])
    //     });
    // },
    //
    // getById: function (id, callback) {
    //     User.findById(id,function (err, entity) {
    //         if (err) {
    //             console.log(err)
    //         }
    //         callback([200, entity])
    //     })
    // },

	create: function (authEntity, callback) {
		authEntity.save(function (err) {
			    callback(err)
		});
	}
};




// const Auth = require('./authEntity');
