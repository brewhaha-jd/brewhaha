import requests, json

API_Key = "swU3r2HulZb-VKASuBPxu9LQiQAvaz94UVLW0PpnXtl_ujv_b84Lt4zLvUDV-iihBY2_5LRcznYQ9f35vldvSlCyCxtiOSZRp1tNyxKFv5iJHAh7Jmhr7ENakVBKXHYx"



url = "https://api.yelp.com/v3/businesses/search"
headers = {
    'Authorization': 'Bearer %s' % API_Key
}

def yelpQuery(url_params, filename, writemode):
    x = 1
    while x < 950:
        if x == 1:
            url_params['offset'] = 0
        else:
            url_params['offset'] = x
        response = requests.get(url, headers=headers, params=url_params)
        with open(filename, writemode) as f:
            # prettyJson = json.dumps(response.json(), indent=4, sort_keys=True)
            # json.dump(prettyJson, f)
            json.dump(response.json(), f)
        x += 50

def atlantaQuery():
    atlanta_params = {'term': 'brewery', 'location': 'Atlanta, GA', 'limit': 50}
    response = requests.get(url, headers=headers, params=atlanta_params)
    with open("atlanta_breweries.json", "w") as f:
        json.dump(response.json(), f)

atlantaQuery()