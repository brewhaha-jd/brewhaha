/**
 * Created by nickhutchinson on 8/26/19.
 */
module.exports = {
	'mongoConfig'   : 'mongodb://localhost:27017/brewhaha',
	'tokenSecret'   : 'secretGoesHere!',
	'tokenExpiresIn': 1140,
    'refreshTokenExpiresIn': 2592000,
	'geoCoderConfig': {
		provider: 'opencage',
		apiKey: 'API KEY HERE'
	}
};