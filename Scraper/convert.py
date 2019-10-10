import json


if __name__== "__main__":
    with open('atlanta_breweries.json') as json_file:
        data = json.load(json_file)
    #         print(data.businesses)
        formattedData = []
        for brew in data['businesses']:
            print(brew['name'])
            print(brew['location']['address1'])
            brewery = {

                "name": brew['name'],
                "address": {
                    "number": brew['location']['address1'].split(" ")[0],
                    "line1": " ".join(brew['location']['address1'].split(" ")[1:]),
                    "line2": brew['location']['address2'],
                    "line3": brew['location']['address3'],
                    "city": brew['location']['city'],
                    "stateOrProvince": brew['location']['state'],
                    "county": "",
                    "country": brew['location']['country'],
                    "postalCode": brew['location']['zip_code'],
                    "telephone": brew['phone']
                }
            }
            formattedData.append(brewery)
        print(json.dumps(formattedData))
        with open('data.json', 'w') as outfile:
            json.dump(formattedData, outfile, indent=4)
