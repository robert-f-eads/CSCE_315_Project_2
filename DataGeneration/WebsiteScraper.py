import requests
from bs4 import BeautifulSoup
import json
import ProductsIngredients

# soup is a tree representation of the html page, easier to parse
def getSoupFromUrl(url):
    r = requests.get(url)
    soup = BeautifulSoup(r.content, 'html.parser')
    return soup

def getHumanReadableProducts():
    smoothieSoup = getSoupFromUrl('https://www.smoothieking.com/menu/smoothies')
    smoothieElements = smoothieSoup.find_all('smoothie-item')
    print(f'There are {len(smoothieElements)} smoothies found')

    products = []
    for smoothieElement in smoothieElements:
        print(smoothieElements.index(smoothieElement))
        smoothieUrl = smoothieElement.get('url')

        humanReadableProduct = ProductsIngredients.HumanReadableProduct()
        humanReadableProduct.product.name = smoothieUrl.split('/')[-1] # product name is last part of url

        ingredientsSoup = getSoupFromUrl(smoothieUrl)
        ingredientList = ingredientsSoup.find('ingredient-list')
        ingredientElements = json.loads(ingredientList.get(':ingredients'))
        for ingredientElement in ingredientElements:
            # str(ingredientElement['id']) + ':' + # save this in case we need ingredient id in the future
            humanReadableProduct.ingredients.append(ingredientElement['title'])
        products.append(humanReadableProduct)
    return products

def getAllIngredients():
    allIngredientsSoup = getSoupFromUrl('https://www.smoothieking.com/menu/smoothies/ingredients')
    allIngredientsElements = allIngredientsSoup.find_all('expandable-panel')
    allIngredientsNames = []
    for ingredientElement in allIngredientsElements:
        allIngredientsNames.append(ingredientElement.get('panel-header').replace('<sup>', '').replace('</sup>', ''))
    return allIngredientsNames

if __name__ == '__main__':
    print(len(getAllIngredients()))