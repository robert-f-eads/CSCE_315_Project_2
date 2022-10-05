import random
from typing import List
import datetime
import json

import numpy as np
import WebsiteScraper
import pprint
import Utils

class ProductEncoder(json.JSONEncoder):
    def default(self, obj):
        return json.loads(obj.toJson())

class Product(Utils.StringRepresentation):
    numberOfProducts = 0
    def __init__(self):
        self.id = Product.numberOfProducts
        Product.numberOfProducts += 1

        self.name = ''
        self.price = 0.0

    def toJson(self):
        return json.dumps(self, indent=4)


class HumanReadableProduct(Utils.StringRepresentation):
    def __init__(self):
        self.product = Product() # contain database product information
        # self.ingredients holds information which helps create the productsToIngredientsTable
        self.ingredients = [] # list of ingredients in human readable form i.e. [id0:name0, id1:name1, ...] or [name0, name1, ...]

    def toJson(self):
        return json.dumps(self, default=lambda o: o.__dict__, indent=4)

def loadProductsByScraping():
    humanReadableProducts: List[HumanReadableProduct] = WebsiteScraper.getHumanReadableProducts()
    for humanReadableProduct in humanReadableProducts:
        humanReadableProduct.product.price = random.choice(np.arange(5.99, 10.99, 1))
    return humanReadableProducts

def saveProductsToJson(products: List[HumanReadableProduct]):
    with open('products.json', 'w') as outputFile:
        json.dump(products, outputFile, cls=ProductEncoder, indent=4)

def loadProductsFromJson():
    humanReadableProducts: List[HumanReadableProduct] = []
    with open('products.json', 'r') as inputFile:
        loadedJson = json.load(inputFile)
        for elem in loadedJson:
            humanReadableProduct = HumanReadableProduct()
            for key, value in elem.items():
                if key == 'product':
                    product = Product()
                    for nested_key, nested_value in value.items():
                        product.__dict__[nested_key] = nested_value
                    humanReadableProduct.__dict__[key] = product
                else:
                    humanReadableProduct.__dict__[key] = value
            humanReadableProducts.append(humanReadableProduct)
    return humanReadableProducts

class Ingredient(Utils.StringRepresentation):
    numberOfIngredients = 0
    
    def __init__(self):
        self.id = Ingredient.numberOfIngredients
        Ingredient.numberOfIngredients += 1

        self.name = ''

        self.expirationDate = '' # date
        self.quantityRemaining = 0.0
        self.measurementUnits = ''
        self.pricePerUnitLastOrder = 0.0
        self.lastOrderDate = '' # date
        self.unitsInLastOrder = 0.0
        
        self.randomizeFields()

    # randomize fields except for id and name
    def randomizeFields(self):
        self.expirationDate = Utils.getRandomDate(datetime.date(2022, 10, 4), datetime.date(2022, 10, 30)) # date
        self.quantityRemaining = round(random.choice(np.arange(0, 30, .1)), 2)
        self.measurementUnits = 'units'
        self.pricePerUnitLastOrder = round(random.choice(np.arange(.5, 1.5, .01)), 2)
        self.lastOrderDate = Utils.getRandomDate(datetime.date(2022, 9, 1), self.expirationDate)
        self.unitsInLastOrder = round(random.choice(np.arange(0, 30, .1)), 2) # could have ordered less than quant remaining

def loadIngredientsByScraping():
    allIngredients: List[Ingredient] = []
    ingredientNames = WebsiteScraper.getAllIngredients()
    for ingredientName in ingredientNames:
        ingredient = Ingredient()
        ingredient.name = ingredientName
        allIngredients.append(ingredient)
    return allIngredients

class ProductToIngredient:
    def __init__(self, productId, ingredientId):
        self.productId = productId
        self.ingredientId = ingredientId

if __name__ == '__main__':
    ingredients = loadIngredientsByScraping()
    ingredientsNames = [ingredient.name for ingredient in ingredients]
    humanReadableProducts = loadProductsByScraping()
    products = [humanReadableProduct.product for humanReadableProduct in humanReadableProducts]
    Utils.writeObjectsToCsv(ingredients, 'Ingredients.csv')
    Utils.writeObjectsToCsv(products, 'Products.csv')

    # connect ingredients to products
    productsToIngredients = []
    for humanReadableProduct in humanReadableProducts:
        productId = humanReadableProduct.product.id
        for ingredient in humanReadableProduct.ingredients:
            ingredientId = ingredients[ingredientsNames.index(ingredient)].id
            productsToIngredients.append(ProductToIngredient(productId, ingredientId))
    Utils.writeObjectsToCsv(productsToIngredients, 'ProductsToIngredients.csv')

    saveProductsToJson(humanReadableProducts)