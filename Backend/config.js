/**
 * Created by nickhutchinson on 8/26/19.
 */
module.exports = {
	'mongoConfig'   : 'mongodb://localhost:27017/brewhaha',
	'tokenSecret'   : 'SECRET HERE',
	'tokenExpiresIn': 1140,
    	'refreshTokenExpiresIn': 2592000,
	'geoCoderConfig': {
		provider: 'opencage',
		apiKey: '9589894c80264e6e9ba94eec10ff56bf'
	}
};
