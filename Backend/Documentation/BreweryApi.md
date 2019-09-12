# Breweries
These routes require authentication! Use header `x-access-token` and pass in `token` given from `/api/auth/login` or `/api/auth/token`
### Get All Breweries
`GET localhost:3000/api/brewery`

Returns: 200

    [
        {
            "address": {
                "number": 3179,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042314201",
                "latitude": 33.8503143,
                "longitude": -84.3620936
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d799961b1564359df38add1",
            "name": "Moondogs",
            "website": "https://moondogs.club",
            "__v": 0
        },
        {
            "address": {
                "number": 3177,
                "line1": "Peachtree Road Northeast",
                "line2": null,
                "line3": null,
                "city": "Atlanta",
                "stateOrProvince": "Georgia",
                "county": "Fulton County",
                "country": "USA",
                "postalCode": 30305,
                "telephone": "4042339801",
                "latitude": 33.8503143,
                "longitude": -84.3620936
            },
            "friendlinessRating": {
                "aggregate": null,
                "kidsFood": null,
                "kidsEntertainment": null,
                "bathrooms": null,
                "minRecommendedAge": null
            },
            "_id": "5d799cfbb1564359df38add2",
            "name": "The Hole In the Wall",
            "website": "holeinthewallatlanta.com",
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



[Link to sample API requests in Postman]: https://www.getpostman.com/collections/1529c1cf1f37a266c471