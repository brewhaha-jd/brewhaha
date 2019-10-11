# Breweries
These routes require authentication! Use header `x-access-token` and pass in `token` given from `/api/auth/login` or `/api/auth/token`
### Get All Breweries
`GET localhost:3000/api/brewery`

Returns: 200

    [
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3620936,
                        33.8503143
                    ],
                    "type": "Point"
                },
                "number": 3179,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042314201"
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d79bb022c8a1a5bccbdf22f",
            "name": "Moondogs",
            "website": "https://moondogs.club",
            "__v": 0
        },
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3794737,
                        33.8420218
                    ],
                    "type": "Point"
                },
                "number": 3182,
                "line1": "Roswell Road",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042543341"
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d79bed42c8a1a5bccbdf230",
            "name": "Lost Dog Tavern",
            "website": "lostdogtavern.com",
            "__v": 0
        }
    ]

 ### Get Brewery
 `GET localhost:3000/api/brewery/<_id>`
 
 Returns: 200

    {
        "address": {
            "location": {
                "coordinates": [
                    -84.3620936,
                    33.8503143
                ],
                "type": "Point"
            },
            "number": 3179,
            "line1": "Peachtree Road Northeast",
            "line2": null,
            "line3": null,
            "city": "Atlanta",
            "stateOrProvince": "Georgia",
            "county": "Fulton County",
            "country": "USA",
            "postalCode": 30305,
            "telephone": "4042314201"
        },
        "friendlinessRating": {
            "aggregate": null,
            "kidsFood": null,
            "kidsEntertainment": null,
            "bathrooms": null,
            "minRecommendedAge": null
        },
        "_id": "5d79bb022c8a1a5bccbdf22f",
        "name": "Moondogs",
        "website": "https://moondogs.club",
        "__v": 0
    }
    
 ### Create Brewery
 `PUT localhost:3000/api/brewery`  
 
 Body:  
 
     {
        "name": "Moondogs",
        "address": {
            "number": 3179,
            "line1": "Peachtree Rd NE",
            "line2": null,
            "line3": null,
            "city": "Atlanta",
            "stateOrProvince": "GA",
            "telephone": "4042314201"
        },
        "website": "https://moondogs.club"
     }
 
 Returns: 201

    {
        "userLink": "/api/brewery/5d799d37b1564359df38add3"
    }


 ### Query Brewery By Location With Range
 `PUT localhost:3000/api/brewery?location=<long>,<lat>&range=<miles>`  

 
 Returns: 200  
 Results are sorted with closest at the top

    [
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3794737,
                        33.8420218
                    ],
                    "type": "Point"
                },
                "number": 3182,
                "line1": "Roswell Road",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042543341"
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d79bed42c8a1a5bccbdf230",
            "name": "Lost Dog Tavern",
            "website": "lostdogtavern.com",
            "__v": 0
        },
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3620936,
                        33.8503143
                    ],
                    "type": "Point"
                },
                "number": 3179,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042314201"
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d79bb022c8a1a5bccbdf22f",
            "name": "Moondogs",
            "website": "https://moondogs.club",
            "__v": 0
        },
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3620936,
                        33.8503143
                    ],
                    "type": "Point"
                },
                "number": 3177,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042339801"
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d79bf0f2c8a1a5bccbdf231",
            "name": "Hole In the Wall",
            "website": "holeinthewallatlanta.com",
            "__v": 0
        }
    ]
    
  ### Query Brewery By Location With Range and Ratings
  `PUT localhost:3000/api/brewery?location=<long>,<lat>&range=<miles>&ratings=true&<ratingType>=<rating>[can have multiple]`  
    
 ### Query Brewery By Ratings
 `GET localhost:3000/api/brewery?ratings=true&<ratingType>=<rating>[can have multiple]`
 
 EXAMPLE : `GET localhost:3000/api/brewery?ratings=true&aggregate=4.2&minRecommendedAge=14`
   
RatingType can be :
- aggregate: actual > query
- kidsFood: actual > query
- kidsEntertainment: actual > query
- bathrooms: actual > query
- minRecommendedAge: actual < query
 
 Returns: 200

    [
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3620936,
                        33.8503143
                    ],
                    "type": "Point"
                },
                "number": 3179,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042314201"
            },
            "friendlinessRating": {
                "aggregate": 4.1,
                "kidsFood": 2,
                "kidsEntertainment": 2.5,
                "bathrooms": 5,
                "minRecommendedAge": 15
            },
            "_id": "5d79d4a7859843c95ec78ae5",
            "name": "Moondogs",
            "website": "https://moondogs.club",
            "__v": 0
        }
    ]

 ### Query Brewery By Name
 `GET localhost:3000/api/brewery?name=Moon`
 
 Returns: 200

    [
        {
            "address": {
                "location": {
                    "coordinates": [
                        -84.3620936,
                        33.8503143
                    ],
                    "type": "Point"
                },
                "number": 3179,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042314201"
            },
            "friendlinessRating": {
                "aggregate": 4.1,
                "kidsFood": 2,
                "kidsEntertainment": 2.5,
                "bathrooms": 5,
                "minRecommendedAge": 15
            },
            "_id": "5d79d4a7859843c95ec78ae5",
            "name": "Moondogs",
            "website": "https://moondogs.club",
            "__v": 0
        }
    ]
    
 ### Update Brewery
 `POST localhost:3000/api/brewery/<id>`
 
 Body:
 
     {
         "address": {
             "location": {
                 "coordinates": [
                     -84.3620936,
                     33.8503143
                 ],
                 "type": "Point"
             },
             "number": 3179,
             "line1": "Peachtree Road Northeast",
             "line2": null,
             "line3": null,
             "city": "Atlanta",
             "stateOrProvince": "Georgia",
             "county": "Fulton County",
             "country": "USA",
             "postalCode": 30305,
             "telephone": "4042314201"
         },
         "name": "Moondogs",
         "website": "https://moondogs.club"
     }
 
 Returns: 200

    {
        "address": {
            "location": {
                "coordinates": [
                    -84.3620936,
                    33.8503143
                ],
                "type": "Point"
            },
            "number": 3179,
            "line1": "Peachtree Road Northeast",
            "line2": null,
            "line3": null,
            "city": "Atlanta",
            "stateOrProvince": "Georgia",
            "county": "Fulton County",
            "country": "USA",
            "postalCode": 30305,
            "telephone": "4042314201"
        },
        "friendlinessRating": {
            "aggregate": null,
            "kidsFood": null,
            "kidsEntertainment": null,
            "bathrooms": null,
            "minRecommendedAge": null
        },
        "_id": "5d9fc957c4591d6c7c3f0227",
        "name": "Moondogs",
        "website": "https://moondogs.club",
        "__v": 1
    }


[Link to sample API requests in Postman]: https://www.getpostman.com/collections/1529c1cf1f37a266c471